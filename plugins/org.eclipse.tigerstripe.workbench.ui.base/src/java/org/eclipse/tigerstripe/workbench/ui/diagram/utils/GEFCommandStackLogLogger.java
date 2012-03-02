package org.eclipse.tigerstripe.workbench.ui.diagram.utils;

import java.util.Iterator;

import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

/**
 * Convinient way to know what happens on the diagram and which commands are
 * executed.
 */
public class GEFCommandStackLogLogger implements IOperationHistoryListener {

	private static final GEFCommandStackLogLogger listener = new GEFCommandStackLogLogger();

	public static void start() {
		OperationHistoryFactory.getOperationHistory()
				.addOperationHistoryListener(listener);
	}

	public static void stop() {
		OperationHistoryFactory.getOperationHistory()
				.removeOperationHistoryListener(listener);
	}

	private static IStatus getStatus(ICommand command) {
		if (command instanceof ICompositeCommand) {
			MultiStatus multiStatus = new MultiStatus(BasePlugin.getPluginId(),
					222, "[COMPOUND] " + getMessage(command), null);
			ICompositeCommand cc = (ICompositeCommand) command;
			Iterator<?> it = cc.iterator();
			while (it.hasNext()) {
				Object next = it.next();
				if (next instanceof ICommand) {
					multiStatus.add(getStatus((ICommand) next));
				} else {
					multiStatus.add(new Status(IStatus.INFO, BasePlugin
							.getPluginId(), next + ""));
				}
			}
			return multiStatus;
		} else if (command instanceof CommandProxy) {
			return new Status(IStatus.INFO, BasePlugin.getPluginId(),
					getMessage(((CommandProxy) command).getCommand()));
		} else {
			return new Status(IStatus.INFO, BasePlugin.getPluginId(),
					getMessage(command));
		}
	}

	private static String getMessage(Command command) {
		if (command == null) {
			return String.format("NULL command executed.");
		} else {
			return String.format("'%s' command executed. '%s'",
					command.getDebugLabel(), command.getClass());
		}
	}

	private static String getMessage(ICommand command) {
		if (command == null) {
			return String.format("NULL command executed.");
		} else {
			return String.format("'%s' command executed. '%s'",
					command.getLabel(), command.getClass());
		}
	}

	public void historyNotification(OperationHistoryEvent event) {
		if (event.getEventType() == OperationHistoryEvent.DONE) {
			IUndoableOperation operation = event.getOperation();
			if (operation instanceof ICommand) {
				EclipsePlugin.getDefault().getLog()
						.log(getStatus((ICommand) operation));
			}
		}
	}
}
