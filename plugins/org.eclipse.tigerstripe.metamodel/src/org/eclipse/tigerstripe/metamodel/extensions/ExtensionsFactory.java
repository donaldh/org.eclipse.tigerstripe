/**
 * <copyright>
 * </copyright>
 *
 * $Id: ExtensionsFactory.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.extensions.ExtensionsPackage
 * @generated
 */
public interface ExtensionsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ExtensionsFactory eINSTANCE = org.eclipse.tigerstripe.metamodel.extensions.impl.ExtensionsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>IProperties</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IProperties</em>'.
	 * @generated
	 */
	IProperties createIProperties();

	/**
	 * Returns a new object of class '<em>IProperty</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IProperty</em>'.
	 * @generated
	 */
	IProperty createIProperty();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ExtensionsPackage getExtensionsPackage();

} //ExtensionsFactory
