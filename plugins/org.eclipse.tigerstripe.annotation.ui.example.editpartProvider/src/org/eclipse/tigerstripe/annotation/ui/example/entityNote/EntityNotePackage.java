/**
 * <copyright>
 * </copyright>
 *
 * $Id: EntityNotePackage.java,v 1.1 2008/07/01 08:49:22 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.example.entityNote;

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
 * @see org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNoteFactory
 * @model kind="package"
 * @generated
 */
public interface EntityNotePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "entityNote";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/tigerstripe/annotation/ui/example/entityNote.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.tigerstripe.annotation.ui.example.entityNote";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EntityNotePackage eINSTANCE = org.eclipse.tigerstripe.annotation.ui.example.entityNote.impl.EntityNotePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.ui.example.entityNote.impl.EntityNoteImpl <em>Entity Note</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.ui.example.entityNote.impl.EntityNoteImpl
	 * @see org.eclipse.tigerstripe.annotation.ui.example.entityNote.impl.EntityNotePackageImpl#getEntityNote()
	 * @generated
	 */
	int ENTITY_NOTE = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_NOTE__DESCRIPTION = 0;

	/**
	 * The number of structural features of the '<em>Entity Note</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_NOTE_FEATURE_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNote <em>Entity Note</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Note</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNote
	 * @generated
	 */
	EClass getEntityNote();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNote#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNote#getDescription()
	 * @see #getEntityNote()
	 * @generated
	 */
	EAttribute getEntityNote_Description();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EntityNoteFactory getEntityNoteFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.ui.example.entityNote.impl.EntityNoteImpl <em>Entity Note</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.ui.example.entityNote.impl.EntityNoteImpl
		 * @see org.eclipse.tigerstripe.annotation.ui.example.entityNote.impl.EntityNotePackageImpl#getEntityNote()
		 * @generated
		 */
		EClass ENTITY_NOTE = eINSTANCE.getEntityNote();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY_NOTE__DESCRIPTION = eINSTANCE.getEntityNote_Description();

	}

} //EntityNotePackage
