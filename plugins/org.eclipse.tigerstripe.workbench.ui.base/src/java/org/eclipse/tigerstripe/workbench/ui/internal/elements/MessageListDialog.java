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

import static org.eclipse.tigerstripe.workbench.ui.internal.utils.StatusUtils.flat;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;

public class MessageListDialog extends TSMessageDialog {

	private String title;
	private Table table;
	private Button copyToClipboardButton;

	private MessageList messageList;

	private String[] tableColumnNames = { "Severity", "Description" };

	private int[] tableColumnWidth = { 65, 400 };

	public MessageListDialog(Shell parent, Collection<IStatus> errorList, String title) {
		super(parent);
		MessageList list = new MessageList();
		for (IStatus error : errorList) {
			Message msg = new Message();
			msg.setMessage(error.getMessage());
			if (error.getSeverity() == IStatus.ERROR)
				msg.setSeverity(Message.ERROR);
			else if (error.getSeverity() == IStatus.INFO)
				msg.setSeverity(Message.INFO);
			else if (error.getSeverity() == IStatus.WARNING)
				msg.setSeverity(Message.WARNING);
			list.addMessage(msg);
		}
		this.messageList = list;
		int shellStyle = getShellStyle();
		setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);
		this.title = title;
	}
	
	public MessageListDialog(Shell parent, IStatus errorList, String title) {
		this(parent, flat(errorList), title);
	}

	public MessageListDialog(Shell parent, MessageList list) {
		super(parent);
		this.title = "XMI Import Log";
		this.messageList = list;
		int shellStyle = getShellStyle();
		setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);

	}

	public MessageListDialog(Shell parent, MessageList list, String title) {
		super(parent);
		this.title = title;
		this.messageList = list;
		int shellStyle = getShellStyle();
		setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);

	}

	public void disableOKButton() {
		Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		area.setLayout(layout);

		// ScrolledComposite scrolledArea = new ScrolledComposite(area,
		// SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		// GridData gd = new GridData( GridData.CENTER | GridData.FILL_BOTH |
		// GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL );
		// gd.horizontalSpan = 3;
		// gd.widthHint = 390;
		// scrolledArea.setLayoutData(gd);

		getShell().setText(title);
		getShell().setMinimumSize(450, 250);

		table = new Table(area, SWT.SINGLE | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// area.setContent(table);
		GridData gd = new GridData(GridData.CENTER | GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		table.setLayoutData(gd);

		createTableColumns(table);
		createTableRows(table);

		copyToClipboardButton = new Button(area, SWT.PUSH);
		copyToClipboardButton.setText("Copy to Clipboard");
		copyToClipboardButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				copyToClipboardPressed();
			}
		});
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.END);
		gd.horizontalSpan = 3;
		copyToClipboardButton.setLayoutData(gd);

		table.pack();
		// scrolledArea.pack();

		return area;
	}

	protected void copyToClipboardPressed() {
		Clipboard clipboard = new Clipboard(getShell().getDisplay());
		clipboard.setContents(new Object[] { asText(messageList) },
				new Transfer[] { TextTransfer.getInstance() });
	}

	private String asText(MessageList list) {
		return list.asText();
	}

	private void createTableColumns(Table table) {
		for (int i = 0; i < tableColumnNames.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(tableColumnNames[i]);
			column.setWidth(tableColumnWidth[i]);
		}
	}

	private void createTableRows(Table table) {
		for (Iterator<Message> iter = messageList.getAllMessages().iterator(); iter
				.hasNext();) {
			Message msg = iter.next();
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(0, Message.severityToString(msg.getSeverity()));
			item.setText(1, msg.getMessage());
		}
	}
}
