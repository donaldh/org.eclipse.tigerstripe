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
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationFactory;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.CompositeRefactorListener;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.IAnnotationProvider;
import org.eclipse.tigerstripe.annotation.core.IAnnotationTarget;
import org.eclipse.tigerstripe.annotation.core.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.core.IRefactoringSupport;
import org.eclipse.tigerstripe.annotation.core.ProviderContext;
import org.eclipse.tigerstripe.annotation.core.RefactoringChange;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;

/**
 * This is implementation of the <code>IAnnotationManager</code>.
 * This class should not be extended or accessed directly, use
 * <code>AnnotationPlugin.getManager()</code> instead.
 * 
 * @see IAnnotationManager
 * @author Yuri Strot
 */
public class AnnotationManager extends AnnotationStorage implements IAnnotationManager, IRefactoringSupport {
	
	private static final String ANNOTATION_TYPE_EXTPT = 
		"org.eclipse.tigerstripe.annotation.core.annotationType";
	
	private static final String ANNOTATION_ADAPTER_EXTPT = 
		"org.eclipse.tigerstripe.annotation.core.annotationAdapter";
	
	private static final String ANNOTATION_PROVIDER_EXTPT = 
		"org.eclipse.tigerstripe.annotation.core.annotationProvider";
	
	private static final String ANNOTATION_ATTR_CLASS = "class";
	
	private static AnnotationManager instance;
	
	private AnnotationType[] types;
	private List<Adapter> adapters;
	private ProviderManager providerManager;
	
	private CompositeRefactorListener refactorListener = new CompositeRefactorListener();
	
	public AnnotationManager() {
	}
	
	public void addRefactoringListener(IRefactoringListener listener) {
		refactorListener.addListener(listener);
	}
	
	public void removeRefactoringListener(IRefactoringListener listener) {
		refactorListener.removeListener(listener);
	}

	public Annotation addAnnotation(Object object, EObject content) {
		URI uri = getUri(object);
		if (uri != null) {
			checkUnique(object, uri, content);
			Annotation annotation = AnnotationFactory.eINSTANCE.createAnnotation();
			annotation.setUri(uri);
			annotation.setContent(content);
			add(annotation);
			return annotation;
		}
		return null;
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationManager#getProviderTargets()
	 */
	public ProviderTarget[] getProviderTargets() {
		ProviderContext[] contexts = getProviderManager().getProviders();
		ProviderTarget[] targets = new ProviderTarget[contexts.length];
		for (int i = 0; i < contexts.length; i++) {
			targets[i] = contexts[i].getTarget();
		}
		return targets;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationManager#getAnnotationTargets(java.lang.Object)
	 */
	public TargetAnnotationType[] getAnnotationTargets(Object object) {
		ProviderTarget[] targets = getProviderTargets();
		AnnotationType[] types = getTypes();
		List<TargetAnnotationType> list = new ArrayList<TargetAnnotationType>();
		for (int i = 0; i < types.length; i++) {
			AnnotationType type = types[i];
			IAnnotationTarget[] annotationTargets = type.getTargets(object, targets);
			if (annotationTargets.length > 0) {
				list.add(new TargetAnnotationType(type, annotationTargets));
			}
		}
		return list.toArray(new TargetAnnotationType[list.size()]);
	}
	
	protected void checkUnique(Object object, URI uri, EObject content) {
		if (isUnique(object, content)) {
			List<Annotation> annotations = getAnnotations(uri);
			for (Annotation annot : annotations) {
				Class<?> clazz = annot.getContent().getClass();
				if (content.getClass().equals(clazz)) {
					throw new RuntimeException("Can't create more tham one annotation for the unique class");
				}
			}
		}
	}
	
	protected boolean isUnique(Object object, EObject content) {
		AnnotationType[] types = getTypes();
		for (AnnotationType annotationType : types) {
			//find annotation type for the content
			if (annotationType.getClazz().equals(content.eClass())) {
				String[] targets = annotationType.getTargets();
				for (String target : targets) {
					//find target object for the content
					if (isApplicable(object, target)) {
						return annotationType.isTargetUnique(target);
					}
				}
			}
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IRefactoringSupport#changed(org.eclipse.emf.common.util.URI, org.eclipse.emf.common.util.URI, boolean)
	 */
	public void changed(URI oldUri, URI newUri, boolean affectChildren) {
		if (affectChildren) {
			List<URI> uris = new ArrayList<URI>();
			List<Annotation> annotations = getPostfixAnnotations(oldUri);
			for (Annotation annotation : annotations) {
				if (!uris.contains(annotation.getUri()))
					uris.add(annotation.getUri());
			}
			for (URI uri : uris) {
				try {
					URI nUri = URIUtil.replacePrefix(uri, oldUri, newUri);
					setUri(uri, nUri);
				}
				catch (Exception e) {
					AnnotationPlugin.log(e);
				}
			}
		}
		else {
	        setUri(oldUri, newUri);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IRefactoringSupport#deleted(org.eclipse.emf.common.util.URI, boolean)
	 */
	public void deleted(URI uri, boolean affectChildren) {
		if (affectChildren) {
			List<URI> uris = new ArrayList<URI>();
			List<Annotation> annotations = getPostfixAnnotations(uri);
			for (Annotation annotation : annotations) {
				if (!uris.contains(annotation.getUri()))
					uris.add(annotation.getUri());
			}
			for (URI childUri : uris) {
				remove(childUri);
			}
		}
		else {
			remove(uri);
		}
	}
	
	public IRefactoringSupport getRefactoringSupport() {
		return this;
	}
	
	protected void setUri(URI oldUri, URI newUri) {
		uriChanged(oldUri, newUri);
		try {
	        refactorListener.refactoringPerformed(
	        		new RefactoringChange(oldUri, newUri));
		}
		catch (Exception e) {
			AnnotationPlugin.log(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationManager#getAnnotations(java.lang.Object, boolean)
	 */
	public Annotation[] getAnnotations(Object object, boolean deepest) {
		List<Annotation> annotations = null;
		if (deepest) {
			annotations = new ArrayList<Annotation>();
			ProviderContext context = getProvider(object);
			if (context != null) {
				collectAllAnnotations(annotations, context, object);
			}
		}
		else {
			URI uri = getUri(object);
			if (uri != null) {
				annotations = getAnnotations(uri);
			}
		}
		return annotations == null ? EMPTY_ARRAY : annotations.toArray(
				new Annotation[annotations.size()]);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationManager#getLoadedAnnotations()
	 */
	public Annotation[] getLoadedAnnotations() {
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationManager#set(org.eclipse.tigerstripe.annotation.core.Annotation, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public void set(Annotation annotation, EObject object,
			EStructuralFeature feature, Object value) {
		database.remove(annotation);
		object.eSet(feature, value);
		database.write(annotation);
	}
	
	public static AnnotationManager getInstance() {
		if (instance == null)
			instance = new AnnotationManager();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.internal.core.AnnotationStorage#trackChanges(org.eclipse.tigerstripe.annotation.core.Annotation)
	 */
	@Override
	protected void trackChanges(Annotation annotation) {
		super.trackChanges(annotation);
		if (annotation.getContent() != null)
			annotation.getContent().eAdapters().addAll(getAdapters());
	}
	
	protected URI getUri(Object object) {
		ProviderContext providerContext = getProvider(object);
		if (providerContext != null)
			return getUri(providerContext.getProvider(), object);
	    return null;
	}
	
	protected ProviderContext getProvider(Object object) {
		ProviderContext[] providers = getProviderManager().getProviders();
		for (ProviderContext providerContext : providers) {
			if (isApplicable(object, providerContext.getTarget().getClassName())) {
				return providerContext;
			}
		}
		return null;
	}
	
	protected void collectAllAnnotations(List<Annotation> annotations, ProviderContext context, Object object) {
		IAnnotationProvider provider = context.getProvider();
		URI uri = getUri(provider, object);
		if (uri != null)
			annotations.addAll(getAnnotations(uri));
		
		String[] delegates = context.getDelegates();
		for (int i = 0; i < delegates.length; i++) {
			ProviderContext newContext = providerManager.getProviderByType(delegates[i]);
			if (newContext != null) {
				Object adapted = newContext.getTarget().adapt(object);
				if (adapted != null)
					collectAllAnnotations(annotations, newContext, adapted);
			}
		}
	}
	
	protected URI getUri(IAnnotationProvider provider, Object object) {
		try {
			return provider.getUri(object);
		}
		catch (Exception e) {
			AnnotationPlugin.log(e);
		}
		return null;
	}
	
	public static Object getAdapted(Object object, String className) {
		Object adapted = Platform.getAdapterManager().loadAdapter(object, className);
		if (adapted != null)
			return adapted;
		try {
			Class<?> clazz = Class.forName(className, true, object.getClass().getClassLoader());
			return Platform.getAdapterManager().getAdapter(object, clazz);
		} catch (ClassNotFoundException e) {
		}
		return null;
	}
	
	public boolean isApplicable(Object object, String className) {
		try {
			Class<?> clazz = Class.forName(className, true, object.getClass().getClassLoader());
			return clazz.isInstance(object);
		} catch (ClassNotFoundException e) {
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationManager#getAnnotatedObject(org.eclipse.tigerstripe.annotation.core.Annotation)
	 */
	public Object getAnnotatedObject(Annotation annotation) {
		return getObject(annotation.getUri());
	}
	
	protected ProviderManager getProviderManager() {
		if (providerManager == null) {
			providerManager = new ProviderManager();
			IConfigurationElement[] configs = Platform.getExtensionRegistry(
				).getConfigurationElementsFor(ANNOTATION_PROVIDER_EXTPT);
	        for (IConfigurationElement config : configs) {
	        	try {
	        		ProviderContext context = new ProviderContext(config);
	                providerManager.addProvider(context);
	            }
	            catch (CoreException e) {
	                e.printStackTrace();
	            }
	        }
		}
		return providerManager;
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

	public Object getObject(URI uri) {
		ProviderContext[] providers = getProviderManager().getProviders();
		for (ProviderContext providerContext : providers) {
			IAnnotationProvider provider = providerContext.getProvider();
			try {
				Object object = provider.getObject(uri);
				if (object != null)
					return object;
			}
			catch (Exception e) {
				AnnotationPlugin.log(e);
			}
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
