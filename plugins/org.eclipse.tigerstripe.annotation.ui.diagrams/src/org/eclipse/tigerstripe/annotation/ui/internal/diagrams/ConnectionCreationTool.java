/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest.ConnectionViewDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.tigerstripe.annotation.ui.diagrams.IAnnotationType;

/**
 * @author Yuri Strot
 *
 */
public class ConnectionCreationTool extends org.eclipse.gmf.runtime.diagram.ui.tools.ConnectionCreationTool {
	
	public ConnectionCreationTool(IElementType type) {
		super(type);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.tools.ConnectionCreationTool#createTargetRequest()
	 */
	@Override
	protected Request createTargetRequest() {
		ConnectionViewDescriptor viewDescriptor = new ConnectionViewDescriptor(
				getElementType(), ((IAnnotationType) getElementType()).getSemanticHint(), getPreferencesHint());
		return new CreateConnectionViewRequest(viewDescriptor);
	}

}
