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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.AssociationClassArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;

import com.thoughtworks.qdox.model.JavaClass;

public class AssociationClassArtifact extends AssociationArtifact implements
		IAssociationClassArtifact {

	/** The marking tag for this Artifact */
	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.ASSOCIATIONCLASS;

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static AssociationClassArtifact MODEL = new AssociationClassArtifact(
			null);

	public final static String LABEL = "Association Class Artifact";

	public AssociationClassArtifact(ArtifactManager artifactManager) {
		super(artifactManager);
		setIStandardSpecifics(new StandardSpecifics(this));
	}

	public AssociationClassArtifact(JavaClass javaClass,
			ArtifactManager artifactManager, ITigerstripeProgressMonitor monitor) {
		super(javaClass, artifactManager, monitor);
		StandardSpecifics specifics = new StandardSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
		AssociationClassArtifact result = new AssociationClassArtifact(
				javaClass, artifactMgr, monitor);
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
		return new AssociationClassArtifactPersister(this, writer);
	}

	@Override
	protected void buildModel(JavaClass clazz,
			ITigerstripeProgressMonitor monitor) {
		super.buildModel(clazz, monitor);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new AssociationClassArtifact(getArtifactManager());
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public String getArtifactType() {
		return IAssociationClassArtifact.class.getName();
	}


	@Override
	public Collection<IMethod> getMethods() {
		return Collections.unmodifiableCollection(methods);
	}	

	
	@Override
	public Collection<IField> getFields() {
		return Collections.unmodifiableCollection(fields);
	}
		
	
	@Override
	public Object[] getChildren() {
		ArrayList<Object> result = new ArrayList<Object>();

		for (IMethod method : getMethods()) {
			result.add(method);
		}

		for (IField field : getFields()) {
			result.add(field);
		}
		result.add(getAEnd());
		result.add(getZEnd());

		return result.toArray();
	}

}
