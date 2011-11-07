package org.eclipse.tigerstripe.annotation.internal.core;

import org.eclipse.emf.transaction.util.Lock;

public class SimpleLock extends Lock {

	@Override
	public void uiSafeAcquire(boolean exclusive) throws InterruptedException {
		acquire(exclusive);
	}
	
}
