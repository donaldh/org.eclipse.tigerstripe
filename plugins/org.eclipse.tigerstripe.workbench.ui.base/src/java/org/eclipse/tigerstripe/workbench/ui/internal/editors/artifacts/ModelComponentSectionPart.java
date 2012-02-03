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

import static org.eclipse.tigerstripe.workbench.ui.EclipsePlugin.getClipboard;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.containsMembers;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.doPaste;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.model.HierarchyWalker;
import org.eclipse.tigerstripe.workbench.model.IHierarchyVisitor;
import org.eclipse.tigerstripe.workbench.model.ModelUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
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

	private final Map<String, IAbstractArtifact> updatedArtifacts = new HashMap<String, IAbstractArtifact>();

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

						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								TableViewer viewer = getViewer();
								if (viewer != null
										&& !viewer.getTable().isDisposed()) {
									viewer.refresh();
								}
							}
						});
					}
				}

				public void artifactRenamed(IAbstractArtifact artifact,
						String fromFQN) {
					artifactChanged(artifact, null);
				}

				public void managerReloaded() {
				}

			};
			IAbstractArtifact artifact = getIArtifact();
			if (artifact != null) {
				ITigerstripeModelProject project = artifact.getProject();
				if (project != null) {
					IArtifactManagerSession session = project
							.getArtifactManagerSession();
					if (session != null) {
						session.addArtifactChangeListener(listener);
					}
				}
			}
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
				if ((project!=null) && (!project.wasDisposed())) {
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
		}
		/*
		 * else { if (artifact != getIArtifact()) { IResource res = (IResource)
		 * artifact .getAdapter(IResource.class); if (res instanceof IFile) {
		 * try { Reader reader = new InputStreamReader( ((IFile)
		 * res).getContents()); try { artifact = artifact .getProject()
		 * .getArtifactManagerSession() .extractArtifact(reader, new
		 * NullProgressMonitor()); } finally { reader.close(); } } catch
		 * (Exception e) { EclipsePlugin.log(e); } } } }
		 */
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

	protected void registerDetailsPage(DetailsPart detailsPart,
			final IDetailsPage detailsPage, final Class<?> clazz) {
		detailsPart.setPageProvider(new IDetailsPageProvider() {
			public Object getPageKey(Object object) {
				if (clazz.isAssignableFrom(object.getClass())) {
					return clazz;
				}
				return null;
			}

			public IDetailsPage getPage(Object key) {
				if (clazz.equals(key)) {
					return detailsPage;
				}
				return null;
			}
		});
		detailsPart.registerPage(clazz, detailsPage);
	}

	protected void makeMenu(Table table) {
		Menu menu = new Menu(table);
		final MenuItem pasteItem = new MenuItem(menu, SWT.PUSH);

		menu.addMenuListener(new MenuAdapter() {

			public void menuShown(MenuEvent e) {
				pasteItem.setEnabled(containsMembers(getClipboard()));
			}
		});

		pasteItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doPaste(getIArtifact(), getClipboard(), false);
				refresh();
				markPageModified();
			}
		});
		pasteItem.setEnabled(false);
		pasteItem.setText("Paste");
		table.setMenu(menu);
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

}
