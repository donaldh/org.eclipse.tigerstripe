package org.eclipse.tigerstripe.annotation.core;

import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.tigerstripe.annotation.internal.core.WriteCommand;

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
	public static void read(final Operation operation) {
		try {
			AnnotationPlugin.getDomain().runExclusive(new Runnable() {
				
				public void run() {
					try {
						operation.run();
					} catch (Throwable e) {
						AnnotationPlugin.log(e);
					}
				}
			});
		} catch (InterruptedException e) {
			AnnotationPlugin.log(e);
		}
	}
	
	/**
	 * Execute operation in a read only transaction
	 * There is the ability to return the result 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T read(final OperationWithResult<T> operation) {
		try {
			return (T) AnnotationPlugin.getDomain().runExclusive(new RunnableWithResult.Impl<T>() {
				
				public void run() {
					try {
						operation.run();
						setResult(operation.getResult());
					} catch (Throwable e) {
						AnnotationPlugin.log(e);
					}
				}
			});
		} catch (InterruptedException e) {
			AnnotationPlugin.log(e);
			return null;
		}
	}
	
	/**
	 * Execute operation in a write transaction
	 */
	public static void write(final Operation operation) {
			
			AnnotationPlugin.getDomain().getCommandStack().execute(new WriteCommand() {
				
				public void execute() {
					try {
						operation.run();
					} catch (Throwable e) {
						AnnotationPlugin.log(e);
					}
				}
			});
				
	}

	/**
	 * Execute operation in a write transaction
	 * There is the ability to return the result 
	 */
	public static <T> T write(final OperationWithResult<T> operation) {
		
		AnnotationPlugin.getDomain().getCommandStack().execute(new WriteCommand() {
			
			public void execute() {
				try {
					operation.run();
				} catch (Throwable e) {
					AnnotationPlugin.log(e);
				}
			}
		});
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
