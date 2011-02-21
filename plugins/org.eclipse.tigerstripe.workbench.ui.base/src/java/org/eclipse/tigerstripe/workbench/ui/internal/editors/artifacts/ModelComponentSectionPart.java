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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.model.HierarchyWalker;
import org.eclipse.tigerstripe.workbench.model.IHierarchyVisitor;
import org.eclipse.tigerstripe.workbench.model.ModelUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
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
				ITigerstripeModelProject project = getIArtifact().getProject();
				if (!project.wasDisposed()) {
					project.getArtifactManagerSession()
							.removeArtifactChangeListener(listener);
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	protected boolean inHierarhy(final IAbstractArtifact changedArtifact) {
		IAbstractArtifact editorArtifact = getIArtifact();
		if (ModelUtils.equalsByFQN(changedArtifact, editorArtifact)) {
			return false;
		}

		final boolean[] result = new boolean[1];
		new HierarchyWalker(true, isListenImplemented()).accept(editorArtifact,
				new IHierarchyVisitor() {

					public boolean accept(IAbstractArtifact artifact) {

						if (ModelUtils.equalsByFQN(changedArtifact, artifact)) {
							result[0] = true;
							// break walk
							return false;
						} else {
							return true;
						}
					}

				});
		return result[0];
	}

	protected abstract boolean isListenImplemented();

	protected Collection<IAbstractArtifact> getHierarchy(
			boolean includeImplemented) {
		LinkedHashSet<IAbstractArtifact> hierarchy = new LinkedHashSet<IAbstractArtifact>();
		fetchHierarchy(getIArtifact(), hierarchy, new HashSet<String>(),
				includeImplemented);
		return hierarchy;
	}

	private void fetchHierarchy(IAbstractArtifact artifact,
			Set<IAbstractArtifact> hierarchy, Set<String> fqns,
			boolean includeImplemented) {
		if (artifact == null) {
			return;
		}
		String fqn = artifact.getFullyQualifiedName();
		if (!fqns.add(fqn)) {
			return;
		}
		IAbstractArtifact updated = updatedArtifacts.get(fqn);
		if (updated != null) {
			artifact = updated;
		} else {
			if (artifact != getIArtifact()) {
				IResource res = (IResource) artifact
						.getAdapter(IResource.class);
				if (res instanceof IFile) {
					try {
						Reader reader = new InputStreamReader(
								((IFile) res).getContents());
						try {
							artifact = artifact
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
		}
		hierarchy.add(artifact);

		IAbstractArtifact extended = artifact.getExtendedArtifact();
		if (extended != null) {
			fetchHierarchy(extended, hierarchy, fqns, includeImplemented);
		}

		if (includeImplemented) {
			for (IAbstractArtifact impl : artifact.getImplementedArtifacts()) {
				fetchHierarchy(impl, hierarchy, fqns, includeImplemented);
			}
		}
	}
}
