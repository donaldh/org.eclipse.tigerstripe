package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IFileExtensionBasedAuditor {

	public String getFileExtension();

	public void run( IProject project, List<IResource> resourcesToCheck, IProgressMonitor monitor);
	
}
