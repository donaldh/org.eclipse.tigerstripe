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
package org.eclipse.tigerstripe.workbench.internal.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class RegExpFQNSetPred implements Predicate {

	private HashSet<Pattern> isIncludedExpSet = new HashSet<Pattern>();
	private HashSet<Pattern> isExcludedExpSet = new HashSet<Pattern>();

	private HashSet<String> isIncludedByAnnotation = new HashSet<String>();
	private HashSet<String> isExcludedByAnnotation = new HashSet<String>();

	private HashMap<String, Pattern> patternsByString = new HashMap<String, Pattern>();
	private Set<String> annotationIds = new HashSet<String>();

	public boolean isEmptyPredicate() {
		return isIncludedExpSet.size() == 0 && isExcludedExpSet.size() == 0
				&& isIncludedByAnnotation.size() == 0
				&& isExcludedByAnnotation.size() == 0;
	}

	public boolean evaluate(Object obj) {
		if (isEmptyPredicate())
			// if there are no rules for what's acceptable and what's not,
			// accept anything
			return true;
		else if (isExcludedExpSet.size() == 0
				&& isExcludedByAnnotation.size() == 0)
			// else if there are no rules for what's unacceptable, look to the
			// rules about what is acceptable to determine what to do
			return isIncluded(obj);
		else if (isIncludedExpSet.size() == 0
				&& isIncludedByAnnotation.size() == 0)
			// else if there are no rules for what's acceptable, look to the
			// rules about what
			// is unacceptable to determine what to do
			return !isExcluded(obj);
		else {
			// else if there are rules for what's both acceptable and
			// unacceptable,
			// see if can find a matching pattern in one ruleset or the other
			boolean included = isIncluded(obj);
			boolean excluded = isExcluded(obj);
			// if is included and not excluded, return true (accept it)
			if (included && !excluded)
				return true;
			// if it's excluded, return false (reject it) regardless of whether
			// or not it's included
			else if (excluded)
				return false;
			// else if it's not included and not excluded, return false (reject
			// it)
			// (note; this is a judgment call, but it's based on the fact that
			// it
			// doesn't fit any of the rules for what's included)
			else
				return false;
		}
	}

	/*
	 * Note; in the four "add" methods for patterns (below), if the pattern
	 * string already exists in this predicate (in either the included or
	 * excluded patterns), then don't add it...it would either be a duplicate
	 * pattern (which would be refused by the set) or create a conflict (since
	 * both the included and excluded expressions would return a match to a
	 * string that matches this pattern)
	 */
	public boolean addIsIncludedPattern(String patternStr) {
		if (containsPattern(patternStr))
			return false;
		Pattern p = Pattern.compile(patternStr);
		patternsByString.put(patternStr, p);
		return isIncludedExpSet.add(p);
	}

	public boolean addIsExcludedPattern(String patternStr) {
		if (containsPattern(patternStr))
			return false;
		Pattern p = Pattern.compile(patternStr);
		patternsByString.put(patternStr, p);
		return isExcludedExpSet.add(p);
	}

	public boolean addIsIncludedByAnnotationPattern(String annotationId) {
		if (containsAnnotationIdPattern(annotationId))
			return false;
		annotationIds.add(annotationId);
		return isIncludedByAnnotation.add(annotationId);
	}

	public boolean addIsExcludedByAnnotationPattern(String annotationId) {
		if (containsAnnotationIdPattern(annotationId))
			return false;
		annotationIds.add(annotationId);
		return isExcludedByAnnotation.add(annotationId);
	}

	/*
	 * the next two methods are used to remove patterns from the Predicate
	 */
	public boolean removeIsIncludedPattern(String patternStr) {
		if (!containsPattern(patternStr))
			return false;
		Pattern p = patternsByString.get(patternStr);
		patternsByString.remove(patternStr);
		return isIncludedExpSet.remove(p);
	}

	public boolean removeIsExcludedPattern(String patternStr) {
		if (!containsPattern(patternStr))
			return false;
		Pattern p = patternsByString.get(patternStr);
		patternsByString.remove(patternStr);
		return isExcludedExpSet.remove(p);
	}

	/*
	 * the following methods are used to get a (read-only) set of the included
	 * or excluded patterns or to check for the existence of patterns in the
	 * included/excluded lists
	 */
	protected Set<Pattern> getIncludedPatterns() {
		return Collections.unmodifiableSet(isIncludedExpSet);
	}

	protected Set<Pattern> getExcludedPatterns() {
		return Collections.unmodifiableSet(isExcludedExpSet);
	}

	protected boolean isIncludedPattern(Pattern p) {
		for (Pattern testPattern : isIncludedExpSet)
			if (testPattern.equals(p.pattern()))
				return true;
		return false;
	}

	protected boolean isExcludedPattern(Pattern p) {
		for (Pattern testPattern : isExcludedExpSet)
			if (testPattern.equals(p.pattern()))
				return true;
		return false;
	}

	// Bug 731: needed that to be public for facet predicate
	public boolean isIncluded(Object obj) {
		if (!(obj instanceof IAbstractArtifact))
			return false;

		IAbstractArtifact artifact = (IAbstractArtifact) obj;

		// look through the patterns...if matches any, then is included
		for (Pattern p : isIncludedExpSet) {
			Matcher m = p.matcher(artifact.getFullyQualifiedName());
			if (m.matches())
				return true;
		}

		for (String annotationId : isIncludedByAnnotation) {

			List<Object> annotations = artifact.getAnnotations("tigerstripe",
					annotationId);
			if (annotations.size() != 0)
				return true;
		}
		return false;
	}

	// Bug 731: needed that to be public for facet predicate
	public boolean isExcluded(Object obj) {
		if (!(obj instanceof IAbstractArtifact))
			return false;

		IAbstractArtifact artifact = (IAbstractArtifact) obj;

		// look through the patterns...if matches any, then is excluded
		for (Pattern p : isExcludedExpSet) {
			Matcher m = p.matcher(artifact.getFullyQualifiedName());
			if (m.matches())
				return true;
		}

		for (String annotationId : isExcludedByAnnotation) {
			List<Object> annotations = artifact.getAnnotations("tigerstripe",
					annotationId);
			if (annotations.size() != 0)
				return true;
		}
		return false;
	}

	/*
	 * internal methods
	 */
	private boolean containsPattern(String patternStr) {
		Set<String> keySet = patternsByString.keySet();
		if (keySet != null && keySet.contains(patternStr))
			return true;
		return false;
	}

	private boolean containsAnnotationIdPattern(String annotationId) {
		return annotationIds.contains(annotationId);
	}

}
