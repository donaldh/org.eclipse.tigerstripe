package org.eclipse.tigerstripe.annotation.internal.core;

import org.eclipse.emf.common.command.AbstractCommand;

public abstract class WriteCommand extends AbstractCommand {

	@Override
	protected boolean prepare() {
		return true;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public boolean canUndo() {
		return false;
	}
	
	public void redo() {
	}
}
