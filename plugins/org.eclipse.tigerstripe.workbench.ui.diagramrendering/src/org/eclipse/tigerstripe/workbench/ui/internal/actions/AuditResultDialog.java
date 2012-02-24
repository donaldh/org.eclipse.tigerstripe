package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import static org.eclipse.core.runtime.IStatus.WARNING;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

/**
 * Display the bunch of statuses
 * 
 * @since 2.1
 */
public class AuditResultDialog extends Dialog {

	private Button copyToClipboardButton;

	private final IStatus[] result;

	public AuditResultDialog(IShellProvider parentShell, IStatus[] result) {
		super(parentShell);
		this.result = result;
	}

	public AuditResultDialog(Shell parentShell, IStatus[] result) {
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
		if (hasErrors(result)) {
			overallResult.setText("Audit Failed.");
			overallResult.setForeground(ColorUtils.TS_ORANGE);
		} else if (hasWarnings(result)) {
			overallResult.setText("Warnings during audit.");
			overallResult.setForeground(ColorUtils.TS_ORANGE);
		} else {
			overallResult.setText("Audit Successful.");
			overallResult.setForeground(ColorUtils.BLACK);
		}
		overallResult.setFont(new Font(null, "Arial", 12, SWT.BOLD));

		Label versionLabel = new Label(area, SWT.NONE);
		versionLabel.setText("Audit Log");
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

		getShell().setText("Audit Result");
		return area;
	}

	private boolean hasWarnings(IStatus[] statuses) {
		for (IStatus st : statuses) {
			if (st.getSeverity() == WARNING) {
				return true;
			}
		}
		return false;
	}

	private void copyToClipboard() {
		Clipboard clipboard = new Clipboard(getShell().getDisplay());
		clipboard.setContents(new Object[] { asText(result) },
				new Transfer[] { TextTransfer.getInstance() });
		clipboard .dispose();
	}

	private String asText(IStatus[] result) {
		StringBuilder buffer = new StringBuilder();

		for (IStatus res : result) {
			
			buffer.append(res.getMessage()).append("\n");
			Throwable th = res.getException();
			if (th != null) {
				StringWriter thWriter = new StringWriter();
				th.printStackTrace(new PrintWriter(thWriter));
				buffer.append(thWriter).append("\n");
			}
		}
		return buffer.toString();
	}

	private boolean hasErrors(IStatus[] result) {
		for (IStatus res : result) {
			if (res.getSeverity() == IStatus.ERROR) {
				return true;
			}
		}
		return false;
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
	
	static class StatusDetailsDialog extends TrayDialog {

		private IStatus status;
		
		private final ITableLabelProvider labelProvider;
		private final TreeViewer provider;

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

		@Override
		public int open() {
			isOpen = true;
			return super.open();
		}

		@Override
		public boolean close() {
			isOpen = false;
			return super.close();
		}

		@Override
		public void create() {
			super.create();

			getShell().setSize(500, 550);

			applyDialogFont(buttonBar);
			getButton(IDialogConstants.OK_ID).setFocus();
		}

		@Override
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
				String stack = getStack(status);
				if (stack != null) {
					details = stack;
				} else {
					details = "Details not available.";
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

		@Override
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

		@Override
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
			label.setText("Severity:");
			severityImageLabel = new Label(textContainer, SWT.NULL);
			severityLabel = new Label(textContainer, SWT.NULL);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			severityLabel.setLayoutData(gd);
		}

		private void createDetailsSection(Composite parent) {
			Composite container = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout(2, false);
			layout.marginHeight = 0;
			layout.marginWidth = 6;
			container.setLayout(layout);
			GridData gd = new GridData(GridData.FILL_BOTH);
			container.setLayoutData(gd);

			Label label = new Label(container, SWT.NONE);
			label.setText("Message:");
			gd = new GridData();
			gd.verticalAlignment = SWT.BOTTOM;
			label.setLayoutData(gd);

			msgText = new Text(container, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP
					| SWT.BORDER);
			gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL);
			gd.grabExcessHorizontalSpace = true;
			gd.heightHint = 50;
			gd.horizontalSpan = 2;
			msgText.setLayoutData(gd);
			msgText.setEditable(false);

			label = new Label(container, SWT.NONE);
			label.setText("Details:");
			gd = new GridData();
			gd.verticalAlignment = SWT.BOTTOM;
			label.setLayoutData(gd);

			detailsText = new Text(container, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
			gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL);
			gd.grabExcessHorizontalSpace = true;
			gd.heightHint = 50;
			gd.horizontalSpan = 2;
			detailsText.setLayoutData(gd);
			detailsText.setEditable(false);
		}
	}

	
}
