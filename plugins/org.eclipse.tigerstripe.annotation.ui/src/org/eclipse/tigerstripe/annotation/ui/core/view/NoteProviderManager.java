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
package org.eclipse.tigerstripe.annotation.ui.core.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class NoteProviderManager {

	private static final String NOTE_EXTPT = "org.eclipse.tigerstripe.annotation.ui.noteProviders";
	private static final String NOTE_ATTR_CLASS = "class";

	public static INoteProvider[] createProviders() {
		IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(NOTE_EXTPT);
		List<INoteProvider> providers = new ArrayList<INoteProvider>();
		for (IConfigurationElement config : configs) {
			try {
				INoteProvider provider = (INoteProvider) config
						.createExecutableExtension(NOTE_ATTR_CLASS);
				providers.add(provider);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return providers.toArray(new INoteProvider[providers.size()]);
	}

}
