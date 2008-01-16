/**
 * <copyright>
 * </copyright>
 *
 * $Id: AnnotationImpl.java,v 1.1 2008/01/16 18:14:18 edillon Exp $
 */
package org.eclipse.tigerstripe.annotations.internal.context.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.tigerstripe.annotations.internal.context.Annotation;
import org.eclipse.tigerstripe.annotations.internal.context.ContextPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annotation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationImpl#getAnnotationSpecificationID <em>Annotation Specification ID</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationImpl#getUri <em>Uri</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AnnotationImpl extends EObjectImpl implements Annotation {
	/**
	 * The default value of the '{@link #getAnnotationSpecificationID() <em>Annotation Specification ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotationSpecificationID()
	 * @generated
	 * @ordered
	 */
	protected static final String ANNOTATION_SPECIFICATION_ID_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getAnnotationSpecificationID() <em>Annotation Specification ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotationSpecificationID()
	 * @generated
	 * @ordered
	 */
	protected String annotationSpecificationID = ANNOTATION_SPECIFICATION_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final Object VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected Object value = VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getUri() <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUri()
	 * @generated
	 * @ordered
	 */
	protected static final String URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUri() <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUri()
	 * @generated
	 * @ordered
	 */
	protected String uri = URI_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnnotationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContextPackage.Literals.ANNOTATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAnnotationSpecificationID() {
		return annotationSpecificationID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnnotationSpecificationID(String newAnnotationSpecificationID) {
		String oldAnnotationSpecificationID = annotationSpecificationID;
		annotationSpecificationID = newAnnotationSpecificationID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContextPackage.ANNOTATION__ANNOTATION_SPECIFICATION_ID, oldAnnotationSpecificationID, annotationSpecificationID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(Object newValue) {
		Object oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContextPackage.ANNOTATION__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUri(String newUri) {
		String oldUri = uri;
		uri = newUri;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContextPackage.ANNOTATION__URI, oldUri, uri));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContextPackage.ANNOTATION__ANNOTATION_SPECIFICATION_ID:
				return getAnnotationSpecificationID();
			case ContextPackage.ANNOTATION__VALUE:
				return getValue();
			case ContextPackage.ANNOTATION__URI:
				return getUri();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ContextPackage.ANNOTATION__ANNOTATION_SPECIFICATION_ID:
				setAnnotationSpecificationID((String)newValue);
				return;
			case ContextPackage.ANNOTATION__VALUE:
				setValue(newValue);
				return;
			case ContextPackage.ANNOTATION__URI:
				setUri((String)newValue);
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
			case ContextPackage.ANNOTATION__ANNOTATION_SPECIFICATION_ID:
				setAnnotationSpecificationID(ANNOTATION_SPECIFICATION_ID_EDEFAULT);
				return;
			case ContextPackage.ANNOTATION__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case ContextPackage.ANNOTATION__URI:
				setUri(URI_EDEFAULT);
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
			case ContextPackage.ANNOTATION__ANNOTATION_SPECIFICATION_ID:
				return ANNOTATION_SPECIFICATION_ID_EDEFAULT == null ? annotationSpecificationID != null : !ANNOTATION_SPECIFICATION_ID_EDEFAULT.equals(annotationSpecificationID);
			case ContextPackage.ANNOTATION__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
			case ContextPackage.ANNOTATION__URI:
				return URI_EDEFAULT == null ? uri != null : !URI_EDEFAULT.equals(uri);
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
		result.append(" (annotationSpecificationID: ");
		result.append(annotationSpecificationID);
		result.append(", value: ");
		result.append(value);
		result.append(", uri: ");
		result.append(uri);
		result.append(')');
		return result.toString();
	}

} //AnnotationImpl
