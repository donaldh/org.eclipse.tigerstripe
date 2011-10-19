package org.eclipse.tigerstripe.annotation.core;

/**
 * Used for filtering annotations during the search. 
 */
public interface Filter {

	boolean apply(Annotation ann);

}
