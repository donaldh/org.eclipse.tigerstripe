/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.tigerstripe.workbench.diagram.IDiagram;

/**
 * This factory is used to create relevant logical nodes for the explorer when
 * needed.
 * 
 * The TigerstripeExplorerContentProvider uses this factory class to create the
 * appropriate logical nodes, so that the logic is contained in this factory
 * class only.
 * 
 * If a match is found the corresponding logical node is returned.
 * 
 * All types of logical nodes are expected to register themselves so they can be
 * "discovered" by the factory.
 * 
 * This is a singleton class
 * 
 * @author Eric Dillon
 * 
 */
public class LogicalExplorerNodeFactory implements IAdapterFactory {


	private AbstractLogicalExplorerNode[] nodeModels = {
			ClassDiagramLogicalNode.MODEL, InstanceDiagramLogicalNode.MODEL };

	private static LogicalExplorerNodeFactory instance;

	public LogicalExplorerNodeFactory() {

	}

	public static LogicalExplorerNodeFactory getInstance() {
		if (instance == null) {
			instance = new LogicalExplorerNodeFactory();
		}
		return instance;
	}

	public Object getNode(Object element) {
		if (element instanceof IResource) {
			IResource res = (IResource) element;
			for (AbstractLogicalExplorerNode model : nodeModels) {
				if (model.isKeyResource(res))
					return model.makeInstance(res);
				else if (model.shouldFilterResource(res))
					return null;
			}
		}
		return element;
	}
	
	// IAdapterFactory I/F
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType.equals(IDiagram.class))
			return getNode(adaptableObject);
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class[] getAdapterList() {
		return new Class[]{IResource.class};
	}	
}
