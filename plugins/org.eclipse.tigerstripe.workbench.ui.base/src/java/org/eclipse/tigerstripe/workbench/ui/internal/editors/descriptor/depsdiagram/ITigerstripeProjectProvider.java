/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.depsdiagram;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface ITigerstripeProjectProvider {

	ITigerstripeModelProject getProject();

}
