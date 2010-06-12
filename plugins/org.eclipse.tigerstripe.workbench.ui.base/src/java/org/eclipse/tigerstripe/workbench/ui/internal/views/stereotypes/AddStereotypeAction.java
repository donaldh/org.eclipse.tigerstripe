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

package org.eclipse.tigerstripe.workbench.ui.internal.views.stereotypes;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForStereotypeDialog;

/**
 * @author Yuri Strot
 * 
 */
public class AddStereotypeAction extends Action {

	private Shell shell;
	private IStereotypeCapable component;

	public AddStereotypeAction(IStereotypeCapable component, Shell shell,
			ImageDescriptor image) {
		super("Add Stereotype", image);
		this.component = component;
		this.shell = shell;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		BrowseForStereotypeDialog dialog = new BrowseForStereotypeDialog(
				component, component.getStereotypeInstances());

		try {
			IStereotype[] selected = dialog.browseAvailableStereotypes(shell);
			if (selected.length > 0) {
				for (IStereotype st : selected) {
					IStereotypeInstance instance = st.makeInstance();
					component.addStereotypeInstance(instance);
					try {
						StereotypeCapableSaveHelper.save(component);
					} catch (CoreException e) {
						ErrorDialog.openError(shell, "Save is failed", e
								.getMessage(), e.getStatus());
						EclipsePlugin.log(e);
					}
				}
			}
		} catch (TigerstripeException ee) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					ee);
		}
	}

}
