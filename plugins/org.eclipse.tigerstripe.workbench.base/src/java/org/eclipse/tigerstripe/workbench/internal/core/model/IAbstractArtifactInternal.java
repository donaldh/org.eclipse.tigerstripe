package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.io.Writer;
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;

public interface IAbstractArtifactInternal extends IAbstractArtifact,
		IArtifactComponentInternal {

	public void removePackageContainment();

	public void removeReferences();

	public void updateExtendingArtifacts(IAbstractArtifact newArtifact);
	
	public void updateMembers(IAbstractArtifact newArtifact);

	public void addToExtending(IAbstractArtifact artifact);

	public void removeFromExtending(IAbstractArtifact artifact);

	public abstract void setProxy(boolean bool);

	public abstract boolean isProxy();

	public abstract void setTSProject(TigerstripeProject project);

	/**
	 * Returns the markingTag for this
	 * 
	 * The Marking Tag is the identifier that uniquely determines the type of
	 * Artifact.
	 * 
	 * @return String - the marking tag for this
	 */
	public abstract String getMarkingTag();

	/**
	 * Returns the correct type of artifact based on the given class
	 * 
	 * This is used by the ArtifactManager while extracting artifacts.
	 * 
	 * @param javaClass
	 * @return TODO: this is an ugly way of implementing this, need to be
	 *         changed.
	 */
	public abstract IAbstractArtifactInternal extractFromClass(
			JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor);

	/**
	 * Performs a validation of the created model's references.
	 * 
	 * This corresponds to a semantic pass on the model once all artifacts have
	 * been extracted into the tigerstripe engine.
	 * 
	 * @throws TigerstripeException
	 */
	public abstract void resolveReferences(IProgressMonitor monitor)
			throws TigerstripeException;

	public abstract void resolvePackageContainment(IProgressMonitor monitor)
			throws TigerstripeException;

	/**
	 * #386 Upon first extraction a dummy copy an artifact is created is the
	 * corresponding definition has not been read yet.
	 * 
	 * This resolves to the correct artifact.
	 */
	public abstract void resolveExtendedArtifact(IProgressMonitor monitor);

	public abstract IAbstractArtifactInternal resolveIfProxy(
			IProgressMonitor monitor);

	public abstract void resolveImplementedArtifacts(IProgressMonitor monitor);

	public abstract Collection<RefComment> getRefComments();

	public abstract void setRefComment(RefComment rComment);

	public abstract RefComment getRefCommentById(String refCommentId);

	public abstract String getUniqueRefCommentId();

	/**
	 * Returns the artifact extended by this artifact.
	 * 
	 * @return AbstractArtifact - the artifact extended by this artifact.
	 */
	public abstract IAbstractArtifactInternal getExtends();

	/**
	 * Returns true if this AbstractArtifact is a model artifact
	 * 
	 * @return
	 */
	public abstract boolean isModelArtifact();

	public abstract boolean isCapabilitiesArtifact();

	public abstract boolean isSessionArtifact();

	public abstract boolean isDatatypeArtifact();

	public abstract JavaSource getJavaSource();

	public abstract void setJavaSource(JavaSource javaSource);

	public abstract IAbstractArtifactInternal getModel();

	public abstract IMethod getMethodById(String methodId);

	public abstract void dispose();

	public abstract void addContainedModelComponents(
			Collection<IModelComponent> components);

	public abstract void addContainedModelComponent(IModelComponent component);

	public abstract void clearContainedModelComponents();

	public abstract void removeContainedModelComponent(IModelComponent component);

	public abstract void setContainingModelComponent(
			IModelComponent containingComponent);

	public abstract AbstractArtifactPersister getArtifactPersister(Writer writer);

}