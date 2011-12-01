package org.eclipse.tigerstripe.workbench.ui.internal.search;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.search.ui.ISearchPageScoreComputer;

public class TigerstripeSearchPageScoreComputerAdapterFactory implements
		IAdapterFactory {

	private Object scoreComputer;

	public Object getAdapter(Object adaptableObject,
			@SuppressWarnings("rawtypes") Class adapterType) {
		if (ISearchPageScoreComputer.class.equals(adapterType)) {
			if (scoreComputer == null) {
				scoreComputer = new TigerstripeSearchPageScoreComputer();
			}
			return scoreComputer;
		}
		return null;
	}

	public Class<?>[] getAdapterList() {
		return new Class[] { ISearchPageScoreComputer.class };
	}
}
