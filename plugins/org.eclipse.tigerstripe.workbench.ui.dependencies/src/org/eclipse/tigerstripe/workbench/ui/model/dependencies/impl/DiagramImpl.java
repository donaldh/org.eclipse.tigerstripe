/**
 * <copyright>
 * </copyright>
 *
 * $Id: DiagramImpl.java,v 1.3 2010/11/17 18:57:49 ystrot Exp $
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

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DiagramImpl#getCurrentLayer <em>Current Layer</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DiagramImpl#getKinds <em>Kinds</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DiagramImpl#getLayers <em>Layers</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DiagramImpl#getLayersHistory <em>Layers History</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramImpl extends EObjectImpl implements Diagram {
	/**
	 * The cached value of the '{@link #getCurrentLayer() <em>Current Layer</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentLayer()
	 * @generated
	 * @ordered
	 */
	protected Layer currentLayer;

	/**
	 * The cached value of the '{@link #getKinds() <em>Kinds</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKinds()
	 * @generated
	 * @ordered
	 */
	protected EList<Kind> kinds;

	/**
	 * The cached value of the '{@link #getLayers() <em>Layers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLayers()
	 * @generated
	 * @ordered
	 */
	protected EList<Layer> layers;

	/**
	 * The cached value of the '{@link #getLayersHistory() <em>Layers History</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLayersHistory()
	 * @generated
	 * @ordered
	 */
	protected EList<Layer> layersHistory;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiagramImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DependenciesPackage.Literals.DIAGRAM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Layer getCurrentLayer() {
		if (currentLayer != null && currentLayer.eIsProxy()) {
			InternalEObject oldCurrentLayer = (InternalEObject)currentLayer;
			currentLayer = (Layer)eResolveProxy(oldCurrentLayer);
			if (currentLayer != oldCurrentLayer) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DependenciesPackage.DIAGRAM__CURRENT_LAYER, oldCurrentLayer, currentLayer));
			}
		}
		return currentLayer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Layer basicGetCurrentLayer() {
		return currentLayer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentLayer(Layer newCurrentLayer) {
		Layer oldCurrentLayer = currentLayer;
		currentLayer = newCurrentLayer;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.DIAGRAM__CURRENT_LAYER, oldCurrentLayer, currentLayer));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Kind> getKinds() {
		if (kinds == null) {
			kinds = new EObjectContainmentEList<Kind>(Kind.class, this, DependenciesPackage.DIAGRAM__KINDS);
		}
		return kinds;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Layer> getLayers() {
		if (layers == null) {
			layers = new EObjectContainmentEList<Layer>(Layer.class, this, DependenciesPackage.DIAGRAM__LAYERS);
		}
		return layers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Layer> getLayersHistory() {
		if (layersHistory == null) {
			layersHistory = new EObjectResolvingEList<Layer>(Layer.class, this, DependenciesPackage.DIAGRAM__LAYERS_HISTORY);
		}
		return layersHistory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DependenciesPackage.DIAGRAM__KINDS:
				return ((InternalEList<?>)getKinds()).basicRemove(otherEnd, msgs);
			case DependenciesPackage.DIAGRAM__LAYERS:
				return ((InternalEList<?>)getLayers()).basicRemove(otherEnd, msgs);
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
			case DependenciesPackage.DIAGRAM__CURRENT_LAYER:
				if (resolve) return getCurrentLayer();
				return basicGetCurrentLayer();
			case DependenciesPackage.DIAGRAM__KINDS:
				return getKinds();
			case DependenciesPackage.DIAGRAM__LAYERS:
				return getLayers();
			case DependenciesPackage.DIAGRAM__LAYERS_HISTORY:
				return getLayersHistory();
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
			case DependenciesPackage.DIAGRAM__CURRENT_LAYER:
				setCurrentLayer((Layer)newValue);
				return;
			case DependenciesPackage.DIAGRAM__KINDS:
				getKinds().clear();
				getKinds().addAll((Collection<? extends Kind>)newValue);
				return;
			case DependenciesPackage.DIAGRAM__LAYERS:
				getLayers().clear();
				getLayers().addAll((Collection<? extends Layer>)newValue);
				return;
			case DependenciesPackage.DIAGRAM__LAYERS_HISTORY:
				getLayersHistory().clear();
				getLayersHistory().addAll((Collection<? extends Layer>)newValue);
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
			case DependenciesPackage.DIAGRAM__CURRENT_LAYER:
				setCurrentLayer((Layer)null);
				return;
			case DependenciesPackage.DIAGRAM__KINDS:
				getKinds().clear();
				return;
			case DependenciesPackage.DIAGRAM__LAYERS:
				getLayers().clear();
				return;
			case DependenciesPackage.DIAGRAM__LAYERS_HISTORY:
				getLayersHistory().clear();
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
			case DependenciesPackage.DIAGRAM__CURRENT_LAYER:
				return currentLayer != null;
			case DependenciesPackage.DIAGRAM__KINDS:
				return kinds != null && !kinds.isEmpty();
			case DependenciesPackage.DIAGRAM__LAYERS:
				return layers != null && !layers.isEmpty();
			case DependenciesPackage.DIAGRAM__LAYERS_HISTORY:
				return layersHistory != null && !layersHistory.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DiagramImpl
