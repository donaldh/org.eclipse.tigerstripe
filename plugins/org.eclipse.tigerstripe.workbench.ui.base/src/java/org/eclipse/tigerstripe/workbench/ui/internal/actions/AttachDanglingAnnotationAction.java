package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import static org.eclipse.emf.ecore.util.EcoreUtil.copy;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

public class AttachDanglingAnnotationAction extends Action {

	private final Annotation annotation;
	private final IResource resource;
	private final IMarker marker;

	public AttachDanglingAnnotationAction(IMarker marker, Annotation annotation, IResource resource) {
		this.marker = marker;
		this.annotation = annotation;
		this.resource = resource;
		AnnotationType type = AnnotationPlugin.getManager().getType(annotation);
		ILabelProvider labelProvider = AnnotationUIPlugin.getManager().getLabelProvider(type);
		URI uri = annotation.getUri();
		if (labelProvider != null) {
			String text = labelProvider.getText(annotation.getContent());
			if (text != null) {
				setText(text, uri);
			} else {
				setText(type.getName(), uri);
			}
		} else {
			setText(type.getName(), uri);
		}
	}
	
	public void setText(String text, URI uri) {
		super.setText(text + " (" + uri + ")");
	}
	
	@Override
	public void run() {
		try {
			AnnotationPlugin.getManager().addAnnotation(resource, copy(annotation.getContent()));
		} catch (AnnotationException e) {
			EclipsePlugin.log(e);
		}
		AnnotationPlugin.getManager().removeAnnotation(annotation);
		try {
			marker.delete();
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
	}
}
