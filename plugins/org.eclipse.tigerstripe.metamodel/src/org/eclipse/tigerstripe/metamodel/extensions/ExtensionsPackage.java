/**
 * <copyright>
 * </copyright>
 *
 * $Id: ExtensionsPackage.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.eclipse.tigerstripe.metamodel.extensions.ExtensionsFactory
 * @model kind="package"
 * @generated
 */
public interface ExtensionsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "extensions";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "tigerstripe-extensions";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ts-extensions";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ExtensionsPackage eINSTANCE = org.eclipse.tigerstripe.metamodel.extensions.impl.ExtensionsPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.impl.IStandardSpecificsImpl <em>IStandard Specifics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.IStandardSpecificsImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.ExtensionsPackageImpl#getIStandardSpecifics()
	 * @generated
	 */
	int ISTANDARD_SPECIFICS = 0;

	/**
	 * The number of structural features of the '<em>IStandard Specifics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISTANDARD_SPECIFICS_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.impl.IPropertiesImpl <em>IProperties</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.IPropertiesImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.ExtensionsPackageImpl#getIProperties()
	 * @generated
	 */
	int IPROPERTIES = 1;

	/**
	 * The feature id for the '<em><b>Entries</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPROPERTIES__ENTRIES = 0;

	/**
	 * The number of structural features of the '<em>IProperties</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPROPERTIES_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.impl.IPropertyImpl <em>IProperty</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.IPropertyImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.ExtensionsPackageImpl#getIProperty()
	 * @generated
	 */
	int IPROPERTY = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPROPERTY__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPROPERTY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>IProperty</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPROPERTY_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.IStandardSpecifics <em>IStandard Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IStandard Specifics</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.IStandardSpecifics
	 * @generated
	 */
	EClass getIStandardSpecifics();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.IProperties <em>IProperties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IProperties</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.IProperties
	 * @generated
	 */
	EClass getIProperties();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.extensions.IProperties#getEntries <em>Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entries</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.IProperties#getEntries()
	 * @see #getIProperties()
	 * @generated
	 */
	EReference getIProperties_Entries();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.IProperty <em>IProperty</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IProperty</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.IProperty
	 * @generated
	 */
	EClass getIProperty();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.IProperty#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.IProperty#getName()
	 * @see #getIProperty()
	 * @generated
	 */
	EAttribute getIProperty_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.IProperty#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.IProperty#getValue()
	 * @see #getIProperty()
	 * @generated
	 */
	EAttribute getIProperty_Value();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ExtensionsFactory getExtensionsFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.impl.IStandardSpecificsImpl <em>IStandard Specifics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.IStandardSpecificsImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.ExtensionsPackageImpl#getIStandardSpecifics()
		 * @generated
		 */
		EClass ISTANDARD_SPECIFICS = eINSTANCE.getIStandardSpecifics();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.impl.IPropertiesImpl <em>IProperties</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.IPropertiesImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.ExtensionsPackageImpl#getIProperties()
		 * @generated
		 */
		EClass IPROPERTIES = eINSTANCE.getIProperties();

		/**
		 * The meta object literal for the '<em><b>Entries</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IPROPERTIES__ENTRIES = eINSTANCE.getIProperties_Entries();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.impl.IPropertyImpl <em>IProperty</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.IPropertyImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.impl.ExtensionsPackageImpl#getIProperty()
		 * @generated
		 */
		EClass IPROPERTY = eINSTANCE.getIProperty();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IPROPERTY__NAME = eINSTANCE.getIProperty_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IPROPERTY__VALUE = eINSTANCE.getIProperty_Value();

	}

} //ExtensionsPackage
