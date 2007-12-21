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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.associationClass;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactFormContentProviderBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.association.AssociationSpecificsSection;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class AssociationClassArtifactFormContentProvider extends
		ArtifactFormContentProviderBase implements
		IOssjArtifactFormContentProvider {

	public boolean enabledInstanceMethods() {
		return false;
	}

	public boolean hasImplements() {
		return true;
	}

	public String getText(String textId) {
		if (IArtifactFormContentProvider.ARTIFACT_CONTENT_COMPONENTS
				.equals(textId)) {
			StringBuffer buf = new StringBuffer();

			buf.append("<form>");
			buf
					.append("<li><a href=\"aEnd\">aEnd</a>: one of the association ends.</li>");
			buf
					.append("<li><a href=\"zEnd\">zEnd</a>: the other association end.</li>");
			buf
					.append("<li><a href=\"attributes\">Attributes</a>: a list of attributes for this Association.</li>");
			buf.append("</form>");

			return buf.toString();
		} else if (IArtifactFormContentProvider.ARTIFACT_CONTENT_DESCRIPTION
				.equals(textId))
			return "<form><p>An Association Class Artifact explicitly models an association between two Artifacts.</p></form>";
		else if (IArtifactFormContentProvider.ARTIFACT_OVERVIEW_TITLE
				.equals(textId))
			return "Association Class Artifact";
		// Annoyance 14 - removed welcome section (js)
		return textId;
	}

	public boolean hasMethods() {
		return true;
	}

	public boolean hasConstants() {
		return false;
	}

	public boolean hasAttributes() {
		return true;
	}

	@Override
	public boolean hasSpecifics() {
		return true;
	}

	private AssociationSpecificsSection section = null;

	@Override
	public TigerstripeSectionPart getSpecifics(TigerstripeFormPage page,
			Composite body, FormToolkit toolkit) {
		if (section == null) {
			section = new AssociationSpecificsSection(page, body, toolkit);
		}
		return section;
	}
}
