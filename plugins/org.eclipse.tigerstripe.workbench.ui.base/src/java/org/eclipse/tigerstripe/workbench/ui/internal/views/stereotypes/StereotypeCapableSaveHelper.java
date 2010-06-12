/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.stereotypes;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IMethodElement;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

/**
 * @author Alena Repina
 * 
 */
public class StereotypeCapableSaveHelper {

	/**
	 * @param capable
	 * @throws CoreException
	 */
	public static void save(IStereotypeCapable capable) throws CoreException {
		try {
			if (capable instanceof AbstractArtifact) {
				AbstractArtifact artifact = (AbstractArtifact) capable;
				IResource resource = (IResource) artifact
						.getAdapter(IResource.class);
				if (resource instanceof IFile) {
					IFile file = (IFile) resource;
					// Saving to the file
					String asText = artifact.asText();
					ByteArrayInputStream source = new ByteArrayInputStream(
							asText.getBytes());
					file.setContents(source, true, true, null);
				}
			} else if (capable instanceof IModelComponent) {
				IModelComponent mc = (IModelComponent) capable;
				IModelComponent parent = mc.getContainingModelComponent();
				save(parent);
			} else if (capable instanceof IMethodElement) {
				IMethodElement me = (IMethodElement) capable;
				IModelComponent parent = me.getContainingMethod();
				save(parent);
			}
		} catch (TigerstripeException e) {
			throw new CoreException(new Status(IStatus.ERROR,
					EclipsePlugin.PLUGIN_ID,
					"Failed to generate source for an artfact.", e));
		}
	}
}
