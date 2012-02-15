package org.eclipse.tigerstripe.workbench.convert;

import static java.util.Arrays.asList;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IEditorPart;

public class ConvertAssociationClassOperation extends ConvertArtifactOperation {

	private IAssociationEnd aEnd;
	private IAssociationEnd zEnd;
	private String comment;
	private EVisibility visibility;
	
	private IAssociationArtifact asscA;
	private IAssociationArtifact asscZ;

	private final Provider<IAbstractArtifact> aProvider = new Provider<IAbstractArtifact>() {

		public IAbstractArtifact get() {
			return asscA;
		}
	};
	
	private final Provider<IAbstractArtifact> zProvider = new Provider<IAbstractArtifact>() {

		public IAbstractArtifact get() {
			return asscZ;
		}
	};
	
	public ConvertAssociationClassOperation(
			IArtifactManagerSession session, IAssociationClassArtifact from,
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
	@SuppressWarnings("unchecked")
	protected void pipelinePartOperations(
			List<IUndoableOperation> partOperations, DiagramEditor diagramEditor) {
		super.pipelinePartOperations(partOperations, diagramEditor);
		
		Tuple<Point, Dimension> ps1 = Tuple.empty();
		Tuple<Point, Dimension> ps2 = Tuple.empty();
		partOperations.add(new DefferedDropOperation(diagramEditor
				.getDiagramEditPart(), asList(aProvider, zProvider), asList(ps1, ps2)));
	}	
	
	private void createAssociations() throws ExecutionException {
		
		Provider<IAbstractArtifact> provider = providers.get(mainFqn);
		
		if (provider == null) {
			return;
		}
		
		IAbstractArtifact converted = provider.get();
		{
			asscA = (IAssociationArtifact) session.makeArtifact(IAssociationArtifact.class.getName());
			asscA.setFullyQualifiedName(generateFqn(1));
			asscA.setVisibility(visibility);
			asscA.setComment(comment);
			asscA.setAEnd(aEnd);
			
			IAssociationEnd end = asscA.makeAssociationEnd();
			copy(zEnd, end);
			IType type = end.makeType();
			type.setFullyQualifiedName(converted.getFullyQualifiedName());
			end.setType(type);
			asscA.setZEnd(end);
		}
		
		{
			asscZ = (IAssociationArtifact) session.makeArtifact(IAssociationArtifact.class.getName());
			asscZ.setFullyQualifiedName(generateFqn(2));
			asscZ.setVisibility(visibility);
			asscZ.setComment(comment);
			asscZ.setZEnd(zEnd);
	
			IAssociationEnd end = asscZ.makeAssociationEnd();
			copy(aEnd, end);
			IType type = end.makeType();
			type.setFullyQualifiedName(converted.getFullyQualifiedName());
			end.setType(type);
			asscZ.setAEnd(end);
		}
		
		IModelUpdater updater = session.getIModelUpdater();
		try {
			
			IArtifactCreateExistingRequest reqA = updater.getRequestFactory()
			.makeRequest(IArtifactCreateExistingRequest.class);
			reqA.setArtifact(asscA);
			updater.handleChangeRequestSynch(reqA);
			
			IArtifactCreateExistingRequest reqZ = updater.getRequestFactory()
			.makeRequest(IArtifactCreateExistingRequest.class);
			reqZ.setArtifact(asscZ);
			updater.handleChangeRequestSynch(reqZ);
			
		} catch (TigerstripeException e) {
			throw new ExecutionException("Can't create association for artifact " + mainFqn, e);
		}
	}

	public void copy(IAssociationEnd from, IAssociationEnd to) {
		to.setAggregation(from.getAggregation());
		to.setChangeable(from.getChangeable());
		to.setComment(from.getComment());
		//to.setMultiplicity(from.getMultiplicity());
		to.setMultiplicity(IModelComponent.EMultiplicity.ONE);
		to.setName(from.getName());
		to.setNavigable(from.isNavigable());
		to.setOrdered(from.isOrdered());
		to.setType(from.getType());
		to.setUnique(from.isUnique());
		to.setVisibility(from.getVisibility());
	}
	
	@Override
	protected void pipelineCreateOperations(List<IUndoableOperation> operations) {
		operations.add(new CreateAssociationsOperation());
	}
	
	private void removeAssociations(IProgressMonitor monitor)
			throws ExecutionException {

		remove(asscA, monitor);
		remove(asscZ, monitor);
		
		asscA = null;
		asscZ = null;
	}

	private void remove(IAbstractArtifact artifact, IProgressMonitor monitor) throws ExecutionException {
		IModelUpdater updater = session.getIModelUpdater();
		try {
			
			IArtifactDeleteRequest req = updater.getRequestFactory()
					.makeRequest(IArtifactDeleteRequest.class);
			req.setArtifactPackage(artifact.getPackage());
			req.setArtifactName(artifact.getName());
			updater.handleChangeRequestSynch(req);
			
		} catch (TigerstripeException e) {
			throw new ExecutionException("Error during delete artifact "
					+ artifact.getFullyQualifiedName());
		}
		
		IResource resource = adapt(artifact, IResource.class);
		if (resource != null) {
			try {
				resource.delete(true, monitor);
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		}
	}
	
	private String generateFqn(int start) {
		String base = mainFqn + "Association";
		String fqn = base + start;
		for (int i = 2; session.getArtifactByFullyQualifiedName(fqn, true) != null; ++i) {
			fqn = base + i;
		}
		return fqn;
	}
	
	private class CreateAssociationsOperation extends AbstractOperation {

		public CreateAssociationsOperation() {
			super("Create Associations");
		}

		@Override
		public IStatus execute(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			createAssociations();
			return Status.OK_STATUS;
		}

		@Override
		public IStatus redo(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			createAssociations();
			return Status.OK_STATUS;
		}

		@Override
		public IStatus undo(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException {
			removeAssociations(monitor);
			return Status.OK_STATUS;
		}
	}
}
