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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.dialogs.TypeSelectionDialog2;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.internal.runtime.messages.NewWizardMessages;

public class Utils {

	public static org.eclipse.jdt.core.IType chooseAttributeType(Shell shell,
			IPackageFragmentRoot root, String initialType) {
		if (root == null)
			return null;
		IJavaElement[] elements = new IJavaElement[] { root.getJavaProject() };
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);

		TypeSelectionDialog2 dialog = new TypeSelectionDialog2(shell, false,
				null, scope, IJavaSearchConstants.TYPE);
		dialog
				.setTitle(NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.SuperClassDialog.title"));
		//$NON-NLS-1$
		dialog
				.setMessage(NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.SuperClassDialog.message"));
		//$NON-NLS-1$
		dialog.setFilter(initialType);

		if (dialog.open() == Window.OK)
			return (org.eclipse.jdt.core.IType) dialog.getFirstResult();
		return null;
	}

}
