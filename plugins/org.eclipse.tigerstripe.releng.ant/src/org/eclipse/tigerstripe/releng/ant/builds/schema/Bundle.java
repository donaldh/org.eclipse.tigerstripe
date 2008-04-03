/**
 * <copyright>
 * </copyright>
 *
 * $Id: Bundle.java,v 1.1 2008/04/03 21:45:31 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.ant.builds.schema;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bundle</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Bundle#getDownloadLink <em>Download Link</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaPackage#getBundle()
 * @model extendedMetaData="name='bundle' kind='elementOnly'"
 * @generated
 */
public interface Bundle extends BuildElement {
	/**
	 * Returns the value of the '<em><b>Download Link</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Download Link</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Download Link</em>' attribute.
	 * @see #setDownloadLink(String)
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaPackage#getBundle_DownloadLink()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='downloadLink'"
	 * @generated
	 */
	String getDownloadLink();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Bundle#getDownloadLink <em>Download Link</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Download Link</em>' attribute.
	 * @see #getDownloadLink()
	 * @generated
	 */
	void setDownloadLink(String value);

} // Bundle
