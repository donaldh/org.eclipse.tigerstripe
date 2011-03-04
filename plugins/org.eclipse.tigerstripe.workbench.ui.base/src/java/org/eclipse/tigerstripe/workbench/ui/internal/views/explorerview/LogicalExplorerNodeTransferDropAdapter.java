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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.action.LogicalNodeCopyAction;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.action.LogicalNodeMoveAction;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;

public class LogicalExplorerNodeTransferDropAdapter extends ViewerDropAdapter
		implements TransferDropTargetListener {

	private List<?> selectedElements;
	private int internalCurrentOperation; // needed to duplicate as no setter in

	// parent class

	public LogicalExplorerNodeTransferDropAdapter(StructuredViewer viewer) {
		super(viewer);
	}

	/**
	 * @see ViewerDropAdapter#performDrop
	 */
	public boolean performDrop(Object data) {
		// should never be called, since we override the drop() method.
		return false;
	}

	@Override
	public void drop(DropTargetEvent event) {
		try {
			switch (internalCurrentOperation) {
			case DND.DROP_MOVE:
				handleDropMove(getCurrentTarget(), event);
				break;
			case DND.DROP_COPY:
				handleDropCopy(getCurrentTarget(), event);
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
		AbstractLogicalExplorerNode[] nodes = getNodes();
		LogicalNodeCopyAction action = new LogicalNodeCopyAction("copy",
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

	public Transfer getTransfer() {
		return LocalSelectionTransfer.getInstance();
	}

	public boolean isEnabled(DropTargetEvent event) {
		initializeSelection();
		AbstractLogicalExplorerNode[] nodes = getNodes();
		return nodes.length != 0;
	}

	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		internalCurrentOperation = determineOperation(target, operation,
				transferType, DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY);
		return internalCurrentOperation != DND.DROP_NONE;
	}

	/**
	 * {@inheritDoc}
	 */
	protected int determineOperation(Object target, int operation,
			TransferData transferType, int operations) {
		int result = internalDetermineOperation(target, operation, operations);

		if (result == DND.DROP_NONE) {
			setSelectionFeedbackEnabled(false);
		} else {
			setSelectionFeedbackEnabled(true);
		}

		return result;
	}

	private int internalDetermineOperation(Object target, int operation,
			int operations) {

		initializeSelection();

		if (target == null)
			return DND.DROP_NONE;

		switch (operation) {
		case DND.DROP_DEFAULT:
		case DND.DROP_NONE:
			return handleValidateDefault(target, operations);
		case DND.DROP_COPY:
			return handleValidateCopy(target);
		case DND.DROP_MOVE:
			return handleValidateMove(target);
		}

		return DND.DROP_NONE;
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

	// public void validateDrop(Object target, DropTargetEvent event, int
	// operation) {
	// event.detail = DND.DROP_NONE;
	//
	// initializeSelection();
	//
	// // Can only DnD logical nodes together with other logical nodes at this
	// // point.
	// if (getNodes().length != selectedElements.size())
	// return;
	// switch (operation) {
	// case DND.DROP_DEFAULT:
	// event.detail = handleValidateDefault(target, event.operations);
	// break;
	// case DND.DROP_COPY:
	// event.detail = handleValidateCopy(target);
	// break;
	// case DND.DROP_MOVE:
	// event.detail = handleValidateMove(target);
	// break;
	// }
	// }
	//
	private int handleValidateDefault(Object target, int operations) {
		if (target == null)
			return DND.DROP_NONE;

		if ((operations & DND.DROP_MOVE) != 0)
			return handleValidateMove(target);
		if ((operations & DND.DROP_COPY) != 0)
			return handleValidateCopy(target);
		return DND.DROP_NONE;
	}

	private int handleValidateCopy(Object target) {
		if (target == null)
			return DND.DROP_NONE;

		if ((target instanceof IContainer || target instanceof IProject
				|| target instanceof IPackageFragment || target instanceof IPackageFragmentRoot)
				&& validateProjectReference(target))
			return DND.DROP_COPY;
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
	private boolean validateProjectReference(Object target) {
		initializeSelection();
		AbstractLogicalExplorerNode[] nodes = getNodes();
		if (nodes.length == 0) {
			return false;
		}
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

	private int handleValidateMove(Object target) {
		if (target == null)
			return DND.DROP_NONE;

		if ((target instanceof IContainer || target instanceof IProject
				|| target instanceof IPackageFragment || target instanceof IPackageFragmentRoot)
				&& validateProjectReference(target))
			return DND.DROP_MOVE;
		else
			return DND.DROP_NONE;
	}

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
