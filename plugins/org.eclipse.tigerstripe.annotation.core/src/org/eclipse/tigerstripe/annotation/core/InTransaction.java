package org.eclipse.tigerstripe.annotation.core;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.annotation.core.storage.internal.Storage;
import org.eclipse.tigerstripe.annotation.internal.core.AnnotationManager;

/**
 * Convenient way to execute something in a transaction of annotations plugin
 * extension point.
 * 
 * There is no need to obtain the domain from the annotation plugin.  
 * 
 */
public class InTransaction {

	/**
	 * Execute operation in a read only transaction 
	 */
	public static void run(final Operation operation) {
		try {
			IWorkspaceRoot wroot = ResourcesPlugin.getWorkspace().getRoot();
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				
				public void run(IProgressMonitor monitor) throws CoreException {
					Storage storage = ((AnnotationManager)AnnotationPlugin.getManager()).getStorage();
					storage.checkpoint();
					try {
						operation.run();
					} catch (Throwable e) {
						AnnotationPlugin.log(e);
					} finally {
						storage.checkpoint();
					}
				}
			}, wroot, 0, new NullProgressMonitor());
		} catch (Exception e) {
			AnnotationPlugin.log(e);
		}
	}

	public static <T> T run(final OperationWithResult<T> operation) {
		Operation op = operation; 
		run(op);
		return operation.value;
	}

	
	public static interface Operation {
		
		void run() throws Throwable;
		
	}
	
	public static abstract class OperationWithResult<T> implements Operation {
		
		private T value;

		public void setResult(T value) {
			this.value = value;
		}
		
		public T getResult() {
			return value;
		}
	}

	
}
