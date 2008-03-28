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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifactTag;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventDescriptorEntry;
import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IEventDescriptorEntry;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEventSpecifics;

public class OssjEventSpecifics extends OssjArtifactSpecifics implements
		IOssjEventSpecifics {

	private final static String EVENT_DESCRIPTOR_TAG = AbstractArtifactTag.PREFIX
			+ "event-descriptor";

	private final static String CUSTOMEVENT_DESCRIPTOR_TAG = AbstractArtifactTag.PREFIX
			+ "customEvent-descriptor";

	private Collection eventDescriptorEntries;

	private Collection customEventDescriptorEntries;

	public OssjEventSpecifics(AbstractArtifact artifact) {
		super(artifact);
		eventDescriptorEntries = new ArrayList();
		customEventDescriptorEntries = new ArrayList();
	}

	public IEventDescriptorEntry[] getEventDescriptorEntries() {
		IEventDescriptorEntry[] result = new IEventDescriptorEntry[eventDescriptorEntries
				.size()];
		return (IEventDescriptorEntry[]) eventDescriptorEntries.toArray(result);
	}

	public void setEventDescriptorEntries(IEventDescriptorEntry[] entries) {
		eventDescriptorEntries.clear();
		for (int i = 0; i < entries.length; i++) {
			eventDescriptorEntries.add(entries[i]);
		}
	}

	public IEventDescriptorEntry[] getCustomEventDescriptorEntries() {
		IEventDescriptorEntry[] result = new IEventDescriptorEntry[customEventDescriptorEntries
				.size()];
		return (IEventDescriptorEntry[]) customEventDescriptorEntries
				.toArray(result);
	}

	public void setCustomEventDescriptorEntries(IEventDescriptorEntry[] entries) {
		customEventDescriptorEntries.clear();
		for (int i = 0; i < entries.length; i++) {
			customEventDescriptorEntries.add(entries[i]);
		}
	}

	@Override
	public void build() {
		super.build();

		Collection evTags = getArtifact().getTagsByName(EVENT_DESCRIPTOR_TAG);
		for (Iterator iter = evTags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			EventDescriptorEntry entry = extractEntry(tag);
			if (entry != null)
				eventDescriptorEntries.add(entry);
		}

		evTags = getArtifact().getTagsByName(CUSTOMEVENT_DESCRIPTOR_TAG);
		for (Iterator iter = evTags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			EventDescriptorEntry entry = extractEntry(tag);
			if (entry != null)
				customEventDescriptorEntries.add(entry);
		}
	}

	private EventDescriptorEntry extractEntry(Tag tag) {
		String label = tag.getProperties().getProperty("label", "");
		String type = tag.getProperties().getProperty("type", "");
		if (!"".equals(label) && !"".equals(type))
			return new EventDescriptorEntry(label, type);
		else
			return null;
	}

}
