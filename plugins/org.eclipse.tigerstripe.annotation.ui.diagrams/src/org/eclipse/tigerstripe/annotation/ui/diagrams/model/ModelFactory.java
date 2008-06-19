/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelFactory.java,v 1.1 2008/06/19 10:46:01 ystrot Exp $
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
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ModelPackage getModelPackage();

} //ModelFactory
