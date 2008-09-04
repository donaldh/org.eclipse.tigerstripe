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
package org.eclipse.tigerstripe.annotation.ui.internal.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationGroup {
	
	public String name;
	public List<TargetAnnotationType> types;
	
	private AnnotationGroup(String name, List<TargetAnnotationType> types) {
		this.name = name;
		this.types = types;
	}
	
	public static AnnotationGroup[] getGroups(TargetAnnotationType[] types) { 
    	Map<EPackage, List<TargetAnnotationType>> actions =
    		new HashMap<EPackage, List<TargetAnnotationType>>();
    	
		for (int i = 0; i < types.length; i++) {
			EPackage pckg = types[i].getType().getClazz().getEPackage();
			List<TargetAnnotationType> typeList = actions.get(pckg);
			if (typeList == null) {
				typeList = new ArrayList<TargetAnnotationType>();
				actions.put(pckg, typeList);
			}
			typeList.add(types[i]);
        }
		
		List<AnnotationGroup> textGroups = new ArrayList<AnnotationGroup>();
		List<AnnotationGroup> emptyGroups = new ArrayList<AnnotationGroup>();
    	
    	for (Entry<EPackage, List<TargetAnnotationType>> entry : actions.entrySet()) {
    		EPackage pckg = entry.getKey();
    		List<TargetAnnotationType> listType = entry.getValue();
    		String label = AnnotationPlugin.getManager().getPackageLabel(pckg);
    		AnnotationGroup group = new AnnotationGroup(label, listType);
    		if (label == null) emptyGroups.add(group);
    		else textGroups.add(group);
		}
    	
    	textGroups.addAll(emptyGroups);
    	return textGroups.toArray(new AnnotationGroup[textGroups.size()]);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the types
	 */
	public List<TargetAnnotationType> getTypes() {
		return types;
	}

}
