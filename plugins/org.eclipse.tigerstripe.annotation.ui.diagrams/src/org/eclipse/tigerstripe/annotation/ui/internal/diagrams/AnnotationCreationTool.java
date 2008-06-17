/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.tigerstripe.annotation.ui.diagrams.IAnnotationType;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationCreationTool extends CreationTool {
	
    public AnnotationCreationTool(IElementType elementType) {
    	super(elementType);
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool#createTargetRequest()
	 */
	@Override
	protected Request createTargetRequest() {
		String hint = ((IAnnotationType)getElementType()).getSemanticHint();
		ViewDescriptor viewDescriptor = new ViewDescriptor(null,
			Node.class, hint, getPreferencesHint());
		return new CreateViewRequest(viewDescriptor);
	}

}
