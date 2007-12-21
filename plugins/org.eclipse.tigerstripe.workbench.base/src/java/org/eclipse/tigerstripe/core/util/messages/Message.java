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
package org.eclipse.tigerstripe.core.util.messages;

/**
 * A message as collected from a long running operation
 * 
 * @author Eric Dillon
 * 
 */
public class Message {

	public final static int ERROR = 0;
	public final static int WARNING = 1;
	public final static int INFO = 2;
	public final static int UNKNOWN = 3;

	private final static String[] messageTags = { "ERROR", "WARNING", "INFO",
			"UNKNOWN" };

	private String message;
	private int severity;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	@Override
	public String toString() {
		return messageTags[getSeverity()] + ": " + getMessage();
	}

	public static String severityToString(int severity) {
		return messageTags[severity];
	}
}
