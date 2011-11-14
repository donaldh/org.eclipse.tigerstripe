package org.eclipse.tigerstripe.workbench.ui.internal.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants;
import org.eclipse.ui.views.markers.WorkbenchMarkerResolution;

/**
 * This marker resolution remove unresolved annotation
 */
public abstract class AnnotationMarkerRemover extends WorkbenchMarkerResolution {

	protected IMarker marker;

	public AnnotationMarkerRemover(IMarker marker) {
		this.marker = marker;
	}

	/**
	 * @param marker
	 * @return annotation from specified marker and null if no annotation found
	 */
	public static Annotation getAnnotation(IMarker marker) {
		try {
			String id = (String) marker
					.getAttribute(BuilderConstants.ANNOTATION_ID);
			return AnnotationPlugin.getManager().getAnnotationById(id);
		} catch (Exception e) {
			return null;
		}
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

	public String getDescription() {
		return getLabel();
	}
}
