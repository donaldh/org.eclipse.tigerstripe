/**
 * <copyright>
 * </copyright>
 *
 * $Id: IMethod.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IMethod</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IMethod#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IMethod#getReturnType <em>Return Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IMethod#isAbstract <em>Abstract</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IMethod#isOrdered <em>Ordered</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IMethod#isUnique <em>Unique</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IMethod#getExceptions <em>Exceptions</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IMethod#isVoid <em>Void</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IMethod#getDefaultReturnValue <em>Default Return Value</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IMethod#getMethodReturnName <em>Method Return Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IMethod#getReturnStereotypeInstances <em>Return Stereotype Instances</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod()
 * @model
 * @generated
 */
public interface IMethod extends IModelComponent {
	/**
	 * Returns the value of the '<em><b>Arguments</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IArgument}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arguments</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arguments</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod_Arguments()
	 * @model
	 * @generated
	 */
	EList<IArgument> getArguments();

	/**
	 * Returns the value of the '<em><b>Return Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Type</em>' reference.
	 * @see #setReturnType(IType)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod_ReturnType()
	 * @model
	 * @generated
	 */
	IType getReturnType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IMethod#getReturnType <em>Return Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Type</em>' reference.
	 * @see #getReturnType()
	 * @generated
	 */
	void setReturnType(IType value);

	/**
	 * Returns the value of the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Abstract</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Abstract</em>' attribute.
	 * @see #setAbstract(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod_Abstract()
	 * @model
	 * @generated
	 */
	boolean isAbstract();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IMethod#isAbstract <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Abstract</em>' attribute.
	 * @see #isAbstract()
	 * @generated
	 */
	void setAbstract(boolean value);

	/**
	 * Returns the value of the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ordered</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ordered</em>' attribute.
	 * @see #setOrdered(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod_Ordered()
	 * @model
	 * @generated
	 */
	boolean isOrdered();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IMethod#isOrdered <em>Ordered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ordered</em>' attribute.
	 * @see #isOrdered()
	 * @generated
	 */
	void setOrdered(boolean value);

	/**
	 * Returns the value of the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unique</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unique</em>' attribute.
	 * @see #setUnique(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod_Unique()
	 * @model
	 * @generated
	 */
	boolean isUnique();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IMethod#isUnique <em>Unique</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unique</em>' attribute.
	 * @see #isUnique()
	 * @generated
	 */
	void setUnique(boolean value);

	/**
	 * Returns the value of the '<em><b>Exceptions</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exceptions</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exceptions</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod_Exceptions()
	 * @model
	 * @generated
	 */
	EList<IType> getExceptions();

	/**
	 * Returns the value of the '<em><b>Void</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Void</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Void</em>' attribute.
	 * @see #setVoid(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod_Void()
	 * @model
	 * @generated
	 */
	boolean isVoid();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IMethod#isVoid <em>Void</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Void</em>' attribute.
	 * @see #isVoid()
	 * @generated
	 */
	void setVoid(boolean value);

	/**
	 * Returns the value of the '<em><b>Default Return Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Return Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Return Value</em>' attribute.
	 * @see #isSetDefaultReturnValue()
	 * @see #unsetDefaultReturnValue()
	 * @see #setDefaultReturnValue(String)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod_DefaultReturnValue()
	 * @model unsettable="true"
	 * @generated
	 */
	String getDefaultReturnValue();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IMethod#getDefaultReturnValue <em>Default Return Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Return Value</em>' attribute.
	 * @see #isSetDefaultReturnValue()
	 * @see #unsetDefaultReturnValue()
	 * @see #getDefaultReturnValue()
	 * @generated
	 */
	void setDefaultReturnValue(String value);

	/**
	 * Unsets the value of the '{@link org.eclipse.tigerstripe.metamodel.IMethod#getDefaultReturnValue <em>Default Return Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDefaultReturnValue()
	 * @see #getDefaultReturnValue()
	 * @see #setDefaultReturnValue(String)
	 * @generated
	 */
	void unsetDefaultReturnValue();

	/**
	 * Returns whether the value of the '{@link org.eclipse.tigerstripe.metamodel.IMethod#getDefaultReturnValue <em>Default Return Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Default Return Value</em>' attribute is set.
	 * @see #unsetDefaultReturnValue()
	 * @see #getDefaultReturnValue()
	 * @see #setDefaultReturnValue(String)
	 * @generated
	 */
	boolean isSetDefaultReturnValue();

	/**
	 * Returns the value of the '<em><b>Method Return Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method Return Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method Return Name</em>' attribute.
	 * @see #setMethodReturnName(String)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod_MethodReturnName()
	 * @model
	 * @generated
	 */
	String getMethodReturnName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IMethod#getMethodReturnName <em>Method Return Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method Return Name</em>' attribute.
	 * @see #getMethodReturnName()
	 * @generated
	 */
	void setMethodReturnName(String value);

	/**
	 * Returns the value of the '<em><b>Return Stereotype Instances</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IStereotypeInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Stereotype Instances</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Stereotype Instances</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIMethod_ReturnStereotypeInstances()
	 * @model
	 * @generated
	 */
	EList<IStereotypeInstance> getReturnStereotypeInstances();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getMethodId();

} // IMethod
