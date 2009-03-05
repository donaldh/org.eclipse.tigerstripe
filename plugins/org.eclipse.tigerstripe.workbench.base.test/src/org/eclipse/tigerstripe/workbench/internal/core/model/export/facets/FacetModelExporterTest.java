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

package org.eclipse.tigerstripe.workbench.internal.core.model.export.facets;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class FacetModelExporterTest extends TestCase {

	private static final String PROJECT_FACET = "default.wfc";

	private ITigerstripeModelProject source;

	private ITigerstripeModelProject destination;

	protected void setUp() throws Exception {

		source = ModelProjectHelper.createModelProject("SourceModelProject", true);
		destination = ModelProjectHelper.createEmptyModelProject("DestinationModelProject");
	}

	protected void tearDown() throws Exception {

		if (source != null && source.exists()) {
			source.delete(true, null);
		}

		if (destination != null && destination.exists()) {
			destination.delete(true, null);
		}
	}

	public void testExportNoFacet() throws Exception {

		IProject iProject = (IProject) source.getAdapter(IProject.class);
		IFile facet = iProject.getFile(PROJECT_FACET);

		FacetModelExportInputManager inputManager = new FacetModelExportInputManager();
		inputManager.setSource(source);
		inputManager.setDestination(destination);
		inputManager.setFacet(facet);
		
		try {
			FacetModelExporter.export(inputManager, new NullProgressMonitor());
		} catch (IllegalArgumentException e) {
			assertEquals("The facet file must exist in the file system: " + facet.getFullPath(), e.getMessage());
			return;
		}

		fail("Expected exception due to facet file null or not on disk");

	}

	public void testExportNullFacet() throws Exception {

		FacetModelExportInputManager inputManager = new FacetModelExportInputManager();
		inputManager.setSource(source);
		inputManager.setDestination(destination);
		
		try {
			FacetModelExporter.export(inputManager, new NullProgressMonitor());
		} catch (IllegalArgumentException e) {
			assertEquals("The facet file may not be null.", e.getMessage());
			return;
		}

		fail("Expected exception due to facet file null or not on disk");

	}

	public void testExportNullSourceProject() throws Exception {

		IProject iProject = (IProject) source.getAdapter(IProject.class);
		IFile facet = iProject.getFile(PROJECT_FACET);
		InternalTigerstripeCore.createModelFacet(facet, new NullProgressMonitor());

		FacetModelExportInputManager inputManager = new FacetModelExportInputManager();
		inputManager.setDestination(destination);
		inputManager.setFacet(facet);
		
		try {
			FacetModelExporter.export(inputManager, new NullProgressMonitor());
		} catch (IllegalArgumentException e) {
			assertEquals("The source project may not be null.", e.getMessage());
			return;
		}

		fail("Expected exception due to null source project");

	}

	public void testExportNullDestinationProject() throws Exception {

		IProject iProject = (IProject) source.getAdapter(IProject.class);
		IFile facet = iProject.getFile(PROJECT_FACET);
		InternalTigerstripeCore.createModelFacet(facet, new NullProgressMonitor());

		FacetModelExportInputManager inputManager = new FacetModelExportInputManager();
		inputManager.setSource(source);
		inputManager.setFacet(facet);
		
		try {
			FacetModelExporter.export(inputManager, new NullProgressMonitor());
		} catch (IllegalArgumentException e) {
			assertEquals("The destination project may not be null.", e.getMessage());
			return;
		}

		fail("Expected exception due to null destination project");

	}

	public void testExportAll() throws Exception {

		// set up source project (specifically, facet)
		IProject iProject = (IProject) source.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { "*" }), facet);

		FacetModelExportInputManager inputManager = new FacetModelExportInputManager();
		inputManager.setSource(source);
		inputManager.setDestination(destination);
		inputManager.setFacet(facetFile);
		
		FacetModelExporter.export(inputManager, new NullProgressMonitor());
		
		verifyProjectArtifact(destination, ModelProjectHelper.M1);
		verifyProjectArtifact(destination, ModelProjectHelper.M2);
		verifyProjectArtifact(destination, ModelProjectHelper.M3);
		verifyProjectArtifact(destination, ModelProjectHelper.AC1);
		verifyProjectArtifact(destination, ModelProjectHelper.AS1);
	}

	public void testExportModelSubset() throws Exception {

		IProject iProject = (IProject) source.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.M1, ModelProjectHelper.M3 }), facet);
		addExcludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.M2, ModelProjectHelper.AC1, ModelProjectHelper.AS1 }), facet);

		FacetModelExportInputManager inputManager = new FacetModelExportInputManager();
		inputManager.setSource(source);
		inputManager.setDestination(destination);
		inputManager.setFacet(facetFile);
		
		FacetModelExporter.export(inputManager, new NullProgressMonitor());

		verifyProjectArtifact(destination, ModelProjectHelper.M1);
		verifyProjectArtifact(destination, ModelProjectHelper.M3);
		verifyProjectArtifactNotExported(destination, ModelProjectHelper.M2);
		verifyProjectArtifactNotExported(destination, ModelProjectHelper.AC1);
		verifyProjectArtifactNotExported(destination, ModelProjectHelper.AS1);

	}

	public void testExportModelAssociation() throws Exception {
		
		IProject iProject = (IProject) source.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.AS1 }), facet);
		
		FacetModelExportInputManager inputManager = new FacetModelExportInputManager();
		inputManager.setSource(source);
		inputManager.setDestination(destination);
		inputManager.setFacet(facetFile);
		
		FacetModelExporter.export(inputManager, new NullProgressMonitor());
		
		verifyProjectArtifact(destination, ModelProjectHelper.M1);
		verifyProjectArtifact(destination, ModelProjectHelper.M2);
		verifyProjectArtifact(destination, ModelProjectHelper.M3);
		verifyProjectArtifact(destination, ModelProjectHelper.AC1);
		verifyProjectArtifact(destination, ModelProjectHelper.AS1);
		
	}
	
	public void testExportModelExcludeAssociation() throws Exception {
		
		IProject iProject = (IProject) source.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.M1 }), facet);
		addExcludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.AS1 }), facet);
		
		FacetModelExportInputManager inputManager = new FacetModelExportInputManager();
		inputManager.setSource(source);
		inputManager.setDestination(destination);
		inputManager.setFacet(facetFile);
		
		FacetModelExporter.export(inputManager, new NullProgressMonitor());
		
		verifyProjectArtifact(destination, ModelProjectHelper.AC1);
		verifyProjectArtifact(destination, ModelProjectHelper.M1);
		verifyProjectArtifact(destination, ModelProjectHelper.M2);
		verifyProjectArtifactNotExported(destination, ModelProjectHelper.AS1);
		verifyProjectArtifactNotExported(destination, ModelProjectHelper.M3);
		
	}

	public void testExportModelExcludeAssociationClass() throws Exception {
		
		IProject iProject = (IProject) source.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.M1 }), facet);
		addExcludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.AC1 }), facet);
		
		FacetModelExportInputManager inputManager = new FacetModelExportInputManager();
		inputManager.setSource(source);
		inputManager.setDestination(destination);
		inputManager.setFacet(facetFile);
		
		FacetModelExporter.export(inputManager, new NullProgressMonitor());
		verifyProjectArtifact(destination, ModelProjectHelper.M1);
		verifyProjectArtifactNotExported(destination, ModelProjectHelper.AC1);
		verifyProjectArtifactNotExported(destination, ModelProjectHelper.M2);
		verifyProjectArtifactNotExported(destination, ModelProjectHelper.AS1);
		verifyProjectArtifactNotExported(destination, ModelProjectHelper.M3);
		
	}

	@SuppressWarnings("deprecation")
	private void verifyProjectArtifact(ITigerstripeModelProject project, String fullyQualifiedName) throws TigerstripeException {

		IAbstractArtifact artifact = project.getArtifactManagerSession().getArtifactByFullyQualifiedName(fullyQualifiedName);
		IResource res = (IResource) artifact.getAdapter(IResource.class);
		assertNotNull(res);
		IProject proj = res.getProject();
		IProject proj2 = (IProject) project.getAdapter(IProject.class);
		assertTrue(proj.equals(proj2));
	}

	@SuppressWarnings("deprecation")
	private void verifyProjectArtifactNotExported(ITigerstripeModelProject project, String fullyQualifiedName) throws Exception {
		
		IAbstractArtifact artifact = project.getArtifactManagerSession().getArtifactByFullyQualifiedName(fullyQualifiedName);
		assertNull(artifact);
	}

	private void addIncludesFacetScopePatterns(List<String> patterns, IContractSegment facet) throws CoreException, TigerstripeException {

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
	
	private void addExcludesFacetScopePatterns(List<String> patterns, IContractSegment facet) throws TigerstripeException {

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
}
