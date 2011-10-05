package org.eclipse.tigerstripe.workbench.ui.internal.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * This marker resolution resolve marker by migrating annotation to modelId
 */
public class AnnotationMarkerResolver extends AnnotationMarkerResolution {

	public AnnotationMarkerResolver(IMarker marker) {
		super(marker);
	}

	public void run(IMarker marker) {
		Annotation annotation = getAnnotation(marker);
		if (annotation != null) {
			URI uri = getNewURI(marker, annotation.getUri());
			annotation.setUri(uri);
			AnnotationPlugin.getManager().save(annotation);
		}
		try {
			marker.delete();
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
	}

	@Override
	protected boolean canResolve(Annotation annotation, IMarker marker) {
		if (annotation != null) {
			URI newURI = getNewURI(marker, annotation.getUri());
			if (AnnotationPlugin.getManager().getAnnotatedObject(newURI) != null) {
				return true;
			}
		}
		return false;
	}

	public String getLabel() {
		return "Reassign annotations to valid elements";
	}

	public String getDescription() {
		return getLabel();
	}

	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_LCL_LINKTO_HELP);
	}

	public static URI getNewURI(IMarker marker, URI uri) {
		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) marker
				.getResource().getProject().getAdapter(
						ITigerstripeModelProject.class);
		String[] segments = uri.segments();
		try {
			segments[0] = tsProject.getModelId();
		} catch (Exception e) {
			EclipsePlugin.log(e);
		}
		return URI.createHierarchicalURI(uri.scheme(), uri.authority(), uri
				.device(), segments, uri.query(), uri.fragment());
	}

}
