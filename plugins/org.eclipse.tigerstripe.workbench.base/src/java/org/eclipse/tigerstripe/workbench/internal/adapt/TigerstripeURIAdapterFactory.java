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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.diagram.IDiagram;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TigerstripeURIAdapterFactory implements IAdapterFactory {

	public static final String SCHEME_TS = "tigerstripe";
	public static final String SCHEME_TS_MODULE = "tigerstripe_module";

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof URI) {
			// try to adapt to Tigerstripe project
			Object result = uriToProject((URI) adaptableObject);
			if (result != null)
				return result;

			// then try to adapt to model component
			return uriToComponent((URI) adaptableObject);
		}
		return null;
	}

	public Class<?>[] getAdapterList() {
		return new Class<?>[] { IModelComponent.class, IDiagram.class };
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
		return SCHEME_TS.equals(uri.scheme())
				|| SCHEME_TS_MODULE.equals(uri.scheme());
	}

	/**
	 * The URI for a Tigerstripe project is expected to be something like
	 * 
	 * tigerstripe:/project
	 * 
	 * where "project" is the label of the project.
	 * 
	 * @param uri
	 * @return
	 */
	public static IAbstractTigerstripeProject uriToProject(URI uri) {
		if (!isRelated(uri))
			return null;

		IPath path = new Path(uri.path());
		if (path.segmentCount() != 1)
			return null;

		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				path);
		if (res != null)
			return (IAbstractTigerstripeProject) res
					.getAdapter(IAbstractTigerstripeProject.class);

		return null;
	}

	/**
	 * The URI for a Tigerstripe project is expected to be something like
	 * 
	 * tigerstripe:/project
	 * 
	 * where "project" is the label of the project.
	 * 
	 * @param uri
	 * @return
	 */
	public static IDiagram uriToDiagram(URI uri) {
		if (!isRelated(uri))
			return null;

		IPath path = new Path(uri.path());
		// if (path.segmentCount() != 1)
		// return null;
		if (!path.segment(1).equals("diagram"))
			return null;

		IPath resPath = new Path(path.segment(0));
		resPath = resPath.append("src").append(
				path.segment(3).replace('.', IPath.SEPARATOR))
				.addFileExtension(path.segment(2));
		System.out.println("Resource path: " + resPath);
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				resPath);
		if (res != null)
			return (IDiagram) res.getAdapter(IDiagram.class);

		return null;
	}

	/**
	 * The URI is expected to be something like:
	 * 
	 * tigerstripe:/project/FQN if artifact from project or
	 * tigerstripe:/project/module-name/FQN if artifact from Module.
	 * 
	 * @param uri
	 * @return
	 */
	public static IModelComponent uriToComponent(URI uri) {

		if (!isRelated(uri))
			return null;

		IPath path = new Path(uri.path());

		// if URI is project URI, can't go any further
		if (path.segmentCount() < 2)
			return null;

		String fqn = path.lastSegment();
		path = path.removeLastSegments(1);

		String project = path.segments()[0];
		try {

			IAbstractArtifact artifact = null;
			if (SCHEME_TS_MODULE.equals(uri.scheme())) {
				// In this case we don't have a project name, we only have a
				// ModuleID. Not sure how to proceed.
				// This logic only works if a module is used ONCE only in the
				// workspace, or else we'll always return the first occurrence
				// :-(
				for (IAbstractTigerstripeProject p : TigerstripeCore.projects()) {
					if ( p instanceof ITigerstripeModelProject ) {
						ITigerstripeModelProject proj = (ITigerstripeModelProject) p;
						for( IDependency dep : proj.getDependencies() ) {
							if ( dep.getIModuleHeader().getModuleID().equals(project)) {
								ArtifactManager mgr = ((Dependency) dep).getArtifactManager(null);
								artifact = mgr.getArtifactByFullyQualifiedName(fqn, false, null);
							}
						}
					}
				}
				return null;
			} else {
				IAbstractTigerstripeProject tsp = TigerstripeCore
						.findProject(project);
				if (!(tsp instanceof ITigerstripeModelProject))
					return null;

				IArtifactManagerSession artifactManagerSession = ((ITigerstripeModelProject) tsp)
						.getArtifactManagerSession();
				artifact = artifactManagerSession
						.getArtifactByFullyQualifiedName(fqn);
			}

			if (artifact == null )
				return null;
			
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

	public static URI toURI(IAbstractTigerstripeProject project)
			throws TigerstripeException {
		IPath path = project.getFullPath();
		return toURI(path, null, false);
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
	public static URI toURI(IDiagram element) throws TigerstripeException {
		IPath fullPath = element.getDiagramFile().getFullPath();
		System.out.println("DiagramFile (location): "
				+ element.getDiagramFile().getLocation() + " (fullpath): "
				+ fullPath);

		String project = fullPath.segment(0);
		IPath truncated = fullPath.removeFirstSegments(2).removeFileExtension();
		StringBuilder sb = new StringBuilder();
		char delim = 0;
		for (String segment : truncated.segments()) {
			if (delim == 0)
				delim = '.';
			else
				sb.append(delim);
			sb.append(segment);
		}
		IPath result = new Path(project);
		result = result.append("diagram").append(fullPath.getFileExtension())
				.append(sb.toString());
		System.out.println("Final path: "+result.toString());
		return toURI(result, null, false);
	}

	/**
	 * Returns a URI that identifies the target of an annotation and which
	 * allows that target to be looked up in the Tigerstripe workbench, but
	 * accepts a second argument whose content will replace a part of the URI
	 * that is appropriate to the type of <code>IModelComponent</code> supplied
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

		return toURI(artifactPath, fragment, art.isReadonly());
	}

	private static IPath getArtifactPath(IAbstractArtifact art, String newName) {
		try {

			IPath path = null;
			if (art.getProject() == null) {
				IModuleHeader header = art.getParentModuleHeader();
				path = new Path(header.getModuleID());
			} else {
				path = new Path(art.getProject().getProjectLabel());
			}

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

	private static URI toURI(IPath path, String fragment, boolean isFromModule) {

		if (path == null)
			return null;

		String scheme = SCHEME_TS;
		if (isFromModule) {
			scheme = SCHEME_TS_MODULE;
		}

		try {
			URI uri = URI.createHierarchicalURI(scheme, null, null, path
					.segments(), null, fragment);
			return uri;
		} catch (IllegalArgumentException e) {
			BasePlugin.log(e);
		}
		return null;
	}

}
