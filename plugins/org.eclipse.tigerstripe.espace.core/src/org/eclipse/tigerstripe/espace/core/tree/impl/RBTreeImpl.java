/**
 * <copyright>
 * </copyright>
 *
 * $Id: RBTreeImpl.java,v 1.1 2008/05/11 12:41:45 ystrot Exp $
 */
package org.eclipse.tigerstripe.espace.core.tree.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.tigerstripe.espace.core.tree.RBNode;
import org.eclipse.tigerstripe.espace.core.tree.RBTree;
import org.eclipse.tigerstripe.espace.core.tree.TreeFactory;
import org.eclipse.tigerstripe.espace.core.tree.TreePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>RB Tree</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBTreeImpl#getFeature <em>Feature</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBTreeImpl#getSize <em>Size</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBTreeImpl#getModCount <em>Mod Count</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.espace.core.tree.impl.RBTreeImpl#getRoot <em>Root</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RBTreeImpl extends EObjectImpl implements RBTree {
    /**
     * The cached value of the '{@link #getFeature() <em>Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeature()
     * @generated
     * @ordered
     */
    protected EStructuralFeature feature;

    /**
     * The default value of the '{@link #getSize() <em>Size</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSize()
     * @generated
     * @ordered
     */
    protected static final int SIZE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getSize() <em>Size</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSize()
     * @generated
     * @ordered
     */
    protected int size = SIZE_EDEFAULT;

    /**
     * The default value of the '{@link #getModCount() <em>Mod Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModCount()
     * @generated
     * @ordered
     */
    protected static final int MOD_COUNT_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getModCount() <em>Mod Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModCount()
     * @generated
     * @ordered
     */
    protected int modCount = MOD_COUNT_EDEFAULT;

    /**
     * The cached value of the '{@link #getRoot() <em>Root</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRoot()
     * @generated
     * @ordered
     */
    protected RBNode root;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RBTreeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return TreePackage.Literals.RB_TREE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EStructuralFeature getFeature() {
        if (feature != null && feature.eIsProxy()) {
            InternalEObject oldFeature = (InternalEObject)feature;
            feature = (EStructuralFeature)eResolveProxy(oldFeature);
            if (feature != oldFeature) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, TreePackage.RB_TREE__FEATURE, oldFeature, feature));
            }
        }
        return feature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EStructuralFeature basicGetFeature() {
        return feature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFeature(EStructuralFeature newFeature) {
        EStructuralFeature oldFeature = feature;
        feature = newFeature;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TreePackage.RB_TREE__FEATURE, oldFeature, feature));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getSize() {
        return size;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSize(int newSize) {
        int oldSize = size;
        size = newSize;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TreePackage.RB_TREE__SIZE, oldSize, size));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getModCount() {
        return modCount;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setModCount(int newModCount) {
        int oldModCount = modCount;
        modCount = newModCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TreePackage.RB_TREE__MOD_COUNT, oldModCount, modCount));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RBNode getRoot() {
        return root;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRoot(RBNode newRoot, NotificationChain msgs) {
        RBNode oldRoot = root;
        root = newRoot;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TreePackage.RB_TREE__ROOT, oldRoot, newRoot);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRoot(RBNode newRoot) {
        if (newRoot != root) {
            NotificationChain msgs = null;
            if (root != null)
                msgs = ((InternalEObject)root).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TreePackage.RB_TREE__ROOT, null, msgs);
            if (newRoot != null)
                msgs = ((InternalEObject)newRoot).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TreePackage.RB_TREE__ROOT, null, msgs);
            msgs = basicSetRoot(newRoot, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TreePackage.RB_TREE__ROOT, newRoot, newRoot));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case TreePackage.RB_TREE__ROOT:
                return basicSetRoot(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case TreePackage.RB_TREE__FEATURE:
                if (resolve) return getFeature();
                return basicGetFeature();
            case TreePackage.RB_TREE__SIZE:
                return new Integer(getSize());
            case TreePackage.RB_TREE__MOD_COUNT:
                return new Integer(getModCount());
            case TreePackage.RB_TREE__ROOT:
                return getRoot();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case TreePackage.RB_TREE__FEATURE:
                setFeature((EStructuralFeature)newValue);
                return;
            case TreePackage.RB_TREE__SIZE:
                setSize(((Integer)newValue).intValue());
                return;
            case TreePackage.RB_TREE__MOD_COUNT:
                setModCount(((Integer)newValue).intValue());
                return;
            case TreePackage.RB_TREE__ROOT:
                setRoot((RBNode)newValue);
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
            case TreePackage.RB_TREE__FEATURE:
                setFeature((EStructuralFeature)null);
                return;
            case TreePackage.RB_TREE__SIZE:
                setSize(SIZE_EDEFAULT);
                return;
            case TreePackage.RB_TREE__MOD_COUNT:
                setModCount(MOD_COUNT_EDEFAULT);
                return;
            case TreePackage.RB_TREE__ROOT:
                setRoot((RBNode)null);
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
            case TreePackage.RB_TREE__FEATURE:
                return feature != null;
            case TreePackage.RB_TREE__SIZE:
                return size != SIZE_EDEFAULT;
            case TreePackage.RB_TREE__MOD_COUNT:
                return modCount != MOD_COUNT_EDEFAULT;
            case TreePackage.RB_TREE__ROOT:
                return root != null;
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
        result.append(" (size: ");
        result.append(size);
        result.append(", modCount: ");
        result.append(modCount);
        result.append(')');
        return result.toString();
    }

	public void clear() {
        modCount++;
        size = 0;
        root = null;
    }

	public EObject[] find(Object key) {
        RBNode p = getEntry(key);
        return p == null ? EPMTY_ARRAY : p.getObjects().toArray(
        		new EObject[p.getObjects().size()]);
    }

	public void insert(EObject item) {
		insert(getKey(item), item);
    }

    private void incrementSize()   { modCount++; size++; }
    private void decrementSize()   { modCount++; size--; }

	public void insert(Object key, EObject item) {
        RBNode t = root;

        if (t == null) {
            incrementSize();
            root = TreeFactory.eINSTANCE.createRBNode();
            root.getObjects().add(item);
            return;
       }

        while (true) {
            int cmp = compare(key, geNodeKey(t));
            if (cmp == 0) {
                t.getObjects().add(item);
                return;
            } else if (cmp < 0) {
                if (t.getLeft() != null) {
                    t = t.getLeft();
                } else {
                    incrementSize();
                    RBNode newNode = TreeFactory.eINSTANCE.createRBNode();
                    newNode.getObjects().add(item);
                    //newNode.setParent(t);
                    t.setLeft(newNode);
                    fixAfterInsertion(newNode);
                }
            } else { // cmp > 0
                if (t.getRight() != null) {
                    t = t.getRight();
                } else {
                    incrementSize();
                    
                    RBNode newNode = TreeFactory.eINSTANCE.createRBNode();
                    newNode.getObjects().add(item);
                    //newNode.setParent(t);
                    t.setRight(newNode);
                    fixAfterInsertion(newNode);
                }
            }
        }
    }

	public boolean isEmpty() {
	    return getSize() == 0;
    }

    /**
     * Removes the mapping for this key from this TreeMap if present.
     *
     * @param  key key for which mapping should be removed
     * @return previous value associated with specified key, or <tt>null</tt>
     *         if there was no mapping for key.  A <tt>null</tt> return can
     *         also indicate that the map previously associated
     *         <tt>null</tt> with the specified key.
     *
     * @throws    ClassCastException key cannot be compared with the keys
     *            currently in the map.
     * @throws NullPointerException key is <tt>null</tt> and this map uses
     *         natural order, or its comparator does not tolerate
     *         <tt>null</tt> keys.
     */
    public void remove(Object key, EObject object) {
    	RBNode p = getEntry(key);
        if (p == null)
            return;

        deleteEntry(p, object);
    }

	public void remove(EObject item) {
		remove(getKey(item), item);
    }

    /**
     * Returns this map's entry for the given key, or <tt>null</tt> if the map
     * does not contain an entry for the key.
     *
     * @return this map's entry for the given key, or <tt>null</tt> if the map
     *                does not contain an entry for the key.
     * @throws ClassCastException if the key cannot be compared with the keys
     *                  currently in the map.
     * @throws NullPointerException key is <tt>null</tt> and this map uses
     *                  natural order, or its comparator does not tolerate *
     *                  <tt>null</tt> keys.
     */
    private RBNode getEntry(Object key) {
    	RBNode p = root;
        while (p != null) {
            int cmp = compare(key, geNodeKey(p));
            if (cmp == 0)
                return p;
            else if (cmp < 0)
                p = p.getLeft();
            else
                p = p.getRight();
        }
        return null;
    }
	
	private Object getKey(EObject item) {
		return item.eGet(getFeature());
	}
	
	private Object geNodeKey(RBNode item) {
		EObject object = item.getObjects().get(0);
		return getKey(object);
	}
	
	@SuppressWarnings("unchecked")
    private int compare(Object key1, Object key2) {
		if (key1 instanceof Comparable) {
			return ((Comparable)key1).compareTo(key2);
		}
		String s1 = key1 == null ? "" : key1.toString();
		String s2 = key2 == null ? "" : key2.toString();
		return s1.compareTo(s2);
	}
    /**
     * Delete node p, and then rebalance the tree.
     */

    private void deleteEntry(RBNode p, EObject object) {
    	p.getObjects().remove(object);
        decrementSize();
        
        if (p.getObjects().size() == 0) {

            // If strictly internal, copy successor's element to p and then make p
            // point to successor.
            if (p.getLeft() != null && p.getRight() != null) {
            	RBNode s = successor (p);
                p.getObjects().addAll(s.getObjects());
                p = s;
            } // p has 2 children

            // Start fixup at replacement node, if it exists.
            RBNode replacement = (p.getLeft() != null ? p.getLeft() : p.getRight());

            if (replacement != null) {
                // Link replacement to parent
                //replacement.setParent(p.getParent());
                if (p.getParent() == null)
                    root = replacement;
                else if (p == p.getParent().getLeft())
                    p.getParent().setLeft(replacement);
                else
                    p.getParent().setRight(replacement);

                // Null out links so they are OK to use by fixAfterDeletion.
                p.setLeft(null);
                p.setRight(null);
                //p.setParent(null);

                // Fix replacement
                if (p.getColor() == RBNode.BLACK)
                    fixAfterDeletion(replacement);
            } else if (p.getParent() == null) { // return if we are the only node.
                root = null;
            } else { //  No children. Use self as phantom replacement and unlink.
                if (p.getColor() == RBNode.BLACK)
                    fixAfterDeletion(p);

                if (p.getParent() != null) {
                    if (p == p.getParent().getLeft())
                        p.getParent().setLeft(null);
                    else if (p == p.getParent().getRight())
                        p.getParent().setRight(null);
                    p.setParent(null);
                }
            }
        }
    }
    /** From CLR **/
    private void fixAfterDeletion(RBNode x) {
        while (x != root && colorOf(x) == RBNode.BLACK) {
            if (x == leftOf(parentOf(x))) {
            	RBNode sib = rightOf(parentOf(x));

                if (colorOf(sib) == RBNode.RED) {
                    setColor(sib, RBNode.BLACK);
                    setColor(parentOf(x), RBNode.RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib))  == RBNode.BLACK &&
                    colorOf(rightOf(sib)) == RBNode.BLACK) {
                    setColor(sib,  RBNode.RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == RBNode.BLACK) {
                        setColor(leftOf(sib), RBNode.BLACK);
                        setColor(sib, RBNode.RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(rightOf(sib), RBNode.BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric
            	RBNode sib = leftOf(parentOf(x));

                if (colorOf(sib) == RBNode.RED) {
                    setColor(sib, RBNode.BLACK);
                    setColor(parentOf(x), RBNode.RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == RBNode.BLACK &&
                    colorOf(leftOf(sib)) == RBNode.BLACK) {
                    setColor(sib,  RBNode.RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == RBNode.BLACK) {
                        setColor(rightOf(sib), RBNode.BLACK);
                        setColor(sib, RBNode.RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(leftOf(sib), RBNode.BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, RBNode.BLACK);
    }

    /**
     * Returns the successor of the specified Entry, or null if no such.
     */
    private RBNode successor(RBNode t) {
        if (t == null)
            return null;
        else if (t.getRight() != null) {
        	RBNode p = t.getRight();
            while (p.getLeft() != null)
                p = p.getLeft();
            return p;
        } else {
        	RBNode p = t.getParent();
        	RBNode ch = t;
            while (p != null && ch == p.getRight()) {
                ch = p;
                p = p.getParent();
            }
            return p;
        }
    }

    /**
     * Balancing operations.
     *
     * Implementations of rebalancings during insertion and deletion are
     * slightly different than the CLR version.  Rather than using dummy
     * nilnodes, we use a set of accessors that deal properly with null.  They
     * are used to avoid messiness surrounding nullness checks in the main
     * algorithms.
     */

    private static int colorOf(RBNode p) {
        return (p == null ? RBNode.BLACK : p.getColor());
    }

    /** From CLR **/
    private void fixAfterInsertion(RBNode x) {
    	x.setColor(RBNode.RED);

        while (x != null && x != root && x.getParent().getColor() == RBNode.RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
            	RBNode y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RBNode.RED) {
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(y, RBNode.BLACK);
                    setColor(parentOf(parentOf(x)), RBNode.RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(parentOf(parentOf(x)), RBNode.RED);
                    if (parentOf(parentOf(x)) != null)
                        rotateRight(parentOf(parentOf(x)));
                }
            } else {
            	RBNode y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RBNode.RED) {
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(y, RBNode.BLACK);
                    setColor(parentOf(parentOf(x)), RBNode.RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), RBNode.BLACK);
                    setColor(parentOf(parentOf(x)), RBNode.RED);
                    if (parentOf(parentOf(x)) != null)
                        rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.setColor(RBNode.BLACK);
    }

    private static void setColor(RBNode p, int c) {
        if (p != null)
        	p.setColor(c);
    }

    private static RBNode leftOf(RBNode p) {
        return (p == null) ? null: p.getLeft();
    }

    private static RBNode rightOf(RBNode p) {
        return (p == null) ? null: p.getRight();
    }

    /** From CLR **/
    private void rotateLeft(RBNode p) {
    	RBNode r = p.getRight();
        p.setRight(r.getLeft());
//        if (r.getLeft() != null)
//            r.getLeft().setParent(p);
        //r.setParent(p.getParent());
        if (p.getParent() == null)
            root = r;
        else if (p.getParent().getLeft() == p)
            p.getParent().setLeft(r);
        else
            p.getParent().setRight(r);
        r.setLeft(p);
        //p.setParent(r);
    }

    /** From CLR **/
    private void rotateRight(RBNode p) {
    	RBNode l = p.getLeft();
        p.setLeft(l.getRight());
        //if (l.getRight() != null) l.getRight().setParent(p);
        //l.setParent(p.getParent());
        if (p.getParent() == null)
            root = l;
        else if (p.getParent().getRight() == p)
            p.getParent().setRight(l);
        else p.getParent().setLeft(l);
        l.setRight(p);
        //p.setParent(l);
    }

    private static RBNode parentOf(RBNode p) {
        return (p == null ? null: p.getParent());
    }

} //RBTreeImpl
