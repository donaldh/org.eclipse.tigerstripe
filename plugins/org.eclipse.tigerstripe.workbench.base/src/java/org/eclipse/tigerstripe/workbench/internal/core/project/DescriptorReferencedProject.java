package org.eclipse.tigerstripe.workbench.internal.core.project;

import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.workbench.project.IDescriptorReferencedProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class DescriptorReferencedProject implements IDescriptorReferencedProject {
	private ITigerstripeModelProject project;
	private String name;
	

	
	
	public ITigerstripeModelProject getProject() {
		
		return this.project;
	}

	public String getProjectName() {
		
		return this.name;
	}
	
	

	public void setProject(ITigerstripeModelProject project) {
		this.project = project;

	}

	public void setProjectName(String name) {
		this.name = name;

	}

}
