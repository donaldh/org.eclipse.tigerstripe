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

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.ModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IActiveWorkbenchProfileChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xml.TigerstripeXMLParserUtils;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPatternBasedWizardValidator;
import org.eclipse.tigerstripe.workbench.patterns.IPatternFactory;
import org.eclipse.tigerstripe.workbench.patterns.IProjectPattern;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.AbstractContributionFactory;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.services.IServiceLocator;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PatternFactory implements IPatternFactory, IActiveWorkbenchProfileChangeListener {

	
	private static AbstractContributionFactory artifactPatternMenuAddition;
	private static AbstractContributionFactory artifactPatternToolbarAddition;
	private static AbstractContributionFactory artifactPatternToolbarDropDownsAddition;
	private static AbstractContributionFactory projectPatternToolbarAddition;
	private static AbstractContributionFactory projectPatternToolbarDropDownsAddition;
	
	public void profileChanged(IWorkbenchProfile newActiveProfile) {
		final IMenuService menuService = (IMenuService) PlatformUI.getWorkbench()
			.getService(IMenuService.class);
		if (Display.getDefault() != null) { 
			Display.getDefault().asyncExec(new Runnable() {                
				public void run() {                    
					menuService.removeContributionFactory(artifactPatternMenuAddition);
					menuService.removeContributionFactory(artifactPatternToolbarAddition);
					menuService.removeContributionFactory(artifactPatternToolbarDropDownsAddition);
					menuService.removeContributionFactory(projectPatternToolbarAddition);
					menuService.removeContributionFactory(projectPatternToolbarDropDownsAddition); 
					addPatternMenuContribution();
				}                            
			});        

		}



	}




	private static PatternFactory instance = null;
	private static Map<String,IPattern> discoveredPatterns = new HashMap<String,IPattern>();
	private static Map<String,IPattern> registeredPatterns = new LinkedHashMap<String,IPattern>();
	private static Collection<String> disabledPatterns = new ArrayList<String>();

	private static ModelChangeRequestFactory requestFactory = new ModelChangeRequestFactory();
	
	
	// Important stuff for the XML parsing and Validation
	private static Bundle baseBundle = org.eclipse.core.runtime.Platform.getBundle("org.eclipse.tigerstripe.workbench.base");
	private static String schemaLocation = "resources/schemas/tigerstripeCreationPatternSchema-v1-0.xsd";
	
	private static String tigerstripeNamespace = "http://org.eclipse.tigerstripe/xml/tigerstripeExport/v2-0";
	private static String patternNamespace     = "http://org.eclipse.tigerstripe/xml/tigerstripeCreationPattern/v1-0";
	private static TigerstripeXMLParserUtils xmlParserUtils;
	private static Document patternDoc;
	private static DocumentBuilder parser;

	private static Integer undefined = 10000;
	private static IArtifactManagerSession session;
	
	public static PatternFactory getInstance(){
		if (instance == null){
			instance = new PatternFactory();
			
			xmlParserUtils = new TigerstripeXMLParserUtils(tigerstripeNamespace);
			
			// read any new Patterns form the extension.
			try {
				IConfigurationElement[] elements  = Platform.getExtensionRegistry()
					.getConfigurationElementsFor("org.eclipse.tigerstripe.workbench.base.creationPatterns");
				
				Map<Integer,String> patternList = new TreeMap<Integer,String>();
				
				for (IConfigurationElement element : elements){
					if (element.getName().equals("patternDefinition")){
						// Need to get the file from the contributing plugin
						String patternFileName  = element.getAttribute("patternFile");
						IContributor contributor = ((IExtension) element.getParent()).getContributor();
						
						Bundle bundle = org.eclipse.core.runtime.Platform.getBundle(contributor.getName());
						
						try {
							IPattern newPattern = parsePatternFile(bundle,patternFileName);
							
							if (element.getAttribute("validator_class") != null){
								IPatternBasedWizardValidator validator = (IPatternBasedWizardValidator) element
									.createExecutableExtension("validator_class");
								if (validator != null){
									((Pattern) newPattern).setWizardValidator(validator);
								}
							}
							
							if (!discoveredPatterns.containsKey(newPattern.getName())){
								discoveredPatterns.put(newPattern.getName(), newPattern);
								int index = newPattern.getIndex();
								
								// protect against two indexes the same
								while (patternList.get(index) != null){
									index++;
								}
								patternList.put(index, newPattern.getName());
								
							} else {
								throw new TigerstripeException("Duplicate pattern name definition");
							}
						} catch (TigerstripeException t){
							BasePlugin.logErrorMessage("Failed to instantiate creation Pattern");
							t.printStackTrace();
						}
					
					} else if (element.getName().equals("disabledPattern")){
						String disabledName  = element.getAttribute("patternName");
						if (!disabledPatterns.contains(disabledName)){
							disabledPatterns.add(disabledName);
						}
					}
					
				}
				
			// sort the discovered patterns based on their indexes 
			// into a LinkedHashSet
				for (Integer patternIndex : patternList.keySet()){
					String patt = patternList.get(patternIndex);
					registeredPatterns.put(patt, discoveredPatterns.get(patt));
				}
				
			
			// Make patterns available from menu
				
			addPatternMenuContribution();
				
			}catch (Exception e ){
				BasePlugin.logErrorMessage("Failed to instantiate creation Patterns");
				e.printStackTrace();
			}
		}
		TigerstripeCore
			.getWorkbenchProfileSession().addActiveProfileListener(instance);
		return instance;
	}
	
	private static IPattern parsePatternFile(Bundle bundle,String patternFileName) throws Exception {
		URL patternURL = bundle.getEntry(patternFileName);
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
			String iconPath     = patternElement.getAttribute("iconPath");
			String disabledIconPath     = patternElement.getAttribute("disabledIconPath");
			
			String description = "";
			NodeList descriptionNodes = patternDoc.getElementsByTagNameNS(patternNamespace,"description");
			for (int dn = 0; dn < descriptionNodes.getLength(); dn++) {
				Element descriptionElement = (Element) descriptionNodes.item(dn);
				description = descriptionElement.getTextContent();
			}
			
			
			// ===============================================================
			// This is where we decide which sort it is....
			// =============================================================== 
			
			try {
				// For a composite we can get several artifacts, so each one will go in a 
				// seperate ArtifactPatterns and be assembled in the composite.
				if (patternType.equals("node") || patternType.equals("relation")){
					NodeList artifactNodes = patternDoc.getElementsByTagNameNS(tigerstripeNamespace,"artifact");
					if (artifactNodes.getLength() != 1){
						throw new TigerstripeException("Too many Artifacts in a Node or Relation pattern");
					} else {
						for (int an = 0; an < artifactNodes.getLength(); an++) {
							Element artifactElement = (Element) artifactNodes.item(an);
							String artifactType = xmlParserUtils.getArtifactType(artifactElement);
							// Need to examine the subtype of Artifact pattern
							if (artifactType.equals(IEnumArtifact.class.getName())){
								pattern = new EnumPattern();
							} else if (artifactType.equals(IQueryArtifact.class.getName())){
								pattern = new QueryPattern();
							} else if (patternType.equals("relation")){
								pattern = new RelationPattern();
							} else {
								pattern = new NodePattern();
							}
							pattern.setParserUtils(xmlParserUtils);
							pattern.setElement(artifactElement);
							
						}
					}
				} else if (patternType.equals("composite")){
					//TODO - Phase 2 
					// Support composite Patterns to create several Artifacts in one go
				} else if (patternType.equals("project")){
					NodeList projectNodes = patternDoc.getElementsByTagNameNS(tigerstripeNamespace,"tigerstripeProject");
					if (projectNodes.getLength() != 1){
						throw new TigerstripeException("Too many Projects in a Project pattern");
					} else {
						for (int an = 0; an < projectNodes.getLength(); an++) {
							Element projectElement = (Element) projectNodes.item(an);
							pattern = new ProjectPattern();
							pattern.setParserUtils(xmlParserUtils);
							pattern.setElement(projectElement);
							
						}
					}
				} else {
					throw new TigerstripeException("Invalid pattern Type in Extension Point");
				}
				

				pattern.setName(patternName);
				pattern.setUILabel(uiLabel);
				pattern.setDescription(description);
				
				URL url = bundle.getResource(iconPath);
				if (url == null){
					// We may need  to look in the metamodel plugin for this one!
					Bundle uiBundle = Platform.getBundle("org.eclipse.tigerstripe.metamodel");
					if (uiBundle != null){
						url = uiBundle.getResource(iconPath);
					}
				}
				
				pattern.setIconURL(url);
				pattern.setIconPath(iconPath);
				
				URL disabledUrl = bundle.getResource(disabledIconPath);
				if (disabledUrl == null){
					// We may need  to look in the metamodel plugin for this one!
					Bundle uiBundle = Platform.getBundle("org.eclipse.tigerstripe.metamodel");
					if (uiBundle != null){
						disabledUrl = uiBundle.getResource(disabledIconPath);
					}
				}
				
				pattern.setDisabledIconURL(disabledUrl);
				pattern.setDisabledIconPath(disabledIconPath);
				
				if (patternElement.hasAttribute("index") && patternElement.getAttribute("index").length() != 0){
					pattern.setIndex(Integer.parseInt(patternElement.getAttribute("index")));
				} else {
					pattern.setIndex(undefined);
				}
				
			} catch (Exception e){
				// This means we failed to create the proper request
				throw new TigerstripeException("Failed to build pattern from Extension Point",e);
			}
		}
		return pattern;
	}
	

	public IPattern getPattern(String patternName) {
		if (getRegisteredPatterns().keySet().contains(patternName)){
			return getRegisteredPatterns().get(patternName);
		}
		return null;
	}

	// This returns patterns that are enabled and supported by the current profile
	// TODO Make them respect some kind of order
	
	public Map<String,IPattern> getRegisteredPatterns() {
		Map<String,IPattern> enabledPatterns = new LinkedHashMap<String, IPattern>();
		IWorkbenchProfile profile = TigerstripeCore
			.getWorkbenchProfileSession()
			.getActiveProfile();
		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
			.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
		for (String patternName : registeredPatterns.keySet()){
			if (! disabledPatterns.contains(patternName)){
				// Check the profile allows this kind of artifact
				IPattern pattern = registeredPatterns.get(patternName);
				if (pattern instanceof ArtifactPattern){
					if (prop.getDetailsForType(((ArtifactPattern)pattern).getTargetArtifactType())
							.isEnabled()) {
						enabledPatterns.put(patternName, registeredPatterns.get(patternName));
					}
				} else {
					enabledPatterns.put(patternName, registeredPatterns.get(patternName));
				}
			}
		}
		return enabledPatterns;
	}

	public static void addPatternMenuContribution() {
		//final Map<String,Expression> expressions = loadExpressionDefinitions();
		IMenuService menuService = (IMenuService) PlatformUI.getWorkbench()
		.getService(IMenuService.class);
		
		final IServiceLocator locator = (IServiceLocator) PlatformUI.getWorkbench();
		
		IWorkbenchProfile profile = TigerstripeCore
			.getWorkbenchProfileSession()
			.getActiveProfile();
		final CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
			.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
		
		
		// See how many Project Patterns we have...
		// If only one then we don't need a drop down!
		
		int projectCount = 0;

		String dropDownProjectItemId = "";
		String dropDownProjectItemName = "";
		for (String key : registeredPatterns.keySet()){
			IPattern pattern = registeredPatterns.get(key);

			if (! disabledPatterns.contains(pattern.getName())){
				if (pattern instanceof IProjectPattern){
					if (projectCount == 0){
						// just grab the name of teh first one that will be created
						// This is need to anchor the artifact stuff
						dropDownProjectItemId = "org.eclipse.tigerstripe.workbench.ui.base.new.patterns.project.dropdown."+pattern.getName();
						dropDownProjectItemName = pattern.getName();
					}
					projectCount++;
				}
			}
		}
		// TODO - problem if there are NONE ?

		final String ddProjectItemName = dropDownProjectItemName;
		final String ddProjectItemId = dropDownProjectItemId;
		//=======================
		int style;
		
		if (projectCount ==1){
			style = CommandContributionItem.STYLE_PUSH;
		} else {
			style = CommandContributionItem.STYLE_PULLDOWN;
		}

		final int buttonStyle = style;
		/*
		 * This part is for the PROJECT patterns in the DROPDPOWN
		 * 
		 */
		// This section should do whichever one we decide is the "top" level for the drop down

		projectPatternToolbarAddition = new AbstractContributionFactory(
				"toolbar:org.eclipse.tigerstripe.workbench.ui.base.toolbar?after=org.eclipse.tigerstripe.workbench.ui.base.start", null){

			@Override
			public void createContributionItems(IServiceLocator serviceLocator,
					IContributionRoot additions) {
				for (String key : registeredPatterns.keySet()){
					IPattern pattern = registeredPatterns.get(key);

					if (! disabledPatterns.contains(pattern.getName())){
						if (pattern instanceof IProjectPattern){
							CommandContributionItemParameter thisOne  = new CommandContributionItemParameter(locator,
									"org.eclipse.tigerstripe.workbench.ui.base.new.patterns.project.dropdown."+pattern.getName(),
									"org.eclipse.tigerstripe.workbench.ui.base.patternBasedProjectCreate",
									buttonStyle
							);

							Map parms = new HashMap();
							parms.put("org.eclipse.tigerstripe.workbench.ui.base.patternName", pattern.getName());				     
							thisOne.parameters = parms;

							thisOne.label = pattern.getUILabel();
							thisOne.icon = pattern.getImageDescriptor();

							CommandContributionItem newItem = new CommandContributionItem(thisOne);
							Expression referenceExpression = null;
							additions.addContributionItem(newItem,referenceExpression);
							// Only do the drop down once
							break;
						}
					}
				}

			}
		};

		menuService.addContributionFactory(projectPatternToolbarAddition);

		projectPatternToolbarDropDownsAddition = new AbstractContributionFactory(
				"menu:"+dropDownProjectItemId, null){

			@Override
			public void createContributionItems(IServiceLocator serviceLocator,
					IContributionRoot additions) {
				for (String key : registeredPatterns.keySet()){
					IPattern pattern = registeredPatterns.get(key);
					if (! disabledPatterns.contains(pattern.getName()) && ! ddProjectItemName.equals(pattern.getName())){
						if (pattern instanceof IProjectPattern){
							CommandContributionItemParameter thisOne  = new CommandContributionItemParameter(locator,
									"org.eclipse.tigerstripe.workbench.ui.base.new.patterns.project.dropdown"+pattern.getName(),
									"org.eclipse.tigerstripe.workbench.ui.base.patternBasedProjectCreate",
									CommandContributionItem.STYLE_PUSH
							);

							Map parms = new HashMap();
							parms.put("org.eclipse.tigerstripe.workbench.ui.base.patternName", pattern.getName());				     
							thisOne.parameters = parms;

							thisOne.label = pattern.getUILabel();
							thisOne.icon = pattern.getImageDescriptor();

							CommandContributionItem newItem = new CommandContributionItem(thisOne);
							Expression referenceExpression = null;
							additions.addContributionItem(newItem,referenceExpression);
						}
					}
				}
			}
		};

		menuService.addContributionFactory(projectPatternToolbarDropDownsAddition);



		/*
		 * This part is for the ARTIFACT patterns in the MENU
		 * May in future need to add "Composites"
		 */
		artifactPatternMenuAddition = new AbstractContributionFactory(
				"menu:org.eclipse.tigerstripe.workbench.ui.base.new", null){
			
			
			@Override
			public void createContributionItems(IServiceLocator serviceLocator,
					IContributionRoot additions) {
				
				for (String key : registeredPatterns.keySet()){
					IPattern pattern = registeredPatterns.get(key);
					
					if (! disabledPatterns.contains(pattern.getName())){
						if (pattern instanceof IArtifactPattern){
							String type = ((IArtifactPattern) pattern).getTargetArtifactType();
							if (prop.getDetailsForType(type).isEnabled()){


								CommandContributionItemParameter thisOne  = new CommandContributionItemParameter(locator,
										"org.eclipse.tigerstripe.workbench.ui.base.new.patterns."+pattern.getName(),
										"org.eclipse.tigerstripe.workbench.ui.base.patternBasedCreate",
										CommandContributionItem.STYLE_PUSH
								);

								Map parms = new HashMap();
								parms.put("org.eclipse.tigerstripe.workbench.ui.base.patternName", pattern.getName());				     
								thisOne.parameters = parms;

								thisOne.label = pattern.getUILabel();
								thisOne.icon = pattern.getImageDescriptor();

								CommandContributionItem newItem = new CommandContributionItem(thisOne);
								Expression referenceExpression = null;
								//						if (pattern instanceof IArtifactPattern){
								//							String target = ((IArtifactPattern) pattern).getTargetArtifactType();
								//							referenceExpression = makeExpression(target);
								//						}
								additions.addContributionItem(newItem,referenceExpression);
							}

						}
					}
				}

			}
		};

		
		menuService.addContributionFactory(artifactPatternMenuAddition);

		/*
		 * This part is for the ARTIFACT patterns in the DROPDPOWN
		 * May in future need to add "Composites"
		 */
		// This section should do whichever one we decide is the "top" level for the drop down
		
		
		// TODO - If this top level item gets disabled in your profile you are in trouble! As it disables the whole list!
		artifactPatternToolbarAddition = new AbstractContributionFactory(
				"toolbar:org.eclipse.tigerstripe.workbench.ui.base.toolbar?after="+ddProjectItemId, null){
			
			@Override
			public void createContributionItems(IServiceLocator serviceLocator,
					IContributionRoot additions) {
				for (String key : registeredPatterns.keySet()){
					IPattern pattern = registeredPatterns.get(key);
					
					if (! disabledPatterns.contains(pattern.getName())){

						if (pattern instanceof IArtifactPattern){
							String type = ((IArtifactPattern) pattern).getTargetArtifactType();
							if (prop.getDetailsForType(type).isEnabled()){
							CommandContributionItemParameter thisOne  = new CommandContributionItemParameter(locator,
									"org.eclipse.tigerstripe.workbench.ui.base.new.patterns.dropdown."+pattern.getName(),
									"org.eclipse.tigerstripe.workbench.ui.base.patternBasedCreate",
									CommandContributionItem.STYLE_PULLDOWN
							);

							Map parms = new HashMap();
							parms.put("org.eclipse.tigerstripe.workbench.ui.base.patternName", pattern.getName());				     
							thisOne.parameters = parms;

							thisOne.label = pattern.getUILabel();
							thisOne.icon = pattern.getImageDescriptor();

							CommandContributionItem newItem = new CommandContributionItem(thisOne);
							Expression referenceExpression = null;
//							if (pattern instanceof IArtifactPattern){
//								String target = ((IArtifactPattern) pattern).getTargetArtifactType();
//								referenceExpression = makeExpression(target);
//							}
							additions.addContributionItem(newItem,referenceExpression);
						}
						}
					}
					// Only do the drop down once
					break;
				}
				
			}
		};

		menuService.addContributionFactory(artifactPatternToolbarAddition);
		//== Temp
		String dropDownItemId = "";
		String dropDownItemName = "";
		for (String key : registeredPatterns.keySet()){
			IPattern pattern = registeredPatterns.get(key);
			
			if (! disabledPatterns.contains(pattern.getName())){
				if (pattern instanceof IArtifactPattern){
					String type = ((IArtifactPattern) pattern).getTargetArtifactType();
					if (prop.getDetailsForType(type).isEnabled()){
						dropDownItemId = "org.eclipse.tigerstripe.workbench.ui.base.new.patterns.dropdown."+pattern.getName();
						dropDownItemName = pattern.getName();
					}
					break;
				}
			}
		}
		final String ddItemName = dropDownItemName;
		//=======================

		artifactPatternToolbarDropDownsAddition = new AbstractContributionFactory(
				"menu:"+dropDownItemId, null){

			@Override
			public void createContributionItems(IServiceLocator serviceLocator,
					IContributionRoot additions) {
				for (String key : registeredPatterns.keySet()){
					IPattern pattern = registeredPatterns.get(key);

					if (! disabledPatterns.contains(pattern.getName()) && ! ddItemName.equals(pattern.getName())){
						if (pattern instanceof IArtifactPattern){
							String type = ((IArtifactPattern) pattern).getTargetArtifactType();
							if (prop.getDetailsForType(type).isEnabled()){
								CommandContributionItemParameter thisOne  = new CommandContributionItemParameter(locator,
										"org.eclipse.tigerstripe.workbench.ui.base.new.patterns."+pattern.getName(),
										"org.eclipse.tigerstripe.workbench.ui.base.patternBasedCreate",
										CommandContributionItem.STYLE_PUSH
								);

								Map parms = new HashMap();
								parms.put("org.eclipse.tigerstripe.workbench.ui.base.patternName", pattern.getName());				     
								thisOne.parameters = parms;

								thisOne.label = pattern.getUILabel();
								thisOne.icon = pattern.getImageDescriptor();

								CommandContributionItem newItem = new CommandContributionItem(thisOne);
								Expression referenceExpression = null;
								//							if (pattern instanceof IArtifactPattern){
								//								String target = ((IArtifactPattern) pattern).getTargetArtifactType();
								//								referenceExpression = makeExpression(target);
								//							}
								additions.addContributionItem(newItem,referenceExpression);
							}
						}
					}
				}
			}
		};

		menuService.addContributionFactory(artifactPatternToolbarDropDownsAddition);


	}
	
	
	
	
	private static Expression makeExpression(String target){
		String type = "";
		if (target.equals(IManagedEntityArtifact.class.getName())){
			 type="entity";
		}
		if (target.equals(IDatatypeArtifact.class.getName())){
			type="datatype";
		}
		if (target.equals(IEnumArtifact.class.getName())){
			type="enumeration";
		}
		if (target.equals(IExceptionArtifact.class.getName())){
			type="exception";
		}
		if (target.equals(IEventArtifact.class.getName())){
			type="event";
		}
		if (target.equals(IQueryArtifact.class.getName())){
			type="query";
		}
		if (target.equals(IUpdateProcedureArtifact.class.getName())){
			type="updateProcedure";
		}
		if (target.equals(ISessionArtifact.class.getName())){
			type="session";
		}
		if (target.equals(IAssociationArtifact.class.getName())){
			type="association";
		}
		if (target.equals(IAssociationClassArtifact.class.getName())){
			type="associationClass";
		}
		if (target.equals(IDependencyArtifact.class.getName())){
			type="dependency";
		}
		if (target.equals(IPackageArtifact.class.getName())){
			type="package";
		}
		if ("".equals(type)){
			return null;
		}
		try {
			String xml	= "<reference definitionId=\"org.eclipse.tigerstripe.workbench.ui.base.enabledInProfile."+type+"\"/>";
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
			Expression referenceExpression = ExpressionConverter.getDefault().perform(doc.getDocumentElement());
			return referenceExpression;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
