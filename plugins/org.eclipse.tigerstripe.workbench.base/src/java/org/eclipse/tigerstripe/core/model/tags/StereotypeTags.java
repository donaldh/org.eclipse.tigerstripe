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
package org.eclipse.tigerstripe.core.model.tags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.Method;
import org.eclipse.tigerstripe.core.model.Tag;
import org.eclipse.tigerstripe.core.profile.stereotype.StereotypeInstance;
import org.eclipse.tigerstripe.core.profile.stereotype.UnresolvedStereotypeInstance;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public final class StereotypeTags {

	private StereotypeTags() {
		// not intended to be instanciated
	}

	/**
	 * Annotate all the arguments based on the list of stereotypes passed
	 * 
	 * An argument stereotype is expected to have the following format
	 * 
	 * tigerstripe.arg.stereotype arg.name="xxx" st.name="xxx" ...all stereotype
	 * attrs...
	 * 
	 * @param arguments
	 * @param tags
	 */
	public static void annotateArguments(Collection<Method.Argument> arguments,
			Collection<Tag> tags) {
		if (tags == null || tags.isEmpty())
			return;

		// a map of all stereotype instances per arg name
		HashMap<String, ArrayList<IStereotypeInstance>> map = new HashMap<String, ArrayList<IStereotypeInstance>>();
		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();

			if (Constants.ARG_STEREOTYPE.equals(tag.getName())) {
				Properties props = tag.getProperties();
				String stereotypeName = props.getProperty(Constants.ST_NAME);
				String argName = props.getProperty(Constants.ST_ARG_NAME);
				IStereotypeInstance instance = extractInstance(tag,
						stereotypeName);
				if (argName != null && argName.length() != 0
						&& instance != null) {
					ArrayList<IStereotypeInstance> argInstances = map
							.get(argName);
					if (argInstances == null) {
						argInstances = new ArrayList<IStereotypeInstance>();
						map.put(argName, argInstances);
					}
					argInstances.add(instance);
				}
			}
		}

		// At this stage the map contains all the stereotype instances indexed
		// by
		// the arg names
		for (Method.Argument argument : arguments) {
			String argName = argument.getName();
			if (argName != null && argName.length() != 0) {
				ArrayList<IStereotypeInstance> argInstances = map.get(argName);
				if (argInstances != null) {
					argument.addStereotypeInstances(argInstances
							.toArray(new IStereotypeInstance[argInstances
									.size()]));
				}
			}
		}

	}

	/**
	 * Extracts all method return instances from the given tags
	 * 
	 */
	public static IStereotypeInstance[] getAllReturnStereotypes(Collection tags) {
		if (tags == null || tags.isEmpty())
			return IStereotypeInstance.EMPTY_ARRAY;

		ArrayList<IStereotypeInstance> result = new ArrayList<IStereotypeInstance>();
		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();

			if (Constants.METH_RETURN_STEREOTYPE.equals(tag.getName())) {
				Properties props = tag.getProperties();
				String stereotypeName = props.getProperty(Constants.ST_NAME);
				result.add(extractInstance(tag, stereotypeName));
			}
		}
		return result.toArray(new IStereotypeInstance[result.size()]);
	}

	/**
	 * Extracts all stereotype instances from the given tags
	 * 
	 */
	public static IStereotypeInstance[] getAllStereotypes(Collection tags) {
		if (tags == null || tags.isEmpty())
			return IStereotypeInstance.EMPTY_ARRAY;

		ArrayList<IStereotypeInstance> result = new ArrayList<IStereotypeInstance>();
		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();

			if (Constants.STEREOTYPE.equals(tag.getName())) {
				Properties props = tag.getProperties();
				String stereotypeName = props.getProperty(Constants.ST_NAME);
				IStereotypeInstance inst = extractInstance(tag, stereotypeName);
				if (inst == null)
					TigerstripeRuntime
							.logWarnMessage("Couldn't read details of '"
									+ stereotypeName
									+ "'. Using wrong profile?");
				else
					result.add(inst);
			}
		}
		return result.toArray(new IStereotypeInstance[result.size()]);
	}

	private static IStereotypeInstance extractInstance(Tag tag,
			String stereotypeName) {
		Properties props = tag.getProperties();
		if (stereotypeName != null && stereotypeName.length() != 0) {
			// now build the IStereotypeInstance from the extracted
			// Properties.
			IWorkbenchProfile profile = API.getIWorkbenchProfileSession()
					.getActiveProfile();
			IStereotype stereotype = profile
					.getStereotypeByName(stereotypeName);
			if (stereotype != null) {
				IStereotypeInstance instance = stereotype.makeInstance();
				for (IStereotypeAttribute attr : stereotype.getAttributes()) {
					try {
						// Check that we can find the attr properly.
						String attrVal = props.getProperty(attr.getName());

						if (!attr.isArray()) {
							instance.setAttributeValue(attr, props.getProperty(
									attr.getName(), attr.getDefaultValue()));
						} else {
							((StereotypeInstance) instance)
									.setAttributeValuesFromStringified(attr,
											props.getProperty(attr.getName(),
													attr.getDefaultValue()));
						}
					} catch (TigerstripeException e) {
						TigerstripeRuntime.logDebugMessage(
								"While extracting annotation", e);
					}
				}
				return instance;
			}
		}

		// If we get to this, it means we are trying to parse a stereotype
		// instance
		// that is not defined in the active profile.
		// Instead return an "unresolved" one, yet populate the attributes as
		// possible.
		return makeUnresolvedInstance(tag, stereotypeName);
	}

	private static UnresolvedStereotypeInstance makeUnresolvedInstance(Tag tag,
			String stereotypeName) {
		UnresolvedStereotypeInstance result = new UnresolvedStereotypeInstance(
				stereotypeName);

		// just populate the attributes that can be decoded in a best effort
		// fashion
		Properties props = tag.getProperties();
		for (Object key : props.keySet()) {
			String name = (String) key;
			String value = props.getProperty(name);
			result.addAttributeAndValue(name, value);
		}

		return result;
	}

	/**
	 * Extracts properties following this format
	 * 
	 * @tigerstripe.stereotype st.name="xxxxx" name1 = value1 name2 = value2 ...
	 *                         nameN = valueN
	 * 
	 * @param tags -
	 *            a collection of org.eclipse.tigerstripe.core.model.Tags
	 * @param name -
	 *            the name for that stereotype ('xxxxx')
	 * @return IStereotypeInstance made of the nameX,valueX pairs
	 * @throws TigerstripeException
	 *             if the stereotype instance cannot be found in the list of
	 *             tags, or the stereotype is not valid in the current profile.
	 */
	public static IStereotypeInstance getStereotypeByName(Collection tags,
			String name) throws TigerstripeException {
		Properties extractedProps = null;

		if (tags == null || tags.isEmpty() || name == null
				|| name.length() == 0)
			throw new TigerstripeException("Unknown stereotype.");

		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();

			if (Constants.STEREOTYPE.equals(tag.getName())) {
				Properties props = tag.getProperties();
				if (props != null
						&& name.equals(props.getProperty(Constants.ST_NAME))) {
					extractedProps = props;
				}
			}
		}

		if (extractedProps == null)
			throw new TigerstripeException("Stereotype " + name + " not found.");

		// now build the IStereotypeInstance from the extracted Properties.
		IWorkbenchProfile profile = API.getIWorkbenchProfileSession()
				.getActiveProfile();
		IStereotype stereotype = profile.getStereotypeByName(name);
		if (stereotype != null) {
			IStereotypeInstance instance = stereotype.makeInstance();
			for (IStereotypeAttribute attr : stereotype.getAttributes()) {
				instance.setAttributeValue(attr, extractedProps.getProperty(
						attr.getName(), attr.getDefaultValue()));
			}
			return instance;
		}

		throw new TigerstripeException("Unknown stereotype");
	}

	public String stereotypeInstanceToString(IStereotypeInstance instance) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(Constants.STEREOTYPE);
		buffer.append(" ");
		buffer.append(instance.getName());

		for (IStereotypeAttribute attr : instance
				.getCharacterizingIStereotype().getAttributes()) {
			buffer.append(" ");
			String name = attr.getName();
			String value = "";
			try {
				value = instance.getAttributeValue(attr);
			} catch (TigerstripeException e) {
				// ignore here
			}
			buffer.append(name);
			buffer.append(" = \"");
			buffer.append(value);
			buffer.append("\"");
		}

		return buffer.toString();
	}
}
