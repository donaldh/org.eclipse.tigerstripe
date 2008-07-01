/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.espace.resources.internal.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;

/**
 * @author Yuri Strot
 *
 */
public class IndexStorage {
	
	public static final String INDEX_DIRECTORY = "INDEX/";
	private ResourceSet resourceSet;
	
	private Map<String, Resource> map;
	
	public IndexStorage(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
		init();
	}
	
	protected void init() {
		map = new HashMap<String, Resource>();
		File indexDirectory = new File(ResourcesPlugin.getDefault(
				).getStateLocation().toFile(), INDEX_DIRECTORY);
		if (!indexDirectory.exists())
			return;
		File[] files = indexDirectory.listFiles();
		for (int i = 0; i < files.length; i++) {
			try {
				if (getFeatureName(files[i]) != null)
					initResource(files[i]);
			}
			catch (Exception e) {
				//ignore
			}
		}
	}
	
	public void removeIndex() {
		map = new HashMap<String, Resource>();
		File indexDirectory = new File(ResourcesPlugin.getDefault(
				).getStateLocation().toFile(), INDEX_DIRECTORY);
		if (!indexDirectory.exists())
			return;
		File[] files = indexDirectory.listFiles();
		for (int i = 0; i < files.length; i++) {
			try {
				if (getFeatureName(files[i]) != null)
					files[i].delete();
			}
			catch (Exception e) {
				//ignore
			}
		}
	}
	
	protected String getFeatureName(File file) {
		String name = file.getName();
		if (name.toLowerCase().endsWith(".xml")) {
			return name.substring(0, name.length() - 4);
		}
		return null;
	}
	
	protected void initResource(File file) {
		URI uri = URI.createFileURI(file.getAbsolutePath());
		Resource resource = resourceSet.getResource(uri, false);
		if (resource == null)
			resource = resourceSet.createResource(uri);
		if (resource != null) {
			try {
				resource.load(null);
            }
            catch (IOException e) {
            	//ignore exception
            }
    		EcoreUtil.resolveAll(resource);
    		map.put(getFeatureName(file), resource);
		}
	}
	
	protected Resource loadResource(File file) {
		URI uri = URI.createFileURI(file.getAbsolutePath());
		Resource resource = resourceSet.getResource(uri, false);
		if (resource == null)
			resource = resourceSet.createResource(uri);
		if (resource != null) {
			try {
				resource.load(null);
            }
            catch (IOException e) {
            	//ignore exception
            }
    		EcoreUtil.resolveAll(resource);
		}
		return resource;
	}
	
	public Resource getResource(String featureName, boolean create) {
		Resource res = map.get(featureName);
		if (res == null) {
			File file = new File(ResourcesPlugin.getDefault().getStateLocation().toFile(), 
					INDEX_DIRECTORY + featureName + ".xml");
			if (!file.exists() && !create)
				return null;
			res = loadResource(file);
			map.put(featureName, res);
		}
		return res;
	}
	
	public Collection<Resource> getResources() {
		return map.values();
	}
	
	public void saveAll() throws IOException {
		for (Resource resource : getResources()) {
            resource.save(null);
		}
	}

}
