package org.eclipse.tigerstripe.workbench.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.internal.core.util.Tuple;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceDiagramEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.EmptyOperation;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

public class ConvertUtils {

	public static Tuple<Point, Dimension> getPointAndSize(DiagramEditPart editPart, String fqn) {
		Diagram diagram = editPart.getDiagramView();
		EObject model = diagram.getElement();
		
		EObject modelArtifact = null;
		if (model instanceof org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) {
	
			org.eclipse.tigerstripe.workbench.ui.visualeditor.Map map = (org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) model;
			
			for (Object o : map.getArtifacts()) {
				QualifiedNamedElement qe = (QualifiedNamedElement) o;
				if (fqn.equals(qe.getFullyQualifiedName())) {
					modelArtifact = qe;
					break;
				}
			}
		}
		
		if (modelArtifact != null) {
			@SuppressWarnings({"unchecked"})
			List<INodeEditPart> found = ((IDiagramGraphicalViewer)editPart.getViewer())
					.findEditPartsForElement(EMFCoreUtil.getProxyID(modelArtifact),
							INodeEditPart.class);
			for (INodeEditPart nep : found) {
				View view = (View) nep.getModel();
				
				EObject element = view.getElement();
				if (element instanceof QualifiedNamedElement) {
					if (fqn.equals(((QualifiedNamedElement) element).getFullyQualifiedName())) {
						Object x = ViewUtil.getStructuralFeatureValue(view, NotationPackage.Literals.LOCATION__X);
						Object y = ViewUtil.getStructuralFeatureValue(view, NotationPackage.Literals.LOCATION__Y);
	
						Object w = ViewUtil.getStructuralFeatureValue(view, NotationPackage.Literals.SIZE__WIDTH);
						Object h = ViewUtil.getStructuralFeatureValue(view, NotationPackage.Literals.SIZE__HEIGHT);
	
						Point point = null;
						Dimension dimension = null;
						if (x != null && y != null) {
							point = new Point((Integer)x, (Integer)y);
						}
						
						if (w != null && h != null) {
							dimension = new Dimension((Integer)w, (Integer)h);
						}
						
						return new Tuple<Point, Dimension>(point, dimension);
					}
				}
			}
		}
			
		return new Tuple<Point, Dimension>(null, null);
	}
	
	public static IUndoableOperation makeDeleteFromClassDiagramCommand(
			final DiagramEditPart part, final String fqn) {
		
		final EObject element = part.getDiagramView().getElement();
		if (element instanceof org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) {
			return new AbstractTransactionalCommand(part.getEditingDomain(),
					"Delete "+fqn, null) {

				@Override
				protected CommandResult doExecuteWithResult(
						IProgressMonitor monitor, IAdaptable info)
						throws ExecutionException {

					org.eclipse.tigerstripe.workbench.ui.visualeditor.Map map = (org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) element;
					if (fqn != null){
						deleteArtifactByFqn(fqn, map.getArtifacts());
						deleteArtifactByFqn(fqn, map.getAssociations());
						deleteArtifactByFqn(fqn, map.getDependencies());
					}
					
					return CommandResult.newOKCommandResult();
				}

				private void deleteArtifactByFqn(final String fqn, List<?> list) {
					Iterator<?> it = list.iterator();
					while (it.hasNext()) {
						QualifiedNamedElement qe = (QualifiedNamedElement) it
								.next();
						if (qe == null) {
							continue;
						}
						if (fqn.equals(qe.getFullyQualifiedName())) {
							it.remove();
							break;
						}
					}
				}
			};
		}

		return new EmptyOperation();
	}

	public static IUndoableOperation makeDeleteFromInstanceDiagramCommand(
			final DiagramEditPart part, final String fqn) {
		return makeDeleteFromInstanceDiagramCommand(part, Collections.singleton(fqn));
	}
	
	public static IUndoableOperation makeDeleteFromInstanceDiagramCommand(
			final DiagramEditPart part, final Set<String> fqn) {
		
		final EObject element = part.getDiagramView().getElement();
		if (element instanceof InstanceMap) {
			return new AbstractTransactionalCommand(part.getEditingDomain(),
					"Delete "+fqn, null) {

				@Override
				protected CommandResult doExecuteWithResult(
						IProgressMonitor monitor, IAdaptable info)
						throws ExecutionException {

					deleteFormInstanceDiagram(fqn, (InstanceMap) element);
					
					return CommandResult.newOKCommandResult();
				}
			};
		}

		return new EmptyOperation();
	}

	public static void deleteFormInstanceDiagram(final Set<String> fqn, final InstanceMap map) {
		deleteInstanceByFqn(fqn, map.getClassInstances());
		deleteInstanceByFqn(fqn, map.getAssociationInstances());
		for (Object o : map.getClassInstances()) {
			ClassInstance ci = (ClassInstance) o;
			deleteInstanceByFqn(fqn, ci.getAssociations());
		}
	}
	
	public static void deleteInstanceByFqn(final Set<String> fqn, @SuppressWarnings("rawtypes") EList list) {
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			Instance i = (Instance) it.next();
			if (i == null) {
				continue;
			}
			if (fqn.contains(i.getFullyQualifiedName())) {
				it.remove();
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void refreshInstance(InstanceDiagramEditor editor, Set<String> fqns) {
	
		InstanceMap map = (InstanceMap) editor.getDiagram().getElement();
		
		List<EObject> semantic = new ArrayList<EObject>();
		
		List<Instance> instances = new ArrayList<Instance>(map.getAssociationInstances().size() + map.getClassInstances().size());
		instances.addAll(map.getAssociationInstances());
		instances.addAll(map.getClassInstances());
		
		for (Object object : instances) {
			Instance i = (Instance) object;
			if (fqns.contains(i.getFullyQualifiedName())) {
				semantic.add(i);
			}
		}

		for (EObject e : semantic) {
			List<INodeEditPart> partsForElement = editor
					.getDiagramGraphicalViewer().findEditPartsForElement(
							EMFCoreUtil.getProxyID(e), INodeEditPart.class);
			for (INodeEditPart part : partsForElement) {
				
				for (Object child : part.getChildren()) {
					((EditPart) child).refresh();
				}
			}
		}
	}
	
	private static final IUndoContext convertContext = new ObjectUndoContext(
			ConvertArtifactOperation.class);

	public static IUndoContext getConvertContext() {
		return convertContext;
	}
	
}
