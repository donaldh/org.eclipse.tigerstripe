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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.IAnnotationTarget;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.ui.PlatformUI;

/**
 * @author Yuri Strot
 * 
 */
public class CreateSpecificTypeAnnotationAction extends Action {

	public static final String ANNOTATION_PROPERTY_VIEW = "org.eclipse.tigerstripe.annotation.view.property";

	private final TargetAnnotationType targetType;
	private boolean isCanceled;

	public CreateSpecificTypeAnnotationAction(TargetAnnotationType targetType) {
		super(targetType.getType().getName());
		AnnotationType type = targetType.getType();
		ILabelProvider provider = AnnotationUIPlugin.getManager()
				.getLabelProvider(type);
		if (provider != null) {
			Image image = provider.getImage(type.createInstance());
			if (image != null)
				setImageDescriptor(ImageDescriptor.createFromImage(image));
		}
		this.targetType = targetType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		IAnnotationTarget[] targets = targetType.getTargets();
		Object selected = null;
		if (targets.length == 1) {
			selected = targets[0].getAdaptedObject();
		} else if (targets.length > 1) {
			ObjectsListDialog dialog = new ObjectsListDialog(targetType,
					new Shell(SWT.RESIZE));
			int returnCode = dialog.open();
			if (returnCode == Dialog.CANCEL) {
				isCanceled = true;
			} else if (returnCode == Dialog.OK) {
				selected = dialog.getSelected();
			}
		}
		if (selected != null) {
			try {
				Object result = AnnotationPlugin.getManager().addAnnotation(
						selected,
						targetType.getType().createInstance());
				if (result != null) {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().showView(ANNOTATION_PROPERTY_VIEW);
				}
			} catch (Exception e) {
				AnnotationUIPlugin.log(e);
			}
		}
	}

	public boolean isCanceled() {
		return isCanceled;
	}
}
