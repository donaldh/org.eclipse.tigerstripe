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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.util.ReaderInputStream;
import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.locale.Messages;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.VelocityContextDefinition;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ArtifactRunnableRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.CopyRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.GlobalRunnableRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.GlobalTemplateRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.Rule;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRule;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.ICopyRule;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class PluggablePluginProject extends GeneratorProjectDescriptor {

	public static final String DEFAULT_FILENAME = ITigerstripeConstants.PLUGIN_DESCRIPTOR;

	@SuppressWarnings("unchecked")
	private final static Class[] SUPPORTED_RULES = { IGlobalTemplateRule.class,
		IGlobalRunnableRule.class,ICopyRule.class };

	private final static String[] SUPPORTED_RULES_LABELS = {
			GlobalTemplateRule.LABEL, GlobalRunnableRule.LABEL, CopyRule.LABEL };

	@SuppressWarnings("unchecked")
	private final static Class[] RULES_IMPL = { GlobalTemplateRule.class,
		GlobalRunnableRule.class,
			CopyRule.class };

	@SuppressWarnings("unchecked")
	private final static Class[] SUPPORTED_ARTIFACTRULES = { IArtifactBasedTemplateRule.class, 
			IArtifactRunnableRule.class};

	private final static String[] SUPPORTED_ARTIFACTRULES_LABELS = { ArtifactBasedTemplateRule.LABEL,
		ArtifactRunnableRule.LABEL};

	@SuppressWarnings("unchecked")
	private final static Class[] ARTIFACTRULES_IMPL = { ArtifactBasedTemplateRule.class,
		ArtifactRunnableRule.class};

	public static final String ROOT_TAG = "ts_plugin";

	// This defines the compatibility level for the project descriptor;
	public static final String COMPATIBILITY_LEVEL = "1.2";

	public static final String ARTIFACT_RULES = "artifactRules";

	private List<IArtifactRule> artifactRules;

	public PluggablePluginProject(File baseDir) {
		super(baseDir, ITigerstripeConstants.PLUGIN_DESCRIPTOR);
		artifactRules = new ArrayList<IArtifactRule>();
		setPluginNature(EPluggablePluginNature.Generic);
	}

	protected Element buildArtifactRulesElement(Document document) {
		Element artifactRules = document.createElement(ARTIFACT_RULES);

		for (IArtifactRule rule : getArtifactRules()) {
			Element propElm = document.createElement("rule");
			propElm.setAttribute("name", rule.getName());
			propElm.setAttribute("type", rule.getType());
			propElm.setAttribute("description", rule.getDescription());
			propElm.setAttribute("enabled", String.valueOf(rule.isEnabled()));

			if (rule instanceof ITemplateBasedRule){
			for (VelocityContextDefinition def : ((ITemplateBasedRule) rule)
					.getVelocityContextDefinitions()) {
				Element ctx = document.createElement("contextEntry");
				ctx.setAttribute("entry", def.getName());
				ctx.setAttribute("classname", def.getClassname());
				propElm.appendChild(ctx);
			}
			}

			propElm.appendChild(((Rule) rule).getBodyAsNode(document));
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
			root.appendChild(buildAnnotationPluginsElement(document));
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
			loadRequiredAnnotationPlugins(document);

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
	@SuppressWarnings("unchecked")
	protected void loadArtifactRules(Document document) {

		artifactRules = new ArrayList<IArtifactRule>();

		NodeList globalProps = document.getElementsByTagName(ARTIFACT_RULES);
		if (globalProps.getLength() != 1)
			return;

		Element globals = (Element) globalProps.item(0);
		NodeList rules = globals.getElementsByTagName("rule");
		for (int index = 0; index < rules.getLength(); index++) {
			Element rule = (Element) rules.item(index);
			String name = rule.getAttribute("name");
			String typeStr = MigrationHelper.pluginMigrateRuleType(rule
					.getAttribute("type"));

			String description = rule.getAttribute("description");
			String enabled = "true";
			if (rule.hasAttribute("enabled")) {
				enabled = rule.getAttribute("enabled");
			}
			try {
				Class type = Class.forName(typeStr);
				IRule iRule = makeRule(type);
				if (iRule instanceof IArtifactRule) {
					IArtifactRule aRule = (IArtifactRule) iRule;
					aRule.setName(name);
					aRule.setDescription(description);
					aRule.setEnabled(Boolean.parseBoolean(enabled));

					if (aRule instanceof ITemplateBasedRule) {
						ITemplateBasedRule iTplRule = (ITemplateBasedRule) aRule;
						NodeList contextEntries = rule
						.getElementsByTagName("contextEntry");
						for (int i = 0; i < contextEntries.getLength(); i++) {
							Element entry = (Element) contextEntries.item(i);
							VelocityContextDefinition def = new VelocityContextDefinition();
							def.setClassname(entry.getAttribute("classname"));
							def.setName(entry.getAttribute("entry"));
							iTplRule.addVelocityContextDefinition(def);
						}

					}
					((Rule) aRule).buildBodyFromNode(rule);
					addArtifactRule(aRule);
				}
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			} catch (ClassNotFoundException e) {
				BasePlugin.log(e);
			}
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
//		} else if (getProjectDetails().getName() == null
//				|| getProjectDetails().getName().length() == 0) {
//			isValid = false;
		}

		return superValid & isValid;
	}

	@SuppressWarnings("unchecked")
	public <T extends IRule> Class<T>[] getSupportedGlobalRules() {
		return SUPPORTED_RULES;
	}

	public String[] getSupportedPluginRuleLabels() {
		return SUPPORTED_RULES_LABELS;
	}

	public void addArtifactRules(IArtifactRule[] rules) {
		for (IArtifactRule rule : rules) {
			addArtifactRule(rule);
		}
	}

	public void addArtifactRule(IArtifactRule rule) {
		if (!artifactRules.contains(rule)) {
			setDirty();
			artifactRules.add(rule);
			if (rule instanceof IContainedObject) {
				IContainedObject obj = (IContainedObject) rule;
				obj.setContainer(this);
			} else {
				throw new IllegalArgumentException("Rule of type "
						+ rule.getClass().getName()
						+ " must implement IContainedObject.");
			}
		}
	}

	public void removeArtifactRules(IRule[] rules) {
		for (IRule rule : rules) {
			removeArtifactRule(rule);
		}
	}

	public void removeArtifactRule(IRule rule) {
		setDirty();
		artifactRules.remove(rule);
		if (rule instanceof IContainedObject) {
			IContainedObject obj = (IContainedObject) rule;
			obj.setContainer(null);
		}
	}

	public IArtifactRule[] getArtifactRules() {
		return this.artifactRules.toArray(new IArtifactRule[artifactRules
				.size()]);
	}

	@SuppressWarnings("unchecked")
	public <T extends IArtifactRule> Class<T>[] getSupportedPluginArtifactRules() {
		return SUPPORTED_ARTIFACTRULES;
	}

	public String[] getSupportedPluginArtifactRuleLabels() {
		return SUPPORTED_ARTIFACTRULES_LABELS;
	}

	@Override
	protected <T extends IRule> Class<T>[] getSupportedGlobalRulesImpl() {
		return RULES_IMPL;
	}

	@Override
	public <T extends IRule> IRule makeRule(Class<T> ruleType)
			throws TigerstripeException {

		IRule result = super.makeRule(ruleType);
		if (result != null)
			return result;
		else {
			// then look thru list of Artifact Rules
			for (int index = 0; index < SUPPORTED_ARTIFACTRULES.length; index++) {
				@SuppressWarnings("unchecked")
				Class type = SUPPORTED_ARTIFACTRULES[index];
				if (type == ruleType) {
					@SuppressWarnings("unchecked")
					Class targetImpl = ARTIFACTRULES_IMPL[index];
					try {
						result = (IRule) targetImpl.newInstance();
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
		}
		throw new TigerstripeException("Un-supported rule type " + ruleType);
	}

	public void artifactResourceChanged(IResource changedArtifactResource) {
		// TODO Auto-generated method stub
		
	}

}
