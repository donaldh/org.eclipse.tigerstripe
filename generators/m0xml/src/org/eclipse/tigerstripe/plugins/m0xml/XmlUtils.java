/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    S. Jerman (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.plugins.m0xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;

public class XmlUtils {
	
	public static String getArtifactType(IAbstractArtifact artifact){
		return "Unkown";
	}

	public static String getArtifactType(IDatatypeArtifact artifact){
		return "ComplexType";
	}
	public static String getArtifactType(IEnumArtifact artifact){
		return "Enumeration";
	}

	public static String getArtifactType(IAssociationClassArtifact artifact){
		return "AssociationClass";
	}

	public static String getArtifactType(IManagedEntityArtifact artifact){
		return "Class";
	}
	
	private static Pattern quotePattern = Pattern.compile("^\".*\"$");
	
	/**
	 * Split up string. delimiter is ',' - strings will be quoted.
	 * Need to XML encode strings.
	 * @param type
	 * @param value
	 * @return
	 */
	
	
	public static Collection<String> splitValues(String type, String value){
		Collection<String> ret = new ArrayList<String>();
		String[] sStr = value.split(",");
		for (String s : sStr){
			ret.add(StringEscapeUtils.escapeXml(s.trim()));
//			ret.add(s);
		}
		return ret;
	}

}