package org.eclipse.tigerstripe.workbench.convert.partactions;

import static java.util.Arrays.asList;
import static org.eclipse.tigerstripe.workbench.convert.ConvertArtifactAndTwoAssociationsOperation.extract;
import static org.eclipse.tigerstripe.workbench.convert.ConvertArtifactAndTwoAssociationsOperation.prepare;

import org.eclipse.tigerstripe.workbench.convert.ConvertArtifactAndTwoAssociationsOperation.ArtifactAndTwoAssociations;
import org.eclipse.tigerstripe.workbench.convert.ConvertArtifactAndTwoAssociationsOperation.Decomposition;
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
		if (!super.isEnabled()) {
			return false;
		}
		IAbstractArtifact[] artifacts = getCorrespondingArtifacts();
		switch (artifacts.length) {
		case 1:
			return exactlyAssociation(artifacts[0]);
		case 3:
			ArtifactAndTwoAssociations extracted = extract(asList(artifacts));
			if (!extracted.isValid()) {
				return false;
			}
			Decomposition decomposition = prepare(extracted.artifact, extracted.one, extracted.two);
			return decomposition.isValid();
		default:
			return false;
		}
	}

	private boolean exactlyAssociation(IAbstractArtifact artifact) {
		return (artifact instanceof IAssociationArtifact)
				&& !(artifact instanceof IAssociationClassArtifact);
	}
}
