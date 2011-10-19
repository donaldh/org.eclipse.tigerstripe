package org.eclipse.tigerstripe.annotation.core.storage.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.tigerstripe.annotation.core.Annotation;

/**
 * @author Yuri Strot
 *
 */
public class PackageFinder {

	public static List<EPackage> getPackages(EObject object) {
		List<EPackage> packages = new ArrayList<EPackage>();
		if (object instanceof Annotation) {
			EObject content = ((Annotation)object).getContent();
			collectPackages(content, packages);
		}
		else {
			collectPackages(object, packages);
		}
		return packages;
	}
	
	protected static void collectPackages(EObject object, List<EPackage> packages) {
		EPackage pack = object.eClass().getEPackage();
		if (!packages.contains(pack))
			packages.add(pack);
        for(EObject child : object.eContents()){
        	collectPackages(child, packages);
        }
	}

}