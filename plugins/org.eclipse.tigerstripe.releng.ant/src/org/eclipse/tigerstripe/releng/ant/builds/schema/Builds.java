/**
 * <copyright>
 * </copyright>
 *
 * $Id: Builds.java,v 1.1 2008/04/03 21:45:32 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.ant.builds.schema;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Builds</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Builds#getGroup <em>Group</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Builds#getBuild <em>Build</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaPackage#getBuilds()
 * @model extendedMetaData="name='builds' kind='elementOnly'"
 * @generated
 */
public interface Builds extends BuildElement {
	/**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaPackage#getBuilds_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:3'"
	 * @generated
	 */
	FeatureMap getGroup();

	/**
	 * Returns the value of the '<em><b>Build</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.releng.ant.builds.schema.Build}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Build</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Build</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaPackage#getBuilds_Build()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='build' namespace='##targetNamespace' group='#group:3'"
	 * @generated
	 */
	EList<Build> getBuild();

} // Builds
