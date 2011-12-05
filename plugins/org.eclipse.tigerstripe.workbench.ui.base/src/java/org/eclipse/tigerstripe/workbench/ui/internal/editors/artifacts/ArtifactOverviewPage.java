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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.datatype.DatatypeArtifactEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.entity.EntityArtifactEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.enumeration.EnumArtifactEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.event.EventArtifactEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.exception.ExceptionArtifactEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.query.QueryArtifactEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.session.SessionArtifactEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.updateProcedure.UpdateProcedureArtifactEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.ide.IDE;


public class ArtifactOverviewPage extends TigerstripeFormPage implements IResourceChangeListener {

	private IArtifactFormLabelProvider labelProvider;

	private IOssjArtifactFormContentProvider contentProvider;

	private IManagedForm managedForm;

	public static final String PAGE_ID = "ossj.entity.overview"; //$NON-NLS-1$
	

	public ArtifactOverviewPage(FormEditor editor,
			IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(editor, PAGE_ID, "Overview");
		this.labelProvider = labelProvider;
		this.contentProvider = contentProvider;
	}

	public ArtifactOverviewPage(FormEditor editor) {
		super(editor, PAGE_ID, "Overview");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		ISelectionProvider selectionProvider = new SelectionProviderIntermediate();
		getSite().setSelectionProvider(selectionProvider);
		
		// Navid Mehregani: If there are serious compile issues in the file,  QDOX won't be able to parse it, thus we need to open the 
		// Java editor instead so user can fix the compile issues.
		if (contentProvider==null || labelProvider==null) {
			form.setText("Tigerstripe Artifact Editor");
			Composite body = form.getBody();
			body.setLayout(new GridLayout());			
			FormText rtext = toolkit.createFormText(body, false);
			rtext.setText("<p>There seems to be an error with this file.  Please try opening it with Java editor:</p>", true, false);
			IEditorInput editorInput = getEditor().getEditorInput();			
			try {
				if (editorInput != null) 
					IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), editorInput, "org.eclipse.jdt.ui.CompilationUnitEditor");
				
				getEditor().close(false);
				 
			} catch (PartInitException exception) {
				// Ignore
			}			
			
		} else {
			form.setText(contentProvider.getText(IArtifactFormContentProvider.ARTIFACT_OVERVIEW_TITLE));
			
			// Navid Mehregani: Used for populating the header with error information
			ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_BUILD);
			
			fillBody(managedForm, toolkit);
			// managedForm.refresh();	
		}		
	}

	@Override
	public void refresh() {
		if (managedForm != null) {
			managedForm.refresh();
		}
	}

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		body.setLayout(TigerstripeLayoutFactory.createClearTableWrapLayout(1,
				false));
		body.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		
		updateErrorMessage();
		
		Composite composite = toolkit.createComposite(body);
		composite.setLayout(TigerstripeLayoutFactory.createPageTableWrapLayout(
				2, true));
		composite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		
		TigerstripeSectionPart part = new ArtifactGeneralInfoSection(this,
				composite, toolkit, labelProvider, contentProvider);
		managedForm.addPart(part);

		part = new ArtifactContentSection(this, composite, toolkit,
				labelProvider, contentProvider);
		managedForm.addPart(part);

		
		composite = toolkit.createComposite(body);
		composite.setLayout(TigerstripeLayoutFactory.createPageTableWrapLayout(
				2, true));
		composite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		part = new ArtifactStereotypesSection(this, composite, toolkit,
				labelProvider, contentProvider);
		managedForm.addPart(part);
		
		part = new ArtifactAnnotationSection(this, composite, toolkit,
				labelProvider, contentProvider);
		managedForm.addPart(part);
		
		
		composite = toolkit.createComposite(body);
		composite.setLayout(TigerstripeLayoutFactory.createPageTableWrapLayout(
				2, false));
		composite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		if (contentProvider.hasSpecifics()) {
			part = contentProvider.getSpecifics(this, composite, toolkit);
			managedForm.addPart(part);
		}

		if (contentProvider.hasAttributes()) {
			part = new ArtifactAttributesSection(this, composite, toolkit,
					labelProvider, contentProvider, getAttributesStyle());
			managedForm.addPart(part);
		}

		if (contentProvider.hasConstants()) {
			part = new ArtifactConstantsSection(this, composite, toolkit,
					labelProvider, contentProvider, getConstantsStyle());

			// see bug #77
			// See #90 no need to set up setForcedType anymore. So #77 is kinda
			// undone.

			managedForm.addPart(part);
		}

		if (contentProvider.hasMethods()) {
			part = new ArtifactMethodsSection(this, composite, toolkit,
					labelProvider, contentProvider, getMethodsStyle());
			managedForm.addPart(part);
		}
	}

	private int getAttributesStyle() {
		if (getEditor() instanceof EntityArtifactEditor
				|| getEditor() instanceof DatatypeArtifactEditor
				|| getEditor() instanceof ExceptionArtifactEditor
				|| getEditor() instanceof QueryArtifactEditor
				|| getEditor() instanceof UpdateProcedureArtifactEditor
				|| getEditor() instanceof EventArtifactEditor) {
			return ExpandableComposite.EXPANDED;
		}
		return ExpandableComposite.COMPACT;
	}

	private int getMethodsStyle() {

		if (getEditor() instanceof SessionArtifactEditor) {
			return ExpandableComposite.EXPANDED;
		}
		return ExpandableComposite.COMPACT;
	}

	private int getConstantsStyle() {
		if (getEditor() instanceof EnumArtifactEditor) {
			return ExpandableComposite.EXPANDED;
		}
		return ExpandableComposite.COMPACT;
	}
	
	public void resourceChanged(IResourceChangeEvent event) {
		
		if (event.getType() == IResourceChangeEvent.POST_BUILD) 
			updateErrorMessage();
	}
	
	private void updateErrorMessage() {
		IEditorInput editorInput = getEditor().getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput)editorInput).getFile();
			
			if ((file != null) && (file.exists())) {
				try {
					IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true,	IResource.DEPTH_INFINITE);
					
					boolean errorsDetected = false;
					if (markers!=null) {
						for (int i=0; i < markers.length; i++) {
							if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
								final Object errorMessage = markers[i].getAttribute(IMarker.MESSAGE);
								if ((errorMessage instanceof String) && (((String)errorMessage).length()>0)) {
									PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
										public void run() {
											if (!managedForm.getForm().isDisposed())
												managedForm.getForm().setMessage("Error Detected: " + (String)errorMessage, IMessageProvider.ERROR);		
										}
									});
									
									errorsDetected = true;
									break;
								}
							}
						}
					}
					
					if (!errorsDetected) {
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
							public void run() {
								if (!managedForm.getForm().isDisposed())
									managedForm.getForm().setMessage("", IMessageProvider.NONE);
							}
						});
					}
					
				} catch (Exception e) {
					EclipsePlugin.logErrorMessage("Could not update header with error status", e);
				}
			}
		}
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	
	
}
