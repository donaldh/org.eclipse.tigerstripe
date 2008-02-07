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
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.AssociationArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILiteral;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;

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
			ArtifactManager artifactManager, IProgressMonitor monitor) {
		super(javaClass, artifactManager, monitor);
		StandardSpecifics specifics = new StandardSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	protected void buildModel(JavaClass clazz,
			IProgressMonitor monitor) {
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
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
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

	@Override
	public Collection<IField> getFields() {
		return IField.EMPTY_LIST;
	}

	@Override
	public Collection<ILiteral> getLiterals() {
		return ILiteral.EMPTY_LIST;
	}

	@Override
	public Collection<IMethod> getMethods() {
		return IMethod.EMPTY_LIST;
	}

	public IAssociationEnd getAEnd() {
		return this.aEnd;
	}

	public IAssociationEnd getZEnd() {
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
	 * @see org.eclipse.tigerstripe.api.artifacts.model.ILiteral#validate()
	 */
	@Override
	public IStatus validate() {
		// first check for errors using the AbstractArtifact.validate() method
		MultiStatus result = (MultiStatus) super.validate();

		// next, check for some Association-specific errors (i.e. that the
		// aEnd and zEnd are valid
		IStatus aEndStatus = ((IAssociationEnd) getAEnd()).validate();
		if (!aEndStatus.isOK())
			result.add(aEndStatus);
		IStatus zEndStatus = ((IAssociationEnd) getZEnd()).validate();
		if (!zEndStatus.isOK())
			result.add(zEndStatus);

		return result;
	}

	@Override
	public Object[] getChildren() {
		Object[] objects = new Object[2];

		objects[0] = getAEnd();
		objects[1] = getZEnd();

		return objects;
	}

	public IAssociationEnd[] getAssociationEnds() {
		IAssociationEnd[] result = new IAssociationEnd[] { getAEnd(), getZEnd() };
		return result;
	}

}
