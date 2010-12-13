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
package org.eclipse.tigerstripe.workbench.ui.components.md;

import org.eclipse.draw2d.GridLayout;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * Simple implementation if {@link IDetails}. Require
 * {@link Details#getExternalContainer()} with {@link GridLayout} layout,
 * because use the exclude option of GridLayout.
 */
public abstract class Details implements IDetails {

	protected Composite container;

	/**
	 * @return composite on which you plan to post all the details. Must have
	 *         {@link GridLayout}
	 */
	protected abstract Composite getExternalContainer();

	/**
	 * Provide operation of creation content for this details on parent
	 * composite. Parent will have {@link GridLayout} and you should not change
	 * it.
	 */
	protected abstract void createContents(Composite parent);

	public void init() {
		container = new Composite(getExternalContainer(), SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(container);
		GridLayoutFactory.fillDefaults().applyTo(container);
		createContents(container);
	}

	public void show() {
		changeVisualState(true);
	}

	public void hide() {
		changeVisualState(false);
	}

	private void changeVisualState(boolean visible) {
		container.setVisible(visible);
		GridDataFactory.createFrom((GridData) container.getLayoutData())
				.exclude(!visible).applyTo(container);
		getExternalContainer().layout(true);
	}
}
