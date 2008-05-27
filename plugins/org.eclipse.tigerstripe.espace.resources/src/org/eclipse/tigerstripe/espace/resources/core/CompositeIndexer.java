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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 *
 */
public class CompositeIndexer implements IIndexer {
	
	private List<IIndexer> indexers;
	
	public CompositeIndexer() {
		indexers = new ArrayList<IIndexer>();
	}
	
	public void addIndexer(IIndexer indexer) {
		indexers.add(indexer);
	}
	
	public void removeIndexer(IIndexer indexer) {
		indexers.remove(indexer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#addToIndex(org.eclipse.emf.ecore.EObject)
	 */
	public void addToIndex(EObject object) {
		for (IIndexer indexer : indexers)
			indexer.addToIndex(object);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#removeFromIndex(org.eclipse.emf.ecore.EObject)
	 */
	public void removeFromIndex(EObject object) {
		for (IIndexer indexer : indexers)
			indexer.removeFromIndex(object);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#resolve()
	 */
	public void resolve() {
		for (IIndexer indexer : indexers)
			indexer.resolve();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#clear()
	 */
	public void clear() {
		for (IIndexer indexer : indexers)
			indexer.clear();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#save()
	 */
	public void applyChanges() {
		for (IIndexer indexer : indexers)
			indexer.applyChanges();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#save()
	 */
	public void save() {
		for (IIndexer indexer : indexers)
			indexer.save();
	}

}
