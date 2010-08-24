package org.eclipse.tigerstripe.workbench.model;

/**
 * Used to mark artifacts as dirty.  It's up to the client dealing with the artifact to determine
 * what it wants to do with a dirty artifact.
 * 
 * @author Navid Mehregani
 * 
 */
public interface IMarkDirty {
	
	/**
	 * Used to set dirty bit
	 * @param dirty: true if artifact is dirty; false otherwise
	 */
	public void setDirty(boolean dirty);
	
	/**
	 * @return True if artifact is dirty; false otherwise
	 */
	public boolean isDirty();

}
