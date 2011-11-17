package org.eclipse.tigerstripe.workbench.model;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.utils.AdaptHelper;

public class FqnUtils {

	/**
	 * The method is used for cases when file can not be adapted to 
	 * the {@link IAbstractArtifact} directly. For instance after artifact removing.
	 * 
	 * @return artifact fully qualified name
	 */
	public static String getFqnForResource(IResource artResource) {
		String name = artResource.getName(); // "Entity.java" We have to cut '.java' suffix 
		if (name.length() < 6) {
			return null;
		}
		String withoutJavaSuffix = name.substring(0, name.length() - 5);
		
		IContainer parent = artResource.getParent();
		IAbstractArtifact pckg = AdaptHelper.adapt(parent, IAbstractArtifact.class);
		if (pckg == null) {
			return withoutJavaSuffix;
		} else {
			return pckg.getFullyQualifiedName() + "." + withoutJavaSuffix;
		}	
	}
	
}
