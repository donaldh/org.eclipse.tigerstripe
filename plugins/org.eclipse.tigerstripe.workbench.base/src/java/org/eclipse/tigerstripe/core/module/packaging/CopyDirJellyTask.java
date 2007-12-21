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

public class CopyDirJellyTask extends TSJellyTask {

	private final static String SCRIPT = "org/eclipse/tigerstripe/core/module/packaging/scripts/copyDir.jelly";

	private File srcDir;

	private File toDir;

	public CopyDirJellyTask(File toDir, File srcFile) {
		super();
		this.srcDir = srcFile;
		this.toDir = toDir;
	}

	@Override
	public void run() throws TigerstripeException {
		try {
			if (srcDir.isDirectory()) {
				setVariable("srcDir", srcDir.getAbsolutePath());
				setVariable("toDir", toDir.getAbsolutePath());
				runScript(SCRIPT);
			}
		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			throw new TigerstripeException("While executing copy: "
					+ e.getMessage(), e);
		}
	}
}
