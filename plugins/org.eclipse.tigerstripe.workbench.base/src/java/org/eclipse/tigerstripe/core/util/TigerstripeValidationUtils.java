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

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class TigerstripeValidationUtils {

	/** static final patterns used to validate fields in Eclipse forms */
	public static final Pattern classNamePattern = compilePattern("^[A-Z_][a-z0-9A-Z_]*$");
	public static final Pattern elementNamePattern = compilePattern("^[a-z_][a-z0-9A-Z_]*$");
	public static final Pattern allUppercase = compilePattern("^[A-Z_][A-Z0-9_]*$");
	public static final Pattern literalNamePattern = compilePattern("^[A-Za-z_][A-Za-z0-9_]*$");
	public static final Pattern packageNamePattern = compilePattern("^[A-Za-z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");
	public static final List keywordList = buildKeywordList();

	private TigerstripeValidationUtils() {
	}

	private static Pattern compilePattern(String pattern) {
		return Pattern.compile(pattern);
	}

	private static List buildKeywordList() {
		String[] strArray = new String[] { "abstract", "continue", "for",
				"new", "switch", "assert", "default", "goto", "package",
				"synchronized", "boolean", "do", "if", "private", "this",
				"break", "double", "implements", "protected", "throw", "byte",
				"else", "import", "public", "throws", "case", "enum",
				"instanceof", "return", "transient", "catch", "extends", "int",
				"short", "try", "char", "final", "interface", "static", "void",
				"class", "finally", "long", "strictfp", "volatile", "const",
				"float", "native", "super", "while", "true", "false", "null" };
		// convert to list of keywords
		return Arrays.asList(strArray);
	}

}
