/******************************************************************************* * Copyright (c) 2007 Cisco Systems, Inc. * All rights reserved. This program and the accompanying materials * are made available under the terms of the Eclipse Public License v1.0 * which accompanies this distribution, and is available at * http://www.eclipse.org/legal/epl-v10.html * * Contributors: *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing *******************************************************************************/package org.eclipse.tigerstripe.workbench.internal.builder;import java.lang.reflect.InvocationTargetException;import java.util.ArrayList;import java.util.Collection;import java.util.HashSet;import java.util.List;import java.util.Set;import org.eclipse.core.internal.resources.WorkspaceRoot;import org.eclipse.core.resources.IFolder;import org.eclipse.core.resources.IProject;import org.eclipse.core.resources.IResource;import org.eclipse.core.resources.IResourceChangeEvent;import org.eclipse.core.resources.IResourceChangeListener;import org.eclipse.core.resources.IResourceDelta;import org.eclipse.core.resources.IResourceDeltaVisitor;import org.eclipse.core.runtime.CoreException;import org.eclipse.core.runtime.IPath;import org.eclipse.core.runtime.IProgressMonitor;import org.eclipse.core.runtime.IStatus;import org.eclipse.core.runtime.NullProgressMonitor;import org.eclipse.core.runtime.Path;import org.eclipse.core.runtime.Status;import org.eclipse.core.runtime.jobs.Job;import org.eclipse.jdt.core.ElementChangedEvent;import org.eclipse.jdt.core.ICompilationUnit;import org.eclipse.jdt.core.IElementChangedListener;import org.eclipse.jdt.core.IJavaElement;import org.eclipse.jdt.core.IJavaElementDelta;import org.eclipse.jdt.core.IJavaProject;import org.eclipse.jdt.core.IPackageFragment;import org.eclipse.jdt.core.JavaModelException;import org.eclipse.jface.dialogs.ProgressMonitorDialog;import org.eclipse.jface.operation.IRunnableWithProgress;import org.eclipse.swt.widgets.Shell;import org.eclipse.tigerstripe.workbench.TigerstripeCore;import org.eclipse.tigerstripe.workbench.TigerstripeException;import org.eclipse.tigerstripe.workbench.internal.BasePlugin;import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactDeleteRequest;import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;import org.eclipse.tigerstripe.workbench.internal.builder.natures.ProjectMigrationUtils;import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeM0GeneratorNature;import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripePluginProjectNature;import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeProjectNature;import org.eclipse.tigerstripe.workbench.internal.contract.segment.FacetReference;import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;import org.eclipse.ui.IWorkbench;import org.eclipse.ui.IWorkbenchWindow;import org.eclipse.ui.PlatformUI;/** * An instance of this is started by the BasePlugin class to listen for * Workspace changes and propagate them to the corresponding Artifact Mgr as * appropriate. *  *  * <b>History of changes</b> (Name: Modification): <br/> * Eric Dillon 	  :	Initial Creation <br/> * Dan and Navid  : Significant performance improvements per defect 324197 *  */public class WorkspaceListener implements IElementChangedListener,		IResourceChangeListener {    private final String BIN_FOLDER = "bin";    private final String CLASS_EXTENSION = "class";    private final String JAVA_EXTENSION = "java";    private final String PACKAGE_NAME = ".package";    private final String CLASS_DIAGRAM_EXTENSION = "wvd";    private final String CLASS_MODEL_EXTENSION = "vwm";    private final String INSTANCE_DIAGRAM_EXTENSION = "wod";    private final String INSTANCE_MODEL_EXTENSION = "owm";    	private class MovedElement {		public IJavaElement fromElement;		public IJavaElement toElement;	}	private ReferencesListener referencesListener = new ReferencesListener();	public void resourceChanged(IResourceChangeEvent event) {		final Collection<IResource> removedArtifacts = new HashSet<IResource>();        final Collection<IResource> removedProjects = new HashSet<IResource>();        final Collection<IResource> removedFacets = new HashSet<IResource>();        final Collection<IResource> changedArtifacts = new HashSet<IResource>();        final Collection<IResource> changedProjects = new HashSet<IResource>();        final Collection<IResource> changedDescriptors = new HashSet<IResource>();        final Collection<IResource> changedFacets = new HashSet<IResource>();        final Collection<IResource> addedArtifacts = new HashSet<IResource>();        final Collection<IResource> addedProjects = new HashSet<IResource>();                IResourceDelta delta = event.getDelta();        if (delta == null)            return;                boolean needToUpdate = false;        // Ignore bin folder, but it is not that simple        // Get the workspace child deltas, they should all be projects        IResourceDelta[] projectDeltas = delta.getAffectedChildren();        for (IResourceDelta projectDelta : projectDeltas) {            if (projectDelta.findMember(new Path(BIN_FOLDER)) != null) {                IResource projectResource = projectDelta.getResource();                if (projectResource instanceof IProject) {                    IResourceDelta[] folderDeltas = projectDelta.getAffectedChildren();                    if (folderDeltas != null && folderDeltas.length == 1) {                        IResourceDelta binFolderDelta = folderDeltas[0];                        if (binFolderDelta != null) {                            IResource binFolder = binFolderDelta.getResource();                            // If we find a project with a single child that is not the bin folder we need to update                            if (!(binFolder instanceof IFolder && binFolder.getName().equals(BIN_FOLDER))) {                                needToUpdate = true;                                break;                            }                        }                    }                    // If there is more than one child in the project we need to update                    else {                        needToUpdate = true;                        break;                    }                }                else {                    // This should never happen only can if there is something other than a project in the workspace root                    needToUpdate = true;                }            }            else {                needToUpdate = true;                break;            }        }                        if (!needToUpdate)            return;                IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {            public boolean visit(IResourceDelta delta) throws CoreException {                int kind = delta.getKind();                IResource resource = delta.getResource();                if (resource == null)                    return false;                                if (resource instanceof WorkspaceRoot)                    return true;                                String resourceFileExtension = resource.getFileExtension();                //We don't want to update all the listeners about the change of a .class file, diagram files, etc.                 if (shouldBeFiltered(resourceFileExtension)) {                    return false;                }                                switch(kind) {                    case IResourceDelta.REMOVED:                        if (resource instanceof IProject) { // If resource is a project                            removedProjects.add(resource);                        } else if (isArtifact(resource)) { // If resource is an artifact                            removedArtifacts.add(resource);                        } else if (IContractSegment.FILE_EXTENSION.equals(resourceFileExtension)) { // If resource is a facet                            removedFacets.add(resource);                        }                         break;                    case IResourceDelta.CHANGED:                        if (resource instanceof IProject) { // If resource is a project                            changedProjects.add(resource);                        } else if (isArtifact(resource)) { // If resource is an artifact                            changedArtifacts.add(resource);                        } else if (IContractSegment.FILE_EXTENSION.equals(resourceFileExtension)) { // If resource is a facet                            changedFacets.add(resource);                        } else if (ITigerstripeConstants.PROJECT_DESCRIPTOR.equals(resource.getName())) { // If resource is a project descriptor                            changedDescriptors.add(resource);                        }                        break;                    case IResourceDelta.ADDED:                        if (resource instanceof IProject) { // If resource is a project                            addedProjects.add(resource);                        } else if (isArtifact(resource)) { // If resource is an artifact                            addedArtifacts.add(resource);                        }                        break;                }                return true;            }        };        try {            delta.accept(visitor);            checkProjectAdded(addedProjects);            checkProjectRemoved(removedProjects);            //TODO Visit referencesListener to see what it is doing            referencesListener.changed(removedProjects, addedProjects, changedProjects);            checkRemovedFacet(removedFacets);            checkActiveFacetChanged(changedFacets);            checkTSDescriptorChanged(changedDescriptors);            checkArtifactResourceChanged(changedArtifacts);            checkArtifactResourceAdded(addedArtifacts);            checkArtifactResourceRemoved(removedArtifacts);        }        catch (CoreException e) {            BasePlugin.log(e);        }	}		/**	 * Checks if a resource is an artifact 	 * @param resource the resource to check	 * @return true if resource is an IResource otherwise false	 */	private boolean isArtifact(IResource resource) {	    if (resource != null) {	        String resourceFileExtension = resource.getFileExtension();    	    if ( JAVA_EXTENSION.equals(resourceFileExtension) || PACKAGE_NAME.equals(resource.getName())) {        	    // Need to filter out any "output folders" so that we can ignore any .java files created  there                ITigerstripeModelProject tsModel = (ITigerstripeModelProject) resource.getProject().getAdapter(ITigerstripeModelProject.class);                if (tsModel != null){                    // Note that if this is null, then this resource is not even in a ts project!                    try {                        String outputDir = tsModel.getProjectDetails().getOutputDirectory();                        IPath basePath = tsModel.getFullPath();                        basePath = basePath.append(outputDir);                        if (!resource.getFullPath().toString().startsWith(basePath.toString())){                            return true;                        }                    } catch (TigerstripeException e) {                        //Ignore                    }                }    	    }	    }        return false;	}		/**	 * Checks if a resource file extension is one that we don't care about	 * 	 * This list has the potential to grow greatly	 * @param fileExtension	 * @return true if we don't care, false if we do care	 */	private boolean shouldBeFiltered(String fileExtension) {	    if (CLASS_EXTENSION.equals(fileExtension) || 	            CLASS_DIAGRAM_EXTENSION.equals(fileExtension) ||	            CLASS_MODEL_EXTENSION.equals(fileExtension) ||	            INSTANCE_DIAGRAM_EXTENSION.equals(fileExtension) ||	            INSTANCE_MODEL_EXTENSION.equals(fileExtension)) {	        return true;	    }	    return false;	}		private void checkArtifactResourceChanged(Collection<IResource> changedResources) {		for (IResource res : changedResources) {							TigerstripeWorkspaceNotifier.INSTANCE					.signalArtifactResourceChanged(res);		}	}		private void checkArtifactResourceAdded(Collection<IResource> addedResources) {		for (IResource res : addedResources) {							TigerstripeWorkspaceNotifier.INSTANCE					.signalArtifactResourceAdded(res);		}	}		private void checkArtifactResourceRemoved(Collection<IResource> removedResources) {		for (IResource res : removedResources) {							TigerstripeWorkspaceNotifier.INSTANCE					.signalArtifactResourceRemoved(res);		}	}		private void checkTSDescriptorChanged(Collection<IResource> changedResources) {		for (IResource res : changedResources) {			if (ITigerstripeConstants.PROJECT_DESCRIPTOR.equals(res.getName())) {				TigerstripeWorkspaceNotifier.INSTANCE					.signalDescriptorChanged(res);			}		}	}		/**	 * 	 * @param addedResources	 */	private void checkProjectAdded(Collection<IResource> addedResources) {		// Make sure the nature migration occurs as needed.		for (IResource res : addedResources) {			if (res instanceof IProject) {				IProject iProject = (IProject) res;				try {					ProjectMigrationUtils.handleProjectMigration(iProject);					IAbstractTigerstripeProject tProject = (IAbstractTigerstripeProject) iProject							.getAdapter(IAbstractTigerstripeProject.class);					if (tProject != null)						TigerstripeWorkspaceNotifier.INSTANCE								.signalProjectAdded(tProject);				} catch (CoreException e) {					BasePlugin.log(e);				}			}		}	}	private void checkProjectRemoved(Collection<IResource> removedResources) {		for (IResource res : removedResources) {			if (res instanceof IProject) {				IProject iProject = (IProject) res;				try {					if (TigerstripeM0GeneratorNature.hasNature(iProject)							|| TigerstripePluginProjectNature									.hasNature(iProject)							|| TigerstripeProjectNature.hasNature(iProject))						TigerstripeWorkspaceNotifier.INSTANCE								.signalProjectDeleted(iProject.getName());				} catch (CoreException e) {					BasePlugin.log(e);				}			}		}	}	private void checkActiveFacetChanged(Collection<IResource> changedResources) {		for (IResource res : changedResources) {			if (IContractSegment.FILE_EXTENSION.equals(res.getFileExtension())) {				final ITigerstripeModelProject tsProject = getCorrespondingTSProject(res);				try {					if ((tsProject!=null) && (tsProject.getActiveFacet() != null)) {						final IFacetReference ref = tsProject.getActiveFacet();						if (res.getLocationURI().equals(ref.getURI())) {							// Reloading active facet							IRunnableWithProgress op = new IRunnableWithProgress() {								public void run(IProgressMonitor monitor) {									// compute the facet predicate while in the									// feedback thread									if (((FacetReference) ref)											.needsToBeEvaluated()) {										try {											tsProject.resetActiveFacet();											ref.computeFacetPredicate(monitor);											tsProject.setActiveFacet(ref,													monitor);										} catch (TigerstripeException e) {											BasePlugin.log(e);										}									}								}							};							IWorkbench wb = PlatformUI.getWorkbench();							IWorkbenchWindow win = wb									.getActiveWorkbenchWindow();							Shell shell = win != null ? win.getShell() : null;							try {								ProgressMonitorDialog dialog = new ProgressMonitorDialog(										shell);								dialog.run(true, false, op);								tsProject.setActiveFacet(ref,										new NullProgressMonitor());							} catch (InterruptedException ee) {								BasePlugin.log(ee);							} catch (InvocationTargetException ee) {								BasePlugin.log(ee);							} catch (TigerstripeException ee) {								BasePlugin.log(ee);							}						}					}				} catch (TigerstripeException e) {					BasePlugin.log(e);				}			}		}	}	private void checkRemovedFacet(Collection<IResource> removedResources) {		for (IResource res : removedResources) {			if (IContractSegment.FILE_EXTENSION.equals(res.getFileExtension())) {				ITigerstripeModelProject tsProject = getCorrespondingTSProject(res);				try {					if (tsProject != null && tsProject.getActiveFacet() != null) {						IFacetReference ref = tsProject.getActiveFacet();						if (res.getLocationURI().equals(ref.getURI())) {							tsProject.resetActiveFacet();						}					}				} catch (TigerstripeException e) {					BasePlugin.log(e);				}			}		}	}	protected ITigerstripeModelProject getCorrespondingTSProject(			IResource resource) {		IProject project = resource.getProject();		IAbstractTigerstripeProject aProject = (IAbstractTigerstripeProject) project				.getAdapter(IAbstractTigerstripeProject.class);		if (aProject instanceof ITigerstripeModelProject) {			ITigerstripeModelProject tsProject = (ITigerstripeModelProject) aProject;			return tsProject;		}		return null;	}	public void elementChanged(ElementChangedEvent event) {		MovedElement[] allChildren = getAllChildren(event);		for (MovedElement element : allChildren) {			if (element.fromElement instanceof IPackageFragment) {				IPackageFragment packFragment = (IPackageFragment) element.fromElement;				if (element.toElement == null)					removeFragmentContent(packFragment);				else {					// If we are handling package artifacts, rename the package					// artifact					IWorkbenchProfile profile = TigerstripeCore							.getWorkbenchProfileSession().getActiveProfile();					CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile							.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);					if (prop							.getDetailsForType(IPackageArtifact.class.getName())							.isEnabled()) {						renameArtifact((IPackageFragment) element.fromElement,								(IPackageFragment) element.toElement);					}					renameFragmentContent(packFragment,							(IPackageFragment) element.toElement);				}			} else if (element.fromElement instanceof ICompilationUnit) {				if (element.toElement != null) {					renameArtifact((ICompilationUnit) element.fromElement,							(ICompilationUnit) element.toElement);				}			}		}		Set<IProject> projectsToRefresh = new HashSet<IProject>();		for (MovedElement element : allChildren) {			try {				IJavaElement toElement = element.toElement;				if (toElement != null						&& toElement.getCorrespondingResource() != null) {					IProject proj = element.toElement							.getCorrespondingResource().getProject();					if (proj != null)						projectsToRefresh.add(proj);				}			} catch (JavaModelException e) {				BasePlugin.log(e);			}		}		refreshTSContent(projectsToRefresh);	}	private void refreshTSContent(Set<IProject> projects) {		for (IProject jProject : projects) {			IProject project = jProject.getProject();			IAbstractTigerstripeProject atsProject = (IAbstractTigerstripeProject) project					.getAdapter(IAbstractTigerstripeProject.class);			if (atsProject instanceof ITigerstripeModelProject) {				ITigerstripeModelProject tsProject = (ITigerstripeModelProject) atsProject;				try {					IArtifactManagerSession session = tsProject							.getArtifactManagerSession();					session.refresh(new NullProgressMonitor());				} catch (TigerstripeException e) {					BasePlugin.log(e);				}			}		}	}	private void renameArtifact(ICompilationUnit fromUnit,			ICompilationUnit toUnit) {		IJavaProject jProject = fromUnit.getJavaProject();		if (jProject != null) {			IProject project = jProject.getProject();			IAbstractTigerstripeProject atsProject = (IAbstractTigerstripeProject) project					.getAdapter(IAbstractTigerstripeProject.class);			if (atsProject instanceof ITigerstripeModelProject) {				ITigerstripeModelProject tsProject = (ITigerstripeModelProject) atsProject;				try {					IArtifactManagerSession session = tsProject							.getArtifactManagerSession();					String fqn = extractFQN(fromUnit);					IAbstractArtifact artifact = session							.getArtifactByFullyQualifiedName(fqn, false);					if (artifact != null) {						// System.out.println(System.currentTimeMillis()						// + " Detected rename: " + extractFQN(fromUnit)						// + " to " + extractFQN(toUnit));						TigerstripeRuntime.logInfoMessage("Detected rename: "								+ extractFQN(fromUnit) + " to "								+ extractFQN(toUnit));						session.renameArtifact(artifact, extractFQN(toUnit));					}				} catch (TigerstripeException e) {					BasePlugin.log(e);				}			}		}	}	private void renameArtifact(IPackageFragment fromPack,			IPackageFragment toPack) {		IJavaProject jProject = fromPack.getJavaProject();		if (jProject != null) {			IProject project = jProject.getProject();			IAbstractTigerstripeProject atsProject = (IAbstractTigerstripeProject) project					.getAdapter(IAbstractTigerstripeProject.class);			if (atsProject instanceof ITigerstripeModelProject) {				ITigerstripeModelProject tsProject = (ITigerstripeModelProject) atsProject;				try {					IArtifactManagerSession session = tsProject							.getArtifactManagerSession();					String fqn = fromPack.getElementName();					IAbstractArtifact artifact = session							.getArtifactByFullyQualifiedName(fqn, false);					if (artifact != null) {						TigerstripeRuntime.logInfoMessage("Detected rename: "								+ fromPack.getElementName() + " to "								+ toPack.getElementName());						session.renameArtifact(artifact, toPack								.getElementName());					}				} catch (TigerstripeException e) {					BasePlugin.log(e);				}			}		}	}	/**	 * Returns the FQN corresponding to the given unit to be used for the	 * Artifact Mgr	 * 	 * @param unit	 * @return	 */	private String extractFQN(ICompilationUnit unit) {		String result = null;		if (unit.getParent() instanceof IPackageFragment) {			IPackageFragment parentFragment = (IPackageFragment) unit					.getParent();			String packageName = parentFragment.getElementName();			String name = unit.getElementName();			if (name.indexOf(".") != -1) {				name = name.substring(0, name.indexOf("."));			}			if (packageName != null && packageName.length() != 0) {				name = packageName + "." + name;			}			result = name;		}		return result;	}	private void removeFragmentContent(IPackageFragment fragment) {		IJavaProject jProject = fragment.getJavaProject();		if (jProject != null) {			IProject project = jProject.getProject();			IAbstractTigerstripeProject atsProject = (IAbstractTigerstripeProject) project					.getAdapter(IAbstractTigerstripeProject.class);			if (atsProject instanceof ITigerstripeModelProject) {				ITigerstripeModelProject tsProject = (ITigerstripeModelProject) atsProject;				try {					IArtifactManagerSession session = tsProject							.getArtifactManagerSession();					Set<IRelationship> relationshipToCascadeDelete = session							.removePackageContent(fragment.getElementName());					if (true) {						final Set<IRelationship> fSet = relationshipToCascadeDelete;						final IModelUpdater updater = session								.getIModelUpdater();						Job cascadeDeleteJob = new Job(								"Cascade delete relationships") {							@Override							protected IStatus run(IProgressMonitor monitor) {								try {									handleRelationshipsToCascadeDelete(fSet,											updater, monitor);									return Status.OK_STATUS;								} catch (TigerstripeException e) {									Status st = new Status(IStatus.ERROR,											BasePlugin.PLUGIN_ID, 222,											"Error while cascade deleting relationships:"													+ e.getMessage(), e);									return st;								}							}						};						cascadeDeleteJob.schedule();					}				} catch (TigerstripeException e) {					BasePlugin.log(e);				}			}		}	}	/**	 * Handles the deletion of given relationship.	 * 	 * This method is called when either one of their ends has been removed from	 * the model.	 * 	 * Also, AssociationClasses are handled in here in a special way to ensure	 * recursive cases (i.e. relationships to AssociationClasses that should be	 * removed as a side effect of the association class being removed).	 * 	 * @param toDeletes	 * @param updater	 * @param monitor	 * @throws TigerstripeException	 */	public static void handleRelationshipsToCascadeDelete(			Set<IRelationship> toDeletes, IModelUpdater updater,			IProgressMonitor monitor) throws TigerstripeException {		// This may be turned off by the user through the General preference		// page		// if (!BasePlugin.getDefault().getPreferenceStore().getBoolean(		// GeneralPreferencePage.P_CASCADEDELETE_RELATIONSHIPS))		// return;		Set<IRelationship> additionalRelationships = new HashSet<IRelationship>();		computeAdditionalRelationships(toDeletes, additionalRelationships,				updater);		toDeletes.addAll(additionalRelationships);		IModelUpdater theUpdater = updater;		monitor.beginTask("Deleting", toDeletes.size());		for (IRelationship toDelete : toDeletes) {			monitor.subTask(((IAbstractArtifact) toDelete)					.getFullyQualifiedName());			// If no updater was provided, let's try and figure it out...			if (updater == null) {				theUpdater = null;				try {					IAbstractArtifact art = (AbstractArtifact) toDelete;					theUpdater = art.getUpdater();				} catch (TigerstripeException e) {					BasePlugin.log(e);				}			}			if (theUpdater != null) {				IArtifactDeleteRequest request = (IArtifactDeleteRequest) theUpdater						.getRequestFactory().makeRequest(								IArtifactDeleteRequest.class.getName());				request.setArtifactName(((IAbstractArtifact) toDelete)						.getName());				request.setArtifactPackage(((IAbstractArtifact) toDelete)						.getPackage());				theUpdater.handleChangeRequest(request);				IResource res = (IResource) ((IAbstractArtifact) toDelete)						.getAdapter(IResource.class);				if (res != null) {					try {						res.delete(true, new NullProgressMonitor());					} catch (CoreException e) {						BasePlugin.log(e);					}				}			}			monitor.worked(1);		}		monitor.done();	}	/**	 * When removing an association class, we may need to remove any	 * relationship to/from that association class.	 * 	 * This methods recursively looks for assocClasses being removed and builds	 * a list of additional relationships to remove	 * 	 * @param existing	 * @param additional	 */	private static void computeAdditionalRelationships(			Collection<IRelationship> existing, Set<IRelationship> additional,			IModelUpdater updater) {		Set<IRelationship> newlyFoundRelationships = new HashSet<IRelationship>();		boolean foundSome = false;		for (IRelationship rel : existing) {			IModelUpdater theUpdater = updater;			if (theUpdater == null) {				try {					theUpdater = ((IAbstractArtifact) rel).getUpdater();				} catch (TigerstripeException e) {					BasePlugin.log(e);				}			}			if (theUpdater != null) {				if (rel instanceof IAssociationClassArtifact) {					try {						IAssociationClassArtifact assocClass = (IAssociationClassArtifact) rel;						Collection<IRelationship> orig = theUpdater								.getSession().getOriginatingRelationshipForFQN(										assocClass.getFullyQualifiedName(),										false);						if (orig.size() != 0) {							foundSome = true;							additional.addAll(orig);							newlyFoundRelationships.addAll(orig);						}						Collection<IRelationship> term = theUpdater								.getSession().getTerminatingRelationshipForFQN(										assocClass.getFullyQualifiedName(),										false);						if (term.size() != 0) {							foundSome = true;							additional.addAll(term);							newlyFoundRelationships.addAll(term);						}					} catch (TigerstripeException e) {						BasePlugin.log(e);					}				}			}		}		if (foundSome) {			computeAdditionalRelationships(newlyFoundRelationships, additional,					updater);		}	}	private void renameFragmentContent(IPackageFragment fromFragment,			IPackageFragment toFragment) {		IJavaProject jProject = fromFragment.getJavaProject();		if (jProject != null) {			IProject project = jProject.getProject();			IAbstractTigerstripeProject atsProject = (IAbstractTigerstripeProject) project					.getAdapter(IAbstractTigerstripeProject.class);			if (atsProject instanceof ITigerstripeModelProject) {				ITigerstripeModelProject tsProject = (ITigerstripeModelProject) atsProject;				try {					IArtifactManagerSession session = tsProject							.getArtifactManagerSession();					session.renamePackageContent(fromFragment.getElementName(),							toFragment.getElementName());				} catch (TigerstripeException e) {					BasePlugin.log(e);				}			}		}	}	private MovedElement[] getAllChildren(ElementChangedEvent event) {		List<MovedElement> result = getAllChildren(event.getDelta());		return result.toArray(new MovedElement[result.size()]);	}	private List<MovedElement> getAllChildren(IJavaElementDelta delta) {		ArrayList<MovedElement> result = new ArrayList<MovedElement>();		if (delta.getRemovedChildren().length != 0) {			for (IJavaElementDelta remDelta : delta.getRemovedChildren()) {				MovedElement elm = new MovedElement();				elm.fromElement = remDelta.getElement();				elm.toElement = remDelta.getMovedToElement();				result.add(elm);			}			return result;		}		if (delta.getAffectedChildren().length == 0)			return result;		else {			for (IJavaElementDelta aDelta : delta.getAffectedChildren()) {				result.addAll(getAllChildren(aDelta));			}			return result;		}	}	public ProjectInfo getProjectDetails(IProject project) {		return referencesListener.getProjectDetails(project);	}}