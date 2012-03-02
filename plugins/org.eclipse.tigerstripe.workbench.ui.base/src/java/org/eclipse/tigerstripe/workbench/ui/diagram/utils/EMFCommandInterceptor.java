package org.eclipse.tigerstripe.workbench.ui.diagram.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;

/**
 * Convinient way to know what happens on the diagram and what the commands are
 * executed. Common way to use that, wrap creation of
 * TransactionalEditingDomain.
 */
public class EMFCommandInterceptor {

	public static TransactionalEditingDomain wrap(
			TransactionalEditingDomain domain) {

		return (TransactionalEditingDomain) Proxy.newProxyInstance(domain
				.getClass().getClassLoader(),
				new Class[] { TransactionalEditingDomain.class },
				new EditingDomainInterceptor());

	}

	private static IStatus getStatus(Command command) {
		String message;
		if (command == null) {
			message = String.format("NULL command executed.");
		} else {
			message = String.format("'%s' command executed. '%s'", command.getLabel(), command.getClass());
		}
		
		Status status = new Status(IStatus.INFO, BasePlugin.getPluginId(), message );
		return status;
	}
	
	private static void log(Command command) {
		BasePlugin.log(getStatus(command));
	}

	private static class EditingDomainInterceptor implements InvocationHandler {
		
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {

			Object result = method.invoke(proxy, args);
			if ("getCommandStack".equals(method.getName())) {
				return (TransactionalEditingDomain) Proxy.newProxyInstance(
						proxy.getClass().getClassLoader(),
						new Class[] { CommandStack.class },
						new CommandStackInterceptor((CommandStack) result));
			}

			return result;
		}
	}

	private static class CommandStackInterceptor implements InvocationHandler {

		private final CommandStack commandStack;
		
		public CommandStackInterceptor(CommandStack commandStack) {
			this.commandStack = commandStack;
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			
			if ("execute".equals(method.getName()) && args.length == 1) {
				Object param = args[0];
				if (param instanceof Command) {
					Command command = (Command) param;
					log(command);
					commandStack.execute(command);
					return null;
				}
			}
			return method.invoke(proxy, args);
		}
	}
}
