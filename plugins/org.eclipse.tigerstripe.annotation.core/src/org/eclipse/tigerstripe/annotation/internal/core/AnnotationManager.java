/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.internal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationFactory;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.IAnnotable;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.IAnnotationProvider;

/**
 * This is implementation of the <code>IAnnotationManager</code>.
 * This class should not be extended or accessed directly, use
 * <code>AnnotationPlugin.getManager()</code> instead.
 * 
 * @see IAnnotationManager
 * @author Yuri Strot
 */
public class AnnotationManager extends AnnotationStorage implements IAnnotationManager {
	
	private static final String ANNOTATION_TYPE_EXTPT = "org.eclipse.tigerstripe.annotation.core.annotationType";
	
	private static final String ANNOTATION_PROVIDER_EXTPT = "org.eclipse.tigerstripe.annotation.core.annotationProvider";
	private static final String ANNOTATION_PROVIDER_ATTR_CLASS = "class";
	private static final String ANNOTATION_PROVIDER_ATTR_ID = "id";
	
	private static AnnotationManager instance;
	
	private Map<IAnnotationProvider, String> providers;
	private AnnotationType[] types;
	private ValidationAdapter validationAdapter;
	
	public AnnotationManager() {
		validationAdapter = new ValidationAdapter();
	}

	public Annotation addAnnotation(Object object, EObject content) {
		URI uri = getUri(object);
		if (uri != null) {
			Annotation annotation = AnnotationFactory.eINSTANCE.createAnnotation();
			annotation.setUri(uri);
			annotation.setContent(content);
			add(annotation);
			return annotation;
		}
		return null;
    }
	
	public void setUri(URI oldUri, URI newUri) {
		uriChanged(oldUri, newUri);
	}

	public Annotation[] getAnnotations(Object object) {
		URI uri = getUri(object);
		if (uri != null)
			return getAnnotations(uri);
		return EMPTY_ARRAY;
    }
	
	public Annotation[] getAnnotations() {
	    return super.getAnnotations();
	}
	
	public void removeAnnotation(Annotation annotation) {
		remove(annotation);
	}
	
	public void removeAnnotations(Object object) {
		URI uri = getUri(object);
		if (uri != null)
			remove(uri);
	}
	
	public static AnnotationManager getInstance() {
		if (instance == null)
			instance = new AnnotationManager();
		return instance;
	}
	
	@Override
	protected void addToList(Annotation annotation, URI uri) {
		annotation.getContent().eAdapters().add(validationAdapter);
	    super.addToList(annotation, uri);
	}
	
	public URI getUri(Object object) {
		IAnnotable annotable = (IAnnotable)Platform.getAdapterManager(
			).getAdapter(object, IAnnotable.class);
		if (annotable == null)
			return null;
		return annotable.getUri();
	}
	
	protected Map<IAnnotationProvider, String> getProvidersMap() {
		if (providers == null) {
			providers = new HashMap<IAnnotationProvider, String>();
			IConfigurationElement[] configs = Platform.getExtensionRegistry(
				).getConfigurationElementsFor(ANNOTATION_PROVIDER_EXTPT);
	        for (IConfigurationElement config : configs) {
	        	try {
	                IAnnotationProvider provider = 
	                	(IAnnotationProvider)config.createExecutableExtension(ANNOTATION_PROVIDER_ATTR_CLASS);
	                String id = config.getAttribute(ANNOTATION_PROVIDER_ATTR_ID);
	                providers.put(provider, id);
	            }
	            catch (CoreException e) {
	                e.printStackTrace();
	            }
	        }
		}
		return providers;
	}
	
	public AnnotationType[] getTypes() {
		if (this.types == null) {
			List<AnnotationType> types = new ArrayList<AnnotationType>();
			IConfigurationElement[] configs = Platform.getExtensionRegistry(
				).getConfigurationElementsFor(ANNOTATION_TYPE_EXTPT);
	        for (IConfigurationElement config : configs) {
	        	try {
	        		types.add(new AnnotationType(config));
	            }
	            catch (Exception e) {
	            	AnnotationPlugin.log(e);
	            }
	        }
	        this.types = types.toArray(new AnnotationType[types.size()]);
		}
		return this.types;
	}
	
	public String getProviderId(URI uri) {
		Iterator<IAnnotationProvider> provs = getProvidersMap().keySet().iterator();
		while (provs.hasNext()) {
			IAnnotationProvider provider = provs.next();
			Object object = provider.getObject(uri);
			if (object != null)
				return getProvidersMap().get(provider);
        }
	    return null;
	}

	public Object getObject(URI uri) {
		Iterator<IAnnotationProvider> provs = getProvidersMap().keySet().iterator();
		while (provs.hasNext()) {
			Object object = provs.next().getObject(uri);
			if (object != null)
				return object;
        }
	    return null;
    }
	
	public AnnotationType getType(Annotation annotation) {
		AnnotationType[] types = getTypes();
		EObject content = annotation.getContent();
		if (content != null) {
			for (int i = 0; i < types.length; i++) {
				if (types[i].getClazz().equals(content.eClass())) {
					return types[i];
				}
			}
		}
		return null;
	}

}
