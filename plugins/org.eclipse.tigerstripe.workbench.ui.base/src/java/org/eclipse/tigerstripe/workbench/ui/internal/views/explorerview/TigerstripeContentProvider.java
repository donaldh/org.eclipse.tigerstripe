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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.navigator.IExtensionStateConstants.Values;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.tigerstripe.workbench.diagram.IDiagram;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.preferences.ExplorerPreferencePage;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.internal.navigator.extensions.CommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.IExtensionStateModel;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.IPipelinedTreeContentProvider;
import org.eclipse.ui.navigator.PipelinedShapeModification;
import org.eclipse.ui.navigator.PipelinedViewerUpdate;

/**
 * Tigerstripe content provider.
 * 
 * @see NewTigerstripeExplorerContentProvider
 */
@SuppressWarnings("restriction")
public class TigerstripeContentProvider extends
		NewTigerstripeExplorerContentProvider implements
		IPipelinedTreeContentProvider, IPropertyChangeListener {

	public TigerstripeContentProvider() {
		super();
		boolean show = EclipsePlugin
				.getDefault()
				.getPreferenceStore()
				.getBoolean(
						ExplorerPreferencePage.P_LABEL_SHOW_RELATIONSHIP_ANCHORS);
		EclipsePlugin.getDefault().getPreferenceStore()
				.addPropertyChangeListener(this);
		setShowRelationshipAnchors(show);
	}

	private IExtensionStateModel fStateModel;

	private IPropertyChangeListener fLayoutPropertyListener;

	private INavigatorContentService contentService;

	public void init(ICommonContentExtensionSite commonContentExtensionSite) {
		IExtensionStateModel stateModel = ((CommonContentExtensionSite) commonContentExtensionSite)
				.getContentService().findStateModel(
						"org.eclipse.jdt.java.ui.javaContent");
		IMemento memento = commonContentExtensionSite.getMemento();
		fStateModel = stateModel;

		contentService = commonContentExtensionSite.getService();

		restoreState(memento);
		fLayoutPropertyListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (Values.IS_LAYOUT_FLAT.equals(event.getProperty())) {
					if (event.getNewValue() != null) {
						boolean newValue = ((Boolean) event.getNewValue())
								.booleanValue() ? true : false;
						setIsFlatLayout(newValue);
					}
				}

			}
		};
		fStateModel.addPropertyChangeListener(fLayoutPropertyListener);

		IPreferenceStore store = PreferenceConstants.getPreferenceStore();
		boolean showCUChildren = store
				.getBoolean(PreferenceConstants.SHOW_CU_CHILDREN);
		setProvideMembers(showCUChildren);
	}

	@Override
	public void dispose() {
		super.dispose();
		fStateModel.removePropertyChangeListener(fLayoutPropertyListener);
		EclipsePlugin.getDefault().getPreferenceStore()
				.removePropertyChangeListener(this);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Object element = inputElement;
		Object[] rawChildren = null;
		if (element instanceof IWorkspaceRoot) {
			IJavaElement jElement = JavaCore.create((IContainer) element);
			try {
				rawChildren = getTigerstripeProjects((IJavaModel) jElement);
			} catch (JavaModelException e) {
				EclipsePlugin.log(e);
			}
			return rawChildren;
		}
		return super.getElements(inputElement);
	}

	private void clearCompilationUnit(Set<Object> proposedChildren) {
		boolean isCompilationUnitChildren = true;
		for (Object object : proposedChildren) {
			if (!(object instanceof IJavaElement)
					|| !(((IJavaElement) object).getParent() instanceof ICompilationUnit)) {
				isCompilationUnitChildren = false;
				break;
			}
		}
		if (isCompilationUnitChildren) {
			proposedChildren.clear();
		}
	}

	private void customize(Object[] elements, Set<Object> proposedChildren) {
		if (elements.length == 0) {
			clearCompilationUnit(proposedChildren);
		}
		for (int i = 0; i < elements.length; i++) {
			Object element = elements[i];
			if (element instanceof IJavaProject) {
				boolean isRemoved = proposedChildren
						.remove(((IJavaProject) element).getProject());
				if (isRemoved) {
					proposedChildren.add(element);
				}
			} else if (element != null) {
				proposedChildren.clear();
				proposedChildren.addAll(Arrays.asList(elements));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void getPipelinedChildren(Object aParent, Set theCurrentChildren) {
		customize(getChildren(aParent), theCurrentChildren);
	}

	@SuppressWarnings("unchecked")
	public void getPipelinedElements(Object anInput, Set theCurrentElements) {
		customize(getElements(anInput), theCurrentElements);
	}

	public Object getPipelinedParent(Object anObject, Object aSuggestedParent) {
		return getParent(anObject);
	}

	public PipelinedShapeModification interceptAdd(
			PipelinedShapeModification anAddModification) {
		Object parent = anAddModification.getParent();

		if (parent instanceof IJavaProject) {
			anAddModification.setParent(((IJavaProject) parent).getProject());
		}

		if (parent instanceof IWorkspaceRoot) {
			deconvertTigerstripeProjects(anAddModification);
		}

		convertToTigerstripeElements(anAddModification);
		return anAddModification;
	}

	public PipelinedShapeModification interceptRemove(
			PipelinedShapeModification aRemoveModification) {
		contentService.update();
		return null;
	}

	public boolean interceptRefresh(
			PipelinedViewerUpdate aRefreshSynchronization) {
		return false;
	}

	public boolean interceptUpdate(PipelinedViewerUpdate anUpdateSynchronization) {
		return false;
	}

	public void restoreState(IMemento aMemento) {
	}

	public void saveState(IMemento aMemento) {
	}

	@SuppressWarnings("unchecked")
	private void deconvertTigerstripeProjects(
			PipelinedShapeModification modification) {
		Set convertedChildren = new LinkedHashSet();
		for (Iterator iterator = modification.getChildren().iterator(); iterator
				.hasNext();) {
			Object added = iterator.next();
			if (added instanceof IJavaProject) {
				iterator.remove();
				convertedChildren.add(((IJavaProject) added).getProject());
			}
		}
		modification.getChildren().addAll(convertedChildren);
	}

	@SuppressWarnings("unchecked")
	private boolean convertToTigerstripeElements(
			PipelinedShapeModification modification) {
		Object[] elements = getElements(modification.getParent());
		for (Object element : elements) {
			if (element instanceof IDiagram) {
				boolean isRemoved = modification.getChildren().remove(
						((IDiagram) element).getDiagramFile());
				isRemoved &= modification.getChildren().remove(
						((IDiagram) element).getModelFile());
				if (!isRemoved) {
					modification.getChildren().clear();
				}
				modification.getChildren().add(element);
			}
			if (element instanceof IJavaProject) {
				boolean isRemoved = modification.getChildren().remove(
						((IJavaProject) element).getProject());
				if (isRemoved) {
					modification.getChildren().add(element);
				}
			} else if (element instanceof IDiagram) {
				modification.getChildren().remove(
						((IDiagram) element).getDiagramFile());
				modification.getChildren().remove(
						((IDiagram) element).getModelFile());
				modification.getChildren().add(element);
			}
		}
		if (modification.getParent() instanceof IProject) {
			Object[] tsProjects = getElements(((IProject) modification
					.getParent()).getParent());
			for (Object prj : tsProjects) {
				if (prj instanceof IJavaProject) {
					if (modification.getParent().equals(
							((IJavaProject) prj).getProject())) {
						modification.setParent(prj);
					}
				}
			}
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void postAdd(final Object parent, final Object element,
			Collection runnables) {
		if (parent instanceof IJavaModel)
			super.postAdd(((IJavaModel) parent).getWorkspace().getRoot(),
					element, runnables);
		else if (parent instanceof IJavaProject)
			super.postAdd(((IJavaProject) parent).getProject(), element,
					runnables);
		else
			super.postAdd(parent, element, runnables);
		update();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void postRefresh(final List toRefresh,
			final boolean updateLabels, Collection runnables) {
		for (Iterator iter = toRefresh.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (element instanceof IJavaModel) {
				iter.remove();
				toRefresh.add(((IJavaModel) element).getWorkspace().getRoot());
				super.postRefresh(toRefresh, updateLabels, runnables);
				return;
			}
		}
		super.postRefresh(toRefresh, updateLabels, runnables);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void postRemove(final Object element, Collection runnables) {
		super.postRemove(element, runnables);

		if (element instanceof IJavaProject) {
			super.postProjectStateChanged(((IJavaProject) element).getProject()
					.getParent(), runnables);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(
				ExplorerPreferencePage.P_LABEL_SHOW_RELATIONSHIP_ANCHORS)) {
			boolean show = EclipsePlugin
					.getDefault()
					.getPreferenceStore()
					.getBoolean(
							ExplorerPreferencePage.P_LABEL_SHOW_RELATIONSHIP_ANCHORS);
			setShowRelationshipAnchors(show);
			update();
		} else {
			super.propertyChange(event);
		}
	}

	private void update() {
		if (contentService != null) {
			contentService.update();
		}
	}

	@Override
	public Object getParent(Object element) {
		// if (element instanceof IProject) {
		// return ((IProject) element).getParent();
		// }
		if (element instanceof IJavaProject) {
			return asResource(element).getParent();
		}
		Object parent = super.getParent(element);
		// if (parent instanceof IJavaProject) {
		// return asResource(parent);
		// }
		return parent;
	}

	private IProject asResource(Object object) {
		return ((IJavaProject) object).getProject();
	}

}
