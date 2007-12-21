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
package org.eclipse.tigerstripe.core.module.packaging;

import java.io.File;
import java.net.URI;

import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.util.TSJellyTask;

public class CreateJarJellyTask extends TSJellyTask {

	private final static String SCRIPT = "org/eclipse/tigerstripe/core/module/packaging/scripts/createJar.jelly";

	private URI jarURI;
	private File tmpDir;

	public CreateJarJellyTask(URI jarURI, File tmpDir) {
		super();
		this.jarURI = jarURI;
		this.tmpDir = tmpDir;
	}

	@Override
	public void run() {
		try {
			setVariable("module.jar.path", jarURI.getPath());
			setVariable("tmp.module.path", tmpDir.getAbsolutePath());
			runScript(SCRIPT);
		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		}
	}
}
