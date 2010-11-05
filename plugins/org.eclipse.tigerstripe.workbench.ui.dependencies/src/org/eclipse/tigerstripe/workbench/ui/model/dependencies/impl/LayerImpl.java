/**
 * <copyright>
 * </copyright>
 *
 * $Id: LayerImpl.java,v 1.4 2010/11/18 17:00:38 ystrot Exp $
 */
package org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Layer</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.LayerImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.LayerImpl#getShapes <em>Shapes</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.LayerImpl#isWasLayouting <em>Was Layouting</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LayerImpl extends EObjectImpl implements Layer {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getShapes() <em>Shapes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShapes()
	 * @generated
	 * @ordered
	 */
	protected EList<Shape> shapes;

	/**
	 * The default value of the '{@link #isWasLayouting() <em>Was Layouting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWasLayouting()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WAS_LAYOUTING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWasLayouting() <em>Was Layouting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWasLayouting()
	 * @generated
	 * @ordered
	 */
	protected boolean wasLayouting = WAS_LAYOUTING_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LayerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DependenciesPackage.Literals.LAYER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.LAYER__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Shape> getShapes() {
		if (shapes == null) {
			shapes = new EObjectContainmentWithInverseEList<Shape>(Shape.class, this, DependenciesPackage.LAYER__SHAPES, DependenciesPackage.SHAPE__PARENT_LAYER);
		}
		return shapes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isWasLayouting() {
		return wasLayouting;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWasLayouting(boolean newWasLayouting) {
		boolean oldWasLayouting = wasLayouting;
		wasLayouting = newWasLayouting;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.LAYER__WAS_LAYOUTING, oldWasLayouting, wasLayouting));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DependenciesPackage.LAYER__SHAPES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getShapes()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DependenciesPackage.LAYER__SHAPES:
				return ((InternalEList<?>)getShapes()).basicRemove(otherEnd, msgs);
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
			case DependenciesPackage.LAYER__ID:
				return getId();
			case DependenciesPackage.LAYER__SHAPES:
				return getShapes();
			case DependenciesPackage.LAYER__WAS_LAYOUTING:
				return isWasLayouting();
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
			case DependenciesPackage.LAYER__ID:
				setId((String)newValue);
				return;
			case DependenciesPackage.LAYER__SHAPES:
				getShapes().clear();
				getShapes().addAll((Collection<? extends Shape>)newValue);
				return;
			case DependenciesPackage.LAYER__WAS_LAYOUTING:
				setWasLayouting((Boolean)newValue);
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
			case DependenciesPackage.LAYER__ID:
				setId(ID_EDEFAULT);
				return;
			case DependenciesPackage.LAYER__SHAPES:
				getShapes().clear();
				return;
			case DependenciesPackage.LAYER__WAS_LAYOUTING:
				setWasLayouting(WAS_LAYOUTING_EDEFAULT);
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
			case DependenciesPackage.LAYER__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case DependenciesPackage.LAYER__SHAPES:
				return shapes != null && !shapes.isEmpty();
			case DependenciesPackage.LAYER__WAS_LAYOUTING:
				return wasLayouting != WAS_LAYOUTING_EDEFAULT;
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
		result.append(" (id: ");
		result.append(id);
		result.append(", wasLayouting: ");
		result.append(wasLayouting);
		result.append(')');
		return result.toString();
	}

} //LayerImpl
