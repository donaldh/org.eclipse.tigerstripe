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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.refactoring.reorg.DeleteAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILiteral;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.builder.WorkspaceListener;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.action.LogicalNodeDeleteAction;
import org.eclipse.ui.IWorkbenchSite;

public class TSDeleteAction extends DeleteAction {

	private boolean iResourceSelected = false;

	private boolean logicalNodeSelected = false;

	private boolean iAbstractArtifactComponentSelected = false;

	public TSDeleteAction(IWorkbenchSite site) {
		super(site);
	}

	/**
	 * We can't handle deletes that have mixed content (IResource) and
	 * (IAbstractArtifact components). We forbid deletes on tigerstripe.xml or
	 * ts-plugin.xml
	 * 
	 */
	@Override
	public void selectionChanged(IStructuredSelection selection) {
		super.selectionChanged(selection);

		iResourceSelected = false;
		iAbstractArtifactComponentSelected = false;
		logicalNodeSelected = false;

		if (selection != null) {
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof AbstractLogicalExplorerNode) {
					logicalNodeSelected = true;
				} else if (obj instanceof IResource) {
					IResource resource = (IResource) obj;
					iResourceSelected = true;
					if (ITigerstripeConstants.PROJECT_DESCRIPTOR
							.equals(resource.getName())
							|| "ts-plugin.xml".equals(resource.getName())) {
						setEnabled(false);
						return;
					} else if (IContractSegment.FILE_EXTENSION.equals(resource
							.getFileExtension())) {
						// Can't delete active facet
						IProject project = resource.getProject();
						IAbstractTigerstripeProject aProject = EclipsePlugin
								.getITigerstripeProjectFor(project);
						if (aProject instanceof ITigerstripeProject) {
							ITigerstripeProject tsProject = (ITigerstripeProject) aProject;
							try {
								if (tsProject.getActiveFacet() != null) {
									IFacetReference ref = tsProject
											.getActiveFacet();
									if (resource.getLocationURI().equals(
											ref.getURI())) {
										setEnabled(false);
										return;
									}
								}
							} catch (TigerstripeException e) {
								EclipsePlugin.log(e);
							}
						}
					}
				} else if (obj instanceof IJavaProject) {
					IJavaProject jProj = (IJavaProject) obj;
					iResourceSelected = true;
					if (!jProj.getProject().isOpen()) {
						setEnabled(false);
						return;
					}
				} else if (obj instanceof IField || obj instanceof IMethod
						|| obj instanceof ILiteral) {
					iAbstractArtifactComponentSelected = true;
				} else {
					iResourceSelected = true;
				}
			}

			if (iAbstractArtifactComponentSelected
					&& (iResourceSelected || logicalNodeSelected)) {
				setEnabled(false);
			} else if (iResourceSelected && logicalNodeSelected) {
				setEnabled(false);
			} else if (iAbstractArtifactComponentSelected) {
				setEnabled(true);
			} else if (logicalNodeSelected) {
				setEnabled(true);
			}

		}
	}

	private class ElementArtifactPair {
		IJavaElement element;

		IAbstractArtifact artifact;

		ITigerstripeProject tsProject;
	}

	@Override
	public void run(IStructuredSelection selection) {
		if (logicalNodeSelected) {

			String msg = "Do you really want to delete ";

			if (selection.size() == 1) {
				msg += "this element?";
			} else {
				msg += "these " + selection.size() + " elements?";
			}
			if (MessageDialog.openQuestion(getShell(), "Delete...", msg)) {
				LogicalNodeDeleteAction action = new LogicalNodeDeleteAction(
						getShell());
				action.selectionChanged(selection);
				action.run();
			}
		} else if (iResourceSelected) {
			// If we're about to delete an artifact in a project, we need to
			// notify
			// the corresponding
			// artifact manager
			List<ElementArtifactPair> selectedResources = new ArrayList<ElementArtifactPair>();
			if (selection != null) {
				for (Iterator iter = selection.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					if (obj instanceof ICompilationUnit) {
						ICompilationUnit jElem = (ICompilationUnit) obj;
						if (jElem != null) {
							IAbstractArtifact artifact = TSExplorerUtils
									.getArtifactFor(jElem);
							if (artifact != null) {
								ElementArtifactPair pair = new ElementArtifactPair();
								pair.element = jElem;
								pair.artifact = artifact;
								selectedResources.add(pair);
							}
						}
					}
				}
			}

			super.run(selection);

			// Now check what was actually deleted and notify the artifact
			// manager
			Set<IRelationship> relationshipsToCascadeDelete = new HashSet<IRelationship>();
			for (ElementArtifactPair pair : selectedResources) {
				if (!pair.element.exists()) {
					AbstractArtifact art = (AbstractArtifact) pair.artifact;

					try {
						relationshipsToCascadeDelete.addAll(art
								.getArtifactManager()
								.getOriginatingRelationshipForFQN(
										art.getFullyQualifiedName(), false));
						relationshipsToCascadeDelete.addAll(art
								.getArtifactManager()
								.getTerminatingRelationshipForFQN(
										art.getFullyQualifiedName(), false));
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
					art.getArtifactManager().notifyArtifactDeleted(
							pair.artifact);
				}
			}

			if (true) {
				handleRelationshipToCascadeDelete(relationshipsToCascadeDelete);
			}
		} else if (iAbstractArtifactComponentSelected) {

			String msg = "Do you really want to delete ";

			if (selection.size() == 1) {
				msg += "this element?";
			} else {
				msg += "these " + selection.size() + " elements?";
			}
			if (MessageDialog.openQuestion(getShell(), "Delete...", msg)) {
				List<IAbstractArtifact> changedArtifacts = new ArrayList<IAbstractArtifact>();
				for (Iterator iter = selection.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					if (obj instanceof IField) {
						IField field = (IField) obj;
						IAbstractArtifact art = (IAbstractArtifact) field
								.getContainingArtifact();
						art.removeFields(Collections.singleton(field));
						try {
							art
									.doSilentSave(new TigerstripeNullProgressMonitor());
							changedArtifacts.add(art);
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					} else if (obj instanceof IMethod) {
						IMethod method = (IMethod) obj;
						IAbstractArtifact art = (IAbstractArtifact) method
								.getContainingArtifact();
						art.removeMethods(Collections.singleton(method));
						try {
							art
									.doSilentSave(new TigerstripeNullProgressMonitor());
							changedArtifacts.add(art);
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					} else if (obj instanceof ILiteral) {
						ILiteral literal = (ILiteral) obj;
						IAbstractArtifact art = (IAbstractArtifact) literal
								.getContainingArtifact();
						art.removeLiterals(Collections.singleton(literal));
						try {
							art
									.doSilentSave(new TigerstripeNullProgressMonitor());
							changedArtifacts.add(art);
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					}
				}

				// Notify of changes on all artifacts
				// We use doSilentSave() above so we need to notify the parent
				// artifact mgr
				// that the save happened. By doing it in 2 steps like this we
				// only get 1 notification
				// per artifact as opposed to one notification per changed
				// component of each artifact.
				for (IAbstractArtifact art : changedArtifacts) {
					ArtifactManager mgr = ((AbstractArtifact) art)
							.getArtifactManager();

					try {
						mgr.addArtifact(art,
								new TigerstripeNullProgressMonitor()); // this
						// will
						// replace
						// the
						// existing definition (not add
						// another one!!)

						// Since we did a silentSave we need to manually notify
						// of the saves
						// now
						mgr.notifyArtifactSaved(art,
								new TigerstripeNullProgressMonitor());

						// for the explorer to be refreshed, the corresponding
						// resource needs to be refreshed
						IResource res = TSExplorerUtils
								.getIResourceForArtifact(art);
						res.refreshLocal(IResource.DEPTH_ONE,
								new NullProgressMonitor());
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					} catch (CoreException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
	}

	private void handleRelationshipToCascadeDelete(Set<IRelationship> toDeletes) {
		try {
			WorkspaceListener.handleRelationshipsToCascadeDelete(toDeletes,
					null, new NullProgressMonitor());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		// for (IRelationship toDelete : toDeletes) {
		// try {
		// AbstractArtifact art = (AbstractArtifact) toDelete;
		// ArtifactManager mgr = art.getArtifactManager();
		// IextAbstractTigerstripeProject aProject = API
		// .getDefaultProjectSession().makeTigerstripeProject(
		// mgr.getTSProject().getBaseDir().toURI());
		// if (aProject instanceof ITigerstripeProject) {
		// IArtifactManagerSession session = ((ITigerstripeProject) aProject)
		// .getArtifactManagerSession();
		// IModelUpdater updater = session.getIModelUpdater();
		//
		// IArtifactDeleteRequest request = (IArtifactDeleteRequest) updater
		// .getRequestFactory().makeRequest(
		// IArtifactDeleteRequest.class.getName());
		// ((IArtifactDeleteRequest) request)
		// .setArtifactName(((IAbstractArtifact) toDelete)
		// .getName());
		// ((IArtifactDeleteRequest) request)
		// .setArtifactPackage(((IAbstractArtifact) toDelete)
		// .getPackage());
		// updater.handleChangeRequest(request);
		//
		// IResource res = TSExplorerUtils
		// .getIResourceForArtifact(((IAbstractArtifact) toDelete));
		//
		// if (res != null) {
		// try {
		// res.delete(true, new NullProgressMonitor());
		// } catch (CoreException e) {
		// EclipsePlugin.log(e);
		// }
		// }
		//
		// }
		// } catch (TigerstripeException e) {
		// EclipsePlugin.log(e);
		// }
		// }
	}
}
