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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Abstract Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AbstractArtifactImpl#getExtends <em>Extends</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AbstractArtifactImpl#getAttributes <em>Attributes</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AbstractArtifactImpl#getLiterals <em>Literals</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AbstractArtifactImpl#getMethods <em>Methods</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AbstractArtifactImpl#getReferences <em>References</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AbstractArtifactImpl#getImplements <em>Implements</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class AbstractArtifactImpl extends QualifiedNamedElementImpl implements
		AbstractArtifact {
	/**
	 * The cached value of the '{@link #getExtends() <em>Extends</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getExtends()
	 * @generated
	 * @ordered
	 */
	protected AbstractArtifact extends_ = null;

	/**
	 * The cached value of the '{@link #getAttributes() <em>Attributes</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList attributes = null;

	/**
	 * The cached value of the '{@link #getLiterals() <em>Literals</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLiterals()
	 * @generated
	 * @ordered
	 */
	protected EList literals = null;

	/**
	 * The cached value of the '{@link #getMethods() <em>Methods</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMethods()
	 * @generated
	 * @ordered
	 */
	protected EList methods = null;

	/**
	 * The cached value of the '{@link #getReferences() <em>References</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getReferences()
	 * @generated
	 * @ordered
	 */
	protected EList references = null;

	/**
	 * The cached value of the '{@link #getImplements() <em>Implements</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getImplements()
	 * @generated
	 * @ordered
	 */
	protected EList implements_ = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AbstractArtifactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.ABSTRACT_ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AbstractArtifact getExtends() {
		if (extends_ != null && extends_.eIsProxy()) {
			InternalEObject oldExtends = (InternalEObject) extends_;
			extends_ = (AbstractArtifact) eResolveProxy(oldExtends);
			if (extends_ != oldExtends) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							VisualeditorPackage.ABSTRACT_ARTIFACT__EXTENDS,
							oldExtends, extends_));
			}
		}
		return extends_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AbstractArtifact basicGetExtends() {
		return extends_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setExtends(AbstractArtifact newExtends) {
		AbstractArtifact oldExtends = extends_;
		extends_ = newExtends;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ABSTRACT_ARTIFACT__EXTENDS, oldExtends,
					extends_));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getAttributes() {
		if (attributes == null) {
			attributes = new EObjectContainmentEList(Attribute.class, this,
					VisualeditorPackage.ABSTRACT_ARTIFACT__ATTRIBUTES);
		}
		return attributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getLiterals() {
		if (literals == null) {
			literals = new EObjectContainmentEList(Literal.class, this,
					VisualeditorPackage.ABSTRACT_ARTIFACT__LITERALS);
		}
		return literals;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getMethods() {
		if (methods == null) {
			methods = new EObjectContainmentEList(Method.class, this,
					VisualeditorPackage.ABSTRACT_ARTIFACT__METHODS);
		}
		return methods;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getReferences() {
		if (references == null) {
			references = new EObjectContainmentEList(Reference.class, this,
					VisualeditorPackage.ABSTRACT_ARTIFACT__REFERENCES);
		}
		return references;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getImplements() {
		if (implements_ == null) {
			implements_ = new EObjectResolvingEList(AbstractArtifact.class,
					this, VisualeditorPackage.ABSTRACT_ARTIFACT__IMPLEMENTS);
		}
		return implements_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case VisualeditorPackage.ABSTRACT_ARTIFACT__ATTRIBUTES:
			return ((InternalEList) getAttributes())
					.basicRemove(otherEnd, msgs);
		case VisualeditorPackage.ABSTRACT_ARTIFACT__LITERALS:
			return ((InternalEList) getLiterals()).basicRemove(otherEnd, msgs);
		case VisualeditorPackage.ABSTRACT_ARTIFACT__METHODS:
			return ((InternalEList) getMethods()).basicRemove(otherEnd, msgs);
		case VisualeditorPackage.ABSTRACT_ARTIFACT__REFERENCES:
			return ((InternalEList) getReferences())
					.basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.ABSTRACT_ARTIFACT__EXTENDS:
			if (resolve)
				return getExtends();
			return basicGetExtends();
		case VisualeditorPackage.ABSTRACT_ARTIFACT__ATTRIBUTES:
			return getAttributes();
		case VisualeditorPackage.ABSTRACT_ARTIFACT__LITERALS:
			return getLiterals();
		case VisualeditorPackage.ABSTRACT_ARTIFACT__METHODS:
			return getMethods();
		case VisualeditorPackage.ABSTRACT_ARTIFACT__REFERENCES:
			return getReferences();
		case VisualeditorPackage.ABSTRACT_ARTIFACT__IMPLEMENTS:
			return getImplements();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		// If this is only allowed on read only, then the initial read
		// can't create anything, and the diagram will ALWAYS be wrong
		// when opened.
		//if (!isReadonly) {
			switch (featureID) {
			case VisualeditorPackage.ABSTRACT_ARTIFACT__EXTENDS:
				setExtends((AbstractArtifact) newValue);
				return;
			case VisualeditorPackage.ABSTRACT_ARTIFACT__ATTRIBUTES:
				getAttributes().clear();
				getAttributes().addAll((Collection) newValue);
				return;
			case VisualeditorPackage.ABSTRACT_ARTIFACT__LITERALS:
				getLiterals().clear();
				getLiterals().addAll((Collection) newValue);
				return;
			case VisualeditorPackage.ABSTRACT_ARTIFACT__METHODS:
				getMethods().clear();
				getMethods().addAll((Collection) newValue);
				return;
			case VisualeditorPackage.ABSTRACT_ARTIFACT__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection) newValue);
				return;
			case VisualeditorPackage.ABSTRACT_ARTIFACT__IMPLEMENTS:
				getImplements().clear();
				getImplements().addAll((Collection) newValue);
				return;
			}
			super.eSet(featureID, newValue);
		//}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void eUnset(int featureID) {
		// If this is only allowed on read only, then the initial read
		// can't create anything, and the diagram will ALWAYS be wrong
		// when opened.
		//if (!isReadonly) {
			switch (featureID) {
			case VisualeditorPackage.ABSTRACT_ARTIFACT__EXTENDS:
				setExtends((AbstractArtifact) null);
				return;
			case VisualeditorPackage.ABSTRACT_ARTIFACT__ATTRIBUTES:
				getAttributes().clear();
				return;
			case VisualeditorPackage.ABSTRACT_ARTIFACT__LITERALS:
				getLiterals().clear();
				return;
			case VisualeditorPackage.ABSTRACT_ARTIFACT__METHODS:
				getMethods().clear();
				return;
			case VisualeditorPackage.ABSTRACT_ARTIFACT__REFERENCES:
				getReferences().clear();
				return;
			case VisualeditorPackage.ABSTRACT_ARTIFACT__IMPLEMENTS:
				getImplements().clear();
				return;
			}
			super.eUnset(featureID);
		//}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case VisualeditorPackage.ABSTRACT_ARTIFACT__EXTENDS:
			return extends_ != null;
		case VisualeditorPackage.ABSTRACT_ARTIFACT__ATTRIBUTES:
			return attributes != null && !attributes.isEmpty();
		case VisualeditorPackage.ABSTRACT_ARTIFACT__LITERALS:
			return literals != null && !literals.isEmpty();
		case VisualeditorPackage.ABSTRACT_ARTIFACT__METHODS:
			return methods != null && !methods.isEmpty();
		case VisualeditorPackage.ABSTRACT_ARTIFACT__REFERENCES:
			return references != null && !references.isEmpty();
		case VisualeditorPackage.ABSTRACT_ARTIFACT__IMPLEMENTS:
			return implements_ != null && !implements_.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // AbstractArtifactImpl
