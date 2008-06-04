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
package org.eclipse.tigerstripe.workbench.internal.core.profile;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;

public class ProfileValidator {

	private MessageList messageList;

	public ProfileValidator() {

	}

	public boolean validate(IWorkbenchProfile profile, MessageList messageList) {
		this.messageList = messageList;

		if (this.messageList == null) {
			messageList = new MessageList();
		}

		messageList.clear();

		checkPrimitiveTypes(profile);
		checkStereotypes(profile);

		return messageList.hasNoError();
	}

	private void reportError(String msgText) {
		internalReport(msgText, Message.ERROR);
	}

	private void reportWarning(String msgText) {
		internalReport(msgText, Message.WARNING);
	}

	private void internalReport(String msgText, int severity) {
		Message msg = new Message();
		msg.setMessage(msgText);
		msg.setSeverity(severity);
		if (messageList != null) {
			messageList.addMessage(msg);
		}
	}

	/*
	 * A method that checks the primitive types in a workbench profile, issuing
	 * errors/warnings via the Eclipse "Problems View" when problems are found.
	 * For now, it just checks the validity of the primitive type names, but
	 * this could grow to other tests on primitive types in the future
	 */
	private void checkPrimitiveTypes(IWorkbenchProfile workbenchProfile) {
		Collection<IPrimitiveTypeDef> primitiveTypes = workbenchProfile
				.getPrimitiveTypeDefs(false);
		for (IPrimitiveTypeDef type : primitiveTypes) {
			if (!type.isValidName()) {
				reportError("Invalid "
						+ ArtifactMetadataFactory.INSTANCE.getMetadata(
								IPrimitiveTypeImpl.class.getName()).getLabel(
								type) + " name '" + type.getName()
						+ "' detected");
			} else if (!type.isRecommendedName()) {
				reportWarning("Type name '"
						+ type.getName()
						+ "' is not recommended as the name of a "
						+ ArtifactMetadataFactory.INSTANCE.getMetadata(
								IPrimitiveTypeImpl.class.getName()).getLabel(
								type));
			}
		}
	}

	private void checkStereotypes(IWorkbenchProfile workbenchProfile) {

		ArrayList<String> duplicateStereotypeNameList = new ArrayList<String>();

		for (IStereotype stereotype : workbenchProfile.getStereotypes()) {

			if (checkValidStereotype(stereotype, workbenchProfile)) {

				if (duplicateStereotypeNameList.contains(stereotype.getName())) {
					reportError("Duplicate Annotation Name '"
							+ stereotype.getName() + "' in profile.");
				} else {
					duplicateStereotypeNameList.add(stereotype.getName());
				}
			}
		}
	}

	private static final char[] invalidStereotypeNameChars = { ' ', ';', '\'',
			':', ',', '<', '>', '"', '/', '?', '!', '@', '#', '$', '%', '^',
			'&', '*', '(', ')', '-', '+', '=', '|', '\\' };

	private boolean checkValidStereotype(IStereotype stereotype,
			IWorkbenchProfile profile) {
		String name = stereotype.getName();
		if (!isValidName(name)) {
			reportError("Invalid Annotation Name '" + stereotype.getName()
					+ "' in profile.");
		}

		for (IStereotypeAttribute attr : stereotype.getAttributes()) {
			if (!isValidName(attr.getName())) {
				reportError("Invalid attribute name '" + attr.getName()
						+ "' in Annotation '" + stereotype.getName()
						+ "' in profile.");
			}
		}

		return true;
	}

	private boolean isValidName(String name) {
		for (int i = 0; i < name.length(); i++) {
			for (char c : invalidStereotypeNameChars) {
				if (name.charAt(i) == c)
					return false;
			}
		}
		return true;
	}

}
