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
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventDescriptorEntry;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjDatatypeSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjEventSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjQuerySpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjUpdateProcedureSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Comparer;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Difference;
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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IEventDescriptorEntry;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XML2TS {

	private class Specifics {
		String artifactType;

		Element specificElement;
	}

	private String namespace = "http://org.eclipse.tigerstripe/xml/tigerstripeExport/v1-0";

	// private MessageList messages;
	private int MESSAGE_LEVEL = 3;

	// private PrintWriter out;

	private IArtifactManagerSession mgrSession;

	private File importFile;

	private Document importDoc;

	private DocumentBuilder parser;

	private PrintWriter out;

	private MessageList messages;

	private IWorkbenchProfileSession profileSession;

	// default constructor
	public XML2TS() {
		this.profileSession = TigerstripeCore.getWorkbenchProfileSession();
	}

	public ImportBundle loadXMLtoTigerstripe(File importFile,
			String tSProjectName, PrintWriter out, MessageList messages,
			IProgressMonitor monitor) throws TigerstripeException {

		this.out = out;
		this.messages = messages;
		this.importFile = importFile;

		Map<String, IAbstractArtifact> extractedArtifacts;
		try {

			// Make sure the TS project is OK
			// Have to do this early as we use the mgrSession

			try {
				IResource tsContainer = ResourcesPlugin.getWorkspace()
						.getRoot().findMember(new Path(tSProjectName));

				URI projectURI = tsContainer.getLocationURI();
				ITigerstripeModelProject tsProject = (ITigerstripeModelProject) TigerstripeCore
						.findProject(projectURI);
				this.mgrSession = tsProject.getArtifactManagerSession();
				String msgText = " Target Project : "
						+ tsProject.getProjectLabel();
				addMessage(messages, msgText, 2);
				out.println("info : " + msgText);
				this.mgrSession.refresh(true, monitor);

			} catch (Exception e) {
				String msgText = "Problem opening TS Project " + tSProjectName;
				addMessage(messages, msgText, 0);
				out.println("Error : " + msgText);
				// e.printStackTrace(this.out);
				return null;
			}

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringComments(true);
			factory.setCoalescing(true);
			factory.setNamespaceAware(true);

			parser = factory.newDocumentBuilder();
			importDoc = parser.parse(importFile);
			DOMSource importSource = new DOMSource(importDoc);

			// TODO Validate against a schema internally or ine found in the
			// same location?
			File tsSchemaFile = new File(importFile.getParentFile()
					+ File.separator + "tigerstripeExportSchema.xsd");
			out.println(tsSchemaFile+" "+tsSchemaFile.exists());
			
			SchemaFactory scFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema tsSchema = scFactory.newSchema(tsSchemaFile);
			Validator tsValidator = tsSchema.newValidator();
			try {
				tsValidator.validate(importSource);
			} catch (Exception e) {
				String msgText = "XML file does not validate aginst the schema "
						+ importFile.getName() + "'" + e.getMessage() + "'";
				addMessage(messages, msgText, 0);
				out.println("Error : " + msgText);
				return null;
			}

			String msgText = "XML Validated against schema";
			addMessage(messages, msgText, 2);
			out.println("info : " + msgText);

			// TODO set the proper count in here
			monitor.beginTask("Processing XML Classes ", 100);

			// TODO We now allow the artifacts to be in child files...
			// So either do one big pass, or get file by file..
			// validate each file, and extract the artifact

			// Get any artifacts in the parent
			extractedArtifacts = extractArtifacts(importDoc, out, messages);

			// Get the files with external artifacts in
			extractedArtifacts.putAll(getIncludedArtifactFiles());

			msgText = "Found a total of " + extractedArtifacts.size()
					+ " artifacts in XML";

			addMessage(messages, msgText, 2);
			out.println("info : " + msgText);
			monitor.done();

		} catch (SAXException saxe) {
			String msgText = "XML Parsing problem reading file "
					+ importFile.getName() + "'" + saxe.getMessage() + "'";
			addMessage(messages, msgText, 0);
			out.println("Error : " + msgText);
			saxe.printStackTrace(out);
			return null;

		} catch (Exception e) {
			// Could be IOException, or someting unexpected.. Shouldn't get IO
			// as we've checked that already !
			String msgText = "Unknown Problem reading XML File "
					+ importFile.getName() + "'" + e.getMessage() + "'";
			addMessage(messages, msgText, 0);
			out.println("Error : " + msgText);
			e.printStackTrace(out);
			return null;

		}
		ImportBundle returnBundle = new ImportBundle();
		returnBundle.setExtractedArtifacts(extractedArtifacts);
		returnBundle.setMgrSession(mgrSession);

		// compareExtractAndProject(returnBundle,monitor,out,messages);

		return returnBundle;

	}

	public ArrayList<Difference> compareExtractAndProject(ImportBundle bundle,
			IProgressMonitor monitor, PrintWriter out, MessageList messages) {

		Map<String, IAbstractArtifact> extractedArtifacts = bundle
				.getExtractedArtifacts();
		IArtifactManagerSession mgrSession = bundle.getMgrSession();
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
				IAbstractArtifact projectArtifact = mgrSession
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
	 * 
	 */
	private Map<String, String> extractProjectInfo() {
		Map<String, String> extractedInfo = new HashMap<String, String>();
		NodeList projectNodes = importDoc.getElementsByTagNameNS(namespace,
				"tigerstripeProject");

		// Should only be the one...
		Element projectElement = (Element) projectNodes.item(0);
		projectElement.getAttribute("name");
		projectElement.getAttribute("version");

		return extractedInfo;
	}

	private Map<String, IAbstractArtifact> getIncludedArtifactFiles() {
		// TigerstripeRuntime.logInfoMessage("Looking for files");

		Map<String, IAbstractArtifact> extractedArtifacts = new HashMap<String, IAbstractArtifact>();
		NodeList artifactFileNodes = importDoc.getElementsByTagNameNS(
				namespace, "artifactFile");
		for (int an = 0; an < artifactFileNodes.getLength(); an++) {
			Element artifactElement = (Element) artifactFileNodes.item(an);
			String artifactFileName = artifactElement.getAttribute("fileName");
			// TigerstripeRuntime.logInfoMessage(artifactFileName);
			File artifactFile = new File(importFile.getParent()
					+ File.separator + artifactFileName);
			try {
				Document artifactDoc = parser.parse(artifactFile);
				DOMSource artifactSource = new DOMSource(artifactDoc);

				File tsSchemaFile = new File(importFile.getParentFile()
						+ File.separator + "tigerstripeArtifactOutput.xsd");
				SchemaFactory scFactory = SchemaFactory
						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Schema tsSchema = scFactory.newSchema(tsSchemaFile);
				Validator tsValidator = tsSchema.newValidator();
				try {
					tsValidator.validate(artifactSource);
				} catch (Exception e) {
					String msgText = "XML file does not validate aginst the schema "
							+ importFile.getName() + "'" + e.getMessage() + "'";
					addMessage(messages, msgText, 0);
					out.println("Error : " + msgText);
					continue;
				}
				extractedArtifacts.putAll(extractArtifacts(artifactDoc, out,
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
	private Map<String, IAbstractArtifact> extractArtifacts(Document doc,
			PrintWriter out, MessageList messages) {
		Map<String, IAbstractArtifact> extractedArtifacts = new HashMap<String, IAbstractArtifact>();

		NodeList artifactNodes = doc.getElementsByTagNameNS(namespace,
				"artifact");
		String myText = "Found "+artifactNodes.getLength()+ " artifact nodes "+namespace;
		addMessage(messages, myText, 0);
		out.println("Error : " + myText);
		
		for (int an = 0; an < artifactNodes.getLength(); an++) {
			Element artifactElement = (Element) artifactNodes.item(an);
			String artifactName = artifactElement.getAttribute("name");

			// Need to determine the artifactType before creating one.
			Specifics specificType = getArtifactSpecifics(artifactElement);
			
			String typeName = getArtifactTypeName(artifactElement);
			
			if (typeName == null) {
				// Can't handle this artifact - Log a message and carry on
				// This is actually invalid XML so should never happen!
				String msgText = "Cannot determine Artifact Type "
						+ artifactName;
				addMessage(messages, msgText, 0);
				continue;
			}

			IAbstractArtifact inArtifact = mgrSession
					.makeArtifact(typeName);
			inArtifact.setFullyQualifiedName(artifactName);
			out.println("Found Artifact in XML : "
					+ inArtifact.getFullyQualifiedName());
			inArtifact.setAbstract(Boolean.parseBoolean(artifactElement
					.getAttribute("isAbstract")));

			// Can't just use this string - it needs to be an artifact....
			// This ony works coz we don't add the exArtifact to any project...
			String extendedArtifact = artifactElement
					.getAttribute("extendedArtifact");
			IAbstractArtifact exArtifact = mgrSession
					.makeArtifact(typeName);
			exArtifact.setFullyQualifiedName(extendedArtifact);
			inArtifact.setExtendedArtifact(exArtifact);

			setLiterals(artifactElement, inArtifact, out, messages);
			setFields(artifactElement, inArtifact, out, messages);
			setMethods(artifactElement, inArtifact, out, messages);

			// Must do specifics AFTER methods - as the flavor stuff refers back
			// to methods.
			if (specificType != null){
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
//		if (aType.equals(IManagedEntityArtifact.class.getName())) {
//			// Only need primary Key
//			IManagedEntityArtifact entity = (IManagedEntityArtifact) artifact;
//			OssjEntitySpecifics specs = (OssjEntitySpecifics) entity
//					.getIStandardSpecifics();
//			specs.setPrimaryKey(element.getAttribute("primaryKeyName"));
//			Properties props = specs.getInterfaceProperties();
//			props.setProperty("package", element
//					.getAttribute("interfacePackage"));
//			props.setProperty("generate", element
//					.getAttribute("interfaceGenerate"));
//			specs.setInterfaceProperties(props);
//			// Things in the "Ossj section" are all "OPTIONAL", so we need to
//			// check for their presence
//			if (element.hasAttribute("singleExtension")) {
//				specs.setSingleExtensionType(Boolean.parseBoolean(element
//						.getAttribute("singleExtension")));
//			}
//			if (element.hasAttribute("sessionFactoryMethods")) {
//				specs.setSessionFactoryMethods(Boolean.parseBoolean(element
//						.getAttribute("sessionFactoryMethods")));
//			}
//			// TODO What about the ManagedEntityDetails type of stuff...
//
//			NodeList entityDetailNodes = element.getElementsByTagNameNS(
//					namespace, "entityMethodDetails");
//			for (int med = 0; med < entityDetailNodes.getLength(); med++) {
//				Element detailElement = (Element) entityDetailNodes.item(med);
//				// get the method name from this element
//				String detailsMethodName = detailElement.getAttribute("name");
//
//				// Get the Flavor info
//				NodeList flavorNodes = detailElement.getElementsByTagNameNS(
//						namespace, "entityMethodFlavorDetails");
//				for (int flav = 0; flav < flavorNodes.getLength(); flav++) {
//					Element flavorElement = (Element) flavorNodes.item(flav);
//					// This name will be something like "simple"...
//					String flavorName = flavorElement
//							.getAttribute("flavorName");
//
//					IEntityMethodFlavorDetails flavorDetails = specs
//							.makeIEntityMethodFlavorDetails();
//
//					flavorDetails.setFlag(flavorElement.getAttribute("flag"));
//					flavorDetails.setComment(getComment(flavorElement));
//
//					ArrayList<String> flavorExceptions = new ArrayList<String>();
//					NodeList flavorExceptionNodes = flavorElement
//							.getElementsByTagNameNS(namespace, "exception");
//					for (int ex = 0; ex < flavorExceptionNodes.getLength(); ex++) {
//						Element exception = (Element) flavorExceptionNodes
//								.item(ex);
//						flavorDetails.addException(exception
//								.getAttribute("name"));
//					}
//
//					OssjEntityMethodFlavor flavEnum = OssjEntityMethodFlavor
//							.valueFromPojoLabel(flavorName);
//					// Now add the flav0r to the artifact or method...
//					if ("create".equals(detailsMethodName)) {
//						specs.setCRUDFlavorDetails(IOssjEntitySpecifics.CREATE,
//								flavEnum, flavorDetails);
//					} else if ("get".equals(detailsMethodName)) {
//						specs.setCRUDFlavorDetails(IOssjEntitySpecifics.GET,
//								flavEnum, flavorDetails);
//					} else if ("set".equals(detailsMethodName)) {
//						specs.setCRUDFlavorDetails(IOssjEntitySpecifics.SET,
//								flavEnum, flavorDetails);
//					} else if ("remove".equals(detailsMethodName)) {
//						specs.setCRUDFlavorDetails(IOssjEntitySpecifics.DELETE,
//								flavEnum, flavorDetails);
//					} else {
//						// Its on a method - need to find the method
//						boolean setone = false;
//						for (IMethod method : artifact.getMethods()) {
//							if (method.getName().equals(detailsMethodName)) {
//								try {
//									method.setEntityMethodFlavorDetails(
//											flavEnum, flavorDetails);
//									setone = true;
//								} catch (TigerstripeException t) {
//									String msgText = "Failed to set Flavor details for "
//											+ method.getName()
//											+ " on "
//											+ artifact.getFullyQualifiedName();
//									addMessage(messages, msgText, 0);
//									out.println("Error : " + msgText);
//									TigerstripeRuntime.logErrorMessage(
//											"TigerstripeException detected", t);
//									continue;
//								}
//							}
//						}
//						if (!setone) {
//							String msgText = "Failed to set Flavor details - could not find method "
//									+ flavorName
//									+ " on "
//									+ artifact.getFullyQualifiedName();
//							addMessage(messages, msgText, 0);
//							out.println("Error : " + msgText);
//						}
//
//					}
//				}
//
//			}
//
//		} else
			if (aType.equals(IEnumArtifact.class.getName())) {
			IEnumArtifact enumArt = (IEnumArtifact) artifact;
			OssjEnumSpecifics specs = (OssjEnumSpecifics) enumArt
					.getIStandardSpecifics();
			String baseType = element.getAttribute("baseType");
			IType type = artifact.makeField().makeType();
			type.setFullyQualifiedName(baseType);
			specs.setBaseIType(type);
//			Properties props = specs.getInterfaceProperties();
//			props.setProperty("package", element
//					.getAttribute("interfacePackage"));
//			props.setProperty("generate", element
//					.getAttribute("interfaceGenerate"));
//			specs.setInterfaceProperties(props);
//			// Things in the "Ossj section" are all "OPTIONAL", so we need to
//			// check for their presence
//			if (element.hasAttribute("extensible")) {
//				specs.setExtensible(Boolean.parseBoolean(element
//						.getAttribute("extensible")));
//			}
//
//		} else if (aType.equals(IEventArtifact.class.getName())) {
//			IEventArtifact eventArt = (IEventArtifact) artifact;
//			OssjEventSpecifics specs = (OssjEventSpecifics) eventArt
//					.getIStandardSpecifics();
//			Properties props = specs.getInterfaceProperties();
//			props.setProperty("package", element
//					.getAttribute("interfacePackage"));
//			props.setProperty("generate", element
//					.getAttribute("interfaceGenerate"));
//			specs.setInterfaceProperties(props);
//			// Things in the "Ossj section" are all "OPTIONAL", so we need to
//			// check for their presence
//			if (element.hasAttribute("singleExtension")) {
//				specs.setSingleExtensionType(Boolean.parseBoolean(element
//						.getAttribute("singleExtension")));
//			}
//			// Get the event descriptor ELEMENTS
//			NodeList descriptorNodes = element.getElementsByTagNameNS(
//					namespace, "eventDescriptorEntry");
//			for (int an = 0; an < descriptorNodes.getLength(); an++) {
//				Element descriptorElement = (Element) descriptorNodes.item(an);
//				ArrayList<IEventDescriptorEntry> entries = new ArrayList<IEventDescriptorEntry>(
//						Arrays.asList(specs.getEventDescriptorEntries()));
//				EventDescriptorEntry ede = new EventDescriptorEntry(
//						descriptorElement.getAttribute("label"),
//						descriptorElement.getAttribute("primitiveType"));
//				entries.add(ede);
//				specs.setEventDescriptorEntries(entries
//						.toArray(new EventDescriptorEntry[0]));
//			}
//			NodeList customDescriptorNodes = element.getElementsByTagNameNS(
//					namespace, "customEventDescriptorEntry");
//			for (int an = 0; an < customDescriptorNodes.getLength(); an++) {
//				Element descriptorElement = (Element) customDescriptorNodes
//						.item(an);
//				ArrayList<IEventDescriptorEntry> entries = new ArrayList<IEventDescriptorEntry>(
//						Arrays.asList(specs.getCustomEventDescriptorEntries()));
//				EventDescriptorEntry ede = new EventDescriptorEntry(
//						descriptorElement.getAttribute("label"),
//						descriptorElement.getAttribute("primitiveType"));
//				entries.add(ede);
//				specs.setCustomEventDescriptorEntries(entries
//						.toArray(new EventDescriptorEntry[0]));
//			}
//
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

				type
						.setTypeMultiplicity(IModelComponent.EMultiplicity
								.parse(element
										.getAttribute("returnedTypeMultiplicity")));
			queryArt.setReturnedType((IType) type);
//			Properties props = specs.getInterfaceProperties();
//			props.setProperty("package", element
//					.getAttribute("interfacePackage"));
//			props.setProperty("generate", element
//					.getAttribute("interfaceGenerate"));
//			specs.setInterfaceProperties(props);
//			// Things in the "Ossj section" are all "OPTIONAL", so we need to
//			// check for their presence
//			if (element.hasAttribute("singleExtension")) {
//				specs.setSingleExtensionType(Boolean.parseBoolean(element
//						.getAttribute("singleExtension")));
//			}
//			if (element.hasAttribute("sessionFactoryMethods")) {
//				specs.setSessionFactoryMethods(Boolean.parseBoolean(element
//						.getAttribute("sessionFactoryMethods")));
//			}

		} else if (aType.equals(ISessionArtifact.class.getName())) {
//			ISessionArtifact sessionArt = (ISessionArtifact) artifact;
//			IOssjArtifactSpecifics specs = (OssjArtifactSpecifics) sessionArt
//					.getIStandardSpecifics();
//			handleSession(element, sessionArt);
//			Properties props = specs.getInterfaceProperties();
//			props.setProperty("package", element
//					.getAttribute("interfacePackage"));
//			props.setProperty("generate", element
//					.getAttribute("interfaceGenerate"));
//			specs.setInterfaceProperties(props);

//		} else if (aType.equals(IExceptionArtifact.class.getName())) {
//			IAbstractArtifact art = (IAbstractArtifact) artifact;
//			IOssjArtifactSpecifics specs = (OssjArtifactSpecifics) art
//					.getIStandardSpecifics();
//			Properties props = specs.getInterfaceProperties();
//			props.setProperty("package", element
//					.getAttribute("interfacePackage"));
//			props.setProperty("generate", element
//					.getAttribute("interfaceGenerate"));
//			specs.setInterfaceProperties(props);

//		} else if (aType.equals(IDatatypeArtifact.class.getName())) {
//			IAbstractArtifact art = (IAbstractArtifact) artifact;
//			OssjDatatypeSpecifics specs = (OssjDatatypeSpecifics) art
//					.getIStandardSpecifics();
//			Properties props = specs.getInterfaceProperties();
//			props.setProperty("package", element
//					.getAttribute("interfacePackage"));
//			props.setProperty("generate", element
//					.getAttribute("interfaceGenerate"));
//			specs.setInterfaceProperties(props);
//			// Things in the "Ossj section" are all "OPTIONAL", so we need to
//			// check for their presence
//			if (element.hasAttribute("singleExtension")) {
//				specs.setSingleExtensionType(Boolean.parseBoolean(element
//						.getAttribute("singleExtension")));
//			}
//			if (element.hasAttribute("sessionFactoryMethods")) {
//				specs.setSessionFactoryMethods(Boolean.parseBoolean(element
//						.getAttribute("sessionFactoryMethods")));
//			}
			
//		} else if (aType.equals(IUpdateProcedureArtifact.class.getName())) {
//			IAbstractArtifact art = (IAbstractArtifact) artifact;
//			OssjUpdateProcedureSpecifics specs = (OssjUpdateProcedureSpecifics) art
//					.getIStandardSpecifics();
//			Properties props = specs.getInterfaceProperties();
//			props.setProperty("package", element
//					.getAttribute("interfacePackage"));
//			props.setProperty("generate", element
//					.getAttribute("interfaceGenerate"));
//			specs.setInterfaceProperties(props);
//			// Things in the "Ossj section" are all "OPTIONAL", so we need to
//			// check for their presence
//			if (element.hasAttribute("singleExtension")) {
//				specs.setSingleExtensionType(Boolean.parseBoolean(element
//						.getAttribute("singleExtension")));
//			}
//			if (element.hasAttribute("sessionFactoryMethods")) {
//				specs.setSessionFactoryMethods(Boolean.parseBoolean(element
//						.getAttribute("sessionFactoryMethods")));
//			}
		}
	}

	private void handleSession(Element element, ISessionArtifact session) {
		NodeList eventNodes = element.getElementsByTagNameNS(namespace,
				"emittedEvent");
		for (int ee = 0; ee < eventNodes.getLength(); ee++) {
			IEmittedEvent event = session.makeEmittedEvent();
			event.setFullyQualifiedName(((Element) eventNodes.item(ee))
					.getAttribute("name"));
			session.addEmittedEvent(event);
		}
		NodeList updateNodes = element.getElementsByTagNameNS(namespace,
				"exposedUpdateProcedure");
		for (int up = 0; up < updateNodes.getLength(); up++) {
			IExposedUpdateProcedure update = session
					.makeExposedUpdateProcedure();
			update.setFullyQualifiedName(((Element) updateNodes.item(up))
					.getAttribute("name"));
			session.addExposedUpdateProcedure(update);
		}
		NodeList queryNodes = element.getElementsByTagNameNS(namespace,
				"exposedQuery");
		for (int q = 0; q < queryNodes.getLength(); q++) {
			INamedQuery query = session.makeNamedQuery();
			query.setFullyQualifiedName(((Element) queryNodes.item(q))
					.getAttribute("name"));
			session.addNamedQuery(query);
		}
		NodeList entityDetailNodes = element.getElementsByTagNameNS(namespace,
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
			 * detailElement.getElementsByTagNameNS(namespace,"entityMethodFlavorDetails");
			 * for (int flav=0;flav<entityDetailNodes.getLength();flav++){
			 * Element flavor = (Element) entityDetailNodes.item(flav); String
			 * name = flavor.getAttribute("name"); String flag =
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
				.getElementsByTagNameNS(namespace, "field");
		for (int fn = 0; fn < fieldNodes.getLength(); fn++) {
			Element field = (Element) fieldNodes.item(fn);

			IField newField = artifact.makeField();
			IType type = newField.makeType();

			newField.setName(field.getAttribute("name"));
			type.setFullyQualifiedName(field.getAttribute("type"));
			// Need to support new and old versions
			type.setTypeMultiplicity(IModelComponent.EMultiplicity
						.parse(field.getAttribute("typeMultiplicity")));
			this.out.println(type.getTypeMultiplicity().getLabel());
			// end
			newField.setType(type);
			newField.setVisibility(EVisibility.parse(field
					.getAttribute("visibility")));
			newField.setOptional(Boolean.parseBoolean(field
					.getAttribute("optional")));
			newField.setReadOnly(Boolean.parseBoolean(field
					.getAttribute("readonly")));
//			if (field.hasAttribute("refBy")) {
//				newField.setRefBy(Integer.valueOf(field.getAttribute("refBy")));
//			}
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
			artifact.addField(newField);

		}
	}

	private void setLiterals(Element element, IAbstractArtifact artifact,
			PrintWriter out, MessageList messages) {
		NodeList literalNodes = element.getElementsByTagNameNS(namespace,
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
			artifact.addLiteral(newLiteral);
		}
	}

	private void setMethods(Element element, IAbstractArtifact artifact,
			PrintWriter out, MessageList messages) {
		NodeList methodNodes = element.getElementsByTagNameNS(namespace,
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
			
//			if (method.hasAttribute("returnRefBy")) {
//				newMethod.setReturnRefBy(Integer.valueOf(method
//					.getAttribute("returnRefBy")));
//			}
			
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

			NodeList argumentNodes = method.getElementsByTagNameNS(namespace,
					"argument");
			for (int a = 0; a < argumentNodes.getLength(); a++) {
				Element argument = (Element) argumentNodes.item(a);
				IArgument newArgument = newMethod.makeArgument();
				newArgument.setName(argument.getAttribute("name"));
//				if (argument.hasAttribute("refBy")){
//					newArgument.setRefBy(Integer.valueOf(argument
//							.getAttribute("refBy")));
//				}
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

			NodeList exceptionNodes = method.getElementsByTagNameNS(namespace,
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

			artifact.addMethod(newMethod);
		}
	}

	public void setAssociationEnds(IAssociationArtifact assArt,
			IAssociationEnd[] ends, Element element) {
		NodeList endNodes = element.getElementsByTagNameNS(namespace,
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
			thisEnd.setOrdered(Boolean.parseBoolean(endNode
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

								// TigerstripeRuntime.logInfoMessage(stereoElement.getAttribute("name"));

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
										.getElementsByTagNameNS(namespace,
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
															namespace, "value");

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

								// TigerstripeRuntime.logInfoMessage(stereoElement.getAttribute("name"));

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
										.getElementsByTagNameNS(namespace,
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
															namespace, "value");

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

	private String getArtifactTypeName(Element artifactElement) {
		String artifactTypeName = artifactElement.getAttribute("artifactType");
		return artifactTypeName;
	}
	
	private Specifics getArtifactSpecifics(Element artifactElement) {
		Specifics specifics = new Specifics();

		NodeList artifactType;
		
		
		artifactType = artifactElement.getElementsByTagNameNS(namespace,
				"managedEntitySpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IManagedEntityArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(namespace,
				"datatypeSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IDatatypeArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(namespace,
				"enumerationSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IEnumArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(namespace,
				"associationSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IAssociationArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(namespace,
				"associationClassSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IAssociationClassArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(namespace,
				"dependencySpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IDependencyArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(namespace,
				"updateProcedureSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IUpdateProcedureArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(namespace,
				"querySpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IQueryArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(namespace,
				"exceptionSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IExceptionArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(namespace,
				"notificationSpecifics");
		if (artifactType.getLength() > 0) {
			specifics.artifactType = IEventArtifact.class.getName();
			specifics.specificElement = (Element) artifactType.item(0);
			return specifics;
		}

		artifactType = artifactElement.getElementsByTagNameNS(namespace,
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
