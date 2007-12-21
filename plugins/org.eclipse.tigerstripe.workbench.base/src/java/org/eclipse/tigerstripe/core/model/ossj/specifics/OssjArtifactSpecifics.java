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
package org.eclipse.tigerstripe.core.model.ossj.specifics;

import java.util.Properties;

import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.StandardSpecifics;
import org.eclipse.tigerstripe.core.model.Tag;
import org.eclipse.tigerstripe.core.plugin.ossj.OssjInterfaceModel;

public abstract class OssjArtifactSpecifics extends StandardSpecifics implements
		IOssjArtifactSpecifics {

	protected final static String OSSJ_JVT_SPEC = "ossj-jvt-spec";

	public OssjArtifactSpecifics(AbstractArtifact artifact) {
		super(artifact);
	}

	private boolean isSingleExtensionDatatype = false;

	private boolean isSessionFactoryMethods = false;

	public boolean isSingleExtensionType() {
		return isSingleExtensionDatatype;
	}

	public void setSingleExtensionType(boolean single) {
		this.isSingleExtensionDatatype = single;
	}

	public void setSessionFactoryMethods(boolean sessionBased) {
		this.isSessionFactoryMethods = sessionBased;
	}

	public boolean isSessionFactoryMethods() {
		return this.isSessionFactoryMethods;
	}

	private Properties interfaceProperties = new Properties();

	public Properties getInterfaceProperties() {
		return interfaceProperties;
	}

	public void setInterfaceProperties(Properties interfaceProperties) {
		this.interfaceProperties = interfaceProperties;
	}

	public void mergeInterfaceProperties(Properties interfaceProperties) {
		for (Object key : interfaceProperties.keySet()) {
			this.interfaceProperties.put(key, interfaceProperties.get(key));
		}
	}

	public void applyDefaults() {
		try {
			this.interfaceProperties.put("package", "com.mycompany");
			ITigerstripeProject tsProject = getArtifact().getIProject();
			getArtifact().getIProject();
			IPluginReference[] refs = tsProject.getPluginReferences();

			for (IPluginReference ref : refs) {
				// if jvtPlugin
				if (OSSJ_JVT_SPEC.equals(ref.getPluginId())) {
					Properties refProps = ref.getProperties();
					this.interfaceProperties.put("package", refProps
							.getProperty("defaultInterfacePackage", ""));
				}
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	@Override
	public void build() {
		super.build();
		// extract the interface properties
		Tag intfTag = getArtifact().getFirstTagByName(
				OssjInterfaceModel.INTERFACE_TAG);
		if (intfTag != null) {
			interfaceProperties = intfTag.getProperties();
		}

		// Extract the base type
		// Extract Extensibility flag
		Tag tag = getArtifact()
				.getFirstTagByName(getArtifact().getMarkingTag());
		if (tag != null) {
			Properties props = tag.getProperties();
			String single = props.getProperty("isSingleExtensionType", "false");
			setSingleExtensionType("true".equalsIgnoreCase(single));

			String sessionBased = props.getProperty("isSessionFactoryMethods",
					"false");
			setSessionFactoryMethods("true".equalsIgnoreCase(sessionBased));

		} else {
			setSingleExtensionType(false);
			setSessionFactoryMethods(false);
		}

	}

}
