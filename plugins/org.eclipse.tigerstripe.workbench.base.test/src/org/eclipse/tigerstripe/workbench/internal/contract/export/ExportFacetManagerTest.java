/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.internal.contract.export;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.export.IExportFacetManager;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ExportFacetManagerTest extends TestCase {

	private static final String ACTIVE_FACET_NAME = "activeFacet.wfc";
	private static final String EXPORT_FACET_NAME = "exportFacet.wfc";

	private IExportFacetManager facetManager;

	private ITigerstripeModelProject project;

	protected void setUp() throws Exception {
		project = ModelProjectHelper.createModelProject("TestFacetModelExport", false);
		facetManager = new ExportFacetManager(project);
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
