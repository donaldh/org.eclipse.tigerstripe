package org.eclipse.tigerstripe.annotation.example.ui;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.annotation.properties.ModelReferenceCellEditor;

public class BackwardsCompatibleCellEditor extends ModelReferenceCellEditor {

	@Override
	protected IModelComponent[] valueToComponents(Object value) {
		if (value == null) {
			return NO_COMPONENTS;
		}

		final String string = ((String) value).trim();
		if (string.isEmpty()) {
			return NO_COMPONENTS;
		}

		final String entityFqn = string.split(":")[0];
		final String fieldName = string.split(":")[1];

		try {
			ITigerstripeModelProject project = getAnnotatedObject()
					.getProject();
			IArtifactManagerSession session = project
					.getArtifactManagerSession();

			IAbstractArtifact found = null;
			IQueryAllArtifacts query = (IQueryAllArtifacts) session
					.makeQuery(IQueryAllArtifacts.class.getName());
			for (final IAbstractArtifact artifact : session
					.queryArtifact(query)) {
				if (artifact.getFullyQualifiedName().equals(entityFqn)) {
					found = artifact;
					break;
				}
			}
			if (found == null) {
				return NO_COMPONENTS;
			}

			for (final IField field : found.getFields()) {
				if (field.getName().equals(fieldName)) {
					return new IModelComponent[] { field };
				}
			}
			return NO_COMPONENTS;

		} catch (TigerstripeException te) {
			EclipsePlugin.log(te);
			return NO_COMPONENTS;
		}
	}

	@Override
	protected Object componentToValue(IModelComponent component) {
		final IField field = (IField)component;
		return field.getContainingArtifact().getFullyQualifiedName() + ":" + field.getName();
	}
}
