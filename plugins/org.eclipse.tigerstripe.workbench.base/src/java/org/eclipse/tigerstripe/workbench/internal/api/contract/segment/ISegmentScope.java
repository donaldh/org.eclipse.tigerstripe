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

	/**
	 * A scope pattern is defined as a string (pattern itself) and a flag
	 * whether this is an include or exclude pattern
	 * 
	 * The requirementLevel is only relevant if the pattern doesn't contain
	 * wildcards or regexp
	 */
	public class ScopePattern {
		public int type = INCLUDES;

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

	public class ScopeStereotypePattern {

		public int type = INCLUDES;

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

}
