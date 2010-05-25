/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.java.ui.internal.refactoring;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;

/**
 * @author Yuri Strot
 *
 */
public class JavaRefactoringDescriptorUtil {

	/**
	 * Retrieves an <code>IResource[]</code> attribute from map.
	 * 
	 * @param map the map with <code>&lt;String, String&gt;</code> mapping
	 * @param countAttribute the attribute that contains the number of elements
	 * @param arrayAttribute the attribute name where the values are stored. The index starting from offset is appended to this
	 * @param offset the starting index for arrayAttribute
	 * @param project the project for resolving the java element. Can be <code>null</code> for workspace
	 * @return the <code>IResource[]</code>
	 * 
	 * @throws IllegalArgumentException if any of the attribute does not exist or is not a number
	 */
	public static IPath[] getResourcePathArray(Map<?, ?> map, String countAttribute, String arrayAttribute, int offset, String project)  throws IllegalArgumentException{
		int count= getInt(map, countAttribute);
		IPath[] result= new IPath[count];
		for (int i= 0; i < count; i++) {
			result[i]= getResourcePath(map, getAttributeName(arrayAttribute, i + offset), project);
		}
		return result;
	}

	/**
	 * Retrieves and resolves an <code>{@link IResource}</code> attribute from map.
	 * 
	 * @param map the map with <code>&lt;String, String&gt;</code> mapping
	 * @param attribute the key in the map
	 * @param project the project for resolving the resource. Can be <code>null</code> for workspace
	 * @return the <code>{@link IResource}</code>, or <code>null</code> if the resource does not exist
	 * 
	 * @throws IllegalArgumentException if the attribute does not exist
	 * @see #handleToResource(String, String)
	 */
	public static IPath getResourcePath(Map<?, ?> map, String attribute, String project)  throws IllegalArgumentException {
		String handle= getString(map, attribute);
		return handleToResourcePath(project, handle);
	}

	/**
	 * Converts an input handle with the given prefix back to the corresponding
	 * resource.
	 * WARNING: this method resolves the handle in the current workspace, since 
	 * the type of the resource (file/folder) cannot be determined from the
	 * handle alone (path never has a trailing separator).
	 * 
	 * @param project
	 *            the project, or <code>null</code> for the workspace
	 * @param handle
	 *            the input handle
	 * 
	 * @return the corresponding resource, or <code>null</code> if no such
	 *         resource exists
	 */
	public static IResource handleToResource(final String project, final String handle) {
		final IWorkspaceRoot root= ResourcesPlugin.getWorkspace().getRoot();
		if ("".equals(handle)) //$NON-NLS-1$
			return null;
		final IPath path= Path.fromPortableString(handle);
		if (path == null)
			return null;
		if (project != null && !"".equals(project)) //$NON-NLS-1$
			return root.getProject(project).findMember(path);
		return root.findMember(path);
	}

	/**
	 * Converts an input handle with the given prefix back to the corresponding
	 * resource path.
	 * 
	 * @param project
	 *            the project, or <code>null</code> for the workspace
	 * @param handle
	 *            the input handle
	 * 
	 * @return the corresponding resource path.
	 *         Note: if the given handle is absolute, the project is not used to resolve.
	 */
	public static IPath handleToResourcePath(final String project, final String handle) {
		final IPath path= Path.fromPortableString(handle);
		if (project != null && !"".equals(project) && ! path.isAbsolute()) //$NON-NLS-1$
			return new Path(project).append(path).makeAbsolute();
		return path;
	}

	/**
	 * Retrieves a {@link String} attribute from map.
	 * 
	 * @param map the map with <code>&lt;String, String&gt;</code> mapping
	 * @param attribute the key in the map
	 * @return the value of the attribute
	 * 
	 * @throws IllegalArgumentException if the value of the attribute is not a {@link String} or the attribute does not exist
	 */
	public static String getString(Map<?, ?> map, String attribute)  throws IllegalArgumentException {
		return getString(map, attribute, false);
	}

	/**
	 * Retrieves a {@link String} attribute from map.
	 * 
	 * @param map the map with <code>&lt;String, String&gt;</code> mapping
	 * @param attribute the key in the map
	 * @param allowNull if <code>true</code> a <code>null</code> will be returned if the attribute does not exist
	 * @return the value of the attribute
	 * 
	 * @throws IllegalArgumentException if the value of the attribute is not a {@link String} or allowNull is <code>false</code> and the attribute does not exist
	 */
	public static String getString(Map<?, ?> map, String attribute, boolean allowNull) throws IllegalArgumentException {
		Object object= map.get(attribute);
		if (object == null) {
			if (allowNull)
				return null;
			throw new IllegalArgumentException("The map does not contain the attribute '" + attribute + "'"); //$NON-NLS-1$//$NON-NLS-2$
		}
		if (object instanceof String) {
			String value= (String) object;
			return value;
		}
		throw new IllegalArgumentException("The provided map does not contain a string for attribute '" + attribute + "'"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Retrieves an <code>{@link IJavaElement}</code> attribute from map.
	 * 
	 * @param map the map with <code>&lt;String, String&gt;</code> mapping
	 * @param attribute the key in the map
	 * @param project the project for resolving the java element. Can be <code>null</code> for workspace
	 * @return a {@link IJavaElement} or <code>null</code>
	 * 
	 * @throws IllegalArgumentException if the attribute does not exist or is not a java element
	 * @see #handleToElement(WorkingCopyOwner, String, String, boolean)
	 */
	public static ILazyObject getJavaElement(Map<?, ?> map, String attribute, String project)  throws IllegalArgumentException{
		return getJavaElement(map, attribute, project, false);
	}

	/**
	 * Retrieves an <code>{@link IJavaElement}</code> attribute from map.
	 * 
	 * @param map the map with <code>&lt;String, String&gt;</code> mapping
	 * @param attribute the key in the map
	 * @param project the project for resolving the java element. Can be <code>null</code> for workspace
	 * @param allowNull if <code>true</code> a <code>null</code> will be returned if the attribute does not exist
	 * @return a {@link IJavaElement} or <code>null</code>
	 * 
	 * @throws IllegalArgumentException if the attribute does not existt
	 * @see #handleToElement(WorkingCopyOwner, String, String, boolean)
	 */
	public static ILazyObject getJavaElement(Map<?, ?> map, String attribute, String project, boolean allowNull)  throws IllegalArgumentException{
		String handle= getString(map, attribute, allowNull);
		if (handle != null)
			return new JavaLazyObject(project, handle); //TODO: update Javadoc
		return null;
	}

	/**
	 * Retrieves an <code>IJavaElement[]</code> attribute from map.
	 * 
	 * @param map the map with <code>&lt;String, String&gt;</code> mapping
	 * @param countAttribute the attribute that contains the number of elements. Can be <code>null</code> to indicate that no count attribute exists
	 * @param arrayAttribute the attribute name where the values are stored. The index starting from offset is appended to this
	 * @param offset the starting index for arrayAttribute
	 * @param project the project for resolving the java element. Can be <code>null</code> for workspace
	 * @param arrayClass the component type for the resulting array. The resulting array can then be safely casted to arrayClass[] 
	 * @return the <code>IJavaElement[]</code>
	 * 
	 * @throws IllegalArgumentException if any of the attribute does not exist or is not a number
	 */
	public static ILazyObject[] getJavaElementArray(Map<?, ?> map, String countAttribute, String arrayAttribute, int offset, String project)  throws IllegalArgumentException{
		if (countAttribute != null) {
			int count= getInt(map, countAttribute);
			ILazyObject[] result= new ILazyObject[count];
			for (int i= 0; i < count; i++) {
				result[i]= getJavaElement(map, getAttributeName(arrayAttribute, i + offset), project);
			}
			return result;
		} else {
			ArrayList<ILazyObject> result= new ArrayList<ILazyObject>();
			ILazyObject element= null;
			while ((element= getJavaElement(map, arrayAttribute, project, true)) != null){
				result.add(element);
			}
			return (ILazyObject[]) result.toArray(new ILazyObject[result.size()]);
		}
	}

	/**
	 * Create the name for accessing the ith element of an attribute.
	 * 
	 * @param attribute the base attribute 
	 * @param index the index that should be accessed
	 * @return the attribute name for the ith element of an attribute
	 */
	public static String getAttributeName(String attribute, int index) {
		return attribute + index;
	}

	/**
	 * Retrieves an <code>int</code> attribute from map.
	 * 
	 * @param map the map with <code>&lt;String, String&gt;</code> mapping
	 * @param attribute the key in the map
	 * @return the value of the attribute
	 * 
	 * @throws IllegalArgumentException if the attribute does not exist or is not a number
	 */
	public static int getInt(Map<?, ?> map, String attribute)  throws IllegalArgumentException{
		String value= getString(map, attribute);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("The attribute '" + attribute + "' does not contain a valid int '" + value + "'"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}
	}

	/**
	 * Converts an input handle back to the corresponding java element.
	 * 
	 * @param owner
	 *            the working copy owner
	 * @param project
	 *            the project, or <code>null</code> for the workspace
	 * @param handle
	 *            the input handle
	 * @param check
	 *            <code>true</code> to check for existence of the element,
	 *            <code>false</code> otherwise
	 * @return the corresponding java element, or <code>null</code> if no such
	 *         element exists
	 */
	public static IJavaElement handleToElement(final WorkingCopyOwner owner, final String project, final String handle, final boolean check) {
		IJavaElement element= null;
		if (owner != null)
			element= JavaCore.create(handle, owner);
		else
			element= JavaCore.create(handle);
		if (element == null && project != null) {
			final IJavaProject javaProject= JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProject(project);
			final String identifier= javaProject.getHandleIdentifier();
			if (owner != null)
				element= JavaCore.create(identifier + handle, owner);
			else
				element= JavaCore.create(identifier + handle);
		}
		if (check && element instanceof IMethod) {
			/*
			 * Resolve the method based on simple names of parameter types
			 * (to accommodate for different qualifications when refactoring is e.g.
			 * recorded in source but applied on binary method):
			 */
			final IMethod method= (IMethod) element;
			final IMethod[] methods= method.getDeclaringType().findMethods(method);
			if (methods != null && methods.length > 0)
				element= methods[0];
		}
		if (element != null && (!check || element.exists()))
			return element;
		return null;
	}

}
