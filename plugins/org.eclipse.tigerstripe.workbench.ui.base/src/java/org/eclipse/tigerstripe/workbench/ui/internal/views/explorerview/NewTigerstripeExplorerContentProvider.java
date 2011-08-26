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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jdt.internal.ui.navigator.JavaNavigatorContentProvider;
import org.eclipse.jdt.internal.ui.packageview.ClassPathContainer;
import org.eclipse.jdt.internal.ui.packageview.ClassPathContainer.RequiredProjectWrapper;
import org.eclipse.tigerstripe.workbench.IElementWrapper;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.ProjectMigrationUtils;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeM0GeneratorNature;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripePluginProjectNature;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeProjectNature;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ContextProjectAwareProxy;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.LogicalExplorerNodeFactory;

public class NewTigerstripeExplorerContentProvider extends
		JavaNavigatorContentProvider {

	private boolean showRelationshipAnchors = false;

	public NewTigerstripeExplorerContentProvider() {
		super(true);
	}

	public void setShowRelationshipAnchors(boolean show) {
		this.showRelationshipAnchors = show;
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		Object[] rawChildren = NO_CHILDREN;

		boolean isJarPackageFragmentRoot = false;

		if (parentElement instanceof IAbstractArtifact) {
			IAbstractArtifact artifact = (IAbstractArtifact) parentElement;
			List<Object> raw = new ArrayList<Object>();

			raw.addAll(artifact.getContainedModelComponents());

			// This code adds the Association Ends below the artifact
			// in the explorer.
			if (showRelationshipAnchors) {
				try {
					IAbstractArtifactInternal art = (IAbstractArtifactInternal) artifact;
					HashSet<String> hierarchy = new HashSet<String>();
					featchHierarhyUp(art, hierarchy);
					ArtifactManager artifactManager = art.getArtifactManager();

					for (String fqn : hierarchy) {
						boolean isInherited = !art.getFullyQualifiedName()
								.equals(fqn);

						List<IRelationship> origs = artifactManager
								.getOriginatingRelationshipForFQN(fqn, true);

						for (IRelationship rel : origs) {
							RelationshipAnchor anchor = new RelationshipAnchor(
									rel.getRelationshipAEnd());
							anchor.setInherited(isInherited);
							raw.add(anchor);
						}

						List<IRelationship> terms = artifactManager
								.getTerminatingRelationshipForFQN(fqn, true);

						for (IRelationship rel : terms) {
							RelationshipAnchor anchor = new RelationshipAnchor(
									rel.getRelationshipZEnd());
							anchor.setInherited(isInherited);
							raw.add(anchor);
						}
					}

				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
			rawChildren = raw.toArray();
		} else if (parentElement instanceof ICompilationUnit
				|| parentElement instanceof IClassFile) {
			IAbstractArtifact artifact = TSExplorerUtils
					.getArtifactFor(parentElement);
			if (artifact != null) {
				getChildren(artifact);
			}
		} else if (parentElement instanceof IJavaModel) {
			rawChildren = getTigerstripeProjects();
		} else if (parentElement instanceof JarPackageFragmentRoot) {
			IPackageFragmentRoot fragmentRoot = (IPackageFragmentRoot) parentElement;
			rawChildren = postProcessPackageFragmentRoot(fragmentRoot,
					super.getChildren(parentElement));
			Object[] result = new Object[rawChildren.length];
			for (int i = 0; i < rawChildren.length; i++) {
				result[i] = new ElementWrapper(rawChildren[i],
						getProjectFor(fragmentRoot.getJavaProject()));
			}
			return result;
		} else if (parentElement instanceof ClassPathContainer) {
			ClassPathContainer classPathContainer = (ClassPathContainer) parentElement;
			ITigerstripeModelProject context = (ITigerstripeModelProject) classPathContainer
					.getJavaProject()
					.getAdapter(ITigerstripeModelProject.class);
			if (context != null) {
				IAdaptable[] childs = ((ClassPathContainer) parentElement)
						.getChildren();
				List<Object> result = new ArrayList<Object>(childs.length);
				for (IAdaptable child : childs) {
					if (child instanceof RequiredProjectWrapper) {
						result.add(new ElementWrapper(
								((RequiredProjectWrapper) child).getProject()
										.getResource(),
								(ITigerstripeModelProject) ((ClassPathContainer) parentElement)
										.getJavaProject().getAdapter(
												ITigerstripeModelProject.class)));
					} else {
						result.add(child);
					}
				}
				return result.toArray(new Object[result.size()]);
			}
		} else if (parentElement instanceof IElementWrapper) {
			IElementWrapper wrapper = (IElementWrapper) parentElement;
			Object[] childs = getChildren(wrapper.getElement());
			List<Object> result = new ArrayList<Object>();
			for (Object child : childs) {
				if (isValidReferencedElement(child)) {
					Object elementToWrap = child;
					if (!(elementToWrap instanceof IModelComponent)) {
						Object adapted = Platform
								.getAdapterManager().getAdapter(child,
										IModelComponent.class);
						if (adapted != null) {
							elementToWrap = adapted;
						}
					}
					if (elementToWrap instanceof IModelComponent
							&& !(child instanceof IContextProjectAware)) {
						elementToWrap = ContextProjectAwareProxy.newInstance(
								elementToWrap, wrapper.getContextProject());

					}
					ElementWrapper elementWrapper = new ElementWrapper(
							elementToWrap, wrapper.getContextProject());
					result.add(elementWrapper);
				}
			}
			rawChildren = result.toArray(new Object[result.size()]);
		} else {
			// delegate
			rawChildren = super.getChildren(parentElement);
		}

		// Filter all children for logical nodes
		List<Object> filteredChildren = new ArrayList<Object>();
		for (Object object : rawChildren) {
			if (object instanceof JarEntryFile) {
				JarEntryFile f = (JarEntryFile) object;
				if (f.getName().endsWith(".package")) {
					continue;
				} else if (f.getName().endsWith(".vwm")
						|| f.getName().endsWith(".wvd")) {
					continue;
				} else {
					filteredChildren.add(f);
				}
			} else {
				Object node = LogicalExplorerNodeFactory.getInstance().getNode(
						object);
				if (node != null) {
					filteredChildren.add(node);
				}
			}
		}

		return filteredChildren.toArray(new Object[filteredChildren.size()]);
	}

	private boolean isValidReferencedElement(Object element) {
		if (element instanceof JarPackageFragmentRoot) {
			return false;
		} else if (element instanceof IFile
				&& ((IFile) element).getName().endsWith(".package")) {
			return false;
		} else if (element instanceof PackageFragmentRoot
				|| (element instanceof IAdaptable && ((IAdaptable) element)
						.getAdapter(IModelComponent.class) != null)) {
			return true;
		}
		return false;
	}

	private ITigerstripeModelProject getProjectFor(IJavaProject jProject) {
		if (jProject != null) {
			IProject project = jProject.getProject();
			IAbstractTigerstripeProject atsProject = (IAbstractTigerstripeProject) project
					.getAdapter(IAbstractTigerstripeProject.class);
			if (atsProject instanceof ITigerstripeModelProject) {
				return (ITigerstripeModelProject) atsProject;
			}
		}
		return null;
	}

	private void featchHierarhyUp(IAbstractArtifact artifact, Set<String> set) {
		if (artifact == null) {
			return;
		}
		if (!set.add(artifact.getFullyQualifiedName())) {
			return;
		}
		featchHierarhyUp(artifact.getExtendedArtifact(), set);
		for (IAbstractArtifact impl : artifact.getImplementingArtifacts()) {
			featchHierarhyUp(impl, set);
		}
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof AbstractLogicalExplorerNode) {
			AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) element;
			element = node.getKeyResource();
		}
		return super.getParent(element);
	}

	/**
	 * Post processing on JAR files to make them appear properly as TS modules
	 * when needed
	 * 
	 * @param packageRoot
	 * @param children
	 * @return
	 */
	private Object[] postProcessPackageFragmentRoot(
			IPackageFragmentRoot packageRoot, Object[] children) {
		ArrayList<Object> result = new ArrayList<Object>();

		for (Object obj : children) {
			if (obj instanceof JarEntryFile) {
				JarEntryFile file = (JarEntryFile) obj;
				String n = file.getName();
				if (file.getName().endsWith("ts-module.xml")) {
					continue; // ts-module shouldn't be displayed
				} else if (file.getName().endsWith(".ann")) {
					continue; // hide contained .ann files
				} else if (file.getName().endsWith(".package")) {
					continue;
				} else if (file.getName().endsWith(".vwm")) {
					continue;
				} else if (file.getName().endsWith(".wvd")) {
					continue;
				}
			} else if (obj instanceof IPackageFragment) {
				IPackageFragment fragment = (IPackageFragment) obj;
				if (fragment.getElementName().startsWith("META-INF")) {
					continue; // META-INF shouldn't be displayed
				}
			} else if (obj instanceof IStorage) {
				IStorage st = (IStorage) obj;
				String n = st.getName();
				if (st.getName().startsWith("META-INF")) {
					continue; // META-INF shouldn't be displayed
				}
			}
			result.add(obj);
		}

		return result.toArray();
	}

	/**
	 * Note: This method is for internal use only. Clients should not call this
	 * method.
	 */
	protected IProject[] getTigerstripeProjects() {
		List<IProject> result = new ArrayList<IProject>();
		IProject[] projects = EclipsePlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < projects.length; i++) {
			try {

				// Since 1.2 the nature has changed name @see #295
				ProjectMigrationUtils.handleProjectMigration(projects[i]);

				if (TigerstripePluginProjectNature.hasNature(projects[i])) {
					result.add(projects[i]);

					ProjectMigrationUtils
							.handlePluginProjectMigration(projects[i]);

					if (projects[i]
							.getAdapter(ITigerstripeGeneratorProject.class) != null) {

					}
				} else if (TigerstripeM0GeneratorNature.hasNature(projects[i])) {
					result.add(projects[i]);

				} else if (TigerstripeProjectNature.hasNature(projects[i])) {
					result.add(projects[i]);

					// At this point we build up the Artifact Manager
					// from the snapshot we have
					if (projects[i].getAdapter(ITigerstripeModelProject.class) != null) {

						// @since 0.4
						// For compatibility reasons, we check that a
						// TigerstripeProjectAuditor
						// is associated with the project. If not, just add it
						// silently.
						final IProject project = projects[i];
						if (!TigerstripeProjectAuditor
								.hasTigerstripeProjectAuditor(projects[i])) {
							TigerstripeProjectAuditor
									.addBuilderToProject(project);
							new Job("Tigerstripe Project Audit") {
								@Override
								protected IStatus run(IProgressMonitor monitor) {
									try {
										project.build(
												IncrementalProjectBuilder.FULL_BUILD,
												TigerstripeProjectAuditor.BUILDER_ID,
												null, monitor);
									} catch (CoreException e) {
										EclipsePlugin.log(e);
									}
									return org.eclipse.core.runtime.Status.OK_STATUS;
								}
							}.schedule();
						}
					}
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		}
		return result.toArray(new IProject[0]);
	}

}
