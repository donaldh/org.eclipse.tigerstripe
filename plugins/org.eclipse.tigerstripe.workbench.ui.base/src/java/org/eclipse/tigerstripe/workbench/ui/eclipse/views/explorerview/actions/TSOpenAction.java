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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jdt.ui.actions.OpenAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact.DependencyEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ReadOnlyArtifactEditorInput;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactAttributesSection;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactConstantsSection;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactMethodsSection;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactOverviewPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.association.AssociationSpecificsSection;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.dependency.DependencySpecificsSection;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.ReadOnlyDescriptorEditorInput;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.FileEditorInput;

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

	public final static String DEPENDENCY_EDITOR = "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ossj.dependencyEditor";

	public TSOpenAction(IWorkbenchSite site) {
		super(site);
	}

	public static IEditorPart openEditor(Object element, IWorkbenchPage page) {
		try {
			if (element instanceof ManagedEntityArtifact) {

				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, ENTITY_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), ENTITY_EDITOR);
				}
			} else if (element instanceof ExceptionArtifact) {
				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, EXCEPTION_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), EXCEPTION_EDITOR);
				}
			} else if (element instanceof QueryArtifact) {
				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, QUERY_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), QUERY_EDITOR);
				}
			} else if (element instanceof EventArtifact) {
				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, EVENT_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), EVENT_EDITOR);
				}
			} else if (element instanceof DatatypeArtifact) {
				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, DATATYPE_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), DATATYPE_EDITOR);
				}
			} else if (element instanceof SessionFacadeArtifact) {
				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, SESSION_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), SESSION_EDITOR);
				}
			} else if (element instanceof EnumArtifact) {
				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, ENUM_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), ENUM_EDITOR);
				}
			} else if (element instanceof UpdateProcedureArtifact) {
				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, UPDATEPROC_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), UPDATEPROC_EDITOR);
				}
			} else if (element instanceof AssociationClassArtifact) {
				// because AssociationClassArtifact is-a AssociationArtifact
				// we must check for AssociationClass first... in that order!
				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, ASSOCIATIONCLASS_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), ASSOCIATIONCLASS_EDITOR);
				}
			} else if (element instanceof AssociationArtifact) {
				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, ASSOCIATION_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), ASSOCIATION_EDITOR);
				}
			} else if (element instanceof IDependencyArtifact) {
				if (((IAbstractArtifact) element).isReadonly()) {
					ReadOnlyArtifactEditorInput input = new ReadOnlyArtifactEditorInput(
							null, (IAbstractArtifact) element);
					return page.openEditor(input, DEPENDENCY_EDITOR);
				} else {
					IResource iResource = TSExplorerUtils
							.getIResourceForArtifact((IAbstractArtifact) element);
					return page.openEditor(new FileEditorInput(
							(IFile) iResource), DEPENDENCY_EDITOR);
				}
			} else if (element instanceof IStorage
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
				return page.openEditor(new FileEditorInput((IFile) node
						.getKeyResource()), node.getEditor());
			}
		} catch (PartInitException e) {
			EclipsePlugin.logErrorMessage("Failed to open the Editor.");
		} catch (TigerstripeException e) {
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
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof IJavaElement) {
				IJavaElement jElem = (IJavaElement) objects[i];
				IAbstractArtifact artifact = null;
				artifact = TSExplorerUtils.getArtifactFor(jElem);
				if (artifact != null) {
					openEditor(artifact, page);
				} else {
					super.run(new Object[] { objects[i] });
				}
			} else if (objects[i] instanceof IAbstractArtifact) {
				openEditor(objects[i], page);
			} else if (objects[i] instanceof IStorage
					&& ITigerstripeConstants.PROJECT_DESCRIPTOR
							.equals(((IStorage) objects[i]).getName())) {
				openEditor(objects[i], page);
			} else if (objects[i] instanceof AbstractLogicalExplorerNode) {
				openEditor(objects[i], page);
			} else if (objects[i] instanceof IField
					|| objects[i] instanceof IMethod
					|| objects[i] instanceof ILiteral
					|| objects[i] instanceof IRelationshipEnd) {
				// if it's a field, a method, or a Label, then open the
				// appropriate section of
				// the appropriate multi-page editor and select the field in
				// question...first, find
				// the containing artifact
				IAbstractArtifact artifact = null;
				if (objects[i] instanceof IField)
					artifact = (IAbstractArtifact) ((IField) objects[i])
							.getContainingArtifact();
				else if (objects[i] instanceof IMethod)
					artifact = (IAbstractArtifact) ((IMethod) objects[i])
							.getContainingArtifact();
				else if (objects[i] instanceof ILiteral)
					artifact = (IAbstractArtifact) ((ILiteral) objects[i])
							.getContainingArtifact();
				else if (objects[i] instanceof DependencyEnd)
					artifact = (IAbstractArtifact) ((DependencyEnd) objects[i])
							.getContainingRelationship();
				else if (objects[i] instanceof IRelationshipEnd)
					artifact = (IAbstractArtifact) ((IAssociationEnd) objects[i])
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
					OssjArtifactOverviewPage selectedPage = (OssjArtifactOverviewPage) artEditor
							.getSelectedPage();
					IFormPart[] formParts = selectedPage.getManagedForm()
							.getParts();
					for (int j = 0; j < formParts.length; j++) {
						if (objects[i] instanceof IField
								&& formParts[j] instanceof OssjArtifactAttributesSection) {
							// if we are working with a field and we've found
							// the Attributes section, then we are in
							// the right place
							OssjArtifactAttributesSection attributesSection = (OssjArtifactAttributesSection) formParts[j];
							// set the section so that it is expanded
							attributesSection.getSection().setExpanded(true);
							// determine where the section is and scroll so that
							// it is visible
							Point origin = attributesSection.getSection()
									.getLocation();
							ScrolledForm scrolledForm = selectedPage
									.getManagedForm().getForm();
							scrolledForm.setOrigin(origin);
							// then select the appropriate row in the table (and
							// make the details visible?)
							TableViewer viewer = attributesSection.getViewer();
							Table table = viewer.getTable();
							table.deselectAll();
							IField thisField = (IField) objects[i];
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
						} else if (objects[i] instanceof IMethod
								&& formParts[j] instanceof OssjArtifactMethodsSection) {
							// else if we are working with a method and we've
							// found the Methods section, then we are in
							// the right place
							OssjArtifactMethodsSection methodsSection = (OssjArtifactMethodsSection) formParts[j];
							// set the section so that it is expanded
							methodsSection.getSection().setExpanded(true);
							// determine where the section is and scroll so that
							// it is visible
							Point origin = methodsSection.getSection()
									.getLocation();
							ScrolledForm scrolledForm = selectedPage
									.getManagedForm().getForm();
							scrolledForm.setOrigin(origin);
							// then select the appropriate row in the table (and
							// make the details visible?)
							TableViewer viewer = methodsSection.getViewer();
							Table table = viewer.getTable();
							table.deselectAll();
							IMethod thisMethod = (IMethod) objects[i];
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
						} else if (objects[i] instanceof ILiteral
								&& formParts[j] instanceof OssjArtifactConstantsSection) {
							// else if we are working with a label and we've
							// found the Constants section, then we are in
							// the right place
							OssjArtifactConstantsSection constantsSection = (OssjArtifactConstantsSection) formParts[j];
							// set the section so that it is expanded
							constantsSection.getSection().setExpanded(true);
							// determine where the section is and scroll so that
							// it is visible
							Point origin = constantsSection.getSection()
									.getLocation();
							ScrolledForm scrolledForm = selectedPage
									.getManagedForm().getForm();
							scrolledForm.setOrigin(origin);
							// then select the appropriate row in the table (and
							// make the details visible?)
							TableViewer viewer = constantsSection.getViewer();
							Table table = viewer.getTable();
							table.deselectAll();
							ILiteral thisLabel = (ILiteral) objects[i];
							for (int row = 0; row < table.getItemCount(); row++) {
								TableItem tableItem = table.getItem(row);
								ILiteral rowLabel = (ILiteral) tableItem.getData();
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
						} else if (objects[i] instanceof IRelationshipEnd
								&& formParts[j] instanceof AssociationSpecificsSection) {
							// else if we are working with a label and we've
							// found the Constants section, then we are in
							// the right place
							AssociationSpecificsSection specificsSection = (AssociationSpecificsSection) formParts[j];
							// set the section so that it is expanded
							specificsSection.getSection().setExpanded(true);
							// determine where the section is and scroll so that
							// it is visible
							Point origin = specificsSection.getSection()
									.getLocation();
							ScrolledForm scrolledForm = selectedPage
									.getManagedForm().getForm();
							scrolledForm.setOrigin(origin);
							IRelationshipEnd relationshipEnd = (IRelationshipEnd) objects[i];
							specificsSection.selectEndByName(relationshipEnd
									.getName());
							break;
						} else if (objects[i] instanceof IRelationshipEnd
								&& formParts[j] instanceof DependencySpecificsSection) {
							// else if we are working with a label and we've
							// found the Constants section, then we are in
							// the right place
							DependencySpecificsSection specificsSection = (DependencySpecificsSection) formParts[j];
							// set the section so that it is expanded
							specificsSection.getSection().setExpanded(true);
							// determine where the section is and scroll so that
							// it is visible
							Point origin = specificsSection.getSection()
									.getLocation();
							ScrolledForm scrolledForm = selectedPage
									.getManagedForm().getForm();
							scrolledForm.setOrigin(origin);
							IRelationshipEnd relationshipEnd = (IRelationshipEnd) objects[i];
							specificsSection.selectEndName(relationshipEnd
									.getName());
							break;
						}
					}
				}
			} else {
				super.run(new Object[] { objects[i] });
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
		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (element instanceof IJavaProject)
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
			if (element instanceof AbstractLogicalExplorerNode)
				continue;
			return false;
		}
		return true;
	}
}
