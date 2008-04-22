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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.packageview.SelectionTransferDropAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.TigerstripePluginConstants;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;

public class ArtifactComponentTransferDropAdapter extends
		SelectionTransferDropAdapter {

	private List selectedElements;

	private static final long DROP_TIME_DIFF_TRESHOLD = 150;

	public ArtifactComponentTransferDropAdapter(StructuredViewer viewer) {
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
		IModelComponent[] nodes = getIModelComponents();
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
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				IModelComponent[] components = getIModelComponents();
				IAbstractArtifact targetArtifact = TSExplorerUtils
						.getArtifactFor(target);

				for (IModelComponent component : components) {
					if (component instanceof IField) {
						IField field = (IField) component;
						IField clonedField = field.clone();
						targetArtifact.addField(field.clone());
					} else if (component instanceof IMethod) {
						IMethod method = (IMethod) component;
						IAbstractArtifact srcArtifact = (IAbstractArtifact) method
								.getContainingArtifact();
						targetArtifact.addMethod(method.clone());
					} else if (component instanceof ILiteral) {
						ILiteral lit = (ILiteral) component;
						IAbstractArtifact srcArtifact = (IAbstractArtifact) lit
								.getContainingArtifact();
						targetArtifact.addLiteral(lit.clone());
					}
				}

				try {
					targetArtifact.doSave(new NullProgressMonitor());
					IResource res = TSExplorerUtils
							.getIResourceForArtifact(targetArtifact);
					if (res != null) {
						try {
							res.refreshLocal(IResource.DEPTH_ZERO,
									new NullProgressMonitor());
						} catch (CoreException e) {
							throw new TigerstripeException(e.getMessage(), e);
						}
					}
				} catch (TigerstripeException e) {
					Status error = new Status(IStatus.ERROR,
							TigerstripePluginConstants.PLUGIN_ID, 222,
							"Exception during model component move: "
									+ e.getMessage(), e);
					MessageDialog
							.openError(getShell(), "Move Error",
									"An error occured during move operation.\nPlease check log for more details");
					EclipsePlugin.log(error);
				}
			}
		});
	}

	private IModelComponent[] getIModelComponents() {
		List<IModelComponent> result = new ArrayList<IModelComponent>();
		for (Object obj : selectedElements) {
			if (obj instanceof IField || obj instanceof IMethod
					|| obj instanceof ILiteral) {
				result.add((IModelComponent) obj);
			}
		}
		return result.toArray(new IModelComponent[result.size()]);
	}

	private void handleDropMove(final Object target, DropTargetEvent event)
			throws InvocationTargetException, InterruptedException {

		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				IModelComponent[] components = getIModelComponents();
				IAbstractArtifact targetArtifact = TSExplorerUtils
						.getArtifactFor(target);

				Set<IAbstractArtifact> srcArtifacts = new HashSet<IAbstractArtifact>();

				for (IModelComponent component : components) {
					if (component instanceof IField) {
						IField field = (IField) component;
						IAbstractArtifact srcArtifact = (IAbstractArtifact) field
								.getContainingArtifact();
						srcArtifact.removeFields(Collections.singleton(field));
						targetArtifact.addField(field);
						srcArtifacts.add(srcArtifact);
					} else if (component instanceof IMethod) {
						IMethod method = (IMethod) component;
						IAbstractArtifact srcArtifact = (IAbstractArtifact) method
								.getContainingArtifact();
						srcArtifact
								.removeMethods(Collections.singleton(method));
						targetArtifact.addMethod(method);
						srcArtifacts.add(srcArtifact);
					} else if (component instanceof ILiteral) {
						ILiteral lit = (ILiteral) component;
						IAbstractArtifact srcArtifact = (IAbstractArtifact) lit
								.getContainingArtifact();
						srcArtifact.removeLiterals(Collections.singleton(lit));
						targetArtifact.addLiteral(lit);
						srcArtifacts.add(srcArtifact);
					}
				}

				try {
					targetArtifact.doSave(new NullProgressMonitor());
					IResource res = TSExplorerUtils
							.getIResourceForArtifact(targetArtifact);
					if (res != null) {
						try {
							res.refreshLocal(IResource.DEPTH_ZERO,
									new NullProgressMonitor());
						} catch (CoreException e) {
							throw new TigerstripeException(e.getMessage(), e);
						}
					}
					for (IAbstractArtifact art : srcArtifacts) {
						art.doSave(new NullProgressMonitor());
						res = TSExplorerUtils.getIResourceForArtifact(art);
						if (res != null) {
							try {
								res.refreshLocal(IResource.DEPTH_ZERO,
										new NullProgressMonitor());
							} catch (CoreException e) {
								throw new TigerstripeException(e.getMessage(),
										e);
							}
						}
					}
				} catch (TigerstripeException e) {
					Status error = new Status(IStatus.ERROR,
							TigerstripePluginConstants.PLUGIN_ID, 222,
							"Exception during model component move: "
									+ e.getMessage(), e);
					MessageDialog
							.openError(getShell(), "Move Error",
									"An error occured during move operation.\nPlease check log for more details");
					EclipsePlugin.log(error);
				}
			}
		});
	}

	@Override
	public void validateDrop(Object target, DropTargetEvent event, int operation) {
		event.detail = DND.DROP_NONE;

		if (tooFast(event))
			return;

		initializeSelection();

		// Can only DnD logical nodes together with other logical nodes at this
		// point.
		if (getIModelComponents().length != selectedElements.size())
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

		if (target instanceof ICompilationUnit && validateNoDuplicate(target)
				&& validateCompatibleTarget(target)
				&& validateSameProject(target))
			return DND.DROP_COPY;
		else
			return DND.DROP_NONE;
	}

	private int handleValidateMove(Object target, DropTargetEvent event) {
		if (target == null)
			return DND.DROP_NONE;

		if (target instanceof ICompilationUnit && validateNoDuplicate(target)
				&& validateCompatibleTarget(target)
				&& validateSameProject(target))
			return DND.DROP_MOVE;
		else
			return DND.DROP_NONE;
	}

	/**
	 * Validates that the target is in the same project as the source.
	 * 
	 * NOTE: this is a limitation that is necessary to avoid problem with cloned
	 * elements that wouldn't belong to the right Artifact Mgr.
	 * 
	 * @param target
	 * @return
	 */
	private boolean validateSameProject(Object target) {
		if (target instanceof ICompilationUnit) {
			ICompilationUnit unit = (ICompilationUnit) target;
			try {
				IProject p = unit.getCorrespondingResource().getProject();
				IModelComponent[] components = getIModelComponents();
				for (IModelComponent component : components) {
					IResource cRes = null;
					if (component instanceof IField) {
						IField field = (IField) component;
						cRes = TSExplorerUtils
								.getIResourceForArtifact((IAbstractArtifact) field
										.getContainingArtifact());
					} else if (component instanceof IMethod) {
						IMethod method = (IMethod) component;
						cRes = TSExplorerUtils
								.getIResourceForArtifact((IAbstractArtifact) method
										.getContainingArtifact());
					} else if (component instanceof ILiteral) {
						ILiteral literal = (ILiteral) component;
						cRes = TSExplorerUtils
								.getIResourceForArtifact((IAbstractArtifact) literal
										.getContainingArtifact());
					}

					if (cRes == null)
						return false;
					else {
						if (!cRes.getProject().equals(p))
							return false;
					}
				}
			} catch (JavaModelException e) {
				EclipsePlugin.log(e);
				return false;
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
				return false;
			}
		}

		return true;
	}

	/**
	 * Validate that the target doesn't contain IModelComponents that have the
	 * same name as those being dropped.
	 * 
	 * @param target
	 * @return
	 */
	private boolean validateNoDuplicate(Object target) {
		IAbstractArtifact artifact = TSExplorerUtils.getArtifactFor(target);
		IModelComponent[] components = getIModelComponents();
		for (IModelComponent component : components) {
			if (component instanceof IField) {
				String name = component.getName();
				for (IField field : artifact.getFields()) {
					if (name.equals(field.getName()))
						return false;
				}
			} else if (component instanceof IMethod) {
				IMethod meth = (IMethod) component;
				String methLabel = meth.getLabelString();
				for (IMethod method : artifact.getMethods()) {
					if (methLabel.equals(method.getLabelString()))
						return false;
				}
			} else if (component instanceof ILiteral) {
				String name = component.getName();
				for (ILiteral literal : artifact.getLiterals()) {
					if (name.equals(literal.getName()))
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * Validates that the target can receive the given IModelComponents. For
	 * example, An Enumeration can not receive methods.
	 * 
	 * @param target
	 * @return
	 */
	private boolean validateCompatibleTarget(Object target) {
		IAbstractArtifact artifact = TSExplorerUtils.getArtifactFor(target);
		boolean acceptLiterals = (artifact instanceof IManagedEntityArtifact
				|| artifact instanceof IDatatypeArtifact || artifact instanceof IEnumArtifact);
		boolean acceptMethods = (artifact instanceof IManagedEntityArtifact
				|| artifact instanceof IDatatypeArtifact
				|| artifact instanceof ISessionArtifact || artifact instanceof IAssociationClassArtifact);
		boolean acceptFields = !(artifact instanceof IEnumArtifact || artifact instanceof ISessionArtifact);

		for (IModelComponent component : getIModelComponents()) {
			if (component instanceof ILiteral && !acceptLiterals)
				return false;
			else if (component instanceof IField && !acceptFields)
				return false;
			else if (component instanceof IMethod && !acceptMethods)
				return false;
		}
		return true;
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
