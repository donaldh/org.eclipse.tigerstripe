package org.eclipse.tigerstripe.workbench.ui.internal.markers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.markers.WorkbenchMarkerResolution;

/**
 * This marker resolution resolve marker by migrating annotation to modelId
 */
public class AnnotationMarkerResolver extends WorkbenchMarkerResolution {

	private IMarker marker;

	public AnnotationMarkerResolver(IMarker marker) {
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
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_LCL_LINKTO_HELP);
	}

	public static URI getNewURI(IMarker marker, URI uri) {
		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) marker
				.getResource().getProject()
				.getAdapter(ITigerstripeModelProject.class);
		String[] segments = uri.segments();
		try {
			segments[0] = tsProject.getModelId();
		} catch (Exception e) {
			EclipsePlugin.log(e);
		}
		return URI.createHierarchicalURI(uri.scheme(), uri.authority(),
				uri.device(), segments, uri.query(), uri.fragment());
	}

}
