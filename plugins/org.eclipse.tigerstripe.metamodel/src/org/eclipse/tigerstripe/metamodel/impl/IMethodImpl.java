/**
 * <copyright>
 * </copyright>
 *
 * $Id: IMethodImpl.java,v 1.2 2008/05/22 18:26:28 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.tigerstripe.metamodel.IArgument;
import org.eclipse.tigerstripe.metamodel.IMethod;
import org.eclipse.tigerstripe.metamodel.IStereotypeInstance;
import org.eclipse.tigerstripe.metamodel.IType;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IMethod</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl#getReturnType <em>Return Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl#isAbstract <em>Abstract</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl#isOrdered <em>Ordered</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl#isUnique <em>Unique</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl#getExceptions <em>Exceptions</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl#isVoid <em>Void</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl#getDefaultReturnValue <em>Default Return Value</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl#getMethodReturnName <em>Method Return Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl#getReturnStereotypeInstances <em>Return Stereotype Instances</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IMethodImpl extends IModelComponentImpl implements IMethod {
	/**
	 * The cached value of the '{@link #getArguments() <em>Arguments</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArguments()
	 * @generated
	 * @ordered
	 */
	protected EList<IArgument> arguments;

	/**
	 * The cached value of the '{@link #getReturnType() <em>Return Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnType()
	 * @generated
	 * @ordered
	 */
	protected IType returnType;

	/**
	 * The default value of the '{@link #isAbstract() <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ABSTRACT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAbstract() <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAbstract()
	 * @generated
	 * @ordered
	 */
	protected boolean abstract_ = ABSTRACT_EDEFAULT;

	/**
	 * The default value of the '{@link #isOrdered() <em>Ordered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOrdered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ORDERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOrdered() <em>Ordered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOrdered()
	 * @generated
	 * @ordered
	 */
	protected boolean ordered = ORDERED_EDEFAULT;

	/**
	 * The default value of the '{@link #isUnique() <em>Unique</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUnique()
	 * @generated
	 * @ordered
	 */
	protected static final boolean UNIQUE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUnique() <em>Unique</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUnique()
	 * @generated
	 * @ordered
	 */
	protected boolean unique = UNIQUE_EDEFAULT;

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
	 * The default value of the '{@link #isVoid() <em>Void</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVoid()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VOID_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVoid() <em>Void</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVoid()
	 * @generated
	 * @ordered
	 */
	protected boolean void_ = VOID_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultReturnValue() <em>Default Return Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultReturnValue()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_RETURN_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultReturnValue() <em>Default Return Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultReturnValue()
	 * @generated
	 * @ordered
	 */
	protected String defaultReturnValue = DEFAULT_RETURN_VALUE_EDEFAULT;

	/**
	 * This is true if the Default Return Value attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean defaultReturnValueESet;

	/**
	 * The default value of the '{@link #getMethodReturnName() <em>Method Return Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethodReturnName()
	 * @generated
	 * @ordered
	 */
	protected static final String METHOD_RETURN_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMethodReturnName() <em>Method Return Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethodReturnName()
	 * @generated
	 * @ordered
	 */
	protected String methodReturnName = METHOD_RETURN_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReturnStereotypeInstances() <em>Return Stereotype Instances</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnStereotypeInstances()
	 * @generated
	 * @ordered
	 */
	protected EList<IStereotypeInstance> returnStereotypeInstances;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IMethodImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.IMETHOD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IArgument> getArguments() {
		if (arguments == null) {
			arguments = new EObjectResolvingEList<IArgument>(IArgument.class, this, MetamodelPackage.IMETHOD__ARGUMENTS);
		}
		return arguments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType getReturnType() {
		if (returnType != null && returnType.eIsProxy()) {
			InternalEObject oldReturnType = (InternalEObject)returnType;
			returnType = (IType)eResolveProxy(oldReturnType);
			if (returnType != oldReturnType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MetamodelPackage.IMETHOD__RETURN_TYPE, oldReturnType, returnType));
			}
		}
		return returnType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType basicGetReturnType() {
		return returnType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReturnType(IType newReturnType) {
		IType oldReturnType = returnType;
		returnType = newReturnType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IMETHOD__RETURN_TYPE, oldReturnType, returnType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAbstract() {
		return abstract_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAbstract(boolean newAbstract) {
		boolean oldAbstract = abstract_;
		abstract_ = newAbstract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IMETHOD__ABSTRACT, oldAbstract, abstract_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isOrdered() {
		return ordered;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOrdered(boolean newOrdered) {
		boolean oldOrdered = ordered;
		ordered = newOrdered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IMETHOD__ORDERED, oldOrdered, ordered));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnique(boolean newUnique) {
		boolean oldUnique = unique;
		unique = newUnique;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IMETHOD__UNIQUE, oldUnique, unique));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IType> getExceptions() {
		if (exceptions == null) {
			exceptions = new EObjectResolvingEList<IType>(IType.class, this, MetamodelPackage.IMETHOD__EXCEPTIONS);
		}
		return exceptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isVoid() {
		return void_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVoid(boolean newVoid) {
		boolean oldVoid = void_;
		void_ = newVoid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IMETHOD__VOID, oldVoid, void_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultReturnValue() {
		return defaultReturnValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultReturnValue(String newDefaultReturnValue) {
		String oldDefaultReturnValue = defaultReturnValue;
		defaultReturnValue = newDefaultReturnValue;
		boolean oldDefaultReturnValueESet = defaultReturnValueESet;
		defaultReturnValueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IMETHOD__DEFAULT_RETURN_VALUE, oldDefaultReturnValue, defaultReturnValue, !oldDefaultReturnValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDefaultReturnValue() {
		String oldDefaultReturnValue = defaultReturnValue;
		boolean oldDefaultReturnValueESet = defaultReturnValueESet;
		defaultReturnValue = DEFAULT_RETURN_VALUE_EDEFAULT;
		defaultReturnValueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, MetamodelPackage.IMETHOD__DEFAULT_RETURN_VALUE, oldDefaultReturnValue, DEFAULT_RETURN_VALUE_EDEFAULT, oldDefaultReturnValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDefaultReturnValue() {
		return defaultReturnValueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMethodReturnName() {
		return methodReturnName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMethodReturnName(String newMethodReturnName) {
		String oldMethodReturnName = methodReturnName;
		methodReturnName = newMethodReturnName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IMETHOD__METHOD_RETURN_NAME, oldMethodReturnName, methodReturnName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IStereotypeInstance> getReturnStereotypeInstances() {
		if (returnStereotypeInstances == null) {
			returnStereotypeInstances = new EObjectResolvingEList<IStereotypeInstance>(IStereotypeInstance.class, this, MetamodelPackage.IMETHOD__RETURN_STEREOTYPE_INSTANCES);
		}
		return returnStereotypeInstances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMethodId() {
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
			case MetamodelPackage.IMETHOD__ARGUMENTS:
				return getArguments();
			case MetamodelPackage.IMETHOD__RETURN_TYPE:
				if (resolve) return getReturnType();
				return basicGetReturnType();
			case MetamodelPackage.IMETHOD__ABSTRACT:
				return isAbstract() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IMETHOD__ORDERED:
				return isOrdered() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IMETHOD__UNIQUE:
				return isUnique() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IMETHOD__EXCEPTIONS:
				return getExceptions();
			case MetamodelPackage.IMETHOD__VOID:
				return isVoid() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IMETHOD__DEFAULT_RETURN_VALUE:
				return getDefaultReturnValue();
			case MetamodelPackage.IMETHOD__METHOD_RETURN_NAME:
				return getMethodReturnName();
			case MetamodelPackage.IMETHOD__RETURN_STEREOTYPE_INSTANCES:
				return getReturnStereotypeInstances();
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
			case MetamodelPackage.IMETHOD__ARGUMENTS:
				getArguments().clear();
				getArguments().addAll((Collection<? extends IArgument>)newValue);
				return;
			case MetamodelPackage.IMETHOD__RETURN_TYPE:
				setReturnType((IType)newValue);
				return;
			case MetamodelPackage.IMETHOD__ABSTRACT:
				setAbstract(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IMETHOD__ORDERED:
				setOrdered(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IMETHOD__UNIQUE:
				setUnique(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IMETHOD__EXCEPTIONS:
				getExceptions().clear();
				getExceptions().addAll((Collection<? extends IType>)newValue);
				return;
			case MetamodelPackage.IMETHOD__VOID:
				setVoid(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IMETHOD__DEFAULT_RETURN_VALUE:
				setDefaultReturnValue((String)newValue);
				return;
			case MetamodelPackage.IMETHOD__METHOD_RETURN_NAME:
				setMethodReturnName((String)newValue);
				return;
			case MetamodelPackage.IMETHOD__RETURN_STEREOTYPE_INSTANCES:
				getReturnStereotypeInstances().clear();
				getReturnStereotypeInstances().addAll((Collection<? extends IStereotypeInstance>)newValue);
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
			case MetamodelPackage.IMETHOD__ARGUMENTS:
				getArguments().clear();
				return;
			case MetamodelPackage.IMETHOD__RETURN_TYPE:
				setReturnType((IType)null);
				return;
			case MetamodelPackage.IMETHOD__ABSTRACT:
				setAbstract(ABSTRACT_EDEFAULT);
				return;
			case MetamodelPackage.IMETHOD__ORDERED:
				setOrdered(ORDERED_EDEFAULT);
				return;
			case MetamodelPackage.IMETHOD__UNIQUE:
				setUnique(UNIQUE_EDEFAULT);
				return;
			case MetamodelPackage.IMETHOD__EXCEPTIONS:
				getExceptions().clear();
				return;
			case MetamodelPackage.IMETHOD__VOID:
				setVoid(VOID_EDEFAULT);
				return;
			case MetamodelPackage.IMETHOD__DEFAULT_RETURN_VALUE:
				unsetDefaultReturnValue();
				return;
			case MetamodelPackage.IMETHOD__METHOD_RETURN_NAME:
				setMethodReturnName(METHOD_RETURN_NAME_EDEFAULT);
				return;
			case MetamodelPackage.IMETHOD__RETURN_STEREOTYPE_INSTANCES:
				getReturnStereotypeInstances().clear();
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
			case MetamodelPackage.IMETHOD__ARGUMENTS:
				return arguments != null && !arguments.isEmpty();
			case MetamodelPackage.IMETHOD__RETURN_TYPE:
				return returnType != null;
			case MetamodelPackage.IMETHOD__ABSTRACT:
				return abstract_ != ABSTRACT_EDEFAULT;
			case MetamodelPackage.IMETHOD__ORDERED:
				return ordered != ORDERED_EDEFAULT;
			case MetamodelPackage.IMETHOD__UNIQUE:
				return unique != UNIQUE_EDEFAULT;
			case MetamodelPackage.IMETHOD__EXCEPTIONS:
				return exceptions != null && !exceptions.isEmpty();
			case MetamodelPackage.IMETHOD__VOID:
				return void_ != VOID_EDEFAULT;
			case MetamodelPackage.IMETHOD__DEFAULT_RETURN_VALUE:
				return isSetDefaultReturnValue();
			case MetamodelPackage.IMETHOD__METHOD_RETURN_NAME:
				return METHOD_RETURN_NAME_EDEFAULT == null ? methodReturnName != null : !METHOD_RETURN_NAME_EDEFAULT.equals(methodReturnName);
			case MetamodelPackage.IMETHOD__RETURN_STEREOTYPE_INSTANCES:
				return returnStereotypeInstances != null && !returnStereotypeInstances.isEmpty();
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
		result.append(" (abstract: ");
		result.append(abstract_);
		result.append(", ordered: ");
		result.append(ordered);
		result.append(", unique: ");
		result.append(unique);
		result.append(", void: ");
		result.append(void_);
		result.append(", defaultReturnValue: ");
		if (defaultReturnValueESet) result.append(defaultReturnValue); else result.append("<unset>");
		result.append(", methodReturnName: ");
		result.append(methodReturnName);
		result.append(')');
		return result.toString();
	}

} //IMethodImpl
