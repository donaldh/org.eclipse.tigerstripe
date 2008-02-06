/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.natures.NatureMigrationUtils;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.GeneralPreferencePage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.TigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.gmf.synchronization.DiagramSynchronizationManager;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * An instance of this is started by the EclipsePlugin class to listener for
 * Workspace changes and propagate them to the corresponding Artifact Mgr as
 * appropriate.
 * 
 * For now, this only listens for Package deletion and propagates that to the
 * corresponding Artifact Mgr so it knows it needs to remove the corresponding
 * artifacts.
 * 
 * @author Eric Dillon
 * 
 */
public class WorkspaceListener implements IElementChangedListener,
		IResourceChangeListener {

	private class MovedElement {
		public IJavaElement fromElement;

		public IJavaElement toElement;
	}

	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();

		// Get the list of removed resources
		Collection<IResource> removedResources = new HashSet<IResource>();
		Collection<IResource> changedResources = new HashSet<IResource>();
		Collection<IResource> addedResources = new HashSet<IResource>();
		buildResourcesLists(event.getDelta(), removedResources,
				changedResources, addedResources);

		checkProjectAdded(addedResources);
		checkProjectRemoved(removedResources);
		checkRemovedFacet(removedResources);
		checkActiveFacetChanged(changedResources);
	}

	private void checkProjectAdded(Collection<IResource> addedResources) {
		for (IResource res : addedResources) {
			if (res instanceof IProject) {

				IProject iProject = (IProject) res;
				try {
					NatureMigrationUtils.handleProjectMigration(iProject);
				} catch (CoreException e) {
					EclipsePlugin.log(e);
				}
				IAbstractTigerstripeProject tsProject = EclipsePlugin
						.getITigerstripeProjectFor(iProject);
				if (tsProject instanceof ITigerstripeProject) {
					DiagramSynchronizationManager.getInstance()
							.addTSProjectToWatch(
									(ITigerstripeProject) tsProject);
				}
			}
		}
	}

	private void checkProjectRemoved(Collection<IResource> removedResources) {
		for (IResource res : removedResources) {
			if (res instanceof IProject) {
				IProject jProject = (IProject) res;
				try {
					IPath path = jProject.getFullPath();
					IPath root = ResourcesPlugin.getWorkspace().getRoot()
							.getLocation();
					URI targetURI = root.append(path).toFile().toURI();
					IAbstractTigerstripeProject tsProject = TigerstripeCore
							.findProject(targetURI);

					// Bug 936: remove from watch list of
					// DiagramSynchronizationManager
					if (tsProject instanceof ITigerstripeProject)
						DiagramSynchronizationManager.getInstance()
								.removeTSProjectToWatch(
										(ITigerstripeProject) tsProject);

				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	private void checkActiveFacetChanged(Collection<IResource> changedResources) {
		for (IResource res : changedResources) {
			if (IContractSegment.FILE_EXTENSION.equals(res.getFileExtension())) {
				ITigerstripeProject tsProject = getCorrespondingTSProject(res);
				try {
					if (tsProject.getActiveFacet() != null) {
						final IFacetReference ref = tsProject.getActiveFacet();
						if (res.getLocationURI().equals(ref.getURI())) {
							// Reloading active facet
							IRunnableWithProgress op = new IRunnableWithProgress() {
								public void run(IProgressMonitor monitor) {
									TigerstripeProgressMonitor tsMonitor = new TigerstripeProgressMonitor(
											monitor);

									// compute the facet predicate while in the
									// feedback thread
									ref.computeFacetPredicate(tsMonitor);
								}
							};
							IWorkbench wb = PlatformUI.getWorkbench();
							IWorkbenchWindow win = wb
									.getActiveWorkbenchWindow();
							Shell shell = win != null ? win.getShell() : null;

							try {
								ProgressMonitorDialog dialog = new ProgressMonitorDialog(
										shell);
								dialog.run(true, false, op);
								tsProject.setActiveFacet(ref,
										new TigerstripeNullProgressMonitor());
							} catch (InterruptedException ee) {
								EclipsePlugin.log(ee);
							} catch (InvocationTargetException ee) {
								EclipsePlugin.log(ee);
							} catch (TigerstripeException ee) {
								EclipsePlugin.log(ee);
							}
						}
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	private void checkRemovedFacet(Collection<IResource> removedResources) {
		for (IResource res : removedResources) {
			if (IContractSegment.FILE_EXTENSION.equals(res.getFileExtension())) {
				ITigerstripeProject tsProject = getCorrespondingTSProject(res);
				try {
					if (tsProject != null && tsProject.getActiveFacet() != null) {
						IFacetReference ref = tsProject.getActiveFacet();
						if (res.getLocationURI().equals(ref.getURI())) {
							tsProject.resetActiveFacet();
						}
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	protected ITigerstripeProject getCorrespondingTSProject(IResource resource) {
		IProject project = resource.getProject();
		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getITigerstripeProjectFor(project);
		if (aProject instanceof ITigerstripeProject) {
			ITigerstripeProject tsProject = (ITigerstripeProject) aProject;
			return tsProject;
		}
		return null;
	}

	public static void buildResourcesLists(IResourceDelta delta,
			Collection itemsToRemove, Collection itemsChanged,
			Collection itemsAdded) {
		if (delta == null) // occurs when deleting project
			return;

		if (delta.getKind() == IResourceDelta.REMOVED) {
			itemsToRemove.add(delta.getResource());
		} else if (delta.getKind() == IResourceDelta.CHANGED
				&& delta.getAffectedChildren().length == 0) {
			itemsChanged.add(delta.getResource());
		} else if (delta.getKind() == IResourceDelta.ADDED
				&& delta.getAffectedChildren().length == 0) {
			itemsAdded.add(delta.getResource());
		} else if (delta.getKind() == IResourceDelta.ADDED
				&& delta.getResource() instanceof IProject) {
			itemsAdded.add(delta.getResource());
		}
		IResourceDelta[] children = delta.getAffectedChildren();
		for (int i = 0; i < children.length; i++) {
			buildResourcesLists(children[i], itemsToRemove, itemsChanged,
					itemsAdded);
		}
	}

	public void elementChanged(ElementChangedEvent event) {

		MovedElement[] allChildren = getAllChildren(event);

		for (MovedElement element : allChildren) {
			if (element.fromElement instanceof IPackageFragment) {
				IPackageFragment packFragment = (IPackageFragment) element.fromElement;
				if (element.toElement == null)
					removeFragmentContent(packFragment);
				else
					renameFragmentContent(packFragment,
							(IPackageFragment) element.toElement);
			} else if (element.fromElement instanceof ICompilationUnit) {
				if (element.toElement != null) {
					renameArtifact((ICompilationUnit) element.fromElement,
							(ICompilationUnit) element.toElement);
				}
			}
		}

		Set<IProject> projectsToRefresh = new HashSet<IProject>();
		for (MovedElement element : allChildren) {
			try {
				IJavaElement toElement = element.toElement;
				if (toElement != null
						&& toElement.getCorrespondingResource() != null) {
					IProject proj = element.toElement
							.getCorrespondingResource().getProject();
					if (proj != null)
						projectsToRefresh.add(proj);
				}
			} catch (JavaModelException e) {
				EclipsePlugin.log(e);
			}
		}
		refreshTSContent(projectsToRefresh);
	}

	private void refreshTSContent(Set<IProject> projects) {
		for (IProject jProject : projects) {
			IProject project = jProject.getProject();
			IAbstractTigerstripeProject atsProject = EclipsePlugin
					.getITigerstripeProjectFor(project);
			if (atsProject instanceof ITigerstripeProject) {
				ITigerstripeProject tsProject = (ITigerstripeProject) atsProject;
				try {
					IArtifactManagerSession session = tsProject
							.getArtifactManagerSession();
					session.refresh(new TigerstripeNullProgressMonitor());
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	private void renameArtifact(ICompilationUnit fromUnit,
			ICompilationUnit toUnit) {
		IJavaProject jProject = fromUnit.getJavaProject();
		if (jProject != null) {
			IProject project = jProject.getProject();
			IAbstractTigerstripeProject atsProject = EclipsePlugin
					.getITigerstripeProjectFor(project);
			if (atsProject instanceof ITigerstripeProject) {
				ITigerstripeProject tsProject = (ITigerstripeProject) atsProject;
				try {
					IArtifactManagerSession session = tsProject
							.getArtifactManagerSession();
					String fqn = extractFQN(fromUnit);
					IAbstractArtifact artifact = session
							.getArtifactByFullyQualifiedName(fqn, false);
					if (artifact != null) {
						TigerstripeRuntime.logInfoMessage("Detected rename: "
								+ extractFQN(fromUnit) + " to "
								+ extractFQN(toUnit));
						session.renameArtifact(artifact, extractFQN(toUnit));
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	/**
	 * Returns the FQN corresponding to the given unit to be used for the
	 * Artifact Mgr
	 * 
	 * @param unit
	 * @return
	 */
	private String extractFQN(ICompilationUnit unit) {
		String result = null;
		if (unit.getParent() instanceof IPackageFragment) {
			IPackageFragment parentFragment = (IPackageFragment) unit
					.getParent();
			String packageName = parentFragment.getElementName();
			String name = unit.getElementName();
			if (name.indexOf(".") != -1) {
				name = name.substring(0, name.indexOf("."));
			}
			if (packageName != null && packageName.length() != 0) {
				name = packageName + "." + name;
			}
			result = name;
		}
		return result;
	}

	private void removeFragmentContent(IPackageFragment fragment) {
		IJavaProject jProject = fragment.getJavaProject();
		if (jProject != null) {
			IProject project = jProject.getProject();
			IAbstractTigerstripeProject atsProject = EclipsePlugin
					.getITigerstripeProjectFor(project);
			if (atsProject instanceof ITigerstripeProject) {
				ITigerstripeProject tsProject = (ITigerstripeProject) atsProject;
				try {
					IArtifactManagerSession session = tsProject
							.getArtifactManagerSession();
					Set<IRelationship> relationshipToCascadeDelete = session
							.removePackageContent(fragment.getElementName());

					if (true) {
						final Set<IRelationship> fSet = relationshipToCascadeDelete;
						final IModelUpdater updater = session
								.getIModelUpdater();
						Job cascadeDeleteJob = new Job(
								"Cascade delete relationships") {
							@Override
							protected IStatus run(IProgressMonitor monitor) {
								try {
									handleRelationshipsToCascadeDelete(fSet,
											updater, monitor);
									return Status.OK_STATUS;
								} catch (TigerstripeException e) {
									Status st = new Status(
											IStatus.ERROR,
											TigerstripePluginConstants.PLUGIN_ID,
											222,
											"Error while cascade deleting relationships:"
													+ e.getMessage(), e);
									return st;
								}
							}
						};
						cascadeDeleteJob.schedule();
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	/**
	 * Handles the deletion of given relationship.
	 * 
	 * This method is called when either one of their ends has been removed from
	 * the model.
	 * 
	 * Also, AssociationClasses are handled in here in a special way to ensure
	 * recursive cases (i.e. relationships to AssociationClasses that should be
	 * removed as a side effect of the association class being removed).
	 * 
	 * @param toDeletes
	 * @param updater
	 * @param monitor
	 * @throws TigerstripeException
	 */
	public static void handleRelationshipsToCascadeDelete(
			Set<IRelationship> toDeletes, IModelUpdater updater,
			IProgressMonitor monitor) throws TigerstripeException {

		// This may be turned off by the user through the General preference
		// page
		if (!EclipsePlugin.getDefault().getPreferenceStore().getBoolean(
				GeneralPreferencePage.P_CASCADEDELETE_RELATIONSHIPS))
			return;

		Set<IRelationship> additionalRelationships = new HashSet<IRelationship>();
		computeAdditionalRelationships(toDeletes, additionalRelationships,
				updater);

		toDeletes.addAll(additionalRelationships);

		IModelUpdater theUpdater = updater;
		monitor.beginTask("Deleting", toDeletes.size());
		for (IRelationship toDelete : toDeletes) {
			monitor.subTask(((IAbstractArtifact) toDelete)
					.getFullyQualifiedName());
			// If no updater was provided, let's try and figure it out...
			if (updater == null) {
				theUpdater = null;
				try {
					IAbstractArtifact art = (AbstractArtifact) toDelete;
					theUpdater = art.getUpdater();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}

			if (theUpdater != null) {
				IArtifactDeleteRequest request = (IArtifactDeleteRequest) theUpdater
						.getRequestFactory().makeRequest(
								IArtifactDeleteRequest.class.getName());
				request.setArtifactName(((IAbstractArtifact) toDelete)
						.getName());
				request.setArtifactPackage(((IAbstractArtifact) toDelete)
						.getPackage());

				theUpdater.handleChangeRequest(request);

				IResource res = TSExplorerUtils
						.getIResourceForArtifact(((IAbstractArtifact) toDelete));

				if (res != null) {
					try {
						res.delete(true, new NullProgressMonitor());
					} catch (CoreException e) {
						EclipsePlugin.log(e);
					}
				}
			}
			monitor.worked(1);
		}
		monitor.done();
	}

	/**
	 * When removing an association class, we may need to remove any
	 * relationship to/from that association class.
	 * 
	 * This methods recursively looks for assocClasses being removed and builds
	 * a list of additional relationships to remove
	 * 
	 * @param existing
	 * @param additional
	 */
	private static void computeAdditionalRelationships(
			Collection<IRelationship> existing, Set<IRelationship> additional,
			IModelUpdater updater) {
		Set<IRelationship> newlyFoundRelationships = new HashSet<IRelationship>();

		boolean foundSome = false;
		for (IRelationship rel : existing) {

			IModelUpdater theUpdater = updater;
			if (theUpdater == null) {
				try {
					theUpdater = ((IAbstractArtifact) rel).getUpdater();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
			if (theUpdater != null) {
				if (rel instanceof IAssociationClassArtifact) {
					try {
						IAssociationClassArtifact assocClass = (IAssociationClassArtifact) rel;
						Collection<IRelationship> orig = theUpdater
								.getSession().getOriginatingRelationshipForFQN(
										assocClass.getFullyQualifiedName(),
										false);
						if (orig.size() != 0) {
							foundSome = true;
							additional.addAll(orig);
							newlyFoundRelationships.addAll(orig);
						}
						Collection<IRelationship> term = theUpdater
								.getSession().getTerminatingRelationshipForFQN(
										assocClass.getFullyQualifiedName(),
										false);
						if (term.size() != 0) {
							foundSome = true;
							additional.addAll(term);
							newlyFoundRelationships.addAll(term);
						}
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}

		if (foundSome) {
			computeAdditionalRelationships(newlyFoundRelationships, additional,
					updater);
		}
	}

	private void renameFragmentContent(IPackageFragment fromFragment,
			IPackageFragment toFragment) {
		IJavaProject jProject = fromFragment.getJavaProject();
		if (jProject != null) {
			IProject project = jProject.getProject();
			IAbstractTigerstripeProject atsProject = EclipsePlugin
					.getITigerstripeProjectFor(project);
			if (atsProject instanceof ITigerstripeProject) {
				ITigerstripeProject tsProject = (ITigerstripeProject) atsProject;
				try {
					IArtifactManagerSession session = tsProject
							.getArtifactManagerSession();
					session.renamePackageContent(fromFragment.getElementName(),
							toFragment.getElementName());
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	private MovedElement[] getAllChildren(ElementChangedEvent event) {
		List<MovedElement> result = getAllChildren(event.getDelta());
		return result.toArray(new MovedElement[result.size()]);
	}

	private List<MovedElement> getAllChildren(IJavaElementDelta delta) {
		ArrayList<MovedElement> result = new ArrayList<MovedElement>();

		if (delta.getRemovedChildren().length != 0) {
			for (IJavaElementDelta remDelta : delta.getRemovedChildren()) {
				MovedElement elm = new MovedElement();
				elm.fromElement = remDelta.getElement();
				elm.toElement = remDelta.getMovedToElement();
				result.add(elm);
			}
			return result;
		}

		if (delta.getAffectedChildren().length == 0)
			return result;
		else {
			for (IJavaElementDelta aDelta : delta.getAffectedChildren()) {
				result.addAll(getAllChildren(aDelta));
			}
			return result;
		}
	}

}
