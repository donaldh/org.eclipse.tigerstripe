/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;

public class DragAndDropMoveRefactorWizard extends AbstractModelRefactorWizard {

	@Override
	public void addPages() {

		addPage(new PreviewWizardPage());
	}
}