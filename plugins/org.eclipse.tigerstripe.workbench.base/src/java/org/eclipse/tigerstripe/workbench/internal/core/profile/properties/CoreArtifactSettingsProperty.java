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
package org.eclipse.tigerstripe.workbench.internal.core.profile.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IWorkbenchProfileProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;

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
			IDatatypeArtifact.class.getName(),
			IEventArtifact.class.getName(),
			IQueryArtifact.class.getName(),
			IUpdateProcedureArtifact.class.getName(),
			ISessionArtifact.class.getName(),
			IAssociationArtifact.class.getName(),
			IAssociationClassArtifact.class.getName(),
			IExceptionArtifact.class.getName(),
			IPrimitiveTypeArtifact.class.getName(),
			IEnumArtifact.class.getName(), 
			IDependencyArtifact.class.getName(),
			IPackageArtifact.class.getName()};

	// Note thst this is used to get them in the same order in the UI.
	// however the tostring is done by the Full Name, not the label
	// eg ManageedEntity rather than Entity
	//    Notification rather than Event
	private TreeMap<String, ArtifactSettingDetails> details = new TreeMap<String, ArtifactSettingDetails>();

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
			String artifactType = MigrationHelper
					.profileMigrateCoreArtifactSettingsArtifactType(detailElem
							.attributeValue("artifactType"));
			String content = detailElem.getText();
			if (artifactType != null && artifactType.length() != 0) {
				ArtifactSettingDetails aSdetails = new ArtifactSettingDetails();
				aSdetails.parseFromSerializedString(content);
				aSdetails.setArtifactType(artifactType);
				this.details.put(artifactType, aSdetails);
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
