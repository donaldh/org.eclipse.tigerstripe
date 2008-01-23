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
package org.eclipse.tigerstripe.core.plugin.ossjxml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.generation.RunConfig;
import org.eclipse.tigerstripe.core.model.ArtifactFilter;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.model.ArtifactNoFilter;
import org.eclipse.tigerstripe.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.core.model.EnumArtifact;
import org.eclipse.tigerstripe.core.model.EventArtifact;
import org.eclipse.tigerstripe.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.core.model.PackageBasedArtifactFilter;
import org.eclipse.tigerstripe.core.model.QueryArtifact;
import org.eclipse.tigerstripe.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.core.plugin.Expander;
import org.eclipse.tigerstripe.core.plugin.PackageToSchemaMapper;
import org.eclipse.tigerstripe.core.plugin.PluginBody;
import org.eclipse.tigerstripe.core.plugin.PluginRef;
import org.eclipse.tigerstripe.core.plugin.PluginRefFactory;
import org.eclipse.tigerstripe.core.plugin.XmlPluginRef;
import org.eclipse.tigerstripe.core.plugin.PackageToSchemaMapper.PckXSDMapping;
import org.eclipse.tigerstripe.core.plugin.base.BasePlugin;
import org.eclipse.tigerstripe.core.plugin.ossj.OssjUtil;
import org.eclipse.tigerstripe.core.plugin.ossjxml.XmlSchemaImportsHelper.NamespaceRef;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OssjXMLSchemaPlugin extends BasePlugin {

	// These are the valid properties for a XML plugin reference

	public final static String[] PROPERTIES = {};

	private final static String LABEL = "JMS/XML Integration Profile";

	private final static String DESCRIPTION = "Generates the JMS/XML integration profile.";

	public final static String PLUGIN_ID = "ossj-xml-spec";

	private final static String GROUP_ID = PluginRefFactory.GROUPID_TS;

	private final static String VERSION = PluginRefFactory.VERSION_1_3;

	private final static String REPORTTEMPLATE = "OSSJ_XML_REPORT.vm";

	private OssjXMLSchemaPluginReport report;

	public OssjXMLSchemaPluginReport getReport() {
		return this.report;
	}

	private final static String[] supportedNatures = { PluginBody.OSSJ_NATURE };

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

	public void trigger(PluginRef pluginRef, RunConfig config)
			throws TigerstripeException {
		// try {
		ITigerstripeProject handle = pluginRef.getProjectHandle();
		// (ITigerstripeProject) API
		// .getDefaultProjectSession().makeTigerstripeProject(
		// pluginRef.getProject().getBaseDir().toURI(), null);
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
				.getArtifactManagerSession();
		ArtifactManager artifactMgr = session.getArtifactManager();
		this.report = new OssjXMLSchemaPluginReport(pluginRef);
		this.report.setTemplate(PackageBasedOssjXmlSchemaModel.TEMPLATE_PREFIX
				+ "/" + pluginRef.getActiveVersion() + "/" + REPORTTEMPLATE);

		XmlPluginRef xmlRef = (XmlPluginRef) pluginRef;
		PackageToSchemaMapper mapper = xmlRef.getMapper();

		if (mapper.useDefaultMapping()) {
			PckXSDMapping mapping = mapper.getDefaultMapping();
			PackageBasedOssjXmlSchemaModel model = new PackageBasedOssjXmlSchemaModel(
					mapping, pluginRef);
			generateWithTemplate(model, pluginRef, artifactMgr, config);
		} else {
			PckXSDMapping[] mappings = mapper.getMappings();
			for (int i = 0; i < mappings.length; i++) {
				PackageBasedOssjXmlSchemaModel model = new PackageBasedOssjXmlSchemaModel(
						mappings[i], pluginRef);
				generateWithTemplate(model, pluginRef, artifactMgr, config);
			}
		}
		this.report.setMapper(mapper);
		// } catch (TigerstripeLicenseException e) {
		// throw new TigerstripeException("License Error: " + e.getMessage());
		// }
	}

	private void generateWithTemplate(PackageBasedOssjXmlSchemaModel model,
			PluginRef pluginRef, ArtifactManager artifactMgr, RunConfig config)
			throws TigerstripeException {

		XmlPluginRef xmlRef = (XmlPluginRef) pluginRef;

		ArtifactFilter filter = null;
		if (xmlRef.getMapper().useDefaultMapping()) {
			filter = new ArtifactNoFilter();
		} else {
			filter = new PackageBasedArtifactFilter(model.getMapping()
					.getPackage(), xmlRef.getMapper().getAllMappedPackages());
		}

		// Let's put what we'll need in the context and get going
		Collection entities = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(ManagedEntityArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor()), filter);
		model.addContent(entities);

		Collection datatypes = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(DatatypeArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor()), filter);
		model.addContent(datatypes);

		Collection events = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(EventArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor()), filter);
		model.addContent(events);

		Collection enums = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(EnumArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor()), filter);
		model.addContent(enums);

		Collection exceptions = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(ExceptionArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor()), filter);
		model.addContent(exceptions);

		Collection queries = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(QueryArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor()), filter);
		model.addContent(queries);

		Collection all_sessions = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(SessionFacadeArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor()), filter);
		model.addContent(all_sessions);

		Collection updateProcedures = ArtifactFilter.filter(artifactMgr
				.getArtifactsByModel(UpdateProcedureArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor()), filter);
		model.addContent(updateProcedures);

		XmlSchemaImportsHelper importsHelper = new XmlSchemaImportsHelper(
				xmlRef.getMapper());
		importsHelper.buildImportList(artifactMgr, model.getContent());

		SchemaUtils sUtils = new SchemaUtils(pluginRef, artifactMgr,
				importsHelper);

		VelocityContext localContext = new VelocityContext(getDefaultContext());
		localContext.put("entities", entities);
		localContext.put("datatypes", datatypes);
		localContext.put("events", events);
		localContext.put("enumerations", enums);
		localContext.put("exceptions", exceptions);
		localContext.put("queries", queries);

		localContext.put("updateProcedures", updateProcedures);
		localContext.put("ossjUtil", new OssjUtil(artifactMgr, pluginRef));
		localContext
				.put("ossjXmlUtil", new OssjXmlUtil(artifactMgr, pluginRef));
		localContext.put("model", model);
		localContext.put("pluginRef", pluginRef);
		localContext.put("runtime", TigerstripeRuntime.getInstance());
		localContext.put("tsProject", pluginRef.getProject());
		localContext.put("schemaUtils", sUtils);
		localContext.put("exp", new Expander(pluginRef));
		localContext.put("manager", artifactMgr);
		localContext.put("targetNamespace", model.getTargetNamespace());
		try {
			setClasspathLoaderForVelocity();
			Expander exp = new Expander(pluginRef);

			String modelName = model.getName();
			if (!modelName.endsWith(".xsd")) {
				modelName = modelName + ".xsd";
			}

			if (all_sessions.size() > 1) {
				// Need to make one "common" XSD, plus one per session.
				// Common one first - use name from mapping
				Template template = Velocity.getTemplate(model
						.getCommonTemplate());
				localContext.put("targetNamespace", model.getTargetNamespace());

				// from here same as the general case
				String filename = model.getDestinationDir() + "/" + modelName;
				setDefaultDestination(pluginRef, new File(exp.expandVar(
						filename, pluginRef.getProject())), config);
				template.merge(localContext, getDefaultWriter());
				getDefaultWriter().close();
				Collection<String> files = this.report.getGeneratedFiles();
				files.add(exp.expandVar(modelName, pluginRef.getProject()));

				// Now do it for each session.
				template = Velocity.getTemplate(model.getOperationTemplate());

				for (int i = 0; i < all_sessions.size(); i++) {
					String sessName = ((SessionFacadeArtifact) all_sessions
							.toArray()[i]).getName();
					// Just make sure there's only one session in here
					Collection sessions = new ArrayList();
					sessions.add(all_sessions.toArray()[i]);
					localContext.put("sessions", sessions);

					// Add the new Namespace details to the importsList
					NamespaceRef newRef = importsHelper.new NamespaceRef();
					newRef.targetNamespace = exp.expandVar(sUtils
							.insertSessionName(model.getTargetNamespace(),
									sessName));
					newRef.targetLocation = exp.expandVar(sUtils
							.insertSessionName(model.getSchemaLocation(),
									sessName));
					newRef.prefix = exp.expandVar(sUtils.insertSessionName(
							model.getMapping().getUserPrefix(), sessName));
					sUtils.getNRefs().add(newRef);

					// And set the model.TargetNamespace

					localContext.put("targetNamespace", newRef.targetNamespace);

					// Set new filename & targetNamespace

					String newFilename = sUtils.insertSessionName(filename,
							sessName);
					// TigerstripeRuntime.logInfoMessage(newFilename);
					// TigerstripeRuntime.logInfoMessage(newRef.targetNamespace);
					// TigerstripeRuntime.logInfoMessage(newRef.targetLocation);
					// TigerstripeRuntime.logInfoMessage(newRef.prefix);

					setDefaultDestination(pluginRef, new File(exp.expandVar(
							newFilename, pluginRef.getProject())), config);
					template.merge(localContext, getDefaultWriter());
					getDefaultWriter().close();

					files.add(exp.expandVar(sUtils.insertSessionName(modelName,
							sessName), pluginRef.getProject()));

					sUtils.getNRefs().remove(newRef);

				}

			} else {
				// The general model of all in the one XSD file
				Template template = Velocity.getTemplate(model.getTemplate());
				localContext.put("targetNamespace", model.getTargetNamespace());
				localContext.put("sessions", all_sessions);

				String filename = model.getDestinationDir() + "/" + modelName;

				setDefaultDestination(pluginRef, new File(exp.expandVar(
						filename, pluginRef.getProject())), config);

				// create the output
				template.merge(localContext, getDefaultWriter());

				getDefaultWriter().close();
				Collection<String> files = this.report.getGeneratedFiles();

				files.add(exp.expandVar(modelName, pluginRef.getProject()));
			}
		} catch (MethodInvocationException e) {
			Throwable th = e.getWrappedThrowable();
			String errorMsg = null;
			if (th instanceof TigerstripeException) {
				TigerstripeException ee = (TigerstripeException) th;
				errorMsg = ee.getLocalizedMessage() + " while calling "
						+ e.getMethodName() + " within '" + model.getTemplate()
						+ "' template." + "(in " + model.getName() + ").";
			} else
				errorMsg = "Unknown Error in " + model.getName()
						+ "(while calling " + e.getMethodName() + " within '"
						+ model.getTemplate() + "' template).";
			throw new TigerstripeException(errorMsg, e);
		} catch (Exception e) {
			throw new TigerstripeException("Unexpected error while merging '"
					+ model.getTemplate() + "' template for " + model.getName()
					+ ".", e);
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

	public static PluginRef getXmlSchemaPluginRef(TigerstripeProject project) {
		Collection pluginRefs = project.getPluginReferences();
		for (Iterator iter = pluginRefs.iterator(); iter.hasNext();) {
			PluginRef ref = (PluginRef) iter.next();
			if (PLUGIN_ID.equals(ref.getPluginId())
					&& GROUP_ID.equals(ref.getGroupId()))
				return ref;
		}
		return null;
	}

	public int getCategory() {
		return IPluginReference.GENERATE_CATEGORY;
	}

}