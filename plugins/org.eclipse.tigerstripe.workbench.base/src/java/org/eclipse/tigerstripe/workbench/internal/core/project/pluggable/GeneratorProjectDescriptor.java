/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.core.project.AbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties.BooleanPPluginProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties.StringPPluginProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties.TablePPluginProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.runtime.PluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IBooleanPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IStringPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITablePluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.ITigerstripePluginProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class GeneratorProjectDescriptor extends
		AbstractTigerstripeProject {

	@SuppressWarnings("unchecked")
	private final static Class[] SUPPORTED_PROPERTIES = {
			IStringPluginProperty.class, IBooleanPluginProperty.class,
			ITablePluginProperty.class };

	@SuppressWarnings("unchecked")
	private final static Class[] PROPERTIES_IMPL = {
			StringPPluginProperty.class, BooleanPPluginProperty.class,
			TablePPluginProperty.class };

	private final static String[] SUPPORTED_PROPERTIES_LABELS = {
			StringPPluginProperty.LABEL, BooleanPPluginProperty.LABEL,
			TablePPluginProperty.LABEL };

	public static final String PLUGIN_NATURE = "pluginNature";
	public static final String LOGGER = "logger";
	public static final String GLOBAL_PROPERTIES = "globalProperties";
	public static final String CLASSPATH_ENTRIES = "classpathEntries";
	public static final String ADDITIONAL_FILES = "additionalFiles";

	// The nature of this generator
	private EPluggablePluginNature pluginNature;

	// Logger related details
	private boolean isLogEnabled = false;
	private String logPath = null;
	private int maxRoll = 9;
	private PluginLog.LogLevel defaultLogLevel = PluginLog.LogLevel.ERROR;

	// Generator Properties details
	private List<IPluginProperty> globalProperties;

	// Generator Classpath Entry details
	private List<IPluginClasspathEntry> classpathEntries;

	// Generator Additional files details
	private List<String> additionalFilesInclude;
	private List<String> additionalFilesExclude;

	protected GeneratorProjectDescriptor(IContainer projectContainer,
			String descriptorFilename) {
		super(projectContainer.getLocation().toFile(), descriptorFilename);
		globalProperties = new ArrayList<IPluginProperty>();
		classpathEntries = new ArrayList<IPluginClasspathEntry>();
		additionalFilesInclude = new ArrayList<String>();
		additionalFilesExclude = new ArrayList<String>();
	}

	/**
	 * @deprecated use GeneratorProjectDescriptor( IFolder, String ) instead
	 * @return
	 */
	protected GeneratorProjectDescriptor(File projectFolder,
			String descriptorFilename) {
		super(projectFolder, descriptorFilename);
		globalProperties = new ArrayList<IPluginProperty>();
		classpathEntries = new ArrayList<IPluginClasspathEntry>();
		additionalFilesInclude = new ArrayList<String>();
		additionalFilesExclude = new ArrayList<String>();
	}

	public String[] getSupportedPluginPropertyLabels() {
		return SUPPORTED_PROPERTIES_LABELS;
	}

	@SuppressWarnings("unchecked")
	public <T extends IPluginProperty> Class<T>[] getSupportedProperties() {
		return SUPPORTED_PROPERTIES;
	}

	@SuppressWarnings("unchecked")
	public Class[] getSupportedPropertiesImpl() {
		return PROPERTIES_IMPL;
	}

	public EPluggablePluginNature getPluginNature() {
		return pluginNature;
	}

	public void setPluginNature(EPluggablePluginNature pluginNature) {
		this.pluginNature = pluginNature;
	}

	public boolean isLogEnabled() {
		return isLogEnabled;
	}

	public void setLogEnabled(boolean isLogEnabled) {
		setDirty();
		this.isLogEnabled = isLogEnabled;
	}

	public String getLogPath() {
		if (logPath == null) {
			logPath = getProjectLabel() + ".log";
		}
		return logPath;
	}

	public void setLogPath(String logPath) {
		setDirty();
		this.logPath = logPath;
	}

	public PluginLog.LogLevel getDefaultLogLevel() {
		return this.defaultLogLevel;
	}

	public void setDefaultLogLevel(PluginLog.LogLevel defaultLogLevel) {
		setDirty();
		this.defaultLogLevel = defaultLogLevel;
	}

	public IPluginProperty[] getGlobalProperties() {
		return globalProperties.toArray(new IPluginProperty[globalProperties
				.size()]);
	}

	public void setGlobalProperties(IPluginProperty[] properties) {
		setDirty();
		this.globalProperties.clear();
		this.globalProperties.addAll(Arrays.asList(properties));
		for (IPluginProperty prop : globalProperties) {
			if (prop instanceof IContainedObject) {
				IContainedObject bProp = (IContainedObject) prop;
				bProp.setContainer(this);
			} else {
				throw new IllegalArgumentException("Property: "
						+ prop.getName() + " of class "
						+ prop.getClass().getName()
						+ " doesn't implement IContainedObject.");
			}
		}
	}

	/**
	 * 
	 * @param property
	 */
	public void addGlobalProperty(IPluginProperty property) {
		setDirty();
		if (!globalProperties.contains(property)) {
			globalProperties.add(property);
			if (property instanceof IContainedObject) {
				IContainedObject bProp = (IContainedObject) property;
				bProp.setContainer(this);
			} else {
				throw new IllegalArgumentException("Property: "
						+ property.getName() + " of class "
						+ property.getClass().getName()
						+ " doesn't implement IContainedObject.");
			}
		}
	}

	public void removeGlobalProperties(IPluginProperty[] properties) {
		for (IPluginProperty property : properties) {
			removeGlobalProperty(property);
		}
	}

	public void removeGlobalProperty(IPluginProperty property) {
		setDirty();
		globalProperties.remove(property);
		if (property instanceof IContainedObject) {
			IContainedObject bProp = (IContainedObject) property;
			bProp.setContainer(null);
		}
	}

	@SuppressWarnings("unchecked")
	public IPluginProperty makeProperty(Class propertyType)
			throws TigerstripeException {
		for (int index = 0; index < getSupportedProperties().length; index++) {
			Class type = getSupportedProperties()[index];
			if (type == propertyType) {
				Class targetImpl = getSupportedPropertiesImpl()[index];
				try {
					IPluginProperty result = (IPluginProperty) targetImpl
							.newInstance();
					return result;
				} catch (IllegalAccessException e) {
					throw new TigerstripeException("Couldn't instantiate "
							+ propertyType + ": " + e.getMessage(), e);
				} catch (InstantiationException e) {
					throw new TigerstripeException("Couldn't instantiate "
							+ propertyType + ": " + e.getMessage(), e);
				}
			}
		}
		throw new TigerstripeException("Un-supported property type "
				+ propertyType);
	}

	public IPluginClasspathEntry[] getClasspathEntries() {
		return this.classpathEntries
				.toArray(new IPluginClasspathEntry[classpathEntries.size()]);
	}

	public void addClasspathEntry(IPluginClasspathEntry entry) {
		setDirty();
		if (!classpathEntries.contains(entry)) {
			classpathEntries.add(entry);
			if (entry instanceof IContainedObject) {
				IContainedObject bEntry = (IContainedObject) entry;
				bEntry.setContainer(this);
			} else {
				throw new IllegalArgumentException("ClasspathEntry: "
						+ entry.getRelativePath() + " of class "
						+ entry.getClass().getName()
						+ " doesn't implement IContainedObject.");
			}
		}
	}

	public void removeClasspathEntry(IPluginClasspathEntry entry) {
		setDirty();
		classpathEntries.remove(entry);
		if (entry instanceof IContainedObject) {
			IContainedObject bEntry = (IContainedObject) entry;
			bEntry.setContainer(null);
		}

	}

	public void removeClasspathEntries(IPluginClasspathEntry[] entries) {
		for (IPluginClasspathEntry entry : entries) {
			removeClasspathEntry(entry);
		}
	}

	public IPluginClasspathEntry makeClasspathEntry() {
		return new PluginClasspathEntry();
	}

	public List<String> getAdditionalFiles(int includeExclude) {
		switch (includeExclude) {
		case ITigerstripePluginProject.ADDITIONAL_FILE_INCLUDE:
			return additionalFilesInclude;
		case ITigerstripePluginProject.ADDITIONAL_FILE_EXCLUDE:
			return additionalFilesExclude;
		default:
			return additionalFilesInclude;
		}
	}

	public void addAdditionalFile(String relativePath, int includeExclude) {
		setDirty();
		switch (includeExclude) {
		case ITigerstripePluginProject.ADDITIONAL_FILE_INCLUDE:
			if (!additionalFilesInclude.contains(relativePath)) {
				additionalFilesInclude.add(relativePath);
			}
			break;
		case ITigerstripePluginProject.ADDITIONAL_FILE_EXCLUDE:
			if (!additionalFilesExclude.contains(relativePath)) {
				additionalFilesExclude.add(relativePath);
			}
		}
	}

	public void removeAdditionalFile(String relativePath, int includeExclude) {
		setDirty();
		switch (includeExclude) {
		case ITigerstripePluginProject.ADDITIONAL_FILE_INCLUDE:
			if (additionalFilesInclude.contains(relativePath)) {
				additionalFilesInclude.remove(relativePath);
			}
			break;
		case ITigerstripePluginProject.ADDITIONAL_FILE_EXCLUDE:
			if (additionalFilesExclude.contains(relativePath)) {
				additionalFilesExclude.remove(relativePath);
			}
		}
	}

	// All element builds for XML
	protected Element buildNatureElement(Document document) {
		Element natureElm = document.createElement(PLUGIN_NATURE);
		natureElm.setAttribute("type", getPluginNature().name());
		return natureElm;
	}

	protected Element buildLoggerElement(Document document) {
		Element logger = document.createElement(LOGGER);
		logger.setAttribute("isEnabled", Boolean.toString(isLogEnabled()));
		logger.setAttribute("defautLevel", String.valueOf(getDefaultLogLevel()
				.toInt()));
		logger.setAttribute("logPath", getLogPath());
		logger.setAttribute("maxRoll", String.valueOf(maxRoll));
		return logger;
	}

	protected Element buildGlobalPropertiesElement(Document document) {
		Element globalProperties = document.createElement(GLOBAL_PROPERTIES);

		for (IPluginProperty prop : getGlobalProperties()) {
			Element propElm = document.createElement("property");
			propElm.setAttribute("name", prop.getName());
			propElm.setAttribute("type", prop.getType());
			propElm.setAttribute("tipToolText", prop.getTipToolText());

			propElm.appendChild(prop.getBodyAsNode(document));
			globalProperties.appendChild(propElm);
		}

		return globalProperties;
	}

	protected Element buildClasspathEntriesElement(Document document) {
		Element classpathEntriesProperties = document
				.createElement(CLASSPATH_ENTRIES);

		for (IPluginClasspathEntry entry : getClasspathEntries()) {
			Element propElm = document.createElement("entry");
			propElm.setAttribute("relativePath", entry.getRelativePath());
			classpathEntriesProperties.appendChild(propElm);
		}

		return classpathEntriesProperties;
	}

	protected Element buildAdditionalFilesElement(Document document) {
		Element additionalFilesElement = document
				.createElement(ADDITIONAL_FILES);

		for (String entry : getAdditionalFiles(ITigerstripePluginProject.ADDITIONAL_FILE_INCLUDE)) {
			Element propElm = document.createElement("includeEntry");
			propElm.setAttribute("relativePath", entry);
			additionalFilesElement.appendChild(propElm);
		}

		for (String entry : getAdditionalFiles(ITigerstripePluginProject.ADDITIONAL_FILE_EXCLUDE)) {
			Element propElm = document.createElement("excludeEntry");
			propElm.setAttribute("relativePath", entry);
			additionalFilesElement.appendChild(propElm);
		}
		return additionalFilesElement;

	}

	protected void loadPluginNature(Document document) {
		NodeList natureList = document.getElementsByTagName(PLUGIN_NATURE);
		if (natureList.getLength() != 1)
			return;

		Element nature = (Element) natureList.item(0);
		this.pluginNature = EPluggablePluginNature.valueOf(nature
				.getAttribute("type"));
	}

	protected void loadLogger(Document document) {

		NodeList loggerList = document.getElementsByTagName(LOGGER);
		if (loggerList.getLength() != 1)
			return;

		Element logger = (Element) loggerList.item(0);
		isLogEnabled = "true"
				.equalsIgnoreCase(logger.getAttribute("isEnabled"));
		defaultLogLevel = PluginLog.LogLevel.fromInt(Integer.valueOf(logger
				.getAttribute("defautLevel")));
		logPath = logger.getAttribute("logPath");
		maxRoll = Integer.valueOf(logger.getAttribute("maxRoll"));
	}

	protected void loadGlobalProperties(Document document) {

		globalProperties = new ArrayList<IPluginProperty>();

		NodeList globalProps = document.getElementsByTagName(GLOBAL_PROPERTIES);
		if (globalProps.getLength() != 1)
			return;

		Element globals = (Element) globalProps.item(0);
		NodeList properties = globals.getElementsByTagName("property");
		for (int index = 0; index < properties.getLength(); index++) {
			Element property = (Element) properties.item(index);
			String name = property.getAttribute("name");
			String typeStr = MigrationHelper.pluginMigrateProperty(property
					.getAttribute("type"));
			String tipToolText = property.getAttribute("tipToolText");

			try {
				@SuppressWarnings("unchecked")
				Class type = PluggablePluginProject.class.getClassLoader()
						.loadClass(typeStr);

				IPluginProperty prop = makeProperty(type);
				prop.setName(name);
				prop.setTipToolText(tipToolText);
				prop.buildBodyFromNode(property);
				globalProperties.add(prop);

				// Make sure to set the container for this property. If not
				// possible
				// throw a runtime exception
				if (prop instanceof IContainedObject) {
					IContainedObject bEntry = (IContainedObject) prop;
					bEntry.setContainer(this);
				} else {
					throw new IllegalArgumentException("Property: "
							+ prop.getName() + " of class "
							+ prop.getClass().getName()
							+ " doesn't implement IContainedObject.");
				}

			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			} catch (ClassNotFoundException e) {
				BasePlugin.log(e);
			}
		}
	}

	protected void loadClasspathEntries(Document document) {
		classpathEntries = new ArrayList<IPluginClasspathEntry>();

		NodeList entryProps = document.getElementsByTagName(CLASSPATH_ENTRIES);
		if (entryProps.getLength() != 1)
			return;

		Element entriesRoot = (Element) entryProps.item(0);
		NodeList entries = entriesRoot.getElementsByTagName("entry");
		for (int index = 0; index < entries.getLength(); index++) {
			Element entry = (Element) entries.item(index);
			String relPath = entry.getAttribute("relativePath");
			if (relPath != null && relPath.length() != 0) {
				IPluginClasspathEntry anEntry = makeClasspathEntry();
				anEntry.setRelativePath(relPath);
				classpathEntries.add(anEntry);

				if (anEntry instanceof IContainedObject) {
					IContainedObject bEntry = (IContainedObject) anEntry;
					bEntry.setContainer(this);
				} else {
					throw new IllegalArgumentException("ClasspathEntry: "
							+ anEntry.getRelativePath() + " of class "
							+ anEntry.getClass().getName()
							+ " doesn't implement IContainedObject.");
				}

			}
		}
	}

	protected void loadAdditionalFiles(Document document) {
		additionalFilesInclude = new ArrayList<String>();
		additionalFilesExclude = new ArrayList<String>();

		NodeList entryProps = document.getElementsByTagName(ADDITIONAL_FILES);
		if (entryProps.getLength() != 1)
			return;

		Element entriesRoot = (Element) entryProps.item(0);
		NodeList entries = entriesRoot.getElementsByTagName("includeEntry");
		for (int index = 0; index < entries.getLength(); index++) {
			Element entry = (Element) entries.item(index);
			String relPath = entry.getAttribute("relativePath");
			if (relPath != null && relPath.length() != 0) {
				additionalFilesInclude.add(relPath);
			}
		}
		entries = entriesRoot.getElementsByTagName("excludeEntry");
		for (int index = 0; index < entries.getLength(); index++) {
			Element entry = (Element) entries.item(index);
			String relPath = entry.getAttribute("relativePath");
			if (relPath != null && relPath.length() != 0) {
				additionalFilesExclude.add(relPath);
			}
		}
	}

}
