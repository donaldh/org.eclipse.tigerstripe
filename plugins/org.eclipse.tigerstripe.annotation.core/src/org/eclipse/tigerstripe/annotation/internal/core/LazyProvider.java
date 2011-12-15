package org.eclipse.tigerstripe.annotation.internal.core;

public class LazyProvider<T> {

	private volatile T value;
	private final Loader<T> loader;

	public LazyProvider(Loader<T> loader) {
		this.loader = loader;
	}
	
	public T get() {
		if (value == null) {
			synchronized (this) {
				if (value == null) {
					value = loader.load();
				}
			}
		}
		return value;
	}
	
	public static interface Loader<T> {
		T load();
	}
}
