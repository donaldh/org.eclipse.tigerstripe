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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModule;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModuleManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies.DependenciesImageProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies.DependenciesSorter;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies.DependencyKind;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies.IDependencyKindResolver;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies.ITextProvider;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

/**
 * Presents the user with a filterable list of Tigerstripe projects for
 * selection
 * 
 * @author Eric Dillon
 * 
 */
public class TigerstripeProjectSelectionDialog extends SelectionStatusDialog {

	private static final IDependencyKindResolver KIND_RESOLVER = new IDependencyKindResolver() {

		public DependencyKind resolve(Object obj) {
			if (obj instanceof IJavaProject) {
				return DependencyKind.PROJECT;
			} else if (obj instanceof InstalledModule) {
				return DependencyKind.MODULE;
			} else if (obj instanceof IResource) {
				return DependencyKind.DEPENDENCY;
			} else {
				return DependencyKind.UNKNOWN;
			}
		}
	};

	class TigerstripeProjectReferencesProvider extends
			StandardJavaElementContentProvider {
		@Override
		public Object[] getElements(Object parent) {
			Map<String, Object> projsAndModulesMap = new HashMap<String, Object>();
			Object[] javaResult = super.getElements(parent);
			InstalledModule[] modules = InstalledModuleManager.getInstance()
					.getModules();

			Collection<Object> projsAndModules = new HashSet<Object>();
			projsAndModules.addAll(asList(javaResult));
			projsAndModules.addAll(asList(modules));

			for (Object o : projsAndModules) {
				String id;
				if (o instanceof IJavaProject) {
					IJavaProject javaProject = (IJavaProject) o;
					ITigerstripeModelProject tsp = (ITigerstripeModelProject) javaProject
							.getAdapter(ITigerstripeModelProject.class);
					if (tsp != null) {
						try {
							id = tsp.getModelId();
						} catch (TigerstripeException e) {
							BasePlugin.log(e);
							continue;
						}
					} else {
						continue;
					}
				} else if (o instanceof InstalledModule) {
					InstalledModule module = (InstalledModule) o;
					id = module.getModuleID();
				} else {
					continue;
				}

				if (!projsAndModulesMap.containsKey(id)) {
					projsAndModulesMap.put(id, o);
				}
			}
			Collection<IResource> dependencies = findDependencies();
			Set<Object> all = new HashSet<Object>(projsAndModulesMap.size()
					+ dependencies.size());
			all.addAll(projsAndModulesMap.values());
			all.addAll(dependencies);
			return all.toArray(new Object[0]);
		}
	}

	private final static Set<String> DEPENDENCIES_EXTENSIONS = new HashSet<String>(
			asList("jar", "zip"));

	private Collection<IResource> findDependencies() {

		IProject project = (IProject) currentProject.getAdapter(IProject.class);

		final Collection<IResource> result = new HashSet<IResource>();

		try {
			project.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if (resource instanceof IFile) {
						if (DEPENDENCIES_EXTENSIONS.contains(resource
								.getFileExtension())) {
							result.add(resource);
						}
						return false;
					} else {
						return true;
					}
				}
			});
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
		return result;
	}

	class TigerstripeProjectReferencesLabelProvider extends
			JavaElementLabelProvider {

		private final DependenciesImageProvider imageProvider = new DependenciesImageProvider(
				KIND_RESOLVER);

		public TigerstripeProjectReferencesLabelProvider() {
			super(SHOW_PARAMETERS | SHOW_SMALL_ICONS);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof InstalledModule) {
				InstalledModule module = (InstalledModule) element;
				StringBuffer buffer = new StringBuffer();
				buffer.append(module.getModuleID());
				String version = module.getModule().getProjectDetails()
						.getVersion();
				if (version != null && version.length() > 0) {
					buffer.append(" (");
					buffer.append(version);
					buffer.append(")");
				}
				return buffer.toString();
			} else if (element instanceof IResource) {
				return ((IResource) element).getName();
			} else {
				return super.getText(element);
			}
		}

		@Override
		public Image getImage(Object element) {
			return imageProvider.getImage(element);
		}
	}

	// the visual selection widget group
	private TableViewer fTableViewer;

	private Collection<ModelReference> fFilteredOutProjects;

	// sizing constants
	private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;

	private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;

	private ViewerFilter fFilter;

	private final ITigerstripeModelProject currentProject;

	public TigerstripeProjectSelectionDialog(Shell parentShell,
			Collection<ModelReference> filteredOutProjects,
			final ITigerstripeModelProject currentProject) {
		super(parentShell);
		this.currentProject = currentProject;
		setTitle("Select Tigerstripe Project");
		setMessage("Select Tigerstripe Project to reference");
		fFilteredOutProjects = filteredOutProjects;

		int shellStyle = getShellStyle();
		setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);

		fFilter = new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if (element instanceof IJavaProject) {
					IJavaProject prj = (IJavaProject) element;

					ITigerstripeModelProject tsProject = (ITigerstripeModelProject) prj
							.getProject().getAdapter(
									ITigerstripeModelProject.class);

					if (tsProject != null && tsProject.exists()) {
						for (ModelReference ref : fFilteredOutProjects) {
							if (ref.isReferenceTo(tsProject))
								return false;
						}
						return true;
					} else {
						return false;
					}
				} else if (element instanceof InstalledModule) {
					InstalledModule module = (InstalledModule) element;
					for (ModelReference ref : fFilteredOutProjects) {
						if (ref.getToModelId().equals(module.getModuleID()))
							return false;
					}
					return true;
				} else if (element instanceof IResource) {

					try {
						IDependency[] dependencies = currentProject
								.getDependencies();

						for (IDependency dep : dependencies) {
							if (dep.getPath().equals(
									((IResource) element)
											.getProjectRelativePath()
											.toOSString())) {
								return false;
							}
						}
						return true;

					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
						return false;
					}
				} else {
					return false;
				}
			}
		};

	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// page group
		Composite composite = (Composite) super.createDialogArea(parent);

		Font font = parent.getFont();
		composite.setFont(font);

		createMessageArea(composite);

		fTableViewer = new TableViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER | SWT.MULTI);
		fTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						doSelectionChanged(((IStructuredSelection) event
								.getSelection()).toArray());
					}
				});
		fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				okPressed();
			}
		});
		fTableViewer.addFilter(fFilter);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
		data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
		fTableViewer.getTable().setLayoutData(data);

		final TigerstripeProjectReferencesLabelProvider labelProvider = new TigerstripeProjectReferencesLabelProvider();

		fTableViewer.setLabelProvider(labelProvider);
		fTableViewer
				.setContentProvider(new TigerstripeProjectReferencesProvider());

		ITextProvider textAdapter = new ITextProvider() {

			public String getText(Object obj) {
				return labelProvider.getText(obj);
			}
		};

		fTableViewer.setSorter(new DependenciesSorter(textAdapter,
				KIND_RESOLVER));
		fTableViewer.getControl().setFont(font);

		// Button checkbox= new Button(composite, SWT.CHECK);
		// checkbox.setText(PreferencesMessages.ProjectSelectionDialog_filter);
		// checkbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true,
		// false));
		// checkbox.addSelectionListener(new SelectionListener() {
		// public void widgetSelected(SelectionEvent e) {
		// updateFilter(((Button) e.widget).getSelection());
		// }
		// public void widgetDefaultSelected(SelectionEvent e) {
		// updateFilter(((Button) e.widget).getSelection());
		// }
		// });
		// IDialogSettings dialogSettings=
		// JavaPlugin.getDefault().getDialogSettings();
		// boolean doFilter=
		// !dialogSettings.getBoolean(DIALOG_SETTINGS_SHOW_ALL) &&
		// !fProjectsWithSpecifics.isEmpty();
		// checkbox.setSelection(doFilter);
		// updateFilter(doFilter);

		IJavaModel input = JavaCore.create(ResourcesPlugin.getWorkspace()
				.getRoot());
		fTableViewer.setInput(input);

		doSelectionChanged(new Object[0]);
		Dialog.applyDialogFont(composite);
		return composite;
	}

	// protected void updateFilter(boolean selected) {
	// if (selected) {
	// fTableViewer.addFilter(fFilter);
	// } else {
	// fTableViewer.removeFilter(fFilter);
	// }
	// JavaPlugin.getDefault().getDialogSettings().put(DIALOG_SETTINGS_SHOW_ALL,
	// !selected);
	// }
	//
	private void doSelectionChanged(Object[] objects) {
		if (objects.length == 0) {
			updateStatus(new StatusInfo(IStatus.ERROR, "")); //$NON-NLS-1$
			setSelectionResult(null);
		} else {
			updateStatus(new StatusInfo());
			setSelectionResult(objects);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#computeResult()
	 */
	@Override
	protected void computeResult() {
	}
}
