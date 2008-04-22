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
package org.eclipse.tigerstripe.workbench.ui.internal.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public abstract class TigerstripeSectionPart extends SectionPart {

	public static final int CLIENT_VSPACING = 4;
	public static final int MAX_RIGHT_COLUMN_WIDTH = 80;

	private TigerstripeFormPage page;
	private FormToolkit toolkit;
	private Composite body;

	public TigerstripeSectionPart(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, int style) {
		super(parent, toolkit, style);
		setPage(page);
		setTSLooknfeel();
		setToolkit(toolkit);
	}

	private void setTSLooknfeel() {
		getSection().setTitleBarBorderColor(ColorUtils.TS_ORANGE);
		getSection().setData("part", this); // so we get notified on disposal
	}

	protected abstract void createContent();

	public TigerstripeFormPage getPage() {
		return this.page;
	}

	protected void setPage(TigerstripeFormPage page) {
		this.page = page;
	}

	protected FormToolkit getToolkit() {
		return toolkit;
	}

	private void setToolkit(FormToolkit toolkit) {
		this.toolkit = toolkit;
	}

	/**
	 * Sets the title to this TigerstripeSectionPart
	 * 
	 * @param title
	 */
	protected void setTitle(String title) {
		getSection().setText(title);
	}

	/**
	 * Sets the description for this TigerstripeSectionPart
	 * 
	 * @param description
	 */
	protected void setDescription(String description) {
		getSection().setDescription(description);
	}

	protected Composite getBody() {
		if (body == null) {
			body = getToolkit().createComposite(getSection());
			TableWrapLayout layout = new TableWrapLayout();
			layout.numColumns = 2;
			TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
			body.setLayoutData(td);
			body.setLayout(layout);
		}
		return body;
	}

	/**
	 * Creates a blank label that spans a specified number of columns
	 * 
	 * @param parent
	 * @param colspan
	 */
	protected void createBlank(Composite parent, int colspan) {
		Label blank = getToolkit().createLabel(parent, "");
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = colspan;
		blank.setLayoutData(td);
	}
}
