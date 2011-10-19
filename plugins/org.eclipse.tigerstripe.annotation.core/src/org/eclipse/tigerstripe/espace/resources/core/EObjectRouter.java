package org.eclipse.tigerstripe.espace.resources.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.annotation.core.Annotation;

/**
 * This interface used to map object to corresponding storage.
 * It can be implemented by the user and registered with the
 * <code>org.eclipse.tigerstripe.annotation.core.router</code>
 * extension point.
 * 
 * @author Yuri Strot
 */
public interface EObjectRouter {
	
	/**
	 * Route object to some Path. If this object can not be routed, this
	 * method shall return null. This path will be used to get resource. 
	 */
	public IResource route(Annotation object);

}
