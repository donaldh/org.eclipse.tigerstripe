package org.eclipse.tigerstripe.workbench.ui.internal.markers;

import static org.eclipse.tigerstripe.annotation.core.AnnotationPackage.Literals.ANNOTATION__ID;
import static org.eclipse.tigerstripe.annotation.core.AnnotationPlugin.getManager;
import static org.eclipse.tigerstripe.annotation.core.Filters.eq;
import static org.eclipse.tigerstripe.annotation.core.Helper.firstOrNull;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.InTransaction;
import org.eclipse.tigerstripe.annotation.core.InTransaction.Operation;
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
			
			return firstOrNull(getManager().getSearcher().find(
					eq(ANNOTATION__ID, id)));
		} catch (Exception e) {
			BasePlugin.log(e);
			return null;
		}
	}

	
	@Override
	public void run(final IMarker[] markers, final IProgressMonitor monitor) {
		InTransaction.run(new Operation() {
			
			public void run() throws Throwable {
				AnnotationMarkerRemover.super.run(markers, monitor);
			}
		});
	}
	
	public void run(IMarker marker) {
		Annotation annotation = getAnnotation(marker);
		if (annotation != null) {
			AnnotationPlugin.getManager().doRemoveAnnotation(annotation);
		}
	}

	public String getDescription() {
		return getLabel();
	}
}
