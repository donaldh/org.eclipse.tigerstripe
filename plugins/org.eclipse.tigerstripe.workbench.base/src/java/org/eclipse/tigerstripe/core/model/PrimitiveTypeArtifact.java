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

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IField;
import org.eclipse.tigerstripe.api.model.ILabel;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.model.ossj.PrimitiveArtifactPersister;
import org.eclipse.tigerstripe.core.model.persist.AbstractArtifactPersister;

import com.thoughtworks.qdox.model.JavaClass;

public class PrimitiveTypeArtifact extends AbstractArtifact implements
		IPrimitiveTypeArtifact {

	/** The marking tag for this Artifact */
	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.PRIMITIVE;

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static PrimitiveTypeArtifact MODEL = new PrimitiveTypeArtifact(
			null);

	public final static String LABEL = "Primitive Type Artifact";

	public PrimitiveTypeArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		setIStandardSpecifics(new StandardSpecifics(this));
	}

	public PrimitiveTypeArtifact(JavaClass javaClass,
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);
		StandardSpecifics specifics = new StandardSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
		PrimitiveTypeArtifact result = new PrimitiveTypeArtifact(javaClass,
				artifactMgr, monitor);
		return result;
	}

	@Override
	public String getMarkingTag() {
		return MARKING_TAG;
	}

	@Override
	public AbstractArtifact getModel() {
		return MODEL;
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new PrimitiveArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new PrimitiveTypeArtifact(getArtifactManager());
	}

	public String getLabel() {
		return LABEL;
	}

	public String getArtifactType() {
		return IPrimitiveTypeArtifact.class.getName();
	}

	public String getIArtifactType() {
		return IPrimitiveTypeArtifact.class.getName();
	}

	@Override
	public IField[] getIFields() {
		return new IField[0];
	}

	@Override
	public ILabel[] getILabels() {
		return new ILabel[0];
	}

	@Override
	public IMethod[] getIMethods() {
		return new IMethod[0];
	}

	@Override
	public void resolveReferences(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {
		super.resolveReferences(monitor);

		// Validation needs to verify that this is not one of the reserved
		// types.
		for (String[] rType : reservedPrimitiveTypes) {
			String type = rType[0];
			if (type.equals(getName()))
				throw new TigerstripeException("Invalid primitive type, '"
						+ type + "' is a reserved type.");
		}
	}

}
