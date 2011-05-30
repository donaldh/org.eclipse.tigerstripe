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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.dialogs.ManageLinksDialog;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.dialogs.ManageLinksDialog.LinkEntry;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.dialogs.ManageLinksDialog.LinksSet;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.utils.ArtifactPropertyChangeHandler;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.VisualeditorRelationshipUtils;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ManageLinksAction extends BaseDiagramPartAction implements
		IObjectActionDelegate {

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);
		// ensure that the action is enabled, no matter what
		try {
			action.setEnabled(mySelectedElements.length == 1
					&& selectionIsAllArtifacts()
					|| mySelectedElements.length == 0);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			action.setEnabled(false);
		}
	}

	public void run(IAction action) {
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();
		Collection<AbstractArtifact> selectedArtifacts = null;
		IAbstractArtifact artifact = null;
		if (mySelectedElements.length != 0) {
			IAbstractArtifact[] artifacts = getCorrespondingArtifacts();
			artifact = artifacts[0];

			selectedArtifacts = new ArrayList<AbstractArtifact>();
			for (EObject eObject : getCorrespondingEObjects()) {
				if (eObject instanceof AbstractArtifact) {
					selectedArtifacts.add((AbstractArtifact) eObject);
				}
			}
		}
		Object[] returnObjects = null;
		Map map = null;
		if (mySelectedElements.length != 0) {
			// if here, selection was an artifact, so build list of
			// relationships
			// from/to that artifact to/from the other artifacts in the diagram
			EObject[] elements = getCorrespondingEObjects();
			map = getMap();
			try {
				ITigerstripeModelProject project = getCorrespondingTigerstripeProject();
				returnObjects = VisualeditorRelationshipUtils
						.getPossibleRelationshipsForArtifact(project, map,
								artifact);
			} catch (TigerstripeException e) {
				return;
			}
		} else {
			IWorkbenchPart wbp = getMyTargetWorkbenchPart();
			if (wbp instanceof TigerstripeDiagramEditor) {
				// else selection was the map itself, , so build list of
				// all of the relationships that can be build between any of
				// the artifacts in the diagram
				TigerstripeDiagramEditor tsd = (TigerstripeDiagramEditor) wbp;
				Diagram obj = (Diagram) tsd.getDiagramEditPart().getModel();
				map = (Map) obj.getElement();
				try {
					ITigerstripeModelProject project = getCorrespondingTigerstripeProject();
					returnObjects = VisualeditorRelationshipUtils
							.getPossibleRelationshipsForMap(project, map);
				} catch (TigerstripeException e) {
					return;
				}
			}
		}

		UpdatableLinksSet[] linkSets = collectLinksSets(map, selectedArtifacts,
				returnObjects);
		boolean hasLinks = false;
		for (UpdatableLinksSet linksSet : linkSets) {
			if (linksSet.hasLinks()) {
				hasLinks = true;
			}
		}
		if (hasLinks) {
			ManageLinksDialog diag = new ManageLinksDialog(shell, linkSets);
			if (diag.open() == Window.OK) {
				CompoundCommand cmd = new CompoundCommand();
				for (UpdatableLinksSet linksSet : linkSets) {
					Command command = linksSet.getUpdateCommand();
					if (command != null) {
						cmd.append(command);
					}
				}

				TigerstripeDiagramEditor editor = null;
				if (mySelectedElements.length != 0
						&& mySelectedElements[0] != null) {
					DiagramGraphicalViewer viewer = (DiagramGraphicalViewer) mySelectedElements[0]
							.getParent().getViewer();
					DiagramEditDomain domain = (DiagramEditDomain) viewer
							.getEditDomain();
					editor = (TigerstripeDiagramEditor) domain.getEditorPart();
				} else {
					editor = (TigerstripeDiagramEditor) getMyTargetWorkbenchPart();
				}

				try {
					BaseETAdapter.setIgnoreNotify(true);
					TransactionalEditingDomain editingDomain = editor
							.getEditingDomain();
					editingDomain.getCommandStack().execute(cmd);
					editingDomain.getCommandStack().flush();
				} finally {
					BaseETAdapter.setIgnoreNotify(false);
				}
			}
		} else {
			// if here, there were no relationships that could be built, so
			// display a warning to the user
			String warningMessage = null;
			if (artifact != null) {
				warningMessage = "No relationships found to/from selected artifact\n"
						+ "(" + artifact.getFullyQualifiedName() + ")";
			} else {
				warningMessage = "No relationships found between artifacts in "
						+ "the current diagram";
			}
			MessageDialog.openWarning(shell, "No Relationship Found",
					warningMessage);
		}
	}

	@SuppressWarnings("unchecked")
	private UpdatableLinksSet[] collectLinksSets(Map map,
			Collection<AbstractArtifact> selectedArtifacts,
			Object[] returnObjects) {
		List<UpdatableLinksSet> linksSets = new ArrayList<UpdatableLinksSet>();
		linksSets.add(getRelationshipsLinksSet(returnObjects, map));

		Collection<AbstractArtifact> toHandle = null;
		if (selectedArtifacts == null || selectedArtifacts.size() == 0) {
			toHandle = map.getArtifacts();
		} else {
			toHandle = selectedArtifacts;
		}
		linksSets.addAll(Arrays.asList(getExtendsImplementsLinksSet(toHandle)));

		return linksSets.toArray(new UpdatableLinksSet[linksSets.size()]);
	}

	@SuppressWarnings("unchecked")
	private UpdatableLinksSet getRelationshipsLinksSet(Object[] returnObjects,
			final Map map) {
		Set<IRelationship> possibleRelationships = null;
		HashMap<String, QualifiedNamedElement> associationsInMap = null;
		HashMap<String, QualifiedNamedElement> dependenciesInMap = null;
		HashMap<String, QualifiedNamedElement> nodes = new HashMap<String, QualifiedNamedElement>();
		if (returnObjects != null && returnObjects.length == 4) {
			// extract the returned values from the object array
			possibleRelationships = (Set<IRelationship>) returnObjects[0];
			associationsInMap = (HashMap<String, QualifiedNamedElement>) returnObjects[1];
			dependenciesInMap = (HashMap<String, QualifiedNamedElement>) returnObjects[2];
			nodes = (HashMap<String, QualifiedNamedElement>) returnObjects[3];
		}

		final HashMap<String, QualifiedNamedElement> nodesInMap = nodes;

		final List<LinkEntry> relEntries = new ArrayList<LinkEntry>();
		if (possibleRelationships != null) {
			for (IRelationship relationship : possibleRelationships) {
				QualifiedNamedElement element = associationsInMap
						.get(relationship.getFullyQualifiedName());
				if (element == null) {
					element = dependenciesInMap.get(relationship
							.getFullyQualifiedName());
				}
				RelationshipLinkEntry entry = new RelationshipLinkEntry(
						relationship, element);
				relEntries.add(entry);
			}
		}

		return new UpdatableLinksSet("Associations", new String[] { "Name",
				"A End", "Z End" }, relEntries) {
			@Override
			public Command getUpdateCommand() {
				List<IRelationship> relationshipsToCreate = new ArrayList<IRelationship>();
				List<QualifiedNamedElement> associationsToDestroy = new ArrayList<QualifiedNamedElement>();
				List<QualifiedNamedElement> dependenciesToDestroy = new ArrayList<QualifiedNamedElement>();
				for (LinkEntry entry : relEntries) {
					RelationshipLinkEntry relEntry = (RelationshipLinkEntry) entry;
					if (relEntry.isEnabled() && !relEntry.isExists()) {
						relationshipsToCreate.add(relEntry.getRelationship());
					} else if (!relEntry.isEnabled() && relEntry.isExists()) {
						QualifiedNamedElement element = relEntry.getElement();
						if (element instanceof Association) {
							associationsToDestroy.add(element);
						} else if (element instanceof Dependency) {
							dependenciesToDestroy.add(element);
						}
					}
				}

				return new UpdateRelationshipsCommand(map, nodesInMap,
						relationshipsToCreate, associationsToDestroy,
						dependenciesToDestroy);
			}

		};

	}

	private UpdatableLinksSet[] getExtendsImplementsLinksSet(
			Collection<AbstractArtifact> artifacts) {
		List<LinkEntry> extendsEntries = new ArrayList<LinkEntry>();
		List<LinkEntry> implementsEntries = new ArrayList<LinkEntry>();
		for (AbstractArtifact art : artifacts) {
			boolean hideExtends = Boolean
					.parseBoolean(NamedElementPropertiesHelper.getProperty(art,
							NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS));
			AbstractArtifact extended = null;
			if (!hideExtends) {
				extended = art.getExtends();
			} else {
				extended = getExtendedFromDiagram(art);
			}
			if (extended != null) {
				extendsEntries.add(new ExtendsLinkEntry(art, extended));
			}

			boolean hideImplements = Boolean
					.parseBoolean(NamedElementPropertiesHelper
							.getProperty(
									art,
									NamedElementPropertiesHelper.ARTIFACT_HIDE_IMPLEMENTS));
			List<AbstractArtifact> implemented = null;
			if (!hideImplements) {
				implemented = art.getImplements();
			} else {
				implemented = getImplementedFromDiagram(art);
			}

			if (implemented != null && implemented.size() > 0) {
				implementsEntries
						.add(new ImplementsLinkEntry(art, implemented));
			}
		}
		UpdatableLinksSet extendsLinkSet = new UpdatableLinksSet("Extends",
				new String[] { "Source", "Extends" }, extendsEntries) {
			@Override
			public Command getUpdateCommand() {
				return new UpdateExtendsImplementsVisibilityCommand(
						this.getLinkEntries(),
						NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS);
			}

		};
		UpdatableLinksSet implementsLinkSet = new UpdatableLinksSet(
				"Implements", new String[] { "Source", "Implements" },
				implementsEntries) {
			@Override
			public Command getUpdateCommand() {
				return new UpdateExtendsImplementsVisibilityCommand(
						this.getLinkEntries(),
						NamedElementPropertiesHelper.ARTIFACT_HIDE_IMPLEMENTS);
			}
		};

		return new UpdatableLinksSet[] { extendsLinkSet, implementsLinkSet };
	}

	private AbstractArtifact getExtendedFromDiagram(AbstractArtifact artifact) {
		AbstractArtifact extended = artifact.getExtends();
		if (extended == null) {
			try {
				Map map = (Map) artifact.eContainer();
				MapHelper mapHelper = new MapHelper(map);
				IAbstractArtifact iArtifact = mapHelper
						.getIArtifactFor(artifact);
				IAbstractArtifact extendedIArtifact = iArtifact
						.getExtendedArtifact();
				if (extendedIArtifact != null) {
					extended = mapHelper
							.findAbstractArtifactFor(extendedIArtifact);
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		return extended;
	}

	@SuppressWarnings("unchecked")
	private List<AbstractArtifact> getImplementedFromDiagram(
			AbstractArtifact artifact) {
		List<AbstractArtifact> implemented = new ArrayList<AbstractArtifact>();
		try {
			Map map = (Map) artifact.eContainer();
			MapHelper mapHelper = new MapHelper(map);
			IAbstractArtifact iArtifact = mapHelper.getIArtifactFor(artifact);
			Collection<IAbstractArtifact> implementedIArtifacts = iArtifact
					.getImplementedArtifacts();
			for (IAbstractArtifact implementedIArtifact : implementedIArtifacts) {
				implemented.add(mapHelper
						.findAbstractArtifactFor(implementedIArtifact));
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return implemented;
	}

	private class UpdateExtendsImplementsVisibilityCommand extends
			AbstractCommand {

		private final Collection<LinkEntry> entries;
		private final String propertyKey;

		public UpdateExtendsImplementsVisibilityCommand(
				Collection<LinkEntry> entries, String propertyKey) {
			this.entries = entries;
			this.propertyKey = propertyKey;
		}

		@Override
		public boolean canExecute() {
			return true;
		}

		@Override
		public boolean canUndo() {
			return false;
		}

		public void execute() {
			for (LinkEntry entry : entries) {
				BaseArtifactLinkEntry artifactEntry = (BaseArtifactLinkEntry) entry;
				if (entry.isEnabled() && !entry.isExists()) {
					updateProperty(artifactEntry.getArtifact(), propertyKey,
							Boolean.TRUE.toString(), Boolean.FALSE.toString());
				} else if (!artifactEntry.isEnabled()
						&& artifactEntry.isExists()) {
					updateProperty(artifactEntry.getArtifact(), propertyKey,
							Boolean.FALSE.toString(), Boolean.TRUE.toString());
				}
			}
		}

		public void redo() {
		}

		private void updateProperty(AbstractArtifact artifact,
				String propertyKey, String oldValue, String newValue) {
			NamedElementPropertiesHelper.setProperty(artifact, propertyKey,
					newValue);
			ArtifactPropertyChangeHandler handler = new ArtifactPropertyChangeHandler(
					artifact);
			handler.handleArtifactPropertyChange(propertyKey, oldValue,
					newValue);
		}
	}

	private abstract class UpdatableLinksSet extends LinksSet {
		public UpdatableLinksSet(String name, String[] columnNames,
				Collection<LinkEntry> entries) {
			super(name, columnNames, entries);
		}

		public abstract Command getUpdateCommand();
	}

	/**
	 * inner class, this class is the command that is used to create new
	 * relationships (associations, association classes, or dependencies) in the
	 * map or to remove existing relationships from the map. The relationships
	 * to create or relationships to destroy are passed into this command using
	 * it's constructor. The creation/destruction of the EObjects that represent
	 * those relationships occurs in this object's "execute()" method.
	 * 
	 * @author tjmcs
	 * 
	 */
	private class UpdateRelationshipsCommand extends AbstractCommand {

		Map map;

		List<IRelationship> relationshipsToCreate;

		List<QualifiedNamedElement> associationsToDestroy;

		List<QualifiedNamedElement> dependenciesToDestroy;

		HashMap<String, QualifiedNamedElement> nodesInMap;

		List<QualifiedNamedElement> associationsAdded = new ArrayList<QualifiedNamedElement>();

		List<QualifiedNamedElement> dependenciesAdded = new ArrayList<QualifiedNamedElement>();

		public UpdateRelationshipsCommand(Map map,
				HashMap<String, QualifiedNamedElement> nodesInMap,
				List<IRelationship> relationshipsToCreate,
				List<QualifiedNamedElement> associationsToDestroy,
				List<QualifiedNamedElement> dependenciesToDestroy) {
			this.map = map;
			this.nodesInMap = nodesInMap;
			this.relationshipsToCreate = relationshipsToCreate;
			this.associationsToDestroy = associationsToDestroy;
			this.dependenciesToDestroy = dependenciesToDestroy;
		}

		@Override
		public boolean canExecute() {
			return true;
		}

		public void execute() {
			associationsAdded.clear();
			dependenciesAdded.clear();
			for (IRelationship relationship : relationshipsToCreate) {
				if (relationship instanceof IAssociationClassArtifact) {
					IAssociationClassArtifact assocClassArtifact = (IAssociationClassArtifact) relationship;
					AssociationClass assocClass = VisualeditorFactory.eINSTANCE
							.createAssociationClass();
					AssociationClassClass assocClassClass = VisualeditorFactory.eINSTANCE
							.createAssociationClassClass();
					assocClass.setAssociatedClass(assocClassClass);
					// it's critical that the assocClassClass instance be added
					// to the map before we try to
					// initialize the association class, otherwise the call to
					// initializeAssociationClass will
					// fail with a NullPointerException if an enumeration is
					// included as one (or more) of the
					// fields in the AssociationClass iArtifact
					map.getArtifacts().add(assocClassClass);
					VisualeditorRelationshipUtils.initializeAssociationClass(
							assocClass, assocClassArtifact, nodesInMap);
					map.getAssociations().add(assocClass);
					associationsAdded.add(assocClass);
				} else if (relationship instanceof IAssociationArtifact) {
					IAssociationArtifact assocArtifact = (IAssociationArtifact) relationship;
					Association assoc = VisualeditorFactory.eINSTANCE
							.createAssociation();
					VisualeditorRelationshipUtils.initializeAssociation(assoc,
							assocArtifact, nodesInMap);
					map.getAssociations().add(assoc);
					associationsAdded.add(assoc);
				} else if (relationship instanceof IDependencyArtifact) {
					IDependencyArtifact depArtifact = (IDependencyArtifact) relationship;
					Dependency dependency = VisualeditorFactory.eINSTANCE
							.createDependency();
					VisualeditorRelationshipUtils.initializeDependency(
							dependency, depArtifact, nodesInMap);
					map.getDependencies().add(dependency);
					dependenciesAdded.add(dependency);
				}
			}
			map.getAssociations().removeAll(associationsToDestroy);
			map.getDependencies().removeAll(dependenciesToDestroy);
		}

		public void redo() {
			map.getAssociations().addAll(associationsAdded);
			map.getDependencies().addAll(dependenciesAdded);
			map.getAssociations().removeAll(associationsToDestroy);
			map.getDependencies().removeAll(dependenciesToDestroy);
		}

		@Override
		public void undo() {
			map.getAssociations().removeAll(associationsAdded);
			map.getDependencies().removeAll(dependenciesAdded);
			map.getAssociations().addAll(associationsToDestroy);
			map.getDependencies().addAll(dependenciesToDestroy);
		}

		@Override
		public boolean canUndo() {
			return true;
		}
	}

	private class RelationshipLinkEntry extends LinkEntry {
		private final IRelationship relationship;
		private final QualifiedNamedElement element;

		public RelationshipLinkEntry(IRelationship relationship,
				QualifiedNamedElement element) {
			this.relationship = relationship;
			this.element = element;
			setEnabled(isExists());
		}

		public IRelationship getRelationship() {
			return relationship;
		}

		public QualifiedNamedElement getElement() {
			return element;
		}

		@Override
		public boolean isExists() {
			return element != null;
		}

		@Override
		public String getLabel(boolean fullName, int column) {
			String result = null;
			switch (column) {
			case 0:
				if (fullName) {
					result = relationship.getFullyQualifiedName();
				} else {
					result = relationship.getName();
				}
				break;
			case 1:
				result = getEndName(fullName,
						relationship.getRelationshipAEnd());
				break;
			case 2:
				result = getEndName(fullName,
						relationship.getRelationshipZEnd());
				break;
			}
			return result;
		}

		private String getEndName(boolean fullName, IRelationshipEnd end) {
			if (fullName) {
				return end.getType().getFullyQualifiedName();
			} else {
				return end.getType().getName();
			}
		}
	}

	private abstract class BaseArtifactLinkEntry extends LinkEntry {

		private final AbstractArtifact artifact;

		public BaseArtifactLinkEntry(AbstractArtifact artifact) {
			this.artifact = artifact;
		}

		public AbstractArtifact getArtifact() {
			return artifact;
		}

		protected String getName(AbstractArtifact art, boolean fullNames) {
			if (fullNames) {
				return art.getFullyQualifiedName();
			} else {
				return art.getName();
			}
		}
	}

	private class ExtendsLinkEntry extends BaseArtifactLinkEntry {
		private final AbstractArtifact extended;

		public ExtendsLinkEntry(AbstractArtifact artifact,
				AbstractArtifact extended) {
			super(artifact);
			this.extended = extended;
			setEnabled(isExists());
		}

		@Override
		public boolean isExists() {
			return !Boolean
					.parseBoolean(NamedElementPropertiesHelper.getProperty(
							getArtifact(),
							NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS));
		}

		@Override
		public String getLabel(boolean fullNames, int column) {
			String result = null;
			if (column == 0) {
				result = getName(getArtifact(), fullNames);
			} else if (column == 1) {
				result = getName(extended, fullNames);
			}
			return result;
		}
	}

	private class ImplementsLinkEntry extends BaseArtifactLinkEntry {
		private final List<AbstractArtifact> implemented;

		public ImplementsLinkEntry(AbstractArtifact artifact,
				List<AbstractArtifact> implemented) {
			super(artifact);
			this.implemented = implemented;
			setEnabled(isExists());
		}

		@Override
		public boolean isExists() {
			return !Boolean
					.parseBoolean(NamedElementPropertiesHelper
							.getProperty(
									getArtifact(),
									NamedElementPropertiesHelper.ARTIFACT_HIDE_IMPLEMENTS));
		}

		@Override
		public String getLabel(boolean fullNames, int column) {
			String result = null;
			if (column == 0) {
				result = getName(getArtifact(), fullNames);
			} else if (column == 1) {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < implemented.size(); i++) {
					if (i != 0) {
						builder.append(", ");
					}
					builder.append(getName(implemented.get(i), fullNames));
				}
				result = builder.toString();
			}
			return result;
		}
	}
}
