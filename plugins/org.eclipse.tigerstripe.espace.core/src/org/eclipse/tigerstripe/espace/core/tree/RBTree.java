package org.eclipse.tigerstripe.espace.core.tree;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Yuri Strot
 * @model
 */
public interface RBTree extends EObject, ITree {
	
	/**
	 * @model
	 */
	public EStructuralFeature getFeature();

    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.espace.core.tree.RBTree#getFeature <em>Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature</em>' reference.
     * @see #getFeature()
     * @generated
     */
    void setFeature(EStructuralFeature value);

    /**
     * The number of entries in the tree
     * @model
     */
    public int getSize();

    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.espace.core.tree.RBTree#getSize <em>Size</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Size</em>' attribute.
     * @see #getSize()
     * @generated
     */
    void setSize(int value);

    /**
     * The number of structural modifications to the tree.
     * @model
     */
    public int getModCount();
    
    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.espace.core.tree.RBTree#getModCount <em>Mod Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Mod Count</em>' attribute.
     * @see #getModCount()
     * @generated
     */
    void setModCount(int value);

    /**
     * @model containment="true"
     */
    public RBNode getRoot();
    
    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.espace.core.tree.RBTree#getRoot <em>Root</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Root</em>' containment reference.
     * @see #getRoot()
     * @generated
     */
    void setRoot(RBNode value);

}
