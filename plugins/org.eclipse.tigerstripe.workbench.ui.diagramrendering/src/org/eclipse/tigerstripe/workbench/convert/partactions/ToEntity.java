package org.eclipse.tigerstripe.workbench.convert.partactions;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;

public class ToEntity extends ConvertAction {
	
	@Override
	protected Class<?> toClass() {
		return IManagedEntityArtifact.class;
	}

}
