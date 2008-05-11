/**
 * <copyright>
 * </copyright>
 *
 * $Id: TreePackage.java,v 1.1 2008/05/11 12:41:45 ystrot Exp $
 */
package org.eclipse.tigerstripe.espace.core.tree;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.eclipse.tigerstripe.espace.core.tree.TreeFactory
 * @model kind="package"
 * @generated
 */
public interface TreePackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "tree";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http:///org/eclipse/tigerstripe/espace/core/tree.ecore";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "org.eclipse.tigerstripe.espace.core.tree";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    TreePackage eINSTANCE = org.eclipse.tigerstripe.espace.core.tree.impl.TreePackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.tigerstripe.espace.core.tree.ITree <em>ITree</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.espace.core.tree.ITree
     * @see org.eclipse.tigerstripe.espace.core.tree.impl.TreePackageImpl#getITree()
     * @generated
     */
    int ITREE = 0;

    /**
     * The number of structural features of the '<em>ITree</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITREE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBNodeImpl <em>RB Node</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.espace.core.tree.impl.RBNodeImpl
     * @see org.eclipse.tigerstripe.espace.core.tree.impl.TreePackageImpl#getRBNode()
     * @generated
     */
    int RB_NODE = 1;

    /**
     * The feature id for the '<em><b>Objects</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_NODE__OBJECTS = 0;

    /**
     * The feature id for the '<em><b>Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_NODE__COLOR = 1;

    /**
     * The feature id for the '<em><b>Parent</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_NODE__PARENT = 2;

    /**
     * The feature id for the '<em><b>Child Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_NODE__CHILD_ELEMENTS = 3;

    /**
     * The feature id for the '<em><b>Child Left</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_NODE__CHILD_LEFT = 4;

    /**
     * The number of structural features of the '<em>RB Node</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_NODE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBTreeImpl <em>RB Tree</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.espace.core.tree.impl.RBTreeImpl
     * @see org.eclipse.tigerstripe.espace.core.tree.impl.TreePackageImpl#getRBTree()
     * @generated
     */
    int RB_TREE = 2;

    /**
     * The feature id for the '<em><b>Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_TREE__FEATURE = ITREE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Size</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_TREE__SIZE = ITREE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Mod Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_TREE__MOD_COUNT = ITREE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Root</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_TREE__ROOT = ITREE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>RB Tree</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RB_TREE_FEATURE_COUNT = ITREE_FEATURE_COUNT + 4;

    /**
     * Returns the meta object for class '{@link org.eclipse.tigerstripe.espace.core.tree.ITree <em>ITree</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>ITree</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.ITree
     * @model instanceClass="org.eclipse.tigerstripe.espace.core.tree.ITree"
     * @generated
     */
    EClass getITree();

    /**
     * Returns the meta object for class '{@link org.eclipse.tigerstripe.espace.core.tree.RBNode <em>RB Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>RB Node</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBNode
     * @generated
     */
    EClass getRBNode();

    /**
     * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.espace.core.tree.RBNode#getObjects <em>Objects</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Objects</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBNode#getObjects()
     * @see #getRBNode()
     * @generated
     */
    EReference getRBNode_Objects();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.espace.core.tree.RBNode#getColor <em>Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Color</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBNode#getColor()
     * @see #getRBNode()
     * @generated
     */
    EAttribute getRBNode_Color();

    /**
     * Returns the meta object for the container reference '{@link org.eclipse.tigerstripe.espace.core.tree.RBNode#getParent <em>Parent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Parent</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBNode#getParent()
     * @see #getRBNode()
     * @generated
     */
    EReference getRBNode_Parent();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.espace.core.tree.RBNode#getChildElements <em>Child Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Child Elements</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBNode#getChildElements()
     * @see #getRBNode()
     * @generated
     */
    EReference getRBNode_ChildElements();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.espace.core.tree.RBNode#isChildLeft <em>Child Left</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Child Left</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBNode#isChildLeft()
     * @see #getRBNode()
     * @generated
     */
    EAttribute getRBNode_ChildLeft();

    /**
     * Returns the meta object for class '{@link org.eclipse.tigerstripe.espace.core.tree.RBTree <em>RB Tree</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>RB Tree</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBTree
     * @generated
     */
    EClass getRBTree();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.espace.core.tree.RBTree#getFeature <em>Feature</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Feature</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBTree#getFeature()
     * @see #getRBTree()
     * @generated
     */
    EReference getRBTree_Feature();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.espace.core.tree.RBTree#getSize <em>Size</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Size</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBTree#getSize()
     * @see #getRBTree()
     * @generated
     */
    EAttribute getRBTree_Size();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.espace.core.tree.RBTree#getModCount <em>Mod Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mod Count</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBTree#getModCount()
     * @see #getRBTree()
     * @generated
     */
    EAttribute getRBTree_ModCount();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.espace.core.tree.RBTree#getRoot <em>Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Root</em>'.
     * @see org.eclipse.tigerstripe.espace.core.tree.RBTree#getRoot()
     * @see #getRBTree()
     * @generated
     */
    EReference getRBTree_Root();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    TreeFactory getTreeFactory();

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
         * The meta object literal for the '{@link org.eclipse.tigerstripe.espace.core.tree.ITree <em>ITree</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigerstripe.espace.core.tree.ITree
         * @see org.eclipse.tigerstripe.espace.core.tree.impl.TreePackageImpl#getITree()
         * @generated
         */
        EClass ITREE = eINSTANCE.getITree();

        /**
         * The meta object literal for the '{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBNodeImpl <em>RB Node</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigerstripe.espace.core.tree.impl.RBNodeImpl
         * @see org.eclipse.tigerstripe.espace.core.tree.impl.TreePackageImpl#getRBNode()
         * @generated
         */
        EClass RB_NODE = eINSTANCE.getRBNode();

        /**
         * The meta object literal for the '<em><b>Objects</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RB_NODE__OBJECTS = eINSTANCE.getRBNode_Objects();

        /**
         * The meta object literal for the '<em><b>Color</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RB_NODE__COLOR = eINSTANCE.getRBNode_Color();

        /**
         * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RB_NODE__PARENT = eINSTANCE.getRBNode_Parent();

        /**
         * The meta object literal for the '<em><b>Child Elements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RB_NODE__CHILD_ELEMENTS = eINSTANCE.getRBNode_ChildElements();

        /**
         * The meta object literal for the '<em><b>Child Left</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RB_NODE__CHILD_LEFT = eINSTANCE.getRBNode_ChildLeft();

        /**
         * The meta object literal for the '{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBTreeImpl <em>RB Tree</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigerstripe.espace.core.tree.impl.RBTreeImpl
         * @see org.eclipse.tigerstripe.espace.core.tree.impl.TreePackageImpl#getRBTree()
         * @generated
         */
        EClass RB_TREE = eINSTANCE.getRBTree();

        /**
         * The meta object literal for the '<em><b>Feature</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RB_TREE__FEATURE = eINSTANCE.getRBTree_Feature();

        /**
         * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RB_TREE__SIZE = eINSTANCE.getRBTree_Size();

        /**
         * The meta object literal for the '<em><b>Mod Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RB_TREE__MOD_COUNT = eINSTANCE.getRBTree_ModCount();

        /**
         * The meta object literal for the '<em><b>Root</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RB_TREE__ROOT = eINSTANCE.getRBTree_Root();

    }

} //TreePackage
