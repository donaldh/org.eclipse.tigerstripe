package org.eclipse.tigerstripe.espace.core.tree;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model
 */
public interface RBNode extends EObject {
    
    public static final int BLACK = 1;    // Black must be 1
    public static final int RED   = 0;
    
    /**
     * @model
     */
    public boolean isChildLeft();
    
    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.espace.core.tree.RBNode#isChildLeft <em>Child Left</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Child Left</em>' attribute.
     * @see #isChildLeft()
     * @generated
     */
    void setChildLeft(boolean value);

    /**
     * @model
     */
    public EList<EObject> getObjects();
    
    /**
     * @model containment="true" opposite="parent"
     */
    public EList<RBNode> getChildElements();
    
    public RBNode getLeft();
    
    public void setLeft(RBNode left);
    
    public RBNode getRight();
    
    public void setRight(RBNode right);
    
    /**
     * @model opposite="childElements"
     */
    public RBNode getParent();

    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.espace.core.tree.RBNode#getParent <em>Parent</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parent</em>' container reference.
     * @see #getParent()
     * @generated
     */
    void setParent(RBNode value);

    /**
     * @model
     */
    public int getColor();      // Color
    
    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.espace.core.tree.RBNode#getColor <em>Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Color</em>' attribute.
     * @see #getColor()
     * @generated
     */
    void setColor(int value);

}
