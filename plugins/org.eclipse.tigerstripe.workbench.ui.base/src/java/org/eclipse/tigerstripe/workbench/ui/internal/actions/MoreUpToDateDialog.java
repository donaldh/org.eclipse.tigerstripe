package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.ModelCompareAction.VersionCompareResult;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * TODO: Incomplete
 * @author nmehrega
 *
 */
public class MoreUpToDateDialog extends SelectionDialog {

	private ITigerstripeModelProject fFirstProject = null;
	private ITigerstripeModelProject fSecondProject = null;
	private VersionCompareResult fMoreUpToDate;
	private Button fFirstProjectButton;
	private Button fSecondProjectButton;
	
	protected MoreUpToDateDialog(Shell parentShell, ITigerstripeModelProject firstProject, ITigerstripeModelProject secondProject, VersionCompareResult result) {
		super(parentShell);
		
		fFirstProject = firstProject;
		fSecondProject = secondProject;
		fMoreUpToDate = result;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
//		GridLayout compositeLayout = (GridLayout)composite.getLayout();
//		compositeLayout.numColumns = 2;
		
		Label instructions = new Label(composite, SWT.NONE);
		instructions.setText("Please indicate which is the more up-to-date project:");
		
		fFirstProjectButton = new Button(composite, SWT.RADIO);
		fSecondProjectButton = new Button(composite, SWT.RADIO);
		
		String buttonText = fFirstProject.getFullPath().toOSString();
		String version = null;
		try {
			version = fFirstProject.getProjectDetails().getVersion();
			
			if (version!=null && version.length()>0) 
				buttonText = buttonText + "  Version: " + version;
		} catch (Exception e) {
			// Ignore
		}
		
		fFirstProjectButton.setText(buttonText);
		
		buttonText = fSecondProject.getFullPath().toOSString();
		
		try {
			version = fSecondProject.getProjectDetails().getVersion();
			
			if (version!=null && version.length()>0)
				buttonText = buttonText + "  Version: " + version;
		} catch (Exception e) {
			// Ignore
		}
		
		fSecondProjectButton.setText(buttonText);
		
		// Set default selection
		if (fMoreUpToDate == VersionCompareResult.FIRST)
			fFirstProjectButton.setSelection(true);
		else if (fMoreUpToDate == VersionCompareResult.SECOND)
			fSecondProjectButton.setSelection(true);
		
		return composite;
		
//		Composite buttonComposite = new Composite(parent, SWT.NONE);
//		GridLayout buttonCompositeLayout = new GridLayout(2, false);
//		buttonComposite.setLayout(buttonCompositeLayout);
//		
//		Button okButton = new Button(buttonComposite, SWT.PUSH);
//		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
//		
//		okButton.setText("OK");
//		cancelButton.setText("Cancel");
//		
//		okButton.addSelectionListener(new SelectionAdapter() {
//
//			public void widgetSelected(SelectionEvent e) {
//				// TODO Auto-generated method stub
//				System.out
//						.println("MoreUpToDateDialog.createDialogArea(...).new SelectionAdapter() {...}.widgetSelected()");
//			}
//
//			
//		});
//		
//		
		
	}
	
	public ITigerstripeModelProject getProjectSelected() {
		if (fFirstProjectButton!=null && fFirstProjectButton.getSelection())
			return fFirstProject;
		else if (fSecondProjectButton!=null && fSecondProjectButton.getSelection())
			return fSecondProject;
		
		return null;
	}

}
