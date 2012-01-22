package org.eclipse.tigerstripe.workbench.headless.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.TarEntry;
import org.eclipse.ui.internal.wizards.datatransfer.ZipLeveledStructureProvider;

@SuppressWarnings("restriction")
public class ProjectImport {

	public static IProject doImport(ZipFile sourceFile) {
		final ZipLeveledStructureProvider structureProvider = new ZipLeveledStructureProvider(sourceFile);
		ZipEntry root = (ZipEntry) structureProvider.getRoot();
		final ZipEntry projectEntry = (ZipEntry) structureProvider.getChildren(root).get(0);
		structureProvider.setStrip(1);
		
		String projectName = findProjectName(projectEntry, structureProvider);
		
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		
		try {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					ImportOperation operation = new ImportOperation(project.getFullPath(),
							projectEntry, structureProvider, null);
					operation.execute(monitor);
				}
			}, new NullProgressMonitor());
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
		return project;
	}
	
	private static String findProjectName(Object entry, ZipLeveledStructureProvider structureProvider) {
		for (Object o : structureProvider.getChildren(entry)) {
			if (structureProvider.isFolder(o)) {
				continue;
			}
			if (IProjectDescription.DESCRIPTION_FILE_NAME.equals(structureProvider.getLabel(o))) {
				return getProjectName(o, structureProvider);
			}
		}
		throw new RuntimeException("Couldn't find project name");
	}
	
	private static String getProjectName(Object projectArchiveFile, ZipLeveledStructureProvider structureProvider) {
		String projectName = null;
		try {
			if (projectArchiveFile != null) {
				InputStream stream = structureProvider
						.getContents(projectArchiveFile);

				// If we can get a description pull the name from there
				if (stream == null) {
					if (projectArchiveFile instanceof ZipEntry) {
						IPath path = new Path(
								((ZipEntry) projectArchiveFile).getName());
						projectName = path.segment(path.segmentCount() - 2);
					} else if (projectArchiveFile instanceof TarEntry) {
						IPath path = new Path(
								((TarEntry) projectArchiveFile).getName());
						projectName = path.segment(path.segmentCount() - 2);
					}
				} else {
					IProjectDescription description = IDEWorkbenchPlugin.getPluginWorkspace()
							.loadProjectDescription(stream);
					stream.close();
					projectName = description.getName();
				}
			}
		} catch (CoreException e) {
			// no good couldn't get the name
		} catch (IOException e) {
			// no good couldn't get the name
		}
		return projectName;
	}
}
