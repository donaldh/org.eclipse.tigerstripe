package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.TigerstripeShapeNodeEditPart;

public class PartAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IModelComponent.class) {
			return getAnnotable(adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return new Class[] { IModelComponent.class };
	}

	protected Object getAnnotable(Object adaptableObject) {
		if (adaptableObject instanceof TigerstripeShapeNodeEditPart) {
			TigerstripeShapeNodeEditPart part = (TigerstripeShapeNodeEditPart) adaptableObject;
			return part.getAdapter(IAbstractArtifact.class);
		} else if (adaptableObject instanceof ConnectionNodeEditPart) {
			ConnectionNodeEditPart part = (ConnectionNodeEditPart) adaptableObject;
			return part.getAdapter(IAbstractArtifact.class);
		}
		return null;
	}
}
