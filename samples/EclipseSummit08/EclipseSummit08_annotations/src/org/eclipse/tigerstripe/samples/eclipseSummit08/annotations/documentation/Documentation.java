/**
 * <copyright>
 * </copyright>
 *
 * $Id: Documentation.java,v 1.1 2008/11/05 19:53:22 edillon Exp $
 */
package org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Documentation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation#getAuthor <em>Author</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation#getContent <em>Content</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.DocumentationPackage#getDocumentation()
 * @model
 * @generated
 */
public interface Documentation extends EObject {
	/**
	 * Returns the value of the '<em><b>Author</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Author</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Author</em>' attribute.
	 * @see #setAuthor(String)
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.DocumentationPackage#getDocumentation_Author()
	 * @model
	 * @generated
	 */
	String getAuthor();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation#getAuthor <em>Author</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Author</em>' attribute.
	 * @see #getAuthor()
	 * @generated
	 */
	void setAuthor(String value);

	/**
	 * Returns the value of the '<em><b>Content</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content</em>' attribute.
	 * @see #setContent(String)
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.DocumentationPackage#getDocumentation_Content()
	 * @model annotation="org.eclipse.tigerstripe.annotation editor='org.eclipse.wst.html.core.htmlsource.source'"
	 * @generated
	 */
	String getContent();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation#getContent <em>Content</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content</em>' attribute.
	 * @see #getContent()
	 * @generated
	 */
	void setContent(String value);

} // Documentation
