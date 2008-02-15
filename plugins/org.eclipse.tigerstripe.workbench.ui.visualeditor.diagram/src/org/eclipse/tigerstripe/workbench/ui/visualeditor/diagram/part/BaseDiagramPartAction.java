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

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeEditableEntityEditPart;
import org.eclipse.ui.IWorkbenchPart;

public abstract class BaseDiagramPartAction {

	/**
	 * 
	 */
	protected EditPart[] mySelectedElements;

	/**
	 * 
	 */
	private Shell myShell;

	protected IWorkbenchPart myTargetWorkbenchPart;

	protected Shell getShell() {
		return this.myShell;
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		myShell = targetPart.getSite().getShell();
		myTargetWorkbenchPart = targetPart;
	}

	protected Map getMap() {
		if (mySelectedElements[0] != null
				&& mySelectedElements[0].getParent() != null)
			return (Map) ((View) mySelectedElements[0].getParent().getModel())
					.getElement();
		return null;
	}

	protected ITigerstripeModelProject getCorrespondingTigerstripeProject()
			throws TigerstripeException {
		DiagramGraphicalViewer viewer = null;
		if (mySelectedElements.length != 0 && mySelectedElements[0] != null) {
			if (mySelectedElements[0] instanceof ShapeNodeEditPart) {
				viewer = (DiagramGraphicalViewer) mySelectedElements[0]
						.getParent().getParent().getViewer();
			} else if (mySelectedElements[0] instanceof CompartmentEditPart) {
				viewer = (DiagramGraphicalViewer) mySelectedElements[0]
						.getParent().getParent().getParent().getViewer();
			} else {
				viewer = (DiagramGraphicalViewer) mySelectedElements[0]
						.getParent().getViewer();
			}
		} else if (myTargetWorkbenchPart instanceof TigerstripeDiagramEditor) {
			TigerstripeDiagramEditor tsd = (TigerstripeDiagramEditor) myTargetWorkbenchPart;
			viewer = (DiagramGraphicalViewer) tsd.getDiagramGraphicalViewer();
		}

		if (viewer == null)
			throw new TigerstripeException(
					"Couldn't determine corresponding Tigerstripe Project: ");

		DiagramEditDomain domain = (DiagramEditDomain) viewer.getEditDomain();

		IResource res = (IResource) domain.getEditorPart().getEditorInput()
				.getAdapter(IResource.class);

		IAbstractTigerstripeProject project = EclipsePlugin
				.getITigerstripeProjectFor(res.getProject());

		// domain.getCommandStack().execute(
		// new ICommandProxy(new RefreshEditPartCommand(mySelectedElement,
		// true)));
		if (project instanceof ITigerstripeModelProject)
			return (ITigerstripeModelProject) project;
		return null;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		mySelectedElements = new EditPart[0];
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() > 0) {
				List<EditPart> selectedParts = new ArrayList<EditPart>();
				for (Iterator iter = structuredSelection.iterator(); iter
						.hasNext();) {
					Object elem = iter.next();
					if (elem instanceof TigerstripeEditableEntityEditPart) {
						selectedParts.add((EditPart) elem);
					}
				}
				mySelectedElements = selectedParts
						.toArray(new EditPart[selectedParts.size()]);
			}
		}
		action.setEnabled(isEnabled());
	}

	/**
	 * 
	 */
	private boolean isEnabled() {
		return mySelectedElements != null && mySelectedElements.length != 0;
	}

	protected EObject[] getCorrespondingEObjects() {
		List<EObject> result = new ArrayList<EObject>();
		if (mySelectedElements.length != 0) {
			for (EditPart mySelectedElement : mySelectedElements) {
				if (mySelectedElement.getModel() instanceof Node) {
					Node obj = (Node) mySelectedElement.getModel();
					result.add(obj.getElement());
				} else if (mySelectedElement.getModel() instanceof Edge) {
					Edge obj = (Edge) mySelectedElement.getModel();
					result.add(obj.getElement());
				}
			}
		} else if (myTargetWorkbenchPart instanceof TigerstripeDiagramEditor) {
			TigerstripeDiagramEditor tsd = (TigerstripeDiagramEditor) myTargetWorkbenchPart;
			Diagram obj = (Diagram) tsd.getDiagramEditPart().getModel();
			result.add(obj.getElement());
		}

		return result.toArray(new EObject[result.size()]);
	}

	protected IModelComponent[] getCorrespondingModelComponents() {
		List<IModelComponent> components = new ArrayList<IModelComponent>();
		ITigerstripeModelProject project = null;
		try {
			project = getCorrespondingTigerstripeProject();
		} catch (TigerstripeException e) {
			return new IModelComponent[0];
		}

		EObject[] elements = getCorrespondingEObjects();
		for (EObject element : elements) {
			if (element instanceof QualifiedNamedElement) {
				try {
					QualifiedNamedElement qne = (QualifiedNamedElement) element;
					IAbstractArtifact artifact = getArtifact(project, qne);
					components.add(artifact);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (element instanceof Attribute) {
				Attribute attr = (Attribute) element;
				if (attr.eContainer() instanceof QualifiedNamedElement) {
					try {
						QualifiedNamedElement qne = (QualifiedNamedElement) attr
								.eContainer();
						IAbstractArtifact artifact = getArtifact(project, qne);
						String attrName = attr.getName();
						for (IField field : artifact.getFields()) {
							if (field.getName().equals(attrName)) {
								components.add(field);
							}
						}
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (element instanceof Method) {
				Method mthod = (Method) element;
				try {
					QualifiedNamedElement qne = (QualifiedNamedElement) mthod
							.eContainer();
					IAbstractArtifact artifact = getArtifact(project, qne);
					String methName = mthod.getName();
					for (IMethod method : artifact.getMethods()) {
						if (method.getName().equals(methName)) {
							components.add(method);
						}
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (element instanceof Literal) {
				Literal lit = (Literal) element;
				try {
					QualifiedNamedElement qne = (QualifiedNamedElement) lit
							.eContainer();
					IAbstractArtifact artifact = getArtifact(project, qne);
					String labelName = lit.getName();
					for (ILiteral literal : artifact.getLiterals()) {
						if (literal.getName().equals(labelName)) {
							components.add(literal);
						}
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}

		return components.toArray(new IModelComponent[components.size()]);
	}

	private IAbstractArtifact getArtifact(ITigerstripeModelProject project,
			QualifiedNamedElement element) throws TigerstripeException {
		String fqn = element.getFullyQualifiedName();
		if (fqn != null && fqn.length() != 0) {
			IArtifactManagerSession session = project
					.getArtifactManagerSession();
			IAbstractArtifact result = session
					.getArtifactByFullyQualifiedName(fqn);
			if (result != null)
				return result;
		}
		throw new TigerstripeException("No FQN for QNE:" + element);
	}

	/**
	 * Returns true if the selection contains only artifacts
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	protected boolean selectionIsAllArtifacts() throws TigerstripeException {
		return getCorrespondingArtifacts().length != 0
				&& (getCorrespondingArtifacts().length == getCorrespondingModelComponents().length);
	}

	protected IAbstractArtifact[] getCorrespondingArtifacts() {
		List<IAbstractArtifact> arts = new ArrayList<IAbstractArtifact>();

		IModelComponent[] comps = getCorrespondingModelComponents();
		for (IModelComponent comp : comps) {
			if (comp instanceof IAbstractArtifact) {
				arts.add((IAbstractArtifact) comp);
			}
		}
		return arts.toArray(new IAbstractArtifact[arts.size()]);
	}

	public IWorkbenchPart getMyTargetWorkbenchPart() {
		return myTargetWorkbenchPart;
	}
}
