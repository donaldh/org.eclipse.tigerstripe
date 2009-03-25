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
import java.net.URI;
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

import org.apache.tools.ant.util.ReaderInputStream;
import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.espace.core.Mode;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.annotation.ModuleAnnotationManager;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.workbench.internal.api.utils.IProjectLocator;
import org.eclipse.tigerstripe.workbench.internal.contract.segment.FacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.locale.Messages;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfigFactory;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.UnknownPluginException;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDescriptor;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.IDescriptorReferencedProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
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
 *         This represent a TigerstripeProject. It corresponds to the
 *         "Tigerstripe.xml" project descriptor.
 * 
 *         This conditions a run of Tigerstripe.
 */
public class TigerstripeProject extends AbstractTigerstripeProject implements
		IProjectDescriptor {

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

	/**
	 * Tigerstripe projects referenced from this one.
	 */
	private List<ITigerstripeModelProject> referencedProjects = new ArrayList<ITigerstripeModelProject>();
	
	/**
	 * Tigerstripe project references contained in descriptor
	 */
	private List<IDescriptorReferencedProject> descriptorsReferencedProjects = new ArrayList<IDescriptorReferencedProject>();

	// ==========================================
	// ==========================================

	public TigerstripeProject(File baseDir) {
		super(baseDir, DEFAULT_FILENAME);
	}

	// ==========================================

	public IPluginConfig[] getPluginConfigs() {
		return this.pluginConfigs.toArray(new IPluginConfig[this.pluginConfigs
				.size()]);
	}

	public void setPluginConfigs(Collection<IPluginConfig> pluginConfigs) {
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

	public void addPluginConfig(IPluginConfig pluginConfig) {
		setDirty();
		if (!this.pluginConfigs.contains(pluginConfig)) {
			this.pluginConfigs.add(pluginConfig);
			((IContainedObject) pluginConfig).setContainer(this);
		}
	}

	public void removePluginConfig(IPluginConfig pluginConfig) {
		setDirty();
		if (this.pluginConfigs.contains(pluginConfig)) {
			this.pluginConfigs.remove(pluginConfig);
			((IContainedObject) pluginConfig).setContainer(null);
		}
	}

	public Collection<ArtifactRepository> getArtifactRepositories() {
		return this.artifactRepositories;
	}

	public void setArtifactRepositories(
			Collection<ArtifactRepository> artifactRepositories) {
		setDirty();
		this.artifactRepositories.clear();
		for (ArtifactRepository repo : artifactRepositories) {
			this.artifactRepositories.add(repo);
			repo.setContainer(this);
		}
	}

	public IDependency[] getDependencies() {
		return this.dependencies.toArray(new IDependency[this.dependencies
				.size()]);
	}

	public void setDependencies(Collection<IDependency> dependencies) {
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
			loadPluginConfigs(document);
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

		for (Iterator<ArtifactRepository> iter = artifactRepositories
				.iterator(); iter.hasNext();) {
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
		Element facetReferencesElement = document
				.createElement("facetReferences");
		for (IFacetReference ref : facetReferences) {
			Element refElm = FacetReference.encode(ref, document, this);
			if (refElm != null)
				facetReferencesElement.appendChild(refElm);
		}
		return facetReferencesElement;
	}

	private Element buildDependenciesElement(Document document) {
		Element dependenciesElm = document.createElement("dependencies");
		String corePath = TigerstripeRuntime
				.getProperty(TigerstripeRuntime.CORE_OSSJ_ARCHIVE);

		for (IDependency dependency : getDependencies()) {
			Dependency dep = (Dependency) dependency;
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

		for (ITigerstripeModelProject ref : getReferencedProjects()) {
			Element refElm = document.createElement(REFERENCE_TAG);

			try {
				URI projectURI = ref.getLocation().toFile().toURI();
				IProjectLocator loc = (IProjectLocator) InternalTigerstripeCore
						.getFacility(InternalTigerstripeCore.PROJECT_LOCATOR_FACILITY);
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
		this.artifactRepositories = new ArrayList<ArtifactRepository>();

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
			ArrayList<String> includes = new ArrayList<String>();
			ArrayList<String> excludes = new ArrayList<String>();
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
			repository.setContainer(this);
		}
	}

	private void loadPluginConfigs(Document document)
			throws TigerstripeException {

		this.pluginConfigs = new ArrayList<IPluginConfig>();
		NodeList plugins = document
				.getElementsByTagName(PluginConfig.PLUGIN_REFERENCE_TAG);

		for (int i = 0; i < plugins.getLength(); i++) {
			Node node = plugins.item(i);
			PluginConfig ref = PluginConfigFactory.getInstance()
					.createPluginConfig((Element) node, this);

			// Bug 219954: when loading the descriptor, make sure we keep all
			// plugin configs even if they don't resolve. So that if the
			// corresponding plugin is deployed later on it will be picked up.
			try {
				ref.resolve();
			} catch (UnknownPluginException e) {
			}

			this.pluginConfigs.add(ref);
			ref.setContainer(this);
		}
	}

	public PluginConfig findPluginConfig(PluginConfig model) {
		for (IPluginConfig iPluginConfig : getPluginConfigs()) {
			PluginConfig ref = (PluginConfig) iPluginConfig;
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

		NodeList referencesNode = document
				.getElementsByTagName("facetReferences");
		if (referencesNode == null || referencesNode.getLength() == 0)
			return;
		NodeList facetRefNode = ((Element) referencesNode.item(0))
				.getElementsByTagName(FACET_TAG);
		for (int i = 0; i < facetRefNode.getLength(); i++) {
			Node node = facetRefNode.item(i);
			IFacetReference ref = FacetReference.decode(node, this);
			if (ref != null)
				addFacetReference(ref);
		}
	}

	
	public ITigerstripeModelProject getTSProject() {
		if (getBaseDir() != null) {
			try {
				return (ITigerstripeModelProject) TigerstripeCore
						.findProject(getBaseDir().toURI());
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);

			}
		}
		return null;
	}

	private void loadReferences(Document document) throws TigerstripeException {

		this.referencedProjects = new ArrayList<ITigerstripeModelProject>();
		this.descriptorsReferencedProjects = new ArrayList<IDescriptorReferencedProject>();

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

			IProjectLocator loc = (IProjectLocator) InternalTigerstripeCore
					.getFacility(InternalTigerstripeCore.PROJECT_LOCATOR_FACILITY);

			ITigerstripeModelProject self = (ITigerstripeModelProject) TigerstripeCore
					.findProject(getBaseDir().toURI());
			
			URI uri = null;
			try {
			uri = loc.locate(self, label);
			} catch (TigerstripeException t) {
				
			}

			ITigerstripeModelProject prj=null;
			if(uri!=null){
			prj = (ITigerstripeModelProject) TigerstripeCore.findProject(uri);
			}

			if (prj != null){
				addReferencedProject(prj);
			    addDescriptorReferencedProject(prj,label);
			} else {
				addDescriptorReferencedProject(prj,label);
			}
			
			    

		}
	}

	public void addDependency(IDependency dependency) {
		if (!this.dependencies.contains(dependency)) {
			setDirty();
			this.dependencies.add(dependency);
			((Dependency) dependency).setContainer(this);

			try {
				ModuleAnnotationManager.INSTANCE.registerAnnotationsFor(
						dependency.getURI(), dependency.getIModuleHeader()
								.getModuleID(), Mode.READ_ONLY);
			} catch (IOException e) {
				BasePlugin.log(e);
			}
		}
	}

	public void removeDependency(IDependency dependency) {
		if (this.dependencies.contains(dependency)) {
			setDirty();
			this.dependencies.remove(dependency);
			((Dependency) dependency).setContainer(null);

			try {
				ModuleAnnotationManager.INSTANCE
						.unRegisterAnnotationsFor(dependency.getURI());
			} catch (IOException e) {
				BasePlugin.log(e);
			}
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

	public void addReferencedProject(ITigerstripeModelProject project)
			throws TigerstripeException {
		setDirty();
		referencedProjects.add(project);
	}
	
	public void addDescriptorReferencedProject(ITigerstripeModelProject project, String label){
		setDirty();
		DescriptorReferencedProject ref = new DescriptorReferencedProject();
		ref.setProject(project);
		ref.setProjectName(label);
		descriptorsReferencedProjects.add(ref);
	}

	public void removeReferencedProject(ITigerstripeModelProject project)
			throws TigerstripeException {
		setDirty();
		referencedProjects.remove(project);
	}
	
	public void removeDescriptorReferencedProject(IDescriptorReferencedProject project)
	throws TigerstripeException {
    setDirty();
    descriptorsReferencedProjects.remove(project);
    }

	public void addReferencedProjects(ITigerstripeModelProject[] projects)
			throws TigerstripeException {
		for (ITigerstripeModelProject project : projects) {
			addReferencedProject(project);
		}
	}

	public void removeReferencedProjects(IDescriptorReferencedProject[] projects)
			throws TigerstripeException {
		for (IDescriptorReferencedProject project : projects) {
			if(project.getProject()!= null)removeReferencedProject(project.getProject());
			removeDescriptorReferencedProject(project);
		}
	}
	
	public void removeReferencedProjects(ITigerstripeModelProject[] projects)
			throws TigerstripeException {
		for (ITigerstripeModelProject project : projects) {
			removeReferencedProject(project);
		}
	}

	public ITigerstripeModelProject[] getReferencedProjects() {
		return referencedProjects
				.toArray(new ITigerstripeModelProject[referencedProjects.size()]);
	}
	
	public IDescriptorReferencedProject[] getDescriptorsReferencedProjects() {
		return descriptorsReferencedProjects
				.toArray(new IDescriptorReferencedProject[descriptorsReferencedProjects.size()]);
	}

	public boolean hasReference(ITigerstripeModelProject project) {
        if(project == null) return false;
		for (Iterator<ITigerstripeModelProject> iter = referencedProjects
				.iterator(); iter.hasNext();) {
			ITigerstripeModelProject prj = (ITigerstripeModelProject) iter
					.next();
			if (project.getLocation().equals(prj.getLocation()))
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
	public boolean hasSameDependencies(TigerstripeProject other,
			boolean ignoreOrder) {

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
	 * @param ignoreOrder
	 *            - whether order should be ignored during comparison
	 * @return true if both projects have same references, false otherwise
	 */
	public boolean hasSameReferences(TigerstripeProject other,
			boolean ignoreOrder) {

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

	public void addFacetReference(IFacetReference facetRef)
			throws TigerstripeException {
		if (!facetReferences.contains(facetRef)) {
			setDirty();
			facetReferences.add(facetRef);
			((FacetReference) facetRef).setContainer(this);
		}
	}

	public void removeFacetReference(IFacetReference facetRef)
			throws TigerstripeException {
		if (facetReferences.contains(facetRef)) {
			setDirty();
			facetReferences.remove(facetRef);
			((FacetReference) facetRef).setContainer(null);
		}
	}

	public IFacetReference[] getFacetReferences() throws TigerstripeException {
		return facetReferences.toArray(new IFacetReference[facetReferences
				.size()]);
	}

	public ITigerstripeModelProject[] getIReferencedProjects()
			throws TigerstripeException {
		return getReferencedProjects();
	}

	public IProjectDetails getIProjectDetails() throws TigerstripeException {
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

	public void reloadFrom(Reader reader) throws TigerstripeException {
		parse(reader);
	}

}