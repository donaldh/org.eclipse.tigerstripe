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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.model.IField;
import org.eclipse.tigerstripe.api.model.ILabel;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactAttributesSection;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactConstantsSection;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactMethodsSection;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactOverviewPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions.TSOpenAction;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeAttributeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeEditableLabelEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeLiteralEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeMethodEditPart;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class OpenArtifactPartInEditorAction extends BaseDiagramPartAction
		implements IObjectActionDelegate {

	public void run(IAction action) {
		IAbstractArtifact artifact = getContainingArtifact();

		if (artifact != null) {
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();

			IEditorPart editorPart = TSOpenAction.openEditor(artifact, page);

			EditPart mySelectedElement = mySelectedElements[0];
			if (mySelectedElement instanceof TigerstripeAttributeEditPart
					|| mySelectedElement instanceof TigerstripeMethodEditPart
					|| mySelectedElement instanceof TigerstripeLiteralEditPart) {
				if (editorPart != null) {
					ArtifactEditorBase artEditor = (ArtifactEditorBase) editorPart;
					artEditor.setActivePage("ossj.entity.overview", artEditor);
					// now, get the active page and loop through the parts of
					// that page
					OssjArtifactOverviewPage selectedPage = (OssjArtifactOverviewPage) artEditor
							.getSelectedPage();
					IFormPart[] formParts = selectedPage.getManagedForm()
							.getParts();
					for (int j = 0; j < formParts.length; j++) {
						if (mySelectedElement instanceof TigerstripeAttributeEditPart
								&& formParts[j] instanceof OssjArtifactAttributesSection) {
							Attribute thisAttribute = (Attribute) ((View) mySelectedElement
									.getModel()).getElement();
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
							for (int row = 0; row < table.getItemCount(); row++) {
								TableItem tableItem = table.getItem(row);
								IField rowField = (IField) tableItem.getData();
								if (thisAttribute.getName().equals(
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
						} else if (mySelectedElement instanceof TigerstripeMethodEditPart
								&& formParts[j] instanceof OssjArtifactMethodsSection) {
							Method thisMethod = (Method) ((View) mySelectedElement
									.getModel()).getElement();
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
						} else if (mySelectedElement instanceof TigerstripeLiteralEditPart
								&& formParts[j] instanceof OssjArtifactConstantsSection) {
							Literal thisLiteral = (Literal) ((View) mySelectedElement
									.getModel()).getElement();
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
							for (int row = 0; row < table.getItemCount(); row++) {
								TableItem tableItem = table.getItem(row);
								ILabel rowLabel = (ILabel) tableItem.getData();
								if (thisLiteral.getName().equals(
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
						}
					}
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		List<EditPart> selecteds = new ArrayList<EditPart>();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			for (Iterator iter = structuredSelection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof TigerstripeEditableLabelEditPart) {
					selecteds.add((EditPart) obj);
				}
			}
		}
		mySelectedElements = selecteds.toArray(new EditPart[selecteds.size()]);
		action.setEnabled(isEnabled());
	}

	/**
	 * 
	 */
	private boolean isEnabled() {
		return mySelectedElements.length != 0;
	}

	protected IAbstractArtifact getContainingArtifact() {
		try {
			ITigerstripeProject project = getCorrespondingTigerstripeProject();
			String fqn = null;

			EObject element = getContainingEObject();
			if (element instanceof QualifiedNamedElement) {
				QualifiedNamedElement qne = (QualifiedNamedElement) element;
				fqn = qne.getFullyQualifiedName();
			}

			if (fqn != null && fqn.length() != 0) {
				try {
					IArtifactManagerSession session = project
							.getArtifactManagerSession();
					IAbstractArtifact result = session
							.getArtifactByFullyQualifiedName(fqn);
					return result;
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	protected EObject getContainingEObject() {
		EObject element = null;
		EditPart mySelectedElement = mySelectedElements[0];
		if (mySelectedElement != null) {
			if (mySelectedElement.getParent().getModel() instanceof Node) {
				Node obj = (Node) mySelectedElement.getParent().getModel();
				element = obj.getElement();
			} else if (mySelectedElement.getParent().getModel() instanceof Edge) {
				Edge obj = (Edge) mySelectedElement.getParent().getModel();
				element = obj.getElement();
			}
		} else if (myTargetWorkbenchPart instanceof TigerstripeDiagramEditor) {
			TigerstripeDiagramEditor tsd = (TigerstripeDiagramEditor) myTargetWorkbenchPart;
			Diagram obj = (Diagram) tsd.getDiagramEditPart().getModel();
			element = obj.getElement();
		}

		return element;
	}

}
