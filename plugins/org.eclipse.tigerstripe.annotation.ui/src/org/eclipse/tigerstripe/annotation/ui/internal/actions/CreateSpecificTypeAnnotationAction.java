/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.actions;

import java.util.HashMap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.ProviderContext;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.util.AdaptableUtil;

/**
 * @author Yuri Strot
 *
 */
public class CreateSpecificTypeAnnotationAction extends Action {
	
	private Object object;
	private AnnotationType type;
	
	public CreateSpecificTypeAnnotationAction(Object object, AnnotationType type) {
		super(type.getName());
		ILabelProvider provider = AnnotationUIPlugin.getManager().getLabelProvider(type);
		if (provider != null) {
			Image image = provider.getImage(type.createInstance());
			if (image != null)
				setImageDescriptor(ImageDescriptor.createFromImage(image));
		}
		this.object = object;
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		HashMap<Object, ProviderContext> objects = new HashMap<Object, ProviderContext>();
		String[] targets = AdaptableUtil.getTargets(object, type);
		for (int i = 0; i < targets.length; i++) {
			ProviderContext provider = AnnotationPlugin.getManager().getProvider(targets[i]);
			Object adapted = AdaptableUtil.getAdapted(object, targets[i]);
			if (provider != null && adapted != null) {
				objects.put(adapted, provider);
			}
		}
		
		Object selected = null;
		if (objects.size() == 1) {
			selected = objects.keySet().toArray()[0];
		}
		else if (objects.size() > 1) {
			ObjectsListDialog dialog = new ObjectsListDialog(objects, new Shell(SWT.RESIZE));
			if (dialog.open() == Dialog.OK) {
				selected = dialog.getSelected();
			}
		}
		if (selected != null) {
			AnnotationPlugin.getManager().addAnnotation(
					selected, type.createInstance());
		}
	}

}
