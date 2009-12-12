package org.eclipse.tigerstripe.workbench.base.test.references;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.project.IDescriptorReferencedProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestProjectReferences1 extends TestCase {

	public void clearErrorLog() {
		Platform.getLogFileLocation().toFile().delete();
	}

	public final void testProjectReferences1() throws TigerstripeException,
			CoreException, IOException, InterruptedException {
		clearErrorLog();
		ITigerstripeModelProject aProject = createTigerstripeModelProject("aProject");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject bProject = createTigerstripeModelProject("bProject");
		assertTrue(aProject instanceof ITigerstripeModelProject);

		ITigerstripeModelProject wc = (ITigerstripeModelProject) aProject
				.makeWorkingCopy(new NullProgressMonitor());
		wc.addReferencedProject(bProject);
		wc.commit(new NullProgressMonitor());
		assertTrue(aProject.getReferencedProjects().length == 1);

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IResource projb = workspace.getRoot().findMember("bProject");
		assertNotNull(projb);

		projb.delete(true, new NullProgressMonitor());
		projb = workspace.getRoot().findMember("bProject");
		assertNull(projb);

		// / PART 2
		clearErrorLog();
		workspace = ResourcesPlugin.getWorkspace();
		IResource proja = workspace.getRoot().findMember("aProject");
		assertNotNull(proja);

		IPath fp = workspace.getRoot().getLocation()
				.append(proja.getFullPath());
		aProject = (TigerstripeProjectHandle) TigerstripeCore.findProject(fp);

		IDescriptorReferencedProject[] refs = aProject
				.getDescriptorsReferencedProjects();
		assertTrue(refs.length == 1);
		assertTrue(refs[0].getProjectName().endsWith("bProject"));

		IJavaProject jProject = (IJavaProject) JavaCore.create(proja);
		IClasspathEntry[] entries = jProject.getRawClasspath();
		for (int i = 0; i < entries.length; i++) {
			if (entries[i].getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				int matching = entries[i].getPath().matchingFirstSegments(
						proja.getFullPath());
				String relPath = entries[i].getPath().removeFirstSegments(
						matching).toString();
				assertTrue(relPath.endsWith("bProject"));
			}
		}

		wc = (ITigerstripeModelProject) aProject
				.makeWorkingCopy(new NullProgressMonitor());
		wc.removeReferencedProjects(refs);
		wc.commit(new NullProgressMonitor());
		assertTrue(aProject.getReferencedProjects().length == 0);
		Thread.sleep(10000);
		boolean found = false;
		for (IClasspathEntry e : jProject.getRawClasspath()) {
			if (e.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				found = true;
			}
		}

		assertFalse(found);

		proja.delete(true, new NullProgressMonitor());
		proja = workspace.getRoot().findMember("bProject");
		assertNull(proja);

		checkErrorLog();
	}

	public void checkErrorLog() throws IOException, InterruptedException {
		File file = Platform.getLogFileLocation().toFile();
		if (file.exists()) {
			FileReader fileReader = new FileReader(file);
			char[] buffer = new char[(int) file.length()];
			fileReader.read(buffer);
			fileReader.close();
			fail(new String(buffer));
		}
	}

	protected ITigerstripeModelProject createTigerstripeModelProject(
			String projectName) throws TigerstripeException {
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		ITigerstripeModelProject aProject = (ITigerstripeModelProject) TigerstripeCore
				.createProject(projectName, details, null,
						ITigerstripeModelProject.class, null,
						new NullProgressMonitor());
		return aProject;
	}

}
