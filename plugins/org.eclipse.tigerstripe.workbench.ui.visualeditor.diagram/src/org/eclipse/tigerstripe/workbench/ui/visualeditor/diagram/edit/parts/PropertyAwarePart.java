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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts;

import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;

/**
 * To be implemented by any part that needs to react to property changes
 * (NamedElement:getProperties)
 * 
 * @author Eric Dillon
 * 
 */
public interface PropertyAwarePart {

	public void propertyChanged(DiagramProperty property);
}
