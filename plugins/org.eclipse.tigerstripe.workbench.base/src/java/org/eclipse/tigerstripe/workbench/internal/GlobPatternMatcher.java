package org.eclipse.tigerstripe.workbench.internal;

import java.util.regex.Pattern;

public class GlobPatternMatcher {
	
	private final Pattern pattern;

	public GlobPatternMatcher(String pattern) {
		this.pattern = compilePattern(pattern);
	}

	private static Pattern compilePattern(String pattern) {
		return Pattern.compile(createRegexpFromGlob(pattern),
				Pattern.CASE_INSENSITIVE);
	}

	private static String createRegexpFromGlob(String pattern) {
		return pattern.replace("*", ".*") + ".*";
				
	}

	public boolean matches(String input) {
		return pattern.matcher(input).matches();
	}
}