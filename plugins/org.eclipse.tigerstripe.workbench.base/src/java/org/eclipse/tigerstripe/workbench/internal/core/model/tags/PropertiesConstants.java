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
package org.eclipse.tigerstripe.workbench.internal.core.model.tags;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;

/**
 * 
 * @author Eric Dillon
 * 
 */
public final class PropertiesConstants {

	private PropertiesConstants() {
		// not intended to be instanciated
	}

	/**
	 * Extracts properties following this format
	 * 
	 * @tigerstripe.property ts.id="xxxxx" name1 = value1 name2 = value2 ...
	 *                       nameN = valueN
	 * 
	 * @param tags -
	 *            a collection of org.eclipse.tigerstripe.core.model.Tags
	 * @param id -
	 *            the id for that property ('xxxxx')
	 * @return Properties made of the nameX,valueX pairs
	 * @throws TigerstripeException
	 *             if the property id cannot be found in the list of tags.
	 */
	public static Properties getPropertiesById(Collection tags, String id)
			throws TigerstripeException {
		Properties result = null;

		if (tags == null || tags.isEmpty() || id == null || id.length() == 0)
			return new Properties();

		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();

			if (Constants.PROPERTY.equals(tag.getName())) {
				Properties props = tag.getProperties();
				if (props != null
						&& id.equals(props.getProperty(Constants.TS_ID))) {
					result = props;
				}
			}
		}

		if (result == null)
			throw new TigerstripeException("Property " + id + " not found.");
		return result;
	}

	public String propertyIdToString(String id, Properties properties) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(Constants.PROPERTY);
		buffer.append(" ");
		buffer.append(id);

		Set keys = properties.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			buffer.append(" ");
			String name = (String) iter.next();
			String value = properties.getProperty(name, "");
			buffer.append(name);
			buffer.append(" = \"");
			buffer.append(value);
			buffer.append("\"");
		}

		return buffer.toString();
	}
}
