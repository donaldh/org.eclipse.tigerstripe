package org.eclipse.tigerstripe.workbench.convert.partactions;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;

public class ToAssociation extends ConvertAction {

	@Override
	protected Class<?> toClass() {
		return IAssociationArtifact.class;
	}

}
