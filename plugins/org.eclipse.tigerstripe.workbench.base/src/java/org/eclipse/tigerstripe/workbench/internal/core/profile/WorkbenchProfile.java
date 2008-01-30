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
package org.eclipse.tigerstripe.workbench.internal.core.profile;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IWorkbenchProfileProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method.ReturnTypeWrapper;
import org.eclipse.tigerstripe.workbench.internal.core.profile.primitiveType.PrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.Stereotype;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILabel;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeScopeDetails;

/**
 * Implementation for a Tigerstripe Workbench Profile
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class WorkbenchProfile implements IWorkbenchProfile {

	private List<IStereotype> stereotypes = new ArrayList<IStereotype>();

	private IPrimitiveTypeDef defaultPrimitiveType = null;

	private static List<IPrimitiveTypeDef> reservedPrimitiveTypeDefs = null;

	private List<IPrimitiveTypeDef> primitiveTypeDefs = new ArrayList<IPrimitiveTypeDef>();

	private HashMap<String, IWorkbenchProfileProperty> properties = new HashMap<String, IWorkbenchProfileProperty>();

	public final static String REQUIRED_COMPATIBILITY_LEVEL = "1.2";

	private final static String DEFAULT_TYPE = "String";

	/**
	 * The compatibility level of this IWorkbenchProfile.
	 * 
	 * This is to be compared against the REQUIRED_COMPATIBILITY_LEVEL. If not
	 * compatible the workbench profile cannot be loaded until the workbench
	 * version is upgraded.
	 */
	private String compatibilityLevel = REQUIRED_COMPATIBILITY_LEVEL;

	/**
	 * A user-defined name for this profile
	 */
	private String name = "NO_NAME";

	/**
	 * A user-defined version for this profile
	 */
	private String version = "NO_VERSION";

	/**
	 * A user-defined description for this profile
	 */
	private String description = "NO_DESCRIPTION";

	public void setCompatibilityLevel(String compatibilityLevel) {
		this.compatibilityLevel = compatibilityLevel;
	}

	public String getCompatibilityLevel() {
		return this.compatibilityLevel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public WorkbenchProfile() {
		applyDefaults();
	}

	public String asText() {
		Document document = DocumentFactory.getInstance().createDocument();
		Element rootElem = DocumentFactory.getInstance().createElement(
				IWorkbenchProfile.XML_ROOT_ELEMENT);
		document.setRootElement(rootElem);
		rootElem.addAttribute(XML_COMPATIBILITY_LEVEL_ATTR, compatibilityLevel);
		rootElem.addAttribute(XML_NAME, getName());
		rootElem.addAttribute(XML_VERSION, getVersion());
		rootElem.addElement(XML_DESCRIPTION).setText(getDescription());

		Element defaultPrimitive = rootElem.addElement("defaultPrimitiveType");
		if (getDefaultPrimitiveType() != null) {
			defaultPrimitive
					.add((getDefaultPrimitiveType().asDefaultElement()));
		}

		Element stereotypes = rootElem.addElement("stereotypes");
		for (IStereotype stereotype : getStereotypes()) {
			stereotypes.add(stereotype.asElement());
		}

		Element primitiveTypes = rootElem.addElement("primitiveTypes");
		for (IPrimitiveTypeDef primitiveTypeDef : getPrimitiveTypeDefs(false)) {
			primitiveTypes.add(primitiveTypeDef.asElement());
		}

		Element properties = rootElem.addElement("properties");
		for (String propName : this.properties.keySet()) {
			IWorkbenchProfileProperty prop = this.properties.get(propName);
			Element propertyElem = properties.addElement("property");
			propertyElem.addAttribute("type", prop.getClass().getName());
			propertyElem.addAttribute("name", propName);

			propertyElem.addCDATA(prop.serializeToString());
		}

		OutputFormat format = OutputFormat.createPrettyPrint();
		StringWriter sWriter = new StringWriter();
		XMLWriter xWriter = new XMLWriter(sWriter, format);
		try {
			xWriter.write(document);
			return sWriter.toString();
		} catch (IOException e) {
			TigerstripeRuntime.logErrorMessage("Error while formatting XML: "
					+ e.getMessage(), e);
			return document.asXML();
		}
	}

	public void parse(Reader reader) throws TigerstripeException {

		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(reader);

			internalLoadProfile(document);
			internalLoadStereotypes(document);
			internalLoadPrimitiveTypeDefs(document);
			internalLoadDefaultPrimitveTypeDef(document);
			internalLoadProperties(document);
		} catch (DocumentException e) {
			throw new TigerstripeException("Couldn't parse profile: "
					+ e.getMessage(), e);
		}
	}

	protected void internalLoadPrimitiveTypeDefs(Document document) {
		this.primitiveTypeDefs.clear();
		Element rootElem = document.getRootElement();
		Element stereotypes = rootElem.element("primitiveTypes");
		if (stereotypes != null) {
			for (Iterator stIter = stereotypes.elementIterator("primitiveType"); stIter
					.hasNext();) {
				Element stElm = (Element) stIter.next();
				PrimitiveTypeDef st = new PrimitiveTypeDef();
				this.primitiveTypeDefs.add(st);
				try {
					st.parse(stElm);
				} catch (TigerstripeException e) {
					TigerstripeRuntime
							.logInfoMessage("Error while parsing primitive types: "
									+ e.getMessage());
				}
			}
		}
	}

	protected void internalLoadDefaultPrimitveTypeDef(Document document) {
		Element rootElem = document.getRootElement();
		Element defPtElem = rootElem.element("defaultPrimitiveType");
		if (defPtElem != null) {
			Element ptElm = defPtElem.element("primitiveType");
			PrimitiveTypeDef typeDef = new PrimitiveTypeDef();
			if (ptElm != null) {
				try {
					typeDef.parse(ptElm);
					setDefaultPrimitiveType(typeDef);
				} catch (TigerstripeException e) {
					TigerstripeRuntime
							.logInfoMessage("Error while parsing primitive types: "
									+ e.getMessage());
				}
			}
		}
	}

	protected void internalLoadStereotypes(Document document) {
		this.stereotypes.clear();
		Element rootElem = document.getRootElement();
		Element stereotypes = rootElem.element("stereotypes");
		if (stereotypes != null) {
			for (Iterator stIter = stereotypes.elementIterator("stereotype"); stIter
					.hasNext();) {
				Element stElm = (Element) stIter.next();
				Stereotype st = new Stereotype(this);
				this.stereotypes.add(st);
				try {
					st.parse(stElm);
				} catch (TigerstripeException e) {
					TigerstripeRuntime
							.logInfoMessage("Error while parsing stereotype: "
									+ e.getMessage());
				}
			}
		}
	}

	protected void internalLoadProperties(Document document) {
		this.properties.clear();

		Element rootElem = document.getRootElement();
		Element propertiesElem = rootElem.element("properties");
		if (propertiesElem != null) {
			for (Iterator prIter = propertiesElem.elementIterator("property"); prIter
					.hasNext();) {
				Element prElem = (Element) prIter.next();
				String type = prElem.attributeValue("type");
				String name = prElem.attributeValue("name");
				String content = prElem.getText();
				try {
					Class clazz = Class.forName(type);
					Object instance = clazz.newInstance();
					if (instance instanceof IWorkbenchProfileProperty) {
						IWorkbenchProfileProperty property = (IWorkbenchProfileProperty) instance;
						property.parseFromSerializedString(content);
						this.properties.put(name, property);
					}
				} catch (ClassNotFoundException e) {
					TigerstripeRuntime.logErrorMessage(
							"ClassNotFoundException detected", e);
				} catch (InstantiationException e) {
					TigerstripeRuntime.logErrorMessage(
							"InstantiationException detected", e);
				} catch (IllegalAccessException e) {
					TigerstripeRuntime.logErrorMessage(
							"IllegalAccessException detected", e);
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
		}
		applyDefaults();
	}

	protected void applyDefaults() {
		// Set default properties for those that don't exist (compatiblity)
		if (!properties
				.containsKey(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS)) {
			properties.put(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS,
					new CoreArtifactSettingsProperty());
		}
		if (!properties
				.containsKey(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS)) {
			properties.put(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS,
					new OssjLegacySettingsProperty());
		}
		if (!properties.containsKey(IWorkbenchPropertyLabels.GLOBAL_SETTINGS)) {
			properties.put(IWorkbenchPropertyLabels.GLOBAL_SETTINGS,
					new GlobalSettingsProperty());
		}
	}

	protected void internalLoadProfile(Document document)
			throws TigerstripeException {

		Element rootElem = document.getRootElement();
		if (!rootElem.getName().equals(IWorkbenchProfile.XML_ROOT_ELEMENT))
			throw new TigerstripeException(
					"Not a valid Workbench profile (ROOT_ELEMENT)");

		String compatibilityLevel = rootElem
				.attributeValue(IWorkbenchProfile.XML_COMPATIBILITY_LEVEL_ATTR);
		// FIXME: check compatibility level before going any further
		setCompatibilityLevel(compatibilityLevel);

		String name = rootElem.attributeValue(IWorkbenchProfile.XML_NAME);
		setName(name);

		String version = rootElem.attributeValue(IWorkbenchProfile.XML_VERSION);
		setVersion(version);

		String description = rootElem
				.element(IWorkbenchProfile.XML_DESCRIPTION).getText();
		setDescription(description);

		// // Load stereotypes
		// Element stereotypesElm = rootElem.element("stereotypes");
		// if (stereotypesElm != null) {
		// for (Iterator iter = stereotypesElm.elementIterator("stereotype");
		// iter
		// .hasNext();) {
		// Element stElm = (Element) iter.next();
		// IStereotype st = new Stereotype();
		// st.parse(stElm);
		// addStereotype(st);
		// }
		// }
		//
		// // Load stereotypes
		// Element primitiveTypesElm = rootElem.element("primitiveTypes");
		// if (primitiveTypesElm != null) {
		// for (Iterator iter = primitiveTypesElm
		// .elementIterator("primitiveType"); iter.hasNext();) {
		// Element stElm = (Element) iter.next();
		// IPrimitiveTypeDef st = new PrimitiveTypeDef();
		// st.parse(stElm);
		// addPrimitiveTypeDef(st);
		// }
		// }
	}

	public void addPrimitiveTypeDef(IPrimitiveTypeDef primitiveTypeDef)
			throws TigerstripeException {
		if (primitiveTypeDefs.contains(primitiveTypeDef))
			throw new TigerstripeException("Duplicate Primitive Type '"
					+ primitiveTypeDef.getName() + "' in profile '" + getName()
					+ "'.");
		else {
			primitiveTypeDefs.add(primitiveTypeDef);
		}
	}

	public Collection<IPrimitiveTypeDef> getPrimitiveTypeDefs(boolean includeReservedTypes) {
		if (!includeReservedTypes)
			return Collections.unmodifiableCollection(primitiveTypeDefs);
		else {
			ArrayList<IPrimitiveTypeDef> result = new ArrayList<IPrimitiveTypeDef>();
			result.addAll(primitiveTypeDefs);
			result.addAll(getReservedPrimitiveTypes());
			return Collections.unmodifiableCollection(result);
		}
	}

	public static Collection<IPrimitiveTypeDef> getReservedPrimitiveTypes() {
		if (reservedPrimitiveTypeDefs == null) {
			reservedPrimitiveTypeDefs = new ArrayList<IPrimitiveTypeDef>();
			for (String[] def : IPrimitiveTypeArtifact.reservedPrimitiveTypes) {
				String name = def[0];
				String desc = def[1];
				PrimitiveTypeDef pDef = new PrimitiveTypeDef();
				pDef.setName(name);
				pDef.setDescription(desc);
				pDef.setPackageName(IPrimitiveTypeArtifact.RESERVED);
				pDef.setReserved(true);
				reservedPrimitiveTypeDefs.add(pDef);
			}
		}
		return Collections.unmodifiableCollection(reservedPrimitiveTypeDefs);
	}

	public void removePrimitiveTypeDef(IPrimitiveTypeDef primitiveTypeDef)
			throws TigerstripeException {
		if (primitiveTypeDefs.contains(primitiveTypeDef)) {
			primitiveTypeDefs.remove(primitiveTypeDef);
		} else
			throw new TigerstripeException("Unknown Primitive Type '"
					+ primitiveTypeDef.getName() + "' in profile '" + getName()
					+ "'.");
	}

	public void removePrimitiveTypeDefs(Collection<IPrimitiveTypeDef> primitiveTypeDefs)
			throws TigerstripeException {
		for (IPrimitiveTypeDef primitiveTypeDef : primitiveTypeDefs) {
			removePrimitiveTypeDef(primitiveTypeDef);
		}
	}

	public void setPrimitiveTypeDefs(Collection<IPrimitiveTypeDef> primitiveTypeDefs)
			throws TigerstripeException {
		this.primitiveTypeDefs.clear();
		for (IPrimitiveTypeDef primitiveTypeDef : primitiveTypeDefs) {
			if (this.primitiveTypeDefs.contains(primitiveTypeDef))
				throw new TigerstripeException("Duplicate Primitive Type '"
						+ primitiveTypeDef.getName() + "' in profile '"
						+ getName() + "'.");
			else {
				this.primitiveTypeDefs.add(primitiveTypeDef);
			}
		}
	}

	public IPrimitiveTypeDef getDefaultPrimitiveType() {
		return defaultPrimitiveType;
	}

	public String getDefaultPrimitiveTypeString() {

		if (getDefaultPrimitiveType() == null)
			return DEFAULT_TYPE;

		if (getDefaultPrimitiveType().isReserved())
			return getDefaultPrimitiveType().getName();
		else
			return getDefaultPrimitiveType().getPackageName() + "."
					+ getDefaultPrimitiveType().getName();
	}

	public void setDefaultPrimitiveType(IPrimitiveTypeDef primitiveTypeDef)
			throws TigerstripeException {

		defaultPrimitiveType = null;
		Collection<IPrimitiveTypeDef> allTypeDefs = getPrimitiveTypeDefs(true);

		if (allTypeDefs.contains(primitiveTypeDef)) {
			defaultPrimitiveType = primitiveTypeDef;
		} else
			throw new TigerstripeException(primitiveTypeDef.getPackageName()
					+ "." + primitiveTypeDef.getName()
					+ " has not been defined as a primitive type.");

	}

	public void addStereotype(IStereotype stereotype)
			throws TigerstripeException {
		if (stereotypes.contains(stereotype))
			throw new TigerstripeException("Duplicate Stereotype '"
					+ stereotype.getName() + "' in profile '" + getName()
					+ "'.");
		else {
			stereotypes.add(stereotype);
		}
	}

	public Collection<IStereotype> getStereotypes() {
		return Collections.unmodifiableCollection(stereotypes);
	}

	public void removeStereotype(IStereotype stereotype)
			throws TigerstripeException {
		if (stereotypes.contains(stereotype)) {
			stereotypes.remove(stereotype);
		} else
			throw new TigerstripeException("Unknown Stereotype '"
					+ stereotype.getName() + "' in profile '" + getName()
					+ "'.");
	}

	public void removeStereotypes(Collection<IStereotype> stereotypes)
			throws TigerstripeException {
		for (IStereotype stereotype : stereotypes) {
			removeStereotype(stereotype);
		}
	}

	public void setStereotypes(Collection<IStereotype> stereotypes)
			throws TigerstripeException {
		this.stereotypes.clear();
		for (IStereotype stereotype : stereotypes) {
			if (this.stereotypes.contains(stereotype))
				throw new TigerstripeException("Duplicate Stereotype '"
						+ stereotype.getName() + "' in profile '" + getName()
						+ "'.");
			else {
				this.stereotypes.add(stereotype);
			}
		}
	}

	public IStereotype getStereotypeByName(String name) {
		for (IStereotype stereotype : stereotypes) {
			if (stereotype.getName().equals(name))
				return stereotype;
		}
		return null;
	}


	public Collection<IStereotype> getAvailableStereotypeForCapable(
			IStereotypeCapable capable) {
		ArrayList<IStereotype> result = new ArrayList<IStereotype>();
		// TODO: we might want to cache this?

		for (IStereotype stereotype : stereotypes) {

			IStereotypeScopeDetails details = stereotype
					.getStereotypeScopeDetails();
			if (capable instanceof IField && details.isAttributeLevel()) {
				result.add(stereotype);
			} else if (capable instanceof IMethod && details.isMethodLevel()) {
				result.add(stereotype);
			} else if (capable instanceof ILabel && details.isLabelLevel()) {
				result.add(stereotype);
			} else if ((capable instanceof IArgument || capable instanceof ReturnTypeWrapper)
					&& details.isArgumentLevel()) {
				result.add(stereotype);
			} else if (capable instanceof IAbstractArtifact) {

				if (details.getArtifactLevelTypes() == null) {
					result.add(stereotype);
				} else {
					for (String type : details.getArtifactLevelTypes()) {
						Class[] interfaces = capable.getClass().getInterfaces();
						for (Class inter : interfaces) {
							if (inter.getName().equals(type)) {
								result.add(stereotype);
							}
						}
					}
				}
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	// =================================================================================
	// =================================================================================
	// Property handling
	//
	// =================================================================================

	public IWorkbenchProfileProperty getProperty(String name) {
		if (properties.containsKey(name))
			return properties.get(name);
		else
			return null;
	}

	public void setProperty(String name, IWorkbenchProfileProperty property) {
		properties.put(name, property);
	}

	private long lastModified = -1;

	public long lastModified() {
		return this.lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

}
