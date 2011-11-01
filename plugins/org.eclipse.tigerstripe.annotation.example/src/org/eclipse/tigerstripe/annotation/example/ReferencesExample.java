/**
 * <copyright>
 * </copyright>
 *
 * $Id: ReferencesExample.java,v 1.1 2011/11/01 11:12:14 asalnik Exp $
 */
package org.eclipse.tigerstripe.annotation.example;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>References Example</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample#getEntity <em>Entity</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample#getStringRefToAttribute <em>String Ref To Attribute</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample#getStringRefsToAttributes <em>String Refs To Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.annotation.example.ExamplePackage#getReferencesExample()
 * @model
 * @generated
 */
public interface ReferencesExample extends EObject {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' containment reference.
	 * @see #setEntity(ModelReference)
	 * @see org.eclipse.tigerstripe.annotation.example.ExamplePackage#getReferencesExample_Entity()
	 * @model containment="true"
	 * @generated
	 */
	ModelReference getEntity();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample#getEntity <em>Entity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' containment reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(ModelReference value);

	/**
	 * Returns the value of the '<em><b>Attributes</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attributes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attributes</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.annotation.example.ExamplePackage#getReferencesExample_Attributes()
	 * @model containment="true"
	 * @generated
	 */
	EList<ModelReference> getAttributes();

	/**
	 * Returns the value of the '<em><b>String Ref To Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>String Ref To Attribute</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>String Ref To Attribute</em>' attribute.
	 * @see #setStringRefToAttribute(String)
	 * @see org.eclipse.tigerstripe.annotation.example.ExamplePackage#getReferencesExample_StringRefToAttribute()
	 * @model
	 * @generated
	 */
	String getStringRefToAttribute();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample#getStringRefToAttribute <em>String Ref To Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>String Ref To Attribute</em>' attribute.
	 * @see #getStringRefToAttribute()
	 * @generated
	 */
	void setStringRefToAttribute(String value);

	/**
	 * Returns the value of the '<em><b>String Refs To Attributes</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>String Refs To Attributes</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>String Refs To Attributes</em>' attribute list.
	 * @see org.eclipse.tigerstripe.annotation.example.ExamplePackage#getReferencesExample_StringRefsToAttributes()
	 * @model
	 * @generated
	 */
	EList<String> getStringRefsToAttributes();

} // ReferencesExample
