/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.ColorUtils;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.KindDescriptor;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Registry;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind;

public class KindPropertiesDialog extends Dialog {

	private ListViewer viewer;
	private final List<Kind> kinds;
	private final Registry registry;
	private ColorSelector bgSelector;
	private ColorSelector fgSelector;
	private Button useCustom;
	private Kind current;
	private final List<Kind> editables;

	protected KindPropertiesDialog(Shell parentShell, List<Kind> kinds,
			Registry registry) {
		super(parentShell);
		Assert.isTrue(!kinds.isEmpty());
		this.kinds = kinds;
		this.registry = registry;
		editables = new ArrayList<Kind>(kinds.size());
		for (Kind kind : kinds) {
			editables.add(EcoreUtil.copy(kind));
		}
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Diagram Element Kinds");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent = (Composite) super.createDialogArea(parent);
		((GridLayout) parent.getLayout()).numColumns = 2;
		viewer = new ListViewer(parent);
		setupViewer();
		GridDataFactory.fillDefaults().hint(100, 300).grab(false, true)
				.applyTo(viewer.getControl());
		Composite settingPanel = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().applyTo(settingPanel);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(settingPanel);

		useCustom = new Button(settingPanel, SWT.CHECK);
		useCustom.setText("Use Custom");
		GridDataFactory.fillDefaults().span(2, SWT.DEFAULT).applyTo(useCustom);

		new Label(settingPanel, SWT.NONE).setText("Background Color:");
		bgSelector = new ColorSelector(settingPanel);

		new Label(settingPanel, SWT.NONE).setText("Foreground Color:");
		fgSelector = new ColorSelector(settingPanel);
		if (!editables.isEmpty()) {
			viewer.setSelection(new StructuredSelection(editables.iterator()
					.next()));
		}
		addListeners();
		return parent;
	}

	private void addListeners() {
		useCustom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (current != null) {
					current.setUseCustomStyle(useCustom.getSelection());
				}
			}
		});
		bgSelector.addListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {

				if (current != null) {
					current.getStyle().setBackgroundColor(
							ColorUtils.fromRGB(bgSelector.getColorValue()));
				}

			}
		});
		fgSelector.addListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {

				if (current != null) {
					current.getStyle().setForegroundColor(
							ColorUtils.fromRGB(fgSelector.getColorValue()));
				}
			}
		});
	}

	private void setupViewer() {
		viewer.setContentProvider(new IStructuredContentProvider() {

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}

			public void dispose() {

			}

			public Object[] getElements(Object inputElement) {
				return editables.toArray();
			}
		});
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				String id = ((Kind) element).getId();
				KindDescriptor kd = registry.getKindType(id);
				if (kd == null) {
					return id;
				}
				return kd.getExternalType().getName();
			}
		});
		viewer.setInput(editables);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {

				if (!(event.getSelection() instanceof IStructuredSelection)) {
					return;
				}

				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				Object first = selection.getFirstElement();

				if (first instanceof Kind) {
					current = (Kind) first;
					update(current);
				}
			}

			private void update(Kind kind) {
				useCustom.setSelection(kind.isUseCustomStyle());
				bgSelector.setColorValue(ColorUtils.toRGB(kind.getStyle()
						.getBackgroundColor()));
				fgSelector.setColorValue(ColorUtils.toRGB(kind.getStyle()
						.getForegroundColor()));
			}
		});
	}

	@Override
	protected void okPressed() {
		super.okPressed();
		for (int i = 0; i < editables.size(); ++i) {
			Kind newKind = editables.get(i);
			Kind oldKind = kinds.get(i);
			oldKind.setStyle(newKind.getStyle());
			oldKind.setUseCustomStyle(newKind.isUseCustomStyle());
		}
	}

	public Collection<? extends Kind> getResult() {
		return kinds;
	}

}
