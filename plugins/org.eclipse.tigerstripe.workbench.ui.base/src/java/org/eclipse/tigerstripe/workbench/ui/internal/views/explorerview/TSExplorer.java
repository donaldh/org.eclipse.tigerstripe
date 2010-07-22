package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.ui.navigator.CommonNavigator;

public class TSExplorer extends CommonNavigator {

	@Override
	public String getFrameToolTipText(Object anElement) {
		return "Tigerstripe Explorer";
	}

}
