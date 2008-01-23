/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

public abstract class AbstractQualifiedNamedElementUpdateCommand extends
		BaseCommand {

	protected QualifiedNamedElement element;
	protected IAbstractArtifact artifact;

	public AbstractQualifiedNamedElementUpdateCommand(
			QualifiedNamedElement element, IAbstractArtifact iArtifact) {
		this.element = element;
		this.artifact = iArtifact;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	public void execute() {
		updateQualifiedNamedElement(element, artifact);
	}

	public void redo() {
		// TODO Auto-generated method stub
	}

	public abstract void updateQualifiedNamedElement(
			QualifiedNamedElement element, IAbstractArtifact artifact);

}
