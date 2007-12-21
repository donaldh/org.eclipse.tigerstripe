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
package org.eclipse.tigerstripe.api.artifacts.model;

import java.util.List;

import org.eclipse.tigerstripe.api.external.model.IextLabel;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;

public interface ILabel extends IextLabel, IModelComponent {

	public void setIType(IType type);

	public IType makeIType();

	public void setValue(String value);

	public IType getIType();

	public String getLabelString();

	public List<TigerstripeError> validate();

	public ILabel clone();
}
