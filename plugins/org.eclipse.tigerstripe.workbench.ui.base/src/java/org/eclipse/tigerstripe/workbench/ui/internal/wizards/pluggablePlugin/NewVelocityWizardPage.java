package org.eclipse.tigerstripe.workbench.ui.internal.wizards.pluggablePlugin;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

/**
 * Velocity template wizard page (enhancement#324709)
 * 
 * @author Navid Mehregani - Initial Creation
 */
public class NewVelocityWizardPage extends WizardPage {
	
	private final String GENERATOR_PROJECT_NATURE_ID = "org.eclipse.tigerstripe.workbench.base.tigerstripePluginProject";
	private Combo fProjectComboBox = null;
	private Text  fFilenameText = null;
	private boolean fCanFinish = false;
	private String fSelectedProjectName = null;
	
	protected NewVelocityWizardPage(IStructuredSelection selection) {
		super("Create a new Velocity Template file");
		setTitle("Velocity Template File");
		setDescription("Wizard to create new Velocity Template File");
		if (selection != null) {
			Object obj = selection.getFirstElement();
			if (obj instanceof IResource) {
				IProject project = ((IResource)obj).getProject();
				if (project!=null) 
					fSelectedProjectName = project.getName();
			} else if (obj instanceof IPackageFragmentRoot) {
				IJavaProject project = ((IPackageFragmentRoot)obj).getJavaProject();
				if (project!=null) {
					fSelectedProjectName = project.getElementName();
				}
			}
		}
	}


	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		
		// Generator Project label
		Label projectLabel = new Label(composite, SWT.NONE);
		projectLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		projectLabel.setText("&Generator Project: ");
		
		fProjectComboBox = new Combo(composite, SWT.READ_ONLY);
		
		GridData projectComboBoxGridData = new GridData();
		projectComboBoxGridData.horizontalAlignment = GridData.FILL;
		projectComboBoxGridData.grabExcessHorizontalSpace = true;
		fProjectComboBox.setLayoutData(projectComboBoxGridData);
		
		// Template file name label 
		Label fileNameLabel = new Label(composite, SWT.NONE);
		fileNameLabel.setText("Filename: ");
		
		fFilenameText = new Text(composite, SWT.BORDER);
		fFilenameText.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		fFilenameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}			
		});
		
		GridData fileNameTextGridData = new GridData();
		fileNameTextGridData.horizontalAlignment = GridData.FILL;
		fileNameTextGridData.grabExcessHorizontalSpace = true;
		fFilenameText.setLayoutData(fileNameTextGridData);	
		
		initialize();
		
		setControl(composite);
	    setVisible(true);
	}
	
	public String getFilename() {
		return fFilenameText.getText();
	}
	
	public String getProjectName() {
		return fProjectComboBox.getText();
	}
	
	private void initialize() {
		boolean selectedProjectIsValid = false;
		ArrayList<String> projectNames = new ArrayList<String>();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i=0; i < projects.length; i++) {
			IProject project = projects[i];
			if (project!=null) {
				try {
					IProjectNature nature = project.getNature(GENERATOR_PROJECT_NATURE_ID);
					if (nature!=null) {
						String projectName = project.getName();
						if (isStringValid(projectName)) {
							projectNames.add(projectName);
							if (projectName.equals(fSelectedProjectName))
								selectedProjectIsValid = true;
						}
					}
				} catch (CoreException e) {
					continue;
				}
			}
		}
		
		if (projectNames.size() > 0) {
			String[] projectNameArray = projectNames.toArray(new String[projectNames.size()]);
			fProjectComboBox.setItems(projectNameArray);
			if (selectedProjectIsValid)
				fProjectComboBox.setText(fSelectedProjectName);
			else
				fProjectComboBox.setText(projectNameArray[0]);
		}
		
		fFilenameText.setText("myTemplateFile.vm");
	}
	
	private boolean isStringValid(String text) {
		return (text!=null && text.length()>0);
	}

	/**
	 * Validates the wizard content
	 * 
	 * @return True if wizard content is valid; false otherwise
	 */
	private boolean validate() {
		String fileName = fFilenameText.getText();
		String fileExtension = null;
		final String INVALID_CHARACTERS = "~!@#$%^&*()+ ";
		String selectedProject = fProjectComboBox.getText();
		
		fCanFinish = true;
		
		// Check selected project
		if (selectedProject==null || selectedProject.length()==0) {
			String errorMessage = "Generator project not selected. Velocity template files can only be created for generator projects. Make sure there is at least one defined in the workspace.";
			setErrorMessage(errorMessage);
    		setMessage(errorMessage, WizardPage.ERROR);
    		fCanFinish = false;
		}		
		// Filename is empty
		else if (fileName==null || fileName.length()==0) {
			String errorMessage = "Filename cannot be empty";
			setErrorMessage(errorMessage);
    		setMessage(errorMessage, WizardPage.ERROR);
    		fCanFinish = false;
		}		
		// Filename ends with '.'
		else if (fileName.endsWith(".")) {
			setErrorMessage("Filename cannot end with '.'");
    		setMessage("Filename cannot end with '.'", WizardPage.ERROR);
    		fCanFinish = false;
		}		
		// Check file extension, make sure it's 'vm'
		else if (fileName.contains(".")) {
			fileExtension = fileName.substring(fileName.indexOf(".") + 1);
			if (!fileExtension.equalsIgnoreCase("vm")) {
				setErrorMessage("File extension must be 'vm'");
	    		setMessage("File extension must be 'vm'", WizardPage.ERROR);
	    		fCanFinish = false;
			}
		} 
		
		// Do further checking.  This can't be an 'else if' block, because the file can contain '.', but still be valid.
		if (fCanFinish) {
			// Does the template file already exist?
			if (doesFileExist(selectedProject, fileName)) {
				setErrorMessage("Specified file already exists in the selected project");
	    		setMessage("Specified file already exists in the selected project", WizardPage.ERROR);
	    		fCanFinish = false;
			}
			// Does filename contain invalid characters?
			else {
				for (int i=0, n=INVALID_CHARACTERS.length(); i < n; i++) {
					if (fileName.contains(String.valueOf(INVALID_CHARACTERS.charAt(i)))) {
						setErrorMessage("Filename is invalid");
			    		setMessage("Filename is invalid", WizardPage.ERROR);
			    		fCanFinish = false;
					}
				}
			}
		}
		
		
		if (fCanFinish) {
			setErrorMessage(null);
			setMessage(null);
		}
		
		getContainer().updateButtons();		
		return fCanFinish;
	}
	
	public boolean canFinish() {
		return fCanFinish;
	}
	
	private boolean doesFileExist(String projectName, String fileName) {
		
		if ((!isStringValid(projectName)) || (!isStringValid(fileName))) {
			EclipsePlugin.logErrorMessage("Velocity template wizard (validation): project name and/or filename is invalid");
			return false;
		}
		
		try {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			if (project!=null) {
				if ((projectName.equals(project.getName())) && (project.getNature(GENERATOR_PROJECT_NATURE_ID)!=null)) {
					IFile file = project.getFile(new Path("/templates/" + fileName));
					if ((file!=null) && (file.exists()))
						return true;
				}
			}
		} catch(CoreException e) {
			EclipsePlugin.logErrorMessage("Velocity template wizard (validation): exception - " + e.getMessage());
		}
		
		return false;
		
	}


}
