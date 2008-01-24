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

import org.apache.log4j.Logger;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.ExceptionArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjExceptionSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IExceptionArtifact;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 */
public class ExceptionArtifact extends AbstractArtifact implements
		IExceptionArtifact {

	/** logger for output */
	private static Logger log = Logger.getLogger(ExceptionArtifact.class);

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.EXCEPTION;

	public final static String LABEL = "Exception Artifact";

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
	public AbstractArtifact getModel() {
		return MODEL;
	}

	public String getLabel() {
		return LABEL;
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
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
			ITigerstripeProgressMonitor monitor) {
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
}