/**
 * <copyright>
 * </copyright>
 *
 * $Id: DesignNotesPackage.java,v 1.1 2008/11/05 19:53:22 edillon Exp $
 */
package org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes;

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
 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.DesignNotesFactory
 * @model kind="package"
 * @generated
 */
public interface DesignNotesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "designNotes";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/tigerstripe/samples/eclipseSummit08/annotations/designNotes.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "dn";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DesignNotesPackage eINSTANCE = org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.DesignNotesPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.NoteImpl <em>Note</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.NoteImpl
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.DesignNotesPackageImpl#getNote()
	 * @generated
	 */
	int NOTE = 0;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__TEXT = 0;

	/**
	 * The number of structural features of the '<em>Note</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.TODOImpl <em>TODO</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.TODOImpl
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.DesignNotesPackageImpl#getTODO()
	 * @generated
	 */
	int TODO = 1;

	/**
	 * The feature id for the '<em><b>Hack</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO__HACK = 0;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO__SUMMARY = 1;

	/**
	 * The number of structural features of the '<em>TODO</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TODO_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.Note <em>Note</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Note</em>'.
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.Note
	 * @generated
	 */
	EClass getNote();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.Note#getText <em>Text</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.Note#getText()
	 * @see #getNote()
	 * @generated
	 */
	EAttribute getNote_Text();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO <em>TODO</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>TODO</em>'.
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO
	 * @generated
	 */
	EClass getTODO();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO#isHack <em>Hack</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hack</em>'.
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO#isHack()
	 * @see #getTODO()
	 * @generated
	 */
	EAttribute getTODO_Hack();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO#getSummary <em>Summary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Summary</em>'.
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO#getSummary()
	 * @see #getTODO()
	 * @generated
	 */
	EAttribute getTODO_Summary();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DesignNotesFactory getDesignNotesFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.NoteImpl <em>Note</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.NoteImpl
		 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.DesignNotesPackageImpl#getNote()
		 * @generated
		 */
		EClass NOTE = eINSTANCE.getNote();

		/**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTE__TEXT = eINSTANCE.getNote_Text();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.TODOImpl <em>TODO</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.TODOImpl
		 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.DesignNotesPackageImpl#getTODO()
		 * @generated
		 */
		EClass TODO = eINSTANCE.getTODO();

		/**
		 * The meta object literal for the '<em><b>Hack</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TODO__HACK = eINSTANCE.getTODO_Hack();

		/**
		 * The meta object literal for the '<em><b>Summary</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TODO__SUMMARY = eINSTANCE.getTODO_Summary();

	}

} //DesignNotesPackage
