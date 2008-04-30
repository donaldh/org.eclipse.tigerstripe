/**
 * <copyright>
 * </copyright>
 *
 * $Id: Component.java,v 1.3 2008/04/30 21:37:10 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Component#getBundle <em>Bundle</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Component#getJunitResultsURL <em>Junit Results URL</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getComponent()
 * @model extendedMetaData="name='component' kind='elementOnly'"
 * @generated
 */
public interface Component extends DownloadSiteElement {
	/**
	 * Returns the value of the '<em><b>Bundle</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bundle</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bundle</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getComponent_Bundle()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='bundle' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Bundle> getBundle();

	/**
	 * Returns the value of the '<em><b>Junit Results URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Junit Results URL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Junit Results URL</em>' attribute.
	 * @see #setJunitResultsURL(String)
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getComponent_JunitResultsURL()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='junitResultsURL'"
	 * @generated
	 */
	String getJunitResultsURL();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Component#getJunitResultsURL <em>Junit Results URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Junit Results URL</em>' attribute.
	 * @see #getJunitResultsURL()
	 * @generated
	 */
	void setJunitResultsURL(String value);

} // Component
