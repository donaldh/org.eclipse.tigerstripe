/**
 * <copyright>
 * </copyright>
 *
 * $Id: AnnotationContextImpl.java,v 1.1 2008/01/16 18:14:18 edillon Exp $
 */
package org.eclipse.tigerstripe.annotations.internal.context.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.annotations.internal.context.Annotation;
import org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext;
import org.eclipse.tigerstripe.annotations.internal.context.ContextPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annotation Context</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationContextImpl#getAnnotations <em>Annotations</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationContextImpl#getNamespaceID <em>Namespace ID</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AnnotationContextImpl extends EObjectImpl implements AnnotationContext {
	/**
	 * The cached value of the '{@link #getAnnotations() <em>Annotations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotations()
	 * @generated
	 * @ordered
	 */
	protected EList<Annotation> annotations;

	/**
	 * The default value of the '{@link #getNamespaceID() <em>Namespace ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespaceID()
	 * @generated
	 * @ordered
	 */
	protected static final String NAMESPACE_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNamespaceID() <em>Namespace ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespaceID()
	 * @generated
	 * @ordered
	 */
	protected String namespaceID = NAMESPACE_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnnotationContextImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContextPackage.Literals.ANNOTATION_CONTEXT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Annotation> getAnnotations() {
		if (annotations == null) {
			annotations = new EObjectContainmentEList<Annotation>(Annotation.class, this, ContextPackage.ANNOTATION_CONTEXT__ANNOTATIONS);
		}
		return annotations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNamespaceID() {
		return namespaceID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNamespaceID(String newNamespaceID) {
		String oldNamespaceID = namespaceID;
		namespaceID = newNamespaceID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContextPackage.ANNOTATION_CONTEXT__NAMESPACE_ID, oldNamespaceID, namespaceID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ContextPackage.ANNOTATION_CONTEXT__ANNOTATIONS:
				return ((InternalEList<?>)getAnnotations()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContextPackage.ANNOTATION_CONTEXT__ANNOTATIONS:
				return getAnnotations();
			case ContextPackage.ANNOTATION_CONTEXT__NAMESPACE_ID:
				return getNamespaceID();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ContextPackage.ANNOTATION_CONTEXT__ANNOTATIONS:
				getAnnotations().clear();
				getAnnotations().addAll((Collection<? extends Annotation>)newValue);
				return;
			case ContextPackage.ANNOTATION_CONTEXT__NAMESPACE_ID:
				setNamespaceID((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ContextPackage.ANNOTATION_CONTEXT__ANNOTATIONS:
				getAnnotations().clear();
				return;
			case ContextPackage.ANNOTATION_CONTEXT__NAMESPACE_ID:
				setNamespaceID(NAMESPACE_ID_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ContextPackage.ANNOTATION_CONTEXT__ANNOTATIONS:
				return annotations != null && !annotations.isEmpty();
			case ContextPackage.ANNOTATION_CONTEXT__NAMESPACE_ID:
				return NAMESPACE_ID_EDEFAULT == null ? namespaceID != null : !NAMESPACE_ID_EDEFAULT.equals(namespaceID);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (namespaceID: ");
		result.append(namespaceID);
		result.append(')');
		return result.toString();
	}

} //AnnotationContextImpl
