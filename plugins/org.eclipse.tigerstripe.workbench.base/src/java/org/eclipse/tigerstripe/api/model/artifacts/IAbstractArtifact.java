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
package org.eclipse.tigerstripe.api.model.artifacts;

import java.io.Writer;
import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IField;
import org.eclipse.tigerstripe.api.model.ILabel;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.IModelComponent;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IStandardSpecifics;
import org.eclipse.tigerstripe.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.api.project.IProjectDescriptor;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;

/**
 * Abstract Artifact for a Tigerstripe Model
 * 
 * 
 * @author Eric Dillon
 */
public interface IAbstractArtifact extends IModelComponent{

	public interface IextFieldTypeRef {
	
		public int getRefBy();
	
		public boolean isRefByValue();
	
		public boolean isRefByKey();
	
		public boolean isRefByKeyResult();
	
		public IType getIType();
	}

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


	public String getArtifactPath() throws TigerstripeException;

	/**
	 * Figures out the updater to use to submit IModelChangeRequests regarding
	 * this artifact.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IModelUpdater getUpdater() throws TigerstripeException;

	/**
	 * Returns the Ancestors of this class if defined. If no ancestor was
	 * defined return new IArtifact[0].
	 * 
	 * @return IArtifact[] - an array of all ancestors to this artifact
	 */
	public IAbstractArtifact[] getAncestors();

	/**
	 * Returns the list of artifacts that are extending "this" artifact.
	 * 
	 * @return
	 * @since 2.2-beta
	 */
	public IAbstractArtifact[] getExtendingIArtifacts();

	/**
	 * Returns an array of all the field types for this artifact.
	 * 
	 * @return
	 */
	public IextFieldTypeRef[] getFieldIextTypes();

	/**
	 * Returns the fully qualified name (ie. package + name) of this artifact.
	 * 
	 * @return String - fully qualified name of this artifact
	 */
	public String getFullyQualifiedName();

	/**
	 * Returns the type of this artifact. This will be the FQN of the class of
	 * the specific type of artifact: eg
	 * "org.eclipse.tigerstripe.api.external.model.artifacts.IextEventArtifact"
	 * 
	 * @return String - the fully qualified type of this artifact
	 */
	public String getIArtifactType();


	/**
	 * Returns the fields defined for this artifact. This will be limited to the
	 * fields defined locally in this artifact. If no field was defined return
	 * new IextField[0].
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty array.
	 * 
	 * @see getInheritedIextFields()
	 * @param filterFacetExcludedFields -
	 *            if set to true, all fields that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all fields
	 *            are returned.
	 * 
	 * @return IextField[] - an array of all the fields for this artifact
	 */
	public IField[] getIFields(boolean filterFacetExcludedFields);


	/**
	 * Returns the labels defined for this artifact. This will be limited to the
	 * labels defined locally in this artifact. If no label was defined return
	 * new IextField[0].
	 * 
	 * Note : Some artifact types do not support Labels and will always return
	 * an empty array.
	 * 
	 * @see getInheritedIextLabels()
	 * 
	 * @param filterFacetExcludedLabels -
	 *            if set to true, all labels that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all labels
	 *            are returned.
	 * @return IextLabel[] - an array of all the labels for this artifact
	 */
	public ILabel[] getILabels(boolean filterFacetExcludedLabels);


	/**
	 * Returns the methods defined for this artifact. This will be limited to
	 * the methods defined locally in this artifact. If no method was defined
	 * return new IextField[0].
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty array.
	 * 
	 * @see getInheritedIextMethods()
	 * 
	 * @param filterFacetExcludedMethods -
	 *            if set to true, all methods that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all methods
	 *            are returned.
	 * @return IextMethod[] - an array of all the methods for this artifact
	 */
	public IMethod[] getIMethods(boolean filterFacetExcludedMethods);

	/**
	 * Returns the details contained in the project that this artifact belongs
	 * to.
	 * 
	 * NOTE: this is always populated, as opposed to
	 * {@link #IextTigerstripeProject()} which returns null for artifacts
	 * contained in Modules.
	 * 
	 * @return
	 */
	public IProjectDescriptor getIextProjectDescriptor();

	/**
	 * Returns the standard specific details for this artifact.
	 * 
	 * @return
	 */
	public IStandardSpecifics getIStandardSpecifics();

	// public IextArtifactManagerSession getIextArtifactManagerSession();
	
	/**
	 * Returns an array containing all the artifacts that are implemented by
	 * this artifact;
	 * 
	 */
	public IAbstractArtifact[] getImplementedArtifacts();

	/**
	 * Returns an array containing all the artifacts that are implementing this.
	 * 
	 * NOTE: this method is not implemented yet and returns an empty array.
	 * 
	 * @return Not implemented - returns an empty array
	 */
	public IAbstractArtifact[] getImplementingIArtifacts();

	/**
	 * Returns the inherited fields for this artifact. Only inherited fields
	 * will be included, but all inherited fields (ie from all ancestors) will
	 * be included. If no field was inherited return new IextField[0].
	 * 
	 * This is equivalent to getInheritedIextFields(false)
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty array.
	 * 
	 * @return IextField[] - an array of all the inherited fields for this
	 *         artifact
	 */
	public IField[] getInheritedIFields();

	/**
	 * Returns the inherited fields for this artifact. Only inherited fields
	 * will be included, but all inherited fields (ie from all ancestors) will
	 * be included. If no field was inherited return new IextField[0].
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty array.
	 * 
	 * @param filterFacetExcludedFields -
	 *            if set to true, all fields that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all fields
	 *            are returned.
	 * @return IextField[] - an array of all the inherited fields for this
	 *         artifact
	 */
	public IField[] getInheritedIFields(boolean filterFacetExcludedFields);

	/**
	 * Returns the inherited labels for this artifact. Only inherited labels
	 * will be included, but all inherited labels (ie from all ancestors) will
	 * be included. If no label was inherited return new IextField[0].
	 * 
	 * this is equivalent to getInheritedIextLabels(false)
	 * 
	 * Note : Some artifact types do not support Labels and will always return
	 * an empty array.
	 * 
	 * @return IextLabel[] - an array of all the inherited labels for this
	 *         artifact
	 */
	public ILabel[] getInheritedILabels();

	/**
	 * Returns the inherited labels for this artifact. Only inherited labels
	 * will be included, but all inherited labels (ie from all ancestors) will
	 * be included. If no label was inherited return new IextField[0].
	 * 
	 * Note : Some artifact types do not support Labels and will always return
	 * an empty array.
	 * 
	 * @param filterFacetExcludedLabels -
	 *            if set to true, all labels that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all labels
	 *            are returned.
	 * @return IextLabel[] - an array of all the inherited labels for this
	 *         artifact
	 */
	public ILabel[] getInheritedILabels(boolean filterFacetExcludedLabels);

	/**
	 * Returns the inherited methods for this artifact. Only inherited methods
	 * will be included, but all inherited methods (ie from all ancestors) will
	 * be included. If no method was inherited return new IextField[0].
	 * 
	 * This is equivalent to getInheritedIextMethods(false)
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty array.
	 * 
	 * @return IextMethod[] - an array of all the inherited methods for this
	 *         artifact
	 */
	public IMethod[] getInheritedIMethods();

	/**
	 * Returns the inherited methods for this artifact. Only inherited methods
	 * will be included, but all inherited methods (ie from all ancestors) will
	 * be included. If no method was inherited return new IextField[0].
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty array.
	 * 
	 * @param filterFacetExcludedMethods -
	 *            if set to true, all methods that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all methods
	 *            are returned.
	 * @return IextMethod[] - an array of all the inherited methods for this
	 *         artifact
	 */
	public IMethod[] getInheritedIMethods(
			boolean filterFacetExcludedMethods);

	/**
	 * Returns the project that contains this artifact.
	 * 
	 * NOTE: if this is contained in a module, the return is null.
	 * {@link #getIextProjectDescriptor()}
	 * 
	 * @return the containing Project.
	 */
	public ITigerstripeProject getITigerstripeProject();

	/**
	 * Returns the package where this artifact is defined. If this artifact is
	 * defined in the default package, null is returned.
	 * 
	 * @return String - the package where this artifact was defined.
	 */
	public String getPackage();

	public Collection<String> getReferencedArtifacts();

	/**
	 * Returns an array containing all the artifacts being referenced (through a
	 * reference or attribute) from this artifact.
	 * 
	 * @return
	 */
	public IAbstractArtifact[] getReferencedIArtifacts();

	/**
	 * Returns an array with all the artifacts referencing this artifact.
	 * 
	 * NOTE: this is currently not implemented and will always return an empty
	 * Array.
	 * 
	 * @return Empty array
	 */
	public IAbstractArtifact[] getReferencingIArtifacts();

	/**
	 * Returns true if this artifact extends another artifact.
	 * 
	 * @return true if this artifacts extends another artifact, false otherwise.
	 */
	public boolean hasExtends();

	/**
	 * Returns true if this Artifact is marked as abstract.
	 * 
	 * @return true if the artifact is abstract, false otherwise
	 */
	public boolean isAbstract();
}
