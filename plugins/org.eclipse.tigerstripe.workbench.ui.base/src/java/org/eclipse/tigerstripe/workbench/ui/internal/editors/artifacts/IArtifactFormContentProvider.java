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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * This interface provides the specific content used throughout forms
 * 
 * @author Eric Dillon
 * 
 */
public interface IArtifactFormContentProvider {

	public final static String ARTIFACT_CONTENT_COMPONENTS = "artifact.content.components.text";

	public final static String ARTIFACT_CONTENT_DESCRIPTION = "artifact.content.description.text";

	public final static String ARTIFACT_OVERVIEW_TITLE = "artifact.overview.title.text";

	// Annoyance 14 - removed welcome section (js)

	public String getText(String textId);

	public boolean hasMethods();

	public boolean hasAttributes();

	public boolean hasConstants();

	public boolean hasSpecifics();

	public boolean hasImplements();

	public TigerstripeSectionPart getSpecifics(TigerstripeFormPage page,
			Composite body, FormToolkit toolkit);
}
