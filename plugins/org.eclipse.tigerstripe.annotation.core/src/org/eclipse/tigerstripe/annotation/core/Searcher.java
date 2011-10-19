package org.eclipse.tigerstripe.annotation.core;

import java.util.List;

/**
 * Convenient interface for search annotations, scanning the resources in domain
 * This search may use in opened transaction or using findTransactional search 
 * outside a transaction. In this case {@link Searcher} open transaction for you.  
 * 
 * @see IAnnotationManager
 */
public interface Searcher {

	List<Annotation> findTransactional(Filter filter);

	List<Annotation> find(Filter filter);
}
