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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationConstraintException;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationFactory;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.CompositeRefactorListener;
import org.eclipse.tigerstripe.annotation.core.IAnnotationConstraint;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.IAnnotationParticipant;
import org.eclipse.tigerstripe.annotation.core.IAnnotationProvider;
import org.eclipse.tigerstripe.annotation.core.IAnnotationTarget;
import org.eclipse.tigerstripe.annotation.core.IAnnotationValidationContext;
import org.eclipse.tigerstripe.annotation.core.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.core.IRefactoringSupport;
import org.eclipse.tigerstripe.annotation.core.ProviderContext;
import org.eclipse.tigerstripe.annotation.core.RefactoringChange;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;

/**
 * This is implementation of the <code>IAnnotationManager</code>. This class
 * should not be extended or accessed directly, use
 * <code>AnnotationPlugin.getManager()</code> instead.
 * 
 * @see IAnnotationManager
 * @author Yuri Strot
 */
public class AnnotationManager extends AnnotationStorage implements
		IAnnotationManager, IRefactoringSupport {

	private static final String EXTPT_PREFIX = "org.eclipse.tigerstripe.annotation.core.";

	private static final String ANNOTATION_TYPE_EXTPT = EXTPT_PREFIX
			+ "annotationType";

	private static final String ANNOTATION_ADAPTER_EXTPT = EXTPT_PREFIX
			+ "annotationAdapter";

	private static final String ANNOTATION_PROVIDER_EXTPT = EXTPT_PREFIX
			+ "annotationProvider";

	private static final String ANNOTATION_CONSTRAINT_EXTPT = EXTPT_PREFIX
			+ "constraints";

	private static final String ANNOTATION_PACKAGE_LABEL_EXTPT = EXTPT_PREFIX
			+ "packageLabel";

	private static final String ANNOTATION_PARTICIPANT_EXTPT = EXTPT_PREFIX
			+ "participants";

	private static final String ANNOTATION_ATTR_CLASS = "class";

	private static final String ATTR_URI = "epackage-uri";

	private static final String ATTR_NAME = "name";

	private static AnnotationManager instance;

	private Map<String, AnnotationType> types;
	private List<Adapter> adapters;
	private ProviderManager providerManager;
	private IAnnotationConstraint[] constraints;
	private Map<EPackage, String> lables;

	private ReentrantLock typesLock = new ReentrantLock();
	private ReentrantLock adaptersLock = new ReentrantLock();
	private ReentrantLock providerManagerLock = new ReentrantLock();
	private ReentrantLock constraintsLock = new ReentrantLock();
	private ReentrantLock lablesLock = new ReentrantLock();

	private CompositeRefactorListener refactorListener = new CompositeRefactorListener();

	private List<IAnnotationParticipant> participants;

	public AnnotationManager() {
		loadParticipants();
	}

	public void addRefactoringListener(IRefactoringListener listener) {
		refactorListener.addListener(listener);
	}

	public void removeRefactoringListener(IRefactoringListener listener) {
		refactorListener.removeListener(listener);
	}

	public Annotation addAnnotation(Object object, EObject content)
			throws AnnotationException {
		URI uri = getUri(object);
		if (uri != null) {
			Annotation annotation = createAnnotation(object, content, uri);
			add(annotation);
			return annotation;
		}
		return null;
	}

	protected Annotation createAnnotation(Object object, EObject content,
			URI uri) throws AnnotationException {
		checkUnique(object, uri, content.eClass());
		Annotation annotation = AnnotationFactory.eINSTANCE.createAnnotation();
		annotation.setUri(uri);
		annotation.setContent(content);
		validateAnnotation(annotation, object);
		return annotation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.core.IAnnotationManager#getProviderTargets
	 * ()
	 */
	public ProviderTarget[] getProviderTargets() {
		ProviderContext[] contexts = getProviderManager().getProviders();
		ProviderTarget[] targets = new ProviderTarget[contexts.length];
		for (int i = 0; i < contexts.length; i++) {
			targets[i] = contexts[i].getTarget();
		}
		return targets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.core.IAnnotationManager#
	 * getAnnotationTargets(java.lang.Object)
	 */
	public TargetAnnotationType[] getAnnotationTargets(Object object) {
		ProviderTarget[] targets = getProviderTargets();
		AnnotationType[] types = getTypes();
		List<TargetAnnotationType> list = new ArrayList<TargetAnnotationType>();
		for (int i = 0; i < types.length; i++) {
			AnnotationType type = types[i];
			IAnnotationTarget[] annotationTargets = type.getTargets(object,
					targets);
			if (annotationTargets.length > 0) {
				list.add(new TargetAnnotationType(type, annotationTargets));
			}
		}
		return list.toArray(new TargetAnnotationType[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.core.IAnnotationManager#isAnnotable
	 * (java.lang.Object)
	 */
	public boolean isAnnotable(Object object) {
		return getUri(object) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.core.IAnnotationManager#isPossibleToAdd
	 * (java.lang.Object, org.eclipse.emf.ecore.EClass)
	 */
	public boolean isPossibleToAdd(Object object, EClass clazz) {
		URI uri = getUri(object);
		if (uri == null)
			return false;
		EObject content = clazz.getEPackage().getEFactoryInstance().create(
				clazz);
		try {
			createAnnotation(object, content, uri);
		} catch (AnnotationException e) {
			return false;
		}
		return true;
	}

	protected void checkUnique(Object object, URI uri, EClass clazz)
			throws AnnotationException {
		if (isUnique(object, clazz)) {
			List<Annotation> annotations = getAnnotations(uri);
			for (Annotation annot : annotations) {
				if (clazz.equals(annot.getContent().eClass())) {
					throw new AnnotationException(
							"Can't create more than one annotation for the unique class");
				}
			}
		}
	}

	protected boolean isUnique(Object object, EClass clazz) {
		AnnotationType[] types = getTypes();
		for (AnnotationType annotationType : types) {
			// find annotation type for the content
			if (annotationType.getClazz().equals(clazz)) {
				String[] targets = annotationType.getTargets();
				if (targets.length == 0)
					return annotationType.isUnique();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.core.IRefactoringSupport#changed(org
	 * .eclipse.emf.common.util.URI, org.eclipse.emf.common.util.URI, boolean)
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
				} catch (Exception e) {
					AnnotationPlugin.log(e);
				}
			}
		} else {
			setUri(oldUri, newUri);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.core.IRefactoringSupport#deleted(org
	 * .eclipse.emf.common.util.URI, boolean)
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
		} else {
			remove(uri);
		}
	}

	public IRefactoringSupport getRefactoringSupport() {
		return this;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.core.IAnnotationManager#getAnnotations
	 * (java.lang.Object, boolean)
	 */
	public Annotation[] getAnnotations(Object object, boolean deepest) {
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
				annotations = getAnnotations(uri);
			}
		}
		return annotations == null ? EMPTY_ARRAY : annotations
				.toArray(new Annotation[annotations.size()]);
	}

	public void removeAnnotation(Annotation annotation) {
		remove(annotation);
	}

	public void removeAnnotations(Object object) {
		URI uri = getUri(object);
		if (uri != null)
			remove(uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.core.IAnnotationManager#getPackageLabel
	 * (org.eclipse.emf.ecore.EPackage)
	 */
	public String getPackageLabel(EPackage pckg) {
		return getPackageLables().get(pckg);
	}

	public static AnnotationManager getInstance() {
		if (instance == null)
			instance = new AnnotationManager();
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.internal.core.AnnotationStorage#
	 * trackChanges(org.eclipse.tigerstripe.annotation.core.Annotation)
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

	protected void collectAllAnnotations(List<Annotation> annotations,
			ProviderContext context, Object object) {
		IAnnotationProvider provider = context.getProvider();
		URI uri = getUri(provider, object);
		if (uri != null)
			annotations.addAll(getAnnotations(uri));

		String[] delegates = context.getDelegates();
		for (int i = 0; i < delegates.length; i++) {
			ProviderContext newContext = getProviderManager()
					.getProviderByType(delegates[i]);
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
		} catch (Exception e) {
			AnnotationPlugin.log(e);
		}
		return null;
	}

	public static Object getAdapted(Object object, String className) {
		Object adapted = Platform.getAdapterManager().loadAdapter(object,
				className);
		if (adapted != null)
			return adapted;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.core.IAnnotationManager#getAnnotatedObject
	 * (org.eclipse.tigerstripe.annotation.core.Annotation)
	 */
	public Object getAnnotatedObject(Annotation annotation) {
		return getObject(annotation.getUri());
	}

	protected ProviderManager getProviderManager() {
		try {
			providerManagerLock.lock();
			if (providerManager == null) {
				providerManager = new ProviderManager();
				IConfigurationElement[] configs = Platform
						.getExtensionRegistry().getConfigurationElementsFor(
								ANNOTATION_PROVIDER_EXTPT);
				for (IConfigurationElement config : configs) {
					try {
						ProviderContext context = new ProviderContext(config);
						providerManager.addProvider(context);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
			return providerManager;
		} finally {
			providerManagerLock.unlock();
		}
	}
	
	public IAnnotationParticipant[] getParticipants() {
		return participants.toArray(new IAnnotationParticipant[participants.size()]);
	}

	protected void loadParticipants() {
		participants = new ArrayList<IAnnotationParticipant>();
		IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ANNOTATION_PARTICIPANT_EXTPT);
		for (IConfigurationElement config : configs) {
			try {
				IAnnotationParticipant participant = (IAnnotationParticipant) config
						.createExecutableExtension(ANNOTATION_ATTR_CLASS);
				participant.initialize();
				participants.add(participant);
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
	}

	protected void validateAnnotation(Annotation annotation, Object object)
			throws AnnotationConstraintException {
		IAnnotationValidationContext context = new AnnotationValidationContext(
				annotation, object);
		IAnnotationConstraint[] constraints = getConstraints();
		for (int i = 0; i < constraints.length; i++) {
			IStatus status = constraints[i].validate(context);
			if (status != null && !status.isOK()) {
				throw new AnnotationConstraintException(status.getMessage(),
						status.getException());
			}
		}
	}

	protected IAnnotationConstraint[] getConstraints() {
		try {
			constraintsLock.lock();
			if (constraints == null) {
				ArrayList<IAnnotationConstraint> constraints = new ArrayList<IAnnotationConstraint>();
				IConfigurationElement[] configs = Platform
						.getExtensionRegistry().getConfigurationElementsFor(
								ANNOTATION_CONSTRAINT_EXTPT);
				for (IConfigurationElement config : configs) {
					try {
						IAnnotationConstraint constraint = (IAnnotationConstraint) config
								.createExecutableExtension(ANNOTATION_ATTR_CLASS);
						constraints.add(constraint);
					} catch (Exception e) {
						AnnotationPlugin.log(e);
					}
				}
				this.constraints = constraints
						.toArray(new IAnnotationConstraint[constraints.size()]);
			}
			return constraints;

		} finally {
			constraintsLock.unlock();
		}
	}

	protected Map<EPackage, String> getPackageLables() {
		try {
			lablesLock.lock();
			if (lables == null) {
				lables = new ConcurrentHashMap<EPackage, String>();
				IConfigurationElement[] configs = Platform
						.getExtensionRegistry().getConfigurationElementsFor(
								ANNOTATION_PACKAGE_LABEL_EXTPT);
				for (IConfigurationElement config : configs) {
					try {
						EPackage pckg = AnnotationUtils.getPackage(config
								.getAttribute(ATTR_URI));
						String newText = config.getAttribute(ATTR_NAME);
						String text = lables.get(pckg);
						if (text != null)
							throw new AnnotationException(
									"Can't define \""
											+ newText
											+ "\" label for "
											+ ATTR_URI
											+ " package, because it's already defined: "
											+ text);
						lables.put(pckg, newText);
					} catch (Exception e) {
						AnnotationPlugin.log(e);
					}
				}
			}
			return lables;

		} finally {
			lablesLock.unlock();
		}
	}

	public Map<String, AnnotationType> getTypesMap() {
		try {
			typesLock.lock();
			if (this.types == null) {
				types = new HashMap<String, AnnotationType>();
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
			}
			return types;
		} finally {
			typesLock.unlock();
		}
	}

	public AnnotationType[] getTypes() {
		Collection<AnnotationType> types = getTypesMap().values();
		return types.toArray(new AnnotationType[types.size()]);
	}

	public AnnotationType getType(String epackage, String eclass) {
		return getTypesMap().get(epackage + "." + eclass);
	}

	public List<Adapter> getAdapters() {
		try {
			adaptersLock.lock();
			if (adapters == null) {
				adapters = new ArrayList<Adapter>();
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
			}
			return adapters;
		} finally {
			adaptersLock.unlock();
		}
	}

	public Object getObject(URI uri) {
		ProviderContext[] providers = getProviderManager().getProviders();
		for (ProviderContext providerContext : providers) {
			IAnnotationProvider provider = providerContext.getProvider();
			try {
				Object object = provider.getObject(uri);
				if (object != null)
					return object;
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

}
