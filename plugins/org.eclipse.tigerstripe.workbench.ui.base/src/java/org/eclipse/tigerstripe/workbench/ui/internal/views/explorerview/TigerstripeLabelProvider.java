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

import static org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener.ANNOTATION;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.navigator.IExtensionStateConstants.Values;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeChangeAdapter;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.event.LabelProviderUpdateHandler;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.event.UIEventGroup;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.internal.navigator.extensions.CommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.eclipse.ui.navigator.IExtensionStateModel;

/**
 * Tigerstripe label provider
 * 
 * @see TigerstripeExplorerLabelProvider
 */
@SuppressWarnings("restriction")
public class TigerstripeLabelProvider extends TigerstripeExplorerLabelProvider
		implements ICommonLabelProvider {

	private TigerstripeChangeAdapter tigerstripeChangeListener;
    private boolean disposed = false;

	private UIEventGroup<LabelProviderChangedEvent> eventGroup = new UIEventGroup<LabelProviderChangedEvent>(
			"Update Annotation Lables", new LabelProviderUpdateHandler(this) {

				@Override
				protected void fireEventImmediately(
						LabelProviderChangedEvent event) {
					if (!disposed) {
						getDelegeteLabelProvider().fireLabelProviderChanged(event);
					}
				}
			});
	
	private static final IActiveFacetChangeListener NULL_FACET_LISTENER = new IActiveFacetChangeListener() {
		public void facetChanged(IFacetReference oldFacet,
				IFacetReference newFacet) {
		}
	};

	private final long LABEL_FLAGS = JavaElementLabels.DEFAULT_QUALIFIED
			| JavaElementLabels.ROOT_POST_QUALIFIED
			| JavaElementLabels.APPEND_ROOT_PATH
			| JavaElementLabels.M_PARAMETER_TYPES
			| JavaElementLabels.M_PARAMETER_NAMES
			| JavaElementLabels.M_APP_RETURNTYPE
			| JavaElementLabels.M_EXCEPTIONS
			| JavaElementLabels.F_APP_TYPE_SIGNATURE
			| JavaElementLabels.T_TYPE_PARAMETERS;

	private final IResourceChangeListener resourceChangeListener = new IResourceChangeListener() {
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			if (delta == null) {
				return;
			}

			for (final IResourceDelta projectDelta : delta
					.getAffectedChildren(IResourceDelta.ADDED
							| IResourceDelta.CHANGED)) {

				final IProject project = (IProject) projectDelta.getResource();
				if (projectDelta.getKind() == IResourceDelta.ADDED
						|| projectDelta.getKind() == IResourceDelta.CHANGED) {
					// project descriptor added
					for (final IResourceDelta file : delta
							.getAffectedChildren(IResourceDelta.ADDED)) {
						if (isProjectDescriptor(file)) {
							addListenerIfNeed(project);
						}
					}

					// project descriptor removed
					for (final IResourceDelta file : delta
							.getAffectedChildren(IResourceDelta.REMOVED)) {
						if (isProjectDescriptor(file)) {
							removeProjectListener(project);
						}
					}
				}
			}

			// project added
			for (IResourceDelta child : delta
					.getAffectedChildren(IResourceDelta.ADDED)) {
				addListenerIfNeed((IProject) child.getResource());
			}

			// project removed
			for (IResourceDelta child : delta
					.getAffectedChildren(IResourceDelta.REMOVED)) {
				removeProjectListener((IProject) child.getResource());
			}
		}

		private boolean isProjectDescriptor(final IResourceDelta delta) {
			return ITigerstripeConstants.PROJECT_DESCRIPTOR.equals(delta
					.getResource().getName());
		}

		private void removeProjectListener(IResource resource) {
			synchronized (listenedProjects) {
				listenedProjects.remove(resource);
			}
		}
	};

	private final Map<IProject, IActiveFacetChangeListener> listenedProjects = new HashMap<IProject, IActiveFacetChangeListener>();

	public TigerstripeLabelProvider() {

		for (IProject project : EclipsePlugin.getWorkspace().getRoot()
				.getProjects()) {
			addListenerIfNeed(project);
		}
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
	}

	protected void addListenerIfNeed(final IProject project) {
		IActiveFacetChangeListener listener = listenedProjects.get(project);
		if (listener == null) {
			synchronized (listenedProjects) {
				listener = listenedProjects.get(project);
				if (listener == null) {
					ITigerstripeModelProject tp = (ITigerstripeModelProject) project
							.getAdapter(ITigerstripeModelProject.class);
					if (tp == null) {
						listenedProjects.put(project, NULL_FACET_LISTENER);
					} else {
						listener = new IActiveFacetChangeListener() {
							public void facetChanged(IFacetReference oldFacet,
									IFacetReference newFacet) {

								Display.getDefault().asyncExec(new Runnable() {
									public void run() {
										LabelProviderChangedEvent event = new LabelProviderChangedEvent(
												TigerstripeLabelProvider.this);
										getDelegeteLabelProvider().fireLabelProviderChanged(event);
									}
								});
							}
						};
						listenedProjects.put(project, listener);
						try {
							tp.getArtifactManagerSession()
									.addActiveFacetListener(listener);
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					}
				}
			}
		}
	}

	private IExtensionStateModel fStateModel;

	private IPropertyChangeListener fLayoutPropertyListener;

	public void init(ICommonContentExtensionSite commonContentExtensionSite) {
		super.init(commonContentExtensionSite);
		fStateModel = ((CommonContentExtensionSite) commonContentExtensionSite)
				.getContentService().findStateModel(
						"org.eclipse.jdt.java.ui.javaContent");

		setIsFlatLayout(fStateModel.getBooleanProperty(Values.IS_LAYOUT_FLAT));
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
		addTigerstripeListener();
	}

	private void addTigerstripeListener() {
		tigerstripeChangeListener = new TigerstripeChangeAdapter() {

			@Override
			public void artifactResourceAdded(IResource addedArtifactResource) {
//				artifactResourceChanged(addedArtifactResource);
			}

			@Override
			public void artifactResourceChanged(IResource resource) {
//				eventGroup.post(new LabelProviderChangedEvent(
//						getDelegeteLabelProvider(), resource));
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
					} else if (component != null) {
						elementsToRefresh.add(component);
					}
				}
				if (!elementsToRefresh.isEmpty()) {
					for (Object o : elementsToRefresh) {
						eventGroup.post(new LabelProviderChangedEvent(
								getDelegeteLabelProvider(), o));
					}
				}
			}
		};

		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(
				tigerstripeChangeListener, ANNOTATION);
		
	}

	@Override
	public void dispose() {
		disposed = true; 
		super.dispose();
		if (fStateModel != null) {
			fStateModel.removePropertyChangeListener(fLayoutPropertyListener);
		}
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(
				resourceChangeListener);
		synchronized (listenedProjects) {
			for (Entry<IProject, IActiveFacetChangeListener> entry : listenedProjects
					.entrySet()) {
				if (NULL_FACET_LISTENER.equals(entry.getValue())) {
					continue;
				}
				ITigerstripeModelProject tp = (ITigerstripeModelProject) entry
						.getKey().getAdapter(ITigerstripeModelProject.class);
				if (tp != null) {
					try {
						IArtifactManagerSession artifactManagerSession = tp
								.getArtifactManagerSession();
						if (!artifactManagerSession.getArtifactManager()
								.wasDisposed()) {
							artifactManagerSession
									.removeActiveFacetListener(entry.getValue());
						}
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
		if (tigerstripeChangeListener != null) {
			TigerstripeWorkspaceNotifier.INSTANCE.removeTigerstripeChangeListener(tigerstripeChangeListener);
		}
	}

	public void restoreState(IMemento aMemento) {
	}

	public void saveState(IMemento aMemento) {
	}

	public String getDescription(Object element) {
		if (element instanceof IJavaElement) {
			return formatJavaElementMessage((IJavaElement) element);
		} else if (element instanceof IResource) {
			return formatResourceMessage((IResource) element);
		}
		return ""; //$NON-NLS-1$
	}

	private String formatJavaElementMessage(IJavaElement element) {
		return JavaElementLabels.getElementLabel(element, LABEL_FLAGS);
	}

	private String formatResourceMessage(IResource element) {
		IContainer parent = element.getParent();
		if (parent != null && parent.getType() != IResource.ROOT)
			return BasicElementLabels.getResourceName(element.getName())
					+ JavaElementLabels.CONCAT_STRING
					+ BasicElementLabels.getPathLabel(parent.getFullPath(),
							false);
		else
			return BasicElementLabels.getResourceName(element.getName());
	}

}