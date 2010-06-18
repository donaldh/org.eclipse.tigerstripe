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

import org.apache.tools.ant.filters.StringInputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BaseContainerObject;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.IContainerObject;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.internal.core.locale.Messages;
import org.eclipse.tigerstripe.workbench.internal.core.util.ContainedProperties;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Eric Dillon
 * 
 *         This represent a TigerstripeProject. It corresponds to the
 *         "Tigerstripe.xml" project descriptor.
 * 
 *         This conditions a run of Tigerstripe.
 */
public abstract class AbstractTigerstripeProject extends BaseContainerObject
		implements IContainerObject, IAdaptable, ITigerstripeChangeListener {

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

	private File fullpath;

	private String filename;

	private boolean notLoaded = true;

	private long loadTStamp = -1;

	private boolean isLocalDirty = false;

	/**
	 * The details of the projects
	 */
	private ProjectDetails projectDetails;

	/**
	 * Advanced properties for this project
	 */
	private ContainedProperties advancedProperties;

	// ==========================================
	// ==========================================

	/**
	 * 
	 */
	protected AbstractTigerstripeProject(File baseDir, String filename) {
		this.filename = filename;
		this.baseDir = baseDir;

		advancedProperties = new ContainedProperties();
		advancedProperties.setContainer(this);
		this.projectDetails = new ProjectDetails(this);
		this.projectDetails.setContainer(this);
		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(this, 
				ITigerstripeChangeListener.MODEL | ITigerstripeChangeListener.PROJECT);
	}

	protected void setDirty() {
		this.isLocalDirty = true;
	}

	public boolean isDirty() {
		return isLocalDirty || isContainedDirty();
	}

	public void clearDirty() {
		isLocalDirty = false;
		clearDirtyOnContained();
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
		this.projectDetails.setContainer(this);
		setDirty();
	}

	public File getBaseDir() {
		return this.baseDir;
	}

	public String getFilename() {
		return this.filename;
	}

	public File getFullPath() {
		if (fullpath == null) {
			this.fullpath = new File(this.baseDir.getAbsolutePath()
					+ File.separator + this.filename);
		}
		return this.fullpath;
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

		// the modelId
		projectDetails.setAttribute("modelId", details.getModelId());

		// Output Directory
		Element outputDir = document.createElement(OUTPUT_DIRECTORY_TAG);
		outputDir.appendChild(document.createTextNode(details
				.getProjectOutputDirectory()));
		projectDetails.appendChild(outputDir);

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
		for (Iterator<Object> iter = prop.keySet().iterator(); iter.hasNext();) {
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

		for (Iterator<Object> iter = advancedProperties.keySet().iterator(); iter
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

			projectDetails.setModelId(el.getAttribute("modelId"));
			if (projectDetails.getModelId() == null) {
				projectDetails.setModelId("");
			}

			projectDetails.setProperties(loadProperties(projects.item(0)));
		}

		// Do it last to preserve the state (not dirty)
		projectDetails.setContainer(this);
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
		advancedProperties.clear();

		NodeList advPropertyNodes = document
				.getElementsByTagName(ADVANCEDPROPS_TAG);
		for (int i = 0; i < advPropertyNodes.getLength(); i++) {
			Element node = (Element) advPropertyNodes.item(i);

			Node valueNode = node.getFirstChild();
			String name = node.getAttribute("name");
			if (name != null && !"".equals(name) && valueNode != null) {
				String value = valueNode.getNodeValue();
				setAdvancedProperty(name, value);
			}
		}

		// To that last to preserve the "clean = non-dirty" state.
		advancedProperties.clearDirty();
	}

	public String getProjectLabel() {

		if (getBaseDir() == null)
			return null; // this is a project desc embedded in a module

		return getBaseDir().getName();
	}

	public String getAdvancedProperty(String prop) {
		return advancedProperties.getProperty(prop);
	}

	public String getAdvancedProperty(String prop, String defaultValue) {
		return advancedProperties.getProperty(prop, defaultValue);
	}

	public void setAdvancedProperty(String prop, String value) {
		setDirty();
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

		boolean needReload = false;
		File theFile = getFullPath();
		if (!theFile.exists())
			throw new TigerstripeException(Messages.formatMessage(
					Messages.TIGERSTRIPE_XML_NOT_FOUND, theFile));

		if (notLoaded || forceReload) {
			needReload = true;
//		} else {
//			// determine if the file has changed on disk
//			long currentTStamp = theFile.lastModified();
//			needReload = currentTStamp != loadTStamp;
		}

		if (needReload) {
			FileReader reader = null;
			try {
				reader = new FileReader(theFile);
				parse(reader);
				notLoaded = false;
				loadTStamp = theFile.lastModified();
				clearDirty();
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

	public void doSave(IProgressMonitor monitor) throws TigerstripeException {

		if (monitor == null)
			monitor = new NullProgressMonitor();

		IPath filePath = new Path(getFullPath().getAbsolutePath());
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IFile[] files = workspace.getRoot().findFilesForLocation(filePath);
		assert (files.length == 1);

		IFile theFile = files[0];
		StringWriter writer = new StringWriter();
		write(writer);
		try {
			theFile.setContents(new StringInputStream(writer.toString()),
					IResource.FORCE | IResource.KEEP_HISTORY, monitor);
			loadTStamp = theFile.getLocalTimeStamp();
			// Make sure we clear our dirty state
			clearDirty();
		} catch (CoreException e) {
			BasePlugin.log(e);
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

	public Object getAdapter(Class adapter) {
		try {
			if (IAbstractTigerstripeProject.class == adapter) {
				try {
					return TigerstripeCore.findProject(getBaseDir().toURI());
				} catch (TigerstripeException e) {
					return null;
				}
			} else if (IProject.class == adapter) {
				return ResourcesPlugin.getWorkspace().getRoot().getProject(
						getProjectLabel());
			} else if (IJavaProject.class == adapter) {
				IProject iProj = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(getProjectLabel());
				if (iProj != null)
					return JavaCore.create(iProj);
			}
		} catch (Exception e){
			return null;
		}
		return null;
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		// TODO Auto-generated method stub
		
	}

	public void descriptorChanged(IResource changedDescriptor) {
		// Override for the various project types as necessary
	}

	public void modelChanged(IModelChangeDelta[] delta) {
		// TODO Auto-generated method stub
		
	}

	public void projectAdded(IAbstractTigerstripeProject project) {
		// TODO Auto-generated method stub
		
	}

	public void projectDeleted(String projectName) {
		// TODO Auto-generated method stub
		
	}

	public void artifactResourceAdded(IResource addedArtifactResource) {
		// TODO Auto-generated method stub
		
	}

	public void artifactResourceChanged(IResource changedArtifactResource) {
		// TODO Auto-generated method stub
		
	}

	public void artifactResourceRemoved(IResource removedArtifactResource) {
		// TODO Auto-generated method stub
		
	}

	
	
}