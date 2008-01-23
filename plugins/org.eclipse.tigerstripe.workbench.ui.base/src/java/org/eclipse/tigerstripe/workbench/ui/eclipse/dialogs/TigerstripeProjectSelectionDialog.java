/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs;

import java.net.URI;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.JavaElementSorter;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.api.project.IAbstractTigerstripeProject;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

/**
 * Presents the user with a filterable list of Tigerstripe projects for
 * selection
 * 
 * @author Eric Dillon
 * 
 */
public class TigerstripeProjectSelectionDialog extends SelectionStatusDialog {

	// the visual selection widget group
	private TableViewer fTableViewer;

	private Set<String> fFilteredOutProjects;

	// sizing constants
	private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;

	private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;

	private ViewerFilter fFilter;

	public TigerstripeProjectSelectionDialog(Shell parentShell,
			Set filteredOutProjects) {
		super(parentShell);
		setTitle("Select Tigerstripe Project");
		setMessage("Select Tigerstripe Project to reference");
		fFilteredOutProjects = filteredOutProjects;

		int shellStyle = getShellStyle();
		setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);

		fFilter = new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if (element instanceof JavaProject) {
					JavaProject prj = (JavaProject) element;
					URI projectURI = prj.getProject().getLocation().toFile()
							.toURI();
					IAbstractTigerstripeProject tsProject = null;
					try {
						tsProject = API.getDefaultProjectSession()
								.makeTigerstripeProject(projectURI, null);
					} catch (TigerstripeLicenseException e) {

					} catch (TigerstripeException e) {

					}
					if (tsProject != null && tsProject.exists())
						return !fFilteredOutProjects.contains(prj
								.getElementName());
				}
				return false;
			}
		};

	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// page group
		Composite composite = (Composite) super.createDialogArea(parent);

		Font font = parent.getFont();
		composite.setFont(font);

		createMessageArea(composite);

		fTableViewer = new TableViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		fTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						doSelectionChanged(((IStructuredSelection) event
								.getSelection()).toArray());
					}
				});
		fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				okPressed();
			}
		});
		fTableViewer.addFilter(fFilter);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
		data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
		fTableViewer.getTable().setLayoutData(data);

		fTableViewer.setLabelProvider(new JavaElementLabelProvider());
		fTableViewer
				.setContentProvider(new StandardJavaElementContentProvider());
		fTableViewer.setSorter(new JavaElementSorter());
		fTableViewer.getControl().setFont(font);

		// Button checkbox= new Button(composite, SWT.CHECK);
		// checkbox.setText(PreferencesMessages.ProjectSelectionDialog_filter);
		// checkbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true,
		// false));
		// checkbox.addSelectionListener(new SelectionListener() {
		// public void widgetSelected(SelectionEvent e) {
		// updateFilter(((Button) e.widget).getSelection());
		// }
		// public void widgetDefaultSelected(SelectionEvent e) {
		// updateFilter(((Button) e.widget).getSelection());
		// }
		// });
		// IDialogSettings dialogSettings=
		// JavaPlugin.getDefault().getDialogSettings();
		// boolean doFilter=
		// !dialogSettings.getBoolean(DIALOG_SETTINGS_SHOW_ALL) &&
		// !fProjectsWithSpecifics.isEmpty();
		// checkbox.setSelection(doFilter);
		// updateFilter(doFilter);

		IJavaModel input = JavaCore.create(ResourcesPlugin.getWorkspace()
				.getRoot());
		fTableViewer.setInput(input);

		doSelectionChanged(new Object[0]);
		Dialog.applyDialogFont(composite);
		return composite;
	}

	// protected void updateFilter(boolean selected) {
	// if (selected) {
	// fTableViewer.addFilter(fFilter);
	// } else {
	// fTableViewer.removeFilter(fFilter);
	// }
	// JavaPlugin.getDefault().getDialogSettings().put(DIALOG_SETTINGS_SHOW_ALL,
	// !selected);
	// }
	//
	private void doSelectionChanged(Object[] objects) {
		if (objects.length != 1) {
			updateStatus(new StatusInfo(IStatus.ERROR, "")); //$NON-NLS-1$
			setSelectionResult(null);
		} else {
			updateStatus(new StatusInfo());
			setSelectionResult(objects);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#computeResult()
	 */
	@Override
	protected void computeResult() {
	}
}
