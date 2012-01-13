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
package org.eclipse.tigerstripe.annotation.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
		Collections.sort(types, new Comparator<TargetAnnotationType>() {
			public int compare(TargetAnnotationType o1, TargetAnnotationType o2) {
				String name1 = o1.getType().getName();
				if (name1 == null)
					name1 = "";
				String name2 = o2.getType().getName();
				if (name2 == null)
					name2 = "";
				return name1.compareTo(name2);
			}
		});
	}

	public static AnnotationGroup[] getGroups(TargetAnnotationType[] types) {
		Map<EPackage, List<TargetAnnotationType>> actions = new HashMap<EPackage, List<TargetAnnotationType>>();

		for (int i = 0; i < types.length; i++) {
			EPackage pckg = types[i].getType().getClazz().getEPackage();
			List<TargetAnnotationType> typeList = actions.get(pckg);
			if (typeList == null) {
				typeList = new ArrayList<TargetAnnotationType>();
				actions.put(pckg, typeList);
			}
			typeList.add(types[i]);
		}

		Map<String, List<TargetAnnotationType>> textGroups = new HashMap<String, List<TargetAnnotationType>>();
		List<AnnotationGroup> emptyGroups = new ArrayList<AnnotationGroup>();

		for (Entry<EPackage, List<TargetAnnotationType>> entry : actions
				.entrySet()) {
			EPackage pckg = entry.getKey();
			List<TargetAnnotationType> listType = entry.getValue();
			String label = AnnotationPlugin.getManager().getPackageLabel(pckg);
			AnnotationGroup group = new AnnotationGroup(label, listType);
			if (label == null)
				emptyGroups.add(group);
			else {
				List<TargetAnnotationType> list = textGroups.get(label);
				if (list == null)
					textGroups.put(label, listType);
				else
					list.addAll(listType);
			}
		}

		List<AnnotationGroup> groups = new ArrayList<AnnotationGroup>();

		Set<String> set = textGroups.keySet();
		String[] keys = set.toArray(new String[set.size()]);
		Arrays.sort(keys);

		for (String name : keys) {
			groups.add(new AnnotationGroup(name, textGroups.get(name)));
		}

		groups.addAll(emptyGroups);
		return groups.toArray(new AnnotationGroup[groups.size()]);
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
