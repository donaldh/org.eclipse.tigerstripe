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
package org.eclipse.tigerstripe.api.contract;

import java.net.URI;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.api.contract.useCase.IUseCase;

/**
 * Facade for Contract related objects
 * 
 * @author Eric Dillon
 * 
 */
public interface IContractSession {

	public IContractSegment makeIContractSegment(URI uri)
			throws TigerstripeException;

	public IUseCase makeIUseCase(URI uri) throws TigerstripeException;
}
