/******************************************************************************* 
 * 
 * Copyright (c) 2008 Cisco Systems, Inc. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 *    Cisco Systems, Inc. - dkeysell
********************************************************************************/

package org.eclipse.tigerstripe.plugins.xml;

import java.lang.String;

public class EntityUtil {

	public String getFlag(String in){
		if (in.contains(":")){
			return in.substring(0,in.indexOf(":"));
		}
		if (in.contains(";")){
			return in.substring(0,in.indexOf(";"));
		}
		else return in;
	}
	
	/**
	 * 	Returns the path which is the package name transformed into a path description.
	 * 
	 * @return String - the path to be used
	 */
	public String convertToPath(String inString){
		return inString.replace(".", "/");
	}
	
	public String pathToRoot(String inString){
		String[] bits = inString.split("\\.");
		String outString = "";
		for(int i=0; i<bits.length; i++){
			outString = outString + "../";
		}
		return outString;
	}
	
/*	public String[] getExceptions(String in){
		return in;
	}
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
}
