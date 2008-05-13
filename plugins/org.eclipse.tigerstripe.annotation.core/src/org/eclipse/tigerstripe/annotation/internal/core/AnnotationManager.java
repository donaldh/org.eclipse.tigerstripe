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
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
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
	
	private static final String ANNOTATION_ADAPTER_EXTPT = "org.eclipse.tigerstripe.annotation.core.annotationAdapter";
	
	private static final String ANNOTATION_PROVIDER_EXTPT = "org.eclipse.tigerstripe.annotation.core.annotationProvider";
	private static final String ANNOTATION_ATTR_CLASS = "class";
	private static final String ANNOTATION_ATTR_ID = "id";
	
	private static final String ANNOTATION_MARKER = "org.eclipse.tigerstripe.annotation";
	private static final String ANNOTATION_UNIQUE = "unique";
	
	private static AnnotationManager instance;
	
	private Map<IAnnotationProvider, String> providers;
	private AnnotationType[] types;
	private List<Adapter> adapters;
	
	public AnnotationManager() {
	}

	public Annotation addAnnotation(Object object, EObject content) {
		URI uri = getUri(object);
		if (uri != null) {
			checkUnique(uri, content);
			Annotation annotation = AnnotationFactory.eINSTANCE.createAnnotation();
			annotation.setUri(uri);
			annotation.setContent(content);
			add(annotation);
			return annotation;
		}
		return null;
    }
	
	protected void checkUnique(URI uri, EObject content) {
		boolean unique = true;
		EAnnotation annotation = content.eClass().getEAnnotation(ANNOTATION_MARKER);
		if (annotation != null) {
			String value = annotation.getDetails().get(ANNOTATION_UNIQUE);
			if (value != null && !Boolean.valueOf(value))
				unique = false;
		}
		if (unique) {
			Annotation[] annotations = getAnnotations(uri);
			for (int i = 0; i < annotations.length; i++) {
				Class<?> clazz = annotations[i].getContent().getClass();
				if (content.getClass().equals(clazz)) {
					throw new RuntimeException("Can't create more tham one annotation for the unique class");
				}
            }
		}
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
		if (annotation.getContent() != null)
			annotation.getContent().eAdapters().addAll(getAdapters());
	    super.addToList(annotation, uri);
	}
	
	public URI getUri(Object object) {
		IAnnotable annotable = (IAnnotable)Platform.getAdapterManager(
			).loadAdapter(object, IAnnotable.class.getName());
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
	                	(IAnnotationProvider)config.createExecutableExtension(ANNOTATION_ATTR_CLASS);
	                String id = config.getAttribute(ANNOTATION_ATTR_ID);
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
	
	public List<Adapter> getAdapters() {
		if (adapters == null) {
			adapters = new ArrayList<Adapter>();
			IConfigurationElement[] configs = Platform.getExtensionRegistry(
				).getConfigurationElementsFor(ANNOTATION_ADAPTER_EXTPT);
	        for (IConfigurationElement config : configs) {
	        	try {
	                Adapter adapter = 
	                	(Adapter)config.createExecutableExtension(ANNOTATION_ATTR_CLASS);
	                adapters.add(adapter);
	            }
	            catch (Exception e) {
	            	AnnotationPlugin.log(e);
	            }
	        }
		}
		return adapters;
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
