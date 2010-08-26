/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.components;

import static org.eclipse.tigerstripe.workbench.ui.internal.utils.StatusUtils.findMaxSeverity;
import static org.eclipse.tigerstripe.workbench.ui.internal.utils.StatusUtils.flat;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.CrossProjectArtifactBrowseDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ModelComponentSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public abstract class NewModelComponentPage extends WizardPage {

	private IAbstractArtifact artifact;
	private Text currentArtifactText;

	public NewModelComponentPage(String pageName, IAbstractArtifact artifact) {
		super(pageName);
		if (canHaveThisModelComponent(artifact)) {
			this.artifact = artifact;
		}
	}

	protected abstract void onArtifactChange();

	protected abstract void beforeSave();

	protected abstract IModelComponent getModelComponent();

	protected void revalidate() {
		setPageComplete(validate());
	}

	protected abstract void fill(FormToolkit toolkit, Composite parent);

	public void createControl(Composite parent) {

		Composite control = new Composite(parent, SWT.NONE);

		Composite artifactPanel = new Composite(control, SWT.NONE);

		artifactPanel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		artifactPanel.setLayout(new GridLayout(3, false));

		new Label(artifactPanel, SWT.NONE).setText("Artifact: ");

		currentArtifactText = new Text(artifactPanel, SWT.BORDER);
		if (artifact != null) {
			currentArtifactText.setText(artifact.getName());
		}
		currentArtifactText.setEnabled(false);

		currentArtifactText
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button currentArtifactBrowseButton = new Button(artifactPanel, SWT.PUSH);
		GridData layoutData = new GridData(GridData.FILL_VERTICAL);
		currentArtifactBrowseButton.setLayoutData(layoutData);
		currentArtifactBrowseButton.setText(" Browse ");
		currentArtifactBrowseButton
				.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {

						ViewerFilter possibleArtifactsFilter = new ViewerFilter() {

							@Override
							public boolean select(Viewer viewer,
									Object parentElement, Object element) {

								if (element instanceof ICompilationUnit) {
									IAbstractArtifact artifact = TSExplorerUtils
											.getArtifactFor(element);
									return canHaveThisModelComponent(artifact);
								} else {
									return true;
								}
							}
						};

						CrossProjectArtifactBrowseDialog dialog = new CrossProjectArtifactBrowseDialog(
								getShell(), getJavaElement(),
								possibleArtifactsFilter);

						if (dialog.open() == Window.OK) {
							setArtifact(TSExplorerUtils.getArtifactFor(dialog
									.getSelected()));
						}
					}
				});

		new Label(control, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		final Display display = parent.getDisplay();
		FormColors formColors = new FormColors(display) {
			@Override
			protected void updateBorderColor() {
				border = asColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
			}
		};
		formColors.setBackground(parent.getBackground());
		FormToolkit toolkit = new FormToolkit(formColors) {
			@Override
			public Text createText(Composite parent, String value, int style) {
				return setColor(super.createText(parent, value, style),
						SWT.COLOR_LIST_BACKGROUND, SWT.COLOR_LIST_FOREGROUND);
			}

			@Override
			public Tree createTree(Composite parent, int style) {
				return setColor(super.createTree(parent, style),
						SWT.COLOR_LIST_BACKGROUND, SWT.COLOR_LIST_FOREGROUND);
			}

			@Override
			public Table createTable(Composite parent, int style) {
				return setColor(super.createTable(parent, style),
						SWT.COLOR_LIST_BACKGROUND, SWT.COLOR_LIST_FOREGROUND);
			}

			@Override
			public void adapt(Control control, boolean trackFocus,
					boolean trackKeyboard) {
				super.adapt(control, trackFocus, trackKeyboard);
				if (control instanceof CCombo) {
					setColor(control, SWT.COLOR_LIST_BACKGROUND,
							SWT.COLOR_LIST_FOREGROUND);
				}
			}

		};
		fill(toolkit, control);
		revalidate();
		setControl(control);
	}

	private boolean validate() {

		if (artifact == null) {
			setMessage("Select artifact", ERROR);
			return false;
		}

		IModelComponent modelComponent = getModelComponent();
		if (modelComponent == null) {
			return false;
		}
		IStatus status = modelComponent.validate();

		if (status.isOK()) {
			setMessage(null);
			return true;
		} else {
			List<IStatus> statuses = flat(status);

			if (statuses.isEmpty()) {
				setMessage(null);
				return true;
			} else {
				if (statuses.size() == 1) {
					IStatus firstStatus = statuses.get(0);
					setMessage(firstStatus.getMessage(),
							toMessageType(firstStatus.getSeverity()));
				} else {
					setMessage(joinMessages(statuses, '\n'),
							toMessageType(findMaxSeverity(statuses)));
				}
				return false;
			}
		}
	}

	private String joinMessages(List<IStatus> statuses, char c) {
		int size = statuses.size();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < size; ++i) {
			result.append(i + 1).append(") ").append(
					statuses.get(i).getMessage());
			if (i < size - 1) {
				result.append(c);
			}
		}
		return result.toString();
	}

	private int toMessageType(int severity) {
		switch (severity) {
		case IStatus.INFO:
			return INFORMATION;
		case IStatus.WARNING:
			return WARNING;
		case IStatus.ERROR:
			return ERROR;
		default:
			return INFORMATION;
		}
	}

	private Color asColor(int value) {
		return getShell().getDisplay().getSystemColor(value);
	}

	private <T extends Control> T setColor(T control, int bg, int fg) {
		control.setBackground(asColor(bg));
		control.setForeground(asColor(fg));
		return control;
	}

	public void setArtifact(IAbstractArtifact artifact) {
		this.artifact = artifact;
		if (currentArtifactText != null) {
			currentArtifactText.setText(artifact != null ? artifact.getName()
					: "");
		}
		onArtifactChange();
		revalidate();
	}

	public IAbstractArtifact getArtifact() {
		return artifact;
	}

	protected IJavaElement getJavaElement() {
		if (artifact == null) {
			return null;
		} else {
			return (IJavaElement) artifact.getAdapter(IJavaElement.class);
		}
	}

	private IArtifactFormContentProvider getContentProvider(
			IAbstractArtifact artifact) {
		if (artifact == null) {
			return null;
		}
		IArtifactFormContentProvider cp = (IArtifactFormContentProvider) Platform
				.getAdapterManager().getAdapter(artifact,
						IArtifactFormContentProvider.class);
		return cp;
	}

	private boolean canHaveThisModelComponent(IAbstractArtifact artifact) {
		IArtifactFormContentProvider cp = getContentProvider(artifact);

		return cp != null
				&& ((this instanceof NewAttributePage && cp.hasAttributes())
						|| (this instanceof NewConstantPage && cp
								.hasConstants()) || (this instanceof NewMethodPage && cp
						.hasMethods()));
	}

	public abstract Class<? extends ModelComponentSectionPart> getSectionPartClass();
}
