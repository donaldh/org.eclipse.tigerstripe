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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJarEntryResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.navigator.IExtensionStateConstants.Values;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeChangeAdapter;
import org.eclipse.tigerstripe.workbench.diagram.IDiagram;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.preferences.ExplorerPreferencePage;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.internal.navigator.extensions.CommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.IExtensionStateModel;
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

	private AbstractTreeViewer viewer;

	private TigerstripeChangeAdapter tigerstripeChangeListener;
	
	private void asyncExec(final Runnable r)  {
		final Control control = viewer.getControl();
		if(viewer != null && !control.isDisposed()) {
			control.getDisplay().asyncExec(new Runnable() {
				
				public void run() {
					if(viewer != null && !control.isDisposed() && !viewer.isBusy()) {
						r.run();
					}
				}
			});
		}
	}

	@Override
	public void init(ICommonContentExtensionSite commonContentExtensionSite) {
		IExtensionStateModel stateModel = ((CommonContentExtensionSite) commonContentExtensionSite)
				.getContentService().findStateModel(
						"org.eclipse.jdt.java.ui.javaContent");
		IMemento memento = commonContentExtensionSite.getMemento();
		fStateModel = stateModel;

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

		tigerstripeChangeListener = new TigerstripeChangeAdapter() {

			@Override
			public void artifactResourceAdded(IResource addedArtifactResource) {
				artifactResourceChanged(addedArtifactResource);
			}

			@Override
			public void artifactResourceChanged(final IResource resource) {
				asyncExec(new Runnable() {
					public void run() {
						viewer.refresh(resource, true);
					}
				});
			}

			@Override
			public void annotationChanged(IModelAnnotationChangeDelta[] deltas) {
				final Set<Object> elementsToRefresh = new HashSet<Object>();
				for (IModelAnnotationChangeDelta delta : deltas) {
					URI uri = delta.getAffectedModelComponentURI();
					IModelComponent component = TigerstripeURIAdapterFactory
							.uriToComponent(uri);
					if (component instanceof IContextProjectAware) {
						ElementWrapper wrapper = new ElementWrapper(
								component,
								((IContextProjectAware) component)
										.getContextProject());
						elementsToRefresh.add(wrapper);
					} else if (component instanceof IAbstractArtifact) {
						IJavaElement element = (IJavaElement) component
								.getAdapter(IJavaElement.class);
						if (element != null) {
							elementsToRefresh.add(element);
						}
					} else {
						elementsToRefresh.add(component);
					}
				}
				if (!elementsToRefresh.isEmpty()) {
					final Object[] toRefresh = elementsToRefresh.toArray();
					asyncExec(new Runnable() {
						public void run() {
							for(final Object o : toRefresh) {
								viewer.refresh(o, true);
							}
						}
					});
				}
			}
		};

		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(
				tigerstripeChangeListener,
				ITigerstripeChangeListener.ARTIFACT_RESOURCES
						| ITigerstripeChangeListener.ANNOTATION);
	}

	@Override
	public void dispose() {
		try {
			super.dispose();
		} catch (Exception e) {
			// ignore any disposing errors
		}
		if (fStateModel != null) {
			fStateModel.removePropertyChangeListener(fLayoutPropertyListener);
		}
		EclipsePlugin.getDefault().getPreferenceStore()
				.removePropertyChangeListener(this);

		if (tigerstripeChangeListener != null) {
			TigerstripeWorkspaceNotifier.INSTANCE
					.removeTigerstripeChangeListener(tigerstripeChangeListener);
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof IWorkspaceRoot) {
			return getTigerstripeProjects();
		} else {
			return super.getElements(inputElement);
		}
	}

	private void clearCompilationUnit(Set<Object> proposedChildren) {
		boolean isCompilationUnitChildren = true;
		for (Object object : proposedChildren) {
			if (!(object instanceof IJavaElement)
					|| !((((IJavaElement) object).getParent() instanceof ICompilationUnit)
					|| ((IJavaElement) object).getParent() instanceof IClassFile)) {
				if (object instanceof IJarEntryResource) {
					if (((IJarEntryResource) object).getName().endsWith(
							".package")) {
						continue;
					}
				}
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
			if (element != null) {
				proposedChildren.clear();
				proposedChildren.addAll(Arrays.asList(elements));
				return;
			}
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getPipelinedChildren(Object aParent, Set theCurrentChildren) {
		customize(getChildren(aParent), theCurrentChildren);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getPipelinedElements(Object anInput, Set theCurrentElements) {
		customize(getElements(anInput), theCurrentElements);
	}

	@Override
	public Object getPipelinedParent(Object anObject, Object aSuggestedParent) {
		return getParent(anObject);
	}

	@Override
	public PipelinedShapeModification interceptAdd(
			PipelinedShapeModification anAddModification) {
		Object parent = anAddModification.getParent();

		if (parent instanceof IProject) {
			anAddModification.setParent(parent);
		}

		if (parent instanceof IWorkspaceRoot) {
			deconvertTigerstripeProjects(anAddModification);
		}

		convertToTigerstripeElements(anAddModification);
		return anAddModification;
	}

	@Override
	public PipelinedShapeModification interceptRemove(PipelinedShapeModification remove) {
		final Set<Object> filtered = new HashSet<Object>();
		for(final Object o : remove.getChildren()) {
			filter(o, remove.getParent(), filtered);
		}
		remove.getChildren().clear();
		remove.getChildren().addAll(filtered);
		return remove;
	}

	@Override
	public void restoreState(IMemento aMemento) {
	}

	@Override
	public void saveState(IMemento aMemento) {
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void deconvertTigerstripeProjects(
			PipelinedShapeModification modification) {
		Set convertedChildren = new LinkedHashSet();
		for (Iterator iterator = modification.getChildren().iterator(); iterator
				.hasNext();) {
			Object added = iterator.next();
			if (added instanceof IProject) {
				iterator.remove();
				convertedChildren.add(added);
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
			if (element instanceof IProject) {
				boolean isRemoved = modification.getChildren().remove(element);
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
				if (prj instanceof IProject) {
					if (modification.getParent().equals(prj)) {
						modification.setParent(prj);
					}
				}
			}
		}
		return true;
	}

	@Override
	protected void postAdd(final Object parent, final Object element,
			Collection runnables) {
		if (parent instanceof IJavaModel)
			super.postAdd(((IJavaModel) parent).getWorkspace().getRoot(),
					element, runnables);
		else if (parent instanceof IJavaProject)
			super.postAdd(((IJavaProject)parent).getProject(), element, runnables);
		else
			super.postAdd(parent, element, runnables);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	@SuppressWarnings("rawtypes")
	protected void postRemove(final Object element, Collection runnables) {
		super.postRemove(element, runnables);

		if (element instanceof IProject) {
			super.postProjectStateChanged(((IProject) element).getParent(),
					runnables);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(
				ExplorerPreferencePage.P_LABEL_STEREO_ARTIFACT)
				|| event.getProperty().equals(
						ExplorerPreferencePage.P_LABEL_STEREO_ATTR)
				|| event.getProperty().equals(
						ExplorerPreferencePage.P_LABEL_STEREO_METH)
				|| event.getProperty().equals(
						ExplorerPreferencePage.P_LABEL_STEREO_METHARGS)
				|| event.getProperty().equals(
						ExplorerPreferencePage.P_LABEL_STEREO_LIT)
				|| event.getProperty().equals(
						ExplorerPreferencePage.P_LABEL_STEREO_END)) {
			viewer.refresh(true);
		} else if (event.getProperty().equals(
				ExplorerPreferencePage.P_LABEL_SHOW_RELATIONSHIP_ANCHORS)) {
			boolean show = EclipsePlugin
					.getDefault()
					.getPreferenceStore()
					.getBoolean(
							ExplorerPreferencePage.P_LABEL_SHOW_RELATIONSHIP_ANCHORS);
			setShowRelationshipAnchors(show);
			viewer.refresh(true);
		} else {
			super.propertyChange(event);
		}
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		if (viewer instanceof AbstractTreeViewer) {
			this.viewer = (AbstractTreeViewer) viewer;
		} else {
			this.viewer = null;
		}
	}

}
