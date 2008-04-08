/**
 * <copyright>
 * </copyright>
 *
 * $Id: Bundle.java,v 1.2 2008/04/08 22:22:15 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bundle</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle#getLink <em>Link</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle#getSize <em>Size</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getBundle()
 * @model extendedMetaData="name='bundle' kind='elementOnly'"
 * @generated
 */
public interface Bundle extends DownloadSiteElement {
	/**
	 * Returns the value of the '<em><b>Link</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Link</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Link</em>' attribute.
	 * @see #setLink(String)
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getBundle_Link()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='link'"
	 * @generated
	 */
	String getLink();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle#getLink <em>Link</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Link</em>' attribute.
	 * @see #getLink()
	 * @generated
	 */
	void setLink(String value);

	/**
	 * Returns the value of the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Size</em>' attribute.
	 * @see #setSize(String)
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getBundle_Size()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='size'"
	 * @generated
	 */
	String getSize();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle#getSize <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Size</em>' attribute.
	 * @see #getSize()
	 * @generated
	 */
	void setSize(String value);

} // Bundle
