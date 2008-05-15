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
import java.util.Iterator;
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
	@Override
	public void addToIndex(EObject object) {
		Iterator<IIndexer> it = indexers.iterator();
		while (it.hasNext()) {
			IIndexer indexer = it.next();
			indexer.addToIndex(object);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#removeFromIndex(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void removeFromIndex(EObject object) {
		Iterator<IIndexer> it = indexers.iterator();
		while (it.hasNext()) {
			IIndexer indexer = it.next();
			indexer.removeFromIndex(object);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#clear()
	 */
	@Override
	public void clear() {
		Iterator<IIndexer> it = indexers.iterator();
		while (it.hasNext()) {
			IIndexer indexer = it.next();
			indexer.clear();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIndexer#save()
	 */
	@Override
	public void save() {
		Iterator<IIndexer> it = indexers.iterator();
		while (it.hasNext()) {
			IIndexer indexer = it.next();
			indexer.save();
		}
	}

}
