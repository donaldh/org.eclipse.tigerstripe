package org.eclipse.tigerstripe.workbench.convert;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.DeleteArtifactOperation;
import org.eclipse.ui.IEditorPart;

public class ConvertArtifactAndAssociationOperation extends
		ConvertArtifactOperation {

	protected String associationFQN; 
	protected IAssociationArtifact association;
	
	public ConvertArtifactAndAssociationOperation(
			IArtifactManagerSession session, IAbstractArtifact artifact,
			IAssociationArtifact association, Set<IEditorPart> contextParts) {
		super(session, artifact, IAssociationClassArtifact.class.getName(),
				contextParts);
		this.association = association;
	}

	@Override
	public boolean init(IProgressMonitor monitor) throws ExecutionException {
		associationFQN = association.getFullyQualifiedName();
		try {
			return super.init(monitor);
		} finally {
			association = null;
		}
	}

	@Override
	protected void pipelineProperties(IAbstractArtifact art,
			Map<String, Object> props) {
		if (mainFqn.equals(art.getFullyQualifiedName())) {
			props.put("AEnd", association.getAEnd());
			props.put("ZEnd", association.getZEnd());
		}
	}

	@Override
	protected void pipelineDeleteOperations(List<IUndoableOperation> oprations) {
		oprations.add(0,
				new DeleteArtifactOperation(association, session, true));
	}
	
	@Override
	protected void pipelinePartOperations(
			List<IUndoableOperation> partOperations, DiagramEditor diagramEditor) {
		partOperations.add(0, makeDeleteFromDiagram(diagramEditor, associationFQN));
	}
}
