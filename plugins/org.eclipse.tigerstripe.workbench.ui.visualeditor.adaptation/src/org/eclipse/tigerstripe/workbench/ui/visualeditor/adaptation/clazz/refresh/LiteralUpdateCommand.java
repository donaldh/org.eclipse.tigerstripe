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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.utils.ClassDiagramUtils;

public class LiteralUpdateCommand extends AbstractArtifactUpdateCommand {

	public LiteralUpdateCommand(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		super(eArtifact, iArtifact);
	}

	@Override
	public void updateEArtifact(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		
		if (! ClassDiagramUtils.checkLiteralOrder(eArtifact, iArtifact.getLiterals())){
			eArtifact.getLiterals().clear();
		}
		
		// go thru attributes in the EMF domain
		List<Literal> eLiterals = eArtifact.getLiterals();
		List<Literal> toDelete = new ArrayList<Literal>();
		for (Literal eLiteral : eLiterals) {
			ILiteral targetLabel = null;
			for (ILiteral iLiteral : iArtifact.getLiterals()) {
				if (iLiteral.getName().equals(eLiteral.getName())) {
					targetLabel = iLiteral;
					break;
				}
			}

			if (targetLabel == null) {
				// not found, delete
				toDelete.add(eLiteral);
			} else {
				// review that everything matches
				String targetType = Misc.removeJavaLangString(targetLabel
						.getType().getFullyQualifiedName());
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
		Literal eLiteral = null;
		for (ILiteral iLiteral : iArtifact.getLiterals()) {
			boolean found = false;
			for (Literal thisLiteral : eLiterals) {
				if (thisLiteral.getName().equals(iLiteral.getName())) {
					eLiteral = thisLiteral;
					found = true;
					break;
				}
			}

			if (!found) {
				// need to create a new method in the EMF domain
				IType iLiteralType = iLiteral.getType();
				if (iLiteralType != null
						&& iLiteralType.getFullyQualifiedName() != null
						&& iLiteralType.getFullyQualifiedName().length() != 0) {
					String typeStr = iLiteralType.getFullyQualifiedName();

					eLiteral = VisualeditorFactory.eINSTANCE.createLiteral();
					eLiteral.setName(iLiteral.getName());
					eLiteral.setType(typeStr);
					eLiteral.setValue(iLiteral.getValue());
					eArtifact.getLiterals().add(eLiteral);
				}
			}

			if (eLiteral != null) {
				if (eLiteral.getStereotypes().size() != iLiteral
						.getStereotypeInstances().size()) {
					// not even the same number of stereotypes, let's redo the list
					eLiteral.getStereotypes().clear();
					eLiteral.setName(iLiteral.getName());// Bug 219454: this is a hack to 
					// force the diagram to go dirty as the stereotype add doesn't??????

					for (IStereotypeInstance stereo : iLiteral
							.getStereotypeInstances()) {
						eLiteral.getStereotypes().add(stereo.getName());
					}
				} else {
					// same number of stereotypes let's see if they all match
					List<String> eStereotypes = eLiteral.getStereotypes();
					Iterator<String> eStereo = eStereotypes.iterator();
					Collection<IStereotypeInstance> iStereotypes = iLiteral.getStereotypeInstances();
					boolean updateNeeded = false;
					for (IStereotypeInstance iStereo : iStereotypes) {
						String eStereotypeName = eStereo.next();
						String iStereotypeName = iStereo.getName();
						
						if (!eStereotypeName.equals(iStereotypeName)) {
							updateNeeded = true;
							break;
						}

					}
					if (updateNeeded){
						// Bug 215646 - Just redo the whole list as the order is relevant -
						// You can confuse the diagram
						eLiteral.getStereotypes().clear();
						for (IStereotypeInstance stereo : iLiteral
								.getStereotypeInstances()) {
							eLiteral.getStereotypes().add(stereo.getName());
						}
							
						eLiteral.setName(iLiteral.getName());// Bug 219454: this is a hack to 
						// force the diagram to go dirty as the stereotype add doesn't??????
						
					}
				}
			}

		}

	}

	public void redo() {
		// TODO Auto-generated method stub
	}

}
