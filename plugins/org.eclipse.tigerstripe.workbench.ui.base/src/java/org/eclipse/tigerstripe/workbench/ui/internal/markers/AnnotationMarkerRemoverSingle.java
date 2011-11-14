package org.eclipse.tigerstripe.workbench.ui.internal.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class AnnotationMarkerRemoverSingle extends AnnotationMarkerRemover {

	public AnnotationMarkerRemoverSingle(IMarker marker) {
		super(marker);
	}

	private static final IMarker[] EMPTY = new IMarker[0];

	@Override
	public IMarker[] findOtherMarkers(IMarker[] markers) {
		return EMPTY;
	}

	public String getLabel() {
		return "Remove Unresolved Annotation";
	}

	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_ELCL_REMOVE);
	}
}
