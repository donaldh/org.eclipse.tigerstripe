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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.datatype;

import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactFormContentProviderBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IOssjArtifactFormContentProvider;

public class DatatypeArtifactFormContentProvider extends
		ArtifactFormContentProviderBase implements
		IOssjArtifactFormContentProvider {

	public boolean hasExtends() {
		return true;
	}
	
	public boolean hasAbstract() {
		return true;
	}
	
	public boolean enabledInstanceMethods() {
		return false;
	}

	public String getText(String textId) {
		if (IArtifactFormContentProvider.ARTIFACT_CONTENT_COMPONENTS
				.equals(textId)) {
			StringBuffer buf = new StringBuffer();

			buf.append("<form>");
			buf
					.append("<li><a href=\"attributes\">Attributes</a>: Containing the detailed information for this "
							+ ArtifactMetadataFactory.INSTANCE
									.getMetadata(
											org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl.class
													.getName()).getLabel(null)
							+ " Artifact.</li>");
			buf
					.append("<li><a href=\"constants\">Constants</a>: Contants related to this "
							+ ArtifactMetadataFactory.INSTANCE
									.getMetadata(
											org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl.class
													.getName()).getLabel(null)
							+ " Artifact.</li>");
			buf
					.append("<li><a href=\"methods\">Methods</a>: Operations that are allowed on this "
							+ ArtifactMetadataFactory.INSTANCE
									.getMetadata(
											org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl.class
													.getName()).getLabel(null)
							+ " Artifact.</li>");
			buf.append("</form>");

			return buf.toString();
		} else if (IArtifactFormContentProvider.ARTIFACT_CONTENT_DESCRIPTION
				.equals(textId))
			return "<form><p>As opposed to "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IManagedEntityArtifactImpl.class.getName())
							.getLabel(null)
					+ " Artifacts, "
					+ ArtifactMetadataFactory.INSTANCE
							.getMetadata(
									org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl.class
											.getName()).getLabel(null)
					+ " Artifacts don't have a key.</p></form>";
		else if (IArtifactFormContentProvider.ARTIFACT_OVERVIEW_TITLE
				.equals(textId))
			return ArtifactMetadataFactory.INSTANCE
					.getMetadata(
							org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl.class
									.getName()).getLabel(null)
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
		return true;
	}

	public boolean hasAttributes() {
		return true;
	}

}
