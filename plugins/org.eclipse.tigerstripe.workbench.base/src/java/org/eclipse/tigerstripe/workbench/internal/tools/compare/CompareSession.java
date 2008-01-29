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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjMethod;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IEntityMethodFlavorDetails;

public class CompareSession {

	public static ArrayList<Difference> compareDetails(ISessionArtifact artA,
			ISessionArtifact artB) {
		ArrayList<Difference> differences = new ArrayList<Difference>();

		String scope = "Session:ManagedEntity";

		Map<String, ISessionArtifact.IManagedEntityDetails> aNames = new HashMap<String, ISessionArtifact.IManagedEntityDetails>();
		Map<String, ISessionArtifact.IManagedEntityDetails> bNames = new HashMap<String, ISessionArtifact.IManagedEntityDetails>();

		for (ISessionArtifact.IManagedEntityDetails entity : artA.getManagedEntityDetails()) {
			aNames.put(entity.getFullyQualifiedName(), entity);
		}
		for (ISessionArtifact.IManagedEntityDetails entity : artB.getManagedEntityDetails()) {
			bNames.put(entity.getFullyQualifiedName(), entity);
		}
		for (String name : aNames.keySet()) {
			if (bNames.keySet().contains(name)) {
				// compare the details now.
				// String location = artifact.getFullyQualifiedName();
				// differences.addAll(compareFlavors(artA,artB,aNames.get(name),bNames.get(name)));
				// Do we need to check the defaults? - NO - they are not held in
				// the Project stuff
				bNames.remove(name);
			} else {
				// String component = name+":Missing from B";
				differences.add(new Difference(
						// "",diffType,artifact.getFullyQualifiedName(),
						// component,"",""));
						"presence", artA.getFullyQualifiedName(), artB
								.getFullyQualifiedName(), scope, name,
						"present", "absent"));
			}
		}
		for (String name : bNames.keySet()) {
			// String component = name+":Missing from A";
			differences.add(new Difference(
					// "",diffType,artifact.getFullyQualifiedName(),
					// component,"",""));
					"presence", artA.getFullyQualifiedName(), artB
							.getFullyQualifiedName(), scope, name, "absent",
					"present"));
		}
		return differences;
	}

	public static ArrayList<Difference> compareFlavors(ISessionArtifact artA,
			ISessionArtifact artB,
			ISessionArtifact.IManagedEntityDetails aDetail,
			ISessionArtifact.IManagedEntityDetails bDetail) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		// for each Flavor compare the five variants..CRUD + per method...
		// String entName = aDetail.getFullyQualifiedName();
		for (IOssjMethod.OssjEntityMethodFlavor flavor : IOssjMethod.OssjEntityMethodFlavor
				.values()) {
			IEntityMethodFlavorDetails aCreate = aDetail
					.getCreateFlavorDetails(flavor);
			IEntityMethodFlavorDetails aSet = aDetail
					.getSetFlavorDetails(flavor);
			IEntityMethodFlavorDetails aGet = aDetail
					.getGetFlavorDetails(flavor);
			IEntityMethodFlavorDetails aRemove = aDetail
					.getRemoveFlavorDetails(flavor);

			IEntityMethodFlavorDetails bCreate = bDetail
					.getCreateFlavorDetails(flavor);
			IEntityMethodFlavorDetails bSet = bDetail
					.getSetFlavorDetails(flavor);
			IEntityMethodFlavorDetails bGet = bDetail
					.getGetFlavorDetails(flavor);
			IEntityMethodFlavorDetails bRemove = bDetail
					.getRemoveFlavorDetails(flavor);

			ArrayList crudA = new ArrayList();
			crudA.add(aCreate);
			crudA.add(aGet);
			crudA.add(aSet);
			crudA.add(aRemove);
			ArrayList crudB = new ArrayList();
			crudB.add(bCreate);
			crudB.add(bGet);
			crudB.add(bSet);
			crudB.add(bRemove);
			ArrayList crudNames = new ArrayList();
			crudNames.add("create");
			crudNames.add("get");
			crudNames.add("set");
			crudNames.add("remove");

			for (IMethod aCustomMeth : artA.getMethods()) {
				if (aCustomMeth.isInstanceMethod()) {
					IEntityMethodFlavorDetails aCust = aDetail
							.getCustomMethodFlavorDetails(aCustomMeth
									.getMethodId(), flavor);
					// See if it's in B
					IEntityMethodFlavorDetails bCust = bDetail
							.getCustomMethodFlavorDetails(aCustomMeth
									.getMethodId(), flavor);
					if (bCust == null) {
						// String component =
						// entName+":"+aMeths[i].getName()+":"+flavor.name()+":Override
						// in A not B";
						differences
								.add(new Difference(
										// "","Session:ManagedEntity",location,
										// component,"",""));
										"presence", artA
												.getFullyQualifiedName(), artB
												.getFullyQualifiedName(),
										"override" + ":"
												+ flavor.getPojoLabel(),
										aCustomMeth.getName(), "present",
										"absent"));

					} else {
						crudA.add(aCust);
						crudB.add(bCust);
						crudNames.add(aCustomMeth.getName());
					}
				}
			}

			for (int i = 0; i < crudA.size(); i++) {
				IEntityMethodFlavorDetails aMeth = (IEntityMethodFlavorDetails) crudA
						.get(i);
				IEntityMethodFlavorDetails bMeth = (IEntityMethodFlavorDetails) crudB
						.get(i);

				String scope = "override:" + crudNames.get(i);
				if (aMeth != null && bMeth != null) {

					if (!aMeth.getFlag().equals(bMeth.getFlag())) {
						// make a Diff
						// String component =
						// entName+":create:"+flavor.name()+":flag";
						differences.add(new Difference(
						// "","Session:ManagedEntity",location,
								// component,aMeth.getFlag(),bMeth.getFlag()));
								"value", artA.getFullyQualifiedName(), artB
										.getFullyQualifiedName(), scope, flavor
										.getPojoLabel()
										+ ":flag", aMeth.getFlag(), bMeth
										.getFlag()));
					}
					differences.addAll(CompareUtils.compareLists(artA
							.getFullyQualifiedName(), artB
							.getFullyQualifiedName(), scope, flavor
							.getPojoLabel()
							+ ":Exception:", (ArrayList<String>) aMeth
							.getExceptions(), (ArrayList<String>) bMeth
							.getExceptions()));
				} else if (aMeth == null && bMeth == null) {
					// both null - don't care that's the same
				} else if (aMeth == null) {
					// a is null and b aint
					// String component =
					// entName+":create:"+flavor.name()+"Override in B not A";
					differences.add(new Difference(
					// "","Session:ManagedEntity",location, component,"",""));
							"presence", artA.getFullyQualifiedName(), artB
									.getFullyQualifiedName(), scope, flavor
									.getPojoLabel(), "abssent", "present"));
				} else {
					// b is null and a aint
					// String component =
					// entName+":create:"+flavor.name()+"Override in A not B";
					differences.add(new Difference(
					// "","Session:ManagedEntity",location, component,"",""));
							"presence", artA.getFullyQualifiedName(), artB
									.getFullyQualifiedName(), scope, flavor
									.getPojoLabel(), "present", "absent"));
				}
			}
		}
		return differences;
	}

	public static ArrayList<Difference> compareEmittedEvents(
			ISessionArtifact artA, ISessionArtifact artB) {

		String scope = "Session:Event";

		ArrayList<Difference> differences = new ArrayList<Difference>();
		ArrayList<String> aNames = new ArrayList<String>();
		ArrayList<String> bNames = new ArrayList<String>();
		for (ISessionArtifact.IEmittedEvent event : artA.getEmittedEvents()) {
			aNames.add(event.getFullyQualifiedName());
		}
		for (ISessionArtifact.IEmittedEvent event : artA.getEmittedEvents()) {
			bNames.add(event.getFullyQualifiedName());
		}
		differences.addAll(CompareUtils.compareLists(artA
				.getFullyQualifiedName(), artB.getFullyQualifiedName(), scope,
				"", aNames, bNames));
		return differences;
	}

	public static ArrayList<Difference> compareEmittedUpdates(
			ISessionArtifact artA, ISessionArtifact artB) {

		String scope = "Session:UpdateProcedure";

		ArrayList<Difference> differences = new ArrayList<Difference>();
		ArrayList<String> aNames = new ArrayList<String>();
		ArrayList<String> bNames = new ArrayList<String>();
		for (ISessionArtifact.IExposedUpdateProcedure update : artA.getExposedUpdateProcedures()) {
			aNames.add(update.getFullyQualifiedName());
		}
		for (ISessionArtifact.IExposedUpdateProcedure update : artB.getExposedUpdateProcedures()) {
			bNames.add(update.getFullyQualifiedName());
		}
		differences.addAll(CompareUtils.compareLists(artA
				.getFullyQualifiedName(), artB.getFullyQualifiedName(), scope,
				"", aNames, bNames));
		return differences;
	}

	public static ArrayList<Difference> compareEmittedQueries(
			ISessionArtifact artA, ISessionArtifact artB) {

		String scope = "Session:Query";

		ArrayList<Difference> differences = new ArrayList<Difference>();
		ArrayList<String> aNames = new ArrayList<String>();
		ArrayList<String> bNames = new ArrayList<String>();
		for (ISessionArtifact.INamedQuery query : artA.getNamedQueries()) {
			aNames.add(query.getFullyQualifiedName());
		}
		for (ISessionArtifact.INamedQuery query : artB.getNamedQueries()) {
			bNames.add(query.getFullyQualifiedName());
		}
		differences.addAll(CompareUtils.compareLists(artA
				.getFullyQualifiedName(), artB.getFullyQualifiedName(), scope,
				"", aNames, bNames));
		return differences;
	}

}
