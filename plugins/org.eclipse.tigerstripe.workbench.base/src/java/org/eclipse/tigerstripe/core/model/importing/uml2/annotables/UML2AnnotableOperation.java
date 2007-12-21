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
package org.eclipse.tigerstripe.core.model.importing.uml2.annotables;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.core.model.importing.AnnotableDatatype;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementOperation;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementOperationException;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementOperationParameter;

public class UML2AnnotableOperation extends UML2AnnotableBase implements
		AnnotableElementOperation {

	private UML2AnnotableDatatype returnType;

	private List<AnnotableElementOperationParameter> parameters = new ArrayList<AnnotableElementOperationParameter>();

	private List<AnnotableElementOperationException> exceptions = new ArrayList<AnnotableElementOperationException>();

	public UML2AnnotableOperation(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void addAnnotableElementOperationParameter(
			AnnotableElementOperationParameter parameter) {
		parameters.add(parameter);
	}

	public List<AnnotableElementOperationParameter> getAnnotableElementOperationParameters() {
		return parameters;
	}

	public AnnotableDatatype getReturnType() {
		return returnType;
	}

	public String getSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getVisibility() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public void setReturnParameter(
			AnnotableElementOperationParameter returnParameter) {
		// do not use
	}

	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub

	}

	public void setReturnType(UML2AnnotableDatatype returnType) {
		this.returnType = returnType;
	}

	public void addAnnotableElementOperationException(
			AnnotableElementOperationException exception) {
		exceptions.add(exception);
	}

	public List<AnnotableElementOperationException> getAnnotableElementOperationExceptions() {
		return exceptions;
	}

}
