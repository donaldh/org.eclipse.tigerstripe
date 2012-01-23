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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.ui.core.view.EObjectBasedNote;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

public class StereotypeNote extends EObjectBasedNote implements INote {

	private static final EObject NULL_CONTENT = EcoreFactory.eINSTANCE
			.createEObject();
	private final IStereotypeInstance stereotype;
	private final IStereotypeCapable capable;
	private EObject eObject;

	public StereotypeNote(IStereotypeCapable capable,
			IStereotypeInstance stereotype) {
		super();
		this.stereotype = stereotype;
		this.capable = capable;
	}

	public String getDescription() {
		IStereotype characterizingStereotype = stereotype
				.getCharacterizingStereotype();
		if (characterizingStereotype == null) {
			return "";
		}
		return characterizingStereotype.getDescription();
	}

	public Image getImage() {
		return Images.get(Images.STEREOTYPE_ICON);
	}

	public String getLabel() {
		return stereotype.getName();
	}

	public boolean isReadOnly() {
		return isReadOnly(capable);
	}

	public static boolean isReadOnly(Object capable) {
		if (capable instanceof IAbstractArtifact) {
			return fromReference((IAbstractArtifact) capable);
		} else if (capable instanceof IModelComponent) {
			IModelComponent containingArtifact = ((IModelComponent) capable).getContainingModelComponent();
			if (containingArtifact instanceof IAbstractArtifact) {
				return fromReference(((IAbstractArtifact) containingArtifact));
			}
		}
		return false;
	}

	public static boolean fromReference(IAbstractArtifact artifact) {
		if (artifact instanceof IContextProjectAware) {
			ITigerstripeModelProject ctxProject = ((IContextProjectAware) artifact)
					.getContextProject();
			if (ctxProject != null) {
				try {
					return !ctxProject.equals(artifact.getProject());
				} catch (TigerstripeException e) {
					BasePlugin.log(e);
				}
			}
		}
		return false;
	}
	
	public void remove() {
		capable.removeStereotypeInstance(stereotype);
		try {
			StereotypeCapableSaveHelper.save(capable);
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
	}

	public void revert() throws CoreException {
		initEObject();
		StereotypeCapableSaveHelper.save(capable);
	}

	public void save() throws CoreException {
		StereotypeConverter.copyEObjectAttributes(eObject, stereotype);
		StereotypeCapableSaveHelper.save(capable);
	}

	public EObject getContent() {
		if (eObject == null) {
			initEObject();
		}
		return eObject;
	}

	private void initEObject() {
		StereotypeConverter converter = new StereotypeConverter();
		eObject = converter.createObject(stereotype);
		if (eObject == null) {
			eObject = NULL_CONTENT;
		}
	}

	public boolean isLoadable() {
		getContent();
		return eObject != NULL_CONTENT;
	}

}
