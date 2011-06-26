package org.eclipse.tigerstripe.workbench.utils;

import org.eclipse.core.runtime.IAdaptable;

public class AdaptHelper {

	public static <T> T adapt(IAdaptable adaptable, Class<T> adapter) {
		return adapter.cast(adaptable.getAdapter(adapter));
	}

}
