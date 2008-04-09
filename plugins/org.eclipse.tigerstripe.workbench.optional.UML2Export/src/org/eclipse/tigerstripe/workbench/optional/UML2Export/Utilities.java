package org.eclipse.tigerstripe.workbench.optional.UML2Export;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.uml2.uml.Model;

public class Utilities {


	public static String mapName(String name, String  modelName) {
		return modelName + "::" + name.replace(".", "::");
	}

	// There doesn't always have to be an upper and lower bound......
	// eg "*", "0" or "1" - what does that come out as ?
	
	public static int getLowerBound(IModelComponent.EMultiplicity multi) {
		if (multi.getLabel().startsWith("0"))
			return 0;
		else if (multi.getLabel().startsWith("1"))
			return 1;
		else
			// This will be a star!
			// Is this correct ?
			return 0;
	}

	public static int getUpperBound(IModelComponent.EMultiplicity multi) {
		if (multi.getLabel().endsWith("0"))
			return 0;
		else if (multi.getLabel().endsWith("1"))
			return 1;
		else
			return -1;
	}

	
	
}
