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

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

/**
 * Display the log of a Generate
 * 
 * @author Eric Dillon
 * @since 2.1
 */
public class GenerateResultDialog extends Dialog {

	private Button copyToClipboardButton;

	private final PluginRunStatus[] result;

	public GenerateResultDialog(IShellProvider parentShell,
			PluginRunStatus[] result) {
		super(parentShell);
		this.result = result;
	}

	public GenerateResultDialog(Shell parentShell, PluginRunStatus[] result) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.result = result;
	}

	@Override
	public void create() {
		super.create();
		getShell().setSize(650, 400);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		area.setLayout(layout);
		area.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(area);

		Label overallResult = new Label(area, SWT.NONE);
		if (result.length == 0) {
			overallResult.setText("No generator enabled.");
			overallResult.setForeground(ColorUtils.TS_ORANGE);
		} else if (hasErrors(result)) {
			overallResult.setText("Generation Failed.");
			overallResult.setForeground(ColorUtils.TS_ORANGE);
		} else if (hasWarnings(result)) {
			overallResult.setText("Warnings during generation.");
			overallResult.setForeground(ColorUtils.TS_ORANGE);
		} else {
			overallResult.setText("Generation Successful.");
			overallResult.setForeground(ColorUtils.BLACK);
		}
		overallResult.setFont(new Font(null, "Arial", 12, SWT.BOLD));

		Label versionLabel = new Label(area, SWT.NONE);
		versionLabel.setText("Run Log");
		createViewer(area);

		copyToClipboardButton = new Button(area, SWT.PUSH);
		copyToClipboardButton.setText("Copy to Clipboard");
		copyToClipboardButton.setImage(PlatformUI.getWorkbench()
				.getSharedImages().getImage(ISharedImages.IMG_TOOL_COPY));
		copyToClipboardButton.setToolTipText("Copy log to clipboard.");
		copyToClipboardButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				copyToClipboard();
			}
		});

		getShell().setText("Generation Result");
		return area;
	}

	private boolean hasWarnings(PluginRunStatus[] statuses) {
		for (PluginRunStatus st : statuses) {
			if (st.hasWarning()) {
				return true;
			}
		}
		return false;
	}

	private void copyToClipboard() {
		Clipboard clipboard = new Clipboard(getShell().getDisplay());
		clipboard.setContents(new Object[] { asText(result, false) },
				new Transfer[] { TextTransfer.getInstance() });
	}

	private String asText(PluginRunStatus[] result, boolean includeHTML) {
		StringBuffer buffer = new StringBuffer();

		if (includeHTML)
			buffer.append("<form>");
		for (PluginRunStatus res : result) {
			buffer.append(res.toString(includeHTML));
		}

		if (includeHTML)
			buffer.append("</form>");
		return buffer.toString();
	}

	private boolean hasErrors(PluginRunStatus[] result) {

		boolean errorFound = false;
		for (PluginRunStatus res : result) {
			if (res.getSeverity() == IStatus.ERROR) {
				errorFound = true;
				continue;
			}
		}

		return errorFound;
	}

	private TreeColumn fColumn1;
	private TreeColumn fColumn2;

	private Tree fTree;
	private FilteredTree fFilteredTree;
	private Action fPropertiesAction;

	private void createViewer(Composite parent) {
		PatternFilter filter = new PatternFilter() {
			@Override
			protected boolean isLeafMatch(Viewer viewer, Object element) {
				if (element instanceof IStatus) {
					IStatus statusEntry = (IStatus) element;
					String message = statusEntry.getMessage();
					String plugin = statusEntry.getPlugin();
					return wordMatches(message) || wordMatches(plugin);
				}
				return false;
			}
		};
		filter.setIncludeLeadingWildcard(true);
		fFilteredTree = new FilteredTree(parent, SWT.FULL_SELECTION
				| SWT.BORDER, filter,
				true);
		fFilteredTree.setLayoutData(new GridData(GridData.FILL_BOTH));
		fTree = fFilteredTree.getViewer().getTree();
		fTree.setLinesVisible(true);
		createColumns(fTree);
		fFilteredTree.getViewer().setAutoExpandLevel(2);
		fFilteredTree.getViewer().setContentProvider(
				new LogViewContentProvider());
		fFilteredTree.getViewer().setLabelProvider(new LogViewLabelProvider());
		fFilteredTree.getViewer().addSelectionChangedListener(
				new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent e) {
						if (fPropertiesAction.isEnabled())
							((StatusDetailsDialogAction) fPropertiesAction)
									.resetSelection();
					}
				});
		fFilteredTree.getViewer().addDoubleClickListener(
				new IDoubleClickListener() {
					public void doubleClick(DoubleClickEvent event) {
						fPropertiesAction.run();
					}
				});
		fFilteredTree.getViewer().setInput(result);

		fPropertiesAction = new StatusDetailsDialogAction(fTree.getShell(),
				fFilteredTree.getViewer());
	}

	private void createColumns(Tree tree) {
		fColumn1 = new TreeColumn(tree, SWT.LEFT);
		fColumn1.setText("Message");
		fColumn1.setWidth(430);

		fColumn2 = new TreeColumn(tree, SWT.LEFT);
		fColumn2.setText("Plugin");
		fColumn2.setWidth(150);

		tree.setHeaderVisible(true);
	}

	private class LogViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		private final int MAX_LABEL_LENGTH = 200;

		private final Image infoImage;
		private final Image okImage;
		private final Image errorImage;
		private final Image errorWithStackImage;
		private final Image warningImage;

		public LogViewLabelProvider() {
			errorImage = Images.get(Images.STATUS_ERROR);
			errorWithStackImage = Images.get(Images.STATUS_ERROR_STACK);
			warningImage = Images.get(Images.STATUS_WARNING);
			infoImage = Images.get(Images.STATUS_INFO);
			okImage = Images.get(Images.STATUS_OK);
		}

		public Image getColumnImage(Object element, int columnIndex) {
			IStatus entry = (IStatus) element;
			if (columnIndex == 0) {
				switch (entry.getSeverity()) {
				case IStatus.INFO:
					return infoImage;
				case IStatus.OK:
					return okImage;
				case IStatus.WARNING:
					return warningImage;
				default:
					return (entry.getException() == null ? errorImage
							: errorWithStackImage);
				}
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			IStatus entry = (IStatus) element;
			switch (columnIndex) {
			case 0:
				if (entry.getMessage() != null) {
					String message = entry.getMessage();
					if (message.length() > MAX_LABEL_LENGTH) {
						String warning = "... (Open details for full message)";
						StringBuffer sb = new StringBuffer(message.substring(0,
								MAX_LABEL_LENGTH - warning.length()));
						sb.append(warning);
						return sb.toString();
					}
					return entry.getMessage();
				}
			case 1:
				if (entry.getPlugin() != null)
					return entry.getPlugin();
			}

			return "";
		}
	}

	private class LogViewContentProvider implements ITreeContentProvider {

		public void dispose() {
		}

		public Object[] getChildren(Object element) {
			return ((IStatus) element).getChildren();
		}

		public Object[] getElements(Object element) {
			if (element instanceof Object[]) {
				return (Object[]) element;
			}
			return new Object[] { element };
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	public class StatusDetailsDialogAction extends SelectionProviderAction {

		private final Shell shell;
		private final ISelectionProvider provider;
		private StatusDetailsDialog propertyDialog;

		public StatusDetailsDialogAction(Shell shell,
				ISelectionProvider provider) {
			super(provider, "Details");
			Assert.isNotNull(shell);
			this.shell = shell;
			this.provider = provider;
		}

		public void resetSelection() {
			Object element = (Object) getStructuredSelection()
					.getFirstElement();
			if ((element == null) || (!(element instanceof IStatus)))
				return;
			if (propertyDialog != null && propertyDialog.isOpen())
				propertyDialog.resetSelection((IStatus) element);
		}

		@Override
		public void run() {
			if (propertyDialog != null && propertyDialog.isOpen()) {
				resetSelection();
				return;
			}

			Object element = (Object) getStructuredSelection()
					.getFirstElement();
			if ((element == null) || (!(element instanceof IStatus)))
				return;

			propertyDialog = new StatusDetailsDialog(shell, (IStatus) element,
					provider);
			propertyDialog.create();
			propertyDialog.getShell().setText("Log Entry Details");
			propertyDialog.open();
		}
	}
}
