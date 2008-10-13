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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xml.TigerstripeXMLParserUtils;
import org.eclipse.tigerstripe.workbench.model.annotation.AnnotationHelper;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPatternBasedCreationValidator;
import org.w3c.dom.Element;

public class Pattern implements IPattern {

	protected int index;
	protected String name;
	protected String description;
	protected String uiLabel;
	protected String iconPath;
	protected URL iconURL;
	protected String disabledIconPath;
	protected URL disabledIconURL;
	protected Collection<IModelChangeRequest> requests = new ArrayList<IModelChangeRequest>();
	protected Collection<IPatternAnnotation> patternAnnotations = new ArrayList<IPatternAnnotation>();
	
	protected AnnotationHelper helper = AnnotationHelper.getInstance();
	protected TigerstripeXMLParserUtils xmlParserUtils;
	protected Element element;
	protected IPatternBasedCreationValidator validator;
	
	public void setParserUtils(TigerstripeXMLParserUtils utils) {
		this.xmlParserUtils = utils;
	}
	
	public Element getElement() {
		return element;
	}
	
	public void setElement(Element element) {
		this.element = element;
	}
	
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
	
	public String getDisabledIconPath() {
		return disabledIconPath;
	}

	public void setDisabledIconPath(String disabledIconPath) {
		this.disabledIconPath = disabledIconPath;
	}

	public URL getDisabledIconURL() {
		return disabledIconURL;
	}

	public void setDisabledIconURL(URL disabledIconURL) {
		this.disabledIconURL = disabledIconURL;
	}

	public ImageDescriptor getImageDescriptor(){
		ImageDescriptor descriptor = null;
		descriptor = ImageDescriptor.createFromURL(this.iconURL);
		return descriptor;

	}
	

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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


	public IPatternBasedCreationValidator getWizardValidator() {
		return validator;
	}

	public void setWizardValidator(IPatternBasedCreationValidator validator) {
		this.validator = validator;
	}
	
	
}
