package org.eclipse.tigerstripe.workbench.convert;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

public class Converter {

	private final Shell shell;
	private final IAbstractArtifact artifact;
	private final Class<?> toClass;
	private final Set<IEditorPart> contextParts = new HashSet<IEditorPart>();
	private boolean retainAssociation;
	private IStatus result;
	
	public Converter(Shell shell, IAbstractArtifact artifact, Class<?> toClass) {
		this.shell = shell;
		this.artifact = artifact;
		this.toClass = toClass;
	}

	public Converter withContextPart(IEditorPart contextPart) {
		this.contextParts.add(contextPart);
		return this;
	}
	
	public Converter retainAssociation(boolean value) {
		retainAssociation = value;
		return this;
	}
	
	public void convert() {
		
		ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(shell);
		try {
			monitorDialog.run(false, false, new IRunnableWithProgress() {

				public void run(final IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					SafeRunner.run(new SafeRunnable() {

						public void run() throws Exception {

							final IArtifactManagerSession session = artifact
									.getProject().getArtifactManagerSession();

							ConvertArtifactOperation operation = createOperation(session);
							
							if (!operation.init(monitor)) {
								return;
							}
							
							/*
							 * Context for diagram will be added after operation execution.
							 */
							operation.addContext(WorkspaceUndoUtil
									.getWorkspaceUndoContext());
							operation.addContext(ConvertUtils.getConvertContext());
							
							if (retainAssociation) {
								
							}
							
							result = OperationHistoryFactory
									.getOperationHistory().execute(operation,
											monitor, null);
						}

						private ConvertArtifactOperation createOperation(
								final IArtifactManagerSession session) {
							
							if (artifact instanceof IAssociationClassArtifact
									&& !IAssociationArtifact.class
											.isAssignableFrom(toClass)) {

								return new ConvertArtifactRetainAssociationOperation(
										session,
										(IAssociationArtifact) artifact,
										toClass.getName(), contextParts);
							} else {
								return new ConvertArtifactOperation(session,
										artifact, toClass.getName(),
										contextParts);
							}
						}
					});
				}
			});
		} catch (Exception e) {
			EclipsePlugin.log(e);
		}
	}

	public IStatus getResult() {
		return result;
	}
}
