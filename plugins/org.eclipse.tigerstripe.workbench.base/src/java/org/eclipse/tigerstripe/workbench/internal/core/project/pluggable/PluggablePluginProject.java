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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.util.ReaderInputStream;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.locale.Messages;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.VelocityContextDefinition;
import org.eclipse.tigerstripe.workbench.internal.core.project.AbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties.BooleanPPluginProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties.StringPPluginProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties.TablePPluginProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ArtifactBasedPPluginRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.CopyRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.SimplePPluginRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.runtime.PluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRunRule;
import org.eclipse.tigerstripe.workbench.plugins.IBooleanPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ICopyRule;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IRunRule;
import org.eclipse.tigerstripe.workbench.plugins.ISimpleTemplateRunRule;
import org.eclipse.tigerstripe.workbench.plugins.IStringPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITablePluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateRunRule;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.ITigerstripePluginProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class PluggablePluginProject extends AbstractTigerstripeProject {

	public static final String DEFAULT_FILENAME = ITigerstripeConstants.PLUGIN_DESCRIPTOR;

	@SuppressWarnings("unchecked")
	private final static Class[] SUPPORTED_PROPERTIES = {
			IStringPluginProperty.class, IBooleanPluginProperty.class,
			ITablePluginProperty.class };

	private final static String[] SUPPORTED_PROPERTIES_LABELS = {
			StringPPluginProperty.LABEL, BooleanPPluginProperty.LABEL,
			TablePPluginProperty.LABEL };

	@SuppressWarnings("unchecked")
	private final static Class[] PROPERTIES_IMPL = {
			StringPPluginProperty.class, BooleanPPluginProperty.class,
			TablePPluginProperty.class };

	@SuppressWarnings("unchecked")
	private final static Class[] SUPPORTED_RULES = {
			ISimpleTemplateRunRule.class, ICopyRule.class };

	private final static String[] SUPPORTED_RULES_LABELS = {
			SimplePPluginRule.LABEL, CopyRule.LABEL };

	@SuppressWarnings("unchecked")
	private final static Class[] RULES_IMPL = { SimplePPluginRule.class,
			CopyRule.class };

	@SuppressWarnings("unchecked")
	private final static Class[] SUPPORTED_ARTIFACTRULES = { IArtifactBasedTemplateRunRule.class, };

	private final static String[] SUPPORTED_ARTIFACTRULES_LABELS = { ArtifactBasedPPluginRule.LABEL };

	@SuppressWarnings("unchecked")
	private final static Class[] ARTIFACTRULES_IMPL = { ArtifactBasedPPluginRule.class };

	public static final String ROOT_TAG = "ts_plugin";

	// This defines the compatibility level for the project descriptor;
	public static final String COMPATIBILITY_LEVEL = "1.2";

	public static final String LOGGER = "logger";

	public static final String PLUGIN_NATURE = "pluginNature";

	public static final String GLOBAL_PROPERTIES = "globalProperties";

	public static final String GLOBAL_RULES = "globalRules";

	public static final String ARTIFACT_RULES = "artifactRules";

	public static final String CLASSPATH_ENTRIES = "classpathEntries";

	public static final String ADDITIONAL_FILES = "additionalFiles";

	private List<IPluginProperty> globalProperties;

	private List<IPluginClasspathEntry> classpathEntries;

	private List<IRunRule> globalRules;

	private List<ITemplateRunRule> artifactRules;

	private List<String> additionalFilesInclude;

	private List<String> additionalFilesExclude;

	private EPluggablePluginNature nature;

	private boolean isLogEnabled = false;

	private String logPath = null;

	private int maxRoll = 9;

	private PluginLog.LogLevel defaultLogLevel = PluginLog.LogLevel.ERROR;

	public PluggablePluginProject(File baseDir) {
		super(baseDir, ITigerstripeConstants.PLUGIN_DESCRIPTOR);
		globalProperties = new ArrayList<IPluginProperty>();
		globalRules = new ArrayList<IRunRule>();
		artifactRules = new ArrayList<ITemplateRunRule>();
		classpathEntries = new ArrayList<IPluginClasspathEntry>();
		additionalFilesInclude = new ArrayList<String>();
		additionalFilesExclude = new ArrayList<String>();
		nature = EPluggablePluginNature.Generic;
	}

	public EPluggablePluginNature getPluginNature() {
		return this.nature;
	}

	public void setPluginNature(EPluggablePluginNature nature) {
		this.nature = nature;
	}

	public IPluginClasspathEntry[] getClasspathEntries() {
		return this.classpathEntries
				.toArray(new IPluginClasspathEntry[classpathEntries.size()]);
	}

	public IPluginProperty[] getGlobalProperties() {
		return this.globalProperties
				.toArray(new IPluginProperty[globalProperties.size()]);
	}

	public void setGlobalProperties(IPluginProperty[] properties) {
		this.globalProperties.clear();
		this.globalProperties.addAll(Arrays.asList(properties));
	}

	public <T extends IPluginProperty> Class<T>[] getSupportedPluginProperties() {
		return SUPPORTED_PROPERTIES;
	}

	public String[] getSupportedPluginPropertyLabels() {
		return SUPPORTED_PROPERTIES_LABELS;
	}

	@SuppressWarnings("unchecked")
	public IPluginProperty makeProperty(Class propertyType)
			throws TigerstripeException {
		for (int index = 0; index < SUPPORTED_PROPERTIES.length; index++) {
			Class type = SUPPORTED_PROPERTIES[index];
			if (type == propertyType) {
				Class targetImpl = PROPERTIES_IMPL[index];
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

	public void addAdditionalFile(String relativePath, int includeExclude) {
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

	protected Element buildLoggerElement(Document document) {
		Element logger = document.createElement(LOGGER);
		logger.setAttribute("isEnabled", Boolean.toString(isLogEnabled()));
		logger.setAttribute("defautLevel", String.valueOf(getDefaultLogLevel()
				.toInt()));
		logger.setAttribute("logPath", getLogPath());
		logger.setAttribute("maxRoll", String.valueOf(maxRoll));
		return logger;
	}

	protected Element buildNatureElement(Document document) {
		Element natureElm = document.createElement(PLUGIN_NATURE);
		natureElm.setAttribute("type", getPluginNature().name());
		return natureElm;
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

	protected Element buildGlobalRulesElement(Document document) {
		Element globalProperties = document.createElement(GLOBAL_RULES);

		for (IRunRule rule : getGlobalRules()) {
			Element propElm = document.createElement("rule");
			propElm.setAttribute("name", rule.getName());
			propElm.setAttribute("type", rule.getType());
			propElm.setAttribute("description", rule.getDescription());
			propElm.setAttribute("enabled", rule.isEnabledStr());

			if (rule instanceof ITemplateRunRule) {
				ITemplateRunRule tRule = (ITemplateRunRule) rule;
				for (VelocityContextDefinition def : tRule
						.getVelocityContextDefinitions()) {
					Element ctx = document.createElement("contextEntry");
					ctx.setAttribute("entry", def.getName());
					ctx.setAttribute("classname", def.getClassname());
					propElm.appendChild(ctx);
				}
			}
			propElm.appendChild(rule.getBodyAsNode(document));
			globalProperties.appendChild(propElm);
		}

		return globalProperties;
	}

	protected Element buildArtifactRulesElement(Document document) {
		Element artifactRules = document.createElement(ARTIFACT_RULES);

		for (ITemplateRunRule rule : getArtifactRules()) {
			Element propElm = document.createElement("rule");
			propElm.setAttribute("name", rule.getName());
			propElm.setAttribute("type", rule.getType());
			propElm.setAttribute("description", rule.getDescription());
			propElm.setAttribute("enabled", rule.isEnabledStr());

			for (VelocityContextDefinition def : rule
					.getVelocityContextDefinitions()) {
				Element ctx = document.createElement("contextEntry");
				ctx.setAttribute("entry", def.getName());
				ctx.setAttribute("classname", def.getClassname());
				propElm.appendChild(ctx);
			}

			propElm.appendChild(rule.getBodyAsNode(document));
			artifactRules.appendChild(propElm);
		}

		return artifactRules;
	}

	@Override
	protected Document buildDOM() {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();

			// The Tigerstripe root
			Element root = document.createElement(ROOT_TAG);
			root.setAttribute("version", COMPATIBILITY_LEVEL);
			document.appendChild(root);

			root.appendChild(buildDetailsElement(document));
			root.appendChild(buildNatureElement(document));
			root.appendChild(buildLoggerElement(document));
			root.appendChild(buildGlobalPropertiesElement(document));
			root.appendChild(buildGlobalRulesElement(document));
			root.appendChild(buildArtifactRulesElement(document));
			root.appendChild(buildClasspathEntriesElement(document));
			root.appendChild(buildAdditionalFilesElement(document));
			// root.appendChild(buildPluginsElement(document));
			// root.appendChild(buildDependenciesElement(document));
			// root.appendChild(buildReferencesElement(document));
			root.appendChild(buildAdvancedElement(document));
		} catch (ParserConfigurationException e) {
			TigerstripeRuntime.logErrorMessage(
					"ParserConfigurationException detected", e);
		}
		return document;
	}

	@Override
	public void parse(Reader reader) throws TigerstripeException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document;

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream stream = new ReaderInputStream(reader);
			document = builder.parse(stream);

			// Load the descriptor version
			NodeList roots = document.getElementsByTagName(ROOT_TAG);
			if (roots.getLength() != 1)
				throw new TigerstripeException(Messages.formatMessage(
						Messages.INVALID_DESCRIPTOR, null));
			else {
				Element root = (Element) roots.item(0);
				this.descriptorVersion = root.getAttribute("version");
				if (descriptorVersion == null || "".equals(descriptorVersion)) {
					descriptorVersion = "1.0.x";
				}
			}

			loadProjectDetails(document);
			loadPluginNature(document);
			loadLogger(document);
			loadGlobalProperties(document);
			loadGlobalRules(document);
			loadArtifactRules(document);
			loadClasspathEntries(document);
			loadAdditionalFiles(document);
			// loadDependencies(document);
			// loadReferences(document);
			loadAdvancedProperties(document);

		} catch (SAXParseException spe) {
			TigerstripeRuntime.logErrorMessage("SAXParseException detected",
					spe);
			Object[] args = new Object[2];
			args[0] = spe.getMessage();
			args[1] = new Integer(spe.getLineNumber());
			throw new TigerstripeException(Messages.formatMessage(
					Messages.XML_PARSING_ERROR, args), spe);
		} catch (SAXException sxe) {
			// Error generated during parsing)
			Exception x = sxe;
			if (sxe.getException() != null) {
				x = sxe.getException();
			}
			throw new TigerstripeException(Messages.UNKNOWN_ERROR, x);
		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			throw new TigerstripeException(Messages.UNKNOWN_ERROR, pce);
		} catch (IOException ioe) {
			// I/O error
			throw new TigerstripeException(Messages.UNKNOWN_ERROR, ioe);
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
			}
		}
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

	protected void loadPluginNature(Document document) {
		NodeList natureList = document.getElementsByTagName(PLUGIN_NATURE);
		if (natureList.getLength() != 1)
			return;

		Element nature = (Element) natureList.item(0);
		this.nature = EPluggablePluginNature.valueOf(nature
				.getAttribute("type"));
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
			String typeStr = property.getAttribute("type");
			String tipToolText = property.getAttribute("tipToolText");

			// Migration from old namespace:
			if (typeStr
					.startsWith("com.tigerstripesoftware.api.plugins.pluggable")) {
				typeStr = "org.eclipse.tigerstripe.workbench.plugins."
						+ typeStr.substring(typeStr.lastIndexOf(".") + 1);
			}

			try {
				@SuppressWarnings("unchecked")
				Class type = PluggablePluginProject.class.getClassLoader()
						.loadClass(typeStr);

				IPluginProperty prop = makeProperty(type);
				prop.setName(name);
				prop.setProject(getHandle());
				prop.setTipToolText(tipToolText);
				prop.buildBodyFromNode(property);
				globalProperties.add(prop);
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			} catch (ClassNotFoundException e) {
				BasePlugin.log(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void loadGlobalRules(Document document) {

		globalRules = new ArrayList<IRunRule>();

		NodeList globalProps = document.getElementsByTagName(GLOBAL_RULES);
		if (globalProps.getLength() != 1)
			return;

		Element globals = (Element) globalProps.item(0);
		NodeList rules = globals.getElementsByTagName("rule");
		for (int index = 0; index < rules.getLength(); index++) {
			Element rule = (Element) rules.item(index);
			String name = rule.getAttribute("name");
			String typeStr = rule.getAttribute("type");
			String description = rule.getAttribute("description");
			String enabled = "true";
			if (rule.hasAttribute("enabled")) {
				enabled = rule.getAttribute("enabled");
			}

			// Migration from old namespace:
			if (typeStr
					.startsWith("com.tigerstripesoftware.api.plugins.pluggable")) {
				typeStr = "org.eclipse.tigerstripe.workbench.plugins."
						+ typeStr.substring(typeStr.lastIndexOf(".") + 1);
			}
			try {
				Class type = Class.forName(typeStr);
				IRunRule prop = makeRule(type);
				prop.setName(name);
				prop.setProject(getHandle());
				prop.setDescription(description);
				prop.setEnabledStr(enabled);

				if (prop instanceof ITemplateRunRule) {
					ITemplateRunRule tRunRule = (ITemplateRunRule) prop;
					NodeList contextEntries = rule
							.getElementsByTagName("contextEntry");
					for (int i = 0; i < contextEntries.getLength(); i++) {
						Element entry = (Element) contextEntries.item(i);
						VelocityContextDefinition def = new VelocityContextDefinition();
						def.setClassname(entry.getAttribute("classname"));
						def.setName(entry.getAttribute("entry"));
						tRunRule.addVelocityContextDefinition(def);
					}
				}

				prop.buildBodyFromNode(rule);
				globalRules.add(prop);
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			} catch (ClassNotFoundException e) {
				BasePlugin.log(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void loadArtifactRules(Document document) {

		artifactRules = new ArrayList<ITemplateRunRule>();

		NodeList globalProps = document.getElementsByTagName(ARTIFACT_RULES);
		if (globalProps.getLength() != 1)
			return;

		Element globals = (Element) globalProps.item(0);
		NodeList rules = globals.getElementsByTagName("rule");
		for (int index = 0; index < rules.getLength(); index++) {
			Element rule = (Element) rules.item(index);
			String name = rule.getAttribute("name");
			String typeStr = rule.getAttribute("type");

			// Migration from old namespace:
			if (typeStr
					.startsWith("com.tigerstripesoftware.api.plugins.pluggable")) {
				typeStr = "org.eclipse.tigerstripe.workbench.plugins."
						+ typeStr.substring(typeStr.lastIndexOf(".") + 1);
			}
			String description = rule.getAttribute("description");
			String enabled = "true";
			if (rule.hasAttribute("enabled")) {
				enabled = rule.getAttribute("enabled");
			}
			try {
				Class type = Class.forName(typeStr);
				IRunRule rProp = makeRule(type);
				if (rProp instanceof ITemplateRunRule) {
					ITemplateRunRule prop = (ITemplateRunRule) rProp;
					prop.setName(name);
					prop.setProject(getHandle());
					prop.setDescription(description);
					prop.setEnabledStr(enabled);

					NodeList contextEntries = rule
							.getElementsByTagName("contextEntry");
					for (int i = 0; i < contextEntries.getLength(); i++) {
						Element entry = (Element) contextEntries.item(i);
						VelocityContextDefinition def = new VelocityContextDefinition();
						def.setClassname(entry.getAttribute("classname"));
						def.setName(entry.getAttribute("entry"));
						prop.addVelocityContextDefinition(def);
					}

					prop.buildBodyFromNode(rule);
					artifactRules.add(prop);
				}
			} catch (TigerstripeException e) {
				// just ignore for now
			} catch (ClassNotFoundException e) {
				BasePlugin.log(e);
			}
		}
	}

	public ITigerstripePluginProject getHandle() {
		try {
			return (ITigerstripePluginProject) TigerstripeCore
					.findProject(getBaseDir().toURI());
		} catch (Exception e) {
			return null; // should never happen
		}
	}

	@Override
	public boolean requiresDescriptorUpgrade() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid() {
		boolean superValid = super.isValid();
		boolean isValid = true;

		if (getProjectDetails() == null) {
			isValid = false;
		} else if (getProjectDetails().getName() == null
				|| getProjectDetails().getName().length() == 0) {
			isValid = false;
		}

		return superValid & isValid;
	}

	public void addGlobalProperties(IPluginProperty[] properties) {
		globalProperties.addAll(Arrays.asList(properties));
	}

	public void addGlobalProperty(IPluginProperty property) {
		globalProperties.add(property);
	}

	public void removeGlobalProperties(IPluginProperty[] properties) {
		globalProperties.removeAll(Arrays.asList(properties));
	}

	public void removeGlobalProperty(IPluginProperty property) {
		globalProperties.remove(property);
	}

	public void addGlobalRules(IRunRule[] rules) {
		globalRules.addAll(Arrays.asList(rules));
	}

	public void addGlobalRule(IRunRule rule) {
		globalRules.add(rule);
	}

	public void removeGlobalRules(IRunRule[] rules) {
		globalRules.removeAll(Arrays.asList(rules));
	}

	public void removeGlobalRule(IRunRule rule) {
		globalRules.remove(rule);
	}

	public IRunRule[] getGlobalRules() {
		return this.globalRules.toArray(new IRunRule[globalRules.size()]);
	}

	public <T extends IRunRule> Class<T>[] getSupportedPluginRules() {
		return SUPPORTED_RULES;
	}

	public String[] getSupportedPluginRuleLabels() {
		return SUPPORTED_RULES_LABELS;
	}

	public void addArtifactRules(ITemplateRunRule[] rules) {
		artifactRules.addAll(Arrays.asList(rules));
	}

	public void addArtifactRule(ITemplateRunRule rule) {
		artifactRules.add(rule);
	}

	public void removeArtifactRules(ITemplateRunRule[] rules) {
		artifactRules.removeAll(Arrays.asList(rules));
	}

	public void removeArtifactRule(ITemplateRunRule rule) {
		artifactRules.remove(rule);
	}

	public ITemplateRunRule[] getArtifactRules() {
		return this.artifactRules.toArray(new ITemplateRunRule[artifactRules
				.size()]);
	}

	public <T extends IArtifactBasedTemplateRunRule> Class<T>[] getSupportedPluginArtifactRules() {
		return SUPPORTED_ARTIFACTRULES;
	}

	public String[] getSupportedPluginArtifactRuleLabels() {
		return SUPPORTED_ARTIFACTRULES_LABELS;
	}

	public void addClasspathEntry(IPluginClasspathEntry entry) {
		classpathEntries.add(entry);
	}

	public void removeClasspathEntry(IPluginClasspathEntry entry) {
		classpathEntries.remove(entry);
	}

	public void removeClasspathEntries(IPluginClasspathEntry[] entries) {
		for (IPluginClasspathEntry entry : entries) {
			removeClasspathEntry(entry);
		}
	}

	public IPluginClasspathEntry makeClasspathEntry() {
		return new PluginClasspathEntry();
	}

	public <T extends IRunRule> IRunRule makeRule(Class<T> ruleType)
			throws TigerstripeException {

		// First look thru list of Global Rules
		for (int index = 0; index < SUPPORTED_RULES.length; index++) {
			@SuppressWarnings("unchecked")
			Class type = SUPPORTED_RULES[index];
			if (type == ruleType) {
				@SuppressWarnings("unchecked")
				Class targetImpl = RULES_IMPL[index];
				try {
					IRunRule result = (IRunRule) targetImpl.newInstance();
					return result;
				} catch (IllegalAccessException e) {
					throw new TigerstripeException("Couldn't instantiate "
							+ ruleType + ": " + e.getMessage(), e);
				} catch (InstantiationException e) {
					throw new TigerstripeException("Couldn't instantiate "
							+ ruleType + ": " + e.getMessage(), e);
				}
			}
		}

		// then look thru list of Artifact Rules
		for (int index = 0; index < SUPPORTED_ARTIFACTRULES.length; index++) {
			Class type = SUPPORTED_ARTIFACTRULES[index];
			if (type == ruleType) {
				@SuppressWarnings("unchecked")
				Class targetImpl = ARTIFACTRULES_IMPL[index];
				try {
					ITemplateRunRule result = (ITemplateRunRule) targetImpl
							.newInstance();
					return result;
				} catch (IllegalAccessException e) {
					throw new TigerstripeException("Couldn't instantiate "
							+ ruleType + ": " + e.getMessage(), e);
				} catch (InstantiationException e) {
					throw new TigerstripeException("Couldn't instantiate "
							+ ruleType + ": " + e.getMessage(), e);
				}
			}
		}
		throw new TigerstripeException("Un-supported rule type " + ruleType);
	}

	public boolean isLogEnabled() {
		return isLogEnabled;
	}

	public void setLogEnabled(boolean isLogEnabled) {
		this.isLogEnabled = isLogEnabled;
	}

	public String getLogPath() {
		if (logPath == null) {
			logPath = getProjectLabel() + ".log";
		}
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public PluginLog.LogLevel getDefaultLogLevel() {
		return this.defaultLogLevel;
	}

	public void setDefaultLogLevel(PluginLog.LogLevel defaultLogLevel) {
		this.defaultLogLevel = defaultLogLevel;
	}
}
