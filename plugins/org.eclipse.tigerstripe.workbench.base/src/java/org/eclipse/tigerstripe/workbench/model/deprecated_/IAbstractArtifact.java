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
package org.eclipse.tigerstripe.workbench.model.deprecated_;

import java.io.Writer;
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IStandardSpecifics;
import org.eclipse.tigerstripe.workbench.project.IProjectDescriptor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Abstract Artifact for a Tigerstripe Model.
 * 
 * 
 * @author Eric Dillon
 */
public interface IAbstractArtifact extends IModelComponent {

	public final static IAbstractArtifact[] EMPTY_ARRAY = new IAbstractArtifact[0];

	public interface IFieldTypeRef {

		public int getRefBy();

		public boolean isRefByValue();

		public boolean isRefByKey();

		public boolean isRefByKeyResult();

		public IType getType();
	}

	// ==================================================

	/**
	 * Returns the type of this artifact. This will be the FQN of the class of
	 * the specific type of artifact: eg
	 * "org.eclipse.tigerstripe.api.external.model.artifacts.IEventArtifact"
	 * 
	 * @return String - the fully qualified type of this artifact
	 */
	public String getArtifactType();

	/**
	 * Returns true if this Artifact is marked as abstract.
	 * 
	 * @return true if the artifact is abstract, false otherwise
	 */
	public boolean isAbstract();

	// ==================================================

	/**
	 * Sets the value of the isAbstract flag for this.
	 * 
	 * @param isAbstract
	 */
	public void setAbstract(boolean isAbstract);

	/**
	 * Returns the fully qualified name (ie. package + name) of this artifact.
	 * 
	 * @return String - fully qualified name of this artifact
	 */
	public String getFullyQualifiedName();

	/**
	 * Sets the Fully Qualified Name of the Abstract Artifact.
	 * 
	 * This sets both the package and name.
	 * 
	 * The format of the fqn should be the package name and the name, using '.'
	 * as the seperator.
	 * 
	 * @param fqn
	 *            - the FullyQualifiedName for this Artifact.
	 */
	public void setFullyQualifiedName(String fqn);

	/**
	 * Returns the package where this artifact is defined. If this artifact is
	 * defined in the default package, null is returned.
	 * 
	 * @return String - the package where this artifact was defined.
	 */
	public String getPackage();

	/**
	 * 
	 * Sets the package of the artifact.
	 * 
	 * The parts of the package name should be separated using '.' as the
	 * seperator.
	 * 
	 * @param fqn
	 *            - the FullyQualifiedName for this Artifact.
	 */
	public void setPackage(String packageName);

	// ==================================================

	/**
	 * Returns all children for this artifact
	 * 
	 * Children are Fields, Methods, Literals, and potentially ends for
	 * IRelationships
	 * 
	 */
	public Collection<Object> getChildren();

	/**
	 * Returns the fields defined for this artifact. This will be limited to the
	 * fields defined locally in this artifact. If no field was defined return
	 * an empty collection.
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty collection.
	 * 
	 * @see getInheritedIFields()
	 * 
	 * @return Collection<IField> - an unmodifiable collection of all the fields
	 *         for this artifact
	 */
	public Collection<IField> getFields();

	/**
	 * Returns the fields defined for this artifact. This will be limited to the
	 * fields defined locally in this artifact. If no field was defined return
	 * an empty collection.
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty collection.
	 * 
	 * @see getInheritedIFields()
	 * @param filterFacetExcludedFields
	 *            - if set to true, all fields that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all fields
	 *            are returned.
	 * 
	 * @return Collection<IField> - an unmodifiable collection of all the fields
	 *         for this artifact
	 */
	public Collection<IField> getFields(boolean filterFacetExcludedFields);

	/**
	 * Returns the inherited fields for this artifact. Only inherited fields
	 * will be included, but all inherited fields (ie from all ancestors) will
	 * be included. If no field was inherited return an empty collection.
	 * 
	 * This is equivalent to getInheritedIFields(false)
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty array.
	 * 
	 * return Collection<IField> - an unmodifiable collection of all the
	 * inherited fields for this artifact
	 */
	public Collection<IField> getInheritedFields();

	/**
	 * Returns the inherited fields for this artifact. Only inherited fields
	 * will be included, but all inherited fields (ie from all ancestors) will
	 * be included. If no field was inherited return an empty collection.
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty collection.
	 * 
	 * @param filterFacetExcludedFields
	 *            - if set to true, all fields that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all fields
	 *            are returned. return Collection<IField> - an unmodifiable
	 *            collection of all the inherited fields for this artifact
	 */
	public Collection<IField> getInheritedFields(
			boolean filterFacetExcludedFields);

	/**
	 * Make a new blank artifact field.
	 * 
	 * This does not add the field to the Abstract Artifact.
	 * 
	 * @return a new IField.
	 */
	public IField makeField();

	/**
	 * Sets the fields for this Abstract Artifact.
	 * 
	 * @param fields
	 * @throws IllegalArgumentException
	 */
	public void setFields(Collection<IField> fields);

	/**
	 * Add a single field to the fields for this Abstract Artifact.
	 * 
	 * @param field
	 *            to add.
	 */
	public void addField(IField field);

	/**
	 * Removes a number of fields from this Abstract Artifact.
	 * 
	 * @param fields
	 *            to be removed.
	 */
	public void removeFields(Collection<IField> fields);

	/**
	 * Returns the literals defined for this artifact. This will be limited to
	 * the literals defined locally in this artifact. If no literal was defined
	 * return an empty collection.
	 * 
	 * Note : Some artifact types do not support literals and will always return
	 * an empty collection.
	 * 
	 * @see getInheritedLietrals()
	 * 
	 * @return Collection<ILiteral> - an unmodifiable collection of all the
	 *         literals for this artifact
	 */
	public Collection<ILiteral> getLiterals();

	/**
	 * Returns the literals defined for this artifact. This will be limited to
	 * the literals defined locally in this artifact. If no literal was defined
	 * return an empty collection.
	 * 
	 * Note : Some artifact types do not support literals and will always return
	 * an empty collection.
	 * 
	 * @see getInheritedLietrals()
	 * 
	 * @param filterFacetExcludedliterals
	 *            - if set to true, all literals that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all
	 *            literals are returned.
	 * @return Collection<ILiteral> - an unmodifiable collection of all the
	 *         literals for this artifact
	 */
	public Collection<ILiteral> getLiterals(boolean filterFacetExcludedLiterals);

	/**
	 * Returns the inherited literals for this artifact. Only inherited literals
	 * will be included, but all inherited literals (ie from all ancestors) will
	 * be included. If no literals was inherited return an empty collection.
	 * 
	 * this is equivalent to getInheritedLiterals(false)
	 * 
	 * Note : Some artifact types do not support literals and will always return
	 * an empty collection.
	 * 
	 * @return Collection<ILiteral> - an unmodifiable collection of all the
	 *         inherited literals for this artifact
	 */
	public Collection<ILiteral> getInheritedLiterals();

	/**
	 * Returns the inherited literals for this artifact. Only inherited literals
	 * will be included, but all inherited literals (ie from all ancestors) will
	 * be included. If no literals was inherited return an empty collection.
	 * 
	 * Note : Some artifact types do not support literals and will always return
	 * an empty collection.
	 * 
	 * @param filterFacetExcludedLiterals
	 *            - if set to true, all literals that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all
	 *            literals are returned.
	 * @return Collection<ILiteral> - an unmodifiable collection of all the
	 *         inherited literals for this artifact
	 */
	public Collection<ILiteral> getInheritedLiterals(
			boolean filterFacetExcludedLiterals);

	/**
	 * Make a new blank artifact literal.
	 * 
	 * This does not add the literal to the Abstract Artifact.
	 * 
	 * @return a new ILiteral
	 */
	public ILiteral makeLiteral();

	/**
	 * Sets the literals for this Abstract Artifact.
	 * 
	 * @param literals
	 * @throws IllegalArgumentException
	 */
	public void setLiterals(Collection<ILiteral> literals);

	/**
	 * Add a single literal to the literals for this Abstract Artifact.
	 * 
	 * @param literal
	 *            to add.
	 */
	public void addLiteral(ILiteral literal);

	/**
	 * Removes a number of literals from this Abstract Artifact.
	 * 
	 * @param literals
	 *            to be removed.
	 */
	public void removeLiterals(Collection<ILiteral> literals);

	/**
	 * Returns the methods defined for this artifact. This will be limited to
	 * the methods defined locally in this artifact. If no method was defined
	 * return an empty collection.
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty collection.
	 * 
	 * @see getInheritedIMethods()
	 * 
	 * @return Collection<IMethod> - an unmodifiable collection of all the
	 *         methods for this artifact
	 */
	public Collection<IMethod> getMethods();

	/**
	 * Returns the methods defined for this artifact. This will be limited to
	 * the methods defined locally in this artifact. If no method was defined
	 * return an empty collection.
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty collection.
	 * 
	 * @see getInheritedIMethods()
	 * 
	 * @param filterFacetExcludedMethods
	 *            - if set to true, all methods that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all methods
	 *            are returned.
	 * @return Collection<IMethod> - an unmodifiable collection of all the
	 *         methods for this artifact
	 */
	public Collection<IMethod> getMethods(boolean filterFacetExcludedMethods);

	/**
	 * Returns the inherited methods for this artifact. Only inherited methods
	 * will be included, but all inherited methods (ie from all ancestors) will
	 * be included. If no method was inherited return an empty collection.
	 * 
	 * This is equivalent to getInheritedIMethods(false)
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty collection.
	 * 
	 * @return Collection<IMethod> - an unmodifiable collection of all the
	 *         inherited methods for this artifact
	 */
	public Collection<IMethod> getInheritedMethods();

	/**
	 * Returns the inherited methods for this artifact. Only inherited methods
	 * will be included, but all inherited methods (ie from all ancestors) will
	 * be included. If no method was inherited return an empty collection.
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty collection.
	 * 
	 * @param filterFacetExcludedMethods
	 *            - if set to true, all methods that are excluded by the active
	 *            facet will be filtered out. If no facet is active, all methods
	 *            are returned.
	 * @return Collection<IMethod> - an unmodifiable collection of all the
	 *         inherited methods for this artifact
	 */
	public Collection<IMethod> getInheritedMethods(
			boolean filterFacetExcludedMethods);

	/**
	 * Make a new blank artifact method.
	 * 
	 * This does not add the method to the Abstract Artifact.
	 * 
	 * @return a new IMethod
	 */
	public IMethod makeMethod();

	/**
	 * Sets the methods for this Abstract Artifact.
	 * 
	 * @param methods
	 * @throws IllegalArgumentException
	 */
	public void setMethods(Collection<IMethod> methods);

	/**
	 * Add a single method to the methods for this Abstract Artifact.
	 * 
	 * @param method
	 *            to add.
	 */
	public void addMethod(IMethod method);

	/**
	 * Removes a number of methods from this Abstract Artifact.
	 * 
	 * @param methods
	 *            to be removed.
	 */
	public void removeMethods(Collection<IMethod> methods);

	/**
	 * Returns the IAbstractArtifact which this artifact extends. If there is no
	 * extends clause, null is returned.
	 * 
	 * @return IAbstractArtifact - the extended artifact
	 */
	public IAbstractArtifact getExtendedArtifact();

	/**
	 * Returns true if this artifact extends another artifact.
	 * 
	 * @return true if this artifacts extends another artifact, false otherwise.
	 */
	public boolean hasExtends();

	/**
	 * Sets the IAbstractArtifact which this artifact extends.
	 * 
	 * @param artifact
	 */
	public void setExtendedArtifact(IAbstractArtifact artifact);

	/**
	 * Sets the extended artifact from its FQN. If a proxy artifact needs to be
	 * created (because the given FQN doesn't correspond to an existing artifact
	 * it will)
	 * 
	 * @param fqn
	 */
	public void setExtendedArtifact(String fqn);

	/**
	 * Returns the Ancestors of this class if defined. If no ancestor was
	 * defined return an empty collection.
	 * 
	 * @return Collection<IAbstractArtifact> - an unmodifiable collection of all
	 *         ancestors to this artifact
	 */
	public Collection<IAbstractArtifact> getAncestors();

	/**
	 * Returns a collection of all the artifacts that directly extend this
	 * artifact.
	 * 
	 * @return Collection<IAbstractArtifact> - artifacts that directly extend
	 *         this artifact
	 */
	public Collection<IAbstractArtifact> getExtendingArtifacts();

	/**
	 * Returns a collection containing all the artifacts that are implemented by
	 * this artifact.
	 * 
	 * @return Collection<IAbstractArtifact> - artifacts that are implemented by
	 *         this artifact
	 */
	public Collection<IAbstractArtifact> getImplementedArtifacts();

	/**
	 * Returns a string containing a ';' separated list of FullyQualifiedNames
	 * of the artifacts that are implemented by this artifact.
	 * 
	 * @return - ';' separated list of fqns of implemented artifacts
	 */
	public String getImplementedArtifactsAsStr();

	/**
	 * Returns a collection containing all the artifacts that are implementing
	 * this.
	 * 
	 * NOTE: this method is not implemented yet and returns an empty collection.
	 * 
	 * @return Not implemented - returns an empty collection
	 */
	public Collection<IAbstractArtifact> getImplementingArtifacts();

	/**
	 * Set the artifacts that are implemented by this artifact.
	 * 
	 * @param artifacts
	 *            that are implemented by this artifact
	 */
	public void setImplementedArtifacts(Collection<IAbstractArtifact> artifacts);

	/**
	 * Returns a collection containing all the artifacts being referenced
	 * (through a reference or attribute) from this artifact.
	 * 
	 * @return
	 */
	public Collection<IAbstractArtifact> getReferencedArtifacts();

	/**
	 * Returns a collection with all the artifacts referencing this artifact.
	 * 
	 * NOTE: this is currently not implemented and will always return an empty
	 * collection.
	 * 
	 * @return Empty collection
	 */
	public Collection<IAbstractArtifact> getReferencingArtifacts();

	/**
	 * Returns the details contained in the project that this artifact belongs
	 * to.
	 * 
	 * NOTE: this is always populated, as opposed to
	 * {@link #ITigerstripeProject()} which returns null for artifacts contained
	 * in Modules.
	 * 
	 * @return - project descriptor for the containing project
	 */
	public IProjectDescriptor getProjectDescriptor();

	/**
	 * Returns the Model project that contains this artifact. This will be null
	 * for artifacts contained in modules.
	 * 
	 * @see getProjectDescriptor()
	 * 
	 * @return - the containing project
	 */
	public ITigerstripeModelProject getTigerstripeProject();

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
	// DON'T THINK THESE ARE CORRECT TO BE INCLUDED

	public String getArtifactPath() throws TigerstripeException;

	// ==================================================
	// UP FOR REVISION

	public void doSave(IProgressMonitor monitor) throws TigerstripeException;

	public void doSilentSave(IProgressMonitor monitor)
			throws TigerstripeException;

	public void write(Writer writer) throws TigerstripeException;

	public String asText() throws TigerstripeException;

	/**
	 * Figures out the updater to use to submit IModelChangeRequests regarding
	 * this artifact.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IModelUpdater getUpdater() throws TigerstripeException;

	// ==================================================

	/**
	 * Returns an array of all the field types for this artifact.
	 * 
	 * @return
	 */
	public IFieldTypeRef[] getFieldTypes();

	/**
	 * Returns the standard specific details for this artifact.
	 * 
	 * @return
	 */
	public IStandardSpecifics getIStandardSpecifics();

	public IModuleHeader getParentModuleHeader();
	
	public IAbstractArtifact makeWorkingCopy(IProgressMonitor monitor) throws TigerstripeException;
}
