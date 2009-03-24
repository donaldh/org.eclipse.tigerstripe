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

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.move;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.model.WorkbenchViewerComparator;

public class MoveInputWizardPage extends WizardPage {

	private IContainer container;

	private IAbstractArtifact artifact;

	private TreeViewer destinationField;

	protected MoveInputWizardPage() {
		super("MoveWizardPage1");
	}

	public IAbstractArtifact getArtifact() {
		return artifact;
	}

	public String getNewFullyQualifiedName() throws TigerstripeException {

		IJavaElement element = (IJavaElement) container.getAdapter(IJavaElement.class);
		if (element instanceof IPackageFragment) {
			IPackageFragment pkg = (IPackageFragment) element.getAdapter(IPackageFragment.class);
			return pkg.getElementName() + '.' + artifact.getName();
		} else {
			throw new TigerstripeException("The supplied container is must be an instance of IPackageFragment!");
		}
		
	}

	public void createControl(Composite parent) {

		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setFont(parent.getFont());

		Label label = new Label(composite, SWT.NONE);
		label.setText("Choose destination for '" + artifact.getName() + ": ");
		label.setLayoutData(new GridData());

		destinationField = new TreeViewer(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
		gd.widthHint = convertWidthInCharsToPixels(40);
		gd.heightHint = convertHeightInCharsToPixels(15);
		destinationField.getTree().setLayoutData(gd);
		destinationField.setLabelProvider(new WorkbenchLabelProvider());
		destinationField.setContentProvider(new BaseWorkbenchContentProvider());
		destinationField.setComparator(new WorkbenchViewerComparator());
		destinationField.setInput(ResourcesPlugin.getWorkspace());
		destinationField.addFilter(new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IProject) {
					IProject project = (IProject) element;
					return project.isAccessible();
				} else if (element instanceof IFolder) {
					return true;
				}
				return false;
			}
		});
		destinationField.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				validatePage();
			}
		});

		setPageComplete(false);
		setControl(composite);

	}

	private final void validatePage() {

		IStructuredSelection selection = (IStructuredSelection) destinationField.getSelection();
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof IContainer) {
			container = (IContainer) firstElement;
		} else {
			setPageComplete(false);
			return;
		}
		setPageComplete(true);
	}

	public void init(IStructuredSelection selection) {

		if (selection == null)
			return;

		if (selection.size() > 0) {

			Object obj = selection.getFirstElement();
			if (obj instanceof IJavaElement) {

				IJavaElement element = (IJavaElement) obj;
				IAbstractArtifact artifact = (IAbstractArtifact) element.getAdapter(IAbstractArtifact.class);
				if (artifact != null) {
					this.artifact = artifact;
				}
			}
		}
	}

}
