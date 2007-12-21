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
package org.eclipse.tigerstripe.core.locale;

import java.text.MessageFormat;

/**
 * @author Eric Dillon
 * 
 * All error messages for Tigerstripe are grouped here.
 * 
 */
public class Messages {

	// Tigerstripe descriptor error
	public final static String INVALID_DESCRIPTOR = "Invalid Tigerstripe descriptor.";

	// Generic error messages
	public final static String UNKNOWN_ERROR = "Unknown error.";

	// Project related messages
	public final static String XML_PARSING_ERROR = "Parsing error: {0}, line {1}.";
	public final static String TIGERSTRIPE_XML_NOT_FOUND = "Cannot find tigerstripe project descriptor ({0})";
	public final static String NO_REPOSITORY_SPECIFIED = "At least 1 repository needs to be specified ({0}).";
	public final static String DIRECTORY_NOT_SPECIFIED = "Directory not specified in repository definition (line {0})";

	// Plugin related messages
	public final static String UNKNOWN_PLUGIN = "Unknown Plugin ({0})";
	public final static String UNKNOWN_ERROR_WHILE_LOADING_PLUGIN = "Unknown error while creating plugin housing for {0}.";
	public final static String INVALID_PLUGIN_REF = "Invalid plug-in reference: {0} is missing.";

	public static String formatMessage(String messageString, Object arg0) {
		MessageFormat mf = new MessageFormat(messageString);
		Object[] args = new Object[1];
		args[0] = arg0;
		return mf.format(args);
	}

	public static String formatMessage(String messageString, Object[] args) {
		MessageFormat mf = new MessageFormat(messageString);
		return mf.format(args);
	}

}