package org.eclipse.tigerstripe.workbench.internal.core.model.export.facets;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.IModelExporterFacetManager;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class FacetModelExporterFacetManagerTest extends TestCase {

	private static final String ACTIVE_FACET_NAME = "activeFacet.wfc";
	private static final String EXPORT_FACET_NAME = "exportFacet.wfc";

	private IModelExporterFacetManager facetManager;

	private ITigerstripeModelProject project;

	protected void setUp() throws Exception {
		project = ModelProjectHelper.createModelProject("TestFacetModelExport", false);
		facetManager = new FacetModelExporterFacetManager(project);
	}

	protected void tearDown() throws Exception {
		facetManager = null;
		if (project != null && project.exists()) {
			project.delete(true, null);
		}
	}

	public void testApplyExportFacet() throws Exception {

		IProject iProject = (IProject) project.getAdapter(IProject.class);

		project.setActiveFacet(getFacetReference(ACTIVE_FACET_NAME), null);
		assertNotNull(project.getActiveFacet());
		assertEquals("Unexpected active facet!", project.getActiveFacet().getProjectRelativePath(), ACTIVE_FACET_NAME);

		facetManager.applyExportFacet(iProject.getFile(EXPORT_FACET_NAME), null);
		assertNotNull(project.getActiveFacet());
		assertEquals("Unexpected active facet!", project.getActiveFacet().getProjectRelativePath(), EXPORT_FACET_NAME);

	}
	
	public void testRestoreActiveFacet() throws Exception {
		
		IProject iProject = (IProject) project.getAdapter(IProject.class);

		project.setActiveFacet(getFacetReference(ACTIVE_FACET_NAME), null);
		assertNotNull(project.getActiveFacet());
		assertEquals("Unexpected active facet!", project.getActiveFacet().getProjectRelativePath(), ACTIVE_FACET_NAME);

		facetManager.applyExportFacet(iProject.getFile(EXPORT_FACET_NAME), null);
		assertNotNull(project.getActiveFacet());
		assertEquals("Unexpected active facet!", project.getActiveFacet().getProjectRelativePath(), EXPORT_FACET_NAME);
		
		facetManager.restoreActiveFacet(null);
		assertNotNull(project.getActiveFacet());
		assertEquals("Unexpected active facet!", project.getActiveFacet().getProjectRelativePath(), ACTIVE_FACET_NAME);
	}

	public void testRestoreActiveFacet_NoActiveFacet() throws Exception {
		
		IProject iProject = (IProject) project.getAdapter(IProject.class);

		assertNull("Unexpected active facet!", project.getActiveFacet());

		facetManager.applyExportFacet(iProject.getFile(EXPORT_FACET_NAME), null);
		assertNotNull(project.getActiveFacet());
		assertEquals("Unexpected active facet!", project.getActiveFacet().getProjectRelativePath(), EXPORT_FACET_NAME);
		
		facetManager.restoreActiveFacet(null);
		assertNull("Unexpected active facet!", project.getActiveFacet());
	}
	
	private IFacetReference getFacetReference(String facetFileName) throws CoreException, TigerstripeException {

		IProject iProject = (IProject) project.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(facetFileName);
		InternalTigerstripeCore.createModelFacet(facetFile, null);
		return project.makeFacetReference(facetFile.getProjectRelativePath().toOSString());
	}
	
}
