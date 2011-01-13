/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class ModelComponentSectionPart extends ArtifactSectionPart {

	public static int MASTER_TABLE_COMPONENT_WIDTH = 250;
	private IArtifactChangeListener listener;

	public ModelComponentSectionPart(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider, int style) {
		super(page, parent, toolkit, labelProvider, contentProvider, style);
	}

	public abstract TableViewer getViewer();

	public abstract void updateMaster();

	protected abstract void createInternalContent();

	protected abstract void onExtendedArtifactChange(IAbstractArtifact artifact);

	private Map<String, IAbstractArtifact> updatedArtifacts = new HashMap<String, IAbstractArtifact>();

	@Override
	protected void createContent() {
		createInternalContent();
		try {
			listener = new IArtifactChangeListener() {

				public void artifactRemoved(IAbstractArtifact artifact) {
					artifactChanged(artifact, null);
				}

				public void artifactAdded(IAbstractArtifact artifact) {
					artifactChanged(artifact, null);
				}

				public void artifactChanged(IAbstractArtifact artifact,
						IAbstractArtifact oldArtifact) {
					if (inHierarhy(artifact)) {
						updatedArtifacts.put(artifact.getFullyQualifiedName(),
								artifact);
						onExtendedArtifactChange(artifact);
					}
				}

				public void artifactRenamed(IAbstractArtifact artifact,
						String fromFQN) {
					artifactChanged(artifact, null);
				}

				public void managerReloaded() {
				}

			};
			getIArtifact().getProject().getArtifactManagerSession()
					.addArtifactChangeListener(listener);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		getViewer().getTable().addFocusListener(
				new SelectionProviderIntermediateFocusListener() {

					@Override
					public IWorkbenchSite getWorkbenchSite() {
						return getPage().getSite();
					}

					@Override
					public ISelectionProvider getSelectionProvider() {
						return getViewer();
					}
				});
	}

	@Override
	public void dispose() {
		if (listener != null) {
			try {
				getIArtifact().getProject().getArtifactManagerSession()
						.removeArtifactChangeListener(listener);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	protected boolean inHierarhy(IAbstractArtifact artifact) {
		IAbstractArtifact extended = getIArtifact().getExtendedArtifact();
		while (extended != null) {
			if (extended.getFullyQualifiedName().equals(
					artifact.getFullyQualifiedName())) {
				return true;
			}
			extended = extended.getExtendedArtifact();
		}
		return false;
	}

	protected List<IAbstractArtifact> getHierarchy() {
		List<IAbstractArtifact> hierarhy = new ArrayList<IAbstractArtifact>();
		IAbstractArtifact rootArtifact = getIArtifact();
		hierarhy.add(rootArtifact);
		IAbstractArtifact extended = rootArtifact.getExtendedArtifact();
		while (extended != null) {
			IAbstractArtifact updated = updatedArtifacts.get(extended
					.getFullyQualifiedName());
			if (updated != null) {
				extended = updated;
			} else {
				IResource res = (IResource) extended
						.getAdapter(IResource.class);
				if (res instanceof IFile) {
					try {
						Reader reader = new InputStreamReader(
								((IFile) res).getContents());
						try {
							extended = extended
									.getProject()
									.getArtifactManagerSession()
									.extractArtifact(reader,
											new NullProgressMonitor());
						} finally {
							reader.close();
						}
					} catch (Exception e) {
						EclipsePlugin.log(e);
					}
				}
			}
			hierarhy.add(extended);
			extended = extended.getExtendedArtifact();
		}
		return hierarhy;
	}
}
