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
package org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjFlavorDefaults;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjMethod;

/**
 * In the context of Managed Entities, methods are defined on the object itself,
 * but may be overiden by the session on which they are attached.
 * 
 * The EntityOveride provides the logic for the overide on top of all methods on
 * an entity. It transparently provides access to methods and their flavor
 * details taking into account overides.
 * 
 * It also provides the writers to produce the pojo tags required.
 * 
 * EntityOverides are basically a HashMap of FlavorOverides where the key to the
 * hash is the methodId.
 * 
 * @author Eric Dillon
 * 
 */
@Deprecated
public class EntityOveride {

	public class FlavorOveride {
		public OssjEntityMethodFlavor flavor;

		public EntityMethodFlavorDetails details;

		@Override
		public FlavorOveride clone() {
			FlavorOveride result = new FlavorOveride();

			result.flavor = flavor;
			result.details = details.clone();
			return result;
		}
	}

	public enum FlavorType {
		ORIG_FLAVOR, OVERIDE_FLAVOR
	};

	// The target managed entity overiden here
	private ManagedEntityDetails managedEntity;

	// The map that contains the defined overides
	private HashMap<String, List<FlavorOveride>> overideMap;

	public EntityOveride(ManagedEntityDetails managedEntity) {
		this.managedEntity = managedEntity;
		this.overideMap = new HashMap<String, List<FlavorOveride>>();
	}

	public ManagedEntityDetails getManagedEntity() {
		return this.managedEntity;
	}

	@Override
	public EntityOveride clone() {
		EntityOveride result = new EntityOveride(managedEntity);
		result.overideMap = new HashMap<String, List<FlavorOveride>>();

		for (String key : overideMap.keySet()) {
			List<FlavorOveride> mList = overideMap.get(key);
			List<FlavorOveride> clonedList = new ArrayList<FlavorOveride>();

			for (FlavorOveride overide : mList) {
				clonedList.add(overide.clone());
			}

			result.overideMap.put(key, clonedList);
		}

		return result;
	}

	/**
	 * Returns the methods as they been overiden. This method also creates
	 * virtual CRUD methods as part of the returned array
	 * 
	 * @return
	 */
	public IOssjMethod[] getMethods() {
		Collection<IOssjMethod> localCol = new ArrayList<IOssjMethod>();
		Collection<IOssjMethod> crudCol = Arrays
				.asList((IOssjMethod[]) buildVirtualCrudMethods(managedEntity
						.getArtifact()));
		localCol.addAll(crudCol);

		// Make sure we only add the non instance methods
		Collection<IMethod> customMethods = managedEntity.getArtifact()
				.getMethods();
		for (IMethod customMethod : customMethods) {
			if (customMethod.isInstanceMethod()) {
				localCol.add((IOssjMethod) customMethod);
			}
		}

		// At this point, we have all the methods as they are in the underlying
		// entity.
		// we now need to apply the overides
		IOssjMethod[] result = applyOverides(localCol);

		return result;
	}

	private IOssjMethod[] applyOverides(Collection<IOssjMethod> origCol) {
		IOssjMethod[] result = new IOssjMethod[origCol.size()];

		int index = 0;
		for (Iterator iter = origCol.iterator(); iter.hasNext();) {
			IOssjMethod method = (IOssjMethod) iter.next();
			if (overideMap.containsKey(method.getMethodId())) {
				List<FlavorOveride> overideList = overideMap.get(method
						.getMethodId());
				result[index] = method.cloneToIOssjMethod();
				for (FlavorOveride overide : overideList) {
					result[index].setFlavorDetails(overide.flavor,
							overide.details);
				}
			} else {
				result[index] = method;
			}
			index++;
		}

		return result;
	}

	/**
	 * Sets an overide for the given method and flavor
	 * 
	 * @param method
	 * @param flavor
	 * @param flavorDetails
	 */
	public void overideMethod(String methodId, OssjEntityMethodFlavor flavor,
			EntityMethodFlavorDetails flavorDetails) {
		List<FlavorOveride> list = overideMap.get(methodId);
		if (list == null) {
			list = new ArrayList<FlavorOveride>();
			overideMap.put(methodId, list);
		}
		// We want to make sure there's only meaningful overides
		// ie.:
		// - overides appear once only for a specific flavor
		// - if the overide is the current value from the entity
		// it shouldn't be an overide
		FlavorOveride fOver = addUniqueOveride(list, flavor, flavorDetails);
	}

	/**
	 * Creates a unique overide for the given method/flavor
	 * 
	 */
	private FlavorOveride addUniqueOveride(List<FlavorOveride> methodList,
			OssjEntityMethodFlavor flavor,
			EntityMethodFlavorDetails flavorDetails) {
		FlavorOveride result = null;
		boolean existingOveride = false;
		for (FlavorOveride overide : methodList) {
			if (overide.flavor == flavor) {
				existingOveride = true;
				// We've found the right flavor
				// if ( !isDefaultEntityFlavorDetails( methodId)) {
				overide.details = flavorDetails;
				result = overide;
				// }
			}
		}
		if (!existingOveride) {
			result = new FlavorOveride();
			result.flavor = flavor;
			result.details = flavorDetails;
			methodList.add(result);
		}

		return result;
	}

	/**
	 * Returns true if the given method is being overiden.
	 * 
	 * @param method
	 * @return
	 */
	public boolean hasOveride(IOssjMethod method) {
		return overideMap.containsKey(method.getMethodId());
	}

	/**
	 * Returns true if the given method is being overiden for the specified
	 * flavor
	 * 
	 * @param method
	 * @param flavor
	 * @return
	 */
	public boolean hasOveride(IOssjMethod method, OssjEntityMethodFlavor flavor) {
		if (hasOveride(method)) {
			List<FlavorOveride> list = overideMap.get(method.getMethodId());
			for (FlavorOveride overide : list) {
				if (overide.flavor == flavor)
					return true;
			}
		}
		return false;
	}

	public EntityMethodFlavorDetails getFlavorDetails(IOssjMethod method,
			OssjEntityMethodFlavor flavor) {
		if (hasOveride(method)) {
			List<FlavorOveride> list = overideMap.get(method.getMethodId());
			for (FlavorOveride overide : list) {
				if (overide.flavor == flavor)
					return overide.details;
			}
		}
		return null;
	}

	/**
	 * We build virtual IMethod for Create, Get, Set and Remove so it can be
	 * handled seamlessly by the GUI.
	 * 
	 * @param artifact
	 * @return
	 */
	static public IMethod[] buildVirtualCrudMethods(IAbstractArtifact artifact) {
		IMethod[] result = new IOssjMethod[4];

		IOssjEntitySpecifics specifics = (IOssjEntitySpecifics) artifact
				.getIStandardSpecifics();

		IOssjMethod createMethod = (IOssjMethod) artifact.makeMethod();
		createMethod.setName("create");
		createMethod
				.setSupportedFlavors(IOssjFlavorDefaults.createMethodFlavors);
		createMethod.setOssjMethodProperties(new Properties());
		createMethod.setOssjMethodProperties(specifics
				.getCRUDProperties(IOssjEntitySpecifics.CREATE));
		createMethod.setReturnType(createMethod.makeType());
		result[0] = createMethod;

		IOssjMethod setMethod = (IOssjMethod) artifact.makeMethod();
		setMethod.setName("set");
		setMethod.setSupportedFlavors(IOssjFlavorDefaults.setMethodFlavors);
		setMethod.setOssjMethodProperties(specifics
				.getCRUDProperties(IOssjEntitySpecifics.SET));
		setMethod.setReturnType(setMethod.makeType());
		result[1] = setMethod;

		IOssjMethod getMethod = (IOssjMethod) artifact.makeMethod();
		getMethod.setName("get");
		getMethod.setSupportedFlavors(IOssjFlavorDefaults.getMethodFlavors);
		getMethod.setOssjMethodProperties(specifics
				.getCRUDProperties(IOssjEntitySpecifics.GET));
		getMethod.setReturnType(getMethod.makeType());
		result[2] = getMethod;

		IOssjMethod removeMethod = (IOssjMethod) artifact.makeMethod();
		removeMethod.setName("remove");
		removeMethod
				.setSupportedFlavors(IOssjFlavorDefaults.removeMethodFlavors);
		removeMethod.setOssjMethodProperties(specifics
				.getCRUDProperties(IOssjEntitySpecifics.DELETE));
		removeMethod.setReturnType(removeMethod.makeType());
		result[3] = removeMethod;
		return result;
	}

	public String toPojo() {

		StringBuffer result = new StringBuffer();
		// @tigerstripe.managed-entity-overide entity="entity-fqn"
		// methodId="xxxx"
		// <flavor>="true|optional|false:<optional-exceptions>"
		for (String methodId : overideMap.keySet()) {
			String item = "@"
					+ OssjSessionFacadeSpecifics.MANAGED_ENTITY_OVERRIDE_TAG
					+ " ";
			item = item + "entity=\""
					+ getManagedEntity().getFullyQualifiedName() + "\" ";
			item = item + "methodId=\"" + methodId + "\" ";

			for (FlavorOveride over : overideMap.get(methodId)) {
				item = item + " " + over.flavor.getPojoLabel() + "=" + "\""
						+ over.details.toString() + "\"";
			}
			result.append(item);
			result.append("\n *  ");
		}
		return result.toString();
	}

	/**
	 * Removes all overrides from this. As a result, the entity defined
	 * selection will prevail.
	 */
	public void unOverrideAll() {
		this.overideMap.clear();
	}

	/**
	 * Remove all overrides from this for the given method
	 * 
	 * @param methodId
	 */
	public void unOverrideMethod(String methodId) {
		List<FlavorOveride> oldList = this.overideMap.get(methodId);
		oldList.clear();
		this.overideMap.remove(methodId);
	}

	/**
	 * Removes the flavor overrides for the given flavor/method
	 * 
	 * @param methodId
	 * @param flavor
	 */
	public void unOverrideFlavor(String methodId, OssjEntityMethodFlavor flavor) {
		List<FlavorOveride> oldList = this.overideMap.get(methodId);
		FlavorOveride targetOveride = null;
		for (FlavorOveride overide : oldList) {
			if (overide.flavor == flavor) {
				targetOveride = overide;
			}
		}
		if (targetOveride != null) {
			oldList.remove(targetOveride);
		}

		if (oldList.isEmpty()) {
			this.overideMap.remove(methodId);
		}
	}
}
