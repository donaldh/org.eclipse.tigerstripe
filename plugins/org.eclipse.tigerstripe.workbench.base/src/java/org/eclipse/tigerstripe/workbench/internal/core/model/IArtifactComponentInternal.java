package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;

public interface IArtifactComponentInternal extends IModelComponent,
		IStereotypeCapable {

	public abstract ArtifactManager getArtifactManager();

	public abstract void setArtifactManager(ArtifactManager artifactManager);

	/**
	 * Return the parent artifact for this component if it exists.
	 * 
	 * @return
	 */
	public abstract IAbstractArtifact getParentArtifact();

	public abstract void setParentArtifact(IAbstractArtifact parentArtifact);

	public abstract void addTag(Tag tag);

	public abstract Collection getTags();

	/**
	 * Returns a collection of Strings. Each string corresponding to a non
	 * Tigerstripe Tag
	 * 
	 * @return
	 */
	public abstract Collection getNonTigerstripeTags();

	/**
	 * Returns all tag with the given name
	 * 
	 * @param name
	 * @return
	 */
	public abstract Collection<Tag> getTagsByName(String name);

	/**
	 * Returns the first tag with the given name
	 * 
	 * @param name
	 * @return
	 */
	public abstract Tag getFirstTagByName(String name);

	public abstract String getCustomProperty(String name, String defaultValue);

	public abstract URI toURI() throws TigerstripeException;

}