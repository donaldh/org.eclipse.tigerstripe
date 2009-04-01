package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactFQRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactComponent;
import org.eclipse.tigerstripe.workbench.internal.core.model.Field;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method.Argument;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;

public class ChangeDeltaLabelProvider extends AbstractArtifactLabelProvider {

	private Image classDiagImg;

	private Image instanceDiagImg;

	@Override
	public Image getImage(Object element) {

		if (element instanceof ModelChangeDelta) {

			ModelChangeDelta delta = (ModelChangeDelta) element;
			return super.getImage(delta.getComponent());

		} else if (element instanceof DiagramChangeDelta) {

			DiagramChangeDelta delta = (DiagramChangeDelta) element;
			if ((delta.getAffDiagramHandle().getDiagramResource()
					.getFileExtension()).equals("wvd")) {

				ImageDescriptor desc = Images
						.getDescriptor(Images.VISUALEDITOR_ICON);
				classDiagImg = desc.createImage();
				return classDiagImg;

			} else if ((delta.getAffDiagramHandle().getDiagramResource()
					.getFileExtension()).equals("wod")) {

				ImageDescriptor desc = Images
						.getDescriptor(Images.INSTANCEEDITOR_ICON);
				instanceDiagImg = desc.createImage();
				return instanceDiagImg;
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {

		if (element instanceof ModelChangeDelta) {

			ModelChangeDelta delta = (ModelChangeDelta) element;
			if (delta.getComponent() instanceof ArtifactComponent) {

				if (delta.getComponent() instanceof AbstractArtifact) {

					if (delta.getFeature().equals(
							IArtifactSetFeatureRequest.RETURNED_TYPE)) {

						AbstractArtifact artifact = (AbstractArtifact) delta
								.getComponent();
						StringBuffer sb = new StringBuffer();
						sb.append(artifact.getName());
						sb.append(" return type moved/renamed from ");
						sb.append(delta.getOldValue());
						sb.append(" to ");
						sb.append(delta.getNewValue());
						return sb.toString();
					}

					StringBuffer sb = new StringBuffer();
					if (delta.getFeature().equals(
							IArtifactFQRenameRequest.FQN_FEATURE)) {
						sb.append(delta.getOldValue());
						sb.append(" moved/renamed to ");
						sb.append(delta.getNewValue());
					} else if (delta.getFeature().equals(
							IArtifactSetFeatureRequest.EXTENDS_FEATURE)) {
						AbstractArtifact artifact = (AbstractArtifact) delta
								.getComponent();
						sb.append(artifact.getFullyQualifiedName() + " ");
						sb.append("generalization ");
						sb.append(delta.getOldValue());
						sb.append(" moved/renamed to ");
						sb.append(delta.getNewValue());
					} else if ((delta.getFeature().equals(
							IArtifactSetFeatureRequest.ZEND) || (delta
							.getFeature()
							.equals(IArtifactSetFeatureRequest.AEND)))) {
						AbstractArtifact artifact = (AbstractArtifact) delta
								.getComponent();
						sb.append(artifact.getFullyQualifiedName() + " ");
						sb.append(delta.getFeature() + " ");
						sb.append("moved/renamed to ");
						sb.append(delta.getNewValue());
					} else if ((delta.getFeature()
							.equals(IArtifactAddFeatureRequest.IMPLEMENTS_FEATURE))) {
						AbstractArtifact artifact = (AbstractArtifact) delta
								.getComponent();
						sb.append(artifact.getFullyQualifiedName() + " ");
						sb.append("implemented interface ");
						sb.append(delta.getOldValue());
						sb.append(" moved/renamed to ");
						sb.append(delta.getNewValue());
					} else {
						sb.append(delta.toString());
					}

					return sb.toString();

				} else {

					ArtifactComponent component = (ArtifactComponent) delta
							.getComponent();

					if (component instanceof Field) {

						StringBuffer sb = new StringBuffer();

						IModelComponent mdlComponent = component
								.getContainingModelComponent();
						IAbstractArtifact artifact = (IAbstractArtifact) mdlComponent;

						sb.append("Attribute ");
						sb.append(artifact.getFullyQualifiedName() + "."
								+ component.getName());
						sb.append(" type moved/renamed from ");
						sb.append(delta.getOldValue());
						sb.append(" to ");
						sb.append(delta.getNewValue());
						return sb.toString();

					}

					if (delta.getFeature().equals(
							IMethodSetRequest.TYPE_FEATURE)) {

						StringBuffer sb = new StringBuffer();

						IModelComponent mdlComponent = component
								.getContainingModelComponent();
						IAbstractArtifact artifact = (IAbstractArtifact) mdlComponent;

						sb.append("Method ");
						sb.append(artifact.getFullyQualifiedName() + "."
								+ component.getName());
						sb.append(" return type moved/renamed from ");
						sb.append(delta.getOldValue());
						sb.append(" to ");
						sb.append(delta.getNewValue());
						return sb.toString();
					}
				}
			}

			// Method Argument is a special case
			if (delta.getComponent() instanceof Argument) {

				Argument arg = (Argument) delta.getComponent();
				StringBuffer sb = new StringBuffer();
				sb.append("Method ");
				sb.append(arg.getContainingArtifact().getFullyQualifiedName());
				sb.append(" argument type moved/renamed from ");
				sb.append(delta.getOldValue());
				sb.append(" to ");
				sb.append(delta.getNewValue());
				return sb.toString();
			}

		} else if (element instanceof DiagramChangeDelta) {

			DiagramChangeDelta delta = (DiagramChangeDelta) element;
			StringBuffer sb = new StringBuffer();
			sb.append("Moving ");
			sb.append(delta.getAffDiagramHandle().getModelResource()
					.getParent().getFullPath());
			sb.append(File.separator);
			sb.append(delta.getAffDiagramHandle().getModelResource().getName());
			sb.append(" to ");
			sb.append(delta.getDestinationPath());
			sb.append(File.separator);
			sb.append(delta.getAffDiagramHandle().getModelResource().getName());
			return sb.toString();
		}
		return null;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (classDiagImg != null) {
			classDiagImg.dispose();
			classDiagImg = null;
		}
		if (instanceDiagImg != null) {
			instanceDiagImg.dispose();
			instanceDiagImg = null;
		}
	}

}
