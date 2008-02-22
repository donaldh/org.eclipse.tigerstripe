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
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.DatatypeArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjDatatypeSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;

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

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static DatatypeArtifact MODEL = new DatatypeArtifact(null);

	public String getArtifactType() {
		return IDatatypeArtifact.class.getName();
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
		return ArtifactMetadataFactory.INSTANCE
				.getMetadata(
						org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl.class
								.getName()).getLabel();
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
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
			ArtifactManager artifactManager, IProgressMonitor monitor) {
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
	public IMethod makeMethod() {
		IMethod result = super.makeMethod();
		result.setInstanceMethod(false); // datatype can't have instance
		// methods
		return result;
	}
}