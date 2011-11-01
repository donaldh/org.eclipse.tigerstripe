/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelReferenceFactory.java,v 1.1 2011/11/01 11:12:11 asalnik Exp $
 */
package org.eclipse.tigerstripe.workbench.annotation.modelReference;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReferencePackage
 * @generated
 */
public interface ModelReferenceFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelReferenceFactory eINSTANCE = org.eclipse.tigerstripe.workbench.annotation.modelReference.impl.ModelReferenceFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model Reference</em>'.
	 * @generated
	 */
	ModelReference createModelReference();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ModelReferencePackage getModelReferencePackage();

} //ModelReferenceFactory
