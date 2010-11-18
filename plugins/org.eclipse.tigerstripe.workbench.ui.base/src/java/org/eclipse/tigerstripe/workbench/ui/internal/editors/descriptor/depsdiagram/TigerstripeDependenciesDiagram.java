/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.depsdiagram;

import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

public class TigerstripeDependenciesDiagram extends
		AbstractTigerstripeDependenciesDiagram {

	private ITigerstripeProjectProvider projectProvider;

	@Override
	protected ITigerstripeModelProject getTSProject(IEditorInput input) {
		if (projectProvider == null) {
			return (ITigerstripeModelProject) ((IFileEditorInput) input)
					.getFile().getAdapter(ITigerstripeModelProject.class);
		} else {
			return projectProvider.getProject();
		}
	}

	@Override
	protected IDependencySubject getRootModel(IEditorInput input) {
		return factory.getSubject(getTSProject(input));
	}

	public void setProjectProvider(ITigerstripeProjectProvider projectProvider) {
		this.projectProvider = projectProvider;
	}

	public ITigerstripeProjectProvider getProjectProvider() {
		return projectProvider;
	}

}
