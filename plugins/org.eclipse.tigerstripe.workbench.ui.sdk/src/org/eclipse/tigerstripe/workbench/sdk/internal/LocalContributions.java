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
package org.eclipse.tigerstripe.workbench.sdk.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.ibundle.IBundlePluginModel;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationExplicitFileRouterContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationPackageLabelContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationPropertyProviderContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationTypeContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ArtifactIconContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ArtifactMetadataContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AuditContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DecoratorContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DisabledPatternContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ModelComponentIconProviderContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.NamingContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.PatternFileContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationTypeContribution.Target;
import org.osgi.framework.Bundle;

public class LocalContributions extends AbstractProvider implements ISDKProvider, IResourceChangeListener{
	
	

	public static String AUDIT_EXT_PT = "org.eclipse.tigerstripe.workbench.base.customArtifactAuditor";
	public static String AUDIT_PART = "customArtifactAuditor";
	
	public static String DECORATOR_EXT_PT = "org.eclipse.tigerstripe.workbench.ui.base.labelDecorator";
	public static String DECORATOR_PART = "labelDecorator";
	
	public static String PATTERNS_EXT_PT = "org.eclipse.tigerstripe.workbench.base.creationPatterns";
	public static String PATTERNS_CREATION_PART = "org.eclipse.tigerstripe.workbench.base.creationPatterns";
	public static String PATTERNS_DISABLED_PART = "org.eclipse.tigerstripe.workbench.base.creationPatterns";
	
	public static String METADATA_EXT_PT = "org.eclipse.tigerstripe.metamodel.customArtifactMetadata";
	public static String METADATA_MODELICON_PART = "modelComponentIconProvider";
	public static String METADATA_ARTIFACTICON_PART = "artifactIcon";
	public static String METADATA_ARTIFACTMETADATA_PART = "artifactMetadata";
	
	public static String NAMING_EXT_PT = "org.eclipse.tigerstripe.workbench.base.customComponentNaming";
	public static String NAMING_PART = "customComponentNaming";
	
	public static String ANNOTATIONS_EXT_PT = "org.eclipse.tigerstripe.annotation.core.annotationType";
	public static String ANNOTATIONS_DEFINITION_PART = "defintion";
	
	public static String ANNOTATIONS_PACKAGELABEL_EXT_PT = "org.eclipse.tigerstripe.annotation.core.packageLabel";
	public static String ANNOTATIONS_PACKAGELABEL_PART = "label";
	
	public static String ANNOTATIONS_EXPLICITROUTER_EXT_PT = "org.eclipse.tigerstripe.annotation.ts2project.explicitFileRouter";
	public static String ANNOTATIONS_EXPLICITROUTER_PART = "router";
	
	public static String ANNOTATIONS_PROPERTYPROVIDER_EXT_PT = "org.eclipse.tigerstripe.annotation.ui.propertyProvider";
	public static String ANNOTATIONS_PROPERTYPROVIDER_PART = "provider";
	
	
	private ListenerList listenerList = new ListenerList();
	
	private class PatternLocation {
		public PatternLocation(IPluginModelBase contributor, String fileName) {
			super();
			this.contributor = contributor;
			this.fileName = fileName;
		}
		private IPluginModelBase contributor;
		private String fileName;
		public IPluginModelBase getContributor() {
			return contributor;
		}
		public String getFileName() {
			return fileName;
		}
		
	}
	
	// TODO This uses an internal PDE class
	public static boolean isWriteableModel(IPluginModelBase model){
		return (model instanceof IBundlePluginModel);
	}
	
	private Map<PatternLocation,IPattern> embeddedPatterns = null;
	
	
	public LocalContributions() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		// TODO This is general - triggered on ALL change in the ws
		System.out.println(event.getDelta().getResource().getName());
		findAll();
		 Object[] listeners = listenerList.getListeners();
		 for (int i = 0; i < listeners.length; ++i) {
		 	((IContributionListener) listeners[i]).resourceChanged(event);
		 }
		 
	}
	
	
	
	
	@SuppressWarnings("restriction")
	public void findAll() {
		
		
		IPluginModelBase[] models = PluginRegistry.getActiveModels();
		

		auditContributions = new ArrayList<AuditContribution>();
		namingContributions = new ArrayList<NamingContribution>();
		decoratorContributions = new ArrayList<DecoratorContribution>();
		patternFileContributions = new ArrayList<PatternFileContribution>();
		disabledPatternContributions = new ArrayList<DisabledPatternContribution>();
		artifactMetadataContributions = new ArrayList<ArtifactMetadataContribution>();
		artifactIconContributions = new ArrayList<ArtifactIconContribution>();
		modelComponentIconProviderContributions = new ArrayList<ModelComponentIconProviderContribution>();
		annotationTypeContributions = new ArrayList<AnnotationTypeContribution>();
		annotationPackageLabelContributions = new ArrayList<AnnotationPackageLabelContribution>();
		annotationExplicitFileRouterContributions = new ArrayList<AnnotationExplicitFileRouterContribution>();
		annotationPropertyProviderContributions = new ArrayList<AnnotationPropertyProviderContribution>();
		
		
		
		IPluginModelBase[] allModels = PluginRegistry.getActiveModels();
		
		
		

		allContributers = Arrays.asList(allModels);
		writeableContributers = new ArrayList<IPluginModelBase>();
		for (IPluginModelBase model : getAllContributers()){
			// This uses an internal PDE class!
			if (isWriteableModel(model)){
					writeableContributers.add(model);
			}	
		}
		 


			
			
		readAuditContributions(allModels);
		readDecoratorContributions(allModels);
		readArtifactMetadataContributions(models);
		readNamingContributions(allModels);
		readPatternContributions(allModels);
		readAnnotationTypeContributions(allModels);
		readAnnotationPackageLabelContributions(allModels);
		readAnnotationExplicitFileRouterContributions(allModels);
		readAnnotationPropertyProviderContributions(allModels);
	}




	public IPattern getPattern(IPluginModelBase contributor, String fileName) {
		if (embeddedPatterns == null){
			findAll();
		}

		for (PatternLocation loc : embeddedPatterns.keySet()){
			if (loc.getContributor().equals(contributor) &&
					loc.getFileName().equals(fileName)){
				// Same one!
				return embeddedPatterns.get(loc);
			}
		}
		
		return null;
	}	
	
	
	private void readPatternContributions(IPluginModelBase[] models) {
		embeddedPatterns = new HashMap<PatternLocation, IPattern>();
		for (IPluginModelBase model : models){

			for (IPluginExtension ext : model.getExtensions().getExtensions()){
				if (PATTERNS_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals("patternDefinition")){
							// Need to get the file from the contributing plugin

							IPluginAttribute patternFileNameAttribute  = ((IPluginElement) child).getAttribute("patternFile");
							String patternFileName = patternFileNameAttribute.getValue();
							IPluginAttribute validatorAttribute  = ((IPluginElement) child).getAttribute("validator_class");
							String validator = "";
							if (validatorAttribute != null){
								validator = validatorAttribute.getValue();
							}
							PatternFileContribution pfc = new PatternFileContribution(model,patternFileName,validator,readOnly);
							patternFileContributions.add(pfc);
							
							// put it in the map
							PatternLocation location = new PatternLocation(model,patternFileName);
							IPattern embeddedPattern = null;
							Bundle bundle = org.eclipse.core.runtime.Platform.getBundle(model.toString());
							if (bundle != null){
								// bundle will be null for plugins that are not yet deployed
								try {
									embeddedPattern = PatternFactory.parsePatternFile(bundle, patternFileName);
								} catch (Exception e){
									embeddedPattern = null;
								}
							} else {
								// Find a local resource
								IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
								IResource pfile = root.findMember(model.toString());
								if (pfile != null){
									try {
										embeddedPattern = PatternFactory.parsePatternFile(pfile, patternFileName);
									} catch (Exception e){
										embeddedPattern = null;
									}
								}else {
									embeddedPattern = null;
								}
								
							}
							embeddedPatterns.put(location, embeddedPattern);

						}

						if (child.getName().equals("disabledPattern")){
							// Need to get the file from the contributing plugin
							String disabledPatternName  = ((IPluginElement) child).getAttribute("patternName").getValue();
							DisabledPatternContribution dpc = new DisabledPatternContribution(model,disabledPatternName,readOnly);
							disabledPatternContributions.add(dpc);
						}
					}
				}
			}
		}
	}

	private void readNamingContributions(IPluginModelBase[] models) {
		for (IPluginModelBase model : models){
			
			for (IPluginExtension ext : model.getExtensions().getExtensions()){
				if (NAMING_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals("customNamingRule")){
							// need to protect against null values - name is an optional field
							IPluginAttribute ruleNameAttribute = ((IPluginElement) child).getAttribute("name");
							String ruleName = "";
							if ( ruleNameAttribute != null){
								 ruleName = ruleNameAttribute.getValue();
							}
							String className = ((IPluginElement) child).getAttribute("namingClass").getValue();
							NamingContribution nc = new NamingContribution(model,ruleName,className, readOnly);
							namingContributions.add(nc);
						}
					}
				}

			
		}
	}
		
	}

	private void readArtifactMetadataContributions(IPluginModelBase[] models) {
		for (IPluginModelBase model : models){

			for (IPluginExtension ext : model.getExtensions().getExtensions()){
				if (METADATA_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(METADATA_MODELICON_PART)){
							String aType = ((IPluginElement) child).getAttribute("artifactType").getValue();
							String provider = ((IPluginElement) child).getAttribute("provider").getValue();
							
							ModelComponentIconProviderContribution mcipc = new ModelComponentIconProviderContribution(
									model,aType, provider, readOnly );
							modelComponentIconProviderContributions.add(mcipc);
						} else if (child.getName().equals(METADATA_ARTIFACTICON_PART)){
							String aType =  ((IPluginElement) child).getAttribute("artifactName").getValue();
							String icon =  ((IPluginElement) child).getAttribute("icon").getValue();
							String icon_new =  ((IPluginElement) child).getAttribute("icon_new").getValue();
							String icon_gs =  ((IPluginElement) child).getAttribute("icon_gs").getValue();
							ArtifactIconContribution aic = new ArtifactIconContribution(model,icon,icon_new,icon_gs,aType, readOnly);
							artifactIconContributions.add(aic);
							
						} else if (child.getName().equals(METADATA_ARTIFACTMETADATA_PART)){
							String aType =  ((IPluginElement) child).getAttribute("artifactType").getValue();
							String userLabel =  ((IPluginElement) child).getAttribute("userLabel").getValue();
							Boolean hasFields =  null;
							if (((IPluginElement) child).getAttribute("hasFields") != null){
								hasFields = Boolean.valueOf(((IPluginElement) child).getAttribute("hasFields").getValue());
							}
							Boolean hasMethods = null;
							if (((IPluginElement) child).getAttribute("hasMethods") != null){
								hasMethods = Boolean.valueOf(((IPluginElement) child).getAttribute("hasMethods").getValue());
							}
						
							Boolean hasLiterals =  null;
							if (((IPluginElement) child).getAttribute("hasLiterals") != null){
								hasLiterals = Boolean.valueOf(((IPluginElement) child).getAttribute("hasLiterals").getValue());
							}
							String icon =  "";
							if (((IPluginElement) child).getAttribute("icon") != null){
								icon = ((IPluginElement) child).getAttribute("icon").getValue();
							}
							String icon_new =  "";
							if (((IPluginElement) child).getAttribute("icon_new") != null){
								icon_new = ((IPluginElement) child).getAttribute("icon_new").getValue();
							}
							String icon_gs =  "";
							if (((IPluginElement) child).getAttribute("icon_gs") != null){
								icon_gs = ((IPluginElement) child).getAttribute("icon_gs").getValue();
							}
							ArtifactMetadataContribution amc = new ArtifactMetadataContribution(model,
									aType,  userLabel, hasFields,hasMethods ,hasLiterals,icon, icon_new,icon_gs, readOnly);
							artifactMetadataContributions.add(amc);
						}
					}
				}
			}

		}
	}

	private void readDecoratorContributions(IPluginModelBase[] models) {
		
		for (IPluginModelBase model : models){
			
			for (IPluginExtension ext : model.getExtensions().getExtensions()){
				if (DECORATOR_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals("decorator")){
							String className = ((IPluginElement) child).getAttribute("class").getValue();
							
							DecoratorContribution dc = new DecoratorContribution(model,className, readOnly);
							decoratorContributions.add(dc);
						}
					}
				}
			}
		}
		
	}

	private void readAuditContributions(IPluginModelBase[] models) {
		
		for (IPluginModelBase model : models){
				
				for (IPluginExtension ext : model.getExtensions().getExtensions()){
					if (AUDIT_EXT_PT.equals(ext.getPoint())){
						boolean readOnly = ! isWriteableModel(model);
						IPluginObject[] children = ext.getChildren();
						for (IPluginObject child: children){
							if (child.getName().equals("customAuditRule")){
								// need to protect against null values - name is an optional field
								IPluginAttribute ruleNameAttribute = ((IPluginElement) child).getAttribute("name");
								String ruleName = "";
								if ( ruleNameAttribute != null){
									 ruleName = ruleNameAttribute.getValue();
								}
								String className = ((IPluginElement) child).getAttribute("auditorClass").getValue();
								AuditContribution ac = new AuditContribution(model,ruleName,className, readOnly);
								auditContributions.add(ac);
							}
						}
					}

				
			}
		}
	
	}


	private void readAnnotationTypeContributions(IPluginModelBase[] models) {

		for (IPluginModelBase model : models){

			for (IPluginExtension ext : model.getExtensions().getExtensions()){
				if (ANNOTATIONS_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals("definition")){
							IPluginAttribute nameAttribute = ((IPluginElement) child).getAttribute("name");
							String name = nameAttribute.getValue();
							
							IPluginAttribute eClassAttribute = ((IPluginElement) child).getAttribute("eclass");
							String eClass = eClassAttribute.getValue();
							
							IPluginAttribute namespaceAttribute = ((IPluginElement) child).getAttribute("epackage-uri");
							String namespace = namespaceAttribute.getValue();
							
							IPluginAttribute unique = ((IPluginElement) child).getAttribute("unique");
							String uniq = "";
							if (unique != null){
								uniq = unique.getValue();
							}
							AnnotationTypeContribution typeContribution = new AnnotationTypeContribution(
									model,
									name, eClass, namespace,uniq, readOnly); 
							// Then need to add some targets
							Collection<Target> targets = new ArrayList<Target>();
							IPluginObject[] grandChildren = ((IPluginElement) child).getChildren();
							for (IPluginObject grandChild: grandChildren){
								if (grandChild.getName().equals("target")){
									IPluginAttribute typeAttribute = ((IPluginElement) grandChild).getAttribute("type");
									String type = typeAttribute.getValue();
									IPluginAttribute uniqueTarget = ((IPluginElement) grandChild).getAttribute("unique");
									String uniqTraget = "";
									if (uniqueTarget != null){
										uniqTraget = uniqueTarget.getValue();
									}
									Target t = typeContribution.new Target(type,uniqTraget);
									targets.add(t);
								}
								
							}
							typeContribution.setTargets(targets);
							
							annotationTypeContributions.add(typeContribution);
							
						}
					}
				}
			}
		}
	}

	private void readAnnotationPackageLabelContributions(IPluginModelBase[] models) {

		for (IPluginModelBase model : models){

			for (IPluginExtension ext : model.getExtensions().getExtensions()){
				if (ANNOTATIONS_PACKAGELABEL_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(ANNOTATIONS_PACKAGELABEL_PART)){
							IPluginAttribute nameAttribute = ((IPluginElement) child).getAttribute("name");
							String name = nameAttribute.getValue();
							
							IPluginAttribute namespaceAttribute = ((IPluginElement) child).getAttribute("epackage-uri");
							String namespace = namespaceAttribute.getValue();
							AnnotationPackageLabelContribution annotationPackageLabelContribution = new AnnotationPackageLabelContribution(
									model, name,namespace, readOnly
									);
							annotationPackageLabelContributions.add(annotationPackageLabelContribution);
						}
					}
				}
			}
		}
	}

	private void readAnnotationExplicitFileRouterContributions(IPluginModelBase[] models) {

		for (IPluginModelBase model : models){

			for (IPluginExtension ext : model.getExtensions().getExtensions()){
				if (ANNOTATIONS_EXPLICITROUTER_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(ANNOTATIONS_EXPLICITROUTER_PART)){
							IPluginAttribute nsURIAttribute = ((IPluginElement) child).getAttribute("nsURI");
							String nsURI = "";
							if ( nsURIAttribute != null)
								nsURI =  nsURIAttribute.getValue();
							
							IPluginAttribute pathAttribute = ((IPluginElement) child).getAttribute("path");
							String path = "";
							if (pathAttribute != null)
								 path = pathAttribute.getValue();
							
							IPluginAttribute ePackageAttribute = ((IPluginElement) child).getAttribute("epackage");
							String ePackage = "";
							if (ePackageAttribute != null)
								 ePackage = ePackageAttribute.getValue();
							
							IPluginAttribute eClassAttribute = ((IPluginElement) child).getAttribute("eclass");
							String eClass = "";
							if (eClassAttribute != null)
								eClass = eClassAttribute.getValue();
							
							AnnotationExplicitFileRouterContribution contribution = new AnnotationExplicitFileRouterContribution(
									model, nsURI, path, eClass, ePackage, readOnly
									);
							annotationExplicitFileRouterContributions.add(contribution);
						}
					}
				}
			}
		}
	}

	private void readAnnotationPropertyProviderContributions(IPluginModelBase[] models) {

		for (IPluginModelBase model : models){

			for (IPluginExtension ext : model.getExtensions().getExtensions()){
				if (ANNOTATIONS_PROPERTYPROVIDER_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(ANNOTATIONS_PROPERTYPROVIDER_PART)){
							String className = ((IPluginElement) child).getAttribute("class").getValue();
							String priority = ((IPluginElement) child).getAttribute("priority").getValue();

							AnnotationPropertyProviderContribution dc = new AnnotationPropertyProviderContribution(model,className, priority, readOnly);
							annotationPropertyProviderContributions.add(dc);
						}
					}
				}
			}
		}

	}



	public void addListener(IContributionListener listener){
		listenerList.add(listener);
	}
	
	public void removeListener(IContributionListener listener){
		listenerList.remove(listener);
	}

}
