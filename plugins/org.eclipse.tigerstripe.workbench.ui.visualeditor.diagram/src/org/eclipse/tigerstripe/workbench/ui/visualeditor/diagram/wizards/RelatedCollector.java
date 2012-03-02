package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.wizards;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class RelatedCollector {

	private final Set<String> namesOfArtifactsInMap;
	private final IAbstractArtifact[] artifacts;
	private final ITigerstripeModelProject tsProject;
	
	private Set<IAbstractArtifact> extendedArtifacts;
	private Set<IAbstractArtifact> extendingArtifacts;
	private Set<IAbstractArtifact> associatedArtifacts;
	private Set<IAbstractArtifact> dependentArtifacts;
	private Set<IAbstractArtifact> associatingArtifacts;
	private Set<IAbstractArtifact> dependingArtifacts;
	private Set<IAbstractArtifact> implementedArtifacts;
	private Set<IAbstractArtifact> implementingArtifacts;
	private Set<IAbstractArtifact> referencedArtifacts;
	private Set<IAbstractArtifact> referencingArtifacts;
	private boolean relationshipsExist;

	public RelatedCollector(Set<String> namesOfArtifactsInMap,
			IAbstractArtifact[] artifacts, ITigerstripeModelProject tsProject) {
		this.namesOfArtifactsInMap = namesOfArtifactsInMap;
		this.artifacts = artifacts;
		this.tsProject = tsProject;
	}

	public BitSet prepare() throws TigerstripeException {
		IArtifactManagerSession session = tsProject.getArtifactManagerSession();
		relationshipsExist = false;

		extendedArtifacts = new HashSet<IAbstractArtifact>();
		for (IAbstractArtifact artifact : artifacts) {
			IAbstractArtifact extendedArtifact = artifact
					.getExtendedArtifact();
			if (extendedArtifact != null
					&& !namesOfArtifactsInMap.contains(extendedArtifact
							.getFullyQualifiedName())) {
				extendedArtifacts.add(extendedArtifact);
				relationshipsExist = true;
			}
		}

		extendingArtifacts = new HashSet<IAbstractArtifact>();
		for (IAbstractArtifact artifact : artifacts) {
			Collection<IAbstractArtifact> extendingArtArray = artifact
					.getExtendingArtifacts();
			for (IAbstractArtifact extendingArt : extendingArtArray) {

				if (!containsInReferences(tsProject,
						extendingArt)) {
					continue;
				}

				if (!namesOfArtifactsInMap.contains(extendingArt
						.getFullyQualifiedName())) {
					relationshipsExist = true;
					extendingArtifacts.add(extendingArt);
				}
			}
		}

		associatedArtifacts = new HashSet<IAbstractArtifact>();
		dependentArtifacts = new HashSet<IAbstractArtifact>();
		for (IAbstractArtifact artifact : artifacts) {
			List<IRelationship> origRels = session
					.getOriginatingRelationshipForFQN(
							artifact.getFullyQualifiedName(), true);
			for (IRelationship relationship : origRels) {
				if (relationship instanceof IAssociationArtifact) {
					IAssociationEnd zEnd = (IAssociationEnd) ((IAssociationArtifact) relationship)
							.getZEnd();
					IAbstractArtifact associatedArt = zEnd.getType()
							.getArtifact();
					// if an artifact of the same type isn't already in the
					// diagram, add it to the list
					// of associated artifacts that could be added
					if (!namesOfArtifactsInMap.contains(associatedArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						associatedArtifacts
								.add((IAbstractArtifact) associatedArt);
					} else if (relationship instanceof IAssociationClassArtifact
							&& !namesOfArtifactsInMap
									.contains(((IAssociationClassArtifact) relationship)
											.getFullyQualifiedName())) {
						associatedArtifacts
								.add((IAbstractArtifact) relationship);
					}
				} else if (relationship instanceof IDependencyArtifact) {
					IRelationshipEnd zEnd = ((IDependencyArtifact) relationship)
							.getRelationshipZEnd();
					IAbstractArtifact dependentArt = zEnd.getType()
							.getArtifact();
					// if an artifact of the same type isn't already in the
					// diagram, add it to the list
					// of dependent artifacts that could be added
					if (!namesOfArtifactsInMap.contains(dependentArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						dependentArtifacts
								.add((IAbstractArtifact) dependentArt);
					}
				}
			}
		}

		associatingArtifacts = new HashSet<IAbstractArtifact>();
		dependingArtifacts = new HashSet<IAbstractArtifact>();
		for (IAbstractArtifact artifact : artifacts) {
			List<IRelationship> termRels = session
					.getTerminatingRelationshipForFQN(
							artifact.getFullyQualifiedName(), true);
			for (IRelationship relationship : termRels) {
				if (relationship instanceof IAssociationArtifact) {
					IAssociationEnd aEnd = (IAssociationEnd) ((IAssociationArtifact) relationship)
							.getAEnd();
					IAbstractArtifact associatingArt = aEnd.getType()
							.getArtifact();
					// if an artifact of the same type isn't already in the
					// diagram, add it to the list
					// of associating artifacts that could be added
					if (!namesOfArtifactsInMap.contains(associatingArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						associatingArtifacts
								.add((IAbstractArtifact) associatingArt);
					} else if (relationship instanceof IAssociationClassArtifact
							&& !namesOfArtifactsInMap
									.contains(((IAssociationClassArtifact) relationship)
											.getFullyQualifiedName())) {
						associatingArtifacts
								.add((IAbstractArtifact) relationship);
					}
				} else if (relationship instanceof IDependencyArtifact) {
					IRelationshipEnd aEnd = ((IDependencyArtifact) relationship)
							.getRelationshipAEnd();
					IAbstractArtifact dependingArt = aEnd.getType()
							.getArtifact();
					// if an artifact of the same type isn't already in the
					// diagram, add it to the list
					// of depending artifacts that could be added
					if (!namesOfArtifactsInMap.contains(dependingArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						dependingArtifacts
								.add((IAbstractArtifact) dependingArt);
					}
				}
			}
		}

		implementedArtifacts = new HashSet<IAbstractArtifact>();
		for (IAbstractArtifact artifact : artifacts) {
			Collection<IAbstractArtifact> implementedArts = artifact
					.getImplementedArtifacts();
			for (IAbstractArtifact implementedArt : implementedArts) {
				// if an artifact of the same type isn't already in the
				// diagram,
				// add it to the list
				// of implemented artifacts that could be added
				if (!namesOfArtifactsInMap.contains(implementedArt
						.getFullyQualifiedName())) {
					relationshipsExist = true;
					implementedArtifacts.add(implementedArt);
				}
			}
		}

		implementingArtifacts = new HashSet<IAbstractArtifact>();
		for (IAbstractArtifact artifact : artifacts) {
			Collection<IAbstractArtifact> implementingArts = ((IAbstractArtifact) artifact)
					.getImplementingArtifacts();
			for (IAbstractArtifact implementingArt : implementingArts) {
				// if an artifact of the same type isn't already in the
				// diagram,
				// add it to the list
				// of implementing artifacts that could be added
				if (!namesOfArtifactsInMap.contains(implementingArt
						.getFullyQualifiedName())) {
					if (!relationshipsExist)
						relationshipsExist = true;
					implementingArtifacts
							.add((IAbstractArtifact) implementingArt);
				}
			}
		}

		referencedArtifacts = new HashSet<IAbstractArtifact>();
		for (IAbstractArtifact artifact : artifacts) {

			for (IAbstractArtifact referencedArt : artifact
					.getReferencedArtifacts()) {
				// if an artifact of the same type isn't already in the
				// diagram,
				// add it to the list
				// of implementing artifacts that could be added
				if (!namesOfArtifactsInMap.contains(referencedArt
						.getFullyQualifiedName())) {
					relationshipsExist = true;
					referencedArtifacts
							.add((IAbstractArtifact) referencedArt);
				}

			}
		}

		referencingArtifacts = new HashSet<IAbstractArtifact>();
		for (IAbstractArtifact artifact : artifacts) {
			for (IAbstractArtifact referencingArt : artifact
					.getReferencingArtifacts()) {
				// if an artifact of the same type isn't already in the
				// diagram,
				// add it to the list
				// of implementing artifacts that could be added
				if (!namesOfArtifactsInMap.contains(referencingArt
						.getFullyQualifiedName())) {
					relationshipsExist = true;
					referencingArtifacts
							.add((IAbstractArtifact) referencingArt);
				}
			}
		}

		// set up a mask to show which of these lists is of non-zero size
		// and which are not
		// (is used in the dialog to determine which types can be added to
		// the diagram and which
		// types cannot, either because they don't exist for the selected
		// object or because they
		// are already in the diagram)
		BitSet creationMask = new BitSet(10);
		if (extendedArtifacts.size() > 0)
			creationMask.set(0);
		if (extendingArtifacts.size() > 0)
			creationMask.set(1);
		if (associatedArtifacts.size() > 0)
			creationMask.set(2);
		if (associatingArtifacts.size() > 0)
			creationMask.set(3);
		if (dependentArtifacts.size() > 0)
			creationMask.set(4);
		if (dependingArtifacts.size() > 0)
			creationMask.set(5);
		if (implementedArtifacts.size() > 0)
			creationMask.set(6);
		if (implementingArtifacts.size() > 0)
			creationMask.set(7);
		if (referencedArtifacts.size() > 0)
			creationMask.set(8);
		if (referencingArtifacts.size() > 0)
			creationMask.set(9);
		
		return creationMask;
	}

	public Set<IAbstractArtifact> collect(AddRelatedArtifactsPage page) {
		
		Set<IAbstractArtifact> artifactsToAdd = new LinkedHashSet<IAbstractArtifact>();
		HashMap<IAssociationClassArtifact, IAbstractArtifact[]> associationClassEndsMap = new HashMap<IAssociationClassArtifact, IAbstractArtifact[]>();
		
		if (page.isExtendedSelected()) {
			updateArtifactsToAdd(extendedArtifacts, artifactsToAdd,
					associationClassEndsMap);
		}
		if (page.isExtendingSelected()) {
			updateArtifactsToAdd(extendingArtifacts,
					artifactsToAdd, associationClassEndsMap);
		}
		if (page.isAssociatedSelected()) {
			updateArtifactsToAdd(associatedArtifacts,
					artifactsToAdd, associationClassEndsMap);
		}
		if (page.isAssociatingSelected()) {
			updateArtifactsToAdd(associatingArtifacts,
					artifactsToAdd, associationClassEndsMap);
		}
		if (page.isDependentSelected()) {
			updateArtifactsToAdd(dependentArtifacts,
					artifactsToAdd, associationClassEndsMap);
		}
		if (page.isDependingSelected()) {
			updateArtifactsToAdd(dependingArtifacts,
					artifactsToAdd, associationClassEndsMap);
		}
		if (page.isImplementedSelected()) {
			updateArtifactsToAdd(implementedArtifacts,
					artifactsToAdd, associationClassEndsMap);
		}
		if (page.isImplementingSelected()) {
			updateArtifactsToAdd(implementingArtifacts,
					artifactsToAdd, associationClassEndsMap);
		}
		if (page.isReferencedSelected()) {
			updateArtifactsToAdd(referencedArtifacts,
					artifactsToAdd, associationClassEndsMap);
		}
		if (page.isReferencingSelected()) {
			updateArtifactsToAdd(referencingArtifacts,
					artifactsToAdd, associationClassEndsMap);
		}
		// now, check the association class ends to make sure that
		// if any of them don't exist
		// they will also be added to the diagram (conversely, if
		// they both exist for any
		// of the association classes, add the association class to
		// the list of artifacts
		// to create so that it will be added to the diagram)
		for (IAssociationClassArtifact assocClassArt : associationClassEndsMap
				.keySet()) {
			IAbstractArtifact[] endArray = associationClassEndsMap
					.get(assocClassArt);
			boolean addingEndpointToDiagram = false;
			for (IAbstractArtifact endArt : endArray) {
				if (!namesOfArtifactsInMap.contains(endArt
						.getFullyQualifiedName())
						&& !artifactsToAdd.contains(endArt)) {
					if (!addingEndpointToDiagram)
						addingEndpointToDiagram = true;
					artifactsToAdd.add(endArt);
				}
			}
			if (!addingEndpointToDiagram)
				artifactsToAdd.add(assocClassArt);
		}
		return artifactsToAdd;
	}
	
	
	private boolean containsInReferences(ITigerstripeModelProject tsProject,
			IAbstractArtifact extendingArt) {
		if (tsProject == null) {
			return false;
		}

		try {
			IArtifactManagerSession session = tsProject
					.getArtifactManagerSession();
			return session.getArtifactByFullyQualifiedName(extendingArt
					.getFullyQualifiedName()) != null;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return false;
		}
	}
	
	// takes a list of artifacts and sorts them into either artifacts that can
	// be
	// added directly to the map or association class artifacts (which need to
	// be
	// handled separately, in case one or the other of their ends don't exist)
	public void updateArtifactsToAdd(
			Set<IAbstractArtifact> artifacts,
			Set<IAbstractArtifact> artifactsToAdd,
			HashMap<IAssociationClassArtifact, IAbstractArtifact[]> associationClassEndsMap) {
		for (IAbstractArtifact artifact : artifacts) {
			if (artifact instanceof AssociationClassArtifact) {
				AssociationClassArtifact assocClassArt = (AssociationClassArtifact) artifact;
				IAbstractArtifact[] endArray = new IAbstractArtifact[] {
						(IAbstractArtifact) assocClassArt.getAEnd().getType()
								.getArtifact(),
						(IAbstractArtifact) assocClassArt.getZEnd().getType()
								.getArtifact() };
				associationClassEndsMap.put(assocClassArt, endArray);
			} else {
				artifactsToAdd.add(artifact);
			}
		}
	}

	public void setRelationshipsExist(boolean relationshipsExist) {
		this.relationshipsExist = relationshipsExist;
	}

	public boolean isRelationshipsExist() {
		return relationshipsExist;
	}
}
