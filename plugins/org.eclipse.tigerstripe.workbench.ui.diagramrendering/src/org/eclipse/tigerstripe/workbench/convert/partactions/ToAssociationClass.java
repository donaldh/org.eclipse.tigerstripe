package org.eclipse.tigerstripe.workbench.convert.partactions;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;

public class ToAssociationClass extends ConvertAction {
	
	@Override
	protected Class<?> toClass() {
		return IAssociationClassArtifact.class;
	}

	@Override
	protected boolean isEnabled() {
		IAbstractArtifact[] artifacts = getCorrespondingArtifacts();
		switch (artifacts.length) {
		case 1:
			return exactlyAssociation(artifacts[0]);
		case 2:
			byte flags = 0;
			for (IAbstractArtifact artifact : artifacts) {
				if (notAssociation(artifact)) {
					flags |= 1;	
				} else if (exactlyAssociation(artifact)) {
					flags |= 2;
				}
			}
			return flags == 3;
		default:
			return false;
		}
	}

	private boolean notAssociation(IAbstractArtifact artifact) {
		return !(artifact instanceof IAssociationArtifact);
	}
	
	private boolean exactlyAssociation(IAbstractArtifact artifact) {
		return (artifact instanceof IAssociationArtifact)
				&& !(artifact instanceof IAssociationClassArtifact);
	}
}
