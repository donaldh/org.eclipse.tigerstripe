package org.eclipse.tigerstripe.workbench.ui.internal.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * This marker resolution remove unresolved annotation
 */
public class AnnotationMarkerRemover extends AnnotationMarkerResolution {

	public AnnotationMarkerRemover(IMarker marker) {
		super(marker);
	}

	public void run(IMarker marker) {
		Annotation annotation = getAnnotation(marker);
		if (annotation != null) {
			AnnotationPlugin.getManager().removeAnnotation(annotation);
		}
		try {
			marker.delete();
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
	}

	public String getLabel() {
		return "Remove unresolved annotations";
	}

	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_ELCL_REMOVEALL);
	}

	public String getDescription() {
		return getLabel();
	}
}
