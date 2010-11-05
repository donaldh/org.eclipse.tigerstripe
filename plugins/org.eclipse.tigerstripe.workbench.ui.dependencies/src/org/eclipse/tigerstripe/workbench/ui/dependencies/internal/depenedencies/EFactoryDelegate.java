/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

public abstract class EFactoryDelegate implements EFactory {

	public abstract EFactory getDelegate();

	public EList<Adapter> eAdapters() {
		return getDelegate().eAdapters();
	}

	public boolean eDeliver() {
		return getDelegate().eDeliver();
	}

	public EList<EAnnotation> getEAnnotations() {
		return getDelegate().getEAnnotations();
	}

	public void eSetDeliver(boolean deliver) {
		getDelegate().eSetDeliver(deliver);
	}

	public EPackage getEPackage() {
		return getDelegate().getEPackage();
	}

	public void eNotify(Notification notification) {
		getDelegate().eNotify(notification);
	}

	public EAnnotation getEAnnotation(String source) {
		return getDelegate().getEAnnotation(source);
	}

	public void setEPackage(EPackage value) {
		getDelegate().setEPackage(value);
	}

	public EObject create(EClass eClass) {
		return getDelegate().create(eClass);
	}

	public Object createFromString(EDataType eDataType, String literalValue) {
		return getDelegate().createFromString(eDataType, literalValue);
	}

	public String convertToString(EDataType eDataType, Object instanceValue) {
		return getDelegate().convertToString(eDataType, instanceValue);
	}

	public EClass eClass() {
		return getDelegate().eClass();
	}

	public Resource eResource() {
		return getDelegate().eResource();
	}

	public EObject eContainer() {
		return getDelegate().eContainer();
	}

	public EStructuralFeature eContainingFeature() {
		return getDelegate().eContainingFeature();
	}

	public EReference eContainmentFeature() {
		return getDelegate().eContainmentFeature();
	}

	public EList<EObject> eContents() {
		return getDelegate().eContents();
	}

	public TreeIterator<EObject> eAllContents() {
		return getDelegate().eAllContents();
	}

	public boolean eIsProxy() {
		return getDelegate().eIsProxy();
	}

	public EList<EObject> eCrossReferences() {
		return getDelegate().eCrossReferences();
	}

	public Object eGet(EStructuralFeature feature) {
		return getDelegate().eGet(feature);
	}

	public Object eGet(EStructuralFeature feature, boolean resolve) {
		return getDelegate().eGet(feature, resolve);
	}

	public void eSet(EStructuralFeature feature, Object newValue) {
		getDelegate().eSet(feature, newValue);
	}

	public boolean eIsSet(EStructuralFeature feature) {
		return getDelegate().eIsSet(feature);
	}

	public void eUnset(EStructuralFeature feature) {
		getDelegate().eUnset(feature);
	}

	public Object eInvoke(EOperation operation, EList<?> arguments)
			throws InvocationTargetException {
		return getDelegate().eInvoke(operation, arguments);
	}
}
