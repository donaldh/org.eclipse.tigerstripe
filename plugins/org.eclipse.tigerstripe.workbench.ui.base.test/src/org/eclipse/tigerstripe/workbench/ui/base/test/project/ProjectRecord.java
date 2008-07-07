package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import java.util.HashMap;
import java.util.Map;

public class ProjectRecord {

	private static Map<String,String> artifactList = new HashMap<String,String>();
	
	public static Map<String,String> getArtifactList() {
		return artifactList;
	}
	
	public static int getArtifactCount(){
		return artifactList.size();
	}
	
	public static void addArtifact(String newArtifact){
		String name = newArtifact;
		if (newArtifact.contains(".")){
			name = newArtifact.substring(newArtifact.lastIndexOf(".")+1);
		}
		artifactList.put(newArtifact, name);
	}
	
	public static void removeArtifact(String oldArtifact){
		String name = oldArtifact;
		if (oldArtifact.contains(".")){
			name = oldArtifact.substring(oldArtifact.lastIndexOf(".")+1);
		}
		if (artifactList.containsKey(oldArtifact)){
			artifactList.remove(oldArtifact);
		}
	}
	
	public static void removeArtifactbyName(String oldArtifact){
		if (artifactList.containsValue(oldArtifact)){
			for (String key : artifactList.keySet()){
				String val = artifactList.get(key);
				if (val.equals(oldArtifact)){
					artifactList.remove(oldArtifact);
					return;
				}
			}
		}
	}
	
}
