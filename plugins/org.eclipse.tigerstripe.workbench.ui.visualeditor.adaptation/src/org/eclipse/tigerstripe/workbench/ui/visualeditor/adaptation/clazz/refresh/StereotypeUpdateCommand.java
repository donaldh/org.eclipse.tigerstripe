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

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

/**
 * Refreshes stuff related to base elements of artifact (readonly) and
 * stereotype stuff.
 * 
 * @author Eric Dillon
 * 
 */
public class StereotypeUpdateCommand extends
		AbstractQualifiedNamedElementUpdateCommand {

	public StereotypeUpdateCommand(QualifiedNamedElement element,
			IAbstractArtifact iArtifact) {
		super(element, iArtifact);
	}

	@Override
	public void updateQualifiedNamedElement(QualifiedNamedElement element,
			IAbstractArtifact artifact) {
		if (element.isIsAbstract() != artifact.isAbstract()) {
			element.setIsAbstract(artifact.isAbstract());
		}

		if (element.isIsReadonly() != artifact.isReadonly())
			element.setIsReadonly(artifact.isReadonly());

		if (element.getStereotypes().size() != artifact
				.getStereotypeInstances().size()) {
			// not even the same number of stereotypes, let's redo the list
			element.getStereotypes().clear();
			element.setName(artifact.getName());// Bug 219454: this is a hack to 
			// force the diagram to go dirty as the stereotype add doesn't??????

			for (IStereotypeInstance stereo : artifact
					.getStereotypeInstances()) {
				element.getStereotypes().add(stereo.getName());
			}
		} else {
			// same number of stereotypes let's see if they all match
			List<String> eStereotypes = element.getStereotypes();
			Iterator<String> eStereo = eStereotypes.iterator();
			Collection<IStereotypeInstance> iStereotypes = artifact.getStereotypeInstances();
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
				// You can confuse the diagram during a delete/replace action
				element.getStereotypes().clear();
				for (IStereotypeInstance stereo : artifact
						.getStereotypeInstances()) {
					element.getStereotypes().add(stereo.getName());
				}
					
				element.setIsAbstract(artifact.isAbstract());// Bug 219454: this is a hack to 
				// force the diagram to go dirty as the stereotype add doesn't??????
				// Don't use the name - that forces a close on the editor!
				
			}
		}
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
	}

}
