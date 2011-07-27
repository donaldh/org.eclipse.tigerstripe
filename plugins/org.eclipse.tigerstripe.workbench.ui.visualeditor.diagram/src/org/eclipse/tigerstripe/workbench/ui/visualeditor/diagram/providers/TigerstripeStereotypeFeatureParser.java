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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement;

public class TigerstripeStereotypeFeatureParser extends
		TigerstripeStructuralFeatureParser {

	public TigerstripeStereotypeFeatureParser(EStructuralFeature feature) {
		super(feature);
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
		String result = "";
		if (!hideStereotypes()) {
		
		EObject element = (EObject) adapter.getAdapter(EObject.class);
			if (element instanceof NamedElement) {
				result = getAnnotationsAsString(false, (NamedElement) element);
			}
		}

		if (!result.isEmpty()) {
			result = processor.format(new Object[] { result },
					new StringBuffer(), new FieldPosition(0)).toString();
		}

		return result;
	}
}
