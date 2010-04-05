package org.eclipse.tigerstripe.workbench.internal.core.module;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

/**
 * This class represent installed module
 */
public class InstalledModule {

	private ModuleRef module;

	public InstalledModule(String location) throws TigerstripeException {
		String ref = "reference:", file = "file:";
		if (location.startsWith(ref)) {
			location = location.substring(ref.length());
		}
		if (location.startsWith(file)) {
			location = location.substring(file.length());
		}
		URI uri;
		try {
			uri = new URI(location);
		} catch (URISyntaxException e) {
			throw new InvalidModuleException("Invalid model uri: " + location,
					e);
		}

		ModuleRefFactory factory = ModuleRefFactory.getInstance();
		module = factory.parseModule(uri, new NullProgressMonitor());
	}

	/**
	 * @return module reference
	 */
	public ModuleRef getModule() {
		return module;
	}

	/**
	 * @return absolute path to this module
	 */
	public IPath getPath() {
		URI uri = module.getJarURI();
		return new Path(uri.getPath());
	}

	/**
	 * @return module ID
	 */
	public String getModuleID() {
		return getModule().getModuleHeader().getModuleID();
	}

}
