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

import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.EXTENDS_RELATIONSHIP_VALUE_HIDE;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_EXTENDS_RELATIONSHIP;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_ROUTING_AVOID_OBSTRUCTIONS;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_ROUTING_CLOSEST_DISTANCE;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_ROUTING_STYLE;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_SHOW_COMPARTMENTS;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.ROUTING_STYLE_VALUE_OBLIQUE;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.ROUTING_STYLE_VALUE_RECTILINEAR;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.SHOW_COMPARTMENTS_VALUE_NAME_ONLY;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.PropertiesSetStyle;
import org.eclipse.gmf.runtime.notation.PropertyValue;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.RoutingStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferencesHelper {

	public static final String PREFERENCES_STYLE_NAME = "org.eclipse.tigerstripe.customDiagramPrefrences";
	public static final PropertiesSetStyle NULL_STYLE = NotationFactory.eINSTANCE
			.createPropertiesSetStyle();

	public static void setRoutingStyle(View view,
			IPreferenceStore preferenceStore) {

		RoutingStyle style = (RoutingStyle) view
				.getStyle(NotationPackage.Literals.ROUTING_STYLE);

		if (style != null) {
			String savedRoutingStyle = preferenceStore
					.getString(P_ROUTING_STYLE);

			if (ROUTING_STYLE_VALUE_OBLIQUE.equals(savedRoutingStyle)) {
				style.setRouting(Routing.MANUAL_LITERAL);
			} else if (ROUTING_STYLE_VALUE_RECTILINEAR
					.equals(savedRoutingStyle)) {
				style.setRouting(Routing.RECTILINEAR_LITERAL);
			} else {
				style.setRouting(Routing.MANUAL_LITERAL);
			}
			style.setAvoidObstructions(preferenceStore
					.getBoolean(P_ROUTING_AVOID_OBSTRUCTIONS));
			style.setClosestDistance(preferenceStore
					.getBoolean(P_ROUTING_CLOSEST_DISTANCE));
		}
	}

	public static void setCompartments(View view,
			IPreferenceStore preferenceStore) {
		DrawerStyle drawerStyle = (DrawerStyle) view
				.getStyle(NotationPackage.Literals.DRAWER_STYLE);
		if (drawerStyle != null) {
			drawerStyle.setCollapsed(SHOW_COMPARTMENTS_VALUE_NAME_ONLY
					.equals(preferenceStore.getString(P_SHOW_COMPARTMENTS)));
		}
	}

	public static String extendsRelationshipValue(IPreferenceStore store) {
		return Boolean.toString(isHideExtendsRelationship(store));
	}

	public static boolean isHideExtendsRelationship(IPreferenceStore store) {
		return EXTENDS_RELATIONSHIP_VALUE_HIDE.equals(store
				.getString(P_EXTENDS_RELATIONSHIP));
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> toMap(PropertiesSetStyle properties) {
		Map<String, PropertyValue> map = properties.getPropertiesMap().map();
		HashMap<String, String> result = new HashMap<String, String>(map.size());
		for (Map.Entry<String, PropertyValue> entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue().getValue().toString());

		}
		return result;
	}

	public static IPreferenceStore getStore(View view) {
		return DiagramsPreferences.chooseStore(toMap(findStyle(view
				.getDiagram())));
	}

	public static IPreferenceStore getStore(Diagram diagram) {
		return DiagramsPreferences.chooseStore(toMap(findStyle(diagram)));
	}

	@SuppressWarnings("unchecked")
	public static PropertiesSetStyle findOrCreateStyle(View view) {
		PropertiesSetStyle result = findStyle(view);
		if (result == NULL_STYLE) {
			result = NotationFactory.eINSTANCE.createPropertiesSetStyle();
			result.setName(PREFERENCES_STYLE_NAME);
			getStyles(view).add(result);
		}
		return result;
	}

	public static PropertiesSetStyle findStyle(View view) {
		for (Object style : getStyles(view)) {
			if (style instanceof PropertiesSetStyle) {
				PropertiesSetStyle pss = (PropertiesSetStyle) style;
				if (PREFERENCES_STYLE_NAME.equals(pss.getName())) {
					return pss;
				}
			}
		}
		return NULL_STYLE;
	}

	@SuppressWarnings("rawtypes")
	private static EList getStyles(View view) {
		return view.getDiagram().getStyles();
	}
}
