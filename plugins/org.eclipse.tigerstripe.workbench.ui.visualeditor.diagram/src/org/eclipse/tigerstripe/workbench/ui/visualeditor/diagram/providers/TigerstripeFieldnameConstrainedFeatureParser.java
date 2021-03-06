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

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl;

public class TigerstripeFieldnameConstrainedFeatureParser extends
		TigerstripeStructuralFeatureParser {

	public TigerstripeFieldnameConstrainedFeatureParser(
			EStructuralFeature feature) {
		super(feature);
	}

	@Override
	protected Object getValidNewValue(EStructuralFeature feature, Object value) {
		Object newValue = super.getValidNewValue(feature, value);
		if (!(newValue instanceof InvalidValue)
				&& (value instanceof String)
				&& !NamedElementImpl.elementNamePattern.matcher((String) value)
						.matches()) {
			value = new InvalidValue(
					"Value that can be converted to a class name is expected");
		}
		return value;
	}

}
