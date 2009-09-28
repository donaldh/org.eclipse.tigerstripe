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
package org.eclipse.tigerstripe.workbench.optional.bulkImport;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xml.TigerstripeXMLParserUtils;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjQuerySpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Comparer;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Difference;
import org.eclipse.tigerstripe.workbench.model.annotation.AnnotationHelper;
import org.eclipse.tigerstripe.workbench.model.annotation.IAnnotationCapable;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.osgi.framework.Bundle;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XML2TS {

	private class Specifics {
		String artifactType;

		Element specificElement;
	}

	private String namespace = "http://org.eclipse.tigerstripe/xml/tigerstripeExport/v1-0";
	private String namespaceV2 = "http://org.eclipse.tigerstripe/xml/tigerstripeExport/v2-0";
	
	private String namespaceSchema = "resources/schemas/tigerstripeExportSchema-v1-0.xsd";
	private String namespaceV2Schema = "resources/schemas/tigerstripeExportSchema-v2-0.xsd";

	private String usedNamespace = null;
	
	private String[] namespaces = new String[]{namespace, namespaceV2};
	private String[] schemaFiles = new String[]{namespaceSchema, namespaceV2Schema};
	
	private Validator tsValidator;
	// private MessageList messages;
	private int MESSAGE_LEVEL = 3;

	// private PrintWriter out;

//	private IArtifactManagerSession projectMgrSession;

	private File importFile;

	private Document importDoc;

	private DocumentBuilder parser;

	private PrintWriter out;

	private MessageList messages;

	private IWorkbenchProfileSession profileSession;

	private TigerstripeXMLParserUtils xmlParserUtils;
	protected AnnotationHelper helper = AnnotationHelper.getInstance();

	// default constructor
	public XML2TS(PrintWriter out, MessageList messages) {
		this.out = out;
		this.messages = messages;

		this.profileSession = TigerstripeCore.getWorkbenchProfileSession();
		
	}

	public static ITigerstripeModelProject createTempProject() throws TigerstripeException {
		// Just in case we didn't clean up last time
		ITigerstripeModelProject tempProject;
		IAbstractTigerstripeProject findProject = TigerstripeCore.findProject("__Import__Temp__");
		if ( findProject != null){
			findProject.delete(true, null);
		}
		tempProject =
			(ITigerstripeModelProject) TigerstripeCore.createProject(
					"__Import__Temp__",
					TigerstripeCore.makeProjectDetails(), 
					null, ITigerstripeModelProject.class, null, null);
		return tempProject;
	}
	
	public ImportBundle loadXMLtoTigerstripe(ITigerstripeModelProject tempProject, InputStream inputStream,
			String tSProjectName, IProgressMonitor monitor)
			throws TigerstripeException {

		this.importFile = null;
		try {


			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringComments(true);
			factory.setCoalescing(true);
			factory.setNamespaceAware(true);

			parser = factory.newDocumentBuilder();
			importDoc = parser.parse(inputStream);
			importDoc.normalizeDocument();
			DOMSource importSource = new DOMSource(importDoc);
			return loadFromSource(tempProject, importSource, tSProjectName, monitor);
		} catch (Exception saxe) {
			String msgText = "XML Parsing problem reading stream  "
					+ "'" + saxe.getMessage() + "'";
			addMessage(messages, msgText, 0);
			out.println("Error : " + msgText);
			saxe.printStackTrace(out);
			return null;

		}
	}
	
	public ImportBundle loadXMLtoTigerstripe(ITigerstripeModelProject tempProject, File importFile,
			String tSProjectName, IProgressMonitor monitor)
			throws TigerstripeException {

		this.importFile = importFile;

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringComments(true);
			factory.setCoalescing(true);
			factory.setNamespaceAware(true);

			parser = factory.newDocumentBuilder();
			importDoc = parser.parse(importFile);
			importDoc.normalizeDocument();
			DOMSource importSource = new DOMSource(importDoc);


			return loadFromSource(tempProject, importSource, tSProjectName, monitor);
		} catch (Exception saxe) {
			String msgText = "XML Parsing problem reading file  "
					+importFile.getName()+ "'" + saxe.getMessage() + "'";
			addMessage(messages, msgText, 0);
			out.println("Error : " + msgText);
			saxe.printStackTrace(out);
			return null;
		}

	}
	
	public ImportBundle loadFromSource (ITigerstripeModelProject tempProject, DOMSource importSource,
			String tSProjectName, IProgressMonitor monitor)
		throws TigerstripeException {
		Map<String, IAbstractArtifact> extractedArtifacts;
		SchemaFactory scFactory = SchemaFactory
		.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			// Try each schema in turn
			// TODO - Better to try reading the schema from the XML file!
			for (int i=0;i<schemaFiles.length;i++){
				String schema = schemaFiles[i];


				Bundle baseBundle = Platform.getBundle("org.eclipse.tigerstripe.workbench.base");
				URL schemaUrl = baseBundle.getEntry(schema);
				Schema tsSchema = scFactory.newSchema(schemaUrl);
				tsValidator = tsSchema.newValidator();
				try {
					tsValidator.validate(importSource);
					usedNamespace = namespaces[i];
					break;
				} catch (Exception e) {
					// This one didn't match, so just try the next one.
					String msgText = "XML source does not validate aginst the schema "
						 + e.getMessage() + "'";
					addMessage(messages, msgText, 0);
					out.println("Error : " + msgText);
					//	return null;
				}

			}
			if (usedNamespace == null){
				String msgText = "XML does not validate aginst the any available schemas ";
				addMessage(messages, msgText, 0);
				out.println("Error : " + msgText);
				return null;
			}

			this.xmlParserUtils = new TigerstripeXMLParserUtils(usedNamespace, out,
					messages);


			String msgText = "XML Validated against schema "+usedNamespace;
			addMessage(messages, msgText, 2);
			out.println("info : " + msgText);

			// TODO set the proper count in here
			monitor.beginTask("Processing XML Classes ", 100);


			// Get any artifacts in the parent
			extractedArtifacts = extractArtifacts(tempProject,importDoc, out, messages);

			// Get the files with external artifacts in - ONLY APplicable for File input
			if (importFile != null){
				extractedArtifacts.putAll(getIncludedArtifactFiles(tempProject,tsValidator));
			}
			msgText = "Found a total of " + extractedArtifacts.size()
			+ " artifacts in XML";

			addMessage(messages, msgText, 2);
			out.println("info : " + msgText);
			monitor.done();


		} catch (Exception e) {
			// Could be IOException, or someting unexpected.. Shouldn't get IO
			// as we've checked that already !
			String msgText = "Unknown Problem reading XML " + e.getMessage() + "'";
			addMessage(messages, msgText, 0);
			out.println("Error : " + msgText);
			e.printStackTrace();
			return null;

		}
		ImportBundle returnBundle = new ImportBundle();
		returnBundle.setExtractedArtifacts(extractedArtifacts);
		returnBundle.setMgrSession(tempProject.getArtifactManagerSession());

		return returnBundle;

	}
	


	public ArrayList<Difference> compareExtractAndProject(ImportBundle bundle, IArtifactManagerSession mgrSession,
			IProgressMonitor monitor, PrintWriter out, MessageList messages) {

		// TODO - A Hack
		//bundle.setMgrSession(mgrSession);
		
		Map<String, IAbstractArtifact> extractedArtifacts = bundle
				.getExtractedArtifacts();
		//IArtifactManagerSession mgrSession = bundle.getMgrSession();
		ArrayList<Difference> allXMLDiffs = new ArrayList<Difference>();
		try {
			Comparer comparer = new Comparer();
			/*
			 * For every artifact in the XML, find the project version (if it
			 * exists) and compare it
			 */
			monitor.beginTask("Comparing XML artifact with Project Artifact ",
					extractedArtifacts.size());

			for (IAbstractArtifact extractedArtifact : extractedArtifacts
					.values()) {
				String artifactName = extractedArtifact.getName();
				out.print("Comparing " + artifactName);
				IAbstractArtifact projectArtifact = bundle.getMgrSession()
						.getArtifactByFullyQualifiedName(extractedArtifact
								.getFullyQualifiedName());
				if (projectArtifact == null) {
					out.println(extractedArtifact.getFullyQualifiedName()
							+ " not found in project");
					// Make a Difference!
					Difference artifactDiff = new Difference("presence",
							extractedArtifact.getFullyQualifiedName(), "",
							"Artifact", "", "present", "absent");
					allXMLDiffs.add(artifactDiff);

				} else {
					ArrayList<Difference> artifactDiffs = comparer
							.compareArtifacts(extractedArtifact,
									projectArtifact, true, false);
					allXMLDiffs.addAll(artifactDiffs);
					out.println(" " + artifactDiffs.size() + " Diffs");

				}
				monitor.worked(1);

			}
			monitor.beginTask("Looking for new Project Artifacts ",
					extractedArtifacts.size());
			IArtifactQuery myQuery = mgrSession
					.makeQuery(IQueryAllArtifacts.class.getName());
			myQuery.setIncludeDependencies(false);
			Collection<IAbstractArtifact> projectArtifacts = mgrSession
					.queryArtifact(myQuery);
			for (IAbstractArtifact pArtifact : projectArtifacts) {
				if (!extractedArtifacts.containsKey(pArtifact
						.getFullyQualifiedName())) {
					Difference artifactDiff = new Difference("presence", "",
							pArtifact.getFullyQualifiedName(), "Artifact", "",
							"absent", "present");
					allXMLDiffs.add(artifactDiff);
				}
			}

			out.println("All diffs : total " + allXMLDiffs.size());
			addMessage(messages, "Total Diffs found : " + allXMLDiffs.size(), 1);

			for (Difference diff : allXMLDiffs) {
				out.println(diff.toString());
				addMessage(messages, diff.getShortString(), 1);
			}

			/*
			 * if (! reviewOnly){
			 * TigerstripeProjectAuditor.setTurnedOffForImport(true); DiffFixer
			 * fixer = new DiffFixer(); fixer.fixAll(allXMLDiffs, mgrSession,
			 * extractedArtifacts, out, messages);
			 * TigerstripeProjectAuditor.setTurnedOffForImport(false); }
			 */

			monitor.done();
			return allXMLDiffs;

		} catch (Exception e) {
			// Could be IOException, or someting unexpected.. Shouldn't get IO
			// as we've checked that already !
			String msgText = "Unknown Problem handling differences  '"
					+ e.getMessage() + "'";
			addMessage(messages, msgText, 0);
			out.println("Error : " + msgText);
			e.printStackTrace(out);
			return allXMLDiffs;

		} finally {
			TigerstripeProjectAuditor.setTurnedOffForImport(false);
		}

	}

	/**
	 * extract the project info from the importDoc Document
	 * 
	 * This is not usedas we import in to an existing project.
	 */
	private Map<String, String> extractProjectInfo() {
		Map<String, String> extractedInfo = new HashMap<String, String>();
		NodeList projectNodes = importDoc.getElementsByTagNameNS(usedNamespace,
				"tigerstripeProject");

		// Should only be the one...
		Element projectElement = (Element) projectNodes.item(0);
		projectElement.getAttribute("name");
		projectElement.getAttribute("version");

		return extractedInfo;
	}

	private Map<String, IAbstractArtifact> getIncludedArtifactFiles(ITigerstripeModelProject tempProject, Validator tsValidator) {
		// TigerstripeRuntime.logInfoMessage("Looking for files");

		Map<String, IAbstractArtifact> extractedArtifacts = new HashMap<String, IAbstractArtifact>();
		NodeList artifactFileNodes = importDoc.getElementsByTagNameNS(
				usedNamespace, "artifactFile");
		for (int an = 0; an < artifactFileNodes.getLength(); an++) {
			Element artifactElement = (Element) artifactFileNodes.item(an);
			String artifactFileName = artifactElement.getAttribute("fileName");
			// TigerstripeRuntime.logInfoMessage(artifactFileName);
			File artifactFile = new File(importFile.getParent()
					+ File.separator + artifactFileName);
			try {
				Document artifactDoc = parser.parse(artifactFile);
				DOMSource artifactSource = new DOMSource(artifactDoc);

//				File tsSchemaFile = new File(importFile.getParentFile()
//						+ File.separator + "tigerstripeArtifactOutput.xsd");
//				SchemaFactory scFactory = SchemaFactory
//						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//				Schema tsSchema = scFactory.newSchema(tsSchemaFile);
//				Validator tsValidator = tsSchema.newValidator();
				try {
					tsValidator.validate(artifactSource);
				} catch (Exception e) {
					String msgText = "XML file does not validate aginst the schema "
							+ importFile.getName() + "'" + e.getMessage() + "'";
					addMessage(messages, msgText, 0);
					out.println("Error : " + msgText);
					continue;
				}
				extractedArtifacts.putAll(extractArtifacts(tempProject,artifactDoc, out,
						messages));

			} catch (SAXException saxe) {
				String msgText = "XML Parsing problem reading file "
						+ importFile.getName() + "'" + saxe.getMessage() + "'";
				addMessage(messages, msgText, 0);
				out.println("Error : " + msgText);
				saxe.printStackTrace(out);
				continue;

			} catch (Exception e) {
				// Could be IOException, or someting unexpected.
				String msgText = "Unknown Problem reading XML File "
						+ importFile.getName() + "'" + e.getMessage() + "'";
				addMessage(messages, msgText, 0);
				out.println("Error : " + msgText);
				e.printStackTrace(out);
				continue;

			}
		}

		return extractedArtifacts;
	}

	/**
	 * extract the artifacts from the importDoc Document this makes some
	 * AbstractArtifacts (not attached to a session) which we can then compare
	 * with the project concerned
	 * 
	 */
	private Map<String, IAbstractArtifact> extractArtifacts(ITigerstripeModelProject tempProject, Document doc,
			PrintWriter out, MessageList messages) {
		
		Map<String, IAbstractArtifact> extractedArtifacts = new HashMap<String, IAbstractArtifact>();
		try {
		IArtifactManagerSession tempMgrSession = tempProject.getArtifactManagerSession();

//			ITigerstripeModelProject tempProject =(ITigerstripeModelProject) TigerstripeCore.createProject("__Import__Temp__",TigerstripeCore.makeProjectDetails(), null, ITigerstripeModelProject.class, null, null);
		
		

		NodeList artifactNodes = doc.getElementsByTagNameNS(usedNamespace,
				"artifact");
		String myText = "Found " + artifactNodes.getLength()
				+ " artifact nodes " + usedNamespace;
		addMessage(messages, myText, 0);
		out.println("Error : " + myText);

		for (int an = 0; an < artifactNodes.getLength(); an++) {
			Element artifactElement = (Element) artifactNodes.item(an);
			String artifactName = artifactElement.getAttribute("name");
			Specifics specificType = getArtifactSpecifics(artifactElement);

			String typeName = xmlParserUtils.getArtifactType(artifactElement);

			if (typeName == null) {
				// Can't handle this artifact - Log a message and carry on
				// This is actually invalid XML so should never happen!
				String msgText = "Cannot determine Artifact Type "
						+ artifactName;
				addMessage(messages, msgText, 0);
				continue;
			}
			IAbstractArtifact inArtifact;
			try {
				inArtifact = tempMgrSession.makeArtifact(typeName);
			} catch (IllegalArgumentException ile){
				String msgText = "failed to create Artifact "
					+ artifactName;
				addMessage(messages, msgText, 0);
			continue;
			}
			inArtifact.setFullyQualifiedName(artifactName);
			out.println("Found Artifact in XML : "
					+ inArtifact.getFullyQualifiedName());
			inArtifact.setAbstract(Boolean.parseBoolean(artifactElement
					.getAttribute("isAbstract")));

			// Can't just use this string - it needs to be an artifact....
			// This ony works coz we don't add the exArtifact to any project...
			String extendedArtifact = artifactElement
					.getAttribute("extendedArtifact");
			IAbstractArtifact exArtifact = tempMgrSession.makeArtifact(typeName);
			exArtifact.setFullyQualifiedName(extendedArtifact);
			inArtifact.setExtendedArtifact(exArtifact);

			
			// implements
			NodeList implNodes = artifactElement
				.getElementsByTagNameNS(usedNamespace, "implementedInterface");
			if (implNodes.getLength()>0){

				Collection<IAbstractArtifact> implArtifacts = new ArrayList<IAbstractArtifact>(); 
				for (int in = 0; in < implNodes.getLength(); in++) {
					Element impl = (Element) implNodes.item(in);
					IAbstractArtifact implArtifact = tempMgrSession.makeArtifact("org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact");
					implArtifact.setFullyQualifiedName(impl.getTextContent());
					implArtifacts.add(implArtifact);
				}
				inArtifact.setImplementedArtifacts(implArtifacts);
			}

			
			setLiterals(artifactElement, inArtifact, out, messages);
			setFields(artifactElement, inArtifact, out, messages);
			setMethods(artifactElement, inArtifact, out, messages);
			setAnnotations(artifactElement, inArtifact, out, messages);
			//System.out.println("Found " + inArtifact.getFullyQualifiedName()+" "+inArtifact.getAnnotations().size());
			//System.out.println("Found " + inArtifact.getProject().getArtifactManagerSession());
			// Must do specifics AFTER methods - as the flavor stuff refers back
			// to methods.
			if (specificType != null) {
				setSpecifics(specificType.specificElement, inArtifact);
			}
			for (IStereotypeInstance st : getStereotypes(artifactElement, out,
					messages)) {
				inArtifact.addStereotypeInstance(st);
			}

			inArtifact.setComment(getComment(artifactElement));

			// Artifact Type specifics

			extractedArtifacts.put(inArtifact.getFullyQualifiedName(),
					inArtifact);

		}
//		if (tempProject != null){
//			tempProject.delete(true, null);
//		}
		
		} catch (TigerstripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
		return extractedArtifacts;
		
	}

	/**
	 * Handle the specifics of a given artifact Type.
	 * 
	 * @param element
	 * @param artifact
	 */
	private void setSpecifics(Element element, IAbstractArtifact artifact) {

		String aType = artifact.getArtifactType();
		
		if (aType.equals(IEnumArtifact.class.getName())) {
			IEnumArtifact enumArt = (IEnumArtifact) artifact;
			OssjEnumSpecifics specs = (OssjEnumSpecifics) enumArt
					.getIStandardSpecifics();
			String baseType = element.getAttribute("baseType");
			IType type = artifact.makeField().makeType();
			type.setFullyQualifiedName(baseType);
			specs.setBaseIType(type);
			
		} else if (aType.equals(IAssociationArtifact.class.getName())
				|| aType.equals(IAssociationClassArtifact.class.getName())) {
			IAssociationArtifact assArt = (IAssociationArtifact) artifact;
			// Get the ends
			IAssociationEnd[] ends = new IAssociationEnd[2];
			ends[0] = assArt.makeAssociationEnd();
			ends[1] = assArt.makeAssociationEnd();
			setAssociationEnds(assArt, ends, element);

		} else if (aType.equals(IDependencyArtifact.class.getName())) {
			IDependencyArtifact depArt = (IDependencyArtifact) artifact;
			IType atype = depArt.makeType();
			atype.setFullyQualifiedName(element.getAttribute("aEndTypeName"));
			depArt.setAEndType(atype);
			IType ztype = depArt.makeType();
			ztype.setFullyQualifiedName(element.getAttribute("zEndTypeName"));
			depArt.setZEndType(ztype);

		} else if (aType.equals(IQueryArtifact.class.getName())) {
			IQueryArtifact queryArt = (IQueryArtifact) artifact;
			OssjQuerySpecifics specs = (OssjQuerySpecifics) queryArt
					.getIStandardSpecifics();
			IType type = queryArt.makeType();
			type
					.setFullyQualifiedName(element
							.getAttribute("returnedTypeName"));

			type.setTypeMultiplicity(IModelComponent.EMultiplicity
					.parse(element.getAttribute("returnedTypeMultiplicity")));
			queryArt.setReturnedType((IType) type);
			

		} else if (aType.equals(ISessionArtifact.class.getName())) {
			
		}
	}

	private void handleSession(Element element, ISessionArtifact session) {
		NodeList eventNodes = element.getElementsByTagNameNS(usedNamespace,
				"emittedEvent");
		for (int ee = 0; ee < eventNodes.getLength(); ee++) {
			IEmittedEvent event = session.makeEmittedEvent();
			event.setFullyQualifiedName(((Element) eventNodes.item(ee))
					.getAttribute("name"));
			session.addEmittedEvent(event);
		}
		NodeList updateNodes = element.getElementsByTagNameNS(usedNamespace,
				"exposedUpdateProcedure");
		for (int up = 0; up < updateNodes.getLength(); up++) {
			IExposedUpdateProcedure update = session
					.makeExposedUpdateProcedure();
			update.setFullyQualifiedName(((Element) updateNodes.item(up))
					.getAttribute("name"));
			session.addExposedUpdateProcedure(update);
		}
		NodeList queryNodes = element.getElementsByTagNameNS(usedNamespace,
				"exposedQuery");
		for (int q = 0; q < queryNodes.getLength(); q++) {
			INamedQuery query = session.makeNamedQuery();
			query.setFullyQualifiedName(((Element) queryNodes.item(q))
					.getAttribute("name"));
			session.addNamedQuery(query);
		}
		NodeList entityDetailNodes = element.getElementsByTagNameNS(usedNamespace,
				"managedEntityDetails");
		for (int med = 0; med < entityDetailNodes.getLength(); med++) {
			IManagedEntityDetails details = session.makeManagedEntityDetails();
			Element detailElement = (Element) entityDetailNodes.item(med);
			details.setFullyQualifiedName(detailElement.getAttribute("name"));

			// This next block is commented out because the session is returning
			// The sum of ManagedEntity + Overrides....
			// We really want the overrides only.
			/*
			 * NodeList flavorNodes =
			 * detailElement.getElementsByTagNameNS(namespace
			 * ,"entityMethodFlavorDetails"); for (int
			 * flav=0;flav<entityDetailNodes.getLength();flav++){ Element flavor
			 * = (Element) entityDetailNodes.item(flav); String name =
			 * flavor.getAttribute("name"); String flag =
			 * flavor.getAttribute("flag"); getComment(flavor); // TODO Handle
			 * all of this stuff..... NodeList flavorExceptionNodes =
			 * flavor.getElementsByTagNameNS(namespace,"exceptions"); }
			 */
			session.addManagedEntityDetails(details);
		}
	}

	private void setFields(Element element, IAbstractArtifact artifact,
			PrintWriter out, MessageList messages) {

		NodeList fieldNodes = element
				.getElementsByTagNameNS(usedNamespace, "field");
		for (int fn = 0; fn < fieldNodes.getLength(); fn++) {
			Element field = (Element) fieldNodes.item(fn);

			IField newField = artifact.makeField();
			IType type = newField.makeType();

			newField.setName(field.getAttribute("name"));
			type.setFullyQualifiedName(field.getAttribute("type"));
			// Need to support new and old versions
			type.setTypeMultiplicity(IModelComponent.EMultiplicity.parse(field
					.getAttribute("typeMultiplicity")));
			//this.out.println(type.getTypeMultiplicity().getLabel());
			// end
			newField.setType(type);
			newField.setVisibility(EVisibility.parse(field
					.getAttribute("visibility")));
			newField.setOptional(Boolean.parseBoolean(field
					.getAttribute("optional")));
			newField.setReadOnly(Boolean.parseBoolean(field
					.getAttribute("readonly")));
			// if (field.hasAttribute("refBy")) {
			// newField.setRefBy(Integer.valueOf(field.getAttribute("refBy")));
			// }
			newField.setComment(getComment(field));

			if (field.hasAttribute("ordered")) {
				newField.setOrdered(Boolean.parseBoolean(field
						.getAttribute("ordered")));
			}

			if (field.hasAttribute("unique")) {
				newField.setUnique(Boolean.parseBoolean(field
						.getAttribute("unique")));
			}

			if (field.hasAttribute("defaultValue")) {
				newField.setDefaultValue(field.getAttribute("defaultValue"));
			}

			for (IStereotypeInstance st : getStereotypes(field, out, messages)) {
				newField.addStereotypeInstance(st);
			}
			
			setAnnotations(field, newField, out, messages);
			
			artifact.addField(newField);

		}
	}

	private void setLiterals(Element element, IAbstractArtifact artifact,
			PrintWriter out, MessageList messages) {
		NodeList literalNodes = element.getElementsByTagNameNS(usedNamespace,
				"literal");
		for (int ln = 0; ln < literalNodes.getLength(); ln++) {
			Element literal = (Element) literalNodes.item(ln);

			ILiteral newLiteral = artifact.makeLiteral();
			IType type = newLiteral.makeType();

			newLiteral.setName(literal.getAttribute("name"));
			newLiteral.setValue(literal.getAttribute("value"));
			type.setFullyQualifiedName(literal.getAttribute("type"));
			newLiteral.setType(type);
			newLiteral.setVisibility(EVisibility.parse(literal
					.getAttribute("visibility")));
			newLiteral.setComment(getComment(literal));

			for (IStereotypeInstance st : getStereotypes(literal, out, messages)) {
				newLiteral.addStereotypeInstance(st);
			}
			
			setAnnotations(literal, newLiteral, out, messages);
			artifact.addLiteral(newLiteral);
		}
	}

	
	private void setAnnotations(Element element, IAnnotationCapable capable,
			PrintWriter out, MessageList messages) {
		
		Collection<EObject> annotationContents =  xmlParserUtils.getAnnotations(element);
		for (EObject content : annotationContents){
			try {
				addAnnotation(capable, content);
			}catch (TigerstripeException t){
				String msgText = t.getMessage();
				addMessage(messages, msgText, 0);
			}
		}
		
	}
	
	protected void addAnnotation(IAnnotationCapable component, EObject content) throws TigerstripeException{
		try {
			out.println("Add Annotation to "+ component.getClass());
			String annotationClass = content.getClass().getInterfaces()[0].getName();
			Annotation anno = helper.addAnnotation(component, Util.packageOf(annotationClass), Util.nameOf(annotationClass));
			anno.setContent(content);
			//AnnotationHelper.getInstance().saveAnnotation(anno);
			
		} catch (Exception e){
			e.printStackTrace();
			throw new TigerstripeException("Exception adding annotation to component",e);
		}
	}
	
	
	private void setMethods(Element element, IAbstractArtifact artifact,
			PrintWriter out, MessageList messages) {
		NodeList methodNodes = element.getElementsByTagNameNS(usedNamespace,
				"method");
		for (int ln = 0; ln < methodNodes.getLength(); ln++) {
			Element method = (Element) methodNodes.item(ln);

			IMethod newMethod = artifact.makeMethod();

			newMethod.setName(method.getAttribute("name"));
			newMethod.setVisibility(EVisibility.parse(method
					.getAttribute("visibility")));
			newMethod.setOptional(Boolean.parseBoolean(method
					.getAttribute("optional")));
			newMethod.setInstanceMethod(Boolean.parseBoolean(method
					.getAttribute("isInstanceMethod")));
			newMethod.setIteratorReturn(Boolean.parseBoolean(method
					.getAttribute("isIteratorReturn")));
			newMethod.setVoid(Boolean.parseBoolean(method
					.getAttribute("isVoid")));
			newMethod.setAbstract(Boolean.parseBoolean(method
					.getAttribute("isAbstract")));

			// if (method.hasAttribute("returnRefBy")) {
			// newMethod.setReturnRefBy(Integer.valueOf(method
			// .getAttribute("returnRefBy")));
			// }

			if (method.hasAttribute("ordered")) {
				newMethod.setOrdered(Boolean.parseBoolean(method
						.getAttribute("ordered")));
			}

			if (method.hasAttribute("unique")) {
				newMethod.setUnique(Boolean.parseBoolean(method
						.getAttribute("unique")));
			}
			if (method.hasAttribute("defaultReturnValue")) {
				newMethod.setDefaultReturnValue(method
						.getAttribute("defaultReturnValue"));
			}

			for (IStereotypeInstance st : getReturnStereotypes(method, out,
					messages)) {
				newMethod.addReturnStereotypeInstance(st);
			}

			// If this is *NOT* void, then we should have
			// A return Type - if we don't the model is invalid
			if (!newMethod.isVoid()) {
				if (method.hasAttribute("returnType")
						&& method.hasAttribute("returnTypeMultiplicity")) {
					IType returnType = newMethod.makeType();
					returnType.setFullyQualifiedName(method
							.getAttribute("returnType"));

					returnType
							.setTypeMultiplicity(IModelComponent.EMultiplicity
									.parse(method
											.getAttribute("returnTypeMultiplicity")));
					newMethod.setReturnType(returnType);
					if (method.hasAttribute("methodReturnName")) {
						newMethod.setReturnName(method
								.getAttribute("methodReturnName"));
					}
					if (method.hasAttribute("defaultReturnValue")) {
						newMethod.setDefaultReturnValue(method
								.getAttribute("defaultReturnValue"));
					}

				} else {
					String msgText = "Invalid Method defintion for "
							+ newMethod.getName()
							+ " on "
							+ artifact.getFullyQualifiedName()
							+ " : Method is not void, but has no return Type set ";
					addMessage(messages, msgText, 0);
					continue;
				}
			} else {
				IType returnType = newMethod.makeType();
				returnType.setFullyQualifiedName("void");
				newMethod.setReturnType(returnType);
			}

			NodeList argumentNodes = method.getElementsByTagNameNS(usedNamespace,
					"argument");
			for (int a = 0; a < argumentNodes.getLength(); a++) {
				Element argument = (Element) argumentNodes.item(a);
				IArgument newArgument = newMethod.makeArgument();
				newArgument.setName(argument.getAttribute("name"));
				// if (argument.hasAttribute("refBy")){
				// newArgument.setRefBy(Integer.valueOf(argument
				// .getAttribute("refBy")));
				// }
				IType argType = newMethod.makeType();
				argType.setFullyQualifiedName(argument.getAttribute("type"));

				argType.setTypeMultiplicity(IModelComponent.EMultiplicity
						.parse(argument.getAttribute("typeMultiplicity")));

				newArgument.setType(argType);
				newArgument.setComment(getComment(argument));
				if (argument.hasAttribute("ordered")) {
					newArgument.setOrdered(Boolean.parseBoolean(argument
							.getAttribute("ordered")));
				}

				if (argument.hasAttribute("unique")) {
					newArgument.setUnique(Boolean.parseBoolean(argument
							.getAttribute("unique")));
				}

				if (argument.hasAttribute("defaultValue")) {
					newArgument.setDefaultValue(argument
							.getAttribute("defaultValue"));
				}

				for (IStereotypeInstance st : getStereotypes(argument, out,
						messages)) {
					newArgument.addStereotypeInstance(st);
				}
				newMethod.addArgument(newArgument);
			}

			NodeList exceptionNodes = method.getElementsByTagNameNS(usedNamespace,
					"exception");
			for (int a = 0; a < exceptionNodes.getLength(); a++) {
				Element exception = (Element) exceptionNodes.item(a);
				IException newException = newMethod.makeException();
				newException.setFullyQualifiedName(exception
						.getAttribute("name"));
				newMethod.addException(newException);
			}

			for (IStereotypeInstance st : getStereotypes(method, out, messages)) {
				newMethod.addStereotypeInstance(st);
			}

			newMethod.setComment(getComment(method));
			setAnnotations(method, newMethod, out, messages);
			artifact.addMethod(newMethod);
		}
	}

	public void setAssociationEnds(IAssociationArtifact assArt,
			IAssociationEnd[] ends, Element element) {
		NodeList endNodes = element.getElementsByTagNameNS(usedNamespace,
				"associationEnd");

		for (int e = 0; e < endNodes.getLength(); e++) {
			IAssociationEnd thisEnd = assArt.makeAssociationEnd();
			Element endNode = (Element) endNodes.item(e);
			thisEnd.setName(endNode.getAttribute("name"));

			thisEnd.setAggregation(EAggregationEnum.parse(endNode
					.getAttribute("aggregation")));
			thisEnd.setChangeable(EChangeableEnum.parse(endNode
					.getAttribute("changeable")));
			thisEnd.setMultiplicity(IModelComponent.EMultiplicity.parse(endNode
					.getAttribute("multiplicity")));
			thisEnd.setNavigable(Boolean.parseBoolean(endNode
					.getAttribute("navigable")));
			thisEnd.setOrdered(Boolean.parseBoolean(endNode
					.getAttribute("ordered")));
			thisEnd.setVisibility(EVisibility.parse(endNode
					.getAttribute("visibility")));
			if (endNode.hasAttribute("unique")) {
				thisEnd.setUnique(Boolean.parseBoolean(endNode
						.getAttribute("unique")));
			}
			IType type = thisEnd.makeType();
			type.setFullyQualifiedName(endNode.getAttribute("type"));
			thisEnd.setType(type);

			if (endNode.getAttribute("end").equals("AEnd")) {
				assArt.setAEnd(thisEnd);
			} else if (endNode.getAttribute("end").equals("ZEnd")) {
				assArt.setZEnd(thisEnd);
			}

		}
	}

	/**
	 * Find the sterotypes for this "thing"
	 * 
	 * @param element
	 * @return
	 */
	private Collection<IStereotypeInstance> getReturnStereotypes(
			Element element, PrintWriter out, MessageList messages) {
		Collection<IStereotypeInstance> isis = new ArrayList<IStereotypeInstance>();
		NodeList groupNodes = element.getChildNodes();
		for (int g = 0; g < groupNodes.getLength(); g++) {
			// Assume valid XML - at most one comment element!
			if (groupNodes.item(g) instanceof Element) {
				Element groupElement = (Element) groupNodes.item(g);
				if (groupElement.getLocalName().equals("returnStereotypes")) {

					NodeList childNodes = groupElement.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); i++) {
						// Assume valid XML - at most one comment element!
						if (childNodes.item(i) instanceof Element) {
							Element stereoElement = (Element) childNodes
									.item(i);
							if (stereoElement.getLocalName().equals(
									"stereotype")) {
								IStereotype tsStereo = this.profileSession
										.getActiveProfile()
										.getStereotypeByName(
												stereoElement
														.getAttribute("name"));

								//TigerstripeRuntime.logInfoMessage(stereoElement
								// .getAttribute("name"));

								if (tsStereo == null) {
									String msgText = "No Stereotype in Current Profile of name :"
											+ stereoElement
													.getAttribute("name");
									addMessage(messages, msgText, 0);
									out.println("Error : " + msgText);
									continue;
								}
								IStereotypeInstance tsStereoInstance = tsStereo
										.makeInstance();
								NodeList stereotypeAttributeNodes = stereoElement
										.getElementsByTagNameNS(usedNamespace,
												"stereotypeAttribute");
								for (int sa = 0; sa < stereotypeAttributeNodes
										.getLength(); sa++) {
									Element stereoAttributeElement = (Element) stereotypeAttributeNodes
											.item(sa);
									IStereotypeAttribute att = tsStereo
											.getAttributeByName(stereoAttributeElement
													.getAttribute("name"));
									if (att != null) {
										try {
											// / New stuff
											NodeList stereotypeAttributeValueNodes = stereoAttributeElement
													.getElementsByTagNameNS(
															usedNamespace, "value");

											if ((Boolean
													.parseBoolean(stereoAttributeElement
															.getAttribute("array")))) {
												// can be any number of value
												// elements
												String[] vals = new String[stereotypeAttributeValueNodes
														.getLength()];
												for (int sav = 0; sav < stereotypeAttributeValueNodes
														.getLength(); sav++) {
													Element stereotypeAttributeValue = (Element) stereotypeAttributeValueNodes
															.item(sav);
													vals[sav] = stereotypeAttributeValue
															.getTextContent();
												}
												tsStereoInstance
														.setAttributeValues(
																att, vals);
											} else {
												// Should only be one value
												// element - take the first
												// one...
												Element stereotypeAttributeValue = (Element) stereotypeAttributeValueNodes
														.item(0);
												String val = stereotypeAttributeValue
														.getTextContent();
												tsStereoInstance
														.setAttributeValue(att,
																val);
											}
											// / end of new stuff

										} catch (Exception e) {
											String msgText = "Failed to set Stereotype Attribute :"
													+ stereoAttributeElement
															.getAttribute("name");
											addMessage(messages, msgText, 0);
											out.println("Error : " + msgText);
											continue;
										}
									}
								}
								isis.add(tsStereoInstance);
							}
						}
					}
				}
			}
		}

		return isis;
	}

	/**
	 * Find the sterotypes for this "thing"
	 * 
	 * @param element
	 * @return
	 */
	private Collection<IStereotypeInstance> getStereotypes(Element element,
			PrintWriter out, MessageList messages) {
		Collection<IStereotypeInstance> isis = new ArrayList<IStereotypeInstance>();
		NodeList groupNodes = element.getChildNodes();
		for (int g = 0; g < groupNodes.getLength(); g++) {
			// Assume valid XML - at most one comment element!
			if (groupNodes.item(g) instanceof Element) {
				Element groupElement = (Element) groupNodes.item(g);
				if (groupElement.getLocalName().equals("stereotypes")) {

					NodeList childNodes = groupElement.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); i++) {
						// Assume valid XML - at most one comment element!
						if (childNodes.item(i) instanceof Element) {
							Element stereoElement = (Element) childNodes
									.item(i);
							if (stereoElement.getLocalName().equals(
									"stereotype")) {
								IStereotype tsStereo = this.profileSession
										.getActiveProfile()
										.getStereotypeByName(
												stereoElement
														.getAttribute("name"));

								//TigerstripeRuntime.logInfoMessage(stereoElement
								// .getAttribute("name"));

								if (tsStereo == null) {
									String msgText = "No Stereotype in Current Profile of name :"
											+ stereoElement
													.getAttribute("name");
									addMessage(messages, msgText, 0);
									out.println("Error : " + msgText);
									continue;
								}
								IStereotypeInstance tsStereoInstance = tsStereo
										.makeInstance();
								NodeList stereotypeAttributeNodes = stereoElement
										.getElementsByTagNameNS(usedNamespace,
												"stereotypeAttribute");
								for (int sa = 0; sa < stereotypeAttributeNodes
										.getLength(); sa++) {
									Element stereoAttributeElement = (Element) stereotypeAttributeNodes
											.item(sa);
									IStereotypeAttribute att = tsStereo
											.getAttributeByName(stereoAttributeElement
													.getAttribute("name"));
									if (att != null) {
										try {
											// / New stuff
											NodeList stereotypeAttributeValueNodes = stereoAttributeElement
													.getElementsByTagNameNS(
															usedNamespace, "value");

											if ((Boolean
													.parseBoolean(stereoAttributeElement
															.getAttribute("array")))) {
												// can be any number of value
												// elements
												String[] vals = new String[stereotypeAttributeValueNodes
														.getLength()];
												for (int sav = 0; sav < stereotypeAttributeValueNodes
														.getLength(); sav++) {
													Element stereotypeAttributeValue = (Element) stereotypeAttributeValueNodes
															.item(sav);
													vals[sav] = stereotypeAttributeValue
															.getTextContent();
												}
												tsStereoInstance
														.setAttributeValues(
																att, vals);
											} else {
												// Should only be one value
												// element - take the first
												// one...
												Element stereotypeAttributeValue = (Element) stereotypeAttributeValueNodes
														.item(0);
												String val = stereotypeAttributeValue
														.getTextContent();
												tsStereoInstance
														.setAttributeValue(att,
																val);
											}
											// / end of new stuff

										} catch (Exception e) {
											String msgText = "Failed to set Stereotype Attribute :"
													+ stereoAttributeElement
															.getAttribute("name");
											addMessage(messages, msgText, 0);
											out.println("Error : " + msgText);
											continue;
										}
									}
								}
								isis.add(tsStereoInstance);
							}
						}
					}
				}
			}
		}

		return isis;
	}

	private Specifics getArtifactSpecifics(Element artifactElement) {
		Specifics specifics = new Specifics();

		NodeList artifactType;

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"managedEntitySpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IManagedEntityArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"datatypeSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IDatatypeArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"enumerationSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IEnumArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"associationSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IAssociationArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"associationClassSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IAssociationClassArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"dependencySpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IDependencyArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"updateProcedureSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IUpdateProcedureArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"querySpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IQueryArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"exceptionSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IExceptionArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"notificationSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IEventArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(usedNamespace,
				"sessionSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = ISessionArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		return null;

	}

	private String getComment(Element element) {
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			// Assume valid XML - at most one comment element!
			if (childNodes.item(i) instanceof Element) {
				Element child = (Element) childNodes.item(i);
				if (child.getLocalName().equals("comment")) {
					String comment = child.getTextContent();
					return comment;
				}
			}
		}
		return null;
	}

	private void addMessage(MessageList messages, String msgText, int severity) {
		if (severity <= MESSAGE_LEVEL) {
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(severity);
			messages.addMessage(newMsg);
		}

	}

}
