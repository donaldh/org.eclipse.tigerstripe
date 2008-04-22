/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.xmi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.NewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.enums.LabelRef;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.model.ArtifactAttributeModel;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.model.ArtifactMethodModel;

/**
 * An Element as annotated to be turned into a POJO
 * 
 * @author Eric Dillon
 * 
 */
public class AnnotatedElement {

	public final static String DATATYPE = "datatype";
	public final static String ENTITY = "entity";
	public final static String ENUMERATION = "enumeration";
	public final static String UNKNOWN = "unknown";

	private List attributeRefs = new ArrayList();
	private List<ArtifactMethodModel> methodModels = new ArrayList<ArtifactMethodModel>();
	private List labelRefs = new ArrayList();
	private String extendedArtifact;
	private String name;
	private String packageName;
	private String type;
	private String description;
	private boolean isAbstract;

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public AnnotatedElement() {
		this.type = UNKNOWN;
	}

	public AnnotatedElement(String packageName, String name) {
		this();
		setPackageName(packageName);
		setName(name);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getFullyQualifiedName() {
		String result = this.packageName;

		if (!"".equals(result)) {
			result = result + ".";
		}

		return result + getName();
	}

	public class AnnotatedElementLabelProvider extends LabelProvider {

		public AnnotatedElementLabelProvider() {
			super();
		}

		public String getName(Object element) {
			if (element == null)
				return "";

			AnnotatedElement annotatedElement = (AnnotatedElement) element;
			return annotatedElement.getName();
		}

		public Object getProperty(Object Element, String propertyName) {
			// TODO Auto-generated method stub
			return null;
		}

		public void setProperty(Object Element, String propertyName,
				Object property) {
			// TODO Auto-generated method stub

		}

		@Override
		public Image getImage(Object element) {
			AnnotatedElement annotatedElement = (AnnotatedElement) element;

			if (DATATYPE.equals(annotatedElement.getType()))
				return Images.get(Images.DATATYPE_ICON);
			if (ENTITY.equals(annotatedElement.getType()))
				return Images.get(Images.ENTITY_ICON);
			if (ENUMERATION.equals(annotatedElement.getType()))
				return Images.get(Images.ENUM_ICON);

			return null;
		}

		public String getPackage(Object element) {
			if (element == null)
				return "";

			AnnotatedElement annotatedElement = (AnnotatedElement) element;
			return annotatedElement.getPackageName();
		}

		@Override
		public String getText(Object element) {
			if (element == null)
				return "unknown";

			AnnotatedElement annotatedElement = (AnnotatedElement) element;
			return annotatedElement.getFullyQualifiedName();
		}
	}

	/**
	 * Merges the AnnotatedElement properties with the given properties.
	 * 
	 * @param prop
	 * @return
	 */
	public Properties mergeProperties(Properties prop) {
		Properties result = new Properties();

		result.setProperty(NewArtifactWizardPage.ARTIFACT_DESCRIPTION,
				(getDescription() == null) ? " " : getDescription());
		result.setProperty(NewArtifactWizardPage.ARTIFACT_NAME, getName());
		result
				.setProperty(NewArtifactWizardPage.PACKAGE_NAME,
						getPackageName());
		result
				.setProperty(NewArtifactWizardPage.INTERFACE_PACKAGE,
						"javax.oss");
		result.setProperty("isAbstract", Boolean.toString(isAbstract()));
		result.put(NewArtifactWizardPage.CONSTANT_LIST, getLabelRefs());
		result.put(NewArtifactWizardPage.ATTRIBUTE_LIST, getAttributeRefs());
		result.put(NewArtifactWizardPage.METHOD_LIST, getMethodDetailsModels());

		if (getExtendedArtifact() == null) {
			result.setProperty(NewArtifactWizardPage.EXTENDED_ARTIFACT,
					"java.lang.Object");
		} else {
			result.setProperty(NewArtifactWizardPage.EXTENDED_ARTIFACT,
					getExtendedArtifact());
		}

		for (Iterator iter = prop.keySet().iterator(); iter.hasNext();) {
			Object key = iter.next();
			result.put(key, prop.get(key));
		}

		return result;
	}

	public List getLabelRefs() {
		return this.labelRefs;
	}

	public void addLabelRef(LabelRef labelRef) {
		labelRefs.add(labelRef);
	}

	public List getAttributeRefs() {
		return this.attributeRefs;
	}

	public void addAttributeRef(ArtifactAttributeModel ref) {
		this.attributeRefs.add(ref);
	}

	public void addMethodModel(ArtifactMethodModel model) {
		this.methodModels.add(model);
	}

	public List<ArtifactMethodModel> getMethodDetailsModels() {
		return this.methodModels;
	}

	public String getExtendedArtifact() {
		return this.extendedArtifact;
	}

	public void setExtendedArtifact(String artifact) {
		this.extendedArtifact = artifact;
	}

	public static AnnotatedElementLabelProvider getLabelProvider() {
		AnnotatedElement elem = new AnnotatedElement();
		return elem.new AnnotatedElementLabelProvider();
	}

	@Override
	public boolean equals(Object arg0) {

		if (!(arg0 instanceof AnnotatedElement))
			return false;

		AnnotatedElement other = (AnnotatedElement) arg0;
		if (other.getPackageName() == null || other.getName() == null)
			return false;

		return other.getPackageName().equals(getPackageName())
				&& other.getName().equals(getName());
	}
}
