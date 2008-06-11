/**
 * <copyright>
 * </copyright>
 *
 * $Id: DesignNotesFactory.java,v 1.1 2008/06/11 23:31:30 edillon Exp $
 */
package org.eclipse.tigerstripe.annotation.example.designNotes;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.annotation.example.designNotes.DesignNotesPackage
 * @generated
 */
public interface DesignNotesFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DesignNotesFactory eINSTANCE = org.eclipse.tigerstripe.annotation.example.designNotes.impl.DesignNotesFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Note</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Note</em>'.
	 * @generated
	 */
	Note createNote();

	/**
	 * Returns a new object of class '<em>TODO</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>TODO</em>'.
	 * @generated
	 */
	TODO createTODO();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DesignNotesPackage getDesignNotesPackage();

} //DesignNotesFactory
