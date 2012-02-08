package org.eclipse.tigerstripe.workbench.model;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.ArtifactSettingDetails;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ArtifactUtils {

	public static IArtifactManagerSession getSession(IAbstractArtifact artifact) {
		try {
			ITigerstripeModelProject project = artifact.getProject();
			if (project != null) {
				return project.getArtifactManagerSession();
			}
		} catch (Exception e) {
			BasePlugin.log(e);
		}
		return null;
	}

	public static ArtifactManager getManager(IAbstractArtifact artifact) {
		IArtifactManagerSession session = getSession(artifact);
		if (session != null) {
			return session.getArtifactManager();
		}
		return null;
	}
	
	public static ITigerstripeModelProject getProject(IAbstractArtifact artifact) {
		try {
			return artifact.getProject();
		} catch (Exception e) {
			BasePlugin.log(e);
		}
		return null;
	}
	
	public static boolean isEnabled(Class<?> modelInterface) {
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
				.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
		ArtifactSettingDetails details = prop
				.getDetailsForType(IEventArtifact.class.getName());
		if (details == null) {
			return false;
		}
		return details.isEnabled();
	}
}
