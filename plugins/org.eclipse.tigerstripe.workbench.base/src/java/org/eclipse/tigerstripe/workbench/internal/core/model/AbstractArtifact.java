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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleDescriptorModel;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.ComparableArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.QDoxUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IStandardSpecifics;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.project.IProjectDescriptor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;

/**
 * @author Eric Dillon
 * 
 *         This is an abstract definition of a Tigerstripe Artifact.
 * 
 *         A Tigerstripe artifact is a source artifact used by the tigerstripe
 *         engine to produce output. It is modeled based on an annotated java
 *         source file.
 * 
 *         The type of AbstractArtifact is determined by the "MarkingTag" of the
 *         artifact. For example, a ManagedEntityArtifact will be recognized by
 *         the presence of a "@tigerstripe.managedEntity" class-level tag.
 * 
 *         The #buildModel() method is called by the constructor to use the
 *         given JavaClass object (from the Qdox parser) and build the
 *         corresponding model by going through the found tags.
 * 
 *         Once all the artifacts have been extracted by the ArtifactManager
 *         based on the provided source files, the #valide() method is called on
 *         all extracted artifact to allow for a semantic pass on the model and
 *         allow tigerstripe to detect semantic errors (unresolved references,
 *         e.g.).
 * 
 */
public abstract class AbstractArtifact extends ArtifactComponent implements
		IAbstractArtifactInternal {

	// the updater that this artifact depends on
	private IModelUpdater myUpdater;

	// The package this AbstractArtifact lives in
	private String _package = "";

	private String _artifactPath = null;

	// The fully qualifiedName for this artifact
	private String _fullyQualifiedName;

	/** The collection of methods for this artifact */
	protected Collection<IMethod> methods;

	/** The collection of fields for this artifact */
	protected Collection<IField> fields;

	/** The collection of (unique, non-primitve) datatypes of the fields */
	private Collection<IFieldTypeRef> fieldTypes;

	/** The collection of literals (enum) for this artifact */
	private Collection<ILiteral> literals;

	/** The collection of inherited fields for this artifact */
	protected Collection<IField> inheritedFields = null;

	/** The collection of inherited literals for this artifact */
	protected Collection<ILiteral> inheritedLiterals = null;

	/** The collection of inherited methods for this artifact */
	protected Collection<IMethod> inheritedMethods = null;

	/** The list of artifacts that are extending "this". */
	private final Collection<IAbstractArtifact> extendingArtifacts = new ArrayList<IAbstractArtifact>();

	/** The map of RefComments for this artifact */
	private final HashMap<String, RefComment> refComments = new HashMap<String, RefComment>();

	/** The artifact referenced by the extends clause */
	private IAbstractArtifactInternal extendsArtifact;

	/** The components owned by this artifact */
	Collection<IModelComponent> containedComponents = new ArrayList<IModelComponent>();

	/**
	 * The artifact containing this artifact Note that In this case it is always
	 * an artifact (a package Artifact to be precise) This might be empty if
	 * Packages are disabled.
	 */
	private IModelComponent containingModelComponent;

	/** The artifacts referenced by the "implements" clause */
	private final List<IAbstractArtifact> implementedArtifacts = new ArrayList<IAbstractArtifact>();

	/** The javaclass that this artifact was extracted from */
	protected JavaClass javaClass;

	/**
	 * The javaSource this artifact was extracted from
	 */
	private JavaSource javaSource;

	/** whether the originating javaClass is abstract or not */
	private boolean isAbstract = false;

	// Facet scoping for Fields/Methods/Literals. Build lazily filtered list
	// when
	// ever needed
	protected Collection<IField> facetFilteredFields = null;

	protected Collection<IField> facetFilteredInheritedFields = null;

	protected Collection<IMethod> facetFilteredMethods = null;

	protected Collection<IMethod> facetFilteredInheritedMethods = null;

	protected Collection<ILiteral> facetFilteredLiterals = null;

	protected Collection<ILiteral> facetFilteredInheritedLiterals = null;

	private boolean isProxy = false;

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#setProxy(boolean)
	 */
	public void setProxy(boolean bool) {
		isProxy = bool;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#isProxy()
	 */
	public boolean isProxy() {
		return isProxy;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#setTSProject(org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject)
	 */
	public void setTSProject(TigerstripeProject project) {
	}

	public TigerstripeProject getTSProject() {
		return getArtifactManager().getTSProject();
	}

	public boolean isReadonly() {
		return getArtifactManager() != null
				&& getArtifactManager() instanceof ModuleArtifactManager;
	}

	public IModuleHeader getParentModuleHeader() {
		if (!isReadonly())
			return null;

		ModuleArtifactManager mMgr = (ModuleArtifactManager) getArtifactManager();
		ModuleDescriptorModel moduleModel = mMgr.getModuleModel();
		return moduleModel.getModuleHeader();
	}

	// ======================================================================
	// since 2.2: Implemented Artifacts
	@ProvideModelComponents
	public Collection<IAbstractArtifact> getImplementedArtifacts() {
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();

		for (IAbstractArtifact art : implementedArtifacts) {
			IAbstractArtifact realArtifact = ((IAbstractArtifactInternal) art)
					.resolveIfProxy(null);
			try {
				// Bug 919: facet needs to be considered here
				if (getArtifactManager() != null
						&& getArtifactManager().getActiveFacet() != null) {
					if (realArtifact.isInActiveFacet())
						result.add(realArtifact);
				} else {
					result.add(realArtifact);
				}
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"Couldn't resolve Implemented Artifacts for "
								+ getFullyQualifiedName() + ": "
								+ e.getMessage(), e);
			}
		}

		return Collections.unmodifiableCollection(result);
	}

	public void setImplementedArtifacts(Collection<IAbstractArtifact> artifacts) {
		implementedArtifacts.clear();
		implementedArtifacts.addAll(artifacts);
	}

	/**
	 * coming from IArtifact (External API)
	 */
	@ProvideModelComponents
	public Collection<IAbstractArtifact> getAncestors() {
		ArrayList<IAbstractArtifact> ancestors = new ArrayList<IAbstractArtifact>();
		if (getExtendedArtifact() != null) {
			ancestors.add(getExtendedArtifact());
			ancestors.addAll(getExtendedArtifact().getAncestors());
		}
		return Collections.unmodifiableCollection(ancestors);
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
	 * @param packageName
	 *            -
	 * @param name
	 *            -
	 */
	public AbstractArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		methods = new ArrayList();
		fields = new ArrayList();
		literals = new ArrayList();

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
		this._package = packageName;
		if (this._package.equals("")) {
			this._fullyQualifiedName = getName();
		} else
			this._fullyQualifiedName = this._package + "." + getName();
		updateArtifactPath();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#getMarkingTag()
	 */
	public abstract String getMarkingTag();

	/**
	 * Returns the name of the Java package this artifact belongs to
	 * 
	 * @return
	 */
	public String getPackage() {
		if (this._package == null) {
			this.setPackage("");
		}
		return _package;
	}

	/**
	 * Returns the qualified name of this artifact
	 * 
	 * @return
	 */
	public String getFullyQualifiedName() {
		return _fullyQualifiedName;
	}

	public void setFullyQualifiedName(String fqn) {
		this.setPackage(Util.packageOf(fqn));
		this.setName(Util.nameOf(fqn));
		this._fullyQualifiedName = fqn;
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		// if (this._package.equals("")){
		// this._fullyQualifiedName = getName();
		// } else
		this._fullyQualifiedName = this._package + "." + getName();
		updateArtifactPath();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#extractFromClass(com.thoughtworks.qdox.model.JavaClass, org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public abstract IAbstractArtifactInternal extractFromClass(
			JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor);

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
	 * @param clazz
	 *            - JavaClass the parsed source file to build a model for
	 */
	protected void buildModel(JavaClass clazz, IProgressMonitor monitor) {

		setName(clazz.getName());
		if (clazz.getPackage() != null) {
			setPackage(clazz.getPackage());
		} else {
			// This can be the case for "top-level" packages
			setPackage("");
		}
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
			if (getTigerstripeProject() != null) {
				// if IProject is null it means we're in a module.
				ignoreTags = "false"
						.equalsIgnoreCase(getTigerstripeProject()
								.getAdvancedProperty(
										IAdvancedProperties.PROP_MISC_IgnoreArtifactElementsWithoutTag));
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
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

		// And the literals
		this.literals = new ArrayList();
		JavaField[] javaFields = clazz.getFields();
		for (int i = 0; i < javaFields.length; i++) {
			Literal literal = new Literal(javaFields[i], getArtifactManager());
			if (ignoreTags) {
				this.literals.add(literal);
				literal.setContainingArtifact(this);
				literal.extractCustomProperties();
			} else if (literal.getFirstTagByName(AbstractArtifactTag.PREFIX
					+ AbstractArtifactTag.LITERAL) != null) {
				this.literals.add(literal);
				literal.setContainingArtifact(this);
				literal.extractCustomProperties();
			}
		}

		// The extends clause
		com.thoughtworks.qdox.model.Type superClass = clazz.getSuperClass();
		if (superClass != null) {
			String typeName = QDoxUtils.getTypeName(superClass);
			if (!Object.class.getName().equals(typeName)) {
				setExtendedArtifact(typeName);
			}
		}
		
		this.containedComponents = new ArrayList<IModelComponent>();
		containedComponents.addAll(this.getFields());
		containedComponents.addAll(this.getMethods());
		containedComponents.addAll(this.getLiterals());
	}

	/**
	 * Sets the extended artifact for this based on FQN
	 * 
	 * This will create a proxy artifact as needed.
	 * 
	 * @param fqn
	 */
	public void setExtendedArtifact(String fqn) {
		if (fqn == null || fqn.length() == 0) {
			setExtendedArtifact((IAbstractArtifact) null);
			return;
		}

		setExtendedArtifact(getArtifactManager()
				.getArtifactByFullyQualifiedName(fqn, true,
						new NullProgressMonitor()));

		if (getExtendedArtifact() == null) {
			// #386 Build a temporary dummy artifact, it will be further
			// resolved once everything has been parsed in the
			// resolveReferences()
			// method below
			IAbstractArtifactInternal art = (IAbstractArtifactInternal) makeArtifact();
			art.setFullyQualifiedName(fqn);
			art.setProxy(true);
			setExtendedArtifact(art);
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

	private void extractImplementedArtifacts(IProgressMonitor monitor) {
		Tag markingTag = getFirstTagByName(getMarkingTag());
		if (markingTag != null) {
			String val = markingTag.getProperties().getProperty("implements",
					"");
			if (!"".equals(val)) {
				String[] fqns = val.split(",");
				for (String fqn : fqns) {
					IAbstractArtifactInternal art = getArtifactManager()
							.getArtifactByFullyQualifiedName(fqn, true, monitor);
					if (art == null) {
						art = new SessionFacadeArtifact(getArtifactManager());
						art.setProxy(true);
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

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#resolveReferences(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void resolveReferences(IProgressMonitor monitor)
			throws TigerstripeException {

		// #386
		resolveExtendedArtifact(monitor);
		resolveImplementedArtifacts(monitor);

		// May need to sets the containing/contained stuff by package.
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
				.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
		if (prop.getDetailsForType(IPackageArtifact.class.getName())
				.isEnabled()) {

			resolvePackageContainment(monitor);

		}

		// Resolve of the inheritedFields so we don't have to walk
		// the inheritance hierarchy over and over
		// resolveInheritedFields(); Lazy-build the list 945
		// resolveInheritedLabels(); Lazy-build the list 945
		// resolveInheritedMethods(); Lazy-build the list 945
		// createUniqueFieldTypeList(); Lazy-build the list #945
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#resolvePackageContainment(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void resolvePackageContainment(IProgressMonitor monitor)
			throws TigerstripeException {
		if (getPackage() != "") {
			// Any *real* artifacts will be properly loaded.
			// Any that we need to spoof should be made here - but not saved!

			IAbstractArtifactInternal existingArtifact = getArtifactManager()
					.getArtifactByFullyQualifiedName(getPackage(), true,
							monitor);
			if (existingArtifact != null) {
				this.setContainingModelComponent(existingArtifact);
				existingArtifact.addContainedModelComponent(this);

			} else {

				// if (this.getProject() != null
				// && !getPackage().equals("primitive")) {
				// IArtifactManagerSession mgr = this.getProject()
				// .getArtifactManagerSession();
				// PackageArtifact newPackageArtifact = PackageArtifact
				// .makeVolatileArtifactForPackage(mgr, getPackage());
				// mgr.addArtifact(newPackageArtifact);
				// this.setContainingModelComponent(newPackageArtifact);
				// newPackageArtifact.addContainedModelComponent(this);
				//
				// }

			}
		}
	}

	public void removePackageContainment() {
		IModelComponent container = getContainingModelComponent();
		if (container instanceof IPackageArtifact)
			((IAbstractArtifactInternal) container)
					.removeContainedModelComponent(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#resolveExtendedArtifact(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void resolveExtendedArtifact(IProgressMonitor monitor) {
		if (extendsArtifact != null) {
			IAbstractArtifact realArtifact = extendsArtifact;
			if (extendsArtifact.isProxy()) {
				realArtifact = extendsArtifact.resolveIfProxy(monitor);
			}
			if (realArtifact != null) {
				setExtendedArtifact(realArtifact);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#resolveIfProxy(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IAbstractArtifactInternal resolveIfProxy(IProgressMonitor monitor) {
		if (!isProxy())
			return this;
		String fqn = getFullyQualifiedName();
		IAbstractArtifactInternal realArtifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(fqn, true, monitor);
		if (realArtifact != null)
			return realArtifact;

		return this;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#resolveImplementedArtifacts(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void resolveImplementedArtifacts(IProgressMonitor monitor) {
		List<IAbstractArtifact> newList = new ArrayList<IAbstractArtifact>();
		for (IAbstractArtifact art : implementedArtifacts) {
			IAbstractArtifact realArtifact = ((IAbstractArtifactInternal) art)
					.resolveIfProxy(monitor);
			if (realArtifact != null) {
				newList.add(realArtifact);
			} else
				newList.add(art);
		}
		implementedArtifacts.clear();
		implementedArtifacts.addAll(newList);
	}

	/**
	 * Remove references TO this object(only to used as a precursor to delete!
	 * References FROM the artifact will be destroyed by the delete.
	 * 
	 */
	public void removeReferences() {
		// Need to look at the containing Model Component.
		// May need to sets the containing/contained stuff by package.
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
				.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
		if (prop.getDetailsForType(IPackageArtifact.class.getName())
				.isEnabled()) {
			// Remove the ref to this in my "Container"
			IModelComponent container = this.getContainingModelComponent();
			if (container instanceof IPackageArtifact) {
				((IAbstractArtifactInternal) container)
						.removeContainedModelComponent(this);

			}
		}

		// Reference to this could also be thru Extends/Implements
		// forcing the object to be a proxy suffice as it will force
		// resolution the relationship is used
		setProxy(true);

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
		JavaField[] javaFields = clazz.getFields();
		for (int i = 0; i < javaFields.length; i++) {
			Literal literal = new Literal(javaFields[i], getArtifactManager());
			if (literal.getFirstTagByName(AbstractArtifactTag.PREFIX
					+ AbstractArtifactTag.REFCOMMENT) != null) {
				RefComment rComment = new RefComment(this);
				rComment.setContent(literal.getComment());
				rComment.setLabel(literal.getName());
				refComments.put(literal.getName(), rComment);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#getRefComments()
	 */
	public Collection<RefComment> getRefComments() {
		return refComments.values();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#setRefComment(org.eclipse.tigerstripe.workbench.internal.core.model.RefComment)
	 */
	public void setRefComment(RefComment rComment) {
		String id = rComment.getLabel();
		if (id != null) {
			refComments.put(id, rComment);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#getRefCommentById(java.lang.String)
	 */
	public RefComment getRefCommentById(String refCommentId) {
		return refComments.get(refCommentId);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#getUniqueRefCommentId()
	 */
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
			Type type = (Type) field.getType();

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

		List<IAbstractArtifactInternal> visited = new ArrayList<IAbstractArtifactInternal>();
		// The inherited fields
		IAbstractArtifactInternal parent = getExtends();
		this.inheritedFields = new ArrayList<IField>();
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
	protected void resolveInheritedLiterals() throws TigerstripeException {
		// The inherited labels
		List<IAbstractArtifactInternal> visited = new ArrayList<IAbstractArtifactInternal>();
		IAbstractArtifactInternal parent = getExtends();
		this.inheritedLiterals = new ArrayList<ILiteral>();
		while (parent != null) {
			this.inheritedLiterals.addAll(parent.getLiterals());
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
		List<IAbstractArtifactInternal> visited = new ArrayList<IAbstractArtifactInternal>();
		IAbstractArtifactInternal parent = getExtends();
		this.inheritedMethods = new ArrayList<IMethod>();
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
	@ProvideModelComponents
	public Collection<IMethod> getMethods() {
		return Collections.unmodifiableCollection(this.methods);
	}

	/**
	 * Returns all the non-inherited fields for this Artifact
	 * 
	 * @return Collection of Field - a collection of Fields for this artifact
	 */
	@ProvideModelComponents
	public Collection<IField> getFields() {
		return Collections.unmodifiableCollection(this.fields);
	}

	/**
	 * Returns all the unique datatype of fields for this Artifact
	 * 
	 * @return Collection of Type - a collection of Type for this artifact
	 */
	/*
	 * public Collection getFieldTypes() { if (this.fieldTypes == null)
	 * createUniqueFieldTypeList(); return this.fieldTypes; }
	 */

	public IFieldTypeRef[] getFieldTypes() {
		if (this.fieldTypes == null)
			createUniqueFieldTypeList();
		Collection<IFieldTypeRef> result = this.fieldTypes;
		return result.toArray(new IFieldTypeRef[result.size()]);
	}

	/**
	 * Returns all the non-inherited literals for this Artifact
	 * 
	 * @return Collection of Literals - a collection of Literals for this
	 *         artifact
	 */
	@ProvideModelComponents
	public Collection<ILiteral> getLiterals() {
		return Collections.unmodifiableCollection(this.literals);
	}

	/**
	 * Returns all the inherited fields for this artifact.
	 * 
	 * @return Collection of Field - a collection of Fields for this artifact
	 */
	@ProvideModelComponents
	public Collection<IField> getInheritedFields() {
		// Bug 249956
		// Don't cache this stuff as the parent may have changed!
		// if (inheritedFields == null) {
		try {
			resolveInheritedFields();
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage(
					"While trying to resolve inherited fields for "
							+ getFullyQualifiedName(), e);
			return Collections.EMPTY_LIST;
		}
		// }
		return this.inheritedFields;
	}

	/**
	 * Returns all the inherited literals for this artifact.
	 * 
	 * @return Collection of Literals - a collection of Literals for this
	 *         artifact
	 */
	@ProvideModelComponents
	public Collection<ILiteral> getInheritedLiterals() {
		// Bug 249956
		// Don't cache this stuff as the parent may have changed!
		// if (inheritedLiterals == null) {
		try {
			resolveInheritedLiterals();
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage(
					"While trying to resolved inherited Literals for "
							+ getFullyQualifiedName(), e);
			return Collections.EMPTY_LIST;
		}
		// }
		return this.inheritedLiterals;
	}

	/**
	 * Returns all the inherited methods for this artifact.
	 * 
	 * @return Collection of Method - a collection of Methods for this artifact
	 */
	@ProvideModelComponents
	public Collection<IMethod> getInheritedMethods() {
		// Bug 249956
		// Don't cache this stuff as the parent may have changed!
		// if (inheritedMethods == null) {
		try {
			resolveInheritedMethods();
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage(
					"While trying to resolve inherited Methods for "
							+ getFullyQualifiedName(), e);
			return Collections.EMPTY_LIST;
		}
		// }
		return this.inheritedMethods;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#getExtends()
	 */
	public IAbstractArtifactInternal getExtends() {
		if (extendsArtifact != null && extendsArtifact.isProxy()) {
			// This means it still hasn't been resolved,
			// Let's try again
			resolveExtendedArtifact(null);
		}
		return this.extendsArtifact;
	}

	/**
	 * Returns true if this artifact extends another artifact
	 * 
	 * @return true if this artifacts extends another artifact, false otherwise.
	 */
	public boolean hasExtends() {
		return extendsArtifact != null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#isModelArtifact()
	 */
	public boolean isModelArtifact() {
		return (this instanceof IManagedEntityArtifact
				|| this instanceof IDatatypeArtifact || this instanceof IEnumArtifact);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#isCapabilitiesArtifact()
	 */
	public boolean isCapabilitiesArtifact() {
		return (this instanceof IQueryArtifact
				|| this instanceof IEventArtifact || this instanceof IExceptionArtifact);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#isSessionArtifact()
	 */
	public boolean isSessionArtifact() {
		return (this instanceof ISessionArtifact);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#isDatatypeArtifact()
	 */
	public boolean isDatatypeArtifact() {
		return (this instanceof IDatatypeArtifact);
	}

	protected abstract AbstractArtifactPersister getPersister(Writer writer);

	// =================================================================
	// Methods to satisfy the IAbstractArtifact interface

	public void addField(IField field) {
		this.fields.add(field);
		((Field) field).setContainingArtifact(this);
		this.addContainedModelComponent(field);
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredFields = null;

	}

	public void removeFields(Collection<IField> fields) {
		removeFromMembers(this.fields, fields);
		for (IField field : fields) {
			this.removeContainedModelComponent(field);
		}

		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredFields = null;

	}

	public IField makeField() {
		Field result = new Field(getArtifactManager());
		result.setContainingArtifact(this);
		return result;
	}

	public void setFields(Collection<IField> fields) {
		this.fields.clear();
		this.fields.addAll(fields);
		Collection<IModelComponent> startingComponents = new ArrayList<IModelComponent>();
		for (IModelComponent component : this.getContainedModelComponents()) {
			if (component instanceof IField) {
				startingComponents.add(component);
			}
		}
		for (IModelComponent component : startingComponents) {
			this.removeContainedModelComponent(component);
		}
		for (IField field : fields) {
			((Field) field).setContainingArtifact(this);
			this.addContainedModelComponent(field);

		}
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredFields = null;
	}

	public void addLiteral(ILiteral literal) {
		this.literals.add(literal);
		((Literal) literal).setContainingArtifact(this);
		this.addContainedModelComponent(literal);
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredLiterals = null;
	}

	public void removeLiterals(Collection<ILiteral> literals) {
		removeFromMembers(this.literals, literals);
		for (ILiteral literal : literals) {
			this.removeContainedModelComponent(literal);
		}
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredLiterals = null;

	}

	public ILiteral makeLiteral() {
		Literal result = new Literal(getArtifactManager());
		result.setContainingArtifact(this);
		return result;
	}

	public void setLiterals(Collection<ILiteral> literals) {
		this.literals.clear();
		this.literals.addAll(literals);
		Collection<IModelComponent> startingComponents = new ArrayList<IModelComponent>();
		for (IModelComponent component : this.getContainedModelComponents()) {
			if (component instanceof ILiteral) {
				startingComponents.add(component);
			}
		}
		for (IModelComponent component : startingComponents) {
			this.removeContainedModelComponent(component);
		}

		for (ILiteral literal : literals) {
			((Literal) literal).setContainingArtifact(this);
		}
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredLiterals = null;
	}

	public IMethod makeMethod() {
		Method result = new Method(getArtifactManager());
		result.setContainingArtifact(this);
		return result;
	}

	public void addMethod(IMethod method) {
		this.methods.add(method);
		((Method) method).setContainingArtifact(this);
		this.addContainedModelComponent(method);
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredMethods = null;
	}

	public void removeMethods(Collection<IMethod> methods) {
		removeFromMembers(this.methods, methods);
		for (IMethod method : methods) {
			this.removeContainedModelComponent(method);
		}
		// Bug 1067: need to reset facet fieltered list so it gets re-computed
		// at next "get"
		facetFilteredMethods = null;

	}

	private void removeFromMembers(Collection<? extends IMember> from, Collection<? extends IMember> toRemove) {
		for (IMember m1 : toRemove.toArray(new IMember[0])) {
			if (m1 == null) {
				continue;
			}
			Iterator<? extends IMember> it = from.iterator();
			while (it.hasNext()) {
				IMember m2 = it.next();
				if (m1.equals(m2)) {
					it.remove();
					m2.setContainingArtifact(null);
				}
			}
			m1.setContainingArtifact(null);
		}
	}
	
	public void setMethods(Collection<IMethod> methods) {
		this.methods.clear();
		this.methods.addAll(methods);
		Collection<IModelComponent> startingComponents = new ArrayList<IModelComponent>();
		for (IModelComponent component : this.getContainedModelComponents()) {
			if (component instanceof IMethod) {
				startingComponents.add(component);
			}
		}
		for (IModelComponent component : startingComponents) {
			this.removeContainedModelComponent(component);
		}
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
	public void removeFromExtending(IAbstractArtifact artifact) {
		if (artifact != null
				&& extendingArtifacts
						.contains(new ComparableArtifact(artifact))) {
			extendingArtifacts.remove(new ComparableArtifact(artifact));
		}
	}

	/**
	 * Adding the given artifact to the list of artifacts that are are extending
	 * this.
	 * 
	 * @param artifact
	 * @since 2.2-beta
	 */
	public void addToExtending(IAbstractArtifact artifact) {
		if (artifact != null
				&& !extendingArtifacts
						.contains(new ComparableArtifact(artifact))) {
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
	public Collection<IAbstractArtifact> getExtendingArtifacts() {
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

		return Collections.unmodifiableCollection(result);
	}

	/**
	 * Updates all the artifacts that were extending this to extend the new
	 * artifact instead.
	 * 
	 * @param newArtifact
	 */
	public void updateExtendingArtifacts(IAbstractArtifact newArtifact) {
		Collection<IAbstractArtifact> arts = getExtendingArtifacts();
		for (IAbstractArtifact art : arts) {
			art.setExtendedArtifact(newArtifact);
		}
		clearExtendingArtifacts(); // This shouldn't be necessary as each set
		// above will
		// remove from our list.
	}

	public void setExtendedArtifact(IAbstractArtifact artifact) {
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
			((IAbstractArtifactInternal) artifact).addToExtending(this);
		}

		this.extendsArtifact = (IAbstractArtifactInternal) artifact;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Check validity of the AbstractArtifact's data fields (name, class it
	 * inherits from, fields, labels, and methods) before saving this object to
	 * the underlying Tigerstripe data model
	 * 
	 * @see
	 * org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact#validate()
	 */
	public IStatus validate() {
		MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
				"Artifact Validation", null);

		/*
		 * check the value of the artifact name to ensure it will make a
		 * legitimate class name in the model
		 */
		if (!TigerstripeValidationUtils.classNamePattern.matcher(getName())
				.matches()
				&& !TigerstripeValidationUtils.elementNamePattern.matcher(
						getName()).matches()) {
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(), "'"
					+ getName() + "' is not a legal artifact name"));
		} else if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			result.add(new Status(
					IStatus.ERROR,
					BasePlugin.getPluginId(),
					"'"
							+ getName()
							+ "' is is a reserved keyword and cannot be used as an artifact name"));
		}
		// check the package name to ensure it is a valid package name
		// check to see which object this object extends (if any)
		IAbstractArtifact iaa = getExtendedArtifact();
		if (iaa != null
				&& iaa.getFullyQualifiedName().equals(
						this.getFullyQualifiedName()))
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(),
					"Illegal Inheritance for '" + this.getName()
							+ "'; an object cannot extend itself"));
		// check validity of the fields defined for this artifact
		for (IField field : getFields()) {
			IStatus fieldStatus = field.validate();
			if (!fieldStatus.isOK())
				result.add(fieldStatus);
		}
		// check validity of the literals (constants) defined for this artifact
		for (ILiteral literal : getLiterals()) {
			IStatus literalStatus = literal.validate();
			if (!literalStatus.isOK())
				result.add(literalStatus);
		}
		// check validity of the methods defined for this artifact
		for (IMethod method : getMethods()) {
			IStatus methodStatus = method.validate();
			if (!methodStatus.isOK())
				result.add(methodStatus);
		}
		return result;
	}

	public void doSave(IProgressMonitor monitor) throws TigerstripeException {
		if (monitor == null)
			monitor = new NullProgressMonitor();
		doSave(true, monitor);
	}

	public void doSilentSave(IProgressMonitor monitor)
			throws TigerstripeException {
		if (monitor == null)
			monitor = new NullProgressMonitor();
		doSave(false, monitor);
	}

	/**
	 * Returns the artifact path relative to the project directory
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public String getArtifactPath() throws TigerstripeException {
		if (_artifactPath == null)
			updateArtifactPath();

		return _artifactPath;
	}

	protected void updateArtifactPath() {
		String packageName = getPackage().replace('.', File.separatorChar);
		if (getTSProject() == null || getTSProject().getBaseDir() == null) {
			_artifactPath = null; // this is part of a module
			return;
		}

		try {
			String repoLocation = getTSProject().getRepositoryLocation();
			_artifactPath = repoLocation + File.separator + packageName
					+ File.separator + getName() + ".java";
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
	}

	private void doSave(boolean notify, IProgressMonitor monitor)
			throws TigerstripeException {

		// For now, we need to handle the Phantom project differently
		// as it lives outside of the workspace
		if (getTSProject() instanceof PhantomTigerstripeProject) {
			String baseDir = getTSProject().getBaseDir().toString();
			try {

				// This bit here is needed to create the parent directory
				// for all artifacts in the phantom project (it corresponds
				// to the "createParentIfNeeded()" below.
				String packageName = getPackage().replace('.',
						File.separatorChar);
				String repoLocation = getTSProject().getRepositoryLocation();
				// Make sure the package dir exists
				File dir = new File(baseDir + File.separator + repoLocation
						+ File.separator + packageName);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				Writer writer = new FileWriter(baseDir + File.separator
						+ getArtifactPath());
				AbstractArtifactPersister persister = getPersister(writer);
				persister.applyTemplate();

			} catch (IOException e) {
				throw new TigerstripeException("Error while saving "
						+ getFullyQualifiedName() + ": "
						+ e.getLocalizedMessage(), e);
			}
		} else {
			IProject proj = (IProject) getTSProject()
					.getAdapter(IProject.class);
			String artifactPath = getArtifactPath();
			IFile file = proj.getFile(artifactPath);
			try {
				createParentIfNeeded(file, monitor);

				StringWriter writer = new StringWriter();
				AbstractArtifactPersister persister = getPersister(writer);
				persister.applyTemplate();
				StringBuffer buffer = writer.getBuffer();

				StringBufferInputStream stream = new StringBufferInputStream(
						buffer.toString());
				if (!file.exists())
					file.create(stream, true, monitor);
				else
					file.setContents(stream, true, true, monitor);

				// Check to see if the "parentPackageArtifact" exists
				// and if not create it
				if (!"".equals(getPackage())) {
					IWorkbenchProfile profile = TigerstripeCore
							.getWorkbenchProfileSession().getActiveProfile();
					CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
							.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
					if (prop.getDetailsForType(IPackageArtifact.class.getName())
							.isEnabled()) {
						IAbstractArtifact parent = getProject()
								.getArtifactManagerSession()
								.getArtifactByFullyQualifiedName(getPackage());
						IResource parentResource = null;
						if (parent != null) {
							parentResource = (IResource) parent
									.getAdapter(IResource.class);
						}
						if (parent == null || parentResource == null
								|| !parentResource.exists()) {
							// Better make one
							// System.out.println("Making "+getPackage());
							parent = getProject().getArtifactManagerSession()
									.makeArtifact(
											IPackageArtifact.class.getName());
							parent.setFullyQualifiedName(getPackage());
							parent.doSave(monitor);
						}
					}
				}

			} catch (CoreException e) {
				BasePlugin.log(e);
			}

		}

		// String baseDir = getTSProject().getBaseDir().toString();
		//
		// try {
		// Writer writer = new FileWriter(baseDir + File.separator
		// + getArtifactPath());
		// AbstractArtifactPersister persister = getPersister(writer);
		// persister.applyTemplate();
		//
		// } catch (IOException e) {
		// throw new TigerstripeException("Error while saving "
		// + getFullyQualifiedName() + ": " + e.getLocalizedMessage(),
		// e);
		// }
		//
		if (notify) {
			// This is what will actually update the content of the Artifact Mgr
			// and notify listeners.
			// getArtifactManager().notifyArtifactSaved(this, monitor);
		}
	}

	private void createParentIfNeeded(IResource res, IProgressMonitor monitor) {
		IContainer parent = res.getParent();
		if (!res.exists()) {
			createParentIfNeeded(parent, monitor);
			if (res instanceof IFolder) {
				IFolder folder = (IFolder) res;
				try {
					folder.create(true, true, monitor);
				} catch (CoreException e) {
					BasePlugin.log(e);
				}
			}
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

	public IProjectDescriptor getProjectDescriptor() {
		return getTSProject();
	}

	/**
	 * Returns a Handle for the project if it exists. Please note that for
	 * Artifacts living in a module, this will return NULL;
	 * 
	 * @deprecated use {@link #getProject()} instead
	 */
	@Deprecated
	public ITigerstripeModelProject getTigerstripeProject() {
		TigerstripeProjectHandle handle = null;

		if (getTSProject() == null || getTSProject().getBaseDir() == null)
			return null;

		try {
			handle = (TigerstripeProjectHandle) TigerstripeCore
					.findProject(getTSProject().getBaseDir().toURI());
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e); // FIXME

		}
		return handle;
	}

	/**
	 * Create an abstract artifact from a JavaClass
	 */
	public AbstractArtifact(JavaClass javaClass, ArtifactManager artifactMgr,
			IProgressMonitor monitor) {
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

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#getJavaSource()
	 */
	public JavaSource getJavaSource() {
		return javaSource;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#setJavaSource(com.thoughtworks.qdox.model.JavaSource)
	 */
	public void setJavaSource(JavaSource javaSource) {
		this.javaSource = javaSource;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#getModel()
	 */
	public abstract IAbstractArtifactInternal getModel();

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#getMethodById(java.lang.String)
	 */
	public IMethod getMethodById(String methodId) {
		for (IMethod method : methods) {
			if (method.getMethodId().equals(methodId))
				return method;
		}
		return null;
	}

	// =================================================================
	// Methods to satisfy the IArtifact interface

	@ProvideModelComponents
	public Collection<IField> getFields(boolean filterFacetExcludedFields) {
		Collection<IField> fields = getFields();
		if (filterFacetExcludedFields) {
			if (facetFilteredFields == null) {
				facetFilteredFields = Field.filterFacetExcludedFields(fields);
			}
			return Collections.unmodifiableCollection(facetFilteredFields);
		} else
			return Collections.unmodifiableCollection(this.fields);
	}

	@ProvideModelComponents
	public Collection<IField> getInheritedFields(
			boolean filterFacetExcludedFields) {
		Collection fields = getInheritedFields();
		if (filterFacetExcludedFields) {
			if (facetFilteredInheritedFields == null) {
				facetFilteredInheritedFields = Field
						.filterFacetExcludedFields(fields);
			}
			return Collections
					.unmodifiableCollection(facetFilteredInheritedFields);
		} else
			return Collections
					.unmodifiableCollection(this.getInheritedFields());
	}

	@ProvideModelComponents
	public Collection<ILiteral> getLiterals(boolean filterFacetExcludedLiterals) {
		Collection<ILiteral> literals = getLiterals();
		if (filterFacetExcludedLiterals) {
			if (facetFilteredLiterals == null) {
				facetFilteredLiterals = Literal
						.filterFacetExcludedLiterals(literals);
			}
			return Collections.unmodifiableCollection(facetFilteredLiterals);
		} else
			return Collections.unmodifiableCollection(this.literals);
	}

	@ProvideModelComponents
	public Collection<ILiteral> getInheritedLiterals(
			boolean filterFacetExcludedLiterals) {
		Collection<ILiteral> literals = getInheritedLiterals();
		if (filterFacetExcludedLiterals) {
			if (facetFilteredInheritedLiterals == null) {
				facetFilteredInheritedLiterals = Literal
						.filterFacetExcludedLiterals(literals);
			}
			return Collections
					.unmodifiableCollection(facetFilteredInheritedLiterals);
		} else
			return Collections.unmodifiableCollection(this
					.getInheritedLiterals());
	}

	@ProvideModelComponents
	public Collection<IMethod> getMethods(boolean filterFacetExcludedMethods) {
		Collection methods = getMethods();
		if (filterFacetExcludedMethods) {
			if (facetFilteredMethods == null) {
				facetFilteredMethods = Method
						.filterFacetExcludedMethods(methods);
			}
			return Collections.unmodifiableCollection(facetFilteredMethods);
		} else
			return Collections.unmodifiableCollection(this.methods);
	}

	@ProvideModelComponents
	public Collection<IMethod> getInheritedMethods(
			boolean filterFacetExcludedMethods) {
		Collection methods = getInheritedMethods();
		if (filterFacetExcludedMethods) {
			if (facetFilteredInheritedMethods == null) {
				facetFilteredInheritedMethods = Method
						.filterFacetExcludedMethods(methods);
			}
			return Collections
					.unmodifiableCollection(facetFilteredInheritedMethods);
		} else
			return Collections.unmodifiableCollection(this
					.getInheritedMethods());
	}

	@ProvideModelComponents
	public IAbstractArtifact getExtendedArtifact() {
		return getExtends();
	}

	@ProvideModelComponents
	public Collection<Object> getChildren() {

		Collection<Object> objects = new ArrayList<Object>();

		objects.addAll(getFields());
		objects.addAll(getMethods());
		objects.addAll(getLiterals());
		return Collections.unmodifiableCollection(objects);
	}

	@Override
	public boolean isInActiveFacet() throws TigerstripeException {
		return getArtifactManager().isInActiveFacet(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof IAbstractArtifactInternal) {
			if (Proxy.isProxyClass(obj.getClass())) {
				return obj.equals(this);
			} else {
				return obj == this; // default behavior
			}
		}
		else if (obj instanceof ComparableArtifact) {
			ComparableArtifact other = (ComparableArtifact) obj;
			return other.getArtifact() != null
					&& other.getArtifact().getFullyQualifiedName()
							.equals(getFullyQualifiedName());
		}

		return false;
	}

	@Override
	public int hashCode() {
		String fqn = getFullyQualifiedName();
		if (fqn == null) {
			return 0;
		} else {
			return fqn.hashCode();
		}
	}

	public Collection<IAbstractArtifact> getImplementingArtifacts() {
		return Collections
				.unmodifiableCollection(new ArrayList<IAbstractArtifact>());
	}

	@ProvideModelComponents
	public Collection<IAbstractArtifact> getReferencedArtifacts() {
		Set<IAbstractArtifact> result = new HashSet<IAbstractArtifact>();

		if (hasExtends()) {
			result.add(getExtends());
		}

		for (IField field : getFields()) {
			if (!field.getType().isPrimitive()
					&& !(field.getType().getArtifact() instanceof IPrimitiveTypeArtifact)
					&& field.getType().getArtifact() != null) {
				result.add(field.getType().getArtifact());
			}
		}

		for (IMethod method : getMethods()) {
			if (!method.isVoid()) {
				IType returnType = method.getReturnType();
				if (!returnType.isPrimitive()
						&& !(returnType.getArtifact() instanceof IPrimitiveTypeArtifact)
						&& returnType.getArtifact() != null) {
					result.add(returnType.getArtifact());
				}
			}

			for (IArgument arg : method.getArguments()) {
				IType artType = arg.getType();
				if (!artType.isPrimitive()
						&& !(artType.getArtifact() instanceof IPrimitiveTypeArtifact)
						&& artType.getArtifact() != null) {
					result.add(artType.getArtifact());
				}
			}

			// FIXME : Include Exceptions.
			// FIXME : Include Associations/Dependencies ?

		}
		return Collections.unmodifiableCollection(result);
	}

	public Collection<IAbstractArtifact> getReferencingArtifacts() {
		return Collections
				.unmodifiableCollection(new HashSet<IAbstractArtifact>());
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
		facetFilteredLiterals = null;
		facetFilteredInheritedFields = null;
		facetFilteredInheritedMethods = null;
		facetFilteredInheritedLiterals = null;
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

			IAbstractTigerstripeProject aProject = TigerstripeCore
					.findProject(mgr.getTSProject().getBaseDir().toURI());
			if (aProject instanceof ITigerstripeModelProject) {
				IArtifactManagerSession session = ((ITigerstripeModelProject) aProject)
						.getArtifactManagerSession();
				myUpdater = session.getIModelUpdater();
			} else
				throw new TigerstripeException("Can't figure updater for "
						+ getFullyQualifiedName());

		}

		return myUpdater;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#dispose()
	 */
	public void dispose() {
		// NON SUPPORTED.
	}

	@Override
	@Contextual
	public ITigerstripeModelProject getProject() throws TigerstripeException {
		TigerstripeProject tsProject = getTSProject();
		if (tsProject == null)
			return null;
		return tsProject.getProjectHandle();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter == IResource.class) {
			try {
				return getIResource();
			} catch (TigerstripeException e) {
				// BasePlugin.log(e);
			}
		} else if (adapter == IJavaElement.class) {
			try {
				IResource res = getIResource();
				return JavaCore.create(res);
			} catch (TigerstripeException e) {
				//
			}
		}

		return super.getAdapter(adapter);
	}

	/**
	 * Returns the IResource that this Artifact is saved in
	 * 
	 * This relies on the fact that each artifact is a pojo. This will need to
	 * be updated as we migrate to EMF.
	 * 
	 * @return
	 */
	private IResource getIResource() throws TigerstripeException {
		String artifactPath = getArtifactPath();

		if (artifactPath == null)
			throw new TigerstripeException("Unknown path for "
					+ getFullyQualifiedName()); // this happens for
		// artifacts in modules.

		IProject iProject = (IProject) getProject().getAdapter(IProject.class);
		if (iProject == null)
			// This will happen when considering artifact from Phantom Project
			throw new TigerstripeException("Unknown path for "
					+ getFullyQualifiedName());
		return iProject.findMember(artifactPath);
	}

	// Default implementation is for
	// all Fields, Methods, Literals
	@ProvideModelComponents
	public Collection<IModelComponent> getContainedModelComponents() {
		return containedComponents;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#addContainedModelComponents(java.util.Collection)
	 */
	public void addContainedModelComponents(
			Collection<IModelComponent> components) {
		for (IModelComponent component : components) {
			addContainedModelComponent(component);
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#addContainedModelComponent(org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent)
	 */
	public void addContainedModelComponent(IModelComponent component) {
		// Don't add if its already there.
		if (!containedComponents.contains(component)) {
			containedComponents.add(component);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#clearContainedModelComponents()
	 */
	public void clearContainedModelComponents() {
		containedComponents.clear();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#removeContainedModelComponent(org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent)
	 */
	public void removeContainedModelComponent(IModelComponent component) {
		Iterator<IModelComponent> it = containedComponents.iterator();
		while (it.hasNext()) {
			if (component.equals(it.next())) {
				it.remove();
			}
		}
	}

	@ProvideModelComponents
	public IModelComponent getContainingModelComponent() {
		return this.containingModelComponent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal2#setContainingModelComponent(org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent)
	 */
	public void setContainingModelComponent(IModelComponent containingComponent) {
		this.containingModelComponent = containingComponent;
		return;
	}

	/**
	 * Clones this artifact
	 * 
	 * TODO: this should be extended to fully support the IWorkingCopy interface
	 * at some point...
	 * 
	 * @param monitor
	 * @return
	 * @throws TigerstripeException
	 */
	public IAbstractArtifact makeWorkingCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		if (monitor == null)
			monitor = new NullProgressMonitor();

		String textual = this.asText();
		StringReader reader = new StringReader(textual);
		IAbstractArtifact cloned = getArtifactManager().extractArtifact(reader,
				monitor);
		return cloned;
	}
}