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
package org.eclipse.tigerstripe.api.external.profile.stereotype;

/**
 * Top-level interface for user-defined stereotypes.
 * 
 * Users can define stereotypes within a Workbench Profile (@see
 * org.eclipse.tigerstripe.api.profile.IWorkbenchProfile) that will be offered
 * for selection when modeling a service contract.
 * 
 * Each Stereotype has the following properties
 * <ul>
 * <li> <b>name</b>: the name (as seen by the end-user) of the stereotype</li>
 * <li> <b>version</b>: the version of the stereotype, for maintenance purpose.
 * </li>
 * <li> <b>parentStereotype</b>: to define a hierarchy of stereotype. All
 * attributes are "public" and inherited.</li>
 * <li> <b>description</b>: a short description of the meaning of the
 * stereotype. This is used as ToolTip displayed to the end-user.</li>
 * <li> <b>visibility</b>: a stereotype must be visible to be presented to the
 * end-user. Since stereotypes can be defined as extensions of each others, this
 * allows for "top-level stereotypes" that gather commonalities between
 * stereotypes without having them visible to the end-user</li>
 * <li> <b>applicabilityScope</b>: the applicability scope of a stereotype
 * conditions where the end-user can use it, i.e. on an artifact, on an
 * attribute of an artifact, etc...</li>
 * <li> <b>stereotypeAttributes</b>: for each stereotype, optional attributes
 * can be defined so that the end-user can further specify the details of that
 * stereotype. For example, string-entry attributes, checkable attributes,
 * list-entry attributes, etc... Attributes can be marked as mandatory or
 * optional, and should have a default value</li>
 * <li> <b>requiresList</b>: the requires list defines which other stereotypes
 * are required for a stereotype to be valid. This allows to define dependencies
 * between multiple stereotypes that will be enforced when the end-user is
 * trying to use them.</li>
 * <li> <b>excludesList</b>: the excludes list defines which other stereotypes
 * can not be used in conjunction with a stereotype.</li>
 * </ul>
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IextStereotype {

	/**
	 * Returns the name (as seen by the end-user) of the stereotype.
	 * 
	 * @return the name (as seen by the end-user) of the stereotype
	 */
	public String getName();

	/**
	 * Returns the version of the stereotype, for maintenance purposes.
	 * 
	 * @return the version of the stereotype
	 */
	public String getVersion();

	/**
	 * Returns a short description of the meaning of the stereotype. This is
	 * used as ToolTip displayed to the end-user.
	 * 
	 * @return a short description of the meaning of the stereotype
	 */
	public String getDescription();

	/**
	 * Get the applicability scopes for this stereotype. The applicability scope
	 * of a stereotype conditions where the end-user can use it, i.e. on an
	 * artifact, on an attribute of an artifact, etc...
	 * 
	 * @return an IextStereotypeScopeDetails object that contains the
	 *         sepcification of the scope.
	 */
	public IextStereotypeScopeDetails getStereotypeScopeDetails();

	/**
	 * Returns the visibility of this stereotype. A stereotype must be visible
	 * to be presented to the end-user. Since stereotypes can be defined as
	 * extensions of each others, this allows for "top-level stereotypes" that
	 * gather commonalities between stereotypes without having them visible to
	 * the end-user.
	 * 
	 * @return true if this stereotype is to be presented to the user
	 */
	public boolean isVisible();

	/**
	 * Return an array of the defined attributes for this stereotype. If no
	 * attributes are defined then an empty array is returned.
	 * 
	 * For each stereotype, optional attributes can be defined so that the
	 * end-user can further specify the details of that stereotype. For example,
	 * string-entry attributes, checkable attributes, list-entry attributes,
	 * etc... Attributes can be marked as mandatory or optional, and should have
	 * a default value
	 * 
	 * @return array of the defined attributes for this stereotype.
	 */
	public IextStereotypeAttribute[] getIextAttributes();

	/**
	 * Returns a specific attribute identified by the name that is passed. If no
	 * matching attribute is found, then null is returned.
	 * 
	 * @param nameToGet
	 * @return attribute whose name matches the nameToGet parameter, or null.
	 */
	public IextStereotypeAttribute getIextAttributeByName(String nameToGet);

	/**
	 * Returns true if the given attribute is a defined attribute for this
	 * stereotype
	 * 
	 * @param attribute
	 * @return treu if the passed attribute defintion exactly matches one of the
	 *         defined attributes.
	 */
	public boolean isValidAttribute(IextStereotypeAttribute attribute);

	/**
	 * Get a array of the names of stereotypes that make up the required list.
	 * 
	 * The requires list defines which other stereotypes are required for a
	 * stereotype to be valid. This allows to define dependencies between
	 * multiple stereotypes that will be enforced when the end-user is trying to
	 * use them.
	 * 
	 * @return array of the names of stereotypes in the required list
	 */
	public String[] getRequiresList();

	/**
	 * Get a array of the names of stereotypes that make up the excluded list.
	 * 
	 * The excludes list defines which other stereotypes can not be used in
	 * conjunction with a stereotype.
	 * 
	 * @return array of the names of stereotypes in the excluded list
	 */
	public String[] getExcludesList();

	/**
	 * Returns the name of the parent of this stereotype (if any). If there is
	 * no defined parent, then return an empty string.
	 * 
	 * @return name of the parent stereotype
	 */
	public String getParentStereotype();

}
