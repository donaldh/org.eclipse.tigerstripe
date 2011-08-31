package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import java.util.Collections;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateExistingRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.internal.core.util.Provider;
import org.eclipse.tigerstripe.workbench.model.ModelUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IArtifactFormContentProvider;

@SuppressWarnings("deprecation")
public class CreateArtifactOperation extends AbstractOperation implements
		Provider<IAbstractArtifact> {

	private final IArtifactManagerSession session;
	private final String type;
	private final Map<String, Object> properties;
	private IAbstractArtifact artifact;

	public CreateArtifactOperation(IArtifactManagerSession session,
			String type, Map<String, Object> properties) {
		super("Create Artifact " + type);
		this.session = session;
		this.type = type;
		this.properties = properties;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {

		artifact = session.makeArtifact(type);
		Class<?> mapClass = artifact instanceof IAssociationArtifact ? IAssociationArtifact.class
				: IAbstractArtifact.class;
		ModelUtils.setProperties(mapClass, artifact, properties);

		cleanUnrelevantProps();
		
		IModelUpdater updater = session.getIModelUpdater();
		try {
			IArtifactCreateExistingRequest req = updater.getRequestFactory()
					.makeRequest(IArtifactCreateExistingRequest.class);
			req.setArtifact(artifact);
			updater.handleChangeRequestSynch(req);
		} catch (TigerstripeException e) {
			throw new ExecutionException("Can't create artifact " + type, e);
		}
		return Status.OK_STATUS;
	}

	private void cleanUnrelevantProps() {
		IArtifactFormContentProvider contentProvider = (IArtifactFormContentProvider) Platform
		.getAdapterManager().getAdapter(artifact,
				IArtifactFormContentProvider.class);
		
		if (contentProvider != null) {
			if (!contentProvider.hasAttributes()) {
				artifact.setFields(Collections.<IField>emptySet());
			}
			if (!contentProvider.hasMethods()) {
				artifact.setMethods(Collections.<IMethod>emptySet());
			}
			if (!contentProvider.hasConstants()) {
				artifact.setLiterals(Collections.<ILiteral>emptySet());
			}
		}
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {

		IModelUpdater updater = session.getIModelUpdater();
		try {
			IArtifactDeleteRequest req = updater.getRequestFactory()
					.makeRequest(IArtifactDeleteRequest.class);
			req.setArtifactPackage(artifact.getPackage());
			req.setArtifactName(artifact.getName());
			updater.handleChangeRequestSynch(req);
		} catch (TigerstripeException e) {
			throw new ExecutionException(String.format(
					"Error during delete artifact '%s'",
					artifact.getFullyQualifiedName()));
		}
		artifact = null;
		return Status.OK_STATUS;
	}

	public IAbstractArtifact get() {
		return artifact;
	}
}
