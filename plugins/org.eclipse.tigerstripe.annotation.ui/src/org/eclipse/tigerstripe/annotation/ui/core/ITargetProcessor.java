package org.eclipse.tigerstripe.annotation.ui.core;

public interface ITargetProcessor {

	boolean isDirty(Object object);

	String getName(Object object);

	String getDirtyViolationMessage(Object object);

}
