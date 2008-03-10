package org.eclipse.tigerstripe.workbench.headless;

import java.io.File;

import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

public class ProjectRecord {
	
	File projectSystemFile;

	String projectName;

	Object parent;

	int level;

	IProjectDescription description;

	/**
	 * Create a record for a project based on the info in the file.
	 * 
	 * @param file
	 */
	ProjectRecord(File file) {
		projectSystemFile = file;
		setProjectName();
	}

	/**
	 * Set the name of the project based on the projectFile.
	 */
	private void setProjectName() {
		
		try {
			
			IPath path = new Path(projectSystemFile.getPath());
			// if the file is in the default location, use the directory name as the project name
			if (isDefaultLocation(path)) {
				projectName = path.segment(path.segmentCount() - 2);
				description = ResourcesPlugin.getWorkspace().newProjectDescription(projectName);
			} else {
				projectName = projectSystemFile.getParentFile().getName();
				description = ResourcesPlugin.getWorkspace().loadProjectDescription(path);
			}
			
		} catch (CoreException e) {
			// no good couldn't get the name
		} 
	}
	
	/**
	 * Returns whether the given project description file path is in the default
	 * location for a project
	 * 
	 * @param path
	 *            The path to examine
	 * @return Whether the given path is the default location for a project
	 */
	private boolean isDefaultLocation(IPath path) {
		// The project description file must at least be within the project,
		// which is within the workspace location
		if (path.segmentCount() < 2)
			return false;
		return path.removeLastSegments(2).toFile().equals(
				Platform.getLocation().toFile());
	}

	/**
	 * Get the name of the project
	 * 
	 * @return String
	 */
	public String getProjectName() {
		return projectName;
	}
	
}
