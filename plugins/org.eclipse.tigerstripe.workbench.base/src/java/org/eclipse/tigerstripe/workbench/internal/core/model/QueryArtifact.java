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
import org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjQuerySpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.QueryArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ide.IDE;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 */
public class QueryArtifact extends AbstractArtifact implements IQueryArtifact,
		IAbstractArtifactInternal {

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.QUERY;

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static QueryArtifact MODEL = new QueryArtifact(null);

	public String getArtifactType() {
		return IQueryArtifact.class.getName();
	}

	@Override
	public String getMarkingTag() {
		return QueryArtifact.MARKING_TAG;
	}

	@Override
	public IAbstractArtifactInternal getModel() {
		return MODEL;
	}

	public String getLabel() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
				IQueryArtifactImpl.class.getName()).getLabel(this);
	}

	@Override
	public IAbstractArtifactInternal extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		QueryArtifact result = new QueryArtifact(javaClass, artifactMgr,
				monitor);

		return result;
	}

	@ProvideModelComponents
	public IType getReturnedType() {
		return getReturnedEntityType();
	}

	public IType makeType() {
		return new Type(getArtifactManager());
	}

	public void setReturnedType(IType type) {
		setReturnedEntityType((Type) type);
	}

	public Type getReturnedEntityType() {
		OssjQuerySpecifics specifics = (OssjQuerySpecifics) getIStandardSpecifics();
		return (Type) specifics.getReturnedEntityIType();
	}

	protected void setReturnedEntityType(Type type) {
		OssjQuerySpecifics specifics = (OssjQuerySpecifics) getIStandardSpecifics();
		specifics.setReturnedEntityIType(type);
	}

	public QueryArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		OssjQuerySpecifics specifics = new OssjQuerySpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	public QueryArtifact(JavaClass javaClass, ArtifactManager artifactMgr,
			IProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);
		OssjQuerySpecifics specifics = new OssjQuerySpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new QueryArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new QueryArtifact(getArtifactManager());
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) throws TigerstripeException {
		super.doSave(monitor);
		
		IResource resource = (IResource) this.getAdapter(IResource.class);
		final String EDITOR_ID = "org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.query.QueryArtifactEditor";
		if (resource instanceof IFile ) {
			IFile file = (IFile) resource;
			IEditorDescriptor editorDescriptor = IDE.getDefaultEditor(file);
			if (editorDescriptor==null || (!EDITOR_ID.equals(editorDescriptor))) 
				IDE.setDefaultEditor((IFile)resource, EDITOR_ID);
		}
	}
}