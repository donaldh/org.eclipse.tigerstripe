package org.eclipse.tigerstripe.workbench.convert.handlers;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;

public class ToDataType extends ConvertationHandler {

	@Override
	protected Class<?> toClass() {
		return IDatatypeArtifact.class;
	}

}
