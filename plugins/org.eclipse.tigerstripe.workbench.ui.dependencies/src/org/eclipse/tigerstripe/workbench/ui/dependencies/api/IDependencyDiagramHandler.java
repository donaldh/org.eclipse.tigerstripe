package org.eclipse.tigerstripe.workbench.ui.dependencies.api;

import java.util.Collection;

/**
 * Used to receive notification from an external system. For example when adding
 * a new element in the dependency graph(appears new Tigerstripe module)
 */
public interface IDependencyDiagramHandler {

	/**
	 * Layout subjects
	 */
	void layout(Collection<IDependencySubject> subjects);

	/**
	 * Called when a new item is added to the dependency graph
	 */
	void addDependency(IDependencySubject from, IDependencySubject to);

	/**
	 * Called when a item is removed from the dependency graph
	 */
	void removeDependency(IDependencySubject from, IDependencySubject to);

	/**
	 * Update the diagram
	 */
	void update();

}
