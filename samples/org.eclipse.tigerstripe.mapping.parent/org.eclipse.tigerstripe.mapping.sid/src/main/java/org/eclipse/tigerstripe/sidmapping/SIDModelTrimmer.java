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
package org.eclipse.tigerstripe.sidmapping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model.IModelTrimmer;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

/**
 * This class is used to "trim" the SID Import model.
 * 
 * It performs a couple of basic actions:
 * 
 * 	a) Changes package names based on the tipPackage:implementationPackage annotation attribute Value.
 * 	b) Removes elements based on the tipPackage:implementationPackage annotation attribute Value.
 * 
 * Actions are logged to the file "SIDImportTrimmer.log" which should be in the same location as the files to be imported.
 * @author rcraddoc
 *
 */
public class SIDModelTrimmer implements IModelTrimmer {
	
	public Model trimModel(Model modelToTrim)
	{
		try
		{
			String importFilename = modelToTrim.eResource().getURI().toFileString();

			File importFile = new File(importFilename);
			File logFile = new File(importFile.getParent() + 
					"/SIDImportTrimmer.log");

			PrintWriter out = new PrintWriter(new FileOutputStream(logFile));

			List<EObject> itemsToRemove = new ArrayList<EObject>();
			TreeIterator<EObject> t = modelToTrim.eAllContents();
			t = modelToTrim.eAllContents();

			String modelBasePackageName = "";
			out.println("Handling Model '" + modelToTrim.getQualifiedName() + "' from " + modelToTrim.eResource().getURI());
			
			for (Stereotype s : modelToTrim.getAppliedStereotypes()) {
				if (s.getName().equals("tipPackage")) {
					Stereotype tipPackage = s;
					if (tipPackage != null) {
						EList<Property> stereoAttributes = tipPackage.getAllAttributes();
						for (int a = 0; a < stereoAttributes.size(); ++a) {
							Property attribute = (Property)stereoAttributes.get(a);
							if (attribute.getName().equals("implementationPackage")) {
								String baseName = (String)modelToTrim.getValue(s, attribute.getName());
								if (baseName != null)
									modelBasePackageName = baseName + '.';
								out.println("   BasePackageName = '" + modelBasePackageName + "'");
							}
						}
					}
				} else {
					out.println("    No tipPackage Stereo on model");
				}
			}
			String basePackageName = "";
			Element classifier;
			while (t.hasNext()) {
				EObject eObject = (EObject)t.next();
				if (eObject instanceof Package) {
					Package p = (Package)eObject;
					if (!(p.eContainer() instanceof Model))
					{
						basePackageName = "";
					}
					else basePackageName = modelBasePackageName;

					try
					{
						out.println("Handling Package '" + p.getQualifiedName() + "' from " + eObject.eResource().getURI());
						for (Stereotype s : p.getAppliedStereotypes())
							if (s.getName().equals("tipPackage")) {
								Stereotype tipPackage = s;
								if (tipPackage != null) {
									List stereoAttributes = tipPackage.getAllAttributes();
									for (int a = 0; a < stereoAttributes.size(); ++a) {
										Property attribute = (Property)stereoAttributes.get(a);
										if (attribute.getName().equals("implementationPackage")) {
											String newname = (String)p.getValue(s, attribute.getName());
											if (newname == null) {
												continue;
											}

											p.setName(basePackageName + newname);
											out.println("   new FQN '" + p.getQualifiedName() + "'");
										}
									}
								}
							}
							else {
								out.println("    No tipPackage Stereo on package");
							}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}

				if (eObject instanceof Element) {
					classifier = (Element)eObject;
					for (Stereotype s : classifier.getAppliedStereotypes())
					{
						if (!(s.getName().equals("doNotImplement"))) {
							continue;
						}
						out.println("Found doNotImplement on " + classifier);
						itemsToRemove.add(eObject);
					}
				}

			}

			for (EObject obj : itemsToRemove) {
				if (obj.eContainer() instanceof Package) {
					Package p = (Package)obj.eContainer();
					out.println("Removing " + p.getName() + " : " + p.getPackagedElements().remove(obj));
				}

			}

			out.close();
			return modelToTrim;
		} catch (Exception localException1) {
		}
		return null;
	}
}
