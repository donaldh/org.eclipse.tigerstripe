package org.eclipse.tigerstripe.workbench.patterns;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public interface IRelationPattern extends IArtifactPattern{

	public void executeRequests(ITigerstripeModelProject project,
			String packageName, String artifactName, String extendedArtifact,
			String aEndType, String zEndType);
	
}
