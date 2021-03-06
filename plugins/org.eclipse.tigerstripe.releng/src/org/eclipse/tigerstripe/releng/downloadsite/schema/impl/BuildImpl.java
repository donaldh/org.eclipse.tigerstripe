/**
 * <copyright>
 * </copyright>
 *
 * $Id: BuildImpl.java,v 1.2 2008/04/08 22:22:16 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.releng.downloadsite.schema.Build;
import org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Component;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Dependency;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Detail;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Build</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BuildImpl#getComponent <em>Component</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BuildImpl#getDependency <em>Dependency</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BuildImpl#getDetail <em>Detail</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BuildImpl#getStream <em>Stream</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BuildImpl#getTstamp <em>Tstamp</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BuildImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BuildImpl extends DownloadSiteElementImpl implements Build {
	/**
	 * The cached value of the '{@link #getComponent() <em>Component</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponent()
	 * @generated
	 * @ordered
	 */
	protected EList<Component> component;

	/**
	 * The cached value of the '{@link #getDependency() <em>Dependency</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependency()
	 * @generated
	 * @ordered
	 */
	protected EList<Dependency> dependency;

	/**
	 * The cached value of the '{@link #getDetail() <em>Detail</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDetail()
	 * @generated
	 * @ordered
	 */
	protected EList<Detail> detail;

	/**
	 * The default value of the '{@link #getStream() <em>Stream</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStream()
	 * @generated
	 * @ordered
	 */
	protected static final String STREAM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStream() <em>Stream</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStream()
	 * @generated
	 * @ordered
	 */
	protected String stream = STREAM_EDEFAULT;

	/**
	 * The default value of the '{@link #getTstamp() <em>Tstamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTstamp()
	 * @generated
	 * @ordered
	 */
	protected static final String TSTAMP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTstamp() <em>Tstamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTstamp()
	 * @generated
	 * @ordered
	 */
	protected String tstamp = TSTAMP_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final BuildType TYPE_EDEFAULT = BuildType.R;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected BuildType type = TYPE_EDEFAULT;

	/**
	 * This is true if the Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean typeESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BuildImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DownloadSitePackage.Literals.BUILD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Component> getComponent() {
		if (component == null) {
			component = new EObjectContainmentEList<Component>(Component.class, this, DownloadSitePackage.BUILD__COMPONENT);
		}
		return component;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Dependency> getDependency() {
		if (dependency == null) {
			dependency = new EObjectContainmentEList<Dependency>(Dependency.class, this, DownloadSitePackage.BUILD__DEPENDENCY);
		}
		return dependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Detail> getDetail() {
		if (detail == null) {
			detail = new EObjectContainmentEList<Detail>(Detail.class, this, DownloadSitePackage.BUILD__DETAIL);
		}
		return detail;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStream() {
		return stream;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStream(String newStream) {
		String oldStream = stream;
		stream = newStream;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DownloadSitePackage.BUILD__STREAM, oldStream, stream));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTstamp() {
		return tstamp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTstamp(String newTstamp) {
		String oldTstamp = tstamp;
		tstamp = newTstamp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DownloadSitePackage.BUILD__TSTAMP, oldTstamp, tstamp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuildType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(BuildType newType) {
		BuildType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DownloadSitePackage.BUILD__TYPE, oldType, type, !oldTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetType() {
		BuildType oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DownloadSitePackage.BUILD__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetType() {
		return typeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DownloadSitePackage.BUILD__COMPONENT:
				return ((InternalEList<?>)getComponent()).basicRemove(otherEnd, msgs);
			case DownloadSitePackage.BUILD__DEPENDENCY:
				return ((InternalEList<?>)getDependency()).basicRemove(otherEnd, msgs);
			case DownloadSitePackage.BUILD__DETAIL:
				return ((InternalEList<?>)getDetail()).basicRemove(otherEnd, msgs);
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
			case DownloadSitePackage.BUILD__COMPONENT:
				return getComponent();
			case DownloadSitePackage.BUILD__DEPENDENCY:
				return getDependency();
			case DownloadSitePackage.BUILD__DETAIL:
				return getDetail();
			case DownloadSitePackage.BUILD__STREAM:
				return getStream();
			case DownloadSitePackage.BUILD__TSTAMP:
				return getTstamp();
			case DownloadSitePackage.BUILD__TYPE:
				return getType();
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
			case DownloadSitePackage.BUILD__COMPONENT:
				getComponent().clear();
				getComponent().addAll((Collection<? extends Component>)newValue);
				return;
			case DownloadSitePackage.BUILD__DEPENDENCY:
				getDependency().clear();
				getDependency().addAll((Collection<? extends Dependency>)newValue);
				return;
			case DownloadSitePackage.BUILD__DETAIL:
				getDetail().clear();
				getDetail().addAll((Collection<? extends Detail>)newValue);
				return;
			case DownloadSitePackage.BUILD__STREAM:
				setStream((String)newValue);
				return;
			case DownloadSitePackage.BUILD__TSTAMP:
				setTstamp((String)newValue);
				return;
			case DownloadSitePackage.BUILD__TYPE:
				setType((BuildType)newValue);
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
			case DownloadSitePackage.BUILD__COMPONENT:
				getComponent().clear();
				return;
			case DownloadSitePackage.BUILD__DEPENDENCY:
				getDependency().clear();
				return;
			case DownloadSitePackage.BUILD__DETAIL:
				getDetail().clear();
				return;
			case DownloadSitePackage.BUILD__STREAM:
				setStream(STREAM_EDEFAULT);
				return;
			case DownloadSitePackage.BUILD__TSTAMP:
				setTstamp(TSTAMP_EDEFAULT);
				return;
			case DownloadSitePackage.BUILD__TYPE:
				unsetType();
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
			case DownloadSitePackage.BUILD__COMPONENT:
				return component != null && !component.isEmpty();
			case DownloadSitePackage.BUILD__DEPENDENCY:
				return dependency != null && !dependency.isEmpty();
			case DownloadSitePackage.BUILD__DETAIL:
				return detail != null && !detail.isEmpty();
			case DownloadSitePackage.BUILD__STREAM:
				return STREAM_EDEFAULT == null ? stream != null : !STREAM_EDEFAULT.equals(stream);
			case DownloadSitePackage.BUILD__TSTAMP:
				return TSTAMP_EDEFAULT == null ? tstamp != null : !TSTAMP_EDEFAULT.equals(tstamp);
			case DownloadSitePackage.BUILD__TYPE:
				return isSetType();
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
		result.append(" (stream: ");
		result.append(stream);
		result.append(", tstamp: ");
		result.append(tstamp);
		result.append(", type: ");
		if (typeESet) result.append(type); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //BuildImpl
