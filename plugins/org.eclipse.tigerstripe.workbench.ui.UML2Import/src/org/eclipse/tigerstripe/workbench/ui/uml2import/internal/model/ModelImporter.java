/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ImportLogUtilities;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.Utilities;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;

public class ModelImporter {

	private ArrayList diffList;
	private MessageList messages;
	private PrintWriter out;
	private ITigerstripeModelProject tigerstripeProject;

	private String importFilename;
	private String profilesDir;

	private Model model;
	private String modelLibrary;
	private Map<EObject, String> classMap;
	private Map<String, IAbstractArtifact> extractedArtifacts;
	private CoreArtifactSettingsProperty property;

	public ModelImporter(String importFilename,
			ITigerstripeModelProject tigerstripeProject, String profilesDir) {
		diffList = new ArrayList();
		this.importFilename = importFilename;
		this.tigerstripeProject = tigerstripeProject;
		this.profilesDir = profilesDir;
		// TODO Filter for active types in profile.
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		this.property = (CoreArtifactSettingsProperty) profile
				.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);

	}

	public boolean doInitialLoad() throws CoreException {

		try {
			messages = new MessageList();

			File importFile = new File(importFilename);
			File logFile = new File(importFile.getParent()
					+ "/UML2ToTigerstripeImport.log");

			out = new PrintWriter(new FileOutputStream(logFile));

			// Bug 252715 - Additional environment information.
			ImportLogUtilities.printHeaderInfo(out);
			
			
			String importText = "INFO : Import " + importFilename + " into "
					+ tigerstripeProject.getName();

			out.println(importText);

			Message message = new Message();
			message.setMessage(importText);
			message.setSeverity(2);
			messages.addMessage(message);

			model = null;
			// Simply get the model - nothing fancy now!
			// Set profile stuff
			Utilities.setupPaths();
			// Add specifics to point to MODEL_PROFILE.
			Utilities.addPaths(new File(getProfilesDir()));

			// load Model
			File modelFile = new File(importFilename);
			model = Utilities.openModelFile(modelFile);

			modelLibrary = getLibraryName(new File(getProfilesDir()), messages);

			// Get any implementations of the ImodelTrimmer from the extension
			// point

			IConfigurationElement[] elements = Platform
					.getExtensionRegistry()
					.getConfigurationElementsFor(
							"org.eclipse.tigerstripe.workbench.ui.UML2Import.umlImportModelTrimmer");
			for (IConfigurationElement element : elements) {

				final IModelTrimmer trimmer = (IModelTrimmer) element
						.createExecutableExtension("trimmer_class");
				String trimmerName = element.getAttribute("name");
				String trimmerText = "Model trimmed using trimmer "
						+ trimmerName + " extension";
				out.println("INFO : " + trimmerText);
				Message trimMessage = new Message();
				trimMessage.setMessage(trimmerText);
				trimMessage.setSeverity(2);
				messages.addMessage(message);

				SafeRunner.run(new ISafeRunnable() {
					public void handleException(Throwable exception) {
						EclipsePlugin.log(exception);
						out
								.println("ERROR  : TRIMMING MODEL with extension Point threw exception.");
						exception.printStackTrace(out);
					}

					public void run() throws Exception {
						out.println("INFO : TRIMMING MODEL");
						model = trimmer.trimModel(model);
					}

				});

			}

			out.flush();
			return true;

		} catch (Exception e) {
			String msgText = "Problem loading UML File " + importFilename;
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(0);
			messages.addMessage(newMsg);

			out.println("ERROR : " + msgText);
			e.printStackTrace(out);
			out.close();
			return false;
		}
	}

	public boolean doMapping() {

		// For every class, AssociationClass, Enumeration, etc
		// in the model, we need to provide a
		// CLASS name for the target - no need to get into a twist about
		// attributes etc at this stage.

		// A mapper can map to stuff that is not in the current profile.
		// removinng "unsupported artifacts" is handled by the extract and the
		// UI.

		// TODO Filter for active types in profile.

		if (model == null) {
			return false;
		}
		try {
			// Get any implementations of the IModelMapper from the extension
			// point
			IConfigurationElement[] elements = Platform
					.getExtensionRegistry()
					.getConfigurationElementsFor(
							"org.eclipse.tigerstripe.workbench.ui.UML2Import.umlImportModelMapper");
			for (IConfigurationElement element : elements) {
				final IModelMapper mapper = (IModelMapper) element
						.createExecutableExtension("mapper_class");
				String mapperName = element.getAttribute("name");
				String mapperText = "INFO : Model mapped using mapper "
						+ mapperName + " extension";
				out.println(mapperText);
				this.classMap = mapper.getMapping(model);
				SafeRunner.run(new ISafeRunnable() {
					public void handleException(Throwable exception) {
						EclipsePlugin.log(exception);
						out
								.println("ERROR  : GETTING MAPPING with extension Point threw exception.");
						exception.printStackTrace(out);
					}

					public void run() throws Exception {
						out.println("INFO : GETTING MAPPINGS");
						classMap = mapper.getMapping(model);
					}

				});
			}

		} catch (Exception e) {
			// If we didn't find a good one, we will use a default,
			// so just carry on
		}
		if (this.classMap == null) {
			// Use the default
			IModelMapper mapper = new DefaultModelMapper();
			this.classMap = mapper.getMapping(model);
		}
		out.println("INFO : MAPPINGS PASSED TO WIZARD");
		for (EObject o : classMap.keySet()) {
			if (o instanceof NamedElement) {
				out.println("INFO : Mapping Element :"
						+ ((NamedElement) o).getQualifiedName() + "    "
						+ classMap.get(o));
			}
		}
		out.flush();
		return true;

	}

	public boolean doSecondLoad() {

		UML2TS uML2TS = new UML2TS(getClassMap(), out, property);
		this.extractedArtifacts = uML2TS.extractArtifacts(model, modelLibrary,
				messages, this.tigerstripeProject);
		out.println("INFO : Extracted arrifact size :"
				+ this.extractedArtifacts.size());
		//out.println(messages.asText());
		out.flush();
		Utilities.tearDown();
		return false;
	}

	/** find the name of the library of primitive types */
	private String getLibraryName(File profilesDir, MessageList list) {
		try {

			File[] fList = profilesDir.listFiles();
			for (int i = 0; i < fList.length; i++) {
				File modelFile = fList[i];
				if (!modelFile.isFile())
					continue;
				if (!modelFile.getName().endsWith("uml2")
						&& !modelFile.getName().endsWith("uml"))
					continue;
				Model model;
				try {
					model = Utilities.openModelFile(modelFile);
					if (model != null)
						return model.getName();
				} catch (InvocationTargetException e) {
					TigerstripeRuntime.logErrorMessage(
							"InvocationTargetException detected", e);
					// this.out.close();
					return null;
				}
			}
		} catch (Exception e) {
			String msgText = "No UML Profile";
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(Message.WARNING);
			list.addMessage(newMsg);
			// this.out.println("Error : "+msgText);
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			// this.out.close();
			return null;
		}
		return null;

	}

	public ArrayList getDiffList() {
		return diffList;
	}

	public void setDiffList(ArrayList diffList) {
		this.diffList = diffList;
	}

	public ITigerstripeModelProject getTigerstripeProject() {
		return tigerstripeProject;
	}

	public String getImportFilename() {
		return importFilename;
	}

	public String getProfilesDir() {
		return profilesDir;
	}

	public Map<EObject, String> getClassMap() {
		return classMap;
	}

	public MessageList getMessages() {
		return messages;
	}

	public Map<String, IAbstractArtifact> getExtractedArtifacts() {
		return extractedArtifacts;
	}

	public PrintWriter getOut() {
		return out;
	}

}
