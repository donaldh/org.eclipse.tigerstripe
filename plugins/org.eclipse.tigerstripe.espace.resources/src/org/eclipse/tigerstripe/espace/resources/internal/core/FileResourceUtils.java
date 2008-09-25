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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;

/**
 * @author Yuri Strot
 *
 */
public class FileResourceUtils {
	
	protected static final String INDEX_DIRECTORY = "INDEX/";
	protected static final String RESOURCES_STORAGE = "resources.xml";
	protected static final String DEFAULT_STORAGE = "defaultStorage.xml";
	
	private static File stateLocation;
	private static File indexDirectory;
	private static File defaultStorage;
	private static File resourceMetaFile;
	
	protected static File getStateLocation() {
		if (stateLocation == null)
			stateLocation = ResourcesPlugin.getDefault().getStateLocation().toFile();
		return stateLocation;
	}
	
	public static File getIndexDirectory() {
		if (indexDirectory == null)
			indexDirectory = new File(getStateLocation(), INDEX_DIRECTORY);
		return indexDirectory;
	}
	
	public static File getDefaultStorage() {
		if (defaultStorage == null)
			defaultStorage = new File(getStateLocation(), DEFAULT_STORAGE);
		return defaultStorage;
	}
	
	public static File getResourceMetaFile() {
		if (resourceMetaFile == null)
			resourceMetaFile = new File(getStateLocation(), RESOURCES_STORAGE);
		return resourceMetaFile;
	}
	
	public static File getFile(Resource resource) {
		URI uri = resource.getURI();
		String fString = uri.toFileString();
		if (fString != null) {
			return new File(fString);
		}
		return null;
	}
	
	public static boolean isIndexFile(File file) {
		File parent = file.getParentFile();
		return parent != null && parent.equals(getIndexDirectory());
	}
	
	public static boolean isSystemResource(Resource resource) {
		File file = getFile(resource);
		if (file != null) {
			if (isIndexFile(file)) return true;
			if (file.equals(getDefaultStorage())) return true;
			if (file.equals(getResourceMetaFile())) return true;
		}
		return false;
	}

}
