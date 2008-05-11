/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.espace.resources.core;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 *
 */
public class IndexPair {
	
	private EObject object;
	private String featureName;
	
	public IndexPair(EObject object, String featureName) {
		this.object = object;
		this.featureName = featureName;
	}
	
	/**
     * @return the object
     */
    public EObject getObject() {
	    return object;
    }
    
    /**
     * @return the featureName
     */
    public String getFeatureName() {
	    return featureName;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (object.hashCode() << 8) ^ featureName.hashCode();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
    	IndexPair pair = (IndexPair)obj;
    	return featureName.equals(pair.featureName) &&
    					object.equals(pair.object);
    }

}
