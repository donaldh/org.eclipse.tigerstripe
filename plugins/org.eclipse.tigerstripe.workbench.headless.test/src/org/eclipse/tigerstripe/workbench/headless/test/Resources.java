package org.eclipse.tigerstripe.workbench.headless.test;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;

public class Resources {

	public static File get(String path) throws IOException {

		File bundleRoot = new File(FileLocator.getBundleFile(
				Activator.getDefault().getBundle()).getAbsolutePath());

		return new File(bundleRoot, "resources/" + path);
	}
	
}
