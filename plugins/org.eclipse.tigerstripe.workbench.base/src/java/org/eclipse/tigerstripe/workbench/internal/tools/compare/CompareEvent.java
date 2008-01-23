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
package org.eclipse.tigerstripe.workbench.internal.tools.compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IEventDescriptorEntry;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjEventSpecifics;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEventArtifact;

public class CompareEvent {

	public static ArrayList<Difference> compareEventSpecifics(
			IEventArtifact aArtifact, IEventArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();

		IOssjEventSpecifics aSpecs = (IOssjEventSpecifics) aArtifact
				.getIStandardSpecifics();
		IOssjEventSpecifics bSpecs = (IOssjEventSpecifics) bArtifact
				.getIStandardSpecifics();
		// Shove it all into one Map per side
		IEventDescriptorEntry[] aEDEs = (IEventDescriptorEntry[]) aSpecs
				.getEventDescriptorEntries();
		IEventDescriptorEntry[] aCEDEs = aSpecs
				.getCustomEventDescriptorEntries();
		IEventDescriptorEntry[] bEDEs = (IEventDescriptorEntry[]) bSpecs
				.getEventDescriptorEntries();
		IEventDescriptorEntry[] bCEDEs = bSpecs
				.getCustomEventDescriptorEntries();
		// Can we sure of order? Possibly not.
		Map aEntryMap = new HashMap();
		Map bEntryMap = new HashMap();
		for (IEventDescriptorEntry entry : aEDEs) {
			aEntryMap.put(entry.getLabel(), entry.getPrimitiveType());
		}
		// for (IEventDescriptorEntry entry :
		// aCEDEs){aEntryMap.put(entry.getLabel(),entry.getPrimitiveType());}
		for (IEventDescriptorEntry entry : bEDEs) {
			bEntryMap.put(entry.getLabel(), entry.getPrimitiveType());
		}
		// for (IEventDescriptorEntry entry :
		// bCEDEs){bEntryMap.put(entry.getLabel(),entry.getPrimitiveType());}

		for (Object label : aEntryMap.keySet()) {
			if (!bEntryMap.keySet().contains(label)) {
				// Missing from B
				differences.add(new Difference(
						// "","Artifact EventSpecifics",
						// aArtifact.getFullyQualifiedName(),
						// "EventDescriptorEntry:"+label+":Missing from
						// B","",""));
						"presence", aArtifact.getFullyQualifiedName(),
						bArtifact.getFullyQualifiedName(),
						"Event:Specifics:EventDescriptorEntry", label
								.toString(), "present", "absent"));
			} else {
				// compare the type
				String aType = (String) aEntryMap.get(label);
				String bType = (String) bEntryMap.get(label);
				if (!aType.equals(bType)) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Event:Specifics:EventDescriptorEntry", label
									.toString(), aType, bType));
				}
				// remove it from B
				bEntryMap.remove(label);
			}
		}
		for (Object label : bEntryMap.keySet()) {
			// Missing from A
			differences.add(new Difference(
					// "","Artifact EventSpecifics",
					// aArtifact.getFullyQualifiedName(),
					// "EventDescriptorEntry:"+label+":Missing from A","",""));
					"presence", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
					"Event:Specifics:EventDescriptorEntry", label.toString(),
					"absent", "present"));
		}

		// Repeat for custom Entries
		aEntryMap = new HashMap();
		bEntryMap = new HashMap();
		// for (IEventDescriptorEntry entry : aEDEs
		// ){aEntryMap.put(entry.getLabel(),entry.getPrimitiveType());}
		for (IEventDescriptorEntry entry : aCEDEs) {
			aEntryMap.put(entry.getLabel(), entry.getPrimitiveType());
		}
		// for (IEventDescriptorEntry entry : bEDEs
		// ){bEntryMap.put(entry.getLabel(),entry.getPrimitiveType());}
		for (IEventDescriptorEntry entry : bCEDEs) {
			bEntryMap.put(entry.getLabel(), entry.getPrimitiveType());
		}

		for (Object label : aEntryMap.keySet()) {
			if (!bEntryMap.keySet().contains(label)) {
				// Missing from B
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(),
						"Event:Specifics:CustomEventDescriptorEntry", label
								.toString(), "present", "absent"));
			} else {
				// compare the type
				String aType = (String) aEntryMap.get(label);
				String bType = (String) bEntryMap.get(label);
				if (!aType.equals(bType)) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Event:Specifics:CustomEventDescriptorEntry", label
									.toString(), aType, bType));
				}
				// remove it from B
				bEntryMap.remove(label);
			}
		}
		for (Object label : bEntryMap.keySet()) {
			// Missing from A
			differences.add(new Difference(
					// "","Artifact EventSpecifics",
					// aArtifact.getFullyQualifiedName(),
					// "EventDescriptorEntry:"+label+":Missing from A","",""));
					"presence", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
					"Event:Specifics:CustomEventDescriptorEntry", label
							.toString(), "absent", "present"));
		}

		return differences;
	}

}
