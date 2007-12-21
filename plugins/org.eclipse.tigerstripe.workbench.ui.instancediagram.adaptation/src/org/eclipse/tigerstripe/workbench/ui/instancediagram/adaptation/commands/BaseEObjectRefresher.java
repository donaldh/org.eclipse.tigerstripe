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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * Top level EObject refresher containing the bare minimum
 * 
 * @author Eric Dillon
 * 
 */
public abstract class BaseEObjectRefresher {

	private boolean isInitialRefresh;

	private TransactionalEditingDomain editingDomain;

	protected BaseEObjectRefresher(TransactionalEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	protected TransactionalEditingDomain getEditingDomain() {
		return this.editingDomain;
	}

	public void setInitialRefresh(boolean initialRefresh) {
		this.isInitialRefresh = initialRefresh;
	}

	public boolean isInitialRefresh() {
		return isInitialRefresh;
	}
}
