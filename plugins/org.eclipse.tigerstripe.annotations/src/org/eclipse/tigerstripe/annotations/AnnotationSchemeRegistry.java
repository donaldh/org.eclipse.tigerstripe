/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    erdillon (Cisco Systems, Inc.) - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.annotations.internal.AnnotationScheme;

/**
 * A registry for all namespace definitions as found from the corresponding
 * Extension Point
 * 
 * @author erdillon
 * 
 */
public class AnnotationSchemeRegistry {

	public static final AnnotationSchemeRegistry eINSTANCE = new AnnotationSchemeRegistry();

	private Map<String, IAnnotationScheme> schemesMap = null;

	/**
	 * Returns an array of all defined schemes for the given URI
	 * 
	 * The schemes are selected using the optional selector provided in the
	 * definition of the scheme. If no selector was provided the scheme will be
	 * selected by default.
	 * 
	 * @param uri -
	 *            the uri to use as the key to get the defined schemes. If null
	 *            all defined schemes are returned.
	 * @return
	 */
	public IAnnotationScheme[] getDefinedSchemes(String uri)
			throws AnnotationCoreException {
		assertSchemesLoaded();

		if (uri == null)
			return schemesMap.values().toArray(
					new IAnnotationScheme[schemesMap.values().size()]);
		else {
			List<IAnnotationScheme> result = new ArrayList<IAnnotationScheme>();
			for (IAnnotationScheme scheme : schemesMap.values()) {
				if (scheme.getSelector().select(uri))
					result.add(scheme);
			}
			return result.toArray(new IAnnotationScheme[result.size()]);
		}
	}

	/**
	 * Reads all the extension points defining schemes if not already done.
	 * 
	 * @throws AnnotationCoreException
	 */
	private void assertSchemesLoaded() throws AnnotationCoreException {
		if (schemesMap == null)
			readExtensionPoints();
	}

	/**
	 * Parses all the Schemes defined as extension points into the corresponding
	 * objects to populate this registry.
	 * 
	 */
	private void readExtensionPoints() throws AnnotationCoreException {

		schemesMap = new HashMap<String, IAnnotationScheme>();

		// Get all defined schemes from the corresponding Extension Point
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry
				.getExtensionPoint(IAnnotationExtensionPoints.SCHEME);

		IExtension extensions[] = extensionPoint.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IAnnotationScheme scheme = new AnnotationScheme(extensions[i]);
			schemesMap.put(scheme.getNamespaceID(), scheme);
		}
	}

	/**
	 * Returns the Scheme for the given ID
	 * 
	 * @param ID
	 * @return the scheme defined with the given ID, null otherwise
	 * @throws AnnotationCoreException
	 */
	public IAnnotationScheme getAnnotationSchemeByID(String ID)
			throws AnnotationCoreException {
		assertSchemesLoaded();

		if (schemesMap.containsKey(ID)) {
			return schemesMap.get(ID);
		}

		return null;
	}
}
