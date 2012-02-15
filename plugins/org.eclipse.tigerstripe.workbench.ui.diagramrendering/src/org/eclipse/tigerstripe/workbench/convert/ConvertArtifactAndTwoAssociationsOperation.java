package org.eclipse.tigerstripe.workbench.convert;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.DeleteArtifactOperation;
import org.eclipse.ui.IEditorPart;

public class ConvertArtifactAndTwoAssociationsOperation extends
		ConvertArtifactOperation {

	protected IAssociationArtifact assoc1;
	protected IAssociationArtifact assoc2;
	protected String assoc1fqn;
	protected String assoc2fqn;
	protected Decomposition decomposition; 
	
	public ConvertArtifactAndTwoAssociationsOperation(
			IArtifactManagerSession session, IAbstractArtifact artifact,
			IAssociationArtifact assoc1, IAssociationArtifact assoc2, Set<IEditorPart> contextParts) {
		super(session, artifact, IAssociationClassArtifact.class.getName(),
				contextParts);
		this.assoc1 = assoc1;
		this.assoc2 = assoc2;
	}

	@Override
	public boolean init(IProgressMonitor monitor) throws ExecutionException {
		
		assoc1fqn = assoc1.getFullyQualifiedName();
		assoc2fqn = assoc2.getFullyQualifiedName();
		
		decomposition = prepare(from, assoc1, assoc2);
		if (!decomposition.isValid()) {
			return false;
		}
		
		try {
			return super.init(monitor);
		} finally {
			assoc1 = null;
			assoc2 = null;
		}
	}

	@Override
	protected void pipelineProperties(IAbstractArtifact art,
			Map<String, Object> props) {
		if (mainFqn.equals(art.getFullyQualifiedName())) {
			props.put("AEnd", decomposition.freeEndA);
			props.put("ZEnd", decomposition.freeEndZ);
		}
	}

	@Override
	protected void pipelineDeleteOperations(List<IUndoableOperation> oprations) {
		oprations.add(0,
				new DeleteArtifactOperation(assoc1, session, true));
		oprations.add(0,
				new DeleteArtifactOperation(assoc2, session, true));

	}
	
	@Override
	protected void pipelinePartOperations(
			List<IUndoableOperation> partOperations, DiagramEditor diagramEditor) {
		partOperations.add(0, makeDeleteFromDiagram(diagramEditor, assoc1fqn));
		partOperations.add(0, makeDeleteFromDiagram(diagramEditor, assoc2fqn));
	}
	
	@Override
	protected Set<IRelationship> getRelations() throws TigerstripeException {
		Set<IRelationship> relations = super.getRelations();
		relations.remove(assoc1);
		relations.remove(assoc2);
		return relations;
	}
	
	public static class ArtifactAndTwoAssociations {
		public IAbstractArtifact artifact;
		public IAssociationArtifact one;
		public IAssociationArtifact two;

		public boolean isValid() {
			return artifact != null && one != null && two != null;
		}
	}

	public static ArtifactAndTwoAssociations extract(
			List<IAbstractArtifact> selected) {
		ArtifactAndTwoAssociations result = new ArtifactAndTwoAssociations();
		byte assocCount = 0;
		for (IAbstractArtifact art : selected) {
			if (exactlyAssociation(art)) {
				switch (assocCount) {
				case 0:
					result.one = (IAssociationArtifact) art;
					break;
				case 1:
					result.two = (IAssociationArtifact) art;
					break;
				}
				++assocCount;
			} else {
				result.artifact = art;
			}
		}
		return result;
	}
	
	private static boolean exactlyAssociation(IAbstractArtifact artifact) {
		return (artifact instanceof IAssociationArtifact)
				&& !(artifact instanceof IAssociationClassArtifact);
	}
	
	public static class Decomposition {
		public IAssociationEnd freeEndA;
		public IAssociationEnd freeEndZ;
		
		public boolean isValid() {
			return freeEndA != null && freeEndZ != null;
		}
	}
	
	public static Decomposition prepare(
			IAbstractArtifact artifact, IAssociationArtifact assoc1,
			IAssociationArtifact assoc2) {

		IAssociationEnd freeEnd1 = null;
		IAssociationEnd freeEnd2 = null;
		
		String fqn = artifact.getFullyQualifiedName();
		
		{
			IAssociationEnd aEnd = assoc1.getAEnd();
			IAssociationEnd zEnd = assoc1.getZEnd();
			if (fqn.equals(aEnd.getType().getFullyQualifiedName())) {
				freeEnd1 = zEnd;
			} else if (fqn.equals(zEnd.getType().getFullyQualifiedName())) {
				freeEnd1 = aEnd;
			}
		}

		{
			IAssociationEnd aEnd = assoc2.getAEnd();
			IAssociationEnd zEnd = assoc2.getZEnd();
			if (fqn.equals(aEnd.getType().getFullyQualifiedName())) {
				freeEnd2 = zEnd;
			} else if (fqn.equals(zEnd.getType().getFullyQualifiedName())) {
				freeEnd2 = aEnd;
			}
		}

		Decomposition result = new Decomposition();
		
		if (freeEnd1 == null || freeEnd2 == null) {
			return result;
		}
		
		if (assoc1.getAEnd().equals(freeEnd1)) {
			result.freeEndA = freeEnd1;
			result.freeEndZ = freeEnd2;
		} else {
			result.freeEndZ = freeEnd1;
			result.freeEndA = freeEnd2;
		}
		return result;
	}
}