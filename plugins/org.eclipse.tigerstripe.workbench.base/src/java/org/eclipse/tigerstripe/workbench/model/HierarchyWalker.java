/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class HierarchyWalker {

	private final boolean includeExtended;
	private final boolean includeImplemented;

	public HierarchyWalker(boolean includeExtended, boolean includeImplemented) {
		this.includeExtended = includeExtended;
		this.includeImplemented = includeImplemented;
	}

	public HierarchyWalker() {
		this(true, true);
	}

	public void accept(IAbstractArtifact artifact, IHierarchyVisitor visitor) {
		fetchHierarchy(visitor, artifact, new HashSet<String>());
	}

	private void fetchHierarchy(IHierarchyVisitor visitor,
			IAbstractArtifact artifact, Set<String> fqns) {
		if (artifact == null) {
			return;
		}
		String fqn = artifact.getFullyQualifiedName();
		if (!fqns.add(fqn)) {
			return;
		}

		if (!visitor.accept(artifact)) {
			return;
		}

		if (includeExtended) {
			IAbstractArtifact extended = artifact.getExtendedArtifact();
			if (extended != null) {
				fetchHierarchy(visitor, extended, fqns);
			}
		}

		if (includeImplemented) {
			for (IAbstractArtifact impl : artifact.getImplementedArtifacts()) {
				fetchHierarchy(visitor, impl, fqns);
			}
		}
	}
}
