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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.EditorUndoManager;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.MessageListDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

public abstract class ArtifactEditorBase extends TigerstripeFormEditor
		implements IArtifactChangeListener, IActiveFacetChangeListener,
		ITigerstripeChangeListener {

	private boolean ignoreResourceChange = false;

	private IAbstractArtifact artifact;

	private boolean previousPageWasModel = true;

	private int sourcePageIndex = -1;

	private ArtifactSourcePage sourcePage;

	private Collection<TigerstripeFormPage> modelPages = new ArrayList<TigerstripeFormPage>();

	public ArtifactEditorBase() {
		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(
				this, ITigerstripeChangeListener.ALL);
	}

	public IAbstractArtifact getIArtifact() {
		return artifact;
	}

	protected void setIArtifact(IAbstractArtifact artifact) {
		this.artifact = artifact;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {

		if (ignoreResourceChange)
			return;

		IResourceDelta selfDelta = lookforSelf(event.getDelta());

		if (selfDelta != null) {

			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				try {
					updateArtifactFromTextEditor();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
		super.resourceChanged(event);
	}

	@Override
	protected void closeMyself() {
		try {
			if (artifact != null && artifact.getTigerstripeProject() != null) {
				artifact.getTigerstripeProject().getArtifactManagerSession()
						.removeArtifactChangeListener(this);
				artifact.getTigerstripeProject().getArtifactManagerSession()
						.removeArtifactChangeListener(this);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		super.closeMyself();
	}

	@Override
	protected void setInput(IEditorInput input) {
		if (input instanceof FileEditorInput) {
			FileEditorInput fileInput = (FileEditorInput) input;

			if (!fileInput.getFile().getProject().isOpen()) {
				closeMyself();
				dispose();
				return;
			}

			IAbstractArtifact artifact = TSExplorerUtils
					.getArtifactFor(fileInput.getFile());
			setIArtifact(artifact);

			try {
				if (artifact != null
						&& artifact.getTigerstripeProject() != null) {
					artifact.getTigerstripeProject()
							.getArtifactManagerSession()
							.addArtifactChangeListener(this);
					artifact.getTigerstripeProject()
							.getArtifactManagerSession()
							.addActiveFacetListener(this);
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		} else if (input instanceof ReadOnlyArtifactEditorInput) {
			ReadOnlyArtifactEditorInput roInput = (ReadOnlyArtifactEditorInput) input;
			setIArtifact(roInput.getArtifact());
			// there won't be any change to listen for since this is readonly
		}

		super.setInput(input);
	}

	private void updateTitle() {

		IAbstractArtifact artifact = getIArtifact();
		if (artifact != null) {
			setPartName(artifact.getName());
			AbstractArtifactLabelProvider prov = new AbstractArtifactLabelProvider();
			setTitleImage(prov.getImage(artifact));
		} else {
			FileEditorInput input = (FileEditorInput) getEditorInput();
			setPartName(input.getFile().getName());
		}
	}

	@Override
	protected void addPages() {
		try {
			addSourcePage();
		} catch (PartInitException e) {
			TigerstripeRuntime.logErrorMessage("PartInitException detected", e);
		}
		updateTitle();
	}

	protected void addSourcePage() throws PartInitException {

		if (!getIArtifact().isReadonly()) {
			sourcePage = new ArtifactSourcePage(this, "id", "Source");

			sourcePageIndex = addPage(sourcePage, getEditorInput());
			setPageText(sourcePageIndex, "Source");
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		getUndoManager().editorSaved();

		if (getActivePage() != sourcePageIndex) {
			updateTextEditorFromArtifact();
		} else {
			try {
				updateArtifactFromTextEditor();
			} catch (TigerstripeException ee) {
				Status status = new Status(IStatus.WARNING, EclipsePlugin
						.getPluginId(), 111, "Unexpected Exception", ee);
				EclipsePlugin.log(status);
			}
		}
		monitor
				.beginTask("Saving " + getIArtifact().getFullyQualifiedName(),
						1);
		// check for errors, if errors are found they will be displayed
		IStatus errorList = getIArtifact().validate();
		if (!errorList.isOK()) {
			if (errorList.matches(IStatus.ERROR)) {
				// display error list and exit without saving
				MessageListDialog dialog = new MessageListDialog(getContainer()
						.getShell(), errorList, "Save Failed: Invalid Artifact");
				dialog.create();
				dialog.disableOKButton();
				dialog.open();
				return;
			}
			// display warning/info list and save when user clicks on "OK"
			// button
			MessageListDialog dialog = new MessageListDialog(getContainer()
					.getShell(), errorList,
					"Warning: non-fatal errors with Artifact");
			int returnCode = dialog.open();
			if (returnCode != Window.OK)
				return;
		}

		// We let Eclipse do the save to the file.
		setIgnoreResourceChange(true); // ignore the resource change notif here
		getEditor(sourcePageIndex).doSave(monitor);
		setIgnoreResourceChange(false);

		// Bug 1027: At this stage we need to update the content of the Artifact
		// Mgr
		// so that the change in the Artifact is broadcast and the POJO state
		// is up2date to avoid a re-parse in the next refresh() of the Mgr
		// This is using a back-door ugly mechanism, but it avoids much trouble
		AbstractArtifact aArt = (AbstractArtifact) getIArtifact();
		ArtifactManager mgr = aArt.getArtifactManager();

		mgr.removeArtifactManagerListener(this);
		mgr.notifyArtifactSaved(aArt, monitor);
		mgr.addArtifactManagerListener(this);

		monitor.done();
	}

	@Override
	public void pageChange(int newPageIndex) {
		if (newPageIndex == sourcePageIndex) {
			if (isPageModified)
				updateTextEditorFromArtifact();
			previousPageWasModel = false;
		} else {
			if (!previousPageWasModel && isDirty()) {
				try {
					updateArtifactFromTextEditor();
				} catch (TigerstripeException ee) {
					Status status = new Status(IStatus.WARNING, EclipsePlugin
							.getPluginId(), 111, "Unexpected Exception", ee);
					EclipsePlugin.log(status);
				}
			}
			previousPageWasModel = true;
		}
		super.pageChange(newPageIndex);
	}

	private void updateTextEditorFromArtifact() {
		FileEditorInput input = (FileEditorInput) sourcePage.getEditorInput();
		IAbstractArtifact artifact = getIArtifact();
		try {
			sourcePage.getDocumentProvider().getDocument(input).set(
					artifact.asText());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void updateArtifactFromTextEditor() throws TigerstripeException {
		FileEditorInput input = (FileEditorInput) sourcePage.getEditorInput();
		IAbstractArtifact originalArtifact = getIArtifact();

		ITigerstripeModelProject project = originalArtifact
				.getTigerstripeProject();
		IArtifactManagerSession session = project.getArtifactManagerSession();

		if (sourcePage.getDocumentProvider().getDocument(input) != null) { // Bug
			// 810
			String text = sourcePage.getDocumentProvider().getDocument(input)
					.get();
			StringReader reader = new StringReader(text);
			try {
				IAbstractArtifact newArtifact = session.extractArtifact(reader,
						new NullProgressMonitor());
				setIArtifact(newArtifact);
				refreshModelPages();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	private boolean isPageModified;

	public void pageModified() {
		isPageModified = true;
		if (!super.isDirty())
			firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	protected void handlePropertyChange(int propertyId) {
		if (propertyId == IEditorPart.PROP_DIRTY)
			isPageModified = super.isDirty();
		super.handlePropertyChange(propertyId);
	}

	@Override
	public boolean isDirty() {
		return isPageModified || super.isDirty();
	}

	protected void addModelPage(TigerstripeFormPage page) {
		modelPages.add(page);
	}

	private void refreshModelPages() {
		for (Iterator<TigerstripeFormPage> iter = modelPages.iterator(); iter
				.hasNext();) {
			TigerstripeFormPage page = iter.next();
			page.refresh();
		}
	}

	public void artifactAdded(IAbstractArtifact artifact) {
		// Nothing to do here.
	}

	public void artifactChanged(IAbstractArtifact artifact) {
		IAbstractArtifact myArtifact = getIArtifact();
		if (myArtifact != null
				&& myArtifact.getFullyQualifiedName().equals(
						artifact.getFullyQualifiedName())) {
			setIArtifact(artifact);
			refreshModelPages();
			updateTextEditorFromArtifact();
		}
	}

	@Override
	public void dispose() {
		try {
			if (artifact != null && artifact.getTigerstripeProject() != null) {
				artifact.getTigerstripeProject().getArtifactManagerSession()
						.removeArtifactChangeListener(this);
			}

			TigerstripeWorkspaceNotifier.INSTANCE
					.removeTigerstripeChangeListener(this);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		super.dispose();
	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		// TODO Auto-generated method stub

	}

	public void managerReloaded() {
		// TODO Auto-generated method stub

	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		// TODO Auto-generated method stub

	}

	public void facetChanged(IFacetReference oldFacet, IFacetReference newFacet) {
		AbstractArtifactLabelProvider prov = new AbstractArtifactLabelProvider();
		setTitleImage(prov.getImage(getIArtifact()));
	}

	private void setIgnoreResourceChange(boolean ignoreResourceChange) {
		this.ignoreResourceChange = ignoreResourceChange;
	}

	@Override
	protected EditorUndoManager createUndoManager() {
		return new ArtifactEditorUndoManager(this);
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		AbstractArtifactLabelProvider prov = new AbstractArtifactLabelProvider();
		setTitleImage(prov.getImage(getIArtifact()));
	}

	public void modelChanged(IModelChangeDelta[] delta) {
		// TODO Auto-generated method stub

	}

	public void projectAdded(IAbstractTigerstripeProject project) {
		// TODO Auto-generated method stub

	}

	public void projectDeleted(String projectName) {
		// TODO Auto-generated method stub

	}

}