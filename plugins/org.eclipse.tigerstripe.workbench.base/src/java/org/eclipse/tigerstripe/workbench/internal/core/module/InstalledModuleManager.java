package org.eclipse.tigerstripe.workbench.internal.core.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.osgi.framework.Bundle;

/**
 * This class used to manage installed modules
 */
public class InstalledModuleManager {

	public static final String MODULE_EXTENSION_POINT = "org.eclipse.tigerstripe.workbench.base.module";

	private static InstalledModuleManager instance;

	private InstalledModuleManager() {
	}

	public static InstalledModuleManager getInstance() {
		if (instance == null) {
			instance = new InstalledModuleManager();
		}
		return instance;
	}

	/**
	 * @param id module identifier
	 * @return installed module by ID or null if no such module
	 */
	public InstalledModule getModule(String id) {
		if (idToModule == null) {
			init();
		}
		return idToModule.get(id);
	}

	/**
	 * @return all installed modules
	 */
	public InstalledModule[] getModules() {
		if (modules == null) {
			init();
		}
		return modules.toArray(new InstalledModule[modules.size()]);
	}

	private void init() {
		modules = new ArrayList<InstalledModule>();
		idToModule = new HashMap<String, InstalledModule>();
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(MODULE_EXTENSION_POINT);
		for (IConfigurationElement element : elements) {
			String id = element.getDeclaringExtension()
					.getNamespaceIdentifier();
			Bundle bundle = Platform.getBundle(id);
			if (bundle != null) {
				try {
					String location = bundle.getLocation();
					InstalledModule module = new InstalledModule(location);
					modules.add(module);
					idToModule.put(module.getModuleID(), module);
				} catch (Exception e) {
					BasePlugin.log(e);
				}
			}
		}
	}

	private Map<String, InstalledModule> idToModule;
	private List<InstalledModule> modules;

}
