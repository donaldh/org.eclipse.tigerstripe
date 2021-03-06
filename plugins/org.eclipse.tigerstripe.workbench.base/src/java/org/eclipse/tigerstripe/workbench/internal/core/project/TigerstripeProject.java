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
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

import org.apache.tools.ant.util.ReaderInputStream;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.internal.annotation.ModuleAnnotationManager;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.workbench.internal.contract.segment.FacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.locale.Messages;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfigFactory;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDescriptor;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Eric Dillon, Navid Mehregani
 * 
 *         This represent a TigerstripeProject. It corresponds to the
 *         "Tigerstripe.xml" project descriptor.
 * 
 *         This conditions a run of Tigerstripe.
 */
public class TigerstripeProject extends AbstractTigerstripeProject implements IProjectDescriptor {

	/**
	 * This is to ensure we don't mess up the way things are saved a maintain
	 * compatibility with all versions of TS, where no modelId was used, i.e. we
	 * keep the reference by name
	 * 
	 * @author erdillon
	 * 
	 */
	private class LegacyModelReference extends ModelReference {

		private String path = null;

		/**
		 * @param projectContext
		 * @param toModelId
		 */
		public LegacyModelReference(ITigerstripeModelProject projectContext, String toModelId, String path) {
			super(projectContext, toModelId);
			this.path = path;
		}

		/**
		 * @param toModelId
		 */
		public LegacyModelReference(String toModelId, String path) {
			super(toModelId);
			this.path = path;
		}

		public String getPath() {
			return this.path;
		}

	}

	// This defines the compatibility level for the project descriptor;
	public static final String COMPATIBILITY_LEVEL = "1.1";

	public static final String DEFAULT_FILENAME = ITigerstripeConstants.PROJECT_DESCRIPTOR;

	public static final String REPOSITORY_TAG = "repository";

	public static final String DEFAULTINTERFACE_PACKAGE_TAG = "defaultInterfacePackage";

	public static final String TIGERSTRIPE_ROOT_TAG = "tigerstripe";

	public static final String DEPENDENCY_TAG = "dependency";

	public static final String REFERENCE_TAG = "reference";

	public static final String FACET_TAG = "facetReference";

	public static final String ENABLED_ATTRIBUTE = "enabled";

	/**
	 * The references to all plugins used by this project
	 */
	private Collection<IPluginConfig> pluginConfigs = new ArrayList<IPluginConfig>();

	/**
	 * A set of ArtifactRepositories
	 */
	private Collection<ArtifactRepository> artifactRepositories = new ArrayList<ArtifactRepository>();

	/**
	 * A set of dependencies
	 */
	private Collection<IDependency> dependencies = new ArrayList<IDependency>();

	/**
	 * A set of facet references
	 */
	private List<IFacetReference> facetReferences = new ArrayList<IFacetReference>();

	private List<ModelReference> modelReferences = new CopyOnWriteArrayList<ModelReference>();

	// ==========================================
	// ==========================================

	protected TigerstripeProject() {
		super(null, DEFAULT_FILENAME);
	}

	public TigerstripeProject(File baseDir) {
		super(baseDir, DEFAULT_FILENAME);
	}

	// ==========================================

	public synchronized IPluginConfig[] getPluginConfigs() {
		return this.pluginConfigs.toArray(new IPluginConfig[this.pluginConfigs.size()]);
	}

	public synchronized void setPluginConfigs(Collection<IPluginConfig> pluginConfigs) {
		setDirty();
		pluginConfigs.clear();
		for (IPluginConfig config : pluginConfigs) {
			pluginConfigs.add(config);
			if (config instanceof IContainedObject) {
				IContainedObject obj = (IContainedObject) config;
				obj.setContainer(this);
			}
		}
	}

	public synchronized void addPluginConfig(IPluginConfig pluginConfig) {
		setDirty();
		if (!this.pluginConfigs.contains(pluginConfig)) {
			this.pluginConfigs.add(pluginConfig);
			((IContainedObject) pluginConfig).setContainer(this);
		}
	}

	public synchronized void removePluginConfig(IPluginConfig pluginConfig) {
		setDirty();
		if (this.pluginConfigs.contains(pluginConfig)) {
			this.pluginConfigs.remove(pluginConfig);
			((IContainedObject) pluginConfig).setContainer(null);
		}
	}

	public synchronized Collection<ArtifactRepository> getArtifactRepositories() {
		return this.artifactRepositories;
	}

	public synchronized void setArtifactRepositories(Collection<ArtifactRepository> artifactRepositories) {
		setDirty();
		this.artifactRepositories.clear();
		for (ArtifactRepository repo : artifactRepositories) {
			this.artifactRepositories.add(repo);
			repo.setContainer(this);
		}
	}

	public synchronized IDependency[] getDependencies() {
		return this.dependencies.toArray(new IDependency[this.dependencies.size()]);
	}

	public synchronized void setDependencies(Collection<IDependency> dependencies) {
		setDirty();
		this.dependencies.clear();
		for (IDependency dep : dependencies) {
			this.dependencies.add(dep);
			((Dependency) dep).setContainer(this);
		}
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
	public synchronized void parse(Reader reader) throws TigerstripeException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream stream = new ReaderInputStream(reader);
			document = builder.parse(stream);

			// Load the descriptor version
			NodeList roots = document.getElementsByTagName(TIGERSTRIPE_ROOT_TAG);
			if (roots.getLength() != 1)
				throw new TigerstripeException(Messages.formatMessage(Messages.INVALID_DESCRIPTOR, null));
			else {
				Element root = (Element) roots.item(0);
				this.descriptorVersion = root.getAttribute("version");
				if (descriptorVersion == null || "".equals(descriptorVersion)) {
					descriptorVersion = "1.0.x";
				}
			}

			loadProjectDetails(document);
			loadRepositories(document);
			loadPluginConfigs(document);
			loadDependencies(document);
			loadReferences(document);
			loadFacetReferences(document);
			loadAdvancedProperties(document);
		} catch (SAXParseException spe) {
			TigerstripeRuntime.logErrorMessage("SAXParseException detected", spe);
			Object[] args = new Object[2];
			args[0] = spe.getMessage();
			args[1] = new Integer(spe.getLineNumber());
			throw new TigerstripeException(Messages.formatMessage(Messages.XML_PARSING_ERROR, args), spe);
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
	public synchronized void write(Writer writer) throws TigerstripeException {

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
			throw new TigerstripeException("Transformer Factory error" + tce.getMessage(), tce);
		} catch (TransformerException te) {
			TigerstripeRuntime.logErrorMessage("TransformerException detected", te);
			throw new TigerstripeException("Transformation error" + te.getMessage(), te);
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
	protected synchronized Document buildDOM() {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
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
			TigerstripeRuntime.logErrorMessage("ParserConfigurationException detected", e);
		}
		return document;
	}

	public synchronized String getBaseRepository() {
		if (artifactRepositories == null || artifactRepositories.isEmpty())
			return null;

		Iterator<ArtifactRepository> iter = artifactRepositories.iterator();

		ArtifactRepository repo = iter.next();
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

		for (Iterator<ArtifactRepository> iter = artifactRepositories.iterator(); iter.hasNext();) {
			ArtifactRepository repo = iter.next();
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

		for (IPluginConfig iPluginConfig : getPluginConfigs()) {
			PluginConfig ref = (PluginConfig) iPluginConfig;
			Element plugin = ref.buildPluginElement(document);

			plugins.appendChild(plugin);
		}

		return plugins;
	}

	private Element buildFacetRefsElement(Document document) {
		Element facetReferencesElement = document.createElement("facetReferences");
		for (IFacetReference ref : facetReferences) {
			Element refElm = FacetReference.encode(ref, document, this);
			if (refElm != null)
				facetReferencesElement.appendChild(refElm);
		}
		return facetReferencesElement;
	}

	private Element buildDependenciesElement(Document document) {
		Element dependenciesElm = document.createElement("dependencies");
		String corePath = TigerstripeRuntime.getProperty(TigerstripeRuntime.CORE_OSSJ_ARCHIVE);

		for (IDependency dependency : getDependencies()) {
			Dependency dep = (Dependency) dependency;
			if (!dep.getPath().equals(corePath)) {
				Element depElm = document.createElement(DEPENDENCY_TAG);

				depElm.setAttribute("path", Util.fixWindowsPath(dep.getPath()));
				depElm.setAttribute(ENABLED_ATTRIBUTE, dep.isEnabled() ? "true" : "false"); // Set
																							// enabled
																							// attribute

				dependenciesElm.appendChild(depElm);
			}
		}

		return dependenciesElm;
	}

	private Element buildReferencesElement(Document document) {
		Element referencesElm = document.createElement("references");

		for (ModelReference mRef : modelReferences) {
			Element refElm = document.createElement(REFERENCE_TAG);

			if (mRef instanceof LegacyModelReference) {
				LegacyModelReference mLRef = (LegacyModelReference) mRef;
				refElm.setAttribute("path", mLRef.getPath());
			} else {
				refElm.setAttribute("path", mRef.isResolved() ? mRef.getResolvedModel().getName() : "");
			}
			refElm.setAttribute("modelId", mRef.getToModelId());

			refElm.setAttribute(ENABLED_ATTRIBUTE, mRef.isEnabled() ? "true" : "false"); // Set
																							// enabled
																							// attribute

			referencesElm.appendChild(refElm);
		}

		return referencesElm;
	}

	private void loadRepositories(Document document) throws TigerstripeException {
		this.artifactRepositories = new ArrayList<ArtifactRepository>();

		NodeList repositories = document.getElementsByTagName(REPOSITORY_TAG);
		if (repositories.getLength() == 0)
			throw new TigerstripeException(Messages.formatMessage(Messages.NO_REPOSITORY_SPECIFIED, getFullPath()));
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
			} else if ((new File(getBaseDir().getAbsolutePath() + File.separator + baseDir)).exists()) {
				// this is a relative path
				repository = new ArtifactRepository(
						new File(getBaseDir().getAbsolutePath() + File.separator + baseDir));
			} else if ((new File(baseDir)).exists()) {
				// this is an absolute path
				repository = new ArtifactRepository(new File(baseDir));
			}

			// Get the Includes and excludes
			NodeList children = node.getChildNodes();
			ArrayList<String> includes = new ArrayList<String>();
			ArrayList<String> excludes = new ArrayList<String>();
			for (int j = 0; j < children.getLength(); j++) {
				if ("includes".equals(children.item(j).getNodeName())) {
					includes.add(children.item(j).getFirstChild().getNodeValue());
				} else if ("excludes".equals(children.item(j).getNodeName())) {
					excludes.add(children.item(j).getFirstChild().getNodeValue());
				}
			}

			repository.setIncludes(includes);
			repository.setExcludes(excludes);
			artifactRepositories.add(repository);
			repository.setContainer(this);
		}
	}

	private void loadPluginConfigs(Document document) throws TigerstripeException {

		this.pluginConfigs = new ArrayList<IPluginConfig>();
		NodeList plugins = document.getElementsByTagName(PluginConfig.PLUGIN_REFERENCE_TAG);
		
		for (int i = 0; i < plugins.getLength(); i++) {
			Node node = plugins.item(i);
			PluginConfig ref = PluginConfigFactory.getInstance().createPluginConfig((Element) node, this);

			// Bug 219954: when loading the descriptor, make sure we keep all
			// plugin configs even if they don't resolve. So that if the
			// corresponding plugin is deployed later on it will be picked up.
			try {
				ref.resolve();
			} catch (Exception e) {
				BasePlugin.logErrorMessage("An error occurred resolving plugin reference for " + ref.getPluginName(), e);
			}

			this.pluginConfigs.add(ref);
			ref.setContainer(this);
		}

	}

	public synchronized PluginConfig findPluginConfig(PluginConfig model) {
		for (IPluginConfig iPluginConfig : getPluginConfigs()) {
			PluginConfig ref = (PluginConfig) iPluginConfig;
			if (model.getPluginId().equals(ref.getPluginId()) && model.getGroupId().equals(ref.getGroupId()))
				return ref;
		}
		return null;
	}

	@Override
	public synchronized void validate(ITigerstripeVisitor visitor) throws TigerstripeException {
		// FIXME
	}

	private void loadDependencies(Document document) throws TigerstripeException {
		this.dependencies = new ArrayList<IDependency>();

		if (getBaseDir() == null)
			// dependencies within modules are ignored
			return;

		NodeList dependencyNode = document.getElementsByTagName(DEPENDENCY_TAG);

		for (int i = 0; i < dependencyNode.getLength(); i++) {
			Node node = dependencyNode.item(i);
			NamedNodeMap namedAttributes = node.getAttributes();
			Node path = namedAttributes.getNamedItem("path");
			Node enabledNode = namedAttributes.getNamedItem(ENABLED_ATTRIBUTE);
			boolean isEnabled = true;

			// NM: Set enabled bit
			if (enabledNode != null) {
				isEnabled = enabledNode.getNodeValue().equalsIgnoreCase("false") ? false : true;
			}

			String pathStr = Util.fixWindowsPath(path.getNodeValue());
			Dependency dep = new Dependency(this, pathStr);
			dep.setEnabled(isEnabled);

			addDependency(dep);
		}
	}

	private void loadFacetReferences(Document document) throws TigerstripeException {
		this.facetReferences = new ArrayList<IFacetReference>();

		NodeList referencesNode = document.getElementsByTagName("facetReferences");
		if (referencesNode == null || referencesNode.getLength() == 0)
			return;
		NodeList facetRefNode = ((Element) referencesNode.item(0)).getElementsByTagName(FACET_TAG);
		for (int i = 0; i < facetRefNode.getLength(); i++) {
			Node node = facetRefNode.item(i);
			IFacetReference ref = FacetReference.decode(node, this);
			if (ref != null)
				addFacetReference(ref);
		}
	}

	/**
	 * TODO Note by Navid: This method should be removed. There is a
	 * getTSProject() in TigerstripeProjectHandle that returns the actual TS
	 * Project. This method seems to return the Project HANDLE and there is
	 * already a getProjectHandle() here, which returns a
	 * TigerstripeProjectHandle, which really IS a ITigerstripeModelProject.
	 * 
	 * @return
	 */
	public synchronized ITigerstripeModelProject getTSProject() {
		if (getBaseDir() != null) {
			try {
				return (ITigerstripeModelProject) TigerstripeCore.findProject(getBaseDir().toURI());
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage("TigerstripeException detected", e);

			}
		}
		return null;
	}

	private void loadReferences(Document document) throws TigerstripeException {
		this.modelReferences = new ArrayList<ModelReference>();
		synchronized (modelReferences) {
			NodeList dependencyNode = document.getElementsByTagName(REFERENCE_TAG);

			for (int i = 0; i < dependencyNode.getLength(); i++) {
				Node node = dependencyNode.item(i);
				NamedNodeMap namedAttributes = node.getAttributes();
				Node pathNode = namedAttributes.getNamedItem("path");
				Node modelIdNode = namedAttributes.getNamedItem("modelId");
				Node enabledNode = namedAttributes.getNamedItem(ENABLED_ATTRIBUTE);

				String modelId = "";
				String path = pathNode.getNodeValue();
				boolean isEnabled = true;
				if (modelIdNode != null) {
					modelId = modelIdNode.getNodeValue();
				} else {
					// default back to path if no modelId saved
					modelId = path;
				}

				// NM: Set enabled bit
				if (enabledNode != null) {
					isEnabled = enabledNode.getNodeValue().equalsIgnoreCase("false") ? false : true;
				}

				// Build a model Reference for that
				LegacyModelReference mRef = new LegacyModelReference(modelId, path);
				mRef.setEnabled(isEnabled);

				modelReferences.add(mRef);
			}
		}
	}

	public synchronized void addDependency(IDependency dependency) {
		URI uri = dependency.getURI();
		IModuleHeader header = dependency.getIModuleHeader();

		if (!this.dependencies.contains(dependency)) {
			setDirty();
			this.dependencies.add(dependency);
			((Dependency) dependency).setContainer(this);

			try {
				if (uri != null && header != null) {
					ModuleAnnotationManager.INSTANCE.registerAnnotationsFor(uri, header.getModuleID());
				}
			} catch (IOException e) {
				BasePlugin.log(e);
			}
		}
	}

	public synchronized void removeDependency(IDependency dependency) {
		if (this.dependencies.contains(dependency)) {
			setDirty();
			this.dependencies.remove(dependency);
			((Dependency) dependency).setContainer(null);

			try {
				ModuleAnnotationManager.INSTANCE.unRegisterAnnotationsFor(dependency.getURI());
			} catch (IOException e) {
				BasePlugin.log(e);
			}
		}
	}

	public synchronized void addDependencies(IDependency[] dependencies) {
		for (int i = 0; i < dependencies.length; i++) {
			addDependency(dependencies[i]);
		}
	}

	public synchronized void removeDependencies(IDependency[] dependencies) {
		for (int i = 0; i < dependencies.length; i++) {
			removeDependency(dependencies[i]);
		}
	}

	public synchronized void addModelReference(ModelReference mRef) throws TigerstripeException {
		setDirty();
		synchronized (modelReferences) {
			modelReferences.add(mRef);
		}
	}

	public synchronized void addModelReferences(ModelReference[] mRefs) throws TigerstripeException {
		setDirty();
		synchronized (modelReferences) {
			for (ModelReference mRef : mRefs)
				modelReferences.add(mRef);
		}
	}

	public synchronized void removeModelReference(ModelReference mRef) throws TigerstripeException {
		setDirty();
		synchronized (modelReferences) {
			modelReferences.remove(mRef);
		}
	}

	public synchronized void removeModelReferences(ModelReference[] mRefs) throws TigerstripeException {
		setDirty();
		synchronized (modelReferences) {
			for (ModelReference mRef : mRefs)
				modelReferences.remove(mRef);
		}
	}

	public synchronized ModelReference[] getModelReferences() {
		return modelReferences.toArray(new ModelReference[modelReferences.size()]);
	}

	public synchronized ITigerstripeModelProject[] getReferencedProjects() {
		List<ITigerstripeModelProject> result = new ArrayList<ITigerstripeModelProject>();
		for (ModelReference mRef : modelReferences) {
			ITigerstripeModelProject model = mRef.getResolvedModel();
			if (model != null)
				result.add(model);
		}
		return result.toArray(new ITigerstripeModelProject[result.size()]);
	}

	// NM: Return just the enabled dependencies
	public synchronized IDependency[] getEnabledDependencies() {
		IDependency[] result = getDependencies();
		if (result == null || result.length == 0)
			return result;

		ArrayList<IDependency> finalResult = new ArrayList<IDependency>();
		for (int i = 0; i < result.length; i++) {
			if (result[i].isEnabled())
				finalResult.add(result[i]);
		}

		return finalResult.toArray(new IDependency[finalResult.size()]);
	}

	// NM: Return just the enabled model references
	public synchronized ModelReference[] getEnabledModelReferences() {
		ModelReference[] result = getModelReferences();
		if (result == null || result.length == 0)
			return result;

		ArrayList<ModelReference> finalResult = new ArrayList<ModelReference>();
		for (int i = 0; i < result.length; i++) {
			if (result[i].isEnabled())
				finalResult.add(result[i]);
		}

		return finalResult.toArray(new ModelReference[finalResult.size()]);
	}

	// NM: Return just the enabled referenced projects
	public synchronized ITigerstripeModelProject[] getEnabledReferencedProjects() {
		List<ITigerstripeModelProject> result = new ArrayList<ITigerstripeModelProject>();
		ModelReference[] enabledModelReferences = getEnabledModelReferences();
		if (enabledModelReferences != null) {
			for (ModelReference mRef : enabledModelReferences) {
				ITigerstripeModelProject model = mRef.getResolvedModel();
				if (model != null)
					result.add(model);
			}
		}
		return result.toArray(new ITigerstripeModelProject[result.size()]);

	}

	public synchronized boolean hasReference(ITigerstripeModelProject project) {
		if (project == null)
			return false;

		for (ModelReference mRef : modelReferences) {
			if (mRef.isReferenceTo(project))
				return true;
		}
		return false;
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
	 * @param ignoreOrder
	 *            - whether order should be ignored during comparison
	 * @return true if both projects have same dependencies, false otherwise
	 */
	public synchronized boolean hasSameDependencies(TigerstripeProject other, boolean ignoreOrder) {

		IDependency[] otherDeps = other.getDependencies();
		if (getDependencies().length != otherDeps.length)
			return false;

		if (otherDeps.length == 0)
			return true;

		int index = 0;
		for (IDependency dep : otherDeps) {
			boolean matched = false;

			if (dep.isValid()) {
				if (ignoreOrder) {
					// When ignoring order, simply look for it
					for (IDependency lDep : getDependencies()) {
						if (lDep.isValid()) {
							// 2 dependencies are considered equal in this
							// context when
							// they have the same relPath and moduleID
							if (lDep.getPath().equals(dep.getPath()) && lDep.getIModuleHeader().getModuleID()
									.equals(dep.getIModuleHeader().getModuleID())) {
								matched = true;
							}
						}
					}
				} else {
					// when not ignoring order, needs to match on the index
					IDependency lDep = ((ArrayList<IDependency>) dependencies).get(index);
					if (lDep.isValid()) {
						// 2 dependencies are considered equal in this context
						// when
						// they have the same relPath and moduleID
						if (lDep.getPath().equals(dep.getPath())
								&& lDep.getIModuleHeader().getModuleID().equals(dep.getIModuleHeader().getModuleID())) {
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
	 * @param ignoreOrder
	 *            - whether order should be ignored during comparison
	 * @return true if both projects have same references, false otherwise
	 */
	public synchronized boolean hasSameReferences(TigerstripeProject other, boolean ignoreOrder) {

		ITigerstripeModelProject[] otherRefs = other.getReferencedProjects();
		if (getReferencedProjects().length != otherRefs.length)
			return false;

		int index = 0;
		for (ITigerstripeModelProject oRef : otherRefs) {
			boolean matched = false;

			if (ignoreOrder) {
				// When ignoring order, simply look for it
				for (ITigerstripeModelProject lRef : getReferencedProjects()) {
					if (lRef.getName().equals(oRef.getName())) {
						matched = true;
					}
				}
			} else {
				ITigerstripeModelProject lRef = getReferencedProjects()[index];
				if (lRef.getName().equals(oRef.getName())) {
					matched = true;
				}
			}

			if (!matched)
				return false;
		}
		return true;
	}

	public synchronized void addFacetReference(IFacetReference facetRef) throws TigerstripeException {
		if (!facetReferences.contains(facetRef)) {
			setDirty();
			facetReferences.add(facetRef);
			((FacetReference) facetRef).setContainer(this);
		}
	}

	public synchronized void removeFacetReference(IFacetReference facetRef) throws TigerstripeException {
		if (facetReferences.contains(facetRef)) {
			setDirty();
			facetReferences.remove(facetRef);
			((FacetReference) facetRef).setContainer(null);
		}
	}

	public synchronized IFacetReference[] getFacetReferences() throws TigerstripeException {
		return facetReferences.toArray(new IFacetReference[facetReferences.size()]);
	}

	public ITigerstripeModelProject[] getIReferencedProjects() throws TigerstripeException {
		return getReferencedProjects();
	}

	public IProjectDetails getIProjectDetails() throws TigerstripeException {
		return getProjectDetails();
	}

	public synchronized String getRepositoryLocation() throws TigerstripeException {
		List<ArtifactRepository> repositories = (List<ArtifactRepository>) getArtifactRepositories();
		if (repositories == null || repositories.size() == 0)
			throw new TigerstripeException(
					"No defined Artifact repository in " + ITigerstripeConstants.PROJECT_DESCRIPTOR);

		ArtifactRepository repo = repositories.get(0);
		if (repo.getIncludes().length == 0)
			throw new TigerstripeException("No 'includes' defined for repository " + repo.getBaseDirectory());

		String repoLocation = repo.getIncludes()[0];
		int index = repoLocation.indexOf("**/*.java");
		if (index == -1)
			throw new TigerstripeException("Incorrect 'includes' clause for repository: '" + repoLocation
					+ "'. Must follow xxx/yyy/**/*.java");
		repoLocation = repoLocation.substring(0, index);

		return repoLocation;
	}

	public synchronized void reloadFrom(Reader reader) throws TigerstripeException {
		parse(reader);
	}

	public synchronized final LegacyModelReference makeLegacyModelReferenceFrom(ITigerstripeModelProject project)
			throws TigerstripeException {
		ModelReference mRef = ModelReference.referenceFromProject(project);
		LegacyModelReference mLRef = new LegacyModelReference(mRef.getProjectContext(), mRef.getToModelId(),
				project.getName());
		return mLRef;
	}

	/**
	 * 
	 */
	public synchronized TigerstripeProjectHandle getProjectHandle() {
		TigerstripeProjectHandle handle = null;

		if (getBaseDir() == null)
			return null;

		try {
			handle = (TigerstripeProjectHandle) TigerstripeCore.findProject(getBaseDir().toURI());
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected", e);
		}
		return handle;
	}

	@Override
	public synchronized void descriptorChanged(IResource changedDescriptor) {
		// If the descriptor has changed, then we need to reload for sure!
		IProject project = (IProject) this.getAdapter(IProject.class);
		if (project != null && changedDescriptor.getProject().equals(project)) {
			// Its our descriptor!
			try {
				reload(true);
			} catch (TigerstripeException e) {

				TigerstripeRuntime.logErrorMessage("Error on Descriptor change", e);
			}
		}

	}
}