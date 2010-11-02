package org.eclipse.tigerstripe.postgeneration.sample;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.IGenerateCompleteListener;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.PlatformUI;

/**
 * Invoked on a generation that fails
 * This class generates an error dialog upon generation failure.
 * 
 * @author Navid Mehregani
 *
 */
public class PostFailedGeneration implements IGenerateCompleteListener {

	@Override
	public PluginRunStatus run(GenerateCompletionStatus status, 
			ITigerstripeModelProject project, PluginRunStatus[] runStatus) {
		
		if (!GenerateCompletionStatus.FAILURE.equals(status)) {
			Activator.logErrorMessage("Something is wrong! This should only get invoked on failed generators");
			return null;
		}
		
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
		                "Generation Failed!",
		                "This is a custom 'post-generation' action invoked on a generation failure.");

			}
		});
		
		
		return null;
	}

}
