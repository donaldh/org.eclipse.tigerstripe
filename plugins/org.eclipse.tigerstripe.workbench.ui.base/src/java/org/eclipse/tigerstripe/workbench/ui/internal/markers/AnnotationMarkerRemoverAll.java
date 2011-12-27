package org.eclipse.tigerstripe.workbench.ui.internal.markers;

import static org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants.ANNOTATION_MARKER_ID;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class AnnotationMarkerRemoverAll extends AnnotationMarkerRemover {

	public AnnotationMarkerRemoverAll(IMarker marker) {
		super(marker);
	}

	@Override
	public IMarker[] findOtherMarkers(IMarker[] markers) {
		List<IMarker> result = new ArrayList<IMarker>();
		// Collect all markers which can be resolved by this marker resolution
		for (IMarker marker : markers) {
			if (this.marker == marker)
				continue;
			try {
				if (ANNOTATION_MARKER_ID.equals(marker.getType())) {
					result.add(marker);
				}
			} catch (CoreException e) {
				BasePlugin.log(e);
			}
		}
		return result.toArray(new IMarker[result.size()]);
	}
	
	public String getLabel() {
		return "Remove Unresolved Annotations";
	}

	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_ELCL_REMOVEALL);
	}
}
