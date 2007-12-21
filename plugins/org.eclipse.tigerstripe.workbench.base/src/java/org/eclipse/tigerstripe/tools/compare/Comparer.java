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
package org.eclipse.tigerstripe.tools.compare;

import java.util.ArrayList;

import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IDependencyArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEventArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IExceptionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IQueryArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IUpdateProcedureArtifact;

public class Comparer {

	public ArrayList<Difference> compareArtifacts(IAbstractArtifact artA,
			IAbstractArtifact artB, boolean full) {
		return compareArtifacts(artA, artB, full, true);
	}

	public ArrayList<Difference> compareArtifacts(IAbstractArtifact artA,
			IAbstractArtifact artB, boolean full, boolean lookinDependencies) {
		// TigerstripeRuntime.logInfoMessage(artA.getFullyQualifiedName()+"
		// "+artB.getFullyQualifiedName());
		ArrayList diffs = new ArrayList<Difference>();
		// Should be of same type
		if (!CompareUtils.typeOf(artA).equals(CompareUtils.typeOf(artB))) {
			Difference typeDiff = new Difference("value", artA
					.getFullyQualifiedName(), artB.getFullyQualifiedName(),
					"Artifact", "Type", CompareUtils.typeOf(artA), CompareUtils
							.typeOf(artB));
			diffs.add(typeDiff);
			// Can't continue in this case
			return diffs;
		}

		if (artA.isAbstract() != artB.isAbstract()) {
			Difference typeDiff = new Difference("value", artA
					.getFullyQualifiedName(), artB.getFullyQualifiedName(),
					"Artifact:Detail", "Abstract",
					((Boolean) artA.isAbstract()).toString(), ((Boolean) artB
							.isAbstract()).toString());
			diffs.add(typeDiff);
		}

		// Compare FQN - this wouldn't normally be useful?
		if (!artA.getFullyQualifiedName().equals(artB.getFullyQualifiedName())) {
			Difference fqnDiff = new Difference("value", artA
					.getFullyQualifiedName(), artB.getFullyQualifiedName(),
					"Artifact", "FQN", artA.getFullyQualifiedName(), artB
							.getFullyQualifiedName());
			diffs.add(fqnDiff);
		}

		if (lookinDependencies) {
			// Compare the location - eg Local re Dependency or Reference
			String projA = CompareUtils.getProjectLocation(artA);
			String projB = CompareUtils.getProjectLocation(artB);
			if (!projA.equals(projB)) {
				Difference locationDiff = new Difference("value", artA
						.getFullyQualifiedName(), artB.getFullyQualifiedName(),
						"Artifact", "Project", projA, projB);
				diffs.add(locationDiff);
			}
		}

		diffs.addAll(CompareUtils.compareExtends(artA, artB));
		diffs.addAll(CompareUtils.compareArtifactComment(artA, artB));
		diffs.addAll(CompareUtils.compareStereotypes(artA, artB, artA, artB,
				"Artifact"));

		// Now get specific based on Artifact Type (which we know are both the
		// same)
		if (artA instanceof ISessionArtifact) {
			diffs.addAll(compareSession((ISessionArtifact) artA,
					(ISessionArtifact) artB));
		} else if (artA instanceof IUpdateProcedureArtifact) {
			diffs.addAll(compareUpdate((IUpdateProcedureArtifact) artA,
					(IUpdateProcedureArtifact) artB));
		} else if (artA instanceof IEnumArtifact) {
			diffs.addAll(compareEnumeration((IEnumArtifact) artA,
					(IEnumArtifact) artB));
		} else if (artA instanceof IEventArtifact) {
			diffs.addAll(compareEvent((IEventArtifact) artA,
					(IEventArtifact) artB));
		} else if (artA instanceof IDatatypeArtifact) {
			diffs.addAll(compareDatatype((IDatatypeArtifact) artA,
					(IDatatypeArtifact) artB));
		} else if (artA instanceof IExceptionArtifact) {
			diffs.addAll(compareException((IExceptionArtifact) artA,
					(IExceptionArtifact) artB));
		} else if (artA instanceof IQueryArtifact) {
			diffs.addAll(compareQuery((IQueryArtifact) artA,
					(IQueryArtifact) artB));
		} else if (artA instanceof IManagedEntityArtifact) {
			diffs.addAll(compareEntity((IManagedEntityArtifact) artA,
					(IManagedEntityArtifact) artB));
		} else if (artA instanceof IAssociationClassArtifact) {
			diffs.addAll(compareAssociationClass(
					(IAssociationClassArtifact) artA,
					(IAssociationClassArtifact) artB));
		} else if (artA instanceof IAssociationArtifact) {
			diffs.addAll(compareAssociation((IAssociationArtifact) artA,
					(IAssociationArtifact) artB));
		} else if (artA instanceof IDependencyArtifact) {
			diffs.addAll(compareDependency((IDependencyArtifact) artA,
					(IDependencyArtifact) artB));
		}
		return diffs;
	}

	public ArrayList<Difference> compareAssociationClass(
			IAssociationClassArtifact artA, IAssociationClassArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		differences.addAll(CompareUtils.compareFields(artA, artB));
		differences.addAll(CompareUtils.compareMethods(artA, artB));
		differences.addAll(CompareUtils.compareAssociationEnds(artA, artB));
		differences.addAll(CompareEntity.compareImplements(artA, artB));
		return differences;
	}

	public ArrayList<Difference> compareAssociation(IAssociationArtifact artA,
			IAssociationArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		differences.addAll(CompareUtils.compareAssociationEnds(artA, artB));
		return differences;
	}

	public ArrayList<Difference> compareDependency(IDependencyArtifact artA,
			IDependencyArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		differences.addAll(CompareUtils.compareDependencyEnds(artA, artB));
		return differences;
	}

	public ArrayList<Difference> compareSession(ISessionArtifact artA,
			ISessionArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();

		differences.addAll(CompareUtils.compareInterface(artA, artB));
		differences.addAll(CompareUtils.compareMethods(artA, artB));

		differences.addAll(CompareSession.compareEmittedEvents(artA, artB));
		differences.addAll(CompareSession.compareEmittedUpdates(artA, artB));
		differences.addAll(CompareSession.compareEmittedQueries(artA, artB));
		differences.addAll(CompareSession.compareDetails(artA, artB));

		return differences;
	}

	public ArrayList<Difference> compareUpdate(IUpdateProcedureArtifact artA,
			IUpdateProcedureArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		differences.addAll(CompareUtils.compareInterface(artA, artB));
		differences.addAll(CompareUtils.compareLabels(artA, artB));
		differences.addAll(CompareUtils.compareFields(artA, artB));
		differences.addAll(CompareUtils.compareSingleExtension(artA, artB));
		differences.addAll(CompareUtils
				.compareSessionFactoryMethods(artA, artB));
		return differences;
	}

	public ArrayList<Difference> compareEnumeration(IEnumArtifact artA,
			IEnumArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		differences.addAll(CompareUtils.compareInterface(artA, artB));
		differences.addAll(CompareUtils.compareLabels(artA, artB));
		differences.addAll(CompareEnumeration.compareEnumSpecifics(artA, artB));
		return differences;
	}

	public ArrayList<Difference> compareEvent(IEventArtifact artA,
			IEventArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		differences.addAll(CompareUtils.compareInterface(artA, artB));
		differences.addAll(CompareUtils.compareLabels(artA, artB));
		differences.addAll(CompareUtils.compareFields(artA, artB));
		differences.addAll(CompareEvent.compareEventSpecifics(artA, artB));
		differences.addAll(CompareUtils.compareSingleExtension(artA, artB));
		return differences;
	}

	public ArrayList<Difference> compareDatatype(IDatatypeArtifact artA,
			IDatatypeArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		differences.addAll(CompareUtils.compareInterface(artA, artB));
		differences.addAll(CompareUtils.compareLabels(artA, artB));
		differences.addAll(CompareUtils.compareFields(artA, artB));
		differences.addAll(CompareUtils.compareMethods(artA, artB));
		// No specifics
		differences.addAll(CompareUtils.compareSingleExtension(artA, artB));
		differences.addAll(CompareUtils
				.compareSessionFactoryMethods(artA, artB));
		return differences;
	}

	public ArrayList<Difference> compareException(IExceptionArtifact artA,
			IExceptionArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		differences.addAll(CompareUtils.compareInterface(artA, artB));
		// There are no attribues,constants,methods or specifics for Exceptions
		// Makes this whole thing a bit pointless!
		return differences;
	}

	public ArrayList<Difference> compareQuery(IQueryArtifact artA,
			IQueryArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		differences.addAll(CompareUtils.compareInterface(artA, artB));
		differences.addAll(CompareUtils.compareLabels(artA, artB));
		differences.addAll(CompareUtils.compareFields(artA, artB));
		differences.addAll(CompareQuery.compareQuerySpecifics(artA, artB));
		differences.addAll(CompareUtils.compareSingleExtension(artA, artB));
		differences.addAll(CompareUtils
				.compareSessionFactoryMethods(artA, artB));
		return differences;
	}

	public ArrayList<Difference> compareEntity(IManagedEntityArtifact artA,
			IManagedEntityArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		differences.addAll(CompareUtils.compareInterface(artA, artB));
		differences.addAll(CompareUtils.compareLabels(artA, artB));
		differences.addAll(CompareUtils.compareFields(artA, artB));
		differences.addAll(CompareUtils.compareMethods(artA, artB));
		differences.addAll(CompareEntity.compareImplements(artA, artB));
		differences.addAll(CompareEntity.compareEntitySpecifics(artA, artB));
		differences.addAll(CompareUtils
				.compareSessionFactoryMethods(artA, artB));
		return differences;
	}

}
