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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.viewsupport.IViewPartInputProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact.DependencyEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.AttributeRef;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.statushandlers.IStatusAdapterConstants;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;

@SuppressWarnings("restriction")
public class WizardUtils {

	private final static String[] scalarTypes = { "int", "boolean", "double",
			"float", "byte", "short" };

	private final static String[] scalarDefaultValues = { "0", "true", "0.",
			"0.", "0" };

	/**
	 * Return a valid default value for the given type
	 * 
	 * @param returnClass
	 * @return
	 */
	public static String getDefaultReturnValue(String returnClass,
			int dimensions) {

		String scalarType = null;
		String scalarValue = null;

		for (int i = 0; i < scalarTypes.length; i++) {
			if (scalarTypes[i].equals(returnClass)) {
				scalarType = scalarTypes[i];
				scalarValue = scalarDefaultValues[i];
			}
		}

		if (scalarType != null) {
			if (dimensions != 0) {
				for (int i = 0; i < dimensions; i++) {
					scalarType = scalarType + "[0]";
				}

				return "new " + scalarType;
			} else
				return scalarValue;
		} else
			return "null";
	}

	public String getRefBy(int refBy) {
		return AttributeRef.refByLabels[refBy];
	}

	/**
	 * Returns a IJavaElement from the current GUI context (explorer, editor)
	 * 
	 * @param selection
	 * @return
	 */
	public static IJavaElement getInitialJavaElement(
			IStructuredSelection selection) {
		IJavaElement jelem = null;
		if (selection != null && !selection.isEmpty()) {
			Object selectedElement = selection.getFirstElement();

			if (selectedElement instanceof IField) {
				IField field = (IField) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) field
						.getContainingArtifact();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null)
					selectedElement = obj;
			} else if (selectedElement instanceof IMethod) {
				IMethod meth = (IMethod) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) meth
						.getContainingArtifact();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null)
					selectedElement = obj;
			} else if (selectedElement instanceof ILiteral) {
				ILiteral la = (ILiteral) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingArtifact();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null)
					selectedElement = obj;
			} else if (selectedElement instanceof IAssociationEnd) {
				IAssociationEnd la = (IAssociationEnd) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingArtifact();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null)
					selectedElement = obj;
			} else if (selectedElement instanceof DependencyEnd) {
				DependencyEnd la = (DependencyEnd) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingRelationship();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null)
					selectedElement = obj;
			} else if (selectedElement instanceof DependencyEnd) {
				DependencyEnd la = (DependencyEnd) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingRelationship();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null) {
					selectedElement = obj;
				}
			}

			if (selectedElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) selectedElement;

				jelem = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
				if (jelem == null) {
					IResource resource = (IResource) adaptable
							.getAdapter(IResource.class);
					if (resource != null
							&& resource.getType() != IResource.ROOT) {
						while (jelem == null
								&& resource.getType() != IResource.PROJECT) {
							resource = resource.getParent();
							jelem = (IJavaElement) resource
									.getAdapter(IJavaElement.class);
						}
						if (jelem == null) {
							jelem = JavaCore.create(resource); // java project
						}
					}
				}
			}
		}
		if (jelem == null) {
			IWorkbenchPart part = EclipsePlugin.getActivePage().getActivePart();
			// if (part instanceof ContentOutline) {
			// part= JavaPlugin.getActivePage().getActiveEditor();
			// }
			if (part instanceof IViewPartInputProvider) {
				Object elem = ((IViewPartInputProvider) part)
						.getViewPartInput();
				if (elem instanceof IJavaElement) {
					jelem = (IJavaElement) elem;
				}
			}
		}

		if (jelem == null || jelem.getElementType() == IJavaElement.JAVA_MODEL) {
			try {
				IJavaProject[] projects = JavaCore.create(
						EclipsePlugin.getWorkspace().getRoot())
						.getJavaProjects();
				if (projects.length == 1) {
					jelem = projects[0];
				}
			} catch (JavaModelException e) {
				EclipsePlugin.log(e);
			}
		}

		return jelem;
	}

	// Returns true if resource creation problems detected
	public static boolean handleProjectCreationErrors(InvocationTargetException e,
			String projectName) {
		Throwable target = e.getTargetException();

		// Detect & handle creation problems
		if (target instanceof CoreException) {
			Throwable c = ((CoreException) target).getStatus().getException();
			if (c instanceof TigerstripeException) {
				TigerstripeException te = (TigerstripeException) c;
				Exception ex = te.getException();
				if (ex instanceof CoreException) {
					CoreException cause = (CoreException) ex;
					String msg;
					if (cause.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
						msg = MessageFormat
								.format("The underlying file system is case insensitive. "
										+ "There is an existing project or directory that conflicts with ''{0}''.",
										projectName);
					} else {
						msg = "Creation Problems";
					}
					StatusAdapter status = new StatusAdapter(
							new Status(cause.getStatus().getSeverity(),
									EclipsePlugin.PLUGIN_ID, msg, cause));
					status.setProperty(IStatusAdapterConstants.TITLE_PROPERTY,
							"Creation Problems");
					StatusManager.getManager().handle(status,
							StatusManager.BLOCK | StatusManager.LOG);
					return true;
				}
			}
		}

		EclipsePlugin.log(target);
		return false;
	}
	
}
