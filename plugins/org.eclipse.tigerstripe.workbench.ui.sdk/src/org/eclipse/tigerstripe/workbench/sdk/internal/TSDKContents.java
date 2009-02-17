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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.repository.metamodel.providers.IModelComponentIconProvider;
import org.eclipse.tigerstripe.workbench.internal.builder.IArtifactAuditor;
import org.eclipse.tigerstripe.workbench.model.IComponentNameProvider;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPatternBasedCreationValidator;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ArtifactIconContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ArtifactMetadataContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AuditContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DecoratorContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DisabledPatternContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ModelComponentIconProviderContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.NamingContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.PatternFileContribution;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.viewers.ITigerstripeLabelDecorator;

public class TSDKContents extends AbstractProvider implements ISDKProvider{

	

	public void findAll(){
			auditContributions = new ArrayList<AuditContribution>();
			namingContributions = new ArrayList<NamingContribution>();
			decoratorContributions = new ArrayList<DecoratorContribution>();
			patternFileContributions = new ArrayList<PatternFileContribution>();
			disabledPatternContributions = new ArrayList<DisabledPatternContribution>();
			artifactMetadataContributions = new ArrayList<ArtifactMetadataContribution>();
			artifactIconContributions = new ArrayList<ArtifactIconContribution>();
			modelComponentIconProviderContributions = new ArrayList<ModelComponentIconProviderContribution>();
			
			readAuditContributions();
			readDecoratorContributions();
			readArtifactMetadataContributions();
			readNamingContributions();
			readPatternContributions();
			
	}
	
	private void readAuditContributions() {
		String EXT_PT = "org.eclipse.tigerstripe.workbench.base.customArtifactAuditor";
		
		
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
			.getConfigurationElementsFor(EXT_PT);
		int counter = 0;
		for (IConfigurationElement element : elements) {
			try {
				String ruleName = element.getAttribute("name");
				if (ruleName == null){
					// Give it a dummy name for now - we just have to assume that no-one in their right mind 
					// use such a name! 
					ruleName = "["+counter+"]";
					counter++;
				}
				IArtifactAuditor className = (IArtifactAuditor) element
					.createExecutableExtension("auditorClass");
				AuditContribution ac = new AuditContribution(element.getContributor().getName(),ruleName,className.getClass().getName(),true);
				auditContributions.add(ac);
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		}
	}
	
	private void readDecoratorContributions() {
		String EXT_PT = "org.eclipse.tigerstripe.workbench.ui.base.labelDecorator";

		IConfigurationElement[] elements = Platform.getExtensionRegistry()
		.getConfigurationElementsFor(EXT_PT);
		for (IConfigurationElement element : elements) {
			try {
				ITigerstripeLabelDecorator deco = (ITigerstripeLabelDecorator) element
					.createExecutableExtension("class");
				DecoratorContribution dc = new DecoratorContribution(element.getContributor().getName(),deco.getClass().getName(), true);
				decoratorContributions.add(dc);
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		}
	}
	
	private void readPatternContributions() {
		String EXT_PT = "org.eclipse.tigerstripe.workbench.base.creationPatterns";
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
			.getConfigurationElementsFor(EXT_PT);
		for (IConfigurationElement element : elements) {

			if (element.getName().equals("patternDefinition")){
				// Need to get the file from the contributing plugin
				try {
					String patternFileName  = element.getAttribute("patternFile");
					IPatternBasedCreationValidator validator = null;
					String validatorClassName = "";
					if (element.getAttribute("validator_class") != null){
						validator = (IPatternBasedCreationValidator) element.createExecutableExtension("validator_class");
						validatorClassName= validator.getClass().getName();
					}
					PatternFileContribution pfc = new PatternFileContribution(element.getContributor().getName(),patternFileName,validatorClassName, true);
					patternFileContributions.add(pfc);
				} catch (CoreException e) {
					EclipsePlugin.log(e);
				}
			}

			if (element.getName().equals("disabledPattern")){
				// Need to get the file from the contributing plugin
				String disabledPatternName  = element.getAttribute("patternName");
				DisabledPatternContribution dpc = new DisabledPatternContribution(element.getContributor().getName(),disabledPatternName,true);
				disabledPatternContributions.add(dpc);
			}
			
		}
	}
	
	private void readArtifactMetadataContributions() {

		String EXT_PT = "org.eclipse.tigerstripe.metamodel.customArtifactMetadata";
		
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
			.getConfigurationElementsFor(EXT_PT);
		for (IConfigurationElement element : elements) {
			if (element.getName().equals("artifactMetadata")){
				String aType =  element.getAttribute("artifactType");
				String userLabel =  element.getAttribute("userLabel");
				Boolean hasFields =  null;
				if (element.getAttribute("hasFields") != null){
					hasFields = Boolean.valueOf(element.getAttribute("hasFields"));
				}
				Boolean hasMethods = null;
				if (element.getAttribute("hasMethods") != null){
					hasMethods = Boolean.valueOf(element.getAttribute("hasMethods"));
				}
			
				Boolean hasLiterals =  null;
				if ( element.getAttribute("hasLiterals") != null){
					hasLiterals = Boolean.valueOf(element.getAttribute("hasLiterals"));
				}
				String icon =  element.getAttribute("icon");
				String icon_new =  element.getAttribute("icon_new");
				String icon_gs =  element.getAttribute("icon_gs");
				ArtifactMetadataContribution amc = new ArtifactMetadataContribution(element.getContributor().getName(),
						aType,  userLabel, hasFields,hasMethods ,hasLiterals,icon, icon_new,icon_gs, true);
				artifactMetadataContributions.add(amc);
			}
			if (element.getName().equals("modelComponentIconProvider")){
				try {
					String aType =  element.getAttribute("artifactType");
					IModelComponentIconProvider provider =  (IModelComponentIconProvider) element.createExecutableExtension("provider");
					ModelComponentIconProviderContribution mcipc = new ModelComponentIconProviderContribution(
							element.getContributor().getName(),aType, provider.getClass().getName(), true );
					modelComponentIconProviderContributions.add(mcipc);
				} catch (CoreException e) {
					EclipsePlugin.log(e);
				}
			}
			if (element.getName().equals("artifactIcon")){
				String aType =  element.getAttribute("artifactName");
				String icon =  element.getAttribute("icon");
				String icon_new =  element.getAttribute("icon_new");
				String icon_gs =  element.getAttribute("icon_gs");
				ArtifactIconContribution aic = new ArtifactIconContribution(element.getContributor().getName(),icon,icon_new,icon_gs,aType, true);
				artifactIconContributions.add(aic);
			}
		}
	}
	
	private void readNamingContributions() {

		String EXT_PT = "org.eclipse.tigerstripe.workbench.base.customComponentNaming";
		
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
		.getConfigurationElementsFor(EXT_PT);
		for (IConfigurationElement element : elements) {
			try {
				String ruleName = element.getAttribute("name");
				IComponentNameProvider className = (IComponentNameProvider) element
					.createExecutableExtension("namingClass");
				NamingContribution nc = new NamingContribution(element.getContributor().getName(),ruleName,className.getClass().getName(),true);
				namingContributions.add(nc);
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	public IPattern getPattern(String contributor, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
