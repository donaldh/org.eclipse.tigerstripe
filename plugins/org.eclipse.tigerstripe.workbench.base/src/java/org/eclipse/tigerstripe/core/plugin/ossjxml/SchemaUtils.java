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

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.core.plugin.Expander;
import org.eclipse.tigerstripe.core.plugin.PackageToSchemaMapper;
import org.eclipse.tigerstripe.core.plugin.PluginRef;
import org.eclipse.tigerstripe.core.plugin.XmlPluginRef;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class SchemaUtils {

	private PluginRef pluginRef;

	private ArtifactManager mgr;

	private XmlSchemaImportsHelper importsHelper;

	public SchemaUtils(PluginRef pluginRef, ArtifactManager mgr,
			XmlSchemaImportsHelper importsHelper) {
		this.pluginRef = pluginRef;
		this.mgr = mgr;
		this.importsHelper = importsHelper;
	}

	public String getPrefixForArtifact(String fqn) {
		String targetNamespace = targetNamespaceForArtifact(fqn);
		if (targetNamespace != null && !"".equals(targetNamespace))
			return importsHelper.getPrefixForNamespace(targetNamespace);
		return importsHelper.getDefaultPrefix();
	}

	public String getDefaultPrefix() {
		return importsHelper.getDefaultPrefix();
	}

	public Collection getNRefs() {
		return importsHelper.getImportsList();
	}

	public String targetNamespaceForArtifact(String fqn) {
		AbstractArtifact artifact = mgr.getArtifactByFullyQualifiedName(fqn,
				false, new TigerstripeNullProgressMonitor());
		Expander exp = new Expander(this.pluginRef);
		if (artifact != null) {
			// It is a local artifact
			PackageToSchemaMapper mapper = ((XmlPluginRef) pluginRef)
					.getMapper();

			return exp.expandVar(mapper.getPckXSDMapping(artifact.getPackage())
					.getTargetNamespace(), this.pluginRef.getProject());
		} else {
			// Let's see if we can find it in a dependency
			artifact = mgr.getArtifactByFullyQualifiedName(fqn, true,
					new TigerstripeNullProgressMonitor());
			if (artifact == null)
				// This is simply an unknown artifact
				return "";
			else {
				// At this point, the artifact is either coming from a
				// referenced project or from a module in the path.
				// In bot cases we need to find the parent project
				TigerstripeProject parentProject = null;
				if (artifact.getArtifactManager() instanceof ModuleArtifactManager) {
					ModuleArtifactManager parentMgr = (ModuleArtifactManager) artifact
							.getArtifactManager();
					parentProject = parentMgr.getEmbeddedProject();
					// TigerstripeRuntime.logInfoMessage(" Found fqn=" + fqn + "
					// in module" +
					// parentProject.getProjectDetails().getName());
				} else {
					try {
						parentProject = ((TigerstripeProjectHandle) artifact
								.getIProject()).getTSProject();
						// TigerstripeRuntime.logInfoMessage(" Found fqn=" + fqn
						// + " in project " + parentProject.getProjectLabel());
					} catch (TigerstripeException e) {
						// shouldn't happen here
						TigerstripeRuntime.logErrorMessage(
								"TigerstripeException detected", e);
					}
				}
				PluginRef xmlPluginRef = findXmlPluginRef(parentProject);
				if (xmlPluginRef != null) {
					PackageToSchemaMapper mapper = ((XmlPluginRef) xmlPluginRef)
							.getMapper();
					// TigerstripeRuntime.logInfoMessage(" Returing : " +
					// mapper.getPckXSDMapping(artifact.getPackage())
					// .getTargetNamespace());
					return exp.expandVar(mapper.getPckXSDMapping(
							artifact.getPackage()).getTargetNamespace(),
							parentProject);
				}
			}
		}
		return "";
	}

	/**
	 * Look for the OSSJ pattern.
	 * 
	 * @param start
	 * @param sessName
	 * @return
	 */
	public String insertSessionName(String start, String sessName) {
		// TigerstripeRuntime.logInfoMessage(start+ " : " + sessName);
		Pattern name = Pattern.compile("\\$\\{name\\}");
		Matcher nameMatcher = name.matcher(start);
		if (nameMatcher.find(0)) {
			String ret = nameMatcher.replaceAll("\\$\\{name\\}-" + sessName);
			return ret;
		}
		if (start.contains("/")) {
			String path = start.substring(0, start.lastIndexOf("/") + 1);
			String scName = start.substring(start.lastIndexOf("/") + 1);

			return path + sessName + scName;

		} else
			return sessName + start;
	}

	private static PluginRef findXmlPluginRef(TigerstripeProject parentProject) {
		return OssjXMLSchemaPlugin.getXmlSchemaPluginRef(parentProject);
	}
}
