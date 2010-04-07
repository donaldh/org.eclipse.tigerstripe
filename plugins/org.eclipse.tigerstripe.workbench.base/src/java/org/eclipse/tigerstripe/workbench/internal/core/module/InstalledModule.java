package org.eclipse.tigerstripe.workbench.internal.core.module;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.InstalledModuleProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;

/**
 * This class represent installed module
 */
public class InstalledModule {

	private ModuleRef module;

	public InstalledModule(String location) throws TigerstripeException {
		String ref = "reference:";
		if (location.startsWith(ref)) {
			location = location.substring(ref.length());
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

	public ITigerstripeModuleProject makeModuleProject()
			throws TigerstripeException {
		ITigerstripeModuleProject result = new InstalledModuleProjectHandle(
				this);
		return result;
	}
}
