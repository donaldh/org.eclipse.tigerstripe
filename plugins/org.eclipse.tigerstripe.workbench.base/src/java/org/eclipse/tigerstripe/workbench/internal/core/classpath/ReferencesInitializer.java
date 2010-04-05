package org.eclipse.tigerstripe.workbench.internal.core.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * This class used to initialize model references for tigerstripe model projects
 */
public class ReferencesInitializer extends ClasspathContainerInitializer {

	/**
	 * Create references container for specified TS model project
	 * 
	 * @param javaProject
	 * @return new references classpath container
	 * @throws TigerstripeException
	 */
	public static ReferencesClasspathContainer createContainer(
			IJavaProject javaProject) throws TigerstripeException {
		IAbstractTigerstripeProject project = TigerstripeCore
				.findProject(javaProject.getProject().getLocation());
		if (project instanceof ITigerstripeModelProject) {
			return new ReferencesClasspathContainer(
					(ITigerstripeModelProject) project);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.ClasspathContainerInitializer#initialize(org.eclipse
	 * .core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	public void initialize(IPath containerPath, IJavaProject javaProject)
			throws CoreException {
		try {
			ReferencesClasspathContainer container = createContainer(javaProject);
			if (container == null) {
				throw newNoModelProjectException(javaProject, null);
			}
			JavaCore.setClasspathContainer(
					IReferencesConstants.REFERENCES_CONTAINER_PATH,
					new IJavaProject[] { javaProject },
					new IClasspathContainer[] { container }, null);
		} catch (TigerstripeException e) {
			throw newNoModelProjectException(javaProject, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.ClasspathContainerInitializer#getComparisonID(org
	 * .eclipse.core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	public Object getComparisonID(IPath containerPath, IJavaProject project) {
		if (containerPath == null || project == null)
			return null;

		return containerPath.segment(0) + "/" + project.getPath().segment(0); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.ClasspathContainerInitializer#getDescription(org
	 * .eclipse.core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	public String getDescription(IPath containerPath, IJavaProject project) {
		return IReferencesConstants.CONTAINER_DESCRIPTION;
	}

	private CoreException newNoModelProjectException(IJavaProject javaProject,
			TigerstripeException e) {
		return new CoreException(new Status(Status.ERROR, BasePlugin.PLUGIN_ID,
				"'" + javaProject.getElementName()
						+ "' is not a tigerstripe model project", e));
	}
}
