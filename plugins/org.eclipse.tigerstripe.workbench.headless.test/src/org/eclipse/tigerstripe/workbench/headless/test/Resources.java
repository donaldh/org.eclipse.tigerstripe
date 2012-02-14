package org.eclipse.tigerstripe.workbench.headless.test;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Plugin;

public class Resources {

	private final Plugin context;

	public Resources(Plugin context) {
		this.context = context;
	}
	
	public File get(String path) throws IOException {

		File bundleRoot = new File(FileLocator.getBundleFile(
				context.getBundle()).getAbsolutePath());

		return new File(bundleRoot, "resources/" + path);
	}
	
}
