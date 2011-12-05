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
package org.eclipse.tigerstripe.workbench.ui.internal.elements;

import static org.eclipse.swt.layout.GridData.FILL_BOTH;
import static org.eclipse.swt.layout.GridData.HORIZONTAL_ALIGN_BEGINNING;
import static org.eclipse.swt.layout.GridData.VERTICAL_ALIGN_CENTER;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

public class NewTSMessageDialog extends Dialog {

	private String titleString;
	private String description;
	protected MyDialogMessageArea messageArea;

	public NewTSMessageDialog(Shell parent, String titleString,
			String description) {
		super(parent);
		this.titleString = titleString;
		this.description = description;
	}

	protected void setMessage(String message) {
		if (message != null && message.length() > 0) {
			messageArea.updateText(message, IMessageProvider.NONE);
		} else {
			messageArea.updateText(description, IMessageProvider.NONE);
		}
	}

	protected void setInfoMessage(String message) {
		if (message != null && message.length() > 0) {
			messageArea.updateText(message, IMessageProvider.INFORMATION);
		} else {
			messageArea.updateText(description, IMessageProvider.NONE);
		}
	}

	protected void setWarningMessage(String message) {
		if (message != null && message.length() > 0) {
			messageArea.updateText(message, IMessageProvider.WARNING);
		} else {
			messageArea.updateText(description, IMessageProvider.NONE);
		}
	}

	protected void setErrorMessage(String message) {
		if (message != null && message.length() > 0) {
			messageArea.updateText(message, IMessageProvider.ERROR);
		} else {
			messageArea.updateText(description, IMessageProvider.NONE);
		}
	}

	protected void createMessageArea(Composite composite, int nColumns) {
		messageArea = new MyDialogMessageArea();
		messageArea.createContents(composite);
		GridData layoutData = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
		layoutData.horizontalSpan = nColumns;
		messageArea.setTitleLayoutData(layoutData);
		layoutData = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
		layoutData.horizontalSpan = nColumns;
		messageArea.setMessageLayoutData(layoutData);
		messageArea.showTitle(titleString);
		setMessage(description);
		createSeparator(composite, nColumns);
	}

	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(
				composite, nColumns, convertHeightInCharsToPixels(1));
	}

	protected class MyDialogMessageArea {
		public Composite messageComposite;
		public CLabel titleLabel;
		public CLabel messageLabel;
		public Link link;
		
		public void createContents(Composite parent) {
			// Title label
			titleLabel = new CLabel(parent, SWT.NONE);
			titleLabel.setFont(JFaceResources.getBannerFont());
			titleLabel.setImage(null);
			// Message composite
			messageComposite = new Composite(parent, SWT.NONE);
			GridLayout messageLayout = new GridLayout();
			messageLayout.numColumns = 2;
			messageLayout.marginWidth = 0;
			messageLayout.marginHeight = 0;
			messageLayout.makeColumnsEqualWidth = false;
			messageComposite.setLayout(messageLayout);
			// Message label
			messageLabel = new CLabel(messageComposite, SWT.NONE);
			messageLabel.setFont(JFaceResources.getDialogFont());
			messageLabel.setImage(null);
			GridData textData = new GridData(VERTICAL_ALIGN_CENTER);
			messageLabel.setLayoutData(textData);
			link = new Link(messageComposite, SWT.NONE);
			link.setLayoutData(new GridData(HORIZONTAL_ALIGN_BEGINNING | VERTICAL_ALIGN_CENTER));
		}

		public void showTitle(String titleMessage) {
			titleLabel.setText(titleMessage);
			if (!titleLabel.isVisible())
				titleLabel.setVisible(true);
			return;
		}

		public void setTitleLayoutData(Object layoutData) {
			titleLabel.setLayoutData(layoutData);
		}

		public void setMessageLayoutData(Object layoutData) {
			messageComposite.setLayoutData(layoutData);
		}

		public void updateText(String newMessage, int newType) {
			updateText(newMessage, newType, false);
		}

		public void updateText(String newMessage, int newType, boolean hideTitle) {
			Image newImage = null;
			switch (newType) {
			case IMessageProvider.NONE:
				break;
			case IMessageProvider.INFORMATION:
				newImage = JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_INFO);
				break;
			case IMessageProvider.WARNING:
				newImage = JFaceResources
						.getImage(Dialog.DLG_IMG_MESSAGE_WARNING);
				break;
			case IMessageProvider.ERROR:
				newImage = JFaceResources
						.getImage(Dialog.DLG_IMG_MESSAGE_ERROR);
				break;
			}
			if (hideTitle)
				titleLabel.setVisible(false);
			// Any more updates required?
			// If the message text equals the tooltip and the text is the same
			// and the image is the same then nothing to do (so just return)
			if (newMessage.equals(messageLabel.getToolTipText())
					&& newImage == messageLabel.getImage()
					&& newMessage.equals(messageLabel.getText()))
				return;
			messageLabel.setImage(newImage);
			messageLabel.setText(newMessage);
			messageLabel.setToolTipText(newMessage);
			messageComposite.setVisible(true);
		}

	}

	public void setTitleString(String titleString) {
		this.titleString = titleString;
	}

}
