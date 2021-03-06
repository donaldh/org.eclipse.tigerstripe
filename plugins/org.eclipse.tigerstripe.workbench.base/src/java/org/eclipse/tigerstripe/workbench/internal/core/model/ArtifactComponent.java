/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.repository.internal.IModelComponentMetadata;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.FacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.tags.StereotypeTags;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.UnresolvedStereotypeInstance;
import org.eclipse.tigerstripe.workbench.internal.core.util.encode.XmlEscape;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeListener;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author Eric Dillon
 * 
 *         Any component of an artifact (method, field, tag)
 */
public abstract class ArtifactComponent implements IArtifactComponentInternal {

	/** the stereotypes attached to this component */
	private final ArrayList<IStereotypeInstance> stereotypeInstances = new ArrayList<IStereotypeInstance>();

	/** the stereotypes listeners attached to this component */
	private final ArrayList<IStereotypeListener> stereotypeListeners = new ArrayList<IStereotypeListener>();

	/** the custom properties defined for this ArtifactComponent */
	// TODO: this looks obsolete - 082906
	private final Properties customProperties;

	private String name;

	private final Collection tags;

	private String comment;

	private EVisibility visibility;

	protected static XmlEscape xmlEncode = new XmlEscape();

	// The parent artifact for this Artifact Component
	private IAbstractArtifact parentArtifact;

	// The manager this artifact belongs to
	private ArtifactManager artifactMgr;

	private final AnnotationCapable annotationCapableDelegate;

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#getArtifactManager()
	 */
	public ArtifactManager getArtifactManager() {
		return this.artifactMgr;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#setArtifactManager(org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager)
	 */
	public void setArtifactManager(ArtifactManager artifactManager) {
		this.artifactMgr = artifactManager;
	}

	public ArtifactComponent(ArtifactManager artifactMgr) {
		this.tags = new ArrayList();
		this.customProperties = new Properties();
		setArtifactManager(artifactMgr);
		this.annotationCapableDelegate = new AnnotationCapable(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#getParentArtifact()
	 */
	@ProvideModelComponents
	public IAbstractArtifact getParentArtifact() {
		return this.parentArtifact;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#setParentArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact)
	 */
	public void setParentArtifact(IAbstractArtifact parentArtifact) {
		this.parentArtifact = parentArtifact;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#addTag(org.eclipse.tigerstripe.workbench.internal.core.model.Tag)
	 */
	public void addTag(Tag tag) {
		tags.add(tag);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#getTags()
	 */
	public Collection getTags() {
		return this.tags;
	}

	public Collection<IStereotypeInstance> getStereotypeInstances() {
		return Collections.unmodifiableCollection(stereotypeInstances);
	}

	public IStereotypeInstance getStereotypeInstanceByName(String name) {
		for (IStereotypeInstance inst : stereotypeInstances) {
			if (inst.getName().equals(name)) {
				return inst;
			}
		}
		return null;
	}

	public boolean hasStereotypeInstance(String name) {
		IStereotypeInstance inst = getStereotypeInstanceByName(name);
		if (inst == null)
			return false;
		else
			return true;
	}

	public void addStereotypeInstance(IStereotypeInstance instance) {
		if (!stereotypeInstances.contains(instance)) {
			this.stereotypeInstances.add(instance);
			for (IStereotypeListener listener : getListeners()) {
				listener.stereotypeAdded(instance);
			}
		}
	}

	public void removeStereotypeInstance(IStereotypeInstance instance) {
		if (stereotypeInstances.contains(instance)) {
			this.stereotypeInstances.remove(instance);
			for (IStereotypeListener listener : getListeners()) {
				listener.stereotypeRemove(instance);
			}
		}
	}

	public void addStereotypeListener(IStereotypeListener listener) {
		if (!stereotypeListeners.contains(listener))
			stereotypeListeners.add(listener);
	}

	public void removeStereotypeListener(IStereotypeListener listener) {
		stereotypeListeners.remove(listener);
	}

	private IStereotypeListener[] getListeners() {
		return stereotypeListeners
				.toArray(new IStereotypeListener[stereotypeListeners.size()]);
	}

	public void removeStereotypeInstances(
			Collection<IStereotypeInstance> instances) {
		for (IStereotypeInstance instance : instances) {
			removeStereotypeInstance(instance);
		}
	}

	protected void extractStereotypes() {
		// NOTE: this needs to be called after the list of tags has been built
		// for this component
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		Collection<IStereotype> stereos = profile
				.getAvailableStereotypeForCapable(this);
		IStereotypeInstance[] stInstances = StereotypeTags
				.getAllStereotypes(getTags());
		for (IStereotypeInstance stInstance : stInstances) {
			if (stereos.contains(stInstance.getCharacterizingStereotype())) {
				addStereotypeInstance(stInstance);
			} else {
				if (stInstance instanceof UnresolvedStereotypeInstance) {
					addStereotypeInstance(stInstance);
				} else {
					// make an unresolvedInstance from this
					addStereotypeInstance(new UnresolvedStereotypeInstance(
							stInstance));
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#getNonTigerstripeTags()
	 */
	public Collection getNonTigerstripeTags() {
		Collection result = new ArrayList();
		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			if (!tag.getName().startsWith(AbstractArtifactTag.PREFIX)) {
				StringBuffer buf = new StringBuffer();
				buf.append("@" + tag.getName());

				// We can't have paramaters and properties at the same time.
				if (tag.getProperties().size() > 0) {
					for (Iterator propIter = tag.getProperties().keySet()
							.iterator(); propIter.hasNext();) {
						String key = (String) propIter.next();
						String propStr = key + " = " + "\""
								+ tag.getProperties().getProperty(key) + "\"";
						buf.append(" " + propStr);
					}
				} else {
					for (Iterator paramIter = tag.getParameters().iterator(); paramIter
							.hasNext();) {
						String paramStr = (String) paramIter.next();
						buf.append(" " + paramStr);
					}
				}

				result.add(buf.toString());
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#getTagsByName(java.lang.String)
	 */
	public Collection<Tag> getTagsByName(String name) {
		ArrayList<Tag> result = new ArrayList<Tag>();

		if (name != null && name.length() != 0) {
			for (Iterator iter = this.tags.iterator(); iter.hasNext();) {
				Tag tag = (Tag) iter.next();
				if (name.equals(tag.getName())) {
					result.add(tag);
				}
			}
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#getFirstTagByName(java.lang.String)
	 */
	public Tag getFirstTagByName(String name) {
		Tag result = null;
		if (name != null && name.length() != 0) {
			for (Iterator iter = this.tags.iterator(); iter.hasNext();) {
				Tag tag = (Tag) iter.next();
				if (name.equals(tag.getName())) {
					result = tag;
				}
			}
		}
		return result;
	}

	/**
	 * Sets the comment for this Artifact Component as seen in the source code.
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Returns the comment for this Artifact Component
	 * 
	 * @return
	 */
	public String getComment() {
		return (this.comment == null ? "" : this.comment);
	}

	protected void addCustomProperty(String name, String value) {
		this.customProperties.put(name, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#getCustomProperty(java.lang.String, java.lang.String)
	 */
	public String getCustomProperty(String name, String defaultValue) {
		String result = this.customProperties.getProperty(name, defaultValue);
		return result;
	}

	protected void extractCustomProperties() {
		// Collection customProps = getTagsByName( "tigerstripe.property");
		// for( Iterator iter=customProps.iterator(); iter.hasNext();) {
		// Tag tag = (Tag) iter.next();
		// Properties props = tag.getProperties();
		// String name = props.getProperty( "name" );
		// String value = props.getProperty( "value" );
		// //TODO needs error handling here incase malformed custom property
		// addCustomProperty( name, value );
		// }
	}

	public void setVisibility(EVisibility visibility) {
		this.visibility = visibility;
	}

	public EVisibility getVisibility() {
		if (this.visibility == null) {
			setVisibility(EVisibility.PUBLIC);
		}
		return this.visibility;
	}

	/*
	 * This method is needed because the QDOX stuff returns a String[] of ALL
	 * modifiers
	 */
	protected void setVisibility(String[] modifiers) {
		setVisibility(EVisibility.PACKAGE);
		for (String modifier : modifiers) {
			if ("public".equals(modifier)) {
				setVisibility(EVisibility.PUBLIC);
			} else if ("protected".equals(modifier)) {
				setVisibility(EVisibility.PROTECTED);
			} else if ("private".equals(modifier)) {
				setVisibility(EVisibility.PRIVATE);
			}
		}
	}

	// By default we only look for Exclusion by annotation.
	@Contextual
	public boolean isInActiveFacet() throws TigerstripeException {
		if (getArtifactManager() != null
				&& getArtifactManager().getActiveFacet() != null) {
			IFacetReference ref = getArtifactManager().getActiveFacet();
			if (ref.getFacetPredicate() instanceof FacetPredicate) {
				FacetPredicate predicate = (FacetPredicate) ref
						.getFacetPredicate();
				return !predicate.isExcludedByStereotype(this) && !predicate.isExcludedByAnnotation(this);
			} else
				return true;
		} else
			return true;
	}

	/**
	 * Returns a duplicate of the initial list where all components that are not
	 * in the current active facet are filtered out.
	 * 
	 * @param components
	 * @return
	 */
	public static Collection<IModelComponent> filterFacetExcludedComponents(
			Collection<IModelComponent> components) {
		ArrayList<IModelComponent> result = new ArrayList<IModelComponent>();
		for (Iterator<IModelComponent> iter = components.iterator(); iter
				.hasNext();) {
			IModelComponent component = iter.next();
			try {
				if (!component.isInActiveFacet())
					continue;
				else
					result.add(component);
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"Error while evaluating isInActiveFacet for "
								+ component.getName() + ": " + e.getMessage(),
						e);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == URI.class) {
			try {
				return toURI();
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		} else if (adapter.isInstance(this)) {
			return this;
		}
		return null;
	}

	public IModelComponentMetadata getMetadata() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(MigrationHelper
				.artifactMetadataMigrateClassname(this.getClass().getName()));
	}

	public List<Object> getAnnotations() {
		return annotationCapableDelegate.getAnnotations();
	}

	public List<Object> getAnnotations(Class<?> type) {
		return annotationCapableDelegate.getAnnotations(type);
	}

	public Object getAnnotation(String annotationSpecificationID) {
		return annotationCapableDelegate
				.getAnnotation(annotationSpecificationID);
	}

	public Object getAnnotationByID(String annotationID) {
		return annotationCapableDelegate.getAnnotationByID(annotationID);
	}

	public List<Object> getAnnotations(String annotationSpecificationID) {
		return annotationCapableDelegate
				.getAnnotations(annotationSpecificationID);
	}

	public boolean hasAnnotations() {
		return annotationCapableDelegate.hasAnnotations();
	}

	public boolean hasAnnotations(String annotationSpecificationID) {
		return annotationCapableDelegate
				.hasAnnotations(annotationSpecificationID);
	}

	public boolean hasAnnotationWithID(String annotationID) {
		return annotationCapableDelegate.hasAnnotationWithID(annotationID);
	}

	public boolean hasAnnotations(Class<?> annotationType) {
		return !getAnnotations(annotationType).isEmpty();
	}

	public ITigerstripeModelProject getProject() throws TigerstripeException {
		if (getParentArtifact() != null)
			return getParentArtifact().getProject();
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IArtifactComponentInternal2#toURI()
	 */
	public URI toURI() throws TigerstripeException {
		return TigerstripeURIAdapterFactory.toURI(this);
	}

	public String getStereotypeString() {
		return getStereotypesAsString(this);
	}

	protected String getStereotypesAsString(IStereotypeCapable stereotypeCapable) {
		if (getStereotypeInstances().size() == 0) {
			return "";
		} else {
			StringBuilder result = new StringBuilder();

			IWorkbenchProfile profile = TigerstripeCore
					.getWorkbenchProfileSession().getActiveProfile();
			for (IStereotypeInstance instance : getStereotypeInstances()) {
				// Check that the stereotype is enabled in the profile
				IStereotype stereotype = profile.getStereotypeByName(instance
						.getName());
				if (stereotype != null) {
					if (result.length() == 0) {
						result.append("<<");
					} else {
						result.append(",");
					}
					result.append(instance.getName());
				}
			}
			if (result.length() > 0) {
				result.append(">>");
			}
			return result.toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof IModelComponent) {
			if (Proxy.isProxyClass(obj.getClass())) {
				return obj.equals(this);
			}
		}
		return super.equals(obj);
	}
}
