/**
 * <copyright>
 * </copyright>
 *
 * $Id: IAbstractArtifactImpl.java,v 1.4 2008/05/22 18:26:28 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IField;
import org.eclipse.tigerstripe.metamodel.ILiteral;
import org.eclipse.tigerstripe.metamodel.IMethod;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>IAbstract Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl#getFields <em>Fields</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl#getMethods <em>Methods</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl#getLiterals <em>Literals</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl#isAbstract <em>Abstract</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl#getExtendedArtifact <em>Extended Artifact</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl#getImplementedArtifacts <em>Implemented Artifacts</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl#getExtendingArtifacts <em>Extending Artifacts</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl#getImplementingArtifacts <em>Implementing Artifacts</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class IAbstractArtifactImpl extends
		IQualifiedNamedComponentImpl implements IAbstractArtifact {
	/**
	 * The cached value of the '{@link #getFields() <em>Fields</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFields()
	 * @generated
	 * @ordered
	 */
	protected EList<IField> fields;

	/**
	 * The cached value of the '{@link #getMethods() <em>Methods</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMethods()
	 * @generated
	 * @ordered
	 */
	protected EList<IMethod> methods;

	/**
	 * The cached value of the '{@link #getLiterals() <em>Literals</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLiterals()
	 * @generated
	 * @ordered
	 */
	protected EList<ILiteral> literals;

	/**
	 * The default value of the '{@link #isAbstract() <em>Abstract</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ABSTRACT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAbstract() <em>Abstract</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isAbstract()
	 * @generated
	 * @ordered
	 */
	protected boolean abstract_ = ABSTRACT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getExtendedArtifact() <em>Extended Artifact</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getExtendedArtifact()
	 * @generated
	 * @ordered
	 */
	protected IAbstractArtifact extendedArtifact;

	/**
	 * The cached value of the '{@link #getImplementedArtifacts() <em>Implemented Artifacts</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getImplementedArtifacts()
	 * @generated
	 * @ordered
	 */
	protected EList<IAbstractArtifact> implementedArtifacts;

	/**
	 * The cached value of the '{@link #getExtendingArtifacts() <em>Extending Artifacts</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getExtendingArtifacts()
	 * @generated
	 * @ordered
	 */
	protected EList<IAbstractArtifact> extendingArtifacts;

	/**
	 * The cached value of the '{@link #getImplementingArtifacts() <em>Implementing Artifacts</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getImplementingArtifacts()
	 * @generated
	 * @ordered
	 */
	protected EList<IAbstractArtifact> implementingArtifacts;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IAbstractArtifactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.IABSTRACT_ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IField> getFields() {
		if (fields == null) {
			fields = new EObjectContainmentEList<IField>(IField.class, this,
					MetamodelPackage.IABSTRACT_ARTIFACT__FIELDS);
		}
		return fields;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IMethod> getMethods() {
		if (methods == null) {
			methods = new EObjectContainmentEList<IMethod>(IMethod.class, this,
					MetamodelPackage.IABSTRACT_ARTIFACT__METHODS);
		}
		return methods;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<ILiteral> getLiterals() {
		if (literals == null) {
			literals = new EObjectContainmentEList<ILiteral>(ILiteral.class,
					this, MetamodelPackage.IABSTRACT_ARTIFACT__LITERALS);
		}
		return literals;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isAbstract() {
		return abstract_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAbstract(boolean newAbstract) {
		boolean oldAbstract = abstract_;
		abstract_ = newAbstract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MetamodelPackage.IABSTRACT_ARTIFACT__ABSTRACT, oldAbstract,
					abstract_));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IAbstractArtifact getExtendedArtifact() {
		if (extendedArtifact != null && extendedArtifact.eIsProxy()) {
			InternalEObject oldExtendedArtifact = (InternalEObject) extendedArtifact;
			extendedArtifact = (IAbstractArtifact) eResolveProxy(oldExtendedArtifact);
			if (extendedArtifact != oldExtendedArtifact) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(
							this,
							Notification.RESOLVE,
							MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT,
							oldExtendedArtifact, extendedArtifact));
			}
		}
		return extendedArtifact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IAbstractArtifact basicGetExtendedArtifact() {
		return extendedArtifact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetExtendedArtifact(
			IAbstractArtifact newExtendedArtifact, NotificationChain msgs) {
		IAbstractArtifact oldExtendedArtifact = extendedArtifact;
		extendedArtifact = newExtendedArtifact;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this,
					Notification.SET,
					MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT,
					oldExtendedArtifact, newExtendedArtifact);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setExtendedArtifact(IAbstractArtifact newExtendedArtifact) {
		if (newExtendedArtifact != extendedArtifact) {
			NotificationChain msgs = null;
			if (extendedArtifact != null)
				msgs = ((InternalEObject) extendedArtifact)
						.eInverseRemove(
								this,
								MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS,
								IAbstractArtifact.class, msgs);
			if (newExtendedArtifact != null)
				msgs = ((InternalEObject) newExtendedArtifact)
						.eInverseAdd(
								this,
								MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS,
								IAbstractArtifact.class, msgs);
			msgs = basicSetExtendedArtifact(newExtendedArtifact, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT,
					newExtendedArtifact, newExtendedArtifact));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IAbstractArtifact> getImplementedArtifacts() {
		if (implementedArtifacts == null) {
			implementedArtifacts = new EObjectWithInverseResolvingEList.ManyInverse<IAbstractArtifact>(
					IAbstractArtifact.class, this,
					MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS,
					MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTING_ARTIFACTS);
		}
		return implementedArtifacts;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IAbstractArtifact> getExtendingArtifacts() {
		if (extendingArtifacts == null) {
			extendingArtifacts = new EObjectWithInverseResolvingEList<IAbstractArtifact>(
					IAbstractArtifact.class, this,
					MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS,
					MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT);
		}
		return extendingArtifacts;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IAbstractArtifact> getImplementingArtifacts() {
		if (implementingArtifacts == null) {
			implementingArtifacts = new EObjectWithInverseResolvingEList.ManyInverse<IAbstractArtifact>(
					IAbstractArtifact.class,
					this,
					MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTING_ARTIFACTS,
					MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS);
		}
		return implementingArtifacts;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IAbstractArtifact> getAncestors() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IField> getInheritedFields() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IMethod> getInheritedMethods() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IAbstractArtifact> getReferencedArtifacts() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean hasExtends() {
		return getExtendedArtifact() != null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT:
			if (extendedArtifact != null)
				msgs = ((InternalEObject) extendedArtifact)
						.eInverseRemove(
								this,
								MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS,
								IAbstractArtifact.class, msgs);
			return basicSetExtendedArtifact((IAbstractArtifact) otherEnd, msgs);
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getImplementedArtifacts())
					.basicAdd(otherEnd, msgs);
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getExtendingArtifacts())
					.basicAdd(otherEnd, msgs);
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTING_ARTIFACTS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getImplementingArtifacts())
					.basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
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
		case MetamodelPackage.IABSTRACT_ARTIFACT__FIELDS:
			return ((InternalEList<?>) getFields()).basicRemove(otherEnd, msgs);
		case MetamodelPackage.IABSTRACT_ARTIFACT__METHODS:
			return ((InternalEList<?>) getMethods())
					.basicRemove(otherEnd, msgs);
		case MetamodelPackage.IABSTRACT_ARTIFACT__LITERALS:
			return ((InternalEList<?>) getLiterals()).basicRemove(otherEnd,
					msgs);
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT:
			return basicSetExtendedArtifact(null, msgs);
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS:
			return ((InternalEList<?>) getImplementedArtifacts()).basicRemove(
					otherEnd, msgs);
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS:
			return ((InternalEList<?>) getExtendingArtifacts()).basicRemove(
					otherEnd, msgs);
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTING_ARTIFACTS:
			return ((InternalEList<?>) getImplementingArtifacts()).basicRemove(
					otherEnd, msgs);
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
		case MetamodelPackage.IABSTRACT_ARTIFACT__FIELDS:
			return getFields();
		case MetamodelPackage.IABSTRACT_ARTIFACT__METHODS:
			return getMethods();
		case MetamodelPackage.IABSTRACT_ARTIFACT__LITERALS:
			return getLiterals();
		case MetamodelPackage.IABSTRACT_ARTIFACT__ABSTRACT:
			return isAbstract() ? Boolean.TRUE : Boolean.FALSE;
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT:
			if (resolve)
				return getExtendedArtifact();
			return basicGetExtendedArtifact();
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS:
			return getImplementedArtifacts();
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS:
			return getExtendingArtifacts();
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTING_ARTIFACTS:
			return getImplementingArtifacts();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case MetamodelPackage.IABSTRACT_ARTIFACT__FIELDS:
			getFields().clear();
			getFields().addAll((Collection<? extends IField>) newValue);
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__METHODS:
			getMethods().clear();
			getMethods().addAll((Collection<? extends IMethod>) newValue);
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__LITERALS:
			getLiterals().clear();
			getLiterals().addAll((Collection<? extends ILiteral>) newValue);
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__ABSTRACT:
			setAbstract(((Boolean) newValue).booleanValue());
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT:
			setExtendedArtifact((IAbstractArtifact) newValue);
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS:
			getImplementedArtifacts().clear();
			getImplementedArtifacts().addAll(
					(Collection<? extends IAbstractArtifact>) newValue);
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS:
			getExtendingArtifacts().clear();
			getExtendingArtifacts().addAll(
					(Collection<? extends IAbstractArtifact>) newValue);
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTING_ARTIFACTS:
			getImplementingArtifacts().clear();
			getImplementingArtifacts().addAll(
					(Collection<? extends IAbstractArtifact>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case MetamodelPackage.IABSTRACT_ARTIFACT__FIELDS:
			getFields().clear();
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__METHODS:
			getMethods().clear();
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__LITERALS:
			getLiterals().clear();
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__ABSTRACT:
			setAbstract(ABSTRACT_EDEFAULT);
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT:
			setExtendedArtifact((IAbstractArtifact) null);
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS:
			getImplementedArtifacts().clear();
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS:
			getExtendingArtifacts().clear();
			return;
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTING_ARTIFACTS:
			getImplementingArtifacts().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case MetamodelPackage.IABSTRACT_ARTIFACT__FIELDS:
			return fields != null && !fields.isEmpty();
		case MetamodelPackage.IABSTRACT_ARTIFACT__METHODS:
			return methods != null && !methods.isEmpty();
		case MetamodelPackage.IABSTRACT_ARTIFACT__LITERALS:
			return literals != null && !literals.isEmpty();
		case MetamodelPackage.IABSTRACT_ARTIFACT__ABSTRACT:
			return abstract_ != ABSTRACT_EDEFAULT;
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT:
			return extendedArtifact != null;
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS:
			return implementedArtifacts != null
					&& !implementedArtifacts.isEmpty();
		case MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS:
			return extendingArtifacts != null && !extendingArtifacts.isEmpty();
		case MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTING_ARTIFACTS:
			return implementingArtifacts != null
					&& !implementingArtifacts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (abstract: ");
		result.append(abstract_);
		result.append(')');
		return result.toString();
	}

} // IAbstractArtifactImpl
