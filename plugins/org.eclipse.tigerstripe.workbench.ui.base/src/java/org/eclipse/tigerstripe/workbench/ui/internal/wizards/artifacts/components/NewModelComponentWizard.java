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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.components;

import static org.eclipse.tigerstripe.workbench.ui.internal.utils.ComponentUtils.scrollToComponentDeferred;

import java.util.List;

import org.apache.tools.ant.filters.StringInputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactOverviewPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ModelComponentSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.MessageListDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.StatusUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.TSOpenAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.Section;

public abstract class NewModelComponentWizard extends Wizard implements
		INewWizard {

	private NewModelComponentPage mainPage;
	private IAbstractArtifact initialArtifact;

	public NewModelComponentWizard(String windowTitle) {
		setDefaultPageImageDescriptor(Images.getDescriptor(Images.TS_LOGO));
		setWindowTitle(windowTitle);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {

		Object element = selection.getFirstElement();
		if (element instanceof ICompilationUnit) {
			initialArtifact = TSExplorerUtils.getArtifactFor(element);
		}
	}

	@Override
	public boolean performFinish() {
		final IAbstractArtifact artifact = mainPage.getArtifact();
		if (artifact == null) {
			return false;
		}
		IJavaElement javaElement = (IJavaElement) artifact
				.getAdapter(IJavaElement.class);
		IResource resource = javaElement.getResource();
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			if (file.exists()) {
				try {
					mainPage.beforeSave();
					IStatus errorList = artifact.validate();
					List<IStatus> statuses = StatusUtils.flat(errorList);

					if (StatusUtils.findMaxSeverity(statuses) == IStatus.ERROR) {
						MessageListDialog dialog = new MessageListDialog(
								getContainer().getShell(), statuses,
								"Save Failed: Invalid Artifact");
						dialog.create();
						dialog.disableOKButton();
						dialog.open();
						return false;
					}

					file.setContents(new StringInputStream(artifact.asText()),
							IResource.FORCE, new NullProgressMonitor());
					Display.getCurrent().asyncExec(new Runnable() {
						public void run() {
							gotoEditor(artifact);
						}
					});
				} catch (Exception e) {
					EclipsePlugin.log(e);
				}
			}
		}
		return true;
	}

	protected abstract NewModelComponentPage createMainPage(
			IAbstractArtifact initArtifact);

	@Override
	public void addPages() {
		mainPage = createMainPage(initialArtifact);
		addPage(mainPage);
	}

	private void gotoEditor(IAbstractArtifact artifact) {
		IEditorPart editorPart = TSOpenAction.openEditor(artifact, PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getActivePage());

		if (editorPart != null) {
			ArtifactEditorBase artEditor = (ArtifactEditorBase) editorPart;
			artEditor.setActivePage("ossj.entity.overview", artEditor);
			ArtifactOverviewPage selectedPage = (ArtifactOverviewPage) artEditor
					.getSelectedPage();
			IFormPart[] formParts = selectedPage.getManagedForm().getParts();

			for (int j = 0; j < formParts.length; j++) {
				if (mainPage.getSectionPartClass().isInstance(formParts[j])) {

					ModelComponentSectionPart part = (ModelComponentSectionPart) formParts[j];

					Section section = part.getSection();
					section.setExpanded(true);
					scrollToComponentDeferred(selectedPage.getManagedForm()
							.getForm(), section);
					TableViewer viewer = part.getViewer();
					Table table = viewer.getTable();
					table.deselectAll();
					for (int row = 0; row < table.getItemCount(); row++) {
						TableItem tableItem = table.getItem(row);
						IModelComponent rowField = (IModelComponent) tableItem
								.getData();

						if (mainPage.getModelComponent().getName()
								.equals(rowField.getName())) {
							table.select(row);
							selectedPage.getManagedForm().fireSelectionChanged(
									part, viewer.getSelection());
							break;
						}
					}
					table.setFocus();
					part.updateMaster();
					break;
				}
			}
		}
	}
}
