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
package org.eclipse.tigerstripe.annotation.java;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author Yuri Strot
 * 
 */
public class JavaURIConverter {

	private static final String SCHEME_JAVA = "java";

	public static boolean isRelated(URI uri) {
		return SCHEME_JAVA.equals(uri.scheme());
	}

	public static URI toURI(IPath path) {
		try {
			return URI.createHierarchicalURI(SCHEME_JAVA, null, null, path
					.segments(), null, null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static IPath toPath(URI uri) {
		if (isRelated(uri))
			return new Path(uri.path());
		return null;
	}

	public static URI toURI(IJavaElement element) {
		return toURI(element, null);
	}

	public static IPath toPath(IJavaElement element, String newName) {
		IPath path = element.getPath();
		IType jType = null;
		String type = null;
		String name = null;
		try {
			switch (element.getElementType()) {
			case IJavaElement.LOCAL_VARIABLE:
			case IJavaElement.IMPORT_CONTAINER:
			case IJavaElement.IMPORT_DECLARATION:
			case IJavaElement.PACKAGE_DECLARATION:
			case IJavaElement.TYPE_PARAMETER:
				// do not support URI for this type
				return null;
			case IJavaElement.JAVA_MODEL:
			case IJavaElement.JAVA_PROJECT:
			case IJavaElement.PACKAGE_FRAGMENT:
			case IJavaElement.PACKAGE_FRAGMENT_ROOT:
				// URI for this type equals with element path
				if (newName != null) {
					boolean haveRealPath = false;
					IJavaElement parent = element.getParent();
					if (parent instanceof IParent) {
						try {
							IJavaElement[] elements = ((IParent) parent)
									.getChildren();
							for (IJavaElement iJavaElement : elements) {
								if (newName.equals(iJavaElement
										.getElementName())) {
									path = iJavaElement.getPath();
									haveRealPath = true;
									break;
								}
							}
						} catch (Exception e) {
							// ignore exceptions
						}
					}
					if (!haveRealPath) {
						String pathName = element.getElementName();
						IPath oldPath = convertToPath(pathName);
						IPath newPath = convertToPath(newName);
						path = path.removeLastSegments(oldPath.segmentCount());
						path = path.append(newPath);
					}
				}
				break;
			case IJavaElement.TYPE:
				type = ((IType) element).getFullyQualifiedName();
				if (newName != null) {
					type = type.substring(0, type.length()
							- element.getElementName().length())
							+ newName;
					String last = path.lastSegment();
					int index = last.indexOf('.');
					String postfix = index >= 0 ? last.substring(index) : "";
					path = path.removeLastSegments(1);
					path = path.append(newName + postfix);
				}
				break;
			case IJavaElement.CLASS_FILE:
				jType = ((IClassFile) element).getType();
				if (jType != null)
					type = jType.getFullyQualifiedName();
				break;
			case IJavaElement.COMPILATION_UNIT:
				jType = ((ICompilationUnit) element).findPrimaryType();
				if (jType != null)
					type = jType.getFullyQualifiedName();
				break;
			case IJavaElement.METHOD:
				IMethod method = (IMethod) element;
				jType = method.getDeclaringType();
				if (jType != null)
					type = jType.getFullyQualifiedName();
				if (newName != null) {
					name = newName
							+ jType.getMethod(newName,
									method.getParameterTypes()).getSignature();
				} else
					name = method.getElementName() + method.getSignature();
				break;
			case IJavaElement.FIELD:
				IField field = (IField) element;
				jType = field.getDeclaringType();
				if (jType != null)
					type = jType.getFullyQualifiedName();
				name = newName == null ? field.getElementName() : newName;
				break;
			case IJavaElement.INITIALIZER:
				IInitializer initializer = (IInitializer) element;
				jType = initializer.getDeclaringType();
				if (jType != null)
					type = jType.getFullyQualifiedName();
				name = initializer.getElementName();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (type != null) {
			path = path.append(type);
			if (name != null)
				path = path.append(name);
		}
		return path;
	}

	public static URI toURI(IJavaElement element, String newName) {
		IPath path = toPath(element, newName);
		if (path == null)
			return null;
		return toURI(path);
	}

	private static IPath convertToPath(String path) {
		return new Path(path);
	}

	private static IType getType(ITypeRoot root, String className)
			throws JavaModelException {
		String[] parts = className.split("\\$");
		if (parts.length == 0)
			return root.findPrimaryType();

		IType type = root.findPrimaryType();
		// IType[] types = type.getTypes();
		for (int j = 1; type != null && j < parts.length; j++) {
			try {
				int number = Integer.parseInt(parts[j]);
				type = type.getType("", number);
			} catch (NumberFormatException e) {
				type = type.getType(parts[j]);
			}
		}
		return type;
	}

	private static IType getType(String path, String name) {
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				new Path(path));
		if (res != null) {
			IJavaElement element = (IJavaElement) Platform.getAdapterManager()
					.getAdapter(res, IJavaElement.class);
			if (element instanceof ITypeRoot) {
				try {
					return getType((ITypeRoot) element, name);
				} catch (Exception e) {
					// ignore
				}
			}
		} else {
			return findType(name, new NullProgressMonitor());
		}
		return null;
	}

	public static IType findType(String className, IProgressMonitor monitor) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject[] projects = workspace.getRoot().getProjects();

		try {
			for (int i = 0; i < projects.length; i++) {
				IProject project = projects[i];
				if (!project.isAccessible()
						|| !project.hasNature(JavaCore.NATURE_ID))
					continue;

				IJavaProject javaProject = JavaCore.create(project);

				String[] parts = className.split("\\$");
				if (parts.length == 0)
					return javaProject.findType(className, monitor);

				IType type = javaProject.findType(parts[0], monitor);
				// IType[] types = type.getTypes();
				for (int j = 1; type != null && j < parts.length; j++) {
					try {
						int number = Integer.parseInt(parts[j]);
						type = type.getType("", number);
					} catch (NumberFormatException e) {
						type = type.getType(parts[j]);
					}
				}

				return type;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static IJavaElement toJava(URI uri) {
		String path = uri.toString();
		boolean isJar = false;
		int sourceIndex = path.indexOf(".java");
		if (sourceIndex < 0) {
			sourceIndex = path.indexOf(".jar");
			isJar = sourceIndex >= 0;
		}
		if (sourceIndex >= 0) {
			int next = path.indexOf('/', sourceIndex);
			int first = path.indexOf('/');
			if (next >= 0 && path.length() > next + 1 && first >= 0) {
				String newPath = path.substring(next + 1);
				String allPath = path.substring(first + 1, next);
				if (isJar) {
					return getJar(allPath);
				}
				int b = newPath.indexOf('/');
				if (b < 0) {
					// this is type
					return getType(allPath, newPath);
				} else {
					String type = newPath.substring(0, b);
					String name = newPath.substring(b + 1);
					IType classType = getType(allPath, type);
					if (classType == null)
						return null;
					int bc = name.indexOf("(");
					if (bc >= 0) {
						// this is method
						String methodName = name.substring(0, bc);
						String methodSignature = name.substring(bc);
						try {
							IMethod[] methods = classType.getMethods();
							for (int i = 0; i < methods.length; i++) {
								IMethod method = methods[i];
								if (method.getElementName().equals(methodName)
										&& method.getSignature().equals(
												methodSignature)) {
									return method;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						// this is field
						return classType.getField(name);
					}
				}
				return null;
			}
		}
		// java resource
		IPath resourcePath = toPath(uri);
		if (resourcePath != null) {
			IResource resource = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(resourcePath);
			if (resource != null)
				return (IJavaElement) Platform.getAdapterManager().getAdapter(
						resource, IJavaElement.class);
		}
		return null;
	}

	private static IJavaElement getJar(String allPath) {
		IResource res = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(allPath));
		if (res != null) {
			IJavaElement element = (IJavaElement) Platform.getAdapterManager()
					.getAdapter(res, IJavaElement.class);
			return element;
		}
		return null;
	}

}
