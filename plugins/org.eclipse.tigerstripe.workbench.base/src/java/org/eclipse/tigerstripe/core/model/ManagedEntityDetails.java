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
package org.eclipse.tigerstripe.core.model;

import org.eclipse.tigerstripe.api.model.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IOssjFlavorDefaults;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IOssjMethod;
import org.eclipse.tigerstripe.core.model.ossj.specifics.EntityMethodFlavorDetails;
import org.eclipse.tigerstripe.core.model.ossj.specifics.EntityOveride;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.core.util.Util;

/**
 * A local representation of a managed entity for a session
 * 
 * It details what operations should be allowed on a specific Managed entity
 * through this session facade.
 * 
 * @author Eric Dillon
 */
public class ManagedEntityDetails implements IManagedEntityDetails {

	private ArtifactManager artifactManager;

	public boolean isResolvedToArtifact() {
		return getArtifact() != null;
	}

	public ManagedEntityDetails(ArtifactManager mgr) {
		if (mgr == null)
			Thread.dumpStack();
		artifactManager = mgr;
		overide = new EntityOveride(this);
	}

	EntityOveride overide;

	String fullyQualifiedName;

	boolean includeDescendants;

	public ManagedEntityArtifact getArtifact() {
		return (ManagedEntityArtifact) artifactManager
				.getArtifactByFullyQualifiedName(this.fullyQualifiedName, true,
						new TigerstripeNullProgressMonitor());
	}

	public EntityOveride getOveride() {
		return this.overide;
	}

	public void setOveride(EntityOveride overide) {
		this.overide = overide;
	}

	public String getName() {
		if (this.fullyQualifiedName.indexOf(".") == -1)
			return this.fullyQualifiedName;
		else {
			String result = fullyQualifiedName.substring(
					this.fullyQualifiedName.lastIndexOf(".") + 1,
					this.fullyQualifiedName.length());
			return result;
		}

	}

	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}

	public void setFullyQualifiedName(String name) {
		this.fullyQualifiedName = name;
	}

	public boolean getIncludeDescendants() {
		return this.includeDescendants;
	}

	public void setIncludeDescendants(String includeDescendants) {
		this.includeDescendants = Util.strBoolean(includeDescendants);
	}

	@Override
	public IManagedEntityDetails clone() {
		ManagedEntityDetails result = new ManagedEntityDetails(
				this.artifactManager);
		result.setFullyQualifiedName(getFullyQualifiedName());
		result.setIncludeDescendants(String.valueOf(getIncludeDescendants())); // is
		// that
		// still
		// used?
		result.setOveride(getOveride().clone());
		return result;
	}

	public IEntityMethodFlavorDetails getCreateFlavorDetails(
			OssjEntityMethodFlavor flavor) {
		return getCustomMethodFlavorDetails(":create()", flavor);
	}

	public IEntityMethodFlavorDetails getCustomMethodFlavorDetails(
			String methodId, OssjEntityMethodFlavor flavor) {
		IOssjMethod[] methods = overide.getMethods();
		for (IOssjMethod method : methods) {
			if (method.getMethodId().equals(methodId))
				return method.getFlavorDetails(flavor);
		}
		return null;
	}

	public IEntityMethodFlavorDetails getGetFlavorDetails(
			OssjEntityMethodFlavor flavor) {
		return getCustomMethodFlavorDetails(":get()", flavor);
	}

	public IEntityMethodFlavorDetails getRemoveFlavorDetails(
			OssjEntityMethodFlavor flavor) {
		return getCustomMethodFlavorDetails(":remove()", flavor);
	}

	public IEntityMethodFlavorDetails getSetFlavorDetails(
			OssjEntityMethodFlavor flavor) {
		return getCustomMethodFlavorDetails(":set()", flavor);
	}

	public IEntityMethodFlavorDetails getCreateFlavorDetailsStr(String flavor) {
		return getCreateFlavorDetails(OssjEntityMethodFlavor
				.valueFromPojoLabel(flavor));
	}

	public IEntityMethodFlavorDetails getCustomMethodFlavorDetailsStr(
			String methodId, String flavor) {
		return getCustomMethodFlavorDetails(methodId, OssjEntityMethodFlavor
				.valueFromPojoLabel(flavor));
	}

	public IEntityMethodFlavorDetails getGetFlavorDetailsStr(String flavor) {
		return getGetFlavorDetails(OssjEntityMethodFlavor
				.valueFromPojoLabel(flavor));
	}

	public IEntityMethodFlavorDetails getRemoveFlavorDetailsStr(String flavor) {
		return getRemoveFlavorDetails(OssjEntityMethodFlavor
				.valueFromPojoLabel(flavor));
	}

	public IEntityMethodFlavorDetails getSetFlavorDetailsStr(String flavor) {
		return getSetFlavorDetails(OssjEntityMethodFlavor
				.valueFromPojoLabel(flavor));
	}

	public IEntityMethodFlavorDetails getDefaultCreateFlavorDetails(
			OssjEntityMethodFlavor targetFlavor) {
		int index = 0, i = 0;
		for (OssjEntityMethodFlavor flavor : IOssjFlavorDefaults.createMethodFlavors) {
			if (flavor == targetFlavor) {
				index = i;
			}
			i++;
		}

		return new EntityMethodFlavorDetails(getArtifact(),
				IOssjFlavorDefaults.createMethodFlavorDefaults[index]);
	}

	public IEntityMethodFlavorDetails getDefaultCreateFlavorDetailsStr(
			String flavorStr) {
		return getDefaultCreateFlavorDetails(OssjEntityMethodFlavor
				.valueFromPojoLabel(flavorStr));
	}

	public IEntityMethodFlavorDetails getDefaultCustomMethodFlavorDetails(
			OssjEntityMethodFlavor targetFlavor) {
		int index = 0, i = 0;
		for (OssjEntityMethodFlavor flavor : IOssjFlavorDefaults.customMethodFlavors) {
			if (flavor == targetFlavor) {
				index = i;
			}
			i++;
		}

		return new EntityMethodFlavorDetails(getArtifact(),
				IOssjFlavorDefaults.customMethodFlavorDefaults[index]);
	}

	public IEntityMethodFlavorDetails getDefaultCustomMethodFlavorDetailsStr(
			String flavorStr) {
		return getDefaultCustomMethodFlavorDetails(OssjEntityMethodFlavor
				.valueFromPojoLabel(flavorStr));
	}

	public IEntityMethodFlavorDetails getDefaultGetFlavorDetails(
			OssjEntityMethodFlavor targetFlavor) {
		int index = 0, i = 0;
		for (OssjEntityMethodFlavor flavor : IOssjFlavorDefaults.getMethodFlavors) {
			if (flavor == targetFlavor) {
				index = i;
			}
			i++;
		}

		return new EntityMethodFlavorDetails(getArtifact(),
				IOssjFlavorDefaults.getMethodFlavorDefaults[index]);
	}

	public IEntityMethodFlavorDetails getDefaultGetFlavorDetailsStr(
			String flavorStr) {
		return getDefaultGetFlavorDetails(OssjEntityMethodFlavor
				.valueFromPojoLabel(flavorStr));
	}

	public IEntityMethodFlavorDetails getDefaultRemoveFlavorDetails(
			OssjEntityMethodFlavor targetFlavor) {
		int index = 0, i = 0;
		for (OssjEntityMethodFlavor flavor : IOssjFlavorDefaults.removeMethodFlavors) {
			if (flavor == targetFlavor) {
				index = i;
			}
			i++;
		}

		return new EntityMethodFlavorDetails(getArtifact(),
				IOssjFlavorDefaults.removeMethodFlavorDefaults[index]);
	}

	public IEntityMethodFlavorDetails getDefaultRemoveFlavorDetailsStr(
			String flavorStr) {
		return getDefaultRemoveFlavorDetails(OssjEntityMethodFlavor
				.valueFromPojoLabel(flavorStr));
	}

	public IEntityMethodFlavorDetails getDefaultSetFlavorDetails(
			OssjEntityMethodFlavor targetFlavor) {
		int index = 0, i = 0;
		for (OssjEntityMethodFlavor flavor : IOssjFlavorDefaults.setMethodFlavors) {
			if (flavor == targetFlavor) {
				index = i;
			}
			i++;
		}

		return new EntityMethodFlavorDetails(getArtifact(),
				IOssjFlavorDefaults.setMethodFlavorDefaults[index]);
	}

	public IEntityMethodFlavorDetails getDefaultSetFlavorDetailsStr(
			String flavorStr) {
		return getDefaultSetFlavorDetails(OssjEntityMethodFlavor
				.valueFromPojoLabel(flavorStr));
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof ManagedEntityDetails) {
			ManagedEntityDetails other = (ManagedEntityDetails) arg0;
			return other.getFullyQualifiedName() != null
					&& other.getFullyQualifiedName().equals(
							getFullyQualifiedName());
		}
		return false;
	}

}