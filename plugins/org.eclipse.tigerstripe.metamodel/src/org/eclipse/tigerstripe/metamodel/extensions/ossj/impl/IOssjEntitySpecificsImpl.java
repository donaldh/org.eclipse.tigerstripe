/**
 * <copyright>
 * </copyright>
 *
 * $Id: IOssjEntitySpecificsImpl.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor;

import org.eclipse.tigerstripe.metamodel.extensions.IProperties;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IOssj Entity Specifics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEntitySpecificsImpl#getFlavorDetails <em>Flavor Details</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEntitySpecificsImpl#getPrimaryKey <em>Primary Key</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEntitySpecificsImpl#getExtensibilityType <em>Extensibility Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEntitySpecificsImpl#isSessionFactoryMethods <em>Session Factory Methods</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEntitySpecificsImpl#getInterfaceKeyProperties <em>Interface Key Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IOssjEntitySpecificsImpl extends IOssjArtifactSpecificsImpl implements IOssjEntitySpecifics {
	/**
	 * The cached value of the '{@link #getFlavorDetails() <em>Flavor Details</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlavorDetails()
	 * @generated
	 * @ordered
	 */
	protected EList<IEntityMethodFlavorDetails> flavorDetails;

	/**
	 * The default value of the '{@link #getPrimaryKey() <em>Primary Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryKey()
	 * @generated
	 * @ordered
	 */
	protected static final String PRIMARY_KEY_EDEFAULT = "String";

	/**
	 * The cached value of the '{@link #getPrimaryKey() <em>Primary Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryKey()
	 * @generated
	 * @ordered
	 */
	protected String primaryKey = PRIMARY_KEY_EDEFAULT;

	/**
	 * The default value of the '{@link #getExtensibilityType() <em>Extensibility Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtensibilityType()
	 * @generated
	 * @ordered
	 */
	protected static final String EXTENSIBILITY_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExtensibilityType() <em>Extensibility Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtensibilityType()
	 * @generated
	 * @ordered
	 */
	protected String extensibilityType = EXTENSIBILITY_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #isSessionFactoryMethods() <em>Session Factory Methods</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSessionFactoryMethods()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SESSION_FACTORY_METHODS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSessionFactoryMethods() <em>Session Factory Methods</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSessionFactoryMethods()
	 * @generated
	 * @ordered
	 */
	protected boolean sessionFactoryMethods = SESSION_FACTORY_METHODS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInterfaceKeyProperties() <em>Interface Key Properties</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInterfaceKeyProperties()
	 * @generated
	 * @ordered
	 */
	protected IProperties interfaceKeyProperties;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IOssjEntitySpecificsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OssjPackage.Literals.IOSSJ_ENTITY_SPECIFICS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IEntityMethodFlavorDetails> getFlavorDetails() {
		if (flavorDetails == null) {
			flavorDetails = new EObjectResolvingEList<IEntityMethodFlavorDetails>(IEntityMethodFlavorDetails.class, this, OssjPackage.IOSSJ_ENTITY_SPECIFICS__FLAVOR_DETAILS);
		}
		return flavorDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrimaryKey(String newPrimaryKey) {
		String oldPrimaryKey = primaryKey;
		primaryKey = newPrimaryKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IOSSJ_ENTITY_SPECIFICS__PRIMARY_KEY, oldPrimaryKey, primaryKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExtensibilityType() {
		return extensibilityType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtensibilityType(String newExtensibilityType) {
		String oldExtensibilityType = extensibilityType;
		extensibilityType = newExtensibilityType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IOSSJ_ENTITY_SPECIFICS__EXTENSIBILITY_TYPE, oldExtensibilityType, extensibilityType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSessionFactoryMethods() {
		return sessionFactoryMethods;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSessionFactoryMethods(boolean newSessionFactoryMethods) {
		boolean oldSessionFactoryMethods = sessionFactoryMethods;
		sessionFactoryMethods = newSessionFactoryMethods;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IOSSJ_ENTITY_SPECIFICS__SESSION_FACTORY_METHODS, oldSessionFactoryMethods, sessionFactoryMethods));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IProperties getInterfaceKeyProperties() {
		if (interfaceKeyProperties != null && interfaceKeyProperties.eIsProxy()) {
			InternalEObject oldInterfaceKeyProperties = (InternalEObject)interfaceKeyProperties;
			interfaceKeyProperties = (IProperties)eResolveProxy(oldInterfaceKeyProperties);
			if (interfaceKeyProperties != oldInterfaceKeyProperties) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OssjPackage.IOSSJ_ENTITY_SPECIFICS__INTERFACE_KEY_PROPERTIES, oldInterfaceKeyProperties, interfaceKeyProperties));
			}
		}
		return interfaceKeyProperties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IProperties basicGetInterfaceKeyProperties() {
		return interfaceKeyProperties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInterfaceKeyProperties(IProperties newInterfaceKeyProperties) {
		IProperties oldInterfaceKeyProperties = interfaceKeyProperties;
		interfaceKeyProperties = newInterfaceKeyProperties;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IOSSJ_ENTITY_SPECIFICS__INTERFACE_KEY_PROPERTIES, oldInterfaceKeyProperties, interfaceKeyProperties));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEntityMethodFlavorDetails getCRUDFlavorDetails(int crudID, OssjEntityMethodFlavor flavor) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCRUDFlavorDetails(int crudID, OssjEntityMethodFlavor flavor, IEntityMethodFlavorDetails details) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OssjEntityMethodFlavor> getSupportedFlavors(int crudID) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__FLAVOR_DETAILS:
				return getFlavorDetails();
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__PRIMARY_KEY:
				return getPrimaryKey();
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__EXTENSIBILITY_TYPE:
				return getExtensibilityType();
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__SESSION_FACTORY_METHODS:
				return isSessionFactoryMethods() ? Boolean.TRUE : Boolean.FALSE;
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__INTERFACE_KEY_PROPERTIES:
				if (resolve) return getInterfaceKeyProperties();
				return basicGetInterfaceKeyProperties();
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
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__FLAVOR_DETAILS:
				getFlavorDetails().clear();
				getFlavorDetails().addAll((Collection<? extends IEntityMethodFlavorDetails>)newValue);
				return;
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__PRIMARY_KEY:
				setPrimaryKey((String)newValue);
				return;
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__EXTENSIBILITY_TYPE:
				setExtensibilityType((String)newValue);
				return;
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__SESSION_FACTORY_METHODS:
				setSessionFactoryMethods(((Boolean)newValue).booleanValue());
				return;
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__INTERFACE_KEY_PROPERTIES:
				setInterfaceKeyProperties((IProperties)newValue);
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
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__FLAVOR_DETAILS:
				getFlavorDetails().clear();
				return;
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__PRIMARY_KEY:
				setPrimaryKey(PRIMARY_KEY_EDEFAULT);
				return;
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__EXTENSIBILITY_TYPE:
				setExtensibilityType(EXTENSIBILITY_TYPE_EDEFAULT);
				return;
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__SESSION_FACTORY_METHODS:
				setSessionFactoryMethods(SESSION_FACTORY_METHODS_EDEFAULT);
				return;
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__INTERFACE_KEY_PROPERTIES:
				setInterfaceKeyProperties((IProperties)null);
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
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__FLAVOR_DETAILS:
				return flavorDetails != null && !flavorDetails.isEmpty();
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__PRIMARY_KEY:
				return PRIMARY_KEY_EDEFAULT == null ? primaryKey != null : !PRIMARY_KEY_EDEFAULT.equals(primaryKey);
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__EXTENSIBILITY_TYPE:
				return EXTENSIBILITY_TYPE_EDEFAULT == null ? extensibilityType != null : !EXTENSIBILITY_TYPE_EDEFAULT.equals(extensibilityType);
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__SESSION_FACTORY_METHODS:
				return sessionFactoryMethods != SESSION_FACTORY_METHODS_EDEFAULT;
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS__INTERFACE_KEY_PROPERTIES:
				return interfaceKeyProperties != null;
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
		result.append(" (primaryKey: ");
		result.append(primaryKey);
		result.append(", extensibilityType: ");
		result.append(extensibilityType);
		result.append(", sessionFactoryMethods: ");
		result.append(sessionFactoryMethods);
		result.append(')');
		return result.toString();
	}

} //IOssjEntitySpecificsImpl
