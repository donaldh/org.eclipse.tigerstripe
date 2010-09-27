package org.eclipse.tigerstripe.workbench.internal.content.describers;

/**
 * Content describer for Event Artifacts
 * 
 * @author Navid Mehregani
 */
public class SessionContentDescriber extends AbstractContentDescriber {

	@Override
	protected String getStringIdentifier() {
		return "@tigerstripe.sessionFacade";
	}

}