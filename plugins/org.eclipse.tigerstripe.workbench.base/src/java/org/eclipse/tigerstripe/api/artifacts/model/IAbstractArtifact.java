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
package org.eclipse.tigerstripe.api.artifacts.model;

import java.io.Writer;
import java.util.List;

import org.eclipse.tigerstripe.api.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;

/**
 * Abstract Artifact for a Tigerstripe Model
 * 
 * 
 * @author Eric Dillon
 */
public interface IAbstractArtifact extends IModelComponent, IArtifact {

	public IStandardSpecifics getIStandardSpecifics();

	public void setFullyQualifiedName(String fqn);

	public void setPackage(String packageName);

	/**
	 * Returns the fields defined for this artifact
	 * 
	 */
	public IField[] getIFields();

	/**
	 * Make a new blank artifact field
	 * 
	 * @return
	 */
	public IField makeIField();

	/**
	 * Sets the fields for this Abstract Artifact
	 * 
	 * @param fields
	 * @throws IllegalArgumentException
	 */
	public void setIFields(IField[] fields);

	public void addIField(IField field);

	public void removeIFields(IField[] fields);

	/**
	 * Returns the labels defined for this artifact
	 * 
	 */
	public ILabel[] getILabels();

	/**
	 * Make a new blank artifact label
	 * 
	 * @return
	 */
	public ILabel makeILabel();

	/**
	 * Sets the labels for this Abstract Artifact
	 * 
	 * @param labels
	 * @throws IllegalArgumentException
	 */
	public void setILabels(ILabel[] labels);

	public void addILabel(ILabel label);

	public void removeILabels(ILabel[] labels);

	public IMethod[] getIMethods();

	public IMethod makeIMethod();

	public void setIMethods(IMethod[] methods);

	public void addIMethod(IMethod method);

	public void removeIMethods(IMethod[] methods);

	public void setExtendedIArtifact(IAbstractArtifact artifact);

	public void doSave(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException;

	public void doSilentSave(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException;

	public List<TigerstripeError> validate();

	public void write(Writer writer) throws TigerstripeException;

	public String asText() throws TigerstripeException;

	public ITigerstripeProject getIProject();

	public String getArtifactType();

	/**
	 * Returns true if this is abstract
	 * 
	 * @return true, if this an abstract Artifact
	 */
	public boolean isAbstract();

	/**
	 * Sets the value of the isAbstract flag for this.
	 * 
	 * @param isAbstract
	 */
	public void setAbstract(boolean isAbstract);

	/**
	 * Returns true if this is a read-only artifact (i.e. it lives in a TS
	 * module where artifact cannot be changed)
	 * 
	 * Please note that this boolean is set when the artifact is added to an
	 * artifact manager. Only when a module is being loaded up in memory will
	 * this flag be set to true. In other words, any manually created artifact
	 * will always return true.
	 * 
	 * @return true if this is readonly.
	 */
	public boolean isReadonly();

	// ==================================================

	/**
	 * Returns the label for this Artifact Type that will be displayed in the
	 * GUI
	 * 
	 * @since 1.2
	 */
	public String getLabel();

	/**
	 * Returns the IArtifact which this artifact extends. If there is no extends
	 * clause, null is returned.
	 * 
	 * @return IArtifact - the extended artifact
	 */
	public IAbstractArtifact getExtendedIArtifact();

	/**
	 * Returns an array of all the artifacts that extends this directly.
	 */
	public IAbstractArtifact[] getExtendingArtifacts();

	/**
	 * Returns all children for this artifact
	 * 
	 * Children are Fields, Methods, Literals, and potentially ends for
	 * IRelationships
	 * 
	 */
	public Object[] getChildren();

	/**
	 * Returns true if this artifact is part of the active facet, false
	 * otherwise.
	 * 
	 * If no active facet, always returns true;
	 */
	public boolean isInActiveFacet() throws TigerstripeException;

	public String getImplementedArtifactsAsStr();

	public void setImplementedArtifacts(IAbstractArtifact[] artifacts);

	public IAbstractArtifact[] getImplementedArtifacts();

	public String getArtifactPath() throws TigerstripeException;

	/**
	 * Figures out the updater to use to submit IModelChangeRequests regarding
	 * this artifact.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IModelUpdater getUpdater() throws TigerstripeException;
}
