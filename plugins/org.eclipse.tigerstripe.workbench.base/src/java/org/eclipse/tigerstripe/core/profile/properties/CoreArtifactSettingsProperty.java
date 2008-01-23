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
package org.eclipse.tigerstripe.core.profile.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IEventArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IExceptionArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfileProperty;
import org.eclipse.tigerstripe.api.profile.properties.IWorkbenchPropertyLabels;

/**
 * Contains the details of the core artifact settings
 * 
 * @see IWorkbenchPropertyLabels#CORE_ARTIFACTS_SETTINGS
 * @author Eric Dillon
 * 
 */
public class CoreArtifactSettingsProperty extends BaseWorkbenchProfileProperty
		implements IWorkbenchProfileProperty {

	private final static String COREARTIFACTSETTINGS = "coreArtifactsSettings";

	private final static String[] coreArtifactsList = {
			IManagedEntityArtifact.class.getName(),
			IDatatypeArtifact.class.getName(), IEventArtifact.class.getName(),
			IQueryArtifact.class.getName(),
			IUpdateProcedureArtifact.class.getName(),
			ISessionArtifact.class.getName(),
			IAssociationArtifact.class.getName(),
			IAssociationClassArtifact.class.getName(),
			IExceptionArtifact.class.getName(),
			IPrimitiveTypeArtifact.class.getName(),
			IEnumArtifact.class.getName(), IDependencyArtifact.class.getName() };

	private HashMap<String, ArtifactSettingDetails> details = new HashMap<String, ArtifactSettingDetails>();

	public CoreArtifactSettingsProperty() {
		checkDefaults();
	}

	public ArtifactSettingDetails getDetailsForType(String artifactType) {
		return details.get(artifactType);
	}

	public ArtifactSettingDetails[] getDetails() {
		return details.values().toArray(
				new ArtifactSettingDetails[details.size()]);
	}

	public void parseFromSerializedString(String serializedString)
			throws TigerstripeException {

		details.clear();
		Document document = documentFromString(serializedString);
		Element rootElem = document.getRootElement();

		for (Iterator<Element> iter = rootElem.elementIterator("details"); iter
				.hasNext();) {
			Element detailElem = iter.next();
			String artifactType = detailElem.attributeValue("artifactType");
			String content = detailElem.getText();
			if (artifactType != null && artifactType.length() != 0) {
				ArtifactSettingDetails details = new ArtifactSettingDetails();
				details.parseFromSerializedString(content);
				details.setArtifactType(artifactType);
				this.details.put(artifactType, details);
			}
		}

		checkDefaults();
	}

	/**
	 * Validates that all details are present, or else add whatever is missing
	 * 
	 */
	public void checkDefaults() {
		for (String name : coreArtifactsList) {
			if (!details.containsKey(name)) {
				ArtifactSettingDetails dets = new ArtifactSettingDetails();
				dets.setArtifactType(name);
				details.put(name, dets);
			}
		}
	}

	public String serializeToString() {
		Document document = DocumentFactory.getInstance().createDocument();
		Element rootElem = DocumentFactory.getInstance().createElement(
				COREARTIFACTSETTINGS);
		document.setRootElement(rootElem);

		for (String artifactName : details.keySet()) {
			ArtifactSettingDetails dets = details.get(artifactName);
			Element detsElem = rootElem.addElement("details");
			detsElem.addAttribute("artifactType", artifactName);
			detsElem.addText(dets.serializeToString());
		}

		return document.asXML();
	}

	public String[] getEnabledArtifactTypes() {
		ArrayList<String> result = new ArrayList<String>();

		for (String artifactName : details.keySet()) {
			ArtifactSettingDetails dets = details.get(artifactName);
			if (dets.isEnabled()) {
				result.add(artifactName);
			}
		}

		return result.toArray(new String[result.size()]);
	}

	public boolean getPropertyValue(String name) {
		throw new UnsupportedOperationException("Not supported for this class");
	}

	public void setPropertyValue(String name, boolean value) {
		throw new UnsupportedOperationException("Not supported for this class");
	}

}
