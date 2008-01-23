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
package org.eclipse.tigerstripe.workbench.internal.core.util.messages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A message list used during long operations to collect messages
 * 
 * @author Eric Dillon
 * 
 */
public class MessageList {

	private List<Message> list = new ArrayList<Message>();

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public void addMessage(Message message) {
		// TigerstripeRuntime.logInfoMessage("Adding message " +
		// message.getMessage() );
		list.add(message);
	}

	public List<Message> getAllMessages() {
		return list;
	}

	public void clear() {
		list.clear();
	}

	public List<Message> getMessagesBySeverity(int severity) {
		List<Message> result = new ArrayList<Message>();

		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Message msg = (Message) iter.next();
			if (msg.getSeverity() == severity) {
				result.add(msg);
			}
		}

		return result;
	}

	public boolean hasNoError() {
		return getMessagesBySeverity(Message.ERROR).isEmpty();
	}

	public String asText() {
		StringBuffer buf = new StringBuffer();
		for (Message msg : list) {
			buf.append(Message.severityToString(msg.getSeverity()) + ": "
					+ msg.getMessage() + "\n");
		}
		return buf.toString();
	}
}
