package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;

public class LifecycleHandlerProvider {

	public static Collection<Lifecycle> getHandlers() {

		final IConfigurationElement[] elements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"org.eclipse.tigerstripe.workbench.ui.base.lifecycle");

		final List<Lifecycle> result = new ArrayList<Lifecycle>();

		SafeRunner.run(new SafeRunnable() {
			public void run() throws Exception {
				for (IConfigurationElement e : elements) {
					result.add((Lifecycle) e.createExecutableExtension("class"));
				}
			}
		});

		return result;
	}
}
