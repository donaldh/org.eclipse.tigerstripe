package org.eclipse.tigerstripe.workbench.convert;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

public class Converter {

	public static void convert(Shell shell, final IAbstractArtifact artifact,
			final Class<?> toClass, final IEditorPart... contextParts) {
		final IStatus[] sc = new IStatus[1];
		
		ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(shell);
		try {
			monitorDialog.run(false, false, new IRunnableWithProgress() {

				public void run(final IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					SafeRunner.run(new SafeRunnable() {

						public void run() throws Exception {

							final IArtifactManagerSession session = artifact
									.getProject().getArtifactManagerSession();

							ConvertArtifactOperation operation = ConvertArtifactOperation
									.make(session, artifact,
											toClass.getName(), contextParts);
							
							if (!operation.init(monitor)) {
								return;
							}
							
							/*
							 * Context for diagram will be added after operation execution.
							 */
							operation.addContext(WorkspaceUndoUtil
									.getWorkspaceUndoContext());
							operation.addContext(ConvertUtils.getConvertContext());
							sc[0] = OperationHistoryFactory
									.getOperationHistory().execute(operation,
											monitor, null);
						}
					});
				}
			});
		} catch (Exception e) {
			EclipsePlugin.log(e);
		}
	}
}
