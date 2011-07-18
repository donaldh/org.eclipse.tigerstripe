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
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateExistingRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.internal.core.util.CheckUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.ide.undo.DeleteResourcesOperation;

@SuppressWarnings("deprecation")
public class DeleteArtifactOperation extends AbstractOperation {

	private final IArtifactManagerSession session;
	private final IUndoableOperation deleteResourceOperation;
	private final IAbstractArtifact artifact;

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

		return deleteResourceOperation.execute(monitor, info);
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
