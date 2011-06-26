package org.eclipse.tigerstripe.workbench.convert.handlers;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;

public class ToAssociationClass extends ConvertationHandler {

	@Override
	protected Class<?> toClass() {
		return IAssociationClassArtifact.class;
	}

}
