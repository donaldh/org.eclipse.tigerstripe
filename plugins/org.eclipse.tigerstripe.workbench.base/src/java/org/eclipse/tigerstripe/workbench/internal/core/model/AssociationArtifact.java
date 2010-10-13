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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.AssociationArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ide.IDE;

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
	protected void buildModel(JavaClass clazz, IProgressMonitor monitor) {
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
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
				IAssociationArtifactImpl.class.getName()).getLabel(this);
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

	public Collection<IRelationshipEnd> getRelationshipEnds() {
		Collection<IRelationshipEnd>  relationshipEnds = new ArrayList<IRelationshipEnd>();
		relationshipEnds.add(getRelationshipAEnd());
		relationshipEnds.add(getRelationshipZEnd());
		return relationshipEnds;
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
	public Collection<Object> getChildren() {
		Collection<Object> objects = new ArrayList<Object>();

		objects.addAll(getAssociationEnds());
		return objects;
	}

	
	public Collection<IAssociationEnd> getAssociationEnds() {
		Collection<IAssociationEnd> result = new ArrayList<IAssociationEnd>();
		// Bug 249290 - not adding "null" ends
		if (getAEnd() != null){
			result.add(getAEnd());
		}
		if (getZEnd() != null){
			result.add(getZEnd());
		}
		return result;
	}

	@Override
	public Collection<IModelComponent> getContainedModelComponents() {
		Collection<IModelComponent> ownedComponents = new ArrayList<IModelComponent>();
		ownedComponents.addAll(super.getContainedModelComponents());
		ownedComponents.addAll(this.getAssociationEnds());
		return ownedComponents;
	}

	@Override
	public void doSave(IProgressMonitor monitor) throws TigerstripeException {
		super.doSave(monitor);
		
		IResource resource = (IResource) this.getAdapter(IResource.class);
		final String EDITOR_ID = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.associationEditor";
		if (resource instanceof IFile ) {
			IFile file = (IFile) resource;
			IEditorDescriptor editorDescriptor = IDE.getDefaultEditor(file);
			if (editorDescriptor==null || (!EDITOR_ID.equals(editorDescriptor))) 
				IDE.setDefaultEditor((IFile)resource, EDITOR_ID);
		}
	}
	
}
