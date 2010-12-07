package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.header;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class ProjectDefaultsSection extends TigerstripeDescriptorSectionPart {

	private Text idText;

	private Label idLabel;

	private Text idText2;

	private Label idLabel2;

	private boolean silentUpdate;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class DefaultPageListener implements ModifyListener, KeyListener,
			SelectionListener {

		public void keyPressed(KeyEvent e) {
			// empty
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
			if (e.character == '\r') {
				commit(false);
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {

		}

	}

	public ProjectDefaultsSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, Section.TITLE_BAR | Section.TWISTIE
				| Section.EXPANDED);
		setTitle("Project Defaults");
		createContent();
	}

	protected void createContent() {
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		try {
			createID(getBody(), getToolkit());
		} catch (TigerstripeException ee) {
			Status status = new Status(IStatus.WARNING,
					EclipsePlugin.PLUGIN_ID, 111, "Unexpected Exception", ee);
			EclipsePlugin.log(status);
		}

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createID(Composite parent, FormToolkit toolkit)
			throws TigerstripeException {
		TableWrapData td = null;

		ITigerstripeModelProject handle = getTSProject();

		DefaultPageListener listener = new DefaultPageListener();

		idLabel = toolkit.createLabel(parent, "Default Artifact Package: ",
				SWT.WRAP);
		
		String defaultArtifactPackage = "";
		if (handle!=null) 
			defaultArtifactPackage = handle.getProjectDetails().getProperty(IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP, "");
		
		idText = toolkit.createText(parent, defaultArtifactPackage);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		idText.setLayoutData(td);
		idText.addModifyListener(listener);
		idText.setEnabled(!this.isReadonly());
		idLabel.setEnabled(!this.isReadonly());

		idLabel2 = toolkit.createLabel(parent,
				"Copyright Notice for all files: ", SWT.NONE);
		
		String copyRightNotice = "";
		
		if (handle!=null) 
			handle.getProjectDetails().getProperty("copyrightNotice", "");		
		
		idText2 = toolkit.createText(parent, copyRightNotice, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 70;
		idText2.setLayoutData(td);
		idText2.addModifyListener(listener);
		idText2.setEnabled(!this.isReadonly());
		idLabel2.setEnabled(!this.isReadonly());

	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	private void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	/**
	 * If silent Update is set, the form should not consider the updates to
	 * fields.
	 * 
	 * @return
	 */
	private boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			ITigerstripeModelProject handle = getTSProject();

			try {
				if (e.getSource() == idText) {
					IProjectDetails projectDetails = handle.getProjectDetails();
					projectDetails.getProperties().setProperty(
							IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP,
							idText.getText().trim());
					handle.setProjectDetails(projectDetails);
				} else if (e.getSource() == idText2) {
					IProjectDetails projectDetails = handle.getProjectDetails();
					projectDetails.getProperties().setProperty(
							IProjectDetails.COPYRIGHT_NOTICE,
							idText2.getText().trim());
					handle.setProjectDetails(projectDetails);
				}
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						EclipsePlugin.PLUGIN_ID,
						222,
						"Error refreshing form OssjDefaultForm on Tigerstripe descriptor",
						ee);
				EclipsePlugin.log(status);
			}
			markPageModified();
		}
	}

	protected void markPageModified() {
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		editor.pageModified();
	}

	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		ITigerstripeModelProject handle = getTSProject();

		try {
			setSilentUpdate(true);
			if (handle != null) {
				idText.setText(handle.getProjectDetails().getProperty(
						IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP, ""));
				idText2.setText(handle.getProjectDetails().getProperty(
						IProjectDetails.COPYRIGHT_NOTICE, ""));
			}

			setSilentUpdate(false);

		} catch (TigerstripeException e) {
			Status status = new Status(
					IStatus.ERROR,
					EclipsePlugin.PLUGIN_ID,
					222,
					"Error refreshing form OssjDefaultForm on Tigerstripe descriptor",
					e);
			EclipsePlugin.log(status);
		}
	}

}
