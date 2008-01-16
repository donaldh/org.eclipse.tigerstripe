/**
 * <copyright>
 * </copyright>
 *
 * $Id: ContextFactory.java,v 1.1 2008/01/16 18:14:19 edillon Exp $
 */
package org.eclipse.tigerstripe.annotations.internal.context;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.annotations.internal.context.ContextPackage
 * @generated
 */
public interface ContextFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ContextFactory eINSTANCE = org.eclipse.tigerstripe.annotations.internal.context.impl.ContextFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Annotation Context</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annotation Context</em>'.
	 * @generated
	 */
	AnnotationContext createAnnotationContext();

	/**
	 * Returns a new object of class '<em>Annotation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annotation</em>'.
	 * @generated
	 */
	Annotation createAnnotation();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ContextPackage getContextPackage();

} //ContextFactory
