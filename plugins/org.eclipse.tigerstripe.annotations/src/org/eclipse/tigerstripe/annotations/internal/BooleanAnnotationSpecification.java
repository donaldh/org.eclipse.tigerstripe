/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    erdillon (Cisco Systems, Inc.) - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.internal;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IBooleanAnnotationSpecification;

public class BooleanAnnotationSpecification extends BaseAnnotationSpecification
		implements IAnnotationSpecification, IBooleanAnnotationSpecification {

	@Override
	void populate(IConfigurationElement element, IAnnotationForm parentForm) {
		super.populate(element, parentForm);

	}

}
