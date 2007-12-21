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
package org.eclipse.tigerstripe.api.profile.stereotype;

import org.dom4j.Element;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotype;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfile;

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
 * <li> <b>applicabilityScope</b>: the applicatibility scope of a stereotype
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
public interface IStereotype extends IextStereotype {

	public void setName(String name);

	public void setVersion(String version);

	public void setDescription(String description);

	/**
	 * Sets the visibility of this stereotype
	 * 
	 * @param isVisible
	 */
	public void setVisible(boolean isVisible);

	public void setAttributes(IStereotypeAttribute[] attributes)
			throws TigerstripeException;

	/**
	 * 
	 * @param attribute
	 * @throws TigerstripeException
	 *             if an attribute with the same name already exists.
	 */
	public void addAttribute(IStereotypeAttribute attribute)
			throws TigerstripeException;

	/**
	 * 
	 * @param attribute
	 * @throws TigerstripeException
	 *             if no attribute with that name exists.
	 */
	public void removeAttribute(IStereotypeAttribute attribute)
			throws TigerstripeException;

	public void removeAttributes(IStereotypeAttribute[] attribute)
			throws TigerstripeException;

	public void setRequiresList(String[] requiredStereotypeNames)
			throws TigerstripeException;

	public void addToRequiresList(String requiredStereotypeName)
			throws TigerstripeException;

	public void removeFromRequiresList(String requiredStereotypeName)
			throws TigerstripeException;

	public void removeFromRequiresList(String[] requiredStereotypeName)
			throws TigerstripeException;

	public void setExcludesList(String[] excludesStereotypeNames)
			throws TigerstripeException;

	public void addToExcludesList(String excludesStereotypeName)
			throws TigerstripeException;

	public void removeFromExcludesList(String excludesStereotypeName)
			throws TigerstripeException;

	public void removeFromExcludesList(String[] excludesStereotypeName)
			throws TigerstripeException;

	/**
	 * Returns an XML representation of this stereotype
	 * 
	 * @return
	 */
	public Element asElement();

	/**
	 * Parses the details of this from the given XML element
	 * 
	 * @param element
	 */
	public void parse(Element element) throws TigerstripeException;

	public void setParentStereotype(String parentStereotype);

	/**
	 * Makes an instance of this stereotype and initializes all the attributes
	 * to their default values.
	 * 
	 * @return
	 */
	public IStereotypeInstance makeInstance();

	public IStereotypeAttribute[] getAttributes();

	public IStereotypeAttribute getAttributeByName(String nameToGet);

	/**
	 * Returns the profile this stereotype belongs to.
	 * 
	 * @return
	 */
	public IWorkbenchProfile getProfile();
}
