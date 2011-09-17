package org.eclipse.tigerstripe.workbench.base.test.references;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.AbstractTigerstripeTestCase;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestProjectReferences1 extends AbstractTigerstripeTestCase {

	private final static String APROJECT = "aProject1";
	private final static String BPROJECT = "bProject1";
	
	public void clearErrorLog() {
		Platform.getLogFileLocation().toFile().delete();
	}

	public final void testProjectReferences1() throws TigerstripeException,
			CoreException, IOException, InterruptedException {
		clearErrorLog();
		final ITigerstripeModelProject aProject = createTigerstripeModelProject(APROJECT);
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject bProject = createTigerstripeModelProject(BPROJECT);
		assertTrue(bProject instanceof ITigerstripeModelProject);

		ITigerstripeModelProject wc = (ITigerstripeModelProject) aProject
				.makeWorkingCopy(new NullProgressMonitor());
		wc.addModelReference(ModelReference.referenceFromProject(bProject));
		wc.commit(new NullProgressMonitor());
		assertTrue(aProject.getReferencedProjects().length == 1);

		IResource projb = workspace.getRoot().findMember(BPROJECT);
		assertNotNull(projb);

		final ModelReference mRefB = ModelReference.referenceFromProject(bProject);

		projb.delete(true, new NullProgressMonitor());
		projb = workspace.getRoot().findMember(BPROJECT);
		assertNull(projb);

		// / PART 2
		clearErrorLog();
		IResource proja = workspace.getRoot().findMember(APROJECT);
		assertNotNull(proja);

		IPath fp = workspace.getRoot().getLocation()
				.append(proja.getFullPath());
		final ITigerstripeModelProject aProject2 = (TigerstripeProjectHandle) TigerstripeCore.findProjectOrCreate(fp);

		ModelReference[] refs = aProject2.getModelReferences();
		assertTrue(refs.length == 1);
		assertTrue(refs[0].getToModelId().endsWith(BPROJECT));

		IJavaProject jProject = (IJavaProject) JavaCore.create(proja);
		IClasspathEntry[] entries = jProject.getRawClasspath();
		for (int i = 0; i < entries.length; i++) {
			if (entries[i].getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				int matching = entries[i].getPath().matchingFirstSegments(
						proja.getFullPath());
				String relPath = entries[i].getPath().removeFirstSegments(
						matching).toString();
				assertTrue(relPath.endsWith(BPROJECT));
			}
		}

		runInWorkspace(new SafeRunnable() {
			
			public void run() throws Exception {
				final ITigerstripeModelProject wc = (ITigerstripeModelProject) aProject2
						.makeWorkingCopy(new NullProgressMonitor());
				wc.removeModelReference(mRefB);
				wc.commit(new NullProgressMonitor());
			}
		});
		
		waitForUpdates();
		
		assertTrue(aProject2.getModelReferences().length == 0);
		assertTrue(aProject2.getReferencedProjects().length == 0);
		
		waitForUpdates();
		
		boolean found = false;
		for (IClasspathEntry e : jProject.getRawClasspath()) {
			if (e.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				found = true;
			}
		}

		assertFalse(found);

		proja.delete(true, new NullProgressMonitor());
		proja = workspace.getRoot().findMember(BPROJECT);
		assertNull(proja);

//		checkErrorLog();
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

	@Override
	protected void setUp() throws Exception {
		cleanup();
	}

	@Override
	protected void tearDown() throws Exception {
		cleanup();
	}
}
