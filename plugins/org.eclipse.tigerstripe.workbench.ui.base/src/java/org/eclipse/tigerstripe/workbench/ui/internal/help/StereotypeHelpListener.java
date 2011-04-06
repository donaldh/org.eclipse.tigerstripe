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

import org.eclipse.help.IContext;
import org.eclipse.help.IContext2;
import org.eclipse.help.IHelpResource;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.ui.PlatformUI;

public class StereotypeHelpListener implements HelpListener {
	private IContext context;
	private final IStereotype stereotype;

	public StereotypeHelpListener(IStereotype stereotype) {
		this.stereotype = stereotype;
	}

	public void helpRequested(HelpEvent e) {
		if (context == null) {
			context = new StereotypeHelpContext();
		}
		PlatformUI.getWorkbench().getHelpSystem().displayHelp(context);
	}

	private class StereotypeHelpContext implements IContext2 {
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
