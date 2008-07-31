package org.eclipse.tigerstripe.workbench.patterns;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface IArtifactPattern extends IPattern{

	
	public void executeRequests(ITigerstripeModelProject project,
			String packageName, String ArtifactName, String extendedArtifact);
	
}
