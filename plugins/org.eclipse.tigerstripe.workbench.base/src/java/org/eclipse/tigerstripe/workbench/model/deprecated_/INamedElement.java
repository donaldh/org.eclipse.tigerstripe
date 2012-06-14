package org.eclipse.tigerstripe.workbench.model.deprecated_;

public interface INamedElement {

	/**
	 * Returns the name associated with this component.
	 * 
	 * @return String - the name of the component
	 */
	public String getName();

	/**
	 * Sets the name associated with this component
	 * 
	 * @return
	 */
	public void setName(String name);

	/**
	 * Returns the comment (or plain-english description) associated with this
	 * model component.
	 * 
	 * @return String - the comment
	 */
	public String getComment();

	/**
	 * Set the comment for this component.
	 * 
	 * @param comment
	 */
	public void setComment(String comment);

}
