package org.eclipse.tigerstripe.workbench.ui.internal.wizards.pluggablePlugin;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.BasicNewResourceWizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.undo.CreateFileOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

/**
 * Velocity template wizard (enhancement#324709)
 * 
 * @author Navid Mehregani - Initial Creation
 */
public class NewVelocityTemplateWizard extends BasicNewResourceWizard implements
		INewWizard {

	private NewVelocityWizardPage fWizardPage = null;
	private IStructuredSelection fSelection = null;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		fSelection = selection;
		fWizardPage = new NewVelocityWizardPage(fSelection);
	}

	@Override
	public void addPages() {
		addPage(fWizardPage);
	}

	@Override
	public boolean performFinish() {
		final String fileName = fWizardPage.getFilename();
		IPath targetPath = fWizardPage.getTargetContainer();

		if ((!isFileNameValid(fileName)) || !isTargetContainerValid(targetPath)) {
			// We should never get to this point
			EclipsePlugin
					.logErrorMessage("Velocity template wizard: either filename or target folder is invalid.");
			return false;
		}

		IContainer targetContainer = ResourcesPlugin.getWorkspace().getRoot()
				.getFolder(targetPath);

		if (targetContainer != null) {
			final IFile file = targetContainer.getFile(new Path(fileName));
			if (!file.exists()) {
				IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						CreateFileOperation fileOperation = new CreateFileOperation(
								file, null, null, "Creating file " + fileName);
						try {
							fileOperation.execute(monitor, WorkspaceUndoUtil
									.getUIInfoAdapter(getShell()));
						} catch (ExecutionException e) {
							EclipsePlugin
									.logErrorMessage("An error occurred while creating the file: "
											+ e.getMessage());
						}
					}

				};

				try {
					getContainer().run(true, false, runnableWithProgress);
				} catch (InvocationTargetException e1) {
					EclipsePlugin
							.logErrorMessage("An error occurred while creating the file: "
									+ e1.getMessage());
					return false;
				} catch (InterruptedException e1) {
					EclipsePlugin
							.logErrorMessage("An error occurred while creating the file: "
									+ e1.getMessage());
					return false;
				}

				IWorkbenchWindow dw = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();

				selectAndReveal(file, dw);

				// Open editor on new file.
				try {
					if (dw != null) {
						IWorkbenchPage page = dw.getActivePage();
						if (page != null) {
							IDE.openEditor(page, file, true);
						}
					}
				} catch (PartInitException e) {
					MessageDialog messageDialog = new MessageDialog(
							PlatformUI.getWorkbench().getDisplay()
									.getActiveShell(),
							"Error",
							null,
							"An error occurred while opening the newly created file in editor",
							MessageDialog.ERROR, new String[] { "OK" }, 0);
					messageDialog.open();
				}
			} else {
				EclipsePlugin.logErrorMessage("Specified file already exists");
				return false;
			}

		}

		return true;
	}

	@Override
	public boolean canFinish() {
		return fWizardPage.canFinish();
	}

	private boolean isFileNameValid(String text) {
		return ((text != null) && (text.trim().length() != 0));
	}

	private boolean isTargetContainerValid(IPath targetContainer) {
		return targetContainer != null && targetContainer.segmentCount() > 0;
	}
}
