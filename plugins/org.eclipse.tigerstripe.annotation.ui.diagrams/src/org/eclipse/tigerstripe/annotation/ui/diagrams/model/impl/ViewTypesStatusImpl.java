/**
 * <copyright>
 * </copyright>
 *
 * $Id: ViewTypesStatusImpl.java,v 1.1 2008/06/24 09:40:14 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.gmf.runtime.notation.View;

import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ModelPackage;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.TypeStatus;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewTypesStatus;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>View Types Status</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ViewTypesStatusImpl#getView <em>View</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ViewTypesStatusImpl#getStatuses <em>Statuses</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ViewTypesStatusImpl extends EObjectImpl implements ViewTypesStatus {
	/**
	 * The cached value of the '{@link #getView() <em>View</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getView()
	 * @generated
	 * @ordered
	 */
	protected View view;

	/**
	 * The cached value of the '{@link #getStatuses() <em>Statuses</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatuses()
	 * @generated
	 * @ordered
	 */
	protected EList<TypeStatus> statuses;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ViewTypesStatusImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.VIEW_TYPES_STATUS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public View getView() {
		if (view != null && view.eIsProxy()) {
			InternalEObject oldView = (InternalEObject)view;
			view = (View)eResolveProxy(oldView);
			if (view != oldView) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.VIEW_TYPES_STATUS__VIEW, oldView, view));
			}
		}
		return view;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public View basicGetView() {
		return view;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setView(View newView) {
		View oldView = view;
		view = newView;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.VIEW_TYPES_STATUS__VIEW, oldView, view));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TypeStatus> getStatuses() {
		if (statuses == null) {
			statuses = new EObjectResolvingEList<TypeStatus>(TypeStatus.class, this, ModelPackage.VIEW_TYPES_STATUS__STATUSES);
		}
		return statuses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.VIEW_TYPES_STATUS__VIEW:
				if (resolve) return getView();
				return basicGetView();
			case ModelPackage.VIEW_TYPES_STATUS__STATUSES:
				return getStatuses();
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
			case ModelPackage.VIEW_TYPES_STATUS__VIEW:
				setView((View)newValue);
				return;
			case ModelPackage.VIEW_TYPES_STATUS__STATUSES:
				getStatuses().clear();
				getStatuses().addAll((Collection<? extends TypeStatus>)newValue);
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
			case ModelPackage.VIEW_TYPES_STATUS__VIEW:
				setView((View)null);
				return;
			case ModelPackage.VIEW_TYPES_STATUS__STATUSES:
				getStatuses().clear();
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
			case ModelPackage.VIEW_TYPES_STATUS__VIEW:
				return view != null;
			case ModelPackage.VIEW_TYPES_STATUS__STATUSES:
				return statuses != null && !statuses.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ViewTypesStatusImpl
