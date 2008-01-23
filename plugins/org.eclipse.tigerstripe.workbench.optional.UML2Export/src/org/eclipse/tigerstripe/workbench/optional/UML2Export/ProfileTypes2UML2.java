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
package org.eclipse.tigerstripe.workbench.optional.UML2Export;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.api.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.core.util.messages.Message;
import org.eclipse.tigerstripe.core.util.messages.MessageList;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.resource.UMLResource;

public class ProfileTypes2UML2 {

	private IWorkbenchProfileSession profileSession;
	private PrintWriter out;
	private Model model;

	private Map<String, Type> primitiveTypeMap;

	/** constructor */
	public ProfileTypes2UML2() {
		this.profileSession = API.getIWorkbenchProfileSession();
	}

	public Map<String, Type> loadTSProfileTypestoUML(File exportDir,
			String exportFilename, PrintWriter out, MessageList messages,
			IProgressMonitor monitor) throws TigerstripeException {

		this.out = out;
		this.primitiveTypeMap = new HashMap<String, Type>();

		String modelName = this.profileSession.getActiveProfile().getName()
				+ ".PrimitiveTypes";

		this.model = UMLFactory.eINSTANCE.createModel();
		model.setName(modelName);

		IPrimitiveTypeDef[] ptds = this.profileSession.getActiveProfile()
				.getPrimitiveTypeDefs(true);
		for (int i = 0; i < ptds.length; i++) {
			IPrimitiveTypeDef ptd = ptds[i];
			Type type = model.createOwnedPrimitiveType(ptd.getName());
			primitiveTypeMap.put(ptd.getName(), type);
		}

		if (!primitiveTypeMap.containsKey("string")) {
			Type type = model.createOwnedPrimitiveType("string");
			primitiveTypeMap.put("string", type);
		}

		URI fileUri = URI.createFileURI(exportDir.getAbsolutePath());

		String msgText = "Created " + primitiveTypeMap.size()
				+ " primitive types";
		Message message = new Message();
		message.setMessage(msgText);
		message.setSeverity(2);
		messages.addMessage(message);

		out.println(msgText);

		msgText = "Modeltypes output to : " + fileUri.toFileString();
		message = new Message();
		message.setMessage(msgText);
		message.setSeverity(2);
		messages.addMessage(message);
		out.println(msgText);

		save(model, fileUri.appendSegment("umlProfile").appendSegment(
				exportFilename + ".ModelTypes").appendFileExtension(
				UMLResource.FILE_EXTENSION));

		return this.primitiveTypeMap;

	}

	public Model getModel() {
		return model;
	}

	public static ResourceSet resourceSet = new ResourceSetImpl();

	/**
	 * Save the model to a Resource.
	 * 
	 * @param package_
	 * @param uri
	 */
	protected void save(org.eclipse.uml2.uml.Package packageToSave,
			org.eclipse.emf.common.util.URI fileUri) {

		Resource resource;// = RESOUR.createResource(uri);
		resource = resourceSet.createResource(fileUri);
		EList contents = resource.getContents();

		contents.add(packageToSave);

		for (Iterator allContents = UML2Util.getAllContents(packageToSave,
				true, false); allContents.hasNext();) {

			EObject eObject = (EObject) allContents.next();

			if (eObject instanceof Element) {
				contents
						.addAll(((Element) eObject).getStereotypeApplications());
			}
		}

		try {
			resource.save(null);
			out.println("Done.");
		} catch (IOException ioe) {
			out.println(ioe.getMessage());
		}

	}
}
