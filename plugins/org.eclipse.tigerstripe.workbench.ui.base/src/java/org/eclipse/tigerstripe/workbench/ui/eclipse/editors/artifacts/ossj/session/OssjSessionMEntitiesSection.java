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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.ManagedEntityOverideDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.ArtifactSelectorDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.IArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjSessionMEntitiesSection extends OssjSessionElementsSection {

	public class MEntitiesContentProvider implements IStructuredContentProvider {

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ISessionArtifact) {
				ISessionArtifact session = (ISessionArtifact) inputElement;
				Collection details = session.getManagedEntityDetails();
				return details.toArray();
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
			return Images.get(Images.ENTITY_ICON);
		}

		public String getText(Object element) {
			if (element instanceof IManagedEntityDetails) {
				IManagedEntityDetails details = (IManagedEntityDetails) element;
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

	private MEntitiesContentProvider contentProvider;

	private MEntitiesLabelProvider labelProvider;

	public OssjSessionMEntitiesSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider);
		this.contentProvider = new MEntitiesContentProvider();
		this.labelProvider = new MEntitiesLabelProvider();
		setTitle("Managed Entities");
	}

	@Override
	protected IStructuredContentProvider getElementsContentProvider() {
		if (contentProvider == null) {
			contentProvider = new MEntitiesContentProvider();
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

	protected void overideButtonSelected(SelectionEvent event)
			throws TigerstripeException {
		TableItem[] selectedItems = getViewer().getTable().getSelection();
		IManagedEntityDetails selectedManagedEntity = (IManagedEntityDetails) selectedItems[0]
				.getData();

		IManagedEntityDetails clonedSelection = selectedManagedEntity.clone();

		ManagedEntityOverideDialog dialog = new ManagedEntityOverideDialog(
				getPage().getManagedForm().getForm().getShell(),
				clonedSelection, (ISessionArtifact) getIArtifact());

		// before the dialog can be opened, the project must be build so that
		// the
		// managed entity can be resolved to an artifact.
		if (!clonedSelection.isResolvedToArtifact()) {
			ErrorDialog eDialog = new ErrorDialog(
					getPage().getManagedForm().getForm().getShell(),
					"Please wait",
					"This project has no been successfully audited yet. Please wait.",
					new Status(IStatus.INFO,
							TigerstripePluginConstants.PLUGIN_ID, 222,
							"Waiting for project audit", null), 0);
			eDialog.open();
		} else {
			if (dialog.open() == Window.OK) {
				// Apply changes
				ISessionArtifact sessionArt = (ISessionArtifact) getIArtifact();
				sessionArt.addManagedEntityDetails(clonedSelection); // This
				// will
				// replace
				// the
				// existing
				// one
				markPageModified();
			}
			updateForm();
		}
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
				IManagedEntityArtifactImpl.class.getName()).getLabel()
				+ " Artifacts");
		elsd.setMessage("Select a set of "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IManagedEntityArtifactImpl.class.getName()).getLabel()
				+ "(s) to be managed through this session.");

		ISessionArtifact session = (ISessionArtifact) getIArtifactFromEditor();
		Object[] availableEntityOptions = getAvailableEntityOptionsList();
		elsd.setElements(availableEntityOptions);

		if (elsd.open() == Window.OK) {
			Object[] details = elsd.getResult();
			for (int i = 0; i < details.length; i++) {
				session
						.addManagedEntityDetails((IManagedEntityDetails) details[i]);
				getViewer().add(details[i]);
				markPageModified();
			}
			getPage().refresh();
		}
	}

	private Object[] getAvailableEntityOptionsList()
			throws TigerstripeException {

		// Get the already selected entities, so the user won't be presented
		// with
		// already selected entities.
		ISessionArtifact session = (ISessionArtifact) getIArtifactFromEditor();
		List selectedOptions = Arrays.asList(session.getManagedEntityDetails());

		Collection entities = new ArrayList();
		IArtifactManagerSession ams = session.getTigerstripeProject()
				.getArtifactManagerSession();
		IQueryArtifactsByType query = (IQueryArtifactsByType) ams
				.makeQuery(IQueryArtifactsByType.class.getName());
		query.setArtifactType(ManagedEntityArtifact.MODEL.getClass().getName());

		entities = ams.queryArtifact(query);

		if (entities == null || entities.size() == 0)
			return new Object[0];

		List result = new ArrayList();

		for (Iterator iter = entities.iterator(); iter.hasNext();) {
			IManagedEntityArtifact entity = (IManagedEntityArtifact) iter
					.next();

			boolean keepGoing = true;
			for (Iterator iterSelected = selectedOptions.iterator(); iterSelected
					.hasNext()
					&& keepGoing;) {
				IManagedEntityDetails opt = (IManagedEntityDetails) iterSelected
						.next();
				if (opt.getFullyQualifiedName().equals(
						entity.getFullyQualifiedName())) {
					keepGoing = false;
				}
			}

			if (keepGoing) {
				IManagedEntityDetails details = session
						.makeManagedEntityDetails();
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
		IManagedEntityDetails[] selectedLabels = new IManagedEntityDetails[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (IManagedEntityDetails) selectedItems[i]
					.getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message
					+ "these "
					+ selectedLabels.length
					+ " "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IManagedEntityArtifactImpl.class.getName())
							.getLabel() + "(s)?";
		} else {
			message = message
					+ "this "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IManagedEntityArtifactImpl.class.getName())
							.getLabel() + "?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove "
						+ ArtifactMetadataFactory.INSTANCE.getMetadata(
								IManagedEntityArtifactImpl.class.getName())
								.getLabel() + "(s)", null, message,
				MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			getViewer().remove(selectedLabels);

			ISessionArtifact session = (ISessionArtifact) getIArtifactFromEditor();
			session.removeManagedEntityDetails(selectedLabels);
			markPageModified();
		}
		updateForm();
	}

	private Button overideButton;

	@Override
	protected void createInterfaceName(Composite parent, FormToolkit toolkit) {
		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.numColumns = 2;
		parent.setLayout(twlayout);

		Table t = toolkit.createTable(parent, SWT.NULL);
		t.setEnabled(!isReadonly());
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 3;
		td.heightHint = 140;
		t.setLayoutData(td);

		addElementButton = toolkit.createButton(parent, "Add", SWT.PUSH);
		addElementButton.setEnabled(!isReadonly());
		addElementButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		addElementButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				try {
					addButtonSelected(event);
				} catch (TigerstripeException e) {
					Status status = new Status(IStatus.WARNING,
							TigerstripePluginConstants.PLUGIN_ID, 111,
							"Unexpected exception.", e);
					EclipsePlugin.log(status);
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		overideButton = toolkit.createButton(parent, "Overide", SWT.PUSH);
		overideButton.setEnabled(!isReadonly());
		overideButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		overideButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				try {
					overideButtonSelected(event);
				} catch (TigerstripeException e) {
					Status status = new Status(IStatus.WARNING,
							TigerstripePluginConstants.PLUGIN_ID, 111,
							"Unexpected exception.", e);
					EclipsePlugin.log(status);
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		removeElementButton = toolkit.createButton(parent, "Remove", SWT.PUSH);
		removeElementButton.setLayoutData(new TableWrapData());
		removeElementButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});
		removeElementButton.setEnabled(false);

		viewer = new TableViewer(t);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				viewerSelectionChanged(event);
			}
		});
		viewer.setContentProvider(getElementsContentProvider());
		viewer.setLabelProvider(getElementsLabelProvider());
		viewer.setInput(((ArtifactEditorBase) getPage().getEditor())
				.getIArtifact());

	}

	@Override
	protected void updateForm() {

		super.updateForm();

		setSilentUpdate(true);

		if (viewer.getSelection().isEmpty()) {
			overideButton.setEnabled(false);
		} else {
			overideButton.setEnabled(!isReadonly());
		}

		setSilentUpdate(false);
	}

}
