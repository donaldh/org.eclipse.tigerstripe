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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.M0ProjectDescriptor;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.PluggablePluginProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog.LogLevel;

/**
 * Housing for Pluggable plugin
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PluggablePlugin extends BasePlugin {

	private static final String DEFAULT_LOG_DIR = "logs";

	private boolean canDelete = true;
	
	protected URLClassLoader classLoader;

	/**
	 * This is the path to a location where stuff is unzipped
	 */
	private String path;

	/**
	 * This is only passed around so that we can delete it.
	 */
	private String zippedFile;

	protected GeneratorProjectDescriptor descriptor = null;

	private String[] definedProperties = null;

	private final static String REPORTTEMPLATE = "PLUGGABLE_REPORT.vm";

	public final static String TEMPLATE_PREFIX = "org/eclipse/tigerstripe/workbench/internal/core/plugin/pluggable/resources";

	private PluggablePluginReport report;

	/**
	 * 
	 * @param path
	 *            - The path to the unzipped plugin directory
	 */
	public PluggablePlugin(String path, String zippedFile) {
		this.path = path;
		this.zippedFile = zippedFile;
		loadProject();
	}
	
	public EPluggablePluginNature getPluginNature() {
		return descriptor.getPluginNature();
	}

	/**
	 * Deletes all traces of this plugin on disk so it won't be reloaded
	 * 
	 */
	public void dispose() {
		File unzippedFile = new File(path);
		if (unzippedFile.exists()) {
			Util.deleteDir(unzippedFile);
		}
		File zfile = new File(zippedFile);
		if (zfile.exists()) {
			zfile.delete();
		}
		GeneratorProjectDescriptor pProject = getPProject();
		if (pProject != null) {
			pProject.dispose();
		}
	}

	private void logClassPath(ClassLoader loader) {
		// Get the URLs
		if (loader instanceof URLClassLoader) {
			URL[] urls = ((URLClassLoader) loader).getURLs();

			for (int i = 0; i < urls.length; i++) {
				PluginLog.logDebug("Classpath entry : " + urls[i].getFile());
			}
			if (loader.getParent() != null) {
				logClassPath(loader.getParent());
			}
		}
	}

	public void trigger(PluginConfig pluginConfig, RunConfig config)
			throws TigerstripeException {
		this.report = new PluggablePluginReport(pluginConfig);
		this.report.setTemplate(TEMPLATE_PREFIX + "/" + REPORTTEMPLATE);

		// Update the pluginConfig with any missing properties, and
		// remove any that are not valid.

		if (isLogEnabled()) {
			// Add the classpath entries to the plugin log
			logClassPath(getClassloader());
		}

		Properties properties = pluginConfig.getProperties();
		String[] definedProps = pluginConfig.getDefinedProperties();
		Properties usableProps = new Properties();

		for (int i = 0; i < definedProps.length; i++) {
			if (properties.getProperty(definedProps[i]) == null
					|| properties.getProperty(definedProps[i]).length() == 0) {
				for (IPluginProperty property : descriptor
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
		 * TigerstripeRuntime.logInfoMessage("Template = "+rr.getTemplate()); }
		 * }
		 */

	}

	public String getLabel() {
		return getPluginId();
	}

	public String getDescription() {
		return descriptor.getProjectDetails().getDescription();
	}

	public GeneratorProjectDescriptor getPProject() {
		return descriptor;
	}

	public String getPluginId() {
		return descriptor.getId();
	}

	public String getPluginName() {
		return descriptor.getName();
	}

	public String getGroupId() {
		return descriptor.getProjectDetails().getProvider();
	}

	public String getVersion() {
		return descriptor.getProjectDetails().getVersion();
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
			IPluginProperty[] props = descriptor.getGlobalProperties();
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
		if (descriptor == null) {
			loadProject();
		}
		return descriptor != null && descriptor.isValid();
	}

	protected void loadProject() {
		descriptor = new PluggablePluginProject(new File(path));
		if (!descriptor.getFullPath().exists()) {
			descriptor = new M0ProjectDescriptor(new File(path));
		}
		try {
			descriptor.reload(true);
			if (!descriptor.isValid()) {
				descriptor = null;
			}
		} catch (TigerstripeException e) {
			descriptor = null;
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
			VelocityContext parentContext, ITemplateBasedRule rule)
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
	@SuppressWarnings("unchecked")
	protected Class getClass(String classname) throws TigerstripeException {
		try {
			Class cl = getClassloader().loadClass(classname);
			return cl;
		} catch (ClassNotFoundException e) {
			throw new TigerstripeException("Couldn't find class '" + classname
					+ "' in plugin classloader ('" + getLabel() + "')");
		}
	}

	@SuppressWarnings("unchecked")
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
				System.out.println(classesDir.toURL().toString());

				// with all packaged up entries
				for (IPluginClasspathEntry entry : getPProject()
						.getClasspathEntries()) {
					File jarFile = new File(getPProject().getBaseDir(), entry
							.getRelativePath());
					urls.add(jarFile.toURL());
					System.out.println(jarFile.toURL().toString());
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
		return descriptor.getDefaultLogLevel();
	}

	@Override
	public String getLogPath() {
		IPath path = new Path(DEFAULT_LOG_DIR);
		return path.append(descriptor.getLogPath()).toOSString();
	}

	@Override
	public boolean isLogEnabled() {
		return descriptor.isLogEnabled();
	}

	public String getPluginPath() {
		return path;
	}

	public void setCanDelete(boolean canDelete){
		this.canDelete = canDelete;
	}
	
	public boolean getCanDelete() {
		return canDelete;
	}

}
