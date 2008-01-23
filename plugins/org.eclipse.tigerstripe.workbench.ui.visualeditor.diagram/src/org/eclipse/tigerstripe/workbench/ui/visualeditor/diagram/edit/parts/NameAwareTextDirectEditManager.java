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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts;

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.tools.TextDirectEditManager;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditor;

/**
 * This is an extension of a TextDirectEditManager that is used to verify that
 * the new name of any artifact is not conflicting with an existing name in the
 * model.
 * 
 * This is used by all Artifacts
 * 
 * @author Eric Dillon
 * 
 */
public class NameAwareTextDirectEditManager extends TextDirectEditManager {

	public NameAwareTextDirectEditManager(ITextAwareEditPart arg0) {
		super(arg0);
	}

	public NameAwareTextDirectEditManager(GraphicalEditPart arg0, Class arg1,
			CellEditorLocator arg2) {
		super(arg0, arg1, arg2);
	}

	@Override
	protected void commit() {

		// Get a hold of the tigerstripe model in context

		GraphicalEditPart part = this.getEditPart();
		CellEditor cellEditor = this.getCellEditor();
		final String newName = (String) cellEditor.getValue();

		View view = (View) part.getModel();
		QualifiedNamedElement element = (QualifiedNamedElement) view
				.getElement();

		String packageName = element.getPackage();

		String targetFQN = newName;
		if (packageName != null && packageName.length() != 0) {
			targetFQN = packageName + "." + newName;
		}

		DiagramGraphicalViewer viewer = (DiagramGraphicalViewer) part
				.getParent().getViewer();
		DiagramEditDomain domain = (DiagramEditDomain) viewer.getEditDomain();

		IResource res = (IResource) domain.getEditorPart().getEditorInput()
				.getAdapter(IResource.class);

		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getITigerstripeProjectFor(res.getProject());

		if (aProject instanceof ITigerstripeProject) {
			ITigerstripeProject project = (ITigerstripeProject) aProject;
			try {
				IArtifactManagerSession session = project
						.getArtifactManagerSession();
				IArtifactQuery allArtifactsQuery = session
						.makeQuery(IQueryAllArtifacts.class.getName());
				Collection<IAbstractArtifact> artifacts = session
						.queryArtifact(allArtifactsQuery);
				for (IAbstractArtifact artifact : artifacts) {
					if (artifact.getFullyQualifiedName().equalsIgnoreCase(
							targetFQN)) {
						bringDown();
						return;
					}
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		// We need special logic for the AssociationClassClass so that upon
		// rename, both Parts are updated.
		if (part instanceof AssociationClassClassNamePackageEditPart) {
			AssociationClassClassEditPart parent = (AssociationClassClassEditPart) part
					.getParent();
			AssociationClassEditPart assocClassPart = parent
					.getAssociatedEditPart();

			if (assocClassPart != null) {
				final AssociationClass assocClass = (AssociationClass) assocClassPart
						.resolveSemanticElement();
				Command cmd = new AbstractCommand() {

					@Override
					public boolean canExecute() {
						return true;
					}

					public void execute() {
						assocClass.setName(newName);
					}

					public void redo() {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean canUndo() {
						return false;
					}

				};
				try {
					BaseETAdapter.setIgnoreNotify(true);
					DiagramEditDomain aa = (DiagramEditDomain) viewer
							.getEditDomain();
					TigerstripeDiagramEditor editor = (TigerstripeDiagramEditor) aa
							.getEditorPart();
					TransactionalEditingDomain editingDomain = editor
							.getEditingDomain();
					editingDomain.getCommandStack().execute(cmd);
					editingDomain.getCommandStack().flush();
				} finally {
					BaseETAdapter.setIgnoreNotify(false);
				}

			}
		}
		super.commit();
	}

}
