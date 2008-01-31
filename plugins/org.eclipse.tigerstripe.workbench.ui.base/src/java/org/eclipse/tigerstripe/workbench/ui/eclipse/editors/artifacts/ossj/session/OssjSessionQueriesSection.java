/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.ArtifactSelectorDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.IArtifactLabelProvider;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class OssjSessionQueriesSection extends OssjSessionElementsSection {

	public class QueriesContentProvider implements IStructuredContentProvider {

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ISessionArtifact) {
				ISessionArtifact session = (ISessionArtifact) inputElement;
				Collection<INamedQuery> queries =  session.getNamedQueries();
				return queries.toArray();
			}
			return new Object[0];
		}

	}

	public class MEntitiesLabelProvider implements IArtifactLabelProvider {

		public String getName(Object element) {
			return Util.nameOf(getText(element));
		}

		public String getPackage(Object element) {
			return Util.packageOf(getText(element));
		}

		public Object getProperty(Object Element, String propertyName) {
			// TODO Auto-generated method stub
			return null;
		}

		public void setProperty(Object Element, String propertyName,
				Object property) {
			// TODO Auto-generated method stub

		}

		public Image getImage(Object element) {
			return TigerstripePluginImages
					.get(TigerstripePluginImages.QUERY_ICON);
		}

		public String getText(Object element) {
			if (element instanceof INamedQuery) {
				INamedQuery details = (INamedQuery) element;
				return details.getFullyQualifiedName();
			}
			return "";
		}

		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}
	}

	private QueriesContentProvider contentProvider;
	private MEntitiesLabelProvider labelProvider;

	public OssjSessionQueriesSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider);
		this.contentProvider = new QueriesContentProvider();
		this.labelProvider = new MEntitiesLabelProvider();
		setTitle("Named Queries");
	}

	@Override
	protected IStructuredContentProvider getElementsContentProvider() {
		if (contentProvider == null) {
			contentProvider = new QueriesContentProvider();
		}
		return contentProvider;
	}

	@Override
	protected ILabelProvider getElementsLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new MEntitiesLabelProvider();
		}
		return labelProvider;
	}

	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	@Override
	protected void addButtonSelected(SelectionEvent event)
			throws TigerstripeException {
		ArtifactSelectorDialog elsd = new ArtifactSelectorDialog(getPage()
				.getManagedForm().getForm().getShell(), labelProvider);

		elsd.setTitle("Named Query Artifacts");
		elsd
				.setMessage("Select a set of Named Queries to be exposed through this session.");

		ISessionArtifact session = (ISessionArtifact) getIArtifactFromEditor();
		Object[] availableEntityOptions = getAvailableQueryOptionsList();
		elsd.setElements(availableEntityOptions);

		if (elsd.open() == Window.OK) {
			Object[] details = elsd.getResult();
			for (int i = 0; i < details.length; i++) {
				session.addNamedQuery((INamedQuery) details[i]);
				getViewer().add(details[i]);
				markPageModified();
			}
		}
	}

	private Object[] getAvailableQueryOptionsList() throws TigerstripeException {

		// Get the already selected entities, so the user won't be presented
		// with
		// already selected entities.
		ISessionArtifact session = (ISessionArtifact) getIArtifactFromEditor();
		List selectedOptions = Arrays.asList(session.getNamedQueries());

		Collection queries = new ArrayList();
		IArtifactManagerSession ams = session.getTigerstripeProject()
				.getArtifactManagerSession();
		IQueryArtifactsByType query = (IQueryArtifactsByType) ams
				.makeQuery(IQueryArtifactsByType.class.getName());
		query.setArtifactType(QueryArtifact.MODEL.getClass().getName());

		queries = ams.queryArtifact(query);

		if (queries == null || queries.size() == 0)
			return new Object[0];

		List result = new ArrayList();

		for (Iterator iter = queries.iterator(); iter.hasNext();) {
			IQueryArtifact entity = (IQueryArtifact) iter.next();

			boolean keepGoing = true;
			for (Iterator iterSelected = selectedOptions.iterator(); iterSelected
					.hasNext()
					&& keepGoing;) {
				INamedQuery opt = (INamedQuery) iterSelected.next();
				if (opt.getFullyQualifiedName().equals(
						entity.getFullyQualifiedName())) {
					keepGoing = false;
				}
			}

			if (keepGoing) {
				INamedQuery details = session.makeNamedQuery();
				details.setFullyQualifiedName(entity.getFullyQualifiedName());
				result.add(details);
			}
		}

		return result.toArray();
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	@Override
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = getViewer().getTable().getSelection();
		INamedQuery[] selectedLabels = new INamedQuery[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (INamedQuery) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message + "these " + selectedLabels.length + " Queries?";
		} else {
			message = message + "this Query?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove Queries", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			getViewer().remove(selectedLabels);

			ISessionArtifact session = (ISessionArtifact) getIArtifactFromEditor();
			session.removeNamedQuery(selectedLabels);
			markPageModified();
		}
		updateForm();
	}
}
