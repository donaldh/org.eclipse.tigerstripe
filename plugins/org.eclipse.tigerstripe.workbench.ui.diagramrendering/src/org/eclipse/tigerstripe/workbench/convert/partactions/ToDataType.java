package org.eclipse.tigerstripe.workbench.convert.partactions;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;

public class ToDataType extends ConvertAction {

	@Override
	protected Class<?> toClass() {
		return IDatatypeArtifact.class;
	}
}
