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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.EntityMethodFlavorDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.tags.PropertiesConstants;
import org.eclipse.tigerstripe.workbench.internal.core.model.tags.StereotypeTags;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.internal.core.util.QDoxUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument.EDirection;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjFlavorDefaults;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjMethod;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeListener;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;

/**
 * @author Eric Dillon
 * 
 */
public class Method extends ArtifactComponent implements IOssjMethod {

	private static IAbstractArtifact[] suitableTypes;

	public static IAbstractArtifact[] getSuitableTypes() {
		if (suitableTypes == null)
			loadSuitableTypes();
		return suitableTypes;
	}

	private static void loadSuitableTypes() {
		List<IAbstractArtifact> suitableModelsList = new ArrayList<IAbstractArtifact>();
		suitableModelsList.add(PrimitiveTypeArtifact.MODEL);
		suitableModelsList.add(DatatypeArtifact.MODEL);
		suitableModelsList.add(EnumArtifact.MODEL);
		suitableModelsList.add(ManagedEntityArtifact.MODEL);
		suitableModelsList.add(AssociationClassArtifact.MODEL);

		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

		boolean displayReference = prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEATTRIBUTES_ASREFERENCE);
		if (displayReference) {
			suitableModelsList.add(ExceptionArtifact.MODEL);
			suitableModelsList.add(AssociationArtifact.MODEL);
			suitableModelsList.add(DependencyArtifact.MODEL);
			suitableModelsList.add(QueryArtifact.MODEL);
			suitableModelsList.add(EventArtifact.MODEL);
			suitableModelsList.add(UpdateProcedureArtifact.MODEL);
			suitableModelsList.add(SessionFacadeArtifact.MODEL);
		}

		suitableTypes = suitableModelsList.toArray(new IAbstractArtifact[0]);
	}

	public String getLabel() {
		return "Method";
	}

	private final static String EXPOSED_PROP_ID = "ossj.method";

	private boolean isAbstract;

	private boolean isVoid;

	private boolean isOrdered;

	private boolean isUnique;

	private boolean isIteratorReturn;

	private boolean isOptional;

	private Type returnType;

	private String defaultReturnValue;

	private Collection exceptions;

	private Collection arguments;

	private IModelComponent containingModelComponent;

	private Properties ossjMethodProperties;

	private final ArrayList<IStereotypeInstance> methodReturnStereotypes = new ArrayList<IStereotypeInstance>();

	private String methodReturnName = "";

	// Supported flavors for this method if any
	private OssjEntityMethodFlavor[] supportedFlavors;

	public class ReturnTypeWrapper implements IReturnedType {


		/**
		 * @return the theMethod
		 */
		public IMethod getContainingMethod() {
			return Method.this;
		}

		public void addStereotypeInstance(IStereotypeInstance instance) {
			Method.this.addReturnStereotypeInstance(instance);
		}

		public Collection<IStereotypeInstance> getStereotypeInstances() {
			return Method.this.getReturnStereotypeInstances();
		}

		public void addStereotypeListener(IStereotypeListener listener) {
			Method.this.addStereotypeListener(listener);
		}

		public void removeStereotypeListener(IStereotypeListener listener) {
			Method.this.removeStereotypeListener(listener);
		}

		public IStereotypeInstance getStereotypeInstanceByName(String name) {
			for (IStereotypeInstance inst : methodReturnStereotypes) {
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

		public void removeStereotypeInstance(IStereotypeInstance instance) {
			Method.this.removeReturnStereotypeInstance(instance);
		}

		public void removeStereotypeInstances(
				Collection<IStereotypeInstance> instances) {
			Method.this.removeReturnStereotypeInstances(instances);
		}

		public Object getAnnotation(String annotationSpecificationID) {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method return"));
			return null;
		}

		public List<Object> getAnnotations() {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method return"));
			return null;
		}

		public List<Object> getAnnotations(String annotationSpecificationID) {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method return"));
			return null;
		}

		public List<Object> getAnnotations(Class<?> clazz) {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method return"));
			return null;
		}

		public boolean hasAnnotations() {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method return"));
			return false;
		}

		public boolean hasAnnotations(String annotationSpecificationID) {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method return"));
			return false;
		}

		public boolean hasAnnotations(Class<?> clazz) {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method return"));
			return false;
		}

		public String getStereotypeString() {
			return getStereotypesAsString(ReturnTypeWrapper.this);
		}

		public Object getAnnotationByID(String annotationID) {
			BasePlugin.log(new TigerstripeException(
			"Annotations not supported on Method return"));
			return null;
		}

		public boolean hasAnnotationWithID(String annotationID) {
			BasePlugin.log(new TigerstripeException(
			"Annotations not supported on Method return"));
			return false;
		}

	}

	public String getDefaultReturnValue() {
		return this.defaultReturnValue;
	}

	public void setDefaultReturnValue(String defaultReturnValue) {
		this.defaultReturnValue = defaultReturnValue;
	}

	public boolean isUnique() {
		return this.isUnique;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

	public boolean isOrdered() {
		return this.isOrdered;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public Method(ArtifactManager artifactMgr) {
		super(artifactMgr);
		this.arguments = new ArrayList();
		this.exceptions = new ArrayList();
		this.ossjMethodProperties = new Properties();

		// By default, a method is considered a custom method for now
		// If it is not, the values shall be overriden directly
		setSupportedFlavors(IOssjFlavorDefaults.customMethodFlavors);

		setDefaultFlavors();
		setDefaultProperties();
	}

	public int getReturnRefBy() {
		String refByStr = getOssjMethodProperties().getProperty("returnRefBy",
				"value");
		for (int i = 0; i < IField.refByLabels.length; i++) {
			if (IField.refByLabels[i].equalsIgnoreCase(refByStr.trim()))
				return i;
		}
		return IField.NON_APPLICABLE;
	}

	public boolean isReturnRefByKey() {
		return getReturnRefBy() == IField.REFBY_KEY;
	}

	public boolean isReturnRefByValue() {
		return getReturnRefBy() == IField.REFBY_VALUE;
	}

	public boolean hasDefaultReturnValue() {
		return defaultReturnValue != null;
	}

	public boolean isReturnRefByKeyResult() {
		return getReturnRefBy() == IField.REFBY_KEYRESULT;
	}

	public String getReturnRefByString() {
		if (getReturnRefBy() == IField.NON_APPLICABLE)
			return "";
		return IField.refByLabels[getReturnRefBy()];
	}

	public void setReturnRefBy(int refBy) {
		getOssjMethodProperties().setProperty("returnRefBy",
				IField.refByLabels[refBy]);
	}

	public void setInstanceMethod(boolean instance) {
		getOssjMethodProperties().setProperty(
				OssjMethodProperty.INSTANCEMETHOD.getPojoLabel(),
				String.valueOf(instance));
	}

	public boolean isInstanceMethod() {
		return "true".equalsIgnoreCase(getOssjMethodProperties().getProperty(
				OssjMethodProperty.INSTANCEMETHOD.getPojoLabel()));
	}

	public Properties getOssjMethodProperties() {
		return this.ossjMethodProperties;
	}

	public void setOssjMethodProperties(Properties prop) {
		this.ossjMethodProperties = prop;
	}

	public Method(JavaMethod method, ArtifactManager artifactMgr) {
		this(artifactMgr);
		this.arguments = new ArrayList();
		this.exceptions = new ArrayList();
		this.ossjMethodProperties = new Properties();
		buildModel(method);
	}

	public void setReturnName(String methodReturnName) {
		this.methodReturnName = methodReturnName;
	}

	public String getReturnName() {
		return this.methodReturnName;
	}

	public Collection<IStereotypeInstance> getReturnStereotypeInstances() {
		return Collections.unmodifiableCollection(methodReturnStereotypes);
	}

	public IStereotypeInstance getReturnStereotypeInstanceByName(String name) {
		for (IStereotypeInstance inst : methodReturnStereotypes) {
			if (inst.getName().equals(name)) {
				return inst;
			}
		}
		return null;
	}

	public boolean hasReturnStereotypeInstance(String name) {
		IStereotypeInstance inst = getReturnStereotypeInstanceByName(name);
		if (inst == null)
			return false;
		else
			return true;
	}

	public void addReturnStereotypeInstance(IStereotypeInstance instance) {
		if (!methodReturnStereotypes.contains(instance)) {
			methodReturnStereotypes.add(instance);
		}
	}

	public void addReturnStereotypeInstances(IStereotypeInstance[] instances) {
		for (IStereotypeInstance instance : instances) {
			addReturnStereotypeInstance(instance);
		}
	}

	public void removeReturnStereotypeInstance(IStereotypeInstance instance) {
		if (methodReturnStereotypes.contains(instance)) {
			methodReturnStereotypes.remove(instance);
		}
	}

	public void removeReturnStereotypeInstances(
			Collection<IStereotypeInstance> instances) {
		for (IStereotypeInstance instance : instances) {
			removeReturnStereotypeInstance(instance);
		}
	}

	@ProvideModelComponents
	public Collection<IArgument> getArguments() {
		return Collections.unmodifiableCollection(this.arguments);
	}

	private void buildModel(JavaMethod method) {

		// Get the comment first
		setComment(xmlEncode.decode(method.getComment()));

		// Then the name of the method
		setName(method.getName());

		// Build the tag list for this method
		DocletTag[] tags = method.getTags();
		for (int i = 0; i < tags.length; i++) {
			Tag tag = new Tag(tags[i]);
			addTag(tag);
		}

		// Extract method tag stuff
		Tag methodTag = getFirstTagByName(AbstractArtifactTag.PREFIX
				+ AbstractArtifactTag.METHOD);

		String typeMultiplicity = null;
		if (methodTag != null) {
			Properties props = methodTag.getProperties();
			String iterRetStr = props.getProperty("iteratorReturn", "false");
			this.isIteratorReturn = "true".equalsIgnoreCase(iterRetStr);
			String optionalStr = props.getProperty("isOptional", "false");
			this.isOptional = "true".equalsIgnoreCase(optionalStr);
			String abstractStr = props.getProperty("isAbstract", "false");
			this.isAbstract = "true".equalsIgnoreCase(abstractStr);
			String orderedStr = props.getProperty("isOrdered", "false");
			this.isOrdered = "true".equalsIgnoreCase(orderedStr);
			String uniqueStr = props.getProperty("isUnique", "false");
			this.isUnique = "true".equalsIgnoreCase(uniqueStr);

			typeMultiplicity = props.getProperty("typeMultiplicity", null);
			String defaultRet = props.getProperty("defaultReturnValue", null);
			if (defaultRet != null) {
				this.setDefaultReturnValue(xmlEncode.decode(defaultRet));
			}

			// Set visibility
			// Since #233884 visibility is stored in a tag rather than
			// using the method modifier in the pojo because private abstract
			// didn't make sense
			// to the java compiler
			String tagVisibility = props.getProperty("visibility", "unset");
			if ("unset".equals(tagVisibility)) {
				setVisibility(method.getModifiers());
			} else {
				setVisibility(new String[] { tagVisibility });
			}

			// Since 2.2.1 extract return type name
			this.methodReturnName = props.getProperty("returnName", "");
		}

		com.thoughtworks.qdox.model.Type type = method.getReturns();
		// Bug 272: we were forcing QDox to resolve the type by a call to
		// getJavaClass
		// although it was only to get access to the fullyQualifiedName.
		// This same FQN is available directly with type.getValue() which
		// doesn't trigger
		// the type resolution
		// TigerstripeRuntime.logInfoMessage("Value= " + type.getValue() + " - "
		// + type.getJavaClass().getFullyQualifiedName());
		
		String returnTypeName = QDoxUtils.getTypeName(type);
		this.returnType = new Type(returnTypeName, EMultiplicity.ONE,
				getArtifactManager());
		if ("void".equals(returnTypeName)) {
			isVoid = true;
		} else {
			if (typeMultiplicity != null) {
				// setting proper type multiplicity is present (i.e. otherwise
				// model is prior to 2.2rc)
				this.returnType
						.setTypeMultiplicity(IModelComponent.EMultiplicity
								.parse(typeMultiplicity));
			}
		}

		// Since 2.2.1 extract Stereotypes on return
		methodReturnStereotypes.addAll(Arrays.asList(StereotypeTags
				.getAllReturnStereotypes(getTags())));

		// get the arguments
		this.arguments = new ArrayList();
		JavaParameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			
			com.thoughtworks.qdox.model.Type ptype = parameters[i].getType();
			String paramTypeName = QDoxUtils.getTypeName(ptype);

			Type argType = new Type(paramTypeName,
					EMultiplicity.ONE, getArtifactManager());
			Method.Argument arg = new Method.Argument(this, parameters[i]
					.getName(), argType);
			this.arguments.add(arg);
		}

		// Since 2.2.1: extract stereotypes on arguments
		StereotypeTags.annotateArguments(arguments, getTags());

		// extract extra info on arguments if present (ref-by)
		Collection argTags = getTagsByName("tigerstripe.method-arg");
		for (Iterator tagIter = argTags.iterator(); tagIter.hasNext();) {
			Tag tag = (Tag) tagIter.next();
			Properties prop = tag.getProperties();
			String argName = prop.getProperty("name");
			String refBy = (prop.getProperty("ref-by", "value")).toLowerCase();
			String defaultValue = prop.getProperty("defaultValue", null);
			boolean isOrdered = "true".equalsIgnoreCase(prop.getProperty(
					"isOrdered", "false"));
			boolean isUnique = "true".equalsIgnoreCase(prop.getProperty(
					"isUnique", "true"));
			String argMultiplicity = prop.getProperty("typeMultiplicity", null);
			String refCommentId = prop.getProperty("refComment");
			String directionStr = prop.getProperty("direction", "in");

			IArgument arg = getArgumentByName(argName);
			if (arg != null) {
				arg.setRefBy(Field.refByFromLabel(refBy));
				if (refCommentId != null) {
					((Argument) arg).setRefCommentId(refCommentId);
				}
				if (defaultValue != null) {
					arg.setDefaultValue(xmlEncode.decode(defaultValue));
				}
				arg.setOrdered(isOrdered);
				arg.setUnique(isUnique);
				if (argMultiplicity != null) {
					arg.getType().setTypeMultiplicity(
							IModelComponent.EMultiplicity
									.parse(argMultiplicity));
				}
				arg.setDirection(EDirection.parse(directionStr));
			}
		}

		this.exceptions = new ArrayList();
		com.thoughtworks.qdox.model.Type[] exceptionTypes = method
				.getExceptions();
		for (int i = 0; i < exceptionTypes.length; i++) {
			Method.Exception exception = new Method.Exception(this,
					QDoxUtils.getTypeName(exceptionTypes[i]));
			this.exceptions.add(exception);
		}

		// Extract the method properties
		try {
			this.ossjMethodProperties = PropertiesConstants.getPropertiesById(
					getTags(), EXPOSED_PROP_ID);
		} catch (TigerstripeException e) {
			// ignore exception.
			// this means the POJO did not have any of the method options
			// embedded in the comment. Anyway, the validateProperties()
			// method in the finally clause, makes sure the default
			// values are added anyway.
		} finally {
			setDefaultFlavors();
			setDefaultProperties();
		}

		// Extract all the stereotypes
		extractStereotypes();

	}

	public IArgument getArgumentByName(String argName) {
		if (argName == null || arguments == null)
			return null;

		for (Iterator iter = this.arguments.iterator(); iter.hasNext();) {
			IArgument arg = (IArgument) iter.next();
			if (argName.equals(arg.getName()))
				return arg;
		}
		return null;
	}

	public void setDefaultFlavors() {
		OssjEntityMethodFlavor[] sup = getSupportedFlavors();
		for (OssjEntityMethodFlavor flavor : sup) {
			if (getOssjMethodProperties().getProperty(flavor.getPojoLabel()) == null) {
				getOssjMethodProperties().setProperty(flavor.getPojoLabel(),
						OssjEntitySpecifics.getFlavorDefaults(sup, flavor));
			}
		}
	}

	/**
	 * Makes sure all the properties have a default value for this
	 * 
	 */
	protected void setDefaultProperties() {
		String val = getOssjMethodProperties().getProperty(
				IOssjMethod.OssjMethodProperty.INSTANCEMETHOD.getPojoLabel());
		if (val == null) {
			setInstanceMethod(true);
		}
	}

	public boolean hasExceptions() {
		return this.exceptions.size() != 0;
	}

	public boolean hasArguments() {
		return this.arguments.size() != 0;
	}

	public Collection<IException> getExceptions() {
		return Collections.unmodifiableCollection(this.exceptions);
	}

	/**
	 * An argument of a method definition
	 * 
	 * @author Eric Dillon
	 */
	public class Argument implements IArgument {

		/** the stereotypes attached to this component */
		private final ArrayList<IStereotypeInstance> stereotypeInstances = new ArrayList<IStereotypeInstance>();

		/** the stereotype listeners attached to this component */
		private final ArrayList<IStereotypeListener> stereotypeListeners = new ArrayList<IStereotypeListener>();

		private String name;

		private String defaultValue;

		private String refBy;

		private Type type;

		private Method parentMethod;

		private String refCommentId;

		private boolean isUnique;

		private boolean isOrdered;

		private EDirection direction;

		public boolean isUnique() {
			return this.isUnique;
		}

		public void setUnique(boolean isUnique) {
			this.isUnique = isUnique;
		}

		public boolean isOrdered() {
			return this.isOrdered;
		}

		public void setOrdered(boolean isOrdered) {
			this.isOrdered = isOrdered;
		}

		public Argument(Method parentMethod) {
			setParentMethod(parentMethod);
		}

		public void setParentMethod(Method parentMethod) {
			this.parentMethod = parentMethod;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public String getDefaultValue() {
			return this.defaultValue;
		}

		public boolean hasDefaultValue() {
			return defaultValue != null;
		}

		public void setRefCommentId(String refCommentId) {
			this.refCommentId = refCommentId;
		}

		public String getRefCommentId() {
			return this.refCommentId;
		}

		public Method getParentMethod() {
			return this.parentMethod;
		}

		public String getComment() {
			if (getRefCommentId() != null) {
				IAbstractArtifactInternal art = (IAbstractArtifactInternal) parentMethod
						.getContainingArtifact();
				RefComment rComment = art.getRefCommentById(getRefCommentId());
				if (rComment != null)
					return xmlEncode.decode(rComment.getContent());
			}
			return "";
		}

		public void setComment(String description) {

			if ((description == null || description.length() == 0)
					&& getRefCommentId() == null)
				return;

			IAbstractArtifactInternal art = (IAbstractArtifactInternal) parentMethod
					.getContainingArtifact();
			if (getRefCommentId() == null) {
				setRefCommentId(art.getUniqueRefCommentId());
			}
			RefComment rComment = new RefComment(art);
			rComment.setLabel(refCommentId);
			rComment.setContent(description);
			art.setRefComment(rComment);
		}

		public void setRefBy(int refBy) {
			if (refBy != IField.NON_APPLICABLE) {
				this.refBy = IField.refByLabels[refBy];
			}
		}

		public int getRefBy() {
			for (int i = 0; i < IField.refByLabels.length; i++) {
				if (IField.refByLabels[i].equalsIgnoreCase(this.refBy))
					return i;
			}
			return IField.NON_APPLICABLE;
		}

		public boolean isRefByValue() {
			return IField.refByLabels[IField.REFBY_VALUE]
					.equalsIgnoreCase(this.refBy);
		}

		public boolean isRefByKey() {
			return IField.refByLabels[IField.REFBY_KEY]
					.equalsIgnoreCase(this.refBy);
		}

		public boolean isRefByKeyResult() {
			return IField.refByLabels[IField.REFBY_KEYRESULT]
					.equalsIgnoreCase(this.refBy);
		}

		public String getRefByString() {
			if (getRefBy() == IField.NON_APPLICABLE)
				return "";
			return IField.refByLabels[getRefBy()];
		}

		public String getSignature() {
			return type.getFullyQualifiedName()
					+ (type.getTypeMultiplicity().isArray() ? "[]" : "") + " "
					+ getName();
		}

		public Argument(Method parentMethod, String name, Type type) {
			this(parentMethod);
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setType(Type type) {
			this.type = type;
		}

		@ProvideModelComponents
		public IType getType() {
			return this.type;
		}

		public void setType(IType type) {
			setType((Type) type);
		}

		@ProvideModelComponents
		public IMethod getContainingMethod() {
			return this.parentMethod;
		}

		@ProvideModelComponents
		public IAbstractArtifact getContainingArtifact() {
			return this.parentMethod
					.getContainingArtifact();
		}

		// ======================================================================
		// ===
		// Stereotype handling

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

		public void addStereotypeInstances(
				Collection<IStereotypeInstance> instances) {
			for (IStereotypeInstance instance : instances) {
				addStereotypeInstance(instance);
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

		public void removeStereotypeInstances(
				Collection<IStereotypeInstance> instances) {
			for (IStereotypeInstance instance : instances) {
				removeStereotypeInstance(instance);
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

		@Override
		public IArgument clone() {
			IArgument result = new Argument(this.getParentMethod());
			result.setComment(getComment());
			result.setDefaultValue(getDefaultValue());
			result.setName(getName());
			result.setType(getType().clone());
			result.setOrdered(isOrdered());
			result.setRefBy(getRefBy());
			result.setUnique(isUnique());
			result.setDirection(getDirection());

			Collection<IStereotypeInstance> stereotypeInstances = getStereotypeInstances();
			for (IStereotypeInstance inst : stereotypeInstances) {
				result.addStereotypeInstance(inst);
			}

			return result;
		}

		public Object getAnnotation(String annotationSpecificationID) {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method Arguments"));
			return null;
		}

		public List<Object> getAnnotations() {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method Arguments"));
			return null;
		}

		public List<Object> getAnnotations(String annotationSpecificationID) {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method Arguments"));
			return null;
		}

		public List<Object> getAnnotations(Class<?> schemeID) {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method Arguments"));
			return null;
		}

		public boolean hasAnnotations() {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method Arguments"));
			return false;
		}

		public boolean hasAnnotations(String annotationSpecificationID) {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method Arguments"));
			return false;
		}

		public boolean hasAnnotations(Class<?> schemeID) {
			BasePlugin.log(new TigerstripeException(
					"Annotations not supported on Method Arguments"));
			return false;
		}

		public String getStereotypeString() {
			return getStereotypesAsString(Argument.this);
		}

		public EDirection getDirection() {
			if (direction == null)
				setDirection(EDirection.INOUT);
			return direction;
		}

		public void setDirection(EDirection direction) {
			this.direction = direction;
		}

		public URI toURI() throws TigerstripeException {
			URI u = TigerstripeURIAdapterFactory.toURI(this);
			return u;
		}

		public Object getAnnotationByID(String annotationID) {
			BasePlugin.log(new TigerstripeException(
			"Annotations not supported on Method Arguments"));
			return null;
		}

		public boolean hasAnnotationWithID(String annotationID) {
			BasePlugin.log(new TigerstripeException(
			"Annotations not supported on Method Arguments"));
			return false;
		}

	}

	public class Exception implements IException {
		private String fullyQualifiedName;
		private IMethod parentMethod;

		public void setContainingMethod(IMethod parentMethod) {
			this.parentMethod = parentMethod;
		}

		public IMethod getContainingMethod() {
			return parentMethod;
		}

		public String getFullyQualifiedName() {
			return this.fullyQualifiedName;
		}

		public String getPackage() {
			return Util.packageOf(getFullyQualifiedName());
		}

		public String getName() {
			return Util.nameOf(getFullyQualifiedName());
		}

		public void setFullyQualifiedName(String fullyQualifiedName) {
			this.fullyQualifiedName = fullyQualifiedName;
		}

		public Exception(IMethod parentMethod, String fullyQualifiedName) {
			setContainingMethod(parentMethod);
			this.fullyQualifiedName = fullyQualifiedName;
		}

		public IStatus validate() {

			MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
					ArtifactMetadataFactory.INSTANCE.getMetadata(
							IExceptionArtifactImpl.class.getName()).getLabel(
							this)
							+ " validation", null);

			// check the fully qualified name of the exception to ensure
			// that it is a valid Java name, do this by checking both the
			// class name and the package name that make up the fully qualified
			// name of this IException, first check the classname
			String className = Util.nameOf(getFullyQualifiedName());
			// check the class name of the IException; it should be a valid
			// class name in Java, otherwise the name cannot be used as a
			// legal type
			if (!TigerstripeValidationUtils.classNamePattern.matcher(className)
					.matches()
					&& !TigerstripeValidationUtils.elementNamePattern.matcher(
							className).matches())
				result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(),
						"'"
								+ getName()
								+ "' is not a legal "
								+ ArtifactMetadataFactory.INSTANCE.getMetadata(
										IExceptionArtifactImpl.class.getName())
										.getLabel(this) + " name"));
			// check class name to ensure it is not a reserved keyword
			else if (TigerstripeValidationUtils.keywordList.contains(className)) {
				result
						.add(new Status(
								IStatus.ERROR,
								BasePlugin.getPluginId(),
								"'"
										+ getName()
										+ "' is a reserved keyword and cannot be used as "
										+ ArtifactMetadataFactory.INSTANCE
												.getMetadata(
														IExceptionArtifactImpl.class
																.getName())
												.getLabel(this) + " name"));
			}

			// check the IException package to ensure that it is a legal package
			// name
			String packageName = Util.packageOf(getFullyQualifiedName());
			if (!TigerstripeValidationUtils.packageNamePattern.matcher(
					packageName).matches())
				result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(),
						"'"
								+ packageName
								+ "' is not a legal "
								+ ArtifactMetadataFactory.INSTANCE.getMetadata(
										IExceptionArtifactImpl.class.getName())
										.getLabel(this) + " package name"));
			else if (TigerstripeValidationUtils.keywordList
					.contains(packageName)) {
				result
						.add(new Status(
								IStatus.ERROR,
								BasePlugin.getPluginId(),
								"'"
										+ packageName
										+ "' is a reserved keyword and cannot be used as an "
										+ ArtifactMetadataFactory.INSTANCE
												.getMetadata(
														IExceptionArtifactImpl.class
																.getName())
												.getLabel(this)
										+ " package name"));
			}

			return result;
		}

		@Override
		public IException clone() {
			IException result = new Exception(getContainingMethod(),
					getFullyQualifiedName());
			return result;
		}
	}

	public void setContainingArtifact(IAbstractArtifact artifact) {
		this.containingModelComponent = artifact;
	}

	@ProvideModelComponents
	public IAbstractArtifact getContainingArtifact() {
		if (this.containingModelComponent instanceof IAbstractArtifact)
			return (IAbstractArtifact) this.containingModelComponent;
		else
			return null;
	}

	public Collection<IModelComponent> getContainedModelComponents() {
		// Fields don't contain anything
		return Collections
				.unmodifiableCollection(new ArrayList<IModelComponent>());
	}

	public void addContainedModelComponent(IModelComponent component)
			throws TigerstripeException {
		throw new TigerstripeException("Methods cannot contain any Components");
	}

	public void addContainedModelComponents(
			Collection<IModelComponent> components) throws TigerstripeException {
		throw new TigerstripeException("Methods cannot contain any Components");
	}

	public void removeContainedModelComponent(IModelComponent component) {
		return;
	}

	@ProvideModelComponents
	public IModelComponent getContainingModelComponent() {
		if (this.containingModelComponent instanceof IAbstractArtifact)
			return this.containingModelComponent;
		else
			return null;
	}

	public void setContainingModelComponent(IModelComponent containingComponent)
			throws TigerstripeException {
		if (containingComponent instanceof IAbstractArtifact)
			this.containingModelComponent = containingComponent;
		else
			throw new TigerstripeException(
					"Methods can only be contained by Artifacts");
	}

	// =======================================================================
	// Methods to satisfy the IMethod interface

	public void setReturnType(IType type) {
		this.returnType = (Type) type;
		if (type == null || "void".equals(type.getFullyQualifiedName())) {
			isVoid = true;
		} else {
			isVoid = false;
		}

	}

	@ProvideModelComponents
	public IType getReturnType() {
		return this.returnType;
	}

	public IType makeType() {
		return new Type(getArtifactManager());
	}

	public String getLabelString() {
		return getLabelString(true);
	}

	public String getLabelString(boolean includeArgStereotypes) {
		IArgumentLabelFormatter aaFormatter = null;
		if (includeArgStereotypes) {
			aaFormatter = new DefaultArgumentStereotypesFormatter();
		}
		return getLabelString(aaFormatter);
	}

	public String getLabelString(
			IArgumentLabelFormatter annotationsFormatter) {
		StringBuilder result = new StringBuilder();
		result.append(getName()).append("(")
				.append(getParamsAsString(annotationsFormatter)).append("):");

		if (isVoid()) {
			result.append("void");
		} else {
			result.append(getReturnType().getName());
			if (getReturnType().getTypeMultiplicity() != IModelComponent.EMultiplicity.ONE) {
				result.append("[")
						.append(getReturnType().getTypeMultiplicity()
								.getLabel()).append("]");
			}
			if (getDefaultReturnValue() != null) {
				result.append("=");
				if (getDefaultReturnValue().length() == 0) {
					result.append("\"\"");
				} else {
					result.append(getDefaultReturnValue());
				}
			}
		}

		return result.toString();
	}

	private String getParamsAsString(
			IArgumentLabelFormatter argumentFormatter) {

		GlobalSettingsProperty propG = (GlobalSettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.GLOBAL_SETTINGS);
		boolean displayDirection = propG
				.getPropertyValue(IGlobalSettingsProperty.ARGUMENTDIRECTION);

		// get the list of method parameters from method
		int numParams = getArguments().size();
		// if the number of arguments is zero, just return an empty string
		if (numParams == 0)
			return "";
		// else, append the parameter types to form a list of types for the
		// method signature
		StringBuilder result = new StringBuilder();
		int paramCount = 0;
		for (IArgument argument : getArguments()) {
			String argumentLabel = getArgumentLabel(argument, displayDirection);
			if (argumentFormatter != null) {
				argumentLabel = argumentFormatter.getArgumentLabel(argument,
						argumentLabel);
			}
			result.append(argumentLabel);

			if (++paramCount < numParams)
				result.append(", ");
		}
		return result.toString();
	}

	private String getArgumentLabel(IArgument argument, boolean displayDirection) {
		StringBuilder result = new StringBuilder();

		if (displayDirection) {
			result.append(argument.getDirection().getLabel()).append(" ");
		}

		String paramString = Util.nameOf(argument.getType()
				.getFullyQualifiedName());
		result.append(Misc.removeJavaLangString(paramString));

		if (argument.getType().getTypeMultiplicity() != IModelComponent.EMultiplicity.ONE) {
			result.append("[")
					.append(argument.getType().getTypeMultiplicity().getLabel())
					.append("]");
		}

		if (argument.getDefaultValue() != null) {
			result.append("=");
			if (argument.getDefaultValue().length() == 0) {
				result.append("\"\"");
			} else {
				result.append(argument.getDefaultValue());
			}
		}
		return result.toString();
	}

	public IArgument makeArgument() {
		return new Argument(this);
	}

	public void addArgument(IArgument argument) {
		arguments.add(argument);
		((Argument) argument).setParentMethod(this);
	}

	public void removeArguments(Collection<IArgument> arguments) {
		this.arguments.removeAll(arguments);
	}

	public void setArguments(Collection<IArgument> arguments) {
		this.arguments = arguments;
		for (Argument arg : (Collection<Argument>) this.arguments) {
			arg.setParentMethod(this);
		}
	}

	public boolean isVoid() {
		return isVoid;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public void setOptional(boolean optional) {
		this.isOptional = optional;
	}

	public boolean isOptional() {
		return this.isOptional;
	}

	public IException makeException() {
		return new Method.Exception(this, "java.lang.Exception");
	}

	public void setExceptions(Collection<IException> exceptions) {
		this.exceptions = exceptions;
		for (IException exp : exceptions) {
			((Exception) exp).setContainingMethod(this);
		}
	}

	public void addException(IException exception) {
		this.exceptions.add(exception);
		((Exception) exception).setContainingMethod(this);
	}

	public void removeExceptions(Collection<IException> exceptions) {
		this.exceptions.removeAll(exceptions);
		for (IException exp : exceptions) {
			((Exception) exp).setContainingMethod(null);
		}
	}

	public String getMethodId() {
		String result = getReturnType().getFullyQualifiedName() + ":"
				+ getName() + "(";

		Iterator<IArgument> argIterator = getArguments().iterator();
		while (argIterator.hasNext()) {
			IArgument arg = argIterator.next();

			result = result + arg.getType().getFullyQualifiedName();
			if (argIterator.hasNext())
				result = result + ",";
		}

		result = result + ")";
		return result;
	}

	public void setSupportedFlavors(OssjEntityMethodFlavor[] supportedFlavors) {
		this.supportedFlavors = supportedFlavors;
	}

	public OssjEntityMethodFlavor[] getSupportedFlavors() {
		return this.supportedFlavors;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		String methodId = getMethodId();
		result = prime * result + ((methodId == null) ? 0 : methodId.hashCode());
		result = prime * result
				+ ((returnType == null) ? 0 : returnType.hashCode());
		result = prime
		* result
		+ ((containingModelComponent == null) ? 0
				: containingModelComponent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (Proxy.isProxyClass(obj.getClass())) {
			return obj.equals(this);
		}
		if (!(obj instanceof Method)) {
			return false;
		}
		Method other = (Method) obj;
		if (!other.getMethodId().equals(getMethodId())) {
			return false;
		}
		if (containingModelComponent == null) {
			if (other.containingModelComponent != null) {
				return false;
			}
		} else if (!containingModelComponent
				.equals(other.containingModelComponent))
			return false;
		if (returnType == null) {
			if (other.returnType != null) {
				return false;
			}
		} else if (!returnType.equals(other.returnType)) {
			return false;
		}
		return true;
	}

	/**
	 * Clones the method into an IOssjMethod that contains the same details,
	 * including all OSS/J Flavors
	 * 
	 */
	public IOssjMethod cloneToIOssjMethod() {
		Method result = new Method(getArtifactManager());
		result.setSupportedFlavors(getSupportedFlavors());
		result.setName(getName());
		result.setComment(getComment());
		result.setArguments(getArguments());
		result.setReturnType(getReturnType());
		result.setExceptions(getExceptions());
		result.setOssjMethodProperties((Properties) getOssjMethodProperties()
				.clone());
		result.setContainingArtifact(getContainingArtifact());
		return result;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public EntityMethodFlavorDetails getFlavorDetails(
			OssjEntityMethodFlavor flavor) {
		String pojoFlavorDetails = getOssjMethodProperties().getProperty(
				flavor.getPojoLabel());
		if (pojoFlavorDetails != null)
			return new EntityMethodFlavorDetails(
					getContainingArtifact(),
					pojoFlavorDetails);
		return null;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public void setFlavorDetails(OssjEntityMethodFlavor flavor,
			EntityMethodFlavorDetails details) {
		getOssjMethodProperties().setProperty(flavor.getPojoLabel(),
				details.toString());
	}

	public boolean isIteratorReturn() {
		return this.isIteratorReturn;
	}

	public void setIteratorReturn(boolean iterate) {
		this.isIteratorReturn = iterate;
	}

	public Properties getAnnotationProperties(String annotation) {
		// Read the tigerstripe tag
		Tag intfTag = getFirstTagByName(annotation);
		if (intfTag != null) {
			Properties prop = intfTag.getProperties();
			return prop;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * used to validate the name, return type, parameter names, and parameter
	 * return types for this method definition when saving it to the underlying
	 * data model
	 * 
	 * @see org.eclipse.tigerstripe.api.artifacts.model.IMethod#validate()
	 */
	public IStatus validate() {

		MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
				"Method validation", null);

		// check method name to ensure that it is a valid method name in Java
		if (!TigerstripeValidationUtils.elementNamePattern.matcher(getName())
				.matches()) {
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(), "'"
					+ getName() + "' is not a valid method name"));
		}
		// check method name to ensure it is not a reserved keyword
		else if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			result
					.add(new Status(
							IStatus.ERROR,
							BasePlugin.getPluginId(),
							"method named '"
									+ getName()
									+ "' is a reserved keyword and cannot be used as method name"));
		}

		// check the validity of the method return type
		boolean isMethodReturnCheck = true;
		IStatus returnTypeStatus = getReturnType()
				.validate(isMethodReturnCheck);
		if (!returnTypeStatus.isOK())
			result.add(returnTypeStatus);

		// check the validity of the method parameter names and types
		Collection<IArgument> argList = getArguments();
		Iterator iter = argList.iterator();
		while (iter.hasNext()) {

			Argument arg = (Argument) iter.next();
			// check parameter name to ensure that it is a valid field name in
			// Java
			if (!TigerstripeValidationUtils.elementNamePattern.matcher(
					arg.getName()).matches()) {
				result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(),
						"'" + getName() + "::" + arg.getName()
								+ "' is not a valid method name"));
			}
			// check parameter name to ensure it is not a reserved keyword
			else if (TigerstripeValidationUtils.keywordList.contains(arg
					.getName())) {
				result
						.add(new Status(
								IStatus.ERROR,
								BasePlugin.getPluginId(),
								"'"
										+ getName()
										+ "::"
										+ arg.getName()
										+ "' is a reserved keyword and cannot be used as method name"));
			}

			// check the parameter type to ensure it is a valid data type
			IStatus typeStatus = arg.getType().validate();
			if (!typeStatus.isOK())
				result.addAll(typeStatus);

		}

		// check exceptions to ensure that they are valid class names
		for (IException exc : getExceptions()) {
			IStatus s = exc.validate();
			if (!s.isOK())
				result.addAll(s);
		}

		return result;

	}

	public IEntityMethodFlavorDetails getEntityMethodFlavorDetails(
			OssjEntityMethodFlavor flavor) throws TigerstripeException {

		if (!(containingModelComponent instanceof IManagedEntityArtifact))
			throw new TigerstripeException("Not a Managed Entity Method");

		String pojoFlavorDetails = getOssjMethodProperties().getProperty(
				flavor.getPojoLabel());
		if (pojoFlavorDetails != null)
			return new EntityMethodFlavorDetails(
					getContainingArtifact(),
					pojoFlavorDetails);
		return null;
	}

	public IEntityMethodFlavorDetails makeEntityMethodFlavorDetails() {
		return new EntityMethodFlavorDetails(
				(IAbstractArtifactInternal) this.containingModelComponent);
	}

	public void setEntityMethodFlavorDetails(OssjEntityMethodFlavor flavor,
			IEntityMethodFlavorDetails details) throws TigerstripeException {
		getOssjMethodProperties().setProperty(flavor.getPojoLabel(),
				details.toString());
	}

	public boolean isAbstract() {
		return this.isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	@Override
	public IMethod clone() {
		if (getContainingArtifact() == null)
			throw new IllegalStateException(
					"Cannot clone method that doesn't below to artifact.");

		IMethod result = getContainingArtifact().makeMethod();
		result.setAbstract(isAbstract());
		result.setComment(getComment());
		result.setDefaultReturnValue(getDefaultReturnValue());
		result.setInstanceMethod(isInstanceMethod());
		result.setIteratorReturn(isIteratorReturn());
		result.setReturnName(getReturnName());
		result.setName(getName());
		result.setOptional(isOptional());
		result.setOrdered(isOrdered());
		result.setReturnType(getReturnType().clone());
		result.setReturnRefBy(getReturnRefBy());
		result.setUnique(isUnique());
		result.setVisibility(getVisibility());
		result.setVoid(isVoid());

		// result.setEntityMethodFlavorDetails(flavor, details)

		Collection<IArgument> clonedArgs = new ArrayList<IArgument>();
		for (IArgument arg : getArguments()) {
			clonedArgs.add(arg.clone());
		}
		result.setArguments(clonedArgs);

		Collection<IException> clonedExcs = new ArrayList<IException>();
		for (IException excep : getExceptions()) {
			clonedExcs.add(excep.clone());
		}
		result.setExceptions(clonedExcs);

		Collection<IStereotypeInstance> stereotypeInstances = getStereotypeInstances();
		for (IStereotypeInstance inst : stereotypeInstances) {
			result.addStereotypeInstance(inst);
		}
		return result;
	}

	/**
	 * Returns a duplicate of the initial list where all components that are not
	 * in the current active facet are filtered out.
	 * 
	 * @param components
	 * @return
	 */
	public static Collection<IMethod> filterFacetExcludedMethods(
			Collection<IMethod> components) {
		ArrayList<IMethod> result = new ArrayList<IMethod>();
		for (Iterator<IMethod> iter = components.iterator(); iter.hasNext();) {
			IMethod component = iter.next();
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

	@Override
	public ITigerstripeModelProject getProject() throws TigerstripeException {
		if (getContainingArtifact() != null)
			return getContainingArtifact().getProject();

		return null;
	}

	@Override
	public String toString() {
		String label = getContainingArtifact()+".";
		label += this.getLabelString(true);
		if (hasExceptions()) {
			label = label + " throws";
			String comma = "";
			for (IException exc : getExceptions()) {
				label = label + " " + comma + exc.getFullyQualifiedName();
				comma = ",";

			}
		}
		return label;
	}
	
	private class DefaultArgumentStereotypesFormatter implements IArgumentLabelFormatter {

		public String getArgumentLabel(IArgument argument, String defaultLabel) {
			return argument.getStereotypeString() + defaultLabel;
		}		
	}

	public String getUniqueName() {
		return getMethodId();
	}

	public String getMemberName() {
		return "method";
	}

	public IType getType() {
		return getReturnType();
	}
}