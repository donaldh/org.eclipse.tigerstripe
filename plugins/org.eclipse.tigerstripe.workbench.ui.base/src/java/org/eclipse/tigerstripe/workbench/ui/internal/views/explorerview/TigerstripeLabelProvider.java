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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.navigator.IExtensionStateConstants.Values;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
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

	private final IResourceChangeListener resourceChangeListener;
	private final Map<IProject, IActiveFacetChangeListener> listenedProjects = new HashMap<IProject, IActiveFacetChangeListener>();

	public TigerstripeLabelProvider() {
		super(new TigerstripeContentProvider());

		for (IProject project : EclipsePlugin.getWorkspace().getRoot()
				.getProjects()) {
			addListenerIfNeed(project);
		}
		resourceChangeListener = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				IResourceDelta delta = event.getDelta();
				if (delta != null) {
					IResourceDelta[] children = delta
							.getAffectedChildren(IResourceDelta.ADDED);
					for (IResourceDelta child : children) {
						IResource resource = child.getResource();
						if (resource instanceof IProject) {
							addListenerIfNeed((IProject) resource);
						}
					}
				}
			}
		};
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
	}

	@SuppressWarnings("deprecation")
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
								LabelProviderChangedEvent event = new LabelProviderChangedEvent(
										TigerstripeLabelProvider.this, project);
								fireLabelProviderChanged(event);
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
	}

	@Override
	@SuppressWarnings("deprecation")
	public void dispose() {
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
						tp.getArtifactManagerSession()
								.removeActiveFacetListener(entry.getValue());
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
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
