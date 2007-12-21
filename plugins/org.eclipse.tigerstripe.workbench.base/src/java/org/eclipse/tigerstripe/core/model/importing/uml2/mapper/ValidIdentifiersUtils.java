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
package org.eclipse.tigerstripe.core.model.importing.uml2.mapper;

import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.util.messages.Message;
import org.eclipse.tigerstripe.core.util.messages.MessageList;

public class ValidIdentifiersUtils {

	private final static String DEFAULT_ATTRIBUTENAME = "attribute";

	private static int ATTRIBUTE_COUNTER = 0;

	private final static String[] illegalStrings = { " ", ",", ";", ":", "'",
			"\"", "(", ")", "{", "}", "[", "]", "*", "&", "^", "%", "$", "#",
			"@", "!", "?", "<", ">", ".", "`", "~", "|", "/", "\\", "+", "=",
			"-" };

	private final static String[] illegalPackageStrings = { " ", ",", ";", ":",
			"'", "\"", "(", ")", "{", "}", "[", "]", "*", "&", "^", "%", "$",
			"#", "@", "!", "?", "<", ">", "`", "~", "|", "/", "\\", "+", "=",
			"-", "_" };

	public static String removeIllegalCharacters(String name) {
		String result = name;
		// remove illegal characters
		for (int i = 0; i < illegalStrings.length; i++) {
			result = result.replace(illegalStrings[i], "");
		}
		return result;
	}

	public static String removeIllegalPackageCharacters(String name) {
		String result = name;
		// remove illegal characters
		for (int i = 0; i < illegalPackageStrings.length; i++) {
			result = result.replace(illegalPackageStrings[i], "");
		}
		return result;
	}

	protected static String getValidAttributeName(MessageList messageList,
			String name) {

		if (name == null)
			return "";

		String result = removeIllegalCharacters(name);

		// can't be empty
		if ("".equals(result)) {
			result = DEFAULT_ATTRIBUTENAME
					+ String.valueOf(ATTRIBUTE_COUNTER++);
			Message msg = new Message();
			msg.setMessage("Mapped attribute name '" + name + "' to " + result);
			msg.setSeverity(Message.WARNING);
			messageList.addMessage(msg);
		}

		// can't begin with a number
		if (result.charAt(0) >= '0' && result.charAt(0) <= '9') {
			result = result + "_";
			Message msg = new Message();
			msg.setMessage("Mapped attribute name '" + name + "' to " + result);
			msg.setSeverity(Message.WARNING);
			messageList.addMessage(msg);
		}

		return result;
	}

	protected static String getValidPackageName(MessageList messageList,
			String name, ITigerstripeProject targetProject, String elementName) {
		String result = removeIllegalPackageCharacters(name);

		if ("".equals(result)) {
			Message msg = new Message();
			msg.setMessage("Use of Default package name not encouraged for '"
					+ elementName + "'.");
			msg.setSeverity(Message.WARNING);
			messageList.addMessage(msg);
			return "";
		}

		// can't begin with a number
		if (result.charAt(0) >= '0' && result.charAt(0) <= '9') {
			result = result + "_";
			Message msg = new Message();
			msg.setMessage("Mapped package name '" + name + "' to " + result);
			msg.setSeverity(Message.WARNING);
			messageList.addMessage(msg);
		}

		return result;
	}

	protected static String getValidOperationName(MessageList messageList,
			String name) {
		return getValidAttributeName(messageList, name);
	}

	protected static String getValidParameterName(MessageList messageList,
			String name) {
		return getValidAttributeName(messageList, name);
	}

	protected static String getValidArtifactName(MessageList messageList,
			String name) {
		return getValidAttributeName(messageList, name);
	}

}
