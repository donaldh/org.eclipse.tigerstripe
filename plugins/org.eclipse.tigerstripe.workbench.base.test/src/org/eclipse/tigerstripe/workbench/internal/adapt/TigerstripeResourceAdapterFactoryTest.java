package org.eclipse.tigerstripe.workbench.internal.adapt;

import junit.framework.TestCase;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TigerstripeResourceAdapterFactoryTest extends TestCase {

	private ITigerstripeModelProject mdlProject;

	@Override
	protected void setUp() throws Exception {

		mdlProject = ModelProjectHelper.createModelProject("TestProject", true);
	}

	@Override
	protected void tearDown() throws Exception {

		if (mdlProject != null && mdlProject.exists()) {
			mdlProject.delete(true, new NullProgressMonitor());
		}
	}

	public void testIContainerToIPackageArtifact() {

		IProject iProject = (IProject) mdlProject.getAdapter(IProject.class);
		IFolder iFolder =  iProject.getFolder("src/com/mycompany");
		IContainer iContainer = (IContainer) iFolder.getAdapter(IContainer.class);
		IPackageArtifact packageArtifact = (IPackageArtifact) iContainer.getAdapter(IPackageArtifact.class);
		assertNotNull(packageArtifact);
		assertEquals(ModelProjectHelper.DEFAULT_PKG, packageArtifact.getFullyQualifiedName());

	}

}
