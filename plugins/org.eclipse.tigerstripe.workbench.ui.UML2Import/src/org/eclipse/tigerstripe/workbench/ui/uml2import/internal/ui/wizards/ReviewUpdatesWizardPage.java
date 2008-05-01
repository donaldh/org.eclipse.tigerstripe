package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import java.util.ArrayList;
import java.util.Iterator;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Difference;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.TSRuntimeBasedWizardPage;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model.CompareModelAndExtract;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model.ModelImporter;

public class ReviewUpdatesWizardPage extends TSRuntimeBasedWizardPage {

	private static final String PAGE_NAME = "ReviewUpdatesWizardPage";
	private Table table;
	private Button copyToClipboardButton;
	private String[] tableColumnNames = { "Scope", "Description" };
	private int[] tableColumnWidth = { 65, 400 };

	private MessageList messageList;
	private ModelImporter importer;
	ArrayList<Difference> projectDiffs;
	
	
	/**
	 * Creates a new <code>NewPackageWizardPage</code>
	 */
	public ReviewUpdatesWizardPage() {
		super(PAGE_NAME);

		setTitle("Proposed Project updates");
		setDescription("Please review changes before applying them to the Tigerstripe project.");
		this.messageList = new MessageList();
		setPageComplete(false);
	}
	
	@Override
	protected void initFromContext() {
		// TODO Auto-generated method stub

	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);

		table = new Table(composite, SWT.SINGLE | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// area.setContent(table);
		GridData gd = new GridData(GridData.CENTER | GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		table.setLayoutData(gd);

		createTableColumns(table);
		createTableRows(table);

		copyToClipboardButton = new Button(composite, SWT.PUSH);
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


		setControl(composite);
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
		table.clearAll();
		for (Iterator iter = messageList.getAllMessages().iterator(); iter
				.hasNext();) {
			Message msg = (Message) iter.next();
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(0, Message.severityToString(msg.getSeverity()));
			item.setText(1, msg.getMessage());
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		super.setVisible(visible);
		// When I go visible, try to get the model...
		MappingWizardPage mappingPage = (MappingWizardPage) getPreviousPage();
			importer = mappingPage.getImporter();
			importer.doSecondLoad();
			
			CompareModelAndExtract comparer = new CompareModelAndExtract();
			projectDiffs = comparer.compareExtractAndProject(importer.getExtractedArtifacts(), importer.getTigerstripeProject(), importer.getOut(), importer.getMessages());
			for (Difference diff : projectDiffs){
				System.out.println(diff.toString());
				ImportMessage mess = new ImportMessage();
				mess.setSeverity(1);
				mess.setMessage(diff.getShortString());
				messageList.addMessage(mess);
			}
			createTableRows(table);
			setPageComplete(true);
		try {
			
		} catch (Exception e){
			setErrorMessage("Failed to read from Model File");
			setPageComplete(false);
			
		}
	}

	public ArrayList<Difference> getProjectDiffs() {
		return projectDiffs;
	}
	
}
