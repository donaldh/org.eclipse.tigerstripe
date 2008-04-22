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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.event;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEventSpecifics;

/**
 * This content provider is expecting a list of EventDescriptorEntries
 * 
 * @author Eric Dillon
 * 
 */
public class EventEntryContentProvider extends ArrayContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		IAbstractArtifact artifact = (IAbstractArtifact) inputElement;
		IOssjEventSpecifics specifics = (IOssjEventSpecifics) artifact
				.getIStandardSpecifics();
		return specifics.getCustomEventDescriptorEntries();
	}

}
