package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

import java.util.Map;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;


public interface IModelMapper {

	public Map<Classifier, String> getMapping(Model model);
	
}
