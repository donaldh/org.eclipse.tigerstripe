/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ILabel;
import org.eclipse.tigerstripe.api.artifacts.model.IType;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeInstance;
import org.eclipse.tigerstripe.core.util.Misc;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;

public class LiteralUpdateCommand extends AbstractArtifactUpdateCommand {

	public LiteralUpdateCommand(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		super(eArtifact, iArtifact);
	}

	@Override
	public void updateEArtifact(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		// go thru attributes in the EMF domain
		List<Literal> eLiterals = eArtifact.getLiterals();
		List<Literal> toDelete = new ArrayList<Literal>();
		for (Literal eLiteral : eLiterals) {
			ILabel targetLabel = null;
			for (ILabel iLabel : iArtifact.getILabels()) {
				if (iLabel.getName().equals(eLiteral.getName())) {
					targetLabel = iLabel;
					break;
				}
			}

			if (targetLabel == null) {
				// not found, delete
				toDelete.add(eLiteral);
			} else {
				// review that everything matches
				String targetType = Misc.removeJavaLangString(targetLabel
						.getIType().getFullyQualifiedName());
				String targetValue = targetLabel.getValue();
				if (eLiteral.getType() == null
						|| !eLiteral.getType().equals(targetType)) {
					eLiteral.setType(targetType);
				}
				if (eLiteral.getValue() == null
						|| !eLiteral.getValue().equals(targetValue)) {
					eLiteral.setValue(targetValue);
				}
			}
		}

		if (toDelete.size() != 0)
			eLiterals.removeAll(toDelete);

		// then make sure all attributes in the TS domain are present
		// in the EMF domain
		eLiterals = eArtifact.getLiterals();
		ILabel[] iLabels = iArtifact.getILabels();
		Literal eLiteral = null;
		for (ILabel iLabel : iLabels) {
			boolean found = false;
			for (Literal thisLiteral : eLiterals) {
				if (thisLiteral.getName().equals(iLabel.getName())) {
					eLiteral = thisLiteral;
					found = true;
					break;
				}
			}

			if (!found) {
				// need to create a new method in the EMF domain
				IType iLabelType = iLabel.getIType();
				if (iLabelType != null
						&& iLabelType.getFullyQualifiedName() != null
						&& iLabelType.getFullyQualifiedName().length() != 0) {
					String typeStr = iLabelType.getFullyQualifiedName();

					eLiteral = VisualeditorFactory.eINSTANCE.createLiteral();
					eLiteral.setName(iLabel.getName());
					eLiteral.setType(typeStr);
					eLiteral.setValue(iLabel.getValue());
					eArtifact.getLiterals().add(eLiteral);
				}
			}

			if (eLiteral != null) {
				if (eLiteral.getStereotypes().size() != iLabel
						.getStereotypeInstances().length) {
					// not even the same number of args, let's redo the list
					eLiteral.getStereotypes().clear();
					for (IextStereotypeInstance stereo : iLabel
							.getStereotypeInstances()) {
						eLiteral.getStereotypes().add(stereo.getName());
					}
				} else {
					// same number of stereotypes let's see if they all match
					List<String> eStereotypes = eLiteral.getStereotypes();
					IextStereotypeInstance[] iStereotypes = iLabel
							.getStereotypeInstances();
					for (int index = 0; index < iStereotypes.length; index++) {
						String eStereotypeName = eStereotypes.get(index);
						String iStereotypeName = iStereotypes[index].getName();
						if (!eStereotypeName.equals(iStereotypeName)) {
							eLiteral.getStereotypes().remove(index);
							eLiteral.getStereotypes().add(index,
									iStereotypeName);
						}
					}
				}
			}

		}

	}

	public void redo() {
		// TODO Auto-generated method stub
	}

}
