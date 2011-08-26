package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;

public class TSExplorerAccess implements IExecutableExtensionFactory,
		IExecutableExtension {
	private String type;

	public Object create() throws CoreException {
		if ("labelProvider".equals(type)) {
			return TSExplorer.getInstance().getLabelProvider();
		} else if ("contentProvider".equals(type)) {
			return TSExplorer.getInstance().getContentProvider();
		} else {
			return null;
		}
	}

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {

		if (data instanceof String) {
			type = (String) data;
		}
	}

}
