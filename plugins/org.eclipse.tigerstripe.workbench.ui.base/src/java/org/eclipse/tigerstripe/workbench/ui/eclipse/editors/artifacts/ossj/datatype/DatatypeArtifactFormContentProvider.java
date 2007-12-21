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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.datatype;

import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactFormContentProviderBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.IOssjArtifactFormContentProvider;

public class DatatypeArtifactFormContentProvider extends
		ArtifactFormContentProviderBase implements
		IOssjArtifactFormContentProvider {

	public boolean enabledInstanceMethods() {
		return false;
	}

	public String getText(String textId) {
		if (IArtifactFormContentProvider.ARTIFACT_CONTENT_COMPONENTS
				.equals(textId)) {
			StringBuffer buf = new StringBuffer();

			buf.append("<form>");
			buf
					.append("<li><a href=\"attributes\">Attributes</a>: Containing the detailed information for this Datatype Artifact.</li>");
			buf
					.append("<li><a href=\"constants\">Constants</a>: Contants related to this Datatype Artifact.</li>");
			buf
					.append("<li><a href=\"methods\">Methods</a>: Operations that are allowed on this Datatype Artifact.</li>");
			buf.append("</form>");

			return buf.toString();
		} else if (IArtifactFormContentProvider.ARTIFACT_CONTENT_DESCRIPTION
				.equals(textId))
			return "<form><p>As opposed to Managed Entity Artifacts, Datatype Artifacts don't have a key.</p></form>";
		else if (IArtifactFormContentProvider.ARTIFACT_OVERVIEW_TITLE
				.equals(textId))
			return "Datatype Artifact";
		// Annoyance 14 - removed welcome section (js)
		return textId;
	}

	public boolean hasImplements() {
		return false;
	}

	public boolean hasMethods() {
		return true;
	}

	public boolean hasConstants() {
		return true;
	}

	public boolean hasAttributes() {
		return true;
	}

}
