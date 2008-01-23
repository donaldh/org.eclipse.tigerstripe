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
package org.eclipse.tigerstripe.core.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.model.IField;
import org.eclipse.tigerstripe.api.model.ILabel;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.model.IMethod.IArgument;
import org.eclipse.tigerstripe.api.model.IMethod.IException;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IStandardSpecifics;
import org.eclipse.tigerstripe.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.api.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.api.project.IAdvancedProperties;
import org.eclipse.tigerstripe.api.project.IProjectDescriptor;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.api.utils.TigerstripeErrorLevel;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.Method.Argument;
import org.eclipse.tigerstripe.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.core.util.ComparableArtifact;
import org.eclipse.tigerstripe.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.core.util.Util;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;

/**
 * @author Eric Dillon
 * 
 * This is an abstract definition of a Tigerstripe Artifact.
 * 
 * A Tigerstripe artifact is a source artifact used by the tigerstripe engine to
 * produce output. It is modeled based on an annotated java source file.
 * 
 * The type of AbstractArtifact is determined by the "MarkingTag" of the
 * artifact. For example, a ManagedEntityArtifact will be recognized by the
 * presence of a "@tigerstripe.managedEntity" class-level tag.
 * 
 * The #buildModel() method is called by the constructor to use the given
 * JavaClass object (from the Qdox parser) and build the corresponding model by
 * going through the found tags.
 * 
 * Once all the artifacts have been extracted by the ArtifactManager based on
 * the provided source files, the #valide() method is called on all extracted
 * artifact to allow for a semantic pass on the model and allow tigerstripe to
 * detect semantic errors (unresolved references, e.g.).
 * 
 */
public abstract class AbstractArtifact extends ArtifactComponent implements
		IAbstractArtifact {

	// the updater that this artifact depends on
	private IModelUpdater myUpdater;

	// The package this AbstractArtifact lives in
	private String packageString = "";

	/** logger for output */
	private static Logger log = Logger.getLogger(AbstractArtifact.class);

	/** The collection of methods for this artifact */
	private Collection<IMethod> methods;

	/** The collection of fields for this artifact */
	private Collection<IField> fields;

	/** The collection of (unique, non-primitve) datatypes of the fields */
	private Collection fieldTypes;

	/** The collection of labels (enum) for this artifact */
	private Collection labels;

	/** The collection of inherited fields for this artifact */
	private Collection inheritedFields = null;

	/** The collection of inherited labels for this artifact */
	private Collection inheritedLabels = null;

	/** The collection of inherited methods for this artifact */
	private Collection inheritedMethods = null;

	/** The list of artifacts that are extending "this". */
	private Collection<IAbstractArtifact> extendingArtifacts = new ArrayList<IAbstractArtifact>();

	/** The map of RefComments for this artifact */
	private HashMap<String, RefComment> refComments = new HashMap<String, RefComment>();

	/** The artifact referenced by the extends clause */
	private AbstractArtifact extendsArtifact;

	/** The artifacts referenced by the "implements" clause */
	private List<IAbstractArtifact> implementedArtifacts = new ArrayList<IAbstractArtifact>();

	/** The javaclass that this artifact was extracted from */
	protected JavaClass javaClass;

	/**
	 * The javaSource this artifact was extracted from
	 */
	private JavaSource javaSource;

	/** whether the originating javaClass is abstract or not */
	private boolean isAbstract = false;

	// Facet scoping for Fields/Methods/Labels. Build lazily filtered list when
	// ever needed
	private IField[] facetFilteredFields = null;

	private IField[] facetFilteredInheritedFields = null;

	private IMethod[] facetFilteredMethods = null;

	private IMethod[] facetFilteredInheritedMethods = null;

	private ILabel[] facetFilteredLabels = null;

	private ILabel[] facetFilteredInheritedLabels = null;

	protected JavaClass getJavaClass() {
		return this.javaClass;
	}

	public void setTSProject(TigerstripeProject project) {
	}

	public TigerstripeProject getTSProject() {
		return getArtifactManager().getTSProject();
	}

	public boolean isReadonly() {
		return getArtifactManager() != null
				&& getArtifactManager() instanceof ModuleArtifactManager;
	}

	// ======================================================================
	// since 2.2: Implemented Artifacts
	public IAbstractArtifact[] getImplementedArtifacts() {
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();

		for (IAbstractArtifact art : implementedArtifacts) {
			try {
				// Bug 919: facet needs to be considered here
				if (getArtifactManager() != null
						&& getArtifactManager().getActiveFacet() != null) {
					if (art.isInActiveFacet())
						result.add(art);
				} else {
					result.add(art);
				}
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"Couldn't resolve Implemented Artifacts for "
								+ getFullyQualifiedName() + ": "
								+ e.getMessage(), e);
			}
		}

		return result.toArray(new IAbstractArtifact[result.size()]);
	}

	public void setImplementedArtifacts(IAbstractArtifact[] artifacts) {
		implementedArtifacts.clear();
		implementedArtifacts.addAll(Arrays.asList(artifacts));
	}

	/**
	 * coming from IArtifact (External API)
	 */
	public IAbstractArtifact[] getAncestors() {
		ArrayList<IAbstractArtifact> ancestors = new ArrayList<IAbstractArtifact>();
		IAbstractArtifact[] ancArray = new IAbstractArtifact[0];
		if (getExtendedIArtifact() != null) {
			ancestors.add(getExtendedIArtifact());
			ancestors.addAll(Arrays.asList(getExtendedIArtifact()
					.getAncestors()));
			return ancestors.toArray(ancArray);

		} else
			return new IAbstractArtifact[0];
	}

	/**
	 * Coming from IArtifact -removed for consistency - use getComment()
	 */
	// public String getDescription() {
	// return this.getComment();
	// }
	/**
	 * Creates an empty Abstract Artifact
	 * 
	 * @param packageName -
	 * @param name -
	 */
	public AbstractArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		methods = new ArrayList();
		fields = new ArrayList();
		labels = new ArrayList();

		if (artifactMgr != null) { // it's null for MODELs
			if (artifactMgr instanceof ModuleArtifactManager) {
				setTSProject(((ModuleArtifactManager) artifactMgr)
						.getEmbeddedProject());
			} else
				setTSProject(artifactMgr.getTSProject());
		}
	}

	/**
	 * Set the package name for this AbstractArtifact
	 * 
	 * @param packageName
	 */
	public void setPackage(String packageName) {
		this.packageString = packageName;
	}

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
	 * Returns the name of the Java package this artifact belongs to
	 * 
	 * @return
	 */
	public String getPackage() {
		if (this.packageString == null) {
			packageString = "";
		}

		return this.packageString;
	}

	/**
	 * Returns the qualified name of this artifact
	 * 
	 * @return
	 */
	public String getFullyQualifiedName() {

		String fullyQualifiedName = getPackage();

		if (!"".equals(getPackage())) {
			fullyQualifiedName = fullyQualifiedName + ".";
		}
		return fullyQualifiedName + getName();
	}

	public void setFullyQualifiedName(String fqn) {
		setPackage(Util.packageOf(fqn));
		this.setName(Util.nameOf(fqn));
	}

	/**
	 * Returns the correct type of artifact based on the given class
	 * 
	 * This is used by the ArtifactManager while extracting artifacts.
	 * 
	 * @param javaClass
	 * @return TODO: this is an ugly way of implementing this, need to be
	 *         changed.
	 */
	public abstract AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor);

	public boolean isAbstract() {
		return this.isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	/**
	 * Builds the model for this artifact (i.e. builds all the tags, methods,
	 * and fields).
	 * 
	 * This method may be specialized by a children artifact.
	 * 
	 * @param clazz -
	 *            JavaClass the parsed source file to build a model for
	 */
	protected void buildModel(JavaClass clazz,
			ITigerstripeProgressMonitor monitor) {

		setName(clazz.getName());
		setPackage(clazz.getPackage());
		setComment(xmlEncode.decode(clazz.getComment()));

		// Start with class-level tags
		DocletTag[] tags = clazz.getTags();
		for (int i = 0; i < tags.length; i++) {
			Tag tag = new Tag(tags[i]);
			addTag(tag);
		}

		// Looks for the isAbstract value on the naming tag
		extractIsAbstract();

		// Extract the class level customProperties
		extractCustomProperties(); // TODO: this seems obsolete - 082906

		// Extract all the stereotypes
		extractStereotypes();

		// Extract implemented Artifacts
		extractImplementedArtifacts(monitor);

		// Extract referenced comments
		extractReferencedComments(clazz);

		boolean ignoreTags = false;
		try {
			if (getIProject() != null) {
				// if IProject is null it means we're in a module.
				ignoreTags = "false"
						.equalsIgnoreCase(getIProject()
								.getAdvancedProperty(
										IAdvancedProperties.PROP_MISC_IgnoreArtifactElementsWithoutTag));
			}
		} catch (TigerstripeException e) {
			log.error(e);
		}

		// Then the methods
		this.methods = new ArrayList();
		JavaMethod[] methods = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = new Method(methods[i], getArtifactManager());

			if (ignoreTags) {
				this.methods.add(method);
				method.extractCustomProperties();
			} else if (method.getFirstTagByName(AbstractArtifactTag.PREFIX
					+ AbstractArtifactTag.METHOD) != null) {
				this.methods.add(method);
				method.setContainingArtifact(this);
				method.extractCustomProperties();
			}

		}

		// And the fields
		this.fields = new ArrayList();
		JavaField[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = new Field(fields[i], getArtifactManager());
			if (ignoreTags) {
				this.fields.add(field);
				field.setContainingArtifact(this);
				field.extractCustomProperties();
			} else if (field.getFirstTagByName(AbstractArtifactTag.PREFIX
					+ AbstractArtifactTag.FIELD) != null) {
				this.fields.add(field);
				field.setContainingArtifact(this);
				field.extractCustomProperties();
			}
		}

		// And the labels
		this.labels = new ArrayList();
		JavaField[] labels = clazz.getFields();
		for (int i = 0; i < labels.length; i++) {
			Label label = new Label(labels[i], getArtifactManager());
			if (ignoreTags) {
				this.labels.add(label);
				label.setContainingArtifact(this);
				label.extractCustomProperties();
			} else if (label.getFirstTagByName(AbstractArtifactTag.PREFIX
					+ AbstractArtifactTag.LABEL) != null) {
				this.labels.add(label);
				label.setContainingArtifact(this);
				label.extractCustomProperties();
			}
		}

		// The extends clause
		if (!"java.lang.Object".equals(getJavaClass().getSuperJavaClass()
				.getFullyQualifiedName())
				&& !"".equals(getJavaClass().getSuperJavaClass()
						.getFullyQualifiedName())) {
			String parentClass = getJavaClass().getSuperJavaClass()
					.getFullyQualifiedName();
			setExtendedIArtifact(getArtifactManager()
					.getArtifactByFullyQualifiedName(parentClass, true, monitor));

			if (getExtendedIArtifact() == null) {
				// #386 Build a temporary dummy artifact, it will be further
				// resolved once everything has been parsed in the
				// resolveReferences()
				// method below
				AbstractArtifact art = (AbstractArtifact) makeArtifact();
				art.setFullyQualifiedName(parentClass);
				setExtendedIArtifact(art);
			}
		}
	}

	/**
	 * This is used by the velocity templates.
	 */
	public String getImplementedArtifactsAsStr() {
		if (implementedArtifacts.size() == 0)
			return "";
		StringBuffer result = new StringBuffer();
		boolean first = true;
		for (IAbstractArtifact art : implementedArtifacts) {
			if (!first) {
				result.append(",");
			}
			result.append(art.getFullyQualifiedName());
			first = false;
		}
		return result.toString();
	}

	private void extractImplementedArtifacts(ITigerstripeProgressMonitor monitor) {
		Tag markingTag = getFirstTagByName(getMarkingTag());
		if (markingTag != null) {
			String val = markingTag.getProperties().getProperty("implements",
					"");
			if (!"".equals(val)) {
				String[] fqns = val.split(",");
				for (String fqn : fqns) {
					IAbstractArtifact art = getArtifactManager()
							.getArtifactByFullyQualifiedName(fqn, true, monitor);
					if (art == null) {
						art = new SessionFacadeArtifact(getArtifactManager());
						art.setFullyQualifiedName(fqn);
					}
					implementedArtifacts.add(art);
				}
			}
		}
	}

	private void extractIsAbstract() {
		Tag markingTag = getFirstTagByName(getMarkingTag());
		if (markingTag != null) {
			String val = markingTag.getProperties().getProperty("isAbstract",
					"false");
			isAbstract = Boolean.parseBoolean(val);
		}
	}

	/**
	 * Performs a validation of the created model's references.
	 * 
	 * This corresponds to a semantic pass on the model once all artifacts have
	 * been extracted into the tigerstripe engine.
	 * 
	 * @throws TigerstripeException
	 */
	public void resolveReferences(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {

		// #386
		resolveExtendedArtifact(monitor);
		resolveImplementedArtifacts(monitor);

		// Resolve of athe inheritedFields so we don't have to walk
		// the inheritance hierarchy over and over
		// resolveInheritedFields(); Lazy-build the list 945
		// resolveInheritedLabels(); Lazy-build the list 945
		// resolveInheritedMethods(); Lazy-build the list 945
		// createUniqueFieldTypeList(); Lazy-build the list #945
	}

	/**
	 * #386 Upon first extraction a dummy copy an artifact is created is the
	 * corresponding definition has not been read yet.
	 * 
	 * This resolves to the correct artifact.
	 */
	public void resolveExtendedArtifact(ITigerstripeProgressMonitor monitor) {
		if (getExtendedIArtifact() != null) {
			String fqn = getExtendedIArtifact().getFullyQualifiedName();
			IAbstractArtifact realArtifact = getArtifactManager()
					.getArtifactByFullyQualifiedName(fqn, true, monitor);
			if (realArtifact != null) {
				setExtendedIArtifact(realArtifact);
			}
		}
	}

	public void resolveImplementedArtifacts(ITigerstripeProgressMonitor monitor) {
		List<IAbstractArtifact> newList = new ArrayList<IAbstractArtifact>();
		for (IAbstractArtifact art : implementedArtifacts) {
			String fqn = art.getFullyQualifiedName();
			IAbstractArtifact realArtifact = getArtifactManager()
					.getArtifactByFullyQualifiedName(fqn, true, monitor);
			if (realArtifact != null) {
				newList.add(realArtifact);
			} else
				newList.add(art);
		}
		implementedArtifacts.clear();
		implementedArtifacts.addAll(newList);
	}

	/**
	 * Sometimes it is necessary to save large textual information (description
	 * i.e.) that won't fit in a property because of ", ', or CR etc...
	 * 
	 * Since we're limited by the POJO format, these need to be saved as
	 * comments on a java attribute, so they can be referenced
	 * 
	 */
	private void extractReferencedComments(JavaClass clazz) {
		JavaField[] labels = clazz.getFields();
		for (int i = 0; i < labels.length; i++) {
			Label label = new Label(labels[i], getArtifactManager());
			if (label.getFirstTagByName(AbstractArtifactTag.PREFIX
					+ AbstractArtifactTag.REFCOMMENT) != null) {
				RefComment rComment = new RefComment(this);
				rComment.setContent(label.getComment());
				rComment.setLabel(label.getName());
				refComments.put(label.getName(), rComment);
			}
		}
	}

	public Collection<RefComment> getRefComments() {
		return refComments.values();
	}

	public void setRefComment(RefComment rComment) {
		String id = rComment.getLabel();
		if (id != null) {
			refComments.put(id, rComment);
		}
	}

	public RefComment getRefCommentById(String refCommentId) {
		return refComments.get(refCommentId);
	}

	public String getUniqueRefCommentId() {
		int index = 0;
		while (refComments.containsKey("_r_e_f_" + index)) {
			index++;
		}
		return "_r_e_f_" + index;
	}

	/**
	 * Capture the type and the associated refBy mode
	 */
	public class FieldTypeRef implements IFieldTypeRef {
		private Type type;

		private int refBy;

		public int getRefBy() {
			return refBy;
		}

		public void setRefBy(int refBy) {
			this.refBy = refBy;
		}

		public boolean isRefByValue() {
			return refBy == IField.REFBY_VALUE;
		}

		public boolean isRefByKey() {
			return refBy == IField.REFBY_KEY;
		}

		public boolean isRefByKeyResult() {
			return refBy == IField.REFBY_KEYRESULT;
		}

		public Type getType() {
			return type;
		}

		public IType getIType() {
			return getType();
		}

		public void setType(Type type) {
			this.type = type;
		}

		@Override
		public boolean equals(Object arg0) {
			if (arg0 instanceof FieldTypeRef) {
				FieldTypeRef other = (FieldTypeRef) arg0;
				return type.equals(other.getType())
						&& refBy == other.getRefBy();
			} else
				return false;
		}
	}

	/**
	 * Iterate and create a unique list of fieldTypes and corresponding refBy
	 * modes to be further used in the plugin to generate makeXXX() methods.
	 * 
	 */
	private void createUniqueFieldTypeList() {
		// Iterate over the extracted fields to get their types

		this.fieldTypes = new ArrayList();
		Iterator it = this.fields.iterator();
		while (it.hasNext()) {
			Field field = (Field) it.next();
			int refBy = field.getRefBy();
			Type type = field.getType();

			FieldTypeRef item = new FieldTypeRef();
			item.setRefBy(refBy);
			item.setType(type);

			if (type.isDatatype() || type.isEntityType()) {
				if (!this.fieldTypes.contains(item)) {
					this.fieldTypes.add(item);
				}
			}
		}
	}

	/**
	 * Make a new blank artifact for the same type
	 * 
	 * @return
	 */
	protected abstract IAbstractArtifact makeArtifact();

	/**
	 * Builds an internal list of all inherited fields for this artifact by
	 * walking up the inheritence tree if any.
	 * 
	 */
	protected void resolveInheritedFields() throws TigerstripeException {

		List<AbstractArtifact> visited = new ArrayList<AbstractArtifact>();
		// The inherited fields
		AbstractArtifact parent = getExtends();
		this.inheritedFields = new ArrayList();
		while (parent != null) {
			this.inheritedFields.addAll(parent.getFields());
			parent = parent.getExtends();
			if (visited.contains(parent))
				throw new TigerstripeException(
						"Circular 'extends' relationship detected for "
								+ getFullyQualifiedName());
			// Detected a circular relationship!
			else {
				visited.add(parent);
			}
		}
	}

	/**
	 * Builds an internal list of all inherited labels for this artifact by
	 * walking up the inheritence tree if any.
	 * 
	 */
	protected void resolveInheritedLabels() throws TigerstripeException {
		// The inherited labels
		List<AbstractArtifact> visited = new ArrayList<AbstractArtifact>();
		AbstractArtifact parent = getExtends();
		this.inheritedLabels = new ArrayList();
		while (parent != null) {
			this.inheritedLabels.addAll(parent.getLabels());
			parent = parent.getExtends();
			if (visited.contains(parent))
				throw new TigerstripeException(
						"Circular 'extends' relationship detected for "
								+ getFullyQualifiedName());
			// Detected a circular relationship!
			else {
				visited.add(parent);
			}
		}
	}

	/**
	 * Builds an internal list of all inherited methods for this artifact by
	 * walking up the inheritence tree if any.
	 * 
	 */
	protected void resolveInheritedMethods() throws TigerstripeException {
		// The inherited methods
		List<AbstractArtifact> visited = new ArrayList<AbstractArtifact>();
		AbstractArtifact parent = getExtends();
		this.inheritedMethods = new ArrayList();
		while (parent != null) {
			this.inheritedMethods.addAll(parent.getMethods());
			parent = parent.getExtends();
			if (visited.contains(parent))
				throw new TigerstripeException(
						"Circular 'extends' relationship detected for "
								+ getFullyQualifiedName());
			// Detected a circular relationship!
			else {
				visited.add(parent);
			}
		}
	}

	/**
	 * Returns all methods for this Artifact
	 * 
	 * @return
	 */
	public Collection getMethods() {
		return this.methods;
	}

	/**
	 * Returns all the non-inherited fields for this Artifact
	 * 
	 * @return Collection of Field - a collection of Fields for this artifact
	 */
	public Collection getFields() {
		return this.fields;
	}

	/**
	 * Returns all the unique datatype of fields for this Artifact
	 * 
	 * @return Collection of Type - a collection of Type for this artifact
	 */
	public Collection getFieldTypes() {
		if (this.fieldTypes == null)
			createUniqueFieldTypeList();
		return this.fieldTypes;
	}

	public IFieldTypeRef[] getFieldITypes() {
		Collection result = getFieldTypes();
		return (IFieldTypeRef[]) result.toArray(new IFieldTypeRef[result
				.size()]);
	}

	/**
	 * Returns all the non-inherited labels for this Artifact
	 * 
	 * @return Collection of Labels - a collection of Labels for this artifact
	 */
	public Collection getLabels() {
		return this.labels;
	}

	/**
	 * Returns all the inherited fields for this artifact.
	 * 
	 * @return Collection of Field - a collection of Fields for this artifact
	 */
	public Collection getInheritedFields() {
		if (inheritedFields == null) {
			try {
				resolveInheritedFields();
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"While trying to resolve inherited fields for "
								+ getFullyQualifiedName(), e);
				return Collections.EMPTY_LIST;
			}
		}
		return this.inheritedFields;
	}

	/**
	 * Returns all the inherited labels for this artifact.
	 * 
	 * @return Collection of Label - a collection of Labels for this artifact
	 */
	public Collection getInheritedLabels() {
		if (inheritedLabels == null) {
			try {
				resolveInheritedLabels();
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"While trying to resolved inherited Labels for "
								+ getFullyQualifiedName(), e);
				return Collections.EMPTY_LIST;
			}
		}
		return this.inheritedLabels;
	}

	/**
	 * Returns all the inherited methods for this artifact.
	 * 
	 * @return Collection of Method - a collection of Methods for this artifact
	 */
	public Collection getInheritedMethods() {
		if (inheritedMethods == null) {
			try {
				resolveInheritedMethods();
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"While trying to resolve inherited Methods for "
								+ getFullyQualifiedName(), e);
				return Collections.EMPTY_LIST;
			}
		}
		return this.inheritedMethods;
	}

	/**
	 * Returns the artifact extended by this artifact.
	 * 
	 * @return AbstractArtifact - the artifact extended by this artifact.
	 */
	public AbstractArtifact getExtends() {
		return this.extendsArtifact;
	}

	/**
	 * Returns true if this artifact extends another artifact
	 * 
	 * @return true if this artifacts extends another artifact, false otherwise.
	 */
	public boolean hasExtends() {
		return getExtends() != null;
	}

	/**
	 * Returns true if this AbstractArtifact is a model artifact
	 * 
	 * @return
	 */
	public boolean isModelArtifact() {
		return (this instanceof ManagedEntityArtifact
				|| this instanceof DatatypeArtifact || this instanceof EnumArtifact);
	}

	public boolean isCapabilitiesArtifact() {
		return (this instanceof QueryArtifact || this instanceof EventArtifact || this instanceof ExceptionArtifact);
	}

	public boolean isSessionArtifact() {
		return (this instanceof SessionFacadeArtifact);
	}

	public boolean isDatatypeArtifact() {
		return (this instanceof DatatypeArtifact);
	}

	protected abstract AbstractArtifactPersister getPersister(Writer writer);

	// =================================================================
	// Methods to satisfy the IAbstractArtifact interface

	public IField[] getIFields() {
		Collection fields = getFields();
		IField[] result = new IField[fields.size()];
		return (IField[]) fields.toArray(result);
	}

	public void addIField(IField field) {
		fields.add(field);
		((Field) field).setContainingArtifact(this);

		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredFields = null;

	}

	public void removeIFields(IField[] fields) {
		this.fields.removeAll(Arrays.asList(fields));
		for (IField field : fields) {
			((Field) field).setContainingArtifact(null);
		}

		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredFields = null;
	}

	public IField makeIField() {
		return new Field(getArtifactManager());
	}

	public void setIFields(IField[] fields) {
		this.fields = Arrays.asList(fields);
		for (IField field : fields) {
			((Field) field).setContainingArtifact(this);
		}
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredFields = null;
	}

	public ILabel[] getILabels() {
		Collection labels = getLabels();
		ILabel[] result = new ILabel[labels.size()];
		return (ILabel[]) labels.toArray(result);
	}

	public void addILabel(ILabel label) {
		labels.add(label);
		((Label) label).setContainingArtifact(this);

		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredLabels = null;
	}

	public void removeILabels(ILabel[] labels) {
		this.labels.removeAll(Arrays.asList(labels));
		for (ILabel label : labels) {
			((Label) label).setContainingArtifact(null);
		}
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredLabels = null;
	}

	public ILabel makeILabel() {
		Label result = new Label(getArtifactManager());
		return result;
	}

	public void setILabels(ILabel[] labels) {
		this.labels = Arrays.asList(labels);
		for (ILabel label : labels) {
			((Label) label).setContainingArtifact(this);
		}
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredLabels = null;
	}

	public IMethod[] getIMethods() {
		IMethod[] result = new IMethod[getMethods().size()];
		return (IMethod[]) getMethods().toArray(result);
	}

	public IMethod makeIMethod() {
		Method result = new Method(getArtifactManager());
		result.setContainingArtifact(this);
		return result;
	}

	public void addIMethod(IMethod method) {
		getMethods().add(method);
		((Method) method).setContainingArtifact(this);

		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredMethods = null;
	}

	public void removeIMethods(IMethod[] methods) {
		getMethods().removeAll(Arrays.asList(methods));
		for (IMethod method : methods) {
			((Method) method).setContainingArtifact(null);
		}
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredMethods = null;
	}

	public void setIMethods(IMethod[] methods) {
		this.methods = Arrays.asList(methods);
		for (IMethod method : methods) {
			((Method) method).setContainingArtifact(this);
		}
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredMethods = null;
	}

	/**
	 * Removing the given artifact from the list of artifacts that are extending
	 * this.
	 * 
	 * @param artifact
	 * @since 2.2-beta
	 */
	private void removeFromExtending(IAbstractArtifact artifact) {
		if (artifact != null && extendingArtifacts.contains(artifact)) {
			extendingArtifacts.remove(artifact);
		}
	}

	/**
	 * Adding the given artifact to the list of artifacts that are are extending
	 * this.
	 * 
	 * @param artifact
	 * @since 2.2-beta
	 */
	private void addToExtending(IAbstractArtifact artifact) {
		if (artifact != null && !extendingArtifacts.contains(artifact)) {
			extendingArtifacts.add(artifact);
		}
	}

	/**
	 * Clears the list of extending artifacts
	 * 
	 */
	/* package */void clearExtendingArtifacts() {
		extendingArtifacts.clear();
	}

	/**
	 * Returns the list of artifacts that are extending "this" artifact.
	 * 
	 * @return
	 * @since 2.2-beta
	 */
	public IAbstractArtifact[] getExtendingArtifacts() {
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();

		for (IAbstractArtifact art : extendingArtifacts) {
			try {
				// Bug 919: facet needs to be considered here
				if (getArtifactManager() != null
						&& getArtifactManager().getActiveFacet() != null) {
					if (art.isInActiveFacet())
						result.add(art);
				} else {
					result.add(art);
				}
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"Couldn't resolve Extending Artifacts for "
								+ getFullyQualifiedName() + ": "
								+ e.getMessage(), e);
			}
		}

		return result.toArray(new IAbstractArtifact[result.size()]);
	}

	public IAbstractArtifact[] getExtendingIArtifacts() {
		return getExtendingArtifacts();
	}

	/**
	 * Updates all the artifacts that were extending this to extend the new
	 * artifact instead.
	 * 
	 * @param newArtifact
	 */
	/* package */void updateExtendingArtifacts(IAbstractArtifact newArtifact) {
		IAbstractArtifact[] arts = getExtendingArtifacts();
		for (IAbstractArtifact art : arts) {
			art.setExtendedIArtifact(newArtifact);
		}
		clearExtendingArtifacts(); // This shouldn't be necessary as each set
		// above will
		// remove from our list.
	}

	public void setExtendedIArtifact(IAbstractArtifact artifact) {
		// Since 2.2-beta we realized that the extended artifact wasn't
		// updated properly: when an artifact is modified, a new instance
		// replaces
		// the old instance in the artifact mgr. However, if other artifacts
		// were
		// extending that instance being replaced, the reference wasn't updated.
		// As a result, we are now keeping a list of artifacts that are
		// extending "this", so that in the event "this" is replaced, we can
		// go thru this list and update their reference to the instance
		// that is replacing "this" in the artifact mgr.

		if (extendsArtifact != null) {
			// need to notify the former extendsArtifact that we are no longer
			// extending it
			extendsArtifact.removeFromExtending(this);
		}

		if (artifact != null) {
			// need to notify the artifact that we are now extending it
			((AbstractArtifact) artifact).addToExtending(this);
		}

		this.extendsArtifact = (AbstractArtifact) artifact;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Check validity of the AbstractArtifact's data fields (name, class it
	 * inherits from, fields, labels, and methods) before saving this object to
	 * the underlying Tigerstripe data model
	 * 
	 * @see org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact#validate()
	 */
	public List<TigerstripeError> validate() {
		List<TigerstripeError> errors = new ArrayList<TigerstripeError>();
		/*
		 * check the value of the artifact name to ensure it will make a
		 * legitimate class name in the model
		 */
		if (!TigerstripeValidationUtils.classNamePattern.matcher(getName())
				.matches()
				&& !TigerstripeValidationUtils.elementNamePattern.matcher(
						getName()).matches()) {
			errors.add(new TigerstripeError(TigerstripeErrorLevel.ERROR, "'"
					+ getName() + "' is not a legal artifact name"));
		} else if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			errors
					.add(new TigerstripeError(
							TigerstripeErrorLevel.ERROR,
							"'"
									+ getName()
									+ "' is is a reserved keyword and cannot be used as an artifact name"));
		}
		// check the package name to ensure it is a valid package name
		// check to see which object this object extends (if any)
		IAbstractArtifact iaa = getExtendedIArtifact();
		if (iaa != null
				&& iaa.getFullyQualifiedName().equals(
						this.getFullyQualifiedName()))
			errors.add(new TigerstripeError(TigerstripeErrorLevel.ERROR,
					"Illegal Inheritance for '" + this.getName()
							+ "'; an object cannot extend itself"));
		// check validity of the fields defined for this artifact
		IField[] fields = getIFields();
		for (int i = 0; i < fields.length; i++) {
			List<TigerstripeError> errorList = fields[i].validate();
			if (!errorList.isEmpty())
				errors.addAll(errorList);
		}
		// check validity of the labels (constants) defined for this artifact
		ILabel[] labels = getILabels();
		for (int i = 0; i < labels.length; i++) {
			List<TigerstripeError> errorList = labels[i].validate();
			if (!errorList.isEmpty())
				errors.addAll(errorList);
		}
		// check validity of the methods defined for this artifact
		IMethod[] methods = getIMethods();
		for (int i = 0; i < methods.length; i++) {
			List<TigerstripeError> errorList = methods[i].validate();
			if (!errorList.isEmpty())
				errors.addAll(errorList);
		}
		return errors;
	}

	public void doSave(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {
		doSave(true, monitor);
	}

	public void doSilentSave(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {
		doSave(false, monitor);
	}

	/**
	 * Returns the artifact path relative to the project directory
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public String getArtifactPath() throws TigerstripeException {
		// Determine the path for this artifact
		String packageName = getPackage().replace('.', File.separatorChar);

		if (getTSProject() == null || getTSProject().getBaseDir() == null)
			return null; // this is part of a module

		String baseDir = getTSProject().getBaseDir().toString();

		// issue#22/Bug 412
		// We need to extract the location of the Artifact Repository. Please
		// note we can only support 1 single location.
		// It can include only 1 'includes' statment that must follow a defined
		// pattern:
		// xxx/xxx/xxx/**/*.java
		// We will explicitly look for **/*.java and take the prefix as the
		// repository location.
		// List<ArtifactRepository> repositories = (List<ArtifactRepository>)
		// getTSProject()
		// .getArtifactRepositories();
		// if (repositories == null || repositories.size() == 0) {
		// throw new TigerstripeException(
		// "No defined Artifact repository in tigerstripe.xml");
		// }
		//
		// ArtifactRepository repo = repositories.get(0);
		// if (repo.getIncludes().length == 0) {
		// throw new TigerstripeException(
		// "No 'includes' defined for repository "
		// + repo.getBaseDirectory());
		// }
		//
		// String repoLocation = repo.getIncludes()[0];
		// int index = repoLocation.indexOf("**/*.java");
		// if (index == -1) {
		// throw new TigerstripeException(
		// "Incorrect 'includes' clause for repository: '"
		// + repoLocation + "'. Must follow xxx/yyy/**/*.java");
		// }
		// repoLocation = repoLocation.substring(0, index);
		String repoLocation = getTSProject().getRepositoryLocation();

		// Make sure the package dir exists
		File dir = new File(baseDir + File.separator + repoLocation
				+ File.separator + packageName);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String artifactPath = repoLocation + File.separator + packageName
				+ File.separator + getName() + ".java";

		return artifactPath;
	}

	private void doSave(boolean notify, ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {

		// Determine the path for this artifact
		String packageName = getPackage().replace('.', File.separatorChar);
		String baseDir = getTSProject().getBaseDir().toString();

		// issue#22/Bug 412
		// We need to extract the location of the Artifact Repository. Please
		// note we can only support 1 single location.
		// It can include only 1 'includes' statment that must follow a defined
		// pattern:
		// xxx/xxx/xxx/**/*.java
		// We will explicitly look for **/*.java and take the prefix as the
		// repository location.
		// List<ArtifactRepository> repositories = (List<ArtifactRepository>)
		// getTSProject()
		// .getArtifactRepositories();
		// if (repositories == null || repositories.size() == 0) {
		// throw new TigerstripeException(
		// "No defined Artifact repository in tigerstripe.xml");
		// }
		//
		// ArtifactRepository repo = repositories.get(0);
		// if (repo.getIncludes().length == 0) {
		// throw new TigerstripeException(
		// "No 'includes' defined for repository "
		// + repo.getBaseDirectory());
		// }
		//
		// String repoLocation = repo.getIncludes()[0];
		// int index = repoLocation.indexOf("**/*.java");
		// if (index == -1) {
		// throw new TigerstripeException(
		// "Incorrect 'includes' clause for repository: '"
		// + repoLocation + "'. Must follow xxx/yyy/**/*.java");
		// }
		// repoLocation = repoLocation.substring(0, index);
		String repoLocation = getTSProject().getRepositoryLocation();

		// Make sure the package dir exists
		File dir = new File(baseDir + File.separator + repoLocation
				+ File.separator + packageName);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String artifactPath = baseDir + File.separator + repoLocation
				+ File.separator + packageName + File.separator + getName()
				+ ".java";

		try {
			Writer writer = new FileWriter(artifactPath);
			AbstractArtifactPersister persister = getPersister(writer);
			persister.applyTemplate();
		} catch (IOException e) {
			throw new TigerstripeException("Error while saving "
					+ getFullyQualifiedName() + ": " + e.getLocalizedMessage(),
					e);
		}

		if (notify) {
			// This is what will actually update the content of the Artifact Mgr
			// and notify listeners.
			getArtifactManager().notifyArtifactSaved(this, monitor);
		}
	}

	public String asText() throws TigerstripeException {
		StringWriter writer = new StringWriter();
		AbstractArtifactPersister persister = getPersister(writer);
		persister.applyTemplate();

		return writer.toString();
	}

	public void write(Writer writer) throws TigerstripeException {
		AbstractArtifactPersister persister = getPersister(writer);
		persister.applyTemplate();
	}

	public IProjectDescriptor getIProjectDescriptor() {
		return getTSProject();
	}

	/**
	 * Returns a Handle for the project if it exists. Please note that for
	 * Artifacts living in a module, this will return NULL;
	 */
	public ITigerstripeProject getIProject() {
		TigerstripeProjectHandle handle = null;

		if (getTSProject() == null || getTSProject().getBaseDir() == null)
			return null;

		try {
			handle = (TigerstripeProjectHandle) API.getDefaultProjectSession()
					.makeTigerstripeProject(
							getTSProject().getBaseDir().toURI(), null);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e); // FIXME
		} catch (TigerstripeLicenseException e) {
			TigerstripeRuntime.logErrorMessage(
					"TigerstripeLicenseException detected", e);
		}
		return handle;
	}

	/**
	 * Create an abstract artifact from a JavaClass
	 */
	public AbstractArtifact(JavaClass javaClass, ArtifactManager artifactMgr,
			ITigerstripeProgressMonitor monitor) {
		this(artifactMgr);

		this.javaClass = javaClass;
		if (javaClass != null) {
			buildModel(javaClass, monitor);
		}
	}

	private IStandardSpecifics specifics;

	public IStandardSpecifics getIStandardSpecifics() {
		return specifics;
	}

	protected void setIStandardSpecifics(IStandardSpecifics specifics) {
		this.specifics = specifics;
	}

	public JavaSource getJavaSource() {
		return javaSource;
	}

	public void setJavaSource(JavaSource javaSource) {
		this.javaSource = javaSource;
	}

	public abstract AbstractArtifact getModel();

	/**
	 * Returns a collection of all artifacts FQNs referenced in this artifact
	 * 
	 * It explores: extended artifacts, fields, method returns and method args
	 * 
	 * @return
	 */
	public Collection getReferencedArtifacts() {
		Collection result = new ArrayList();

		if (hasExtends()) {
			result.add(getExtends().getFullyQualifiedName());
		}

		for (Iterator iter = fields.iterator(); iter.hasNext();) {
			Field field = (Field) iter.next();
			String fqn = field.getIType().getFullyQualifiedName();
			if (!field.getIType().isPrimitive() && !result.contains(fqn))
				result.add(fqn);
		}

		for (Iterator iter = methods.iterator(); iter.hasNext();) {
			Method method = (Method) iter.next();
			String fqn = method.getReturnIType().getFullyQualifiedName();
			if (!"void".equals(fqn) && !method.getReturnIType().isPrimitive()
					&& !result.contains(fqn)) {
				result.add(fqn);
			}

			for (Iterator argIter = method.getArguments().iterator(); argIter
					.hasNext();) {
				Argument arg = (Argument) argIter.next();
				if (!arg.getIType().isPrimitive()
						&& !result.contains(arg.getIType()
								.getFullyQualifiedName())) {
					result.add(arg.getIType().getFullyQualifiedName());
				}
			}

			for (IException except : method.getIExceptions()) {
				result.add(except.getFullyQualifiedName());
			}
		}

		return result;
	}

	public IMethod getMethodById(String methodId) {
		for (IMethod method : methods) {
			if (method.getMethodId().equals(methodId))
				return method;
		}
		return null;
	}

	// =================================================================
	// Methods to satisfy the IArtifact interface


	public IField[] getIFields(boolean filterFacetExcludedFields) {
		Collection fields = getFields();
		if (filterFacetExcludedFields) {
			if (facetFilteredFields == null) {
				Collection filtered = filterFacetExcludedComponents(fields);
				facetFilteredFields = (IField[]) filtered
						.toArray(new IField[filtered.size()]);
			}
			return facetFilteredFields;
		} else
			return (IField[]) fields.toArray(new IField[fields.size()]);
	}

	public IField[] getInheritedIFields() {
		return getInheritedIFields(false);
	}

	public IField[] getInheritedIFields(boolean filterFacetExcludedFields) {
		Collection fields = getInheritedFields();
		if (filterFacetExcludedFields) {
			if (facetFilteredInheritedFields == null) {
				Collection filtered = filterFacetExcludedComponents(fields);
				facetFilteredInheritedFields = (IField[]) filtered
						.toArray(new IField[filtered.size()]);
			}
			return facetFilteredInheritedFields;
		} else
			return (IField[]) fields.toArray(new IField[fields.size()]);
	}


	public ILabel[] getILabels(boolean filterFacetExcludedLabels) {
		Collection labels = getLabels();
		if (filterFacetExcludedLabels) {
			if (facetFilteredLabels == null) {
				Collection filtered = filterFacetExcludedComponents(labels);
				facetFilteredLabels = (ILabel[]) filtered
						.toArray(new ILabel[filtered.size()]);
			}
			return facetFilteredLabels;
		} else
			return (ILabel[]) labels.toArray(new ILabel[labels.size()]);
	}

	public ILabel[] getInheritedILabels() {
		return getInheritedILabels(false);
	}

	public ILabel[] getInheritedILabels(boolean filterFacetExcludedLabels) {
		Collection labels = getInheritedLabels();
		if (filterFacetExcludedLabels) {
			if (facetFilteredInheritedLabels == null) {
				Collection filtered = filterFacetExcludedComponents(labels);
				facetFilteredInheritedLabels = (ILabel[]) filtered
						.toArray(new ILabel[filtered.size()]);
			}
			return facetFilteredInheritedLabels;
		} else
			return (ILabel[]) labels.toArray(new ILabel[labels.size()]);
	}


	public IMethod[] getIMethods(boolean filterFacetExcludedMethods) {
		Collection methods = getMethods();
		if (filterFacetExcludedMethods) {
			if (facetFilteredMethods == null) {
				Collection filtered = filterFacetExcludedComponents(methods);
				facetFilteredMethods = (IMethod[]) filtered
						.toArray(new IMethod[filtered.size()]);
			}
			return facetFilteredMethods;
		} else
			return (IMethod[]) methods
					.toArray(new IMethod[methods.size()]);
	}

	public IMethod[] getInheritedIMethods() {
		return getInheritedIMethods(false);
	}

	public IMethod[] getInheritedIMethods(
			boolean filterFacetExcludedMethods) {
		Collection methods = getInheritedMethods();
		if (filterFacetExcludedMethods) {
			if (facetFilteredInheritedMethods == null) {
				Collection filtered = filterFacetExcludedComponents(methods);
				facetFilteredInheritedMethods = (IMethod[]) filtered
						.toArray(new IMethod[filtered.size()]);
			}
			return facetFilteredInheritedMethods;
		} else
			return (IMethod[]) methods
					.toArray(new IMethod[methods.size()]);
	}

	public IAbstractArtifact getExtendedIArtifact() {
		return getExtends();
	}


	public ITigerstripeProject getITigerstripeProject() {
		return getIProject();
	}
	
	public Object[] getChildren() {
		IField[] fields = getIFields();
		IMethod[] methods = getIMethods();
		ILabel[] labels = getILabels();

		Object[] objects = new Object[fields.length + methods.length
				+ labels.length];
		int index = 0;
		for (IField field : fields) {
			objects[index++] = field;
		}
		for (IMethod method : methods) {
			objects[index++] = method;
		}
		for (ILabel label : labels) {
			objects[index++] = label;
		}
		return objects;
	}

	@Override
	public boolean isInActiveFacet() throws TigerstripeException {
		return getArtifactManager().isInActiveFacet(this);
	}

	/**
	 * This method is called right before this artifact is removed from its
	 * artifact manager.
	 * 
	 * @since 2.2-beta
	 */
	/* package */void dispose() {
		// nothing for now
		// Bug 882: was setting extends to null incorrectly.
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractArtifact)
			return obj == this; // default behavior
		else if (obj instanceof ComparableArtifact) {
			ComparableArtifact other = (ComparableArtifact) obj;
			return other.getArtifact() != null
					&& other.getArtifact().getFullyQualifiedName().equals(
							getFullyQualifiedName());
		}

		return false;
	}

	public IAbstractArtifact[] getImplementingIArtifacts() {
		return new IAbstractArtifact[0];
	}

	public IAbstractArtifact[] getReferencedIArtifacts() {
		Set<IAbstractArtifact> result = new HashSet<IAbstractArtifact>();
		for (IField field : getIFields()) {
			if (!field.getIType().isPrimitive()
					&& !(field.getIType().getIArtifact() instanceof IPrimitiveTypeArtifact)
					&& field.getIType().getIArtifact() != null) {
				result.add(field.getIType().getIArtifact());
			}
		}

		for (IMethod method : getIMethods()) {
			if (!method.isVoid()) {
				IType returnType = method.getReturnIType();
				if (!returnType.isPrimitive()
						&& !(returnType.getIArtifact() instanceof IPrimitiveTypeArtifact)
						&& returnType.getIArtifact() != null) {
					result.add(returnType.getIArtifact());
				}
			}

			for (IArgument arg : method.getIArguments()) {
				IType artType = arg.getIType();
				if (!artType.isPrimitive()
						&& !(artType.getIArtifact() instanceof IPrimitiveTypeArtifact)
						&& artType.getIArtifact() != null) {
					result.add(artType.getIArtifact());
				}
			}
		}
		return result.toArray(new IAbstractArtifact[result.size()]);
	}

	public IAbstractArtifact[] getReferencingIArtifacts() {
		return new IAbstractArtifact[0];
	}

	// public boolean equals( Object obj ) {
	// if ( obj instanceof AbstractArtifact ) {
	// AbstractArtifact other = (AbstractArtifact) obj;
	// if ( other.getArtifactManager() == getArtifactManager() ) {
	// if ( other.getFullyQualifiedName() != null &&
	// other.getFullyQualifiedName().equals(getFullyQualifiedName())) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	// Fields/Methods/Labels can be filtered by facets. Upon first request we
	// build these lists
	// lazily, but when the facet either changes or goes away, we must clear
	// this cache.
	// This method is called by our Artifact Mgr whenever the facet changes.
	/* package */void resetFacetFilteredLists() {
		facetFilteredFields = null;
		facetFilteredMethods = null;
		facetFilteredLabels = null;
		facetFilteredInheritedFields = null;
		facetFilteredInheritedMethods = null;
		facetFilteredInheritedLabels = null;
	}

	@Override
	public String toString() {
		return this.getFullyQualifiedName() + "(" + this.hashCode() + ")";
	}

	/**
	 * Computes and returns the IModelUpdater that should be used to submit any
	 * IModelChangeRequest on this artifact
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IModelUpdater getUpdater() throws TigerstripeException {
		if (myUpdater == null) {
			ArtifactManager mgr = getArtifactManager();
			if (mgr instanceof ModuleArtifactManager)
				throw new TigerstripeException("No updater for "
						+ getFullyQualifiedName()
						+ ": artifact is read-only (module)");
			try {
				IAbstractTigerstripeProject aProject = API
						.getDefaultProjectSession().makeTigerstripeProject(
								mgr.getTSProject().getBaseDir().toURI());
				if (aProject instanceof ITigerstripeProject) {
					IArtifactManagerSession session = ((ITigerstripeProject) aProject)
							.getArtifactManagerSession();
					myUpdater = session.getIModelUpdater();
				} else
					throw new TigerstripeException("Can't figure updater for "
							+ getFullyQualifiedName());
			} catch (TigerstripeLicenseException e) {
				throw new TigerstripeException("Can't figure updater for "
						+ getFullyQualifiedName() + ": " + e.getMessage(), e);
			}
		}

		return myUpdater;
	}
}