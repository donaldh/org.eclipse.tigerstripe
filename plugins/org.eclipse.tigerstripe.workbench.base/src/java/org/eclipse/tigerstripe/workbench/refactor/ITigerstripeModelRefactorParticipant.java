/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.refactor;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

/**
 * All registered ITigerstripeModelRefactorParticipants are called while the
 * model is visited to establish what commands are to be executed as a result of
 * a refactor request.
 * 
 * The participants are expected to return a IRefactorCommand that will be
 * triggered when the refactor is executed. The getCommand(..) method is called
 * while the ModelRefactorCommandFactory visits the whole model.
 * 
 * @author erdillon
 * 
 */
public interface ITigerstripeModelRefactorParticipant {

	public IRefactorCommand getCommand(ModelRefactorRequest request,
			IAbstractArtifact visitedArtifact) throws TigerstripeException;

}
