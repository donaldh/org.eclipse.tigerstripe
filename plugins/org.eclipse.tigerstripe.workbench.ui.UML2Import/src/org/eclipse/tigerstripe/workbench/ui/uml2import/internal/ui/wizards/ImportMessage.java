/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;

public class ImportMessage extends Message {
	
	public final static int ARTIFACT = 4;
	public final static int WARNING = 5;
	public final static int INFO = 6;


	private final static String[] messageTags = { "ERROR", "WARNING", "INFO",
			"UNKNOWN", "ARTIFACT" };
	
	
	public static String severityToString(int severity) {
		return messageTags[severity];
	}
	
}
