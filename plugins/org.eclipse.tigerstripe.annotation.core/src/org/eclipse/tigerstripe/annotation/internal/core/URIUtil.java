/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.internal.core;

import org.eclipse.emf.common.util.URI;

/**
 * @author Yuri Strot
 * 
 */
public class URIUtil {

	public static URI replacePrefix(URI uri, URI oldPrefix, URI newPrefix) {

		// Get what's left of the segments after trimming the prefix.
		String[] tailSegments = getTailSegments(uri, oldPrefix);
		if (tailSegments == null)
			return null;

		// If the new prefix has segments, it is not the root absolute path,
		// and we need to drop the trailing empty segment and append the tail
		// segments.
		String[] mergedSegments = tailSegments;
		if (newPrefix.segmentCount() != 0) {
			int segmentsToKeep = newPrefix.segmentCount();
			mergedSegments = new String[segmentsToKeep + tailSegments.length];
			System.arraycopy(newPrefix.segments(), 0, mergedSegments, 0,
					segmentsToKeep);

			if (tailSegments.length != 0) {
				System.arraycopy(tailSegments, 0, mergedSegments,
						segmentsToKeep, tailSegments.length);
			}
		}

		// no validation needed since all components are from existing URIs
		return URI.createHierarchicalURI(newPrefix.scheme(), newPrefix
				.authority(), newPrefix.device(), mergedSegments, uri.query(),
				uri.fragment());
	}

	private static String[] getTailSegments(URI uri, URI prefix) {

		// Don't even consider it unless this is hierarchical and has scheme,
		// authority, device and path absoluteness equal to those of the prefix.
		if (!uri.isHierarchical()
				|| !equals(uri.scheme(), prefix.scheme(), true)
				|| !equals(uri.authority(), prefix.authority())
				|| !equals(uri.device(), prefix.device())) {
			return null;
		}

		// If the prefix has no segments, then it is the root absolute path, and
		// we know this is an absolute path, too.
		if (prefix.segmentCount() == 0)
			return uri.segments();

		// This must have no fewer segments than the prefix. Since the prefix
		// is not the root absolute path, its last segment is empty; all others
		// must match.
		int i = 0;
		int segmentsToCompare = prefix.segmentCount();
		if (uri.segments().length < segmentsToCompare)
			return null;

		for (; i < segmentsToCompare; i++) {
			if (!uri.segments()[i].equals(prefix.segment(i)))
				return null;
		}

		// Otherwise, the path needs only the remaining segments.
		String[] newSegments = new String[uri.segments().length - i];
		System.arraycopy(uri.segments(), i, newSegments, 0, newSegments.length);
		return newSegments;
	}

	// Tests two objects for equality, tolerating nulls; null is considered
	// to be a valid value that is only equal to itself.
	private static boolean equals(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	// Tests two strings for equality, tolerating nulls and optionally
	// ignoring case.
	private static boolean equals(String s1, String s2, boolean ignoreCase) {
		return s1 == null ? s2 == null : ignoreCase ? s1.equalsIgnoreCase(s2)
				: s1.equals(s2);
	}

}
