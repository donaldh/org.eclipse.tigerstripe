package org.eclipse.tigerstripe.workbench.convert;

import static org.eclipse.core.runtime.Status.CANCEL_STATUS;
import static org.eclipse.tigerstripe.workbench.convert.ConvertArtifactAndTwoAssociationsOperation.extract;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.convert.ConvertArtifactAndTwoAssociationsOperation.ArtifactAndTwoAssociations;
import org.eclipse.tigerstripe.workbench.model.ArtifactUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

public class Converter {

	private final Shell shell;
	private final Class<?> toClass;
	private final Set<IEditorPart> contextParts = new HashSet<IEditorPart>();
	private final List<IAbstractArtifact> artifacts;
	private IStatus result = CANCEL_STATUS;
	
	public Converter(Shell shell, Class<?> toClass,
			List<IAbstractArtifact> artifacts) {
		this.shell = shell;
		this.toClass = toClass;
		this.artifacts = artifacts;
	}

	public Converter withContextPart(IEditorPart contextPart) {
		this.contextParts.add(contextPart);
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
							doRun(monitor);
						}
					});
				}
			});
		} catch (Exception e) {
			EclipsePlugin.log(e);
		}
	}

	private void doRun(final IProgressMonitor monitor)
			throws ExecutionException {
		ConvertArtifactOperation operation;

		switch (artifacts.size()) {
		case 1: {
			IAbstractArtifact artifact = artifacts.get(0);
			IArtifactManagerSession session = ArtifactUtils
					.getSession(artifact);
			if (session == null) {
				return;
			}
			if (artifact instanceof IAssociationClassArtifact
					&& !IAssociationArtifact.class.isAssignableFrom(toClass)) {

				operation = new ConvertAssociationClassOperation(session,
						(IAssociationClassArtifact) artifact,
						toClass.getName(), contextParts);
			} else {
				operation = new ConvertArtifactOperation(session, artifact,
						toClass.getName(), contextParts);
			}
			break;
		}
		case 3: {
			if (!IAssociationClassArtifact.class.isAssignableFrom(toClass)) {
				return;
			}
			
			ArtifactAndTwoAssociations extracted = extract(artifacts);
			if (!extracted.isValid()) {
				return;
			}

			IArtifactManagerSession session = ArtifactUtils
					.getSession(extracted.artifact);
			if (session == null) {
				return;
			}
			operation = new ConvertArtifactAndTwoAssociationsOperation(session,
					extracted.artifact, extracted.one, extracted.two,
					contextParts);
			break;
		}
		default:
			return;
		}

		if (!operation.init(monitor)) {
			return;
		}

		/*
		 * Context for diagram will be added after operation execution.
		 */
		operation.addContext(WorkspaceUndoUtil.getWorkspaceUndoContext());
		operation.addContext(ConvertUtils.getConvertContext());

		result = OperationHistoryFactory.getOperationHistory().execute(
				operation, monitor, null);
	}
	
	public IStatus getResult() {
		return result;
	}
}
