package org.eclipse.tigerstripe.workbench.internal.startup;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.annotation.AnnotationUtils;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProjectMgr;
import org.osgi.framework.Bundle;

public class TigerstripeClassPathInitializer extends ClasspathVariableInitializer {

	public TigerstripeClassPathInitializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(String variable) {
		if (variable.equals(ITigerstripeConstants.PHANTOMLIB)){
			String pathStr = PhantomTigerstripeProjectMgr.getInstance()
			.getPhantomURI().getPath()
			+ ITigerstripeConstants.PHANTOMLIB_DEF;
			Path phantomPath = new Path(pathStr);
			try {
				JavaCore.setClasspathVariable(ITigerstripeConstants.PHANTOMLIB,
						phantomPath, null);
				if (!phantomPath.toFile().exists()) {
					IStatus status = new Status(
							IStatus.ERROR,
							BasePlugin.getPluginId(),
							222,
							ITigerstripeConstants.PHANTOMLIB + " couldn't be resolved.",
							null);
					BasePlugin.log(status);
				}
			} catch (Exception e) {
				IStatus status = new Status(
						IStatus.ERROR,
						BasePlugin.getPluginId(),
						222,
						"Error setting up "+ITigerstripeConstants.PHANTOMLIB + " classPathVariable.",
						e);
				BasePlugin.log(status);
			}

		} else if (variable.equals(ITigerstripeConstants.EQUINOX_COMMON)){
			IPath equinoxPath = findEquinoxCommonJarPath();
			try {
				JavaCore.setClasspathVariable(ITigerstripeConstants.EQUINOX_COMMON,
						equinoxPath, null);
			} catch (JavaModelException e) {
				IStatus status = new Status(
						IStatus.ERROR,
						BasePlugin.getPluginId(),
						222,
						"Error setting up "+ITigerstripeConstants.EQUINOX_COMMON + " classPathVariable.",
						e);
				BasePlugin.log(status);
			}
		} else if (variable.equals(ITigerstripeConstants.EXTERNALAPI_LIB)){
			IPath tigerstripeBasePath = findTigerstripeBaseJarPath();
			try {
				JavaCore.setClasspathVariable(
						ITigerstripeConstants.EXTERNALAPI_LIB, tigerstripeBasePath,
						null);
				
				// Create lib entries for each Annotation plugin so we can reference
				// them directly
				for (String pluginId : AnnotationUtils.getAnnotationPluginIds()) {
					IPath pPath = AnnotationUtils.getAnnotationPluginPath(pluginId);
					JavaCore.setClasspathVariable(pluginId, pPath, null);
				}
				
				
			} catch (JavaModelException e) {
				IStatus status = new Status(
						IStatus.ERROR,
						BasePlugin.getPluginId(),
						222,
						"Error setting up "+ITigerstripeConstants.EXTERNALAPI_LIB + " classPathVariable.",
						e);
				BasePlugin.log(status);
			}
		}

	}
	
	private IPath findEquinoxCommonJarPath() {
		Bundle b = Platform.getBundle("org.eclipse.equinox.common");
		try {
			File bFile = FileLocator.getBundleFile(b);
			return (new Path(bFile.getAbsolutePath())).makeAbsolute();
		} catch (IOException e) {
			BasePlugin.log(e);
		}
		return new Path("unknown_location_for_org.eclipse.equinox.common");
	}

	private IPath findTigerstripeBaseJarPath() {
		Bundle b = Platform.getBundle("org.eclipse.tigerstripe.workbench.base");
		try {
			File bFile = FileLocator.getBundleFile(b);
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
		return new Path(
				"unknown_location_for_org.eclipse.tigerstripe.workbench.base");
	}

}
