package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Model;


public interface IModelMapper {

	public Map<EObject, String> getMapping(Model model);
	
}
