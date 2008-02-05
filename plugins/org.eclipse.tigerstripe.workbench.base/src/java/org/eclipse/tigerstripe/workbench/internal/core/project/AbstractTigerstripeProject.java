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
package org.eclipse.tigerstripe.workbench.internal.core.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.workbench.internal.api.utils.IProjectLocator;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.locale.Messages;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Eric Dillon
 * 
 * This represent a TigerstripeProject. It corresponds to the "Tigerstripe.xml"
 * project descriptor.
 * 
 * This conditions a run of Tigerstripe.
 */
public abstract class AbstractTigerstripeProject {

	public static final String OUTPUT_DIRECTORY_TAG = "outputDirectory";

	public static final String NATURE_TAG = "nature";

	public static final String DESCRIPTION_TAG = "description";

	public static final String VERSION_TAG = "version";

	public static final String PROJECT_TAG = "project";

	public static final String PROVIDER_TAG = "provider";

	public static final String LABEL_TAG = "label";

	public static final String ADVANCED_TAG = "advanced";

	public static final String ADVANCEDPROPS_TAG = "advancedProperty";

	protected String descriptorVersion;

	private File baseDir;

	private String filename;

	private boolean notLoaded = true;

	/**
	 * The details of the projects
	 */
	private ProjectDetails projectDetails;

	/**
	 * Advanced properties for this project
	 */
	private Properties advancedProperties = new Properties();

	// ==========================================
	// ==========================================

	/**
	 * 
	 */
	protected AbstractTigerstripeProject(File baseDir, String filename) {
		this.filename = filename;
		this.baseDir = baseDir;
		this.projectDetails = new ProjectDetails(this);
	}

	// ==========================================
	/**
	 * Returns the project details for this tigerstripe project
	 * 
	 * @return ProjectDetails
	 */
	public ProjectDetails getProjectDetails() {
		return this.projectDetails;
	}

	public void setProjectDetails(ProjectDetails projectDetails) {
		this.projectDetails = projectDetails;
		projectDetails.setParentProject(this);
	}

	public File getBaseDir() {
		return this.baseDir;
	}

	public String getFilename() {
		return this.filename;
	}

	public File getFullPath() {
		return new File(this.baseDir.getAbsolutePath() + File.separator
				+ this.filename);
	}

	// =============================================

	/**
	 * Checks that this project is valid
	 * 
	 * TODO implement me!
	 */
	public boolean isValid() {
		return true;
	}

	public abstract void parse(Reader reader) throws TigerstripeException;

	/**
	 * Write the current project to the passed writer
	 * 
	 * @param writer
	 * @throws TigerstripeException
	 */
	public void write(Writer writer) throws TigerstripeException {

		try {
			Document document = buildDOM();
			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(document);

			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException tce) {
			throw new TigerstripeException("Transformer Factory error"
					+ tce.getMessage(), tce);
		} catch (TransformerException te) {
			throw new TigerstripeException("Transformation error"
					+ te.getMessage(), te);
		}
	}

	public String asText() throws TigerstripeException {
		StringWriter writer = new StringWriter();
		write(writer);
		return writer.toString();
	}

	/**
	 * Builds the DOM for this Project
	 * 
	 */
	protected abstract Document buildDOM();

	protected Element buildDetailsElement(Document document) {
		Element projectDetails = document.createElement(PROJECT_TAG);
		ProjectDetails details = getProjectDetails();

		// The name
		projectDetails.setAttribute("name", details.getName());

		// Output Directory
		Element outputDir = document.createElement(OUTPUT_DIRECTORY_TAG);
		outputDir.appendChild(document.createTextNode(details
				.getProjectOutputDirectory()));
		projectDetails.appendChild(outputDir);

		// Nature
		Element nature = document.createElement(NATURE_TAG);
		nature.appendChild(document.createTextNode(details.getNature()));
		projectDetails.appendChild(nature);

		// version
		Element version = document.createElement(VERSION_TAG);
		version.appendChild(document.createTextNode(details.getVersion()));
		projectDetails.appendChild(version);

		// description
		Element description = document.createElement(DESCRIPTION_TAG);
		description.appendChild(document.createTextNode(details
				.getDescription()));
		projectDetails.appendChild(description);

		// provider
		Element provider = document.createElement(PROVIDER_TAG);
		provider.appendChild(document.createTextNode(details.getProvider()));
		projectDetails.appendChild(provider);

		// All Properties
		Properties prop = details.getProperties();
		for (Iterator iter = prop.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String propertyValue = prop.getProperty(propertyName);

			Element property = document.createElement("property");
			property.setAttribute("name", propertyName);
			property.appendChild(document.createTextNode(propertyValue));
			projectDetails.appendChild(property);
		}
		return projectDetails;
	}

	protected Element buildAdvancedElement(Document document) {
		Element advancedElm = document.createElement(ADVANCED_TAG);

		for (Iterator iter = advancedProperties.keySet().iterator(); iter
				.hasNext();) {

			Element propElm = document.createElement(ADVANCEDPROPS_TAG);
			String key = (String) iter.next();
			String value = advancedProperties.getProperty(key);
			propElm.setAttribute("name", key);
			propElm.appendChild(document.createTextNode(value));

			advancedElm.appendChild(propElm);
		}

		return advancedElm;
	}

	protected void loadProjectDetails(Document document)
			throws TigerstripeException {
		projectDetails = new ProjectDetails(this);

		// Extract the project nature
		NodeList natures = document.getElementsByTagName(NATURE_TAG);
		if (natures.getLength() != 0) {
			NodeList nodes = natures.item(0).getChildNodes();
			if (nodes != null && nodes.getLength() != 0) {
				projectDetails.setNature(nodes.item(0).getNodeValue());
			}
		}
		if (projectDetails.getNature() == null) {
			projectDetails.setNature(PluginBody.UNKNOWN_NATURE);
		}

		// Extract the project description
		NodeList descriptions = document.getElementsByTagName(DESCRIPTION_TAG);
		if (descriptions.getLength() != 0) {
			NodeList nodes = descriptions.item(0).getChildNodes();
			if (nodes != null && nodes.getLength() != 0) {
				projectDetails.setDescription(nodes.item(0).getNodeValue());
			}
		}
		if (projectDetails.getDescription() == null) {
			projectDetails.setDescription("");
		}

		// Extract the project provider
		NodeList providers = document.getElementsByTagName(PROVIDER_TAG);
		if (providers.getLength() != 0) {
			NodeList nodes = providers.item(0).getChildNodes();
			if (nodes != null && nodes.getLength() != 0) {
				projectDetails.setProvider(nodes.item(0).getNodeValue());
			}
		}
		if (projectDetails.getProvider() == null) {
			projectDetails.setProvider("");
		}

		// Extract the project version
		NodeList versions = document.getElementsByTagName(VERSION_TAG);
		if (versions.getLength() != 0) {
			NodeList nodes = versions.item(0).getChildNodes();
			if (nodes != null && nodes.getLength() != 0) {
				projectDetails.setVersion(nodes.item(0).getNodeValue());
			}
		}
		if (projectDetails.getVersion() == null) {
			projectDetails.setVersion("");
		}

		// Extract the output directory
		NodeList outputs = document.getElementsByTagName(OUTPUT_DIRECTORY_TAG);
		if (outputs.getLength() != 0) {
			NodeList nodes = outputs.item(0).getChildNodes();
			if (nodes != null && nodes.getLength() != 0) {
				projectDetails.setProjectOutputDirectory(nodes.item(0)
						.getNodeValue());
			}
		}
		if (projectDetails.getProjectOutputDirectory() == null) {
			projectDetails.setProjectOutputDirectory("/target/tigerstripe.gen");
		}

		// Load project-level properties
		NodeList projects = document.getElementsByTagName(PROJECT_TAG);
		if (projects.getLength() != 0) {
			Element el = (Element) projects.item(0);

			projectDetails.setName(el.getAttribute("name"));
			if (projectDetails.getName() == null) {
				projectDetails.setName("");
			}

			projectDetails.setProperties(loadProperties(projects.item(0)));
		}
	}

	private Properties loadProperties(Node node) {
		Properties result = new Properties();

		NodeList propertiesList = node.getChildNodes();

		for (int i = 0; i < propertiesList.getLength(); i++) {
			Node prop = propertiesList.item(i);
			if ("property".equals(prop.getNodeName())) {
				NamedNodeMap namedAttributes = prop.getAttributes();
				Node name = namedAttributes.getNamedItem("name");
				Node value = prop.getFirstChild();

				if (name != null && !"".equals(name.getNodeValue())
						&& value != null) {
					String truc = value.getNodeValue();
					String trac = name.getNodeValue();
					result.setProperty(name.getNodeValue(), value
							.getNodeValue());
				}
			}
		}

		return result;
	}

	public void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		// FIXME
	}

	protected void loadAdvancedProperties(Document document)
			throws TigerstripeException {
		this.advancedProperties = new Properties();

		NodeList advPropertyNodes = document
				.getElementsByTagName(ADVANCEDPROPS_TAG);
		for (int i = 0; i < advPropertyNodes.getLength(); i++) {
			Element node = (Element) advPropertyNodes.item(i);

			Node valueNode = node.getFirstChild();
			String name = node.getAttribute("name");
			if (name != null && !"".equals(name) && valueNode != null) {
				String value = valueNode.getNodeValue();
				advancedProperties.setProperty(name, value);
			}
		}
	}

	/**
	 * This method checks whether this project contains the necessary mandatory
	 * IDependency.DEFAULT_CORE_MODEL_DEPENDENCY in the list of dependencies for
	 * this project.
	 * 
	 * It also checks that the version of the attached dependency if it exist is
	 * the correct version.
	 * 
	 * @throws
	 * @since 1.0.3
	 */

	public String getProjectLabel() {
		try {
			IProjectLocator loc = (IProjectLocator) InternalTigerstripeCore
					.getFacility(InternalTigerstripeCore.PROJECT_LOCATOR_FACILITY);
			return loc.getLocalLabel(getBaseDir().toURI());
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return "unknwon";
	}

	public String getAdvancedProperty(String prop) {
		return advancedProperties.getProperty(prop);
	}

	public String getAdvancedProperty(String prop, String defaultValue) {
		return advancedProperties.getProperty(prop, defaultValue);
	}

	public void setAdvancedProperty(String prop, String value) {
		this.advancedProperties.setProperty(prop, value);
	}

	public String getDescriptorVersion() {
		return this.descriptorVersion;
	}

	public abstract boolean requiresDescriptorUpgrade();

	/**
	 * reloads the project from file
	 * 
	 * @throws TigerstripeException
	 */
	public void reload(boolean forceReload) throws TigerstripeException {

		if (notLoaded) {
			forceReload = true;
		}

		if (forceReload) {
			File theFile = getFullPath();

			if (!theFile.exists())
				throw new TigerstripeException(Messages.formatMessage(
						Messages.TIGERSTRIPE_XML_NOT_FOUND, theFile));
			FileReader reader = null;
			try {
				reader = new FileReader(theFile);
				parse(reader);
				notLoaded = false;
			} catch (FileNotFoundException e) {
				throw new TigerstripeException(
						"Tigerstripe descriptor not found ("
								+ theFile.getAbsolutePath() + ").", e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		}
	}

	public void doSave() throws TigerstripeException {
		File theFile = getFullPath();
		Writer writer = null;
		try {
			writer = new FileWriter(theFile);
			write(writer);
		} catch (IOException e) {
			throw new TigerstripeException("Tigerstripe descriptor not found ("
					+ theFile.getAbsolutePath() + ").", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
}