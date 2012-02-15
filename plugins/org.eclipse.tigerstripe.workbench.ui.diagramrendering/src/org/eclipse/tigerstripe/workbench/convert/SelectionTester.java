package org.eclipse.tigerstripe.workbench.convert;

import static org.eclipse.tigerstripe.workbench.convert.ConvertArtifactAndTwoAssociationsOperation.extract;
import static org.eclipse.tigerstripe.workbench.convert.ConvertArtifactAndTwoAssociationsOperation.prepare;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.convert.ConvertArtifactAndTwoAssociationsOperation.ArtifactAndTwoAssociations;
import org.eclipse.tigerstripe.workbench.convert.ConvertArtifactAndTwoAssociationsOperation.Decomposition;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class SelectionTester extends PropertyTester {

	private final static String CAN_TWO_ASSOC_AND_ARTIFACT = "canTwoAssocAndArtifact";
	
	public SelectionTester() {
	}

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		
		if (CAN_TWO_ASSOC_AND_ARTIFACT.equals(property)) {
			if (receiver instanceof IStructuredSelection) {
				List<IAbstractArtifact> artifacts = new ArrayList<IAbstractArtifact>();
				@SuppressWarnings("rawtypes")
				Iterator it = ((IStructuredSelection) receiver).iterator();
				while (it.hasNext()) {
					Object obj = it.next();
					if (obj instanceof IAdaptable) {
						IAbstractArtifact artifact = adapt((IAdaptable) obj, IAbstractArtifact.class);
						if (artifact != null) {
							artifacts.add(artifact);
						}
					}
				}
				ArtifactAndTwoAssociations extracted = extract(artifacts);
				if (!extracted.isValid()) {
					return false;
				}
				Decomposition prepare = prepare(extracted.artifact, extracted.one, extracted.two);
				return prepare.isValid();
			}
		}
		return false;
	}
}
