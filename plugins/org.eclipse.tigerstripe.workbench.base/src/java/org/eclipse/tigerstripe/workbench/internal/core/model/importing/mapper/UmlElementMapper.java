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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.mapper;

import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;

/**
 * 
 * @author Eric Dillon
 * 
 */
public abstract class UmlElementMapper {

	private final static String DEFAULT_ATTRIBUTENAME = "attribute";
	private static int ATTRIBUTE_COUNTER = 0;

	private MessageList messageList;

	private final static String[] illegalStrings = { " ", ",", ";", ":", "'",
			"\"", "(", ")", "{", "}", "[", "]", "*", "&", "^", "%", "$", "#",
			"@", "!", "?", "<", ">", ".", "`", "~", "|", "/", "\\", "+", "=",
			"-" };

	protected UmlElementMapper(MessageList list) {
		if (list == null) {
			this.messageList = new MessageList();
		} else {
			this.messageList = list;
		}
	}

	/**
	 * Returns the MessageList of messages collected thru this UmlElementMapper
	 * 
	 * @return
	 */
	public MessageList getMessageList() {
		return this.messageList;
	}

	public String removeIllegalCharacters(String name) {
		String result = name;
		// remove illegal characters
		for (int i = 0; i < illegalStrings.length; i++) {
			result = result.replace(illegalStrings[i], "");
		}
		return result;
	}

	protected String getValidAttributeName(String name) {

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
			getMessageList().addMessage(msg);
		}

		// can't begin with a number
		if (result.charAt(0) >= '0' && result.charAt(0) <= '9') {
			result = result + "_";
			Message msg = new Message();
			msg.setMessage("Mapped attribute name '" + name + "' to " + result);
			msg.setSeverity(Message.WARNING);
			getMessageList().addMessage(msg);
		}

		return result;
	}

	protected String getValidPackageName(String name) {
		String result = removeIllegalCharacters(name);

		// can't begin with a number
		if (result.charAt(0) >= '0' && result.charAt(0) <= '9') {
			result = result + "_";
			Message msg = new Message();
			msg.setMessage("Mapped package name '" + name + "' to " + result);
			msg.setSeverity(Message.WARNING);
			getMessageList().addMessage(msg);
		}

		return result;
	}

	protected String getValidOperationName(String name) {
		return getValidAttributeName(name);
	}

	protected String getValidParameterName(String name) {
		return getValidAttributeName(name);
	}

	// protected String lookForDescriptionInTaggedValues( ModelElement
	// modelElement ) {
	// String result = "";
	// Collection<TaggedValue> taggedValues = modelElement.getTaggedValue();
	// for( TaggedValue value : taggedValues ) {
	// if ( "documentation".equals(value.getName()) ) {
	// Collection<String> dataValues = value.getDataValue();
	// StringBuffer buffer = new StringBuffer();
	// for( String str : dataValues ) {
	// buffer.append(str);
	// }
	// result = buffer.toString();
	// }
	// }
	//		
	// return result;
	// }
}
