package org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.osgi.framework.Bundle;

public class ContributedPlugin extends PluggablePlugin {

	private String bundleName;
	private Bundle bundle;
	
	/**
	 * This class is within a jar
	 * @param path
	 * @param zippedFile
	 */
	public ContributedPlugin(String path, Bundle bundle) {
		super(path, null);
		this.bundle = bundle;
		this.bundleName = (String) bundle.getHeaders().get("Bundle-Name");
	}
	
	public String getPluginName() {
		return bundleName;
	}

	@Override
	public String getPluginId() {
		return bundleName + "("
			+descriptor.getVersion() + ")";
	}

	protected ClassLoader getClassloader() throws TigerstripeException {

			if (classLoader == null) {
				List<URL> urls = new ArrayList<URL>();
				// Create a classloader that includes the additional classpath
				
				
				// In this case that is all at the top level of the bundle.
				URL classesUrl = bundle.getEntry(bundleName+".jar"); //$NON_NLS-1$
				if (classesUrl != null){
					urls.add(classesUrl);
				}

				// This is really only required during debug session!
				URL binUrl = bundle.getEntry("bin"); //$NON_NLS-1$
				if (binUrl != null){
					urls.add(binUrl);
				}
				
				// with all packaged up entries
				for (IPluginClasspathEntry entry : getPProject()
						.getClasspathEntries()) {
					URL jarUrl = bundle.getEntry(entry
							.getRelativePath());
					if (jarUrl != null){
						urls.add(jarUrl);
					}
				}
				
				classLoader = new URLClassLoader(urls.toArray(new URL[urls
						.size()]), this.getClass().getClassLoader());
			}

			
			return classLoader;
	}
	
}
