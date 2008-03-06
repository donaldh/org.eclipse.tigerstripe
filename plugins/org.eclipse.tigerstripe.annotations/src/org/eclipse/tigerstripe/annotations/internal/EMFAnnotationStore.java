/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    erdillon (Cisco Systems, Inc.) - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.internal;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationStore;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.internal.context.Annotation;
import org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext;
import org.eclipse.tigerstripe.annotations.internal.context.ContextFactory;

/**
 * This is the default implementation for Annotation Store. It is based on EMF
 * Resource
 * 
 * @author erdillon
 * 
 */
public class EMFAnnotationStore extends AnnotationStore {

	private Resource resource;
	private IAnnotationScheme scheme;
	private boolean isDirty = false;

	public EMFAnnotationStore(Resource resource, IAnnotationScheme scheme) {
		this.resource = resource;
		this.scheme = scheme;
	}

	protected IAnnotationScheme getScheme() {
		return this.scheme;
	}

	private AnnotationContext getAnnotationContext()
			throws AnnotationCoreException {
		EObject obj = resource.getContents().get(0);
		if (obj instanceof AnnotationContext) {
			return (AnnotationContext) obj;
		}
		throw new AnnotationCoreException();
	}

	@Override
	public void load() throws AnnotationCoreException {
		try {
			resource.load(null);
			setDirty(false);
		} catch (IOException e) {
			throw new AnnotationCoreException(e);
		}
	}

	@Override
	public void store() throws AnnotationCoreException {
		try {
			resource.save(null);
			setDirty(false);
		} catch (IOException e) {
			throw new AnnotationCoreException(e);
		}
	}

	public boolean isDirty() throws AnnotationCoreException {
		return isDirty;
	}

	protected void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	@Override
	public void setAnnotation(IAnnotationSpecification specification,
			String URI, Object value) throws AnnotationCoreException {

		Annotation targetAnnotation = null;

		// TODO use a query to do this
		// look for an existing value
		for (Annotation ann : getAnnotationContext().getAnnotations()) {
			if (ann.getAnnotationSpecificationID()
					.equals(specification.getID())
					&& ann.getUri().equals(URI)) {
				targetAnnotation = ann;
			}
		}

		if (targetAnnotation == null) {
			targetAnnotation = ContextFactory.eINSTANCE.createAnnotation();
			getAnnotationContext().getAnnotations().add(targetAnnotation);
		}

		targetAnnotation.setAnnotationSpecificationID(specification.getID());
		targetAnnotation.setUri(URI);
		targetAnnotation.setValue(value);

		setDirty(true);
	}

	@Override
	public void unsetAnnotation(IAnnotationSpecification specification,
			String URI) throws AnnotationCoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getAnnotation(IAnnotationSpecification specification,
			String URI) throws AnnotationCoreException {
		// TODO use a query to implement
		for (Annotation ann : getAnnotationContext().getAnnotations()) {
			if (ann.getAnnotationSpecificationID()
					.equals(specification.getID())
					&& ann.getUri().equals(URI)) {
				return ann.getValue();
			}
		}
		return null;
	}

	@Override
	public void uriChanged(String oldURI, String newURI)
			throws AnnotationCoreException {
		for (Annotation ann : getAnnotationContext().getAnnotations()) {
			if (ann.getUri().equals(oldURI)) {
				ann.setUri(newURI);
				setDirty(true);
			}
		}

		if (isDirty())
			store();
	}

	@Override
	public void uriRemoved(String URI) throws AnnotationCoreException {
		// TODO Auto-generated method stub

	}

	/**
	 * This callback is used when the factory detects that the underlying store
	 * file was removed In this case, the store is marked as dirty.
	 * 
	 */
	public void storeFileDeleted() {
		// TODO
	}

	/**
	 * This callback is used when the factory detects that the underlying store
	 * file was changed In this case, the store needs to be reloaded.
	 * 
	 */
	public void storeFileChanged() {
		// TODO
	}
}
