package org.eclipse.tigerstripe.workbench.convert;

import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization.DiagramSynchronizationManager;
import org.eclipse.tigerstripe.workbench.utils.RunnableWithResult;

public class WithoutSynchRunner {

	public static <T, E extends Throwable> T run(RunnableWithResult<T, E> runnable) throws E {
		DiagramSynchronizationManager sm = DiagramSynchronizationManager
				.getInstance();
		
		boolean enabled = sm.isEnabled();
		boolean ignoreNofigy = BaseETAdapter.ignoreNofigy();
		
		try {
			BaseETAdapter.setIgnoreNotify(true);
			sm.setEnabled(false);
			
			return runnable.run();
		} finally {
			sm.setEnabled(enabled);
			BaseETAdapter.setIgnoreNotify(ignoreNofigy);
		}
	}
}
