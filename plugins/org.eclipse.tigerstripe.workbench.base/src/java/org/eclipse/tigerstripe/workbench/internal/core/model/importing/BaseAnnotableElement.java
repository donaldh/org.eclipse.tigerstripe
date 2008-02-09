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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class BaseAnnotableElement extends BaseAnnotable implements
		AnnotableElement {

	private boolean isAbstract = false;

	private AnnotableElementPackage annotableElementPackage;

	private AnnotableElement parentAnnotableElement;

	private List<AnnotableElementAttribute> annotableElementAttributes = new ArrayList<AnnotableElementAttribute>();

	private List<AnnotableElementOperation> annotableElementOperations = new ArrayList<AnnotableElementOperation>();

	private List<AnnotableElementConstant> annotableElementConstants = new ArrayList<AnnotableElementConstant>();

	private String annotationType = AS_UNKNOWN;

	private int delta = DELTA_UNKNOWN;

	public int getMultiplicity() {
		return IType.MULTIPLICITY_SINGLE;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	public BaseAnnotableElement(String name) {
		super(name);
	}

	public boolean isMapped() {
		return !AS_UNKNOWN.equals(getAnnotationType())
				&& !(getAnnotationType() == null);
	}

	public String getAnnotationType() {
		return annotationType;
	}

	public void setAnnotationType(String annotationType) {
		this.annotationType = annotationType;
	}

	public AnnotableElement getParentAnnotableElement() {
		return parentAnnotableElement;
	}

	public void setParentAnnotableElement(
			AnnotableElement parentAnnotableElement) {
		this.parentAnnotableElement = parentAnnotableElement;
	}

	public AnnotableElementPackage getAnnotableElementPackage() {
		return annotableElementPackage;
	}

	public void setAnnotableElementPackage(
			AnnotableElementPackage elementPackage) {
		this.annotableElementPackage = elementPackage;
	}

	public String getFullyQualifiedName() {

		String prefix = "";
		if (annotableElementPackage != null
				&& !"".equals(annotableElementPackage.getFullyQualifiedName())) {
			prefix = annotableElementPackage.getFullyQualifiedName() + ".";
		}
		return prefix + getName();
	}

	public List<AnnotableElementAttribute> getAnnotableElementAttributes() {
		return annotableElementAttributes;
	}

	public void addAnnotableElementAttribute(AnnotableElementAttribute attribute) {
		annotableElementAttributes.add(attribute);
	}

	public List getAnnotableElementOperations() {
		return annotableElementOperations;
	}

	public void addAnnotableElementOperation(AnnotableElementOperation operation) {
		annotableElementOperations.add(operation);
	}

	public List getAnnotableElementConstants() {
		return annotableElementConstants;
	}

	public void addAnnotableElementConstant(AnnotableElementConstant constant) {
		annotableElementConstants.add(constant);
	}

	public IAbstractArtifact makeIArtifact(ITigerstripeModelProject project)
			throws TigerstripeException {
		IArtifactManagerSession session = project.getArtifactManagerSession();
		return ArtifactFactoryFromAnnotables.getInstance().makeIArtifact(this,
				session);
	}

	public AnnotableElementAttribute getAttributeByName(String name) {
		for (AnnotableElementAttribute attr : annotableElementAttributes) {
			if (attr.getName().equals(name))
				return attr;
		}
		return null;
	}

	public AnnotableElementConstant getConstantByName(String name) {
		for (AnnotableElementConstant cst : annotableElementConstants) {
			if (cst.getName().equals(name))
				return cst;
		}
		return null;
	}

	public AnnotableElementOperation getOperationBySignature(String signature) {
		for (AnnotableElementOperation op : annotableElementOperations) {
			if (op.getSignature().equals(signature))
				return op;
		}
		return null;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof BaseAnnotableElement) {
			BaseAnnotableElement other = (BaseAnnotableElement) arg0;
			if (getFullyQualifiedName() != null)
				return getFullyQualifiedName().equals(
						other.getFullyQualifiedName());
		}
		return false;
	}

}
