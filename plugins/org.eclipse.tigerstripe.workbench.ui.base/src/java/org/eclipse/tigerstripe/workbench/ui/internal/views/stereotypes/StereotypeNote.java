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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.ui.core.view.EObjectBasedNote;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

public class StereotypeNote extends EObjectBasedNote implements INote {

	private IStereotypeInstance stereotype;
	private IStereotypeCapable capable;
	private EObject eObject;

	public StereotypeNote(IStereotypeCapable capable,
			IStereotypeInstance stereotype) {
		super();
		this.stereotype = stereotype;
		this.capable = capable;
	}

	public String getDescription() {
		return stereotype.getCharacterizingStereotype().getDescription();
	}

	public Image getImage() {
		return Images.get(Images.STEREOTYPE_ICON);
	}

	public String getLabel() {
		return stereotype.getName();
	}

	public boolean isReadOnly() {
		return false;
	}

	public void remove() {
		capable.removeStereotypeInstance(stereotype);
	}

	public void revert() {
		initEObject();
	}

	public void save() {
		StereotypeConverter.copyEObjectAttributes(eObject, stereotype);
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
	}

}
