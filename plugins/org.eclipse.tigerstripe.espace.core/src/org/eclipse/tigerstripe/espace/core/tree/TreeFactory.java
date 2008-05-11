/**
 * <copyright>
 * </copyright>
 *
 * $Id: TreeFactory.java,v 1.1 2008/05/11 12:41:45 ystrot Exp $
 */
package org.eclipse.tigerstripe.espace.core.tree;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.espace.core.tree.TreePackage
 * @generated
 */
public interface TreeFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    TreeFactory eINSTANCE = org.eclipse.tigerstripe.espace.core.tree.impl.TreeFactoryImpl.init();

    /**
     * Returns a new object of class '<em>RB Node</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>RB Node</em>'.
     * @generated
     */
    RBNode createRBNode();

    /**
     * Returns a new object of class '<em>RB Tree</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>RB Tree</em>'.
     * @generated
     */
    RBTree createRBTree();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    TreePackage getTreePackage();

} //TreeFactory
