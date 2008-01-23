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

import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class QueryReturnsUpdateCommand extends AbstractArtifactUpdateCommand {

	public QueryReturnsUpdateCommand(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		super(eArtifact, iArtifact);
	}

	@Override
	public void updateEArtifact(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		String iReturns = null;
		AbstractArtifact target = null;
		if (((IQueryArtifact) iArtifact).getReturnedType() != null) {
			iReturns = ((IQueryArtifact) iArtifact).getReturnedType()
					.getFullyQualifiedName();
		}
		if (iReturns != null) {
			MapHelper helper = new MapHelper((Map) eArtifact.eContainer());
			target = helper.findAbstractArtifactFor(iReturns);

			if (((NamedQueryArtifact) eArtifact).getReturnedType() != target)
				((NamedQueryArtifact) eArtifact).setReturnedType(target);
		}
	}

	public void redo() {
		// TODO Auto-generated method stub

	}

}
