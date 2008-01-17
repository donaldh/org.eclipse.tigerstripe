/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    erdillon (Cisco Systems, Inc.) - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.internal;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.internal.context.Annotation;
import org.eclipse.tigerstripe.annotations.internal.context.ContextFactory;

/**
 * This is the top level class for an annotation specification.
 * 
 * @author erdillon
 * 
 */
public abstract class BaseAnnotationSpecification implements
		IAnnotationSpecification {

	private String _ID;
	private IAnnotationForm parentForm;
	private String userLabel;
	private String defaultValue;
	private int index = UNDEF_INDEX;

	private void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public String getUserLabel() {
		return this.userLabel;
	}

	public String getID() {
		return _ID;
	}

	public IAnnotationForm getParentForm() {
		return parentForm;
	}

	public int getIndex() {
		return this.index;
	}

	/**
	 * Populate this from the Element found in the Extension Point
	 * 
	 * @param element
	 */
	/* package */void populate(IConfigurationElement element,
			IAnnotationForm parentForm) {
		this.parentForm = parentForm;
		this._ID = element.getAttribute("ID");
		this.userLabel = element.getAttribute("userLabel");
		setDefaultValue(element.getAttribute("defaultValue"));
		String indexField = element.getAttribute("index");
		if (indexField != null && indexField.length() != 0)
			this.index = Integer.parseInt(indexField);
	}

	public boolean equals(Object other) {
		if (other instanceof IAnnotationSpecification) {
			IAnnotationSpecification otherSpec = (IAnnotationSpecification) other;
			return otherSpec.getID().equals(getID())
					&& otherSpec.getParentForm().equals(getParentForm());
		}
		return false;
	}

	public Annotation makeAnnotation(String URI) {
		Annotation ann = ContextFactory.eINSTANCE.createAnnotation();
		ann.setAnnotationSpecificationID(getID());
		ann.setUri(URI);
		return ann;
	}

}
