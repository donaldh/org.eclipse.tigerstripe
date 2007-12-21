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
package org.eclipse.tigerstripe.api.external.model;

import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeCapable;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeInstance;

/**
 * This class represents a Method for an IArtifact.
 * 
 * 
 * 
 */
public interface IextMethod extends IextModelComponent {

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

	/**
	 * This class represents an argument for a method.
	 * 
	 */
	public interface IextArgument extends IextStereotypeCapable {

		/**
		 * returns the default value for this argument if it is defined, null
		 * otherwise.
		 * 
		 * @return
		 */
		public String getDefaultValue();

		/**
		 * Returns the name associated with this argument.
		 * 
		 * @return String - the name of the argument
		 */
		public String getName();

		/**
		 * Returns the type of this argument.
		 * 
		 * @return IextType - the type of this argument
		 * @deprecated use getIextType for consistency
		 */
		@Deprecated
		public IextType getIType();

		/**
		 * Returns the type of this argument.
		 * 
		 * @return IextType - the type of this argument
		 */
		public IextType getIextType();

		/**
		 * Returns an integer value indicating the reference type of the
		 * argument. Possible values are defined in the static fields of
		 * IextField.
		 * 
		 * @return int - the integer value corresponding to the refBy
		 */
		public int getRefBy();

		/**
		 * Returns an String value indicating the reference type of the
		 * argument. Possible values are defined in the refByLabels field of
		 * IextField.
		 * 
		 * @return String - the refBy type
		 */
		public String getRefByString();

		/**
		 * Returns the comment (or plain-english description) associated with
		 * this argument.
		 * 
		 * @return String - the comment
		 */
		public String getComment();

		/**
		 * Returns the IextMethod that is the "container" for the argument.
		 * 
		 * @return the containing method.
		 */
		public IextMethod getContainingIextMethod();

		/**
		 * Returns the IArtifact that is the "container" for the Method.
		 * 
		 * @return the containing artifact.
		 */
		public IArtifact getContainingArtifact();

		public boolean isUnique();

		public boolean isOrdered();
	}

	/**
	 * This class represents an exception for a method.
	 * 
	 */
	public interface IextException {

		/**
		 * Returns the name associated with this exception.
		 * 
		 * @return String - the name of the exception
		 */
		public String getName();

		/**
		 * Returns the Fully Qualified Name of an exception.
		 * 
		 * @return String - the Fully Qualified Name
		 */
		public String getFullyQualifiedName();
	}

	/**
	 * Returns an array of all of the exceptions for this Method. Returns an
	 * empty array if no exceptions are defined.
	 * 
	 * @return IextException[] - An array of all defined exceptions for this
	 *         Method
	 */
	public IextException[] getIextExceptions();

	/**
	 * Returns true if the return of this Method is void.
	 * 
	 * @return boolean - true if void
	 */
	public boolean isVoid();

	/**
	 * Returns true if the return of this method contains unique values
	 * (multiplicity > 1)
	 * 
	 * @return
	 */
	public boolean isUnique();

	/**
	 * Returns true if the return of this method contains ordered values
	 * (multiplicity > 1)
	 * 
	 * @return
	 */
	public boolean isOrdered();

	/**
	 * Returns the return type for this Method. If isVoid() the result of this
	 * method is unknown.
	 * 
	 * @return IextType - the type returned by this Method.
	 */
	public IextType getReturnIextType();

	/**
	 * Returns an integer value indicating the reference type of the return.
	 * Possible values are defined in the static fields of IextField.
	 * 
	 * @return int - the integer value corresponding to the refBy
	 */
	public int getReturnRefBy();

	/**
	 * Returns an String value indicating the reference type of the return.
	 * Possible values are defined in the refByLabels field of IextField.
	 * 
	 * @return String - the refBy type
	 */
	public String getReturnRefByString();

	/**
	 * Returns an array of all of the arguments for this Method. Returns an
	 * empty array if no arguments are defined.
	 * 
	 * @return IextArgument[] - array of arguments for this Method
	 */
	public IextArgument[] getIextArguments();

	/**
	 * Returns a boolean indicating if this Method is optional or mandatory.
	 * 
	 * @return boolean - true if optional
	 */
	public boolean isOptional();

	/**
	 * Returns a boolean indicating whether this method is abstract or not.
	 * Methods can only be abstract if the containingArtifact is also Abstract.
	 * 
	 * @return boolean - true if this method is abstract, false otherwise
	 */
	public boolean isAbstract();

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

	// public Properties getOssjMethodProperties();

	/**
	 * Returns the IArtifact that is the "container" for the Method.
	 * 
	 * @return the containing artifact.
	 */
	public IArtifact getContainingArtifact();

	/**
	 * Returns the default return value for this method is it has been defined,
	 * null otherwise
	 * 
	 * @return
	 */
	public String getDefaultReturnValue();

	/**
	 * Returns the name of the return for this method.
	 * 
	 * @return
	 */
	public String getMethodReturnName();

	public IextStereotypeInstance[] getReturnStereotypeInstances();

}
