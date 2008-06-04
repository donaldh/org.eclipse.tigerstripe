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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.session;

import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactFormContentProviderBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IOssjArtifactFormContentProvider;

public class SessionArtifactFormContentProvider extends
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
					.append("<li><b>Managed Entities</b>: establishing stewardship for entity artifacts.</li>");
			buf
					.append("<li><b>Named Queries</b>: the queries exposed through this session.</li>");
			buf
					.append("<li><b>Notifications</b>: notifications emitted by this session.</li>");
			buf.append("</form>");

			return buf.toString();
		} else if (IArtifactFormContentProvider.ARTIFACT_CONTENT_DESCRIPTION
				.equals(textId))
			return "<form><p></p></form>";
		else if (IArtifactFormContentProvider.ARTIFACT_OVERVIEW_TITLE
				.equals(textId))
			return ArtifactMetadataFactory.INSTANCE.getMetadata(
					ISessionArtifactImpl.class.getName()).getLabel(null)
					+ " Artifact";
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
		return false;
	}

	public boolean hasAttributes() {
		return false;
	}

}
