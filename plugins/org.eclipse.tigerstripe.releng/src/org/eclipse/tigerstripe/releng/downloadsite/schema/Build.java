/**
 * <copyright>
 * </copyright>
 *
 * $Id: Build.java,v 1.2 2008/04/08 22:22:15 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Build</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *         		A download site hosts a number of builds. Each build is
 *         		uniquely identified with a triplet {stream, type,
 *         		tstamp}. Builds are made of: - components that are in
 *         		turn made of downloadable bundles to users. -
 *         		dependencies that were used for that build - details
 *         		that contain various details about the build.
 *         	
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getComponent <em>Component</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getDependency <em>Dependency</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getDetail <em>Detail</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getStream <em>Stream</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getTstamp <em>Tstamp</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getBuild()
 * @model extendedMetaData="name='build' kind='elementOnly'"
 * @generated
 */
public interface Build extends DownloadSiteElement {
	/**
	 * Returns the value of the '<em><b>Component</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.releng.downloadsite.schema.Component}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getBuild_Component()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='component' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Component> getComponent();

	/**
	 * Returns the value of the '<em><b>Dependency</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.releng.downloadsite.schema.Dependency}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependency</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependency</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getBuild_Dependency()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='dependency' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Dependency> getDependency();

	/**
	 * Returns the value of the '<em><b>Detail</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.releng.downloadsite.schema.Detail}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Detail</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Detail</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getBuild_Detail()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='detail' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Detail> getDetail();

	/**
	 * Returns the value of the '<em><b>Stream</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Stream</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stream</em>' attribute.
	 * @see #setStream(String)
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getBuild_Stream()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='stream'"
	 * @generated
	 */
	String getStream();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getStream <em>Stream</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Stream</em>' attribute.
	 * @see #getStream()
	 * @generated
	 */
	void setStream(String value);

	/**
	 * Returns the value of the '<em><b>Tstamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tstamp</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tstamp</em>' attribute.
	 * @see #setTstamp(String)
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getBuild_Tstamp()
	 * @model dataType="org.eclipse.tigerstripe.releng.downloadsite.schema.TStamp"
	 *        extendedMetaData="kind='attribute' name='tstamp'"
	 * @generated
	 */
	String getTstamp();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getTstamp <em>Tstamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tstamp</em>' attribute.
	 * @see #getTstamp()
	 * @generated
	 */
	void setTstamp(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The default value is <code>"R"</code>.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(BuildType)
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#getBuild_Type()
	 * @model default="R" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='type'"
	 * @generated
	 */
	BuildType getType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	void setType(BuildType value);

	/**
	 * Unsets the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(BuildType)
	 * @generated
	 */
	void unsetType();

	/**
	 * Returns whether the value of the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getType <em>Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(BuildType)
	 * @generated
	 */
	boolean isSetType();

} // Build
