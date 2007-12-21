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

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.util.TSJellyTask;

public class CopyFilesetToDirJellyTask extends TSJellyTask {

	private final static String SCRIPT = "org/eclipse/tigerstripe/core/module/packaging/scripts/copyFilesetToDir.jelly";

	private String includes;

	private File toDir;
	private File srcDir;

	public CopyFilesetToDirJellyTask(File toDir, File srcDir, String includes) {
		super();
		this.includes = includes;
		this.toDir = toDir;
		this.srcDir = srcDir;
	}

	@Override
	public void run() throws TigerstripeException {
		try {
			setVariable("includes", includes);
			setVariable("toDir", toDir.getAbsolutePath());
			setVariable("srcDir", srcDir.getAbsolutePath());
			runScript(SCRIPT);
		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			throw new TigerstripeException("While executing copy: "
					+ e.getMessage(), e);
		}
	}
}
