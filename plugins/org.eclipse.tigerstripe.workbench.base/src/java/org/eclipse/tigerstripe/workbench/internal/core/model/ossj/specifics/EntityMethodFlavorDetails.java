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
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.RefComment;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact.IEntityMethodFlavorDetails;

/**
 * Entity method need to be handled differently than others because they can
 * materialize in multiple flavors on the corresponding session facade.
 * 
 * @author Eric Dillon
 * 
 */
public class EntityMethodFlavorDetails implements IEntityMethodFlavorDetails {

	public final static String FLAVOR_TRUE = "true";

	public final static String FLAVOR_OPTIONAL = "optional";

	public final static String FLAVOR_FALSE = "false";

	private String flag;

	private String refCommentId = null;

	private AbstractArtifact parentArtifact;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	// We're only storing exception fqns
	Collection<String> exceptions;

	public String getComment() {
		if (refCommentId != null) {
			RefComment rComment = parentArtifact
					.getRefCommentById(refCommentId);
			if (rComment != null)
				return rComment.getContent();
		}
		return "";
	}

	public void setComment(String description) {

		if ((description == null || description.length() == 0)
				&& refCommentId == null)
			return;

		if (refCommentId == null) {
			refCommentId = parentArtifact.getUniqueRefCommentId();
		}
		RefComment rComment = new RefComment(parentArtifact);
		rComment.setLabel(refCommentId);
		rComment.setContent(description);
		parentArtifact.setRefComment(rComment);
	}

	public Collection<String> getExceptions() {
		return this.exceptions;
	}

	public void addException(String exceptionFqn) {
		if (!this.exceptions.contains(exceptionFqn))
			exceptions.add(exceptionFqn);
	}

	public void removeException(String exceptionFqn) {
		exceptions.remove(exceptionFqn);
	}

	// Because we want to store them in properties on the pojos, we need to
	// Stringify these...
	@Override
	public String toString() {
		String result = flag;

		if (!exceptions.isEmpty()) {
			result = result + ":";
			boolean first = true;
			for (String exception : exceptions) {
				if (!first) {
					result = result + ",";
				}
				result = result + exception;
				first = false;
			}
		}

		if (refCommentId != null) {
			result = result + ";" + refCommentId;
		}

		return result;
	}

	public EntityMethodFlavorDetails(AbstractArtifact e) {
		this.parentArtifact = e;
		exceptions = new ArrayList<String>();
		flag = FLAVOR_TRUE;
	}

	@Override
	public EntityMethodFlavorDetails clone() {
		return new EntityMethodFlavorDetails(parentArtifact, this.toString());
	}

	public EntityMethodFlavorDetails(AbstractArtifact parentArtifact,
			String property) {
		this(parentArtifact);

		setFlag(extractFlag(property));
		exceptions = extractExceptions(property);
		refCommentId = extractRefCommentId(property);
	}

	private String extractRefCommentId(String property) {
		if (property.indexOf(";") != -1)
			return property.substring(property.indexOf(";") + 1, property
					.length());
		return null;
	}

	private String extractFlag(String property) {
		if (property != null) {
			if (property.startsWith(FLAVOR_FALSE))
				return FLAVOR_FALSE;
			else if (property.startsWith(FLAVOR_TRUE))
				return FLAVOR_TRUE;
			else if (property.startsWith(FLAVOR_OPTIONAL))
				return FLAVOR_OPTIONAL;
		}

		return FLAVOR_FALSE;
	}

	private Collection extractExceptions(String property) {
		Collection result = new ArrayList();
		if (property != null) {

			int index = property.lastIndexOf(":");
			if (index != -1) {
				int semiIndex = property.indexOf(";");
				String excString = property.substring(index + 1);
				if (semiIndex != -1) {
					excString = property.substring(index + 1, semiIndex);
				}
				String[] exceptionFQNs = excString.split(",");
				result.addAll(Arrays.asList(exceptionFQNs));
			}

		}
		return result;
	}
}
