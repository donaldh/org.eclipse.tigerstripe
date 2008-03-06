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
package org.eclipse.tigerstripe.workbench.internal.api.profile.properties;

import org.eclipse.tigerstripe.workbench.internal.api.profile.IWorkbenchProfileProperty;

public interface IOssjLegacySettigsProperty extends IWorkbenchProfileProperty {

	public final static String USEATTRIBUTES_ASREFERENCE = "useAttributesAsReference";
	public final static String DISPLAY_OSSJSPECIFICS = "displayOssjSpecifics";
	public final static String USEREFBY_MODIFIERS = "useRefbyModifiers";
	public final static String ENABLE_INSTANCEMETHOD = "enableInstanceMethods";
	public final static String ENABLE_ISOPTIONAL = "enableIsOptional";

	public final static String USEMANAGEDENTITIES_ONSESSION = "useManagedEntitiesOnSession";
	public final static String USEEMITTEDNOTIFICATIONS_ONSESSION = "useEmittedNotificationsOnSession";
	public final static String USEEXPOSEDPROCEDURES_ONSESSION = "useExposedUpdateProceduresOnSession";
	public final static String USENAMEDQUERIES_ONSESSION = "useNamedQueriesOnSession";

	//public final static String ENABLE_JVT_PLUGIN = "enableJVTLegacyPlugin";
	//public final static String ENABLE_XML_PLUGIN = "enableXMLLegacyPlugin";
	//public final static String ENABLE_WSDL_PLUGIN = "enableWSDLLegacyPlugin";

}
