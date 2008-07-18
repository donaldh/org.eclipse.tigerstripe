/**
 * <copyright>
 * </copyright>
 *
 * $Id: SomeTestAnnotsFactory.java,v 1.1 2008/07/18 01:07:18 jworrell Exp $
 */
package org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.SomeTestAnnotsPackage
 * @generated
 */
public interface SomeTestAnnotsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SomeTestAnnotsFactory eINSTANCE = org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.SomeTestAnnotsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Test Annot1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Test Annot1</em>'.
	 * @generated
	 */
	TestAnnot1 createTestAnnot1();

	/**
	 * Returns a new object of class '<em>Test Annot2</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Test Annot2</em>'.
	 * @generated
	 */
	TestAnnot2 createTestAnnot2();

	/**
	 * Returns a new object of class '<em>Test Annot3</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Test Annot3</em>'.
	 * @generated
	 */
	TestAnnot3 createTestAnnot3();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SomeTestAnnotsPackage getSomeTestAnnotsPackage();

} //SomeTestAnnotsFactory
