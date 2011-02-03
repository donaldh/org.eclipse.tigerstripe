package org.eclipse.tigerstripe.workbench.ui.internal.wizards.pluggablePlugin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeLabelProvider;
import org.eclipse.ui.part.DrillDownComposite;

/**
 * Velocity template wizard page (enhancement#324709)
 * 
 * @author Navid Mehregani - Initial Creation
 */
public class NewVelocityWizardPage extends WizardPage {

	private final String GENERATOR_PROJECT_NATURE_ID = "org.eclipse.tigerstripe.workbench.base.tigerstripePluginProject";

	private final String M0_GENERATOR_PROJECT_NATURE_ID = "org.eclipse.tigerstripe.workbench.base.m0GeneratorProject";
	private ContainerSelectionGroup targetContainer;
	private Text fFilenameText = null;
	private boolean fCanFinish = false;
	private IContainer initialSelection;

	protected NewVelocityWizardPage(IStructuredSelection selection) {
		super("Create a new Velocity Template file");
		setTitle("Velocity Template File");
		setDescription("Wizard to create new Velocity Template File");
		if (selection != null) {
			Object obj = selection.getFirstElement();
			if (obj instanceof IResource) {
				if (obj instanceof IContainer) {
					initialSelection = (IContainer) obj;
				} else {
					initialSelection = ((IResource) obj).getParent();
				}
			}
		}
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		targetContainer = new ContainerSelectionGroup(composite,
				new Listener() {
					public void handleEvent(Event event) {
						validate();
					}
				});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = 2;
		targetContainer.setLayoutData(gridData);

		// Template file name label
		Label fileNameLabel = new Label(composite, SWT.NONE);
		fileNameLabel.setText("Filename: ");

		fFilenameText = new Text(composite, SWT.BORDER);
		fFilenameText.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));
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
		targetContainer.setInitialFocus();
		if (initialSelection != null) {
			targetContainer.setSelectedContainer(initialSelection);
		}
	}

	public String getFilename() {
		return fFilenameText.getText();
	}

	public IPath getTargetContainer() {
		IContainer selectedContainer = targetContainer.getSelectedContainer();
		if (selectedContainer != null) {
			return selectedContainer.getFullPath();
		}
		return null;
	}

	private void initialize() {
		fFilenameText.setText("myTemplateFile.vm");
	}

	private boolean isStringValid(String text) {
		return (text != null && text.length() > 0);
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
		IContainer selectedContainer = targetContainer.getSelectedContainer();

		fCanFinish = true;

		// Check selected project
		if (selectedContainer == null || selectedContainer instanceof IProject) {
			String errorMessage = "Destination folder isn't selected. Velocity template files"
					+ " can only be created for generator projects.";
			setErrorMessage(errorMessage);
			setMessage(errorMessage, WizardPage.ERROR);
			fCanFinish = false;
		}
		// Filename is empty
		else if (fileName == null || fileName.length() == 0) {
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

		// Do further checking. This can't be an 'else if' block, because the
		// file can contain '.', but still be valid.
		if (fCanFinish) {
			// Does the template file already exist?
			if (doesFileExist(selectedContainer, fileName)) {
				setErrorMessage("Specified file already exists");
				setMessage("Specified file already exists", WizardPage.ERROR);
				fCanFinish = false;
			}
			// Does filename contain invalid characters?
			else {
				for (int i = 0, n = INVALID_CHARACTERS.length(); i < n; i++) {
					if (fileName.contains(String.valueOf(INVALID_CHARACTERS
							.charAt(i)))) {
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

	private boolean doesFileExist(IContainer container, String fileName) {
		if (container == null || (!isStringValid(fileName))) {
			EclipsePlugin
					.logErrorMessage("Velocity template wizard (validation): folder and/or filename is invalid");
			return false;
		}

		IFile file = container.getFile(new Path(fileName));
		if ((file != null) && (file.exists()))
			return true;

		return false;

	}

	private class ContainerSelectionGroup extends Composite {
		private final Listener listener;

		private IContainer selectedContainer;

		private TreeViewer treeViewer;

		private static final int SIZING_SELECTION_PANE_WIDTH = 320;

		private static final int SIZING_SELECTION_PANE_HEIGHT = 300;

		public ContainerSelectionGroup(Composite parent, Listener listener) {
			this(parent, listener, null);
		}

		public ContainerSelectionGroup(Composite parent, Listener listener,
				String message) {
			this(parent, listener, message, SIZING_SELECTION_PANE_HEIGHT,
					SIZING_SELECTION_PANE_WIDTH);
		}

		public ContainerSelectionGroup(Composite parent, Listener listener,
				String message, int heightHint, int widthHint) {
			super(parent, SWT.NONE);
			this.listener = listener;
			if (message != null) {
				createContents(message, heightHint, widthHint);
			} else {
				createContents("Select the folder:", heightHint, widthHint);
			}
		}

		private void containerSelectionChanged(IContainer container) {
			selectedContainer = container;

			if (listener != null) {
				Event changeEvent = new Event();
				changeEvent.type = SWT.Selection;
				changeEvent.widget = this;
				listener.handleEvent(changeEvent);
			}
		}

		private void createContents(String message, int heightHint,
				int widthHint) {
			GridLayout layout = new GridLayout();
			layout.marginWidth = 0;
			setLayout(layout);
			setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			Label label = new Label(this, SWT.WRAP);
			label.setText(message);
			label.setFont(this.getFont());

			createTreeViewer(heightHint);
			Dialog.applyDialogFont(this);
		}

		private void createTreeViewer(int heightHint) {
			DrillDownComposite drillDown = new DrillDownComposite(this,
					SWT.BORDER);
			GridData spec = new GridData(SWT.FILL, SWT.FILL, true, true);
			spec.widthHint = SIZING_SELECTION_PANE_WIDTH;
			spec.heightHint = heightHint;
			drillDown.setLayoutData(spec);

			treeViewer = new TreeViewer(drillDown, SWT.NONE);
			drillDown.setChildTree(treeViewer);
			TemplatesContainerContentProvider cp = new TemplatesContainerContentProvider();
			treeViewer.setContentProvider(cp);
			treeViewer.setLabelProvider(new TigerstripeLabelProvider());
			treeViewer.setComparator(new ViewerComparator());
			treeViewer.setUseHashlookup(true);
			treeViewer
					.addSelectionChangedListener(new ISelectionChangedListener() {
						public void selectionChanged(SelectionChangedEvent event) {
							IStructuredSelection selection = (IStructuredSelection) event
									.getSelection();
							containerSelectionChanged((IContainer) selection
									.getFirstElement());
						}
					});
			treeViewer.addDoubleClickListener(new IDoubleClickListener() {
				public void doubleClick(DoubleClickEvent event) {
					ISelection selection = event.getSelection();
					if (selection instanceof IStructuredSelection) {
						Object item = ((IStructuredSelection) selection)
								.getFirstElement();
						if (item == null) {
							return;
						}
						if (treeViewer.getExpandedState(item)) {
							treeViewer.collapseToLevel(item, 1);
						} else {
							treeViewer.expandToLevel(item, 1);
						}
					}
				}
			});

			treeViewer.setInput(ResourcesPlugin.getWorkspace());
		}

		public IContainer getSelectedContainer() {
			return selectedContainer;
		}

		public void setInitialFocus() {
			treeViewer.getTree().setFocus();
		}

		public void setSelectedContainer(IContainer container) {
			selectedContainer = container;

			List<IContainer> itemsToExpand = new ArrayList<IContainer>();
			IContainer parent = container.getParent();
			while (parent != null) {
				itemsToExpand.add(0, parent);
				parent = parent.getParent();
			}
			treeViewer.setExpandedElements(itemsToExpand.toArray());
			treeViewer.setSelection(new StructuredSelection(container), true);
		}

		private class TemplatesContainerContentProvider implements
				ITreeContentProvider {

			public Object[] getChildren(Object element) {
				if (element instanceof IWorkspace) {
					List<IProject> result = new ArrayList<IProject>();
					for (IProject project : ((IWorkspace) element).getRoot()
							.getProjects()) {
						try {
							if (project.isOpen()
									&& (project
											.getNature(GENERATOR_PROJECT_NATURE_ID) != null || project
											.getNature(M0_GENERATOR_PROJECT_NATURE_ID) != null)) {
								result.add(project);
							}
						} catch (CoreException e) {
							EclipsePlugin.log(e);
						}
					}
					return result.toArray();
				} else if (element instanceof IContainer) {
					IContainer container = (IContainer) element;
					if (container.isAccessible()) {
						IResource[] members = null;

						if (container instanceof IProject) {
							IProject project = (IProject) container;
							IResource member = project.getFolder(new Path(
									"templates"));//$NON-NLS-1$
							if (member != null) {
								members = new IResource[] { member };
							}
						} else {
							try {
								members = container.members();
							} catch (CoreException e) {
								EclipsePlugin.log(e);
							}
						}

						if (members != null) {
							List<IResource> result = new ArrayList<IResource>();
							for (IResource member : members) {
								if (member.getType() != IResource.FILE) {
									result.add(member);
								}
							}
							return result.toArray();
						}
					}
				}
				return new Object[0];
			}

			public Object[] getElements(Object element) {
				return getChildren(element);
			}

			public Object getParent(Object element) {
				if (element instanceof IResource) {
					return ((IResource) element).getParent();
				}
				return null;
			}

			public boolean hasChildren(Object element) {
				return getChildren(element).length > 0;
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

			public void dispose() {
			}
		}
	}
}
