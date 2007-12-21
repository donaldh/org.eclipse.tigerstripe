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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IField;
import org.eclipse.tigerstripe.api.artifacts.model.IMethod;
import org.eclipse.tigerstripe.api.external.model.IextField;
import org.eclipse.tigerstripe.api.external.model.IextLabel;
import org.eclipse.tigerstripe.api.external.model.IextMethod;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationClassArtifact;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.core.model.ossj.AssociationClassArtifactPersister;
import org.eclipse.tigerstripe.core.model.persist.AbstractArtifactPersister;

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
	public String getIArtifactType() {
		return IextAssociationClassArtifact.class.getName();
	}

	@Override
	public IextLabel[] getIextLabels() {
		return new IextLabel[0];
	}

	@Override
	public IextMethod[] getIextMethods() {
		Collection methods = getMethods();
		IextMethod[] result = new IextMethod[methods.size()];
		return (IextMethod[]) methods.toArray(result);
	}

	@Override
	public IextField[] getIextFields() {
		Collection fields = getFields();
		IextField[] result = new IextField[fields.size()];
		return (IextField[]) fields.toArray(result);
	}

	@Override
	public IextField[] getInheritedIextFields() {
		Collection fields = getInheritedFields();
		IextField[] result = new IextField[fields.size()];
		return (IextField[]) fields.toArray(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * used to validate the AssociationClass when saving it to the underlying
	 * data model (in this class, just delegates responsibility to it's
	 * superclass, the Association class)
	 * 
	 * @see org.eclipse.tigerstripe.core.model.AssociationArtifact#validate()
	 */
	@Override
	public List<TigerstripeError> validate() {
		List<TigerstripeError> errors = new ArrayList();
		List<TigerstripeError> errorList = super.validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);
		return errors;
	}

	@Override
	public Object[] getChildren() {
		ArrayList<Object> result = new ArrayList<Object>();

		for (IMethod method : getIMethods()) {
			result.add(method);
		}

		for (IField field : getIFields()) {
			result.add(field);
		}
		result.add(getAEnd());
		result.add(getZEnd());

		return result.toArray();
	}

}
