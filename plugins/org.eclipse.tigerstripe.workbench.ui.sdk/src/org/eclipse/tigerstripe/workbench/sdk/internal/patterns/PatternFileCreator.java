/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal.patterns;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.tigerstripe.workbench.internal.core.util.encode.XmlEscape;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PatternFileCreator {

	
	private String PATTERN_NSURI = "http://org.eclipse.tigerstripe/xml/tigerstripeCreationPattern/v1-0";
	
	private XmlEscape escaper = new XmlEscape();
	
	public boolean makeArtifactPattern(IAbstractArtifact sourceArtifact,

	  File targetFile,
	  String patternName,
	  boolean includeEndNames,
	  String uILabel,
	  String iconPath,
	  String index,
	  String description){
		boolean success = false;
		
		String patternType = "node";
		
		if (sourceArtifact instanceof IRelationship){
			patternType = "relation";
		}
		
		
		try {
			// Make a XML document containing the various components of the pattern
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.newDocument();
			
			
			Element patternElement = document.createElementNS(PATTERN_NSURI, "creationPattern");
			patternElement.setPrefix("pattern");
			patternElement.setAttribute("patternName", escaper.encode(patternName));
			patternElement.setAttribute("patternType",patternType);
			patternElement.setAttribute("uiLabel", escaper.encode(uILabel));
			patternElement.setAttribute("iconPath", iconPath);
			// This is not used
			patternElement.setAttribute("disabledIconPath", "");
			if (! "".equals(index)){
				patternElement.setAttribute("index", index);
			}
			document.appendChild(patternElement);
			
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			
			Element descriptionElement = document.createElementNS(PATTERN_NSURI, "description");
			descriptionElement.appendChild(document.createTextNode(escaper.encode(description)));
			patternElement.appendChild(descriptionElement);
			
			ArtifactToXML artifactToXML = new ArtifactToXML(document);
			Element artifactElement = artifactToXML.artifactToElement(sourceArtifact, includeEndNames);
			patternElement.appendChild(artifactElement);
			
			
			DOMSource source = new DOMSource(document);
			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			t.transform(source, sr);
			
			// Now write it to the file
			
			PrintWriter out = new PrintWriter(new FileWriter(targetFile));
			out.println(sw.toString());
			out.close();

		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
		return success;
	}
	
}
