/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.core.RefactoringChange;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.core.ISelectionFilter;
import org.eclipse.tigerstripe.annotation.ui.util.AsyncExecUtil;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;

public class PropertySheet extends PageBookView 
	implements ISelectionListener, IRefactoringListener, ISelectionFilter, IAnnotationEditorListener {
	
	public static final String ID = "org.eclipse.tigerstripe.annotation.view.property";
	
	private Action saveAction;
	private Action saveAllAction;
	private Action revertAction;
	private Action revertAllAction;

    /**
     * Creates a property sheet view.
     */
    public PropertySheet() {
        super();
    }

    /* (non-Javadoc)
     * Method declared on PageBookView.
     * Returns the default property sheet page.
     */
    protected IPage createDefaultPage(PageBook book) {
        PropertySheetPage page = new PropertySheetPage();
        initPage(page);
        page.createControl(book);
        return page;
    }

    /**
     * The <code>PropertySheet</code> implementation of this <code>IWorkbenchPart</code>
     * method creates a <code>PageBook</code> control with its default page showing.
     */
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        addListeners();
    }
	
	protected void addListeners() {
		AnnotationPlugin.getManager().addRefactoringListener(this);
		AnnotationUIPlugin.getManager().addSelectionListener(this);
		AnnotationUIPlugin.getManager().addSelectionFilter(this);
	}
	
	protected void removeListeners() {
		AnnotationUIPlugin.getManager().removeSelectionFilter(this);
		AnnotationUIPlugin.getManager().removeSelectionListener(this);
		AnnotationPlugin.getManager().removeRefactoringListener(this);
	}

    /* (non-Javadoc)
     * Method declared on IWorkbenchPart.
     */
    public void dispose() {
        // run super.
        super.dispose();
        removeListeners();
    }

    /* (non-Javadoc)
     * Method declared on PageBookView.
     */
    protected PageRec doCreatePage(IWorkbenchPart part) {
        // Try to get a custom property sheet page.
//        IPropertySheetPage page = (IPropertySheetPage) getAdapter(part,
//                IPropertySheetPage.class, false);
    	PropertiesBrowserPage page = new PropertiesBrowserPage(
    			new ITabbedPropertySheetPageContributor() {
		
			public String getContributorId() {
				return "org.eclipse.tigerstripe.annotation.ui.properties";
			}
		
		}, this);
        if (page != null) {
            if (page instanceof IPageBookViewPage) {
				initPage((IPageBookViewPage) page);
			}
            page.createControl(getPageBook());
            return new PageRec(part, page);
        }

        // Use the default page		
        return null;
    }

    /* (non-Javadoc)
     * Method declared on PageBookView.
     */
    protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
        IPropertySheetPage page = (IPropertySheetPage) rec.page;
        page.dispose();
        rec.dispose();
    }

    /* (non-Javadoc)
     * Method declared on PageBookView.
     * Returns the active part on the same workbench page as this property 
     * sheet view.
     */
    protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPage page = getSite().getPage();
        if (page != null) {
            return page.getActivePart();
        } 
        return null;
    }
    
    protected PropertiesBrowserPage getPage() {
    	IPage page = getCurrentPage();
    	if (page instanceof PropertiesBrowserPage)
    		return (PropertiesBrowserPage)page;
    	return null;
    }
    
    private void hookGlobalActions(final Action save, final Action saveAll) {
    	IActionBars bars = getViewSite().getActionBars();
    	bars.clearGlobalActionHandlers();
    	IHandlerService service = (IHandlerService) getSite().getService(
    			IHandlerService.class);
    	service.activateHandler("org.eclipse.ui.file.save",
    			new AbstractHandler() {
					public Object execute(ExecutionEvent arg0)
							throws ExecutionException {
						if (save.isEnabled())
							save.run();
						return null;
					}
				});     	
    	service.activateHandler("org.eclipse.ui.file.saveAll",
    			new AbstractHandler() {
					public Object execute(ExecutionEvent arg0)
							throws ExecutionException {
						if (saveAll.isEnabled())
							saveAll.run();
						return null;
					}
				});
   }
    /* (non-Javadoc)
     * Method declared on IViewPart.
     */
    public void init(IViewSite site) throws PartInitException {
        super.init(site);
        saveAction = new Action("Save") {
        	public void run() {
        		PropertiesBrowserPage page = getPage();
        		if (page != null)
        			page.saveAnnotation();
        	}
        };
        saveAction.setImageDescriptor(AnnotationUIPlugin.createImageDescriptor("icons/save.gif"));
        getViewSite().getActionBars().getToolBarManager().add(saveAction);
        
        saveAllAction = new Action("Save All") {
        	public void run() {
        		PropertiesBrowserPage page = getPage();
        		if (page != null)
        			page.saveAllAnnotations();
        	}
        };
        saveAllAction.setImageDescriptor(AnnotationUIPlugin.createImageDescriptor("icons/save_all.gif"));
        getViewSite().getActionBars().getToolBarManager().add(saveAllAction);
        
        hookGlobalActions(saveAction, saveAllAction);
        
        revertAction = new Action("Revert") {
        	public void run() {
        		PropertiesBrowserPage page = getPage();
        		if (page != null)
        			page.revertAnnotation();
        	}
        };
        revertAction.setImageDescriptor(AnnotationUIPlugin.createImageDescriptor("icons/revert.gif"));
        getViewSite().getActionBars().getToolBarManager().add(revertAction);
        
        revertAllAction = new Action("Revert All") {
        	public void run() {
        		PropertiesBrowserPage page = getPage();
        		if (page != null)
        			page.revertAllAnnotations();
        	}
        };
        revertAllAction.setImageDescriptor(AnnotationUIPlugin.createImageDescriptor("icons/revert_all.gif"));
        getViewSite().getActionBars().getToolBarManager().add(revertAllAction);
        getViewSite().getActionBars().updateActionBars();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.annotation.ui.internal.view.property.IAnnotationEditorListener#dirtyChanged(int)
     */
    public void dirtyChanged(int status) {
    	switch (status) {
			case NO_CHANGES:
				saveAction.setEnabled(false);
				saveAllAction.setEnabled(false);
				revertAction.setEnabled(false);
				revertAllAction.setEnabled(false);
				break;
			case NON_SELECTION_CHANGES:
				saveAction.setEnabled(false);
				saveAllAction.setEnabled(true);
				revertAction.setEnabled(false);
				revertAllAction.setEnabled(true);
				break;
			case SELECTION_CHANGES:
				saveAction.setEnabled(true);
				saveAllAction.setEnabled(true);
				revertAction.setEnabled(true);
				revertAllAction.setEnabled(true);
				break;
		}
    }

    /* (non-Javadoc)
     * Method declared on PageBookView.
     * The property sheet may show properties for any view other than this view.
     */
    protected boolean isImportant(IWorkbenchPart part) {
        return part != this;
    }
    
    /**
     * If it is possible to adapt the given object to the given type, this
     * returns the adapter. Performs the following checks:
     * 
     * <ol>
     * <li>Returns <code>sourceObject</code> if it is an instance of the
     * adapter type.</li>
     * <li>If sourceObject implements IAdaptable, it is queried for adapters.</li>
     * <li>If sourceObject is not an instance of PlatformObject (which would have
     * already done so), the adapter manager is queried for adapters</li>
     * </ol>
     * 
     * Otherwise returns null.
     * 
     * @param sourceObject
     *            object to adapt, or null
     * @param adapter
     *            type to adapt to
     * @param activatePlugins 
     *            true if IAdapterManager.loadAdapter should be used (may trigger plugin activation)
     * @return a representation of sourceObject that is assignable to the
     *         adapter type, or null if no such representation exists
     */
    public static Object getAdapter(Object sourceObject, Class<?> adapter, boolean activatePlugins) {
    	Assert.isNotNull(adapter);
        if (sourceObject == null) {
            return null;
        }
        if (adapter.isInstance(sourceObject)) {
            return sourceObject;
        }

        if (sourceObject instanceof IAdaptable) {
            IAdaptable adaptable = (IAdaptable) sourceObject;

            Object result = adaptable.getAdapter(adapter);
            if (result != null) {
                // Sanity-check
                Assert.isTrue(adapter.isInstance(result));
                return result;
            }
        } 
        
        if (!(sourceObject instanceof PlatformObject)) {
        	Object result;
        	if (activatePlugins) {
        		result = Platform.getAdapterManager().loadAdapter(sourceObject, adapter.getName());
        	} else {
        		result = Platform.getAdapterManager().getAdapter(sourceObject, adapter);
        	}
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /**
     * The <code>PropertySheet</code> implementation of this <code>IPartListener</code>
     * method first sees if the active part is an <code>IContributedContentsView</code>
     * adapter and if so, asks it for its contributing part.
     */
    public void partActivated(final IWorkbenchPart part) {
    	AsyncExecUtil.run(getPageBook(), new Runnable() {
		
			public void run() {
		    	// Look for a declaratively-contributed adapter - including not yet loaded adapter factories.
		    	// See bug 86362 [PropertiesView] Can not access AdapterFactory, when plugin is not loaded.
		        IContributedContentsView view = (IContributedContentsView) getAdapter(part,
		                IContributedContentsView.class, true);
		        IWorkbenchPart source = null;
		        if (view != null) {
					source = view.getContributingPart();
				}
		        
		        if (source != null) {
					PropertySheet.super.partActivated(source);
				} else {
					PropertySheet.super.partActivated(part);
				}
			}
		
		});
    }

    /* (non-Javadoc)
     * Method declared on ISelectionListener.
     * Notify the current page that the selection has changed.
     */
    public void selectionChanged(IWorkbenchPart part, ISelection sel) {
        // we ignore our own selection or null selection
        if (part == this)
			return;

        // pass the selection to the page		
        IPropertySheetPage page = (IPropertySheetPage) getCurrentPage();
        if (page != null) {
			page.selectionChanged(part, sel);
		}
    }
    
    /**
	 * The <code>PropertySheet</code> implementation of this
	 * <code>PageBookView</code> method handles the <code>ISaveablePart</code>
	 * adapter case by calling <code>getSaveablePart()</code>.
	 * 
	 * @since 3.2
	 */
	@SuppressWarnings("unchecked")
	protected Object getViewAdapter(Class key) {
		if (ISaveablePart.class.equals(key)) {
			return getSaveablePart();
		}
		return super.getViewAdapter(key);
	}

	/**
	 * Returns an <code>ISaveablePart</code> that delegates to the source part
	 * for the current page if it implements <code>ISaveablePart</code>, or
	 * <code>null</code> otherwise.
	 * 
	 * @return an <code>ISaveablePart</code> or <code>null</code>
	 * @since 3.2
	 */
	protected ISaveablePart getSaveablePart() {
		IWorkbenchPart part = getCurrentContributingPart();
		if (part instanceof ISaveablePart) {
			return (ISaveablePart) part;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IRefactoringListener#refactoringPerformed(org.eclipse.tigerstripe.annotation.core.RefactoringChange)
	 */
	public void refactoringPerformed(RefactoringChange change) {
		AsyncExecUtil.run(getPageBook(), new Runnable() {
			
			public void run() {
				selectionChanged(null, AnnotationUIPlugin.getManager().getSelection());
			}
		
		});
	}

	public boolean select(IWorkbenchPart part, ISelection selection) {
		return part != this;
    }
}
