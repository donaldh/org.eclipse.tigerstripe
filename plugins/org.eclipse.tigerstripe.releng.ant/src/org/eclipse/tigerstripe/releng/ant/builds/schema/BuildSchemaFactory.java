/**
 * <copyright>
 * </copyright>
 *
 * $Id: BuildSchemaFactory.java,v 1.1 2008/04/03 21:45:31 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.ant.builds.schema;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaPackage
 * @generated
 */
public interface BuildSchemaFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	BuildSchemaFactory eINSTANCE = org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Build</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Build</em>'.
	 * @generated
	 */
	Build createBuild();

	/**
	 * Returns a new object of class '<em>Build Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Build Element</em>'.
	 * @generated
	 */
	BuildElement createBuildElement();

	/**
	 * Returns a new object of class '<em>Builds</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Builds</em>'.
	 * @generated
	 */
	Builds createBuilds();

	/**
	 * Returns a new object of class '<em>Bundle</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Bundle</em>'.
	 * @generated
	 */
	Bundle createBundle();

	/**
	 * Returns a new object of class '<em>Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Component</em>'.
	 * @generated
	 */
	Component createComponent();

	/**
	 * Returns a new object of class '<em>Dependency</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Dependency</em>'.
	 * @generated
	 */
	Dependency createDependency();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	BuildSchemaPackage getBuildSchemaPackage();

} //BuildSchemaFactory
