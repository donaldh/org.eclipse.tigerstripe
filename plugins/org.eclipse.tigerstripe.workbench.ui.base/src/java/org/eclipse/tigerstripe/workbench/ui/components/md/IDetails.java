package org.eclipse.tigerstripe.workbench.ui.components.md;

/**
 * The object that encapsulates the contents of forms for certain kinds of
 * objects
 */
public interface IDetails {

	/**
	 * Called before the first show. There must be created content. It is used
	 * for lazy initialization of content.
	 */
	void init();

	/**
	 * Called when the need to make the content visible
	 */
	void show();

	/**
	 * Called when the need to make the content invisible
	 */
	void hide();

	/**
	 * Called when switching to another object. Suggests that the need to fill
	 * in the form a new object.
	 * 
	 * @param target
	 *            new switched object
	 */
	void switchTarget(Object target);

	static final IDetails NULL = new IDetails() {

		public void show() {
		}

		public void init() {
		}

		public void hide() {
		}

		public void switchTarget(Object target) {
		}
	};
}
