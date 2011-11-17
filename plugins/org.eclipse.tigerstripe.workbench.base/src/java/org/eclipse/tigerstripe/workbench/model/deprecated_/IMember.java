package org.eclipse.tigerstripe.workbench.model.deprecated_;

public interface IMember {

	void setContainingArtifact(IAbstractArtifact artifact);

	IAbstractArtifact getContainingArtifact();

	String getUniqueName();
	
	String getMemberName();

	String getName();
	
	IType getType();
	
}
