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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.packageview.SelectionTransferDropAdapter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.action.LogicalNodeMoveAction;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;

public class LogicalExplorerNodeTransferDropAdapter extends
		SelectionTransferDropAdapter {

	private List selectedElements;

	private static final long DROP_TIME_DIFF_TRESHOLD = 150;

	public LogicalExplorerNodeTransferDropAdapter(StructuredViewer viewer) {
		super(viewer);
	}

	private boolean tooFast(DropTargetEvent event) {
		return Math.abs(LocalSelectionTransfer.getInstance()
				.getSelectionSetTime()
				- (event.time & 0xFFFFFFFFL)) < DROP_TIME_DIFF_TRESHOLD;
	}

	@Override
	public boolean isEnabled(DropTargetEvent event) {
		initializeSelection();
		AbstractLogicalExplorerNode[] nodes = getNodes();
		return nodes.length != 0;
	}

	@Override
	public void drop(Object target, DropTargetEvent event) {
		try {
			switch (event.detail) {
			case DND.DROP_MOVE:
				handleDropMove(target, event);
				break;
			case DND.DROP_COPY:
				handleDropCopy(target, event);
				break;
			}
		} catch (InvocationTargetException e) {
			EclipsePlugin.log(e);
		} catch (InterruptedException e) {
			// ok
		} finally {
			// The drag source listener must not perform any operation
			// since this drop adapter did the remove of the source even
			// if we moved something.
			event.detail = DND.DROP_NONE;
		}
	}

	private void handleDropCopy(final Object target, DropTargetEvent event)
			throws InvocationTargetException, InterruptedException {
		// IJavaElement[] javaElements= ReorgUtils.getJavaElements(fElements);
		// IResource[] resources= ReorgUtils.getResources(fElements);
		// ReorgCopyStarter starter= null;
		// if (target instanceof IResource)
		// starter= ReorgCopyStarter.create(javaElements, resources,
		// (IResource)target);
		// else if (target instanceof IJavaElement)
		// starter= ReorgCopyStarter.create(javaElements, resources,
		// (IJavaElement)target);
		// if (starter != null)
		// starter.run(getShell());
	}

	private AbstractLogicalExplorerNode[] getNodes() {
		List<AbstractLogicalExplorerNode> result = new ArrayList<AbstractLogicalExplorerNode>();
		for (Object obj : selectedElements) {
			if (obj instanceof AbstractLogicalExplorerNode) {
				result.add((AbstractLogicalExplorerNode) obj);
			}
		}
		return result.toArray(new AbstractLogicalExplorerNode[result.size()]);
	}

	private void handleDropMove(final Object target, DropTargetEvent event)
			throws InvocationTargetException, InterruptedException {
		AbstractLogicalExplorerNode[] nodes = getNodes();
		LogicalNodeMoveAction action = new LogicalNodeMoveAction("move",
				getShell());
		if (target instanceof IContainer)
			action.runBatch(nodes, (IContainer) target);
		else if (target instanceof IAdaptable) {
			IResource res = (IResource) ((IAdaptable) target)
					.getAdapter(IResource.class);
			if (res instanceof IContainer) {
				action.runBatch(nodes, (IContainer) res);
			}
		}
	}

	@Override
	public void validateDrop(Object target, DropTargetEvent event, int operation) {
		event.detail = DND.DROP_NONE;

		if (tooFast(event))
			return;

		initializeSelection();

		// Can only DnD logical nodes together with other logical nodes at this
		// point.
		if (getNodes().length != selectedElements.size())
			return;
		switch (operation) {
		case DND.DROP_DEFAULT:
			event.detail = handleValidateDefault(target, event);
			break;
		case DND.DROP_COPY:
			event.detail = handleValidateCopy(target, event);
			break;
		case DND.DROP_MOVE:
			event.detail = handleValidateMove(target, event);
			break;
		}
	}

	private int handleValidateDefault(Object target, DropTargetEvent event) {
		if (target == null)
			return DND.DROP_NONE;

		if ((event.operations & DND.DROP_MOVE) != 0)
			return handleValidateMove(target, event);
		if ((event.operations & DND.DROP_COPY) != 0)
			return handleValidateCopy(target, event);
		return DND.DROP_NONE;
	}

	private int handleValidateCopy(Object target, DropTargetEvent event) {
		if (target == null)
			return DND.DROP_NONE;

		if ((target instanceof IContainer || target instanceof IJavaProject
				|| target instanceof IPackageFragment || target instanceof IPackageFragmentRoot)
				&& validateProjectReference(target, event))
			return DND.DROP_MOVE;
		else
			return DND.DROP_NONE;
	}

	/**
	 * Validates a drop by checking that the node is dragged into the same
	 * project or a child project
	 * 
	 * @param target
	 * @param event
	 * @return
	 */
	private boolean validateProjectReference(Object target,
			DropTargetEvent event) {
		initializeSelection();
		AbstractLogicalExplorerNode[] nodes = getNodes();
		if (target instanceof IResource || target instanceof IJavaElement) {
			try {
				IResource res = null;
				if (target instanceof IResource)
					res = (IResource) target;
				else if (target instanceof IJavaElement)
					res = ((IJavaElement) target).getCorrespondingResource();
				IProject targetProject = res.getProject();
				IResource keyRes = nodes[0].getKeyResource();
				if (keyRes != null) {
					IProject keyProject = keyRes.getProject();

					// Same project ?
					if (targetProject.equals(keyProject))
						return true;

					IProject[] refProjects = keyProject
							.getReferencingProjects();
					for (IProject proj : refProjects) {
						if (targetProject.equals(proj))
							return true;
					}
					return false;
				}
			} catch (JavaModelException e) {
				EclipsePlugin.log(e);
				return false;
			}
		}
		return true;
	}

	private int handleValidateMove(Object target, DropTargetEvent event) {
		if (target == null)
			return DND.DROP_NONE;

		if ((target instanceof IContainer || target instanceof IJavaProject
				|| target instanceof IPackageFragment || target instanceof IPackageFragmentRoot)
				&& validateProjectReference(target, event))
			return DND.DROP_MOVE;
		else
			return DND.DROP_NONE;
	}

	@Override
	protected void initializeSelection() {
		ISelection s = LocalSelectionTransfer.getInstance().getSelection();
		if (!(s instanceof IStructuredSelection))
			return;
		selectedElements = ((IStructuredSelection) s).toList();
	}

	private Shell getShell() {
		return getViewer().getControl().getShell();
	}

}
