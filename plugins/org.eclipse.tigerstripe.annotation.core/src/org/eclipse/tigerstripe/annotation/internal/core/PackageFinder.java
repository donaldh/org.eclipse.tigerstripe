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
package org.eclipse.tigerstripe.annotation.internal.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.espace.resources.core.IPackageFinder;

/**
 * @author Yuri Strot
 *
 */
public class PackageFinder implements IPackageFinder {

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.espace.resources.core.IIdentifyManager#getPackages(org.eclipse.emf.ecore.EObject)
	 */
	public List<EPackage> getPackages(EObject object) {
		List<EPackage> packages = new ArrayList<EPackage>();
		if (object instanceof Annotation) {
			EObject content = ((Annotation)object).getContent();
			collectPackages(content, packages);
		}
		else {
			collectPackages(object, packages);
		}
		return packages;
	}
	
	protected void collectPackages(EObject object, List<EPackage> packages) {
		EPackage pack = object.eClass().getEPackage();
		if (!packages.contains(pack))
			packages.add(pack);
        for(EObject child : object.eContents()){
        	collectPackages(child, packages);
        }
	}

}
