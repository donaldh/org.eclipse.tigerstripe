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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.session;

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
import org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IOssjArtifactFormLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.ArtifactSelectorDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.IArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class OssjSessionNotificationsSection extends OssjSessionElementsSection {

	public class NotificationsContentProvider implements
			IStructuredContentProvider {

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ISessionArtifact) {
				ISessionArtifact session = (ISessionArtifact) inputElement;
				Collection<IEmittedEvent> events = session.getEmittedEvents();
				return events.toArray();
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
			return Images.get(Images.NOTIFICATION_ICON);
		}

		public String getText(Object element) {
			if (element instanceof IEmittedEvent) {
				IEmittedEvent details = (IEmittedEvent) element;
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

	private NotificationsContentProvider contentProvider;
	private MEntitiesLabelProvider labelProvider;

	public OssjSessionNotificationsSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider);
		this.contentProvider = new NotificationsContentProvider();
		this.labelProvider = new MEntitiesLabelProvider();
		setTitle("Emitted Notifications");
	}

	@Override
	protected IStructuredContentProvider getElementsContentProvider() {
		if (contentProvider == null) {
			contentProvider = new NotificationsContentProvider();
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

		elsd.setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IEventArtifactImpl.class.getName()).getLabel() + " Artifacts");
		elsd
				.setMessage("Select a set of "  +ArtifactMetadataFactory.INSTANCE.getMetadata(
						IEventArtifactImpl.class.getName()).getLabel() + "(s) to be emitted through this session.");

		ISessionArtifact session = (ISessionArtifact) getIArtifactFromEditor();
		Object[] availableEntityOptions = getAvailableNotificationsList();
		elsd.setElements(availableEntityOptions);

		if (elsd.open() == Window.OK) {
			Object[] details = elsd.getResult();
			for (int i = 0; i < details.length; i++) {
				session.addEmittedEvent((IEmittedEvent) details[i]);
				getViewer().add(details[i]);
				markPageModified();
			}
		}
	}

	private Object[] getAvailableNotificationsList()
			throws TigerstripeException {

		// Get the already selected entities, so the user won't be presented
		// with
		// already selected entities.
		ISessionArtifact session = (ISessionArtifact) getIArtifactFromEditor();
		Collection<IEmittedEvent> selectedOptions = session.getEmittedEvents();

		Collection queries = new ArrayList();
		IArtifactManagerSession ams = session.getTigerstripeProject()
				.getArtifactManagerSession();
		IQueryArtifactsByType query = (IQueryArtifactsByType) ams
				.makeQuery(IQueryArtifactsByType.class.getName());
		query.setArtifactType(EventArtifact.MODEL.getClass().getName());

		queries = ams.queryArtifact(query);

		if (queries == null || queries.size() == 0)
			return new Object[0];

		List result = new ArrayList();

		for (Iterator iter = queries.iterator(); iter.hasNext();) {
			IEventArtifact entity = (IEventArtifact) iter.next();

			boolean keepGoing = true;
			for (Iterator iterSelected = selectedOptions.iterator(); iterSelected
					.hasNext()
					&& keepGoing;) {
				IEmittedEvent opt = (IEmittedEvent) iterSelected.next();
				if (opt.getFullyQualifiedName().equals(
						entity.getFullyQualifiedName())) {
					keepGoing = false;
				}
			}

			if (keepGoing) {
				IEmittedEvent details = session.makeEmittedEvent();
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
		IEmittedEvent[] selectedLabels = new IEmittedEvent[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (IEmittedEvent) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message + "these " + selectedLabels.length
					+ " Notifications?";
		} else {
			message = message + "this Notification?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove Notifications", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			getViewer().remove(selectedLabels);

			ISessionArtifact session = (ISessionArtifact) getIArtifactFromEditor();
			session.removeEmittedEvent(selectedLabels);
			markPageModified();
		}
		updateForm();
	}
}
