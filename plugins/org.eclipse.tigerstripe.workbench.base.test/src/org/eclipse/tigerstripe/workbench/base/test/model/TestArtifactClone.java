/* ====================================================================
O *   Copyright 2007 Tigerstripe, Inc.
 *
 *   Licensed under the TigerStripe(tm) License (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.tigerstripesoftware.com/licenses/TIGERSTRIPE_LICENSE.txt
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * 
 * ====================================================================
 */
package org.eclipse.tigerstripe.workbench.base.test.model;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Helper class for multiple actions on Artifacts
 * 
 * @author Eric Dillon
 * 
 */
public class TestArtifactClone extends TestCase {

	private ITigerstripeModelProject projectSrc;
	private ITigerstripeModelProject projectDst;

	/**
	 * Creates 2 projects, a src project with a couple of Artifacts, and a
	 * Destination empty project.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void setUp() throws Exception {
		projectSrc = ModelProjectHelper.createModelProject(
				"TestProjectClose_src", true);
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDst = (ITigerstripeModelProject) TigerstripeCore.createProject(
				"TestProjectClose_dst", projectDetails, null,
				ITigerstripeModelProject.class, null, null);
	}

	@Override
	protected void tearDown() throws Exception {
		if (projectSrc != null && projectSrc.exists())
			projectSrc.delete(true, null);
		if (projectDst != null && projectDst.exists())
			projectDst.delete(true, null);
	}

	/**
	 * 	Copies M1 from src project to dst project
	 * @param project
	 */
	public void testArtifactClone() throws Exception {
		assertNotNull(projectSrc);
		assertNotNull(projectDst);
		
		IAbstractArtifact M1Src = projectSrc.getArtifactManagerSession().getArtifactByFullyQualifiedName(ModelProjectHelper.M1);
		assertNotNull(M1Src);
		
		IAbstractArtifact clonedM1 = ((AbstractArtifact) M1Src).makeWorkingCopy(null);
		projectDst.getArtifactManagerSession().addArtifact(clonedM1);
		clonedM1.doSave(null);
		
		// check that the file was saved in the dst project
		IResource res = (IResource) clonedM1.getAdapter(IResource.class);
		assertNotNull(res);
		IProject proj = res.getProject();
		IProject proj2 = (IProject) projectDst.getAdapter(IProject.class);
		assertTrue(proj.equals(proj2));
	}

}
