package org.eclipse.tigerstripe.postgeneration.sample;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.generation.GenerateCompleteListenerRunStatus;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.IGenerateCompleteListener;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Invoked on a successful generation
 * This class moves the following file from user's workspace: 
 * /SampleProject/Source/source.xml  TO  /SampleProject/Target
 *
 * If the project or folders do not exist, they're created.  If 'source.xml' already
 * exists under /Sample/Target, it's removed before the move.
 * 
 * @author Navid Mehregani
 *
 */
public class PostSuccessfulGeneration implements IGenerateCompleteListener {

	@Override
	public GenerateCompleteListenerRunStatus run(GenerateCompletionStatus status, 
			ITigerstripeModelProject project, PluginRunStatus[] runStatus) {
	
		final String PROJECT_NAME = "SampleProject";
		final String SOURCE_FOLDER = "Source";
		final String TARGET_FOLDER = "Target";
		final String SOURCE_FILE   = "source.xml";
		final String SOURCE_FILE_CONTENT = "";
		final String TARGET_FILE_PATH = PROJECT_NAME + "/" + TARGET_FOLDER + "/" + SOURCE_FILE;
		
		if (!GenerateCompletionStatus.SUCCESS.equals(status)) {
			Activator.logErrorMessage("Something is wrong! This should only get invoked on failed generators");
			return null;
		}
		
		IProgressMonitor monitor = new NullProgressMonitor();
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject sampleProject = workspace.getRoot().getProject(PROJECT_NAME);
		
		try {
			if (!sampleProject.exists()) {
				IProjectDescription description = workspace.newProjectDescription(PROJECT_NAME);
				sampleProject.create(description,monitor);
			}
			
			sampleProject.open(monitor);
			IFolder sourceFolder = sampleProject.getFolder(SOURCE_FOLDER);
			if (!sourceFolder.exists())
				sourceFolder.create(false, true, monitor);
			
			IFile sourceFile = sourceFolder.getFile(SOURCE_FILE);
			if (!sourceFile.exists()) 
				sourceFile.create(new ByteArrayInputStream(SOURCE_FILE_CONTENT.getBytes()), true, monitor);
			
			IFolder targetFolder = sampleProject.getFolder(TARGET_FOLDER);
			if (!targetFolder.exists())
				targetFolder.create(false, true, monitor);
			
			IFile targetFile = targetFolder.getFile(SOURCE_FILE);
			if (targetFile.exists())
				targetFile.delete(true, monitor);
			
			sourceFile.move(new Path("/" + TARGET_FILE_PATH), true, monitor);
			
			sampleProject.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			
		} catch (CoreException e) {
			Activator.logErrorMessage(e.getMessage(), e);
			return null;
		}
		
		return null;
	}

}
