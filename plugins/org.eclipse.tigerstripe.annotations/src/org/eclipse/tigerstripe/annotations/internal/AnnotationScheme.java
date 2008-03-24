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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.tigerstripe.annotations.Activator;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.ISelector;

public class AnnotationScheme implements IAnnotationScheme {

	private String namespaceID = null;
	private String namespaceUserLabel = null;
	private ISelector selector = ISelector.DEFAULT;
	private Set<IAnnotationForm> forms = new HashSet<IAnnotationForm>();

	public AnnotationScheme(IExtension extension)
			throws AnnotationCoreException {
		parse(extension);
	}

	public String getNamespaceID() {
		return this.namespaceID;
	}

	public String getNamespaceUserLabel() {
		return this.namespaceUserLabel;
	}

	public IAnnotationForm[] getDefinedForms() {
		return forms.toArray(new IAnnotationForm[forms.size()]);
	}

	public IAnnotationForm selectForm(String URI) {

		if (URI == null)
			return null;

		// The priority is given to forms with defined selectors.
		IAnnotationForm foundDefined = null;
		IAnnotationForm foundDefault = null;
		for (IAnnotationForm form : forms) {
			if (form.getSelector().select(URI)) {
				if (form == ISelector.DEFAULT) {
					foundDefault = form;
				} else {
					foundDefined = form;
					break;
				}
			}
		}

		if (foundDefined != null)
			return foundDefined;
		else if (foundDefault != null)
			return foundDefault;
		else
			return null;
	}

	public ISelector getSelector() {
		return selector;
	}

	private void parse(IExtension extension) throws AnnotationCoreException {
		IConfigurationElement[] children = extension.getConfigurationElements();

		// Extract the namespace details first
		for (IConfigurationElement child : children) {
			if ("scheme".equals(child.getName())) {
				namespaceID = child.getAttribute("ID");
				namespaceUserLabel = child.getAttribute("userLabel");
				if (namespaceUserLabel == null)
					namespaceUserLabel = namespaceID; // by default

				// Extract Selector
				try {
					Object obj = child.createExecutableExtension("selector");
					if (obj instanceof ISelector) {
						selector = (ISelector) obj;
						selector.setContext(this);
					}
				} catch (CoreException e) {
					// this means no definition of selector... ignore.
				}

				IConfigurationElement[] formElements = child
						.getChildren("annotationForm");
				for (IConfigurationElement formElement : formElements) {
					AnnotationForm form = new AnnotationForm();
					try {
						form.parse(formElement);
						form.setScheme(this);
						forms.add(form);
					} catch (AnnotationCoreException e) {
						Activator.log(e);
					}
				}
			}
		}

		if (isValid().length != 0) {
			throw new AnnotationCoreException();
		}
	}

	/**
	 * Returns an empty array if the scheme is valid. If not it returns an array
	 * of reasons.
	 * 
	 * @return
	 */
	protected String[] isValid() {
		List<String> result = new ArrayList<String>();

		if (namespaceID == null || namespaceID.length() == 0) {
			result.add("Scheme has no namespaceID.");
		}

		return result.toArray(new String[result.size()]);
	}

	public boolean equals(Object other) {
		if (other instanceof IAnnotationScheme) {
			IAnnotationScheme otherScheme = (IAnnotationScheme) other;
			return getNamespaceID().equals(otherScheme.getNamespaceID());
		}
		return false;
	}

	public IAnnotationSpecification findAnnotationSpecification(
			String annotationSpecificationID) throws AnnotationCoreException {
		for (IAnnotationForm form : getDefinedForms()) {
			for (IAnnotationSpecification spec : form.getSpecifications()) {
				if (spec.getID().equals(annotationSpecificationID)) {
					return spec;
				}
			}
		}
		throw new AnnotationCoreException(
				"Undefined annotation specification: "
						+ annotationSpecificationID);
	}
}
