/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.factories;

import org.eclipse.gef.Tool;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;
import org.eclipse.tigerstripe.annotation.ui.diagrams.DiagramAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.ConnectionCreationTool;

/**
 * @author Yuri Strot
 *
 */
public class DiagramPaletteFactory extends PaletteFactory.Adapter {
	
	private static final String TOOL_CONNECTION = "annotationConnectionTool"; //$NON-NLS-1$
	
	public DiagramPaletteFactory() {
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory#createTool(java.lang.String)
	 */
	public Tool createTool(String toolId) {
		if (toolId.equals(TOOL_CONNECTION)) {
  			return new ConnectionCreationTool(DiagramAnnotationType.CONNECTION);
		}
		return null;
	}

}
