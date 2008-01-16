/**
 * <copyright>
 * </copyright>
 *
 * $Id: AnnotationContext.java,v 1.1 2008/01/16 18:14:19 edillon Exp $
 */
package org.eclipse.tigerstripe.annotations.internal.context;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation Context</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext#getAnnotations <em>Annotations</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext#getNamespaceID <em>Namespace ID</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.annotations.internal.context.ContextPackage#getAnnotationContext()
 * @model
 * @generated
 */
public interface AnnotationContext extends EObject {
	/**
	 * Returns the value of the '<em><b>Annotations</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.annotations.internal.context.Annotation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Annotations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Annotations</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.annotations.internal.context.ContextPackage#getAnnotationContext_Annotations()
	 * @model containment="true"
	 * @generated
	 */
	EList<Annotation> getAnnotations();

	/**
	 * Returns the value of the '<em><b>Namespace ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Namespace ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Namespace ID</em>' attribute.
	 * @see #setNamespaceID(String)
	 * @see org.eclipse.tigerstripe.annotations.internal.context.ContextPackage#getAnnotationContext_NamespaceID()
	 * @model
	 * @generated
	 */
	String getNamespaceID();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext#getNamespaceID <em>Namespace ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Namespace ID</em>' attribute.
	 * @see #getNamespaceID()
	 * @generated
	 */
	void setNamespaceID(String value);

} // AnnotationContext
