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
package org.eclipse.tigerstripe.annotation.ui.wizard;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.Images;
import org.eclipse.tigerstripe.annotation.ui.util.AnnotationGroup;

/**
 * @author Yuri Strot
 * 
 */
public class CreateAnnotationWizardPage extends WizardPage {

	private static final String TITLE = "Select Annotation Type";
	private TargetAnnotationType type;
	private TargetAnnotationType[] types;
	private Tree combo;
	private Text descriptionText;
	private final Object object;

	private ComboListener comboListener;
	private Image defaultImage;
	private Image groupImage;

	public CreateAnnotationWizardPage(Object object) {
		super(TITLE);
		setTitle(TITLE);
		this.object = object;
	}

	public TargetAnnotationType getType() {
		return type;
	}

	public Image getDefaultImage() {
		if (defaultImage == null) {
			defaultImage = Images.getImage(Images.ANNOTATION);
		}
		return defaultImage;
	}

	public Image getGroupImage() {
		if (groupImage == null) {
			groupImage = Images.getImage(Images.ANNOTATIONS);
		}
		return groupImage;
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		createTypeControls(composite);
		createDescription(composite);
		
		setFirstSelection();

		setControl(composite);
		Dialog.applyDialogFont(composite);
	}

	private void setFirstSelection() {
		if (combo.getItemCount() > 0) {
			TreeItem item = combo.getItem(0);
			item.setExpanded(true);
			TreeItem[] items = item.getItems();
			if (items.length > 0)
				combo.select(items[0]);
			else
				combo.select(item);
		}
		
		combo.forceFocus();
		comboListener.updateDescription();
	}

	public boolean canFinish() {
		return type != null;
	}

	protected void createType(TargetAnnotationType type) {
		createType(null, type);
	}

	protected void createType(TreeItem parent, TargetAnnotationType targetType) {
		TreeItem item = parent == null ? new TreeItem(combo, SWT.NONE)
				: new TreeItem(parent, SWT.NONE);
		AnnotationType type = targetType.getType();
		item.setText(type.getName());
		item.setData(targetType);
		Image image = null;
		ILabelProvider provider = AnnotationUIPlugin.getManager()
				.getLabelProvider(type);
		if (provider != null) {
			try {
				image = provider.getImage(type.createInstance());
			} catch (Exception e) {
				AnnotationUIPlugin.log(e);
			}
		}
		if (image == null)
			image = getDefaultImage();
		item.setImage(image);
	}

	protected void createTypeControls(Composite parent) {
		Label label = new Label(parent, SWT.TOP);
		label.setText("Type: ");
		label.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		types = AnnotationPlugin.getManager().getAnnotationTargets(object);

		combo = new Tree(parent, SWT.BORDER);
		combo.setHeaderVisible(false);
		combo.setLinesVisible(false);
		AnnotationGroup[] groups = AnnotationGroup.getGroups(types);
		for (AnnotationGroup group : groups) {
			String name = group.getName();
			if (name != null) {
				TreeItem item = new TreeItem(combo, SWT.NONE);
				item.setText(name);
				item.setImage(getGroupImage());
				for (TargetAnnotationType type : group.getTypes()) {
					createType(item, type);
				}
			} else {
				for (TargetAnnotationType type : group.getTypes()) {
					createType(type);
				}
			}
		}
		combo.setLayoutData(new GridData(GridData.FILL_BOTH));

		comboListener = new ComboListener();
		combo.addSelectionListener(comboListener);
		combo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TreeItem[] selection = combo.getSelection();
				if (selection.length > 0) {
					type = (TargetAnnotationType) selection[0].getData();
					if (type != null) {
						final IWizardContainer container = getContainer();
						if (container instanceof WizardDialog) {
							final WizardDialog dialog = (WizardDialog) container;
							if (getWizard().canFinish()) {
								if (getWizard().performFinish()) {
									dialog.close();
								}
							}
						}
					}
				}
			}
		});
	}
	
	private void createDescription(Composite composite) {
		Label descrLabel = new Label(composite, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		data.horizontalSpan = 2;
		descrLabel.setLayoutData(data);
		descrLabel.setText("Annotation Description:");
		descriptionText = new Text(composite, SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL | SWT.READ_ONLY);
		descriptionText.setFont(composite.getFont());
		descriptionText.setBackground(composite.getBackground());
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		data.horizontalSpan = 2;
		data.widthHint = convertWidthInCharsToPixels(50);
		data.heightHint = convertHeightInCharsToPixels(4);
		descriptionText.setLayoutData(data);
	}

	private class ComboListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			updateDescription();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			updateDescription();
		}

		public void updateDescription() {
			String description = "";
			TreeItem[] selection = combo.getSelection();
			if (selection.length > 0) {
				type = (TargetAnnotationType) selection[0].getData();
				if (type != null && type.getType().getDescription() != null) {
					description = type.getType().getDescription();
				}
			} else {
				type = null;
			}
			descriptionText.setText(description);
			getWizard().getContainer().updateButtons();
		}
	}
}
