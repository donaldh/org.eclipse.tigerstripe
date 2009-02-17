/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.annotation.AnnotationPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.artifactMetadata.ArtifactMetadataPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.audit.AuditPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.decorator.DecoratorPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.naming.NamingPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.patterns.PatternPage;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;

public class ConfigEditor extends TigerstripeFormEditor {

	private boolean ignoreResourceChange = false;

	private ISDKProvider provider;

	
	public ConfigEditor() {
	}

	public ISDKProvider getIProvider() {
		return provider;
	}

	protected void setIProvider(ISDKProvider provider) {
		if (provider == null)
			System.out.println("arggg Null");
		this.provider = provider;
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
	protected void setInput(IEditorInput input) {
		if (input instanceof SDKEditorInput) {
			ISDKProvider provider = ((SDKEditorInput) input).getProvider();
			setIProvider(provider);
			
		}

		super.setInput(input);
	}

	@Override
	protected void addPages() {
		int index = -1;
		try {
			//ConfigOverviewPage page = new ConfigOverviewPage(this);
			//addPage(page);
			AuditPage auditPage = new AuditPage(this);
			index =  addPage(auditPage);
			DecoratorPage dPage = new DecoratorPage(this);
			addPage(dPage);
			NamingPage nPage = new NamingPage(this);
			addPage(nPage);
			ArtifactMetadataPage artifactMetadataPage  = new ArtifactMetadataPage(this);
			addPage(artifactMetadataPage);
			PatternPage patternsPage  = new PatternPage(this);
			addPage(patternsPage);
			AnnotationPage annotationsPage  = new AnnotationPage(this);
			addPage(annotationsPage);

			
		} catch (PartInitException e) {
			EclipsePlugin.log(e);
		}

		setActivePage(index);
	}


	@Override
	public void doSave(IProgressMonitor monitor) {

		getUndoManager().editorSaved();

		//if (getActivePage() != sourcePageIndex) {
			updateTextEditorFromArtifact();
//		} else {
//			try {
//				updateArtifactFromTextEditor();
//			} catch (TigerstripeException ee) {
//				Status status = new Status(IStatus.WARNING, EclipsePlugin
//						.getPluginId(), 111, "Unexpected Exception", ee);
//				EclipsePlugin.log(status);
//			}
//		}
//		monitor
//				.beginTask("Saving " + getIProvider().getFullyQualifiedName(),
//						1);
//		// check for errors, if errors are found they will be displayed
//		IStatus errorList = getIProvider().validate();
//		if (!errorList.isOK()) {
//			if (errorList.matches(IStatus.ERROR)) {
//				// display error list and exit without saving
//				MessageListDialog dialog = new MessageListDialog(getContainer()
//						.getShell(), errorList, "Save Failed: Invalid Artifact");
//				dialog.create();
//				dialog.disableOKButton();
//				dialog.open();
//				return;
//			}
//			// display warning/info list and save when user clicks on "OK"
//			// button
//			MessageListDialog dialog = new MessageListDialog(getContainer()
//					.getShell(), errorList,
//					"Warning: non-fatal errors with Artifact");
//			int returnCode = dialog.open();
//			if (returnCode != Window.OK)
//				return;
//		}

		// We let Eclipse do the save to the file.
		setIgnoreResourceChange(true); // ignore the resource change notif here
		//getEditor(sourcePageIndex).doSave(monitor);
		setIgnoreResourceChange(false);

		// Bug 1027: At this stage we need to update the content of the Artifact
		// Mgr
		// so that the change in the Artifact is broadcast and the POJO state
		// is up2date to avoid a re-parse in the next refresh() of the Mgr
		// This is using a back-door ugly mechanism, but it avoids much trouble
//		AbstractArtifact aArt = (AbstractArtifact) getIArtifact();
//		ArtifactManager mgr = aArt.getArtifactManager();
//
//		mgr.removeArtifactManagerListener(this);
//		mgr.notifyArtifactSaved(aArt, monitor);
//		mgr.addArtifactManagerListener(this);

		monitor.done();
	}



	private void updateTextEditorFromArtifact() {
//		FileEditorInput input = (FileEditorInput) sourcePage.getEditorInput();
//		IAbstractArtifact artifact = getIArtifact();
//		try {
//			sourcePage.getDocumentProvider().getDocument(input).set(
//					artifact.asText());
//		} catch (TigerstripeException e) {
//			EclipsePlugin.log(e);
//		}
	}

	private void updateArtifactFromTextEditor() throws TigerstripeException {
//		FileEditorInput input = (FileEditorInput) sourcePage.getEditorInput();
//		IAbstractArtifact originalArtifact = getIArtifact();
//
//		ITigerstripeModelProject project = originalArtifact
//				.getTigerstripeProject();
//		IArtifactManagerSession session = project.getArtifactManagerSession();
//
//		if (sourcePage.getDocumentProvider().getDocument(input) != null) { // Bug
//			// 810
//			String text = sourcePage.getDocumentProvider().getDocument(input)
//					.get();
//			StringReader reader = new StringReader(text);
//			try {
//				IAbstractArtifact newArtifact = session.extractArtifact(reader,
//						new NullProgressMonitor());
//				setIArtifact(newArtifact);
//				refreshModelPages();
//			} catch (TigerstripeException e) {
//				EclipsePlugin.log(e);
//			}
//		}
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

	
	private void setIgnoreResourceChange(boolean ignoreResourceChange) {
		this.ignoreResourceChange = ignoreResourceChange;
	}

}