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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.diagram.IDiagram;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ContextProjectAwareProxy;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TigerstripeURIAdapterFactory implements IAdapterFactory {

	public static final String SCHEME_TS = "tigerstripe";

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof URI) {
			// try to adapt to Tigerstripe project
			Object result = uriToProject((URI) adaptableObject);
			if (result != null)
				return result;

			// then try to adapt to model component
			IModelComponent comp = uriToComponent((URI) adaptableObject);
			if (comp != null)
				return comp;

			// then special case for IArgument
			return uriToArgument((URI) adaptableObject);
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
		return SCHEME_TS.equals(uri.scheme());
	}

	/**
	 * The URI for a Tigerstripe model project is expected to be something like
	 * 
	 * tigerstripe:/modelId
	 * 
	 * where "modelId" is a tigerstripe project model id.
	 * 
	 * @param uri
	 * @return
	 */
	public static ITigerstripeModelProject uriToProject(URI uri) {
		if (!isRelated(uri))
			return null;

		IPath path = new Path(uri.path());
		if (path.segmentCount() != 1)
			return null;

		return TigerstripeCore.findModelProjectByID(path.segment(0));
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
		if (path.segmentCount() < 2 || !path.segment(1).equals("diagram"))
			return null;

		ITigerstripeModelProject project = TigerstripeCore
				.findModelProjectByID(path.segment(0));
		if (project == null)
			return null;

		IPath resPath = project.getFullPath();
		if (resPath == null || resPath.isEmpty())
			return null;
		resPath = resPath.append("src")
				.append(path.segment(3).replace('.', IPath.SEPARATOR))
				.addFileExtension(path.segment(2));
		System.out.println("Resource path: " + resPath);
		IResource res = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(resPath);
		if (res != null)
			return (IDiagram) res.getAdapter(IDiagram.class);

		return null;
	}

	protected static IAbstractArtifact extractArtifact(URI uri)
			throws TigerstripeException {
		if (!isRelated(uri))
			return null;

		IPath path = new Path(uri.path());

		// if URI is project URI, can't go any further
		if (path.segmentCount() < 2)
			return null;

		String fqn = path.lastSegment();
		path = path.removeLastSegments(1);

		String project = path.segments()[0];

		IAbstractArtifact artifact = null;
		if (path.segmentCount() == 3) {
			String container = path.segments()[0];
			project = path.segments()[1];

			for (IAbstractTigerstripeProject p : TigerstripeCore.projects()) {
				if (p instanceof ITigerstripeModelProject) {
					ITigerstripeModelProject proj = (ITigerstripeModelProject) p;
					if (container != null
							&& !proj.getModelId().equals(container)) {
						continue;
					}
					for (IDependency dep : proj.getDependencies()) {
						if (dep.getIModuleHeader().getModuleID()
								.equals(project)) {
							ArtifactManager mgr = ((Dependency) dep)
									.getArtifactManager(null);
							artifact = mgr.getArtifactByFullyQualifiedName(fqn,
									false, (IProgressMonitor) null);
							if (artifact != null) {
								return artifact;
							}
						}
					}

					for (ModelReference reference : proj.getModelReferences()) {
						if (project.equals(reference.getToModelId())) {
							if (reference.isInstalledModuleReference()) {
								ITigerstripeModelProject resolvedModel = reference
										.getResolvedModel();
								if (resolvedModel != null) {
									artifact = resolvedModel
											.getArtifactManagerSession()
											.getArtifactByFullyQualifiedName(
													fqn, false);
									if (artifact != null) {
										if (container != null) {
											return (IAbstractArtifact) ContextProjectAwareProxy
													.newInstance(artifact, proj);
										} else {
											return artifact;
										}
									}
								}
							}
						}
					}

					for (ITigerstripeModelProject reference : proj
							.getReferencedProjects()) {
						if (project.equals(reference.getModelId())) {
							artifact = reference
									.getArtifactManagerSession()
									.getArtifactByFullyQualifiedName(fqn, false);
							if (artifact != null) {
								if (container != null) {
									return (IAbstractArtifact) ContextProjectAwareProxy
											.newInstance(artifact, proj);
								} else {
									return artifact;
								}
							}
						}
					}
				}
			}
			return null;
		} else {
			ITigerstripeModelProject modelProject = TigerstripeCore
					.findModelProjectByID(project);
			if (modelProject == null)
				return null;

			IArtifactManagerSession artifactManagerSession = modelProject
					.getArtifactManagerSession();
			artifact = artifactManagerSession
					.getArtifactByFullyQualifiedName(fqn);

			if (artifact == null) {
				// try to find the according resource
				if (modelProject instanceof TigerstripeProjectHandle) {
					TigerstripeProjectHandle handle = (TigerstripeProjectHandle) modelProject;
					String sourceFolder = handle.getBaseRepository();
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
							.getRoot();
					IProject iproject = root.getProject(project);
					if (iproject != null && iproject.exists()) {
						IPath resourcePath = new Path(uri.path()
								.replace('.', IPath.SEPARATOR)
								.replace(project, sourceFolder));
						// try to find source
						IResource resource = iproject.findMember(resourcePath);
						if (resource == null) {
							// try to find source
							resourcePath = resourcePath
									.addFileExtension("java");
							resource = iproject.findMember(resourcePath);
						}
						if (resource != null) {
							// try to resolve artifact by resource
							artifact = (IAbstractArtifact) Platform
									.getAdapterManager().getAdapter(resource,
											IAbstractArtifact.class);
						}
					}
				}
			}
		}

		return artifact;
	}

	public static IArgument uriToArgument(URI uri) {

		try {

			IAbstractArtifact artifact = extractArtifact(uri);
			if (artifact == null)
				return null;

			String fragment = uri.fragment();

			if (fragment != null) {

				// Let's extract the method, and go from there
				String methodId = fragment.substring(0, fragment.indexOf(";;"));
				String argName = fragment.substring(fragment.indexOf(";;") + 2);
				for (IMethod m : artifact.getMethods()) {
					if (m.getMethodId().equals(methodId)) {
						for (IArgument arg : m.getArguments()) {
							if (arg.getName().equals(argName)) {
								return arg;
							}
						}
					}
				}
			}
			return null;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return null;
		}
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
		try {
			IAbstractArtifact artifact = extractArtifact(uri);
			if (artifact == null)
				return null;

			IModelComponent result = artifact;

			String fragment = uri.fragment();

			if (fragment != null) {
				if (fragment.contains(";;")) {
					// This means we're dealing with a method argument.
					// Since IArgument is not a IModelComponent yet so it'll get
					// picked up later
					return null;
				} else if (fragment.contains(";")
						&& artifact instanceof IAssociationArtifact) {
					IAssociationArtifact assoc = (IAssociationArtifact) artifact;
					if (fragment.endsWith(";aEnd"))
						result = assoc.getAEnd();
					else if (fragment.endsWith(";zEnd"))
						result = assoc.getZEnd();
				} else if (fragment.endsWith(")") && fragment.contains("(")) {
					for (IMethod m : artifact.getMethods()) {
						if (m.getMethodId().equals(fragment)) {
							result = m;
							break;
						}
					}
				} else {
					
					Collection<ILiteral> literals = artifact.getLiterals();
					Collection<IField> fields = artifact.getFields();
					Collection<IModelComponent> components = new ArrayList<IModelComponent>(
							literals.size() + fields.size());

					components.addAll(fields);
					components.addAll(literals);

					for (IModelComponent c : components) {
						if (c.getName().equals(fragment)) {
							result = c;
							break;
						}
					}
				}
			}

			if (!(result instanceof IAbstractArtifact)
					&& !(result instanceof IContextProjectAware)
					&& artifact instanceof IContextProjectAware) {
				return (IModelComponent) ContextProjectAwareProxy.newInstance(
						result,
						((IContextProjectAware) artifact).getContextProject());
			}
			return result;
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

	public static URI toURI(IArgument argument) throws TigerstripeException {
		IMethod method = argument.getContainingMethod();
		IAbstractArtifact art = getArtifact(method);

		IPath artifactPath = getArtifactPath(art, null);

		String fragment = method.getMethodId() + ";;" + argument.getName();
		return toURI(artifactPath, fragment);
	}

	public static URI toURI(ITigerstripeModelProject project)
			throws TigerstripeException {
		IPath path = new Path(project.getModelId());
		return toURI(path, null);
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
		return toURI(element.getDiagramFile());
	}

	
	public static URI toURI(IFile diagramFile) throws TigerstripeException {
		IPath fullPath = diagramFile.getFullPath();
		ITigerstripeModelProject project = (ITigerstripeModelProject) diagramFile
				.getProject().getAdapter(ITigerstripeModelProject.class);
		if (project == null) {
			return null;
		}

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
		IPath result = new Path(project.getModelId());
		result = result.append("diagram").append(fullPath.getFileExtension())
				.append(sb.toString());
		return toURI(result, null);
	}
	
	public static URI memberToURI(IAbstractArtifact art, String memberId)
			throws TigerstripeException {
		return toURI(getArtifactPath(art, null), memberId);
	}
	
	public static URI argumentToURI(IAbstractArtifact art, String methodId,
			String argumentId) throws TigerstripeException {
		String fragment = methodId + ";;" + argumentId;
		return toURI(getArtifactPath(art, null), fragment);
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
	public static URI toURI(IModelComponent component, String newName)
			throws TigerstripeException {
		// System.out.println("toURI: "+component+" / "+newName);
		IAbstractArtifact art = getArtifact(component);
		if (art == null) {
			return null;
		}
		
		return toURI(art, component, newName);
	}
	
	public static URI toURI(IAbstractArtifact art, IModelComponent component,
			String newName) throws TigerstripeException {
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
			StringBuilder b = new StringBuilder(
					newName == null ? assoc.getName() : newName);
			if (end.equals(assoc.getAEnd())) {
				b.append(";aEnd");
			} else {
				b.append(";zEnd");
			}
			fragment = b.toString();
		}

		ITigerstripeModelProject context = null;
		if (component instanceof IContextProjectAware) {
			context = ((IContextProjectAware) component).getContextProject();
		}
		return toURI(artifactPath, fragment, context);
	}

	private static IPath getArtifactPath(IAbstractArtifact art, String newName) {
		IPath path = null;

		TigerstripeProject tsProject = art.getTSProject();

		if (tsProject != null) {
			ProjectDetails details = tsProject.getProjectDetails();
			if (details != null) {
				path = new Path(details.getModelId());
			}
		}

		if (path == null || path.isEmpty()) {
			IModuleHeader header = art.getParentModuleHeader();
			if (header == null) {
				throw new IllegalStateException(
						"Can't determinate module id for artifact "
								+ art.getFullyQualifiedName());
			}
			path = new Path(header.getModuleID());
		}

		path = path.append(newName == null ? art.getFullyQualifiedName()
				: newName);
		return path;
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

	private static URI toURI(IPath path, String fragment)
			throws TigerstripeException {
		return toURI(path, fragment, null);
	}

	private static URI toURI(IPath path, String fragment,
			ITigerstripeModelProject context) throws TigerstripeException {
		if (path == null)
			return null;
		IPath resPath = path;

		if (context != null) {
			String container = context.getModelId();
			if (container != null) {
				StringBuilder res = new StringBuilder();
				res.append(container);
				for (int i = 0; i < path.segmentCount(); i++) {
					res.append(File.separator);
					res.append(path.segment(i));
				}
				resPath = new Path(path.getDevice(), res.toString());
			}
		}

		try {
			URI uri = URI.createHierarchicalURI(SCHEME_TS, null, null,
					resPath.segments(), null, fragment);
			return uri;
		} catch (IllegalArgumentException e) {
			BasePlugin.log(e);
		}
		return null;
	}
}
