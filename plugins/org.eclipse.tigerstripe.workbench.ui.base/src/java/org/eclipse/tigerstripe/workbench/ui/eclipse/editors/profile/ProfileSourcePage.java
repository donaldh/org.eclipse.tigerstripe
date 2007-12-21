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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;

public class ProfileSourcePage extends TextEditor implements IFormPage,
		IGotoMarker {

	private String id;
	private ProfileEditor editor;
	private Control control;
	private int index;

	/**
	 * 
	 */
	public ProfileSourcePage(ProfileEditor editor, String id, String title) {
		this.id = id;
		initialize(editor);
		IPreferenceStore[] stores = new IPreferenceStore[2];
		stores[0] = EclipsePlugin.getDefault().getPreferenceStore();
		stores[1] = EditorsUI.getPreferenceStore();
		setPreferenceStore(new ChainedPreferenceStore(stores));
		setRangeIndicator(new DefaultRangeIndicator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#initialize(org.eclipse.ui.forms.editor.FormEditor)
	 */
	public void initialize(FormEditor editor) {
		this.editor = (ProfileEditor) editor;
	}

	public FormEditor getEditor() {
		return editor;
	}

	public IManagedForm getManagedForm() {
		// not a form page
		return null;
	}

	public void setActive(boolean active) {
		// TODO Auto-generated method stub
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canLeaveThePage() {
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		Control[] children = parent.getChildren();
		control = children[children.length - 1];
	}

	public Control getPartControl() {
		return control;
	}

	public String getId() {
		return this.id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isEditor() {
		return true;
	}

	public boolean selectReveal(Object object) {
		if (object instanceof IMarker) {
			IDE.gotoMarker(this, (IMarker) object);
			return true;
		}
		return false;
	}

	protected void setEditable(boolean isEditable) {
		this.getSourceViewer().setEditable(isEditable);
	}

}
