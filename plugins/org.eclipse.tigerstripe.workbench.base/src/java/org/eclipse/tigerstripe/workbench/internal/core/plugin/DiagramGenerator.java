/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - jworrell
 *******************************************************************************//**
 * 
 */
package org.eclipse.tigerstripe.workbench.internal.core.plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
//import org.eclipse.core.resources.IFolder;
//import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
//import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
//import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
//import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.rendering.IDiagramRenderer;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IDiagramDescriptor;
import org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator;
import org.eclipse.tigerstripe.workbench.plugins.IDiagramDescriptor.DiagramType;

import static org.eclipse.tigerstripe.workbench.plugins.IDiagramDescriptor.DiagramType;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author jworrell
 *
 */
public class DiagramGenerator implements IDiagramGenerator {
	
	private static final String RENDERER = "org.eclipse.tigerstripe.workbench.ui.rendererplugin.actions.Renderer";
	private ITigerstripeModelProject handle;
	
	protected static class DiagramDescriptor implements IDiagramDescriptor {
		/* (non-Javadoc)
		 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramDescriptor#getName()
		 */
		private String name;

		/* (non-Javadoc)
		 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramDescriptor#getProjectLabel()
		 */
		private String projectLabel;

		/* (non-Javadoc)
		 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramDescriptor#getRelativePath()
		 */
		private String relativePath;
		
		/* (non-Javadoc)
		 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramDescriptor#getDiagramType()
		 */
		private DiagramType diagramType;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the projectLabel
		 */
		public String getProjectLabel() {
			return projectLabel;
		}

		/**
		 * @return the relativePath
		 */
		public String getRelativePath() {
			return relativePath;
		}
		
		/**
		 * @return the diagramType
		 */
		public DiagramType getDiagramType() {
			return diagramType;
		}

		/**
		 * @param name
		 * @param projectLabel
		 * @param relativePath
		 * @param diagramType
		 */
		protected DiagramDescriptor(String name, String projectLabel, String relativePath, DiagramType diagramType) {
			super();
			this.name = name;
			this.projectLabel = projectLabel;
			this.relativePath = relativePath;
			this.diagramType = diagramType;
		}

		public String toString() {
			return "DiagramDescriptor: (name: "+name+" projectLabel: "+projectLabel+" relativePath: "+relativePath+" diagramType: "+diagramType+")";
		}
	}
	
//	private static DiagramGenerator instance;
//	
//	static IDiagramGenerator getInstance()
//	{
//		if(instance == null)
//		{
//			instance = new DiagramGenerator();
//		}
//		return instance;
//	}
	
	public DiagramGenerator(ITigerstripeModelProject handle){
		this.handle = handle;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#generateDiagram(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean generateDiagram(IDiagramDescriptor diagram, String outputPath, String imageType) {
		return generateDiagram(diagram, outputPath, Enum.valueOf(ImageType.class, imageType));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#generateDiagram(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean generateDiagram(IDiagramDescriptor diagram, String outputPath, ImageType imageType) {
		System.out.println("generateDiagram-args: diagram: ("+diagram+") outputPath: "+outputPath+" imageType: "+imageType);
		try {
			IDiagramRenderer renderer = InternalTigerstripeCore.getIDiagramRenderingSession().getRendererByName(RENDERER);
//			IPath projectLocation = handle.getLocation();
			IPath outputDir = new Path(handle.getProjectDetails().getOutputDirectory());
			IPath targetLocation = outputDir.append(outputPath).append(diagram.getName()).addFileExtension(imageType.extension());
			System.out.println("target location: "+targetLocation);
			renderer.renderDiagram(diagram.getProjectLabel(), diagram.getRelativePath(), imageType.extension(), handle.getProjectDetails().getName(), targetLocation.toString());
			return true;
		} catch (TigerstripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(boolean, org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum, java.lang.String)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, String diagramType, String namePattern) {
		return getAllDiagrams(includeDependencies, diagramType!=null?Enum.valueOf(DiagramType.class, diagramType):null, namePattern);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(boolean, org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum, java.lang.String)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, DiagramType diagramType, String namePattern) {
		return getAllDiagrams(includeDependencies, diagramType, null, namePattern);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(boolean, org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum, java.lang.String)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, String diagramType, IPackageArtifact packij, String namePattern) {
		return getAllDiagrams(includeDependencies, diagramType!=null?Enum.valueOf(DiagramType.class, diagramType):null, packij, namePattern);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(boolean, org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum, java.lang.String)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, DiagramType diagramType, IPackageArtifact packij, String namePattern) {
		System.out.println("getAllDiagrams-args: includeDependencies: "+includeDependencies+" diagramType: "+diagramType+" namePattern: "+namePattern);
		List<IContainer> containers = includeDependencies ? getAllRoots() : Collections.singletonList(getRoot());
		Collection<IDiagramDescriptor> diagrams = new LinkedList<IDiagramDescriptor>();
		Set<DiagramType> types = diagramType != null ? Collections.singleton(diagramType) : DIAGRAM_TYPES;
		Pattern pattern = namePattern != null ? Pattern.compile(namePattern) : null;
		for(DiagramType t : types)
		{
			System.out.println("DiagramType: "+t);
			for(IContainer c : containers)
			{
				System.out.println("Container: "+c);
				List<IResource> diags = TigerstripeProjectAuditor.findAll(c, t.extension());
				for(IResource d : diags)
				{
					IPath diagRelativePath = d.getProjectRelativePath();
					String diagName = diagRelativePath.removeFileExtension().lastSegment();
					System.out.println("DiagramName: "+diagName+" ("+diagRelativePath.toOSString()+")");
					// Should we skip diagrams in bin?
					try {
						IPath packageArtifactPath = packij!=null?new Path(packij.getArtifactPath()).removeLastSegments(1):null;
						System.out.println("ArtifactPath: "+packageArtifactPath+" ("+diagRelativePath.toOSString()+")");
//						if(packageArtifactPath == null || packageArtifactPath.isPrefixOf(diagRelativePath))
						if(packageArtifactPath == null || packageArtifactPath.equals(diagRelativePath.removeLastSegments(1)))
						{
							if(pattern == null || pattern.matcher(diagName).matches())
							{
								System.out.println("Matched: "+diagName);
								diagrams.add(new DiagramDescriptor(diagName, d.getProject().getName(), diagRelativePath.toOSString(), t));
							}
						}
					} catch (TigerstripeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return diagrams;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(boolean, org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, DiagramType diagramType) {
		return getAllDiagrams(includeDependencies, diagramType, null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(boolean, org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, String diagramType) {
		return getAllDiagrams(includeDependencies, diagramType, null);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(boolean)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies) {
		return getAllDiagrams(includeDependencies, (String)null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams()
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams() {
		return getAllDiagrams(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(DiagramType diagramType) {
		return getAllDiagrams(false, diagramType);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum, java.lang.String)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(DiagramType diagramType, String namePattern) {
		return getAllDiagrams(false, diagramType, namePattern);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(boolean, org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum, java.lang.String)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(DiagramType diagramType, IPackageArtifact packij, String namePattern) {
		return getAllDiagrams(false, diagramType, packij, namePattern);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(String diagramType) {
		return getAllDiagrams(false, diagramType);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum, java.lang.String)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(String diagramType, String namePattern) {
		return getAllDiagrams(false, diagramType, namePattern);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#getAllDiagrams(boolean, org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator.DiagramTypeEnum, java.lang.String)
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(String diagramType, IPackageArtifact packij, String namePattern) {
		return getAllDiagrams(false, diagramType, packij, namePattern);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.plugins.IDiagramGenerator#isDiagramGenerationAvailable()
	 */
	public boolean isDiagramGenerationAvailable() {
		// TODO Need to figure out what the correct test is here - Eric suggests the next line
		Display.getDefault();
		return true;
	}

	protected IContainer getRoot() {
		return getRoot(handle);
	}
	
	protected static IContainer getRoot(ITigerstripeModelProject handle) {
		try {
			String pName = handle.getProjectDetails().getName();
			IContainer root = (IContainer) ResourcesPlugin.getWorkspace().getRoot().findMember(pName);
			return root;
		} catch (TigerstripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected static List<IContainer> getAllRoots(ITigerstripeModelProject handle) {
		List<IContainer> containers = new LinkedList<IContainer>();
		containers.add(getRoot(handle));
		try {
			for(ITigerstripeModelProject h : handle.getReferencedProjects())
			{
				containers.addAll(getAllRoots(h));
			}
		} catch (TigerstripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return containers;
	}
	
	protected List<IContainer> getAllRoots() {
		return getAllRoots(handle);
	}
}
