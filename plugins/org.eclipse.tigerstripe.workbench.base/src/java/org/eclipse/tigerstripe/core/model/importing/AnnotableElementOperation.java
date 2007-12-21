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
package org.eclipse.tigerstripe.core.model.importing;

import java.util.List;

public interface AnnotableElementOperation extends Annotable {

	public List<AnnotableElementOperationParameter> getAnnotableElementOperationParameters();

	public void addAnnotableElementOperationParameter(
			AnnotableElementOperationParameter parameter);

	public List<AnnotableElementOperationException> getAnnotableElementOperationExceptions();

	public void addAnnotableElementOperationException(
			AnnotableElementOperationException exception);

	public AnnotableDatatype getReturnType();

	public void setReturnParameter(
			AnnotableElementOperationParameter returnParameter);

	public void setVisibility(int visibility);

	public int getVisibility();

	public String getSignature();

	public boolean isAbstract();

}
