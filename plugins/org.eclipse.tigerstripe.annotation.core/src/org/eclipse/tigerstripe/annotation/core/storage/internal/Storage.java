package org.eclipse.tigerstripe.annotation.core.storage.internal;

import static org.eclipse.core.resources.IResourceChangeEvent.POST_CHANGE;
import static org.eclipse.core.resources.IResourceDelta.MARKERS;
import static org.eclipse.emf.ecore.resource.Resource.RESOURCE__CONTENTS;
import static org.eclipse.emf.ecore.xmi.XMLResource.OPTION_RESOURCE_HANDLER;
import static org.eclipse.tigerstripe.annotation.core.Helper.makeUri;
import static org.eclipse.tigerstripe.annotation.core.Helper.runForWrite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.Filter;
import org.eclipse.tigerstripe.annotation.core.Helper;
import org.eclipse.tigerstripe.annotation.core.Searcher;
import org.eclipse.tigerstripe.annotation.internal.core.IAnnotationFilesRecognizer;

/**
 * This class responsible for 
 * 1. Synchronizing state between the eclipse workspace and ResourceSet state
 * 2. Listening changing in resource set and send save request if resources was changed
 * 3. Implementing the {@link Searcher} interface and do search in all annotations using {@link Filter}
 */
public class Storage implements IResourceChangeListener, ISchedulingRule,
		AutosaveManager, Searcher {

	private final TransactionalEditingDomain domain;
	private final IAnnotationFilesRecognizer annFilesRecognizer;
	private final SaveExecutor saveExecutor;
	private final Set<Transaction> ignoreTransactions = new HashSet<Transaction>();
	private final Map<Object, Object> options;
	
	private final ResourceSetListener resourceSetListener = new ResourceSetListenerImpl(){
		
		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			InternalTransaction currentTransaction = (InternalTransaction) event.getTransaction();
			InternalTransaction rootTransaction = currentTransaction.getRoot();
			boolean ignoreTransaction = ignoreTransactions.contains(rootTransaction);
			
			if (ignoreTransaction) {
				if (currentTransaction.equals(rootTransaction)) {
					ignoreTransactions.remove(currentTransaction);
				}
				return;
			}
			
			Set<Resource> toSave = new HashSet<Resource>();
				for (Notification n : event.getNotifications()) {
					if (n.isTouch()) {
						continue;
					}
					Object notifier = n.getNotifier();
					if (notifier instanceof Resource) {
						Resource r = (Resource) notifier;
						
						if (n.getFeatureID(null) == RESOURCE__CONTENTS &&
							r.getResourceSet() != null &&
							r.isLoaded()) {
							
							toSave.add((Resource) notifier);
						}
					} else if (notifier instanceof EObject) {
						EObject eo = (EObject) notifier;
						if (eo.eResource() != null) {
							toSave.add(eo.eResource());
						}
					} 
				}
			
			for (Resource resource : toSave) {
				save(resource);
			}
		}
	};
	
	public Storage(TransactionalEditingDomain domain, IAnnotationFilesRecognizer annFilesRecognizer) {
		this.domain = domain;
		this.annFilesRecognizer = annFilesRecognizer;
		
		ResourceSet resourceSet = domain.getResourceSet();
		Registry delegat = resourceSet.getResourceFactoryRegistry();
		AnnotationResourceFactoryRegistry factoryRegistry = new AnnotationResourceFactoryRegistry(delegat);
		resourceSet.setResourceFactoryRegistry(factoryRegistry);
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, POST_CHANGE);
		domain.addResourceSetListener(resourceSetListener);
		
		AnnotationResourceHandler handler = AnnotationResourceHandler.getInstance();
		options = Collections.<Object, Object>singletonMap(OPTION_RESOURCE_HANDLER, handler);
		resourceSet.getLoadOptions().putAll(options);
		
		saveExecutor = new SaveExecutor(domain, this, options);
		
		scanResources();
	}

	private void scanResources() {
		
		Job proccessJob = new Job("Scanning for Annotations") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				
				runForWrite(domain, new Runnable() {
					
					public void run() {
						disableAutoSave();
						for (IProject proj : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
							if (!proj.exists() || !proj.isOpen()) {
								continue;
							}
							try {
								proj.accept(new IResourceVisitor() {
									public boolean visit(IResource resource) throws CoreException {
										if (resource instanceof IContainer) {
											return annFilesRecognizer
													.couldContainAnnotationFiles((IContainer) resource);
										} else if (resource instanceof IFile) {
											IFile file = (IFile) resource;
											if (annFilesRecognizer.isAnnotationFile(file)) {
												annotationFileWasAdded(file);
											}
										}
										return true;
									}
								});
							} catch (CoreException e) {
								AnnotationPlugin.log(e);
							}
						}
					}
				});

				
				return Status.OK_STATUS;
			}
		};
		proccessJob.setPriority(Job.LONG);
		proccessJob.setRule(ResourcesPlugin.getWorkspace().getRoot());
		proccessJob.schedule();
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		domain.removeResourceSetListener(resourceSetListener);
	}
	
	public void resourceChanged(IResourceChangeEvent event) {
		
		if (event == null || event.getType() != POST_CHANGE) {
			return;
		}
		IResourceDelta delta = event.getDelta();
		
		if (delta == null) {
			return;
		}
		
		final Set<IFile> added = new HashSet<IFile>();
		final Set<IFile> changed = new HashSet<IFile>();
		final Set<IFile> removed = new HashSet<IFile>();
		final Set<IProject> removedProjects = new HashSet<IProject>();
		
		try {
			delta.accept(new IResourceDeltaVisitor() {
				
				public boolean visit(IResourceDelta delta) throws CoreException {
					if (delta == null) {
						return false;
					}
					
					if (!isSet(delta.getFlags(), MARKERS)) {
						IResource resource = delta.getResource();
						
						if (resource instanceof IFile) {
							IFile file = (IFile) resource;
							if (annFilesRecognizer.isAnnotationFile(file)) {
								if (saveExecutor.isSaving(resource)) {
									saveExecutor.removeFromSaving(resource);
									return false;
								}
								
								switch (delta.getKind()) {
								case IResourceDelta.ADDED:
									added.add(file);
									break;
								case IResourceDelta.CHANGED:
									changed.add(file);
									break;
								case IResourceDelta.REMOVED:
									removed.add(file);
									break;
								}
							}
						} else if (resource instanceof IProject) {
							switch (delta.getKind()) {
							case IResourceDelta.REMOVED:
								removedProjects.add((IProject) resource);
								break;
							}
						}
					}
					return true;
				}
			});
		} catch (CoreException e) {
			AnnotationPlugin.log(e);
		}
		if (!added.isEmpty() || !changed.isEmpty() || !removed.isEmpty()
				|| !removedProjects.isEmpty()) {
					
			runForWrite(domain, new Runnable() {
				
				public void run() {
					disableAutoSave();
					for (IFile file : added) {
						annotationFileWasAdded(file);
					}
					for (IFile file : changed) {
						annotationFileWasChanged(file);
					}
					for (IFile file : removed) {
						annotationFileWasRemoved(file);
					}
					for (IProject proj : removedProjects) {
						projectWasRemoved(proj);
					}
				}
			});
		}
	}
	
	private void projectWasRemoved(IProject proj) {
		Iterator<Resource> it = getResourceSet().getResources().iterator();
		while (it.hasNext()) {
			Resource resource = it.next();
			IFile file = WorkspaceSynchronizer.getFile(resource);
			if (file == null) {
				continue;
			}
			saveExecutor.removeFromQueue(file);
			if (proj.equals(file.getProject())) {
				it.remove();
			}
		}
	}
	
	public void save(Resource resource) {
		if (resource == null) {
			return;
		}
		saveExecutor.save(resource);
	}
	
	private void annotationFileWasChanged(final IFile file) {
		saveExecutor.removeFromQueue(file);
		Resource resource = getResourceSet().getResource(makeUri(file), false);
		if (resource != null) {
			resource.unload();
			try {
				resource.load(getResourceSet().getLoadOptions());
			} catch (IOException e) {
				AnnotationPlugin.log(e);
			}
		}
	}
	
	private void annotationFileWasAdded(final IFile file) {
		ResourceSet rs = getResourceSet();
		URI uri = makeUri(file);
		Resource resource = rs.getResource(uri, false);
		if (resource == null) {
			resource = rs.createResource(uri);
			try {
				resource.load(rs.getLoadOptions());
			} catch (IOException e) {
				AnnotationPlugin.log(e);
			}
		}
	}
	
	private void annotationFileWasRemoved(final IFile file) {
		saveExecutor.removeFromQueue(file);
		Resource resource = getResourceSet().getResource(makeUri(file), false);
		if (resource != null) {
			resource.unload();
			getResourceSet().getResources().remove(resource);
		}
	}

	/**
	 * Disables autosave till transiaction not finished
	 */
	public void disableAutoSave() {
		InternalTransaction activeTransaction = ((InternalTransactionalEditingDomain) domain)
				.getActiveTransaction();
		
		if (activeTransaction == null) {
			throw new IllegalStateException("Active transaction is null");
		}
		ignoreTransactions.add(activeTransaction.getRoot());
	}

	private static boolean isSet(int flags, int mask) {
		return (flags & mask) == mask;
	}
	
	public List<Annotation> findTransactional(final Filter filter) {
		
		return Helper.runExclusive(domain, new RunnableWithResult.Impl<List<Annotation>>() {

			public void run() {
				setResult(find(filter));
			}
		});
	}

	public List<Annotation> find(final Filter filter) {
		final List<Annotation> result = new ArrayList<Annotation>();

		List<Resource> resources = new ArrayList<Resource>(getResourceSet().getResources());
		
		Collections.sort(resources, ResourcesComparator.INSTANCE);
		
		for (Resource resource : resources) {
			for (EObject obj : resource.getContents()) {
				if (obj instanceof Annotation) {
					Annotation ann = (Annotation) obj;
					if (filter.apply(ann)) {
						result.add(ann);
					}
				}
			}
		}
		return result;
	}
	
	private static enum ResourcesComparator implements Comparator<Resource> {
		
		INSTANCE;

		public int compare(Resource o1, Resource o2) {
			if (o1 == null) {
				return o2 == null ? 0 : 1;
			}
			URI uri1 = o1.getURI();
			URI uri2 = o2.getURI();
			if (uri1 == null) {
				return uri2 == null ? 0 : 1;
			}
			return uri1.toString().compareTo(uri2.toString());
		}
	}
	
	public Resource getResourceForWrite(final URI uri) {
		ResourceSet rs = getResourceSet();
		Resource resource = rs.getResource(uri, false);
		if (resource == null) {
			resource = rs.createResource(uri);
		}
		return resource;
	}
	
	public ResourceSet getResourceSet() {
		return domain.getResourceSet();
	}
	
	public boolean contains(ISchedulingRule rule) {
		return rule == this;
	}

	public boolean isConflicting(ISchedulingRule rule) {
		return rule == this;
	}

	public Map<Object, Object> getOptions() {
		return options;
	}
}
