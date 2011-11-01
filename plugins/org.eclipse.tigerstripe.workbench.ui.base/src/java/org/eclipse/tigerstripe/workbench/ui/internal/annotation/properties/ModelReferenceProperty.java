package org.eclipse.tigerstripe.workbench.ui.internal.annotation.properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyImpl;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditableFeature;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditableListValue;
import org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue;
import org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.annotation.properties.ModelReferenceCellEditor;

public class ModelReferenceProperty extends EPropertyImpl {
	private final EClass annotationClass;
	private final EStructuralFeature feature;

	public ModelReferenceProperty(final IEditableValue value,
			final EClass annotationClass, final EStructuralFeature feature) {
		super(value);
		this.annotationClass = annotationClass;
		this.feature = feature;
	}

	@Override
	protected CellEditor createEditor(Composite parent) {
		try {
			ModelReferenceCellEditor editor = ModelReferenceEditorRegistry
					.getInstance().createCellEditor(annotationClass, feature);
			editor.setFeature(feature);
			editor.setAnnotatedObject(getAnnotatedObject());
			editor.setInitialSelection(super.getValue());
			editor.create(parent);
			return editor;
		} catch (CoreException e) {
			EclipsePlugin.log(e);
			return null;
		}
	}

	private ModelReference getModelReference() {
		Object value = super.getValue();
		if (value instanceof ModelReference) {
			return (ModelReference) value;
		}
		return null;
	}

	@Override
	public Object getValue() {
		ModelReference reference = getModelReference();
		if (reference != null) {
			return reference.getUri();
		}
		return super.getValue();
	}

	@Override
	public String getDisplayName() {
		ModelReference reference = getModelReference();
		if (reference != null) {
			String sUri = reference.getUri();
			if (sUri != null) {
				return uriToLabel(sUri);
			}
		}
		return super.getDisplayName();
	}

	private IModelComponent getAnnotatedObject() {
		EObject container = null;
		if (value instanceof EditableFeature) {
			container = ((EditableFeature) value).getObject().eContainer();
		} else if (value instanceof EditableListValue) {
			container = ((EditableListValue) value).getParent().getObject()
					.eContainer();
		}
		if (container instanceof Annotation) {
			Object o = AnnotationPlugin.getManager().getAnnotatedObject(
					(Annotation) container);
			if (o instanceof IModelComponent) {
				return (IModelComponent) o;
			}
		}

		return null;
	}

	private String uriToLabel(String sUri) {
		URI uri = URI.createURI(sUri);
		StringBuilder result = new StringBuilder(uri.lastSegment());
		if (uri.hasFragment()) {
			result.append(":");
			result.append(uri.fragment());
		}
		return result.toString();
	}
}
