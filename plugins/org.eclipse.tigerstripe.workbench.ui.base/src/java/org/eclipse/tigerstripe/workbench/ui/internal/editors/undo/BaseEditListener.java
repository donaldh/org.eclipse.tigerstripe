/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.undo;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.undo.ModelUndoableEdit;

public abstract class BaseEditListener implements SelectionListener,
		ModifyListener {

	private TigerstripeFormEditor editor;
	private String feature;
	private int type;

	/**
	 * 
	 * @param editor
	 * @param feature
	 * @param type -
	 *            the {@link IModelChangeDelta} type
	 */
	public BaseEditListener(TigerstripeFormEditor editor, String feature,
			int type) {
		this.editor = editor;
		this.feature = feature;
		this.type = type;
	}

	protected TigerstripeFormEditor getEditor() {
		return this.editor;
	}

	protected String getFeature() {
		return this.feature;
	}

	protected int getType() {
		return this.type;
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}

	protected ModelUndoableEdit makeEdit(URI uri, String feature, int type,
			Object oldValue, Object newValue, ITigerstripeModelProject project) {
		ModelUndoableEdit edit = new ModelUndoableEdit(uri, type, feature,
				oldValue, newValue, project);
		return edit;
	}

	public void modifyText(ModifyEvent e) {

	}

	public void widgetSelected(SelectionEvent e) {

	}
}
