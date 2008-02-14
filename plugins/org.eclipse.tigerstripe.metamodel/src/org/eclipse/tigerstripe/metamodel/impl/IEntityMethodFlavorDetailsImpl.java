/**
 * <copyright>
 * </copyright>
 *
 * $Id: IEntityMethodFlavorDetailsImpl.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.metamodel.IMethod;
import org.eclipse.tigerstripe.metamodel.IType;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;
import org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IEntity Method Flavor Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IEntityMethodFlavorDetailsImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IEntityMethodFlavorDetailsImpl#getFlag <em>Flag</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IEntityMethodFlavorDetailsImpl#getExceptions <em>Exceptions</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IEntityMethodFlavorDetailsImpl#getMethod <em>Method</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IEntityMethodFlavorDetailsImpl#getFlavor <em>Flavor</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IEntityMethodFlavorDetailsImpl#getMethodType <em>Method Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IEntityMethodFlavorDetailsImpl extends EObjectImpl implements IEntityMethodFlavorDetails {
	/**
	 * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected static final String COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected String comment = COMMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getFlag() <em>Flag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlag()
	 * @generated
	 * @ordered
	 */
	protected static final EEntityMethodFlavorFlag FLAG_EDEFAULT = EEntityMethodFlavorFlag.TRUE;

	/**
	 * The cached value of the '{@link #getFlag() <em>Flag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlag()
	 * @generated
	 * @ordered
	 */
	protected EEntityMethodFlavorFlag flag = FLAG_EDEFAULT;

	/**
	 * The cached value of the '{@link #getExceptions() <em>Exceptions</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExceptions()
	 * @generated
	 * @ordered
	 */
	protected EList<IType> exceptions;

	/**
	 * The cached value of the '{@link #getMethod() <em>Method</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethod()
	 * @generated
	 * @ordered
	 */
	protected IMethod method;

	/**
	 * The default value of the '{@link #getFlavor() <em>Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlavor()
	 * @generated
	 * @ordered
	 */
	protected static final OssjEntityMethodFlavor FLAVOR_EDEFAULT = OssjEntityMethodFlavor.SIMPLE;

	/**
	 * The cached value of the '{@link #getFlavor() <em>Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlavor()
	 * @generated
	 * @ordered
	 */
	protected OssjEntityMethodFlavor flavor = FLAVOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getMethodType() <em>Method Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethodType()
	 * @generated
	 * @ordered
	 */
	protected static final EMethodType METHOD_TYPE_EDEFAULT = EMethodType.CRUD_CREATE;

	/**
	 * The cached value of the '{@link #getMethodType() <em>Method Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethodType()
	 * @generated
	 * @ordered
	 */
	protected EMethodType methodType = METHOD_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IEntityMethodFlavorDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.IENTITY_METHOD_FLAVOR_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComment(String newComment) {
		String oldComment = comment;
		comment = newComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__COMMENT, oldComment, comment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEntityMethodFlavorFlag getFlag() {
		return flag;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFlag(EEntityMethodFlavorFlag newFlag) {
		EEntityMethodFlavorFlag oldFlag = flag;
		flag = newFlag == null ? FLAG_EDEFAULT : newFlag;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__FLAG, oldFlag, flag));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IType> getExceptions() {
		if (exceptions == null) {
			exceptions = new EObjectResolvingEList<IType>(IType.class, this, MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__EXCEPTIONS);
		}
		return exceptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IMethod getMethod() {
		if (method != null && method.eIsProxy()) {
			InternalEObject oldMethod = (InternalEObject)method;
			method = (IMethod)eResolveProxy(oldMethod);
			if (method != oldMethod) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD, oldMethod, method));
			}
		}
		return method;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IMethod basicGetMethod() {
		return method;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMethod(IMethod newMethod) {
		IMethod oldMethod = method;
		method = newMethod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD, oldMethod, method));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OssjEntityMethodFlavor getFlavor() {
		return flavor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFlavor(OssjEntityMethodFlavor newFlavor) {
		OssjEntityMethodFlavor oldFlavor = flavor;
		flavor = newFlavor == null ? FLAVOR_EDEFAULT : newFlavor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__FLAVOR, oldFlavor, flavor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMethodType getMethodType() {
		return methodType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMethodType(EMethodType newMethodType) {
		EMethodType oldMethodType = methodType;
		methodType = newMethodType == null ? METHOD_TYPE_EDEFAULT : newMethodType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD_TYPE, oldMethodType, methodType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__COMMENT:
				return getComment();
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__FLAG:
				return getFlag();
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__EXCEPTIONS:
				return getExceptions();
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD:
				if (resolve) return getMethod();
				return basicGetMethod();
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__FLAVOR:
				return getFlavor();
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD_TYPE:
				return getMethodType();
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
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__COMMENT:
				setComment((String)newValue);
				return;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__FLAG:
				setFlag((EEntityMethodFlavorFlag)newValue);
				return;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__EXCEPTIONS:
				getExceptions().clear();
				getExceptions().addAll((Collection<? extends IType>)newValue);
				return;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD:
				setMethod((IMethod)newValue);
				return;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__FLAVOR:
				setFlavor((OssjEntityMethodFlavor)newValue);
				return;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD_TYPE:
				setMethodType((EMethodType)newValue);
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
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__COMMENT:
				setComment(COMMENT_EDEFAULT);
				return;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__FLAG:
				setFlag(FLAG_EDEFAULT);
				return;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__EXCEPTIONS:
				getExceptions().clear();
				return;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD:
				setMethod((IMethod)null);
				return;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__FLAVOR:
				setFlavor(FLAVOR_EDEFAULT);
				return;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD_TYPE:
				setMethodType(METHOD_TYPE_EDEFAULT);
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
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__COMMENT:
				return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__FLAG:
				return flag != FLAG_EDEFAULT;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__EXCEPTIONS:
				return exceptions != null && !exceptions.isEmpty();
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD:
				return method != null;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__FLAVOR:
				return flavor != FLAVOR_EDEFAULT;
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS__METHOD_TYPE:
				return methodType != METHOD_TYPE_EDEFAULT;
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
		result.append(" (comment: ");
		result.append(comment);
		result.append(", flag: ");
		result.append(flag);
		result.append(", flavor: ");
		result.append(flavor);
		result.append(", methodType: ");
		result.append(methodType);
		result.append(')');
		return result.toString();
	}

} //IEntityMethodFlavorDetailsImpl
