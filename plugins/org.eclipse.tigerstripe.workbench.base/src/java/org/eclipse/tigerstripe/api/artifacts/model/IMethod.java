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
package org.eclipse.tigerstripe.api.artifacts.model;

import java.util.List;
import java.util.Properties;

import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextMethod;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;

public interface IMethod extends IextMethod, IModelComponent {

	public interface IArgument extends IextArgument, IStereotypeCapable {

		public void setDefaultValue(String defaultValue);

		public void setComment(String description);

		public void setName(String name);

		public void setOrdered(boolean isOrdered);

		public void setUnique(boolean isUnique);

		public IType getIType();

		public void setIType(IType type);

		/**
		 * valid values are IField.REFBY_VALUE, IField.REFBY_KEY,
		 * IField.REFBY_KEYRESULT
		 * 
		 * @param refBy
		 */
		public void setRefBy(int refBy);

		public int getRefBy();

		public String getRefByString();

		public IArgument clone();
	}

	public interface IException extends IextException {

		public String getFullyQualifiedName();

		public void setFullyQualifiedName(String fqn);

		public List<TigerstripeError> validate();

		public IException clone();
	}

	public IException makeIException();

	public void setIExceptions(IException[] exceptions);

	public void addIException(IException exception);

	public void removeIExceptions(IException[] exception);

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
	public IType makeIType();

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
	public void setReturnIType(IType returnType);

	public IArgument makeIArgument();

	public void setIArguments(IArgument[] arguments);

	public void addIArgument(IArgument argument);

	public void removeIArguments(IArgument[] arguments);

	public void setOptional(boolean optional);

	/**
	 * @deprecated DO NOT USE. Please use setOssjEntityMethodFlavorDetails
	 *             instead.
	 * 
	 * @param prop -
	 *            the internal representation for the flavor details.
	 */
	@Deprecated
	public void setOssjMethodProperties(Properties prop);

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
	 * Returns the array of arguments for this method.
	 * 
	 * @return
	 */
	public IArgument[] getIArguments();

	/**
	 * Returns the array of exceptions for this method.
	 * 
	 * @return
	 */
	public IException[] getIExceptions();

	/**
	 * Returns the return type for this Method. If isVoid() the result of this
	 * methods is unknown.
	 * 
	 * @return
	 */
	public IType getReturnIType();

	public List<TigerstripeError> validate();

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

	/**
	 * @deprecated Do not use. Use getEntityMethodFlavorDetails instead.
	 * @return internal representation for OSSJ method flavor details.
	 */
	@Deprecated
	public Properties getOssjMethodProperties();

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
	public IStereotypeInstance[] getReturnStereotypeInstances();

	public void addReturnStereotypeInstance(IStereotypeInstance instance);

	public void removeReturnStereotypeInstance(IStereotypeInstance instance);

	public void removeReturnStereotypeInstances(IStereotypeInstance[] instances);

	public IMethod clone();
}
