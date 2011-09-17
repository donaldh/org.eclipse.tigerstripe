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
package org.eclipse.tigerstripe.espace.resources.internal.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.tigerstripe.espace.core.tree.RBTree;
import org.eclipse.tigerstripe.espace.core.tree.TreeFactory;

/**
 * @author Yuri Strot
 *
 */
public class FeatureIndexer extends AbstractIndexer {
	
	private static final String ANNOTATION_MARKER = "org.eclipse.tigerstripe.annotation";
	private static final String ANNOTATION_INDEX = "indexed";
	
	public FeatureIndexer(IndexStorage storage) {
		super(storage);
	}
	
	public EObject[] read(EStructuralFeature feature, Object value) {
		Resource res = getResource(feature, false);
		if (res != null) {
			RBTree tree = (RBTree)getContainer(res, feature);
			return tree.find(value);
		}
		return EMPTY;
	}
	
	public EObject[] readPostfixes(EStructuralFeature feature, Object value) {
		Resource res = getResource(feature, false);
		if (res != null) {
			RBTree tree = (RBTree)getContainer(res, feature);
			return tree.getPostfixes(value);
		}
		return EMPTY;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#resolve(org.eclipse.emf.ecore.EObject, java.lang.Object)
	 */
	@Override
	protected void resolve(EObject container, Object object) {
		RBTree tree = (RBTree)container;
		EObject eobject = (EObject)object;
		Object value = eobject.eGet(tree.getFeature());
		((RBTree)container).find(value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#getContainer(org.eclipse.emf.ecore.resource.Resource, java.lang.Object)
	 */
	@Override
	protected EObject getContainer(Resource resource, Object object) {
		RBTree tree = null;
		if (resource.getContents().size() == 1 && resource.getContents().get(0) instanceof RBTree) {
			tree = (RBTree)resource.getContents().get(0);
		}
		else {
			tree = TreeFactory.eINSTANCE.createRBTree();
			tree.setFeature((EStructuralFeature)object);
			resource.getContents().add(tree);
		}
		return tree;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#getFeatureName(java.lang.Object)
	 */
	@Override
	protected String getFeatureName(Object object) {
		EStructuralFeature feature = (EStructuralFeature)object;
		EClass container = (EClass)feature.eContainer();
		return getInstanceClassName(container) + FILE_PART_SEPARATOR + feature.getName();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#getTargets(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected Object[] getTargets(EObject object) {
		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
		Iterator<EStructuralFeature> it = object.eClass().getEStructuralFeatures().iterator();
		while (it.hasNext()) {
	        EStructuralFeature feature = it.next();
	        if (isFeatureIndexed(feature))
	        	features.add(feature);
        }
		return features.toArray(new EStructuralFeature[features.size()]);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#insert(org.eclipse.emf.ecore.EObject, java.lang.Object)
	 */
	@Override
	protected void insert(EObject container, Object object) {
		((RBTree)container).insert((EObject)object);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#remove(org.eclipse.emf.ecore.EObject, java.lang.Object)
	 */
	@Override
	protected void remove(EObject container, Object object) {
		((RBTree)container).remove((EObject)object);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#isEmpty(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected boolean isEmpty(EObject container) {
		return ((RBTree)container).isEmpty();
	}

	public boolean isFeatureIndexed(EStructuralFeature feature) {
		String isIndexed = EcoreUtil.getAnnotation(feature, ANNOTATION_MARKER, ANNOTATION_INDEX);
		return Boolean.valueOf(isIndexed);
	}

}
