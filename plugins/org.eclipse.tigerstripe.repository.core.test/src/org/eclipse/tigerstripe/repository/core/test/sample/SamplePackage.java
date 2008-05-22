/**
 * <copyright>
 * </copyright>
 *
 * $Id: SamplePackage.java,v 1.1 2008/05/22 17:46:37 edillon Exp $
 */
package org.eclipse.tigerstripe.repository.core.test.sample;

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
 * @see org.eclipse.tigerstripe.repository.core.test.sample.SampleFactory
 * @model kind="package"
 * @generated
 */
public interface SamplePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "sample";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/tigerstripe/repository/sample";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "sm";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SamplePackage eINSTANCE = org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.TopImpl <em>Top</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.TopImpl
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getTop()
	 * @generated
	 */
	int TOP = 0;

	/**
	 * The number of structural features of the '<em>Top</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOP_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.VillageImpl <em>Village</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.VillageImpl
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getVillage()
	 * @generated
	 */
	int VILLAGE = 1;

	/**
	 * The feature id for the '<em><b>Streets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VILLAGE__STREETS = TOP_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VILLAGE__NAME = TOP_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Neighboring Villages</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VILLAGE__NEIGHBORING_VILLAGES = TOP_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Village</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VILLAGE_FEATURE_COUNT = TOP_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.HouseImpl <em>House</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.HouseImpl
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getHouse()
	 * @generated
	 */
	int HOUSE = 2;

	/**
	 * The feature id for the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOUSE__NUMBER = TOP_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Windows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOUSE__WINDOWS = TOP_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>House</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOUSE_FEATURE_COUNT = TOP_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.StreetImpl <em>Street</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.StreetImpl
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getStreet()
	 * @generated
	 */
	int STREET = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STREET__NAME = TOP_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Houses</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STREET__HOUSES = TOP_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Cars</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STREET__CARS = TOP_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cross Streets</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STREET__CROSS_STREETS = TOP_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Street</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STREET_FEATURE_COUNT = TOP_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.CarImpl <em>Car</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.CarImpl
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getCar()
	 * @generated
	 */
	int CAR = 4;

	/**
	 * The feature id for the '<em><b>License Plate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR__LICENSE_PLATE = 0;

	/**
	 * The number of structural features of the '<em>Car</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAR_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.RegionImpl <em>Region</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.RegionImpl
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getRegion()
	 * @generated
	 */
	int REGION = 5;

	/**
	 * The feature id for the '<em><b>Villages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REGION__VILLAGES = TOP_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Next To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REGION__NEXT_TO = TOP_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REGION__NAME = TOP_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Region</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REGION_FEATURE_COUNT = TOP_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.WindowImpl <em>Window</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.WindowImpl
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getWindow()
	 * @generated
	 */
	int WINDOW = 6;

	/**
	 * The feature id for the '<em><b>Direction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDOW__DIRECTION = TOP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Window</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDOW_FEATURE_COUNT = TOP_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.repository.core.test.sample.Top <em>Top</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Top</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Top
	 * @generated
	 */
	EClass getTop();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.repository.core.test.sample.Village <em>Village</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Village</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Village
	 * @generated
	 */
	EClass getVillage();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.repository.core.test.sample.Village#getStreets <em>Streets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Streets</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Village#getStreets()
	 * @see #getVillage()
	 * @generated
	 */
	EReference getVillage_Streets();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.repository.core.test.sample.Village#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Village#getName()
	 * @see #getVillage()
	 * @generated
	 */
	EAttribute getVillage_Name();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.repository.core.test.sample.Village#getNeighboringVillages <em>Neighboring Villages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Neighboring Villages</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Village#getNeighboringVillages()
	 * @see #getVillage()
	 * @generated
	 */
	EReference getVillage_NeighboringVillages();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.repository.core.test.sample.House <em>House</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>House</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.House
	 * @generated
	 */
	EClass getHouse();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.repository.core.test.sample.House#getNumber <em>Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.House#getNumber()
	 * @see #getHouse()
	 * @generated
	 */
	EAttribute getHouse_Number();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.repository.core.test.sample.House#getWindows <em>Windows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Windows</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.House#getWindows()
	 * @see #getHouse()
	 * @generated
	 */
	EReference getHouse_Windows();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.repository.core.test.sample.Street <em>Street</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Street</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Street
	 * @generated
	 */
	EClass getStreet();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.repository.core.test.sample.Street#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Street#getName()
	 * @see #getStreet()
	 * @generated
	 */
	EAttribute getStreet_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.repository.core.test.sample.Street#getHouses <em>Houses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Houses</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Street#getHouses()
	 * @see #getStreet()
	 * @generated
	 */
	EReference getStreet_Houses();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.repository.core.test.sample.Street#getCars <em>Cars</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Cars</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Street#getCars()
	 * @see #getStreet()
	 * @generated
	 */
	EReference getStreet_Cars();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.repository.core.test.sample.Street#getCrossStreets <em>Cross Streets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Cross Streets</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Street#getCrossStreets()
	 * @see #getStreet()
	 * @generated
	 */
	EReference getStreet_CrossStreets();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.repository.core.test.sample.Car <em>Car</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Car</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Car
	 * @generated
	 */
	EClass getCar();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.repository.core.test.sample.Car#getLicensePlate <em>License Plate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>License Plate</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Car#getLicensePlate()
	 * @see #getCar()
	 * @generated
	 */
	EAttribute getCar_LicensePlate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.repository.core.test.sample.Region <em>Region</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Region</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Region
	 * @generated
	 */
	EClass getRegion();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.repository.core.test.sample.Region#getVillages <em>Villages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Villages</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Region#getVillages()
	 * @see #getRegion()
	 * @generated
	 */
	EReference getRegion_Villages();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.repository.core.test.sample.Region#getNextTo <em>Next To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Next To</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Region#getNextTo()
	 * @see #getRegion()
	 * @generated
	 */
	EReference getRegion_NextTo();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.repository.core.test.sample.Region#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Region#getName()
	 * @see #getRegion()
	 * @generated
	 */
	EAttribute getRegion_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.repository.core.test.sample.Window <em>Window</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Window</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Window
	 * @generated
	 */
	EClass getWindow();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.repository.core.test.sample.Window#getDirection <em>Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Direction</em>'.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.Window#getDirection()
	 * @see #getWindow()
	 * @generated
	 */
	EAttribute getWindow_Direction();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SampleFactory getSampleFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.TopImpl <em>Top</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.TopImpl
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getTop()
		 * @generated
		 */
		EClass TOP = eINSTANCE.getTop();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.VillageImpl <em>Village</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.VillageImpl
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getVillage()
		 * @generated
		 */
		EClass VILLAGE = eINSTANCE.getVillage();

		/**
		 * The meta object literal for the '<em><b>Streets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VILLAGE__STREETS = eINSTANCE.getVillage_Streets();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VILLAGE__NAME = eINSTANCE.getVillage_Name();

		/**
		 * The meta object literal for the '<em><b>Neighboring Villages</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VILLAGE__NEIGHBORING_VILLAGES = eINSTANCE.getVillage_NeighboringVillages();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.HouseImpl <em>House</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.HouseImpl
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getHouse()
		 * @generated
		 */
		EClass HOUSE = eINSTANCE.getHouse();

		/**
		 * The meta object literal for the '<em><b>Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HOUSE__NUMBER = eINSTANCE.getHouse_Number();

		/**
		 * The meta object literal for the '<em><b>Windows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HOUSE__WINDOWS = eINSTANCE.getHouse_Windows();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.StreetImpl <em>Street</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.StreetImpl
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getStreet()
		 * @generated
		 */
		EClass STREET = eINSTANCE.getStreet();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STREET__NAME = eINSTANCE.getStreet_Name();

		/**
		 * The meta object literal for the '<em><b>Houses</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STREET__HOUSES = eINSTANCE.getStreet_Houses();

		/**
		 * The meta object literal for the '<em><b>Cars</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STREET__CARS = eINSTANCE.getStreet_Cars();

		/**
		 * The meta object literal for the '<em><b>Cross Streets</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STREET__CROSS_STREETS = eINSTANCE.getStreet_CrossStreets();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.CarImpl <em>Car</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.CarImpl
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getCar()
		 * @generated
		 */
		EClass CAR = eINSTANCE.getCar();

		/**
		 * The meta object literal for the '<em><b>License Plate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CAR__LICENSE_PLATE = eINSTANCE.getCar_LicensePlate();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.RegionImpl <em>Region</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.RegionImpl
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getRegion()
		 * @generated
		 */
		EClass REGION = eINSTANCE.getRegion();

		/**
		 * The meta object literal for the '<em><b>Villages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REGION__VILLAGES = eINSTANCE.getRegion_Villages();

		/**
		 * The meta object literal for the '<em><b>Next To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REGION__NEXT_TO = eINSTANCE.getRegion_NextTo();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REGION__NAME = eINSTANCE.getRegion_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.WindowImpl <em>Window</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.WindowImpl
		 * @see org.eclipse.tigerstripe.repository.core.test.sample.impl.SamplePackageImpl#getWindow()
		 * @generated
		 */
		EClass WINDOW = eINSTANCE.getWindow();

		/**
		 * The meta object literal for the '<em><b>Direction</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WINDOW__DIRECTION = eINSTANCE.getWindow_Direction();

	}

} //SamplePackage
