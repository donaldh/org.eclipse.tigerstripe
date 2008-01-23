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

import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;



/**
 * A checkable attribute is rendered as a simple checkbox for the end-user
 * 
 * @author Eric Dillon
 * 
 */
public class StringEntryStereotypeAttribute extends BaseStereotypeAttribute {
	public StringEntryStereotypeAttribute() {
		super(IStereotypeAttribute.STRING_ENTRY_KIND);
	}
}
