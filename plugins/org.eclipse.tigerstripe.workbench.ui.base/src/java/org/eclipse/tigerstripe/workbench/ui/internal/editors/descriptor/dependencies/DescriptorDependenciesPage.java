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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class DescriptorDependenciesPage extends TigerstripeFormPage implements
		IResourceChangeListener {

	public static final String PAGE_ID = "descriptor.dependencies"; //$NON-NLS-1$

	private IManagedForm managedForm;

	public DescriptorDependenciesPage(FormEditor editor) {
		super(editor, PAGE_ID, "Dependencies");
	}

	public DescriptorDependenciesPage() {
		super(PAGE_ID, "Dependencies");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Project Dependencies");

		// Navid Mehregani: Used for populating the header with error
		// information
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
				IResourceChangeEvent.POST_BUILD);

		fillBody(managedForm, toolkit);
		managedForm.refresh();
	}

	@Override
	public void refresh() {
		if (managedForm != null) {
			managedForm.refresh();
		}
	}

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		body.setLayout(TigerstripeLayoutFactory.createPageGridLayout(1, true));

		updateErrorMessage();
		// sections
		managedForm.addPart(new ReferencedProjectsSection(this, body, toolkit));
	}

	public void resourceChanged(IResourceChangeEvent event) {

		if (event.getType() == IResourceChangeEvent.POST_BUILD)
			updateErrorMessage();
	}

	// N.M: Bugzilla 319768: If there are any compile issues, print the error
	// message in the form's header
	private void updateErrorMessage() {
		IEditorInput editorInput = getEditor().getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) editorInput).getFile();

			if (file != null && file.isAccessible()) {
				try {
					IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true,
							IResource.DEPTH_INFINITE);

					boolean errorDetected = false;
					if (markers != null) {
						for (int i = 0; i < markers.length; i++) {
							if (IMarker.SEVERITY_ERROR == markers[i]
									.getAttribute(IMarker.SEVERITY,
											IMarker.SEVERITY_INFO)) {
								final Object errorMessage = markers[i]
										.getAttribute(IMarker.MESSAGE);
								if ((errorMessage instanceof String)
										&& (((String) errorMessage).length() > 0)) {
									PlatformUI.getWorkbench().getDisplay()
											.asyncExec(new Runnable() {
												public void run() {
													if (!managedForm.getForm()
															.isDisposed())
														managedForm
																.getForm()
																.setMessage(
																		"Error Detected: "
																				+ (String) errorMessage,
																		IMessageProvider.ERROR);
												}
											});

									errorDetected = true;
									break;
								}
							}
						}
					}

					if (!errorDetected) {
						PlatformUI.getWorkbench().getDisplay()
								.asyncExec(new Runnable() {
									public void run() {
										if (!managedForm.getForm().isDisposed())
											managedForm.getForm().setMessage(
													"", IMessageProvider.NONE);
									}
								});
					}

				} catch (Exception e) {
					EclipsePlugin.logErrorMessage(
							"Could not update header with error status", e);
				}
			}
		}
	}
}
