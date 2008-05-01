package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

import org.eclipse.uml2.uml.Model;

/**
 * This interface is used by the importer to optionally trim
 * a model prior to the import
 * @author rcraddoc
 *
 */
public interface IModelTrimmer {

	/**
	 * Take the modelToTrim, remove elements, and return the new Model.
	 * 
	 * The returned Model needs to be  a valid model in its own right.
	 * @param modelToTrim
	 * @return
	 */
	public Model trimModel(Model modelToTrim);	
}
