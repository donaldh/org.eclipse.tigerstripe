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
package org.eclipse.tigerstripe.workbench.internal.tools.compare;

import java.util.ArrayList;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IEntityMethodFlavorDetails;

public class CompareEntity {

	/**
	 * The order is not significant.... but we can only set the whole set in one
	 * go so when we generate a difference just put the whole set in (and don't
	 * do more than one of them).
	 * 
	 * @param aArtifact
	 * @param bArtifact
	 * @return
	 */
	public static ArrayList<Difference> compareImplements(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();

		ArrayList<String> aImplsNames = new ArrayList<String>();
		ArrayList<String> bImplsNames = new ArrayList<String>();
		for (IAbstractArtifact imp : aArtifact.getImplementedArtifacts()) {
			aImplsNames.add(imp.getFullyQualifiedName());
		}
		for (IAbstractArtifact imp : bArtifact.getImplementedArtifacts()) {
			bImplsNames.add(imp.getFullyQualifiedName());
		}
		for (String name : aImplsNames) {
			if (!bImplsNames.contains(name)) {
				differences.add(new Difference("value", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), "Artifact:Implements", name
						.toString(), aArtifact.getImplementedArtifactsAsStr(),
						bArtifact.getImplementedArtifactsAsStr()));
			} else {
				bImplsNames.remove(name);
			}
		}
		for (String name : bImplsNames) {
			differences.add(new Difference("value", aArtifact
					.getFullyQualifiedName(),
					bArtifact.getFullyQualifiedName(), "Artifact:Implements",
					name, aArtifact.getImplementedArtifactsAsStr(), bArtifact
							.getImplementedArtifactsAsStr()));
		}

		return differences;
	}

	public static ArrayList<Difference> compareEntitySpecifics(
			IManagedEntityArtifact aArtifact, IManagedEntityArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		// EntitySpecifics
		IOssjEntitySpecifics aSpecs = (IOssjEntitySpecifics) aArtifact
				.getIStandardSpecifics();
		IOssjEntitySpecifics bSpecs = (IOssjEntitySpecifics) bArtifact
				.getIStandardSpecifics();
		if (!aSpecs.getExtensibilityType()
				.equals(bSpecs.getExtensibilityType())) {
			differences.add(new Difference(
					// "","Artifact ValueSpecifics",
					// aArtifact.getFullyQualifiedName(),
					// "ExtensibilityType",aSpecs.getExtensibilityType(),bSpecs.getExtensibilityType()));
					"value", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
					"ManagedEntity:Specifics", "ExtensibilityType", aSpecs
							.getExtensibilityType(), bSpecs
							.getExtensibilityType()));
		}
		if (!aSpecs.getPrimaryKey().equals(bSpecs.getPrimaryKey())) {
			differences.add(new Difference(
					// "","Artifact ValueSpecifics",
					// aArtifact.getFullyQualifiedName(),
					// "PrimaryKey",aSpecs.getPrimaryKey(),bSpecs.getPrimaryKey()));
					"value", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
					"ManagedEntity:Specifics", "PrimaryKeyType", aSpecs
							.getPrimaryKey(), bSpecs.getPrimaryKey()));
		}
		// differences.addAll(CompareUtils.compareProps(aArtifact,bArtifact,
		// "InterfaceKey" ,aSpecs.getInterfaceKeyProperties(),
		// bSpecs.getInterfaceKeyProperties()));
		differences.addAll(compareEntityMethodDetails(aArtifact, bArtifact));
		return differences;
	}

	public static ArrayList<Difference> compareEntityMethodDetails(
			IManagedEntityArtifact aArtifact, IManagedEntityArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		ManagedEntityArtifact aArt = (ManagedEntityArtifact) aArtifact;
		ManagedEntityArtifact bArt = (ManagedEntityArtifact) bArtifact;

		OssjEntitySpecifics aSpecs = (OssjEntitySpecifics) aArt
				.getIStandardSpecifics();
		OssjEntitySpecifics bSpecs = (OssjEntitySpecifics) bArt
				.getIStandardSpecifics();

		for (int crudID = 0; crudID < 4; crudID++) {
			for (OssjEntityMethodFlavor flavor : aSpecs
					.getSupportedFlavors(crudID)) {
				IEntityMethodFlavorDetails aDetails = aSpecs
						.getCRUDFlavorDetails(crudID, flavor);
				IEntityMethodFlavorDetails bDetails = bSpecs
						.getCRUDFlavorDetails(crudID, flavor);
				String crudLabel;
				switch (crudID) {
				case 0:
					crudLabel = "create";
					break;
				case 1:
					crudLabel = "get";
					break;
				case 2:
					crudLabel = "set";
					break;
				case 3:
					crudLabel = "remove";
					break;
				default:
					crudLabel = "unknown";
					break;
				}
				// compare Them
				if (aDetails.getFlag() != bDetails.getFlag()) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"ManagedEntity:MethodDetails", crudLabel + ":"
									+ flavor.getPojoLabel() + ":Flag", aDetails
									.getFlag(), bDetails.getFlag()));
				}
				if (!aDetails.getComment().equals(bDetails.getComment())) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"ManagedEntity:MethodDetails", crudLabel + ":"
									+ flavor.getPojoLabel() + ":Comment",
							aDetails.getComment(), bDetails.getComment()));
				}
				// compare List of exceptions
				differences.addAll(CompareUtils.compareLists(aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(),
						"ManagedEntity:MethodDetails", crudLabel + ":"
								+ flavor.getPojoLabel() + ":Exception:",
						((ArrayList) aDetails.getExceptions()),
						((ArrayList) bDetails.getExceptions())));
			}
		}

		// Do any custom methods

		for (IMethod imethod : aArt.getMethods()) {
			try {
				String methodName = imethod.getName();
				for (OssjEntityMethodFlavor flavor : ((Method) imethod)
						.getSupportedFlavors()) {

					IEntityMethodFlavorDetails aDetails = imethod
							.getEntityMethodFlavorDetails(flavor);

					IMethod bMethod = null;
					for (IMethod tempMeth : bArt.getMethods()) {
						if (tempMeth.getName().equals(imethod.getName())) {
							bMethod = tempMeth;
						}
					}
					/*
					 * B might not have this, but that should have been picked
					 * up by the instanceMethod stuff...
					 */
					if (bMethod == null) {
						continue;
					}
					IEntityMethodFlavorDetails bDetails = bMethod
							.getEntityMethodFlavorDetails(flavor);

					// compare Them
					if (aDetails.getFlag() != bDetails.getFlag()) {
						differences.add(new Difference("value", aArtifact
								.getFullyQualifiedName(), bArtifact
								.getFullyQualifiedName(),
								"ManagedEntity:MethodDetails", methodName + ":"
										+ flavor.getPojoLabel() + ":Flag",
								aDetails.getFlag(), bDetails.getFlag()));
					}
					if (!aDetails.getComment().equals(bDetails.getComment())) {
						differences.add(new Difference("value", aArtifact
								.getFullyQualifiedName(), bArtifact
								.getFullyQualifiedName(),
								"ManagedEntity:MethodDetails", methodName + ":"
										+ flavor.getPojoLabel() + ":Comment",
								aDetails.getComment(), bDetails.getComment()));
					}
					// compare List of exceptions
					differences.addAll(CompareUtils.compareLists(aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"ManagedEntity:MethodDetails", methodName + ":"
									+ flavor.getPojoLabel() + ":Exception:",
							((ArrayList) aDetails.getExceptions()),
							((ArrayList) bDetails.getExceptions())));
				}
			} catch (TigerstripeException t) {
				TigerstripeRuntime.logInfoMessage(
						"Error getting Flavor Details", t);
				continue;
			}
		}

		return differences;
	}

}
