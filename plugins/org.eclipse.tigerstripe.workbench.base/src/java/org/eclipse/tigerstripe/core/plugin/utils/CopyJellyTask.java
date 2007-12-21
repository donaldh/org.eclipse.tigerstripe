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
package org.eclipse.tigerstripe.core.plugin.utils;

import java.io.File;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.util.TSJellyTask;

public class CopyJellyTask extends TSJellyTask {

	private final static String SCRIPT = "org/eclipse/tigerstripe/core/plugin/utils/scripts/copy.jelly";

	private File srcFile;
	private File toFile;

	public CopyJellyTask(File srcFile, File toFile) {
		super();
		this.srcFile = srcFile;
		this.toFile = toFile;
	}

	@Override
	public void run() throws TigerstripeException {
		try {
			setVariable("srcFile", srcFile.getAbsolutePath());
			setVariable("toFile", toFile.getAbsolutePath());
			runScript(SCRIPT);
		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			throw new TigerstripeException("While executing copy: "
					+ e.getMessage(), e);
		}
	}
}
