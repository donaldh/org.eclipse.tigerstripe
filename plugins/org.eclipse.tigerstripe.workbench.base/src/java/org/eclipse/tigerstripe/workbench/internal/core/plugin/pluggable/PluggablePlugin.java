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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.PluggablePluginProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateRunRule;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog.LogLevel;

/**
 * Housing for Pluggable plugin
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PluggablePlugin extends BasePlugin {

	private ClassLoader classLoader;

	private String path;

	private String zippedFile;

	private PluggablePluginProject ppProject = null;

	private String[] definedProperties = null;

	private final static String REPORTTEMPLATE = "PLUGGABLE_REPORT.vm";

	public final static String TEMPLATE_PREFIX = "org/eclipse/tigerstripe/workbench/internal/core/plugin/pluggable/resources";

	private PluggablePluginReport report;

	/**
	 * 
	 * @param path -
	 *            The path to the unzipped plugin directory
	 */
	public PluggablePlugin(String path, String zippedFile) {
		this.path = path;
		this.zippedFile = zippedFile;
		loadProject();
	}

	/**
	 * Deletes all traces of this plugin on disk so it won't be reloaded
	 * 
	 */
	public void dispose() {
		File unzippedFile = new File(path);
		if (unzippedFile.exists()) {
			boolean result = Util.deleteDir(unzippedFile);
		}

		File zfile = new File(zippedFile);
		if (zfile.exists()) {
			boolean result = zfile.delete();
		}
	}

	public void trigger(PluginConfig pluginConfig, RunConfig config)
			throws TigerstripeException {
		this.report = new PluggablePluginReport(pluginConfig);
		this.report.setTemplate(TEMPLATE_PREFIX + "/" + REPORTTEMPLATE);

		// Update the ref with any missing properties, and
		// remove any that are not valid.

		Properties properties = pluginConfig.getProperties();
		String[] definedProps = pluginConfig.getDefinedProperties();
		Properties usableProps = new Properties();

		for (int i = 0; i < definedProps.length; i++) {
			if (properties.getProperty(definedProps[i]) == null
					|| properties.getProperty(definedProps[i]).length() == 0) {
				for (IPluginProperty property : ppProject
						.getGlobalProperties()) {
					if (property.getName().equals(definedProps[i])) {
						usableProps.setProperty(definedProps[i], property
								.getDefaultValue().toString());
					}
				}
			} else {
				usableProps.setProperty(definedProps[i], properties
						.getProperty(definedProps[i]));
			}
		}
		pluginConfig.setProperties(usableProps);

		PluginRuleExecutor executor = new PluginRuleExecutor(this,
				(PluggablePluginConfig) pluginConfig, config);
		executor.trigger();

		ArrayList<RuleReport> ruleReports = executor.getReports();

		this.report.getChildReports().addAll(ruleReports);
		/*
		 * for (int f=0;f<this.report.getChildReports().size();f++){ RuleReport
		 * rr = ruleReports.get(f);
		 * TigerstripeRuntime.logInfoMessage(rr.getClass()); if (rr != null){
		 * TigerstripeRuntime.logInfoMessage("Template = "+rr.getTemplate()); } }
		 */

	}

	public String getLabel() {
		return getPluginId();
	}

	public String getDescription() {
		return ppProject.getProjectDetails().getDescription();
	}

	public PluggablePluginProject getPProject() {
		return ppProject;
	}

	public String getPluginId() {
		return ppProject.getProjectDetails().getName() + "("
				+ ppProject.getProjectDetails().getVersion() + ")";
	}

	public String getGroupId() {
		return ppProject.getProjectDetails().getProvider();
	}

	public String getVersion() {
		return ppProject.getProjectDetails().getVersion();
	}

	public PluginReport getReport() {
		return this.report;
	}

	public String[] getSupportedNatures() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getDefinedProperties() {
		if (definedProperties == null) {
			ArrayList<String> result = new ArrayList<String>();
			IPluginProperty[] props = ppProject.getGlobalProperties();
			for (IPluginProperty prop : props) {
				result.add(prop.getName());
			}
			definedProperties = result.toArray(new String[result.size()]);
		}

		return definedProperties;
	}

	public int getCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isValid() {
		if (ppProject == null) {
			loadProject();
		}
		return ppProject != null && ppProject.isValid();
	}

	protected void loadProject() {
		ppProject = new PluggablePluginProject(new File(path));
		try {
			ppProject.reload(true);
			if (!ppProject.isValid()) {
				ppProject = null;
			}
		} catch (TigerstripeException e) {
			ppProject = null;
		}
	}

	/**
	 * Builds the velocity context as defined in the project descriptor by
	 * creating a classloader that will include the .class files that have been
	 * packaged up.
	 * 
	 * @param parentContext
	 * @return
	 * @throws TigerstripeException
	 */
	public VelocityContext getLocalVelocityContext(
			VelocityContext parentContext, ITemplateRunRule rule)
			throws TigerstripeException {
		VelocityContext result = new VelocityContext(parentContext);

		VelocityContextDefinition[] contextDefs = rule
				.getVelocityContextDefinitions();
		for (VelocityContextDefinition def : contextDefs) {
			Object entryObj = getInstance(def.getClassname());
			result.put(def.getName(), entryObj);
		}
		return result;
	}

	/**
	 * Gets the corresponding class from the Plugin Classloader
	 * 
	 * @param classname
	 * @return
	 * @throws TigerstripeException
	 */
	protected Class getClass(String classname) throws TigerstripeException {
		try {
			Class cl = getClassloader().loadClass(classname);
			return cl;
		} catch (ClassNotFoundException e) {
			throw new TigerstripeException("Couldn't find class '" + classname
					+ "' in plugin classloader ('" + getLabel() + "')");
		}
	}

	public Object getInstance(String classname) throws TigerstripeException {
		try {
			Class clazz = getClass(classname);
			Object result = clazz.newInstance();
			return result;
		} catch (InstantiationException e) {
			throw new TigerstripeException("Couldn't instantiate class '"
					+ classname + "' within context of plugin ('" + getLabel()
					+ "'): " + e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new TigerstripeException("Couldn't access class '"
					+ classname + "' within context of plugin ('" + getLabel()
					+ "'): " + e.getMessage(), e);
		}
	}

	protected ClassLoader getClassloader() throws TigerstripeException {
		try {
			if (classLoader == null) {
				List<URL> urls = new ArrayList<URL>();
				// Create a classloader that includes the additional classpath
				File classesDir = new File(getPProject().getBaseDir(),
						"classes");
				urls.add(classesDir.toURL());

				// with all packaged up entries
				for (IPluginClasspathEntry entry : getPProject()
						.getClasspathEntries()) {
					File jarFile = new File(getPProject().getBaseDir(), entry
							.getRelativePath());
					urls.add(jarFile.toURL());
				}

				classLoader = new URLClassLoader(urls.toArray(new URL[urls
						.size()]), this.getClass().getClassLoader());
			}

			// TigerstripeRuntime.logInfoMessage(" Returning classloader= " +
			// classLoader);
			return classLoader;
		} catch (MalformedURLException e) {
			throw new TigerstripeException(
					"Unable to create plugin classloader for '" + getLabel()
							+ "':" + e.getMessage(), e);
		}
	}

	@Override
	public LogLevel getDefaultLogLevel() {
		return ppProject.getDefaultLogLevel();
	}

	@Override
	public String getLogPath() {
		return ppProject.getLogPath();
	}

	@Override
	public boolean isLogEnabled() {
		return ppProject.isLogEnabled();
	}

}
