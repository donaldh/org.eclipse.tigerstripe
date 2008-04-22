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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.DefaultContentProvider;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.FileEditorInput;

public class IncludedFilesSection extends GeneratorDescriptorSectionPart implements
		IResourceChangeListener, IResourceDeltaVisitor {

	protected CheckboxTreeViewer fTreeViewer;

	private boolean fDoRefresh = false;

	protected IProject fProject;

	protected IResource fOriginalResource, fParentResource;

	protected boolean isChecked;

	public class TreeContentProvider extends DefaultContentProvider implements
			ITreeContentProvider {

		public Object[] getElements(Object parent) {
			if (parent instanceof IProject) {
				try {
					return ((IProject) parent).members();
				} catch (CoreException e) {
					EclipsePlugin.log(e);
				}
			}
			return new Object[0];
		}

		/**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(Object parent) {
			try {
				if (parent instanceof IFolder)
					return ((IFolder) parent).members();
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
			return new Object[0];
		}

		public Object[] getFolderChildren(Object parent) {
			IResource[] members = null;
			try {
				if (!(parent instanceof IFolder))
					return new Object[0];
				members = ((IFolder) parent).members();
				ArrayList results = new ArrayList();
				for (int i = 0; i < members.length; i++) {
					if ((members[i].getType() == IResource.FOLDER)) {
						results.add(members[i]);
					}
				}
				return results.toArray();
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
			return new Object[0];
		}

		/**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		public Object getParent(Object element) {
			if (element != null && element instanceof IResource)
				return ((IResource) element).getParent();
			return null;
		}

		/**
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		public boolean hasChildren(Object element) {
			if (element instanceof IFolder)
				return getChildren(element).length > 0;
			return false;
		}
	}

	public IncludedFilesSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Included Files");
		EclipsePlugin.getWorkspace().addResourceChangeListener(this);

		FileEditorInput input = (FileEditorInput) getPage().getEditorInput();
		fProject = input.getFile().getProject();

		createContent();
	}

	@Override
	protected void createContent() {

		final Section section = getSection();
		FormToolkit toolkit = getToolkit();

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		Composite container = createClientContainer(section, 2, toolkit);

		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 2;
		container.setLayout(layout);
		fTreeViewer = new CheckboxTreeViewer(toolkit.createTree(container,
				SWT.CHECK));
		fTreeViewer.setContentProvider(new TreeContentProvider());
		fTreeViewer.setLabelProvider(new WorkbenchLabelProvider());
		fTreeViewer.setAutoExpandLevel(0);
		fTreeViewer.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(final CheckStateChangedEvent event) {
				final Object element = event.getElement();
				BusyIndicator.showWhile(section.getDisplay(), new Runnable() {

					public void run() {
						if (element instanceof IFile) {
							IFile file = (IFile) event.getElement();
							handleCheckStateChanged(file, event.getChecked());
						} else if (element instanceof IFolder) {
							IFolder folder = (IFolder) event.getElement();
							handleCheckStateChanged(folder, event.getChecked());
						}
					}
				});
			}
		});
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 150;
		gd.widthHint = 100;
		fTreeViewer.getTree().setLayoutData(gd);
		initialize();
		try {
			initializeCheckState();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		toolkit.paintBordersFor(container);
		section.setClient(container);

	}

	protected void handleCheckStateChanged(IResource resource, boolean checked) {
		fOriginalResource = resource;
		isChecked = checked;
		boolean wasTopParentChecked = fTreeViewer.getChecked(fOriginalResource
				.getParent());
		if (!isChecked) {
			resource = handleAllUnselected(resource, resource.getName());
		}
		fParentResource = resource;
		handleBuildCheckStateChange(wasTopParentChecked);
	}

	protected IResource handleAllUnselected(IResource resource, String name) {
		IResource parent = resource.getParent();
		if (parent == resource.getProject())
			return resource;
		try {
			boolean uncheck = true;
			IResource[] members = ((IFolder) parent).members();
			for (int i = 0; i < members.length; i++) {
				if (fTreeViewer.getChecked(members[i])
						&& !members[i].getName().equals(name))
					uncheck = false;
			}
			if (uncheck)
				return handleAllUnselected(parent, parent.getName());
			return resource;
		} catch (CoreException e) {
			EclipsePlugin.log(e);
			return null;
		}
	}

	protected void setChildrenGrayed(IResource folder, boolean isGray) {
		fTreeViewer.setGrayed(folder, isGray);
		if (((TreeContentProvider) fTreeViewer.getContentProvider())
				.hasChildren(folder)) {
			Object[] members = ((TreeContentProvider) fTreeViewer
					.getContentProvider()).getFolderChildren(folder);
			for (int i = 0; i < members.length; i++) {
				setChildrenGrayed((IFolder) members[i], isGray);
			}
		}
	}

	protected void setParentsChecked(IResource resource) {
		if (resource.getParent() != resource.getProject()) {
			fTreeViewer.setChecked(resource.getParent(), true);
			setParentsChecked(resource.getParent());
		}
	}

	protected Composite createClientContainer(Composite parent, int span,
			FormToolkit toolkit) {
		Composite container = toolkit.createComposite(parent);
		org.eclipse.swt.layout.GridLayout layout = new org.eclipse.swt.layout.GridLayout();
		layout.marginWidth = layout.marginHeight = 2;
		layout.numColumns = span;
		container.setLayout(layout);
		return container;
	}

	protected void initializeCheckState() throws TigerstripeException {

		uncheckAll();
		final ITigerstripeGeneratorProject project = getIPluggablePluginProject();
		final List<String> excludes = Arrays
				.asList(project
						.getAdditionalFiles(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_EXCLUDE));
		final List<String> includes = Arrays
				.asList(project
						.getAdditionalFiles(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE));

		fTreeViewer.getTree().getDisplay().asyncExec(new Runnable() {

			public void run() {
				if (fTreeViewer.getTree().isDisposed())
					return;
				Vector fileExt = new Vector();
				String[] inclTokens, exclTokens = new String[0];
				if (fProject == null || includes == null)
					return;
				inclTokens = includes.toArray(new String[includes.size()]);
				if (excludes != null)
					exclTokens = excludes.toArray(new String[excludes.size()]);
				Set temp = new TreeSet();
				for (int i = 0; i < inclTokens.length; i++)
					temp.add(inclTokens[i]);
				for (int i = 0; i < exclTokens.length; i++)
					temp.add(exclTokens[i]);
				Iterator iter = temp.iterator();
				while (iter.hasNext()) {
					String resource = iter.next().toString();
					boolean isIncluded = includes.contains(resource);
					if (resource.equals(".") || resource.equals("./")
							|| resource.equals(".\\")) { //$NON-NLS-1$ 
						// ignore - should be root directory
					} else if (resource.lastIndexOf(IPath.SEPARATOR) == resource
							.length() - 1) {
						IFolder folder = fProject.getFolder(resource);
						fTreeViewer.setSubtreeChecked(folder, isIncluded);
						fTreeViewer.setParentsGrayed(folder, true);
						if (isIncluded && folder.exists()) {
							setParentsChecked(folder);
							fTreeViewer.setGrayed(folder, false);
						}
					} else if (resource.startsWith("*.")) { //$NON-NLS-1$
						if (isIncluded)
							fileExt.add(resource.substring(2));
					} else {
						IFile file = fProject.getFile(resource);
						fTreeViewer.setChecked(file, isIncluded);
						fTreeViewer.setParentsGrayed(file, true);
						if (isIncluded && file.exists()) {
							fTreeViewer.setGrayed(file, false);
							setParentsChecked(file);
						}
					}
				}
				if (fileExt.size() == 0)
					return;
				try {
					IResource[] members = fProject.members();
					for (int i = 0; i < members.length; i++) {
						if (!(members[i] instanceof IFolder)
								&& (fileExt.contains(members[i]
										.getFileExtension()))) {
							fTreeViewer.setChecked(members[i], includes
									.contains("*." //$NON-NLS-1$
											+ members[i].getFileExtension()));
						}
					}
				} catch (CoreException e) {
					EclipsePlugin.log(e);
				}
			}
		});
	}

	protected void handleBuildCheckStateChange(boolean wasTopParentChecked) {
		IResource resource = fParentResource;
		String resourceName = fParentResource.getProjectRelativePath()
				.toString();
		// IBuild build = fBuildModel.getBuild();
		// IBuildEntry includes = build
		// .getEntry(IBuildPropertiesConstants.PROPERTY_BIN_INCLUDES);
		// IBuildEntry excludes = build
		// .getEntry(IBuildPropertiesConstants.PROPERTY_BIN_EXCLUDES);
		//
		resourceName = handleResourceFolder(resource, resourceName);

		if (isChecked)
			handleCheck(resourceName, resource, wasTopParentChecked);
		else
			handleUncheck(resourceName, resource);

		deleteEmptyEntries();
		fParentResource = fOriginalResource = null;
	}

	protected void handleCheck(String resourceName, IResource resource,
			boolean wasTopParentChecked) {

		ITigerstripeGeneratorProject project = getIPluggablePluginProject();
		try {
			if (!wasTopParentChecked) {
				project.addAdditionalFile(resourceName,
						ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE);
				markPageModified();
				refreshUponStateChange(
						ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE,
						null, resourceName);
			}

			if (Arrays
					.asList(
							project
									.getAdditionalFiles(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_EXCLUDE))
					.contains(resourceName)) {
				project.removeAdditionalFile(resourceName,
						ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_EXCLUDE);
				refreshUponStateChange(
						ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_EXCLUDE,
						resourceName, null);
				markPageModified();
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	// protected boolean isValidIncludeEntry(IBuildEntry includes,
	// IBuildEntry excludes, IResource resource, String resourceName) {
	// if (excludes == null)
	// return true;
	// IPath resPath = resource.getProjectRelativePath();
	// while (resPath.segmentCount() > 1) {
	// resPath = resPath.removeLastSegments(1);
	// if (includes.contains(resPath.toString() + IPath.SEPARATOR))
	// return false;
	// else if (excludes != null
	// && excludes.contains(resPath.toString() + IPath.SEPARATOR))
	// return true;
	// }
	// return !excludes.contains(resourceName);
	// }

	protected void handleUncheck(String resourceName, IResource resource) {

		try {
			ITigerstripeGeneratorProject project = getIPluggablePluginProject();
			List<String> excludes = Arrays
					.asList(project
							.getAdditionalFiles(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_EXCLUDE));
			List<String> includes = Arrays
					.asList(project
							.getAdditionalFiles(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE));

			if (fTreeViewer.getChecked(resource.getParent())) {
				if (!excludes.contains(resourceName)
						&& (includes != null ? !includes.contains(resourceName)
								: true)) {
					project.addAdditionalFile(resourceName,
							ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_EXCLUDE);
					refreshUponStateChange(
							ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_EXCLUDE,
							null, resourceName);
					markPageModified();
				}
			}
			if (includes != null) {
				if (includes.contains(resourceName)) {
					project.removeAdditionalFile(resourceName,
							ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE);
					refreshUponStateChange(
							ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE,
							resourceName, null);
					markPageModified();
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void refreshUponStateChange(int includeExclude, String oldValue,
			String newValue) {
		if (fParentResource == null && fOriginalResource == null) {
			if (oldValue == null && newValue != null) {
				// adding token
				IFile file = fProject.getFile(new Path(newValue));
				if (!file.exists())
					return;
				fParentResource = fOriginalResource = file;
				isChecked = true;
			} else if (oldValue != null && newValue == null) {
				// removing token
				IFile file = fProject.getFile(new Path(oldValue));
				if (!file.exists())
					return;
				fParentResource = fOriginalResource = file;
				isChecked = false;
			} else
				return;
			return;
		}
		fTreeViewer.setChecked(fParentResource, isChecked);
		fTreeViewer.setGrayed(fOriginalResource, false);
		fTreeViewer.setParentsGrayed(fParentResource, true);
		setParentsChecked(fParentResource);
		fTreeViewer.setGrayed(fParentResource, false);
		if (fParentResource instanceof IFolder) {
			fTreeViewer.setSubtreeChecked(fParentResource, isChecked);
			setChildrenGrayed(fParentResource, false);
		}
		while (!fOriginalResource.equals(fParentResource)) {
			fTreeViewer.setChecked(fOriginalResource, isChecked);
			fOriginalResource = fOriginalResource.getParent();
		}
		fParentResource = null;
		fOriginalResource = null;

	}

	protected String getResourceFolderName(String resourceName) {
		return resourceName + IPath.SEPARATOR;
	}

	/**
	 * @param resource -
	 *            file/folder being modified in tree
	 * @param resourceName -
	 *            name file/folder
	 * @return relative path of folder if resource is folder, otherwise, return
	 *         resourceName
	 */
	protected String handleResourceFolder(IResource resource,
			String resourceName) {
		if (resource instanceof IFolder) {
			deleteFolderChildrenFromEntries((IFolder) resource);
			return getResourceFolderName(resourceName);
		}
		return resourceName;
	}

	protected void deleteFolderChildrenFromEntries(IFolder folder) {
		// IBuild build = fBuildModel.getBuild();
		// IBuildEntry binIncl = build
		// .getEntry(IBuildPropertiesConstants.PROPERTY_BIN_INCLUDES);
		// IBuildEntry binExcl = build
		// .getEntry(IBuildPropertiesConstants.PROPERTY_BIN_EXCLUDES);
		String parentFolder = getResourceFolderName(folder
				.getProjectRelativePath().toString());

		removeChildren(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE,
				parentFolder);
		removeChildren(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE,
				parentFolder);
	}

	public void initialize() {
		if (fTreeViewer.getInput() == null) {
			fTreeViewer.setUseHashlookup(true);
			fTreeViewer.setInput(fProject);
		}
		// fBuildModel.addModelChangedListener(this);
	}

	@Override
	public void dispose() {
		// fBuildModel.removeModelChangedListener(this);
		EclipsePlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	protected void deleteEmptyEntries() {
		// IBuild build = fBuildModel.getBuild();
		// IBuildEntry[] entries = {
		// build.getEntry(IBuildPropertiesConstants.PROPERTY_BIN_EXCLUDES),
		// build.getEntry(IBuildPropertiesConstants.PROPERTY_BIN_INCLUDES),
		// build.getEntry(IBuildPropertiesConstants.PROPERTY_SRC_EXCLUDES),
		// build.getEntry(IBuildPropertiesConstants.PROPERTY_SRC_INCLUDES)};
		// try {
		// for (int i = 0; i < entries.length; i++) {
		// if (entries[i] != null && entries[i].getTokens().length == 0)
		// build.remove(entries[i]);
		// }
		// } catch (CoreException e) {
		// PDEPlugin.logException(e);
		// }
	}

	public CheckboxTreeViewer getTreeViewer() {
		return fTreeViewer;
	}

	protected ISelection getViewerSelection() {
		return getTreeViewer().getSelection();
	}

	@Override
	public void refresh() {
		try {
			initializeCheckState();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		super.refresh();
	}

	public void uncheckAll() {
		fTreeViewer.setCheckedElements(new Object[0]);
	}

	protected void removeChildren(int includeExclude, String parentFolder) {

		try {
			List<String> entries = null;
			ITigerstripeGeneratorProject project = getIPluggablePluginProject();
			List<String> excludes = Arrays
					.asList(project
							.getAdditionalFiles(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_EXCLUDE));
			List<String> includes = Arrays
					.asList(project
							.getAdditionalFiles(ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE));

			switch (includeExclude) {
			case ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_INCLUDE:
				entries = includes;
				break;
			case ITigerstripeM1GeneratorProject.ADDITIONAL_FILE_EXCLUDE:
				entries = excludes;
			}
			if (entries != null) {
				String[] tokens = entries.toArray(new String[entries.size()]);
				for (int i = 0; i < tokens.length; i++) {
					if (tokens[i].indexOf(IPath.SEPARATOR) != -1
							&& tokens[i].startsWith(parentFolder)
							&& !tokens[i].equals(parentFolder)) {

						project.removeAdditionalFile(tokens[i], includeExclude);
					}
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void resourceChanged(IResourceChangeEvent event) {
		if (fTreeViewer.getControl().isDisposed())
			return;
		fDoRefresh = false;
		IResourceDelta delta = event.getDelta();
		try {
			if (delta != null)
				delta.accept(this);
			if (fDoRefresh) {
				asyncRefresh();
				fDoRefresh = false;
			}
		} catch (CoreException e) {
		}
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();

		FileEditorInput input = (FileEditorInput) getPage().getEditorInput();
		IProject project = input.getFile().getProject();

		if ((resource instanceof IFile || resource instanceof IFolder)
				&& resource.getProject().equals(project)) {
			if (delta.getKind() == IResourceDelta.ADDED
					|| delta.getKind() == IResourceDelta.REMOVED) {
				fDoRefresh = true;
				return false;
			}
		} else if (resource instanceof IProject
				&& ((IProject) resource).equals(project))
			return delta.getKind() != IResourceDelta.REMOVED;
		return true;
	}

	private void asyncRefresh() {
		Control control = fTreeViewer.getControl();
		if (!control.isDisposed()) {
			control.getDisplay().asyncExec(new Runnable() {

				public void run() {
					if (!fTreeViewer.getControl().isDisposed()) {
						fTreeViewer.refresh(true);
						try {
							initializeCheckState();
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					}
				}
			});
		}
	}

	// ====
	protected void markPageModified() {
		PluginDescriptorEditor editor = (PluginDescriptorEditor) getPage()
				.getEditor();
		editor.pageModified();
	}

}
