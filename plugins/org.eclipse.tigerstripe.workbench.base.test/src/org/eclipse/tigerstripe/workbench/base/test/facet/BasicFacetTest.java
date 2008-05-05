/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.facet;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class BasicFacetTest extends TestCase {

	private ITigerstripeModelProject project;

	@Override
	protected void setUp() throws Exception {
		project = ModelProjectHelper
				.createModelProject("TestProjectGenerationBasics");
	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);
	}

	public void testBasicFacetCreation() throws Exception {
		IProject iProject = (IProject) project.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile("basicFacet");
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(
				facetFile, null);

		assertTrue(facetFile.exists());
		assertNotNull(facet);
	}

	public void testActivateDeactivateFacet() throws Exception {

		IProject iProject = (IProject) project.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile("basicFacet");
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(
				facetFile, null);
		ISegmentScope scope = facet.getISegmentScope();
		ScopePattern pattern = new ISegmentScope.ScopePattern();
		pattern.pattern = ModelProjectHelper.M1;
		pattern.type = ISegmentScope.INCLUDES;
		scope.addPattern(pattern);
		facet.doSave();

		IFacetReference ref = project.makeFacetReference(facetFile
				.getProjectRelativePath().toOSString());

		project.setActiveFacet(ref, null);

		IAbstractArtifact artM1 = project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName(ModelProjectHelper.M1);
		IAbstractArtifact artM2 = project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName(ModelProjectHelper.M2);

		assertTrue(artM1.isInActiveFacet());
		assertFalse(artM2.isInActiveFacet());

		project.resetActiveFacet();

		// When no facet active, everybody is in.
		assertTrue(artM1.isInActiveFacet());
		assertTrue(artM2.isInActiveFacet());
	}
}
