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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.TigerstripeRefactorWizardDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.DragAndDropMoveRefactorWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;

public class ArtifactComponentTransferDropAdapter extends ViewerDropAdapter
		implements TransferDropTargetListener {

	private List<?> selectedElements;
	private int internalCurrentOperation; // needed to duplicate as no setter in

	public ArtifactComponentTransferDropAdapter(StructuredViewer viewer) {
		super(viewer);
	}

	/**
	 * @see ViewerDropAdapter#performDrop
	 */
	public boolean performDrop(Object data) {
		// should never be called, since we override the drop() method.
		return false;
	}

	public Transfer getTransfer() {
		return LocalSelectionTransfer.getInstance();
	}

	public boolean isEnabled(DropTargetEvent event) {
		initializeSelection();
		IModelComponent[] nodes = getIModelComponents();
		return nodes.length != 0;
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
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				IModelComponent[] components = getIModelComponents();
				IAbstractArtifact targetArtifact = TSExplorerUtils
						.getArtifactFor(target);

				for (IModelComponent component : components) {
					if (component instanceof IField) {
						IField field = (IField) component;
						targetArtifact.addField(field.clone());
					} else if (component instanceof IMethod) {
						IMethod method = (IMethod) component;
						targetArtifact.addMethod(method.clone());
					} else if (component instanceof ILiteral) {
						ILiteral lit = (ILiteral) component;
						targetArtifact.addLiteral(lit.clone());
					}
				}

				try {
					targetArtifact.doSave(new NullProgressMonitor());
					IResource res = (IResource) targetArtifact
							.getAdapter(IResource.class);
					if (res != null) {
						try {
							res.refreshLocal(IResource.DEPTH_ZERO,
									new NullProgressMonitor());
						} catch (CoreException e) {
							throw new TigerstripeException(e.getMessage(), e);
						}
					}
				} catch (TigerstripeException e) {
					Status error = new Status(IStatus.ERROR, EclipsePlugin
							.getPluginId(), 222,
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
			} else if (obj instanceof IAdaptable) {
				IAbstractArtifact comp = (IAbstractArtifact) ((IAdaptable) obj)
						.getAdapter(IAbstractArtifact.class);
				if (comp != null) {
					result.add(comp);
				}
			}
		}
		return result.toArray(new IModelComponent[result.size()]);
	}

	private void handleDropMove(final Object target, DropTargetEvent event)
			throws InvocationTargetException, InterruptedException {

		Display.getCurrent().asyncExec(new Runnable() {

			public void allArtifactsRun() {
				
				IModelComponent[] allArtifactsToBeMoved = getIModelComponents();
				IResource targetContainer = (IResource) ((IAdaptable) target)
						.getAdapter(IResource.class);

				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				DragAndDropMoveRefactorWizard wizard = new DragAndDropMoveRefactorWizard();

				for (IModelComponent modelComponent : allArtifactsToBeMoved) {

					try {

						IAbstractArtifact srcArtifact = (IAbstractArtifact) modelComponent;
						ITigerstripeModelProject srcProject = srcArtifact
								.getProject();
						ITigerstripeModelProject destProject = (ITigerstripeModelProject) targetContainer
								.getProject().getAdapter(
										ITigerstripeModelProject.class);
						
						IPackageArtifact pkg = (IPackageArtifact) targetContainer
								.getAdapter(IPackageArtifact.class);
						String destFQN = pkg.getFullyQualifiedName() + '.'
								+ srcArtifact.getName();

						ModelRefactorRequest request = new ModelRefactorRequest();
						request.setOriginal(srcProject, srcArtifact
								.getFullyQualifiedName());
						request.setDestination(destProject, destFQN);
						wizard.addRequest(request);
					} catch (TigerstripeException e) {
						Status error = new Status(IStatus.ERROR, EclipsePlugin
								.getPluginId(), 222,
								"Invalid refactor request: " + e.getMessage(),
								e);
						MessageDialog
								.openError(getShell(), "Refactor Error",
										"An error occured during move operation.\nPlease check log for more details");
						EclipsePlugin.log(error);
					}

				}

				TigerstripeRefactorWizardDialog dialog = new TigerstripeRefactorWizardDialog(
						window.getShell(), wizard);
				dialog.open();

			}

			public void run() {
				IModelComponent[] components = getIModelComponents();

				if (isAllArtifacts())
					allArtifactsRun();

				IAbstractArtifact targetArtifact = TSExplorerUtils
						.getArtifactFor(target);

				Set<IAbstractArtifact> srcArtifacts = new HashSet<IAbstractArtifact>();

				for (IModelComponent component : components) {
					if (component instanceof IField) {
						IField field = (IField) component;
						URI oldValue = (URI) component.getAdapter(URI.class);
						IAbstractArtifact srcArtifact = (IAbstractArtifact) field
								.getContainingArtifact();
						srcArtifact.removeFields(Collections.singleton(field));
						targetArtifact.addField(field);
						URI newValue = (URI) field.getAdapter(URI.class);
						srcArtifacts.add(srcArtifact);

						// Create a notification and push down the pipe
						ModelChangeDelta delta = new ModelChangeDelta(
								IModelChangeDelta.MOVE);
						delta.setAffectedModelComponentURI((URI) srcArtifact
								.getAdapter(URI.class));
						delta.setOldValue(oldValue);
						delta.setNewValue(newValue);
						TigerstripeWorkspaceNotifier.INSTANCE
								.signalModelChange(delta);
					} else if (component instanceof IMethod) {
						IMethod method = (IMethod) component;
						URI oldValue = (URI) component.getAdapter(URI.class);
						IAbstractArtifact srcArtifact = (IAbstractArtifact) method
								.getContainingArtifact();
						srcArtifact
								.removeMethods(Collections.singleton(method));
						targetArtifact.addMethod(method);
						URI newValue = (URI) method.getAdapter(URI.class);
						srcArtifacts.add(srcArtifact);

						// Create a notification and push down the pipe
						ModelChangeDelta delta = new ModelChangeDelta(
								IModelChangeDelta.MOVE);
						delta.setAffectedModelComponentURI((URI) srcArtifact
								.getAdapter(URI.class));
						delta.setOldValue(oldValue);
						delta.setNewValue(newValue);
						TigerstripeWorkspaceNotifier.INSTANCE
								.signalModelChange(delta);
					} else if (component instanceof ILiteral) {
						ILiteral lit = (ILiteral) component;
						URI oldValue = (URI) component.getAdapter(URI.class);
						IAbstractArtifact srcArtifact = (IAbstractArtifact) lit
								.getContainingArtifact();
						srcArtifact.removeLiterals(Collections.singleton(lit));
						targetArtifact.addLiteral(lit);
						URI newValue = (URI) lit.getAdapter(URI.class);
						srcArtifacts.add(srcArtifact);

						// Create a notification and push down the pipe
						ModelChangeDelta delta = new ModelChangeDelta(
								IModelChangeDelta.MOVE);
						delta.setAffectedModelComponentURI((URI) srcArtifact
								.getAdapter(URI.class));
						delta.setOldValue(oldValue);
						delta.setNewValue(newValue);
						TigerstripeWorkspaceNotifier.INSTANCE
								.signalModelChange(delta);
					}
				}

				try {
					targetArtifact.doSave(new NullProgressMonitor());
					IResource res = (IResource) targetArtifact
							.getAdapter(IResource.class);
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
						res = (IResource) art.getAdapter(IResource.class);
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
					Status error = new Status(IStatus.ERROR, EclipsePlugin
							.getPluginId(), 222,
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
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		internalCurrentOperation = DND.DROP_NONE;

		initializeSelection();

		// Can only DnD logical nodes together with other logical nodes at this
		// point.
		if (getIModelComponents().length != selectedElements.size())
			return false;
		switch (operation) {
		case DND.DROP_DEFAULT:
			internalCurrentOperation = handleValidateDefault(target,
					DND.DROP_MOVE | DND.DROP_COPY);
			break;
		case DND.DROP_COPY:
			internalCurrentOperation = handleValidateCopy(target);
			break;
		case DND.DROP_MOVE:
			internalCurrentOperation = handleValidateMove(target);
			break;
		}
		return true;
	}

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

		if (target instanceof ICompilationUnit && validateNoDuplicate(target)
				&& validateCompatibleTarget(target)
				&& validateSameProject(target))
			return DND.DROP_COPY;
		else
			return DND.DROP_NONE;
	}

	private int handleValidateMove(Object target) {
		if (target == null)
			return DND.DROP_NONE;

		if (handleValidateArtifactMove(target) == DND.DROP_MOVE)
			return DND.DROP_MOVE;
		else if (target instanceof ICompilationUnit
				&& validateNoDuplicate(target)
				&& validateCompatibleTarget(target)
				&& validateSameProject(target))
			return DND.DROP_MOVE;
		else
			return DND.DROP_NONE;
	}

	private boolean isAllArtifacts() {
		IModelComponent[] comps = getIModelComponents();
		if (comps.length == 0)
			return false;

		for (IModelComponent comp : comps) {
			if (!(comp instanceof IAbstractArtifact))
				return false;
		}
		return true;
	}

	private int handleValidateArtifactMove(Object target) {
		if (target instanceof IAdaptable) {
			if (target instanceof IPackageFragment)
				target = (IResource) ((IPackageFragment) target)
						.getAdapter(IResource.class);
			IPackageArtifact packArt = (IPackageArtifact) ((IAdaptable) target)
					.getAdapter(IPackageArtifact.class);
			if (packArt != null && isAllArtifacts())
				return DND.DROP_MOVE;
		}

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
						cRes = (IResource) ((IAbstractArtifact) field
								.getContainingArtifact())
								.getAdapter(IResource.class);
					} else if (component instanceof IMethod) {
						IMethod method = (IMethod) component;
						cRes = (IResource) ((IAbstractArtifact) method
								.getContainingArtifact())
								.getAdapter(IResource.class);
					} else if (component instanceof ILiteral) {
						ILiteral literal = (ILiteral) component;
						cRes = (IResource) ((IAbstractArtifact) literal
								.getContainingArtifact())
								.getAdapter(IResource.class);
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