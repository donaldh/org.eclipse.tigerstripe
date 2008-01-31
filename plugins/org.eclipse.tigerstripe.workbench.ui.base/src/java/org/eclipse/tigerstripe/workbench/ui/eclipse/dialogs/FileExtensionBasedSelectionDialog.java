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
package org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.viewsupport.AppearanceAwareLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * A Selection dialog for files with specific extensions.
 * 
 * 
 */
public class FileExtensionBasedSelectionDialog extends
		ElementTreeSelectionDialog {

	private String[] fileExtensions = new String[0];

	/**
	 * Constructor for FacetSelectionDialog.
	 */
	public FileExtensionBasedSelectionDialog(Shell parent, boolean multiSelect,
			boolean acceptFolders) {
		super(parent, new FileLabelProvider(), new FileContentProvider());
		setSorter(new FileViewerSorter());
		addFilter(new FileTemplateFilter());
		setValidator(new FileSelectionValidator(multiSelect, acceptFolders));
	}

	public void setFileExtensions(String[] fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	public String[] getFileExtensions() {
		return this.fileExtensions;
	}

	private boolean isTargetFile(IFile file) {
		String name = file.getName();
		for (String extension : fileExtensions) {
			if (name.endsWith("." + extension) || name.endsWith(extension))
				return true;
		}
		return false;
	}

	private static class FileLabelProvider extends AppearanceAwareLabelProvider {
		// private final Image IMG_FOLDER = PlatformUI.getWorkbench()
		// .getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		//
		// public Image getImage(Object element) {
		//			
		// if (element instanceof File) {
		// File curr = (File) element;
		// if (curr.isDirectory()) {
		// return IMG_FOLDER;
		// } else {
		// return TigerstripePluginImages
		// .get(TigerstripePluginImages.CONTRACTUSECASE_ICON);
		// }
		// }
		// return null;
		// }
		//
		@Override
		public String getText(Object element) {
			if (element instanceof IResource)
				return ((IResource) element).getName();
			return super.getText(element);
		}
	}

	private static class FileContentProvider implements ITreeContentProvider {

		private final Object[] EMPTY = new Object[0];

		public Object[] getChildren(Object parentElement) {
			try {
				if (parentElement instanceof IFolder) {
					IResource[] children = ((IFolder) parentElement).members();
					if (children != null)
						return children;
				} else if (parentElement instanceof IProject) {
					IResource[] children = ((IProject) parentElement).members();
					if (children != null)
						return children;
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
			return EMPTY;
		}

		public Object getParent(Object element) {
			if (element instanceof IResource)
				return ((IResource) element).getParent();
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

	private class FileTemplateFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parent, Object element) {
			if (element instanceof IFile) {
				IFile file = (IFile) element;
				return isTargetFile(file);
			}
			return true;
		}
	}

	private static class FileViewerSorter extends ViewerSorter {
		@Override
		public int category(Object element) {
			if (element instanceof IFile)
				return 1;
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
				if (curr instanceof IFolder) {
					if (!fAcceptFolders)
						return new StatusInfo(IStatus.ERROR, ""); //$NON-NLS-1$
				}
			}
			return new StatusInfo();
		}
	}
}