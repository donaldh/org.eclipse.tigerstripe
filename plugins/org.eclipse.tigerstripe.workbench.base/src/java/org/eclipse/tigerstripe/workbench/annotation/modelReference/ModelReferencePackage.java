/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelReferencePackage.java,v 1.2 2011/12/01 15:24:06 verastov Exp $
 */
package org.eclipse.tigerstripe.workbench.annotation.modelReference;

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
 * @see org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReferenceFactory
 * @model kind="package"
 * @generated
 */
public interface ModelReferencePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "modelReference";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/tigerstripe/workbench/annotation/modelreference.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.tigerstripe.workbench.annotation.modelreference";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelReferencePackage eINSTANCE = org.eclipse.tigerstripe.workbench.annotation.modelReference.impl.ModelReferencePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.annotation.modelReference.impl.ModelReferenceImpl <em>Model Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.annotation.modelReference.impl.ModelReferenceImpl
	 * @see org.eclipse.tigerstripe.workbench.annotation.modelReference.impl.ModelReferencePackageImpl#getModelReference()
	 * @generated
	 */
	int MODEL_REFERENCE = 0;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_REFERENCE__URI = 0;

	/**
	 * The number of structural features of the '<em>Model Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_REFERENCE_FEATURE_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference <em>Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Reference</em>'.
	 * @see org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference
	 * @generated
	 */
	EClass getModelReference();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference#getUri()
	 * @see #getModelReference()
	 * @generated
	 */
	EAttribute getModelReference_Uri();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelReferenceFactory getModelReferenceFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.annotation.modelReference.impl.ModelReferenceImpl <em>Model Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.annotation.modelReference.impl.ModelReferenceImpl
		 * @see org.eclipse.tigerstripe.workbench.annotation.modelReference.impl.ModelReferencePackageImpl#getModelReference()
		 * @generated
		 */
		EClass MODEL_REFERENCE = eINSTANCE.getModelReference();

		/**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_REFERENCE__URI = eINSTANCE.getModelReference_Uri();

	}

} //ModelReferencePackage
