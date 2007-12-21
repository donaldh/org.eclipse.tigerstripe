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
package org.eclipse.tigerstripe.core.profile.stereotype;

import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeAttribute;

/**
 * A checkable attribute is rendered as a simple checkbox for the end-user
 * 
 * @author Eric Dillon
 * 
 */
public class CheckableStereotypeAttribute extends BaseStereotypeAttribute {
	public CheckableStereotypeAttribute() {
		super(IextStereotypeAttribute.CHECKABLE_KIND);
	}
}
