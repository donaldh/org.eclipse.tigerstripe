/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;

public class DefaultModelMapper implements IModelMapper {

	public Map<EObject, String> getMapping(Model model) {
		Map<EObject, String> classMap = new HashMap<EObject, String>();
		
		/* 
		 * First build a map of the eObject and the "default" artifact
		 * type that we will map to based on the Class in the UML
		 */
		TreeIterator t = model.eAllContents();
		t = model.eAllContents();

		while (t.hasNext()) {
				
			EObject eObject = (EObject) t.next();
			if (eObject instanceof AssociationClass) {
				classMap.put( eObject, IAssociationClassArtifact.class.getName());
			} else if (eObject instanceof Association) {
				classMap.put( eObject, IAssociationArtifact.class.getName());
			} else if (eObject instanceof Enumeration) {
				classMap.put( eObject, IEnumArtifact.class.getName());
			} else if (eObject instanceof Interface) {
				classMap.put( eObject,  ISessionArtifact.class.getName());
			} else if (eObject instanceof Class) {
				// We cannot determine "Class" types - could be Entity, Datatype, Exception etc
				classMap.put( eObject, "");
			}
			if (eObject instanceof Classifier) {
				Classifier element = (Classifier) eObject;
				for (Object depO : element.getClientDependencies()) {
					if (depO instanceof Dependency && !(depO instanceof InterfaceRealization)) {
						Dependency dep = (Dependency) depO;
						classMap.put( dep, IDependencyArtifact.class.getName());
					}
				}
			}
		}
		return classMap;
	}

}
