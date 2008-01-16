/**
 * <copyright>
 * </copyright>
 *
 * $Id: Annotation.java,v 1.1 2008/01/16 18:14:19 edillon Exp $
 */
package org.eclipse.tigerstripe.annotations.internal.context;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotations.internal.context.Annotation#getAnnotationSpecificationID <em>Annotation Specification ID</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotations.internal.context.Annotation#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotations.internal.context.Annotation#getUri <em>Uri</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.annotations.internal.context.ContextPackage#getAnnotation()
 * @model
 * @generated
 */
public interface Annotation extends EObject {
	/**
	 * Returns the value of the '<em><b>Annotation Specification ID</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Annotation Specification ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Annotation Specification ID</em>' attribute.
	 * @see #setAnnotationSpecificationID(String)
	 * @see org.eclipse.tigerstripe.annotations.internal.context.ContextPackage#getAnnotation_AnnotationSpecificationID()
	 * @model default="" required="true"
	 * @generated
	 */
	String getAnnotationSpecificationID();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotations.internal.context.Annotation#getAnnotationSpecificationID <em>Annotation Specification ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Annotation Specification ID</em>' attribute.
	 * @see #getAnnotationSpecificationID()
	 * @generated
	 */
	void setAnnotationSpecificationID(String value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Object)
	 * @see org.eclipse.tigerstripe.annotations.internal.context.ContextPackage#getAnnotation_Value()
	 * @model
	 * @generated
	 */
	Object getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotations.internal.context.Annotation#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Object value);

	/**
	 * Returns the value of the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uri</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uri</em>' attribute.
	 * @see #setUri(String)
	 * @see org.eclipse.tigerstripe.annotations.internal.context.ContextPackage#getAnnotation_Uri()
	 * @model
	 * @generated
	 */
	String getUri();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotations.internal.context.Annotation#getUri <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uri</em>' attribute.
	 * @see #getUri()
	 * @generated
	 */
	void setUri(String value);

} // Annotation
