package org.eclipse.tigerstripe.workbench.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.util.ImmutableProvider;
import org.eclipse.tigerstripe.workbench.internal.core.util.Provider;
import org.eclipse.tigerstripe.workbench.internal.core.util.Tuple;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.CompositeOperation;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.VisualeditorSwitch;

public class ClassDiagramCorrector {

	@SuppressWarnings("unchecked")
	public void correct(DiagramEditPart part, ArtifactManager artifactManager,
			IProgressMonitor monitor) {

		Diagram diagram = part.getDiagramView();

		Map map = (Map) diagram.getElement();

		List<QualifiedNamedElement> artifacts = new ArrayList<QualifiedNamedElement>(); 

		artifacts.addAll(map.getArtifacts());
		artifacts.addAll(map.getAssociations());
		artifacts.addAll(map.getDependencies());
		
		List<IUndoableOperation> commands = new ArrayList<IUndoableOperation>();
		for (QualifiedNamedElement qe : artifacts) {

			String fqn = qe.getFullyQualifiedName();
			IAbstractArtifactInternal wArt = artifactManager
				.getArtifactByFullyQualifiedName(fqn, true, monitor);

			if (wArt == null) {
				commands.add(ConvertUtils.makeDeleteFromClassDiagramCommand(
						part, fqn));
  			}
			if (qe instanceof AbstractArtifact) {
				AbstractArtifact dArt = (AbstractArtifact) qe;
				Class<? extends IAbstractArtifact> workspaceType = getWorkspaceType(dArt);
				
				if (wArt == null) {
					commands.add(ConvertUtils.makeDeleteFromClassDiagramCommand(
							part, fqn));
				} else if (workspaceType != null && !workspaceType.isInstance(wArt)) {
					
					IUndoableOperation deleteCommand = ConvertUtils
					.makeDeleteFromClassDiagramCommand(part, fqn);
					Tuple<Point, Dimension> ps = ConvertUtils.getPointAndSize(part,
							fqn);
					List<Provider<IAbstractArtifact>> providers = Collections
					.<Provider<IAbstractArtifact>> singletonList(new ImmutableProvider<IAbstractArtifact>(
							wArt));
					DefferedDropOperation dropOperation = new DefferedDropOperation(
							part, providers, Collections.singletonList(ps));
					
					CompositeOperation correctCommand = new CompositeOperation(
							"Correct " + fqn, Arrays.asList(deleteCommand,
									dropOperation));
					commands.add(correctCommand);
				}
			}
		}
		doConvert(commands, null);
	}

	private void doConvert(List<IUndoableOperation> commands,
			IProgressMonitor monitor) {
		if (commands.isEmpty()) {
			return;
		}
		try {
			new CompositeOperation("Correct", commands).execute(monitor, null);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends IAbstractArtifact> getWorkspaceType(
			AbstractArtifact dArt) {

		return (Class<? extends IAbstractArtifact>) new VisualeditorSwitch() {

			@Override
			public Object caseAssociation(Association object) {
				return IAssociationArtifact.class;
			}

			@Override
			public Object caseAssociationClass(AssociationClass object) {
				return IAssociationClassArtifact.class;
			}

			@Override
			public Object caseAssociationClassClass(AssociationClassClass object) {
				return IAssociationClassArtifact.class;
			}

			@Override
			public Object caseDatatypeArtifact(DatatypeArtifact object) {
				return IDatatypeArtifact.class;
			}

			@Override
			public Object caseDependency(Dependency object) {
				return IDependencyArtifact.class;
			}

			@Override
			public Object caseEnumeration(Enumeration object) {
				return IEnumArtifact.class;
			}

			@Override
			public Object caseExceptionArtifact(ExceptionArtifact object) {
				return IExceptionArtifact.class;
			}

			@Override
			public Object caseNotificationArtifact(NotificationArtifact object) {
				return IEventArtifact.class;
			}

			@Override
			public Object caseSessionFacadeArtifact(SessionFacadeArtifact object) {
				return ISessionArtifact.class;
			}

			@Override
			public Object caseUpdateProcedureArtifact(
					UpdateProcedureArtifact object) {
				return IUpdateProcedureArtifact.class;
			}

			@Override
			public Object caseManagedEntityArtifact(ManagedEntityArtifact object) {
				return IManagedEntityArtifact.class;
			}

			@Override
			public Object caseNamedQueryArtifact(NamedQueryArtifact object) {
				return IQueryArtifact.class;
			}
		}.doSwitch(dArt);
	}

}
