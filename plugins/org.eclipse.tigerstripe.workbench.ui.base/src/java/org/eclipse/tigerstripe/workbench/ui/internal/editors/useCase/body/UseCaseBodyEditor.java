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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.useCase.body;

import org.eclipse.tigerstripe.workbench.ui.internal.editors.useCase.UseCaseEditor;
import org.eclipse.ui.editors.text.TextEditor;

public class UseCaseBodyEditor extends TextEditor {

	public UseCaseBodyEditor(UseCaseEditor editor) {
		super();
		UseCaseBodyDocumentProvider documentProvider = new UseCaseBodyDocumentProvider();
		setDocumentProvider(documentProvider);
	}

}
