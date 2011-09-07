/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.annotation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.espace.resources.core.IAnnotationResourceProcessor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TigerstripeAnnotationResourceProcessor implements
		IAnnotationResourceProcessor {

	private List<IAnnotationProcessor> postLoadAnnotationProcessors;

	public void postLoad(XMLResource resource) {
		ITigerstripeModelProject context = getContextProject(resource);
		if (context != null) {
			for (EObject eObject : resource.getContents()) {
				if (eObject instanceof Annotation) {
					processPostLoadAnnotation((Annotation) eObject, context);
				}
			}
		}
	}

	public void preSave(XMLResource resource) {
		for (EObject eObject : resource.getContents()) {
			if (eObject instanceof Annotation) {
				processPreSaveAnnotation((Annotation) eObject);
			}
		}
	}

	public void postSave(XMLResource resource) {
		postLoad(resource);
	}

	protected void processPostLoadAnnotation(Annotation annotation,
			ITigerstripeModelProject context) {
		for (IAnnotationProcessor processor : getPostLoadProcessors()) {
			if (processor.isApplicable(annotation, context)) {
				processor.process(annotation, context);
			}
		}
	}

	protected void processPreSaveAnnotation(Annotation annotation) {
		if (TigerstripeURIAdapterFactory.isRelated(annotation.getUri())) {
			URI originalUri = annotation.getUri();

			String[] originalSegments = originalUri.segments();
			if (originalSegments.length == 3) {
				URI newUri = URI.createHierarchicalURI(originalUri.scheme(),
						null, null, new String[] { originalSegments[1],
								originalSegments[2] }, null,
						originalUri.fragment());
				annotation.setUri(newUri);
			}
		}
	}

	private ITigerstripeModelProject getContextProject(Resource eResource) {
		URI resourceUri = eResource.getURI();
		if (resourceUri.isPlatformResource()) {
			String platformString = resourceUri.toPlatformString(true);
			IResource resource = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(platformString);
			if (resource != null && resource.getProject() != null) {
				return (ITigerstripeModelProject) resource.getProject()
						.getAdapter(ITigerstripeModelProject.class);
			}
		}
		return null;
	}

	private List<IAnnotationProcessor> getPostLoadProcessors() {
		// be careful, processors order is important
		if (postLoadAnnotationProcessors == null) {
			postLoadAnnotationProcessors = new ArrayList<IAnnotationProcessor>();
			postLoadAnnotationProcessors
					.add(new ModuleOrRefProjectAnnotationURIProcessor());
			postLoadAnnotationProcessors
					.add(new AppendContextAnnotationURIProcessor());
		}
		return postLoadAnnotationProcessors;
	}

	private URI createURI(String[] segments, String fragment) {
		return createURI(TigerstripeURIAdapterFactory.SCHEME_TS, segments,
				fragment);
	}

	private URI createURI(String scheme, String[] segments, String fragment) {
		return URI.createHierarchicalURI(scheme, null, null, segments, null,
				fragment);
	}

	private class ModuleOrRefProjectAnnotationURIProcessor implements
			IAnnotationProcessor {

		public static final String SCHEME_TS_MODULE = "tigerstripe_module";
		public static final String SCHEME_TS_REF_PROJECT = "tigerstripe_ref_project";

		public static final String SCHEME_TS_CONTEXT_PROJECT_SEPARATOR = ":";

		public boolean isApplicable(Annotation annotation,
				ITigerstripeModelProject context) {
			URI uri = annotation.getUri();
			if ((SCHEME_TS_MODULE.equals(uri.scheme()) || SCHEME_TS_REF_PROJECT
					.equals(uri.scheme())) && uri.segmentCount() == 2) {
				return true;
			}
			return false;
		}

		public void process(Annotation annotation,
				ITigerstripeModelProject context) {
			URI uri = annotation.getUri();
			String[] segments = null;
			String[] elements = uri.segment(0).split(
					SCHEME_TS_CONTEXT_PROJECT_SEPARATOR);
			if (elements.length == 2) {
				segments = new String[] { elements[0], elements[1],
						uri.segment(1) };
			} else {
				segments = uri.segments();
			}
			annotation.setUri(createURI(segments, uri.fragment()));
		}
	}

	private class AppendContextAnnotationURIProcessor implements
			IAnnotationProcessor {
		public boolean isApplicable(Annotation annotation,
				ITigerstripeModelProject context) {
			URI uri = annotation.getUri();
			if (TigerstripeURIAdapterFactory.SCHEME_TS.equals(uri.scheme())
					&& uri.segmentCount() == 2) {
				try {
					if (!uri.segment(0).equals(context.getModelId())) {
						return true;
					}
				} catch (TigerstripeException e) {
					BasePlugin.log(e);
				}
			}
			return false;
		}

		public void process(Annotation annotation,
				ITigerstripeModelProject context) {
			try {
				URI uri = annotation.getUri();
				String[] segments = new String[] { context.getModelId(),
						uri.segment(0), uri.segment(1) };
				annotation.setUri(createURI(segments, uri.fragment()));
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		}
	}

	private interface IAnnotationProcessor {

		public boolean isApplicable(Annotation annotation,
				ITigerstripeModelProject context);

		public void process(Annotation annotation,
				ITigerstripeModelProject context);
	}
}
