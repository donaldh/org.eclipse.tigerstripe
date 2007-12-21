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
package org.eclipse.tigerstripe.api.external.model.artifacts;

import java.util.Collection;

import org.eclipse.tigerstripe.api.external.model.IextField;
import org.eclipse.tigerstripe.api.external.model.IextLabel;
import org.eclipse.tigerstripe.api.external.model.IextMethod;
import org.eclipse.tigerstripe.api.external.model.IextModelComponent;
import org.eclipse.tigerstripe.api.external.model.IextType;
import org.eclipse.tigerstripe.api.external.project.IextProjectDescriptor;
import org.eclipse.tigerstripe.api.external.project.IextTigerstripeProject;

/**
 * This interface implements what a user sees of an artifact when implementing
 * an Artifact-based rule.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IArtifact extends IextModelComponent {

	/**
	 * Returns the fully qualified name (ie. package + name) of this artifact.
	 * 
	 * @return String - fully qualified name of this artifact
	 */
	public String getFullyQualifiedName();

	/**
	 * Returns the package where this artifact is defined. If this artifact is
	 * defined in the default package, null is returned.
	 * 
	 * @return String - the package where this artifact was defined.
	 */
	public String getPackage();

	/**
	 * Returns the type of this artifact. This will be the FQN of the class of
	 * the specific type of artifact: eg
	 * "org.eclipse.tigerstripe.api.external.model.artifacts.IextEventArtifact"
	 * 
	 * @return String - the fully qualified type of this artifact
	 */
	public String getIArtifactType();

	/**
	 * Returns the Ancestors of this class if defined. If no ancestor was
	 * defined return new IArtifact[0].
	 * 
	 * @return IArtifact[] - an array of all ancestors to this artifact
	 */
	public IArtifact[] getAncestors();

	/**
	 * Returns the fields defined for this artifact. This will be limited to the
	 * fields defined locally in this artifact. If no field was defined return
	 * new IextField[0].
	 * 
	 * This is equivalent to getIextFields(false);
	 * 
	 * Note : Some artifact types do not support Fields and will always return
	 * an empty array.
	 * 
	 * @see getInheritedIextFields()
	 * 
	 * @return IextField[] - an array of all the fields for this artifact
	 */
	public IextField[] getIextFields();

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
	public IextField[] getIextFields(boolean filterFacetExcludedFields);

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
	public IextField[] getInheritedIextFields();

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
	public IextField[] getInheritedIextFields(boolean filterFacetExcludedFields);

	/**
	 * Returns the labels defined for this artifact. This will be limited to the
	 * labels defined locally in this artifact. If no label was defined return
	 * new IextField[0].
	 * 
	 * This is equivalent to getIextLabels(false)
	 * 
	 * Note : Some artifact types do not support Labels and will always return
	 * an empty array.
	 * 
	 * @see getInheritedIextLabels()
	 * 
	 * @return IextLabel[] - an array of all the labels for this artifact
	 */
	public IextLabel[] getIextLabels();

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
	public IextLabel[] getIextLabels(boolean filterFacetExcludedLabels);

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
	public IextLabel[] getInheritedIextLabels();

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
	public IextLabel[] getInheritedIextLabels(boolean filterFacetExcludedLabels);

	/**
	 * Returns the methods defined for this artifact. This will be limited to
	 * the methods defined locally in this artifact. If no method was defined
	 * return new IextField[0].
	 * 
	 * this is equivalent to getIextMethods(false)
	 * 
	 * Note : Some artifact types do not support Methods and will always return
	 * an empty array.
	 * 
	 * @see getInheritedIextMethods()
	 * 
	 * @return IextMethod[] - an array of all the methods for this artifact
	 */
	public IextMethod[] getIextMethods();

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
	public IextMethod[] getIextMethods(boolean filterFacetExcludedMethods);

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
	public IextMethod[] getInheritedIextMethods();

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
	public IextMethod[] getInheritedIextMethods(
			boolean filterFacetExcludedMethods);

	/**
	 * Returns the IArtifact which this artifact extends. If there is no extends
	 * clause, null is returned.
	 * 
	 * @return IArtifact - the extended artifact
	 */
	public IArtifact getExtendedIArtifact();

	/**
	 * Returns true if this artifact extends another artifact.
	 * 
	 * @return true if this artifacts extends another artifact, false otherwise.
	 */
	public boolean hasExtends();

	public Collection<String> getReferencedArtifacts();

	/**
	 * Returns an array containing all the artifacts being referenced (through a
	 * reference or attribute) from this artifact.
	 * 
	 * @return
	 */
	public IArtifact[] getReferencedIArtifacts();

	/**
	 * Returns an array with all the artifacts referencing this artifact.
	 * 
	 * NOTE: this is currently not implemented and will always return an empty
	 * Array.
	 * 
	 * @return Empty array
	 */
	public IArtifact[] getReferencingIArtifacts();

	/**
	 * Returns the project that contains this artifact.
	 * 
	 * NOTE: if this is contained in a module, the return is null.
	 * {@link #getIextProjectDescriptor()}
	 * 
	 * @return the containing Project.
	 */
	public IextTigerstripeProject getIextTigerstripeProject();

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
	public IextProjectDescriptor getIextProjectDescriptor();

	/**
	 * Returns true if this Artifact is marked as abstract.
	 * 
	 * @return true if the artifact is abstract, false otherwise
	 */
	public boolean isAbstract();

	/**
	 * Returns the standard specific details for this artifact.
	 * 
	 * @return
	 */
	public IextStandardSpecifics getIextStandardSpecifics();

	/**
	 * Returns an array of all the field types for this artifact.
	 * 
	 * @return
	 */
	public IextFieldTypeRef[] getFieldIextTypes();

	// public IextArtifactManagerSession getIextArtifactManagerSession();

	/**
	 * Returns an array containing all the artifacts that are implemented by
	 * this artifact;
	 * 
	 */
	public IArtifact[] getImplementedArtifacts();

	/**
	 * Returns an array containing all the artifacts that are implementing this.
	 * 
	 * NOTE: this method is not implemented yet and returns an empty array.
	 * 
	 * @return Not implemented - returns an empty array
	 */
	public IArtifact[] getImplementingIArtifacts();

	/**
	 * Returns the list of artifacts that are extending "this" artifact.
	 * 
	 * @return
	 * @since 2.2-beta
	 */
	public IArtifact[] getExtendingIArtifacts();

	public interface IextFieldTypeRef {

		public int getRefBy();

		public boolean isRefByValue();

		public boolean isRefByKey();

		public boolean isRefByKeyResult();

		public IextType getIextType();
	}

}
