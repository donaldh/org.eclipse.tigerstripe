/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    J. Strawn (Cisco Systems, Inc.) - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.ui.views;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationSchemeRegistry;
import org.eclipse.tigerstripe.annotations.AnnotationStore;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.ui.internal.AnnotationSpecificationFormManager;
import org.eclipse.tigerstripe.annotations.ui.internal.widgets.AnnotationFormComposite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class AnnotationsView extends ViewPart {

	private String uri;

	private IProject project;

	private Map specComposites;

	private AnnotationStore store;

	private Composite parent;

	private ComboViewer schemeComboViewer;

	private ISelectionListener pageSelectionListener;

	private final class SchemeLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			return null;
		}

		public String getText(Object element) {
			return ((IAnnotationScheme) element).getNamespaceUserLabel();
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

		public void addListener(ILabelProviderListener listener) {
		}
	}

	public AnnotationsView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		GridData gridData;

		this.parent = parent;

		parent.setLayout(new GridLayout());

		Label label = new Label(parent, SWT.LEFT);
		label.setText("Annotation Scheme: ");

		schemeComboViewer = new ComboViewer(parent, SWT.DROP_DOWN);
		schemeComboViewer.setLabelProvider(new SchemeLabelProvider());
		schemeComboViewer.setContentProvider(new ArrayContentProvider());
		schemeComboViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						schemeComboSelectionChanged(event);
					}
				});

		Combo schemeCombo = schemeComboViewer.getCombo();

		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		schemeCombo.setLayoutData(gridData);
		hookPageSelection();

	}

	@Override
	public void setFocus() {
		schemeComboViewer.getCombo().setFocus();
	}

	@Override
	public void dispose() {
		super.dispose();
		if (pageSelectionListener != null) {
			getSite().getPage().removeSelectionListener(pageSelectionListener);
		}
	}

	private void hookPageSelection() {
		pageSelectionListener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart part,
					ISelection selection) {
				pageSelectionChanged(part, selection);
			}
		};
		getSite().getPage().addSelectionListener(pageSelectionListener);
	}

	private void pageSelectionChanged(IWorkbenchPart part, ISelection selection) {

		if (part == this) {
			return;
		}
		if (!(selection instanceof IStructuredSelection)) {
			return;
		}

		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Object element = structuredSelection.getFirstElement();
		if (!(element instanceof IAdaptable)) {
			return;
		}

		IAdaptable adaptable = (IAdaptable) element; // to IAnnotable
		IResource resource = (IResource) adaptable.getAdapter(IResource.class);
		if (resource != null) {
			setComboViewerInput(resource);
		}

	}

	private void setComboViewerInput(IResource resource) {
		System.out.println(resource.getLocationURI().toString());
		try {
			uri = resource.getLocationURI().toString();
			project = resource.getProject();
			clearAnnotationFormData();
			IAnnotationScheme[] schemes = AnnotationSchemeRegistry.eINSTANCE
					.getDefinedSchemes(uri);
			schemeComboViewer.setInput(schemes);
		} catch (AnnotationCoreException e) {
			// TODO - Pop up exception dialog
			e.printStackTrace();
		}
	}

	private void schemeComboSelectionChanged(SelectionChangedEvent event) {

		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		if (!(selection.getFirstElement() instanceof IAnnotationScheme)) {
			return;
		}

		clearAnnotationFormData();

		Composite composite = new AnnotationFormComposite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));

		IAnnotationScheme scheme = (IAnnotationScheme) selection
				.getFirstElement();

		final IAnnotationForm form = scheme.selectForm(uri);
		try {
			store = AnnotationStore.getDefaultFactory().getAnnotationStore(
					project, scheme);

			// discuss this... uri
			specComposites = AnnotationSpecificationFormManager
					.addAnnotationFormComposites(composite, store, uri, form);
		} catch (AnnotationCoreException e1) {
			e1.printStackTrace();
		}

		// TODO - exclude button if no annotation forms
		final Button applyBtn = new Button(composite, SWT.PUSH);
		applyBtn.setText("Apply");
		applyBtn.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				persistAnnotations(form);
			}
		});

		parent.layout();

	}

	private void persistAnnotations(IAnnotationForm form) {

		try {
			IAnnotationSpecification[] specs = form.getSpecifications();
			for (IAnnotationSpecification spec : specs) {

				// TODO - FIX THIS!!!
				Widget widget = (Widget) specComposites.get(spec.getID());
				if (widget instanceof Text) {
					Text text = (Text) widget;
					store.setAnnotation(spec, uri, text.getText());
				} else if (widget instanceof Button) {
					Button button = (Button) widget;
					store.setAnnotation(spec, uri, button.getSelection());
				}
			}

			store.store();

		} catch (AnnotationCoreException e) {
			e.printStackTrace();
		}

	}

	private void clearAnnotationFormData() {

		store = null;
		specComposites = null;

		Control[] children = parent.getChildren();
		for (Control control : children) {
			if (control instanceof AnnotationFormComposite) {
				control.dispose();
			}
		}
	}

}
