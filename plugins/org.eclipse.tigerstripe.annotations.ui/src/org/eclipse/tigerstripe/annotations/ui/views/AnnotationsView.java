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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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
import org.eclipse.tigerstripe.annotations.ui.internal.AnnotationFormManager;
import org.eclipse.tigerstripe.annotations.ui.internal.widgets.AnnotationFormComposite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class AnnotationsView extends ViewPart {

	private String uri;

	private IProject project;

	private Map compositeMap;

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

		FormData formData;
		this.parent = parent;

		parent.setLayout(new FormLayout());

		Label label = new Label(parent, SWT.LEFT);
		label.setText("Annotation Scheme: ");
		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		label.setLayoutData(formData);
		
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
		formData = new FormData();
		formData.top = new FormAttachment(label, 5);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		schemeCombo.setLayoutData(formData);
		
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

		try {
			uri = getURIFromResource(resource);
			project = resource.getProject();
			clearAnnotationFormData();
			IAnnotationScheme[] schemes = AnnotationSchemeRegistry.eINSTANCE
					.getDefinedSchemes(uri);
			schemeComboViewer.setInput(schemes);
			schemeComboViewer.getCombo().select(0);
			schemeComboViewer.setSelection(schemeComboViewer.getSelection());
		} catch (AnnotationCoreException e) {
			// TODO - Pop up exception dialog
			e.printStackTrace();
		}
	}

	// probably temporary
	private String getURIFromResource(IResource resource) {
		
		IAdaptable adaptable = (IAdaptable) resource;
		if(adaptable.getAdapter(IProject.class) != null) {
			String uri = resource.getLocationURI().toString();
			uri = uri.replaceAll("file:/", "project:/");
			return uri;
		} else {
			return resource.getLocationURI().toString();
		}
	}

	private void schemeComboSelectionChanged(SelectionChangedEvent event) {

		FormData formData;
		Button defaults = null;
		Button apply = null;
		Button cancel = null;

		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		if (!(selection.getFirstElement() instanceof IAnnotationScheme)) {
			return;
		}

		clearAnnotationFormData();

		IAnnotationScheme scheme = (IAnnotationScheme) selection
				.getFirstElement();
		
		final IAnnotationForm form = scheme.selectForm(uri);

		Composite container = new AnnotationFormComposite(parent, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(schemeComboViewer.getCombo(), 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		container.setLayoutData(formData);
		container.setLayout(new FormLayout());

		Composite composite = new Composite(container, SWT.NONE);
		try {
			store = AnnotationStore.getDefaultFactory().getAnnotationStore(
					project, scheme);
			compositeMap = AnnotationFormManager.addFormComposites(composite,
					store, uri, form);
		} catch (AnnotationCoreException e1) {
			e1.printStackTrace();
		}

		// add buttons
		cancel = new Button(container, SWT.PUSH);
		cancel.setText("Cancel");
		formData = new FormData();
		formData.right = new FormAttachment(100, -5);
		formData.bottom = new FormAttachment(100, -5);
		cancel.setLayoutData(formData);
		cancel.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				cancelUserInput(form);
			}
		});

		apply = new Button(container, SWT.PUSH);
		apply.setText("&Apply");
		formData = new FormData();
		formData.right = new FormAttachment(cancel, -5);
		formData.bottom = new FormAttachment(100, -5);
		apply.setLayoutData(formData);
		apply.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				persistAnnotations(form);
			}
		});

		defaults = new Button(container, SWT.PUSH);
		defaults.setText("Restore &Defaults");
		formData = new FormData();
		formData.right = new FormAttachment(apply, -5);
		formData.bottom = new FormAttachment(100, -5);
		defaults.setLayoutData(formData);
		defaults.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				restoreDefaults(form);
			}

		});

		// Add annotation form
		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, 5);
		composite.setLayoutData(formData);
		composite.setLayout(new FillLayout(SWT.VERTICAL));

		parent.layout();

	}

	// TODO - REFACTOR
	private void persistAnnotations(IAnnotationForm form) {

		try {
			IAnnotationSpecification[] specs = form.getSpecifications();
			for (IAnnotationSpecification spec : specs) {

				Widget widget = (Widget) compositeMap.get(spec.getID());
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

	// TODO - REFACTOR
	private void restoreDefaults(IAnnotationForm form) {

		IAnnotationSpecification[] specs = form.getSpecifications();
		for (IAnnotationSpecification spec : specs) {

			Widget widget = (Widget) compositeMap.get(spec.getID());
			if (widget instanceof Text) {
				Text text = (Text) widget;
				if (spec.getDefaultValue() == null) {
					text.setText("");
				} else {
					text.setText(spec.getDefaultValue());
				}
			} else if (widget instanceof Button) {
				Button button = (Button) widget;
				if (spec.getDefaultValue() == null) {
					button.setSelection(false);
				} else {
					button.setSelection(Boolean.getBoolean(spec
							.getDefaultValue()));
				}
			}
		}
	}

	// TODO - REFACTOR
	protected void cancelUserInput(IAnnotationForm form) {

		try {
			IAnnotationSpecification[] specifications = form
					.getSpecifications();
			for (IAnnotationSpecification spec : specifications) {

				Widget widget = (Widget) compositeMap.get(spec.getID());
				if (widget instanceof Text) {
					Text text = (Text) widget;
					if (store.getAnnotation(spec, uri) == null) {
						text.setText("");
					} else {
						text.setText((String) store.getAnnotation(spec, uri));
					}
				} else if (widget instanceof Button) {
					Button button = (Button) widget;
					if (store.getAnnotation(spec, uri) == null) {
						button.setSelection(false);
					} else {
						button.setSelection((Boolean) store.getAnnotation(spec,
								uri));
					}
				}
			}
		} catch (AnnotationCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void clearAnnotationFormData() {

		store = null;
		compositeMap = null;

		Control[] children = parent.getChildren();
		for (Control control : children) {
			if (control instanceof AnnotationFormComposite) {
				control.dispose();
			}
		}
	}

}
