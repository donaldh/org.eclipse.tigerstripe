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
package org.eclipse.tigerstripe.core.model.importing.xmi;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.core.model.importing.AnnotableDatatype;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementOperation;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementOperationException;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementOperationParameter;

@Deprecated
public class XMIAnnotableElementOperation extends XMIAnnotableBase implements
		AnnotableElementOperation {

	// private Operation umlOperation;

	private AnnotableElementOperationParameter returnParameter;

	private List<AnnotableElementOperationParameter> annotableElementOperationParameters = new ArrayList<AnnotableElementOperationParameter>();

	private int visibility;

	public XMIAnnotableElementOperation(String name) {
		super(name);
	}

	public XMIAnnotableElementOperation() {
		super("unknown");
	}

	@Override
	public Object getCorrespondingUmlElement() {
		return null;
		// return getUmlOperation();
	}

	// public Operation getUmlOperation() {
	// return umlOperation;
	// }
	//
	// public void setUmlOperation(Operation umlOperation) {
	// this.umlOperation = umlOperation;
	// }
	//
	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public List getAnnotableElementOperationParameters() {
		return annotableElementOperationParameters;
	}

	public void addAnnotableElementOperationParameter(
			AnnotableElementOperationParameter param) {
		this.annotableElementOperationParameters.add(param);
	}

	public AnnotableDatatype getReturnType() {
		if (returnParameter == null)
			return null;
		return returnParameter.getType();
	}

	public AnnotableElementOperationParameter getReturnParameter() {
		return returnParameter;
	}

	public void setReturnParameter(
			AnnotableElementOperationParameter returnParameter) {
		this.returnParameter = returnParameter;
	}

	public String getSignature() {
		String result = getReturnType().getFullyQualifiedName();
		for (AnnotableElementOperationParameter param : annotableElementOperationParameters) {
			result = result + ";" + param.getType().getFullyQualifiedName();
		}
		return result;
	}

	public void addAnnotableElementOperationException(
			AnnotableElementOperationException exception) {
		// FIXME: not implemented
	}

	public List<AnnotableElementOperationException> getAnnotableElementOperationExceptions() {
		// FIXME: not implemented
		return null;
	}

}
