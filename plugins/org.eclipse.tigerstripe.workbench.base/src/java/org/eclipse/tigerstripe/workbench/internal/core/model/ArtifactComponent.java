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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.repository.internal.IModelComponentMetadata;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.FacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.tags.StereotypeTags;
import org.eclipse.tigerstripe.workbench.internal.core.util.encode.XmlEscape;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author Eric Dillon
 * 
 * Any component of an artifact (method, field, tag)
 */
public abstract class ArtifactComponent implements IModelComponent,
		IStereotypeCapable {

	public static final String TS_SCHEME = "tigerstripe";

	/** the stereotypes attached to this component */
	private ArrayList<IStereotypeInstance> stereotypeInstances = new ArrayList<IStereotypeInstance>();

	/** the custom properties defined for this ArtifactComponent */
	// TODO: this looks obsolete - 082906
	private Properties customProperties;

	private String name;

	private Collection tags;

	private String comment;

	private EVisibility visibility;

	protected static XmlEscape xmlEncode = new XmlEscape();

	// The parent artifact for this Artifact Component
	private IAbstractArtifact parentArtifact;

	// The manager this artifact belongs to
	private ArtifactManager artifactMgr;

	public ArtifactManager getArtifactManager() {
		return this.artifactMgr;
	}

	public void setArtifactManager(ArtifactManager artifactManager) {
		this.artifactMgr = artifactManager;
	}

	public ArtifactComponent(ArtifactManager artifactMgr) {
		this.tags = new ArrayList();
		this.customProperties = new Properties();
		setArtifactManager(artifactMgr);
	}

	/**
	 * Return the parent artifact for this component if it exists.
	 * 
	 * @return
	 */
	public IAbstractArtifact getParentArtifact() {
		return this.parentArtifact;
	}

	public void setParentArtifact(IAbstractArtifact parentArtifact) {
		this.parentArtifact = parentArtifact;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addTag(Tag tag) {
		tags.add(tag);
	}

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
		}
	}

	public void removeStereotypeInstance(IStereotypeInstance instance) {
		if (stereotypeInstances.contains(instance)) {
			this.stereotypeInstances.remove(instance);
		}
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
		IStereotypeInstance[] stInstances = StereotypeTags
				.getAllStereotypes(getTags());
		for (IStereotypeInstance stInstance : stInstances) {
			addStereotypeInstance(stInstance);
		}
	}

	/**
	 * Returns a collection of Strings. Each string corresponding to a non
	 * Tigerstripe Tag
	 * 
	 * @return
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

	/**
	 * Returns all tag with the given name
	 * 
	 * @param name
	 * @return
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

	/**
	 * Returns the first tag with the given name
	 * 
	 * @param name
	 * @return
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
	public boolean isInActiveFacet() throws TigerstripeException {
		if (getArtifactManager() != null
				&& getArtifactManager().getActiveFacet() != null) {
			IFacetReference ref = getArtifactManager().getActiveFacet();
			if (ref.getFacetPredicate() instanceof FacetPredicate) {
				FacetPredicate predicate = (FacetPredicate) ref
						.getFacetPredicate();
				return !predicate.isExcludedByAnnotation(this);
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
	public Object getAdapter(Class adapter) {
		if (adapter == URI.class) {
			try {
				return toURI();
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		}
		return null;
	}

	public IModelComponentMetadata getMetadata() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(MigrationHelper
				.artifactMetadataMigrateClassname(this.getClass().getName()));
	}

	public List<Object> getAnnotations() {
		IAnnotationManager mgr = AnnotationPlugin.getManager();
		List<Object> annotations = new LinkedList<Object>();
		Annotation[] all = mgr.getAnnotations(this, false);
		for (Annotation a : all) {
			annotations.add(a.getContent());
		}
		return Collections.unmodifiableList(annotations);
	}

	public List<Object> getAnnotations(String schemeID) {
		IAnnotationManager mgr = AnnotationPlugin.getManager();
		List<Object> annotations = new LinkedList<Object>();
		Annotation[] all = mgr.getAnnotations(this, false);
		for (Annotation a : all) {
			String e = a.getUri().scheme();
			if (a.getUri().scheme().equals(schemeID)) {
				annotations.add(a.getContent());
			}
		}
		return Collections.unmodifiableList(annotations);
	}

	public Object getAnnotation(String annotationSpecificationID) {
		List<Object> all = getAnnotations(TS_SCHEME);
		for (Object obj : all) {
			if (isAnnotationMatch(annotationSpecificationID, obj))
				return obj;
		}

		return null;
	}
	
	public Object getAnnotation(String schemeID,
			String annotationSpecificationID) {
		List<Object> all = getAnnotations(schemeID);
		for (Object obj : all) {
			if (isAnnotationMatch(annotationSpecificationID, obj))
				return obj;
		}

		return null;
	}

	/**
	 * @param annotationSpecificationID
	 * @param obj
	 * @return
	 */
	private boolean isAnnotationMatch(String annotationSpecificationID,
			Object obj) {
		Class<?>[] interfaces = obj.getClass().getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i].getName().endsWith(annotationSpecificationID))
				return true;
		}
		return false;
	}

	public List<Object> getAnnotations(String schemeID,
			String annotationSpecificationID) {
		List<Object> annotations = getAnnotations(schemeID);
		for (Iterator<Object> i = annotations.iterator(); i.hasNext();) {
			if (!isAnnotationMatch(annotationSpecificationID, i.next()))
				i.remove();
		}

		return Collections.unmodifiableList(annotations);
	}

	public boolean hasAnnotations() {
		return !getAnnotations().isEmpty();
	}
	
	public boolean hasAnnotations(String schemeID) {
		return !getAnnotations(schemeID).isEmpty();
	}

	public boolean hasAnnotations(String schemeID,
			String annotationSpecificationID) {
		List<Object> annotations = getAnnotations(schemeID);
		for (Iterator<Object> i = annotations.iterator(); i.hasNext();) {
			if (isAnnotationMatch(annotationSpecificationID, i.next()))
				return true;
		}
		return false;
	}

	public Annotation addAnnotation(String scheme, String packij, String clazz)
	    throws TigerstripeException
	{
		IAnnotationManager manager = AnnotationPlugin.getManager();
		AnnotationType type = manager.getType(packij, clazz);
		if(type == null)
			throw new InvalidAnnotationTargetException("No such AnnotationType");
		// Questionable stuff: not sure this should be here
		String[] targets = type.getTargets();
		boolean ok = targets.length == 0;
		if(!ok)
		{
			for(int t = 0; t < targets.length; t++)
			{
				try {
					if(Class.forName(targets[t], false, this.getClass().getClassLoader()).isInstance(this))
						ok = true;
				} catch (ClassNotFoundException e) {/* Nothing */}
			}
		}
		if(!ok)
			throw new InvalidAnnotationTargetException("Target not allowed for AnnotationType");
		// END questionable stuff
		EObject content = type.createInstance();
		try {
			return manager.addAnnotation(this, content);
		} catch (AnnotationException e) {
			throw new TigerstripeException("Failed to add annotation of type: "+content.getClass().getName(), e);
		}
	}
	
	public Annotation addAnnotation(String packij, String clazz) throws TigerstripeException
	{
		return addAnnotation(TS_SCHEME, packij, clazz);
	}
	
	public Annotation addAnnotation(Class<? extends EObject> clazz) throws TigerstripeException
	{
		return addAnnotation(TS_SCHEME, clazz.getPackage().getName(), clazz.getName().substring(clazz.getName().lastIndexOf('.')+1));
	}
	
	public void saveAnnotation(Annotation annotation)
	{
		AnnotationPlugin.getManager().save(annotation);
	}
	
	public ITigerstripeModelProject getProject() throws TigerstripeException {
		if (getParentArtifact() != null)
			return getParentArtifact().getProject();
		return null;
	}

	public URI toURI() throws TigerstripeException {
		return TigerstripeURIAdapterFactory.toURI(this);
	}
}
