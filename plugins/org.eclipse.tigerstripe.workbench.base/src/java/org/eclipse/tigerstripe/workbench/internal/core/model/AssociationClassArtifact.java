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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.AssociationClassArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ide.IDE;

import com.thoughtworks.qdox.model.JavaClass;

public class AssociationClassArtifact extends AssociationArtifact implements
		IAssociationClassArtifact, IAbstractArtifactInternal {

	/** The marking tag for this Artifact */
	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.ASSOCIATIONCLASS;

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static AssociationClassArtifact MODEL = new AssociationClassArtifact(
			null);

	public AssociationClassArtifact(ArtifactManager artifactManager) {
		super(artifactManager);
		setIStandardSpecifics(new StandardSpecifics(this));
	}

	public AssociationClassArtifact(JavaClass javaClass,
			ArtifactManager artifactManager, IProgressMonitor monitor) {
		super(javaClass, artifactManager, monitor);
		StandardSpecifics specifics = new StandardSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	public IAbstractArtifactInternal extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		AssociationClassArtifact result = new AssociationClassArtifact(
				javaClass, artifactMgr, monitor);
		return result;
	}

	@Override
	public String getMarkingTag() {
		return MARKING_TAG;
	}

	@Override
	public IAbstractArtifactInternal getModel() {
		return MODEL;
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new AssociationClassArtifactPersister(this, writer);
	}

	@Override
	protected void buildModel(JavaClass clazz, IProgressMonitor monitor) {
		super.buildModel(clazz, monitor);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new AssociationClassArtifact(getArtifactManager());
	}

	@Override
	public String getLabel() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
				IAssociationClassArtifactImpl.class.getName()).getLabel(this);
	}

	@Override
	public String getArtifactType() {
		return IAssociationClassArtifact.class.getName();
	}

	@Override
	@ProvideModelComponents
	public Collection<IMethod> getMethods() {
		return Collections.unmodifiableCollection(methods);
	}

	@Override
	@ProvideModelComponents
	public Collection<IField> getFields() {
		return Collections.unmodifiableCollection(fields);
	}

	@Override
	@ProvideModelComponents
	public Collection<Object> getChildren() {
		ArrayList<Object> result = new ArrayList<Object>();

		result.addAll(getMethods());
		result.addAll(getFields());
		result.addAll(getAssociationEnds());

		return result;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) throws TigerstripeException {
		super.doSave(monitor);
		
		IResource resource = (IResource) this.getAdapter(IResource.class);
		final String EDITOR_ID = "org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.associationClass.AssociationClassArtifactEditor";
		if (resource instanceof IFile ) {
			IFile file = (IFile) resource;
			IEditorDescriptor editorDescriptor = IDE.getDefaultEditor(file);
			if (editorDescriptor==null || (!EDITOR_ID.equals(editorDescriptor))) 
				IDE.setDefaultEditor((IFile)resource, EDITOR_ID);
		}
	}

}
