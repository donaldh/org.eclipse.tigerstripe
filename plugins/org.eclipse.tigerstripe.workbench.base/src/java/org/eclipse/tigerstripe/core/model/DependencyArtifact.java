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

import org.eclipse.tigerstripe.api.model.IField;
import org.eclipse.tigerstripe.api.model.ILabel;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.IRelationship;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.api.utils.TigerstripeErrorLevel;
import org.eclipse.tigerstripe.core.model.ossj.DependencyArtifactPersister;
import org.eclipse.tigerstripe.core.model.persist.AbstractArtifactPersister;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

public class DependencyArtifact extends AbstractArtifact implements
		IDependencyArtifact , IRelationship{

	private IRelationshipEnd aRelationshipEnd;

	private IRelationshipEnd zRelationshipEnd;

	/** The marking tag for this Artifact */
	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.DEPENDENCY;

	public final static String AEND_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.DEPENDENCY + "-aEnd";

	public final static String ZEND_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.DEPENDENCY + "-zEnd";

	public final static String LABEL = "Dependency Artifact";

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static DependencyArtifact MODEL = new DependencyArtifact(null);

	public DependencyArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		setIStandardSpecifics(new StandardSpecifics(this));
	}

	public DependencyArtifact(JavaClass javaClass, ArtifactManager artifactMgr,
			ITigerstripeProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);
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
			DocletTag tag = field.getTagByName(AEND_TAG);
			if (tag != null) {
				// extracting aEnd
				String typeStr = field.getType().getValue();
				IType type = makeIType();
				type.setFullyQualifiedName(typeStr);
				setAEndType(type);
			} else {
				tag = field.getTagByName(ZEND_TAG);
				if (tag != null) {
					String typeStr = field.getType().getValue();
					IType type = makeIType();
					type.setFullyQualifiedName(typeStr);
					setZEndType(type);
				}
			}
		}
	}

	public String getLabel() {
		return LABEL;
	}

	public IType makeIType() {
		return new Type(getArtifactManager());
	}

	public void setAEndType(IType aEndType) {
		((DependencyEnd) getRelationshipAEnd()).setType(aEndType);
	}

	public void setZEndType(IType zEndType) {
		((DependencyEnd) getRelationshipZEnd()).setType(zEndType);
	}

	public String getArtifactType() {
		return IDependencyArtifact.class.getName();
	}

	public String getIArtifactType() {
		return IDependencyArtifact.class.getName();
	}

	public IType getAEndType() {
		return getRelationshipAEnd().getIType();
	}

	public IType getZEndType() {
		return getRelationshipZEnd().getIType();
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
		DependencyArtifact result = new DependencyArtifact(javaClass,
				artifactMgr, monitor);
		return result;
	}

	@Override
	public IMethod[] getIMethods() {
		return new IMethod[0];
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
	public String getMarkingTag() {
		return MARKING_TAG;
	}

	@Override
	public AbstractArtifact getModel() {
		return MODEL;
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new DependencyArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new DependencyArtifact(getArtifactManager());
	}

	public class DependencyEnd implements IRelationshipEnd {

		private IDependencyArtifact containingArtifact;

		private IType type;

		private String name;

		public DependencyEnd(String name, IDependencyArtifact dependency) {
			this.containingArtifact = dependency;
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public IRelationship getContainingRelationship() {
			return containingArtifact;
		}

		public IRelationshipEnd getOtherEnd() {
			if (this == getContainingRelationship().getRelationshipAEnd())
				return getContainingRelationship().getRelationshipZEnd();
			else
				return getContainingRelationship().getRelationshipAEnd();
		}

		public IType getIType() {
			return type;
		}

		public void setType(IType type) {
			this.type = type;
		}

		public List<TigerstripeError> validate() {

			List<TigerstripeError> errors = new ArrayList();

			// check the validity of the type for this association end
			List<TigerstripeError> errorList = ((IType) getIType()).validate();
			if (!errorList.isEmpty())
				errors.addAll(errorList);

			// making sure association ends are not scalars
			if (getIType() != null
					&& (getIType().isPrimitive() || getIType()
							.getFullyQualifiedName().equals("String"))) {
				errors.add(new TigerstripeError(TigerstripeErrorLevel.ERROR,
						"Dependency End cannot be a primitive type."));
			}

			return errors;
		}

		public String getNameForType(String typeName) {
			if (typeName.equals(aRelationshipEnd.getIType()
					.getFullyQualifiedName()))
				return "aEnd";
			else if (typeName.equals(zRelationshipEnd.getIType()
					.getFullyQualifiedName()))
				return "zEnd";
			return "";
		}

	}

	public IRelationshipEnd getRelationshipAEnd() {
		if (aRelationshipEnd == null) {
			aRelationshipEnd = new DependencyEnd("aEnd", this);
		}
		return aRelationshipEnd;
	}

	public IRelationshipEnd[] getRelationshipEnds() {
		return new IRelationshipEnd[] { getRelationshipAEnd(),
				getRelationshipZEnd() };
	}

	public IRelationshipEnd getRelationshipZEnd() {
		if (zRelationshipEnd == null) {
			zRelationshipEnd = new DependencyEnd("zEnd", this);
		}
		return zRelationshipEnd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * used to validate the Dependency when saving it to the underlying data
	 * model
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

		// in addition to the AbstractArtifact.validate() checks, need to also
		// check the aEnd and zEnd types to ensure that they are valid
		errorList = ((IType) getAEndType()).validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);
		errorList = ((IType) getZEndType()).validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);

		errorList = ((DependencyEnd) aRelationshipEnd).validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);
		errorList = ((DependencyEnd) zRelationshipEnd).validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);

		return errors;

	}

	@Override
	public Object[] getChildren() {
		Object[] objects = new Object[2];

		objects[0] = getRelationshipAEnd();
		objects[1] = getRelationshipZEnd();

		return objects;
	}

}
