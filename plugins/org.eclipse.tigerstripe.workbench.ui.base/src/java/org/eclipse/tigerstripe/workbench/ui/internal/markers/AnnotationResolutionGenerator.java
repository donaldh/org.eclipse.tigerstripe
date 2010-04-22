package org.eclipse.tigerstripe.workbench.ui.internal.markers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

/**
 * This class provide two marker resolutions:<br>
 * 1. Remove unresolved annotation<br>
 * 2. Resolve unresolved annotation (if possible)
 */
public class AnnotationResolutionGenerator implements
		IMarkerResolutionGenerator {

	public IMarkerResolution[] getResolutions(IMarker marker) {
		List<IMarkerResolution> resolutions = new ArrayList<IMarkerResolution>();
		AnnotationMarkerResolver resolver = new AnnotationMarkerResolver(marker);
		if (resolver.canResolve(marker)) {
			resolutions.add(resolver);
		}
		AnnotationMarkerRemover remover = new AnnotationMarkerRemover(marker);
		if (remover.canResolve(marker)) {
			resolutions.add(remover);
		}
		return resolutions.toArray(new IMarkerResolution[resolutions.size()]);
	}

}
