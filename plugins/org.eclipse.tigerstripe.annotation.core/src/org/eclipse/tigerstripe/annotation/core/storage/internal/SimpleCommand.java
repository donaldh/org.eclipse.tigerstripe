package org.eclipse.tigerstripe.annotation.core.storage.internal;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;

/**
 * Simple implementation of EMF {@link Command}. This command used for running
 * {@link Runnable} operations on the EMF command stack
 */
public abstract class SimpleCommand extends AbstractCommand {

	@Override
	protected boolean prepare() {
		return true;
	}

	public void redo() {
		execute();
	}

}
