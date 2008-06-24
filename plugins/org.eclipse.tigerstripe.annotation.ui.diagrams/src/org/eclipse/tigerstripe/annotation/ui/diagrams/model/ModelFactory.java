/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelFactory.java,v 1.2 2008/06/24 09:40:14 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.ModelPackage
 * @generated
 */
public interface ModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelFactory eINSTANCE = org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Annotation Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annotation Node</em>'.
	 * @generated
	 */
	AnnotationNode createAnnotationNode();

	/**
	 * Returns a new object of class '<em>Meta Annotation Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Meta Annotation Node</em>'.
	 * @generated
	 */
	MetaAnnotationNode createMetaAnnotationNode();

	/**
	 * Returns a new object of class '<em>Type Status</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Type Status</em>'.
	 * @generated
	 */
	TypeStatus createTypeStatus();

	/**
	 * Returns a new object of class '<em>View Types Status</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>View Types Status</em>'.
	 * @generated
	 */
	ViewTypesStatus createViewTypesStatus();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ModelPackage getModelPackage();

} //ModelFactory
