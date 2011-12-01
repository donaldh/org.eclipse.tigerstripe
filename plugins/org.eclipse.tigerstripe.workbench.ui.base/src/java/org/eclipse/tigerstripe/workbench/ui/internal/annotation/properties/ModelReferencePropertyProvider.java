package org.eclipse.tigerstripe.workbench.ui.internal.annotation.properties;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditableFeature;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditableListValue;
import org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue;

public class ModelReferencePropertyProvider implements EPropertyProvider {

	public EProperty getProperty(IEditableValue value) {
		if (value instanceof EditableFeature) {
			return createProperty(value, (EditableFeature) value);
		} else if (value instanceof EditableListValue) {
			return createProperty(value,
					((EditableListValue) value).getParent());
		}
		return null;
	}

	private EProperty createProperty(final IEditableValue value,
			final EditableFeature featureValue) {
		final EClass annotationClass = featureValue.getObject().eClass();
		final EStructuralFeature feature = featureValue.getFeature();
		if (ModelReferenceEditorRegistry.getInstance().getApplicable(
				annotationClass, feature)) {
			return new ModelReferenceProperty(value, annotationClass, feature);
		}
		return null;
	}
}
