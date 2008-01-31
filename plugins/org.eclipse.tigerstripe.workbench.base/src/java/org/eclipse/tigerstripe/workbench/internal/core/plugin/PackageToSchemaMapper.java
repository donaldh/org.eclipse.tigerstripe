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
package org.eclipse.tigerstripe.workbench.internal.core.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This represents a map between Java packages and XML schemas. This allows the
 * user to split the generated XML schema in multiple XSDs based on the java
 * package where the Artifacts live.
 * 
 * This map is also packaged into the project module when this project is
 * packaged so the client project can make references to the correct XSDs.
 * 
 * @author Eric Dillon
 * 
 */
public class PackageToSchemaMapper {

	/**
	 * A mapping for a package.
	 */
	public class PckXSDMapping {

		public PckXSDMapping() {

		}

		public PckXSDMapping(PckXSDMapping toClone) {
			pkg = toClone.getPackage();
			targetNamespace = toClone.getTargetNamespace();
			xsdLocation = toClone.getXsdLocation();
			xsdName = toClone.getXsdName();
			userPrefix = toClone.getUserPrefix();
		}

		private String targetNamespace = "";

		/** The package,i.e. the key into the mapping */
		private String pkg = "";

		// Corresponding xsdName
		private String xsdName = "";

		// Corresponding xsdLocation
		private String xsdLocation = "";

		// User specified prefix to use
		private String userPrefix = "";

		public String getTargetNamespace() {
			return this.targetNamespace;
		}

		public void setTargetNamespace(String targetNamespace) {
			this.targetNamespace = targetNamespace;
		}

		public String getPackage() {
			return pkg;
		}

		public void setPackage(String pkg) {
			this.pkg = pkg;
		}

		public String getXsdLocation() {
			return xsdLocation;
		}

		public void setXsdLocation(String xsdLocation) {
			this.xsdLocation = xsdLocation;
		}

		public String getXsdName() {
			return xsdName;
		}

		public void setXsdName(String xsdName) {
			this.xsdName = xsdName;
		}

		public void setuserPrefix(String userPrefix) {
			this.userPrefix = userPrefix;
		}

		public String getUserPrefix() {
			return userPrefix;
		}

		@Override
		public boolean equals(Object arg0) {
			if (arg0 instanceof PckXSDMapping) {
				PckXSDMapping other = (PckXSDMapping) arg0;
				return pkg.equals(other.getPackage());
			} else
				return false;
		}
	}

	// The actual map
	private HashMap pkgMap;

	// the default mapping to use, if no mapping defined.
	private PckXSDMapping defaultMapping;

	// If set to true, the default mapping should be used for all artifacts.
	// if false, the mapping is based on the hashMap.
	private boolean useDefaultMapping;

	public PckXSDMapping getDefaultMapping() {
		return defaultMapping;
	}

	/**
	 * The default constructor
	 * 
	 * @param pluginConfig
	 */
	public PackageToSchemaMapper() {
		pkgMap = new HashMap();
		useDefaultMapping = true;
		defaultMapping = new PckXSDMapping();
	}

	/**
	 * The main lookup method: returns the mapping for the given artifact
	 * 
	 * @param packageToMap
	 *            the artifact to get the mapping for
	 * @return the corresponding mapping, or null if the artifact cannot be
	 *         resolved.
	 */
	public PckXSDMapping getPckXSDMapping(String packageToMap) {

		// Check we have a valid artifact first
		if (packageToMap == null)
			return null;

		if (useDefaultMapping)
			return defaultMapping;
		else {
			// find the first mapping
			PckXSDMapping result = null;

			// first try the package as is
			result = (PckXSDMapping) pkgMap.get(packageToMap);
			if (result != null)
				// Good, we found a direct match
				return result;
			else {
				int index = packageToMap.lastIndexOf(".");
				if (index == -1)
					return defaultMapping;
				else {
					String newPackageToMap = packageToMap;
					do {
						// TigerstripeRuntime.logInfoMessage(newPackageToMap);
						index = newPackageToMap.lastIndexOf(".");
						if (index != -1) {
							newPackageToMap = newPackageToMap.substring(0,
									index);
							// TigerstripeRuntime.logInfoMessage(newPackageToMap);
							result = (PckXSDMapping) pkgMap
									.get(newPackageToMap);
						}
					} while (result == null && index != -1);
					if (result == null) {
						result = defaultMapping;
					}
				}
			}
			return result;
		}
	}

	public void appendToXML(Element parent, Document doc) {
		Element packageMappingElem = doc.createElement("packageMapping");

		packageMappingElem.setAttribute("useDefaultMapping", String
				.valueOf(useDefaultMapping));
		packageMappingElem.setAttribute("defaultLocation",
				defaultMapping.xsdLocation);
		packageMappingElem.setAttribute("defaultName", defaultMapping.xsdName);
		packageMappingElem.setAttribute("targetNamespace",
				defaultMapping.targetNamespace);
		packageMappingElem.setAttribute("defaultUserPrefix",
				defaultMapping.userPrefix);

		for (Iterator iter = pkgMap.values().iterator(); iter.hasNext();) {
			PckXSDMapping mapping = (PckXSDMapping) iter.next();
			Element mappingElem = doc.createElement("mapping");
			mappingElem.setAttribute("package", mapping.pkg);
			mappingElem.setAttribute("xsdName", mapping.xsdName);
			mappingElem.setAttribute("xsdLocation", mapping.xsdLocation);
			mappingElem
					.setAttribute("targetNamespace", mapping.targetNamespace);
			mappingElem.setAttribute("userPrefix", mapping.userPrefix);
			packageMappingElem.appendChild(mappingElem);
		}

		parent.appendChild(packageMappingElem);
	}

	public void updateFromXML(Element element) {
		clear();
		NodeList nodeList = element.getElementsByTagName("packageMapping");
		if (nodeList == null || nodeList.getLength() == 0) {
			// empty
		} else {
			Element packageMappingElem = (Element) nodeList.item(0);
			useDefaultMapping = "true".equalsIgnoreCase(packageMappingElem
					.getAttribute("useDefaultMapping"));
			defaultMapping.xsdLocation = packageMappingElem
					.getAttribute("defaultLocation");
			defaultMapping.xsdName = packageMappingElem
					.getAttribute("defaultName");
			defaultMapping.targetNamespace = packageMappingElem
					.getAttribute("targetNamespace");
			defaultMapping.userPrefix = packageMappingElem
					.getAttribute("defaultUserPrefix");

			// Now handle all the mappings
			nodeList = packageMappingElem.getElementsByTagName("mapping");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element mappingElem = (Element) nodeList.item(i);
				PckXSDMapping mapping = new PckXSDMapping();
				mapping.pkg = mappingElem.getAttribute("package");
				mapping.xsdLocation = mappingElem.getAttribute("xsdLocation");
				mapping.xsdName = mappingElem.getAttribute("xsdName");
				mapping.targetNamespace = mappingElem
						.getAttribute("targetNamespace");
				mapping.userPrefix = mappingElem.getAttribute("userPrefix");
				addMapping(mapping);
			}
		}
	}

	private void clear() {
		this.useDefaultMapping = true;
		this.pkgMap.clear();
	}

	private void addMapping(PckXSDMapping mapping) {
		pkgMap.put(mapping.pkg, mapping);
	}

	public PckXSDMapping[] getMappings() {
		PckXSDMapping[] result = new PckXSDMapping[pkgMap.values().size()];
		Collection values = pkgMap.values();
		return (PckXSDMapping[]) values.toArray(result);
	}

	public void setMappings(PckXSDMapping[] mappings) {
		this.pkgMap.clear();
		for (int i = 0; i < mappings.length; i++) {
			pkgMap.put(mappings[i].pkg, mappings[i]);
		}
	}

	public String getDefaultSchemaLocation() {
		return defaultMapping.xsdLocation;
	}

	public String getDefaultSchemaName() {
		return defaultMapping.xsdName;
	}

	public void setDefaultSchemaLocation(String location) {
		defaultMapping.xsdLocation = location;
	}

	public void setDefaultSchemaName(String name) {
		defaultMapping.xsdName = name;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.defaultMapping.targetNamespace = targetNamespace;
	}

	public void setDefaultUserPrefix(String userPrefix) {
		defaultMapping.userPrefix = userPrefix;
	}

	public String getDefaultUserPrefix() {
		return defaultMapping.userPrefix;
	}

	public String getTargetNamespace() {
		return defaultMapping.getTargetNamespace();
	}

	public void setUseDefaultMapping(boolean use) {
		useDefaultMapping = use;
	}

	public boolean useDefaultMapping() {
		return useDefaultMapping;
	}

	/**
	 * Returns a list of all mapped packages in this mapper
	 * 
	 * @return Collection of Strings
	 */
	public Collection getAllMappedPackages() {
		PckXSDMapping[] mappings = getMappings();
		Collection result = new ArrayList();
		for (int i = 0; i < mappings.length; i++) {
			result.add(mappings[i].getPackage());
		}
		return result;
	}
}
