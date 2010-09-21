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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.io.Writer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjEventSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.EventArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ide.IDE;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 */
public class EventArtifact extends AbstractArtifact implements IEventArtifact {

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.EVENT;

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static EventArtifact MODEL = new EventArtifact(null);

	public String getArtifactType() {
		return IEventArtifact.class.getName();
	}

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	@Override
	public String getMarkingTag() {
		return EventArtifact.MARKING_TAG;
	}

	@Override
	public AbstractArtifact getModel() {
		return MODEL;
	}

	public String getLabel() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
				IEventArtifactImpl.class.getName()).getLabel(this);
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		EventArtifact result = new EventArtifact(javaClass, artifactMgr,
				monitor);

		return result;
	}

	public EventArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		OssjEventSpecifics specifics = new OssjEventSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	public EventArtifact(JavaClass javaClass, ArtifactManager artifactMgr,
			IProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);
		OssjEventSpecifics specifics = new OssjEventSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new EventArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new EventArtifact(getArtifactManager());
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) throws TigerstripeException {
		super.doSave(monitor);
		
		IResource resource = (IResource) this.getAdapter(IResource.class);
		final String EDITOR_ID = "org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.event.EventArtifactEditor";
		if (resource instanceof IFile ) {
			IFile file = (IFile) resource;
			IEditorDescriptor editorDescriptor = IDE.getDefaultEditor(file);
			if (editorDescriptor==null || (!EDITOR_ID.equals(editorDescriptor))) 
				IDE.setDefaultEditor((IFile)resource, EDITOR_ID);
		}
	}

}