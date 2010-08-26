/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;

public class ArtifactTester extends PropertyTester {

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		IAbstractArtifact artifact = TSExplorerUtils.getArtifactFor(receiver);
		if (artifact == null) {
			return false;
		}
		IArtifactFormContentProvider contentProvider = (IArtifactFormContentProvider) Platform
				.getAdapterManager().getAdapter(artifact,
						IArtifactFormContentProvider.class);

		if (contentProvider == null) {
			return false;
		}
		
		if ("hasMethods".equals(property)) {
			return contentProvider.hasMethods();
		} else if ("hasAttributes".equals(property)) {
			return contentProvider.hasAttributes();
		} else if ("hasConstants".equals(property)) {
			return contentProvider.hasConstants();
		} else {
			throw new IllegalArgumentException(String.format(
					"Property '%s' not supported", property));
		}
	}
}
