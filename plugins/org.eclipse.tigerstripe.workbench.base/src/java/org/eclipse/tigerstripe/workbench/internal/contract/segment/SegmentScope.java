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

	private List<ScopeStereotypePattern> stereotypePatterns = new ArrayList<ScopeStereotypePattern>();

	private List<ScopeAnnotationPattern> annotationPatterns = new ArrayList<ScopeAnnotationPattern>();
	
	private List<ScopeAnnotationPattern> annotationContext = new ArrayList<ScopeAnnotationPattern>();

	public void addStereotypePattern(ScopeStereotypePattern pattern) {
		if (!stereotypePatterns.contains(pattern)) {
			stereotypePatterns.add(pattern);
		}
	}

	public void addAnnotationPattern(ScopeAnnotationPattern pattern) {
		if (!annotationPatterns.contains(pattern)) {
			annotationPatterns.add(pattern);
		}
	}

	public void addAnnotationContextPattern(ScopeAnnotationPattern pattern) {
		if (!annotationContext.contains(pattern)) {
			annotationContext.add(pattern);
		}
	}

	public void addPattern(ScopePattern pattern) {
		if (!patterns.contains(pattern)) {
			patterns.add(pattern);
		}
	}
	
	// Inspired by bugzilla 322400
	public boolean containsPattern(ScopePattern pattern) {
		return patterns.contains(pattern);
	}

	public ScopeStereotypePattern[] getStereotypePatterns() {
		return stereotypePatterns
				.toArray(new ScopeStereotypePattern[stereotypePatterns.size()]);
	}

	public ScopeAnnotationPattern[] getAnnotationPatterns() {
		return annotationPatterns
				.toArray(new ScopeAnnotationPattern[annotationPatterns.size()]);
	}

	public ScopeAnnotationPattern[] getAnnotationContextPatterns() {
		return annotationContext
				.toArray(new ScopeAnnotationPattern[annotationContext.size()]);
	}

	public ScopePattern[] getPatterns() {
		return patterns.toArray(new ScopePattern[patterns.size()]);
	}

	public ScopeStereotypePattern[] getStereotypePatterns(int type) {
		List<ScopeStereotypePattern> subList = new ArrayList<ScopeStereotypePattern>();
		for (ScopeStereotypePattern pattern : stereotypePatterns) {
			if (pattern.type == type) {
				subList.add(pattern);
			}
		}
		return subList.toArray(new ScopeStereotypePattern[subList.size()]);
	}

	public ScopeAnnotationPattern[] getAnnotationPatterns(int type) {
		List<ScopeAnnotationPattern> subList = new ArrayList<ScopeAnnotationPattern>();
		for (ScopeAnnotationPattern pattern : annotationPatterns) {
			if (pattern.type == type) {
				subList.add(pattern);
			}
		}
		return subList.toArray(new ScopeAnnotationPattern[subList.size()]);
	}

	public ScopeAnnotationPattern[] getAnnotationContextPatterns(int type) {
		List<ScopeAnnotationPattern> subList = new ArrayList<ScopeAnnotationPattern>();
		for (ScopeAnnotationPattern pattern : annotationContext) {
			if (pattern.type == type) {
				subList.add(pattern);
			}
		}
		return subList.toArray(new ScopeAnnotationPattern[subList.size()]);
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
		stereotypePatterns.clear();
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
		if (stereotypePatterns.contains(pattern)) {
			stereotypePatterns.remove(pattern);
		}
	}

	public void removeAnnotationPattern(ScopeAnnotationPattern pattern) {
		if (annotationPatterns.contains(pattern)) {
			annotationPatterns.remove(pattern);
		}
	}

	public void removeAnnotationContextPattern(ScopeAnnotationPattern pattern) {
		if (annotationContext.contains(pattern)) {
			annotationContext.remove(pattern);
		}
	}

}
