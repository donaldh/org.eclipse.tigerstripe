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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.osgi.framework.Bundle;

public class Pattern implements IPattern {

	

	protected String name;
	protected String description;
	protected String uiLabel;
	protected String iconPath;
	protected URL iconURL;
	protected Collection<IModelChangeRequest> requests = new ArrayList<IModelChangeRequest>();
	protected Collection<IPatternAnnotation> patternAnnotations = new ArrayList<IPatternAnnotation>();
	
	public Collection<IPatternAnnotation> getPatternAnnotations() {
		return patternAnnotations;
	}

	public void setPatternAnnotations(
			Collection<IPatternAnnotation> patternAnnotations) {
		this.patternAnnotations = patternAnnotations;
	}

	protected void setName(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUILabel() {
		return uiLabel;
	}

	public void setUILabel(String uiLabel) {
		this.uiLabel = uiLabel;
	}

	public String getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	public URL getIconURL() {
		return iconURL;
	}
	
	public void setIconURL(URL iconURL) {
		this.iconURL = iconURL;
	}

	public ImageDescriptor getDescriptor(){
		ImageDescriptor descriptor = null;
		descriptor = ImageDescriptor.createFromURL(this.iconURL);
		return descriptor;

	}
	

	public class PatternAnnotation implements IPatternAnnotation{

		private String target;
		
		private EObject content;
		
		public PatternAnnotation(){
			
		}

		public void setTarget(String target) {
			this.target = target;
		}

		public String getTarget() {
			return this.target;
		}

		/**
		 * @return the content
		 */
		public EObject getAnnotationContent() {
			return content;
		}

		/**
		 * @param content the content to set
		 */
		public void setAnnotationContent(EObject content) {
			this.content = content;
		}
	}
}
