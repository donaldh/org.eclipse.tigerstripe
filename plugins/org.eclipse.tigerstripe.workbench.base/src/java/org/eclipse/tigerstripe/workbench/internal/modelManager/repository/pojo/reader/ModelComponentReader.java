/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.reader;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;
import org.eclipse.tigerstripe.workbench.internal.core.util.encode.XmlEscape;

import com.thoughtworks.qdox.model.DocletTag;

public class ModelComponentReader {
	protected static XmlEscape xmlEncode = new XmlEscape();

	protected List<Tag> tags = new ArrayList<Tag>();

	protected void resetTags() {
		tags.clear();
	}

	protected void extractTags(DocletTag[] docletTags) {
		for (int i = 0; i < docletTags.length; i++) {
			Tag tag = new Tag(docletTags[i]);
			tags.add(tag);
		}
	}

	protected Tag getFirstTagByName(String name) {
		for (Tag tag : tags) {
			if (tag.getName().equals(name))
				return tag;
		}
		return null;
	}

	protected List<Tag> getTagsByName(String name) {
		List<Tag> result = new ArrayList<Tag>();
		for (Tag tag : tags) {
			if (tag.getName().equals(name)) {
				result.add(tag);
			}
		}
		return result;
	}

	
	
	protected void readStereotypes() {

	}

	protected void readReferencedComments() {

	}
}
