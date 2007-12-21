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

import java.net.URL;

import org.apache.commons.jelly.JellyContext;

public class TSJellyContext extends JellyContext {

	public TSJellyContext() {
		super();
		initTSContext();
	}

	public TSJellyContext(URL arg0) {
		super(arg0);
		initTSContext();
	}

	public TSJellyContext(URL arg0, URL arg1) {
		super(arg0, arg1);
		initTSContext();
	}

	public TSJellyContext(JellyContext arg0) {
		super(arg0);
		initTSContext();
	}

	public TSJellyContext(JellyContext arg0, URL arg1) {
		super(arg0, arg1);
		initTSContext();
	}

	public TSJellyContext(JellyContext arg0, URL arg1, URL arg2) {
		super(arg0, arg1, arg2);
		initTSContext();
	}

	protected void initTSContext() {
		java.util.Properties p = System.getProperties();
		java.util.Enumeration keys = p.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			this.setVariable(key, p.getProperty(key));
		}
	}

}
