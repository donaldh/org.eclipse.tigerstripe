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

import java.text.FieldPosition;
import java.text.MessageFormat;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;

public class TigerstripeStereotypeFeatureParser extends
		TigerstripeStructuralFeatureParser {

	private EStructuralFeature feature;

	public TigerstripeStereotypeFeatureParser(EStructuralFeature feature) {
		super(feature);
		this.feature = feature;
	}

	@Override
	public String getPrintString(IAdaptable adapter, int flags) {
		setCurrentMap(adapter);
		if (hideStereotypes())
			return "";
		else
			return getStringByPattern(adapter, flags, getViewPattern(),
					getViewProcessor());
	}

	@Override
	protected String getStringByPattern(IAdaptable adapter, int flags,
			String pattern, MessageFormat processor) {
		
		setCurrentMap(adapter);
		if (hideStereotypes())
			return "";
		
		EObject element = (EObject) adapter.getAdapter(EObject.class);
		Object value = element.eGet(feature);
		value = getValidValue(feature, value);
		// catch case where the value is an empty EList...return an empty string
		// in that case (so that the square brackets from displaying a list
		// won't be
		// included in the label).
		if (value instanceof EList && ((EList) value).size() == 0)
			return "";
		// return processor.format(new Object[] { "" }, new StringBuffer(),
		// new FieldPosition(0)).toString();
		// if value is a list of values, put together a comma-separated string
		// that
		// contains those values and pass that string off to the processor for
		// formatting
		
		if (value instanceof EDataTypeUniqueEList) {
			IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession().getActiveProfile();
			
			StringBuffer buff = new StringBuffer();
			EDataTypeUniqueEList stereotypes = (EDataTypeUniqueEList) value;
			
			for (Object obj : stereotypes) {
				String val = (String) obj;
				IStereotype stereo = profile.getStereotypeByName(val);
				if (stereo != null){
					if (buff.length() > 0)
						buff.append(", ");
					buff.append(val);
				}

			}
			if (buff.toString().equals("")){
				return "";
			}
			return processor.format(new Object[] { buff.toString() },
					new StringBuffer(), new FieldPosition(0)).toString();
		}
		return "";
		
	}

}
