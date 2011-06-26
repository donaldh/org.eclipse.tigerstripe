package org.eclipse.tigerstripe.workbench.convert.handlers;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;

public class ToEntity extends ConvertationHandler {

	@Override
	protected Class<?> toClass() {
		return IManagedEntityArtifact.class;
	}

}
