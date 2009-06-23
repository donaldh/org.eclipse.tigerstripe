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

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.osgi.framework.Bundle;

public class ImportUtilities {

	private static Map<String,String> mappings = new HashMap<String, String>();
	
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

	public static void setMappings(Map<String,String> inMappings){
		mappings = inMappings;
	}
	
	private static Map<String,String> getMappings(){
		return mappings;
	}
	
	/**
	 * check for invalid constructs
	 * 
	 * @param name
	 * @return
	 */
	public static String nameCheck(String name, MessageList messages,PrintWriter out) {
		
		String prefix = "_";
		
		if (name != null && name.length()!=0) {
			
			String cleanName = name.trim();
			// A check for reserved words
			if (isRservedWord(cleanName)){
				cleanName = prefix+cleanName;
			}
			// Check for numerical prefixes
			if (cleanName.matches("^[0-9].*")){
				cleanName = prefix+cleanName;
			}
			// Check for dodgy characters.
			for (String exp : getMappings().keySet()){
				try{
					if ( cleanName.contains(exp)){
						if (exp.equals("."))
							cleanName = cleanName.replaceAll("\\.", getMappings().get(exp));
						else if (exp.equals("("))		
							cleanName = cleanName.replaceAll("\\(", getMappings().get(exp));
						else if (exp.equals("{"))		
							cleanName = cleanName.replaceAll("\\{", getMappings().get(exp));
						else if (exp.equals("["))		
							cleanName = cleanName.replaceAll("\\[", getMappings().get(exp));
						else if (exp.equals(")"))		
							cleanName = cleanName.replaceAll("\\)", getMappings().get(exp));
						else		
							cleanName = cleanName.replaceAll(exp, getMappings().get(exp));
					}
				} catch (PatternSyntaxException p){
					String msgText = "Invalid Pattern defintion '"+exp+"'";
					if (messages != null){
						addMessage(msgText, 1, messages);
					}
					if (out != null){
						out.println("WARN:" + msgText);
					}
				}
			}
	
			if (!cleanName.equals(name) ) {
				String msgText = " Name mapped : " + name + " -> " + cleanName;
				if (messages != null){
					addMessage(msgText, 1, messages);
				}
				if (out != null){
					out.println("WARN:" + msgText);
				}
			}
	
			return cleanName;
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
	public static String convertToFQN(String modelName, String name, MessageList messages, PrintWriter out) {
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
					dottedName = ImportUtilities.nameCheck(segmentName, messages, out);
				} else {
					dottedName = dottedName+"."+ImportUtilities.nameCheck(segmentName, messages, out);
				}
			}
			dottedName = dottedName+"."+ImportUtilities.nameCheck(segments[segments.length-1], messages, out);
			// This removes the model name if its there
			// rememberr model name may have had underscores replaced
			if (dottedName.substring(0,dottedName.indexOf(".")).equals(ImportUtilities.nameCheck(modelName,messages,out))){
				return dottedName.substring(dottedName.indexOf(".")+1);
			} else{
				return dottedName;
			}
		} else {
			return null;
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
						dottedName = ImportUtilities.nameCheck(segmentName, messages, out);
					} else {
						dottedName = dottedName+"."+ImportUtilities.nameCheck(segmentName, messages, out);
					}
				}
				dottedName = dottedName+"."+ImportUtilities.nameCheck(segments[segments.length-1], messages, out);
				// This removes the model name
				return dottedName.substring(dottedName.indexOf(".")+1);
			} else {
				return null;
			}
		}
	
		private static List<String> reserved = Arrays.asList(
				"abstract","continue","for","new","switch","assert",
				"default","goto","package","synchronized",
				"boolean","do","if","private","this",
				"break","double","implements","protected","throw",
				"byte","else","import","public","throws",
				"case","enum","instanceof","return","transient",
				"catch","extends","int","short","try",
				"char","final","interface","static","void",
				"class","finally","long","strictfp","volatile",
				"const","float","native","super","while");
		
		public static boolean isRservedWord(String word){
			return reserved.contains(word);
		}
		
		
}
