package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import java.util.HashMap;
import java.util.Map;

public class ProjectRecord {

	private static Map<String,String> artifactList = new HashMap<String,String>();
	
	public static Map<String,String> getArtifactList() {
		return artifactList;
	}
	
	
	public static void addArtifact(String newArtifact){
		String name = newArtifact;
		if (newArtifact.contains(".")){
			name = newArtifact.substring(newArtifact.lastIndexOf(".")+1);
		}
		artifactList.put(newArtifact, name);
	}
	
}
