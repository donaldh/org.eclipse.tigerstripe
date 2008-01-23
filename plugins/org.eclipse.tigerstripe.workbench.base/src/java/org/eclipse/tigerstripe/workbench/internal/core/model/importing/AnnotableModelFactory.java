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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.DBImporter;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.DBModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.UML2Importer;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.UML2ModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * The main factory for AnnotableModels
 * 
 * This is a singleton class.
 * 
 * @author Eric Dillon
 * 
 */
public class AnnotableModelFactory {

	private static AnnotableModelImporter[] registeredImporters;

	private static IModelImportConfiguration[] registeredImporterConfigs;

	private static AnnotableModelFactory instance = null;

	private static HashMap importersMap = new HashMap();

	private static HashMap importerConfigsMap = new HashMap();

	// Temp file used during XSLT if needed
	private File tempFile;

	public static AnnotableModelFactory getInstance() {
		if (instance == null) {
			try {
				registeredImporters = new AnnotableModelImporter[] {
				// new XMIImporter(),
						new DBImporter(), new UML2Importer() };
				registeredImporterConfigs = new IModelImportConfiguration[] {
						// new XMIModelImportConfiguration(null),
						new DBModelImportConfiguration(null),
						new UML2ModelImportConfiguration(null) };
				// } catch (TigerstripeException e) {
				// TigerstripeRuntime.logErrorMessage("TigerstripeException
				// detected", e);
			} finally {
				if (registeredImporters == null) {
					registeredImporters = new AnnotableModelImporter[0];
				}
			}
			instance = new AnnotableModelFactory();
			instance.init();
		}
		return instance;
	}

	private AnnotableModelFactory() {
		// empty
	}

	/**
	 * Initialize the factory by registering all the known model importers
	 * 
	 */
	private void init() {
		for (int i = 0; i < registeredImporters.length; i++) {
			registerImporter(registeredImporters[i],
					registeredImporterConfigs[i]);
		}
	}

	private void cleanTempFile() {
		if (this.tempFile != null) {
			tempFile.delete();
		}
	}

	private void registerImporter(AnnotableModelImporter importer,
			IModelImportConfiguration config) {
		importersMap.put(importer.getClass().getName(), importer);
		importerConfigsMap.put(importer.getClass().getName(), config);
	}

	public IModelImportConfiguration makeConfiguration(Class importerClass,
			ITigerstripeProject referenceProject) {
		IModelImportConfiguration config = (IModelImportConfiguration) importerConfigsMap
				.get(importerClass.getName());
		return config.make(referenceProject);
	}

	public ModelImportResult importModelAfterXSLT(String modelUri,
			Class importerClass, String xslUri, MessageList list,
			ModelImporterListener listener, IModelImportConfiguration config,
			ITigerstripeProject targetProject) throws ModelImportException {
		String transformedModelUri = applyXSLT(modelUri, xslUri);

		return importModel(transformedModelUri, importerClass, list, listener,
				config, targetProject);
	}

	private final static String DEFAULT_XSLTS_PREFIX = "/org/eclipse/tigerstripe/core/model/importing/resources/";

	private final static String[] defaultXslts = { AnnotableModelFactory.class
			.getResource(DEFAULT_XSLTS_PREFIX + "multiplicityRange-to-MDR.xsl")
			.toString(),
	// AnnotableModelFactory.class.getResource( DEFAULT_XSLTS_PREFIX +
	// "removeUMLAttributeContent.xsl").toString()
	};

	private String applyDefaultXSLTs(String modelUri)
			throws ModelImportException {
		String initialModelUri = modelUri;
		String finalModelUri = null;

		for (int i = 0; i < defaultXslts.length; i++) {
			finalModelUri = applyXSLT(initialModelUri, defaultXslts[i]);
		}
		return finalModelUri;
	}

	private String applyXSLT(String modelUri, String xslUri)
			throws ModelImportException {

		TransformerFactory tFactory = TransformerFactory.newInstance();
		String result = null;
		try {
			tempFile = File.createTempFile("tigerstripe", ".xml");
			result = tempFile.toURL().toString();
			Transformer transformer = tFactory.newTransformer(new StreamSource(
					xslUri));
			transformer.transform(new StreamSource(modelUri), new StreamResult(
					new FileOutputStream(tempFile)));

		} catch (TransformerConfigurationException e) {
			throw new ModelImportException(e.getLocalizedMessage(), e);
		} catch (TransformerException e) {
			throw new ModelImportException(e.getLocalizedMessage(), e);
		} catch (FileNotFoundException e) {
			throw new ModelImportException(e.getLocalizedMessage(), e);
		} catch (IOException e) {
			throw new ModelImportException(e.getLocalizedMessage(), e);
		}

		return result;
	}

	public ModelImportResult importModel(String modelUri, Class importerClass,
			MessageList list, ModelImporterListener listener,
			IModelImportConfiguration config, ITigerstripeProject targetProject)
			throws ModelImportException {

		String finalModelUri = applyDefaultXSLTs(modelUri);

		AnnotableModelImporter importer = (AnnotableModelImporter) importersMap
				.get(importerClass.getName());

		// make sure there's a message list even if the client doesn't want to
		// see it!
		if (list == null) {
			list = new MessageList();
		}

		ModelImportResult result = importer.importFromURI(finalModelUri, list,
				listener, config, targetProject);

		cleanTempFile();

		return result;
	}

	public ModelImportResult importModelFromDb(MessageList list,
			ModelImporterListener listener, ITigerstripeProject targetProject,
			IModelImportConfiguration config) {
		DBImporter importer = new DBImporter();

		// make sure there's a message list even if the client doesn't want to
		// see it!
		if (list == null) {
			list = new MessageList();
		}

		ModelImportResult result = importer.importFromDB(list, listener,
				config, targetProject);

		return result;
	}

	public ModelImportResult importModelFromUML2(MessageList list,
			ModelImporterListener listener, ITigerstripeProject targetProject,
			IModelImportConfiguration config) {

		UML2Importer importer = new UML2Importer();

		if (list == null) {
			list = new MessageList();
		}

		ModelImportResult result = importer.importFromUML2(list, listener,
				config, targetProject);

		return result;
	}
}
