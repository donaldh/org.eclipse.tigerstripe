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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class ArtifactFormContentProviderBase implements
		IArtifactFormContentProvider {

	public boolean hasSpecifics() {
		return false;
	}

	public TigerstripeSectionPart getSpecifics(TigerstripeFormPage page,
			Composite body, FormToolkit toolkit) {
		return null;
	}
}
