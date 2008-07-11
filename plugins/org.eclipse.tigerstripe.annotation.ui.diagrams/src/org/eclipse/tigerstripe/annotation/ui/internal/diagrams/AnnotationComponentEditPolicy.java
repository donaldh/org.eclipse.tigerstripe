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

import java.util.List;

import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewLocationNode;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationComponentEditPolicy extends ViewComponentEditPolicy {
	
	protected ViewLocationNode getLocation(View view) {
		List<View> views = DiagramRebuildUtils.getAnnotationEnd(view);
		for (View view2 : views) {
			if (view2 instanceof ViewLocationNode)
				return (ViewLocationNode)view2;
		}
		return null;
	}
	
	protected boolean isLocationEmpty(View view, ViewLocationNode location) {
		for (Object object : location.getSourceEdges()) {
			Edge edge = (Edge)object;
			if (edge.getTarget() != view)
				return false;
		}
		for (Object object : location.getTargetEdges()) {
			Edge edge = (Edge)object;
			if (edge.getSource() != view)
				return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy#getDeleteCommand(org.eclipse.gef.requests.GroupRequest)
	 */
	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		Command command = super.getDeleteCommand(request);
        TransactionalEditingDomain domain = getEditingDomain();
		if (domain == null) {
			return command;
		}
		
		View view = (View) getHost().getModel();
		ViewLocationNode location = getLocation(view);
		if (location != null && isLocationEmpty(view, location)) {
			CompoundCommand cc = null;
			if (command instanceof CompoundCommand) {
				cc = (CompoundCommand)command;
			}
			else {
				cc = new CompoundCommand();
				cc.add(command);
			}
			cc.add(new ICommandProxy(new DeleteCommand(domain, location)));
			return cc;
		}
		
		return command;
	}
    
    private TransactionalEditingDomain getEditingDomain() {
         if (getHost() instanceof IGraphicalEditPart) {
            return ((IGraphicalEditPart) getHost()).getEditingDomain();
        } else if (getHost() instanceof IEditingDomainProvider) {
            Object domain = ((IEditingDomainProvider) getHost())
                .getEditingDomain();
            if (domain instanceof TransactionalEditingDomain) {
                return (TransactionalEditingDomain) domain;
            }
        }
        return null;
    }

}
