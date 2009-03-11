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
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xml.TigerstripeXMLParserUtils;
import org.eclipse.tigerstripe.workbench.model.annotation.AnnotationHelper;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPatternBasedCreationValidator;
import org.w3c.dom.Element;

public abstract class Pattern implements IPattern {

	protected int index;
	protected String name;
	protected String description;
	protected String uiLabel;
	protected String iconPath;
	protected URL iconURL;
	protected String disabledIconPath;
	protected URL disabledIconURL;
	protected Collection<IModelChangeRequest> requests = new ArrayList<IModelChangeRequest>();
	
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


	public IPatternBasedCreationValidator getValidator() {
		return validator;
	}

	public void setValidator(IPatternBasedCreationValidator validator) {
		this.validator = validator;
	}
	
	protected void addUniqueAnnotations(Collection<Class<?>> usedAnnotations, Element element){
		try{
			Collection<EObject> artifactAnnos = xmlParserUtils.getAnnotations(element);
			for (EObject eObject : artifactAnnos){
				Class<?> objectClass  = eObject.getClass().getInterfaces()[0];

				if (! usedAnnotations.contains(objectClass)){
					usedAnnotations.add(objectClass);
				}

			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * This method is used by the SDK to check for usage of Annotations
	 */
	public Collection<Class<?>> getUsedAnnotations(Element element) {
		Collection<Class<?>> usedAnnotations = new ArrayList<Class<?>>();
		addUniqueAnnotations(usedAnnotations, element);
		return usedAnnotations;
	}
	
	/**
	 * This method is used by the SDK to check for usage of Annotations
	 */
	public Collection<Class<?>> getUsedAnnotations() {
		return getUsedAnnotations(getElement());
	}
}
