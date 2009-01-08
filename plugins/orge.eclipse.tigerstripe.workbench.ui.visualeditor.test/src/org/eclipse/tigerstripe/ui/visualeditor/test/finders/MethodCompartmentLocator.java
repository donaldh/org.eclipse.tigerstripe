/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.ui.visualeditor.test.finders;

import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactMethodCompartmentEditPart;

import com.windowtester.runtime.swt.locator.SWTWidgetLocator;

public class MethodCompartmentLocator extends SWTWidgetLocator {

	private static final long serialVersionUID = 1L;
	
	public MethodCompartmentLocator() {
		super(ManagedEntityArtifactMethodCompartmentEditPart.class);
	}

	public MethodCompartmentLocator(String text, SWTWidgetLocator parent) {
		super(ManagedEntityArtifactMethodCompartmentEditPart.class, text, parent);
	}

	public MethodCompartmentLocator(String text, int index, SWTWidgetLocator parent) {
		super(ManagedEntityArtifactMethodCompartmentEditPart.class, text, index, parent);
	}

}
