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
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjExceptionSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.ExceptionArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ide.IDE;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 */
public class ExceptionArtifact extends AbstractArtifact implements
		IExceptionArtifact, IAbstractArtifactInternal {

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.EXCEPTION;

	public String getArtifactType() {
		return IExceptionArtifact.class.getName();
	}

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static ExceptionArtifact MODEL = new ExceptionArtifact(null);

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	@Override
	public String getMarkingTag() {
		return ExceptionArtifact.MARKING_TAG;
	}

	@Override
	public IAbstractArtifactInternal getModel() {
		return MODEL;
	}

	public String getLabel() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
				IExceptionArtifactImpl.class.getName()).getLabel(this);
	}

	@Override
	public IAbstractArtifactInternal extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		ExceptionArtifact result = new ExceptionArtifact(javaClass,
				artifactMgr, monitor);

		return result;
	}

	public ExceptionArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		OssjExceptionSpecifics specifics = new OssjExceptionSpecifics(this);
		setIStandardSpecifics(specifics);
	}

	public ExceptionArtifact(JavaClass javaClass, ArtifactManager artifactMgr,
			IProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);
		OssjExceptionSpecifics specifics = new OssjExceptionSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new ExceptionArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new ExceptionArtifact(getArtifactManager());
	}

	@Override
	public boolean isReadonly() {
		return Exception.class.getName().equals(getFullyQualifiedName())
				|| super.isReadonly();
	}

	/**
	 * Returns true if this artifact extends another artifact
	 * 
	 * @return true if this artifacts extends another artifact, false otherwise.
	 *         for exception, everything is an extension of java.lang.exception,
	 *         so we have to ignore that
	 * 
	 */
	@Override
	public boolean hasExtends() {
		if (getExtends() == null)
			return false;
		else if (getExtends().getFullyQualifiedName().equals(
				"java.lang.Exception"))
			return false;
		else
			return true;

	}

	@Override
	public void doSave(IProgressMonitor monitor) throws TigerstripeException {
		super.doSave(monitor);

		IResource resource = (IResource) this.getAdapter(IResource.class);
		final String EDITOR_ID = "org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.exception.ExceptionArtifactEditor";
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			IEditorDescriptor editorDescriptor = IDE.getDefaultEditor(file);
			if (editorDescriptor == null
					|| (!EDITOR_ID.equals(editorDescriptor)))
				IDE.setDefaultEditor((IFile) resource, EDITOR_ID);
		}
	}
}