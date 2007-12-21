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
package org.eclipse.tigerstripe.core.model;

import java.io.Writer;

import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IMethod;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextDatatypeArtifact;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.model.ossj.DatatypeArtifactPersister;
import org.eclipse.tigerstripe.core.model.ossj.specifics.OssjDatatypeSpecifics;
import org.eclipse.tigerstripe.core.model.persist.AbstractArtifactPersister;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DatatypeArtifact extends AbstractArtifact implements
		IDatatypeArtifact {

	/** The marking tag for this Artifact */
	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.DATATYPE;

	public final static String LABEL = "Datatype Artifact";

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static DatatypeArtifact MODEL = new DatatypeArtifact(null);

	public String getArtifactType() {
		return IDatatypeArtifact.class.getName();
	}

	public String getIArtifactType() {
		return IextDatatypeArtifact.class.getName();
	}

	/**
	 * Returns the marking tag for this Artifact.
	 * 
	 * @return String - the marking tag for this artifact
	 */
	@Override
	public String getMarkingTag() {
		return DatatypeArtifact.MARKING_TAG;
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
		// validate( javaClass );
		DatatypeArtifact result = new DatatypeArtifact(javaClass, artifactMgr,
				monitor);
		return result;
	}

	public DatatypeArtifact(ArtifactManager artifactManager) {
		super(artifactManager);
		OssjDatatypeSpecifics specifics = new OssjDatatypeSpecifics(this);
		setIStandardSpecifics(specifics);
	}

	public DatatypeArtifact(JavaClass javaClass,
			ArtifactManager artifactManager, ITigerstripeProgressMonitor monitor) {
		super(javaClass, artifactManager, monitor);
		OssjDatatypeSpecifics specifics = new OssjDatatypeSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new DatatypeArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new DatatypeArtifact(getArtifactManager());
	}

	@Override
	public IMethod makeIMethod() {
		IMethod result = super.makeIMethod();
		result.setInstanceMethod(false); // datatype can't have instance
		// methods
		return result;
	}
}