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

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

public class VMFileSelectionDialog extends ElementTreeSelectionDialog {
	/**
	 * Constructor for JARFileSelectionDialog.
	 */
	public VMFileSelectionDialog(Shell parent, boolean multiSelect,
			boolean acceptFolders) {
		super(parent, new FileLabelProvider(), new FileContentProvider());
		setSorter(new FileViewerSorter());
		addFilter(new FileVelocityTemplateFilter());
		setValidator(new FileSelectionValidator(multiSelect, acceptFolders));
	}

	private static boolean isVelocityTemplate(File file) {
		String name = file.getName();
		return name.endsWith(".vm");
	}

	private static class FileLabelProvider extends LabelProvider {
		private final Image IMG_FOLDER = PlatformUI.getWorkbench()
				.getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		private final Image IMG_JAR = JavaPlugin.getDefault()
				.getImageRegistry().get(JavaPluginImages.IMG_OBJS_TEMPLATE);

		@Override
		public Image getImage(Object element) {
			if (element instanceof File) {
				File curr = (File) element;
				if (curr.isDirectory())
					return IMG_FOLDER;
				else
					return IMG_JAR;
			}
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof File)
				return ((File) element).getName();
			return super.getText(element);
		}
	}

	private static class FileContentProvider implements ITreeContentProvider {

		private final Object[] EMPTY = new Object[0];

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof File) {
				File[] children = ((File) parentElement).listFiles();
				if (children != null)
					return children;
			}
			return EMPTY;
		}

		public Object getParent(Object element) {
			if (element instanceof File)
				return ((File) element).getParentFile();
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		public Object[] getElements(Object element) {
			return getChildren(element);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private static class FileVelocityTemplateFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parent, Object element) {
			if (element instanceof File) {
				File file = (File) element;
				if (file.isFile())
					return isVelocityTemplate(file);
				else
					return true;
			}
			return false;
		}
	}

	private static class FileViewerSorter extends ViewerSorter {
		@Override
		public int category(Object element) {
			if (element instanceof File) {
				if (((File) element).isFile())
					return 1;
			}
			return 0;
		}
	}

	private static class FileSelectionValidator implements
			ISelectionStatusValidator {
		private boolean fMultiSelect;
		private boolean fAcceptFolders;

		public FileSelectionValidator(boolean multiSelect, boolean acceptFolders) {
			fMultiSelect = multiSelect;
			fAcceptFolders = acceptFolders;
		}

		public IStatus validate(Object[] selection) {
			int nSelected = selection.length;
			if (nSelected == 0 || (nSelected > 1 && !fMultiSelect))
				return new StatusInfo(IStatus.ERROR, ""); //$NON-NLS-1$
			for (int i = 0; i < selection.length; i++) {
				Object curr = selection[i];
				if (curr instanceof File) {
					File file = (File) curr;
					if (!fAcceptFolders && !file.isFile())
						return new StatusInfo(IStatus.ERROR, ""); //$NON-NLS-1$
				}
			}
			return new StatusInfo();
		}
	}
}