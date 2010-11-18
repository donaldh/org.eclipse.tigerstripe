/**
 * <copyright>
 * </copyright>
 *
 * $Id: ShapeImpl.java,v 1.4 2010/11/18 17:00:38 ystrot Exp $
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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Dimension;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Point;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Shape</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl#getSize <em>Size</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl#getSourceConnections <em>Source Connections</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl#getTargetConnections <em>Target Connections</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl#getParentLayer <em>Parent Layer</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl#isWasLayouting <em>Was Layouting</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ShapeImpl extends EObjectImpl implements Shape {
	/**
	 * The cached value of the '{@link #getLocation() <em>Location</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected Point location;

	/**
	 * The cached value of the '{@link #getSize() <em>Size</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected Dimension size;

	/**
	 * The cached value of the '{@link #getStyle() <em>Style</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStyle()
	 * @generated
	 * @ordered
	 */
	protected ShapeStyle style;

	/**
	 * The cached value of the '{@link #getSourceConnections() <em>Source Connections</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceConnections()
	 * @generated
	 * @ordered
	 */
	protected EList<Connection> sourceConnections;

	/**
	 * The cached value of the '{@link #getTargetConnections() <em>Target Connections</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetConnections()
	 * @generated
	 * @ordered
	 */
	protected EList<Connection> targetConnections;

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
	protected ShapeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DependenciesPackage.Literals.SHAPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLocation(Point newLocation, NotificationChain msgs) {
		Point oldLocation = location;
		location = newLocation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DependenciesPackage.SHAPE__LOCATION, oldLocation, newLocation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocation(Point newLocation) {
		if (newLocation != location) {
			NotificationChain msgs = null;
			if (location != null)
				msgs = ((InternalEObject)location).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DependenciesPackage.SHAPE__LOCATION, null, msgs);
			if (newLocation != null)
				msgs = ((InternalEObject)newLocation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DependenciesPackage.SHAPE__LOCATION, null, msgs);
			msgs = basicSetLocation(newLocation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.SHAPE__LOCATION, newLocation, newLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSize(Dimension newSize, NotificationChain msgs) {
		Dimension oldSize = size;
		size = newSize;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DependenciesPackage.SHAPE__SIZE, oldSize, newSize);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSize(Dimension newSize) {
		if (newSize != size) {
			NotificationChain msgs = null;
			if (size != null)
				msgs = ((InternalEObject)size).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DependenciesPackage.SHAPE__SIZE, null, msgs);
			if (newSize != null)
				msgs = ((InternalEObject)newSize).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DependenciesPackage.SHAPE__SIZE, null, msgs);
			msgs = basicSetSize(newSize, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.SHAPE__SIZE, newSize, newSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShapeStyle getStyle() {
		return style;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStyle(ShapeStyle newStyle, NotificationChain msgs) {
		ShapeStyle oldStyle = style;
		style = newStyle;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DependenciesPackage.SHAPE__STYLE, oldStyle, newStyle);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStyle(ShapeStyle newStyle) {
		if (newStyle != style) {
			NotificationChain msgs = null;
			if (style != null)
				msgs = ((InternalEObject)style).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DependenciesPackage.SHAPE__STYLE, null, msgs);
			if (newStyle != null)
				msgs = ((InternalEObject)newStyle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DependenciesPackage.SHAPE__STYLE, null, msgs);
			msgs = basicSetStyle(newStyle, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.SHAPE__STYLE, newStyle, newStyle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Connection> getSourceConnections() {
		if (sourceConnections == null) {
			sourceConnections = new EObjectResolvingEList<Connection>(Connection.class, this, DependenciesPackage.SHAPE__SOURCE_CONNECTIONS);
		}
		return sourceConnections;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Connection> getTargetConnections() {
		if (targetConnections == null) {
			targetConnections = new EObjectContainmentEList<Connection>(Connection.class, this, DependenciesPackage.SHAPE__TARGET_CONNECTIONS);
		}
		return targetConnections;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Layer getParentLayer() {
		if (eContainerFeatureID() != DependenciesPackage.SHAPE__PARENT_LAYER) return null;
		return (Layer)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParentLayer(Layer newParentLayer, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newParentLayer, DependenciesPackage.SHAPE__PARENT_LAYER, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentLayer(Layer newParentLayer) {
		if (newParentLayer != eInternalContainer() || (eContainerFeatureID() != DependenciesPackage.SHAPE__PARENT_LAYER && newParentLayer != null)) {
			if (EcoreUtil.isAncestor(this, newParentLayer))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParentLayer != null)
				msgs = ((InternalEObject)newParentLayer).eInverseAdd(this, DependenciesPackage.LAYER__SHAPES, Layer.class, msgs);
			msgs = basicSetParentLayer(newParentLayer, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.SHAPE__PARENT_LAYER, newParentLayer, newParentLayer));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.SHAPE__WAS_LAYOUTING, oldWasLayouting, wasLayouting));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DependenciesPackage.SHAPE__PARENT_LAYER:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetParentLayer((Layer)otherEnd, msgs);
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
			case DependenciesPackage.SHAPE__LOCATION:
				return basicSetLocation(null, msgs);
			case DependenciesPackage.SHAPE__SIZE:
				return basicSetSize(null, msgs);
			case DependenciesPackage.SHAPE__STYLE:
				return basicSetStyle(null, msgs);
			case DependenciesPackage.SHAPE__TARGET_CONNECTIONS:
				return ((InternalEList<?>)getTargetConnections()).basicRemove(otherEnd, msgs);
			case DependenciesPackage.SHAPE__PARENT_LAYER:
				return basicSetParentLayer(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case DependenciesPackage.SHAPE__PARENT_LAYER:
				return eInternalContainer().eInverseRemove(this, DependenciesPackage.LAYER__SHAPES, Layer.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DependenciesPackage.SHAPE__LOCATION:
				return getLocation();
			case DependenciesPackage.SHAPE__SIZE:
				return getSize();
			case DependenciesPackage.SHAPE__STYLE:
				return getStyle();
			case DependenciesPackage.SHAPE__SOURCE_CONNECTIONS:
				return getSourceConnections();
			case DependenciesPackage.SHAPE__TARGET_CONNECTIONS:
				return getTargetConnections();
			case DependenciesPackage.SHAPE__PARENT_LAYER:
				return getParentLayer();
			case DependenciesPackage.SHAPE__WAS_LAYOUTING:
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
			case DependenciesPackage.SHAPE__LOCATION:
				setLocation((Point)newValue);
				return;
			case DependenciesPackage.SHAPE__SIZE:
				setSize((Dimension)newValue);
				return;
			case DependenciesPackage.SHAPE__STYLE:
				setStyle((ShapeStyle)newValue);
				return;
			case DependenciesPackage.SHAPE__SOURCE_CONNECTIONS:
				getSourceConnections().clear();
				getSourceConnections().addAll((Collection<? extends Connection>)newValue);
				return;
			case DependenciesPackage.SHAPE__TARGET_CONNECTIONS:
				getTargetConnections().clear();
				getTargetConnections().addAll((Collection<? extends Connection>)newValue);
				return;
			case DependenciesPackage.SHAPE__PARENT_LAYER:
				setParentLayer((Layer)newValue);
				return;
			case DependenciesPackage.SHAPE__WAS_LAYOUTING:
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
			case DependenciesPackage.SHAPE__LOCATION:
				setLocation((Point)null);
				return;
			case DependenciesPackage.SHAPE__SIZE:
				setSize((Dimension)null);
				return;
			case DependenciesPackage.SHAPE__STYLE:
				setStyle((ShapeStyle)null);
				return;
			case DependenciesPackage.SHAPE__SOURCE_CONNECTIONS:
				getSourceConnections().clear();
				return;
			case DependenciesPackage.SHAPE__TARGET_CONNECTIONS:
				getTargetConnections().clear();
				return;
			case DependenciesPackage.SHAPE__PARENT_LAYER:
				setParentLayer((Layer)null);
				return;
			case DependenciesPackage.SHAPE__WAS_LAYOUTING:
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
			case DependenciesPackage.SHAPE__LOCATION:
				return location != null;
			case DependenciesPackage.SHAPE__SIZE:
				return size != null;
			case DependenciesPackage.SHAPE__STYLE:
				return style != null;
			case DependenciesPackage.SHAPE__SOURCE_CONNECTIONS:
				return sourceConnections != null && !sourceConnections.isEmpty();
			case DependenciesPackage.SHAPE__TARGET_CONNECTIONS:
				return targetConnections != null && !targetConnections.isEmpty();
			case DependenciesPackage.SHAPE__PARENT_LAYER:
				return getParentLayer() != null;
			case DependenciesPackage.SHAPE__WAS_LAYOUTING:
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
		result.append(" (wasLayouting: ");
		result.append(wasLayouting);
		result.append(')');
		return result.toString();
	}

} //ShapeImpl
