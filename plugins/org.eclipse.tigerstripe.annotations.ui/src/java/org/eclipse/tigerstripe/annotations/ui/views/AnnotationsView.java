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

import java.util.Arrays;
import java.util.HashMap;
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
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationSchemeRegistry;
import org.eclipse.tigerstripe.annotations.AnnotationStore;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.annotations.IAnnotationStoreFactory;
import org.eclipse.tigerstripe.annotations.ui.internal.AnnotationFormManager;
import org.eclipse.tigerstripe.annotations.ui.internal.AnnotationSchemeComparator;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class AnnotationsView extends ViewPart {

	private Composite parent;

	private Button defaults = null;

	private Button apply = null;

	private Button cancel = null;

	private ComboViewer schemeComboViewer;

	private IResource resource;

	private Composite formComposite;

	private ISelectionListener pageSelectionListener;

	// Move map for form manager??
	private Map<String, Composite> formCompositeMap = new HashMap<String, Composite>();

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
	public void setFocus() {
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
		schemeComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
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

		cancel = new Button(parent, SWT.PUSH);
		cancel.setText("Cancel");
		// cancel.setEnabled(false);
		formData = new FormData();
		formData.right = new FormAttachment(100, -5);
		formData.bottom = new FormAttachment(100, -5);
		cancel.setLayoutData(formData);
		cancel.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				cancelUserInput();
			}
		});

		apply = new Button(parent, SWT.PUSH);
		apply.setText("&Apply");
		// apply.setEnabled(false);
		formData = new FormData();
		formData.right = new FormAttachment(cancel, -5);
		formData.bottom = new FormAttachment(100, -5);
		apply.setLayoutData(formData);
		apply.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				writeFormData();
			}
		});

		defaults = new Button(parent, SWT.PUSH);
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
				restoreFormData();
			}

		});

		formComposite = new Composite(parent, SWT.BORDER);
		formData = new FormData();
		formData.top = new FormAttachment(schemeCombo, 5);
		formData.bottom = new FormAttachment(cancel, -5);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formComposite.setLayoutData(formData);
		formComposite.setLayout(new StackLayout());

		hookPageSelection();

	}

	@Override
	public void dispose() {
		super.dispose();
		if (pageSelectionListener != null) {
			getSite().getPage().removeSelectionListener(pageSelectionListener);
		}
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

		IAdaptable adaptable = (IAdaptable) element;
		resource = (IResource) adaptable.getAdapter(IResource.class);
		if (resource != null) {
			try {
				IAnnotationScheme[] schemes = AnnotationSchemeRegistry.eINSTANCE
						.getDefinedSchemes(getURIFromResource());
				Arrays.sort(schemes, new AnnotationSchemeComparator());
				schemeComboViewer.setInput(schemes);
				schemeComboViewer.getCombo().select(0);
				schemeComboViewer.setSelection(schemeComboViewer.getSelection());
			} catch (AnnotationCoreException e) {

				// TODO - Pop up exception dialog
				e.printStackTrace();
			}
		}
	}

	private void schemeComboSelectionChanged(SelectionChangedEvent event) {

		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		if (!(selection.getFirstElement() instanceof IAnnotationScheme)) {
			return;
		}

		IAnnotationScheme scheme = (IAnnotationScheme) selection.getFirstElement();
		IAnnotationForm form = scheme.selectForm(getURIFromResource());

		Composite composite = (Composite) formCompositeMap.get(form.getID());
		if (composite == null) {
			composite = AnnotationFormManager.createFormComposite(formComposite, form);
			formCompositeMap.put(form.getID(), composite);
		}

		try {
			AnnotationFormManager.clearFormCompositeData(composite);
			AnnotationStore store = AnnotationStore.getDefaultFactory().getAnnotationStore(resource.getProject(), scheme);
			AnnotationFormManager.setFormCompositeData(composite, store, getURIFromResource(), false);
		} catch (AnnotationCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		formComposite.setData(form);
		((StackLayout) formComposite.getLayout()).topControl = composite;

		parent.layout(true, true);
	}

	private void writeFormData() {

		try {
			IAnnotationForm form = (IAnnotationForm) formComposite.getData();
			AnnotationStore store = AnnotationStore.getDefaultFactory().getAnnotationStore(resource.getProject(),
					form.getScheme());
			Composite composite = (Composite) ((StackLayout) formComposite.getLayout()).topControl;
			AnnotationFormManager.writeFormCompositeData(composite, store, getURIFromResource());
		} catch (AnnotationCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void restoreFormData() {
		
		try {
			IAnnotationForm form = (IAnnotationForm) formComposite.getData();
			AnnotationStore store = AnnotationStore.getDefaultFactory().getAnnotationStore(resource.getProject(),
					form.getScheme());
			Composite composite = (Composite) ((StackLayout) formComposite.getLayout()).topControl;
			AnnotationFormManager.setFormCompositeData(composite, store, getURIFromResource(), true);
			AnnotationFormManager.writeFormCompositeData(composite, store, getURIFromResource());
		} catch (AnnotationCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cancelUserInput() {
		try {
			IAnnotationForm form = (IAnnotationForm) formComposite.getData();
			AnnotationStore store = AnnotationStore.getDefaultFactory().getAnnotationStore(resource.getProject(),
					form.getScheme());
			Composite composite = (Composite) ((StackLayout) formComposite.getLayout()).topControl;
			AnnotationFormManager.setFormCompositeData(composite, store, getURIFromResource(), false);
		} catch (AnnotationCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getURIFromResource() {

		IAdaptable adaptable = (IAdaptable) resource;
		if (adaptable.getAdapter(IProject.class) != null) {
			String uri = resource.getLocationURI().toString();
			uri = uri.replaceAll("file:/", "project:/");
			return uri;
		} else {
			return resource.getLocationURI().toString();
		}
	}

	private void hookPageSelection() {
		pageSelectionListener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				pageSelectionChanged(part, selection);
			}
		};
		getSite().getPage().addSelectionListener(pageSelectionListener);
	}

}
