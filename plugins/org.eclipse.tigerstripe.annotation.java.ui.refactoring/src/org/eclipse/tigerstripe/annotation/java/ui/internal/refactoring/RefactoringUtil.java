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
package org.eclipse.tigerstripe.annotation.java.ui.internal.refactoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.resource.MoveResourcesDescriptor;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceDescriptor;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;

/**
 * @author Yuri Strot
 *
 */
public class RefactoringUtil {
	
	public static class RenameJavaResult {
		
		private boolean typeParameter;
		private ILazyObject element;
		private String name;
		private String parameter;
		
		public RenameJavaResult(boolean typeParameter, ILazyObject element, 
				String name, String parameter) {
	        this.typeParameter = typeParameter;
	        this.element = element;
	        this.name = name;
	        this.parameter = parameter;
        }
		
		public ILazyObject getElement() {
	        return element;
        }
		
		public String getName() {
	        return name;
        }
		
		public boolean isTypeParameter() {
	        return typeParameter;
        }
		
		public String getParameter() {
	        return parameter;
        }
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			RenameJavaResult res = (RenameJavaResult)obj;
			if (typeParameter != res.typeParameter)
				return false;
			if (!name.equals(res.name))
				return false;
			if (!parameter.equals(res.parameter))
				return false;
			if (!element.equals(res.element))
				return false;
			return true;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return element.hashCode() >> 16 ^ name.hashCode();
		}
		
	}
	
	private static final String ATTR_INPUT = "input";
	private static final String ATTR_NAME = "name";

	/**
	 * Predefined argument called <code>element&lt;Number&gt;</code>.
	 * <p>
	 * This argument should be used to describe the elements being refactored.
	 * The value of this argument does not necessarily have to uniquely identify
	 * the elements. However, it must be possible to uniquely identify the
	 * elements using the value of this argument in conjunction with the values
	 * of the other user-defined attributes.
	 * </p>
	 * <p>
	 * The element arguments are simply distinguished by appending a number to
	 * the argument name, e.g. element1. The indices of this argument are one-based.
	 * </p>
	 */
	protected static final String ATTRIBUTE_ELEMENT= "element"; //$NON-NLS-1$

	/**
	 * Predefined argument called <code>input</code>.
	 * <p>
	 * This argument should be used to describe the element being refactored.
	 * The value of this argument does not necessarily have to uniquely identify
	 * the input element. However, it must be possible to uniquely identify the
	 * input element using the value of this argument in conjunction with the
	 * values of the other user-defined attributes.
	 * </p>
	 */
	protected static final String ATTRIBUTE_INPUT= "input"; //$NON-NLS-1$

	/**
	 * Predefined argument called <code>name</code>.
	 * <p>
	 * This argument should be used to name the element being refactored. The
	 * value of this argument may be shown in the user interface.
	 * </p>
	 */
	protected static final String ATTRIBUTE_NAME= "name"; //$NON-NLS-1$

	/** The parameter attribute */
	protected static final String ATTRIBUTE_PARAMETER= "parameter"; //$NON-NLS-1$
	
	/** The files attribute */
	private static final String ATTRIBUTE_FILES= "files"; //$NON-NLS-1$
	/** The folders attribute */
	private static final String ATTRIBUTE_FOLDERS= "folders"; //$NON-NLS-1$

	/** The fragments attribute */
	private static final String ATTRIBUTE_FRAGMENTS= "fragments"; //$NON-NLS-1$

	/** The roots attribute */
	private static final String ATTRIBUTE_ROOTS= "roots"; //$NON-NLS-1$

	/** The units attribute */
	private static final String ATTRIBUTE_UNITS= "units"; //$NON-NLS-1$

	/** The destination attribute */
	private static final String ATTRIBUTE_DESTINATION= "destination"; //$NON-NLS-1$

	/** The target attribute */
	private static final String ATTRIBUTE_TARGET= "target"; //$NON-NLS-1$
//
//	/** The move members policy */
//	private static final String POLICY_MOVE_MEMBERS= "org.eclipse.jdt.ui.moveMembers"; //$NON-NLS-1$

	/** The move packages policy */
	private static final String POLICY_MOVE_PACKAGES= "org.eclipse.jdt.ui.movePackages"; //$NON-NLS-1$

	/** The move resources policy */
	private static final String POLICY_MOVE_RESOURCES= "org.eclipse.jdt.ui.moveResources"; //$NON-NLS-1$

	/** The move package fragment roots policy */
	private static final String POLICY_MOVE_ROOTS= "org.eclipse.jdt.ui.moveRoots"; //$NON-NLS-1$

	/** The move packages policy */
	private static final String POLICY_COPY_PACKAGES= "org.eclipse.jdt.ui.copyPackages"; //$NON-NLS-1$

	/** The move resources policy */
	private static final String POLICY_COPY_RESOURCES= "org.eclipse.jdt.ui.copyResources"; //$NON-NLS-1$

	/** The move package fragment roots policy */
	private static final String POLICY_COPY_ROOTS= "org.eclipse.jdt.ui.copyRoots"; //$NON-NLS-1$

	/** The policy attribute */
	private static final String ATTRIBUTE_POLICY= "policy"; //$NON-NLS-1$
	
	public static RenameJavaResult getElement(RefactoringDescriptor des) {
		RefactoringContribution contr = RefactoringCore.getRefactoringContribution(des.getID());
		String project = des.getProject();
		Map<?, ?> attrs = contr.retrieveArgumentMap(des);
		
		String name = (String)attrs.get(ATTRIBUTE_NAME);
		String parameter = (String)attrs.get(ATTRIBUTE_PARAMETER);
		ILazyObject element = null;
		String path = (String)attrs.get(ATTRIBUTE_INPUT);
		if (path != null)
			element = new JavaLazyObject(project, path);
		boolean typeParameter = des.getID().equals(IJavaRefactorings.RENAME_TYPE_PARAMETER);
		return new RenameJavaResult(typeParameter, element, name, parameter);
		
	}
	
	public static ILazyObject[] getResources(RefactoringDescriptor des) {
		RefactoringContribution contr = RefactoringCore.getRefactoringContribution(des.getID());
		String project = des.getProject();
		Map<?, ?> fArguments = contr.retrieveArgumentMap(des);
		
		String fMovePolicy= JavaRefactoringDescriptorUtil.getString(fArguments, ATTRIBUTE_POLICY);
		
		Object fDestination= JavaRefactoringDescriptorUtil.getJavaElement(fArguments, ATTRIBUTE_DESTINATION, project, true);
		if (fDestination == null)
			fDestination= JavaRefactoringDescriptorUtil.getResourcePath(fArguments, ATTRIBUTE_TARGET, project);
		
		List<ILazyObject> changes = new ArrayList<ILazyObject>();

		if (POLICY_MOVE_RESOURCES.equals(fMovePolicy) || POLICY_COPY_RESOURCES.equals(fMovePolicy)) {
			int offset= 1;
			IPath[] fFiles= JavaRefactoringDescriptorUtil.getResourcePathArray(fArguments, ATTRIBUTE_FILES, ATTRIBUTE_ELEMENT, offset, project);
			changes.addAll(toChanges(fFiles));
			offset+= fFiles.length;
			IPath[] fFolders= JavaRefactoringDescriptorUtil.getResourcePathArray(fArguments, ATTRIBUTE_FOLDERS, ATTRIBUTE_ELEMENT, offset, project);
			changes.addAll(toChanges(fFolders));
			offset+= fFolders.length;
			ILazyObject[] units = JavaRefactoringDescriptorUtil.getJavaElementArray(fArguments, ATTRIBUTE_UNITS, ATTRIBUTE_ELEMENT, offset, project);
			changes.addAll(Arrays.asList(units));
		} else if (POLICY_MOVE_ROOTS.equals(fMovePolicy) || POLICY_COPY_ROOTS.equals(fMovePolicy)) {
			ILazyObject[] roots = (ILazyObject[]) JavaRefactoringDescriptorUtil.getJavaElementArray(fArguments, ATTRIBUTE_ROOTS, ATTRIBUTE_ELEMENT, 1, project);
			changes.addAll(Arrays.asList(roots));
		} else if (POLICY_MOVE_PACKAGES.equals(fMovePolicy) || POLICY_COPY_PACKAGES.equals(fMovePolicy)) {
			ILazyObject[] fragments = (ILazyObject[]) JavaRefactoringDescriptorUtil.getJavaElementArray(fArguments, ATTRIBUTE_FRAGMENTS, ATTRIBUTE_ELEMENT, 1, project);
			changes.addAll(Arrays.asList(fragments));
		}
//		TODO: members ignored
//		else if (POLICY_MOVE_MEMBERS.equals(fMovePolicy)) {
//			IMember[] fMembers= (IMember[]) JavaRefactoringDescriptorUtil.getJavaElementArray(fArguments, ATTRIBUTE_MEMBERS, ATTRIBUTE_ELEMENT, 1, project, IMember.class);
//		}
		
		return changes.toArray(new ILazyObject[changes.size()]);
	}
	
	protected static List<ILazyObject> toChanges(IPath[] paths) {
		List<ILazyObject> changes = new ArrayList<ILazyObject>();
		for (int i = 0; i < paths.length; i++) {
			changes.add(new ResourceLazyObject(paths[i]));
		}
		return changes;
	}
	
	/**
	 * Converts an input handle back to the corresponding java element.
	 * 
	 * @param project
	 *            the project, or <code>null</code> for the workspace
	 * @param handle
	 *            the input handle
	 * @return the corresponding java element, or <code>null</code> if no such
	 *         element exists
	 */
	protected static IJavaElement getJavaElement(final String project, final String handle) {
		return getJavaElement(null, project, handle, true);
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
	protected static IJavaElement getJavaElement(final WorkingCopyOwner owner, final String project, final String handle, final boolean check) {
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
			final IMethod method= (IMethod) element;
			final IMethod[] methods= method.getDeclaringType().findMethods(method);
			if (methods != null && methods.length > 0)
				element= methods[0];
		}
		if (element != null && (!check || element.exists()))
			return element;
		return null;
	}
	
	public static ILazyObject getDestination(RefactoringDescriptor des) {
		RefactoringContribution contr = RefactoringCore.getRefactoringContribution(des.getID());
		String project = des.getProject();
		Map<?, ?> attrs = contr.retrieveArgumentMap(des);
		ILazyObject element = null;
		String destination = (String)attrs.get(ATTRIBUTE_DESTINATION);
		if (destination != null) {
			element = new JavaLazyObject(project, destination);
		}
		
		if (element != null)
			return element;
		
		String target = (String)attrs.get(ATTRIBUTE_TARGET);
		if (target != null) {
			IPath path = getResourcePath(null, target);
			if (path != null)
				element = new ResourceLazyObject(path);
		}
		return element;
	}
	
	public static IPath getResourcePath(RefactoringDescriptor rrd) {
		RefactoringContribution contr = RefactoringCore.getRefactoringContribution(rrd.getID());
		String project = rrd.getProject();
		Map<?, ?> attrs = contr.retrieveArgumentMap(rrd);
		String input = (String)attrs.get(ATTR_INPUT);
		return getResourcePath(project, input);
	}
	
	public static IPath getNewPath(IPath path, RefactoringDescriptor rrd) {
		RefactoringContribution contr = RefactoringCore.getRefactoringContribution(rrd.getID());
		Map<?, ?> attrs = contr.retrieveArgumentMap(rrd);
		String name = (String)attrs.get(ATTR_NAME);
		IPath path1 = path;
		IPath path2 = path1.removeLastSegments(1).append(name);
		return path2;
	}
	
	public static IPath getResourcePath(String project, String handle) {
		IPath path = Path.fromPortableString(handle);
		if (path == null)
			return null;
		if (project != null && !"".equals(project)) {
			path = new Path(project).append(path);
		}
		return path;
	}
	
	public static IResource getResource(String project, String handle) {
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
	
	public static ILazyObject getDestination(MoveResourcesDescriptor des) {
		return new ResourceLazyObject(des.getDestinationPath());
	}
	
	public static ILazyObject[] getResourcesPath(MoveResourcesDescriptor des) {
		IPath[] paths = des.getResourcePathsToMove();
		ResourceLazyObject[] objects = new ResourceLazyObject[paths.length];
		for (int i = 0; i < objects.length; i++)
			objects[i] = new ResourceLazyObject(paths[i]);
		return objects;
	}
	
	public static IPath getResourcePath(RenameResourceDescriptor rrd) {
		return rrd.getResourcePath();
	}
	
	public static IPath getNewPath(IPath path, RenameResourceDescriptor rrd) {
		String name = rrd.getNewName();
		IPath path2 = path.removeLastSegments(1).append(name);
		return path2;
	}

}
