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
package org.eclipse.tigerstripe.core.plugin.ossj.ws;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OssjWsdlUtil {

	/**
	 * remove the wsdl file extension
	 * 
	 * @param inString
	 * @return
	 */
	public String stripExtension(String inString) {
		if (inString.endsWith(".wsdl"))
			return inString.substring(0, inString.length() - 5);
		return inString;
	}

}