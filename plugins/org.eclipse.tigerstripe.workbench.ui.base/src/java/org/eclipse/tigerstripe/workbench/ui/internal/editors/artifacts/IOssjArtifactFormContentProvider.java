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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;


public interface IOssjArtifactFormContentProvider extends
		IArtifactFormContentProvider {

	/**
	 * If true, enable "instanceMethods" on the artifact. This can only be true
	 * on Managed Entities.
	 * 
	 * If true, the corresponding methods should be exposed on the corresponding
	 * JVTSession that manages the target entity, and all various flavors are
	 * possible (byKey, byValue, etc...) If false, the corresponding methods are
	 * exposed "as-is" on the corresponding interface.
	 * 
	 * @return
	 */
	public boolean enabledInstanceMethods();
}
