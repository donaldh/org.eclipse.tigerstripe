/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.utils;

import java.util.Collection;

/**
 * Static utility methods for defensive programming.
 */
public final class CheckUtils {

	public static <T> T notNull(T value, String parameterName) {
		if (value == null)
			throw new IllegalArgumentException(parameterName
					+ " parameter was null.");

		return value;
	}

	public static Collection<?> notEmpty(Collection<?> value,
			String parameterName) {
		if (value.isEmpty())
			throw new IllegalArgumentException(parameterName
					+ " parameter was empty.");

		return value;
	}

	public static <T> T[] notEmpty(T[] value, String parameterName) {
		if (value.length == 0)
			throw new IllegalArgumentException(parameterName
					+ " parameter was empty.");

		return value;
	}

	public static String notBlank(String value, String parameterName) {
		if (value != null) {
			String trimmedValue = value.trim();

			if (!trimmedValue.equals(""))
				return trimmedValue;
		}

		throw new IllegalArgumentException(parameterName
				+ " parameter was blank.");
	}

	public static <T> T cast(Object parameterValue, Class<T> type,
			String parameterName) {
		notNull(parameterValue, parameterName);

		if (!type.isInstance(parameterValue))
			throw new IllegalArgumentException(String.format(
					"Can't cast parameter %s=%s to %s", parameterName,
					parameterValue, type));

		return type.cast(parameterValue);
	}
}
