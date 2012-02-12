package org.eclipse.tigerstripe.workbench.convert;

import static java.util.Collections.singletonList;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateExistingRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.internal.core.util.Provider;
import org.eclipse.tigerstripe.workbench.internal.core.util.Tuple;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IEditorPart;

public class ConvertArtifactRetainAssociationOperation extends ConvertArtifactOperation {

	private IAssociationEnd aEnd;
	private IAssociationEnd zEnd;
	private String comment;
	private EVisibility visibility;
	
	private IAssociationArtifact artifact;

	private final Provider<IAbstractArtifact> artifactProvider = new Provider<IAbstractArtifact>() {

		public IAbstractArtifact get() {
			return artifact;
		}
	};
	
	public ConvertArtifactRetainAssociationOperation(
			IArtifactManagerSession session, IAssociationArtifact from,
			String toType, Set<IEditorPart> contextParts) {
		super(session, from, toType, contextParts);
	}
	
	@Override
	public boolean init(IProgressMonitor monitor) throws ExecutionException {
		aEnd = ((IAssociationArtifact) from).getAEnd();
		zEnd = ((IAssociationArtifact) from).getZEnd();
		comment = from.getComment();
		visibility = from.getVisibility();
		return super.init(monitor);
	}
	
	@Override
	protected void pipelinePartOperations(
			List<IUndoableOperation> partOperations, DiagramEditor diagramEditor) {
		super.pipelinePartOperations(partOperations, diagramEditor);
		List<Provider<IAbstractArtifact>> providers = Collections
				.<Provider<IAbstractArtifact>> singletonList(artifactProvider);
		Tuple<Point, Dimension> ps = Tuple.empty();
		partOperations.add(new DefferedDropOperation(diagramEditor
				.getDiagramEditPart(), providers, singletonList(ps)));
	}	
	
	private void createAssociation() throws ExecutionException {
		artifact = (IAssociationArtifact) session.makeArtifact(IAssociationArtifact.class.getName());
		artifact.setFullyQualifiedName(generateFqn());
		artifact.setVisibility(visibility);
		artifact.setComment(comment);
		artifact.setAEnd(aEnd);
		artifact.setZEnd(zEnd);
		
		IModelUpdater updater = session.getIModelUpdater();
		try {
			IArtifactCreateExistingRequest req = updater.getRequestFactory()
			.makeRequest(IArtifactCreateExistingRequest.class);
			req.setArtifact(artifact);
			updater.handleChangeRequestSynch(req);
		} catch (TigerstripeException e) {
			throw new ExecutionException("Can't create association for artifact " + mainFqn, e);
		}
	}

	@Override
	protected void doExecute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		createAssociation();
		super.doExecute(monitor, info);
	}

	@Override
	protected void doRedo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		createAssociation();
		super.doRedo(monitor, info);
	}
	
	@Override
	protected void doUndo(IProgressMonitor monitor, IAdaptable info)
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
		
		IResource resource = adapt(artifact, IResource.class);
		if (resource != null) {
			try {
				resource.delete(true, monitor);
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		}
		
		artifact = null;
		super.doUndo(monitor, info);
	}
	
	private String generateFqn() {
		String base = mainFqn + "Association";
		String fqn = base;
		for (int i = 0; session.getArtifactByFullyQualifiedName(fqn, true) != null; ++i) {
			fqn = base + i;
		}
		return fqn;
	}
}

