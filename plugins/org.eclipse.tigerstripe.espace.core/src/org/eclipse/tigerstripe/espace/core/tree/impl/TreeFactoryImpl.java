/**
 * <copyright>
 * </copyright>
 *
 * $Id: TreeFactoryImpl.java,v 1.1 2008/05/11 12:41:45 ystrot Exp $
 */
package org.eclipse.tigerstripe.espace.core.tree.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.tigerstripe.espace.core.tree.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TreeFactoryImpl extends EFactoryImpl implements TreeFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static TreeFactory init() {
        try {
            TreeFactory theTreeFactory = (TreeFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/tigerstripe/espace/core/tree.ecore"); 
            if (theTreeFactory != null) {
                return theTreeFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new TreeFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TreeFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case TreePackage.RB_NODE: return createRBNode();
            case TreePackage.RB_TREE: return createRBTree();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RBNode createRBNode() {
        RBNodeImpl rbNode = new RBNodeImpl();
        return rbNode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RBTree createRBTree() {
        RBTreeImpl rbTree = new RBTreeImpl();
        return rbTree;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TreePackage getTreePackage() {
        return (TreePackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static TreePackage getPackage() {
        return TreePackage.eINSTANCE;
    }

} //TreeFactoryImpl
