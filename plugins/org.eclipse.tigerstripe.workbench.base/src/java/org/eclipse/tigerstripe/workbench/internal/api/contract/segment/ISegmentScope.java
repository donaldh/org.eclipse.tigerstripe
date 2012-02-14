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
package org.eclipse.tigerstripe.workbench.internal.api.contract.segment;

import java.util.Comparator;

/**
 * A Segment Scope defines a scope within a ITigerstripeProject that will be
 * used in a Segment as part of a Contract.
 * 
 * @author Eric Dillon
 * 
 */
public interface ISegmentScope {

	public final static int INCLUDES = 0;

	public final static int EXCLUDES = 1;

	public enum PatternReqLevel {
		UNKNOWN, INFORMATIONAL, OPTIONAL, MANDATORY
	}

	public enum Kind {
		PATTERN, STEREOTYPE, ANNOTATION, ANNOTATION_CONTEXT;
	}
	
	public class BaseScopePattern {
		public int type = INCLUDES;
	}
	
	/**
	 * A scope pattern is defined as a string (pattern itself) and a flag
	 * whether this is an include or exclude pattern
	 * 
	 * The requirementLevel is only relevant if the pattern doesn't contain
	 * wildcards or regexp
	 */
	public class ScopePattern extends BaseScopePattern {

		public String pattern;

		public PatternReqLevel requirementLevel;

		public ScopePattern() {

		}

		/**
		 * Returns true if the pattern is an explicity FQN (i.e. it doesn't
		 * contain wildcards)
		 * 
		 * @return
		 */
		public boolean isFQN() {
			return pattern != null && pattern.indexOf("*") == -1;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ScopePattern) {
				ScopePattern other = (ScopePattern) obj;
				if (other.pattern == null)
					return false;
				return other.type == type && other.pattern.equals(pattern)
						&& other.requirementLevel == requirementLevel;
			}
			return false;
		}
	}

	public class ScopeAnnotationPattern extends BaseScopePattern {

		public String annotationID;

		public ScopeAnnotationPattern() {

		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ScopeAnnotationPattern) {
				ScopeAnnotationPattern other = (ScopeAnnotationPattern) obj;
				if (other.annotationID == null)
					return false;
				return other.type == type
						&& other.annotationID.equals(annotationID);
			}
			return false;
		}

	}

	public class ScopeStereotypePattern extends BaseScopePattern {

		public String stereotypeName;

		public ScopeStereotypePattern() {

		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ScopeStereotypePattern) {
				ScopeStereotypePattern other = (ScopeStereotypePattern) obj;
				if (other.stereotypeName == null)
					return false;
				return other.type == type
						&& other.stereotypeName.equals(stereotypeName);
			}
			return false;
		}
	}

	/**
	 * Return true if this segment scope is valid
	 * 
	 * @return
	 */
	public boolean isValid();

	/**
	 * Returns all of a ScopePatterns regardless of there type
	 * 
	 * @return
	 */
	public ScopePattern[] getPatterns();

	public ScopeStereotypePattern[] getStereotypePatterns();

	public ScopeAnnotationPattern[] getAnnotationPatterns();

	public ScopeAnnotationPattern[] getAnnotationContextPatterns();

	/**
	 * Returns all the ScopePatterns of the specified type
	 * 
	 * @param type
	 * @return
	 */
	public ScopePattern[] getPatterns(int type);

	public void addPattern(ScopePattern pattern);

	public void removePattern(ScopePattern pattern);

	public ScopeStereotypePattern[] getStereotypePatterns(int type);

	public void addStereotypePattern(ScopeStereotypePattern pattern);

	public void removeStereotypePattern(ScopeStereotypePattern pattern);

	public ScopeAnnotationPattern[] getAnnotationPatterns(int type);

	public void addAnnotationPattern(ScopeAnnotationPattern pattern);

	public void removeAnnotationPattern(ScopeAnnotationPattern pattern);

	public ScopeAnnotationPattern[] getAnnotationContextPatterns(int type);

	public void addAnnotationContextPattern(ScopeAnnotationPattern pattern);

	public void removeAnnotationContextPattern(ScopeAnnotationPattern pattern);

	public boolean containsPattern(ScopePattern pattern);

	public void sort(int type, Comparator<Object> comparator,
			ISegmentScope.Kind kind);
}
