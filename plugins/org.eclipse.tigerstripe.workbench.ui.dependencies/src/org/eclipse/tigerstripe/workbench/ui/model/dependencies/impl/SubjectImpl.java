/**
 * <copyright>
 * </copyright>
 *
 * $Id: SubjectImpl.java,v 1.2 2010/11/17 06:03:52 ystrot Exp $
 */
package org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Subject</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.SubjectImpl#isOpened <em>Opened</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.SubjectImpl#isUseCustomStyle <em>Use Custom Style</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.SubjectImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.SubjectImpl#isMaster <em>Master</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.SubjectImpl#isLoaded <em>Loaded</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SubjectImpl extends ShapeImpl implements Subject {
	/**
	 * The default value of the '{@link #isOpened() <em>Opened</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOpened()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OPENED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOpened() <em>Opened</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOpened()
	 * @generated
	 * @ordered
	 */
	protected boolean opened = OPENED_EDEFAULT;

	/**
	 * The default value of the '{@link #isUseCustomStyle() <em>Use Custom Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseCustomStyle()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_CUSTOM_STYLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUseCustomStyle() <em>Use Custom Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseCustomStyle()
	 * @generated
	 * @ordered
	 */
	protected boolean useCustomStyle = USE_CUSTOM_STYLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected Kind kind;

	/**
	 * The default value of the '{@link #isMaster() <em>Master</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMaster()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MASTER_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMaster() <em>Master</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMaster()
	 * @generated
	 * @ordered
	 */
	protected boolean master = MASTER_EDEFAULT;

	/**
	 * The default value of the '{@link #isLoaded() <em>Loaded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLoaded()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LOADED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLoaded() <em>Loaded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLoaded()
	 * @generated
	 * @ordered
	 */
	protected boolean loaded = LOADED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SubjectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DependenciesPackage.Literals.SUBJECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isOpened() {
		return opened;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOpened(boolean newOpened) {
		boolean oldOpened = opened;
		opened = newOpened;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.SUBJECT__OPENED, oldOpened, opened));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUseCustomStyle() {
		return useCustomStyle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUseCustomStyle(boolean newUseCustomStyle) {
		boolean oldUseCustomStyle = useCustomStyle;
		useCustomStyle = newUseCustomStyle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.SUBJECT__USE_CUSTOM_STYLE, oldUseCustomStyle, useCustomStyle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Kind getKind() {
		if (kind != null && kind.eIsProxy()) {
			InternalEObject oldKind = (InternalEObject)kind;
			kind = (Kind)eResolveProxy(oldKind);
			if (kind != oldKind) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DependenciesPackage.SUBJECT__KIND, oldKind, kind));
			}
		}
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Kind basicGetKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKind(Kind newKind) {
		Kind oldKind = kind;
		kind = newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.SUBJECT__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMaster() {
		return master;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaster(boolean newMaster) {
		boolean oldMaster = master;
		master = newMaster;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.SUBJECT__MASTER, oldMaster, master));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoaded(boolean newLoaded) {
		boolean oldLoaded = loaded;
		loaded = newLoaded;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.SUBJECT__LOADED, oldLoaded, loaded));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DependenciesPackage.SUBJECT__OPENED:
				return isOpened();
			case DependenciesPackage.SUBJECT__USE_CUSTOM_STYLE:
				return isUseCustomStyle();
			case DependenciesPackage.SUBJECT__KIND:
				if (resolve) return getKind();
				return basicGetKind();
			case DependenciesPackage.SUBJECT__MASTER:
				return isMaster();
			case DependenciesPackage.SUBJECT__LOADED:
				return isLoaded();
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
			case DependenciesPackage.SUBJECT__OPENED:
				setOpened((Boolean)newValue);
				return;
			case DependenciesPackage.SUBJECT__USE_CUSTOM_STYLE:
				setUseCustomStyle((Boolean)newValue);
				return;
			case DependenciesPackage.SUBJECT__KIND:
				setKind((Kind)newValue);
				return;
			case DependenciesPackage.SUBJECT__MASTER:
				setMaster((Boolean)newValue);
				return;
			case DependenciesPackage.SUBJECT__LOADED:
				setLoaded((Boolean)newValue);
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
			case DependenciesPackage.SUBJECT__OPENED:
				setOpened(OPENED_EDEFAULT);
				return;
			case DependenciesPackage.SUBJECT__USE_CUSTOM_STYLE:
				setUseCustomStyle(USE_CUSTOM_STYLE_EDEFAULT);
				return;
			case DependenciesPackage.SUBJECT__KIND:
				setKind((Kind)null);
				return;
			case DependenciesPackage.SUBJECT__MASTER:
				setMaster(MASTER_EDEFAULT);
				return;
			case DependenciesPackage.SUBJECT__LOADED:
				setLoaded(LOADED_EDEFAULT);
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
			case DependenciesPackage.SUBJECT__OPENED:
				return opened != OPENED_EDEFAULT;
			case DependenciesPackage.SUBJECT__USE_CUSTOM_STYLE:
				return useCustomStyle != USE_CUSTOM_STYLE_EDEFAULT;
			case DependenciesPackage.SUBJECT__KIND:
				return kind != null;
			case DependenciesPackage.SUBJECT__MASTER:
				return master != MASTER_EDEFAULT;
			case DependenciesPackage.SUBJECT__LOADED:
				return loaded != LOADED_EDEFAULT;
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
		result.append(" (opened: ");
		result.append(opened);
		result.append(", useCustomStyle: ");
		result.append(useCustomStyle);
		result.append(", master: ");
		result.append(master);
		result.append(", loaded: ");
		result.append(loaded);
		result.append(')');
		return result.toString();
	}

} //SubjectImpl
