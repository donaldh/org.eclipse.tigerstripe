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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.gmf.TigerstripeShapeNodeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.BaseDiagramPartAction;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;
import org.eclipse.ui.IObjectActionDelegate;

public abstract class ArtifactToggleAction extends BaseDiagramPartAction
		implements IObjectActionDelegate {

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

		List<EditPart> selecteds = new ArrayList<EditPart>();
		mySelectedElements = new EditPart[0];
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			for (Iterator iter = structuredSelection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof TigerstripeShapeNodeEditPart) {
					selecteds.add((EditPart) obj);
				}
			}
		}
		mySelectedElements = selecteds.toArray(new EditPart[selecteds.size()]);
		computeEnabledAndChecked(action);
	}

	protected void computeEnabledAndChecked(IAction action) {
		boolean allShapeNodeEditParts = true;
		boolean allChecked = true;
		for (EditPart mySelectedElement : mySelectedElements) {
			if (!(mySelectedElement instanceof TigerstripeShapeNodeEditPart)) {
				allShapeNodeEditParts = false;
				break;
			} else {
				allChecked = allChecked
						& Boolean.parseBoolean(getHelper(mySelectedElement)
								.getProperty(getTargetProperty()));
			}
		}

		action.setChecked(allChecked);
		action.setEnabled(allShapeNodeEditParts);
	}

	private NamedElementPropertiesHelper getHelper(EditPart mySelectedElement) {
		TigerstripeShapeNodeEditPart shapeNodePart = (TigerstripeShapeNodeEditPart) mySelectedElement;
		AbstractArtifact art = (AbstractArtifact) ((Node) (shapeNodePart
				.getModel())).getElement();
		NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
				art);
		return helper;
	}

	public void run(IAction action) {

		TransactionalEditingDomain editDomain = ((TigerstripeShapeNodeEditPart) mySelectedElements[0])
				.getEditingDomain();
		final boolean isChecked = action.isChecked();
		Command cmd = new AbstractCommand() {
			@Override
			public boolean canExecute() {
				return true;
			}

			public void execute() {
				for (EditPart mySelectedElement : mySelectedElements) {
					String oldValue = getHelper(mySelectedElement).getProperty(
							getTargetProperty());
					String newValue = String.valueOf(isChecked);
					getHelper(mySelectedElement).setProperty(
							getTargetProperty(), newValue);
					TigerstripeShapeNodeEditPart shapeNodePart = (TigerstripeShapeNodeEditPart) mySelectedElement;
					shapeNodePart.artifactPropertyChanged(getTargetProperty(),
							oldValue, newValue);
				}
			}

			public void redo() {

			}

			@Override
			public boolean canUndo() {
				return false;
			}
		};

		try {
			BaseETAdapter.setIgnoreNotify(true);
			editDomain.getCommandStack().execute(cmd);
			editDomain.getCommandStack().flush();
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}

	}

	protected abstract String getTargetProperty();

	public AbstractArtifact[] getCorrespondingEArtifacts() {
		List<AbstractArtifact> result = new ArrayList<AbstractArtifact>();
		for (EditPart mySelectedElement : mySelectedElements) {
			if (mySelectedElement instanceof TigerstripeShapeNodeEditPart) {
				TigerstripeShapeNodeEditPart shape = (TigerstripeShapeNodeEditPart) mySelectedElement;
				result.add((AbstractArtifact) ((Node) shape.getModel())
						.getElement());
			}
		}

		return result.toArray(new AbstractArtifact[result.size()]);
	}

	public IAbstractArtifact[] getCorrespondingIArtifacts() {
		if (mySelectedElements.length != 0
				&& mySelectedElements[0] instanceof TigerstripeShapeNodeEditPart) {
			TigerstripeShapeNodeEditPart shape = (TigerstripeShapeNodeEditPart) mySelectedElements[0];
			MapEditPart mapEditPart = (MapEditPart) shape.getParent();
			Map map = (Map) ((Diagram) mapEditPart.getModel()).getElement();
			MapHelper mapHelper = new MapHelper(map);
			ITigerstripeProject tsProject = map
					.getCorrespondingITigerstripeProject();
			if (tsProject != null) {
				AbstractArtifact[] eArtifacts = getCorrespondingEArtifacts();
				List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
				for (AbstractArtifact eArtifact : eArtifacts) {
					if (eArtifact != null) {
						try {
							IArtifactManagerSession session = tsProject
									.getArtifactManagerSession();
							IAbstractArtifact iArtifact = session
									.getArtifactByFullyQualifiedName(eArtifact
											.getFullyQualifiedName());
							result.add(iArtifact);
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					}
				}
				return result.toArray(new IAbstractArtifact[result.size()]);
			}
		}
		return new IAbstractArtifact[0];
	}
}
