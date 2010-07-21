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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.jdt.internal.ui.navigator.IExtensionStateConstants.Values;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeExplorerLabelProvider;
import org.eclipse.ui.internal.navigator.extensions.CommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.IExtensionStateModel;

/**
 * Tigerstripe label provider
 * 
 * @see TigerstripeExplorerLabelProvider
 */
@SuppressWarnings("restriction")
public class TigerstripeLabelProvider extends TigerstripeExplorerLabelProvider {

	public TigerstripeLabelProvider() {
		super(new TigerstripeContentProvider());
	}

	private IExtensionStateModel fStateModel;

	private IPropertyChangeListener fLayoutPropertyListener;

	public void init(ICommonContentExtensionSite commonContentExtensionSite) {
		fStateModel = ((CommonContentExtensionSite) commonContentExtensionSite)
				.getContentService().findStateModel(
						"org.eclipse.jdt.java.ui.javaContent");

		setIsFlatLayout(fStateModel.getBooleanProperty(Values.IS_LAYOUT_FLAT));
		fLayoutPropertyListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (Values.IS_LAYOUT_FLAT.equals(event.getProperty())) {
					if (event.getNewValue() != null) {
						boolean newValue = ((Boolean) event.getNewValue())
								.booleanValue() ? true : false;
						setIsFlatLayout(newValue);
					}
				}

			}
		};
		fStateModel.addPropertyChangeListener(fLayoutPropertyListener);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (fStateModel != null) {
			fStateModel.removePropertyChangeListener(fLayoutPropertyListener);
		}
	}
}
