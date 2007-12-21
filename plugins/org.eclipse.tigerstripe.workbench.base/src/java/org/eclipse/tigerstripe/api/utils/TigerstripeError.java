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
package org.eclipse.tigerstripe.api.utils;

import java.util.Collection;
import java.util.Iterator;

public class TigerstripeError {

	private TigerstripeErrorLevel errorLevel = null;
	private String errorMessage;
	private Exception exception;

	public TigerstripeError(TigerstripeErrorLevel errorLevel,
			String errorMessage) {
		this.errorLevel = errorLevel;
		this.errorMessage = errorMessage;
	}

	public void setCorrespondingException(Exception exception) {
		this.exception = exception;
	}

	public Exception getCorrespondingException() {
		return this.exception;
	}

	public TigerstripeErrorLevel getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(TigerstripeErrorLevel errorLevel) {
		this.errorLevel = errorLevel;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public static boolean errorExists(Collection<TigerstripeError> c) {
		for (Iterator<TigerstripeError> iter = c.iterator(); iter.hasNext();)
			if (iter.next().errorLevel == TigerstripeErrorLevel.ERROR)
				return true;
		return false;
	}

	public static boolean warningExists(Collection<TigerstripeError> c) {
		for (Iterator<TigerstripeError> iter = c.iterator(); iter.hasNext();)
			if (iter.next().errorLevel == TigerstripeErrorLevel.WARNING)
				return true;
		return false;
	}
}
