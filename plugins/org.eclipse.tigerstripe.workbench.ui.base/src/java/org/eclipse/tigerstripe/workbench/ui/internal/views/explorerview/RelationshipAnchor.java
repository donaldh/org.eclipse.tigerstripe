package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;

/**
 * A relationship anchor is what represents the end of a relationship to/from an
 * artifact.
 * 
 * It provides a slightly different view than the IRelationshipEnd for the
 * purpose of display in the Explorer.
 * 
 * @author erdillon
 * 
 */
public class RelationshipAnchor {

	private final String targetTypeFQN;
	private final String relationshipFQN;
	private final String anchorName;
	private boolean inherited;

	private final IRelationshipEnd end;

	public RelationshipAnchor(IRelationshipEnd end) {
		IRelationshipEnd otherEnd = end.getOtherEnd();
		this.targetTypeFQN = otherEnd.getType().getFullyQualifiedName();
		this.relationshipFQN = end.getContainingRelationship()
				.getFullyQualifiedName();
		this.anchorName = otherEnd.getName();
		this.end = end;
	}

	public IRelationshipEnd getEnd() {
		return this.end;
	}
	
	public String getLabel() {
		return anchorName + ":" + Util.nameOf(targetTypeFQN) + " ("
				+ Util.nameOf(relationshipFQN) + ")";
	}

	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	public boolean isInherited() {
		return inherited;
	}
}
