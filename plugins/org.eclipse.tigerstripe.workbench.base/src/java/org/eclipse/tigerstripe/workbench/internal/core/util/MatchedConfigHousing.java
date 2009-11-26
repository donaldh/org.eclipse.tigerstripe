package org.eclipse.tigerstripe.workbench.internal.core.util;

import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;

public class MatchedConfigHousing {

	private IPluginConfig config;
	
	private PluggableHousing housing;
	
	public MatchedConfigHousing(IPluginConfig config, PluggableHousing housing) {
		super();
		this.config = config;
		this.housing = housing;
	}
	
	
	public IPluginConfig getConfig() {
		return config;
	}

	public PluggableHousing getHousing() {
		return housing;
	}

	
}
