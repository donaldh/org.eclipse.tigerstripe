/**
 * <copyright>
 * </copyright>
 *
 * $Id: AnnotationNodeImpl.java,v 1.2 2008/06/19 11:23:41 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annotation Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.AnnotationNodeImpl#getAnnotationId <em>Annotation Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AnnotationNodeImpl extends NodeImpl implements AnnotationNode {
	/**
	 * The default value of the '{@link #getAnnotationId() <em>Annotation Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotationId()
	 * @generated
	 * @ordered
	 */
	protected static final String ANNOTATION_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAnnotationId() <em>Annotation Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotationId()
	 * @generated
	 * @ordered
	 */
	protected String annotationId = ANNOTATION_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnnotationNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.ANNOTATION_NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAnnotationId() {
		return annotationId;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnnotationId(String newAnnotationId) {
		String oldAnnotationId = annotationId;
		annotationId = newAnnotationId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ANNOTATION_NODE__ANNOTATION_ID, oldAnnotationId, annotationId));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode#getAnnotation()
	 */
	public Annotation getAnnotation() {
		return AnnotationPlugin.getManager().getAnnotationById(getAnnotationId());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.ANNOTATION_NODE__ANNOTATION_ID:
				return getAnnotationId();
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
			case ModelPackage.ANNOTATION_NODE__ANNOTATION_ID:
				setAnnotationId((String)newValue);
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
			case ModelPackage.ANNOTATION_NODE__ANNOTATION_ID:
				setAnnotationId(ANNOTATION_ID_EDEFAULT);
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
			case ModelPackage.ANNOTATION_NODE__ANNOTATION_ID:
				return ANNOTATION_ID_EDEFAULT == null ? annotationId != null : !ANNOTATION_ID_EDEFAULT.equals(annotationId);
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
		result.append(" (annotationId: ");
		result.append(annotationId);
		result.append(')');
		return result.toString();
	}

} //AnnotationNodeImpl
