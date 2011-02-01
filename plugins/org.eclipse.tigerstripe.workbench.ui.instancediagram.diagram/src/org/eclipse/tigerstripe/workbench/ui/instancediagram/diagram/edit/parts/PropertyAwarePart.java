/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts;

import org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty;

/**
 * To be implemented by any part that needs to react to property changes
 * (NamedElement:getProperties)
 * 
 */
public interface PropertyAwarePart {

	public void propertyChanged(DiagramProperty property);
}
