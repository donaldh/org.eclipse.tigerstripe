/**
 * <copyright>
 * </copyright>
 *
 * $Id: EntityNoteFactory.java,v 1.1 2008/07/01 08:49:22 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.example.entityNote;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNotePackage
 * @generated
 */
public interface EntityNoteFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EntityNoteFactory eINSTANCE = org.eclipse.tigerstripe.annotation.ui.example.entityNote.impl.EntityNoteFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Entity Note</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity Note</em>'.
	 * @generated
	 */
	EntityNote createEntityNote();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EntityNotePackage getEntityNotePackage();

} //EntityNoteFactory
