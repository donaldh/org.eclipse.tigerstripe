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
package org.eclipse.tigerstripe.espace.resources.internal.format;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMISaveImpl;
import org.eclipse.tigerstripe.espace.resources.core.IPackageFinder;

/**
 * @author Yuri Strot
 *
 */
public class MetaXMISave extends XMISaveImpl {
	
	private IPackageFinder finder;

	/**
	 * @param helper
	 */
	public MetaXMISave(XMLHelper helper, IPackageFinder finder) {
		super(helper);
		this.finder = finder;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMISaveImpl#writeTopObjects(java.util.List)
	 */
	@Override
	public Object writeTopObjects(List<? extends EObject> contents) {
		doc.startElement(XMI_TAG_NS);
		Object mark = doc.mark();

		saveEcoreDefinition(getPackages(contents));

		for (int i = 0, size = contents.size(); i < size; i++) {
			EObject top = contents.get(i);
			EClass eClass = top.eClass();
			if (extendedMetaData == null
					|| featureTable.getDocumentRoot(eClass.getEPackage()) != eClass) {
				String name = helper.getQName(eClass);
				doc.startElement(name);
				root = top;
				saveElementID(top);
			} else {
				doc.startElement(null);
				root = top;
				saveFeatures(top);
				doc.addLine();
			}
		}

		doc.endElement();
		return mark;
	}
	
	protected List<EPackage> getPackages(List<? extends EObject> contents) {
		List<EPackage> packages = new ArrayList<EPackage>();
		if (finder != null) {
			for (EObject object : contents) {
				List<EPackage> newPackages = finder.getPackages(object);
				for (EPackage newPackage : newPackages) {
					if (!packages.contains(newPackage))
						packages.add(newPackage);
				}
			}
		}
		return packages;
	}
	
	protected void saveEcoreDefinition(List<EPackage> packages) {
		doc.startElement(IFormatConstants.ECORE_DEFINITION_TAG);
		for (int i = 0, size = packages.size(); i < size; i++) {
			EObject top = packages.get(i);
			EClass eClass = top.eClass();
			if (extendedMetaData == null
					|| featureTable.getDocumentRoot(eClass.getEPackage()) != eClass) {
				String name = helper.getQName(eClass);
				doc.startElement(name);
				root = top;
				saveElementID(top);
			} else {
				doc.startElement(null);
				root = top;
				saveFeatures(top);
				doc.addLine();
			}
		}
		doc.endElement();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl#writeTopObject(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected Object writeTopObject(EObject top) {
		List<EObject> objects = new ArrayList<EObject>();
		objects.add(top);
		return writeTopObjects(objects);
	}

}
