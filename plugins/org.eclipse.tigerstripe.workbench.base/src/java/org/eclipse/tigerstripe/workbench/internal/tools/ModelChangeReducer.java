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
package org.eclipse.tigerstripe.workbench.internal.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.workbench.IModelChangeDelta;

public class ModelChangeReducer {

	public static Collection<IModelChangeDelta> reduceDeltas(
			Collection<IModelChangeDelta> deltas) {
		List<IModelChangeDelta> reducedDeltas = new ArrayList<IModelChangeDelta>();

		reducedDeltas.addAll(deltas);
		return reducedDeltas;
	}
}
