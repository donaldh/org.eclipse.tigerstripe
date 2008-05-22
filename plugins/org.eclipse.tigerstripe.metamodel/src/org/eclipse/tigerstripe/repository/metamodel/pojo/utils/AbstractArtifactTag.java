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
package org.eclipse.tigerstripe.repository.metamodel.pojo.utils;

/**
 * @author Eric Dillon
 * 
 * This interface is an enumeration of all supported tags
 * 
 */
public interface AbstractArtifactTag {

	/** This is the prefix separator for all tags */
	public final static String SEPARATOR = ".";

	/** This is the prefix to every tigerstripe tags */
	public final static String PREFIX = "tigerstripe" + SEPARATOR;

	/**
	 * Field marker
	 * 
	 * Field markers are used in Datatypes, ManagedEntities, Queries and events.
	 */
	public final static String FIELD = "field";
	public final static String METHOD = "method";
	public final static String LITERAL = "label";
	public final static String REFCOMMENT = "refComment";

	// =========================== MARKING TAGS
	public final static String DATATYPE = "datatype";
	public final static String EVENT = "event";
	public final static String MANAGEDENTITY = "managedEntity";
	public final static String QUERY = "query";
	public final static String SESSIONFACADE = "sessionFacade";
	public final static String ENUM = "enum";
	public final static String EXCEPTION = "exception";
	public final static String UPDATEPROC = "updateProcedure";
	public final static String ASSOCIATION = "association";
	public final static String ASSOCIATIONCLASS = "associationClass";
	public final static String PRIMITIVE = "primitive";
	public final static String DEPENDENCY = "dependency";

	// ===========================
}
