/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.builder;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

public class BuilderUtils {

	public static boolean addBuilder(IProject project, String builderId)
			throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();

		boolean found = false;

		for (int i = 0; i < commands.length; ++i) {
			if (builderExists(commands[i], builderId)) {
				found = true;
				break;
			}
		}
		if (!found) {
			// add builder to project
			ICommand command = desc.newCommand();
			command.setBuilderName(builderId);
			ICommand[] newCommands = new ICommand[commands.length + 1];

			// Add it before other builders.
			System.arraycopy(commands, 0, newCommands, 1, commands.length);
			newCommands[0] = command;
			desc.setBuildSpec(newCommands);
			project.setDescription(desc, null);
			return true;
		} else {
			return false;
		}
	}

	private static boolean builderExists(ICommand command, String builderId) {

		if (command.getBuilderName().equals(builderId)) {
			return true;
		}

		Object launchConfig = command.getArguments().get("LaunchConfigHandle");
		if (launchConfig != null && launchConfig.toString().contains(builderId)) {
			return true;
		}

		return false;
	}

}
