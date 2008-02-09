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
package org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics;

import java.util.Properties;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.StandardSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

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
			ITigerstripeModelProject tsProject = getArtifact().getTigerstripeProject();
			getArtifact().getTigerstripeProject();
			IPluginConfig[] refs = tsProject.getPluginConfigs();

			for (IPluginConfig ref : refs) {
				// if jvtPlugin
				if (OSSJ_JVT_SPEC.equals(ref.getPluginId())) {
					if (ref.getProperty("defaultInterfacePackage") == null){
						this.interfaceProperties.put("package","");
					}else {
						this.interfaceProperties.put("package", ref.getProperty("defaultInterfacePackage"));
					}
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
				OssjTags.INTERFACE_TAG);
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
