/**
 * <copyright>
 * </copyright>
 *
 * $Id: MetaAnnotationNodeImpl.java,v 1.1 2008/06/24 09:40:13 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.gmf.runtime.notation.impl.NodeImpl;

import org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaAnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ModelPackage;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewTypesStatus;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Meta Annotation Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaAnnotationNodeImpl#getViewsTypeStatuses <em>Views Type Statuses</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MetaAnnotationNodeImpl extends NodeImpl implements MetaAnnotationNode {
	/**
	 * The cached value of the '{@link #getViewsTypeStatuses() <em>Views Type Statuses</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViewsTypeStatuses()
	 * @generated
	 * @ordered
	 */
	protected EList<ViewTypesStatus> viewsTypeStatuses;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MetaAnnotationNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.META_ANNOTATION_NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ViewTypesStatus> getViewsTypeStatuses() {
		if (viewsTypeStatuses == null) {
			viewsTypeStatuses = new EObjectResolvingEList<ViewTypesStatus>(ViewTypesStatus.class, this, ModelPackage.META_ANNOTATION_NODE__VIEWS_TYPE_STATUSES);
		}
		return viewsTypeStatuses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.META_ANNOTATION_NODE__VIEWS_TYPE_STATUSES:
				return getViewsTypeStatuses();
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
			case ModelPackage.META_ANNOTATION_NODE__VIEWS_TYPE_STATUSES:
				getViewsTypeStatuses().clear();
				getViewsTypeStatuses().addAll((Collection<? extends ViewTypesStatus>)newValue);
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
			case ModelPackage.META_ANNOTATION_NODE__VIEWS_TYPE_STATUSES:
				getViewsTypeStatuses().clear();
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
			case ModelPackage.META_ANNOTATION_NODE__VIEWS_TYPE_STATUSES:
				return viewsTypeStatuses != null && !viewsTypeStatuses.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MetaAnnotationNodeImpl
