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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;

/**
 * @author Yuri Strot
 * 
 */
public class URIUtil {
	
	private static final boolean USE_OLD_VERSION = false;
	
	private static final int SCHEME = 0;
	private static final int AUTHORITY = 1;
	private static final int DEVICE = 2;
	private static final int SEGMENT = 3;
	private static final int QUERY = 4;
	private static final int FRAGMENT = 5;
	
	private static class Part {
		
		private String content;
		private int kind;
		
		public Part(String content, int kind) {
			this.content = content;
			this.kind = kind;
		}
		
		/**
		 * @return the kind
		 */
		public int getKind() {
			return kind;
		}
		
		/**
		 * @return the content
		 */
		public String getContent() {
			return content;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return content;
		}
	}
	
	private static class PartedURI {
		
		private Part[] parts;
		
		private PartedURI(Part[] parts) {
			this.parts = parts;
		}
		
		/**
		 * NOTE: return null only when URI not hierarchical
		 * 
		 * @param uri
		 * @return
		 */
		public static PartedURI fromURI(URI uri) {
			List<Part> parts = new ArrayList<Part>();
			if (!uri.isHierarchical())
				return null;
			if (!uri.isRelative())
				parts.add(new Part(uri.scheme(), SCHEME));
			if (uri.hasAuthority())
				parts.add(new Part(uri.authority(), AUTHORITY));
			if (uri.hasDevice())
				parts.add(new Part(uri.device(), DEVICE));
			String[] segments = uri.segments();
			for (int i = 0; i < segments.length; i++)
				parts.add(new Part(segments[i], SEGMENT));
			if (uri.hasQuery())
				parts.add(new Part(uri.query(), QUERY));
			if (uri.hasFragment())
				parts.add(new Part(uri.fragment(), FRAGMENT));
			return new PartedURI(parts.toArray(new Part[parts.size()]));
		}
		
		public PartedURI cut(PartedURI prefix) {
			Part[] prefixParts = prefix.parts;
			if (parts.length < prefixParts.length)
				return null;
			int i = 0;
			for (; i < prefixParts.length; i++) {
				if (!prefixParts[i].getContent().equals(
						parts[i].getContent()))
					return null;
			}
			Part[] result = new Part[parts.length - i];
			if (result.length != 0) {
				System.arraycopy(parts, i, result, 0, result.length);
			}
			return new PartedURI(result);
		}
		
		public PartedURI concat(PartedURI postfix) {
			Part[] postfixParts = postfix.parts;
			if (postfixParts.length == 0)
				return this;
			if (parts.length == 0)
				return postfix;
			Part[] result = new Part[parts.length + postfixParts.length];
			System.arraycopy(parts, 0, result, 0, parts.length);
			System.arraycopy(postfixParts, 0, result,
					parts.length, postfixParts.length);
			PartedURI newPURI = new PartedURI(result);
			return newPURI.validate() ? newPURI : null;
		}
		
		private boolean validate() {
			int waitingFor = SCHEME;
			for (int i = 0; i < parts.length; i++) {
				int kind = parts[i].getKind();
				if (kind >= waitingFor)
					waitingFor = kind == SEGMENT ? kind : kind + 1;
				else
					return false;
			}
			return true;
		}
		
		public URI toURI() throws InvalidReplaceException {
			String scheme = null;
			String authority = null;
			String device = null;
			String query = null;
			String fragment = null;
			List<String> segments = new ArrayList<String>();
			for (int i = 0; i < parts.length; i++) {
				Part part = parts[i];
				switch (part.getKind()) {
					case SCHEME:
						if (scheme != null)
							throw new InvalidReplaceException("URI scheme should be only one");
						scheme = part.getContent();
						break;
					case AUTHORITY:
						if (authority != null)
							throw new InvalidReplaceException("URI authority should be only one");
						authority = part.getContent();
						break;
					case DEVICE:
						if (device != null)
							throw new InvalidReplaceException("URI device should be only one");
						device = part.getContent();
						break;
					case SEGMENT:
						segments.add(part.getContent());
						break;
					case QUERY:
						if (query != null)
							throw new InvalidReplaceException("URI query should be only one");
						query = part.getContent();
						break;
					case FRAGMENT:
						if (fragment != null)
							throw new InvalidReplaceException("URI fragment should be only one");
						fragment = part.getContent();
						break;
					default:
						throw new InvalidReplaceException("Invalid part kind");
				}
			}
			return URI.createHierarchicalURI(scheme, authority, device,
					segments.toArray(new String[segments.size()]), query, fragment);
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			if (parts == null)
				return "";
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < parts.length - 1; i++) {
				buffer.append(parts[i]);
				buffer.append("/");
			}
			if (parts.length > 0)
				buffer.append(parts[parts.length - 1]);
			return buffer.toString();
		}
		
	}
	
	private static URI oldReplace(URI uri, URI oldPrefix, URI newPrefix) {

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
	
	private static URI newReplace(URI uri, URI oldPrefix, URI newPrefix) throws InvalidReplaceException {
		PartedURI pUri = PartedURI.fromURI(uri);
		if (pUri == null)
			throw new InvalidReplaceException(uri.toString() + " should be hierarchical");
		PartedURI pOldPrefix = PartedURI.fromURI(oldPrefix);
		if (pOldPrefix == null)
			throw new InvalidReplaceException(oldPrefix.toString() + " should be hierarchical");
		PartedURI pNewPrefix = PartedURI.fromURI(newPrefix);
		if (pNewPrefix == null)
			throw new InvalidReplaceException(newPrefix.toString() + " should be hierarchical");
		PartedURI postfix = pUri.cut(pOldPrefix);
		if (postfix == null)
			throw new InvalidReplaceException(oldPrefix.toString() + " is not a prefix of the " + 
					uri.toString());
		PartedURI result = pNewPrefix.concat(postfix);
		if (result == null)
			throw new InvalidReplaceException("Replacing [" + oldPrefix + "] to the [" +
					newPrefix + "] in the [" + uri + "] is invalid");
		return result.toURI();
	}

	public static URI replacePrefix(URI uri, URI oldPrefix, URI newPrefix) throws InvalidReplaceException {
		return USE_OLD_VERSION ? oldReplace(uri, oldPrefix, newPrefix) :
			newReplace(uri, oldPrefix, newPrefix);
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
