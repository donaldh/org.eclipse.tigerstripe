/**
 * <copyright>
 * </copyright>
 *
 * $Id: IAbstractArtifactImpl.java,v 1.2 2008/02/22 20:01:22 edillon Exp $
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
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IArtifactMetadata;
import org.eclipse.tigerstripe.metamodel.IField;
import org.eclipse.tigerstripe.metamodel.ILiteral;
import org.eclipse.tigerstripe.metamodel.IMethod;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

import org.eclipse.tigerstripe.metamodel.extensions.IStandardSpecifics;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;

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
 * <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl#getStandardSpecifics <em>Standard Specifics</em>}</li>
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
	 * The cached value of the '{@link #getStandardSpecifics() <em>Standard Specifics</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStandardSpecifics()
	 * @generated
	 * @ordered
	 */
	protected IStandardSpecifics standardSpecifics;

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
	public void setExtendedArtifact(IAbstractArtifact newExtendedArtifact) {
		IAbstractArtifact oldExtendedArtifact = extendedArtifact;
		extendedArtifact = newExtendedArtifact;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MetamodelPackage.IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT,
					oldExtendedArtifact, extendedArtifact));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IAbstractArtifact> getImplementedArtifacts() {
		if (implementedArtifacts == null) {
			implementedArtifacts = new EObjectResolvingEList<IAbstractArtifact>(
					IAbstractArtifact.class, this,
					MetamodelPackage.IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS);
		}
		return implementedArtifacts;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IStandardSpecifics getStandardSpecifics() {
		if (standardSpecifics != null && standardSpecifics.eIsProxy()) {
			InternalEObject oldStandardSpecifics = (InternalEObject) standardSpecifics;
			standardSpecifics = (IStandardSpecifics) eResolveProxy(oldStandardSpecifics);
			if (standardSpecifics != oldStandardSpecifics) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(
							this,
							Notification.RESOLVE,
							MetamodelPackage.IABSTRACT_ARTIFACT__STANDARD_SPECIFICS,
							oldStandardSpecifics, standardSpecifics));
			}
		}
		return standardSpecifics;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IStandardSpecifics basicGetStandardSpecifics() {
		return standardSpecifics;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStandardSpecifics(IStandardSpecifics newStandardSpecifics) {
		IStandardSpecifics oldStandardSpecifics = standardSpecifics;
		standardSpecifics = newStandardSpecifics;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MetamodelPackage.IABSTRACT_ARTIFACT__STANDARD_SPECIFICS,
					oldStandardSpecifics, standardSpecifics));
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
	public EList<IAbstractArtifact> getExtendingArtifacts() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<IAbstractArtifact> getImplementingArtifact() {
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
		case MetamodelPackage.IABSTRACT_ARTIFACT__STANDARD_SPECIFICS:
			if (resolve)
				return getStandardSpecifics();
			return basicGetStandardSpecifics();
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
		case MetamodelPackage.IABSTRACT_ARTIFACT__STANDARD_SPECIFICS:
			setStandardSpecifics((IStandardSpecifics) newValue);
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
		case MetamodelPackage.IABSTRACT_ARTIFACT__STANDARD_SPECIFICS:
			setStandardSpecifics((IStandardSpecifics) null);
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
		case MetamodelPackage.IABSTRACT_ARTIFACT__STANDARD_SPECIFICS:
			return standardSpecifics != null;
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
