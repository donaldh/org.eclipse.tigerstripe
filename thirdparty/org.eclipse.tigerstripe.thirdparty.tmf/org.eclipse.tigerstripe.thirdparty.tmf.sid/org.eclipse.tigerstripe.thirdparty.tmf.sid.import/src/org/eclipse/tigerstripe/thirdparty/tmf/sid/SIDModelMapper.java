/*******************************************************************************
 * Copyright (c) 2010 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.thirdparty.tmf.sid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model.IModelMapper;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

/**
 * This class is used to "Map" the SID Import into a revised class structure.
 * The key point is about AssociationClasses that are children of Class in the UML and Vice-versa. 
 * This s not supported in TS, so a more unusual mapping is undertaken.
 *  
 * @author rcraddoc
 *
 */
public class SIDModelMapper implements IModelMapper {
	
	public Map<EObject, String> getMapping(Model model)
	{
		try
		{
			String importFilename = model.eResource().getURI().toFileString();

			File importFile = new File(importFilename);
			File logFile = new File(importFile.getParent() + 
					"/SIDImportMapper.log");

			PrintWriter out = new PrintWriter(new FileOutputStream(logFile));
			try {
				Map<EObject,String> classMap = new HashMap<EObject,String>();

				TreeIterator<EObject> t = model.eAllContents();
				t = model.eAllContents();

				while (t.hasNext())
				{
					EObject eObject = (EObject)t.next();

					if (eObject instanceof Package)
						classMap.put(eObject, IPackageArtifact.class.getName());
					boolean data;
					if (eObject instanceof AssociationClass) {
						classMap.put(eObject, IAssociationClassArtifact.class.getName());
					} else if (eObject instanceof Association) {
						classMap.put(eObject, IAssociationArtifact.class.getName());
					} else if (eObject instanceof Enumeration) {
						classMap.put(eObject, IEnumArtifact.class.getName());
					} else if (eObject instanceof Interface) {
						classMap.put(eObject, ISessionArtifact.class.getName());
					} else if (eObject instanceof DataType) {
						classMap.put(eObject, IDatatypeArtifact.class.getName());
					} else if (eObject instanceof org.eclipse.uml2.uml.Class)
					{
						org.eclipse.uml2.uml.Class clazz = (org.eclipse.uml2.uml.Class)eObject;
						EList<EObject> stereos = clazz.getStereotypeApplications();
						data = false;
						for (EObject o : stereos) {
							if (o instanceof Stereotype) {
								Stereotype s = ((Stereotype)o);
								if ((!(s.getName().equals("baseType"))) && 	(!(s.getName().equals("dataType")))) 
									continue;
								data = true;
							}

						}

						if (data) {
							classMap.put(eObject, IDatatypeArtifact.class.getName());
						}
						else {
							boolean isAssoc = false;
							Object gens = clazz.getGenerals();
							ListIterator genIt = ((List)gens).listIterator();
							while (genIt.hasNext()) {
								Classifier gen = (Classifier)genIt.next();
								String name = "unknown";
								if ((clazz.getName() != null) && (!(clazz.getName().equals(""))))
									name = clazz.getName();
								out.println("Class :" + name + " Gen : " + gen.getName() + " " + gen.getClass().getName());
								boolean genISAssocClassMapped = false;
								if (classMap.get(gen) != null) {
									genISAssocClassMapped = ((String)classMap.get(gen)).equals(IAssociationClassArtifact.class.getName());
								}
								if ((!(gen instanceof AssociationClass)) && (!(genISAssocClassMapped))) {
									continue;
								}
								out.println("Mapping as an Assoc Class NOT and Entity " + name);
								classMap.put(eObject, IAssociationClassArtifact.class.getName());
								isAssoc = true;
								break;
							}

							if (!(isAssoc)) {
								classMap.put(eObject, IManagedEntityArtifact.class.getName());
							}

						}

					}

					if (eObject instanceof Classifier) {
						Classifier element = (Classifier)eObject;
						for ( Iterator<Dependency> it = element.getClientDependencies().iterator(); it.hasNext(); ) { 
							Object depO = it.next();
							if ((depO instanceof Dependency) && (!(depO instanceof InterfaceRealization))) {
								Dependency dep = (Dependency)depO;
								classMap.put(dep, IDependencyArtifact.class.getName());
							}
						}
					}
				}
				out.close();
				return classMap;
			} catch (Exception e) {
				e.printStackTrace(out);
				out.close();
				return null;
			}
		}
		catch (Exception e) {
			e.printStackTrace(); }
		return ((Map<EObject, String>)null);
	}
}
