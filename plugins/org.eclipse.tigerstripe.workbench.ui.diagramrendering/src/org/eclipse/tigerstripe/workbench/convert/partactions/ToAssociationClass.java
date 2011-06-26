package org.eclipse.tigerstripe.workbench.convert.partactions;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;

public class ToAssociationClass extends ConvertAction {
	
	@Override
	protected Class<?> toClass() {
		return IAssociationClassArtifact.class;
	}
}
