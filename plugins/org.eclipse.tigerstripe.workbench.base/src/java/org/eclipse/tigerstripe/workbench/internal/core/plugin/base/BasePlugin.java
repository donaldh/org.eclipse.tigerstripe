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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.PluginLogger;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M1RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginBody;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.util.VelocityContextUtil;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog.LogLevel;

/**
 * @author Eric Dillon
 * 
 * TODO: this is only temporary until the plugin manager is fully operational
 */
public abstract class BasePlugin implements PluginBody {

	private File defaultDestination;

	private Writer defaultWriter;

	private VelocityContext defaultVContext;

	/**
	 * Sets the default destination file path
	 * 
	 * @param path
	 * @throws IOException
	 *             if the file cannot be written
	 */
	protected void setDefaultDestination(PluginConfig ref, File file,
			M1RunConfig config) throws IOException {

		final String baseDir;
		if (config != null && config.getAbsoluteOutputDir() != null) {
			baseDir = config.getAbsoluteOutputDir();
		} else if(ref.getProjectHandle().getLocation() == null) {
			baseDir = ref.getProjectHandle().getLocation().toOSString();
		} else {
			throw new IOException("Project Directory is NULL");
		}
		File targetInCorrectPath = new File(baseDir + File.separator
				+ file.getPath());
		this.defaultDestination = targetInCorrectPath;

		// Do we need to create the target dir ?
		if (!this.defaultDestination.getParentFile().exists()) {
			this.defaultDestination.getParentFile().mkdirs();
		}

		this.defaultWriter = new FileWriter(this.defaultDestination);
	}

	protected File getDefaultDestination() {
		return this.defaultDestination;
	}

	/**
	 * Returns the default writer for this plugin
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	protected Writer getDefaultWriter() throws TigerstripeException {
		if (this.defaultWriter == null)
			throw new TigerstripeException("No default writer.");

		return this.defaultWriter;
	}


	/**
	 * Returns the default velocity context.
	 * 
	 * @return VelocityContext - the default context
	 */
	protected VelocityContext getDefaultContext() {
		if (this.defaultVContext == null) {
			this.defaultVContext = new VelocityContext();
			VelocityContextUtil util = new VelocityContextUtil();
			this.defaultVContext.put("util", util);
		}

		return this.defaultVContext;
	}

	public LogLevel getDefaultLogLevel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLogPath() {
		String logPath = getLabel();
		if (logPath == null || logPath.length() == 0) {
			logPath = PluginLogger.DEFAULT_PATH;
		}
		return logPath;
	}

	public boolean isLogEnabled() {
		return false;
	}

	public void setLogEnabled(boolean isLogEnabled) {

	}
}