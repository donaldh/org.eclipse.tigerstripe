package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.viewsupport.ImageDescriptorRegistry;
import org.eclipse.jdt.internal.ui.viewsupport.ImageImageDescriptor;
import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.jdt.ui.ProblemsLabelDecorator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.viewers.ITigerstripeLabelDecorator;

@SuppressWarnings("restriction")
public class TigerstripeProblemsLabelDecorator extends LabelProvider implements
		ITigerstripeLabelDecorator {

	private JavaProblemsLabelDecorator delegate = new JavaProblemsLabelDecorator();

	public Image decorateImage(Image image, IModelComponent component) {
		return decorateImage(image, (Object) component);
	}

	public String decorateText(String text, IModelComponent component) {
		return decorateText(text, (Object) component);
	}

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

			int ticks = getErrorTicksFromMarkers(res, uri);
			if (ticks != JavaElementImageDescriptor.ERROR) {
				IJavaElement javaElement = (IJavaElement) Platform
						.getAdapterManager().getAdapter(model,
								IJavaElement.class);
				ticks = Math.max(ticks, getErrorTicksFromMarkers(javaElement));
			}
			return ticks;
		}
		return 0;
	}

	protected ImageDescriptorRegistry getRegistry() {
		return JavaPlugin.getImageDescriptorRegistry();
	}

	protected int getErrorTicksFromMarkers(IJavaElement element) {
		return delegate.computeJavaAdornmentFlags(element);
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

	private static class JavaProblemsLabelDecorator extends
			ProblemsLabelDecorator {
		public int computeJavaAdornmentFlags(IJavaElement element) {
			return computeAdornmentFlags(element);
		}
	}

}
