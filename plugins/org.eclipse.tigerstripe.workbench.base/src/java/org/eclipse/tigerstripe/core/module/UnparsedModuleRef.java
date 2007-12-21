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
package org.eclipse.tigerstripe.core.module;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.tigerstripe.api.modules.IModuleRef;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

/**
 * An unparsed module ref is a Module where only the header is parsed. This
 * allows to speed up the GUI in a number of instances
 * 
 * @author eric
 * 
 */
public class UnparsedModuleRef extends ModuleRef implements IModuleRef {

	public UnparsedModuleRef(URI jarURI, ITigerstripeProgressMonitor monitor)
			throws InvalidModuleException {
		super(jarURI, monitor);
	}

	@Override
	protected void parseTSModuleDescriptor(TigerstripeProject embeddedProject,
			ITigerstripeProgressMonitor monitor) throws InvalidModuleException,
			IOException {
		JarFile file = new JarFile(this.jarURI.getPath());
		JarEntry tsModuleEntry = file
				.getJarEntry(ModuleDescriptorModel.DESCRIPTOR);

		if (tsModuleEntry == null)
			throw new InvalidModuleException("can't find "
					+ ModuleDescriptorModel.DESCRIPTOR + " in "
					+ this.jarURI.getPath());

		InputStream stream = file.getInputStream(tsModuleEntry);
		Reader reader = new InputStreamReader(stream, "UTF-8");
		model = new ModuleDescriptorModel(embeddedProject, reader, false,
				monitor);
		file.close();
	}

}
