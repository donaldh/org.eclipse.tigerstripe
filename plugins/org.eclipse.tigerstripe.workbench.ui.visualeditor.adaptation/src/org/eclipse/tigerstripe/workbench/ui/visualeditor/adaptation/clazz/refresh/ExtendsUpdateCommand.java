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

import java.util.List;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;

public class ExtendsUpdateCommand extends AbstractArtifactUpdateCommand {

	public ExtendsUpdateCommand(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		super(eArtifact, iArtifact);
	}

	protected boolean hideExtends() {
		NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
				getEArtifact());
		return Boolean
				.parseBoolean(helper
						.getProperty(NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS));
	}

	@Override
	public void updateEArtifact(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {

		// Bug 933
		if (hideExtends()) {
			if (eArtifact.getExtends() != null)
				eArtifact.setExtends(null);
			return;
		}

		String iExtends = null;
		AbstractArtifact target = null;
		if (iArtifact.getExtendedArtifact() != null) {
			iExtends = iArtifact.getExtendedArtifact().getFullyQualifiedName();
		}

		if (iExtends != null) {
			Map map = (Map) eArtifact.eContainer();
			List<AbstractArtifact> artifacts = map.getArtifacts();
			for (AbstractArtifact artifact : artifacts) {
				if (artifact.getFullyQualifiedName() != null
						&& artifact.getFullyQualifiedName().equals(iExtends)) {
					target = artifact;
				}
			}
			if (target != null
					&& (eArtifact.getExtends() == null || !target
							.getFullyQualifiedName().equals(
									eArtifact.getExtends()
											.getFullyQualifiedName()))) {
				eArtifact.setExtends(target);
			}
		} else {
			if (eArtifact.getExtends() != null) {
				eArtifact.setExtends(null);
			}
		}

	}

	public void redo() {
		// TODO Auto-generated method stub

	}

}
