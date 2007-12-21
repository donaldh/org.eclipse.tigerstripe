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
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextManagedEntityArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.ossjSpecifics.IextOssjEntitySpecifics;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.ossj.ManagedEntityArtifactPersister;
import org.eclipse.tigerstripe.core.model.ossj.specifics.OssjEntitySpecifics;
import org.eclipse.tigerstripe.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.core.model.tags.PropertiesConstants;
import org.eclipse.tigerstripe.core.util.Util;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ManagedEntityArtifact extends AbstractArtifact implements
		IManagedEntityArtifact {

	/** logger for output */
	private static Logger log = Logger.getLogger(ManagedEntityArtifact.class);

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.MANAGEDENTITY;

	public final static String LABEL = "Managed Entity Artifact";

	public String getArtifactType() {
		return IManagedEntityArtifact.class.getName();
	}

	public String getIArtifactType() {
		return IextManagedEntityArtifact.class.getName();
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
		return LABEL;
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
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
		log.debug(" Extracting Entity from "
				+ javaClass.getFullyQualifiedName());
		// validate( javaClass );

		ManagedEntityArtifact result = new ManagedEntityArtifact(javaClass,
				artifactMgr, monitor);
		return result;
	}

	public ManagedEntityArtifact(JavaClass javaClass,
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);

		// this.derivedEntities = new ArrayList();
		OssjEntitySpecifics specifics = new OssjEntitySpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	public void resolveReferences(ITigerstripeProgressMonitor monitor)
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
			specifics.setCRUDProperties(IextOssjEntitySpecifics.CREATE,
					PropertiesConstants.getPropertiesById(getTags(),
							IextOssjEntitySpecifics.CREATE_PROP_ID));
			specifics.setCRUDProperties(IextOssjEntitySpecifics.GET,
					PropertiesConstants.getPropertiesById(getTags(),
							IextOssjEntitySpecifics.GET_PROP_ID));
			specifics.setCRUDProperties(IextOssjEntitySpecifics.SET,
					PropertiesConstants.getPropertiesById(getTags(),
							IextOssjEntitySpecifics.SET_PROP_ID));
			specifics.setCRUDProperties(IextOssjEntitySpecifics.DELETE,
					PropertiesConstants.getPropertiesById(getTags(),
							IextOssjEntitySpecifics.REMOVE_PROP_ID));
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	public PrimaryKey getPrimaryKey() {
		OssjEntitySpecifics specifics = (OssjEntitySpecifics) getIStandardSpecifics();
		return new PrimaryKey(specifics.getPrimaryKey());
	}

	public class PrimaryKey implements IPrimaryKey {

		private String fullyQualifiedName;

		public PrimaryKey(String fullyQualifiedName) {
			this.fullyQualifiedName = fullyQualifiedName;
		}

		public String getFullyQualifiedName() {
			return this.fullyQualifiedName;
		}

		public void setFullyQualifiedName(String fullyQualifiedName) {
			this.fullyQualifiedName = fullyQualifiedName;
		}

		public String getPackage() {
			return Util.packageOf(this.fullyQualifiedName);
		}

		public String getName() {
			return Util.nameOf(this.fullyQualifiedName);
		}
	}

	// private void populateDerivedEntities( ITigerstripeProgressMonitor monitor
	// ) {
	//
	// if (getJavaClass() != null) {
	// JavaClass[] derivedClasses = getJavaClass().getDerivedClasses();
	//
	// for (int i = 0; i < derivedClasses.length; i++) {
	// AbstractArtifact artifact = getArtifactManager()
	// .getArtifactByFullyQualifiedName(
	// derivedClasses[i].getFullyQualifiedName(), true, monitor);
	// if (artifact instanceof ManagedEntityArtifact) {
	// derivedEntities.add(artifact);
	// }
	// }
	// }
	// }
	//
	public Properties getCreateProperties() {
		OssjEntitySpecifics specifics = (OssjEntitySpecifics) getIStandardSpecifics();
		return specifics.getCRUDProperties(IextOssjEntitySpecifics.CREATE);
	}

	public Properties getGetProperties() {
		OssjEntitySpecifics specifics = (OssjEntitySpecifics) getIStandardSpecifics();
		return specifics.getCRUDProperties(IextOssjEntitySpecifics.GET);
	}

	public Properties getSetProperties() {
		OssjEntitySpecifics specifics = (OssjEntitySpecifics) getIStandardSpecifics();
		return specifics.getCRUDProperties(IextOssjEntitySpecifics.SET);
	}

	public Properties getRemoveProperties() {
		OssjEntitySpecifics specifics = (OssjEntitySpecifics) getIStandardSpecifics();
		return specifics.getCRUDProperties(IextOssjEntitySpecifics.DELETE);
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new ManagedEntityArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new ManagedEntityArtifact(getArtifactManager());
	}

}