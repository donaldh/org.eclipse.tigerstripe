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
package org.eclipse.tigerstripe.core.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.tools.ant.util.ReaderInputStream;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.api.project.IDependency;
import org.eclipse.tigerstripe.api.project.IProjectChangeListener;
import org.eclipse.tigerstripe.api.project.IProjectDescriptor;
import org.eclipse.tigerstripe.api.project.IProjectDetails;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.api.utils.IProjectLocator;
import org.eclipse.tigerstripe.contract.segment.FacetReference;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.cli.App;
import org.eclipse.tigerstripe.core.locale.Messages;
import org.eclipse.tigerstripe.core.plugin.PluginRef;
import org.eclipse.tigerstripe.core.plugin.PluginRefFactory;
import org.eclipse.tigerstripe.core.plugin.UnknownPluginException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Eric Dillon
 * 
 * This represent a TigerstripeProject. It corresponds to the "Tigerstripe.xml"
 * project descriptor.
 * 
 * This conditions a run of Tigerstripe.
 */
public class TigerstripeProject extends AbstractTigerstripeProject implements
		IProjectDescriptor {

	/** logger for output */
	private static Logger log = Logger.getLogger(App.class);

	// This defines the compatibility level for the project descriptor;
	public static final String COMPATIBILITY_LEVEL = "1.1";

	public static final String DEFAULT_FILENAME = ITigerstripeConstants.PROJECT_DESCRIPTOR;

	public static final String REPOSITORY_TAG = "repository";

	public static final String DEFAULTINTERFACE_PACKAGE_TAG = "defaultInterfacePackage";

	public static final String TIGERSTRIPE_ROOT_TAG = "tigerstripe";

	public static final String DEPENDENCY_TAG = "dependency";

	public static final String REFERENCE_TAG = "reference";

	public static final String FACET_TAG = "facetReference";

	/**
	 * The references to all plugins used by this project
	 */
	private Collection pluginReferences = new ArrayList();

	/**
	 * A set of ArtifactRepositories
	 */
	private Collection artifactRepositories = new ArrayList();

	/**
	 * A set of dependencies
	 */
	private Collection<IDependency> dependencies = new ArrayList<IDependency>();

	/**
	 * A set of facet references
	 */
	private List<IFacetReference> facetReferences = new ArrayList<IFacetReference>();

	/**
	 * Tigerstripe projects referenced from this one.
	 */
	private List<ITigerstripeProject> referencedProjects = new ArrayList<ITigerstripeProject>();

	// ==========================================
	// ==========================================

	public TigerstripeProject(File baseDir) {
		super(baseDir, DEFAULT_FILENAME);
	}

	// ==========================================
	public Collection getPluginReferences() {
		return this.pluginReferences;
	}

	public void setPluginReferences(Collection pluginReferences) {
		this.pluginReferences = pluginReferences;
	}

	public Collection getArtifactRepositories() {
		return this.artifactRepositories;
	}

	public void setArtifactRepositories(Collection artifactRepositories) {
		this.artifactRepositories = artifactRepositories;
	}

	public Collection<IDependency> getDependencies() {
		return this.dependencies;
	}

	public void setDependencies(Collection dependencies) {
		this.dependencies = dependencies;
	}

	// =============================================

	/**
	 * Checks that this project is valid
	 * 
	 * TODO implement me!
	 */
	@Override
	public boolean isValid() {
		return true;
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
			NodeList roots = document
					.getElementsByTagName(TIGERSTRIPE_ROOT_TAG);
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
			loadRepositories(document);
			loadPluginReferences(document);
			loadDependencies(document);
			loadReferences(document);
			loadFacetReferences(document);
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

	/**
	 * Write the current project to the passed writer
	 * 
	 * @param writer
	 * @throws TigerstripeException
	 */
	@Override
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
			TigerstripeRuntime.logErrorMessage("TransformerException detected",
					te);
			throw new TigerstripeException("Transformation error"
					+ te.getMessage(), te);
		}
	}

	@Override
	public String asText() throws TigerstripeException {
		StringWriter writer = new StringWriter();
		write(writer);
		return writer.toString();
	}

	/**
	 * Builds the DOM for this Project
	 * 
	 */
	@Override
	protected Document buildDOM() {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();

			// The Tigerstripe root
			Element root = document.createElement(TIGERSTRIPE_ROOT_TAG);
			root.setAttribute("version", COMPATIBILITY_LEVEL);
			document.appendChild(root);

			root.appendChild(buildDetailsElement(document));
			root.appendChild(buildRepositoriesElement(document));
			root.appendChild(buildPluginsElement(document));
			root.appendChild(buildDependenciesElement(document));
			root.appendChild(buildReferencesElement(document));
			root.appendChild(buildFacetRefsElement(document));
			root.appendChild(buildAdvancedElement(document));
		} catch (ParserConfigurationException e) {
			TigerstripeRuntime.logErrorMessage(
					"ParserConfigurationException detected", e);
		}
		return document;
	}

	public String getBaseRepository() {
		if (artifactRepositories == null || artifactRepositories.isEmpty())
			return null;

		Iterator iter = artifactRepositories.iterator();

		ArtifactRepository repo = (ArtifactRepository) iter.next();
		if (repo.getIncludes() != null && repo.getIncludes().length != 0) {
			String includeRaw = repo.getIncludes()[0];
			String include = includeRaw.substring(0, includeRaw.indexOf("**"));
			return include;
		}
		return null;
	}

	private Element buildRepositoriesElement(Document document) {
		Element repositories = document.createElement("repositories");

		// There should always be at least one repository
		if (artifactRepositories.size() == 0) {
			ArtifactRepository repo = new ArtifactRepository(getBaseDir());
			repo.setIncludes(Arrays.asList(new String[] { "src/**/*.java" }));
			artifactRepositories.add(repo);
		}

		for (Iterator iter = artifactRepositories.iterator(); iter.hasNext();) {
			ArtifactRepository repo = (ArtifactRepository) iter.next();
			Element repository = document.createElement(REPOSITORY_TAG);

			// the baseDir
			repository.setAttribute("baseDir", ".");

			String[] includes = repo.getIncludes();
			String[] excludes = repo.getExcludes();

			for (int i = 0; i < includes.length; i++) {
				Element include = document.createElement("includes");
				include.appendChild(document.createTextNode(includes[i]));
				repository.appendChild(include);
			}

			for (int i = 0; i < excludes.length; i++) {
				Element exclude = document.createElement("excludes");
				exclude.appendChild(document.createTextNode(excludes[i]));
				repository.appendChild(exclude);
			}

			repositories.appendChild(repository);
		}

		return repositories;
	}

	private Element buildPluginsElement(Document document) {
		Element plugins = document.createElement("plugins");

		for (Iterator iter = getPluginReferences().iterator(); iter.hasNext();) {
			PluginRef ref = (PluginRef) iter.next();
			Element plugin = ref.buildPluginElement(document);

			plugins.appendChild(plugin);
		}

		return plugins;
	}

	private Element buildFacetRefsElement(Document document) {
		Element facetReferencesElement = document
				.createElement("facetReferences");
		for (IFacetReference ref : facetReferences) {
			Element refElm = document.createElement(FACET_TAG);
			if (ref.isAbsolute()) {
				try {
					refElm.setAttribute("uri", ref.getURI().toASCIIString());
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			} else {
				refElm.setAttribute("relPath", ref.getProjectRelativePath());
				if (ref.getContainingProject() != null
						&& !ref.getContainingProject().getProjectLabel()
								.equals(getProjectLabel())) {
					refElm.setAttribute("project", ref.getContainingProject()
							.getProjectLabel());
				}
			}
			refElm.setAttribute("genDir", ref.getGenerationDir());
			facetReferencesElement.appendChild(refElm);
		}
		return facetReferencesElement;
	}

	private Element buildDependenciesElement(Document document) {
		Element dependenciesElm = document.createElement("dependencies");
		String corePath = TigerstripeRuntime
				.getProperty(TigerstripeRuntime.CORE_OSSJ_ARCHIVE);

		for (Iterator iter = getDependencies().iterator(); iter.hasNext();) {

			Dependency dep = (Dependency) iter.next();
			if (!dep.getPath().equals(corePath)) {
				Element depElm = document.createElement(DEPENDENCY_TAG);

				depElm.setAttribute("path", dep.getPath());
				dependenciesElm.appendChild(depElm);
			}
		}

		return dependenciesElm;
	}

	private Element buildReferencesElement(Document document) {
		Element referencesElm = document.createElement("references");

		for (ITigerstripeProject ref : getReferencedProjects()) {
			Element refElm = document.createElement(REFERENCE_TAG);

			try {
				URI projectURI = ref.getURI();
				IProjectLocator loc = (IProjectLocator) API
						.getFacility(API.PROJECT_LOCATOR_FACILITY);
				String label = loc.getLocalLabel(projectURI);
				refElm.setAttribute("path", label);
				referencesElm.appendChild(refElm);
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}

		return referencesElm;
	}

	private void loadRepositories(Document document)
			throws TigerstripeException {
		this.artifactRepositories = new ArrayList();

		NodeList repositories = document.getElementsByTagName(REPOSITORY_TAG);
		if (repositories.getLength() == 0)
			throw new TigerstripeException(Messages.formatMessage(
					Messages.NO_REPOSITORY_SPECIFIED, getFullPath()));
		for (int i = 0; i < repositories.getLength(); i++) {
			Node node = repositories.item(i);
			ArtifactRepository repository = null;

			// Check on the baseDir
			String baseDir;
			NamedNodeMap namedAttributes = node.getAttributes();
			Node dir = namedAttributes.getNamedItem("baseDir");
			if (dir == null || dir.getNodeValue().length() == 0)
				// this should not happen and be caught when validating XML doc
				throw new TigerstripeException(Messages.DIRECTORY_NOT_SPECIFIED);
			baseDir = dir.getNodeValue();
			if (".".equals(baseDir)) {
				repository = new ArtifactRepository(getBaseDir());
			} else if ((new File(getBaseDir().getAbsolutePath()
					+ File.separator + baseDir)).exists()) {
				// this is a relative path
				repository = new ArtifactRepository(new File(getBaseDir()
						.getAbsolutePath()
						+ File.separator + baseDir));
			} else if ((new File(baseDir)).exists()) {
				// this is an absolute path
				repository = new ArtifactRepository(new File(baseDir));
			}

			// Get the Includes and excludes
			NodeList children = node.getChildNodes();
			ArrayList includes = new ArrayList();
			ArrayList excludes = new ArrayList();
			for (int j = 0; j < children.getLength(); j++) {
				if ("includes".equals(children.item(j).getNodeName())) {
					includes.add(children.item(j).getFirstChild()
							.getNodeValue());
				} else if ("excludes".equals(children.item(j).getNodeName())) {
					excludes.add(children.item(j).getFirstChild()
							.getNodeValue());
				}
			}

			repository.setIncludes(includes);
			repository.setExcludes(excludes);
			artifactRepositories.add(repository);
		}
	}

	private void loadPluginReferences(Document document)
			throws TigerstripeException {

		this.pluginReferences = new ArrayList();
		NodeList plugins = document
				.getElementsByTagName(PluginRef.PLUGIN_REFERENCE_TAG);

		for (int i = 0; i < plugins.getLength(); i++) {
			Node node = plugins.item(i);
			try {
				PluginRef ref = PluginRefFactory.getInstance().createPluginRef(
						(Element) node, this);

				// need to make sure it can be resolved
				ref.resolve();

				this.pluginReferences.add(ref);
			} catch (UnknownPluginException e) {
				log.info(e);
			}
		}
	}

	public PluginRef findPluginRef(PluginRef model) {
		for (Iterator iter = getPluginReferences().iterator(); iter.hasNext();) {
			PluginRef ref = (PluginRef) iter.next();
			if (model.getPluginId().equals(ref.getPluginId())
					&& model.getGroupId().equals(ref.getGroupId()))
				return ref;
		}
		return null;
	}

	@Override
	public void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		// FIXME
	}

	private void loadDependencies(Document document)
			throws TigerstripeException {
		this.dependencies = new ArrayList<IDependency>();

		if (getBaseDir() == null)
			// dependencies within modules are ignored
			return;

		// handle default Dep
		if (getBaseDir() != null
				&& !getProjectDetails().getName().equals("Phantom Project")) { // don't
			// do
			// that
			// on
			// TS
			// Modules
			addDependency(TigerstripeRuntime.getCoreOssjArchive());
		}

		NodeList dependencyNode = document.getElementsByTagName(DEPENDENCY_TAG);

		for (int i = 0; i < dependencyNode.getLength(); i++) {
			Node node = dependencyNode.item(i);
			NamedNodeMap namedAttributes = node.getAttributes();
			Node path = namedAttributes.getNamedItem("path");

			String pathStr = path.getNodeValue();
			Dependency dep = new Dependency(this, pathStr);
			addDependency(dep);
		}
	}

	private void loadFacetReferences(Document document)
			throws TigerstripeException {
		this.facetReferences = new ArrayList<IFacetReference>();

		NodeList facetRefNode = document.getElementsByTagName(FACET_TAG);
		for (int i = 0; i < facetRefNode.getLength(); i++) {
			Node node = facetRefNode.item(i);
			NamedNodeMap namedAttributes = node.getAttributes();
			Node uriNode = namedAttributes.getNamedItem("uri");
			Node relPath = namedAttributes.getNamedItem("relPath");
			Node genDir = namedAttributes.getNamedItem("genDir");
			Node projectLabel = namedAttributes.getNamedItem("project");
			String uriStr = null;
			String genDirStr = null;
			String relPathStr = null;
			String projectLabelStr = null;
			if (uriNode != null)
				uriStr = uriNode.getNodeValue();

			if (genDir != null) {
				genDirStr = genDir.getNodeValue();
			}

			if (relPath != null) {
				relPathStr = relPath.getNodeValue();
			}

			if (projectLabel != null) {
				projectLabelStr = projectLabel.getNodeValue();
			}

			try {
				FacetReference ref = null;
				if (uriStr != null) {
					URI uri = new URI(uriStr);
					ref = new FacetReference(uri, this.getTSProject());
				} else if (relPathStr != null) {

					if (projectLabelStr != null) {
						ref = new FacetReference(relPathStr, projectLabelStr,
								this.getTSProject());
					} else {
						ref = new FacetReference(relPathStr, this);
					}
				}
				if (ref != null) {
					ref.setGenerationDir(genDirStr);
					facetReferences.add(ref);
				}
			} catch (URISyntaxException e) {
				TigerstripeRuntime.logErrorMessage(
						"URISyntaxException detected", e);
			}
		}
	}

	private ITigerstripeProject getTSProject() {
		if (getBaseDir() != null) {
			try {
				return (ITigerstripeProject) API.getDefaultProjectSession()
						.makeTigerstripeProject(getBaseDir().toURI());
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			} catch (TigerstripeLicenseException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeLicenseException detected", e);
			}
		}
		return null;
	}

	private void loadReferences(Document document) throws TigerstripeException {

		this.referencedProjects = new ArrayList<ITigerstripeProject>();

		// Bug 259: references should be ignored when within an embedded module
		// descriptor.
		if (getBaseDir() == null)
			return;

		NodeList dependencyNode = document.getElementsByTagName(REFERENCE_TAG);

		for (int i = 0; i < dependencyNode.getLength(); i++) {
			Node node = dependencyNode.item(i);
			NamedNodeMap namedAttributes = node.getAttributes();
			Node path = namedAttributes.getNamedItem("path");

			String label = path.getNodeValue();
			try {

				IProjectLocator loc = (IProjectLocator) API
						.getFacility(API.PROJECT_LOCATOR_FACILITY);

				ITigerstripeProject self = (ITigerstripeProject) API
						.getDefaultProjectSession().makeTigerstripeProject(
								getBaseDir().toURI(), null);
				URI uri = loc.locate(self, label);
				ITigerstripeProject prj = (ITigerstripeProject) API
						.getDefaultProjectSession().makeTigerstripeProject(uri,
								null);

				if (prj != null)
					addReferencedProject(prj);
			} catch (TigerstripeLicenseException e) {
				throw new TigerstripeException(
						"Licensing error while loading referenced projects", e);
			}
		}
	}

	// Shipped Core dependencies don't exist anymore @see #299
	// /**
	// * This method checks whether this project contains the necessary
	// mandatory
	// * IDependency.DEFAULT_CORE_MODEL_DEPENDENCY in the list of dependencies
	// for
	// * this project.
	// *
	// * It also checks that the version of the attached dependency if it exist
	// is
	// * the correct version.
	// *
	// * @throws
	// * @since 1.0.3
	// */
	// public void checkDefaultCoreModelDependency() throws
	// NoCoreModelException,
	// MismatchedCoreModelException {
	//
	// String shippedVersion = getShippedCoreModelVersion();
	//
	// // If no core model dep was shipped, just ignore
	// if (shippedVersion == null) {
	// TigerstripeRuntime.logInfoMessage(
	// "Warning: No core model was shipped with this version of Tigerstripe: "
	// +
	// TigerstripeRuntime.getProperty(TigerstripeRuntime.TIGERSTRIPE_FEATURE_VERSION));
	// return;
	// }
	//
	// boolean found = false;
	// for (IDependency dep : dependencies) {
	// if (dep.getIModuleHeader() != null) {
	// String moduleID = dep.getIModuleHeader().getModuleID();
	// String moduleVersion = dep.getIProjectDetails().getVersion();
	// if (IDependency.DEFAULT_CORE_MODEL_DEPENDENCY.equals(moduleID)) {
	// found = true;
	//
	// // Now check for versions
	// // if (TigerstripeRuntime.PROPERTIES_NOT_LOADED
	// // .equals(shippedVersion)) {
	// // // This means we're in a dev environment not in a final
	// // // built version
	// // TigerstripeRuntime.logInfoMessage(
	// // "Warning: couldn't check Core model version because this is a Dev
	// build");
	// // } else if (!shippedVersion.equals(moduleVersion)) {
	// // throw new MismatchedCoreModelException(
	// // "Mismatched versions for Core Model: found "
	// // + moduleVersion + " while expecting "
	// // + shippedVersion);
	// // }
	// }
	// }
	// }
	// if (!found) {
	// throw new NoCoreModelException(
	// "Couldn't find Tigerstripe Core Module ("
	// + IDependency.DEFAULT_CORE_MODEL_DEPENDENCY
	// + ") in the listed "
	// + "dependencies of the project.");
	// }
	// }

	// /**
	// * Returns the version of the core model as shipped (by looking into the
	// * modules dir in the install dir)
	// *
	// * @return
	// */
	// private String getShippedCoreModelVersion() {
	// IDependency core = Dependency.getDefaultCoreModelDependency();
	// if (core == null) {
	// // none found
	// return null;
	// } else {
	// return core.getIProjectDetails().getVersion();
	// }
	// }
	//
	// /**
	// * Attaches the core model (IDependency.DEFAULT_CORE_MODEL_DEPENDENCY) as
	// * shipped in this version of Tigerstripe.
	// *
	// * If a core model is already attached, the overwrite is conditions by the
	// * forceOverwrite parameter.
	// *
	// * @param forceOverwrite,
	// * if true, overwrite any existing version of the core model,
	// * don't otherwise (an exception will be raised)
	// * @throws TigerstripeException,
	// * if the attach was not successful
	// */
	// public void attachDefaultCoreModelDependency(boolean forceOverwrite)
	// throws TigerstripeException {
	// // TigerstripeRuntime.logInfoMessage("Attaching core model Dep "
	// // + Dependency.getDefaultCoreModelDependency().getIModuleHeader()
	// // .getModuleID());
	//
	// IDependency localCopy = Dependency.copyToAsLocalDependency(this,
	// Dependency.getDefaultCoreModelDependency());
	//
	// addDependency(localCopy);
	// }

	public void addDependency(IDependency dependency) {
		if (!this.dependencies.contains(dependency)) {
			this.dependencies.add(dependency);
			dependencyAdded(dependency);
		}
	}

	public void removeDependency(IDependency dependency) {
		if (this.dependencies.contains(dependency)) {
			this.dependencies.remove(dependency);
			dependencyRemoved(dependency);
		}
	}

	public void addDependencies(IDependency[] dependencies) {
		for (int i = 0; i < dependencies.length; i++) {
			addDependency(dependencies[i]);
		}
	}

	public void removeDependencies(IDependency[] dependencies) {
		for (int i = 0; i < dependencies.length; i++) {
			removeDependency(dependencies[i]);
		}
	}

	// ============================================================
	// For listeners
	private Collection projectChangeListener = new ArrayList();

	public void addProjectChangeListener(IProjectChangeListener listener) {
		projectChangeListener.add(listener);
	}

	public void removeProjectChangeListener(IProjectChangeListener listener) {
		projectChangeListener.remove(listener);
	}

	private void dependencyRemoved(IDependency dependency) {
		for (Iterator iter = projectChangeListener.iterator(); iter.hasNext();) {
			IProjectChangeListener listener = (IProjectChangeListener) iter
					.next();
			listener.dependencyRemoved(dependency);
		}
	}

	private void dependencyAdded(IDependency dependency) {
		for (Iterator iter = projectChangeListener.iterator(); iter.hasNext();) {
			IProjectChangeListener listener = (IProjectChangeListener) iter
					.next();
			listener.dependencyAdded(dependency);
		}
	}

	public void addReferencedProject(ITigerstripeProject project)
			throws TigerstripeException {
		referencedProjects.add(project);
	}

	public void removeReferencedProject(ITigerstripeProject project)
			throws TigerstripeException {
		referencedProjects.remove(project);
	}

	public void addReferencedProjects(ITigerstripeProject[] projects)
			throws TigerstripeException {
		for (ITigerstripeProject project : projects) {
			addReferencedProject(project);
		}
	}

	public void removeReferencedProjects(ITigerstripeProject[] projects)
			throws TigerstripeException {
		for (ITigerstripeProject project : projects) {
			removeReferencedProject(project);
		}
	}

	public ITigerstripeProject[] getReferencedProjects() {
		return referencedProjects
				.toArray(new ITigerstripeProject[referencedProjects.size()]);
	}

	public boolean hasReference(ITigerstripeProject project) {

		for (Iterator iter = referencedProjects.iterator(); iter.hasNext();) {
			ITigerstripeProject prj = (ITigerstripeProject) iter.next();
			if (project.getURI().equals(prj.getURI()))
				return true;
		}

		return false;
	}

	@Override
	public String getProjectLabel() {

		if (getBaseDir() == null)
			return null; // this is a project desc embedded in a module

		try {
			IProjectLocator loc = (IProjectLocator) API
					.getFacility(API.PROJECT_LOCATOR_FACILITY);
			return loc.getLocalLabel(getBaseDir().toURI());
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return "unknwon";
	}

	@Override
	public String getDescriptorVersion() {
		return this.descriptorVersion;
	}

	@Override
	public boolean requiresDescriptorUpgrade() {
		return COMPATIBILITY_LEVEL.compareTo(this.descriptorVersion) > 0;
	}

	/**
	 * Returns true if this project has the same list of dependencies than the
	 * given project.
	 * 
	 * @param other
	 * @param ignoreOrder -
	 *            whether order should be ignored during comparison
	 * @return true if both projects have same dependencies, false otherwise
	 */
	public boolean hasSameDependencies(TigerstripeProject other,
			boolean ignoreOrder) {

		Collection<IDependency> otherDeps = other.getDependencies();
		if (getDependencies().size() != otherDeps.size())
			return false;

		if (otherDeps.size() == 0)
			return true;

		int index = 0;
		for (Iterator oIter = otherDeps.iterator(); oIter.hasNext();) {
			boolean matched = false;
			IDependency dep = (IDependency) oIter.next();

			if (dep.isValid()) {
				if (ignoreOrder) {
					// When ignoring order, simply look for it
					for (IDependency lDep : getDependencies()) {
						if (lDep.isValid()) {
							// 2 dependencies are considered equal in this
							// context when
							// they have the same relPath and moduleID
							if (lDep.getPath().equals(dep.getPath())
									&& lDep.getIModuleHeader().getModuleID()
											.equals(
													dep.getIModuleHeader()
															.getModuleID())) {
								matched = true;
							}
						}
					}
				} else {
					// when not ignoring order, needs to match on the index
					IDependency lDep = ((ArrayList<IDependency>) dependencies)
							.get(index);
					if (lDep.isValid()) {
						// 2 dependencies are considered equal in this context
						// when
						// they have the same relPath and moduleID
						if (lDep.getPath().equals(dep.getPath())
								&& lDep.getIModuleHeader().getModuleID()
										.equals(
												dep.getIModuleHeader()
														.getModuleID())) {
							matched = true;
						}
					}
				}
			}

			if (!matched)
				return false;
			index++;
		}
		return true;
	}

	/**
	 * Returns true if this project has the same list of references than the
	 * given project.
	 * 
	 * @param other
	 * @param ignoreOrder -
	 *            whether order should be ignored during comparison
	 * @return true if both projects have same references, false otherwise
	 */
	public boolean hasSameReferences(TigerstripeProject other,
			boolean ignoreOrder) {

		ITigerstripeProject[] otherRefs = other.getReferencedProjects();
		if (getReferencedProjects().length != otherRefs.length)
			return false;

		int index = 0;
		for (ITigerstripeProject oRef : otherRefs) {
			boolean matched = false;

			if (ignoreOrder) {
				// When ignoring order, simply look for it
				for (ITigerstripeProject lRef : getReferencedProjects()) {
					if (lRef.getProjectLabel().equals(oRef.getProjectLabel())) {
						matched = true;
					}
				}
			} else {
				ITigerstripeProject lRef = getReferencedProjects()[index];
				if (lRef.getProjectLabel().equals(oRef.getProjectLabel())) {
					matched = true;
				}
			}

			if (!matched)
				return false;
		}
		return true;
	}

	public void addFacetReference(IFacetReference facetRef)
			throws TigerstripeException {
		if (!facetReferences.contains(facetRef))
			facetReferences.add(facetRef);
	}

	public void removeFacetReference(IFacetReference facetRef)
			throws TigerstripeException {
		if (facetReferences.contains(facetRef)) {
			facetReferences.remove(facetRef);
		}
	}

	public List<IFacetReference> getFacetReferences()
			throws TigerstripeException {
		return facetReferences;
	}

	public IPluginReference[] getIPluginReferences()
			throws TigerstripeException {
		return (IPluginReference[]) pluginReferences
				.toArray(new IPluginReference[pluginReferences.size()]);
	}

	public ITigerstripeProject[] getIReferencedProjects()
			throws TigerstripeException {
		return getReferencedProjects();
	}

	public IProjectDetails getIProjectDetails()
			throws TigerstripeException {
		return getProjectDetails();
	}

	public String getRepositoryLocation() throws TigerstripeException {
		List<ArtifactRepository> repositories = (List<ArtifactRepository>) getArtifactRepositories();
		if (repositories == null || repositories.size() == 0)
			throw new TigerstripeException("No defined Artifact repository in "
					+ ITigerstripeConstants.PROJECT_DESCRIPTOR);

		ArtifactRepository repo = repositories.get(0);
		if (repo.getIncludes().length == 0)
			throw new TigerstripeException(
					"No 'includes' defined for repository "
							+ repo.getBaseDirectory());

		String repoLocation = repo.getIncludes()[0];
		int index = repoLocation.indexOf("**/*.java");
		if (index == -1)
			throw new TigerstripeException(
					"Incorrect 'includes' clause for repository: '"
							+ repoLocation + "'. Must follow xxx/yyy/**/*.java");
		repoLocation = repoLocation.substring(0, index);

		return repoLocation;
	}
}