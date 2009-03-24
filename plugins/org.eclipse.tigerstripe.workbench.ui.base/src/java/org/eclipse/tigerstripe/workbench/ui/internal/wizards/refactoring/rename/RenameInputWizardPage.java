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

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.rename;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLog;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.RefactorPreviewWizardPage;

public class RenameInputWizardPage extends WizardPage {

	private String newFullyQualifiedName;

	private IAbstractArtifact artifact;

	protected RenameInputWizardPage() {
		super("RenameInputWizardPage");
	}

	public IAbstractArtifact getArtifact() {
		return artifact;
	}

	public String getNewFullyQualifiedName() {
		
		return newFullyQualifiedName;
	}
	
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);
		setControl(composite);

		new Label(composite, SWT.NONE).setText("New name:");
		final Text nameField = new Text(composite, SWT.BORDER);
		nameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameField.setText(artifact.getFullyQualifiedName());
		nameField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if(nameField.getText().contains(".")) {
					newFullyQualifiedName = nameField.getText();
				} else {
					newFullyQualifiedName = artifact.getPackage() + '.' + nameField.getText();
				}
				validatePage();
			}

		});
		
		setPageComplete(false);
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

	protected final void validatePage() {
		
		setPageComplete(!newFullyQualifiedName.equals(artifact.getFullyQualifiedName()));
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete() && super.getNextPage() != null;
	}

	@Override
	public IWizardPage getNextPage() {
		
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) {

					monitor.beginTask("Analyzing destination project...", IProgressMonitor.UNKNOWN);

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						TigerstripeLog.logError(e1);
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e) {
			TigerstripeLog.logError(e);
		} catch (InterruptedException e) {
			TigerstripeLog.logError(e);
		}

		IWizardPage page = getWizard().getPage(RefactorPreviewWizardPage.PAGE_NAME);
		return page;
	}

}
