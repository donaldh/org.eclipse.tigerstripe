package org.eclipse.tigerstripe.workbench.convert;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.workspace.ResourceUndoContext;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers.InstanceDiagramEditorHelper;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.Lifecycle;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.DiagramEditorHelper;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

public class ConverterLifecycle implements Lifecycle {

	public void init(DiagramEditPart editPart, DiagramEditor editorPart, IUndoContext undoContext) {
		EObject model = editPart.getDiagramView().getElement();
		if (model instanceof Map) {
			correctClass(editPart, editorPart);
		} else if (model instanceof InstanceMap) {
			correctInstance(editPart, editorPart);
		}
		addUndoContext(editPart, editorPart, undoContext);
	}

	private void correctClass(DiagramEditPart editPart, final DiagramEditor editorPart) {
		DiagramEditorHelper helper = new DiagramEditorHelper(editorPart);
		ITigerstripeModelProject project = helper.getCorrespondingTigerstripeProject();
		if (project != null) {
			try {
				ArtifactManager artifactManager = project.getArtifactManagerSession().getArtifactManager();
				new ClassDiagramCorrector().correct(editPart, artifactManager, new NullProgressMonitor());
				
				//Save after canonical edit policy 
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					
					public void run() {
						editorPart.doSave(new NullProgressMonitor());
					}
				});
				
			} catch (TigerstripeException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void correctInstance(DiagramEditPart editPart, final DiagramEditor editorPart) {
		InstanceDiagramEditorHelper helper = new InstanceDiagramEditorHelper(editorPart);
		ITigerstripeModelProject project = helper.getCorrespondingTigerstripeProject();
		if (project != null) {
			try {
				ArtifactManager artifactManager = project.getArtifactManagerSession().getArtifactManager();
				new InstanceDiagramCorrector().correct(editPart, artifactManager, new NullProgressMonitor());
				
				//Save after canonical edit policy 
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					
					public void run() {
						editorPart.doSave(new NullProgressMonitor());
					}
				});
				
			} catch (TigerstripeException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private void addUndoContext(DiagramEditPart editPart, EditorPart editorPart, IUndoContext undoContext) {
		for (IUndoableOperation op : getOperations()) {
			op.addContext(undoContext);
			
			ResourceUndoContext resourceUndoContext = new ResourceUndoContext(editPart.getEditingDomain(), editPart.getDiagramView().eResource());
			if (!op.hasContext(resourceUndoContext)) {
				op.addContext(resourceUndoContext);
			}
		}
	}

	private Collection<IUndoableOperation> getOperations() {
		IUndoableOperation[] undoHistory = OperationHistoryFactory.getOperationHistory().getUndoHistory(ConvertUtils.getConvertContext());
		IUndoableOperation[] redoHistory = OperationHistoryFactory.getOperationHistory().getRedoHistory(ConvertUtils.getConvertContext());
		HashSet<IUndoableOperation> set = new HashSet<IUndoableOperation>(undoHistory.length + redoHistory.length);
		set.addAll(Arrays.asList(undoHistory));
		set.addAll(Arrays.asList(redoHistory));
		return set;
	}
	
	public void dispose(DiagramEditPart editPart, DiagramEditor editorPart, IUndoContext undoContext) {
		for (IUndoableOperation op : getOperations()) {
			if (op instanceof ConvertArtifactOperation) {
				((ConvertArtifactOperation) op).removeEditor(editorPart);
			}
			op.removeContext(undoContext);
			
			ResourceUndoContext resourceUndoContext = new ResourceUndoContext(
					editPart.getEditingDomain(), editPart.getDiagramView()
							.eResource());
			
			for (IUndoContext ctx : op.getContexts()) {
				if (ctx instanceof ResourceUndoContext) {
					if (ctx.matches(resourceUndoContext)) {
						op.removeContext(ctx);
					}
				}
			}
		}
	}
}
