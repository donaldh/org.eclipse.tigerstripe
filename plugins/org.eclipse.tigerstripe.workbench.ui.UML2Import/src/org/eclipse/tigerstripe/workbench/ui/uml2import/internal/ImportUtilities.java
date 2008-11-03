/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Dictionary;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.osgi.framework.Bundle;

public class ImportUtilities {

	
	public static void printHeaderInfo(PrintWriter out)
	{
		out.println("================= Header =============================================");
		// a) Timestamp
		out.println(ImportUtilities.getTimeStamp());
		
		// b) Java info
		out.println("java.runtime.name = "+System.getProperty("java.runtime.name"));
		out.println("java.version = "+System.getProperty("java.version"));
		
		// c) eclipse info
		out.println("eclipse.buildId = "+System.getProperty("eclipse.buildId"));
		
		// d) Tigerstripe Info
		printBundle(out, "org.eclipse.tigerstripe.workbench.base");
		
		// e) Import version
		printBundle(out, "org.eclipse.tigerstripe.workbench.ui.UML2Import");
		
		out.println("================= End of Header ======================================");
	}

	public static void printBundle(PrintWriter out, String bundleName){
		Bundle bundle = Platform.getBundle(bundleName);
		try{
			Dictionary<String, String> dict = bundle.getHeaders();

			out.println(bundleName+" ("+dict.get("Bundle-Version")+") \""+dict.get("Bundle-Name")+"\"");
		} catch (Exception e){
			// who cares?

		}
	}
	
	
	public static String getTimeStamp() {
		java.util.Date date = new Date();
		java.text.Format dateformatter = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.text.Format timeformatter = new java.text.SimpleDateFormat(
				"HH.mm.ss");
		String s = "["+dateformatter.format(date) + ""
				+ timeformatter.format(date)+ "]";
		return s;
	}

	/**
	 * check for invalid constructs
	 * 
	 * eg "." in name - map to "_" "-" in name - map to "_" " " in name - map to
	 * "_"
	 * 
	 * 
	 * 
	 * @param name
	 * @return
	 */
	public static String nameCheck(String name, MessageList messages,PrintWriter out) {
		if (name != null && name.length()!=0) {
			String inName = name.trim();
			if (inName.contains(" ")) {
				inName = inName.replaceAll(" ", "_");
			}
			if (inName.contains("-")) {
				inName = inName.replaceAll("-", "_");
			}
			if (inName.contains(".")) {
				inName = inName.replaceAll("\\.", "_");
			}
	
			if (!inName.equals(name) ) {
				String msgText = " Name mapped : " + name + " -> " + inName;
				if (messages != null){
					addMessage(msgText, 1, messages);
				}
				if (out != null){
					out.println("WARN:" + msgText);
				}
			}
	
			return inName;
		} else {
			return null;
		}
	}

	public static void addMessage(String msgText, int severity, MessageList messages) {
		if (messages != null){
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(severity);
			messages.addMessage(newMsg);
		}
	}

	/**
		 * map naming to TS compatible style
		 * 
		 */
		public static String convertToFQN(String name, MessageList messages, PrintWriter out) {
			if (name != null) {
	
				String dottedName = "";
				String[] segments = name.split("::");
				for (int i=0;i<segments.length-1;i++){
					String segmentName = segments[i];
	                // Make sure the packages all start with a lower case letter
	//				String segmentName =  segments[i].substring(0,1).toLowerCase()+segments[i].substring(1);
	//				if (! segmentName.substring(0,1).equals(segments[i].substring(0,1)) && i!=0){
	//					String msgText = " Package Name Segment mapped : " + segments[i] + " -> " + segmentName;
	//					addMessage(msgText, 1, messages);
	//					out.println("WARN :" + msgText);
	//				}
					if (i==0){
						//dottedName = nameCheck(segmentName);
						dottedName = segmentName;
					} else {
						dottedName = dottedName+"."+ImportUtilities.nameCheck(segmentName, messages, out);
					}
				}
				dottedName = dottedName+"."+ImportUtilities.nameCheck(segments[segments.length-1], messages, out);
				return dottedName.substring(dottedName.indexOf(".")+1);
			} else {
				return null;
			}
		}
	
}
