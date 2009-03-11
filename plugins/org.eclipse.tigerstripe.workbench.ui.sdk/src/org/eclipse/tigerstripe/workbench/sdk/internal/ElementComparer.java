/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal;

import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginObject;

public class ElementComparer {

	/**
	 * Cmpore the two elements to see if they are structurally equal.
	 * The elements should be in the same order I think!
	 * Attributes do not have an order?
	 * 
	 * @param element1
	 * @param element2
	 * @return
	 */
	public static boolean compareElements(IPluginElement element1, IPluginElement element2){

		
		
		boolean equalAttributes = compareAttributes(element1, element2);
		if ( !equalAttributes)
			return false;
		boolean equalElements = compareChildElements(element1, element2);
		if ( !equalElements)
			return false;
		
		return true;
	}
	
	
	private static boolean compareAttributes(IPluginElement element1, IPluginElement element2){
		// Quick & dirty!
		if (element1.getAttributeCount() != element2.getAttributeCount()){
			return false;
		}
		
		IPluginAttribute[] attributes1 = element1.getAttributes();
		for (IPluginAttribute attribute1 : attributes1){
			String attributeName = attribute1.getName();
			String attributeValue = attribute1.getValue();
			IPluginAttribute attribute2 = element2.getAttribute(attributeName);
			if (attribute2 != null){
				boolean equalValue = attributeValue.equals(attribute2.getValue());
				if (!equalValue)
					return false;
			} else 
				return false;
		}
		
		return true;
	}
	
	private static boolean compareChildElements(IPluginElement element1, IPluginElement element2){
		// Quick & dirty!
		if (element1.getChildCount() != element2.getChildCount()){
			return false;
		}
		
		IPluginObject[] element1Objects = element1.getChildren();
		IPluginObject[] element2Objects = element2.getChildren();
		for (int i=0; i<element1Objects.length;i++){
			IPluginObject element1Object = element1Objects[i];
			IPluginObject element2Object = element2Objects[i];
			if (element1Object instanceof IPluginElement && element2Object instanceof IPluginElement){
				IPluginElement element1Element = (IPluginElement) element1Object;
				IPluginElement element2Element = (IPluginElement) element2Object;
				if (! compareElements(element1Element, element2Element)){
					return false;
				}
				
			} else {
				System.out.println(element1Object.getClass());
				System.out.println(element2Object.getClass());
			}
			
			
		}
	
		return true;
	}
	
}
