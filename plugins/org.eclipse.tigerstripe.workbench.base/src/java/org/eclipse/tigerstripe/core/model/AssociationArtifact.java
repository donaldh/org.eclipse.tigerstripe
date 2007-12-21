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
import java.util.List;

import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationEnd;
import org.eclipse.tigerstripe.api.external.model.IextField;
import org.eclipse.tigerstripe.api.external.model.IextLabel;
import org.eclipse.tigerstripe.api.external.model.IextMethod;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.core.model.ossj.AssociationArtifactPersister;
import org.eclipse.tigerstripe.core.model.persist.AbstractArtifactPersister;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

public class AssociationArtifact extends AbstractArtifact implements
		IAssociationArtifact {

	private IAssociationEnd aEnd;

	private IAssociationEnd zEnd;

	/** The marking tag for this Artifact */
	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.ASSOCIATION;

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static AssociationArtifact MODEL = new AssociationArtifact(
			null);

	public final static String LABEL = "Association Artifact";

	public AssociationArtifact(ArtifactManager artifactManager) {
		super(artifactManager);
		setIStandardSpecifics(new StandardSpecifics(this));
	}

	public AssociationArtifact(JavaClass javaClass,
			ArtifactManager artifactManager, ITigerstripeProgressMonitor monitor) {
		super(javaClass, artifactManager, monitor);
		StandardSpecifics specifics = new StandardSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	protected void buildModel(JavaClass clazz,
			ITigerstripeProgressMonitor monitor) {
		super.buildModel(clazz, monitor);

		// Now extract the aEnd and zEnd
		JavaField[] fields = clazz.getFields();
		for (JavaField field : fields) {
			DocletTag tag = field.getTagByName(AssociationEnd.AEND_TAG);
			if (tag != null) {
				// extracting aEnd
				AssociationEnd end = new AssociationEnd(field,
						getArtifactManager());
				end.setContainingAssociation(this);
				setAEnd(end);
			} else {
				tag = field.getTagByName(AssociationEnd.ZEND_TAG);
				if (tag != null) {
					AssociationEnd end = new AssociationEnd(field,
							getArtifactManager());
					end.setContainingAssociation(this);
					setZEnd(end);
				}
			}
		}
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
		AssociationArtifact result = new AssociationArtifact(javaClass,
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
		return new AssociationArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new AssociationArtifact(getArtifactManager());
	}

	public IAssociationEnd makeAssociationEnd() {
		return new AssociationEnd(getArtifactManager());
	}

	public String getLabel() {
		return LABEL;
	}

	public String getArtifactType() {
		return IAssociationArtifact.class.getName();
	}

	public String getIArtifactType() {
		return IextAssociationArtifact.class.getName();
	}

	@Override
	public IextField[] getIextFields() {
		return new IextField[0];
	}

	@Override
	public IextLabel[] getIextLabels() {
		return new IextLabel[0];
	}

	@Override
	public IextMethod[] getIextMethods() {
		return new IextMethod[0];
	}

	public IextAssociationEnd getAEnd() {
		return this.aEnd;
	}

	public IextAssociationEnd getZEnd() {
		return this.zEnd;
	}

	public void setAEnd(IAssociationEnd aEnd) {
		this.aEnd = aEnd;
		((AssociationEnd) aEnd).setContainingAssociation(this);
	}

	public void setZEnd(IAssociationEnd zEnd) {
		this.zEnd = zEnd;
		((AssociationEnd) zEnd).setContainingAssociation(this);
	}

	public IRelationshipEnd getRelationshipAEnd() {
		return getAEnd();
	}

	public IRelationshipEnd[] getRelationshipEnds() {
		return new IRelationshipEnd[] { getRelationshipAEnd(),
				getRelationshipZEnd() };
	}

	public IRelationshipEnd getRelationshipZEnd() {
		return getZEnd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * used to validate an AssociationArtifact when saving it to the underlying
	 * data model. To do this, first check with the AbstractArtifact it inherits
	 * from to ensure consistency, then check the validity of the AEnd and ZEnd
	 * values (both are IAssociationEnds so just check with the underlying
	 * IAssociationEnd class to see if they are valid)
	 * 
	 * @see org.eclipse.tigerstripe.api.artifacts.model.ILabel#validate()
	 */
	@Override
	public List<TigerstripeError> validate() {
		List<TigerstripeError> errors = new ArrayList();

		// first check for errors using the AbstractArtifact.validate() method
		List<TigerstripeError> errorList = super.validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);

		// next, check for some Association-specific errors (i.e. that the
		// aEnd and zEnd are valid
		errorList = ((IAssociationEnd) getAEnd()).validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);
		errorList = ((IAssociationEnd) getZEnd()).validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);

		return errors;

	}

	@Override
	public Object[] getChildren() {
		Object[] objects = new Object[2];

		objects[0] = getAEnd();
		objects[1] = getZEnd();

		return objects;
	}

	public IextAssociationEnd[] getAssociationEnds() {
		IextAssociationEnd[] result = new IextAssociationEnd[] { getAEnd(),
				getZEnd() };
		return result;
	}

}
