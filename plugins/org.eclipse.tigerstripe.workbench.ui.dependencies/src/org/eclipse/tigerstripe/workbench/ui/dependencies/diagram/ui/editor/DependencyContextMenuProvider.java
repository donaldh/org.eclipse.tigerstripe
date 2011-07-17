/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.editor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyAction;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.image.GraphicalViewerImageRenderer;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts.NoteEditPart;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts.SubjectEditPart;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.LayerDescriptor;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.ModelsFactory;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Registry;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.SubjectDescriptor;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Dimension;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Point;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;

public class DependencyContextMenuProvider extends ContextMenuProvider {

	/** The editor's action registry. */
	private ActionRegistry actionRegistry;
	private IWorkbenchPartSite site;

	/**
	 * Instantiate a new menu context provider for the specified EditPartViewer
	 * and ActionRegistry.
	 * 
	 * @param viewer
	 *            the editor's graphical viewer
	 * @param registry
	 *            the editor's action registry
	 * @throws IllegalArgumentException
	 *             if registry is <tt>null</tt>.
	 */
	public DependencyContextMenuProvider(IWorkbenchPartSite site,
			EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		if (registry == null) {
			throw new IllegalArgumentException();
		}
		actionRegistry = registry;
		this.site = site;
	}

	/**
	 * Called when the context menu is about to show. Actions, whose state is
	 * enabled, will appear in the context menu.
	 * 
	 * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	public void buildContextMenu(IMenuManager menu) {
		// Add standard action groups to the menu
		GEFActionConstants.addStandardActionGroups(menu);

		// Add actions to the menu
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, // target group id
				getAction(ActionFactory.UNDO.getId())); // action to add
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO,
				getAction(ActionFactory.REDO.getId()));

		menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new Action(
				"Save To File...") {

			@Override
			public void run() {
				GraphicalViewerImageRenderer.save(getMenu().getShell(),
						(GraphicalViewer) getViewer());
			}

		});

		menu.appendToGroup(GEFActionConstants.GROUP_VIEW,
				new Action("Go Into") {

					@Override
					public void run() {
						SubjectEditPart editPart = getSelectedSubjectEditPart();
						if (editPart == null) {
							return;
						}
						Registry registry = Utils.getService(getViewer(),
								Registry.class);
						
						LayerDescriptor layerInfo = registry.getLayerInfo(Utils.getDiagram(editPart).getCurrentLayer().getId());
						
						SubjectDescriptor subjectDescriptor = layerInfo.getLayerData().get(editPart
								.getSubject().getExternalId());
						
						if (subjectDescriptor == null) {
							return;
						}
						
						Layer layer = registry.initLayer(subjectDescriptor.getExternalSubject());
						Utils.switchToLayer(layer, getViewer());
					}

					@Override
					public boolean isEnabled() {

						SubjectEditPart editPart = getSelectedSubjectEditPart();
						if (editPart == null) {
							return false;
						}
						
						Diagram diagram = Utils.getDiagram(editPart);
						return !diagram.getCurrentLayer().getId()
								.equals(editPart.getSubject().getExternalId());
					}
				});

		menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new Action(
				"Back To Parent") {

			@Override
			public void run() {
				Utils.popLayer(getViewer());
			}

			@Override
			public boolean isEnabled() {
				Diagram diagram = Utils.getDiagram(getViewer()
						.getRootEditPart());
				return !diagram.getLayersHistory().isEmpty();
			}

		});

		// OpenAction openAction = new OpenAction(site);
		// CheckoutAction checkoutAction = new CheckoutAction(site);
		// menu.appendToGroup(GEFActionConstants.GROUP_VIEW, openAction);
		// menu.appendToGroup(GEFActionConstants.GROUP_VIEW, checkoutAction);

		menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new LayoutAction(
				getViewer().getContents()));
		MenuManager showInSubMenu = new MenuManager("Show In...");
		showInSubMenu.add(ContributionItemFactory.VIEWS_SHOW_IN.create(site
				.getWorkbenchWindow()));
		menu.appendToGroup(GEFActionConstants.GROUP_VIEW, showInSubMenu);
		menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new Action(
				"Add Note...") {
			@Override
			public void run() {
				SubjectEditPart subjectEditPart = getSelectedSubjectEditPart();
				if (subjectEditPart == null) {
					return;
				}
				Subject subject = (Subject) subjectEditPart.getModel();
				Note note = ModelsFactory.INSTANCE.createNote();

				final int shiftX = 10, shiftY = 10;

				Point location = subject.getLocation();
				Dimension size = subject.getSize();
				note.getLocation().setX(
						location.getX() + size.getWidth() + shiftX);
				note.getLocation().setY(
						location.getY() - note.getSize().getHeight() - shiftY);

				Utils.link(subject, note);
				Utils.getDiagram(subjectEditPart).getCurrentLayer().getShapes()
						.add(note);
				subjectEditPart.getParent().refresh();
				subjectEditPart.refresh();
			}

			@Override
			public boolean isEnabled() {
				return getSelectedSubjectEditPart() != null;
			}

		});

		final GraphicalEditPart selectedEditPart = getSelectedEditPart();

		if (selectedEditPart instanceof NoteEditPart) {
			menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new Action(
					"Remove Note") {
				@Override
				public void run() {
					NoteEditPart noteEditPart = (NoteEditPart) selectedEditPart;
					Note note = noteEditPart.getNote();

					note.getParentLayer().getShapes().remove(note);
					Set<Shape> toRefresh = new HashSet<Shape>();
					for (Connection con : note.getSourceConnections()) {
						Shape target = con.getTarget();
						toRefresh.add(target);
						target.getTargetConnections().remove(con);
					}
					Map<?, ?> partRegistry = noteEditPart.getViewer()
							.getEditPartRegistry();
					noteEditPart.getParent().refresh();
					for (Shape shape : toRefresh) {
						((GraphicalEditPart) partRegistry.get(shape)).refresh();
					}

				}

			});
		}

		SubjectEditPart subjectEditPart = getSelectedSubjectEditPart();
		if (subjectEditPart != null) {
			final IDependencySubject externalModel = Utils.getExternalModel(
					subjectEditPart.getSubject(), subjectEditPart.getViewer());
			if (externalModel != null) {
				for (final IDependencyAction action : externalModel.getType()
						.getActions()) {
					menu.appendToGroup(GEFActionConstants.GROUP_VIEW,
							new Action(action.getName()) {
								@Override
								public void run() {
									action.run(externalModel);
								}

								@Override
								public boolean isEnabled() {
									return action.isEnabled(externalModel);
								}

							});
				}
			}
		}

		menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new Action(
				"Kind Properties...") {
			@Override
			public void run() {
				KindPropertiesDialog kindPropertiesDialog = new KindPropertiesDialog(
						null, getKinds(), Utils.getRegistry(getViewer()));
				kindPropertiesDialog.open();
			}

			private EList<Kind> getKinds() {
				Diagram diagram = Utils.getDiagram(getViewer()
						.getRootEditPart());
				EList<Kind> kinds = diagram.getKinds();
				return kinds;
			}

			@Override
			public boolean isEnabled() {
				return !getKinds().isEmpty();
			}
		});

		menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new Action(
				"Diagram Style...") {

			@Override
			public void run() {
				Diagram diagram = Utils.getDiagram(getViewer()
						.getRootEditPart());
				new DiagramPropertiesDialog(null, diagram).open();
			}
		});
	}

	private GraphicalEditPart getSelectedEditPart() {
		Iterator<?> it = getViewer().getSelectedEditParts().iterator();
		if (it.hasNext()) {
			return (GraphicalEditPart) it.next();
		}
		return null;
	}

	private SubjectEditPart getSelectedSubjectEditPart() {
		Iterator<?> it = getViewer().getSelectedEditParts().iterator();
		if (it.hasNext()) {
			Object next = it.next();
			if (next instanceof SubjectEditPart) {
				return (SubjectEditPart) next;
			}
		}
		return null;
	}

	private IAction getAction(String actionId) {
		return actionRegistry.getAction(actionId);
	}

}
