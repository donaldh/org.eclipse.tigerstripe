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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.helpers;

import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * @generated
 */
public class AssociationClassEditHelper extends TigerstripeBaseEditHelper {

	private AbstractEditHelperAdvice[] aeha = null;

	public AssociationClassEditHelper() {
		super();
		aeha = new AbstractEditHelperAdvice[1];
		aeha[0] = new AssociationClassEditHelperAdvice();
	}

	@Override
	protected IEditHelperAdvice[] getEditHelperAdvice(IEditCommandRequest req) {
		return aeha;
	}

}