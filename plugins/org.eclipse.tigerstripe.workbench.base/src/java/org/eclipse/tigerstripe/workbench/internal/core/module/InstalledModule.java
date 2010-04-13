package org.eclipse.tigerstripe.workbench.internal.core.module;

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.InstalledModuleProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.osgi.framework.Bundle;

/**
 * This class represent installed module
 */
public class InstalledModule {

	private ModuleRef module;

	public InstalledModule(Bundle bundle) throws TigerstripeException {
		URI uri = null;
		try {
			File file = FileLocator.getBundleFile(bundle);
			uri = file.toURI();
		} catch (Exception e) {
			throw new InvalidModuleException("Invalid model uri: "
					+ bundle.getLocation(), e);
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

	public ArtifactManager getArtifactManager() {
		return module.getArtifactManager();
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
