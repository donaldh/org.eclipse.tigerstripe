/**
 * <copyright>
 * </copyright>
 *
 * $Id: RBNodeImpl.java,v 1.1 2008/05/11 12:41:45 ystrot Exp $
 */
package org.eclipse.tigerstripe.espace.core.tree.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.tigerstripe.espace.core.tree.RBNode;
import org.eclipse.tigerstripe.espace.core.tree.TreePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>RB Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBNodeImpl#getObjects <em>Objects</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBNodeImpl#getColor <em>Color</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBNodeImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBNodeImpl#getChildElements <em>Child Elements</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBNodeImpl#isChildLeft <em>Child Left</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RBNodeImpl extends EObjectImpl implements RBNode {
    /**
     * The cached value of the '{@link #getObjects() <em>Objects</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getObjects()
     * @generated
     * @ordered
     */
    protected EList<EObject> objects;

    /**
     * The default value of the '{@link #getColor() <em>Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getColor()
     * @generated
     * @ordered
     */
    protected static final int COLOR_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getColor() <em>Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getColor()
     * @generated
     * @ordered
     */
    protected int color = COLOR_EDEFAULT;

    /**
     * The cached value of the '{@link #getChildElements() <em>Child Elements</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChildElements()
     * @generated
     * @ordered
     */
    protected EList<RBNode> childElements;

    /**
     * The default value of the '{@link #isChildLeft() <em>Child Left</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isChildLeft()
     * @generated
     * @ordered
     */
    protected static final boolean CHILD_LEFT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isChildLeft() <em>Child Left</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isChildLeft()
     * @generated
     * @ordered
     */
    protected boolean childLeft = CHILD_LEFT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RBNodeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return TreePackage.Literals.RB_NODE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<EObject> getObjects() {
        if (objects == null) {
            objects = new EObjectResolvingEList<EObject>(EObject.class, this, TreePackage.RB_NODE__OBJECTS);
        }
        return objects;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getColor() {
        return color;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setColor(int newColor) {
        int oldColor = color;
        color = newColor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TreePackage.RB_NODE__COLOR, oldColor, color));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RBNode getParent() {
        if (eContainerFeatureID != TreePackage.RB_NODE__PARENT) return null;
        return (RBNode)eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetParent(RBNode newParent, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject)newParent, TreePackage.RB_NODE__PARENT, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setParent(RBNode newParent) {
        if (newParent != eInternalContainer() || (eContainerFeatureID != TreePackage.RB_NODE__PARENT && newParent != null)) {
            if (EcoreUtil.isAncestor(this, newParent))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newParent != null)
                msgs = ((InternalEObject)newParent).eInverseAdd(this, TreePackage.RB_NODE__CHILD_ELEMENTS, RBNode.class, msgs);
            msgs = basicSetParent(newParent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TreePackage.RB_NODE__PARENT, newParent, newParent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<RBNode> getChildElements() {
        if (childElements == null) {
            childElements = new EObjectContainmentWithInverseEList<RBNode>(RBNode.class, this, TreePackage.RB_NODE__CHILD_ELEMENTS, TreePackage.RB_NODE__PARENT);
        }
        return childElements;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isChildLeft() {
        return childLeft;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setChildLeft(boolean newChildLeft) {
        boolean oldChildLeft = childLeft;
        childLeft = newChildLeft;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TreePackage.RB_NODE__CHILD_LEFT, oldChildLeft, childLeft));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case TreePackage.RB_NODE__PARENT:
                if (eInternalContainer() != null)
                    msgs = eBasicRemoveFromContainer(msgs);
                return basicSetParent((RBNode)otherEnd, msgs);
            case TreePackage.RB_NODE__CHILD_ELEMENTS:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildElements()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case TreePackage.RB_NODE__PARENT:
                return basicSetParent(null, msgs);
            case TreePackage.RB_NODE__CHILD_ELEMENTS:
                return ((InternalEList<?>)getChildElements()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (eContainerFeatureID) {
            case TreePackage.RB_NODE__PARENT:
                return eInternalContainer().eInverseRemove(this, TreePackage.RB_NODE__CHILD_ELEMENTS, RBNode.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case TreePackage.RB_NODE__OBJECTS:
                return getObjects();
            case TreePackage.RB_NODE__COLOR:
                return new Integer(getColor());
            case TreePackage.RB_NODE__PARENT:
                return getParent();
            case TreePackage.RB_NODE__CHILD_ELEMENTS:
                return getChildElements();
            case TreePackage.RB_NODE__CHILD_LEFT:
                return isChildLeft() ? Boolean.TRUE : Boolean.FALSE;
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case TreePackage.RB_NODE__OBJECTS:
                getObjects().clear();
                getObjects().addAll((Collection<? extends EObject>)newValue);
                return;
            case TreePackage.RB_NODE__COLOR:
                setColor(((Integer)newValue).intValue());
                return;
            case TreePackage.RB_NODE__PARENT:
                setParent((RBNode)newValue);
                return;
            case TreePackage.RB_NODE__CHILD_ELEMENTS:
                getChildElements().clear();
                getChildElements().addAll((Collection<? extends RBNode>)newValue);
                return;
            case TreePackage.RB_NODE__CHILD_LEFT:
                setChildLeft(((Boolean)newValue).booleanValue());
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case TreePackage.RB_NODE__OBJECTS:
                getObjects().clear();
                return;
            case TreePackage.RB_NODE__COLOR:
                setColor(COLOR_EDEFAULT);
                return;
            case TreePackage.RB_NODE__PARENT:
                setParent((RBNode)null);
                return;
            case TreePackage.RB_NODE__CHILD_ELEMENTS:
                getChildElements().clear();
                return;
            case TreePackage.RB_NODE__CHILD_LEFT:
                setChildLeft(CHILD_LEFT_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case TreePackage.RB_NODE__OBJECTS:
                return objects != null && !objects.isEmpty();
            case TreePackage.RB_NODE__COLOR:
                return color != COLOR_EDEFAULT;
            case TreePackage.RB_NODE__PARENT:
                return getParent() != null;
            case TreePackage.RB_NODE__CHILD_ELEMENTS:
                return childElements != null && !childElements.isEmpty();
            case TreePackage.RB_NODE__CHILD_LEFT:
                return childLeft != CHILD_LEFT_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (color: ");
        result.append(color);
        result.append(", childLeft: ");
        result.append(childLeft);
        result.append(')');
        return result.toString();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.espace.core.tree.RBNode#getLeft()
     */
    public RBNode getLeft() {
    	if (isChildLeft())
    		return getChild(0);
    	else
    		return getChild(1);
    }
    
    private RBNode getChild(int index) {
    	if (getChildElements().size() > index) {
    		return getChildElements().get(index);
    	}
    	return null;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.espace.core.tree.RBNode#getRight()
     */
    public RBNode getRight() {
    	if (isChildLeft())
    		return getChild(1);
    	else
    		return getChild(0);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.espace.core.tree.RBNode#setLeft(org.eclipse.tigerstripe.espace.core.tree.RBNode)
     */
    public void setLeft(RBNode left) {
    	switch (getChildElements().size()) {
            case 0:
        		if (left != null) {
            		getChildElements().add(left);
            		setChildLeft(true);
        		}
    	        break;
            case 1:
            	if (isChildLeft()) {
            		getChildElements().remove(0);
            		if (left != null)
            			getChildElements().add(left);
            	}
            	else {
            		if (left != null)
            			getChildElements().add(left);
            	}
    	        break;
            default:
            	if (isChildLeft()) {
            		getChildElements().remove(0);
            		if (left != null)
            			getChildElements().add(0, left);
            		else {
            			setChildLeft(false);
            		}
            	}
            	else {
            		getChildElements().remove(1);
            		if (left != null)
            			getChildElements().add(left);
            	}
    	        break;
        }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.espace.core.tree.RBNode#setRight(org.eclipse.tigerstripe.espace.core.tree.RBNode)
     */
    public void setRight(RBNode right) {
    	switch (getChildElements().size()) {
            case 0:
        		if (right != null) {
            		getChildElements().add(right);
            		setChildLeft(false);
        		}
    	        break;
            case 1:
            	if (isChildLeft()) {
            		if (right != null)
            			getChildElements().add(right);
            	}
            	else {
            		getChildElements().remove(0);
            		if (right != null)
            			getChildElements().add(right);
            	}
    	        break;
            default:
            	if (isChildLeft()) {
            		getChildElements().remove(1);
            		if (right != null)
            			getChildElements().add(right);
            	}
            	else {
            		getChildElements().remove(0);
            		if (right != null)
            			getChildElements().add(0, right);
            		else {
            			setChildLeft(true);
            		}
            	}
    	        break;
    	}
    }

} //RBNodeImpl
