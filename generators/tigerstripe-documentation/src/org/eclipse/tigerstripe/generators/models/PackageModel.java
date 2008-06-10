/******************************************************************************* 
 * 
 * Copyright (c) 2008 Cisco Systems, Inc. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 *    Cisco Systems, Inc. - dkeysell
********************************************************************************/
package org.eclipse.tigerstripe.generators.models;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.generators.utils.DocUtils;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactModel;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

import org.eclipse.tigerstripe.generators.models.AbstractClassModel;
import org.eclipse.tigerstripe.generators.models.ModelFactory;

/**
 * This class is used to "spoof" a package when a rule needs to run over all artifacts 
 * in a given package.
 * 
 * The package is taken from the artifact that is passed in the setIArtifact.
 * 
 * When this is used in a rule, the rule should be set to not overwrite to prevent 
 * unnecessary processing.
 * 
 *
 */
public class PackageModel implements IArtifactModel{

	private IAbstractArtifact artifact;
	private IPluginConfig pluginRef;

	private String fullyQualifiedName;
	
	public PackageModel(){
		
	}
	
	public void setIArtifact(IAbstractArtifact artifact) {
		this.artifact = artifact;
		this.fullyQualifiedName = artifact.getPackage();
	}

	public void setPluginConfig(IPluginConfig pluginRef) {
		this.pluginRef = pluginRef;
		
	}

	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public String getName() {
		return getFullyQualifiedName().substring(getFullyQualifiedName().lastIndexOf(".")+1);
	}
	
	/**
	 * This will do a lot of work every time called. If necessary it could be made more efficient?
	 * 
	 * @return
	 */
	public Collection<AbstractClassModel> getOwnedElements(){
		ArrayList<AbstractClassModel> ownedElements = new ArrayList<AbstractClassModel>();
		try {
			ITigerstripeModelProject project = artifact.getTigerstripeProject();
			IArtifactManagerSession session = project.getArtifactManagerSession();
			IArtifactQuery query = session.makeQuery(IQueryAllArtifacts.class.getName());
			Collection allArtifacts = session.queryArtifact(query);
			for (Object art : allArtifacts){
				IAbstractArtifact localArtifact = (IAbstractArtifact) art;
				if (localArtifact.getPackage().equals(getFullyQualifiedName())){
					PluginLog.logDebug("Adding "+localArtifact.getName()+ " to ownedElements of "+getFullyQualifiedName());
					AbstractClassModel model = ModelFactory.getInstance().getModel(localArtifact);
					model.setPluginRef(this.pluginRef);
					ownedElements.add(model);
				}
				
			}
		} catch (NullPointerException n){
			PluginLog.logError("Failure to read package artifacts - no project");
		} catch (TigerstripeException t){
			PluginLog.logError("Failure to read package artifacts",t);
		}
		return ownedElements;

	}
	
	public String getHash(){
		return DocUtils.getHash(getFullyQualifiedName());
	}

	public Collection<PackageModel> getSubPackages(){
		ArrayList<PackageModel> subPackages = new ArrayList<PackageModel>();
		ArrayList<String> subPackageNames = new ArrayList<String>();
		
		try {
			// This *might* not work for a project with dependencies
			ITigerstripeModelProject project = artifact.getTigerstripeProject();
			IArtifactManagerSession session = project.getArtifactManagerSession();
			IArtifactQuery query = session.makeQuery(IQueryAllArtifacts.class.getName());
			Collection allArtifacts = session.queryArtifact(query);
			for (Object art : allArtifacts){
				IAbstractArtifact localArtifact = (IAbstractArtifact) art;
				if (localArtifact.getPackage().startsWith(getFullyQualifiedName())){
					String subPackage = localArtifact.getPackage().replaceFirst(getFullyQualifiedName(), "");
			
					if ( subPackage.length() > 0 ){
						// We need to add a new subPackage here
						String newPackageName = subPackage.substring(1);
						if (newPackageName.contains(".")){
							newPackageName = newPackageName.substring(0,newPackageName.indexOf("."));
						}
						
						if (!subPackageNames.contains(newPackageName)){
							// There might be no artifact at this level of the hierarchy, so set the name directly
							PluginLog.logDebug("Adding "+newPackageName+ " to subPackages of "+getFullyQualifiedName());
							PackageModel model = new PackageModel();
							model.setIArtifact(localArtifact);
							// Must do this AFTER setting the artifact
							model.setFullyQualifiedName(getFullyQualifiedName()+"."+newPackageName);
							model.setPluginConfig(this.pluginRef);
							subPackages.add(model);
							subPackageNames.add(newPackageName);
						}
					}
					
				}
				
			}
		} catch (NullPointerException n){
			PluginLog.logError("Failure to read package artifacts - no project");
		} catch (TigerstripeException t){
			PluginLog.logError("Failure to read package artifacts",t);
		}
		return subPackages;
	}

	private void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}
	
}
