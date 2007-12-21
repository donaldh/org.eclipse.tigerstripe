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

import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.util.TSJellyTask;

public class CopyClassesJellyTask extends TSJellyTask {

	private final static String SCRIPT = "org/eclipse/tigerstripe/core/module/packaging/scripts/copyClasses.jelly";

	private File tmpDir;
	private File classesDir;

	public CopyClassesJellyTask(File tmpDir, File classesDir) {
		super();
		this.tmpDir = tmpDir;
		this.classesDir = classesDir;
	}

	@Override
	public void run() {
		try {
			setVariable("tmp.module.path", tmpDir.getAbsolutePath());
			setVariable("classes.dir.path", classesDir.getAbsolutePath());
			runScript(SCRIPT);
		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		}
	}
}
