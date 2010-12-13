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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.ui.IWorkbenchSite;

public abstract class SelectionProviderIntermediateFocusListener implements
		FocusListener {

	public abstract ISelectionProvider getSelectionProvider();

	public abstract IWorkbenchSite getWorkbenchSite();

	public void focusGained(FocusEvent e) {
		setSelectionProvider(getSelectionProvider());
	}

	public void focusLost(FocusEvent e) {
		setSelectionProvider(null);
	}

	private void setSelectionProvider(ISelectionProvider provider) {
		SelectionProviderIntermediate intermediate = getIntermediate();
		if (intermediate != null) {
			intermediate.setSelectionProviderDelegate(provider);
		}
	}

	private SelectionProviderIntermediate getIntermediate() {
		IWorkbenchSite site = getWorkbenchSite();
		if (site != null) {
			ISelectionProvider selectionProvider = getWorkbenchSite()
					.getSelectionProvider();
			if (selectionProvider != null
					&& selectionProvider instanceof SelectionProviderIntermediate) {
				return (SelectionProviderIntermediate) selectionProvider;
			}
		}
		return null;
	}
}
