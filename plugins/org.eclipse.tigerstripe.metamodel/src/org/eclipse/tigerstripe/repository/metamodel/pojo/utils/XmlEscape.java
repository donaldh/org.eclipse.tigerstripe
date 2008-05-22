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
 * This class is used to make sure that we don't include "bad" characters in the
 * POJO comments.
 * 
 * It is in effect using XML escape rules, augmented with special handling for
 * javadoc special characters
 * 
 * @author erdillon
 * 
 */
public class XmlEscape {

	/**
	 * encodes the given string with XML conventions
	 * 
	 * @param inputString
	 * @return
	 */
	public String encode(String str) {
		if (str == null)
			return "";

		StringBuffer sb = new StringBuffer();
		char[] data = str.toCharArray();
		char c, lastC = 0x0;
		for (char element : data) {
			c = element;
			if (c == '"') {
				sb.append("&quot;");
			} else if (c == '\'') {
				sb.append("&apos;");
			} else if (c == '&') {
				sb.append("&amp;");
			} else if (c == '/' && lastC == '*') { // added to handle Javadoc
				// comments
				sb.deleteCharAt(sb.length() - 1);
				sb.append("&eCom;");
				lastC = 0x0;
				continue;
			} else if (c == '*' && lastC == '/') { // added to handle Javadoc
				// comments
				sb.deleteCharAt(sb.length() - 1);
				sb.append("&bCom;");
				lastC = 0x0;
				continue;
			} else {
				sb.append(c);
			}
			lastC = c;
		}
		return sb.toString();
	}

	public String decode(String str) {
		if (str == null)
			return "";
		StringBuffer sb = new StringBuffer();
		int i;
		for (i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '&') {
				int end = str.indexOf(';', i + 1);

				// if we found no amperstand then we are done
				if (end == -1) {
					sb.append(c);
					continue;
				}
				String entity = str.substring(i + 1, end);
				i += entity.length() + 1;
				if (entity.equals("amp")) {
					sb.append('&');
				} else if (entity.equals("lt")) {
					sb.append('<');
				} else if (entity.equals("gt")) {
					sb.append('>');
				} else if (entity.equals("quot")) {
					sb.append('"');
				} else if (entity.equals("apos")) {
					sb.append('\'');
				} else if (entity.equals("eCom")) { // added to handle javadoc
					// comments
					sb.append("*/");
				} else if (entity.equals("bCom")) { // added to handle javadoc
					// comments
					sb.append("/*");
				} else {
					sb.append("&" + entity + ";");
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();

	}
}
