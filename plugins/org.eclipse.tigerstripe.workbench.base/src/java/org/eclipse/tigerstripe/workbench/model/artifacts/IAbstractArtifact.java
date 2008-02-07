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
package org.eclipse.tigerstripe.workbench.model.artifacts;

import java.io.Writer;
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.IWorkingCopy;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IStandardSpecifics;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILiteral;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.project.IProjectDescriptor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * Abstract Artifact for a Tigerstripe Model
 * 
 * 
 * @author Eric Dillon
 */
public interface IAbstractArtifact extends IModelComponent, IWorkingCopy {

	public interface IFieldTypeRef {

		public int getRefBy();

		public boolean isRefByValue();

		public boolean isRefByKey();

		public boolean isRefByKeyResult();

		public IType getType();
	}

	public void setFullyQualifiedName(String fqn);

	public void setPackage(String packageName);

	/**
	 * Returns the fields defined for this artifact
	 * 
	 */
	public Collection<IField> getFields();

	/**
	 * Make a new blank artifact field
	 * 
	 * @return
	 */
	public IField makeField();

	/**
	 * Sets the fields for this Abstract Artifact
	 * 
	 * @param fields
	 * @throws IllegalArgumentException
	 */
	public void setFields(Collection<IField> fields);

	public void addField(IField field);

	public void removeFields(Collection<IField> fields);

	/**
	 * Returns the literals defined for this artifact
	 * 
	 */
	public Collection<ILiteral> getLiterals();

	/**
	 * Make a new blank artifact literal
	 * 
	 * @return
	 */
	public ILiteral makeLiteral();

	/**
	 * Sets the literals for this Abstract Artifact
	 * 
	 * @param literals
	 * @throws IllegalArgumentException
	 */
	public void setLiterals(Collection<ILiteral> literals);

	public void addLiteral(ILiteral literal);

	public void removeLiterals(Collection<ILiteral> literals);

	public Collection<IMethod> getMethods();

	public IMethod makeMethod();

	public void setMethods(Collection<IMethod> methods);

	public void addMethod(IMethod method);

	public void removeMethods(Collection<IMethod> methods);

	public void setExtendedArtifact(IAbstractArtifact artifact);

	public void doSave(IProgressMonitor monitor)
			throws TigerstripeException;

	public void doSilentSave(IProgressMonitor monitor)
			throws TigerstripeException;

	public void write(Writer writer) throws TigerstripeException;

	public String asText() throws TigerstripeException;

	public ITigerstripeProject getTigerstripeProject();

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
	public IAbstractArtifact getExtendedArtifact();

	/**
	 * Returns the Ancestors of this class if defined. If no ancestor was
	 * defined return new IArtifact[0].
	 * 
	 * @return IArtifact[] - an array of all ancestors to this artifact
	 */
	public Collection<IAbstractArtifact> getAncestors();
	
	/**
	 * Returns an array of all the artifacts that extends this directly.
	 */
	public Collection<IAbstractArtifact> getExtendingArtifacts();

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

	public void setImplementedArtifacts(Collection<IAbstractArtifact> artifacts);

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
	 * Returns an array of all the field types for this artifact.
	 * 
	 * @return
	 */
	public IFieldTypeRef[] getFieldTypes();

	/**
	 * Returns the fully qualified name (ie. package + name) of this artifact.
	 * 
	 * @return String - fully qualified name of this artifact
	 */
	public String getFullyQualifiedName();

	/**
	 * Returns the type of this artifact. This will be the FQN of the class of
	 * the specific type of artifact: eg
	 * "org.eclipse.tigerstripe.api.external.model.artifacts.IEventArtifact"
	 * 
	 * @return String - the fully qualified type of this artifact
	 */
	public String getArtifactType();

	/**
	 * Returns the fields defined for this artifact. This will be limited to the
	 * fields defined locally in this artifact. If no field was defined return
	 * new IField[0].
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty array.
	 * 
	 * @see getInheritedIFields()
	 * @param filterFacetExcludedFields -
	 *            if set to true, all fields that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all fields
	 *            are returned.
	 * 
	 * @return IField[] - an array of all the fields for this artifact
	 */
	public Collection<IField> getFields(boolean filterFacetExcludedFields);

	/**
	 * Returns the literals defined for this artifact. This will be limited to the
	 * literals defined locally in this artifact. If no literal was defined return
	 * new IField[0].
	 * 
	 * Note : Some artifact types do not support literals and will always return
	 * an empty array.
	 * 
	 * @see getInheritedLietrals()
	 * 
	 * @param filterFacetExcludedliterals -
	 *            if set to true, all literals that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all literals
	 *            are returned.
	 * @return Collection<ILiteral> - a collection of all the literals for this artifact
	 */
	public Collection<ILiteral> getLiterals(boolean filterFacetExcludedLiterals);

	/**
	 * Returns the methods defined for this artifact. This will be limited to
	 * the methods defined locally in this artifact. If no method was defined
	 * return new IField[0].
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty array.
	 * 
	 * @see getInheritedIMethods()
	 * 
	 * @param filterFacetExcludedMethods -
	 *            if set to true, all methods that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all methods
	 *            are returned.
	 * @return IMethod[] - an array of all the methods for this artifact
	 */
	public Collection<IMethod> getMethods(boolean filterFacetExcludedMethods);

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
	public IProjectDescriptor getProjectDescriptor();

	/**
	 * Returns the standard specific details for this artifact.
	 * 
	 * @return
	 */
	public IStandardSpecifics getIStandardSpecifics();

	/**
	 * Returns an array containing all the artifacts that are implemented by
	 * this artifact;
	 * 
	 */
	public Collection<IAbstractArtifact> getImplementedArtifacts();

	/**
	 * Returns an array containing all the artifacts that are implementing this.
	 * 
	 * NOTE: this method is not implemented yet and returns an empty array.
	 * 
	 * @return Not implemented - returns an empty array
	 */
	public Collection<IAbstractArtifact> getImplementingArtifacts();

	/**
	 * Returns the inherited fields for this artifact. Only inherited fields
	 * will be included, but all inherited fields (ie from all ancestors) will
	 * be included. If no field was inherited return new IField[0].
	 * 
	 * This is equivalent to getInheritedIFields(false)
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty array.
	 * 
	 * @return IField[] - an array of all the inherited fields for this artifact
	 */
	public Collection<IField> getInheritedFields();

	/**
	 * Returns the inherited fields for this artifact. Only inherited fields
	 * will be included, but all inherited fields (ie from all ancestors) will
	 * be included. If no field was inherited return new IField[0].
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty array.
	 * 
	 * @param filterFacetExcludedFields -
	 *            if set to true, all fields that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all fields
	 *            are returned.
	 * @return IField[] - an array of all the inherited fields for this artifact
	 */
	public Collection<IField> getInheritedFields(boolean filterFacetExcludedFields);

	/**
	 * Returns the inherited literals for this artifact. Only inherited literals
	 * will be included, but all inherited literals (ie from all ancestors) will
	 * be included. If no literals was inherited return new IField[0].
	 * 
	 * this is equivalent to getInheritedLiterals(false)
	 * 
	 * Note : Some artifact types do not support literals and will always return
	 * an empty array.
	 * 
	 * @return Collection<ILiteral> - a collection of all the inherited literals for this artifact
	 */
	public Collection<ILiteral> getInheritedLiterals();

	/**
	 * Returns the inherited literals for this artifact. Only inherited literals
	 * will be included, but all inherited literals (ie from all ancestors) will
	 * be included. If no literals was inherited return new Iield[0].
	 * 
	 * Note : Some artifact types do not support literals and will always return
	 * an empty array.
	 * 
	 * @param filterFacetExcludedLiterals -
	 *            if set to true, all literals that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all literals
	 *            are returned.
	 * @return Collection<ILiteral> - a collection of all the inherited literals for this artifact
	 */
	public Collection<ILiteral> getInheritedLiterals(boolean filterFacetExcludedLiterals);

	/**
	 * Returns the inherited methods for this artifact. Only inherited methods
	 * will be included, but all inherited methods (ie from all ancestors) will
	 * be included. If no method was inherited return new IField[0].
	 * 
	 * This is equivalent to getInheritedIMethods(false)
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty array.
	 * 
	 * @return IMethod[] - an array of all the inherited methods for this
	 *         artifact
	 */
	public Collection<IMethod> getInheritedMethods();

	/**
	 * Returns the inherited methods for this artifact. Only inherited methods
	 * will be included, but all inherited methods (ie from all ancestors) will
	 * be included. If no method was inherited return new IField[0].
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty array.
	 * 
	 * @param filterFacetExcludedMethods -
	 *            if set to true, all methods that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all methods
	 *            are returned.
	 * @return IMethod[] - an array of all the inherited methods for this
	 *         artifact
	 */
	public Collection<IMethod> getInheritedMethods(boolean filterFacetExcludedMethods);


	/**
	 * Returns the package where this artifact is defined. If this artifact is
	 * defined in the default package, null is returned.
	 * 
	 * @return String - the package where this artifact was defined.
	 */
	public String getPackage();


	/**
	 * Returns an array containing all the artifacts being referenced (through a
	 * reference or attribute) from this artifact.
	 * 
	 * @return
	 */
	public Collection<IAbstractArtifact> getReferencedArtifacts();

	/**
	 * Returns an array with all the artifacts referencing this artifact.
	 * 
	 * NOTE: this is currently not implemented and will always return an empty
	 * Array.
	 * 
	 * @return Empty array
	 */
	public Collection<IAbstractArtifact> getReferencingArtifacts();

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
