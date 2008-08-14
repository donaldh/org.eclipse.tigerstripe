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
package org.eclipse.tigerstripe.workbench.internal.api.patterns;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.ModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactLinkCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IStereotypeAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IStereotypeAddFeatureRequest.ECapableClass;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xml.TigerstripeXMLParserUtils;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.StereotypeInstance;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.patterns.INodePattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPatternFactory;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PatternFactory implements IPatternFactory {

	private static PatternFactory instance = null;
	private static Map<String,IPattern> registeredPatterns = new HashMap<String,IPattern>();
	private static Collection<String> disabledPatterns = new ArrayList<String>();
	
	private static ModelChangeRequestFactory requestFactory = new ModelChangeRequestFactory();
	
	
	// Important stuff for the XML parsing and Validation
	private static Bundle baseBundle = org.eclipse.core.runtime.Platform.getBundle("org.eclipse.tigerstripe.workbench.base");
	private static String schemaLocation = "src/java/org/eclipse/tigerstripe/workbench/patterns/schemas/tigerstripeCreationPatternSchema.xsd";
	
	private static String tigerstripeNamespace = "http://org.eclipse.tigerstripe/xml/tigerstripeExport/v1-0";
	private static String patternNamespace     = "http://org.eclipse.tigerstripe/xml/tigerstripeCreationPattern/v1-0";
	private static TigerstripeXMLParserUtils xmlParserUtils;
	private static Document patternDoc;
	private static DocumentBuilder parser;

	
	public static PatternFactory getInstance(){
		if (instance == null){
			instance = new PatternFactory();
			
			xmlParserUtils = new TigerstripeXMLParserUtils(tigerstripeNamespace);
			
			
			
			// read any new Patterns form the extension.
			try {
				IConfigurationElement[] elements  = Platform.getExtensionRegistry()
					.getConfigurationElementsFor("org.eclipse.tigerstripe.workbench.base.creationPatterns");
				
				
				for (IConfigurationElement element : elements){
					if (element.getName().equals("patternDefinition")){
						// Need to get the file from the contributing plugin
						String patternFileName  = element.getAttribute("patternFile");
						IContributor contributor = ((IExtension) element.getParent()).getContributor();
						Bundle bundle = org.eclipse.core.runtime.Platform.getBundle(contributor.getName());
						URL patternURL = bundle.getEntry(patternFileName);
						IPattern newPattern = parsePatternFile(patternURL);
						if (!registeredPatterns.containsKey(newPattern.getName())){
							registeredPatterns.put(newPattern.getName(), newPattern);
						} else {
							throw new TigerstripeException("Duplicate pattern name definition");
						}
					
					} else if (element.getName().equals("disabledPattern")){
						String disabledName  = element.getAttribute("patternName");
						if (!disabledPatterns.contains(disabledName)){
							disabledPatterns.add(disabledName);
						}
					}
					
				}
			}catch (Exception e ){
				BasePlugin.logErrorMessage("Failed to instantiate creation Patterns");
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	private static IPattern parsePatternFile(URL patternURL) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();
		factory.setIgnoringComments(true);
		factory.setCoalescing(true);
		factory.setNamespaceAware(true);

		parser = factory.newDocumentBuilder();
		try {
			patternDoc = parser.parse(patternURL.openStream());
		} catch (Exception e){
			String msgText = "Could not open patternURL "
				+ patternURL ;
			//System.out.println("Error : " + msgText);
			throw new TigerstripeException(msgText,e);
		}
		DOMSource patternSource = new DOMSource(patternDoc);

		URL tsSchemaURL = baseBundle.getEntry(schemaLocation);

		SchemaFactory scFactory = SchemaFactory
			.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema tsSchema = scFactory.newSchema(tsSchemaURL);
		Validator tsValidator = tsSchema.newValidator();
		try {
			tsValidator.validate(patternSource);
		} catch (Exception e) {
			String msgText = "XML file does not validate aginst the schema "
				+ patternURL + "'" + e.getMessage() + "'";
			//System.out.println("Error : " + msgText);
			throw new TigerstripeException(msgText,e);
		}

		
		
		// ===============================================================
		// Make a Pattern of the appropriate Type and set the general Info
		// ===============================================================
		Pattern pattern = null;
		NodeList patternNodes = patternDoc.getElementsByTagNameNS(patternNamespace,"creationPattern");
		// The schema says there should only be one of these per file.
		for (int pn = 0; pn < patternNodes.getLength(); pn++) {
			Element patternElement = (Element) patternNodes.item(pn);
			String patternName = patternElement.getAttribute("patternName");
			String patternType = patternElement.getAttribute("patternType");
			String uiLabel     = patternElement.getAttribute("uiLabel");
			String iconURL     = patternElement.getAttribute("iconURL");
			String description = "";
			NodeList descriptionNodes = patternDoc.getElementsByTagNameNS(patternNamespace,"description");
			for (int dn = 0; dn < descriptionNodes.getLength(); dn++) {
				Element descriptionElement = (Element) descriptionNodes.item(dn);
				description = descriptionElement.getTextContent();
			}
			
	
			if (patternType.equals("node")){
				pattern = new NodePattern();
			} else if (patternType.equals("relation")){
				pattern = new RelationPattern();
//			} else if (patternType.equals("composite")){
				//TODO - Phase 2 
				// Support composite Patterns to create several Artifacts in one go
			} else {
				throw new TigerstripeException("Invalid pattern Type in Extension Point");
			}
			pattern.setName(patternName);
			pattern.setUILabel(uiLabel);
			pattern.setIconURL(iconURL);
			pattern.setDescription(description);
			try {
				// For a composite we can get several artifacts, so each one will go in a 
				// seperate ArtifactPatterns and be assembled in the composite.
				if (pattern instanceof NodePattern || pattern instanceof RelationPattern){
					NodeList artifactNodes = patternDoc.getElementsByTagNameNS(tigerstripeNamespace,"artifact");
					if (artifactNodes.getLength() != 1){
						throw new TigerstripeException("Too many Artifacts in a Node or Relation pattern");
					} else {
						for (int an = 0; an < artifactNodes.getLength(); an++) {
							Element artifactElement = (Element) artifactNodes.item(an);
							singleArtifactPattern(pattern, artifactElement);
						}
					}
				} else {
					// We can add "subPatterns" to a CompositePattern
				}
			} catch (Exception e){
				// This means we failed to create the proper request
				throw new TigerstripeException("Failed to build pattern from Extension Point",e);
			}
		}
		return pattern;
	}

	private static void singleArtifactPattern(Pattern pattern,Element artifactElement ) throws TigerstripeException {
		
		String artifactType = xmlParserUtils.getArtifactType(artifactElement);
		// Check the profile allows this kind of artifact
		IWorkbenchProfile profile = TigerstripeCore
			.getWorkbenchProfileSession()
			.getActiveProfile();
		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
			.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
		if (prop.getDetailsForType(artifactType)
				.isEnabled()) {

			if (pattern instanceof INodePattern){
				addNodeRequests(pattern,artifactElement, artifactType);
			} else if (pattern instanceof IRelationPattern){
				addLinkRequests(pattern,artifactElement, artifactType);
			}
			String commentText = xmlParserUtils.getComment(artifactElement);
			if (commentText != null){
				IArtifactSetFeatureRequest setRequest =(IArtifactSetFeatureRequest)requestFactory.makeRequest(IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
				setRequest.setFeatureId(IArtifactSetFeatureRequest.COMMENT_FEATURE);
				setRequest.setFeatureValue(commentText);
				pattern.requests.add(setRequest);
			}
			// Optional attributes
			if (artifactElement.hasAttribute("isAbstract")){
				IArtifactSetFeatureRequest setRequest =(IArtifactSetFeatureRequest)requestFactory.makeRequest(IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
				setRequest.setFeatureId(IArtifactSetFeatureRequest.ISABSTRACT_FEATURE);
				setRequest.setFeatureValue(artifactElement.getAttribute("isAbstract"));
				pattern.requests.add(setRequest);
			}
			// artifact Stereotypes
			Collection<IStereotypeInstance> stereotypeInstances = xmlParserUtils.getStereotypes(artifactElement, "stereotypes");
			if ( stereotypeInstances.size()>0){
				for (IStereotypeInstance instance : stereotypeInstances){
					IStereotypeAddFeatureRequest stereotypeAddRequest = (IStereotypeAddFeatureRequest) requestFactory.makeRequest(IModelChangeRequestFactory.STEREOTYPE_ADD);
					stereotypeAddRequest.setCapableClass(ECapableClass.ARTIFACT);
					stereotypeAddRequest.setFeatureValue(instance);
					pattern.requests.add(stereotypeAddRequest);
				}
			}
			
			addComponentRequests(pattern,artifactElement);
		}
		
	}
	
	private static ArrayList<String> fieldCreateFeatures = new ArrayList<String>(Arrays.asList(
			IAttributeSetRequest.NAME_FEATURE,
			IAttributeSetRequest.TYPE_FEATURE,
			IAttributeSetRequest.MULTIPLICITY_FEATURE));
	
	private static ArrayList<String> literalCreateFeatures = new ArrayList<String>(Arrays.asList(
			ILiteralSetRequest.NAME_FEATURE,
			ILiteralSetRequest.TYPE_FEATURE,
			ILiteralSetRequest.VALUE_FEATURE));
	
	private static ArrayList<String> methodCreateFeatures = new ArrayList<String>(Arrays.asList(
			IMethodSetRequest.NAME_FEATURE,
			IMethodSetRequest.TYPE_FEATURE,
			IMethodSetRequest.MULTIPLICITY_FEATURE));
	
	private static ArrayList<String> methodAddFeatures = new ArrayList<String>(Arrays.asList(
			IMethodAddFeatureRequest.EXCEPTIONS_FEATURE,
			IMethodAddFeatureRequest.ARGUMENTS_FEATURE));
	
	/**
	 * This takes the artifactElement and generates a whole series of Modify actions.
	 * These are all items that have NO inputs from the UI.
	 * 
	 * @param pattern
	 * @param artifactElement
	 */
	private static void addComponentRequests(Pattern pattern, Element artifactElement)throws TigerstripeException {
		
		//TODO  Stereotypes. Annotations.
		
		// Do the fields
		Collection<Map<String,Object>> allFieldData = xmlParserUtils.getArtifactFieldData(artifactElement);
		for (Map<String,Object> fieldData : allFieldData){
			// Create the createRequest
			IAttributeCreateRequest createRequest =(IAttributeCreateRequest)requestFactory.makeRequest(IModelChangeRequestFactory.ATTRIBUTE_CREATE);
			createRequest.setAttributeName((String) fieldData.get(IAttributeSetRequest.NAME_FEATURE));
			createRequest.setAttributeType((String) fieldData.get(IAttributeSetRequest.TYPE_FEATURE));
			createRequest.setAttributeMultiplicity((String) fieldData.get(IAttributeSetRequest.MULTIPLICITY_FEATURE));
			pattern.requests.add(createRequest);
			// iterate over other features
			for (String feature : fieldData.keySet()){
				if (feature.equals(IStereotypeAddFeatureRequest.STEREOTYPE_FEATURE)){
					IStereotypeAddFeatureRequest stereotypeAddRequest = (IStereotypeAddFeatureRequest) requestFactory.makeRequest(IModelChangeRequestFactory.STEREOTYPE_ADD);
					stereotypeAddRequest.setCapableClass(ECapableClass.FIELD);
					stereotypeAddRequest.setCapableName((String) fieldData.get(IAttributeSetRequest.NAME_FEATURE));
					stereotypeAddRequest.setFeatureValue((IStereotypeInstance) fieldData.get(feature));
					pattern.requests.add(stereotypeAddRequest);
				} else if (!fieldCreateFeatures.contains(feature)){
					IAttributeSetRequest setRequest =(IAttributeSetRequest)requestFactory.makeRequest(IModelChangeRequestFactory.ATTRIBUTE_SET);
					setRequest.setAttributeName((String) fieldData.get(IAttributeSetRequest.NAME_FEATURE));
					setRequest.setFeatureId(feature);
					setRequest.setNewValue((String) fieldData.get(feature));
					pattern.requests.add(setRequest);
				}
			}
			// field Stereotypes
			
		}
		
		
		// Do the literals
		Collection<Map<String,Object>> allLiteralData = xmlParserUtils.getArtifactLiteralData(artifactElement);
		for (Map<String,Object> literalData : allLiteralData){
			// Create the createRequest
			ILiteralCreateRequest createRequest =(ILiteralCreateRequest)requestFactory.makeRequest(IModelChangeRequestFactory.LITERAL_CREATE);
			createRequest.setLiteralName((String) literalData.get(ILiteralSetRequest.NAME_FEATURE));
			createRequest.setLiteralType((String) literalData.get(ILiteralSetRequest.TYPE_FEATURE));
			createRequest.setLiteralValue((String) literalData.get(ILiteralSetRequest.VALUE_FEATURE));
			pattern.requests.add(createRequest);
			// iterate over other features
			for (String feature : literalData.keySet()){if (feature.equals(IStereotypeAddFeatureRequest.STEREOTYPE_FEATURE)){
				IStereotypeAddFeatureRequest stereotypeAddRequest = (IStereotypeAddFeatureRequest) requestFactory.makeRequest(IModelChangeRequestFactory.STEREOTYPE_ADD);
				stereotypeAddRequest.setCapableClass(ECapableClass.LITERAL);
				stereotypeAddRequest.setCapableName((String) literalData.get(ILiteralSetRequest.NAME_FEATURE));
				stereotypeAddRequest.setFeatureValue((IStereotypeInstance) literalData.get(feature));
				pattern.requests.add(stereotypeAddRequest);
			} else	if (!literalCreateFeatures.contains(feature)){
					ILiteralSetRequest setRequest =(ILiteralSetRequest)requestFactory.makeRequest(IModelChangeRequestFactory.LITERAL_SET);
					setRequest.setLiteralName((String) literalData.get(ILiteralSetRequest.NAME_FEATURE));
					setRequest.setFeatureId(feature);
					setRequest.setNewValue((String) literalData.get(feature));
					pattern.requests.add(setRequest);
				}
			}
		}
		
		// Do the methods
		Collection<Map<String,Object>> allmethodData = xmlParserUtils.getArtifactMethodData(artifactElement);
		for (Map<String,Object> methodData : allmethodData){
			// Create the createRequest
			IMethodCreateRequest createRequest =(IMethodCreateRequest)requestFactory.makeRequest(IModelChangeRequestFactory.METHOD_CREATE);
			createRequest.setMethodName((String) methodData.get(IMethodSetRequest.NAME_FEATURE));
			createRequest.setMethodType((String) methodData.get(IMethodSetRequest.TYPE_FEATURE));
			createRequest.setMethodMultiplicity((String) methodData.get(IMethodSetRequest.MULTIPLICITY_FEATURE));
			pattern.requests.add(createRequest);
			// Now any "Add" features -
			//ARGUMENTS
			if (methodData.containsKey(IMethodAddFeatureRequest.ARGUMENTS_FEATURE)){
				Collection<Map<String,Object>> allArgumentData = (Collection<Map<String,Object>>) methodData.get(IMethodAddFeatureRequest.ARGUMENTS_FEATURE);
				int argumentPostion = 0;
				for (Map<String,Object> argumentData : allArgumentData){
					for (String feature : argumentData.keySet()){
						if (feature.equals(IStereotypeAddFeatureRequest.STEREOTYPE_FEATURE)){
							IStereotypeAddFeatureRequest stereotypeAddRequest = (IStereotypeAddFeatureRequest) requestFactory.makeRequest(IModelChangeRequestFactory.STEREOTYPE_ADD);
							stereotypeAddRequest.setCapableClass(ECapableClass.METHODARGUMENT);
							stereotypeAddRequest.setArgumentPosition(argumentPostion);
							stereotypeAddRequest.setFeatureValue((IStereotypeInstance) argumentData.get(feature));
							pattern.requests.add(stereotypeAddRequest);
						} else {
							IMethodAddFeatureRequest addRequest = (IMethodAddFeatureRequest) requestFactory.makeRequest(IModelChangeRequestFactory.METHOD_ADD_FEATURE);
							addRequest.setFeatureId(feature);
							addRequest.setNewValue((String) argumentData.get(feature));
							addRequest.setArgumentPosition(argumentPostion);
							pattern.requests.add(addRequest);
					}
				}
				argumentPostion++;
				}
			}
			//EXCEPTIONS
			if (methodData.containsKey(IMethodAddFeatureRequest.EXCEPTIONS_FEATURE)){
				Collection<String> exceptions = (Collection<String>) methodData.get(IMethodAddFeatureRequest.EXCEPTIONS_FEATURE);
				for (String exceptionName : exceptions){
					IMethodAddFeatureRequest addRequest = (IMethodAddFeatureRequest) requestFactory.makeRequest(IModelChangeRequestFactory.METHOD_ADD_FEATURE);
					addRequest.setFeatureId(IMethodAddFeatureRequest.EXCEPTIONS_FEATURE);
					addRequest.setNewValue(exceptionName);
					pattern.requests.add(addRequest);
				}
			}
			
			
			// iterate over other features
			for (String feature : methodData.keySet()){

				if (feature.equals(IStereotypeAddFeatureRequest.STEREOTYPE_FEATURE)){
					IStereotypeAddFeatureRequest stereotypeAddRequest = (IStereotypeAddFeatureRequest) requestFactory.makeRequest(IModelChangeRequestFactory.STEREOTYPE_ADD);
					stereotypeAddRequest.setCapableClass(ECapableClass.METHOD);
					//stereotypeAddRequest.setCapableName((String) methodData.get(IMethodSetRequest.NAME_FEATURE));
					stereotypeAddRequest.setFeatureValue((IStereotypeInstance) methodData.get(feature));
					pattern.requests.add(stereotypeAddRequest);
				} else if (feature.equals(IStereotypeAddFeatureRequest.RETURN_STEREOTYPE_FEATURE)){
					IStereotypeAddFeatureRequest stereotypeAddRequest = (IStereotypeAddFeatureRequest) requestFactory.makeRequest(IModelChangeRequestFactory.STEREOTYPE_ADD);
					stereotypeAddRequest.setCapableClass(ECapableClass.METHODRETURN);
					stereotypeAddRequest.setCapableName((String) methodData.get(IMethodSetRequest.NAME_FEATURE));
					stereotypeAddRequest.setFeatureValue((IStereotypeInstance) methodData.get(feature));
					pattern.requests.add(stereotypeAddRequest);
				} else	if (!methodCreateFeatures.contains(feature) && !methodAddFeatures.contains(feature)){
					IMethodSetRequest setRequest =(IMethodSetRequest)requestFactory.makeRequest(IModelChangeRequestFactory.METHOD_SET);	
					// Note we can't set the *LABEL* here as it may be changing as we go along
					setRequest.setFeatureId(feature);
					setRequest.setNewValue((String) methodData.get(feature));
					pattern.requests.add(setRequest);
				}
			}
		}
	}
	
	
	private static void addNodeRequests(Pattern pattern, Element artifactElement, String artifactType)throws TigerstripeException {
		IArtifactCreateRequest createRequest = (IArtifactCreateRequest) requestFactory.makeRequest(IModelChangeRequestFactory.ARTIFACT_CREATE);
		createRequest.setArtifactType(artifactType);
		pattern.requests.add(createRequest);
		if (artifactType.equals(IManagedEntityArtifact.class.getName())){
			addImplementsRequests(pattern, artifactElement);
		}
		if (artifactType.equals(IEnumArtifact.class.getName())){
			addBaseTypeRequest(pattern, artifactElement);
		}
		if (artifactType.equals(IQueryArtifact.class.getName())){
			addReturnTypeRequest(pattern, artifactElement);
		}
	}
	
	private static void addImplementsRequests(Pattern pattern, Element artifactElement)throws TigerstripeException {
		Collection<String> implementsData = xmlParserUtils.getArtifactImplementsData(artifactElement);
		for (String implemented : implementsData){
			IArtifactAddFeatureRequest addRequest = (IArtifactAddFeatureRequest) requestFactory.makeRequest(IModelChangeRequestFactory.ARTIFACT_ADD_FEATURE);
			addRequest.setFeatureId(IArtifactAddFeatureRequest.IMPLEMENTS_FEATURE);
			addRequest.setFeatureValue(implemented);
			pattern.requests.add(addRequest);
		}
		
	}
	
	private static void addBaseTypeRequest(Pattern pattern, Element artifactElement)throws TigerstripeException {
		String baseType = xmlParserUtils.getBaseType(artifactElement);
		if (baseType != null){
			IArtifactSetFeatureRequest setRequest =(IArtifactSetFeatureRequest)requestFactory.makeRequest(IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
			setRequest.setFeatureId(IArtifactSetFeatureRequest.BASE_TYPE);
			setRequest.setFeatureValue(baseType);
			pattern.requests.add(setRequest);
		}
		
	}
	
	private static void addReturnTypeRequest(Pattern pattern, Element artifactElement)throws TigerstripeException {
		String returnType = xmlParserUtils.getReturnType(artifactElement);
		if (returnType != null){
			IArtifactSetFeatureRequest setRequest =(IArtifactSetFeatureRequest)requestFactory.makeRequest(IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
			setRequest.setFeatureId(IArtifactSetFeatureRequest.RETURNED_TYPE);
			setRequest.setFeatureValue(returnType);
			pattern.requests.add(setRequest);
		}
		
	}
	private static ArrayList<String> endCreateFeatures = new ArrayList<String>(Arrays.asList(
			IArtifactSetFeatureRequest.AENDMULTIPLICITY,
			IArtifactSetFeatureRequest.AENDNAVIGABLE,
			IArtifactSetFeatureRequest.ZENDMULTIPLICITY,
			IArtifactSetFeatureRequest.ZENDNAVIGABLE));
	/**
	 * This takes the artifactElement and generates a LINL request then some set SET actions.
	 * For the ends.
	 * 
	 * @param pattern
	 * @param artifactElement
	 */
	private static void addLinkRequests(Pattern pattern, Element artifactElement,String artifactType)throws TigerstripeException {
		Map<String,Object> endData = xmlParserUtils.getArtifactEndData(artifactElement);
		IArtifactLinkCreateRequest createRequest =(IArtifactLinkCreateRequest)requestFactory.makeRequest(IModelChangeRequestFactory.ARTIFACTLINK_CREATE);
		createRequest.setArtifactType(artifactType);
		createRequest.setAEndMultiplicity ((String) endData.get(IArtifactSetFeatureRequest.AENDMULTIPLICITY));
		createRequest.setZEndMultiplicity ((String) endData.get(IArtifactSetFeatureRequest.ZENDMULTIPLICITY));
		createRequest.setAEndNavigability (Boolean.parseBoolean((String)endData.get(IArtifactSetFeatureRequest.AENDNAVIGABLE)));
		createRequest.setZEndNavigability (Boolean.parseBoolean((String) endData.get(IArtifactSetFeatureRequest.ZENDNAVIGABLE)));
		pattern.requests.add(createRequest);
		for (String feature : endData.keySet()){
			if (!endCreateFeatures.contains(feature)){
				IArtifactSetFeatureRequest setRequest =(IArtifactSetFeatureRequest)requestFactory.makeRequest(IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
				setRequest.setFeatureId(feature);
				setRequest.setFeatureValue((String) endData.get(feature));
				pattern.requests.add(setRequest);
			}
		}
		
	}
	
	
	public IPattern getPattern(String patternName) {
		if (! disabledPatterns.contains(patternName)){
			return registeredPatterns.get(patternName);
		}
		return null;
	}

	public Map<String,IPattern> getRegisteredPatterns() {
		Map<String,IPattern> enabledPatterns = new HashMap<String, IPattern>();
		for (String patternName : registeredPatterns.keySet()){
			if (! disabledPatterns.contains(patternName)){
				enabledPatterns.put(patternName, registeredPatterns.get(patternName));
			}
		}
		return enabledPatterns;
	}
	
}
