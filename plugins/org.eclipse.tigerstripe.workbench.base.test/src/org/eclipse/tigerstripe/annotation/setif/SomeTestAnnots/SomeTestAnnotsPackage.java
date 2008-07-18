/**
 * <copyright>
 * </copyright>
 *
 * $Id: SomeTestAnnotsPackage.java,v 1.1 2008/07/18 01:07:18 jworrell Exp $
 */
package org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.SomeTestAnnotsFactory
 * @model kind="package"
 * @generated
 */
public interface SomeTestAnnotsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "SomeTestAnnots";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://org/eclipse/tigerstripe/annotation/testAnnots.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.tigerstripe.annotation.testAnnots";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SomeTestAnnotsPackage eINSTANCE = org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.SomeTestAnnotsPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot1Impl <em>Test Annot1</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot1Impl
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.SomeTestAnnotsPackageImpl#getTestAnnot1()
	 * @generated
	 */
	int TEST_ANNOT1 = 0;

	/**
	 * The feature id for the '<em><b>Twine</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ANNOT1__TWINE = 0;

	/**
	 * The number of structural features of the '<em>Test Annot1</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ANNOT1_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot2Impl <em>Test Annot2</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot2Impl
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.SomeTestAnnotsPackageImpl#getTestAnnot2()
	 * @generated
	 */
	int TEST_ANNOT2 = 1;

	/**
	 * The number of structural features of the '<em>Test Annot2</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ANNOT2_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot3Impl <em>Test Annot3</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot3Impl
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.SomeTestAnnotsPackageImpl#getTestAnnot3()
	 * @generated
	 */
	int TEST_ANNOT3 = 2;

	/**
	 * The feature id for the '<em><b>N</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ANNOT3__N = 0;

	/**
	 * The number of structural features of the '<em>Test Annot3</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ANNOT3_FEATURE_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1 <em>Test Annot1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Test Annot1</em>'.
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1
	 * @generated
	 */
	EClass getTestAnnot1();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1#getTwine <em>Twine</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Twine</em>'.
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1#getTwine()
	 * @see #getTestAnnot1()
	 * @generated
	 */
	EAttribute getTestAnnot1_Twine();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot2 <em>Test Annot2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Test Annot2</em>'.
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot2
	 * @generated
	 */
	EClass getTestAnnot2();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot3 <em>Test Annot3</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Test Annot3</em>'.
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot3
	 * @generated
	 */
	EClass getTestAnnot3();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot3#getN <em>N</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>N</em>'.
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot3#getN()
	 * @see #getTestAnnot3()
	 * @generated
	 */
	EAttribute getTestAnnot3_N();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SomeTestAnnotsFactory getSomeTestAnnotsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot1Impl <em>Test Annot1</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot1Impl
		 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.SomeTestAnnotsPackageImpl#getTestAnnot1()
		 * @generated
		 */
		EClass TEST_ANNOT1 = eINSTANCE.getTestAnnot1();

		/**
		 * The meta object literal for the '<em><b>Twine</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_ANNOT1__TWINE = eINSTANCE.getTestAnnot1_Twine();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot2Impl <em>Test Annot2</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot2Impl
		 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.SomeTestAnnotsPackageImpl#getTestAnnot2()
		 * @generated
		 */
		EClass TEST_ANNOT2 = eINSTANCE.getTestAnnot2();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot3Impl <em>Test Annot3</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot3Impl
		 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.SomeTestAnnotsPackageImpl#getTestAnnot3()
		 * @generated
		 */
		EClass TEST_ANNOT3 = eINSTANCE.getTestAnnot3();

		/**
		 * The meta object literal for the '<em><b>N</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_ANNOT3__N = eINSTANCE.getTestAnnot3_N();

	}

} //SomeTestAnnotsPackage
