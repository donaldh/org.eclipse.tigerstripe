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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModule;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModuleManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

/**
 * Presents the user with a filterable list of Tigerstripe projects for
 * selection
 * 
 * @author Eric Dillon
 * 
 */
public class TigerstripeProjectSelectionDialog extends SelectionStatusDialog {

	class TigerstripeProjectReferencesProvider extends
			StandardJavaElementContentProvider {
		@Override
		public Object[] getElements(Object parent) {
			Map<String, Object> elements = new HashMap<String, Object>();
			InstalledModule[] modules = InstalledModuleManager.getInstance()
					.getModules();
			Object[] javaResult = super.getElements(parent);
			Object[] all = new Object[javaResult.length + modules.length];
			System.arraycopy(javaResult, 0, all, 0, javaResult.length);
			System
					.arraycopy(modules, 0, all, javaResult.length,
							modules.length);
			for (Object o : all) {
				String id = null;
				if (o instanceof IJavaProject) {
					IJavaProject javaProject = (IJavaProject) o;
					ITigerstripeModelProject tsp = (ITigerstripeModelProject) javaProject
							.getAdapter(ITigerstripeModelProject.class);
					if (tsp != null) {
						try {
							id = tsp.getModelId();
						} catch (TigerstripeException e) {
							BasePlugin.log(e);
						}
					}
				} else if (o instanceof InstalledModule) {
					InstalledModule module = (InstalledModule) o;
					id = module.getModuleID();
				}
				if (id != null && !elements.containsKey(id)) {
					elements.put(id, o);
				}
			}

			return elements.values().toArray(new Object[0]);
		}
	}

	class TigerstripeProjectReferencesLabelProvider extends
			JavaElementLabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof InstalledModule) {
				InstalledModule module = (InstalledModule) element;
				StringBuffer buffer = new StringBuffer();
				buffer.append(module.getModuleID());
				String version = module.getModule().getProjectDetails()
						.getVersion();
				if (version != null && version.length() > 0) {
					buffer.append(" (");
					buffer.append(version);
					buffer.append(")");
				}
				return buffer.toString();
			}
			return super.getText(element);
		}

		@SuppressWarnings("restriction")
		@Override
		public Image getImage(Object element) {
			if (element instanceof InstalledModule) {
				return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_JAR);
			}
			return super.getImage(element);
		}
	}

	// the visual selection widget group
	private TableViewer fTableViewer;

	private Collection<ModelReference> fFilteredOutProjects;

	// sizing constants
	private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;

	private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;

	private ViewerFilter fFilter;

	public TigerstripeProjectSelectionDialog(Shell parentShell,
			Collection<ModelReference> filteredOutProjects) {
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
				if (element instanceof IJavaProject) {
					IJavaProject prj = (IJavaProject) element;

					ITigerstripeModelProject tsProject = (ITigerstripeModelProject) prj
							.getProject().getAdapter(
									ITigerstripeModelProject.class);

					if (tsProject != null && tsProject.exists()) {
						for (ModelReference ref : fFilteredOutProjects) {
							if (ref.isReferenceTo(tsProject))
								return false;
						}
						return true;
					}
				} else if (element instanceof InstalledModule) {
					InstalledModule module = (InstalledModule) element;
					for (ModelReference ref : fFilteredOutProjects) {
						if (ref.getToModelId().equals(module.getModuleID()))
							return false;
					}
					return true;
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

		fTableViewer
				.setLabelProvider(new TigerstripeProjectReferencesLabelProvider());
		fTableViewer
				.setContentProvider(new TigerstripeProjectReferencesProvider());
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
