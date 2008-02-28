/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.reader;

import org.eclipse.tigerstripe.metamodel.ERefByEnum;
import org.eclipse.tigerstripe.metamodel.IMultiplicity;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.metamodel.VisibilityEnum;

public class ReaderUtils {

	/**
	 * Extracts the visibility out of the modifiers as returned by QDox
	 * 
	 * @param modifiers
	 * @return
	 */
	public static VisibilityEnum toVisibility(String[] modifiers) {
		VisibilityEnum result = VisibilityEnum.PACKAGE;
		for (String modifier : modifiers) {
			if ("public".equals(modifier)) {
				result = VisibilityEnum.PUBLIC;
			} else if ("protected".equals(modifier)) {
				result = VisibilityEnum.PROTECTED;
			} else if ("private".equals(modifier)) {
				result = VisibilityEnum.PRIVATE;
			}
		}
		return result;
	}

	public static IMultiplicity toMultiplicity(String multStr) {
		IMultiplicity result = MetamodelFactory.eINSTANCE.createIMultiplicity();
		if ("1".equals(multStr)) {
			result.setLowerBound(1);
			result.setUpperBound(1);
		} else if ("0".equals(multStr)) {
			result.setLowerBound(0);
			result.setUpperBound(0);
		} else if ("*".equals(multStr)) {
			result.setLowerBound(0);
			result.setUpperBound(-1);
		} else if (multStr.indexOf("..") != -1) {
			String loStr = multStr.substring(0, multStr.indexOf(".."));
			String upStr = multStr.substring(multStr.indexOf("..") + 2);
			int lower = 0;
			int upper = -1;
			try {
				lower = Integer.parseInt(loStr);
			} catch (NumberFormatException e) {
				// ignore
			}
			try {
				upper = Integer.parseInt(upStr);
			} catch (NumberFormatException e) {
				// ignore
			}
			result.setLowerBound(lower);
			result.setUpperBound(upper);
		}
		return result;
	}

	public static ERefByEnum toRefByEnum(String str) {
		if (str == null || str.length() == 0)
			return ERefByEnum.NON_APPLICABLE;
		else if ("value".equals(str))
			return ERefByEnum.REF_BY_VALUE;
		else if ("key".equals(str))
			return ERefByEnum.REF_BY_KEY;
		else if ("keyResult".equals(str))
			return ERefByEnum.REF_BY_KEY_RESULT;

		return ERefByEnum.NON_APPLICABLE;
	}
}
