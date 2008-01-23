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
package org.eclipse.tigerstripe.workbench.internal.contract;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.internal.core.util.RegExpFQNSetPred;

/**
 * Utils class related to Contracts and ContractSegments
 * 
 * @author Eric Dillon
 * 
 */
public class ContractUtils {

	public static RegExpFQNSetPred getPredicateForScope(ISegmentScope scope)
			throws TigerstripeException {
		RegExpFQNSetPred pred = new RegExpFQNSetPred();
		for (ScopePattern pattern : scope.getPatterns()) {
			if (pattern.type == ISegmentScope.INCLUDES) {
				pred.addIsIncludedPattern(mapFromUserPattern(pattern.pattern));
			} else {
				pred.addIsExcludedPattern(mapFromUserPattern(pattern.pattern));
			}
		}

		return pred;
	}

	// public static Predicate getPredicateFor(IContractSegment facet)
	// throws TigerstripeException {
	// List<IContractSegment> segments = new ArrayList<IContractSegment>();
	// segments.add(facet);
	// return getPredicateFor(segments, LogicalPredicate.OR);
	// }
	//
	// private static Predicate getPredicateFor(List<IContractSegment> segments,
	// String andOrSemantics) {
	// Predicate result = null;
	// if (segments == null || segments.size() == 0) {
	// return null;
	// }
	//
	// if (segments.size() == 1) {
	// RegExpFQNSetPred pred = new RegExpFQNSetPred();
	// IContractSegment seg = segments.get(0);
	// for (ScopePattern pattern : seg.getISegmentScope().getPatterns()) {
	// if (pattern.type == ISegmentScope.INCLUDES) {
	// pred
	// .addIsIncludedPattern(mapFromUserPattern(pattern.pattern));
	// } else {
	// pred
	// .addIsExcludedPattern(mapFromUserPattern(pattern.pattern));
	// }
	// }
	// result = pred;
	// } else {
	// LogicalPredicate pred = new LogicalPredicate(andOrSemantics);
	// for (IContractSegment segment : segments) {
	// RegExpFQNSetPred rPred = new RegExpFQNSetPred();
	// for (ScopePattern pattern : segment.getISegmentScope()
	// .getPatterns()) {
	// if (pattern.type == ISegmentScope.INCLUDES) {
	// rPred
	// .addIsIncludedPattern(mapFromUserPattern(pattern.pattern));
	// } else {
	// rPred
	// .addIsExcludedPattern(mapFromUserPattern(pattern.pattern));
	// }
	// }
	// pred.add(rPred);
	// }
	// result = pred;
	// }
	// return result;
	// }
	//
	public static String mapFromUserPattern(final String userPattern) {
		String result = "";
		for (int index = 0; index < userPattern.length(); index++) {
			char c = userPattern.charAt(index);
			if (c == '.') {
				result += "[.]";
			} else if (c == '*') {
				result += ".*";
			} else if (c == '?') {
				result += ".";
			} else {
				result += c;
			}
		}
		return result;
	}
}
