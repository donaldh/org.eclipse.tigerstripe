package org.eclipse.tigerstripe.workbench.ui.internal.markers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants;
import org.eclipse.ui.views.markers.WorkbenchMarkerResolution;

/**
 * Base class for annotation marker resolution
 */
public abstract class AnnotationMarkerResolution extends
		WorkbenchMarkerResolution {

	private IMarker marker;

	public AnnotationMarkerResolution(IMarker marker) {
		this.marker = marker;
	}

	@Override
	public IMarker[] findOtherMarkers(IMarker[] markers) {
		List<IMarker> result = new ArrayList<IMarker>();
		// Collect all markers which can be resolved by this marker resolution
		for (IMarker marker : markers) {
			if (this.marker == marker)
				continue;
			if (canResolve(marker)) {
				result.add(marker);
			}
		}
		return result.toArray(new IMarker[result.size()]);
	}

	/**
	 * @param marker
	 * @return true if this marker resolution can resolve specified marked and
	 *         false otherwise
	 */
	public boolean canResolve(IMarker marker) {
		try {
			if (BuilderConstants.ANNOTATION_MARKER_ID.equals(marker.getType())) {
				Annotation annotation = getAnnotation(marker);
				return canResolve(annotation, marker);
			}
		} catch (Exception e) {
		}
		return false;
	}

	protected boolean canResolve(Annotation annotation, IMarker marker) {
		return annotation != null;
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

}
