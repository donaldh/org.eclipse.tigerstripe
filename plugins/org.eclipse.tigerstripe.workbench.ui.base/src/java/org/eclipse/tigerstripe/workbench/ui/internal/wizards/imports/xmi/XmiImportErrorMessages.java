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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.xmi;

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImportException;

public class XmiImportErrorMessages {

	public static String getTextForException(ModelImportException exception) {

		StringBuffer buf = new StringBuffer();

		buf.append("XMI import error:");
		buf.append(" Malformed XMI\n\n");
		buf.append("Reason: " + exception.getException().getMessage());
		buf.append("\n\n");
		buf
				.append("Hint:\n  The XMI file you are trying to import contains invalid XMI content.");
		buf
				.append("You may want to use an XSL transformation to correct this and leave the XMI file un-touched.");

		buf
				.append("\n\nNote: use the 'view source' button to view the file that was actually used for import after XSL transformations.");

		return buf.toString();
	}
}