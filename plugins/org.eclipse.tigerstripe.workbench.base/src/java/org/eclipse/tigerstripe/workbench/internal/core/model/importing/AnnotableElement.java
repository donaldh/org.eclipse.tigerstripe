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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

import java.util.List;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * An element as extract from an AnnotableModel
 * 
 * @author Eric Dillon
 * 
 * @see org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModel
 */
public interface AnnotableElement extends AnnotableDatatype {

	public final static String AS_DATATYPE = "datatype";
	public final static String AS_ENTITY = "entity";
	public final static String AS_ENUMERATION = "enumeration";
	public final static String AS_ASSOCIATION = "association";
	public final static String AS_ASSOCIATIONCLASS = "associationClass";
	public final static String AS_DEPENDENCY = "dependency";
	public final static String AS_NAMEDQUERY = "namedQuery";
	public final static String AS_SESSIONFACADE = "sessionFacade";
	public final static String AS_NOTIFICATION = "notification";
	public final static String AS_EXCEPTION = "exception";
	public final static String AS_UPDATEPROC = "updateProcedure";
	public final static String AS_UNKNOWN = "unknown";

	/**
	 * The package this Element leaves in
	 */
	public AnnotableElementPackage getAnnotableElementPackage();

	public boolean isAbstract();

	public AnnotableElement getParentAnnotableElement();

	public void setParentAnnotableElement(AnnotableElement annotableElement);

	public List<AnnotableElementAttribute> getAnnotableElementAttributes();

	public List<AnnotableElementOperation> getAnnotableElementOperations();

	public List<AnnotableElementConstant> getAnnotableElementConstants();

	public AnnotableElementAttribute getAttributeByName(String name);

	public AnnotableElementConstant getConstantByName(String name);

	public AnnotableElementOperation getOperationBySignature(String signature);

	/**
	 * Returns the annotation type for this AnnotableElement
	 * 
	 * valid values are AS_DATATYPE, AS_ENTITY, AS_UNKNOWN, AS_ENUMERATION;
	 * 
	 * @return
	 */
	public String getAnnotationType();

	/**
	 * Sets the annotation type for this AnnotableElement
	 * 
	 * valid values are AS_DATATYPE, AS_ENTITY, AS_UNKNOWN, AS_ENUMERATION;
	 * 
	 * @param annotationType
	 */
	public void setAnnotationType(String annotationType);

	/**
	 * Returns the IAbstractArtifact corresponding to the mapped element once
	 * all annotations have been applied.
	 * 
	 * @param project
	 *            the target project that will host the artifact
	 * @return
	 * @throws TigerstripeException
	 */
	public IAbstractArtifact makeIArtifact(ITigerstripeModelProject project)
			throws TigerstripeException;

}
