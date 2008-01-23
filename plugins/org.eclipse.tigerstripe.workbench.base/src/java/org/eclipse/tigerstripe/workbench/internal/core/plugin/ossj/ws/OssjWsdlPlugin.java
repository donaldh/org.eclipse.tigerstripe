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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.ossj.ws;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactNoFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageBasedArtifactFilter;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.Expander;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PackageToSchemaMapper;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginBody;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginRef;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginRefFactory;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.XmlPluginRef;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PackageToSchemaMapper.PckXSDMapping;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.ossj.OssjUtil;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.ossjxml.SchemaUtils;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.ossjxml.XmlSchemaImportsHelper;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.ossjxml.XmlSchemaImportsHelper.NamespaceRef;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.project.IPluginReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OssjWsdlPlugin extends BasePlugin {

	// These are the defined properties that can be set for this ref
	public final static String TARGET_NAMESPACE = "targetNamespace";

	public final static String INCLUDE_WSNOTIFICATIONS = "includeWSNotifications";

	public final static String WSNOTIFICATION_LOCATION = "WSNotificationsLocation";

	public final static String[] PROPERTIES = { TARGET_NAMESPACE,
			INCLUDE_WSNOTIFICATIONS, WSNOTIFICATION_LOCATION };

	private final static String LABEL = "OSS/J Web Services Integration Profile";

	private final static String DESCRIPTION = "Generates the OSS/J WS Integration Profile.";

	public final static String PLUGIN_ID = "ossj-wsdl-spec";

	private final static String GROUP_ID = PluginRefFactory.GROUPID_TS;

	private final static String VERSION = PluginRefFactory.VERSION_1_3;

	private final static String REPORTTEMPLATE = "OSSJ_WSDL_REPORT.vm";

	private Map namespaceMap;

	private final static String[] supportedNatures = { PluginBody.OSSJ_NATURE };

	private PluginReport report;

	public PluginReport getReport() {
		return this.report;
	}

	public String[] getSupportedNatures() {
		return supportedNatures;
	}

	public String getLabel() {
		return LABEL;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getPluginId() {
		return PLUGIN_ID;
	}

	public String getGroupId() {
		return GROUP_ID;
	}

	public String getVersion() {
		return VERSION;
	}

	/**
	 * This method figures out the xsd to import based on the given facade. We
	 * only need to import one XSD (the one where the facade is defined as it
	 * will itself import all the required definitions).
	 * 
	 * @return
	 */
	public PckXSDMapping computeXSDImport(ISessionArtifact facade,
			PluginRef pluginRef) throws TigerstripeException {
		TigerstripeProject tsProject = pluginRef.getProject();
		XmlPluginRef xmlRef = (XmlPluginRef) tsProject
				.findPluginRef(XmlPluginRef.MODEL);

		PackageToSchemaMapper mapper = xmlRef.getMapper();
		if (mapper.useDefaultMapping())
			return mapper.getDefaultMapping();
		else {
			// Fing the mapping that corresponds to the facade
			String facadePkg = facade.getPackage();
			PckXSDMapping targetMapping = mapper.getPckXSDMapping(facadePkg);
			if (targetMapping == null)
				throw new TigerstripeException(
						"Facade '"
								+ facade.getFullyQualifiedName()
								+ "' doesn't belong to a mapped package. No XSD to import.");
			return targetMapping;
		}
	}

	public void trigger(PluginRef pluginRef, RunConfig config)
			throws TigerstripeException {

		// try {
		ITigerstripeProject handle = pluginRef.getProjectHandle();
		// (ITigerstripeProject) API.getDefaultProjectSession()
		// .makeTigerstripeProject(
		// pluginRef.getProject().getBaseDir().toURI(), null);
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
				.getArtifactManagerSession();

		ArtifactManager artifactMgr = session.getArtifactManager();
		this.report = new PluginReport(pluginRef);
		this.report.setTemplate(OssjWsdlModel.TEMPLATE_PREFIX + "/"
				+ pluginRef.getActiveVersion() + "/" + REPORTTEMPLATE);

		buildNamespaceList(artifactMgr, pluginRef);

		Collection facades = artifactMgr.getArtifactsByModel(
				SessionFacadeArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		for (Iterator iter = facades.iterator(); iter.hasNext();) {
			SessionFacadeArtifact facade = (SessionFacadeArtifact) iter.next();
			OssjWsdlModel model = new OssjWsdlModel(facade, pluginRef);

			generateWithTemplate(model, pluginRef, artifactMgr, config);

		}
		// } catch (TigerstripeLicenseException e) {
		// throw new TigerstripeException("License issue", e);
		// }
	}

	private void generateWithTemplate(OssjWsdlModel model, PluginRef pluginRef,
			ArtifactManager artifactMgr, RunConfig config)
			throws TigerstripeException {

		TigerstripeProject tsProject = pluginRef.getProject();
		XmlPluginRef xmlRef = (XmlPluginRef) tsProject
				.findPluginRef(XmlPluginRef.MODEL);

		// Some extra stuff in case of multiple schemas in same package
		// which means we changed the name...

		PackageToSchemaMapper mapper = xmlRef.getMapper();
		PckXSDMapping mapping;
		if (mapper.useDefaultMapping()) {
			mapping = mapper.getDefaultMapping();
		} else {
			// do this so that it's never null!
			mapping = mapper.getDefaultMapping();
			PckXSDMapping[] mappings = mapper.getMappings();
			for (int i = 0; i < mappings.length; i++) {
				if (mappings[i].getPackage().equals(
						model.getArtifact().getPackage())) {
					mapping = mappings[i];
					continue;
				}
			}
		}

		ArtifactFilter filter = null;

		if (xmlRef.getMapper().useDefaultMapping()) {
			filter = new ArtifactNoFilter();
		} else {
			filter = new PackageBasedArtifactFilter(model.getArtifact()
					.getPackage(), mapper.getAllMappedPackages());
		}
		Collection all_sessions = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(SessionFacadeArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor()), filter);

		/*
		 * // Let's put what we'll need in the context and get going Collection
		 * entities = artifactMgr.getArtifactsByModel(
		 * ManagedEntityArtifact.MODEL, false); Collection datatypes =
		 * artifactMgr.getArtifactsByModel( DatatypeArtifact.MODEL, false);
		 * Collection events = artifactMgr.getArtifactsByModel(
		 * EventArtifact.MODEL, false); Collection enums =
		 * artifactMgr.getArtifactsByModel( EnumArtifact.MODEL, false);
		 * Collection exceptions = artifactMgr.getArtifactsByModel(
		 * ExceptionArtifact.MODEL, false); Collection queries =
		 * artifactMgr.getArtifactsByModel( QueryArtifact.MODEL, false);
		 * Collection updates = artifactMgr.getArtifactsByModel(
		 * UpdateProcedureArtifact.MODEL, false);
		 * 
		 */

		XmlSchemaImportsHelper importsHelper = new XmlSchemaImportsHelper(
				xmlRef.getMapper());
		// The list of imports will be based on the Sessions and whatever they
		// reference
		model.addContent(model.getArtifact());
		importsHelper.buildImportList(artifactMgr, model.getContent(), false);

		SchemaUtils sUtils = new SchemaUtils(xmlRef, artifactMgr, importsHelper);
		Expander exp = new Expander(pluginRef);
		VelocityContext localContext = new VelocityContext(getDefaultContext());
		/*
		 * localContext.put("entities", entities); // TODO scope?
		 * localContext.put("datatypes", datatypes); // TODO scope?
		 * localContext.put("events", events); // TODO scope?
		 * localContext.put("enumerations", enums); // TODO scope?
		 * localContext.put("exceptions", exceptions); // TODO scope?
		 * localContext.put("queries", queries); // TODO scope?
		 * localContext.put("updateProcs", updates); // TODO scope?
		 */
		localContext.put("ossjUtil", new OssjUtil(artifactMgr, pluginRef));
		localContext.put("ossjXmlUtil", new OssjWsdlUtil());
		localContext.put("ossjWsdlUtil", new OssjWsdlUtil());
		localContext.put("model", model);
		localContext.put("artifact", model.getArtifact());
		localContext.put("pluginRef", pluginRef);
		localContext.put("tsProject", pluginRef.getProject());
		localContext.put("runtime", TigerstripeRuntime.getInstance());
		localContext.put("schemaUtils", sUtils);
		localContext.put("exp", exp);

		String sessName = model.getArtifact().getName();
		String wsdlProp = pluginRef.getProperties().getProperty(
				"targetNamespace",
				"http://www.eclipse.tigerstripe.org/xml/DefaultWSDLName");

		// PckXSDMapping mapping = computeXSDImport( (ISessionArtifact)
		// model.getArtifact(), pluginRef);
		// localContext.put( "xsdMapping", mapping );

		try {
			if (all_sessions.size() > 1) {
				localContext
						.put("prefix", exp.expandVar(sUtils.insertSessionName(
								pluginRef.getProperties().getProperty(
										"targetPrefix", "tns"), sessName)));
				localContext.put("wsdlName", exp.expandVar(sUtils
						.insertSessionName(wsdlProp, sessName)));
				// Need to change pointers to source .xsd as its been renamed
				// Add the new Namespace details to the importsList

				NamespaceRef newRef = importsHelper.new NamespaceRef();

				newRef.targetNamespace = exp.expandVar(sUtils
						.insertSessionName(mapping.getTargetNamespace(),
								sessName));
				newRef.targetLocation = exp.expandVar(sUtils.insertSessionName(
						mapping.getXsdLocation(), sessName));
				newRef.prefix = exp.expandVar(sUtils.insertSessionName(mapping
						.getUserPrefix(), sessName));

				sUtils.getNRefs().add(newRef);

				// and remove the original
				for (int a = 0; a < sUtils.getNRefs().size(); a++) {
					NamespaceRef oldRef = (NamespaceRef) sUtils.getNRefs()
							.toArray()[a];
					if (oldRef.getNamespace().equals(
							mapping.getTargetNamespace())) {
						sUtils.getNRefs().remove(oldRef);
						continue;
					}
				}

				localContext.put("xsdNamespace", newRef.prefix);

			} else {
				localContext.put("prefix", exp.expandVar(pluginRef
						.getProperties().getProperty("targetPrefix", "tns")));
				localContext.put("wsdlName", exp.expandVar(wsdlProp));
				localContext.put("xsdNamespace", sUtils
						.getPrefixForArtifact(model.getArtifact()
								.getFullyQualifiedName()));

			}

			// generic case
			setClasspathLoaderForVelocity();
			Template template = Velocity.getTemplate(model.getTemplate());
			String filename = model.getDestinationDir() + File.separator
					+ model.getName() + ".wsdl";
			setDefaultDestination(pluginRef, new File(filename), config);
			// create the output
			template.merge(localContext, getDefaultWriter());
			getDefaultWriter().close();
			Collection<String> files = this.report.getGeneratedFiles();
			files.add(model.getName() + ".wsdl");

		} catch (MethodInvocationException e) {
			Throwable th = e.getWrappedThrowable();
			String errorMsg = null;
			if (th instanceof TigerstripeException) {
				TigerstripeException ee = (TigerstripeException) th;
				errorMsg = ee.getLocalizedMessage() + " while calling "
						+ e.getMethodName() + " within '" + model.getTemplate()
						+ "' template." + "(in "
						+ model.getArtifact().getFullyQualifiedName() + ").";
			} else
				errorMsg = "Unknown Error in "
						+ model.getArtifact().getFullyQualifiedName()
						+ "(while calling " + e.getMethodName() + " within '"
						+ model.getTemplate() + "' template).";
			throw new TigerstripeException(errorMsg, e);
		} catch (TigerstripeException e) {
			throw e;
		} catch (Exception e) {
			throw new TigerstripeException("Unexpected error while merging '"
					+ model.getTemplate() + "' template for "
					+ model.getArtifact().getFullyQualifiedName() + ".", e);
		}

	}

	/**
	 * Go thru all the abstract artifacts to figure out what the namespaces will
	 * be.
	 * 
	 */
	private void buildNamespaceList(ArtifactManager artifactMgr,
			PluginRef pluginRef) {
		Collection artifacts = artifactMgr.getAllArtifacts(true,
				new TigerstripeNullProgressMonitor());

		this.namespaceMap = new HashMap();

		// Iterate thru all artifacts, and build a model to extract
		// the namespace and location for each
		int prefix = 0;
		for (Iterator iter = artifacts.iterator(); iter.hasNext();) {
			AbstractArtifact artifact = (AbstractArtifact) iter.next();
			try {
				OssjWsdlModel model = new OssjWsdlModel(artifact, pluginRef);
				if (!this.namespaceMap.containsKey(model.getTargetNamespace())) {
					Namespace ns = new Namespace();
					ns.setTargetNamespace(model.getTargetNamespace());
					ns.setSchemaLocation(model.getSchemaLocation());
					ns.setPrefix("p" + prefix);
					prefix++;
					this.namespaceMap.put(model.getTargetNamespace(), ns);
				}
			} catch (TigerstripeException e) {
				// TODO: not sure what to do here.
				// TODO: needs proper error handling
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}
	}

	public class Namespace {

		private String targetNamespace;

		private String schemaLocation;

		private String prefix;

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public String getPrefix() {
			return this.prefix;
		}

		public String getTargetNamespace() {
			return this.targetNamespace;
		}

		public void setTargetNamespace(String targetNamespace) {
			this.targetNamespace = targetNamespace;
		}

		public String getSchemaLocation() {
			return this.schemaLocation;
		}

		public void setSchemaLocation(String schemaLocation) {
			this.schemaLocation = schemaLocation;
		}
	}

	public String[] getDefinedProperties() {
		return PROPERTIES;
	}

	/**
	 * Returns true if this plugin is enabled in the given ITigerstripeProject
	 * 
	 * @param pluginRef
	 * @return
	 */
	public static boolean isEnabled(ITigerstripeProject tsProject)
			throws TigerstripeException {
		IPluginReference[] refs = tsProject.getPluginReferences();

		for (int i = 0; i < refs.length; i++) {
			if (PLUGIN_ID.equals(refs[i].getPluginId())
					&& GROUP_ID.equals(refs[i].getGroupId())
					&& refs[i].isEnabled())
				return true;
		}

		return false;
	}

	public int getCategory() {
		return IPluginReference.GENERATE_CATEGORY;
	}

}