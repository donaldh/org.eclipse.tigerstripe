/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.annotation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

public class TigerstripeChanges {
	private String name;
	private URI uri;
	private Set<URI> allAffectedUris;

	public TigerstripeChanges(IModelComponent element)
			throws TigerstripeException {
		name = element.getName();
		uri = RefactoringUtil.getUri(element);
		allAffectedUris = new HashSet<URI>();
		RefactoringUtil.collectAllUris(element, allAffectedUris);
	}

	public Map<URI, URI> getChanges(IModelComponent element) {
		return getChanges(RefactoringUtil.getUri(element));
	}

	public Map<URI, URI> getChanges(URI newUri) {
		Map<URI, URI> changes = new HashMap<URI, URI>();
		if (newUri != null) {
			String oldPrefix = uri.toString();
			String newPrefix = newUri.toString();
			if (newPrefix != null)
				collectChanges(oldPrefix, newPrefix, changes);
		}
		return changes;
	}

	private void collectChanges(String oldPrefix, String newPrefix,
			Map<URI, URI> changes) {
		for (URI oldUri : allAffectedUris) {
			URI newUri = URI.createURI(oldUri.toString().replace(oldPrefix,
					newPrefix));
			// String[] segments = oldUri.segments();
			// String last = segments[segments.length - 1];
			// segments[segments.length - 1] = last.replace(oldPrefix,
			// newPrefix);
			// URI newUri = URI.createHierarchicalURI(oldUri.scheme(), null,
			// null,
			// segments, null, oldUri.fragment());
			changes.put(oldUri, newUri);
		}
	}

	public String getOldName() {
		return name;
	}

	public URI getOldUri() {
		return uri;
	}
}
