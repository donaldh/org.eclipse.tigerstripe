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
package org.eclipse.tigerstripe.repository.metamodel.pojo.writer;

import org.eclipse.tigerstripe.metamodel.IMultiplicity;
import org.eclipse.tigerstripe.metamodel.VisibilityEnum;

public class WriterUtils {

	public final static String getVisibilityAsString(VisibilityEnum visibility) {
		if (visibility == VisibilityEnum.PACKAGE || visibility == null)
			return "";
		return visibility.getLiteral();
	}

	public final static String getMultiplicityAsString(
			IMultiplicity multiplicity) {
		int lower = multiplicity.getLowerBound();
		int upper = multiplicity.getUpperBound();

		if (lower == 0 && upper == 0)
			return "0";
		else if (lower == 1 && upper == 1)
			return "1";
		else if (lower == 0 && upper == -1)
			return "*";

		String lowerStr = String.valueOf(lower);
		String upperStr = String.valueOf(upper);
		if (upper == -1)
			upperStr = "*";

		return lowerStr + ".." + upperStr;
	}

}
