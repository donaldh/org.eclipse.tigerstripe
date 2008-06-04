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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.association;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactFormContentProviderBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IOssjArtifactFormContentProvider;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class AssociationArtifactFormContentProvider extends
		ArtifactFormContentProviderBase implements
		IOssjArtifactFormContentProvider {

	public boolean enabledInstanceMethods() {
		return false;
	}

	public boolean hasImplements() {
		return false;
	}

	public String getText(String textId) {
		if (IArtifactFormContentProvider.ARTIFACT_CONTENT_COMPONENTS
				.equals(textId)) {
			StringBuffer buf = new StringBuffer();

			buf.append("<form>");
			buf.append("<li><a href=\"aEnd\">aEnd</a>: one of the "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IAssociationArtifactImpl.class.getName()).getLabel(
							null) + " ends.</li>");
			buf.append("<li><a href=\"zEnd\">zEnd</a>: the other "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IAssociationArtifactImpl.class.getName()).getLabel(
							null) + " end.</li>");
			buf.append("</form>");

			return buf.toString();
		} else if (IArtifactFormContentProvider.ARTIFACT_CONTENT_DESCRIPTION
				.equals(textId))
			return "<form><p>An "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IAssociationArtifactImpl.class.getName()).getLabel(
							null)
					+ " Artifact explicitly models an association between two Artifacts.</p></form>";
		else if (IArtifactFormContentProvider.ARTIFACT_OVERVIEW_TITLE
				.equals(textId))
			return ArtifactMetadataFactory.INSTANCE.getMetadata(
					IAssociationArtifactImpl.class.getName()).getLabel(null)
					+ " Artifact";

		return textId;
	}

	public boolean hasMethods() {
		return false;
	}

	public boolean hasConstants() {
		return false;
	}

	public boolean hasAttributes() {
		return false;
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
