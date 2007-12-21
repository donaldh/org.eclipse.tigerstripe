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
package org.eclipse.tigerstripe.core.model;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;

/**
 * Filters artifacts by package.
 * 
 * @author Eric Dillon
 * 
 */
public class PackageBasedArtifactFilter extends ArtifactFilter {

	private String packageString;
	private Collection allPackageStrings;

	public PackageBasedArtifactFilter(String packageString,
			Collection allPackageStrings) {
		super();
		this.packageString = packageString;
		this.allPackageStrings = allPackageStrings;
		allPackageStrings.remove(packageString);// we want a list of all the
		// OTHER packages
	}

	@Override
	public boolean select(IAbstractArtifact artifact) {
		if (artifact == null)
			return false;

		String pkg = artifact.getPackage();
		if (pkg == null || pkg.length() == 0)
			return packageString == null || packageString.length() == 0;

		// If this matches another package then we should not include it
		boolean matchOther = matchOther(pkg);

		return pkg.startsWith(packageString) && !matchOther;
	}

	private boolean matchOther(String pkg) {
		boolean result = false;
		for (Iterator iter = allPackageStrings.iterator(); iter.hasNext();) {
			String pack = (String) iter.next();
			result = result
					| (pkg.startsWith(pack) && (pack.length() > packageString
							.length()));
		}
		return result;
	}

}
