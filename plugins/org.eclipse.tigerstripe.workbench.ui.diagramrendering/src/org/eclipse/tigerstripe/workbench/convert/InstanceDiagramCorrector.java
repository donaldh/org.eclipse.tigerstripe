package org.eclipse.tigerstripe.workbench.convert;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;

@SuppressWarnings("unchecked")
public class InstanceDiagramCorrector {

	public void correct(DiagramEditPart part, ArtifactManager artifactManager,
			IProgressMonitor monitor) {

		Diagram diagram = part.getDiagramView();

		InstanceMap map = (InstanceMap) diagram.getElement();

		EList<ClassInstance> classInstances = map.getClassInstances();
		EList<AssociationInstance> associationInstances = map
				.getAssociationInstances();

		Set<String> toDelete = new HashSet<String>();

		Iterator<ClassInstance> cit = classInstances.iterator();
		while (cit.hasNext()) {
			ClassInstance ci = cit.next();
			String fqn = ci.getFullyQualifiedName();
			IAbstractArtifact art = artifactManager
					.getArtifactByFullyQualifiedName(fqn, true, monitor);
			if (art == null) {
				toDelete.add(fqn);
			}
			if ((art instanceof IRelationship)
					&& !(art instanceof IAssociationClassArtifact)) {
				toDelete.add(fqn);
			}
		}

		Iterator<AssociationInstance> ait = associationInstances.iterator();
		while (ait.hasNext()) {
			AssociationInstance ai = ait.next();
			String fqn = ai.getFullyQualifiedName();
			IAbstractArtifact art = artifactManager
					.getArtifactByFullyQualifiedName(fqn, true, monitor);
			if (art == null) {
				toDelete.add(fqn);
			}
			if (art instanceof IAssociationClassArtifact) {
				toDelete.add(fqn);
			}
		}
		if (!toDelete.isEmpty()) {
			try {
				ConvertUtils.makeDeleteFromInstanceDiagramCommand(part,
						toDelete).execute(new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
