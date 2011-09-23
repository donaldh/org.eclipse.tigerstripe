/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Valentin Erastov) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.handlers.HandlerUtil;

public class AttachDanglingAnnotationHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		Shell shell = window.getShell();
		
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		final List<AttachDanglingAnnotationAction> items = new ArrayList<AttachDanglingAnnotationAction>();
		
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			final Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			if (firstElement != null) {
				try {
					dialog.run(true, true, new IRunnableWithProgress() {
						
						public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
							collectMarkers(items, monitor, firstElement);
						}
					});
				} catch (Exception e) {
					EclipsePlugin.log(e);
					return null;
				}
			}
		}
		
		if (items.isEmpty()) {
			MessageDialog
					.openInformation(shell, "Annotations have not been found",
							"There are no dangling annotations that may be attached to the selected object");
			return null;
		}

		ListDialog ld = new ListDialog(shell);
		ld.setContentProvider(new ArrayContentProvider());
		
		ld.setLabelProvider(new LabelProvider() {
			
			@Override
			public String getText(Object element) {
				AttachDanglingAnnotationAction action = ((AttachDanglingAnnotationAction) element);
				return action.getText();
			}
			
		});
		ld.setTitle("List of Dangling Annotations");
		ld.setInput(items.toArray());
		
		if (ld.open() == Window.OK) {
			Object[] result = ld.getResult();
			if (result.length > 0) {
				((AttachDanglingAnnotationAction)result[0]).run();
			}
		}
		return null;
	}
	
	private void collectMarkers(List<AttachDanglingAnnotationAction> items,
			IProgressMonitor monitor, Object selected) {
		IMarker[] markers;
		try {
			markers = ResourcesPlugin
					.getWorkspace()
					.getRoot()
					.findMarkers(BuilderConstants.ANNOTATION_MARKER_ID, true,
							IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			EclipsePlugin.log(e);
			return;
		}

		IAnnotationManager manager = AnnotationPlugin.getManager();
		if (selected instanceof IAdaptable) {
			Object target = findTarget((IAdaptable) selected);
			if (target != null) {
				for (IMarker m : markers) {
					if (monitor.isCanceled()) {
						return;
					}
					try {

						String id = (String) m
								.getAttribute(BuilderConstants.ANNOTATION_ID);
						if (id == null) {
							continue;
						}
						Annotation annotation = manager.getAnnotationByIdRaw(id);
						if (annotation == null) {
							continue;
						}
						AnnotationType type = manager.getType(annotation);
						if (type == null) {
							continue;
						}
						
						if (manager.isPossibleToAdd(target, type.getClazz())) {
							items.add(new AttachDanglingAnnotationAction(m,
									annotation, target));
						}
					} catch (CoreException e) {
						EclipsePlugin.log(e);
						continue;
					}
				}
			}
		}
	}

	private Object findTarget(IAdaptable selected) {
		
		if (selected instanceof IModelComponent) {
			return selected;
		}
		
		IModelComponent mc = adapt(selected, IModelComponent.class);
		if (mc != null) {
			return mc;
		}
		
		IResource resource = adapt(selected, IResource.class);
		if (resource != null) {
			return resource;
		}

		IAssociationEnd end = adapt(selected, IAssociationEnd.class);
		if (end != null) {
			IType type = end.getType();
			if (type != null) {
				IAbstractArtifact artifact = type.getArtifact();
				if (artifact != null) {
					return adapt(artifact, IResource.class);
				}
			}
			return null;
		}
		
		return null;
	}
}
