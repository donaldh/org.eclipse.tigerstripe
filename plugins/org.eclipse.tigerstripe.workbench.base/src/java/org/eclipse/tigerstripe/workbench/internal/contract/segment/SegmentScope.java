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
package org.eclipse.tigerstripe.workbench.internal.contract.segment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;

public class SegmentScope implements ISegmentScope {

	private List<ScopePattern> patterns = new ArrayList<ScopePattern>();

	private List<ScopeStereotypePattern> annotationPatterns = new ArrayList<ScopeStereotypePattern>();

	public void addStereotypePattern(ScopeStereotypePattern pattern) {
		if (!annotationPatterns.contains(pattern)) {
			annotationPatterns.add(pattern);
		}
	}

	public void addPattern(ScopePattern pattern) {
		if (!patterns.contains(pattern)) {
			patterns.add(pattern);
		}
	}

	public ScopeStereotypePattern[] getStereotypePatterns() {
		return annotationPatterns
				.toArray(new ScopeStereotypePattern[annotationPatterns.size()]);
	}

	public ScopePattern[] getPatterns() {
		return patterns.toArray(new ScopePattern[patterns.size()]);
	}

	public ScopeStereotypePattern[] getStereotypePatterns(int type) {
		List<ScopeStereotypePattern> subList = new ArrayList<ScopeStereotypePattern>();
		for (ScopeStereotypePattern pattern : annotationPatterns) {
			if (pattern.type == type) {
				subList.add(pattern);
			}
		}
		return subList.toArray(new ScopeStereotypePattern[subList.size()]);
	}

	public ScopePattern[] getPatterns(int type) {
		List<ScopePattern> subList = new ArrayList<ScopePattern>();
		for (ScopePattern pattern : patterns) {
			if (pattern.type == type) {
				subList.add(pattern);
			}
		}
		return subList.toArray(new ScopePattern[subList.size()]);
	}

	public void clear() {
		patterns.clear();
		annotationPatterns.clear();
	}

	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	public void removePattern(ScopePattern pattern) {
		if (patterns.contains(pattern)) {
			patterns.remove(pattern);
		}
	}

	public void removeStereotypePattern(ScopeStereotypePattern pattern) {
		if (annotationPatterns.contains(pattern)) {
			annotationPatterns.remove(pattern);
		}
	}

}
