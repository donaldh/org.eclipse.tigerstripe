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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.UpdateProcedureArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjUpdateProcedureSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class UpdateProcedureArtifact extends AbstractArtifact implements
		IUpdateProcedureArtifact {

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.UPDATEPROC;

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static UpdateProcedureArtifact MODEL = new UpdateProcedureArtifact(
			null);

	public String getArtifactType() {
		return IUpdateProcedureArtifact.class.getName();
	}

	@Override
	public String getMarkingTag() {
		return UpdateProcedureArtifact.MARKING_TAG;
	}

	@Override
	public AbstractArtifact getModel() {
		return MODEL;
	}

	public String getLabel() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
				IUpdateProcedureArtifactImpl.class.getName()).getLabel();
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		UpdateProcedureArtifact result = new UpdateProcedureArtifact(javaClass,
				artifactMgr, monitor);

		return result;
	}

	public UpdateProcedureArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		OssjUpdateProcedureSpecifics specifics = new OssjUpdateProcedureSpecifics(
				this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	public UpdateProcedureArtifact(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);
		OssjUpdateProcedureSpecifics specifics = new OssjUpdateProcedureSpecifics(
				this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new UpdateProcedureArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new UpdateProcedureArtifact(getArtifactManager());
	}
}