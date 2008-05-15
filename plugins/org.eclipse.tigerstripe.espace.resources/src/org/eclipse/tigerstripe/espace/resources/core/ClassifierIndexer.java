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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.tigerstripe.espace.resources.EObjectList;
import org.eclipse.tigerstripe.espace.resources.ResourcesFactory;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;

/**
 * @author Yuri Strot
 *
 */
public class ClassifierIndexer extends AbstractIndexer {
	
	public static final String INDEX_DIRECTORY = "INDEX/";
	
	public ClassifierIndexer(ResourceSet resourceSet) {
		super(resourceSet);
	}
	
	public EObject[] read(EClassifier classifier) {
		Resource res = getResource(classifier, false);
		if (res != null) {
			EObjectList list = (EObjectList)getContainer(res, classifier);
			return list.getObjects().toArray(new EObject[list.getObjects().size()]);
		}
		return EMPTY;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#getContainer(org.eclipse.emf.ecore.resource.Resource, java.lang.Object)
	 */
	@Override
	protected EObject getContainer(Resource resource, Object object) {
		EObjectList list = null;
		if (resource.getContents().size() == 1 && resource.getContents().get(0) instanceof EObjectList) {
			list = (EObjectList)resource.getContents().get(0);
		}
		else {
			list = ResourcesFactory.eINSTANCE.createEObjectList();
			resource.getContents().add(list);
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#getFile(java.lang.Object)
	 */
	@Override
	protected File getFile(Object object) {
		String featurePath = ((EClass)object).getInstanceClassName() + ".xml";
		return new File(ResourcesPlugin.getDefault().getStateLocation().toFile(), 
			INDEX_DIRECTORY + featurePath);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#getTargets(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected Object[] getTargets(EObject object) {
		List<EClass> classes = new ArrayList<EClass>();
		classes.add(object.eClass());
		classes.addAll(object.eClass().getEAllSuperTypes());
		return classes.toArray(new EClass[classes.size()]);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#insert(org.eclipse.emf.ecore.EObject, java.lang.Object)
	 */
	@Override
	protected void insert(EObject container, Object object) {
		List<EObject> list = ((EObjectList)container).getObjects();
		EObject eobject = (EObject)object;
		if (!list.contains(eobject))
			list.add(eobject);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#remove(org.eclipse.emf.ecore.EObject, java.lang.Object)
	 */
	@Override
	protected void remove(EObject container, Object object) {
		((EObjectList)container).getObjects().remove((EObject)object);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.AbstractIndexer#isEmpty(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected boolean isEmpty(EObject container) {
		return ((EObjectList)container).getObjects().isEmpty();
	}

}
