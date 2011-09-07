package org.eclipse.tigerstripe.espace.resources.core;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class EObjectAndResource {

	public final EObject eObject;
	public final Resource resource;
	
	public EObjectAndResource(EObject eObject, Resource resource) {
		this.eObject = eObject;
		this.resource = resource;
	}
}
