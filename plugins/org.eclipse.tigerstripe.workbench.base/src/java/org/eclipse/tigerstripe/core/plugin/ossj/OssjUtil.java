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
package org.eclipse.tigerstripe.core.plugin.ossj;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.core.model.QueryArtifact;
import org.eclipse.tigerstripe.core.model.Tag;
import org.eclipse.tigerstripe.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.core.plugin.PluginRef;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

/**
 * @author Eric Dillon
 * 
 */
public class OssjUtil {

	private ArtifactManager artifactManager;

	private PluginRef pluginRef;

	public OssjUtil(ArtifactManager artifactManager, PluginRef pluginRef) {
		this.artifactManager = artifactManager;
		this.pluginRef = pluginRef;
	}

	public boolean isTSArtifact(String fullyQualifiedName) {
		AbstractArtifact artifact = artifactManager
				.getArtifactByFullyQualifiedName(fullyQualifiedName, true,
						new TigerstripeNullProgressMonitor());

		if (artifact == null)
			return false;
		else
			return true;
	}

	public OssjInterfaceModel interfaceOf(String fullyQualifiedName)
			throws TigerstripeException {
		AbstractArtifact artifact = artifactManager
				.getArtifactByFullyQualifiedName(fullyQualifiedName, true,
						new TigerstripeNullProgressMonitor());

		if (artifact == null)
			throw new TigerstripeException("Interface look up of '"
					+ fullyQualifiedName + "' failed.");

		if (artifact instanceof ManagedEntityArtifact)
			return new ValueInterfaceModel(artifact, this.pluginRef);
		else if (artifact instanceof DatatypeArtifact)
			return new DatatypeInterfaceModel(artifact, this.pluginRef);
		else if (artifact instanceof QueryArtifact)
			return new QueryInterfaceModel(artifact, this.pluginRef);
		else if (artifact instanceof UpdateProcedureArtifact)
			return new UpdateProcedureInterfaceModel(artifact, this.pluginRef);
		else
			return new OssjInterfaceModel(artifact, pluginRef);
	}

	public ValueIteratorInterfaceModel valueIteratorInterfaceOf(
			String fullyQualifiedName) throws TigerstripeException {
		AbstractArtifact artifact = artifactManager
				.getArtifactByFullyQualifiedName(fullyQualifiedName, true,
						new TigerstripeNullProgressMonitor());

		if (artifact instanceof ManagedEntityArtifact)
			return new ValueIteratorInterfaceModel(artifact, this.pluginRef);
		else
			throw new TigerstripeException(
					"Value iterator interface look up of '"
							+ fullyQualifiedName + "' failed.");
	}

	public KeyInterfaceModel keyInterfaceOf(String fullyQualifiedName)
			throws TigerstripeException {
		AbstractArtifact artifact = artifactManager
				.getArtifactByFullyQualifiedName(fullyQualifiedName, true,
						new TigerstripeNullProgressMonitor());

		if (artifact instanceof ManagedEntityArtifact)
			return new KeyInterfaceModel(artifact, this.pluginRef);
		else
			throw new TigerstripeException("Key interface look up of '"
					+ fullyQualifiedName + "' failed.");
	}

	public KeyInterfaceModel keyResultInterfaceOf(String fullyQualifiedName)
			throws TigerstripeException {
		AbstractArtifact artifact = artifactManager
				.getArtifactByFullyQualifiedName(fullyQualifiedName, true,
						new TigerstripeNullProgressMonitor());

		if (artifact instanceof ManagedEntityArtifact)
			return new KeyResultInterfaceModel(artifact, this.pluginRef);
		else
			throw new TigerstripeException("Key result interface look up of '"
					+ fullyQualifiedName + "' failed.");
	}

	public KeyResultIteratorInterfaceModel keyResultIteratorInterfaceOf(
			String fullyQualifiedName) throws TigerstripeException {
		AbstractArtifact artifact = artifactManager
				.getArtifactByFullyQualifiedName(fullyQualifiedName, true,
						new TigerstripeNullProgressMonitor());

		if (artifact instanceof ManagedEntityArtifact)
			return new KeyResultIteratorInterfaceModel(artifact, this.pluginRef);
		else
			throw new TigerstripeException(
					"Key result iterator interface look up of '"
							+ fullyQualifiedName + "' failed.");
	}

	// getNext$ossjUtil.keyResultInterfaceOf($artifact.FullyQualifiedName).getName()s

	public String pluralisedForm(String name) {
		// Very special one off case!
		// If word ends with a consonant then y
		// then change the plural to be "ies"

		String ending = ".*[^aeiou]y";
		if (name.matches(ending)) {

			name = name.substring(0, name.length() - 1) + "ies";
			return name;
		} else
			return name + "s";

		// if (name.endsWith("Entity")){
		// return name.replace("Entity","Entities");
		// }else{
		// return name+"s";
		// }

	}

	public String underscoredName(String attrName) {

		String[] parts = splitAttribute(attrName);
		String result = "";
		for (int i = 0; i < parts.length; i++) {

			if (!"".equals(result)) {
				result = result + "_";
			}

			result = result + parts[i].toUpperCase();
		}
		return result;
	}

	/**
	 * Splits an attribute name based on capitalized letters. "anAttributeName"
	 * would return { "an", "Attribute", "Name" } also spliuts based on "."
	 * 
	 * @param attrName
	 * @return
	 */
	private String[] splitAttribute(String attrName) {
		List result = new ArrayList();

		String attr = attrName.substring(0, 1).toUpperCase()
				+ attrName.substring(1);

		while (!attr.equals("")) {
			int nextIndex = indexOfFirstSplit(attr);
			String part = attr.substring(0, nextIndex);
			result.add(part);
			attr = attr.substring(nextIndex);
			if (attr.startsWith(".")) {
				attr = attr.substring(1);
			}

		}

		String[] arrResult = new String[result.size()];
		result.toArray(arrResult);
		return arrResult;
	}

	private int indexOfFirstSplit(String aString) {

		if (aString == null || "".equals(aString))
			return -1;

		final int LOWER = 0;
		final int UPPER = 1;

		for (int i = 1; i < aString.length(); i++) {
			int currentCase = getCase(aString.charAt(i));
			int prevCase = getCase(aString.charAt(i - 1));
			int nextCase;

			if (i == aString.length() - 1) {
				// there is no next char.
				nextCase = -1;
			} else {
				nextCase = getCase(aString.charAt(i + 1));
			}
			if (currentCase == UPPER) {
				if (prevCase == LOWER)
					// change from low to up equals New word!
					return i;
				else if (nextCase == LOWER)
					return i;
			}
		}
		return aString.length();

	}

	// Method added by RC

	private int getCase(char c) {
		final int LOWER = 0;
		final int UPPER = 1;
		if (c == '.')
			return UPPER;
		if (c >= 'a' && c <= 'z')
			return LOWER;
		else
			return UPPER;
	}

	public String pluralise(String single) {
		String plural = single;
		if (single.endsWith("ty")) {
			plural = single.substring(0, (single.length() - 1)) + "ie";
		}
		return plural;
	}

	/*
	 * reformats a comment string to turn it into a decently formatted Java
	 * comment (ie like this one with * at the front)
	 */
	public String formatComment(String comment) {
		if (comment.length() == 0)
			return " *";

		StringBuffer buff = new StringBuffer(comment);
		String newline = "\n";

		buff.insert(0, " *  ");
		int start = 0;
		while (buff.indexOf(newline, start) != -1) {
			buff.insert(buff.indexOf(newline, start) + 1, " *  ");
			start = buff.indexOf(newline, start) + 1;
		}
		// remove trailing spaces
		return buff.toString().replaceAll(" *\n", "\n");
	}

	public String getDoclet(String fullyQualifiedName, String tagName)
			throws TigerstripeException {

		AbstractArtifact artifact = artifactManager
				.getArtifactByFullyQualifiedName(fullyQualifiedName, true,
						new TigerstripeNullProgressMonitor());

		String returnString = "";
		String leadString = " *  @" + tagName + ": ";
		ArrayList theseTags = new ArrayList(artifact.getTagsByName(tagName));
		if (theseTags.size() == 0)
			return "";
		ListIterator tagIterator = theseTags.listIterator();
		while (tagIterator.hasNext()) {
			Tag thisTag = (Tag) tagIterator.next();
			if (tagName.matches("tigerstripe.*")) {
				String thisParam = "";
				returnString = leadString;
				ArrayList params = new ArrayList(thisTag.getParameters());
				ListIterator paramIterator = params.listIterator();
				while (paramIterator.hasNext()) {
					thisParam = paramIterator.next().toString();
					returnString = returnString + "\n" + "              "
							+ thisParam;
				}

			} else {
				returnString = returnString + leadString + thisTag.getValue();
			}
			// if (tagIterator.hasNext()) {
			returnString = returnString + "\n";
			// }
		}

		return returnString;
	}
}
