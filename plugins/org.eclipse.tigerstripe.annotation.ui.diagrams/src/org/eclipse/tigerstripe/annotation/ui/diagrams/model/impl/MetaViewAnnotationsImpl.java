/**
 * <copyright>
 * </copyright>
 *
 * $Id: MetaViewAnnotationsImpl.java,v 1.2 2008/06/27 12:12:11 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.gmf.runtime.notation.View;

import org.eclipse.gmf.runtime.notation.impl.NodeImpl;

import org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Meta View Annotations</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaViewAnnotationsImpl#getView <em>View</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaViewAnnotationsImpl#getTypes <em>Types</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaViewAnnotationsImpl#getExclusionAnnotations <em>Exclusion Annotations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MetaViewAnnotationsImpl extends NodeImpl implements MetaViewAnnotations {
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
	 * The cached value of the '{@link #getTypes() <em>Types</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<String> types;

	/**
	 * The cached value of the '{@link #getExclusionAnnotations() <em>Exclusion Annotations</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExclusionAnnotations()
	 * @generated
	 * @ordered
	 */
	protected EList<String> exclusionAnnotations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MetaViewAnnotationsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.META_VIEW_ANNOTATIONS;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.META_VIEW_ANNOTATIONS__VIEW, oldView, view));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.META_VIEW_ANNOTATIONS__VIEW, oldView, view));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getTypes() {
		if (types == null) {
			types = new EDataTypeUniqueEList<String>(String.class, this, ModelPackage.META_VIEW_ANNOTATIONS__TYPES);
		}
		return types;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getExclusionAnnotations() {
		if (exclusionAnnotations == null) {
			exclusionAnnotations = new EDataTypeUniqueEList<String>(String.class, this, ModelPackage.META_VIEW_ANNOTATIONS__EXCLUSION_ANNOTATIONS);
		}
		return exclusionAnnotations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.META_VIEW_ANNOTATIONS__VIEW:
				if (resolve) return getView();
				return basicGetView();
			case ModelPackage.META_VIEW_ANNOTATIONS__TYPES:
				return getTypes();
			case ModelPackage.META_VIEW_ANNOTATIONS__EXCLUSION_ANNOTATIONS:
				return getExclusionAnnotations();
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
			case ModelPackage.META_VIEW_ANNOTATIONS__VIEW:
				setView((View)newValue);
				return;
			case ModelPackage.META_VIEW_ANNOTATIONS__TYPES:
				getTypes().clear();
				getTypes().addAll((Collection<? extends String>)newValue);
				return;
			case ModelPackage.META_VIEW_ANNOTATIONS__EXCLUSION_ANNOTATIONS:
				getExclusionAnnotations().clear();
				getExclusionAnnotations().addAll((Collection<? extends String>)newValue);
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
			case ModelPackage.META_VIEW_ANNOTATIONS__VIEW:
				setView((View)null);
				return;
			case ModelPackage.META_VIEW_ANNOTATIONS__TYPES:
				getTypes().clear();
				return;
			case ModelPackage.META_VIEW_ANNOTATIONS__EXCLUSION_ANNOTATIONS:
				getExclusionAnnotations().clear();
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
			case ModelPackage.META_VIEW_ANNOTATIONS__VIEW:
				return view != null;
			case ModelPackage.META_VIEW_ANNOTATIONS__TYPES:
				return types != null && !types.isEmpty();
			case ModelPackage.META_VIEW_ANNOTATIONS__EXCLUSION_ANNOTATIONS:
				return exclusionAnnotations != null && !exclusionAnnotations.isEmpty();
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
		result.append(" (types: ");
		result.append(types);
		result.append(", exclusionAnnotations: ");
		result.append(exclusionAnnotations);
		result.append(')');
		return result.toString();
	}

} //MetaViewAnnotationsImpl
