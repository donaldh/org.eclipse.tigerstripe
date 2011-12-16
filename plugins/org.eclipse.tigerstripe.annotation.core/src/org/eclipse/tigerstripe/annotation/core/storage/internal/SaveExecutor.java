package org.eclipse.tigerstripe.annotation.core.storage.internal;

import static org.eclipse.core.resources.IResource.FOLDER;
import static org.eclipse.tigerstripe.annotation.core.AnnotationPlugin.warn;
import static org.eclipse.tigerstripe.annotation.core.Helper.toArray;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;

/**
 * This class respond for saving resources into workspace.
 * Clients send requests to save EMF resource, executor put it into
 * queue and execute save job. Save job is executed with delay for avoid 
 * saving the same resources in a short period of time.
 */
public class SaveExecutor {

	private final Set<IResource> savingResources = Collections
			.synchronizedSet(new HashSet<IResource>());

	private final Set<Resource> toSave = new HashSet<Resource>();
	private final Storage storage;
	private final Map<Object, Object> saveOptions;
	private final Job job;

	public SaveExecutor(Storage storage, Map<Object, Object> saveOptions) {
		this.storage = storage;
		this.saveOptions = saveOptions;
	}

	{
		job = new Job("Save Annotation Files") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
						
						public void run(IProgressMonitor monitor) throws CoreException {
							Resource[] resources;
							synchronized (SaveExecutor.this) {
								resources = toArray(Resource.class, toSave);
								toSave.clear();
							}
							
							storage.checkpoint();
							storage.silentMode();
							try {
								for (Resource resource : resources) {
									
									URI uri = resource.getURI();
									if (uri == null || uri.isArchive()) {
										continue;
									}
									
									final IFile file = WorkspaceSynchronizer.getFile(resource);
									
									if (file == null) {
										warn("Resource '%s' has no workspace resource. Can't save",
												resource.getURI());
										continue;
									}
									savingResources.add(file);
									try {
										if (resource.getContents().isEmpty()) {
											resource.delete(null);
											removeDanglingFolders(file, monitor);
										} else {
											resource.save(saveOptions);
										}
									} catch (Exception e) {
										AnnotationPlugin.log(e);
									}
								}
							} finally {
								storage.checkpoint();
							}
						}
					}, null, IWorkspace.AVOID_UPDATE, monitor);
				} catch (CoreException e) {
					AnnotationPlugin.log(e);
				}
				return Status.OK_STATUS;
			}
		};
		job.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				checkForRun();
			}
		});
		IWorkspaceRoot wroot = ResourcesPlugin.getWorkspace().getRoot();
		job.setRule(wroot);
	}

	public boolean isSaving(IResource resource) {
		return savingResources.contains(resource);
	}

	public boolean removeFromSaving(IResource resource) {
		return savingResources.remove(resource);
	}

	public void save(Resource resource) {
		synchronized (this) {
			toSave.add(resource);
		}
		checkForRun();
	}

	public synchronized void removeFromQueue(IFile file) {
		toSave.remove(file);
	}
	
	public synchronized void checkForRun() {
		if (job.getState() != Job.NONE) {
			return;
		}
		if (toSave.isEmpty()) {
			return;
		}
		job.schedule(500);
	}

	private void removeDanglingFolders(IFile file, IProgressMonitor monitor)
			throws CoreException {
		IContainer parent = file.getParent();
		IFolder toDelete = null;
		int possibleChildren = 0;
		while (parent != null && parent.getType() == FOLDER
				&& parent.members().length == possibleChildren) {
			toDelete = (IFolder) parent;
			parent = parent.getParent();
			possibleChildren = 1;
		}
		if (toDelete != null) {
			toDelete.delete(true, monitor);
		}
	}
}
