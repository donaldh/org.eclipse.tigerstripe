/**
 * <copyright>
 * </copyright>
 *
 * $Id: DownloadSiteElement.java,v 1.2 2008/04/08 22:22:15 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *         		This is the top level class for download sites elements.
 *         		Each element is expected to have at least these
 *         		characteristics.
 *         	
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getSummary <em>Summary</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getDownloadSiteElement()
 * @model extendedMetaData="name='downloadSiteElement' kind='elementOnly'"
 * @generated
 */
public interface DownloadSiteElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getDownloadSiteElement_Description()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getDownloadSiteElement_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Summary</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Summary</em>' attribute.
	 * @see #setSummary(String)
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getDownloadSiteElement_Summary()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='summary'"
	 * @generated
	 */
	String getSummary();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getSummary <em>Summary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Summary</em>' attribute.
	 * @see #getSummary()
	 * @generated
	 */
	void setSummary(String value);

} // DownloadSiteElement
