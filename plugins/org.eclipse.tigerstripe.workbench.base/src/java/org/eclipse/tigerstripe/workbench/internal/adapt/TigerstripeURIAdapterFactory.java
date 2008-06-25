/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.adapt;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TigerstripeURIAdapterFactory implements IAdapterFactory {

	public static final String SCHEME_TS = "tigerstripe";

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof URI) {
			return uriToComponent((URI) adaptableObject);
		}
		return null;
	}

	public Class<?>[] getAdapterList() {
		return new Class<?>[] { IModelComponent.class, };
	}

	/**
	 * Returns the value <code>true</code> if the supplied URI is for the same
	 * scheme as that converted by an instance of this converter
	 * 
	 * @param uri
	 *            the URI whose scheme we wish to test for compatibility with an
	 *            instance of this converter.
	 * @return the value <code>true</code> if the supplied URI is for the same
	 *         scheme as that converted by an instance of this converter
	 */
	public static boolean isRelated(URI uri) {
		return SCHEME_TS.equals(uri.scheme());
	}
	
	public static IModelComponent uriToComponent(URI uri) {
		IPath path = new Path(uri.path());
		String fqn = path.lastSegment();
		path = path.removeLastSegments(1);
		try {
			IAbstractTigerstripeProject tsp = TigerstripeCore.findProject(path
					.lastSegment());

			if (!(tsp instanceof ITigerstripeModelProject))
				return null;

			IArtifactManagerSession artifactManagerSession = ((ITigerstripeModelProject) tsp)
					.getArtifactManagerSession();
			IAbstractArtifact artifact = artifactManagerSession
					.getArtifactByFullyQualifiedName(fqn);
			String fragment = uri.fragment();

			if (fragment != null) {
				if (fragment.contains(";")
						&& artifact instanceof IAssociationArtifact) {
					IAssociationArtifact assoc = (IAssociationArtifact) artifact;
					if (fragment.endsWith(";aEnd"))
						return assoc.getAEnd();
					else if (fragment.endsWith(";zEnd"))
						return assoc.getZEnd();
				} else if (fragment.endsWith(")") && fragment.contains("(")) {
					for (IMethod m : artifact.getMethods()) {
						if (m.getMethodId().equals(fragment))
							return m;
					}
				} else {
					for (IField f : artifact.getFields()) {
						if (f.getName().equals(fragment))
							return f;
					}
				}

			}
			return artifact;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return null;
		}

	}

	/**
	 * Returns a URI that identifies the target of an annotation and which
	 * allows that target to be looked up in the Tigerstripe workbench
	 * 
	 * @param element
	 *            the <code>IModelComponent</code> for which we require a URI
	 * @return a URI that identifies the target of an annotation and which
	 *         allows that target to be looked up in the Tigerstripe workbench
	 * @throws TigerstripeException
	 */
	public static URI toURI(IModelComponent element)
			throws TigerstripeException {
		return toURI(element, null);
	}

	/**
	 * Returns a URI that identifies the target of an annotation and which
	 * allows that target to be looked up in the Tigerstripe workbench, but
	 * accepts a second argument whose content will replace a part of the URI
	 * that is appropriate to the type of <code>IModelComponent</code>
	 * supplied
	 * 
	 * @param element
	 *            the <code>IModelComponent</code> for which we require a URI
	 * @param newName
	 *            the new fragment that will replace a part of the URI that is
	 *            appropriate to the type of <code>IModelComponent</code>
	 *            supplied
	 * @return a URI that identifies the target of an annotation and which
	 *         allows that target to be looked up in the Tigerstripe workbench
	 * @throws TigerstripeException
	 */
	public static URI toURI(IModelComponent component, String newName) {
		// System.out.println("toURI: "+component+" / "+newName);
		IAbstractArtifact art = getArtifact(component);
		IPath artifactPath = getArtifactPath(art, newName);

		String fragment = null;

		if (component instanceof IField) {
			fragment = newName == null ? ((IField) component).getName()
					: newName;
		} else if (component instanceof IMethod) {
			IMethod method = ((IMethod) component);
			fragment = method.getMethodId();
		} else if (component instanceof ILiteral) {
			fragment = newName == null ? ((ILiteral) component).getName()
					: newName;
		} else if (component instanceof IAssociationEnd) {
			IAssociationEnd end = (IAssociationEnd) component;
			IAssociationArtifact assoc = end.getContainingAssociation();
			StringBuilder b = new StringBuilder(newName == null ? assoc
					.getName() : newName);
			if (assoc.getAEnd() == end) {
				b.append(";aEnd");
			} else {
				b.append(";zEnd");
			}
			fragment = b.toString();
		}

		return toURI(artifactPath, fragment);
	}

	private static IPath getArtifactPath(IAbstractArtifact art, String newName) // throws
	// TigerstripeException
	{
		try {
			IPath path = new Path(art.getProject().getProjectLabel());
			path = path.append(newName == null ? art.getFullyQualifiedName()
					: newName);
			return path;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return null;
		}
	}

	private static IAbstractArtifact getArtifact(IModelComponent component) {
		IAbstractArtifact art = null;
		if (component instanceof IAbstractArtifact) {
			art = (IAbstractArtifact) component;
		} else if (component instanceof IField) {
			art = ((IField) component).getContainingArtifact();
		} else if (component instanceof IMethod) {
			art = ((IMethod) component).getContainingArtifact();
		} else if (component instanceof ILiteral) {
			art = ((ILiteral) component).getContainingArtifact();
		} else if (component instanceof IAssociationEnd) {
			art = ((IAssociationEnd) component).getContainingArtifact();
		}
		return art;
	}

	private static URI toURI(IPath path, String fragment) {
		try {
			URI uri = URI.createHierarchicalURI(SCHEME_TS, null, null, path
					.segments(), null, fragment);
			return uri;
		} catch (IllegalArgumentException e) {
			BasePlugin.log(e);
		}
		return null;
	}

}
