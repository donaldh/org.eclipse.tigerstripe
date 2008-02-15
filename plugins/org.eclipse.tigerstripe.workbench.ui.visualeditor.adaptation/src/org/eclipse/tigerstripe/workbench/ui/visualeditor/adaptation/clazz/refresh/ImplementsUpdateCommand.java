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
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class ImplementsUpdateCommand extends AbstractArtifactUpdateCommand {

	public ImplementsUpdateCommand(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		super(eArtifact, iArtifact);
	}

	@Override
	public void updateEArtifact(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		Collection<IAbstractArtifact> iImplements = new ArrayList<IAbstractArtifact>();
		if (iArtifact.getImplementedArtifacts() != null) {
			iImplements = iArtifact.getImplementedArtifacts();
		}

		List<AbstractArtifact> eImplements = eArtifact.getImplements();

		Map map = (Map) eArtifact.eContainer();
		MapHelper helper = new MapHelper(map);
		if (eImplements.size() != iImplements.size()) {
			eImplements.clear();
			for (IAbstractArtifact iImpl : iImplements) {
				AbstractArtifact eArt = helper.findAbstractArtifactFor(iImpl);
				if (eArt != null)
					eImplements.add(eArt);
			}
		} else {
			for (IAbstractArtifact iArt : iImplements) {
				String fqn = iArt.getFullyQualifiedName();
				boolean found = false;
				for (AbstractArtifact eArt : eImplements) {
					if (eArt.getFullyQualifiedName().equals(fqn)) {
						found = true;
						break;
					}
				}
				if (!found) {
					AbstractArtifact target = helper
							.findAbstractArtifactFor(iArt);
					if (target != null) {
						eImplements.add(target);
					}
				}
			}

			// then remove those that are in the eImplements but not in the
			// iImplements
			for (Iterator<AbstractArtifact> iter = eImplements.iterator(); iter
					.hasNext();) {
				AbstractArtifact eArt = iter.next();
				String fqn = eArt.getFullyQualifiedName();
				boolean found = false;
				for (IAbstractArtifact iArt : iImplements) {
					if (iArt.getFullyQualifiedName().equals(fqn)) {
						found = true;
						break;
					}
				}
				if (!found) {
					iter.remove();
				}
			}
		}
	}

	public void redo() {
		// TODO Auto-generated method stub

	}

}
