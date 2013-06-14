package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.internal.core.generation.M0RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;

public class M0GlobalRunnableRule extends GlobalRunnableRule {

	public final static String LABEL = "M0 Global Runnable Rule";
	
	protected Map<String, Object> addContextEntries(Map<String, Object> context, IPluginRuleExecutor exec ){
		M0RunConfig config = (M0RunConfig) exec.getConfig();

		context.put("artifactInstances", config.getInstanceMap());

		// FIXME: this is a work around until we have a proper Metamodel defined
		// for instances
		List<Object> classInstances = new ArrayList<Object>();
		List<Object> associationInstances = new ArrayList<Object>();

		@SuppressWarnings("unchecked")
		List<Object> instances = (List<Object>) config.getInstanceMap();
		for (Object instance : instances) {
			if (instance != null) {
				if (instance.getClass().getName().contains("ClassInstance")) {
					classInstances.add(instance);
				} else {
					associationInstances.add(instance);
				}
			}
		}
		context.put("classInstances", classInstances);
		context.put("associationInstances", associationInstances);
		
		return context;
	}

	
}
