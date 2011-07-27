/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

public class VisualeditorModelAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IModelComponent.class) {
			return getAnnotable(adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return new Class[] { IModelComponent.class };
	}

	protected Object getAnnotable(Object adaptableObject) {
		if (adaptableObject instanceof QualifiedNamedElement) {
			return getCorrespondingIArtifact((QualifiedNamedElement) adaptableObject);
		} else if (adaptableObject instanceof Attribute) {
			Attribute attribute = (Attribute) adaptableObject;
			AbstractArtifact eArtifact = (AbstractArtifact) attribute
					.eContainer();
			if (eArtifact != null) {
				IAbstractArtifact artifact = getCorrespondingIArtifact(eArtifact);
				if (artifact != null) {
					for (IField field : artifact.getFields()) {
						if (field.getName().equals(attribute.getName())) {
							return field;
						}
					}
				}
			}
		} else if (adaptableObject instanceof Method) {
			Method method = (Method) adaptableObject;
			AbstractArtifact eArtifact = (AbstractArtifact) method.eContainer();
			if (eArtifact != null) {
				IAbstractArtifact artifact = getCorrespondingIArtifact(eArtifact);
				if (artifact != null) {
					for (IMethod m : artifact.getMethods()) {
						if (m.getName().equals(method.getName())) {
							return m;
						}
					}
				}
			}
		} else if (adaptableObject instanceof Literal) {
			Literal literal = (Literal) adaptableObject;
			AbstractArtifact eArtifact = (AbstractArtifact) literal
					.eContainer();
			if (eArtifact != null) {
				IAbstractArtifact artifact = getCorrespondingIArtifact(eArtifact);
				if (artifact != null) {
					for (ILiteral l : artifact.getLiterals()) {
						if (l.getName().equals(literal.getName())) {
							return l;
						}
					}
				}
			}
		}
		return null;
	}

	protected IAbstractArtifact getCorrespondingIArtifact(
			QualifiedNamedElement element) {
		IAbstractArtifact result = null;
		try {
			result = element.getCorrespondingIArtifact();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return result;
	}
}
