/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.utils.ClassDiagramPartsUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.LiteralImpl;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl;

public class LiteralParser extends TigerstripeStructuralFeaturesParser {

	public LiteralParser(List features) {
		super(features);
	}

	@Override
	public String getPrintString(IAdaptable adapter, int flags) {
		
		setCurrentMap(adapter);
		// first get the print string from the super-class
		String printString = super.getPrintString(adapter, flags);
		// then get the list of method parameters from the wrapped adapter
		Literal literal = (Literal) adapter.getAdapter(LiteralImpl.class);
		// from that method, get the prefix to use to indicate the method's
		// visibility
		String visibilityPrefix = ClassDiagramPartsUtils
				.visibilityPrefix(literal.getVisibility());
		// if the method has any stereotypes, then put together a stereotype
		// prefix string for the method
		String stereotypePref = "";
		if (!hideStereotypes()) {
			IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession().getActiveProfile();
			StringBuffer stereoPrefBuf = new StringBuffer();
			EList stereotypes = literal.getStereotypes();
			for (Object obj : stereotypes) {
				String val = (String) obj;
				IStereotype stereo = profile.getStereotypeByName(val);
				if (stereo != null){
					if (stereoPrefBuf.length() == 0)
						stereoPrefBuf.append("<<");
					else 
						stereoPrefBuf.append(", ");
					stereoPrefBuf.append(val);
				}
			}
			if (stereoPrefBuf.length() > 0)
				stereoPrefBuf.append(">>");

			stereotypePref = stereoPrefBuf.toString();
		}
		if (stereotypePref.length() == 0)
			return visibilityPrefix + printString;
		// else, return with the stereotype prefix...
		return stereotypePref + " " + visibilityPrefix + printString;
	}

	@Override
	protected Object getValidNewValue(EStructuralFeature feature, Object value) {
		Object newValue = super.getValidNewValue(feature, value);
		if (!(newValue instanceof InvalidValue) && (value instanceof String)) {
			// the name of the feature is constrained to match a fieldname, not
			// the values (which are also passed through this method to check
			// for validity), so only apply the elementNamePattern constraint
			// if this feature is named "name" (the other way to look at this
			// is that we are going to not apply the pattern for features that
			// have the name "value)
			if ("name".equals(feature.getName())
					&& !NamedElementImpl.elementNamePattern.matcher(
							(String) value).matches()) {
				value = new InvalidValue(
						"Value that can be converted to a element name is expected");
			}
		}
		return value;
	}

}
