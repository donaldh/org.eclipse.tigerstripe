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
package org.eclipse.tigerstripe.core.util;

/**
 * @author Eric Dillon
 * 
 * This is a convenience class that can be accessed from the default velocity
 * context with "$util"
 * 
 */
public class VelocityContextUtil {

	/**
	 * Turn the first character to upper-case
	 * 
	 * @param str
	 * @return
	 */
	public String capitalize(String str) {
		return str.substring(0, 1).toUpperCase()
				+ str.substring(1, str.length());
	}

	/**
	 * Returns a copy of the string given as argument and turns the first
	 * character into a lower case if necessary
	 * 
	 * @param string -
	 *            String the string
	 * @return String - first character is lowercase
	 */
	public static String unCapitalize(String string) {
		if (string == null)
			return null;
		return string.substring(0, 1).toLowerCase()
				+ string.substring(1, string.length());
	}

	public String toUpperCase(String str) {
		return str.toUpperCase();
	}

	public String toLowerCase(String str) {
		return str.toLowerCase();
	}

	public String getLastSegment(String inString) {
		return inString.substring(inString.lastIndexOf(".") + 1);
	}

	public int getArrayLength(Object[] in) {
		return in.length;
	}

	public boolean isArrayEmpty(Object[] in) {
		if (in.length > 0)
			return false;
		return true;
	}

	static public String stripExternalQuotes(String str) {
		if (str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"')
			return str.substring(1, str.length() - 1);
		else
			return str;

	}
}