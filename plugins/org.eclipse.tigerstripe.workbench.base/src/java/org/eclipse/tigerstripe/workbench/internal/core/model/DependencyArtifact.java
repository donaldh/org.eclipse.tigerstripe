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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.DependencyArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

public class DependencyArtifact extends AbstractArtifact implements
		IDependencyArtifact, IRelationship {

	private IRelationshipEnd aRelationshipEnd;

	private IRelationshipEnd zRelationshipEnd;

	/** The marking tag for this Artifact */
	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.DEPENDENCY;

	public final static String AEND_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.DEPENDENCY + "-aEnd";

	public final static String ZEND_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.DEPENDENCY + "-zEnd";

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
			IProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);
		StandardSpecifics specifics = new StandardSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	protected void buildModel(JavaClass clazz, IProgressMonitor monitor) {
		super.buildModel(clazz, monitor);

		// Now extract the aEnd and zEnd
		JavaField[] fields = clazz.getFields();
		for (JavaField field : fields) {
			DocletTag tag = field.getTagByName(AEND_TAG);
			if (tag != null) {
				// extracting aEnd
				String typeStr = field.getType().getValue();
				IType type = makeType();
				type.setFullyQualifiedName(typeStr);
				setAEndType(type);
			} else {
				tag = field.getTagByName(ZEND_TAG);
				if (tag != null) {
					String typeStr = field.getType().getValue();
					IType type = makeType();
					type.setFullyQualifiedName(typeStr);
					setZEndType(type);
				}
			}
		}
	}

	public String getLabel() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
				IDependencyArtifactImpl.class.getName()).getLabel();
	}

	public IType makeType() {
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

	public IType getAEndType() {
		return getRelationshipAEnd().getType();
	}

	public IType getZEndType() {
		return getRelationshipZEnd().getType();
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		DependencyArtifact result = new DependencyArtifact(javaClass,
				artifactMgr, monitor);
		return result;
	}

	@Override
	public Collection<IMethod> getMethods() {
		return IMethod.EMPTY_LIST;
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

		public IType getType() {
			return type;
		}

		public void setType(IType type) {
			this.type = type;
		}

		public IStatus validate() {

			MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
					"DependendyEnd Validation", null);

			// check the validity of the type for this association end
			IStatus typeStatus = ((IType) getType()).validate();
			if (!typeStatus.isOK())
				result.add(typeStatus);

			// making sure association ends are not scalars
			if (getType() != null
					&& (getType().isPrimitive() || getType()
							.getFullyQualifiedName().equals("String"))) {
				result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(),
						ArtifactMetadataFactory.INSTANCE.getMetadata(
								IDependencyArtifactImpl.class.getName()).getLabel() + " End cannot be a " + ArtifactMetadataFactory.INSTANCE.getMetadata(
										IPrimitiveTypeImpl.class.getName())
										.getLabel() + "."));
			}

			return result;
		}

		public String getNameForType(String typeName) {
			if (typeName.equals(aRelationshipEnd.getType()
					.getFullyQualifiedName()))
				return "aEnd";
			else if (typeName.equals(zRelationshipEnd.getType()
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

	public Collection<IRelationshipEnd> getRelationshipEnds() {
		Collection<IRelationshipEnd>  relationshipEnds = new ArrayList<IRelationshipEnd>();
		relationshipEnds.add(getRelationshipAEnd());
		relationshipEnds.add(getRelationshipZEnd());
		return relationshipEnds;
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
	 * @see org.eclipse.tigerstripe.api.artifacts.model.ILiteral#validate()
	 */
	@Override
	public IStatus validate() {

		// first check for errors using the AbstractArtifact.validate() method
		MultiStatus result = (MultiStatus) super.validate();

		// in addition to the AbstractArtifact.validate() checks, need to also
		// check the aEnd and zEnd types to ensure that they are valid
		IStatus aEndTypeStatus = ((IType) getAEndType()).validate();
		if (!aEndTypeStatus.isOK())
			result.add(aEndTypeStatus);
		IStatus zEndTypeStatus = ((IType) getZEndType()).validate();
		if (!zEndTypeStatus.isOK())
			result.add(zEndTypeStatus);

		IStatus aEndStatus = ((DependencyEnd) aRelationshipEnd).validate();
		if (!aEndStatus.isOK())
			result.add(aEndStatus);
		IStatus zEndStatus = ((DependencyEnd) zRelationshipEnd).validate();
		if (!zEndStatus.isOK())
			result.add(zEndStatus);

		return result;

	}

	@Override
	public Collection<Object> getChildren() {
		Collection<Object> objects = new ArrayList<Object>();

		objects.addAll(getRelationshipEnds());

		return objects;
	}

}
