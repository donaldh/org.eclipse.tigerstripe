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
package org.eclipse.tigerstripe.generators.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.plugins.PluginLog;


public class DocPackage {

	private String fullyQualifiedName;
	public  boolean elements = false;
	private Map<String,DocPackage> subPackageMap = new HashMap<String,DocPackage>();
	
	
	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}
	
	public String getName() {
		return getFullyQualifiedName().substring(getFullyQualifiedName().lastIndexOf(".")+1);
	}
	
	public void setFullyQualifiedName(String fqn) {
		this.fullyQualifiedName = fqn;
	}
	
	public Collection<DocPackage> getSubPackages() {
		return DocUtils.sortByName(subPackageMap.values());
	}
	public void addSubPackage(DocPackage packageToAdd) {
		if (! subPackageMap.containsKey(packageToAdd.getFullyQualifiedName())){
			PluginLog.logDebug("Adding sub package"+packageToAdd.getFullyQualifiedName()+ " to "+getFullyQualifiedName());
			subPackageMap.put(packageToAdd.getFullyQualifiedName(), packageToAdd);
		}
	}

	public boolean hasElements() {
		return elements;
	}

	public void setElements(boolean elements) {
		
		this.elements = elements;
	}
	
	
	
	
}
