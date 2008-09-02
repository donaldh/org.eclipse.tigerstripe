/**
 * <copyright>
 * </copyright>
 *
 * $Id: CorePackage.java,v 1.1 2008/09/02 12:07:36 ystrot Exp $
 */
package org.eclipse.tigerstripe.espace.core;

import org.eclipse.emf.ecore.EEnum;
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
 * @see org.eclipse.tigerstripe.espace.core.CoreFactory
 * @model kind="package"
 * @generated
 */
public interface CorePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "core";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/tigerstripe/espace/core.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.tigerstripe.espace.core";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CorePackage eINSTANCE = org.eclipse.tigerstripe.espace.core.impl.CorePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.espace.core.ReadWriteOption <em>Read Write Option</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.espace.core.ReadWriteOption
	 * @see org.eclipse.tigerstripe.espace.core.impl.CorePackageImpl#getReadWriteOption()
	 * @generated
	 */
	int READ_WRITE_OPTION = 0;


	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.espace.core.ReadWriteOption <em>Read Write Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Read Write Option</em>'.
	 * @see org.eclipse.tigerstripe.espace.core.ReadWriteOption
	 * @generated
	 */
	EEnum getReadWriteOption();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CoreFactory getCoreFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.espace.core.ReadWriteOption <em>Read Write Option</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.espace.core.ReadWriteOption
		 * @see org.eclipse.tigerstripe.espace.core.impl.CorePackageImpl#getReadWriteOption()
		 * @generated
		 */
		EEnum READ_WRITE_OPTION = eINSTANCE.getReadWriteOption();

	}

} //CorePackage
