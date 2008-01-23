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
package org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype;

import org.dom4j.Element;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;

public class StereotypeAttributeFactory {

	public static IStereotypeAttribute makeAttribute(int kind) {
		IStereotypeAttribute result = null;

		switch (kind) {
		case IStereotypeAttribute.CHECKABLE_KIND:
			result = new CheckableStereotypeAttribute();
			break;

		case IStereotypeAttribute.ENTRY_LIST_KIND:
			result = new EntryListStereotypeAttribute();
			break;

		case IStereotypeAttribute.STRING_ENTRY_KIND:
			result = new StringEntryStereotypeAttribute();
			break;
		}

		return result;
	}

	public static IStereotypeAttribute makeAttribute(Element elm) {
		IStereotypeAttribute result = null;

		int kind = Integer.parseInt(elm.attributeValue("kind"));
		result = makeAttribute(kind);
		result.parse(elm);
		return result;
	}
}
