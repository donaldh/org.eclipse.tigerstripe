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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeProjectNature;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeLabelProvider;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

public class CrossProjectArtifactBrowseDialog extends SelectionStatusDialog {

	private static final ViewerFilter VIEWER_FILTER = new ArtifactFilter();

	private TreeViewer treeViewer;
	private IJavaElement selected;
	private final ViewerFilter[] filters;

	public CrossProjectArtifactBrowseDialog(Shell parent) {
		this(parent, null);
	}

	public CrossProjectArtifactBrowseDialog(Shell parent,
			IJavaElement selected, ViewerFilter... filters) {
		super(parent);

		this.filters = new ViewerFilter[filters.length + 1];
		System.arraycopy(filters, 0, this.filters, 0, filters.length);
		this.filters[filters.length] = VIEWER_FILTER;

		setTitle("Select artifact");
		this.selected = selected;
	}

	@Override
	protected void computeResult() {
	}

	@Override
	protected Control createContents(Composite parent) {
		Control result = super.createContents(parent);
		checkCanFinish();
		return result;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		composite.setLayout(new GridLayout());

		treeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.BORDER);
		final TigerstripeContentProvider contentProvider = new TigerstripeContentProvider();
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new TigerstripeLabelProvider(contentProvider));
		treeViewer.setComparator(new JavaElementComparator());
		treeViewer.setFilters(filters);

		IJavaModel input = JavaCore.create(ResourcesPlugin.getWorkspace()
				.getRoot());
		treeViewer.setInput(input);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 350;
		gridData.widthHint = 350;
		treeViewer.getTree().setLayoutData(gridData);

		if (selected != null) {
			treeViewer.setSelection(new StructuredSelection(selected));
			treeViewer.expandToLevel(selected, 1);
		}

		treeViewer.getTree().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Object firstElement = ((IStructuredSelection) treeViewer
						.getSelection()).getFirstElement();
				if (firstElement instanceof IJavaElement) {
					selected = (IJavaElement) firstElement;
				} else {
					selected = null;
				}
				checkCanFinish();
			}
		});
		return composite;
	}

	private void checkCanFinish() {
		boolean canFinish = selected instanceof ICompilationUnit
				&& TSExplorerUtils.getArtifactFor(selected) != null;
		getOkButton().setEnabled(canFinish);
	}

	public IJavaElement getSelected() {
		return selected;
	}

	static class ArtifactFilter extends ViewerFilter {

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {

			if (element instanceof IProject) {
				try {
					return TigerstripeProjectNature
							.hasNature((IProject) element);
				} catch (CoreException e) {
					EclipsePlugin.log(e);
					return false;
				}
			}

			// Avoid default package
			if (element instanceof IPackageFragment) {
				IPackageFragment pf = (IPackageFragment) element;
				try {
					return !(pf.isDefaultPackage() && !pf.hasChildren());
				} catch (JavaModelException e) {
					EclipsePlugin.log(e);
				}
			}

			if (element instanceof ICompilationUnit) {
				return TSExplorerUtils.getArtifactFor(element) != null;
			}

			return element instanceof IPackageFragmentRoot
					|| element instanceof IPackageFragment;
		}
	}

}
