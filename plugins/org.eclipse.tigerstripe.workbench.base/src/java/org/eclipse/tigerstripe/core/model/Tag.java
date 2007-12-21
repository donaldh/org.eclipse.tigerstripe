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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.thoughtworks.qdox.model.DocletTag;

/**
 * @author Eric Dillon
 * 
 * This is any tag that may be found on an Artifact
 * 
 */
public class Tag {
	private String value;
	private String name;
	private Collection parameters;
	private Properties properties;

	/**
	 * Default constructor
	 * 
	 */
	public Tag(String name) {
		setName(name);
	}

	public Tag(DocletTag tag) {
		this(tag.getName());
		buildModel(tag);
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public Collection getParameters() {
		return this.parameters;
	}

	private void buildModel(DocletTag tag) {
		this.value = tag.getValue();
		// Build all the parameters for this tag
		String[] params = tag.getParameters();
		this.parameters = new ArrayList();
		for (int i = 0; i < params.length; i++) {
			this.parameters.add(params[i]);
		}

		// Build all the properties of this tag
		this.properties = new Properties();
		Map namedParams = tag.getNamedParameterMap();

		if (namedParams != null) {
			Set keys = namedParams.keySet();
			for (Iterator iter = keys.iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String value = (String) namedParams.get(name);
				this.properties.put(name, value);
			}
		}
	}
}
