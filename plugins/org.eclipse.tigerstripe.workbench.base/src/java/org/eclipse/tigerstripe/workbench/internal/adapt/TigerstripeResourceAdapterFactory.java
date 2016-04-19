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
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeM0GeneratorNature;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripePluginProjectNature;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeProjectNature;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM0GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Overall Tigerstripe Resource Adapter factory
 * 
 * @author erdillon
 * 
 */
public class TigerstripeResourceAdapterFactory implements IAdapterFactory {

	private final static Class<?>[] artifactTypes = { IAbstractArtifact.class,
			IManagedEntityArtifact.class, IDatatypeArtifact.class,
			IPrimitiveTypeArtifact.class, IEnumArtifact.class,
			IExceptionArtifact.class, ISessionArtifact.class,
			IUpdateProcedureArtifact.class, IEventArtifact.class,
			IQueryArtifact.class, IAssociationArtifact.class,
			IAssociationClassArtifact.class, IDependencyArtifact.class,
			IPackageArtifact.class };

	protected boolean isArtifactType(Class<?> adapterType) {
		for (Class<?> type : artifactTypes) {
			if (type == adapterType) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == ITigerstripeModelProject.class) {
			IAbstractTigerstripeProject aProject = adaptToProject(adaptableObject);
			if (aProject instanceof ITigerstripeModelProject) {
				return aProject;
			}
		} else if (adapterType == ITigerstripeGeneratorProject.class
				|| adapterType == ITigerstripeM0GeneratorProject.class
				|| adapterType == ITigerstripeM1GeneratorProject.class) {
			IAbstractTigerstripeProject aProject = adaptToProject(adaptableObject);
			if (aProject instanceof ITigerstripeGeneratorProject) {
				return aProject;
			}
		} else if (adapterType == IAbstractTigerstripeProject.class) {
			return adaptToProject(adaptableObject);
		} else if (isArtifactType(adapterType)) {
			return adaptToArtifact(adaptableObject, adapterType);
		} else if (adapterType == IModelComponent.class) {
			return adaptToArtifact(adaptableObject, adapterType);
		}

		return null;
	}

	protected IAbstractArtifact adaptToArtifact(Object adaptableObject,
			Class<?> adapterType) {
		if (adaptableObject instanceof IFile) {
			IFile res = (IFile) adaptableObject;
			if (res != null) {
				try {
					ITigerstripeModelProject project = (ITigerstripeModelProject) res
							.getProject().getAdapter(
									ITigerstripeModelProject.class);

					if (project != null) {
						IArtifactManagerSession mgr = project
								.getArtifactManagerSession();

						IAbstractArtifact artifact = ((ArtifactManagerSessionImpl) mgr)
								.getArtifactManager().getArtifactByFilename(
										res.getLocation().toOSString());

						if (artifact == null && res.exists()) {
							InputStreamReader reader = null;
							try {
								if (!res.isSynchronized(IResource.DEPTH_ONE)) {
									res.refreshLocal(IResource.DEPTH_ONE, null);
								}
								reader = new InputStreamReader(
										res.getContents());
								artifact = mgr.extractArtifact(reader, res.getFullPath().toOSString(), new NullProgressMonitor());
							} catch (CoreException e) {
								BasePlugin.log(e);
							} finally {
								if (reader != null) {
									try {
										reader.close();
									} catch (IOException e) {
									}
								}
							}
						}
						if (adapterType.isInstance(artifact))
							return artifact;
						else
							return null;
					} else
						return null;

				} catch (TigerstripeException e) {
					// This means we couldn't parse it. Must some kind of other
					// file (not pojo artifact)
					// just ignore
					return null;
				}
			} else
				return null;
		} else if (adaptableObject instanceof IFolder) {
			IFolder folder = (IFolder) adaptableObject;
			if (folder != null) {
				try {
					IProject project = folder.getProject();
					ITigerstripeModelProject aProject = (ITigerstripeModelProject) project
							.getAdapter(ITigerstripeModelProject.class);
					if (aProject != null) {

						IArtifactManagerSession mgr = aProject
								.getArtifactManagerSession();
						IAbstractArtifact artifact = ((ArtifactManagerSessionImpl) mgr)
								.getArtifactManager()
								.getArtifactByFullyQualifiedName(
										folder.getProjectRelativePath()
												.removeFirstSegments(1)
												.toOSString()
												.replace(File.separatorChar,
														'.'), false,
										new NullProgressMonitor());

						if (artifact != null)
							return artifact;
						else {
							try {
								if (folder.exists()) {
									IResource[] resources = folder.members();
									for (IResource res : resources) {
										if (res instanceof IFile) {
											IFile f = (IFile) res;
											if (f.getName().equals(".package")) {
												InputStreamReader reader = null;
												try {
													reader = new InputStreamReader(
															f.getContents());
													artifact = mgr
															.extractArtifact(
																	reader,
																	null, new NullProgressMonitor());
													return artifact;
												} finally {
													if (reader != null) {
														try {
															reader.close();
														} catch (IOException e) {
														}
													}
												}
											}
										}
									}
								}
							} catch (CoreException e) {
								BasePlugin.log(e);
							}
							return null;
						}
					}
				} catch (Exception e) {
					// This means we couldn't parse it. Must some kind of other
					// file (not pojo artifact)
					// just ignore
					return null;
				}

			} else
				return null;

		} else
			return null;

		return null;
	}

	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return new Class[] { ITigerstripeModelProject.class,
				ITigerstripeGeneratorProject.class,
				IAbstractTigerstripeProject.class,
				ITigerstripeM0GeneratorProject.class,
				ITigerstripeM1GeneratorProject.class, IModelComponent.class,
				IAbstractArtifact.class };
	}

	private IAbstractTigerstripeProject adaptToProject(Object adaptableObject) {
		if (adaptableObject instanceof IProject) {
			IProject project = (IProject) adaptableObject;
			if (project.exists() && project.isOpen()) {

				// If none of the known natures, no need to try
				try {
					if (!TigerstripeM0GeneratorNature.hasNature(project)
							&& !TigerstripeProjectNature.hasNature(project)
							&& !TigerstripePluginProjectNature
									.hasNature(project)) {
						return null;
					}
				} catch (CoreException e) {
					BasePlugin.log(e);
					return null;
				}

				try {
					IAbstractTigerstripeProject tsProject = TigerstripeCore
							.findProject(project);

					return tsProject;

				} catch (TigerstripeException e) {
					BasePlugin.log(e);
				}
			}
			// } else if (adaptableObject instanceof IResource) {
			// IResource res = (IResource) adaptableObject;
			// if (res != null) {
			// return adaptToProject(res.getProject());
			// }
			// } else if (adaptableObject instanceof IAdaptable) {
			// IResource res = (IResource) ((IAdaptable) adaptableObject)
			// .getAdapter(IResource.class);
			// if (res != null) {
			// return adaptToProject(res.getProject());
			// }
		} else if (adaptableObject instanceof IFile) {
			IFile file = (IFile) adaptableObject;
			if (ITigerstripeConstants.PROJECT_DESCRIPTOR.equals(file.getName())
					|| (ITigerstripeConstants.PLUGIN_DESCRIPTOR.equals(file
							.getName()))
					|| (ITigerstripeConstants.M0_GENERATOR_DESCRIPTOR
							.equals(file.getName()))) {
				return adaptToProject(file.getProject());
			}
		}

		return null;
	}

	public static String fqnForResource(IResource resource) {
		if ("java".equals(resource.getFileExtension())) {
			IPath projectRelPath = resource.getProjectRelativePath();
			IPath path = projectRelPath.removeFirstSegments(1); // the first
			// segment is
			// the "src/"
			// dir
			path = path.removeFileExtension();
			return path.toPortableString().replace(IPath.SEPARATOR, '.');
		} else if ("package".equals(resource.getFileExtension())) {
			IPath projectRelPath = resource.getProjectRelativePath();
			IPath path = projectRelPath.removeFirstSegments(1); // the first
			// segment is
			// the "src/"
			// dir
			path = path.removeFileExtension();
			return path.toPortableString().replace(IPath.SEPARATOR, '.');
		}
		throw new IllegalArgumentException(resource.getFullPath()
				.toPortableString());
	}

	private static ITigerstripeModelProject getProjectFor(IJavaProject jProject) {
		if (jProject != null) {
			IProject project = jProject.getProject();
			IAbstractTigerstripeProject atsProject = (IAbstractTigerstripeProject) project
					.getAdapter(IAbstractTigerstripeProject.class);
			if (atsProject instanceof ITigerstripeModelProject) {
				return (ITigerstripeModelProject) atsProject;
			}
		}
		return null;
	}
}
