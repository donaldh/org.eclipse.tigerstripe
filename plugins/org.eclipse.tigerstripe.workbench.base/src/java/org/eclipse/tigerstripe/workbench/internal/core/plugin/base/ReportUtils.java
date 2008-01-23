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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.base;

import java.util.Date;

import org.eclipse.tigerstripe.workbench.project.IPluginReference;

public class ReportUtils {

	public ReportUtils() {

	}

	public boolean isGeneratePlugin(int value) {
		if (value == IPluginReference.GENERATE_CATEGORY)
			return true;
		else
			return false;
	}

	public String stripPropertyString(String inString) {
		if (inString != null) {
			String outString = inString.replaceAll(">", "'");
			return outString.replaceAll("<", "'");
		} else
			return inString;
	}

	public String getTimeStamp() {
		java.util.Date date = new Date();
		java.text.Format dateformatter = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.text.Format timeformatter = new java.text.SimpleDateFormat(
				"HH.mm.ss");
		String s = dateformatter.format(date) + "T"
				+ timeformatter.format(date);
		return s;
	}

	public String xMLComment(String inString) {
		if (inString != null) {
			StringBuffer inBuff = new StringBuffer(inString);
			inBuff = replaceChar(inBuff, "<", "&lt;");
			inBuff = replaceChar(inBuff, ">", "&gt;");
			inBuff = replaceChar(inBuff, "\"", "&quot;");
			return inBuff.toString();
		} else
			return inString;
	}

	public StringBuffer replaceChar(StringBuffer inBuff, String what,
			String with) {
		while (inBuff.indexOf(what) != -1) {
			int pos = inBuff.indexOf(what);
			inBuff.deleteCharAt(pos);
			inBuff.insert(pos, with);
		}
		return inBuff;
	}

}
