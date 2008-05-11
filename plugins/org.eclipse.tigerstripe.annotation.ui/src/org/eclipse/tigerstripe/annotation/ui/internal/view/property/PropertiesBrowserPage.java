/******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    xored software, Inc. - initial API and Implementation (Yuri Strot) 
 ****************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.core.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.ui.internal.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.ui.internal.util.AsyncExecUtil;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * A property sheet page for annotations.
 * 
 * @author Yuri Strot
 */
public class PropertiesBrowserPage
	extends TabbedPropertySheetPage
	implements IPropertyChangeListener, IAnnotationListener, IRefactoringListener {

	/**
	 * the contributor for this property sheet page
	 */
	private ITabbedPropertySheetPageContributor contributor;

	private IStructuredSelection selectedElements;
	
	private IWorkbenchPart part;

	private Composite composite;
	private Composite leftPart;
	
	private org.eclipse.swt.widgets.List list;
	
	private Annotation[] EMPTY_ARRAY = new Annotation[0];
	
	private Annotation[] currentSelection;

	private StructuredSelection EMPTY_SELECTION = new StructuredSelection();

	/**
	 * Constructor
	 * @param contributor the <code>ITabbedPropertySheetPageContributor</code> 
	 *  for this property sheet page
	 */
	public PropertiesBrowserPage(ITabbedPropertySheetPageContributor contributor) {
		super(contributor);

		this.contributor = contributor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose() {
		super.dispose();
        /**
         * Clean up
         */
        contributor = null;
        selectedElements = null;
        removeListeners();
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#setActionBars(org.eclipse.ui.IActionBars)
	 */
	public void setActionBars(IActionBars actionBars) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		/* not implemented */
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() {
		getControl().setFocus();
	}
	
	protected void addListeners() {
		AnnotationUIPlugin.getManager().addRefactoringListener(this);
		AnnotationPlugin.getManager().addAnnotationListener(this);
	}
	
	protected void removeListeners() {
		AnnotationPlugin.getManager().removeAnnotationListener(this);
		AnnotationUIPlugin.getManager().removeRefactoringListener(this);
	}

	public void annotationAdded(Annotation annotation) {
		updateSelection();
    }
	
	public void annotationsRemoved(Annotation[] annotations) {
		updateSelection();
	}

	public void annotationsChanged(Annotation[] annotations) {
		updateSelection();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationListener#annotationLoaded(org.eclipse.tigerstripe.annotation.core.Annotation)
	 */
	public void annotationLoaded(Annotation annotation) {
	}

	public void containerUpdated() {
		AsyncExecUtil.run(composite, new Runnable() {
		
			public void run() {
				updateSelection();
			}
		
		});
    }

	public void refactoringPerformed(Map<URI, URI> changes) {
    }


	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		
		TabbedPropertySheetWidgetFactory factory = new TabbedPropertySheetWidgetFactory();
		
		composite = factory.createComposite(parent);
		composite.setLayout(getEmptyLayout(2));
		
		leftPart = factory.createComposite(composite);
		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.widthHint = 200;
		leftPart.setLayoutData(data);
		createNavigateMenu(leftPart, factory);
		leftPart.setVisible(false);
		
		Composite rightPart = factory.createComposite(composite);
		FormLayout layout = new FormLayout();
		rightPart.setLayout(layout);
		rightPart.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		super.createControl(rightPart);
		addListeners();
	}
	
	private GridLayout getEmptyLayout(int columns) {
		GridLayout layout = new GridLayout();
		layout.numColumns = columns;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		return layout;
	}
	
	protected Composite createTitle(Composite parent, TabbedPropertySheetWidgetFactory factory) {
		parent.setLayout(new FormLayout());
		
		TabbedPropertyTitle title = new TabbedPropertyTitle(parent, factory);
		title.setTitle("Annotation URI:", null);

        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(0, 0);
        title.setLayoutData(formData);
        
        Composite composite = factory.createComposite(parent, SWT.NONE);
        
        formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(title, 0);
		formData.bottom = new FormAttachment(100, 0);
		composite.setLayoutData(formData);
		
		return composite;
	}
	
	protected void createNavigateMenu(Composite parent, TabbedPropertySheetWidgetFactory factory) {
		
		Composite composite = createTitle(parent, factory);
		FormLayout formLayout = new FormLayout();
		formLayout.marginTop = 4;
		formLayout.marginRight = 4;
		composite.setLayout(formLayout);
		
		list = new org.eclipse.swt.widgets.List(composite, SWT.BORDER);
		list.addSelectionListener(new SelectionListener() {
		
			public void widgetSelected(SelectionEvent e) {
				onSelection();
			}
		
			public void widgetDefaultSelected(SelectionEvent e) {
				onSelection();
			}
			
			public void onSelection() {
				int index = list.getSelectionIndex();
				if (index >= 0 && index < currentSelection.length) {
					setPageSelection(currentSelection[index]);
				}
			}
		
		});
		
		factory.paintBordersFor(composite);
        
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		
		list.setLayoutData(formData);
	}
	
	protected void setPageSelection(Annotation annotation) {
		super.selectionChanged(part, new StructuredSelection(annotation));
	}
	
	protected void updatePage() {
		currentSelection = getAnnotation(selectedElements);
		int newSelection = list.getSelectionIndex();
		list.removeAll();
		boolean showPage = currentSelection.length > 0;
		if (showPage) {
			for (int i = 0; i < currentSelection.length; i++) {
				list.add(getDisplayName(currentSelection[i]));
            }
			int index = getAnnotationIndex();
			if (index >= 0) {
				newSelection = index;
			}
			else if (newSelection < 0 || newSelection >= list.getItemCount()) {
				newSelection = 0;
			}
			list.select(newSelection);
			setPageSelection(currentSelection[newSelection]);
		}
		else {
			super.selectionChanged(null, EMPTY_SELECTION);
		}
		leftPart.setVisible(showPage);
		composite.layout();
	}
	
	protected String getDisplayName(Annotation annotation) {
		EObject content = annotation.getContent();
		if (content == null) {
			return "<no content>";
		}
		else {
			return content.eClass().getName() + "@" + Integer.toHexString(content.hashCode());
		}
	}
	
	private int getAnnotationIndex() {
		Annotation annotation = AnnotationUtils.getAnnotation(selectedElements);
		for (int i = 0; i < currentSelection.length; i++) {
	        if (currentSelection[i].equals(annotation))
	        	return i;
        }
		return -1;
	}
	
	private Annotation[] getAnnotation(ISelection selection) {
		Object object = AnnotationUtils.getAnnotableElement(selection);
		if (object == null)
			return EMPTY_ARRAY;
		Annotation[] annotations = AnnotationPlugin.getManager().getAnnotations(object);
		if (annotations != null)
			return annotations;
		return EMPTY_ARRAY;
	}
	
	@Override
	public Control getControl() {
	    return composite;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	protected void updateSelection() {
		ISelection selection = AnnotationUIPlugin.getManager().getSelection();
		if (selection instanceof IStructuredSelection)
			selectedElements = (IStructuredSelection) selection;
		updatePage();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection)
			selectedElements = (IStructuredSelection) selection;
		this.part = part;
		updatePage();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProviderListener#labelProviderChanged(org.eclipse.jface.viewers.LabelProviderChangedEvent)
	 */
	public void labelProviderChanged(LabelProviderChangedEvent event) {
		if (event.getElements() == null && getControl() != null) {
            super.labelProviderChanged(event);
            return;
        }

		IStructuredSelection structuredSelection = getSelectedElements();
		if (structuredSelection == null) {
			return;
		}

		List<EObject> selection = new ArrayList<EObject>();
		for (Iterator<?> e = structuredSelection.iterator(); e.hasNext();) {
			Object next = e.next();
			if (next instanceof IAdaptable) {
				EObject object = (EObject)((IAdaptable) next).getAdapter(EObject.class);
				if (object != null)
					selection.add(object);
			} else if (next instanceof EObject) {
				selection.add((EObject)next);
			}
		}


		if (selection.isEmpty()) // no point in expensive calculations if there
								 // are no elements
			return;
		
		List<EObject> elementsAffected = new ArrayList<EObject>();
		for (int i = 0; i < event.getElements().length; i++) {
			Object next = event.getElements()[i];
			if (next instanceof IAdaptable) {
				EObject object = (EObject)((IAdaptable) next).getAdapter(EObject.class);
				if (object != null)
					elementsAffected.add(object);
			} else if (next instanceof EObject) {
				elementsAffected.add((EObject)next);
			}
		}

		selection.retainAll(elementsAffected);
		if (!selection.isEmpty())
			super.labelProviderChanged(event);

	}
	
	/**
	 * Get the property sheet page contributor.
	 * 
	 * @return the property sheet page contributor.
	 */
	public ITabbedPropertySheetPageContributor getContributor() {
		return contributor;
	}
	/**
	 * @return Returns the selectedElements.
	 */
	protected IStructuredSelection getSelectedElements() {
		return selectedElements;
	}

}