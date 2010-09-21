package org.eclipse.tigerstripe.workbench.internal.content.describers;

/**
 * Content describer for Entity Artifacts
 * 
 * @author Navid Mehregani
 */
public class EntityContentDescriber extends AbstractContentDescriber {

	@Override
	protected String getStringIdentifier() {
		return "@tigerstripe.managedEntity";
	}

}
