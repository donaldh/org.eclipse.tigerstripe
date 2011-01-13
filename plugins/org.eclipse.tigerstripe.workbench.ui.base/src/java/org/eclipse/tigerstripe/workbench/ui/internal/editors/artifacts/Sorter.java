package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import java.util.Collection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

public class Sorter extends ViewerSorter {

	private int dir = SWT.DOWN;
	private IAbstractArtifact owner;

	/**
	 * @param column
	 * @param dir
	 */
	public Sorter() {
		this(SWT.DOWN, null);
	}

	public Sorter(int dir) {
		this(dir, null);
	}

	public Sorter(int dir, IAbstractArtifact owner) {
		this.dir = dir;
		this.owner = owner;
	}

	private int compareAsSibling(IModelComponent c1, IModelComponent c2) {

		Collection<? extends IModelComponent> ownerMembers;

		if (c1 instanceof IField) {
			ownerMembers = owner.getFields();
		} else if (c1 instanceof IMethod) {
			ownerMembers = owner.getMethods();
		} else if (c1 instanceof ILiteral) {
			ownerMembers = owner.getLiterals();
		} else {
			ownerMembers = owner.getContainedModelComponents();
		}

		if (!ownerMembers.contains(c1) && ownerMembers.contains(c2)) {
			return 1;
		} else if (ownerMembers.contains(c1) && !ownerMembers.contains(c2)) {
			return -1;
		} else {

			IModelComponent c1Comp = c1.getContainingModelComponent();
			IModelComponent c2Comp = c2.getContainingModelComponent();

			if (c1Comp == null || c2Comp == null) {
				return 0;
			}

			if (c1Comp.equals(c2Comp)) {
				return 0;
			}

			if (c1Comp instanceof IAbstractArtifact
					&& c2Comp instanceof IAbstractArtifact) {
				IAbstractArtifact art1 = (IAbstractArtifact) c1Comp;
				IAbstractArtifact art2 = (IAbstractArtifact) c2Comp;

				String ownerFQN = owner.getFullyQualifiedName();
				String fqn1 = art1.getFullyQualifiedName();
				String fqn2 = art2.getFullyQualifiedName();

				if (!ownerFQN.equals(fqn1) && ownerFQN.equals(fqn2)) {
					return 1;
				} else if (ownerFQN.equals(fqn1) && !ownerFQN.equals(fqn2)) {
					return -1;
				}

				if (inheritorOf(art1, art2)) {
					return 1;
				}
				if (inheritorOf(art2, art1)) {
					return -1;
				}

			}

			return c1Comp.getName().compareTo(c2Comp.getName());
		}
	}

	private boolean inheritorOf(IAbstractArtifact candidat,
			IAbstractArtifact target) {
		IAbstractArtifact extended = target.getExtendedArtifact();
		while (extended != null) {
			if (candidat.equals(extended)) {
				return true;
			}
			extended = extended.getExtendedArtifact();
		}
		return false;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		if (owner != null) {
			int value = compareAsSibling((IModelComponent) e1,
					(IModelComponent) e2);
			if (value != 0) {
				return value;
			}
		}

		int returnValue = 0;
		if (e1 instanceof ILiteral && e2 instanceof ILiteral) {
			ILiteral lit1 = (ILiteral) e1;
			ILiteral lit2 = (ILiteral) e2;
			if (lit1.getType().getName().equals("int")
					&& lit2.getType().getName().equals("int")) {
				int i1 = Integer.valueOf(lit1.getValue());
				int i2 = Integer.valueOf(lit2.getValue());
				if (i1 > i2)
					returnValue = 1;
				else
					returnValue = -1;
			} else {
				returnValue = lit1.getValue().compareToIgnoreCase(
						lit2.getValue());
			}

		} else {
			returnValue = ((IModelComponent) e1).getName().compareToIgnoreCase(
					((IModelComponent) e2).getName());
		}
		if (this.dir == SWT.DOWN) {
			returnValue *= -1;
		}
		return returnValue;
	}

}