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
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

public interface IMethod extends IModelComponent {

	public final static List<IMethod> EMPTY_LIST = new ArrayList<IMethod>();

	public interface IArgument extends IStereotypeCapable {

		public void setDefaultValue(String defaultValue);

		public void setComment(String description);

		public void setName(String name);

		public void setOrdered(boolean isOrdered);

		public void setUnique(boolean isUnique);

		public void setType(IType type);

		/**
		 * valid values are IField.REFBY_VALUE, IField.REFBY_KEY,
		 * IField.REFBY_KEYRESULT
		 * 
		 * @param refBy
		 */
		public void setRefBy(int refBy);

		public IArgument clone();

		/**
		 * Returns the comment (or plain-english description) associated with
		 * this argument.
		 * 
		 * @return String - the comment
		 */
		public String getComment();

		/**
		 * Returns the IArtifact that is the "container" for the Method.
		 * 
		 * @return the containing artifact.
		 */
		public IAbstractArtifact getContainingArtifact();

		/**
		 * Returns the IMethod that is the "container" for the argument.
		 * 
		 * @return the containing method.
		 */
		public IMethod getContainingIMethod();

		/**
		 * returns the default value for this argument if it is defined, null
		 * otherwise.
		 * 
		 * @return
		 */
		public String getDefaultValue();

		/**
		 * Returns the type of this argument.
		 * 
		 * @return IType - the type of this argument
		 */
		public IType getType();

		/**
		 * Returns the name associated with this argument.
		 * 
		 * @return String - the name of the argument
		 */
		public String getName();

		/**
		 * Returns an integer value indicating the reference type of the
		 * argument. Possible values are defined in the static fields of IField.
		 * 
		 * @return int - the integer value corresponding to the refBy
		 */
		public int getRefBy();

		/**
		 * Returns an String value indicating the reference type of the
		 * argument. Possible values are defined in the refByLabels field of
		 * IField.
		 * 
		 * @return String - the refBy type
		 */
		public String getRefByString();

		public boolean isOrdered();

		public boolean isUnique();
	}

	public interface IException {

		public void setFullyQualifiedName(String fqn);

		public IStatus validate();

		public IException clone();

		/**
		 * Returns the Fully Qualified Name of an exception.
		 * 
		 * @return String - the Fully Qualified Name
		 */
		public String getFullyQualifiedName();

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

	// Entity Properties
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

	public IException makeException();

	public void setExceptions(Collection<IException> exceptions);

	public void addException(IException exception);

	public void removeExceptions(Collection<IException> exception);

	/**
	 * Sets the return type to void
	 */
	public void setVoid(boolean isVoid);

	public void setIteratorReturn(boolean iterate);

	/**
	 * Factory method for IType
	 * 
	 * @return
	 */
	public IType makeType();

	/**
	 * Returns a String containing the methodName+profile&returntype. This is
	 * used for display on class diagrams e.g. but also to uniquely identify a
	 * method within the context of an artifact.
	 * 
	 * @return
	 */
	public String getLabelString();

	public void setReturnRefBy(int refBy);

	/**
	 * Sets the return type for this Method.
	 * 
	 * @param returnType -
	 *            the return type for this method.
	 */
	public void setReturnType(IType returnType);

	public IArgument makeArgument();

	public void setArguments(Collection<IArgument> arguments);

	public void addArgument(IArgument argument);

	public void removeArguments(Collection<IArgument> arguments);

	public void setOptional(boolean optional);


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
	 * Returns the OSSJ flavor details for this method
	 * 
	 * @param flavor -
	 *            OssjEntityMethodFlavor target flavor
	 * @return OSSJ flavor details for this method
	 * @throws TigerstripeException,
	 *             if method doesn't belong to Managed Entity
	 */
	public IEntityMethodFlavorDetails getEntityMethodFlavorDetails(
			OssjEntityMethodFlavor flavor) throws TigerstripeException;

	/**
	 * Factory method for OSSJ Entity Details. The returned entity details is
	 * the default details for the given flavor for a method.
	 * 
	 * @return
	 */
	public IEntityMethodFlavorDetails makeEntityMethodFlavorDetails();

	/**
	 * Sets the OSSJ Entity flavor details for this method.
	 * 
	 * @param flavor -
	 *            the target flavor
	 * @param details -
	 *            the details for this target flavor
	 * @throws TigerstripeException -
	 *             if this method doesn't belong to a ManagedEntity
	 */
	public void setEntityMethodFlavorDetails(OssjEntityMethodFlavor flavor,
			IEntityMethodFlavorDetails details) throws TigerstripeException;



	public void setUnique(boolean isUnique);

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
	 * @param isAbstract -
	 *            boolean, true to set this method as abstract
	 */
	public void setAbstract(boolean isAbstract);

	public void setDefaultReturnValue(String defaultReturnValue);

	/**
	 * Sets the name of the return for this method.
	 * 
	 * @return
	 */
	public void setMethodReturnName(String methodReturnName);

	/**
	 * Returns all the stereotype instances for this
	 * 
	 * @return
	 */
	public Collection<IStereotypeInstance> getReturnStereotypeInstances();

	public void addReturnStereotypeInstance(IStereotypeInstance instance);

	public void removeReturnStereotypeInstance(IStereotypeInstance instance);

	public void removeReturnStereotypeInstances(Collection<IStereotypeInstance> instances);

	public IMethod clone();

	// public Properties getOssjMethodProperties();

	/**
	 * Returns the IArtifact that is the "container" for the Method.
	 * 
	 * @return the containing artifact.
	 */
	public IAbstractArtifact getContainingArtifact();

	/**
	 * Returns the default return value for this method is it has been defined,
	 * null otherwise
	 * 
	 * @return
	 */
	public String getDefaultReturnValue();

	/**
	 * Returns an array of all of the arguments for this Method. Returns an
	 * empty array if no arguments are defined.
	 * 
	 * @return IArgument[] - array of arguments for this Method
	 */
	public Collection<IArgument> getArguments();

	/**
	 * Returns an array of all of the exceptions for this Method. Returns an
	 * empty array if no exceptions are defined.
	 * 
	 * @return IException[] - An array of all defined exceptions for this Method
	 */
	public Collection<IException> getExceptions();

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
	 * Returns the name of the return for this method.
	 * 
	 * @return
	 */
	public String getReturnName();

	/**
	 * Returns the return type for this Method. If isVoid() the result of this
	 * method is unknown.
	 * 
	 * @return IType - the type returned by this Method.
	 */
	public IType getReturnType();

	/**
	 * Returns an integer value indicating the reference type of the return.
	 * Possible values are defined in the static fields of IField.
	 * 
	 * @return int - the integer value corresponding to the refBy
	 */
	public int getReturnRefBy();

	/**
	 * Returns an String value indicating the reference type of the return.
	 * Possible values are defined in the refByLabels field of IField.
	 * 
	 * @return String - the refBy type
	 */
	public String getReturnRefByString();

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
	 * @return boolean - true if this method return type shoud be an iterator
	 */
	public boolean isIteratorReturn();

	/**
	 * Returns a boolean indicating if this Method is optional or mandatory.
	 * 
	 * @return boolean - true if optional
	 */
	public boolean isOptional();

	/**
	 * Returns true if the return of this method contains ordered values
	 * (multiplicity > 1)
	 * 
	 * @return
	 */
	public boolean isOrdered();

	/**
	 * Returns true if the return of this method contains unique values
	 * (multiplicity > 1)
	 * 
	 * @return
	 */
	public boolean isUnique();

	/**
	 * Returns true if the return of this Method is void.
	 * 
	 * @return boolean - true if void
	 */
	public boolean isVoid();
}
