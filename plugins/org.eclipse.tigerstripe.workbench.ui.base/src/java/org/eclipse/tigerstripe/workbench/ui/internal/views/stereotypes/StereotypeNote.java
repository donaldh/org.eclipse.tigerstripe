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

import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.tigerstripe.annotation.ui.core.view.INoteChangeListener;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

public class StereotypeNote implements INote {

	private IStereotypeInstance stereotype;
	private IStereotypeCapable capable;

	public StereotypeNote(IStereotypeCapable capable,
			IStereotypeInstance stereotype) {
		this.stereotype = stereotype;
		this.capable = capable;
	}

	public String getDescription() {
		return null;
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
	}

	public void save() {
	}

	public void addChangeListener(INoteChangeListener listener) {
	}

	public void removeChangeListener(INoteChangeListener listener) {
	}

	public INoteChangeListener[] getListeners() {
		return null;
	}

}
