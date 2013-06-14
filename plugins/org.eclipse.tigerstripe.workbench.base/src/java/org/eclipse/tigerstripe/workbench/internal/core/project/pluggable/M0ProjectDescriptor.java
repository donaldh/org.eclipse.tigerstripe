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
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.util.ReaderInputStream;
import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.locale.Messages;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.M0GlobalRunnableRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.M0GlobalTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Represents an M0-Level Project Descriptor
 * 
 * @author erdillon
 * 
 */
public class M0ProjectDescriptor extends GeneratorProjectDescriptor {

	private final static String ROOT_TAG = "m0-generator";

	@SuppressWarnings("unchecked")
	private final static Class[] SUPPORTED_RULES = { IGlobalTemplateRule.class, IGlobalRunnableRule.class };

	private final static String[] SUPPORTED_RULES_LABELS = { M0GlobalTemplateRule.LABEL, M0GlobalRunnableRule.LABEL };

	@SuppressWarnings("unchecked")
	private final static Class[] RULES_IMPL = { M0GlobalTemplateRule.class, M0GlobalRunnableRule.class };

	public M0ProjectDescriptor(File baseDir) {
		super(baseDir, ITigerstripeConstants.M0_GENERATOR_DESCRIPTOR);
		setPluginNature(EPluggablePluginNature.M0);
	}

	@Override
	public boolean requiresDescriptorUpgrade() {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IRule> Class<T>[] getSupportedGlobalRules() {
		return SUPPORTED_RULES;
	}

	@Override
	public String[] getSupportedPluginRuleLabels() {
		return SUPPORTED_RULES_LABELS;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T extends IRule> Class<T>[] getSupportedGlobalRulesImpl() {
		return RULES_IMPL;
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
			document.appendChild(root);

			root.appendChild(buildDetailsElement(document));
			root.appendChild(buildNatureElement(document));
			root.appendChild(buildLoggerElement(document));
			root.appendChild(buildGlobalPropertiesElement(document));
			 root.appendChild(buildGlobalRulesElement(document));
			// root.appendChild(buildArtifactRulesElement(document));
			root.appendChild(buildClasspathEntriesElement(document));
			root.appendChild(buildAdditionalFilesElement(document));
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
			// loadArtifactRules(document);
			loadClasspathEntries(document);
			loadAdditionalFiles(document);
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

}
