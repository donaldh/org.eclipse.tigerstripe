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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.Images;
import org.eclipse.tigerstripe.annotation.ui.util.AnnotationGroup;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * @author Eric Dillon
 * 
 * 
 */
public class BrowseForAnnotationsDialog {

	private static final class AnnotationTreeSelectionDialog extends
			ElementTreeSelectionDialog {
		
		private Text descriptionText;

		private AnnotationTreeSelectionDialog(Shell parent,
				ILabelProvider labelProvider,
				ITreeContentProvider contentProvider) {
			super(parent, labelProvider, contentProvider);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super
					.createDialogArea(parent);
			Label l = new Label(composite, SWT.NONE);
			l.setText("Annotation Description:");
			descriptionText = new Text(composite, SWT.WRAP | SWT.MULTI
					| SWT.V_SCROLL | SWT.READ_ONLY);
			descriptionText.setFont(composite.getFont());
			descriptionText.setBackground(composite.getBackground());
			GridData data = new GridData(GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL);
			data.horizontalSpan = 2;
			data.widthHint = convertWidthInCharsToPixels(50);
			data.heightHint = convertHeightInCharsToPixels(4);
			descriptionText.setLayoutData(data);
			return composite;
		}

		@Override
		protected void updateButtonsEnableState(IStatus status) {
			Object firstResult = getFirstResult();
			if (firstResult instanceof TargetAnnotationType) {
				String description = ((TargetAnnotationType) firstResult)
						.getType().getDescription();
				descriptionText.setText(description);
			} else {
				descriptionText.setText("");
			}
			super.updateButtonsEnableState(status);
		}
	}

	private final class AnnotationTypeContentProvider implements
			ITreeContentProvider {
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		}

		public void dispose() {
		}

		public boolean hasChildren(Object arg0) {
			if (arg0 instanceof AnnotationGroup){
				AnnotationGroup group = (AnnotationGroup) arg0;
				return !group.getTypes().isEmpty();
			}
			return false;
		}

		public Object getParent(Object arg0) {
			return null;
		}

		public Object[] getElements(Object arg0) {
			return (Object[]) arg0;
		}

		public Object[] getChildren(Object arg0) {
			if (arg0 instanceof AnnotationGroup){
				AnnotationGroup group = (AnnotationGroup) arg0;
				return group.getTypes().toArray();
			}
			return null;
		}
	}

	private class AnnotationTypeLabelProvider extends LabelProvider {
		
		private Image defaultImage;
		
		@Override
		public String getText(Object element) {
			if (element instanceof AnnotationGroup) {
				AnnotationGroup group = (AnnotationGroup) element;
				return group.getName();
			} else if (element instanceof TargetAnnotationType) {
				TargetAnnotationType target = (TargetAnnotationType) element;
				return target.getType().getName();
			}
			return null;
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof AnnotationGroup) {
				return getDefaultImage();
			} else if (element instanceof TargetAnnotationType) {
				TargetAnnotationType target = (TargetAnnotationType) element;
				AnnotationType type = target.getType();
				ILabelProvider prov = AnnotationUIPlugin.getManager()
						.getLabelProvider(type);
				Image image = null;
				if (prov != null)
					image = prov.getImage(type.createInstance());
				if (image == null)
					image = getDefaultImage();
				return image;
			}
			return null;
		}

		private Image getDefaultImage() {
			if (defaultImage == null) {
				defaultImage = Images.getImage(Images.ANNOTATIONS);
			}
			return defaultImage;
		}

	}

	private static final class AnnotationTypeSelectionValidator implements
			ISelectionStatusValidator {
		
		public static final IStatus OK_STATUS = new Status(IStatus.OK, EclipsePlugin.PLUGIN_ID, null);
		public static final IStatus ERROR_STATUS = new Status(IStatus.ERROR, EclipsePlugin.PLUGIN_ID, null);

		public IStatus validate(Object[] arg0) {
			for (Object o : arg0){
				if (!(o instanceof TargetAnnotationType))
					return ERROR_STATUS;
			}
			return OK_STATUS;
		}
	}

	private Collection<AnnotationType> existingAnnotationTypes;

	private String title = "Annotation Type Selection";

	private String message = "Please select a Annotation Type";

	/**
	 * 
	 * @param initialElement
	 * @param model
	 */
	public BrowseForAnnotationsDialog(
			Collection<AnnotationType> existingInstances) {
		this.existingAnnotationTypes = existingInstances;
	}

	/**
	 * Opens up a dialog box with a list of available entities and returns the
	 * EntityOptions that have been selected.
	 * 
	 * @return EntityOption[] - Returns an array of EntityOption as selected
	 *         from the dialog
	 */
	public AnnotationType[] browseAvailableAnnotationTypes(Shell parentShell)
			throws TigerstripeException {

		ElementTreeSelectionDialog dialog = new AnnotationTreeSelectionDialog(
				parentShell, new AnnotationTypeLabelProvider(),
				new AnnotationTypeContentProvider());

		dialog.setTitle(getTitle());
		dialog.setMessage(getMessage());
		dialog.setValidator(new AnnotationTypeSelectionValidator());
		dialog.setInput(getContent());

		if (dialog.open() == Window.OK) {

			Object[] objects = dialog.getResult();
			if (objects != null && objects.length != 0) {
				AnnotationType[] result = new AnnotationType[objects.length];
				for (int i = 0; i < result.length; i++) {
					result[i] = ((TargetAnnotationType) objects[i]).getType();
				}

				return result;
			}
		}
		return new AnnotationType[0];
	}

	/**
	 * 
	 * @return
	 */
	private Object[] getContent()
			throws TigerstripeException {

		List<TargetAnnotationType> targets = new ArrayList<TargetAnnotationType>();

		for (AnnotationType type : AnnotationPlugin.getManager().getTypes()){
			targets.add(new TargetAnnotationType(type, null));
		}

		if (targets.size() == 0)
			return new TargetAnnotationType[0];

		// now go thru the list and remove those that can't be re added
		for (AnnotationType instance : existingAnnotationTypes) {
			int target = -1;
			for (int i = 0; i < targets.size(); i++) {
				if (targets.get(i).getType().getId().equals(instance.getId())) {
					target = i;
					break;
				}
			}
			if (target != -1)
				targets.remove(target);
		}

//		Collections.sort(targets, new Comparator<TargetAnnotationType>() {
//
//			public int compare(TargetAnnotationType o1, TargetAnnotationType o2) {
//				return o1.getType().getName().compareTo(o2.getType().getName());
//			}
//
//		});

		AnnotationGroup[] groups = AnnotationGroup.getGroups(targets
				.toArray(new TargetAnnotationType[targets.size()]));

		List<Object> result = new ArrayList<Object>();
		for (AnnotationGroup group : groups) {
			if (group.getName() != null) {
				result.add(group);
			} else {
				result.addAll(group.getTypes());
			}
		}

		return result.toArray();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
