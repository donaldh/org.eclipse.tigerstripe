package org.eclipse.tigerstripe.workbench.optional.buckminster.internal;

public enum VersionQueryEnum {

	PROJECT("/tigerstripe/project/version");

	private final String query;

	VersionQueryEnum(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}
	
}
