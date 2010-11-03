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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.facets.FacetExporterInput;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestExportDiff extends AbstractExportTestCase {

	private static final String PROJECT_FACET = "default.wfc";

	private IFile facetFile;

	private ITigerstripeModelProject source;

	private ITigerstripeModelProject destination;

	private ITigerstripeModelProject destinationFullContent;

	protected void setUp() throws Exception {

		source = ModelProjectHelper.createModelProject("SourceProject", true);
		destination = ModelProjectHelper.createModelProject("DestinationProject", false);

		IProject iProject = (IProject) source.getAdapter(IProject.class);
		facetFile = iProject.getFile(PROJECT_FACET);
	}

	protected void tearDown() throws Exception {

		if (source != null && source.exists()) {
			source.delete(true, new NullProgressMonitor());
			source = null;
		}

		if (destination != null && destination.exists()) {
			destination.delete(true, new NullProgressMonitor());
			destination = null;
		}

	}

	public void testGetArtifactsListIncludeAll() throws Exception {

		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { "*" }), facet);

		FacetExporterInput inputManager = new FacetExporterInput();
		inputManager.setSource(source);
		inputManager.setDestination(destination);
		inputManager.setFacet(facetFile);

		List<IAbstractArtifact> artifacts = ExportDiff.getDuplicates(inputManager);
		assertNotNull("Artifacts list is null", artifacts);
		String list = "";
		for(IAbstractArtifact art : artifacts){
			list = list + art+ " ";
		}
		
		System.out.println(list);
		
		assertEquals(4, artifacts.size()); // two for pkg, two artifacts

		assertTrue(artifactExistsByFqn(artifacts, "com"));
		assertTrue(artifactExistsByFqn(artifacts, "com.mycompany"));
		assertTrue(artifactExistsByFqn(artifacts, ModelProjectHelper.M1));
		assertTrue(artifactExistsByFqn(artifacts, ModelProjectHelper.M2));
		assertFalse(artifactExistsByFqn(artifacts, ModelProjectHelper.M3));
		assertFalse(artifactExistsByFqn(artifacts, ModelProjectHelper.AC1));
		assertFalse(artifactExistsByFqn(artifacts, ModelProjectHelper.AS1));

	}

	public void testGetArtifactsListWithEmptyDestination() throws Exception {

		// overwrite destination project
		if (destination != null && destination.exists()) {
			destination.delete(true, new NullProgressMonitor());
			destination = null;
		}
		
		destination = ModelProjectHelper.createEmptyModelProject("DestinationProject", null);

		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { "*" }), facet);

		FacetExporterInput inputManager = new FacetExporterInput();
		inputManager.setSource(source);
		inputManager.setDestination(destination);
		inputManager.setFacet(facetFile);
		List<IAbstractArtifact> artifacts = ExportDiff.getDuplicates(inputManager);

		assertNotNull("Artifacts list is null", artifacts);
		assertEquals(0, artifacts.size());

	}

	public void testGetArtifactsListRestrictedByFacet() throws Exception {

		System.out.println("JS-DEBUG testGetArtifactsListRestrictedByFacet");
		
		// overwrite destination project
		if (destination != null && destination.exists()) {
			destination.delete(true, new NullProgressMonitor());
			destination = null;
		}
		
		destination = ModelProjectHelper.createModelProject("DestinationProject", true);

		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.M1 }), facet);
		addExcludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.AC1 }), facet);

		FacetExporterInput inputManager = new FacetExporterInput();
		inputManager.setSource(source);
		inputManager.setDestination(destination);
		inputManager.setFacet(facetFile);

		List<IAbstractArtifact> artifacts = ExportDiff.getDuplicates(inputManager);
		
		assertNotNull("Artifacts list is null", artifacts);
		
		String list = "";
		for(IAbstractArtifact art : artifacts){
			list = list + art + " ";
		}
		
		// System.out.println(list);
		
		assertEquals("Expected 3 - got these " + list, 3, artifacts.size()); 

		assertTrue(artifactExistsByFqn(artifacts, "com"));
		assertTrue(artifactExistsByFqn(artifacts, "com.mycompany"));
		assertTrue(artifactExistsByFqn(artifacts, ModelProjectHelper.M1));
		
		assertFalse(artifactExistsByFqn(artifacts, ModelProjectHelper.M2));
		assertFalse(artifactExistsByFqn(artifacts, ModelProjectHelper.M3));
		assertFalse(artifactExistsByFqn(artifacts, ModelProjectHelper.AC1));
		assertFalse(artifactExistsByFqn(artifacts, ModelProjectHelper.AS1));

		// System.out.println("JS-DEBUG testGetArtifactsListRestrictedByFacet END");
		
	}

}
