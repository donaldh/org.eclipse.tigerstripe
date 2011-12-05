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

import static java.util.Collections.singleton;
import static org.eclipse.emf.ecore.resource.Resource.RESOURCE__CONTENTS;
import static org.eclipse.emf.ecore.util.EcoreUtil.generateUUID;
import static org.eclipse.tigerstripe.annotation.core.AnnotationPackage.Literals.ANNOTATION__ID;
import static org.eclipse.tigerstripe.annotation.core.AnnotationPackage.Literals.ANNOTATION__URI;
import static org.eclipse.tigerstripe.annotation.core.AnnotationPlugin.PLUGIN_ID;
import static org.eclipse.tigerstripe.annotation.core.Filters.eq;
import static org.eclipse.tigerstripe.annotation.core.Filters.startWith;
import static org.eclipse.tigerstripe.annotation.core.Helper.firstOrNull;
import static org.eclipse.tigerstripe.annotation.core.Helper.makeUri;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationFactory;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.IAnnotationProvider;
import org.eclipse.tigerstripe.annotation.core.IAnnotationTarget;
import org.eclipse.tigerstripe.annotation.core.InTransaction;
import org.eclipse.tigerstripe.annotation.core.InTransaction.Operation;
import org.eclipse.tigerstripe.annotation.core.InTransaction.OperationWithResult;
import org.eclipse.tigerstripe.annotation.core.ProviderContext;
import org.eclipse.tigerstripe.annotation.core.Searcher;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;
import org.eclipse.tigerstripe.annotation.core.refactoring.CompositeRefactorListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.RefactoringChange;
import org.eclipse.tigerstripe.annotation.core.storage.internal.ChangeListener;
import org.eclipse.tigerstripe.annotation.core.storage.internal.Storage;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.internal.core.LazyProvider.Loader;


public class AnnotationManager implements IAnnotationManager {

	public static final String FEATURE_ANNOTATION_URI = "http:///org/eclipse/tigerstripe/annotation/Info";
	
	public static final String DESCRIPTION_ANNOTATION = "description";

	private static final String EXTPT_PREFIX = PLUGIN_ID + ".";

	public static final String ROUTER_EXTPT = EXTPT_PREFIX + "router";

	private static final String ANNOTATION_TYPE_EXTPT = EXTPT_PREFIX
			+ "annotationType";

	private static final String ANNOTATION_ADAPTER_EXTPT = EXTPT_PREFIX
			+ "annotationAdapter";

	private static final String ANNOTATION_PROVIDER_EXTPT = EXTPT_PREFIX
			+ "annotationProvider";

	private static final String ANNOTATION_PACKAGE_LABEL_EXTPT = EXTPT_PREFIX
			+ "packageLabel";

	private static final String REFACTORING_CHANGES_LISTENER_EXTPT = EXTPT_PREFIX
			+ "refactoringListeners";

	private static final String ANNOTATION_ATTR_CLASS = "class";

	private static final String ATTR_URI = "epackage-uri";

	private static final String ATTR_NAME = "name";

	private static final Annotation[] EMPTY_ARRAY = new Annotation[0];

	private final MasterRouter masterRouter = new MasterRouter();
	private final Storage storage;
	private final ChangeListener changeListener;
	private final IAnnotationFilesRecognizer annFilesRecognizer;
	
	public AnnotationManager() {
		annFilesRecognizer = new AnnotationFilesRecognizer();
		storage = new Storage(annFilesRecognizer);
		changeListener = makeChangeListener();
		storage.addListener(changeListener);
	}

	private final LazyProvider<Map<String, AnnotationType>> types = new LazyProvider<Map<String, AnnotationType>>(
			new Loader<Map<String, AnnotationType>>() {

				public Map<String, AnnotationType> load() {
					return createTypes();
				}
			});
	
	private final LazyProvider<Map<EPackage, String>> lables = new LazyProvider<Map<EPackage, String>>(
			new Loader<Map<EPackage, String>>() {

				public Map<EPackage, String> load() {
					return createLables();
				}
			});
	
	private final LazyProvider<List<Adapter>> adapters = new LazyProvider<List<Adapter>>(
			new Loader<List<Adapter>>() {

				public List<Adapter> load() {
					return createAdapters();
				}
			});
	private final LazyProvider<ProviderManager> providerManager = new LazyProvider<ProviderManager>(
			new Loader<ProviderManager>() {

				public ProviderManager load() {
					return createProviderManager();
				}
			});
	
	private ChangeListener makeChangeListener() {
		return new ChangeListener() {
			
			public void onChange(List<Notification> notifications) {
				for (Notification n : notifications) {
					if (n.isTouch()) {
						continue;
					}
					Object notifier = n.getNotifier();
					if (notifier instanceof Resource) {
						if (n.getFeatureID(null) == RESOURCE__CONTENTS) {
							
							Object oldValue = n.getOldValue();
							Object newValue = n.getNewValue();
							
							switch (n.getEventType()) {
							case Notification.ADD:
								if (newValue instanceof Annotation) {
									fireAnnotationsAdded(singleton((Annotation) newValue));
								}
								break;
							case Notification.REMOVE:
								if (oldValue instanceof Annotation) {
									fireAnnotationsRemoved(singleton((Annotation) oldValue));
								}
								break;
							case Notification.ADD_MANY:
									fireAnnotationsAdded(toAnnotations(newValue));
								break;
							case Notification.REMOVE_MANY:
								fireAnnotationsRemoved(toAnnotations(oldValue));
								break;
							}
						}
					} else if (notifier instanceof EObject) {
						if (notifier instanceof Annotation) {
							fireAnnotationsChanged(singleton((Annotation) notifier));
						} else {
							EObject rootContainer = EcoreUtil.getRootContainer((EObject) notifier);
							if (rootContainer instanceof Annotation) {
								fireAnnotationsChanged(singleton((Annotation) rootContainer));
							}
						}
					} 
				}
			}
		};
	}

	private Collection<Annotation> toAnnotations(Object value) {
		if (!(value instanceof Collection)) {
			return Collections.emptyList();
		}
		       
		Collection<?> coll = (Collection<?>) value;
		ArrayList<Annotation> anns = new ArrayList<Annotation>(coll.size());
		for (Object obj : coll) {
			if (obj instanceof Annotation) {
				anns.add((Annotation) obj);
			}
		}
		return anns;
	}

	
	private final CompositeRefactorListener refactorListener = new CompositeRefactorListener();

	private IRefactoringChangesListener[] refactoringListeners;

	public void addRefactoringListener(IRefactoringListener listener) {
		refactorListener.addListener(listener);
	}

	public void removeRefactoringListener(IRefactoringListener listener) {
		refactorListener.removeListener(listener);
	}

	public Annotation addAnnotation(final Object object, final EObject content)
			throws AnnotationException {
		
		Object result = InTransaction.run(new OperationWithResult<Object>() {
		
			public void run() {
				try {
					setResult(doAddAnnotation(object, content));
				} catch (AnnotationException e) {
					setResult(e);
				}
			}
		});
		
		if (result instanceof AnnotationException) {
			throw (AnnotationException)result;
		}
		return (Annotation) result;
	}

	public Annotation doAddAnnotation(final Object object, final EObject content)
			throws AnnotationException {
		URI uri = getUri(object);
		if (uri != null) {
			final Annotation annotation = createAnnotation(object, content, uri);
			URI resUri = makeUri(masterRouter.route(annotation)); 
			Resource resource = storage.getResourceForWrite(resUri);
			resource.getContents().add(annotation);
			return annotation;
		} else {
			return null;
		}
	}
	
	protected Annotation createAnnotation(Object object, EObject content,
			URI uri) throws AnnotationException {
		checkUnique(object, uri, content.eClass());
		Annotation annotation = AnnotationFactory.eINSTANCE.createAnnotation();
		annotation.setUri(uri);
		annotation.setContent(content);
		annotation.setId(generateUUID());
		return annotation;
	}

	public TargetAnnotationType[] getAnnotationTargets(Object object) {
		
		Collection<ProviderContext> providers = getProviderManager().getProviders();
		
		AnnotationType[] types = getTypes();
		List<TargetAnnotationType> list = new ArrayList<TargetAnnotationType>();
		for (int i = 0; i < types.length; i++) {
			AnnotationType type = types[i];
			IAnnotationTarget[] annotationTargets = type.getTargets(object,
					providers);
			if (annotationTargets.length > 0) {
				list.add(new TargetAnnotationType(type, annotationTargets));
			}
		}
		return list.toArray(new TargetAnnotationType[list.size()]);
	}

	public boolean isAnnotable(Object object) {
		return getUri(object) != null;
	}

	public boolean isPossibleToAdd(Object object, EClass clazz) {
		URI uri = getUri(object);
		if (uri == null) {
			return false;
		}
		return !violatesUnique(object, uri, clazz);
	}

	protected void checkUnique(Object object, URI uri, EClass clazz)
			throws AnnotationException {
		if (violatesUnique(object, uri, clazz)) {
			throw new AnnotationException(
					"Can't create more than one annotation for the unique class");
		}
	}

	protected boolean violatesUnique(Object object, URI uri, EClass clazz) {
		if (isUnique(object, clazz)) {
			List<Annotation> annotations = getAnnotations(uri);
			for (Annotation annot : annotations) {
				if (clazz.equals(annot.getContent().eClass())) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected boolean isUnique(Object object, EClass clazz) {
		AnnotationType[] types = getTypes();
		for (AnnotationType annotationType : types) {
			// find annotation type for the content
			if (annotationType.getClazz().equals(clazz)) {
				String[] targets = annotationType.getTargets();
				if (targets.length == 0) {
					return annotationType.isUnique();
				}
				for (String target : targets) {
					// find target object for the content
					if (isApplicable(object, target)) {
						return annotationType.isTargetUnique(target);
					}
				}
			}
		}
		return true;
	}

	public void changed(final URI oldUri, final URI newUri, final boolean affectChildren) {
		
		InTransaction.run(new Operation() {
			
			public void run() {
				if (affectChildren) {
					Set<URI> uris = new HashSet<URI>();
					List<Annotation> annotations = storage.find(startWith(ANNOTATION__URI, oldUri));
					for (Annotation annotation : annotations) {
						uris.add(annotation.getUri());
					}
					for (URI uri : uris) {
						if (!uri.equals(newUri) && URIUtil.isPrefix(uri, oldUri)) {
							try {
								URI nUri = uri.equals(oldUri) ? newUri : URIUtil
										.replacePrefix(uri, oldUri, newUri);
								setUri(uri, nUri);
							} catch (Exception e) {
								AnnotationPlugin.log(e);
							}
						}
					}
				} else {
					setUri(oldUri, newUri);
				}
			}
		});
	}

	public void copied(final URI fromUri, final URI toUri,
			final boolean affectChildren) {
		
		InTransaction.run(new Operation() {
			
			public void run() {
				if (affectChildren) {
					Set<URI> uris = new HashSet<URI>();
					
					List<Annotation> annotations = storage.find(startWith(ANNOTATION__URI, fromUri));
					for (Annotation annotation : annotations) {
						uris.add(annotation.getUri());
					}
					for (URI uri : uris) {
						if (!uri.equals(toUri) && URIUtil.isPrefix(uri, fromUri)) {
							try {
								URI nUri = uri.equals(fromUri) ? toUri : URIUtil
										.replacePrefix(uri, fromUri, toUri);
								doCopyAnnotations(uri, nUri);
							} catch (Exception e) {
								AnnotationPlugin.log(e);
							}
						}
					}
				} else {
					try {
						doCopyAnnotations(fromUri, toUri);
					} catch (Exception e) {
						AnnotationPlugin.log(e);
					}
				}
			}
		});
	}

	private void doCopyAnnotations(URI fromUri, URI toUri)
			throws AnnotationException {
		Object to = getAnnotatedObject(toUri);
		if (to == null) {
			return;
		}
		List<Annotation> annotations = storage.find(eq(ANNOTATION__URI, fromUri));
		for (Annotation annotation : annotations) {
			doAddAnnotation(to, annotation.getContent());
		}
	}
	
	public void deleted(final URI uri, final boolean affectChildren) {
		
		InTransaction.run(new Operation() {
			
			public void run() {
				if (affectChildren) {
					List<URI> uris = new ArrayList<URI>();
					List<Annotation> annotations = storage.find(startWith(ANNOTATION__URI, uri));
					for (Annotation annotation : annotations) {
						if (!uris.contains(annotation.getUri())) {
							uris.add(annotation.getUri());
						}
					}
					for (URI childUri : uris) {
						doRemove(childUri);
					}
				} else {
					doRemove(uri);
				}
			}
		});
	}

	protected void setUri(URI oldUri, URI newUri) {
		uriChanged(oldUri, newUri);
		try {
			refactorListener.refactoringPerformed(new RefactoringChange(oldUri,
					newUri));
		} catch (Exception e) {
			AnnotationPlugin.log(e);
		}
	}

	public Annotation[] doGetAnnotations(final Object object, final boolean deepest) {
		List<Annotation> annotations = null;
		if (deepest) {
			annotations = new ArrayList<Annotation>();
			ProviderContext context = getProvider(object);
			if (context != null) {
				collectAllAnnotations(annotations, context, object);
			}
		} else {
			URI uri = getUri(object);
			if (uri != null) {
				annotations = storage.find(eq(ANNOTATION__URI, uri));
			}
		}
		return annotations == null ? EMPTY_ARRAY : annotations
				.toArray(new Annotation[annotations.size()]);
	}
	
	public Annotation[] getAnnotations(final Object object, final boolean deepest) {
		
		return InTransaction.run(new OperationWithResult<Annotation[]>() {

			public void run() {
				setResult(doGetAnnotations(object, deepest));
			}
		});
	}

	public void removeAnnotation(final Annotation annotation) {
		InTransaction.run(new Operation() {
			
			public void run() {
				if (isReadOnly(annotation)) {
					return;
				}
				EcoreUtil.remove(annotation);
			}
		});
	}

	public void removeAnnotations(Object object) {
		final URI uri = getUri(object);
		if (uri != null) {
			InTransaction.run(new Operation() {
				
				public void run() {
					doRemove(uri);
				}
			});
		}
	}

	public String getPackageLabel(EPackage pckg) {
		return getPackageLables().get(pckg);
	}

	protected URI getUri(Object object) {
		ProviderContext providerContext = getProvider(object);
		if (providerContext != null) {
			return getUri(providerContext.getProvider(), object);
		}
		return null;
	}

	protected ProviderContext getProvider(Object object) {
		Collection<ProviderContext> providers = getProviderManager().getProviders();
		for (ProviderContext providerContext : providers) {
			if (isApplicable(object, providerContext.getTarget().getClassName())) {
				return providerContext;
			}
		}
		return null;
	}

	protected void collectAllAnnotations(List<Annotation> annotations,
			ProviderContext context, Object object) {
		IAnnotationProvider provider = context.getProvider();
		URI uri = getUri(provider, object);
		if (uri != null) {
			annotations.addAll(storage.find(eq(ANNOTATION__URI, uri)));
		}

		String[] delegates = context.getDelegates();
		for (int i = 0; i < delegates.length; i++) {
			ProviderContext newContext = getProviderManager()
					.getProviderByType(delegates[i]);
			if (newContext != null) {
				Object adapted = newContext.getTarget().adapt(object);
				if (adapted != null) {
					collectAllAnnotations(annotations, newContext, adapted);
				}
			}
		}
	}

	protected URI getUri(IAnnotationProvider provider, Object object) {
		try {
			return provider.getUri(object);
		} catch (Exception e) {
			AnnotationPlugin.log(e);
		}
		return null;
	}

	public static Object getAdapted(Object object, String className) {
		Object adapted = Platform.getAdapterManager().loadAdapter(object,
				className);
		if (adapted != null) {
			return adapted;
		}
		try {
			Class<?> clazz = Class.forName(className, true, object.getClass()
					.getClassLoader());
			return Platform.getAdapterManager().getAdapter(object, clazz);
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	public boolean isApplicable(Object object, String className) {
		try {
			Class<?> clazz = Class.forName(className, true, object.getClass()
					.getClassLoader());
			return clazz.isInstance(object);
		} catch (ClassNotFoundException e) {
		}
		return false;
	}

	public Object getAnnotatedObject(Annotation annotation) {
		return getAnnotatedObject(annotation.getUri());
	}

	public ProviderManager getProviderManager() {
		return providerManager.get();
	}
	
	private ProviderManager createProviderManager() {
		Map<String, ProviderContext> types = new HashMap<String, ProviderContext>();
		IConfigurationElement[] configs = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						ANNOTATION_PROVIDER_EXTPT);
		for (IConfigurationElement config : configs) {
			try {
				ProviderContext context = new ProviderContext(config);
				types.put(context.getTarget().getClassName(), context);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return new ProviderManager(types);
	}

	protected Map<EPackage, String> createLables() {
		Map<EPackage, String> lables = new HashMap<EPackage, String>();
		IConfigurationElement[] configs = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						ANNOTATION_PACKAGE_LABEL_EXTPT);
		for (IConfigurationElement config : configs) {
			try {
				EPackage pckg = AnnotationUtils.getPackage(config
						.getAttribute(ATTR_URI));
				String newText = config.getAttribute(ATTR_NAME);
				String text = lables.get(pckg);
				if (text != null) {
					throw new AnnotationException(
							"Can't define \""
									+ newText
									+ "\" label for "
									+ ATTR_URI
									+ " package, because it's already defined: "
									+ text);
				}
				lables.put(pckg, newText);
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
		return Collections.unmodifiableMap(lables);
	}
	
	protected Map<EPackage, String> getPackageLables() {
		return lables.get();
	}

	public Map<String, AnnotationType> getTypesMap() {
		return types.get();
	}
	
	public Map<String, AnnotationType> createTypes() {
		Map<String, AnnotationType> types = new HashMap<String, AnnotationType>();
		IConfigurationElement[] configs = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						ANNOTATION_TYPE_EXTPT);
		for (IConfigurationElement config : configs) {
			try {
				AnnotationType type = new AnnotationType(config);
				types.put(AnnotationUtils.getInstanceClassName(
						type.getClazz()).getFullClassName(), type);
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
		return Collections.unmodifiableMap(types);
	}

	public AnnotationType[] getTypes() {
		Collection<AnnotationType> types = getTypesMap().values();
		return types.toArray(new AnnotationType[types.size()]);
	}

	public AnnotationType getType(String epackage, String eclass) {
		return getTypesMap().get(epackage + "." + eclass);
	}

	public List<Adapter> getAdapters() {
		return adapters.get();
	}
	
	private List<Adapter> createAdapters() {
		List<Adapter> adapters = new ArrayList<Adapter>();
		IConfigurationElement[] configs = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						ANNOTATION_ADAPTER_EXTPT);
		for (IConfigurationElement config : configs) {
			try {
				Adapter adapter = (Adapter) config
						.createExecutableExtension(ANNOTATION_ATTR_CLASS);
				adapters.add(adapter);
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
		return adapters;
	}

	public Object getAnnotatedObject(URI uri) {
		Collection<ProviderContext> providers = getProviderManager().getProviders();
		for (ProviderContext providerContext : providers) {
			IAnnotationProvider provider = providerContext.getProvider();
			try {
				Object object = provider.getObject(uri);
				if (object != null) {
					return object;
				}
			} catch (Exception e) {
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

	volatile boolean ignoreDeletion = false;
	
	public void fireDeleted(ILazyObject path) {
		if (!ignoreDeletion) {
			for (IRefactoringChangesListener listener : getRefactoringListeners()) {
				listener.deleted(path);
			}
		}
	}

	public void fireChanged(ILazyObject oldPath, ILazyObject newPath, int kind) {
		if (kind == IRefactoringChangesListener.ABOUT_TO_CHANGE) {
			ignoreDeletion = true;
		}
		for (IRefactoringChangesListener listener : getRefactoringListeners()) {
			listener.changed(oldPath, newPath, kind);
		}
		if (kind == IRefactoringChangesListener.CHANGED) {
			ignoreDeletion = false;
		}
	}

	public void fireMoved(ILazyObject[] objects, ILazyObject destination,
			int kind) {
		if (kind == IRefactoringChangesListener.ABOUT_TO_CHANGE) {
			ignoreDeletion = true;
		}
		for (IRefactoringChangesListener listener : getRefactoringListeners()) {
			listener.moved(objects, destination, kind);
		}
		if (kind == IRefactoringChangesListener.CHANGED) {
			ignoreDeletion = false;
		}
	}

	public void fireCopy(ILazyObject[] objects, ILazyObject destination,
			Map<ILazyObject, String> newNames, int kind) {
		if (kind == IRefactoringChangesListener.ABOUT_TO_CHANGE) {
			ignoreDeletion = true;
		}
		for (IRefactoringChangesListener listener : getRefactoringListeners()) {
			listener.copied(objects, destination, newNames, kind);
		}
		if (kind == IRefactoringChangesListener.CHANGED) {
			ignoreDeletion = false;
		}
	}

	private IRefactoringChangesListener[] getRefactoringListeners() {
		if (refactoringListeners == null) {
			List<IRefactoringChangesListener> list = new ArrayList<IRefactoringChangesListener>();
			IConfigurationElement[] configs = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(
							REFACTORING_CHANGES_LISTENER_EXTPT);
			for (IConfigurationElement config : configs) {
				try {
					IRefactoringChangesListener listener = (IRefactoringChangesListener) config
							.createExecutableExtension(ANNOTATION_ATTR_CLASS);
					list.add(listener);
				} catch (Exception e) {
					AnnotationPlugin.log(e);
				}
			}
			refactoringListeners = list
					.toArray(new IRefactoringChangesListener[list.size()]);
		}
		return refactoringListeners;
	}

	public List<Annotation> getAnnotations(URI uri) {
		if (uri == null) {
			return Collections.emptyList();
		}
		return storage.findTransactional(eq(ANNOTATION__URI, uri));
	}

	public Annotation getAnnotationById(String id) {
		if (id == null) {
			return null;
		}
		return firstOrNull(storage.findTransactional(eq(ANNOTATION__ID, id)));
	}

	public List<Annotation> getPostfixAnnotations(URI uri) {
		if (uri == null) {
			return Collections.emptyList();
		}
		return storage.findTransactional(startWith(ANNOTATION__URI, uri));
	}

	public void uriChanged(final URI oldUri, final URI newUri) {
		
		if (oldUri == null) {
			return;
		}
		List<Annotation> oldList = storage.find(eq(ANNOTATION__URI, oldUri));				
		for (Annotation annotation : oldList) {
			if (isReadOnly(annotation)) {
				continue;
			}
			if (newUri == null) {
				EcoreUtil.remove(annotation);
			}
			annotation.setUri(newUri);
		}
	}

	public void doRemove(final URI uri) {
		List<Annotation> list = storage.find(eq(ANNOTATION__URI, uri));				
		for (Annotation annotation : list) {
			if (isReadOnly(annotation)) {
				continue;
			}
			EcoreUtil.remove(annotation);
		}
	}

	
	public boolean isReadOnly(Annotation annotation) {
		if (isDynamic(annotation)) {
			return true;
		}
		Resource resource = annotation.eResource();
		if (resource == null) {
			return false;
		}
		URI uri = resource.getURI();
		if (uri == null) {
			return false;
		}
		return uri.isArchive();
	}

	public void addAnnotations(final Resource resource) {
		InTransaction.run(new Operation() {
			
			public void run() {
				storage.getResourceSet().getResources().add(resource);				
				fireAnnotationsAdded(resource);
			}
		});
	}

	public void removeAnnotations(final Resource resource) {
		InTransaction.run(new Operation() {
			
			public void run() {
				storage.getResourceSet().getResources().remove(resource);				
				fireAnnotationsRemoved(resource);
			}
		});
	}

	protected boolean isDynamic(Annotation annotation) {
		return annotation.getContent() instanceof DynamicEObjectImpl;
	}

	private final ListenerList listeners = new ListenerList();

	public void addAnnotationListener(IAnnotationListener listener) {
		listeners.add(listener);
	}

	public void removeAnnotationListener(IAnnotationListener listener) {
		listeners.remove(listener);
	}

	protected void fireAnnotationsAdded(Collection<Annotation> annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener) objects[i];
				for (Annotation annotation : annotations) {
					listener.annotationAdded(annotation);
				}
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}

	protected void fireAnnotationsRemoved(Collection<Annotation> annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener) objects[i];
				for (Annotation annotation : annotations) {
					listener.annotationRemoved(annotation);
				}
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}

	protected void fireAnnotationsChanged(Collection<Annotation> annotations) {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			try {
				IAnnotationListener listener = (IAnnotationListener) objects[i];
				for (Annotation annotation : annotations) {
					listener.annotationChanged(annotation);
				}
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}

	protected void fireAnnotationsAdded(Resource resource) {
		fireAnnotationsAdded(toAnnotations(resource.getContents()));
	}

	protected void fireAnnotationsRemoved(Resource resource) {
		for (EObject obj : resource.getContents()) {
			if (obj instanceof Annotation) {
				fireAnnotationsRemoved(Collections.singleton((Annotation)obj));
			}
		}
	}

	public void save(Annotation annotation) {
		if (isReadOnly(annotation)) {
			return;
		}
		storage.save(annotation.eResource());
	}

	public void dispose() {
		storage.dispose();
		storage.removeListener(changeListener);
	}

	public Searcher getSearcher() {
		return storage;
	}

	public boolean isAnnotationFile(IFile file) {
		return annFilesRecognizer.isAnnotationFile(file);
	}
	
	public Storage getStorage() {
		return storage;
	}

	public ResourceSet getResourceSet() {
		return storage.getResourceSet();
	}

	public void disableNotifications() {
		storage.disableNotifications();
	}
}
