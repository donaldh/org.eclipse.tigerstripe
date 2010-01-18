/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeOssjProjectHandle;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchViewerComparator;

public class MoveInputWizardPage extends AbstractModelRefactorWizardPage {

	public static final String PAGE_NAME = "MoveInputPage";

	private TreeViewer destinationField;
	
	private Image projectImg;

	private Image srcImg;

	private Image packageImg;

	public MoveInputWizardPage() {
		super(PAGE_NAME);
	}

	public void createControl(Composite parent) {

		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setFont(parent.getFont());

		Label label = new Label(composite, SWT.NONE);
		label.setText("Choose destination for '" + artifact.getName() + ": ");
		label.setLayoutData(new GridData());

		destinationField = new TreeViewer(composite, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true, 2,
				1);
		gd.widthHint = convertWidthInCharsToPixels(40);
		gd.heightHint = convertHeightInCharsToPixels(15);
		destinationField.getTree().setLayoutData(gd);
		destinationField.setLabelProvider(new LabelProvider() {

			
			@Override
			public Image getImage(Object element) {

				if (element instanceof IProject) {

					ITigerstripeModelProject tsproject;
					IProject iProject = (IProject) element;
					tsproject = getTigerstripeProject(iProject);

					if (tsproject != null) {
						if (projectImg == null) {
							ImageDescriptor desc = Images
									.getDescriptor(Images.TSPROJECT_FOLDER);
							projectImg = desc.createImage();
						}
						return projectImg;
					}
				} else if (element instanceof IResource) {

					try {
						IResource iResource = (IResource) element;
						if (resourceIsSourceDir(iResource)) {
							if (srcImg == null) {
								IWorkbench wb = PlatformUI.getWorkbench();
								ISharedImages images = wb.getSharedImages();
								srcImg = images
										.getImage(ISharedImages.IMG_OBJ_FOLDER);
							}
							return srcImg;
						}

						IPackageArtifact pkgArtifact = (IPackageArtifact) iResource
								.getAdapter(IPackageArtifact.class);
						if (pkgArtifact != null) {
							if (packageImg == null) {
								ImageDescriptor desc = Images
										.getDescriptor(Images.PACKAGE_ICON);
								packageImg = desc.createImage();
							}
							return packageImg;
						}
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
						return null;
					}
				}

				return null;
			}

			@Override
			public String getText(Object element) {

				if (element instanceof IProject) {

					return getTigerstripeProject((IProject) element).getName();

				} else if (element instanceof IFolder) {

					return ((IFolder) element).getName();
				}
				return null;
			}


		});
		destinationField.setContentProvider(new BaseWorkbenchContentProvider());
		destinationField.addFilter(new ViewerFilter() {

			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if (element instanceof IProject) {

					ITigerstripeModelProject tsProject;
					IProject iProject = (IProject) element;
					tsProject = getTigerstripeProject(iProject);

					if (iProject.isAccessible() && tsProject != null) {

						return true;
					} else {

						return false;
					}

				} else if (element instanceof IFolder) {
					try {

						return folderIsInSourceTree((IFolder) element);

					} catch (TigerstripeException e) {

						EclipsePlugin.log(e);
						return false;
					}
				} else {

					return false;
				}
			}

			/*
			 * The folder should be displayed if it is the model's src folder,
			 * or if it is a descendant of the model's src folder.
			 */
			private boolean folderIsInSourceTree(IFolder iFolder)
					throws TigerstripeException {

				ITigerstripeModelProject tsProject;
				IProject iProject = iFolder.getProject();
				tsProject = getTigerstripeProject(iProject);

				if (tsProject != null) {

					IPath srcFolderPath = getSourcePath(iProject);
					IPath projFolderPath = iFolder.getProjectRelativePath();
					if (projFolderPath.equals(srcFolderPath)) {
						return true;
					} else if (srcFolderPath.isPrefixOf(projFolderPath)) {
						return true;
					}
				}

				return false;
			}

		});
		destinationField.setComparator(new WorkbenchViewerComparator());
		destinationField.setInput(ResourcesPlugin.getWorkspace());

		destinationField
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {

						IStructuredSelection selection = (IStructuredSelection) destinationField
								.getSelection();
						if (!(selection.getFirstElement() instanceof IContainer)) {
							return;
						}


						try {
							AbstractModelRefactorWizard wizard = (AbstractModelRefactorWizard) getWizard();
							wizard.clearRequests();

							ITigerstripeModelProject destinationProject = getContainerProject((IContainer) selection
									.getFirstElement());
							String fullyQualifiedName = getContainerFqn((IContainer) selection
									.getFirstElement());
							ModelRefactorRequest request = new ModelRefactorRequest();
							request.setOriginal(artifact.getProject(), artifact
									.getFullyQualifiedName());
							request.setDestination(destinationProject,
									fullyQualifiedName);

							if (validatePage(request)) {
								wizard.addRequest(request);
							}
						} catch (TigerstripeException t){
							setMessage(t.getMessage());
							setPageComplete(false);
						}


					}

					private String getContainerFqn(IContainer container)
							throws TigerstripeException {

						IJavaElement element = (IJavaElement) container
								.getAdapter(IJavaElement.class);
						if (element instanceof IPackageFragment) {
							IPackageFragment pkg = (IPackageFragment) element
									.getAdapter(IPackageFragment.class);
							return pkg.getElementName() + '.'
									+ artifact.getName();
						} else {
							throw new TigerstripeException(
									"The supplied container must be an instance of IPackageFragment");
						}
					}

					private ITigerstripeModelProject getContainerProject(
							IContainer container) {

						return (ITigerstripeModelProject) container
								.getProject().getAdapter(
										ITigerstripeModelProject.class);
					}
				});

		setPageComplete(false);
		setControl(composite);

	}

	private ITigerstripeModelProject getTigerstripeProject(IProject iProject) {

		Object adptObj = iProject.getAdapter(ITigerstripeModelProject.class);
		return (ITigerstripeModelProject) adptObj;
	}

	private boolean resourceIsSourceDir(IResource resource)
			throws TigerstripeException {
		IPath srcPath;
		try {
			srcPath = getSourcePath(resource.getProject());
			if (srcPath.equals(resource.getProjectRelativePath())) {
				return true;
			} else {
				return false;
			}
		} catch (TigerstripeException e) {
			throw e;
		}
	}

	private IPath getSourcePath(IProject project) throws TigerstripeException {

		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) project
				.getAdapter(ITigerstripeModelProject.class);
		String srcRepo = ((TigerstripeOssjProjectHandle) tsProject)
				.getBaseRepository();

		IResource srcResource = project.findMember(srcRepo);
		return srcResource.getProjectRelativePath();
	}
	
	@Override
	public void dispose() {

		super.dispose();
		if (projectImg != null) {
			projectImg.dispose();
			projectImg = null;
		}
		if (srcImg != null) {
			srcImg.dispose();
			srcImg = null;
		}
		if (packageImg != null) {
			packageImg.dispose();
			packageImg = null;
		}
	}
}
