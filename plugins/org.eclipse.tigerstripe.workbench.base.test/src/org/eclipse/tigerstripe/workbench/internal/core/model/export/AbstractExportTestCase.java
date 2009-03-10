/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Jim Strawn
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.internal.core.model.export;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

import junit.framework.TestCase;

public class AbstractExportTestCase extends TestCase {

	@SuppressWarnings("deprecation")
	protected void verifyProjectArtifactNotExported(ITigerstripeModelProject project, String fullyQualifiedName) throws Exception {
		
		IAbstractArtifact artifact = project.getArtifactManagerSession().getArtifactByFullyQualifiedName(fullyQualifiedName);
		assertNull(artifact);
	}

	@SuppressWarnings("deprecation")
	protected void verifyProjectArtifact(ITigerstripeModelProject project, String fullyQualifiedName) throws TigerstripeException {
	
		IAbstractArtifact artifact = project.getArtifactManagerSession().getArtifactByFullyQualifiedName(fullyQualifiedName);
		IResource res = (IResource) artifact.getAdapter(IResource.class);
		assertNotNull(res);
		IProject proj = res.getProject();
		IProject proj2 = (IProject) project.getAdapter(IProject.class);
		assertTrue(proj.equals(proj2));
	}

	protected void addIncludesFacetScopePatterns(List<String> patterns, IContractSegment facet) throws CoreException, TigerstripeException {
	
		ISegmentScope scope = facet.getISegmentScope();
		for (Iterator<String> iterator = patterns.iterator(); iterator.hasNext();) {
	
			String patternStr = (String) iterator.next();
			ScopePattern pattern = new ISegmentScope.ScopePattern();
			pattern.type = ISegmentScope.INCLUDES;
			pattern.pattern = patternStr;
			scope.addPattern(pattern);
			facet.doSave();
		}
	}

	protected void addExcludesFacetScopePatterns(List<String> patterns, IContractSegment facet) throws TigerstripeException {
	
		ISegmentScope scope = facet.getISegmentScope();
		for (Iterator<String> iterator = patterns.iterator(); iterator.hasNext();) {
	
			String patternStr = (String) iterator.next();
			ScopePattern pattern = new ISegmentScope.ScopePattern();
			pattern.type = ISegmentScope.EXCLUDES;
			pattern.pattern = patternStr;
			scope.addPattern(pattern);
			facet.doSave();
		}
	}

	protected boolean artifactExistsByFqn(List <IAbstractArtifact> artifacts, String fqn) {
		
		for (IAbstractArtifact artifact: artifacts) {
				
			if(artifact.getFullyQualifiedName().equals(fqn)) {
				return true;
			}
		}
		return false;
	}

}
