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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.etadapter;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class AssociationClassClassETAdapter extends AbstractArtifactETAdapter {

	public AssociationClassClassETAdapter(
			AssociationClassClass assocClassClass, IModelUpdater modelUpdater,
			ClassDiagramSynchronizer synchronizer) {
		super(assocClassClass, modelUpdater, synchronizer);
	}

}
