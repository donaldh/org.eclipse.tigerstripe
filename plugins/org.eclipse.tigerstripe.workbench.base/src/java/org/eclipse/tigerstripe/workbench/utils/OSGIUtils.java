package org.eclipse.tigerstripe.workbench.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.osgi.framework.Bundle;

public class OSGIUtils {

	public static Set<String> getAllRequiredBundles(String bundleId) {
		HashSet<String> requiredBundles = new HashSet<String>();
		State state = Platform.getPlatformAdmin().getState();
		collectRequiredBundles(state, bundleId, requiredBundles);
		return requiredBundles;
	}

	private static void collectRequiredBundles(State state, String bundleId,
			Set<String> pathes) {
		if (!pathes.add(bundleId))
			return;
		BundleDescription[] bundles = state.getBundles(bundleId);
		if (bundles == null || bundles.length == 0)
			return;
		BundleDescription description = bundles[0];
		BundleSpecification[] requiredBundles = description
				.getRequiredBundles();
		for (BundleSpecification b : requiredBundles) {
			collectRequiredBundles(state, b.getName(), pathes);
		}
	}

	public static IPath getBundleJarPath(Bundle bundle) {
		try {
			File bFile = FileLocator.getBundleFile(bundle);
			if (bFile.getName().endsWith(".jar")) {
				return (new Path(bFile.getAbsolutePath())).makeAbsolute();
			} else {
				File result;
				File binFile = new File(bFile, "bin");
				if (binFile.exists()) {
					result = binFile;
				} else {
					result = bFile;
				}
				return new Path(result.getAbsolutePath()).makeAbsolute();

			}
		} catch (IOException e) {
			BasePlugin.log(e);
		}
		return new Path("unknown_location_for_" + bundle.getSymbolicName());
	}
}