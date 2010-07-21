/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.dnd;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.ArtifactComponentTransferDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;

/**
 * DropAssistant wrapper for ArtifactComponentTransferDropAdapter
 * 
 * @see ArtifactComponentTransferDropAdapter
 */
public class ArtifactComponentDropAssistant extends CommonDropAdapterAssistant {
	private ArtifactComponentTransferDropAdapter dropAdapter;

	public ArtifactComponentDropAssistant() {
		dropAdapter = new ArtifactComponentTransferDropAdapter(null);
	}

	@Override
	public IStatus handleDrop(CommonDropAdapter aDropAdapter,
			DropTargetEvent aDropTargetEvent, Object aTarget) {
		if (SpecialUtils.getView(dropAdapter) == null) {
			SpecialUtils.setView(dropAdapter, SpecialUtils
					.getView(aDropAdapter));
		}
		dropAdapter.dragEnter(aDropTargetEvent);
		dropAdapter.drop(aDropTargetEvent);
		return null;
	}

	@Override
	public IStatus validateDrop(Object target, int operation,
			TransferData transferType) {
		boolean isValid = dropAdapter.validateDrop(target, operation,
				transferType);
		return isValid ? Status.OK_STATUS : null;
	}

}
