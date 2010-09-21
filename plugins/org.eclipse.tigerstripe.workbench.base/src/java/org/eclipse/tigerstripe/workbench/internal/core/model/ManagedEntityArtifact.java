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
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.ManagedEntityArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.tags.PropertiesConstants;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEntitySpecifics;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ide.IDE;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * This is a Managed Entity Artifact
 * 
 * @author Eric Dillon
 */
public class ManagedEntityArtifact extends AbstractArtifact implements
		IManagedEntityArtifact {

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.MANAGEDENTITY;

	public String getArtifactType() {
		return IManagedEntityArtifact.class.getName();
	}

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static ManagedEntityArtifact MODEL = new ManagedEntityArtifact(
			null);

	// private Collection derivedEntities;

	@Override
	public String getMarkingTag() {
		return ManagedEntityArtifact.MARKING_TAG;
	}

	@Override
	public AbstractArtifact getModel() {
		return MODEL;
	}

	public String getLabel() {
		return getMetadata().getLabel(this);
	}

	// public Collection getDerivedEntities() {
	// return this.derivedEntities;
	// }
	//
	public ManagedEntityArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		// this.derivedEntities = new ArrayList();
		setIStandardSpecifics(new OssjEntitySpecifics(this));
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		ManagedEntityArtifact result = new ManagedEntityArtifact(javaClass,
				artifactMgr, monitor);
		return result;
	}

	public ManagedEntityArtifact(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);

		// this.derivedEntities = new ArrayList();
		OssjEntitySpecifics specifics = new OssjEntitySpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	public void resolveReferences(IProgressMonitor monitor)
			throws TigerstripeException {
		super.resolveReferences(monitor);

		// see Bug #53: duplicate logic with OssjEntitySpecifics

		// Extract the primary Key
		// Tag tag = getFirstTagByName(getMarkingTag());
		// Properties props = tag.getProperties();
		// String fullQual = props.getProperty("primary-key",
		// "java.lang.String");
		// this.primaryKey = new PrimaryKey(fullQual);
		//
		// Populate the derived entities now
		// populateDerivedEntities(monitor);

		// Extract CRUD operations Options
		try {
			OssjEntitySpecifics specifics = (OssjEntitySpecifics) getIStandardSpecifics();
			specifics.setCRUDProperties(IOssjEntitySpecifics.CREATE,
					PropertiesConstants.getPropertiesById(getTags(),
							IOssjEntitySpecifics.CREATE_PROP_ID));
			specifics.setCRUDProperties(IOssjEntitySpecifics.GET,
					PropertiesConstants.getPropertiesById(getTags(),
							IOssjEntitySpecifics.GET_PROP_ID));
			specifics.setCRUDProperties(IOssjEntitySpecifics.SET,
					PropertiesConstants.getPropertiesById(getTags(),
							IOssjEntitySpecifics.SET_PROP_ID));
			specifics.setCRUDProperties(IOssjEntitySpecifics.DELETE,
					PropertiesConstants.getPropertiesById(getTags(),
							IOssjEntitySpecifics.REMOVE_PROP_ID));
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	public Properties getCreateProperties() {
		OssjEntitySpecifics specifics = (OssjEntitySpecifics) getIStandardSpecifics();
		return specifics.getCRUDProperties(IOssjEntitySpecifics.CREATE);
	}

	public Properties getGetProperties() {
		OssjEntitySpecifics specifics = (OssjEntitySpecifics) getIStandardSpecifics();
		return specifics.getCRUDProperties(IOssjEntitySpecifics.GET);
	}

	public Properties getSetProperties() {
		OssjEntitySpecifics specifics = (OssjEntitySpecifics) getIStandardSpecifics();
		return specifics.getCRUDProperties(IOssjEntitySpecifics.SET);
	}

	public Properties getRemoveProperties() {
		OssjEntitySpecifics specifics = (OssjEntitySpecifics) getIStandardSpecifics();
		return specifics.getCRUDProperties(IOssjEntitySpecifics.DELETE);
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new ManagedEntityArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new ManagedEntityArtifact(getArtifactManager());
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) throws TigerstripeException {
		super.doSave(monitor);
		
		IResource resource = (IResource) this.getAdapter(IResource.class);
		final String EDITOR_ID = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.entityEditor";
		if (resource instanceof IFile ) {
			IFile file = (IFile) resource;
			IEditorDescriptor editorDescriptor = IDE.getDefaultEditor(file);
			if (editorDescriptor==null || (!EDITOR_ID.equals(editorDescriptor))) 
				IDE.setDefaultEditor((IFile)resource, EDITOR_ID);
		}
	}

}