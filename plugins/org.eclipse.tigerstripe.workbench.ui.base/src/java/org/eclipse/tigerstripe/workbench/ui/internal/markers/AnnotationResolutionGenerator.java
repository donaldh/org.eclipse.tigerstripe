package org.eclipse.tigerstripe.workbench.ui.internal.markers;

import static org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants.ANNOTATION_MARKER_ID;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

/**
 * This class provide following marker resolutions:<br>
 * 1. Remove unresolved annotation<br>
 */
public class AnnotationResolutionGenerator implements
		IMarkerResolutionGenerator {

	public IMarkerResolution[] getResolutions(IMarker marker) {
		List<IMarkerResolution> resolutions = new ArrayList<IMarkerResolution>();
		try {
			if (ANNOTATION_MARKER_ID.equals(marker.getType())) {
				resolutions.add(new AnnotationMarkerRemoverAll(marker));
				resolutions.add(new AnnotationMarkerRemoverSingle(marker));
			}
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
		return resolutions.toArray(new IMarkerResolution[resolutions.size()]);
	}

}
