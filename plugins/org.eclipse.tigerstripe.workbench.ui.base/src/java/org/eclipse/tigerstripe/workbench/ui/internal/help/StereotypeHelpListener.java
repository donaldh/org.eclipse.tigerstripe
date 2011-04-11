/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.help;

import org.eclipse.help.IContext2;
import org.eclipse.help.IHelpResource;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.ui.PlatformUI;

public class StereotypeHelpListener implements HelpListener {
	private StereotypeHelpContext context;
	private IStereotype stereotype;

	public StereotypeHelpListener(IStereotype stereotype) {
		this.stereotype = stereotype;
	}

	public void helpRequested(HelpEvent e) {
		displayHelp();
	}

	public void setStereotype(IStereotype iStereotype) {
		this.stereotype = iStereotype;
		if (isContextHelpDisplayed()) {
			displayHelp();
		}
	}

	private boolean isContextHelpDisplayed() {
		Shell activeShell = PlatformUI.getWorkbench().getDisplay()
				.getActiveShell();
		Object object = activeShell.getData();
		if (object instanceof TrayDialog) {
			TrayDialog trayDialog = (TrayDialog) object;
			if (trayDialog.getTray() != null) {
				return true;
			}
		}
		return false;
	}

	private void displayHelp() {
		if (stereotype != null) {
			if (context == null || !context.getStereotype().equals(stereotype)) {
				context = new StereotypeHelpContext(stereotype);
			}
			PlatformUI.getWorkbench().getHelpSystem().displayHelp(context);
		} else {
			PlatformUI.getWorkbench().getHelpSystem().displayHelp();
		}
	}

	private class StereotypeHelpContext implements IContext2 {
		private final IStereotype stereotype;

		public StereotypeHelpContext(IStereotype stereotype) {
			this.stereotype = stereotype;
		}

		public IStereotype getStereotype() {
			return stereotype;
		}

		public IHelpResource[] getRelatedTopics() {
			return null;
		}

		public String getText() {
			return getStyledText();
		}

		public String getTitle() {
			return stereotype.getName() + " description";
		}

		public String getStyledText() {
			StringBuilder builder = new StringBuilder(
					stereotype.getDescription());
			for (IStereotypeAttribute attribute : stereotype.getAttributes()) {
				builder.append("<br><b>");
				builder.append(attribute.getName());
				builder.append("</b>: ");
				builder.append(attribute.getDescription());
			}
			return builder.toString();
		}

		public String getCategory(IHelpResource topic) {
			return null;
		}
	}
}
