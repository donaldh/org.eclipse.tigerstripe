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
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;

public class PropertySheet extends PageBookView implements ISelectionListener,
		IRefactoringListener, ISelectionFilter, IAnnotationEditorListener,
		IPropertiesSelectionListener {

	public static final String ID = "org.eclipse.tigerstripe.annotation.view.property";

	private Action addAction;
	private Action removeAction;
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

	/*
	 * (non-Javadoc) Method declared on PageBookView. Returns the default
	 * property sheet page.
	 */
	protected IPage createDefaultPage(PageBook book) {
		return doCreatePage(book);
	}

	protected PropertiesBrowserPage doCreatePage(PageBook book) {
		PropertiesBrowserPage page = new PropertiesBrowserPage(
				new ITabbedPropertySheetPageContributor() {

					public String getContributorId() {
						return "org.eclipse.tigerstripe.annotation.ui.properties";
					}

				}, this);
		initPage(page);
		page.createControl(book);
		return page;
	}

	/**
	 * The <code>PropertySheet</code> implementation of this
	 * <code>IWorkbenchPart</code> method creates a <code>PageBook</code>
	 * control with its default page showing.
	 */
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		addListeners();
	}

	protected void addListeners() {
		AnnotationPlugin.getManager().addRefactoringListener(this);
		AnnotationUIPlugin.getManager().addSelectionListener(this);
		AnnotationUIPlugin.getManager().addSelectionFilter(this);
		PropertiesSelectionManager.getInstance().addListener(this);
	}

	protected void removeListeners() {
		PropertiesSelectionManager.getInstance().removeListener(this);
		AnnotationUIPlugin.getManager().removeSelectionFilter(this);
		AnnotationUIPlugin.getManager().removeSelectionListener(this);
		AnnotationPlugin.getManager().removeRefactoringListener(this);
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchPart.
	 */
	public void dispose() {
		// run super.
		super.dispose();
		removeListeners();
	}

	/*
	 * (non-Javadoc) Method declared on PageBookView.
	 */
	protected PageRec doCreatePage(IWorkbenchPart part) {
		PropertiesBrowserPage page = doCreatePage(getPageBook());
		return new PageRec(part, page);
	}

	/*
	 * (non-Javadoc) Method declared on PageBookView.
	 */
	protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
		IPropertySheetPage page = (IPropertySheetPage) rec.page;
		page.dispose();
		rec.dispose();
	}

	/*
	 * (non-Javadoc) Method declared on PageBookView. Returns the active part on
	 * the same workbench page as this property sheet view.
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
			return (PropertiesBrowserPage) page;
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

	/*
	 * (non-Javadoc) Method declared on IViewPart.
	 */
	public void init(IViewSite site) throws PartInitException {
		super.init(site);

		addAction = new Action("Add") {
			public void run() {
				PropertiesSelectionManager.getInstance().getSelection()
						.addDefaultValue();
			}
		};
		addAction.setImageDescriptor(AnnotationUIPlugin
				.createImageDescriptor("icons/add.gif"));
		getViewSite().getActionBars().getToolBarManager().add(addAction);

		removeAction = new Action("Remove") {
			public void run() {
				PropertiesSelectionManager.getInstance().getSelection()
						.remove();
			}
		};
		removeAction.setImageDescriptor(AnnotationUIPlugin
				.createImageDescriptor("icons/remove.gif"));
		getViewSite().getActionBars().getToolBarManager().add(removeAction);

		saveAction = new Action("Save") {
			public void run() {
				PropertiesBrowserPage page = getPage();
				if (page != null)
					page.saveAnnotation();
			}
		};
		saveAction.setImageDescriptor(AnnotationUIPlugin
				.createImageDescriptor("icons/save.gif"));
		getViewSite().getActionBars().getToolBarManager().add(saveAction);

		saveAllAction = new Action("Save All") {
			public void run() {
				PropertiesBrowserPage page = getPage();
				if (page != null)
					page.saveAllAnnotations();
			}
		};
		saveAllAction.setImageDescriptor(AnnotationUIPlugin
				.createImageDescriptor("icons/save_all.gif"));
		getViewSite().getActionBars().getToolBarManager().add(saveAllAction);

		hookGlobalActions(saveAction, saveAllAction);

		revertAction = new Action("Revert") {
			public void run() {
				PropertiesBrowserPage page = getPage();
				if (page != null)
					page.revertAnnotation();
			}
		};
		revertAction.setImageDescriptor(AnnotationUIPlugin
				.createImageDescriptor("icons/revert.gif"));
		getViewSite().getActionBars().getToolBarManager().add(revertAction);

		revertAllAction = new Action("Revert All") {
			public void run() {
				PropertiesBrowserPage page = getPage();
				if (page != null)
					page.revertAllAnnotations();
			}
		};
		revertAllAction.setImageDescriptor(AnnotationUIPlugin
				.createImageDescriptor("icons/revert_all.gif"));
		getViewSite().getActionBars().getToolBarManager().add(revertAllAction);
		getViewSite().getActionBars().updateActionBars();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.ui.internal.view.property.
	 * IAnnotationEditorListener#dirtyChanged(int)
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

	/*
	 * (non-Javadoc) Method declared on PageBookView. The property sheet may
	 * show properties for any view other than this view.
	 */
	protected boolean isImportant(IWorkbenchPart part) {
		return part != this;
	}

	/**
	 * The <code>PropertySheet</code> implementation of this
	 * <code>IPartListener</code> method first sees if the active part is an
	 * <code>IContributedContentsView</code> adapter and if so, asks it for its
	 * contributing part.
	 */
	public void partActivated(final IWorkbenchPart part) {
	}

	/*
	 * (non-Javadoc) Method declared on ISelectionListener. Notify the current
	 * page that the selection has changed.
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection sel) {
		// we ignore our own selection or null selection
		if (part == this)
			return;

		// pass the selection to the page
		IPropertySheetPage page = (IPropertySheetPage) getCurrentPage();
		if (page == null)
			doCreatePage(part);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.core.IRefactoringListener#
	 * refactoringPerformed
	 * (org.eclipse.tigerstripe.annotation.core.RefactoringChange)
	 */
	public void refactoringPerformed(RefactoringChange change) {
		AsyncExecUtil.run(getPageBook(), new Runnable() {

			public void run() {
				selectionChanged(null, AnnotationUIPlugin.getManager()
						.getSelection());
			}

		});
	}

	public boolean select(IWorkbenchPart part, ISelection selection) {
		return part != this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.ui.internal.view.property.
	 * IPropertiesSelectionListener#selectionChanged(java.util.List, int)
	 */
	public void selectionChanged(PropertySelection selection) {
		int status = PropertySelection.SINGLE_SELECTION;
		if (selection != null && !selection.isReadOnly()) {
			status = selection.getStatus();
		}
		switch (status) {
		case PropertySelection.SINGLE_SELECTION:
			addAction.setToolTipText("Add to the list");
			removeAction.setToolTipText("Remove element");
			break;
		case PropertySelection.CHILD_SELECTION:
			addAction.setToolTipText("Insert element before selection");
			removeAction.setToolTipText("Remove selected element");
			break;
		default:
			addAction.setToolTipText("Append element to the end of the list");
			removeAction.setToolTipText("Clear list");
			break;
		}
		addAction.setEnabled(status > 0);
		removeAction.setEnabled(status > 0);
	}
}
