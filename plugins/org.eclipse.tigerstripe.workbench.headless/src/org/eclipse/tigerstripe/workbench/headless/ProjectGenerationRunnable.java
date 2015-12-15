package org.eclipse.tigerstripe.workbench.headless;

import java.util.concurrent.Callable;

import org.eclipse.core.resources.IProject;

public class ProjectGenerationRunnable implements Callable<Boolean> {

	private IProject project;

	public ProjectGenerationRunnable(IProject project) {
		this.project = project;
	}

	public Boolean call() throws Exception {
		GenerationUtils.generate(project);
		return true;
	}
}
