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

		ArrayList<String> strs = new ArrayList<String>();
		for (IStereotypeInstance instance : artifact.getStereotypeInstances()) {
			String name = instance.getName();
			if (!element.getStereotypes().contains(name)) {
				element.getStereotypes().add(name);
				element.setIsAbstract(artifact.isAbstract());// Bug 219454:
																// this is a
																// hack to
				// force the diagram to go dirty as the stereotype add
				// doesn't??????
			}
		}

		ArrayList<String> forRemoval = new ArrayList<String>();
		for (Object st : element.getStereotypes()) {
			String stName = (String) st;
			boolean found = false;
			for (IStereotypeInstance instance : artifact
					.getStereotypeInstances()) {
				if (instance.getName().equals(stName)) {
					found = true;
					break;
				}
			}
			if (!found) {
				forRemoval.add(stName);
			}
		}
		if (forRemoval.size() != 0) {
			if (forRemoval.size() != element.getStereotypes().size()) {
				element.getStereotypes().removeAll(forRemoval);
				element.setIsAbstract(artifact.isAbstract());// Bug 219454:
																// this is a
																// hack to
				// force the diagram to go dirty as the stereotype add
				// doesn't??????
			} else {
				element.resetStereotypes();
				element.setIsAbstract(artifact.isAbstract());// Bug 219454:
																// this is a
																// hack to
				// force the diagram to go dirty as the stereotype add
				// doesn't??????
			}
		}
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
	}

}
