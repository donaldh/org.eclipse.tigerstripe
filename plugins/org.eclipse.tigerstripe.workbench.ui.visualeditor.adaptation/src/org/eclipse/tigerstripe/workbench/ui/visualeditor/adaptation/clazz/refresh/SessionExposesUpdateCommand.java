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

import java.util.Collection;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class SessionExposesUpdateCommand extends AbstractArtifactUpdateCommand {

	public SessionExposesUpdateCommand(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		super(eArtifact, iArtifact);
	}

	@Override
	public void updateEArtifact(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		SessionFacadeArtifact session = (SessionFacadeArtifact) eArtifact;
		ISessionArtifact iSession = (ISessionArtifact) iArtifact;

		session.getExposedProcedures().clear();
		Collection<IExposedUpdateProcedure> details = iSession
				.getExposedUpdateProcedures();
		MapHelper helper = new MapHelper((Map) eArtifact.eContainer());
		for (IExposedUpdateProcedure detail : details) {
			String fqn = detail.getFullyQualifiedName();
			AbstractArtifact target = helper.findAbstractArtifactFor(fqn);
			if (target != null) {
				session.getExposedProcedures().add(target);
			}
		}
	}

	public void redo() {
		// TODO Auto-generated method stub

	}

}
