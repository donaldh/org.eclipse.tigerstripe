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
package org.eclipse.tigerstripe.api.impl;

import java.net.URI;

import org.eclipse.tigerstripe.api.contract.IContractSession;
import org.eclipse.tigerstripe.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.api.contract.useCase.IUseCase;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.contract.segment.ContractSegment;
import org.eclipse.tigerstripe.contract.useCase.UseCase;

public class ContractSession implements IContractSession {

	public IContractSegment makeIContractSegment(URI uri)
			throws TigerstripeException {
		ContractSegment result = new ContractSegment(uri);
		if (uri != null)
			result.parse(uri);
		return result;
	}

	public IUseCase makeIUseCase(URI uri) throws TigerstripeException {
		UseCase result = new UseCase(uri);
		if (uri != null)
			result.parse(uri);
		return result;
	}

}
