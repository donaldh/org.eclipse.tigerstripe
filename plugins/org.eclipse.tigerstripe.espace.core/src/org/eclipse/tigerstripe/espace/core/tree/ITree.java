package org.eclipse.tigerstripe.espace.core.tree;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 *
 */
public interface ITree {
	
	public static EObject[] EPMTY_ARRAY = new EObject[0];
	
    public void insert( EObject item );
	
    public void insert( Object key, EObject item );
    
    public void remove( EObject item );
    
    public void remove( Object key, EObject item );
    
    public EObject[] find( Object key );
    
    public void clear();
    
    public boolean isEmpty();
    
    public int getSize();

}
