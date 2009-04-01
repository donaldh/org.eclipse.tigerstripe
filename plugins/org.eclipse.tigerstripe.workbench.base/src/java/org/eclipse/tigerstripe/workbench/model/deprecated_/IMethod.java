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
package org.eclipse.tigerstripe.workbench.model.deprecated_;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.annotation.IAnnotationCapable;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

public interface IMethod extends IModelComponent {

	

	public final static List<IMethod> EMPTY_LIST = new ArrayList<IMethod>();

	public interface IArgument extends IStereotypeCapable, IAnnotationCapable {

		public enum EDirection {
			IN("in"), OUT ("out"), INOUT("inOut");
			
			private String label;
			private static String[] labels;
			
			private EDirection(String label){
				this.label = label;
			}
			
			public String getLabel() {
				return this.label;
			}
			
			public static EDirection parse(String label) {
				for (EDirection dir : values()) {
					if (dir.label.equals(label))
						return dir;
				}
				return null;
			}
			
			public static String[] labels() {
				if (labels == null) {
					labels = new String[values().length];
					int i = 0;
					for (EDirection mult : values()) {
						labels[i] = mult.label;
						i++;
					}
				}
				return labels;
			}
			
			public static int indexOf(EDirection dir) {
				for (int index = 0; index < values().length; index++) {
					if (dir == values()[index])
						return index;
				}
				throw new IllegalArgumentException("Illegal direction literal: "
						+ dir);
			}

			public static EDirection at(int index) {
				if (index >= 0 && index < values().length)
					return values()[index];
				throw new IllegalArgumentException("Illegal direction index: "
						+ index);
			}
		}

		/**
		 * Returns the IArtifact that is the "container" for the Argument.
		 * 
		 * This will be the same as the IArtifact that is the container for the
		 * Method.
		 * 
		 * @return the containing artifact.
		 */
		public IAbstractArtifact getContainingArtifact();

		/**
		 * Returns the IMethod that is the "container" for the argument.
		 * 
		 * @return the containing method.
		 */
		public IMethod getContainingMethod();

		/**
		 * Returns the comment (or plain-english description) associated with
		 * this argument.
		 * 
		 * @return String - the comment
		 */
		public String getComment();

		/**
		 * Sets the comment for this argument.
		 * 
		 * @param description
		 */
		public void setComment(String description);

		/**
		 * Returns the name associated with this argument.
		 * 
		 * @return String - the name of the argument
		 */
		public String getName();

		/**
		 * Set the name for this argument.
		 * 
		 * @param name
		 */
		public void setName(String name);

		/**
		 * Returns true if the return of this argument contains ordered values.
		 * 
		 * @return
		 */
		public boolean isOrdered();

		/**
		 * Sets the ordered attribute for this argument.
		 * 
		 * @param isOrdered
		 */
		public void setOrdered(boolean isOrdered);

		/**
		 * Returns true if the argument contains unique values.
		 * 
		 * @return
		 */
		public boolean isUnique();

		/**
		 * Sets the unique attribute for this argument.
		 * 
		 * @param isUnique
		 */
		public void setUnique(boolean isUnique);

		/**
		 * Returns the type of this argument.
		 * 
		 * @return IType - the type of this argument
		 */
		public IType getType();

		/**
		 * Set the type of this argument.
		 * 
		 * @param type
		 */
		public void setType(IType type);

		/**
		 * Returns an integer value indicating the reference type of the
		 * argument. Possible values are defined in the static fields of IField.
		 * 
		 * @return int - the integer value corresponding to the refBy
		 */
		public int getRefBy();

		/**
		 * Set the reference type of the argument. Possible values are defined
		 * in the static fields of IField.
		 * 
		 * @param refBy
		 */
		public void setRefBy(int refBy);

		/**
		 * Returns an String value indicating the reference type of the
		 * argument. Possible values are defined in the refByLabels field of
		 * IField.
		 * 
		 * @return String - the refBy type
		 */
		public String getRefByString();

		/**
		 * Returns the default value for this argument if it is defined, null
		 * otherwise.
		 * 
		 * @return
		 */
		public String getDefaultValue();

		/**
		 * Sets the default value fpor this argument.
		 * 
		 * @param defaultValue
		 */
		public void setDefaultValue(String defaultValue);

		/**
		 * Clone this argument.
		 * 
		 * @return
		 */
		public IArgument clone();

		public URI toURI() throws TigerstripeException;

		/**
		 * Returns the "Direction" of this IArgument
		 * @return
		 */
		public EDirection getDirection();
		
		/**
		 * Sets the "Direction" of this IArgumnet
		 * @param direction
		 */
		public void setDirection(EDirection direction);
		
	}

	public interface IException {

		/**
		 * Returns the IMethod that is the "container" for the argument.
		 * 
		 * @return the containing method.
		 */
		public IMethod getContainingMethod();
		
		/**
		 * Returns the Fully Qualified Name of an exception.
		 * 
		 * @return String - the Fully Qualified Name
		 */
		public String getFullyQualifiedName();

		/**
		 * Sets the Fully Qualified Name of an exception.
		 * 
		 * @param fqn
		 */
		public void setFullyQualifiedName(String fqn);

		/**
		 * Returns the name associated with this exception.
		 * 
		 * @return String - the name of the exception
		 */
		public String getName();

		/**
		 * Returns the package associated with this exception.
		 * 
		 * @return String - the package where this exception is defined.
		 */
		public String getPackage();

		public IStatus validate();

		/**
		 * Clone this exception.
		 * 
		 * @return
		 */
		public IException clone();

	}

	// Entity Method Flavor
	/**
	 * This enum represents the "Flavors" that might be supported for a method.
	 * It is designed specifically for OSS/J usage.
	 */
	public enum OssjEntityMethodFlavor {

		SIMPLE("simple"), SIMPLEBYKEY("simpleByKey"), BULKATOMIC("bulkAtomic"), BULKATOMICBYKEYS(
				"bulkAtomicByKeys"), BULKBESTEFFORT("bulkBestEffort"), BULKBESTEFFORTBYKEYS(
				"bulkBestEffortByKeys"), BYTEMPLATE("byTemplate"), BYTEMPLATES(
				"byTemplates"), BYTEMPLATEBESTEFFORT("byTemplateBestEffort"), BYTEMPLATESBESTEFFORT(
				"byTemplatesBestEffort"), BYAUTONAMING("byAutoNaming");

		private final String pojoLabel;

		private OssjEntityMethodFlavor(String pojoLabel) {
			this.pojoLabel = pojoLabel;
		}

		public String getPojoLabel() {
			return this.pojoLabel;
		}

		public static OssjEntityMethodFlavor valueFromPojoLabel(String label) {
			for (OssjEntityMethodFlavor flavor : values()) {
				if (flavor.pojoLabel.equals(label))
					return flavor;
			}
			return null;
		}
	}

	/**
	 * This enum represents some key "Properties" of a method. It is designed
	 * specifically for OSS/J usage.
	 */
	public enum OssjMethodProperty {

		INSTANCEMETHOD("instanceMethod");

		private String pojoLabel;

		OssjMethodProperty(String pojoLabel) {
			this.pojoLabel = pojoLabel;
		}

		public String getPojoLabel() {
			return this.pojoLabel;
		}
	}

	/**
	 * Returns the IArtifact that is the "container" for the Method.
	 * 
	 * @return the containing artifact.
	 */
	public IAbstractArtifact getContainingArtifact();

	/**
	 * Returns a String containing the methodName+profile&returntype. This is
	 * used for display on class diagrams e.g. but also to uniquely identify a
	 * method within the context of an artifact.
	 * 
	 * The format is: name(argumentList)::returnTypeName
	 * 
	 * This is equivalent to getLabelString(true)
	 * 
	 * @return formatted String
	 */
	public String getLabelString();

	/**
	 * Returns a String containing the methodName+profile&returntype. This is
	 * used for display on class diagrams e.g. but also to uniquely identify a
	 * method within the context of an artifact.
	 * 
	 * The format is: name(argumentList)::returnTypeName
	 * 
	 * @param includeArgStereotypes
	 *            - <<xxx>> on args will be included if true
	 * @return formatted String
	 */
	public String getLabelString(boolean includeArgStereotypes);

	/**
	 * Returns an identifier that uniquely identifies this method within the
	 * scope of its artifact.
	 * 
	 * This id is assembled based on the signature of this method.
	 * 
	 * There can't be 2 methods with the same signatureId per artifact.
	 * 
	 * @return String - a unique identifier for this Method
	 */
	public String getMethodId();

	/**
	 * Returns an array of all of the exceptions for this Method. Returns an
	 * empty array if no exceptions are defined.
	 * 
	 * @return IException[] - An array of all defined exceptions for this Method
	 */
	public Collection<IException> getExceptions();

	/**
	 * Add a single Exception to the method.
	 * 
	 * @param exception
	 *            to add
	 */
	public void addException(IException exception);

	/**
	 * Set the exceptions for the method.
	 * 
	 * @param exceptions
	 */
	public void setExceptions(Collection<IException> exceptions);

	/**
	 * Remove exceptions from the method.
	 * 
	 * @param exception
	 */
	public void removeExceptions(Collection<IException> exception);

	/**
	 * Make a blank Exception.
	 * 
	 * Note this is not added to the method.
	 * 
	 * @return a new IException
	 */
	public IException makeException();

	/**
	 * Returns a collection of all of the arguments for this Method. Returns an
	 * empty collection if no arguments are defined.
	 * 
	 * @return Collection<IArgument> - unmodifiable collection of arguments for
	 *         this Method
	 */
	public Collection<IArgument> getArguments();

	/**
	 * Add an argument to the method.
	 * 
	 * The argument will be added to the end of the argument list.
	 * 
	 * @param argument
	 */
	public void addArgument(IArgument argument);

	/**
	 * Set the arguments for the method.
	 * 
	 * @param arguments
	 */
	public void setArguments(Collection<IArgument> arguments);

	/**
	 * Remove arguments from the argument list.
	 * 
	 * @param arguments
	 */
	public void removeArguments(Collection<IArgument> arguments);

	/**
	 * Make a blank Argument.
	 * 
	 * Note the argument is not added to the method.
	 * 
	 * @return a new IArgument.
	 */
	public IArgument makeArgument();

	/**
	 * Returns true if the return of this Method is void.
	 * 
	 * @return boolean - true if void
	 */
	public boolean isVoid();

	/**
	 * Sets the return type to void
	 */
	public void setVoid(boolean isVoid);

	/**
	 * Returns the name of the return for this method.
	 * 
	 * This is useful when generating XML for example where a name is required.
	 * 
	 * @return
	 */
	public String getReturnName();

	/**
	 * Sets the name of the return for this method.
	 * 
	 * @return
	 */
	public void setReturnName(String methodReturnName);

	/**
	 * Returns the return type for this Method. If isVoid() the result of this
	 * method is unknown.
	 * 
	 * @return IType - the type returned by this Method.
	 */
	public IType getReturnType();

	/**
	 * Sets the return type for this Method.
	 * 
	 * @param returnType
	 *            - the return type for this method.
	 */
	public void setReturnType(IType returnType);

	/**
	 * Factory method for IType
	 * 
	 * @return a blank IType.
	 */
	public IType makeType();

	/**
	 * Returns the default return value for this method is it has been defined,
	 * null otherwise.
	 * 
	 * The string is not checked for its ability to be parsed into an object of
	 * the appropriate return type for this method.
	 * 
	 * @return a string representation.
	 */
	public String getDefaultReturnValue();

	/**
	 * Sets the default return Value for this method.
	 * 
	 * @param defaultReturnValue
	 */
	public void setDefaultReturnValue(String defaultReturnValue);

	/**
	 * Returns all the stereotype instances for the return type of the method.
	 * 
	 * @return the stereotypes applied to the method return
	 */
	public Collection<IStereotypeInstance> getReturnStereotypeInstances();

	/**
	 * Returns true if the method has the named return Stereotype
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasReturnStereotypeInstance(String name);

	/**
	 * Returns the named stereotype instances for the return type of the method.
	 * 
	 * @return the named stereotype applied to the method return
	 */
	public IStereotypeInstance getReturnStereotypeInstanceByName(String name);

	/**
	 * Add a single stereotype to the return type.
	 * 
	 * @param instance
	 *            to add.
	 */
	public void addReturnStereotypeInstance(IStereotypeInstance instance);

	/**
	 * Remove a single Stereotype instance from the method return.
	 * 
	 * @param instance
	 *            to remove
	 */
	public void removeReturnStereotypeInstance(IStereotypeInstance instance);

	/**
	 * Remove Stereotype instances from the method return.
	 * 
	 * @param instances
	 *            to remove
	 */
	public void removeReturnStereotypeInstances(
			Collection<IStereotypeInstance> instances);

	/**
	 * Returns a boolean indicating if this Method is optional or mandatory.
	 * 
	 * @return boolean - true if optional
	 */
	public boolean isOptional();

	/**
	 * Set the optional attribute of this method.
	 * 
	 * @param optional
	 */
	public void setOptional(boolean optional);

	/**
	 * Returns true if the return of this method contains unique values
	 * (multiplicity > 1)
	 * 
	 * @return
	 */
	public boolean isUnique();

	/**
	 * Sets the unique attribute for this method.
	 * 
	 * @param isUnique
	 */
	public void setUnique(boolean isUnique);

	/**
	 * Returns true if the return of this method contains ordered values
	 * (multiplicity > 1)
	 * 
	 * @return
	 */
	public boolean isOrdered();

	/**
	 * Sets the ordered attribute for this method.
	 * 
	 * @param isOrdered
	 */
	public void setOrdered(boolean isOrdered);

	/**
	 * Returns a boolean indicating whether this method is abstract or not.
	 * 
	 * @return boolean - true if this method is abstract, false otherwise
	 */
	public boolean isAbstract();

	/**
	 * sets whether this method is abstract or not
	 * 
	 * @param isAbstract
	 *            - boolean, true to set this method as abstract
	 */
	public void setAbstract(boolean isAbstract);

	/**
	 * Clone this method.
	 * 
	 * @return
	 */
	public IMethod clone();

	/**
	 * Returns an integer value indicating the reference type of the return.
	 * Possible values are defined in the static fields of IField.
	 * 
	 * @return int - the integer value corresponding to the refBy
	 */
	public int getReturnRefBy();

	/**
	 * Set the reference type of the return. Possible values are defined in the
	 * static fields of IField.
	 * 
	 * @param refBy
	 */
	public void setReturnRefBy(int refBy);

	/**
	 * Returns an String value indicating the reference type of the return.
	 * Possible values are defined in the refByLabels field of IField.
	 * 
	 * @return String - the refBy type
	 */
	public String getReturnRefByString();

	/**
	 * Sets the "InstanceMethod" flag.
	 * 
	 * This is only applicable to Entity methods. By default an entity method is
	 * an instanceMethod (i.e. it is exposed on a JVTSession that manages that
	 * entity type). When not an instanceMethod, the method is instead exposed
	 * on the managed entity value directly, without any transformation. In that
	 * case no variation is available (byKey, byTemplates, etc...)
	 * 
	 * @param instance
	 */
	public void setInstanceMethod(boolean instance);

	/**
	 * Returns true if this Method is an instance method.
	 * 
	 * @return boolean - true if this Method is an instance method
	 */
	public boolean isInstanceMethod();

	/**
	 * Returns a boolean indicating whether the return for this method is an
	 * iterator or not.
	 * 
	 * @return boolean - true if this method return type should be an iterator
	 */
	public boolean isIteratorReturn();

	/**
	 * Sets the isIterator attribute of the method.
	 * 
	 * @param iterate
	 */
	public void setIteratorReturn(boolean iterate);

	/**
	 * Returns the OSSJ flavor details for this method
	 * 
	 * @param flavor
	 *            - OssjEntityMethodFlavor target flavor
	 * @return OSSJ flavor details for this method
	 * @throws TigerstripeException
	 *             , if method doesn't belong to Managed Entity
	 */
	public IEntityMethodFlavorDetails getEntityMethodFlavorDetails(
			OssjEntityMethodFlavor flavor) throws TigerstripeException;

	/**
	 * Sets the OSSJ Entity flavor details for this method.
	 * 
	 * @param flavor
	 *            - the target flavor
	 * @param details
	 *            - the details for this target flavor
	 * @throws TigerstripeException
	 *             - if this method doesn't belong to a ManagedEntity
	 */
	public void setEntityMethodFlavorDetails(OssjEntityMethodFlavor flavor,
			IEntityMethodFlavorDetails details) throws TigerstripeException;

	/**
	 * Factory method for OSSJ Entity Details. The returned entity details is
	 * the default details for the given flavor for a method.
	 * 
	 * @return
	 */
	public IEntityMethodFlavorDetails makeEntityMethodFlavorDetails();
}
