package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import static org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory.ARTIFACT_DELETE;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateExistingRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.internal.core.util.CheckUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ReadOnlyArtifactEditorInput;
import org.eclipse.tigerstripe.workbench.utils.AdaptHelper;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.DeleteResourcesOperation;

public class DeleteArtifactOperation extends AbstractOperation {

	private final IArtifactManagerSession session;
	private final IUndoableOperation deleteResourceOperation;
	private final IAbstractArtifact artifact;
	private final boolean closeEditors;

	public DeleteArtifactOperation(IAbstractArtifact artifact,
			IArtifactManagerSession session, boolean deleteResourse) {

		super(String.format("Delete Operation for Artifact '%s'",
				artifact.getFullyQualifiedName()));

		this.artifact = CheckUtils.notNull(artifact, "artifact");
		this.session = CheckUtils.notNull(session, "session");

		String opName = String.format(
				"Delete Resource Operation for Artifact '%s'",
				artifact.getFullyQualifiedName());

		if (deleteResourse) {
			IResource resource = (IResource) artifact
					.getAdapter(IResource.class);
			if (resource != null) {
				deleteResourceOperation = new DeleteResourcesOperation(
						new IResource[] { resource }, opName, false);
			} else {
				deleteResourceOperation = new EmptyOperation();
			}
		} else {
			deleteResourceOperation = new EmptyOperation();
		}
		closeEditors = deleteResourceOperation instanceof EmptyOperation;
	}

	public <T extends IModelChangeRequest> T getRequest(Class<T> clazz,
			String id, IModelUpdater updater) throws TigerstripeException {
		IModelChangeRequest request = updater.getRequestFactory().makeRequest(
				id);
		return clazz.cast(request);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {

		try {
			IModelUpdater updater = session.getIModelUpdater();
			IArtifactDeleteRequest deleteRequest = getRequest(
					IArtifactDeleteRequest.class, ARTIFACT_DELETE, updater);
			deleteRequest.setArtifactName(artifact.getName());
			deleteRequest.setArtifactPackage(artifact.getPackage());
			updater.handleChangeRequestSynch(deleteRequest);
		} catch (TigerstripeException e) {
			return EclipsePlugin.getStatus(e);
		}
		IStatus status = deleteResourceOperation.execute(monitor, info);
		if (closeEditors && status.isOK()) {
			closeEditors();
		}
		return status;
	}

	private void closeEditors() {
		for (IWorkbenchWindow w : PlatformUI.getWorkbench()
				.getWorkbenchWindows()) {
			for (IWorkbenchPage page : w.getPages()) {

				for (IEditorReference ref : page.getEditorReferences()) {
					try {
						IEditorInput editorInput = ref.getEditorInput();
						if (editorInput instanceof IFileEditorInput) {
							IAbstractArtifact artifact = AdaptHelper.adapt(
									((IFileEditorInput) editorInput).getFile(),
									IAbstractArtifact.class);
							if (artifact != null && isDeletingArtifact(artifact)) {
								page.closeEditor(ref.getEditor(false), false);
							}
						} else if (editorInput instanceof ReadOnlyArtifactEditorInput) {
							IAbstractArtifact artifact = ((ReadOnlyArtifactEditorInput) editorInput)
									.getArtifact();
							if (artifact != null
									&& isDeletingArtifact(artifact)) {
								page.closeEditor(ref.getEditor(false), false);
							}
						}
					} catch (PartInitException e) {
						BasePlugin.log(e);
					}
				}
			}
		}
	}

	private boolean isDeletingArtifact(IAbstractArtifact artifact) {
		return this.artifact.getFullyQualifiedName().equals(
				artifact.getFullyQualifiedName());
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {

		try {
			IModelUpdater updater = session.getIModelUpdater();
			IArtifactCreateExistingRequest createRequest = getRequest(
					IArtifactCreateExistingRequest.class,
					IModelChangeRequestFactory.ARTIFACT_CREATE_EXISTING,
					updater);

			createRequest.setArtifact(artifact);

			updater.handleChangeRequestSynch(createRequest);

		} catch (TigerstripeException e) {
			return EclipsePlugin.getStatus(e);
		}

		return deleteResourceOperation.undo(monitor, info);
	}

}
