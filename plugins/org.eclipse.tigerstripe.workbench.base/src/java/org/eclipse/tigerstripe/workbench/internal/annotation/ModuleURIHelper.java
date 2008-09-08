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
package org.eclipse.tigerstripe.workbench.internal.annotation;

import org.eclipse.emf.common.util.URI;

/**
 * This class contains module URI; URI of the annotation file 
 * in the archive and module ID
 * 
 * @author Yuri Strot
 */
public class ModuleURIHelper {
	
	private URI moduleUri;
	private URI archiveUri;
	private String moduleID;
	
	public ModuleURIHelper(URI moduleUri) {
		this.moduleUri = moduleUri;
		init();
	}
	
	protected void init() {
		String path = moduleUri.path();
		int moduleStart = 0;
		int moduleEnd = path.indexOf("/");
		if (moduleEnd == 0) {
			moduleStart = 1;
			moduleEnd = path.indexOf("/", 1);
		}
		moduleID = path.substring(moduleStart, moduleEnd);
		path = path.substring(moduleEnd + 1);
		archiveUri = URI.createURI("archive:file:/" + path);
	}
	
	/**
	 * @return the archiveUri
	 */
	public URI getArchiveUri() {
		return archiveUri;
	}
	
	/**
	 * @return the moduleID
	 */
	public String getModuleID() {
		return moduleID;
	}
	
	/**
	 * @return the moduleUri
	 */
	public URI getModuleUri() {
		return moduleUri;
	}

}
