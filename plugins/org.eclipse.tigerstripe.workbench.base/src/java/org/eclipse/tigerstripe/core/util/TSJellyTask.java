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
package org.eclipse.tigerstripe.core.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.XMLOutput;
import org.eclipse.tigerstripe.api.external.TigerstripeException;

public abstract class TSJellyTask {

	private TSJellyContext context;
	private StringWriter writer = new StringWriter();

	public TSJellyTask() {
		super();

		context = new TSJellyContext();
	}

	protected void setVariable(String variableName, String variableValue) {
		this.context.setVariable(variableName, variableValue);
	}

	public abstract void run() throws TigerstripeException;

	public String getOutput() {
		return writer.toString();
	}

	/**
	 * Runs the given script and generate a log file
	 * 
	 * @param scriptPath -
	 *            Looked for on the classpath
	 * @param logPath
	 */
	protected void runScript(String scriptPath) throws FileNotFoundException,
			UnsupportedEncodingException, JellyException {
		writer = new StringWriter();
		XMLOutput xmlOutput = XMLOutput.createXMLOutput(writer);
		URL url = TSJellyTask.class.getClassLoader().getResource(scriptPath);
		context.runScript(url, xmlOutput);
	}

	/**
	 * Runs the given script and generate a log file Variant required to allow
	 * for search on plugin Classpath
	 * 
	 * @param scriptURL
	 * @param logPath
	 */
	protected void runScript(URL scriptURL, String logPath)
			throws FileNotFoundException, UnsupportedEncodingException,
			JellyException {
		OutputStream output = new FileOutputStream(logPath);
		XMLOutput xmlOutput = XMLOutput.createXMLOutput(output);
		;
		context.runScript(scriptURL, xmlOutput);
	}

}
