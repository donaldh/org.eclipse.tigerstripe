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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.tigerstripe.annotation.ui.core.view.INoteListener;
import org.eclipse.tigerstripe.annotation.ui.core.view.INoteProvider;
import org.eclipse.tigerstripe.annotation.ui.core.view.NoteProviderManager;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.views.properties.IPropertySheetPage;

public class PropertySheet extends PageBookView implements INoteListener,
		ISelectionListener {

	public static final String ID = "org.eclipse.tigerstripe.annotation.view.property";

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
				new INotePropertySheetContributor() {

					public String getContributorId() {
						return "org.eclipse.tigerstripe.annotation.ui.properties";
					}

					public void updateNotes() {
						updateSelection();
					}

				}, getProviders());
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

	public void notesChanged(INote[] notes) {
		updateSelection();
	}

	private void addListeners() {
		for (INoteProvider provider : getProviders()) {
			provider.addListener(this);
		}
		WorkbenchUtil.getWindow().getSelectionService()
				.addPostSelectionListener(this);
	}

	private void removeListeners() {
		WorkbenchUtil.getWindow().getSelectionService()
				.removePostSelectionListener(this);
		for (INoteProvider provider : getProviders()) {
			provider.removeListener(this);
		}
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
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// we ignore our own selection
		if (part == this)
			return;
		if (selection == null) {
			selection = new StructuredSelection();
		}
		this.part = part;
		for (INoteProvider provider : getProviders()) {
			provider.setSelection(part, selection);
		}
		updateSelection();
	}

	private void updateSelection() {
		List<INote> allNotes = new ArrayList<INote>();
		for (INoteProvider provider : getProviders()) {
			boolean hide = getCurrentPage().isHideNotes(provider);
			if (!hide) {
				INote[] notes = provider.getNotes();
				for (INote iNote : notes) {
					allNotes.add(iNote);
				}
			}
		}

		ISelection sel = new StructuredSelection(allNotes);

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

	private INoteProvider[] getProviders() {
		if (providers == null) {
			providers = NoteProviderManager.createProviders();
		}
		return providers;
	}

	private IWorkbenchPart part;
	private INoteProvider[] providers;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.PageBookView#getCurrentPage()
	 */
	@Override
	public PropertiesBrowserPage getCurrentPage() {
		return (PropertiesBrowserPage) super.getCurrentPage();
	}
}
