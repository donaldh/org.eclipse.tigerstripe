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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationFactory;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.core.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.core.RefactoringChange;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.core.IAnnotationActionConstants;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.OpenAnnotationWizardAction;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.RemoveAnnotationAction;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.RemoveURIAnnotationAction;
import org.eclipse.tigerstripe.annotation.ui.internal.util.AnnotationSelectionUtils;
import org.eclipse.tigerstripe.annotation.ui.util.AsyncExecUtil;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ISectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
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
	implements IPropertyChangeListener, IAnnotationListener,
	IRefactoringListener, IResourceChangeListener {

	/**
	 * the contributor for this property sheet page
	 */
	private ITabbedPropertySheetPageContributor contributor;

	private IStructuredSelection selectedElements;

	private List<IResource> selectedResources;
	
	private IWorkbenchPart part;

	private Composite composite;
	private Composite leftPart;
	
	private ListViewer viewer;
	
	private Annotation[] currentSelection;
	
	private static Annotation NULL_ANNOTATION = AnnotationFactory.eINSTANCE.createAnnotation();

	private StructuredSelection EMPTY_SELECTION = new StructuredSelection();
	
	private Map<Annotation, DirtyAdapter> adapters = new HashMap<Annotation, DirtyAdapter>();
	
	private IAnnotationEditorListener listener;
	private boolean block = false;

	/**
	 * Constructor
	 * @param contributor the <code>ITabbedPropertySheetPageContributor</code> 
	 *  for this property sheet page
	 */
	public PropertiesBrowserPage(ITabbedPropertySheetPageContributor contributor,
			IAnnotationEditorListener listener) {
		super(contributor);
		this.contributor = contributor;
		this.listener = listener;
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
		AnnotationPlugin.getManager().addRefactoringListener(this);
		AnnotationPlugin.getManager().addAnnotationListener(this);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	
	protected void removeListeners() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		AnnotationPlugin.getManager().removeAnnotationListener(this);
		AnnotationPlugin.getManager().removeRefactoringListener(this);
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
	public void annotationsLoaded(Annotation[] annotations) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IRefactoringListener#refactoringPerformed(org.eclipse.tigerstripe.annotation.core.RefactoringChange)
	 */
	public void refactoringPerformed(RefactoringChange change) {
		updateSelection();
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
	
	private void initMenu(Control control, IMenuListener menuListener) {
		MenuManager menuMgr = new LimitMenuManager(new String[] {
				IAnnotationActionConstants.ANNOTATION_PROPERTIES,
				IAnnotationActionConstants.ANNOTATION_PROPERTIES_GROUP});
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(menuListener);
		Menu menu = menuMgr.createContextMenu(control);
		control.setMenu(menu);
		listener.getSite().registerContextMenu(menuMgr, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage#updateTabs(org.eclipse.ui.views.properties.tabbed.ITabDescriptor[])
	 */
	@Override
	protected void updateTabs(ITabDescriptor[] descriptors) {
		super.updateTabs(descriptors);
	}
	
	protected List<ISectionDescriptor> getDescriptors(ISectionDescriptor descriptor) {
		ArrayList<ISectionDescriptor> descriptors = new ArrayList<ISectionDescriptor>();
		descriptors.add(descriptor);
		return descriptors;
	}
	
	private void fillContentMenu(IMenuManager manager) {
		String group = IAnnotationActionConstants.ANNOTATION_PROPERTIES_GROUP;
		manager.add(new GroupMarker(group));
		
	    Annotation annotation = getSelectedAnnotation();
	    Object annotable = getAnnotableElement();
	    if (annotable != null) {
			IAction action = new OpenAnnotationWizardAction(annotable, "Add");
		    ActionContributionItem item = new ActionContributionItem(action);
		    manager.appendToGroup(group, item);
	    }
	    
	    if (annotation != null) {
	    	RemoveAnnotationAction action = new RemoveAnnotationAction(annotation);
		    ActionContributionItem item = new ActionContributionItem(action);
		    manager.appendToGroup(group, item);
	    }
	    
	    if (annotable != null) {
		    RemoveURIAnnotationAction removeAll = new RemoveURIAnnotationAction(annotable);
		    if (removeAll.isEnabled()) {
		    	ActionContributionItem item = new ActionContributionItem(removeAll);
			    manager.appendToGroup(group, item);
		    }
	    }
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
		title.setTitle("Annotations", null);

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
	
	private Annotation getSelectedAnnotation() {
		int index = viewer.getList().getSelectionIndex();
		if (currentSelection != null) {
			if (index >= 0 && index < currentSelection.length)
				return currentSelection[index];
		}
		return null;
	}
	
	private void save(Annotation annotation) {
		DirtyAdapter adapter = adapters.get(annotation);
		if (adapter == null || adapter.isDirty()) {
			block = true;
			AnnotationPlugin.getManager().save(annotation);
			if (adapter != null) adapter.clear();
			block = false;
		}
	}
	
	private void revert(Annotation annotation) {
		DirtyAdapter adapter = adapters.get(annotation);
		block = true;
		AnnotationPlugin.getManager().revert(annotation);
		block = false;
		if (adapter != null)
			adapter.clear();
	}
	
	public void saveAnnotation() {
		Annotation annotation = getSelectedAnnotation();
		if (annotation != null) {
			save(annotation);
		}
	}
	
	public void saveAllAnnotations() {
		if (currentSelection != null) {
			for (int i = 0; i < currentSelection.length; i++) {
				save(currentSelection[i]);
			}
		}
	}
	
	public void revertAnnotation() {
		Annotation annotation = getSelectedAnnotation();
		if (annotation != null) {
			revert(annotation);
		}
		updatePageSelection();
	}
	
	private void updatePageSelection() {
		Annotation annotation = getSelectedAnnotation();
		if (annotation != null) {
			superSelectionChanged(part, new StructuredSelection(annotation) {
				public boolean equals(Object o) {
					return false;
				}
			});
		}
	}
	
	public void revertAllAnnotations() {
		if (currentSelection != null) {
			for (int i = 0; i < currentSelection.length; i++) {
				revert(currentSelection[i]);
			}
		}
		updatePageSelection();
	}
	
	protected void createNavigateMenu(Composite parent, TabbedPropertySheetWidgetFactory factory) {
		Composite composite = createTitle(parent, factory);
		FormLayout formLayout = new FormLayout();
		formLayout.marginTop = 4;
		formLayout.marginRight = 4;
		composite.setLayout(formLayout);
		
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(composite, SWT.BORDER);
		factory.paintBordersFor(composite);
        
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		
		list.setLayoutData(formData);
		
		viewer = new ListViewer(list);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
		
			public void selectionChanged(SelectionChangedEvent event) {
				Annotation annotation = getSelectedAnnotation();
				if (annotation != null) {
					setPageSelection(annotation);
					updateStatus();
				}
			}
		});
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new AnnotationDisplayLabelProvider());
		
		initMenu(list, new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContentMenu(manager);
			}
		});
	}
	
	protected void setPageSelection(Annotation annotation) {
		superSelectionChanged(part, new StructuredSelection(annotation));
	}
	
	protected void setPageEmpty() {
		superSelectionChanged(part, new StructuredSelection(NULL_ANNOTATION));
	}
	
	protected void adapt(int index) {
		EObject content = currentSelection[index].getContent();
		if (content != null) {
			DirtyAdapter adapter = new DirtyAdapter(index);
			content.eAdapters().add(adapter);
			adapters.put(currentSelection[index], adapter);
		}
	}
	
	protected void updatePage() {
		if (viewer != null && !viewer.getList().isDisposed()) {
			listener.dirtyChanged(IAnnotationEditorListener.NO_CHANGES);
			currentSelection = getAnnotation(selectedElements);
			int newSelection = viewer.getList().getSelectionIndex();
			boolean showPage = currentSelection != null;
			if (showPage) {
				viewer.setInput(currentSelection);
				for (int i = 0; i < currentSelection.length; i++) {
					adapt(i);
	            }
				int index = getAnnotationIndex();
				int itemCount = viewer.getList().getItemCount();
				if (index >= 0) {
					newSelection = index;
				}
				else if (newSelection < 0 || newSelection >= itemCount) {
					newSelection = 0;
				}
				if (newSelection >= 0 && itemCount > newSelection) {
					viewer.getList().select(newSelection);
					setPageSelection(currentSelection[newSelection]);
				}
				else {
					setPageEmpty();
				}
			}
			else {
				superSelectionChanged(null, EMPTY_SELECTION);
			}
			leftPart.setVisible(showPage);
			composite.layout();
		}
	}
	
	private void superSelectionChanged(IWorkbenchPart part, ISelection selection) {
		Annotation annotation = getSelectedAnnotation();
		if (annotation != null)
			TabDescriptionManipulator.getInstance().update(annotation);
		super.selectionChanged(part, selection);
	}
	
	private void updateStatus() {
		int status = IAnnotationEditorListener.NO_CHANGES;
		if (currentSelection != null) {
			Annotation selected = getSelectedAnnotation();
			for (int i = 0; i < currentSelection.length; i++) {
				DirtyAdapter adapter = adapters.get(currentSelection[i]);
				if (adapter != null && adapter.isDirty()) {
					if (selected == currentSelection[i]) {
						status = IAnnotationEditorListener.SELECTION_CHANGES;
						break;
					}
					status = IAnnotationEditorListener.NON_SELECTION_CHANGES;
				}
			}
		}
		listener.dirtyChanged(status);
	}
	
	private class DirtyAdapter extends AdapterImpl {
		
		private int index;
		private boolean dirty;
		
		public DirtyAdapter(int index) {
			this.index = index;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse.emf.common.notify.Notification)
		 */
		@Override
		public void notifyChanged(Notification msg) {
			if (msg.getEventType() == Notification.RESOLVE || 
					msg.getEventType() == Notification.REMOVING_ADAPTER)
				return;
			viewer.getList().setItem(index, "*" + DisplayAnnotationUtil.getText(
					currentSelection[index]));
			if (!dirty) {
				dirty = true;
				updateStatus();
			}
		}
		
		public void clear() {
			dirty = false;
			viewer.getList().setItem(index, DisplayAnnotationUtil.getText(
					currentSelection[index]));
			updateStatus();
		}
		
		public boolean isDirty() {
			return dirty;
		}
		
	}
	
	private int getAnnotationIndex() {
		Annotation annotation = AnnotationSelectionUtils.getAnnotation(selectedElements);
		for (int i = 0; i < currentSelection.length; i++) {
	        if (currentSelection[i].equals(annotation))
	        	return i;
        }
		return -1;
	}
	
	private Object getAnnotableElement() {
		Object annotable = null;
		if (selectedElements != null) {
			annotable = AnnotationSelectionUtils.getAnnotableElement(selectedElements);
			Annotation annotation = AnnotationSelectionUtils.getAnnotation(annotable);
			if (annotation != null) {
				annotable = AnnotationPlugin.getManager(
						).getAnnotatedObject(annotation);
			}
		}
		return annotable;
	}
	
	private Annotation[] getAnnotation(ISelection selection) {
		Object object = AnnotationSelectionUtils.getAnnotableElement(selection);
		if (object == null)
			return null;
		Annotation annotation = AnnotationSelectionUtils.getAnnotation(object);
		if (annotation != null)
			return new Annotation[] { annotation };
		List<Annotation> annotations = new ArrayList<Annotation>();
		if (!AnnotationUtils.getAllAnnotations(object, annotations))
			return null;
		return annotations.toArray(new Annotation[annotations.size()]);
	}
	
	@Override
	public Control getControl() {
	    return composite;
	}
	
	protected void disposeSelection() {
		List<Annotation> dirties = new ArrayList<Annotation>(); 
		if (currentSelection != null) {
			for (Annotation annotation : currentSelection) {
				EObject content = annotation.getContent();
				DirtyAdapter adapter = adapters.get(annotation);
				if (content != null && adapter != null) {
					if (adapter.isDirty())
						dirties.add(annotation);
					content.eAdapters().remove(adapter);
				}
			}
		}
		adapters = new HashMap<Annotation, DirtyAdapter>();
		if (dirties.size() > 0) {
			block = true;
			Shell shell = WorkbenchUtil.getShell();
			if (shell != null) {
				Annotation[] annotations = dirties.toArray(new Annotation[dirties.size()]);
				MessageBox message = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				message.setMessage("Annotations have been modified. Save changes?");
				message.setText("Save Annotations");
				if (message.open() == SWT.YES)
					for (Annotation annotation : annotations)
						AnnotationPlugin.getManager().save(annotation);
			}
			block = false;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	protected void updateSelection() {
		if (block)
			return;
		disposeSelection();
		AsyncExecUtil.run(composite, new Runnable() {
			
			public void run() {
				setSelected(AnnotationUIPlugin.getManager().getSelection());
				updatePage();
			}
		
		});
	}
	
	private void setSelected(ISelection selection) {
		if (selection instanceof IStructuredSelection)
			selectedElements = (IStructuredSelection) selection;
		selectedResources = null;
	}
	
	private List<IResource> getResources() {
		if (selectedResources == null) {
			if (selectedElements != null) {
				selectedResources = new ArrayList<IResource>();
				Iterator<?> it = selectedElements.iterator();
				while (it.hasNext()) {
					Object object = it.next();
					IResource res = (IResource)Platform.getAdapterManager().getAdapter(object, IResource.class);
					if (res != null)
						selectedResources.add(res);
				}
			}
			else {
				selectedResources = new ArrayList<IResource>();
			}
		}
		return selectedResources;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		disposeSelection();
		setSelected(selection);
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


		if (selection.isEmpty())
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

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		final List<IResource> resources = getResources();
		final boolean[] updateSelection = new boolean[1];
		if (delta != null && resources.size() > 0) {
			try {
				delta.accept(new IResourceDeltaVisitor() {
					public boolean visit(IResourceDelta delta) throws CoreException {
						if ((delta.getFlags() & IResourceDelta.OPEN) > 0) {
							IResource res = delta.getResource();
							if (res != null && resources.contains(res)) {
								updateSelection[0] = true;
							}
						}
						return !updateSelection[0];
					}
				});
			} catch (CoreException e) {
				AnnotationUIPlugin.log(e);
			}
		}
		if (updateSelection[0]) updateSelection();
	}

}