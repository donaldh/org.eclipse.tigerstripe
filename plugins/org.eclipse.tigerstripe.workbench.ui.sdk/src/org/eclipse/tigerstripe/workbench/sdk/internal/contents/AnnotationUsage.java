package org.eclipse.tigerstripe.workbench.sdk.internal.contents;

import org.eclipse.core.resources.IResource;

public class AnnotationUsage {

	public AnnotationUsage(IResource resource, IContribution contribution) {
		super();
		this.resource = resource;
		this.contribution = contribution;
	}
	private IResource resource;
	private IContribution contribution;
	
	
	public IResource getResource() {
		return resource;
	}
	public void setResource(IResource resource) {
		this.resource = resource;
	}
	public IContribution getContribution() {
		return contribution;
	}
	public void setContribution(IContribution contribution) {
		this.contribution = contribution;
	}
	
}
