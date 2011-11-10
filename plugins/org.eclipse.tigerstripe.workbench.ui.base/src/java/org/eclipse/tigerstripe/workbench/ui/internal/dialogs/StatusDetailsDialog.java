package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;

public class StatusDetailsDialog extends TrayDialog {

	private IStatus status;
	
	private ITableLabelProvider labelProvider;
	private TreeViewer provider;

	private boolean isOpen;
	
	private Label severityImageLabel;
	private Label severityLabel;
	private Text msgText;
	private Text detailsText;
	private SashForm sashForm;

	protected StatusDetailsDialog(Shell parentShell, IStatus selection, ISelectionProvider provider) {
		super(parentShell);
		this.provider = (TreeViewer) provider;
		this.labelProvider = (ITableLabelProvider) this.provider.getLabelProvider();
		this.status = selection;
		setShellStyle(SWT.MODELESS | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.CLOSE | SWT.BORDER | SWT.TITLE);
	}

	public boolean isOpen() {
		return isOpen;
	}

	public int open() {
		isOpen = true;
		return super.open();
	}

	public boolean close() {
		isOpen = false;
		return super.close();
	}

	public void create() {
		super.create();

		getShell().setSize(500, 550);

		applyDialogFont(buttonBar);
		getButton(IDialogConstants.OK_ID).setFocus();
	}

	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId)
			okPressed();
		else if (IDialogConstants.CANCEL_ID == buttonId)
			cancelPressed();
	}

	public void resetSelection(IStatus selected) {
		if (status.equals(selected)) {
			updateProperties();
			return;
		}
		status = selected;
		updateProperties();
	}

	public void updateProperties() {
		if (status != null) {
			IStatus statusEntry = status;
			severityImageLabel
					.setImage(labelProvider.getColumnImage(status, 0));
			severityLabel.setText(getSeverityText(statusEntry.getSeverity()));
			msgText.setText(statusEntry.getMessage() != null ? statusEntry
					.getMessage() : "");
			String details;
			if (statusEntry instanceof PluginRunStatus) {
				details = ((PluginRunStatus) statusEntry).toString(false, false);
			} else {
				String stack = getStack(status);
				if (stack != null) {
					details = stack;
				} else {
					details = "An exception stack trace is not available.";
				}
			}
			detailsText.setText(details);
		} else {
			severityImageLabel.setImage(null);
			severityLabel.setText("");
			msgText.setText("");
			detailsText.setText("");
		}
	}
	
	private String getSeverityText(int severity) {
		switch (severity) {
			case IStatus.ERROR : {
				return "Error";
			}
			case IStatus.WARNING : {
				return "Warning";
			}
			case IStatus.INFO : {
				return "Info";
			}
			case IStatus.OK : {
				return "Ok";
			}
		}
		return "?";
	}
	
	private String getStack(IStatus status) {
		Throwable throwable = status.getException();
		if (throwable != null) {
			StringWriter swriter = new StringWriter();
			PrintWriter pwriter = new PrintWriter(swriter);
			throwable.printStackTrace(pwriter);
			pwriter.flush();
			pwriter.close();
			return swriter.toString();
		}
		return null;
	}

	public SashForm getSashForm() {
		return sashForm;
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(gd);

		createHeadSection(container);
		createSashForm(container);
		createDetailsSection(getSashForm());
		
		updateProperties();
		Dialog.applyDialogFont(container);
		return container;
	}

	private void createSashForm(Composite parent) {
		sashForm = new SashForm(parent, SWT.VERTICAL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 0;
		sashForm.setLayout(layout);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	protected void createButtonsForButtonBar(Composite parent) {
		// create OK button only by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	private void createHeadSection(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createTextSection(container);
	}

	private void createTextSection(Composite parent) {
		Composite textContainer = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginHeight = layout.marginWidth = 0;
		textContainer.setLayout(layout);
		textContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(textContainer, SWT.NONE);
		label.setText("Severity");
		severityImageLabel = new Label(textContainer, SWT.NULL);
		severityLabel = new Label(textContainer, SWT.NULL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		severityLabel.setLayoutData(gd);

		label = new Label(textContainer, SWT.NONE);
		label.setText("Message:");
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		label.setLayoutData(gd);
		msgText = new Text(textContainer, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		msgText.setEditable(false);
		gd = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING | GridData.GRAB_VERTICAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 44;
		gd.grabExcessVerticalSpace = true;
		msgText.setLayoutData(gd);
	}

	private void createDetailsSection(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 6;
		container.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 100;
		container.setLayoutData(gd);

		Label label = new Label(container, SWT.NONE);
		label.setText("Details:");
		gd = new GridData();
		gd.verticalAlignment = SWT.BOTTOM;
		label.setLayoutData(gd);

		detailsText = new Text(container, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		detailsText.setLayoutData(gd);
		detailsText.setEditable(false);
	}
}
