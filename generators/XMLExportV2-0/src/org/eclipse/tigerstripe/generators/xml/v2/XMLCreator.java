package org.eclipse.tigerstripe.generators.xml.v2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.tigerstripe.generators.util.AbstractRunnable;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xml.ArtifactToXML;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;


public class XMLCreator extends AbstractRunnable{

		
	public void run() throws Exception {
		Object artifactsObj = context.get(IRule.ARTIFACTS);
		Collection<IAbstractArtifact> artifacts = (Collection<IAbstractArtifact>) artifactsObj;
		String f = exp.expandVar(config.getProperty("singleFileOnly").toString());
		DocumentBuilderFactory dbf = null;
		
		try {
			// Make a XML document containing the various components of the pattern
			dbf = DocumentBuilderFactory.newInstance();
		} catch (Exception e){
			e.printStackTrace();
		}
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.newDocument();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			
			ArtifactToXML artifactToXML = new ArtifactToXML(document);
			
			Element rootElement = artifactToXML.getRootElement(modelProject);
			PluginLog.logDebug(rootElement.toString());
			document.appendChild(rootElement);
			
			
			
			
			if(f.equalsIgnoreCase("false")){
			/*<?xml-stylesheet version="1.0" type="text/xsl" href="./xsl/$indexXSL"?>*/
			ProcessingInstruction xslt = document.createProcessingInstruction("xml-stylesheet", "href=\"./index.xsl\" type=\"text/xsl\"");
			document.insertBefore(xslt, rootElement);
			for (IAbstractArtifact artifact : artifacts){
				Element artifactElement = artifactToXML.artifactToIndexElement(artifact);
				rootElement.appendChild(artifactElement);
			}
			} else {					
			for (IAbstractArtifact artifact : artifacts){
				Element artifactElement = artifactToXML.artifactToElement(artifact);
				rootElement.appendChild(artifactElement);
			}
			}
			
			
			DOMSource source = new DOMSource(document);
			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			t.transform(source, sr);
			
			// Now write it to the file
			
			
			save(makeFileName(),sw.toString());
		
			
			if(f.equalsIgnoreCase("false")){
				for (IAbstractArtifact arti : artifacts){
					Document artiDocument = db.newDocument();
					ArtifactToXML aartifactToXML = new ArtifactToXML(artiDocument);
					
					String toRoot = pathToRoot(arti.getFullyQualifiedName());
					String end = "artifact.xsl\" type=\"text/xsl\"";
					String transform = "href=\"".concat(toRoot).concat(end);
					ProcessingInstruction xslt = artiDocument.createProcessingInstruction("xml-stylesheet", transform);
					
					Element arootElement = aartifactToXML.getRootElement(modelProject);					
					artiDocument.appendChild(arootElement);
					artiDocument.insertBefore(xslt, arootElement);
					
					
					
					Element artifactElement = aartifactToXML.artifactToElement(arti);					
					arootElement.appendChild(artifactElement);
					
					
					DOMSource asource = new DOMSource(artiDocument);
					StringWriter asw = new StringWriter();
					StreamResult asr = new StreamResult(asw);
					t.transform(asource, asr);
					
					// Now write it to the file
					
					
					save(makeSingleArtiFileName(arti),asw.toString());
					}
				}
			
		
		}
					
		
		
	
	public String pathToRoot(String inString){
		String[] bits = inString.split("\\.");
		//PluginLog.logDebug("String in: "+inString+", lenght of bits :"+bits.length);
		String outString = "";
		if(bits.length!=1){
		for(int i=0; i<(bits.length-1); i++){
			outString = outString + "../";
		}
		return outString;
		}
		else return "./";
	}
	
}
