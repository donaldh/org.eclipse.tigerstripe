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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.refactoring.nls.PackageBrowseAdapter;
import org.eclipse.jdt.internal.ui.refactoring.nls.PackageSelectionDialogButtonField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class TSPackageBrowseAdapter extends PackageBrowseAdapter {

	protected IJavaProject project;

	PackageSelectionDialogButtonField fReceiver;

	@Override
	public void setReceiver(PackageSelectionDialogButtonField receiver) {
		fReceiver = receiver;
	}

	public TSPackageBrowseAdapter(IJavaProject project) {
		super(null);
		this.project = project;
	}

	@Override
	public void changeControlPressed(DialogField field) {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				Display.getCurrent().getActiveShell(),
				new JavaElementLabelProvider());
		dialog.setIgnoreCase(false);
		dialog.setTitle("Package selection");
		dialog.setMessage("Choose a package");
		dialog.setElements(createPackageListInput(project, null));
		if (dialog.open() == Window.OK) {
			IPackageFragment selectedPackage = (IPackageFragment) dialog
					.getFirstResult();
			if (selectedPackage != null) {
				fReceiver.setPackage(selectedPackage);
			}
		}
	}

	public static Object[] createPackageListInput(IJavaProject project,
			String elementNameMatch) {
		try {
			IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
			List result = new ArrayList();
			HashMap entered = new HashMap();
			for (int i = 0; i < roots.length; i++) {
				if (canAddPackageRoot(roots[i])) {
					getValidPackages(roots[i], result, entered,
							elementNameMatch);
				}
			}
			return result.toArray();
		} catch (JavaModelException e) {
			JavaPlugin.log(e);
			return new Object[0];
		}
	}

	static boolean canAddPackageRoot(IPackageFragmentRoot root)
			throws JavaModelException {
		if (!root.exists())
			return false;
		if (root.isArchive())
			return false;
		if (root.isExternal())
			return false;
		if (root.isReadOnly())
			return false;
		if (!root.isStructureKnown())
			return false;
		return true;
	}

	static void getValidPackages(IPackageFragmentRoot root, List result,
			HashMap entered, String elementNameMatch) throws JavaModelException {
		IJavaElement[] children = null;
		try {
			children = root.getChildren();
		} catch (JavaModelException e) {
			return;
		}
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof IPackageFragment) {
				IPackageFragment packageFragment = (IPackageFragment) children[i];
				String packageName = packageFragment.getElementName();

				if ((entered != null)
						&& (entered.containsKey(packageName)) == true) {
					continue;
				}

				if (canAddPackage(packageFragment)) {
					if ((elementNameMatch == null)
							|| (elementNameMatch.equals(packageName))) {
						result.add(packageFragment);
						if (entered != null) {
							entered.put(packageName, null);
						}
					}
				}
			}
		}
	}

	static boolean canAddPackage(IPackageFragment p) throws JavaModelException {
		if (!p.exists())
			return false;
		if (p.isReadOnly())
			return false;
		if (!p.isStructureKnown())
			return false;
		return true;
	}

}
