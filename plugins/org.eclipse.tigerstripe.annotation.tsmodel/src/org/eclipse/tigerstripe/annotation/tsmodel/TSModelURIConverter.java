/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    John Worrell (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.tsmodel;

import static org.eclipse.jdt.core.IJavaElement.COMPILATION_UNIT;
import static org.eclipse.jdt.core.IJavaElement.CLASS_FILE;
import static org.eclipse.jdt.core.IJavaElement.JAVA_MODEL;
import static org.eclipse.jdt.core.IJavaElement.JAVA_PROJECT;
import static org.eclipse.jdt.core.IJavaElement.PACKAGE_FRAGMENT;
import static org.eclipse.jdt.core.IJavaElement.PACKAGE_FRAGMENT_ROOT;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author John Worrell
 * 
 */
public class TSModelURIConverter {
	
	private static final String SCHEME_TS = "tigerstripe";
	
	/**
	 * Returns the value <code>true</code> if the supplied URI is for the same
	 * scheme as that converted by an instance of this converter
	 * @param uri the URI whose scheme we wish to test for compatibility with
	 * an instance of this converter.
	 * @return the value <code>true</code> if the supplied URI is for the same
	 * scheme as that converted by an instance of this converter
	 */
	public static boolean isRelated(URI uri) {
		return SCHEME_TS.equals(uri.scheme());
	}
	
	/**
	 * Returns a URI that identifies the target of an annotation and which allows that target
	 * to be looked up in the Tigerstripe workbench
	 * @param element the <code>IModelComponent</code> for which we require a URI
	 * @return a URI that identifies the target of an annotation and which allows that target
	 * to be looked up in the Tigerstripe workbench
	 * @throws TigerstripeException
	 */
	public static URI toURI(IModelComponent element) throws TigerstripeException
	{
		return toURI(element, null);
	}
	
	/**
	 * Returns a URI that identifies the target of an annotation and which allows that target
	 * to be looked up in the Tigerstripe workbench, but accepts a second argument whose
	 * content will replace a part of the URI that is appropriate to the type of
	 * <code>IModelComponent</code> supplied
	 * @param element the <code>IModelComponent</code> for which we require a URI
	 * @param newName the new fragment that will replace a part of the URI that is appropriate
	 * to the type of <code>IModelComponent</code> supplied
	 * @return a URI that identifies the target of an annotation and which allows that target
	 * to be looked up in the Tigerstripe workbench
	 * @throws TigerstripeException
	 */
	public static URI toURI(IModelComponent component, String newName)
	{
		IAbstractArtifact art = getArtifact(component);
		IPath artifactPath = getArtifactPath(art, newName);
		
		String fragment = null;
		
		if (component instanceof IField) {
			fragment = newName == null ? ((IField) component).getName() : newName;
		} else if (component instanceof IMethod) {
			IMethod method = ((IMethod) component);
			StringBuilder b = new StringBuilder(newName == null ? method.getName() : newName);
			b.append("(");
			String comma = null;
			for (IArgument arg : method.getArguments()) {
				if (comma == null) {
					comma = ", ";
				}
				else {
					b.append(comma);
				}
				b.append(arg.getName()).append(":").append(arg.getType().getFullyQualifiedName());
			}
			b.append(")");
			fragment = b.toString();
		} else if (component instanceof ILiteral) {
			fragment = newName == null ? ((ILiteral) component).getName() : newName;
		} else if (component instanceof IAssociationEnd) {
			IAssociationEnd end = (IAssociationEnd) component;
			IAssociationArtifact assoc = end.getContainingAssociation();
			StringBuilder b = new StringBuilder(newName == null ? assoc.getName() : newName);
			if (assoc.getAEnd() == end) {
				b.append(";aEnd");
			} else {
				b.append(";zEnd");
			}
			fragment = b.toString();
		}

		return toURI(artifactPath, fragment);
	}
	
	/**
	 * Returns the <code>IModelComponent</code> that is identified by the supplied
	 * URI
	 * @param uri the URI for which the <code>IModelComponent</code> it identifies is
	 * required
	 * @return the <code>IModelComponent</code> that is identified by the supplied
	 * URI
	 */
	public static IModelComponent toComponent(URI uri) {
		IPath path = new Path(uri.path());
		String fqn = toFQN(path);
		path = path.removeLastSegments(1);
//		System.out.println("TSModelURIConverter.toComponent(...): "+uri+" / "+fqn);
		try {
			IAbstractTigerstripeProject tsp = TigerstripeCore.findProject(path);

			IArtifactManagerSession artifactManagerSession = ((ITigerstripeModelProject)tsp).getArtifactManagerSession();
			return artifactManagerSession.getArtifactByFullyQualifiedName(fqn);
		} catch (TigerstripeException e) {
			AnnotationPlugin.log(e);
			return null;
		}
	}

	/**
	 * Returns the fully-qualified name extracted from the supplied <code>IPath</code>
	 * of the <code>IModelComponent</code> located by the path. The fully-qualified name
	 * forms the last segment of the <code>IPath</code> 
	 * @param artifactPath the <code>IPath</code> from which to extract the fully-qualified
	 * name of an <code>IModelComponent</code>
	 * @return
	 */
	public static String toFQN(IPath artifactPath) {
		return artifactPath.lastSegment();
	}

	/**
	 * Returns the <code>IModelComponent</code> that is associated with the supplied
	 * <code>Object</code> which must implement the interface <code>IJavaElement</code>
	 * for a non-null value to be returned
	 * @param firstElement the <code>Object</code> for which the corresponding
	 * <code>IModelComponent</code> is required
	 */
	public static IModelComponent toModelComponent(Object firstElement) {
		if(!(firstElement instanceof IJavaElement))
				return null;
		IJavaElement element = (IJavaElement)firstElement;
		IJavaProject jp = element.getJavaProject();
//		System.out.println("Element name/project: "+element.getElementName()+" / "+element.getElementType()+" / "+jp.getElementName());
		try {
			IAbstractTigerstripeProject tsp = TigerstripeCore.findProject(jp.getProject().getLocation());
			if(tsp instanceof ITigerstripeModelProject)
			{
				switch(element.getElementType())
				{
				case JAVA_MODEL:
				case JAVA_PROJECT:
				case PACKAGE_FRAGMENT_ROOT:
				case PACKAGE_FRAGMENT:
					break;
				case COMPILATION_UNIT:
				case CLASS_FILE:
				IPath elementPath = element.getPath();
				IPath rootPath    = element.getAncestor(PACKAGE_FRAGMENT_ROOT).getPath();
				IPath typePath = element.getAncestor(COMPILATION_UNIT).getPath();
				IPath artifactPath = typePath.removeFirstSegments(rootPath.segmentCount());
//				System.out.println("Element path: "+elementPath+" / root path: "+rootPath+" / relative: "+artifactPath);
				String fqn = artifactPath.removeTrailingSeparator().toPortableString();
				fqn = fqn.substring(0,fqn.lastIndexOf('.')).replace('/', '.');
				IArtifactManagerSession artifactManagerSession = ((ITigerstripeModelProject)tsp).getArtifactManagerSession();
//				System.out.println("FQN: "+fqn);
				IAbstractArtifact artifact = artifactManagerSession.getArtifactByFullyQualifiedName(fqn, true);
				return artifact;
				default:
				}
			}
		} catch (TigerstripeException e) {
			AnnotationPlugin.log(e);
		}
		return null;
	}
	
	private static IPath getArtifactPath(IAbstractArtifact art, String newName) //throws TigerstripeException
	{
		try {
			IPath path = art.getProject().getLocation();
			path = path.append(newName == null ? art.getFullyQualifiedName() : newName);
			return path;
		} catch (TigerstripeException e) {
			AnnotationPlugin.log(e);
			return null;
		}
	}

	private static IAbstractArtifact getArtifact(IModelComponent component)
	{
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
	{
		try {
			return URI.createHierarchicalURI(SCHEME_TS, null, null, path.segments(), null, fragment);
        }
        catch (IllegalArgumentException e) {
	        e.printStackTrace();
        }
        return null;
	}
}
