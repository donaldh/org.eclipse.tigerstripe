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
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationUsageExtractor;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ArtifactIconContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ArtifactMetadataContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AuditContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DecoratorContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DisabledPatternContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.GeneratedPackageContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ModelComponentIconProviderContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.NamingContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.PatternFileContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationTypeContribution.Target;
import org.osgi.framework.Bundle;

public class LocalContributions extends AbstractProvider implements ISDKProvider, IResourceChangeListener{
	
	

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
		generatedPackageContributions = new ArrayList<GeneratedPackageContribution>();
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
		readGeneratedPackageContributions(allModels);
		
		extractor = new AnnotationUsageExtractor(this);
	}




	public IPattern getPattern(IPluginModelBase contributor, String fileName) {
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
				if (SDKConstants.PATTERNS_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(SDKConstants.PATTERNS_CREATION_PART)){
							// Need to get the file from the contributing plugin

							IPluginAttribute patternFileNameAttribute  = ((IPluginElement) child).getAttribute("patternFile");
							String patternFileName = "";
							if (patternFileNameAttribute != null){
								patternFileName = patternFileNameAttribute.getValue();
							}
							
							
							IPluginAttribute validatorAttribute  = ((IPluginElement) child).getAttribute("validator_class");
							String validator = "";
							if (validatorAttribute != null){
								validator = validatorAttribute.getValue();
							}
							PatternFileContribution pfc = new PatternFileContribution(model,patternFileName,validator,readOnly
									, (IPluginElement) child);
							patternFileContributions.add(pfc);
							
							// put it in the map
							PatternLocation location = new PatternLocation(model,patternFileName);
							IPattern embeddedPattern = null;
							Bundle bundle = org.eclipse.core.runtime.Platform.getBundle(model.toString());
							if (bundle != null){
								// bundle will be null for plugins that are not yet deployed
								try {
									embeddedPattern = PatternFactory.getInstance().parsePatternFile(bundle, patternFileName);
								} catch (Exception e){
									embeddedPattern = null;
								}
							} else {
								// Find a local resource
								IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
								IResource pfile = root.findMember(model.toString());
								if (pfile != null){
									try {
										embeddedPattern = PatternFactory.getInstance().parsePatternFile(pfile, patternFileName);
									} catch (Exception e){
										embeddedPattern = null;
									}
								}else {
									embeddedPattern = null;
								}
								
							}
							embeddedPatterns.put(location, embeddedPattern);

						}

						if (child.getName().equals(SDKConstants.PATTERNS_DISABLED_PART)){
							// Need to get the file from the contributing plugin
							String disabledPatternName  = ((IPluginElement) child).getAttribute("patternName").getValue();
							DisabledPatternContribution dpc = new DisabledPatternContribution(model,disabledPatternName,readOnly, (IPluginElement) child);
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
				if (SDKConstants.NAMING_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(SDKConstants.NAMING_PART)){
							// need to protect against null values - name is an optional field
							IPluginAttribute ruleNameAttribute = ((IPluginElement) child).getAttribute("name");
							String ruleName = "";
							if ( ruleNameAttribute != null){
								 ruleName = ruleNameAttribute.getValue();
							}
							IPluginAttribute classAttribute = ((IPluginElement) child).getAttribute("namingClass");
							String className = "";
							if (classAttribute != null){
								className = classAttribute.getValue();
							}
							NamingContribution nc = new NamingContribution(model,ruleName,className, readOnly, (IPluginElement) child);
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
				if (SDKConstants.METADATA_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(SDKConstants.METADATA_MODELICON_PART)){
							
							IPluginAttribute typeAttribute = ((IPluginElement) child).getAttribute("artifactType");
							String aType = "";
							if (typeAttribute != null){
								aType = typeAttribute.getValue();
							}
							IPluginAttribute providerAttribute = ((IPluginElement) child).getAttribute("provider");
							String provider = "";
							if (providerAttribute != null){
								provider = providerAttribute.getValue();
							}
							
							ModelComponentIconProviderContribution mcipc = new ModelComponentIconProviderContribution(
									model,aType, provider, readOnly, (IPluginElement) child );
							modelComponentIconProviderContributions.add(mcipc);
						} else if (child.getName().equals(SDKConstants.METADATA_ARTIFACTICON_PART)){
							IPluginAttribute typeAttribute = ((IPluginElement) child).getAttribute("artifactName");
							String aType = "";
							if (typeAttribute != null){
								aType = typeAttribute.getValue();
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
							
							ArtifactIconContribution aic = new ArtifactIconContribution(model,icon,icon_new,icon_gs,aType, readOnly, (IPluginElement) child);
							artifactIconContributions.add(aic);
							
						} else if (child.getName().equals(SDKConstants.METADATA_ARTIFACTMETADATA_PART)){
							IPluginAttribute typeAttribute = ((IPluginElement) child).getAttribute("artifactType");
							String aType = "";
							if (typeAttribute != null){
								aType = typeAttribute.getValue();
							}
							IPluginAttribute labelAttribute = ((IPluginElement) child).getAttribute("userLabel");
							String userLabel = "";
							if (labelAttribute != null){
								userLabel = labelAttribute.getValue();
							}
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
									aType,  userLabel, hasFields,hasMethods ,hasLiterals,icon, icon_new,icon_gs, readOnly, (IPluginElement) child);
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
				if (SDKConstants.DECORATOR_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(SDKConstants.DECORATOR_PART)){
							IPluginAttribute classAttribute = ((IPluginElement) child).getAttribute("class");
							String className = "";
							if (classAttribute != null){
								className = classAttribute.getValue();
							}
							
							DecoratorContribution dc = new DecoratorContribution(model,className, readOnly, (IPluginElement) child);
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
					if (SDKConstants.AUDIT_EXT_PT.equals(ext.getPoint())){
						boolean readOnly = ! isWriteableModel(model);
						IPluginObject[] children = ext.getChildren();
						for (IPluginObject child: children){
							if (child.getName().equals(SDKConstants.AUDIT_PART)){
								// need to protect against null values - stuff can be badly defined
								IPluginAttribute ruleNameAttribute = ((IPluginElement) child).getAttribute("name");
								String ruleName = "";
								if ( ruleNameAttribute != null){
									 ruleName = ruleNameAttribute.getValue();
								}
								IPluginAttribute auditorClassAttribute = ((IPluginElement) child).getAttribute("auditorClass");
								String className = "";
								if (auditorClassAttribute != null){
									className = auditorClassAttribute.getValue();
								}
								
								
								
								AuditContribution ac = new AuditContribution(model,ruleName,className, readOnly, (IPluginElement) child);
								auditContributions.add(ac);
							}
						}
					}

				
			}
		}
	
	}


	private void readGeneratedPackageContributions(IPluginModelBase[] models) {

		for (IPluginModelBase model : models){

			for (IPluginExtension ext : model.getExtensions().getExtensions()){
				if (SDKConstants.EMF_GENERATED_PACKAGE_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(SDKConstants.EMF_GENERATED_PACKAGE_PACKAGE_PART)){
							IPluginAttribute uriAttribute = ((IPluginElement) child).getAttribute("uri");
							String uri = "";
							if (uriAttribute != null){
								uri = uriAttribute.getValue();
							}
							
							IPluginAttribute _ClassAttribute = ((IPluginElement) child).getAttribute("class");
							String _Class = "";
							if (_ClassAttribute != null){
								_Class= _ClassAttribute.getValue();
							}
							
							IPluginAttribute genModelAttribute = ((IPluginElement) child).getAttribute("genModel");
							String genModel = "";
							if (genModelAttribute != null){
								genModel = genModelAttribute.getValue();
							}
							
							GeneratedPackageContribution typeContribution = new GeneratedPackageContribution(
									model,
									uri, _Class, genModel, readOnly, (IPluginElement) child); 
							
							generatedPackageContributions.add(typeContribution);
							
						}
					}
				}
			}
		}
	}

	
	private void readAnnotationTypeContributions(IPluginModelBase[] models) {

		for (IPluginModelBase model : models){

			for (IPluginExtension ext : model.getExtensions().getExtensions()){
				if (SDKConstants.ANNOTATIONS_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(SDKConstants.ANNOTATIONS_DEFINITION_PART)){
							IPluginAttribute nameAttribute = ((IPluginElement) child).getAttribute("name");
							String name = "";
							if (nameAttribute != null){
								name = nameAttribute.getValue();
							}
							
							IPluginAttribute eClassAttribute = ((IPluginElement) child).getAttribute("eclass");
							String eClass = "";
							if (eClassAttribute != null){
								eClass= eClassAttribute.getValue();
							}
							
							IPluginAttribute namespaceAttribute = ((IPluginElement) child).getAttribute("epackage-uri");
							String namespace = "";
							if (namespaceAttribute != null){
								namespace = namespaceAttribute.getValue();
							}
							
							IPluginAttribute unique = ((IPluginElement) child).getAttribute("unique");
							String uniq = "";
							if (unique != null){
								uniq = unique.getValue();
							}
							AnnotationTypeContribution typeContribution = new AnnotationTypeContribution(
									model,
									name, eClass, namespace,uniq, readOnly, (IPluginElement) child); 
							// Then need to add some targets
							Collection<Target> targets = new ArrayList<Target>();
							IPluginObject[] grandChildren = ((IPluginElement) child).getChildren();
							for (IPluginObject grandChild: grandChildren){
								if (grandChild.getName().equals("target")){
									IPluginAttribute typeAttribute = ((IPluginElement) grandChild).getAttribute("type");
									String type = "";
									if ( typeAttribute != null){
										type = typeAttribute.getValue();
									}
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
				if (SDKConstants.ANNOTATIONS_PACKAGELABEL_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(SDKConstants.ANNOTATIONS_PACKAGELABEL_PART)){
							IPluginAttribute nameAttribute = ((IPluginElement) child).getAttribute("name");
							String name = "";
							if (nameAttribute != null){
								name = nameAttribute.getValue();
							}
							IPluginAttribute namespaceAttribute = ((IPluginElement) child).getAttribute("epackage-uri");
							String namespace = "";
							if (namespaceAttribute != null){
								namespace = namespaceAttribute.getValue();
							}
							AnnotationPackageLabelContribution annotationPackageLabelContribution = new AnnotationPackageLabelContribution(
									model, name,namespace, readOnly, (IPluginElement) child
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
				if (SDKConstants.ANNOTATIONS_EXPLICITROUTER_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(SDKConstants.ANNOTATIONS_EXPLICITROUTER_PART)){
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
									model, nsURI, path, eClass, ePackage, readOnly, (IPluginElement) child
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
				if (SDKConstants.ANNOTATIONS_PROPERTYPROVIDER_EXT_PT.equals(ext.getPoint())){
					boolean readOnly = ! isWriteableModel(model);
					IPluginObject[] children = ext.getChildren();
					for (IPluginObject child: children){
						if (child.getName().equals(SDKConstants.ANNOTATIONS_PROPERTYPROVIDER_PART)){
							IPluginAttribute classAttribute = ((IPluginElement) child).getAttribute("class");
							String className = "";
							if (classAttribute != null){
								className = classAttribute.getValue();
							}
							
							IPluginAttribute priorityAttribute = ((IPluginElement) child).getAttribute("priority");
							String priority = "";
							if (priorityAttribute != null){
								priority = priorityAttribute.getValue();
							}

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
	
	public String getPackageForAnnotation(AnnotationTypeContribution annotation){
		String uri = annotation.getNamespace();
		// Now look for this in the GeneratedPackages
		if (getGeneratedPackageContributions() != null){
			for (GeneratedPackageContribution gp : getGeneratedPackageContributions()){
				if (gp.getUri().equals(uri)){
					// We have a match!
					String packageClass = gp.get_class();
					if (packageClass != null){
						int indx = packageClass.lastIndexOf(".");
						return packageClass.substring(0, indx+1);
					}
				}
			}
		}
		return "";
	}
	
}
