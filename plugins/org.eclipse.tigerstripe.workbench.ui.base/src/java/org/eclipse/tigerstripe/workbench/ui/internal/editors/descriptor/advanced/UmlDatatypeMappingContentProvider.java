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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.advanced;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.mapper.UmlDatatypeMapper;

public class UmlDatatypeMappingContentProvider extends ArrayContentProvider {

	public UmlDatatypeMappingContentProvider() {
		super();
	}

	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		UmlDatatypeMapper mapper = (UmlDatatypeMapper) inputElement;
		return mapper.getMappings();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
	}

}
