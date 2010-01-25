/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;

public abstract class AbstractModelRefactorWizardPage extends WizardPage {

	protected List<IAbstractArtifact> artifacts;
	
	protected AbstractModelRefactorWizardPage(String pageName) {
		super(pageName);
		artifacts = new ArrayList<IAbstractArtifact>();
	}

	@SuppressWarnings("unchecked")
	public void init(IStructuredSelection selection) {
	
		if (selection == null)
			return;
	
		if (selection.size() > 0) {
			
			Iterator<IJavaElement> iter = selection.iterator();
			while(iter.hasNext()) {
				
				Object obj = iter.next();
				if (obj instanceof IJavaElement) {
		
					IJavaElement element = (IJavaElement) obj;
					IAbstractArtifact artifact = (IAbstractArtifact) element.getAdapter(IAbstractArtifact.class);
					if (artifact != null) {
						artifacts.add(artifact);
					}
				}
			}
		}
	}

	protected boolean validatePage(ModelRefactorRequest request) {
	
		if (request.isValid().getSeverity() == IStatus.OK) {
			setPageComplete(true);
			return true;
		} else {
			setPageComplete(false);
			return false;
		}
	}


}
