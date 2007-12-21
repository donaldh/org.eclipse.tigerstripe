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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import org.eclipse.emf.common.command.Command;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

/**
 * Top interface to be implemented by Refreshers that can refresh an EObject
 * based on a IAbstractArtifact
 * 
 * @author Eric Dillon
 * 
 */
public interface IEObjectRefresher {

	/**
	 * Creates a command (compound if needed) to refresh the representation of
	 * the given element in the diagram based on the value of iArtifact
	 * 
	 * @param element
	 * @param iArtifact
	 * @throws DiagramRefreshException
	 */
	public Command refresh(QualifiedNamedElement element,
			IAbstractArtifact iArtifact) throws DiagramRefreshException;

	/**
	 * Upon initial refresh the behavior is slightly different so we need to
	 * identify whether this update command is part of the initial refresh or
	 * not.
	 * 
	 * @param initialRefresh
	 */
	public void setInitialRefresh(boolean initialRefresh);

	public boolean isInitialRefresh();
}
