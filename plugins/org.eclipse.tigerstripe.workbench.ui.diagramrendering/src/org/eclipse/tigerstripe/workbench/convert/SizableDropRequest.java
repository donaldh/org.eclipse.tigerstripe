package org.eclipse.tigerstripe.workbench.convert;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.Sizable;

public class SizableDropRequest extends DropObjectsRequest implements Sizable {

	private Dimension dimension;

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public Dimension getDimension() {
		return dimension;
	}
}
