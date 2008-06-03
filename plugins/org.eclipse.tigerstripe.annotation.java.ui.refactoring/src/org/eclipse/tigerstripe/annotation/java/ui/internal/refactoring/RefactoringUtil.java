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
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.IElementChanges;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.JavaChanges;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.ResourceChanges;

/**
 * @author Yuri Strot
 *
 */
public class RefactoringUtil {
	
	public static class RenameJavaResult {
		
		private boolean typeParameter;
		private IJavaElement element;
		private String name;
		private String parameter;
		
		public RenameJavaResult(boolean typeParameter, IJavaElement element, 
				String name, String parameter) {
	        this.typeParameter = typeParameter;
	        this.element = element;
	        this.name = name;
	        this.parameter = parameter;
        }
		
		public IJavaElement getElement() {
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

	/** The policy attribute */
	private static final String ATTRIBUTE_POLICY= "policy"; //$NON-NLS-1$
	
	public static RenameJavaResult getElement(RefactoringDescriptor des) {
		RefactoringContribution contr = RefactoringCore.getRefactoringContribution(des.getID());
		String project = des.getProject();
		Map<?, ?> attrs = contr.retrieveArgumentMap(des);
		
		String name = (String)attrs.get(ATTRIBUTE_NAME);
		String parameter = (String)attrs.get(ATTRIBUTE_PARAMETER);
		IJavaElement element = null;
		String path = (String)attrs.get(ATTRIBUTE_INPUT);
		if (path != null)
			element = getJavaElement(project, path);
		boolean typeParameter = des.getID().equals(IJavaRefactorings.RENAME_TYPE_PARAMETER);
		return new RenameJavaResult(typeParameter, element, name, parameter);
		
	}
	
	public static IElementChanges[] getResources(RefactoringDescriptor des) {
		RefactoringContribution contr = RefactoringCore.getRefactoringContribution(des.getID());
		String project = des.getProject();
		Map<?, ?> fArguments = contr.retrieveArgumentMap(des);
		
		String fMovePolicy= JavaRefactoringDescriptorUtil.getString(fArguments, ATTRIBUTE_POLICY);
		
		Object fDestination= JavaRefactoringDescriptorUtil.getJavaElement(fArguments, ATTRIBUTE_DESTINATION, project, true);
		if (fDestination == null)
			fDestination= JavaRefactoringDescriptorUtil.getResourcePath(fArguments, ATTRIBUTE_TARGET, project);
		
		List<IElementChanges> changes = new ArrayList<IElementChanges>();
		
		if (POLICY_MOVE_RESOURCES.equals(fMovePolicy)) {
			int offset= 1;
			IPath[] fFiles= JavaRefactoringDescriptorUtil.getResourcePathArray(fArguments, ATTRIBUTE_FILES, ATTRIBUTE_ELEMENT, offset, project);
			changes.addAll(toChanges(fFiles));
			offset+= fFiles.length;
			IPath[] fFolders= JavaRefactoringDescriptorUtil.getResourcePathArray(fArguments, ATTRIBUTE_FOLDERS, ATTRIBUTE_ELEMENT, offset, project);
			changes.addAll(toChanges(fFolders));
			offset+= fFolders.length;
			ICompilationUnit[] units = (ICompilationUnit[]) JavaRefactoringDescriptorUtil.getJavaElementArray(fArguments, ATTRIBUTE_UNITS, ATTRIBUTE_ELEMENT, offset, project, ICompilationUnit.class);
			changes.addAll(toChanges(units));
		} else if (POLICY_MOVE_ROOTS.equals(fMovePolicy)) {
			IPackageFragmentRoot[] roots = (IPackageFragmentRoot[]) JavaRefactoringDescriptorUtil.getJavaElementArray(fArguments, ATTRIBUTE_ROOTS, ATTRIBUTE_ELEMENT, 1, project, IPackageFragmentRoot.class);
			changes.addAll(toChanges(roots));
		} else if (POLICY_MOVE_PACKAGES.equals(fMovePolicy)) {
			IPackageFragment[] fragments = (IPackageFragment[]) JavaRefactoringDescriptorUtil.getJavaElementArray(fArguments, ATTRIBUTE_FRAGMENTS, ATTRIBUTE_ELEMENT, 1, project, IPackageFragment.class);
			changes.addAll(toChanges(fragments));
		}
//		TODO: members ignored
//		else if (POLICY_MOVE_MEMBERS.equals(fMovePolicy)) {
//			IMember[] fMembers= (IMember[]) JavaRefactoringDescriptorUtil.getJavaElementArray(fArguments, ATTRIBUTE_MEMBERS, ATTRIBUTE_ELEMENT, 1, project, IMember.class);
//		}
		
		return changes.toArray(new IElementChanges[changes.size()]);
	}
	
	protected static List<IElementChanges> toChanges(IPath[] paths) {
		List<IElementChanges> changes = new ArrayList<IElementChanges>();
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (int i = 0; i < paths.length; i++) {
			IResource resource = root.findMember(paths[i]);
			if (resource != null)
				changes.add(new ResourceChanges(resource));
		}
		return changes;
	}
	
	protected static List<IElementChanges> toChanges(IJavaElement[] elements) {
		ArrayList<IElementChanges> changes = new ArrayList<IElementChanges>();
		for (int i = 0; i < elements.length; i++) {
			changes.add(new JavaChanges(elements[i]));
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
	
	public static IResource getDestination(RefactoringDescriptor des) {
		RefactoringContribution contr = RefactoringCore.getRefactoringContribution(des.getID());
		String project = des.getProject();
		Map<?, ?> attrs = contr.retrieveArgumentMap(des);
		IResource resource = null;
		String destination = (String)attrs.get(ATTRIBUTE_DESTINATION);
		if (destination != null) {
			IJavaElement element = getJavaElement(project, destination);
			if (element != null)
				resource = (IResource)Platform.getAdapterManager().getAdapter(element, IResource.class);
		}
		
		if (resource != null) return resource;
		
		String target = (String)attrs.get(ATTRIBUTE_TARGET);
		if (target != null)
			//target resource can be not in the same project.
			//Therefor project parameter is null 
			resource = getResource(null, target);
		
		return resource;
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

}
