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

import static org.eclipse.tigerstripe.workbench.ui.internal.utils.ComponentUtils.scrollToComponentDeferred;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jdt.ui.actions.OpenAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.IElementWrapper;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ContextProjectAwareProxy;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact.DependencyEnd;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactAttributesSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactConstantsSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactMethodsSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactOverviewPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ReadOnlyArtifactEditorInput;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.association.AssociationSpecificsSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.dependency.DependencySpecificsSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.ReadOnlyDescriptorEditorInput;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLog;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.RelationshipAnchor;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.part.FileEditorInput;

@SuppressWarnings("restriction")
public class TSOpenAction extends OpenAction {

	public final static String DESCRIPTOR_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptorEditor";

	public final static String ENTITY_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.entityEditor";

	public final static String EXCEPTION_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.exceptionEditor";

	public final static String QUERY_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.queryEditor";

	public final static String EVENT_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.eventEditor";

	public final static String DATATYPE_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.datatypeEditor";

	public final static String SESSION_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.sessionEditor";

	public final static String ENUM_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.enumEditor";

	public final static String UPDATEPROC_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.updateProcedureEditor";

	public final static String ASSOCIATION_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.associationEditor";

	public final static String ASSOCIATIONCLASS_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.associationClassEditor";

	public final static String PLUGIN_DESCRIPTOR_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptorEditor";

	public final static String M0_DESCRIPTOR_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.m0DescriptorEditor";

	public final static String DEPENDENCY_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.dependencyEditor";

	public final static String PACKAGE_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.packageEditor";

	public TSOpenAction(IWorkbenchSite site) {
		super(site);
	}

	public static String getEditorIdForArtifact(IAbstractArtifact artifact) {
		if (artifact instanceof IManagedEntityArtifact) {
			return ENTITY_EDITOR;
		} else if (artifact instanceof IExceptionArtifact) {
			return EXCEPTION_EDITOR;
		} else if (artifact instanceof IQueryArtifact) {
			return QUERY_EDITOR;
		} else if (artifact instanceof IEventArtifact) {
			return EVENT_EDITOR;
		} else if (artifact instanceof IDatatypeArtifact) {
			return DATATYPE_EDITOR;
		} else if (artifact instanceof ISessionArtifact) {
			return SESSION_EDITOR;
		} else if (artifact instanceof IEnumArtifact) {
			return ENUM_EDITOR;
		} else if (artifact instanceof IUpdateProcedureArtifact) {
			return UPDATEPROC_EDITOR;
		} else if (artifact instanceof IAssociationClassArtifact) {
			return ASSOCIATIONCLASS_EDITOR;
		} else if (artifact instanceof IAssociationArtifact) {
			return ASSOCIATION_EDITOR;
		} else if (artifact instanceof IDependencyArtifact) {
			return DEPENDENCY_EDITOR;
		} else if (artifact instanceof IPackageArtifact) {
			return PACKAGE_EDITOR;
		}
		return null;
	}

	private static boolean isReadOnly(IAbstractArtifact artifact) {
		if (artifact.isReadonly() || artifact instanceof IContextProjectAware) {
			return true;
		}
		return false;
	}

	public static IAbstractArtifact reload(IAbstractArtifact artifact) {
		
		if (artifact == null) {
			return null;
		}
		
		try {
			ITigerstripeModelProject project = artifact.getProject();

			if (project == null) {
				return artifact;
			}

			IArtifactManagerSession artifactManagerSession = project
					.getArtifactManagerSession();

			if (artifactManagerSession == null) {
				return artifact;
			}

			ArtifactManager artifactManager = artifactManagerSession
					.getArtifactManager();

			if (artifactManager == null) {
				return artifact;
			}

			IAbstractArtifactInternal actual = artifactManager
					.getArtifactByFullyQualifiedName(
							artifact.getFullyQualifiedName(), false,
							new NullProgressMonitor());
			
			if (ContextProjectAwareProxy.isContextualProxy(artifact)) {
				ContextProjectAwareProxy.changeTraget(artifact, actual);
				return artifact;
			} else {
				return actual;
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return artifact;
		}
	}
	
	public static IEditorPart openEditor(Object element, IWorkbenchPage page) {
		try {
			if (element instanceof IAbstractArtifact) {
				IAbstractArtifact artifact = (IAbstractArtifact) element;
				
				String editorId = getEditorIdForArtifact(artifact);
				if (editorId != null) {
					IEditorInput input = null;
					if (isReadOnly(artifact)) {
						input = new ReadOnlyArtifactEditorInput(null, reload(artifact));
					} else {
						IResource iResource = (IResource) artifact
								.getAdapter(IResource.class);
						if (artifact instanceof IPackageArtifact
								&& iResource == null) {
							// This was a volatile Package created on the fly!
							// we need to do a proper create now
							try {
								ITigerstripeModelProject project = ((IAbstractArtifact) element)
										.getProject();
								IPackageArtifact packageArtifact = PackageArtifact
										.makeArtifactForPackage(
												project.getArtifactManagerSession(),
												((IAbstractArtifact) element)
														.getFullyQualifiedName());
								iResource = (IResource) packageArtifact
										.getAdapter(IResource.class);
							} catch (Exception e) {
								TigerstripeLog
										.logError(
												"Could not extract or create package artifact",
												e);
							}
						}
						input = new FileEditorInput((IFile) iResource);
					}
					return page.openEditor(input, editorId);
				}
			}

			if (element instanceof IStorage
					&& ITigerstripeConstants.PROJECT_DESCRIPTOR
							.equals(((IStorage) element).getName())) {
				if (element instanceof JarEntryFile) {
					// We're trying to open a tigerstripe.xml out of a module
					JarEntryFile jFile = (JarEntryFile) element;
					ReadOnlyDescriptorEditorInput input = new ReadOnlyDescriptorEditorInput(
							jFile);
					return page.openEditor(input, DESCRIPTOR_EDITOR);
				} else {
					IFile file = (IFile) ((IAdaptable) element)
							.getAdapter(IFile.class);
					return page.openEditor(new FileEditorInput(file),
							DESCRIPTOR_EDITOR);
				}
			} else if (element instanceof AbstractLogicalExplorerNode) {
				AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) element;
				return page.openEditor(
						new FileEditorInput((IFile) node.getKeyResource()),
						node.getEditor());
			}
		} catch (PartInitException e) {
			EclipsePlugin.logErrorMessage("Failed to open the Editor.");
		}
		return null;
	}

	@Override
	public void run(Object[] objects) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page == null)
			return;
		for (Object object : objects) {
			if (object instanceof IElementWrapper) {
				object = ((IElementWrapper) object).getElement();
			}

			if (object instanceof IJavaElement) {
				IAbstractArtifact artifact = TSExplorerUtils
						.getArtifactFor(object);
				if (artifact != null) {
					openEditor(artifact, page);
				} else {
					super.run(new Object[] { object });
				}
			} else if (object instanceof IAbstractArtifact) {
				openEditor(object, page);
			} else if (object instanceof IStorage
					&& ITigerstripeConstants.PROJECT_DESCRIPTOR
							.equals(((IStorage) object).getName())) {
				openEditor(object, page);
			} else if (object instanceof AbstractLogicalExplorerNode) {
				openEditor(object, page);
			} else if (object instanceof IField || object instanceof IMethod
					|| object instanceof ILiteral
					|| object instanceof IRelationshipEnd
					|| object instanceof RelationshipAnchor) {
				// if it's a field, a method, or a Label, then open the
				// appropriate section of
				// the appropriate multi-page editor and select the field in
				// question...first, find
				// the containing artifact
				IAbstractArtifact artifact = null;
				if (object instanceof IField)
					artifact = ((IField) object).getContainingArtifact();
				else if (object instanceof IMethod)
					artifact = ((IMethod) object).getContainingArtifact();
				else if (object instanceof ILiteral)
					artifact = ((ILiteral) object).getContainingArtifact();
				else if (object instanceof DependencyEnd)
					artifact = (IAbstractArtifact) ((DependencyEnd) object)
							.getContainingRelationship();
				else if (object instanceof RelationshipAnchor)
					artifact = (IAbstractArtifact) (((RelationshipAnchor) object)
							.getEnd()).getContainingRelationship();
				else if (object instanceof IRelationshipEnd)
					artifact = ((IAssociationEnd) object)
							.getContainingArtifact();
				// then open the editor for that artifact
				IEditorPart iEditorPart = openEditor(artifact, page);
				// then, once the editor is open, set the "overview" page as the
				// active page
				if (iEditorPart != null) {
					ArtifactEditorBase artEditor = (ArtifactEditorBase) iEditorPart;
					artEditor.setActivePage("ossj.entity.overview", artEditor);
					// now, get the active page and loop through the parts of
					// that page
					ArtifactOverviewPage selectedPage = (ArtifactOverviewPage) artEditor
							.getSelectedPage();
					IFormPart[] formParts = selectedPage.getManagedForm()
							.getParts();
					for (int j = 0; j < formParts.length; j++) {
						if (object instanceof IField
								&& formParts[j] instanceof ArtifactAttributesSection) {
							// if we are working with a field and we've found
							// the Attributes section, then we are in
							// the right place
							ArtifactAttributesSection attributesSection = (ArtifactAttributesSection) formParts[j];
							// set the section so that it is expanded
							attributesSection.getSection().setExpanded(true);
							// determine where the section is and scroll so that
							// it is visible
							scrollToComponentDeferred(selectedPage
									.getManagedForm().getForm(),
									attributesSection.getSection());
							// then select the appropriate row in the table (and
							// make the details visible?)
							TableViewer viewer = attributesSection.getViewer();
							Table table = viewer.getTable();
							table.deselectAll();
							IField thisField = (IField) object;
							for (int row = 0; row < table.getItemCount(); row++) {
								TableItem tableItem = table.getItem(row);
								IField rowField = (IField) tableItem.getData();
								if (thisField.getName().equals(
										rowField.getName())) {
									table.select(row);
									selectedPage.getManagedForm()
											.fireSelectionChanged(formParts[j],
													viewer.getSelection());
									break;
								}
							}
							table.setFocus();
							attributesSection.updateMaster();
							break;
						} else if (object instanceof IMethod
								&& formParts[j] instanceof ArtifactMethodsSection) {
							// else if we are working with a method and we've
							// found the Methods section, then we are in
							// the right place
							ArtifactMethodsSection methodsSection = (ArtifactMethodsSection) formParts[j];
							// set the section so that it is expanded
							methodsSection.getSection().setExpanded(true);
							// determine where the section is and scroll so that
							// it is visible
							scrollToComponentDeferred(selectedPage
									.getManagedForm().getForm(),
									methodsSection.getSection());
							// then select the appropriate row in the table (and
							// make the details visible?)
							TableViewer viewer = methodsSection.getViewer();
							Table table = viewer.getTable();
							table.deselectAll();
							IMethod thisMethod = (IMethod) object;
							for (int row = 0; row < table.getItemCount(); row++) {
								TableItem tableItem = table.getItem(row);
								IMethod rowMethod = (IMethod) tableItem
										.getData();
								if (thisMethod.getName().equals(
										rowMethod.getName())) {
									table.select(row);
									selectedPage.getManagedForm()
											.fireSelectionChanged(formParts[j],
													viewer.getSelection());
									break;
								}
							}
							table.setFocus();
							methodsSection.updateMaster();
							break;
						} else if (object instanceof ILiteral
								&& formParts[j] instanceof ArtifactConstantsSection) {
							// else if we are working with a label and we've
							// found the Constants section, then we are in
							// the right place
							ArtifactConstantsSection constantsSection = (ArtifactConstantsSection) formParts[j];
							// set the section so that it is expanded
							constantsSection.getSection().setExpanded(true);
							// determine where the section is and scroll so that
							// it is visible
							scrollToComponentDeferred(selectedPage
									.getManagedForm().getForm(),
									constantsSection.getSection());
							// then select the appropriate row in the table (and
							// make the details visible?)
							TableViewer viewer = constantsSection.getViewer();
							Table table = viewer.getTable();
							table.deselectAll();
							ILiteral thisLabel = (ILiteral) object;
							for (int row = 0; row < table.getItemCount(); row++) {
								TableItem tableItem = table.getItem(row);
								ILiteral rowLabel = (ILiteral) tableItem
										.getData();
								if (thisLabel.getName().equals(
										rowLabel.getName())) {
									table.select(row);
									selectedPage.getManagedForm()
											.fireSelectionChanged(formParts[j],
													viewer.getSelection());
									break;
								}
							}
							table.setFocus();
							constantsSection.updateMaster();
							break;
						} else if ((object instanceof IRelationshipEnd || object instanceof RelationshipAnchor)
								&& formParts[j] instanceof AssociationSpecificsSection) {
							// else if we are working with a label and we've
							// found the Constants section, then we are in
							// the right place
							AssociationSpecificsSection specificsSection = (AssociationSpecificsSection) formParts[j];
							// set the section so that it is expanded
							specificsSection.getSection().setExpanded(true);
							// determine where the section is and scroll so that
							// it is visible
							scrollToComponentDeferred(selectedPage
									.getManagedForm().getForm(),
									specificsSection.getSection());
							IRelationshipEnd relationshipEnd = null;
							if (object instanceof IRelationshipEnd) {
								relationshipEnd = (IRelationshipEnd) object;
							} else if (object instanceof RelationshipAnchor) {
								relationshipEnd = ((RelationshipAnchor) object)
										.getEnd();
							}
							specificsSection.selectEndByName(relationshipEnd
									.getName());
							break;
						} else if (object instanceof IRelationshipEnd
								&& formParts[j] instanceof DependencySpecificsSection) {
							// else if we are working with a label and we've
							// found the Constants section, then we are in
							// the right place
							DependencySpecificsSection specificsSection = (DependencySpecificsSection) formParts[j];
							// set the section so that it is expanded
							specificsSection.getSection().setExpanded(true);
							// determine where the section is and scroll so that
							// it is visible
							scrollToComponentDeferred(selectedPage
									.getManagedForm().getForm(),
									specificsSection.getSection());
							IRelationshipEnd relationshipEnd = (IRelationshipEnd) object;
							specificsSection.selectEndName(relationshipEnd
									.getName());
							break;
						}
					}
				}
			} else {
				super.run(new Object[] { object });
			}
		}
	}

	@Override
	public void selectionChanged(IStructuredSelection selection) {
		setEnabled(checkEnabled(selection));
	}

	@Override
	public void run(IStructuredSelection selection) {
		if (!checkEnabled(selection))
			return;
		run(selection.toArray());
	}

	private boolean checkEnabled(IStructuredSelection selection) {
		if (selection.isEmpty())
			return false;
		for (Iterator<?> iter = selection.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (element instanceof IElementWrapper) {
				element = ((IElementWrapper) element).getElement();
			}

			if (element instanceof IProject)
				return false;
			if (element instanceof ISourceReference)
				continue;
			if (element instanceof IFile)
				continue;
			if (element instanceof IStorage)
				continue;
			if (element instanceof IAbstractArtifact)
				continue;
			if (element instanceof IField)
				continue;
			if (element instanceof IMethod)
				continue;
			if (element instanceof ILiteral)
				continue;
			if (element instanceof IRelationshipEnd)
				continue;
			if (element instanceof RelationshipAnchor)
				continue;
			if (element instanceof AbstractLogicalExplorerNode)
				continue;
			return false;
		}
		return true;
	}
}
