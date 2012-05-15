package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.viewsupport.IProblemChangedListener;
import org.eclipse.jdt.internal.ui.viewsupport.ImageDescriptorRegistry;
import org.eclipse.jdt.internal.ui.viewsupport.ImageImageDescriptor;
import org.eclipse.jdt.internal.ui.viewsupport.ProblemMarkerManager;
import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

@SuppressWarnings("restriction")
public class TigerstripeProblemsLabelDecorator extends LabelProvider implements
		ILabelDecorator, ILightweightLabelDecorator {

	public void decorate(Object element, IDecoration decoration) {
		int adornmentFlags = computeAdornmentFlags(element);
		if (adornmentFlags == JavaElementImageDescriptor.ERROR) {
			decoration.addOverlay(JavaPluginImages.DESC_OVR_ERROR);
		} else if (adornmentFlags == JavaElementImageDescriptor.WARNING) {
			decoration.addOverlay(JavaPluginImages.DESC_OVR_WARNING);
		}
	}

	public Image decorateImage(Image image, Object element) {
		if (image == null)
			return null;

		int adornmentFlags = computeAdornmentFlags(element);
		if (adornmentFlags != 0) {
			ImageDescriptor baseImage = new ImageImageDescriptor(image);
			Rectangle bounds = image.getBounds();

			image = getRegistry().get(
					new JavaElementImageDescriptor(baseImage, adornmentFlags,
							new Point(bounds.width, bounds.height)));
		}
		return image;
	}

	public String decorateText(String text, Object element) {
		return text;
	}

	protected int computeAdornmentFlags(Object element) {
		if (element instanceof IMember && element instanceof IModelComponent) {
			IMember member = (IMember) element;
			IResource res = (IResource) member.getContainingArtifact()
					.getAdapter(IResource.class);

			IModelComponent model = (IModelComponent) element;
			URI uri = (URI) model.getAdapter(URI.class);

			return getErrorTicksFromMarkers(res, uri);
		}
		return 0;
	}

	protected ImageDescriptorRegistry getRegistry() {
		return JavaPlugin.getImageDescriptorRegistry();
	}

	protected int getErrorTicksFromMarkers(IResource res, URI emfUri) {
		if (res == null || !res.isAccessible() || emfUri == null) {
			return 0;
		}

		final IMarker[] markers;
		try {
			markers = res.findMarkers(null, true, IResource.DEPTH_ZERO);
		} catch (CoreException ex) {
			EclipsePlugin.logErrorMessage(
					"Can't get markers for " + res.getLocationURI(), ex);
			return 0;
		}

		int severity = 0;
		for (IMarker marker : markers) {
			if (isOwner(emfUri, marker)) {
				int markerSeverity = getSeverity(marker);
				if (severity < markerSeverity) {
					severity = markerSeverity;
				}
			}
		}

		if (severity == IMarker.SEVERITY_ERROR) {
			return JavaElementImageDescriptor.ERROR;
		} else if (severity == IMarker.SEVERITY_WARNING) {
			return JavaElementImageDescriptor.WARNING;
		}

		return 0;
	}

	protected boolean isOwner(URI emfUri, IMarker marker) {
		return emfUri.equals(getUri(marker));
	}

	protected int getSeverity(IMarker marker) {
		return marker.getAttribute(IMarker.SEVERITY, -1);
	}

	protected URI getUri(IMarker marker) {
		String location = marker.getAttribute(IMarker.LOCATION, (String) null);
		return location == null ? null : URI.createURI(location, true);
	}

	private IProblemChangedListener fProblemChangedListener;

	@Override
	public void addListener(ILabelProviderListener listener) {
		super.addListener(listener);
		if (fProblemChangedListener == null) {
			fProblemChangedListener = new IProblemChangedListener() {
				public void problemsChanged(IResource[] changedResources,
						boolean isMarkerChange) {
					fireProblemsChanged(changedResources);
				}
			};
			getProblemMarkerManager().addListener(fProblemChangedListener);
		}
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		super.removeListener(listener);
		if (getListeners().length == 0 && fProblemChangedListener != null) {
			getProblemMarkerManager().removeListener(fProblemChangedListener);
			fProblemChangedListener = null;
		}
	}

	@Override
	public void dispose() {
		if (fProblemChangedListener != null) {
			getProblemMarkerManager().removeListener(fProblemChangedListener);
			fProblemChangedListener = null;
		}
		super.dispose();
	}

	private ProblemMarkerManager getProblemMarkerManager() {
		return JavaPlugin.getDefault().getProblemMarkerManager();
	}

	protected void fireProblemsChanged(IResource[] changedResources) {
		Set<IMember> newInvalidMembers = new HashSet<IMember>();
		for (IResource resource : changedResources) {
			final IMarker[] markers;
			try {
				markers = resource
						.findMarkers(null, true, IResource.DEPTH_ZERO);
				for (IMarker marker : markers) {
					URI uri = getUri(marker);
					if (uri != null) {
						IModelComponent model = TigerstripeURIAdapterFactory
								.uriToComponent(uri);
						if (model instanceof IMember) {
							newInvalidMembers.add((IMember) model);
						}
					}
				}
			} catch (CoreException ex) {
				EclipsePlugin.logErrorMessage("Can't get markers for "
						+ resource.getLocationURI(), ex);
			}
		}

		invalidMembers.addAll(newInvalidMembers);
		if (invalidMembers.size() > 0) {
			fireLabelProviderChanged(new LabelProviderChangedEvent(this,
					invalidMembers.toArray()));
		}
		invalidMembers = newInvalidMembers;
	}

	Set<IMember> invalidMembers = new HashSet<IMember>();

}
