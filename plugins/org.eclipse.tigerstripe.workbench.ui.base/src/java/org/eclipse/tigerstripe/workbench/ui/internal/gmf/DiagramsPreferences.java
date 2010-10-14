/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class DiagramsPreferences {

	public static final String ROUTING_STYLE_VALUE_RECTILINEAR = "rectilinear";
	public static final String ROUTING_STYLE_VALUE_OBLIQUE = "oblique";
	public static final String SHOW_COMPARTMENTS_VALUE_NAME_ONLY = "nameOnly";
	public static final String SHOW_COMPARTMENTS_VALUE_ALL = "all";
	public static final String EXTENDS_RELATIONSHIP_VALUE_SHOW = "show";
	public static final String EXTENDS_RELATIONSHIP_VALUE_HIDE = "hide";

	public static final String P_ROUTING_STYLE = "p.diagram.routing.style";
	public static final String P_ROUTING_AVOID_OBSTRUCTIONS = "p.diagram.routing.avoidObstructions";
	public static final String P_ROUTING_CLOSEST_DISTANCE = "p.diagram.routing.closestDistance";
	public static final String P_SHOW_COMPARTMENTS = "p.diagram.showCompartments";
	public static final String P_EXTENDS_RELATIONSHIP = "p.diagram.extendsRelationship";
	public static final String P_USE_CUSTOM_PREFERENCES = "p.diagram.useCustomPreferences";

	public static Set<String> ALL = unmodifiableSet(new HashSet<String>(asList(
			P_ROUTING_STYLE, P_ROUTING_AVOID_OBSTRUCTIONS,
			P_ROUTING_CLOSEST_DISTANCE, P_SHOW_COMPARTMENTS,
			P_EXTENDS_RELATIONSHIP, P_USE_CUSTOM_PREFERENCES)));

	public static void initializeDefaults(IPreferenceStore store) {
		store.setDefault(P_ROUTING_STYLE, ROUTING_STYLE_VALUE_OBLIQUE);
		store.setDefault(P_ROUTING_AVOID_OBSTRUCTIONS, true);
		store.setDefault(P_ROUTING_CLOSEST_DISTANCE, true);
		store.setDefault(P_SHOW_COMPARTMENTS, SHOW_COMPARTMENTS_VALUE_ALL);
		store.setDefault(P_EXTENDS_RELATIONSHIP,
				EXTENDS_RELATIONSHIP_VALUE_SHOW);
	}

	public static IPreferenceStore getScopeStore() {
		return new ScopedPreferenceStore(new InstanceScope(),
				DiagramsPreferences.class.getName());

	}

	public static IPreferenceStore chooseStore(Map<String, String> localData) {
		IPreferenceStore store = getGlobalStore();
		initializeDefaults(store);
		if (Boolean.TRUE.toString().equals(
				localData.get(P_USE_CUSTOM_PREFERENCES))) {
			store = new CustomPreferenceStore(localData, store);
		}
		return store;
	}

	public static IPreferenceStore getGlobalStore() {
		IPreferenceStore store = getScopeStore();
		initializeDefaults(store);
		return store;
	}
}
