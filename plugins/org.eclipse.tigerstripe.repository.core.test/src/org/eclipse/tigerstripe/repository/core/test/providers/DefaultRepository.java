/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.core.test.providers;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.repository.manager.DefaultXMLModelRepository;
import org.eclipse.tigerstripe.repository.manager.IModelRepository;

public class DefaultRepository extends DefaultXMLModelRepository implements
		IModelRepository {

	public DefaultRepository(URI uri) {
		super(uri);
	}

}
