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
package org.eclipse.tigerstripe.annotation.core;

/**
 * Interface for listening to annotation changes.
 * <p>
 * This interface may be implemented by clients.
 * </p>
 *
 * @see IAnnotationListener#annotationAdded(Annotation)
 * @see IAnnotationListener#annotationsRemoved(Annotation[])
 * 
 * @author Yuri Strot
 */
public interface IAnnotationListener {
	
	/**
     * Notifies this listener that an annotation was added.
     * 
	 * @param annotation added annotation
	 */
	public void annotationAdded(Annotation annotation);
	
	/**
     * Notifies this listener that annotations was removed.
     * 
	 * @param annotations removed annotations
	 */
	public void annotationsRemoved(Annotation[] annotations);
	
	/**
     * Notifies this listener that annotations was changed.
     * 
	 * @param annotations changed annotations
	 */
	public void annotationsChanged(Annotation[] annotations);
	
	/**
     * Notifies this listener that an annotation was loaded from the storage.
     * 
	 * @param annotation loaded annotation
	 */
	public void annotationsLoaded(Annotation[] annotation);

}
