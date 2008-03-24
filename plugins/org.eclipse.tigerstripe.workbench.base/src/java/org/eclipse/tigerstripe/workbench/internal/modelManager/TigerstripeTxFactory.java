/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.modelManager;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain.Factory;

public class TigerstripeTxFactory implements Factory {

	public TransactionalEditingDomain createEditingDomain() {
		return TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain();
	}

	public TransactionalEditingDomain createEditingDomain(ResourceSet rset) {
		return TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain(rset);
	}

	public TransactionalEditingDomain getEditingDomain(ResourceSet rset) {
		return TransactionalEditingDomain.Factory.INSTANCE
				.getEditingDomain(rset);
	}

}
