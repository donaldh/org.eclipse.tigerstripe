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

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Helper class for multiple actions on Artifacts
 * 
 * @author Eric Dillon
 * 
 */
public class ArtifactTestHelper {

	private ITigerstripeModelProject project;
	private IArtifactManagerSession session;

	public ArtifactTestHelper(ITigerstripeModelProject project) {
		Assert.assertNotNull(project);
		this.project = project;
		try {
			this.session = project.getArtifactManagerSession();
		} catch (TigerstripeException t) {
			t.printStackTrace();
		}
	}

	/**
	 * Creates the given artifact, saves it on to disk and checks that it was
	 * indeed saved properly.
	 * 
	 * @param artifactType
	 * @param name
	 * @param packageName
	 * @return
	 * @throws TigerstripeException
	 */
	public IAbstractArtifact createArtifact(String artifactType, String name,
			String packageName) throws TigerstripeException {
		IAbstractArtifact artifact = session.makeArtifact(artifactType);
		Assert.assertNotNull("Couldn't make artifact: " + artifactType,
				artifact);
		artifact.setName(name);
		artifact.setPackage(packageName);

		artifact.doSave(new NullProgressMonitor());

		String artifactPath = artifact.getArtifactPath();
		File artFile = new File(project.getLocation().toFile()
				.getAbsolutePath()
				+ File.separator + artifactPath);
		Assert.assertTrue(artFile.exists());
		Assert.assertTrue("Artifact " + artifact.getArtifactType()
				+ " write error", artFile.length() != 0);
		return artifact;
	}

	public void remove(IAbstractArtifact artifact) throws TigerstripeException {
		Assert.assertNotNull(artifact);
		String artifactPath = artifact.getArtifactPath();
		File artFile = new File(project.getLocation().toFile()
				.getAbsolutePath()
				+ File.separator + artifactPath);
		session.removeArtifact(artifact);

		// At this stage the POJO has not been removed yet!
		// FIXME shouldn't the remove method delete the POJO too????
		Assert.assertTrue("POJO for " + artifact.getFullyQualifiedName()
				+ " doesnt exist anymore", artFile.exists());

		boolean result = artFile.delete();
		Assert.assertTrue("Couldnt delete POJO" + artifact.getArtifactType()
				+ " : " + artifact.getFullyQualifiedName() + ","
				+ artFile.getAbsolutePath(), result);
	}
}
