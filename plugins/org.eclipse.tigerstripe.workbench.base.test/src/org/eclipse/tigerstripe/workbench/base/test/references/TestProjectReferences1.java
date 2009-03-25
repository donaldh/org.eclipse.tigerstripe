package org.eclipse.tigerstripe.workbench.base.test.references;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.workbench.IWorkingCopy;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

import junit.framework.TestCase;

public class TestProjectReferences1 extends TestCase {
	
	public void clearErrorLog() {
		Platform.getLogFileLocation().toFile().delete();
	}

	
	
	public final void testProjectReferences1() throws TigerstripeException, CoreException, IOException, InterruptedException {
		clearErrorLog();
		ITigerstripeModelProject aProject = createTigerstripeModelProject("aProject");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject bProject = createTigerstripeModelProject("bProject");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		
		ITigerstripeModelProject wc = (ITigerstripeModelProject) aProject.makeWorkingCopy(new NullProgressMonitor());
		wc.addReferencedProject(bProject);
		wc.commit(new NullProgressMonitor());
		assertTrue(aProject.getReferencedProjects().length == 1);
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();		
		IResource projb = workspace.getRoot().findMember("bProject");
		assertNotNull(projb);
		
		projb.delete(true, new NullProgressMonitor());
		projb = workspace.getRoot().findMember("bProject");
		assertNull(projb);
		
		checkErrorLog();
	}
	
	public void checkErrorLog() throws IOException, InterruptedException {
		File file= Platform.getLogFileLocation().toFile();
		if (file.exists()) {
			FileReader fileReader= new FileReader(file);
			char[] buffer= new char[(int) file.length()];
			fileReader.read(buffer);
			fileReader.close();
			fail(new String(buffer));
		}
	}
	
	protected ITigerstripeModelProject createTigerstripeModelProject(String projectName)
	throws TigerstripeException {
        IProjectDetails details = TigerstripeCore.makeProjectDetails();
        ITigerstripeModelProject aProject = (ITigerstripeModelProject) TigerstripeCore.createProject(projectName, details, null, ITigerstripeModelProject.class, null, new NullProgressMonitor());
        return aProject;
}

}
