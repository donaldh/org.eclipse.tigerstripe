package org.eclipse.tigerstripe.workbench.sdk.internal.ui.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.dialogs.SelectContributerDialog;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;

public abstract class AbstractWizardPage extends NewElementWizardPage {

	protected Text contributerText;
	protected IPluginModelBase contributerSelection = null; 
	protected IType classType = null;
	
	protected Shell shell;
	protected ISDKProvider provider;
	
	public AbstractWizardPage(String name) {
		super(name);

	}
	
	protected class WizardPageListener implements ModifyListener,
	 SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}


		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	protected abstract void updatePageComplete();
	protected abstract void handleWidgetSelected(SelectionEvent e);
	protected abstract void handleModifyText(ModifyEvent e);
	
	protected void chooseContributerButtonPressed(){
		try {
			SelectContributerDialog dialog = new SelectContributerDialog(
					this.shell,
					provider);
			dialog.setTitle("Contributer Selection");
			dialog.setMessage("Enter a filter (* = any number of characters)"
					+ " or an empty string for no filtering: ");

			IPluginModelBase contributerForSaving= dialog.browseAvailableContribuers();
				setContributerSelection(contributerForSaving);

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected String getContribution() {
		return contributerText.getText().trim();
	}


	protected IPluginModelBase getContributerSelection() {
		return contributerSelection;
	}


	protected void setContributerSelection(IPluginModelBase contributerSelection) {
		if (contributerSelection != null){
			this.contributerSelection = contributerSelection;
			contributerText.setText(contributerSelection.toString());
		}
	}
	
	
	protected void browseFilesButtonPressed(Text textWidget, String title){

		IResource res = (IResource) getContributerSelection().getAdapter(IResource.class);
		if (res != null){
			IProject contProject = (IProject) res.getProject();
			ResourceSelectionDialog dialog =
				new ResourceSelectionDialog(getShell(), contProject, title);
			dialog.open();
			
			if (dialog.getReturnCode() != dialog.CANCEL){

				if (dialog.getResult().length > 0 ) {
					if ((dialog.getResult()[0] instanceof IResource) && !(dialog.getResult()[0] instanceof IContainer)){
						IResource result = (IResource) dialog.getResult()[0];
						textWidget.setText(result.getProjectRelativePath().toString());
					}
				}
			}
		}
	}
	
	
	
	protected void browseClassButtonPressed(IPluginModelBase contributerSelection, Text textWidget, String title){
		IResource res = (IResource) contributerSelection.getAdapter(IResource.class);
		if (res != null){
			IProject contProject = (IProject) res.getProject();
			try {
				
				IJavaSearchScope scope= SearchEngine.createJavaSearchScope(new IJavaProject[] { JavaCore.create(contProject) }, false);
				
				SelectionDialog  dialog = JavaUI.createTypeDialog(getShell(), new ProgressMonitorDialog(getShell()), scope, 
						IJavaElementSearchConstants.CONSIDER_CLASSES, false);		
				dialog.setTitle(title);
				dialog.open();

				if (dialog.getReturnCode() != dialog.CANCEL){	
					if (dialog.getResult().length > 0 ) {
						IType type = (IType) dialog.getResult()[0];
						setClassType(type);
						textWidget.setText(type.getFullyQualifiedName());
					}
				}
			} catch (JavaModelException j){
				// Err?
			}
		}
	}
	
	public IType getClassType() {
		return classType;
	}


	public void setClassType(IType classType) {
		this.classType = classType;
	}
	
	
}
