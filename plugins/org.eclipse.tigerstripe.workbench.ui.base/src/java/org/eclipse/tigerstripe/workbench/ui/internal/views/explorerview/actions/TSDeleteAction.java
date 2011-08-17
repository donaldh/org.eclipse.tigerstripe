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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.internal.ui.refactoring.reorg.DeleteAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceListener;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.action.LogicalNodeDeleteAction;
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
                        IAbstractTigerstripeProject aProject = (IAbstractTigerstripeProject) project
                                .getAdapter(IAbstractTigerstripeProject.class);
                        if (aProject instanceof ITigerstripeModelProject) {
                            ITigerstripeModelProject tsProject = (ITigerstripeModelProject) aProject;
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
                } else if (obj instanceof IProject) {
                    IProject proj = (IProject) obj;
                    iResourceSelected = true;
                    if (!proj.isOpen()) {
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
            Set<IResource> packageResources = new HashSet<IResource>();
            if (selection != null) {
                for (Iterator<?> iter = selection.iterator(); iter.hasNext();) {
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
                    } else if (obj instanceof IPackageFragment) {
                        // We are deleting a package so might need to do the
                        // same for
                        // package artifacts
                        IPackageFragment pack = (IPackageFragment) obj;
                        if (pack != null) {
                            IAbstractArtifact artifact = TSExplorerUtils
                                    .getArtifactFor(pack);
                            if (artifact != null) {
                                ElementArtifactPair pair = new ElementArtifactPair();
                                pair.element = pack;
                                pair.artifact = artifact;
                                selectedResources.add(pair);
                            }
                        }
                        addParentPackgeResources(pack, packageResources);

                    }
                }
            }

            List<Object> toDelete = new ArrayList<Object>(
                    asList(selection.toArray()));
            toDelete.addAll(packageResources);
            super.run(new StructuredSelection(toDelete));

            // Now check what was actually deleted and notify the artifact
            // manager
            Set<IRelationship> relationshipsToCascadeDelete = new HashSet<IRelationship>();
            for (ElementArtifactPair pair : selectedResources) {
                if (!pair.element.exists()) {
					IAbstractArtifactInternal art = (IAbstractArtifactInternal) pair.artifact;

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
                //Bug 320140 - placing artifacts in a map instead of a list so if they come up again
                //we will use the already modified artifact in the map and modify it further
                //otherwise only the last change will take affect
                Map<String, IAbstractArtifact> changedArtifacts = new HashMap<String, IAbstractArtifact>();
                for (Iterator iter = selection.iterator(); iter.hasNext();) {
                    Object obj = iter.next();
                    if (obj instanceof IField) {
                        IField field = (IField) obj;
                        try {
                            IAbstractArtifact art;
                            String FQN = field.getContainingArtifact()
                                    .getFullyQualifiedName();
                            //Bug 320140 - here is where we check if we have modified this artifact before
                            if (changedArtifacts.containsKey(FQN)) {
                                art = changedArtifacts.get(FQN);

                            } else {
                                art = field.getContainingArtifact()
                                        .makeWorkingCopy(null);
                            }
                            for (IField realField : art.getFields()) {
                                if (realField.getLabelString().equals(
                                        field.getLabelString()))
                                    field = realField;
                            }

                            art.removeFields(Collections.singleton(field));

                            changedArtifacts.put(FQN, art);
                        } catch (TigerstripeException e) {
                            EclipsePlugin.log(e);
                        }
                    } else if (obj instanceof IMethod) {
                        IMethod method = (IMethod) obj;
                        try {
                            IAbstractArtifact art;
                            String FQN = method.getContainingArtifact()
                                    .getFullyQualifiedName();
                            if (changedArtifacts.containsKey(FQN)) {
                                art = changedArtifacts.get(FQN);

                            } else {
                                art = method.getContainingArtifact()
                                        .makeWorkingCopy(null);
                            }

                            art.removeMethods(Collections.singleton(method));

                            changedArtifacts.put(FQN, art);
                        } catch (TigerstripeException e) {
                            EclipsePlugin.log(e);
                        }
                    } else if (obj instanceof ILiteral) {
                        ILiteral literal = (ILiteral) obj;
                        try {
                            IAbstractArtifact art;
                            String FQN = literal.getContainingArtifact()
                                    .getFullyQualifiedName();
                            if (changedArtifacts.containsKey(FQN)) {
                                art = changedArtifacts.get(FQN);

                            } else {
                                art = literal.getContainingArtifact()
                                        .makeWorkingCopy(null);
                            }

                            for (ILiteral realLiteral : art.getLiterals()) {
                                if (realLiteral.getLabelString().equals(
                                        literal.getLabelString()))
                                    literal = realLiteral;
                            }
                            art.removeLiterals(Collections.singleton(literal));

                            changedArtifacts.put(FQN, art);
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
                for (IAbstractArtifact art : changedArtifacts.values()) {
					ArtifactManager mgr = ((IAbstractArtifactInternal) art)
                            .getArtifactManager();
                    try {
                        art.doSave(null);
                        // this will replace the existing definition (not add
                        // another one!!)
                        mgr.addArtifact(art, new NullProgressMonitor());

                        // for the explorer to be refreshed, the corresponding
                        // resource needs to be refreshed
                        IResource res = (IResource) art
                                .getAdapter(IResource.class);
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

    private void addParentPackgeResources(IPackageFragment root,
			Set<IResource> packageResources) {
		IAbstractArtifact rootArtifact = TSExplorerUtils.getArtifactFor(root);
		if (rootArtifact instanceof IPackageArtifact
				&& isOrphaned((IPackageArtifact) rootArtifact)) {
			IModelComponent current = rootArtifact
					.getContainingModelComponent();
			while (current instanceof IPackageArtifact
					&& current.getContainedModelComponents().size() == 1) {
				IResource resource = (IResource) current
						.getAdapter(IResource.class);
				if (resource != null) {
					packageResources.add(resource);
				}
				current = current.getContainingModelComponent();
			}
		}
	}

	private boolean isOrphaned(IPackageArtifact artifact) {
		Collection<IModelComponent> subComponents = artifact
				.getContainedModelComponents();
		for (IModelComponent component : subComponents) {
			if (component instanceof IPackageArtifact) {
				return false;
			}
		}
		return true;
	}

    private void handleRelationshipToCascadeDelete(Set<IRelationship> toDeletes) {
        try {
            WorkspaceListener.handleRelationshipsToCascadeDelete(toDeletes,
                    null, new NullProgressMonitor());
        } catch (TigerstripeException e) {
            EclipsePlugin.log(e);
        }
    }
}
