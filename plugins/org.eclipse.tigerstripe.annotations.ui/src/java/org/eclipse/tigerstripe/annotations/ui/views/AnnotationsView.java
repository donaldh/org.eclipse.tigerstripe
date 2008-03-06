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
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.tigerstripe.annotations.IAnnotable;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.annotations.ui.Activator;
import org.eclipse.tigerstripe.annotations.ui.internal.AnnotationFormManager;
import org.eclipse.tigerstripe.annotations.ui.internal.AnnotationSchemeComparator;
import org.eclipse.tigerstripe.annotations.ui.internal.DefaultAnnotable;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class AnnotationsView extends ViewPart {

	private Button defaults;

	private Button apply;

	private Button cancel;

	private Composite formComposite;

	private ComboViewer schemeComboViewer;

	// The current annotatble.
	private IAnnotable currentAnnotable;

	private String currentURI;

	private boolean isDirty = false;

	// private IResource resource;

	private ISelectionListener pageSelectionListener;

	private Map<String, String> schemeForUriMap = new HashMap<String, String>();

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

	public void markModified() {
		setDirty(true);
		updateButtonState();
	}

	protected void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	protected boolean isDirty() {
		return this.isDirty;
	}

	protected String getCurrentURI() throws AnnotationCoreException {
		if (currentAnnotable == null)
			throw new AnnotationCoreException("No current annotable");
		return this.currentURI;
	}

	protected void setCurrentAnnotable(IAnnotable annotable)
			throws AnnotationCoreException {
		this.currentAnnotable = annotable;
		currentURI = null;
		if (annotable != null)
			currentURI = annotable.getURI();
	}

	protected IAnnotable getCurrentAnnotable() {
		return this.currentAnnotable;
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		super.dispose();
		if (pageSelectionListener != null) {
			getSite().getPage().removeSelectionListener(pageSelectionListener);
		}
	}

	@Override
	public void createPartControl(Composite parent) {

		FormData formData;

		parent.setLayout(new FormLayout());

		Label label = new Label(parent, SWT.LEFT);
		label.setText("Annotation Scheme: ");
		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		label.setLayoutData(formData);

		schemeComboViewer = new ComboViewer(parent, SWT.DROP_DOWN
				| SWT.READ_ONLY);
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

		cancel = new Button(parent, SWT.PUSH);
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
				processButtonEvent(e);
			}
		});

		apply = new Button(parent, SWT.PUSH);
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
				processButtonEvent(e);
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
				processButtonEvent(e);
			}

		});

		formComposite = new Composite(parent, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(schemeCombo, 5);
		formData.bottom = new FormAttachment(cancel, -5);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formComposite.setLayoutData(formData);
		formComposite.setLayout(new StackLayout());

		hookPageSelection();
		updateButtonState();
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

		try {
			// First try to adapt as a IAnnotable
			setCurrentAnnotable((IAnnotable) adaptable
					.getAdapter(IAnnotable.class));
			if (getCurrentAnnotable() == null) {
				// Try then to default to IResource and build a fake annotable
				IResource res = (IResource) adaptable
						.getAdapter(IResource.class);
				if (res != null) {
					setCurrentAnnotable(new DefaultAnnotable(res));
				}
			}

			if (getCurrentAnnotable() == null) {
				schemeComboViewer.setInput(new String[] {});
				schemeComboViewer.getCombo().setEnabled(false);
				((StackLayout) formComposite.getLayout()).topControl = null;
				formComposite.getParent().layout(true, true);
			} else {

				try {
					int index = 0;
					IAnnotationScheme[] schemes = AnnotationSchemeRegistry.eINSTANCE
							.getDefinedSchemes(getCurrentURI());
					Arrays.sort(schemes, new AnnotationSchemeComparator());
					schemeComboViewer.getCombo().setEnabled(true);
					schemeComboViewer.setInput(schemes);

					// get selected scheme for a particular URI
					String schemeUserId = schemeForUriMap
							.get(getSchemeTypeURI());
					if (schemeUserId != null) {
						index = getSchemeIndexFromCombo(schemeUserId);
					}

					schemeComboViewer.getCombo().select(index);
					schemeComboViewer.setSelection(schemeComboViewer
							.getSelection());
				} catch (AnnotationCoreException e) {
					Activator.log(e);
				}
			}
		} catch (AnnotationCoreException e) {
			Activator.log(e);
		}
		setDirty(false);
		updateButtonState();
	}

	private int getSchemeIndexFromCombo(String schemeUserId) {

		for (int i = 0; i < schemeComboViewer.getCombo().getItemCount(); i++) {
			if (schemeUserId.equals(schemeComboViewer.getCombo().getItem(i))) {
				return i;
			}
		}
		return 0;
	}

	private void schemeComboSelectionChanged(SelectionChangedEvent event) {

		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		if (!(selection.getFirstElement() instanceof IAnnotationScheme)) {
			return;
		}

		IAnnotationScheme scheme = (IAnnotationScheme) selection
				.getFirstElement();

		Composite composite = null;
		IAnnotationForm form = null;

		try {
			// maintain selected scheme for a particular URI if previously
			// visited
			schemeForUriMap.put(getSchemeTypeURI(), scheme
					.getNamespaceUserLabel());

			form = scheme.selectForm(getCurrentURI());
			if (form == null) {
				((StackLayout) formComposite.getLayout()).topControl = null;
				formComposite.getParent().layout(true, true);
				return;
			}
			composite = (Composite) formCompositeMap.get(form.getID());
			if (composite == null) {
				composite = AnnotationFormManager.createFormComposite(
						formComposite, form, this);
				formCompositeMap.put(form.getID(), composite);
			}

			AnnotationFormManager.clearFormCompositeData(composite);
			AnnotationStore store = currentAnnotable.getStore(scheme);
			AnnotationFormManager.setFormCompositeData(composite, store,
					getCurrentURI(), false);
		} catch (AnnotationCoreException e) {
			Activator.log(e);
		}

		formComposite.setData(form);
		((StackLayout) formComposite.getLayout()).topControl = composite;

		formComposite.getParent().layout(true, true);
	}

	private void processButtonEvent(SelectionEvent event) {

		Composite composite = null;
		IAnnotationForm form = null;
		AnnotationStore store = null;

		try {
			form = (IAnnotationForm) formComposite.getData();
			store = currentAnnotable.getStore(form.getScheme());
			composite = (Composite) ((StackLayout) formComposite.getLayout()).topControl;

			if (event.getSource() == apply) {
				AnnotationFormManager.writeFormCompositeData(composite, store,
						getCurrentURI());
				setDirty(false);
			} else if (event.getSource() == defaults) {
				AnnotationFormManager.setFormCompositeData(composite, store,
						getCurrentURI(), true);
				setDirty(true);
			} else if (event.getSource() == cancel) {
				AnnotationFormManager.setFormCompositeData(composite, store,
						getCurrentURI(), false);
				setDirty(false);
			}
			updateButtonState();
		} catch (AnnotationCoreException e) {
			Activator.log(e);
		}
	}

	// Temp until we sort out IAnnotable (hacked to work with .java files for
	// now...)
	private String getSchemeTypeURI() throws AnnotationCoreException {

		String uri = getCurrentURI();
		String type = uri.substring(0, uri.indexOf(':'));

		// now see if .java file (i.e. ManagedEntity)
		if (uri.indexOf('.') > 0) {
			type += uri.substring(uri.indexOf('.'));
		}

		return type;

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

	protected void updateButtonState() {
		if (apply != null)
			apply.setEnabled(isDirty());

		if (cancel != null)
			cancel.setEnabled(isDirty());
	}

}
