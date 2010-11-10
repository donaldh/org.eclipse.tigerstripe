package org.eclipse.tigerstripe.workbench.ui.dependencies.api;

/**
 * Used to receive notification from an external system. For example when adding
 * a new element in the dependency graph(appears new Tigerstripe module)
 */
public interface IExternalContextListener {

	/**
	 * Called when a new item is added to the dependency graph
	 */
	void dependencyAdded(IDependencySubject parent,
			IDependencySubject newSubject);

	/**
	 * Called when a item is removed from the dependency graph
	 */
	void dependencyRemoved(IDependencySubject parent,
			IDependencySubject removedSubject);

}
