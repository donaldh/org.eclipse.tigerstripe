package org.eclipse.tigerstripe.workbench.internal.core.model.export.facets;

import java.io.File;
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

	private ITigerstripeModelProject sourceProject;

	private ITigerstripeModelProject destinationProject;

	protected void setUp() throws Exception {

		sourceProject = ModelProjectHelper.createModelProject("SourceModelProject", true);
		destinationProject = ModelProjectHelper.createEmptyModelProject("DestinationModelProject");
	}

	protected void tearDown() throws Exception {

		if (sourceProject != null && sourceProject.exists()) {
			sourceProject.delete(true, null);
		}

		if (destinationProject != null && destinationProject.exists()) {
			destinationProject.delete(true, null);
		}
	}

	public void testExportNoFacet() throws Exception {

		IProject iProject = (IProject) sourceProject.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);

		FacetModelExporter exporter = new FacetModelExporter(sourceProject, destinationProject, facetFile);
		try {
			exporter.export(false, new NullProgressMonitor());
		} catch (TigerstripeException e) {
			assertEquals("The facet file " + sourceProject.getFullPath() + File.separator + PROJECT_FACET + " does not exist", e.getMessage());
			return;
		}

		fail("Expected exception due to facet file null or not on disk");

	}

	public void testExportNullFacet() throws Exception {

		FacetModelExporter exporter = new FacetModelExporter(sourceProject, destinationProject, null);
		try {
			exporter.export(false, new NullProgressMonitor());
		} catch (TigerstripeException e) {
			assertEquals("The facet file *null* does not exist", e.getMessage());
			return;
		}

		fail("Expected exception due to facet file null or not on disk");

	}

	public void testExportNullSourceProject() throws Exception {

		IProject iProject = (IProject) sourceProject.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		InternalTigerstripeCore.createModelFacet(facetFile, null);

		FacetModelExporter exporter = new FacetModelExporter(null, destinationProject, facetFile);
		try {
			exporter.export(false, new NullProgressMonitor());
		} catch (TigerstripeException e) {
			assertEquals("The source project does not exist", e.getMessage());
			return;
		}

		fail("Expected exception due to source project file null or not on disk");

	}

	public void testExportNullDestinationProject() throws Exception {

		IProject iProject = (IProject) sourceProject.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		InternalTigerstripeCore.createModelFacet(facetFile, null);

		FacetModelExporter exporter = new FacetModelExporter(sourceProject, null, facetFile);
		try {
			exporter.export(false, new NullProgressMonitor());
		} catch (TigerstripeException e) {
			assertEquals("The destination project does not exist", e.getMessage());
			return;
		}

		fail("Expected exception due to project file null or not on disk");

	}

	public void testExportAll() throws Exception {

		// set up source project (specifically, facet)
		IProject iProject = (IProject) sourceProject.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { "*" }), facet);

		FacetModelExporter exporter = new FacetModelExporter(sourceProject, destinationProject, facetFile);
		exporter.export(false, new NullProgressMonitor());

		verifyProjectArtifact(destinationProject, ModelProjectHelper.M1);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.M2);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.M3);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.AC1);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.AS1);
	}

	public void testExportModelSubset() throws Exception {

		IProject iProject = (IProject) sourceProject.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.M1, ModelProjectHelper.M3 }), facet);
		addExcludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.M2, ModelProjectHelper.AC1, ModelProjectHelper.AS1 }), facet);

		FacetModelExporter exporter = new FacetModelExporter(sourceProject, destinationProject, facetFile);
		exporter.export(false, new NullProgressMonitor());

		verifyProjectArtifact(destinationProject, ModelProjectHelper.M1);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.M3);

		verifyProjectArtifactNotExported(destinationProject, ModelProjectHelper.M2);
		verifyProjectArtifactNotExported(destinationProject, ModelProjectHelper.AC1);
		verifyProjectArtifactNotExported(destinationProject, ModelProjectHelper.AS1);

	}

	public void testExportModelAssociation() throws Exception {
		
		IProject iProject = (IProject) sourceProject.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.AS1 }), facet);
		
		FacetModelExporter exporter = new FacetModelExporter(sourceProject, destinationProject, facetFile);
		exporter.export(false, new NullProgressMonitor());
		
		verifyProjectArtifact(destinationProject, ModelProjectHelper.M1);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.M2);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.M3);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.AC1);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.AS1);
		
	}
	
	public void testExportModelExcludeAssociation() throws Exception {
		
		IProject iProject = (IProject) sourceProject.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.M1 }), facet);
		addExcludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.AS1 }), facet);
		
		FacetModelExporter exporter = new FacetModelExporter(sourceProject, destinationProject, facetFile);
		exporter.export(false, new NullProgressMonitor());
		
		verifyProjectArtifact(destinationProject, ModelProjectHelper.AC1);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.M1);
		verifyProjectArtifact(destinationProject, ModelProjectHelper.M2);
		verifyProjectArtifactNotExported(destinationProject, ModelProjectHelper.AS1);
		verifyProjectArtifactNotExported(destinationProject, ModelProjectHelper.M3);
		
	}

	public void testExportModelExcludeAssociationClass() throws Exception {
		
		IProject iProject = (IProject) sourceProject.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(PROJECT_FACET);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(facetFile, new NullProgressMonitor());
		addIncludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.M1 }), facet);
		addExcludesFacetScopePatterns(Arrays.asList(new String[] { ModelProjectHelper.AC1 }), facet);
		
		FacetModelExporter exporter = new FacetModelExporter(sourceProject, destinationProject, facetFile);
		exporter.export(false, new NullProgressMonitor());
		
		verifyProjectArtifact(destinationProject, ModelProjectHelper.M1);
		verifyProjectArtifactNotExported(destinationProject, ModelProjectHelper.AC1);
		verifyProjectArtifactNotExported(destinationProject, ModelProjectHelper.M2);
		verifyProjectArtifactNotExported(destinationProject, ModelProjectHelper.AS1);
		verifyProjectArtifactNotExported(destinationProject, ModelProjectHelper.M3);
		
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
