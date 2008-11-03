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
import org.osgi.framework.Bundle;

public class ImportLogUtilities {

	
	public static void printHeaderInfo(PrintWriter out)
	{
		out.println("================= Header =============================================");
		// a) Timestamp
		out.println(ImportLogUtilities.getTimeStamp());
		
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
	
}
