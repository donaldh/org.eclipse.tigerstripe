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
package org.eclipse.tigerstripe.eclipse.wizards.project;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.modules.ExternalModules;
import org.eclipse.tigerstripe.core.module.packaging.CopyToDirJellyTask;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewProjectWizardPageTwo extends WizardPage {
	private Text projectNameText;

	private String projectDirectory;

	private Map cMo = new HashMap();

	private Map modules;

	private HashMap moduleMapByFile = new HashMap<String, String>();

	public NewProjectWizardPageTwo(ImageDescriptor image) {
		super("wizardPage");
		setTitle("New Tigerstripe Project");
		setImageDescriptor(image);

		setDescription("This wizard creates new Tigerstripe Project.");
		ExternalModules.getInstance().reload();
		this.modules = ExternalModules.modules;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		Group projectLocation = new Group(container, SWT.NULL);
		projectLocation.setText("External Modules");
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		projectLocation.setLayout(gridLayout);
		final GridData locationGridData = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		locationGridData.horizontalSpan = 2;
		locationGridData.verticalSpan = 4;
		projectLocation.setLayoutData(locationGridData);

		String moduleValues = "";
		TreeObject lastOne = null;
		Map loaded = new HashMap();
		Map root = new HashMap();

		Set<String> moduleKeys = modules.keySet();
		String[] st = new String[moduleKeys.size()];
		st = moduleKeys.toArray(st);

		StringBuffer modulePath = new StringBuffer();

		for (int l = 0; l < st.length; l++) {
			String[] rootPlus = st[l].split("[/\\\\]");
			if (!root.containsKey(rootPlus[0])) {
				root.put(rootPlus[0], l);
			}
		}
		final TreeObject[] rootFolders = new TreeObject[root.size()];

		int h = 0;

		for (int i = 0; i < st.length; i++) {

			String[] multFolders = st[i].split("[/\\\\]");
			TreeObject[] otherFolders = new TreeObject[multFolders.length];
			for (int j = 0; j < multFolders.length; j++) {

				if (!loaded.containsKey(multFolders[j])) {
					if (j == 0) {
						if (!loaded.containsKey(multFolders[j])) {
							rootFolders[h] = new TreeObject(multFolders[0],
									null);
							otherFolders[j] = rootFolders[h];
							lastOne = rootFolders[h];
							modulePath.append(multFolders[0]);
							modulePath.append("\\");
							moduleValues = "";
							if (modules.containsKey(multFolders[0])) {
								moduleValues = (String) modules
										.get(multFolders[0]);
								if (moduleValues != "") {
									String[] strModules = moduleValues
											.split("~");
									for (int k = 0; k < strModules.length; k++) {
										String[] parts = strModules[k]
												.split(":");
										new TreeObject(parts[0], lastOne);
									}
								}
							}
							loaded.put(multFolders[j], multFolders[j]);
							h++;
						}
					} else {
						if (!loaded.containsKey(multFolders[j])) {
							otherFolders[j] = new TreeObject(multFolders[j],
									otherFolders[j - 1]);
							lastOne = otherFolders[j];
							modulePath.append(multFolders[j]);
							moduleValues = "";
							if (modules.containsKey(modulePath.toString())) {
								moduleValues = (String) modules.get(modulePath
										.toString());
								if (moduleValues != "") {
									String[] strModules = moduleValues
											.split("~");
									for (int k = 0; k < strModules.length; k++) {
										String[] parts = strModules[k]
												.split(":");
										new TreeObject(parts[0], lastOne);
									}
								}
								modulePath.append("\\");
							}
							loaded.put(multFolders[j], multFolders[j]);
						}
					}
				}

			}
		}

		GridData data = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL);

		final CheckboxTreeViewer ctv = new CheckboxTreeViewer(projectLocation);
		ctv.getControl().setLayoutData(data);
		ctv.setContentProvider(new ITreeContentProvider() {
			public Object[] getChildren(Object parentElement) {
				return ((TreeObject) parentElement).children
						.toArray(new Object[((TreeObject) parentElement).children
								.size()]);
			}

			public Object getParent(Object element) {
				return ((TreeObject) element).parent;
			}

			public boolean hasChildren(Object element) {
				return ((TreeObject) element).children.size() > 0;
			}

			public Object[] getElements(Object inputElement) {
				return rootFolders;
			}

			public void dispose() {

			}

			public void inputChanged(Viewer viewer, Object oldObject,
					Object newInput) {

			}
		});

		final LabelProvider labelProvider = new LabelProvider();
		ctv.setLabelProvider(labelProvider);
		ctv.setInput("root1");
		ctv.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent se) {
				IStructuredSelection iss = (IStructuredSelection) ctv
						.getSelection();
				if (iss.getFirstElement() != null) {
					TreeObject selected = (TreeObject) iss.getFirstElement();
					if (!selected.hasChildren(selected)) {
						displayModuleDescription((TreeObject) iss
								.getFirstElement());
					}
				}
			}
		});
		ctv.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent csce) {
				if (csce.getChecked()) {
					ctv.setSubtreeChecked(csce.getElement(), true);
					Object[] checked = ctv.getCheckedElements();
					int j = 0;
					String[] checkedModules = new String[checked.length];
					for (int i = 0; i < checked.length; i++) {
						if (!((TreeObject) checked[i])
								.hasChildren((TreeObject) checked[i])) {
							checkedModules[j] = ((TreeObject) checked[i])
									.toString();
							cMo.put(checkedModules[j], j);
							j++;
						}
					}
					for (int k = 0; k < checkedModules.length; k++) {
						if (checkedModules[k] != null) {
						}
					}
					displayModuleDescription((TreeObject) csce.getElement());
				}
				if (!csce.getChecked()) {
					cMo.remove(csce.getElement().toString());
				}
			}
		});

		createModuleValuesGroup(container);
		initialize();
		updatePage();
		setControl(container);
	}

	private Label moduleDetailsNameLabel;

	private Text moduleDetailsNameText;

	private Label moduleDetailsIdLabel;

	private Text moduleDetailsIdText;

	private Label moduleDetailsVersionLabel;

	private Text moduleDetailsVersionText;

	private Label moduleDetailsDescLabel;

	private Text moduleDetailsDescText;

	public void createModuleValuesGroup(Composite parent) {
		Group moduleGroup = new Group(parent, SWT.NULL);
		moduleGroup.setText("");
		final GridLayout natureGridLayout = new GridLayout();
		natureGridLayout.numColumns = 2;
		moduleGroup.setLayout(natureGridLayout);
		final GridData natureGridData = new GridData(GridData.FILL_HORIZONTAL);
		natureGridData.horizontalSpan = 2;
		moduleGroup.setLayoutData(natureGridData);

		moduleDetailsNameLabel = new Label(moduleGroup, SWT.NONE);
		moduleDetailsNameLabel.setLayoutData(new GridData(GridData.BEGINNING));
		moduleDetailsNameLabel.setText("Module Name :");

		moduleDetailsNameText = new Text(moduleGroup, SWT.BORDER);
		moduleDetailsNameText.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		moduleDetailsNameText.setText("");
		moduleDetailsNameText.setEditable(false);

		moduleDetailsIdLabel = new Label(moduleGroup, SWT.NONE);
		moduleDetailsIdLabel.setLayoutData(new GridData(GridData.BEGINNING));
		moduleDetailsIdLabel.setText("Module Id :");

		moduleDetailsIdText = new Text(moduleGroup, SWT.BORDER);
		moduleDetailsIdText
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		moduleDetailsIdText.setText("");
		moduleDetailsIdText.setEditable(false);

		moduleDetailsVersionLabel = new Label(moduleGroup, SWT.NONE);
		moduleDetailsVersionLabel
				.setLayoutData(new GridData(GridData.BEGINNING));
		moduleDetailsVersionLabel.setText("Module Version :");

		moduleDetailsVersionText = new Text(moduleGroup, SWT.BORDER);
		moduleDetailsVersionText.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		moduleDetailsVersionText.setText("");
		moduleDetailsVersionText.setEditable(false);

		moduleDetailsDescLabel = new Label(moduleGroup, SWT.NONE);
		moduleDetailsDescLabel.setLayoutData(new GridData(GridData.BEGINNING));
		moduleDetailsDescLabel.setText("Module Description :");

		moduleDetailsDescText = new Text(moduleGroup, SWT.BORDER | SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 50;
		moduleDetailsDescText.setLayoutData(gd);
		moduleDetailsDescText.setText("");
		moduleDetailsDescText.setEditable(false);

	}

	private void displayModuleDescription(TreeObject module) {
		moduleDetailsIdText.setText("");
		moduleDetailsVersionText.setText("");
		moduleDetailsDescText.setText("");

		ExternalModules extM = ExternalModules.getInstance();
		if (ExternalModules.modulesExist) {
			moduleMapByFile = ExternalModules.moduleMapByFile;
		}

		if (!module.hasChildren(module)) {
			String path = moduleMapByFile.get(module.toString()).toString();
			String[] spl = path.split("modules");
			String parent = module.getParent().toString();
			// Now get the corresponding Map entry
			String parentMap = (String) modules.get(spl[1].substring(1, spl[1]
					.length() - 1));
			String[] allModules = parentMap.split("~");
			// Go through the loop to find out which one
			for (int i = 0; i < allModules.length; i++) {
				String[] components = allModules[i].split(":");
				if (components[0].equalsIgnoreCase(module.toString())) {
					moduleDetailsNameText.setText(components[1]);
					moduleDetailsIdText.setText(components[2]);
					moduleDetailsVersionText.setText(components[3]);
					moduleDetailsDescText.setText(components[4]);
				}
			}
		}
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		// defaultDirectoryPath = Platform.getLocation();
	}

	/**
	 * 
	 * @return
	 */
	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(
				projectNameText.getText().trim());
	}

	/**
	 * Returns the current project location path as entered by the user, or its
	 * anticipated initial value. Note that if the default has been returned the
	 * path in a project description used to create a project should not be set.
	 * 
	 * @return the project location path or its anticipated initial value.
	 */
	public IPath getLocationPath() {
		return new Path(projectDirectory.trim());
	}

	public Map getCMo() {
		return cMo;
	}

	/**
	 * Ensures that both text fields are set.
	 */
	private void updatePage() {
	}

	static class TreeObject {
		String s;

		TreeObject parent;

		ArrayList children;

		TreeObject(String s, TreeObject parent) {
			this.s = s;
			this.parent = parent;
			children = new ArrayList();
			if (parent != null) {
				parent.addChild(this);
			}
		}

		@Override
		public String toString() {
			return s;
		}

		public boolean isRoot() {
			boolean retValue = false;
			if (parent == null) {
				retValue = true;
			}
			return retValue;
		}

		public TreeObject getParent() {
			return parent;
		}

		public boolean hasChildren(TreeObject to) {
			boolean retValue = false;
			if (to.getChildren(to).size() > 0) {
				retValue = true;
			}
			return retValue;
		}

		public ArrayList getChildren(TreeObject root) {
			return children;
		}

		void addChild(TreeObject child) {
			children.add(child);
		}
	}

	/**
	 * Returns all the modules that are required to be added to the new created
	 * project That includes those that have been selected by the user, and
	 * those that are the "base" mandatory ones.
	 * 
	 * @return
	 */
	public Map getRequiredModules() {
		Map reqModules = new HashMap();

		if (ExternalModules.modulesExist) {
			reqModules = getCMo();
		}

		if (ExternalModules.baseModules.containsKey("base")) {
			String allValues = (String) ExternalModules.baseModules.get("base");
			String[] allModules = allValues.split("~");
			for (int i = 0; i < allModules.length; i++) {
				String[] moduleName = allModules[i].split(":");
				reqModules.put(moduleName[0], i);
			}
		}
		return reqModules;
	}

	public IRunnableWithProgress getRunnable(final NewProjectWizardPage pageOne) {
		return new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {

				NewProjectDetails projectDetails = pageOne
						.getProjectNewProjectDetails();

				// get a project descriptor
				IPath defaultPath = Platform.getLocation();
				IPath newPath = getLocationPath();
				if (defaultPath.equals(newPath)) {
					newPath = null;
				}
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				final IProjectDescription description = workspace
						.newProjectDescription(pageOne.getProjectName());
				description.setLocation(newPath);

				Set<String> cSet = getRequiredModules().keySet();
				String[] strs = new String[cSet.size()];
				strs = cSet.toArray(strs);
				for (String s : strs) {
					String directory = projectDetails.getProjectDirectory();
					String name = projectDetails.getProjectName();
					String copyDir = directory + File.separator + name;
					int posi = directory.indexOf(name);
					if (posi > 0) {
						copyDir = directory;
					}
					// Copy the modules into the project folder
					CopyToDirJellyTask co = new CopyToDirJellyTask(new File(
							copyDir), new File(moduleMapByFile.get(s)
							+ File.separator + s));
					try {
						co.run();
					} catch (TigerstripeException e) {
						throw new InvocationTargetException(e);
					}
				}
			}
		};
	}
}