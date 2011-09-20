package org.eclipse.tigerstripe.workbench.ui.internal;

import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.annotation.ui.core.ITargetProcessor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.Editors;
import org.eclipse.tigerstripe.workbench.utils.AdaptHelper;
import org.eclipse.ui.IEditorPart;

public class TigerstripeAnnotationTargetProcessor implements ITargetProcessor {

	public boolean isDirty(Object object) {
		if (object instanceof IModelComponent) {
			IMember m = (IMember) object;
			IAbstractArtifact art = m.getContainingArtifact();
			if (art == null) {
				return false;
			}
			IResource res = AdaptHelper.adapt(art, IResource.class);
			if (res == null) {
				return false;
			}
			IEditorPart editorPart = Editors.findForResource(res);
			if (editorPart == null) {
				return false;
			}
			return editorPart.isDirty();
		}
		
		return false;
	}

	public String getName(Object object) {
		if (object instanceof IMember) {
			IMember member = (IMember) object;
			return member.getMemberName() + " " + member.getUniqueName();
		}
		return null;
	}

	public String getDirtyViolationMessage(Object object) {
		if (object instanceof IMember) {
			IAbstractArtifact containingArtifact = ((IMember) object).getContainingArtifact();
			StringBuilder msg = new StringBuilder(getName(object));
			msg.append(" is dirty. Please save an open editor with the artifact");
			if (containingArtifact != null) {
				msg.append(" ").append(containingArtifact.getFullyQualifiedName());
			}
			msg.append(", before adding the annotation");
			return msg.toString();
		}
		return null;
	}
	
}
