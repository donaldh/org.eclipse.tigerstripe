package org.eclipse.tigerstripe.workbench.convert.handlers;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;

public class ToAssociation extends ConvertationHandler {

	@Override
	protected Class<?> toClass() {
		return IAssociationArtifact.class;
	}

}
