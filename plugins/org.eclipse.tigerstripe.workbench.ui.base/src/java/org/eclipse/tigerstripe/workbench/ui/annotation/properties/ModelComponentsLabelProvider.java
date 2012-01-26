package org.eclipse.tigerstripe.workbench.ui.annotation.properties;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.internal.core.model.NullAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.RelationshipAnchor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ModelComponentsLabelProvider extends LabelProvider {

	private final AbstractArtifactLabelProvider artifactLabelProvider = new AbstractArtifactLabelProvider();

	@Override
	public Image getImage(Object element) {
		if (isEmptySelectionElement(element)) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_ELCL_REMOVE_DISABLED);
		} else if (element instanceof IAbstractArtifact)
			return artifactLabelProvider.getImage(element, true, true);
		else if (element instanceof IField)
			return artifactLabelProvider.getImage(element, true, true);
		else if (element instanceof IMethod)
			return artifactLabelProvider.getImage(element, true, true);
		else if (element instanceof ILiteral)
			return artifactLabelProvider.getImage(element, true, true);
		else if (element instanceof IAssociationEnd)
			return Images.get(Images.ASSOCIATION_ICON);
		else if (element instanceof IRelationshipEnd)
			return Images.get(Images.DEPENDENCY_ICON);
		else if (element instanceof RelationshipAnchor)
			return Images.get(Images.ASSOCIATION_ICON);
		else
			return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (isEmptySelectionElement(element)) {
			return "<empty selection>";
		} else if (element instanceof IAbstractArtifact) {
			return ((IAbstractArtifact) element).getFullyQualifiedName();
		} else if (element instanceof IModelComponent) {
			return ((IModelComponent) element).getName();
		}
		return super.getText(element);
	}

	private boolean isEmptySelectionElement(Object element) {
		if (NullAbstractArtifact.INSATNCE.equals(element)) {
			return true;
		}
		return false;
	}
}
