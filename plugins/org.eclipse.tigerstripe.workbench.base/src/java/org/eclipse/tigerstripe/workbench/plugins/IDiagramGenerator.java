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
package org.eclipse.tigerstripe.workbench.plugins;

import java.util.Collection;
import java.util.EnumSet;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;

import static org.eclipse.tigerstripe.workbench.plugins.IDiagramDescriptor.DiagramType;

/**
 * This defines the interface presented to a <em>Tigerstripe Generator</em> for the purposes of
 * manipulating diagrams. The interface provides methods for:
 * <ul>
 * <li>discovering whether diagram generation is enabled</li>
 * <li>discovering what diagrams are available</li>
 * <li>rendering diagrams into <code>GIF</code> or <code>JPEG</code> files in locations specified
 * relative to the generation output directory</li>
 * </ul>
 * 
 * @author jworrell
 *
 */
public interface IDiagramGenerator {

	/**
	 * This enum classifies the types of image file that may be created. The types are:
	 * <ul>
	 * <li>GIF</li>
	 * <li>JPEG</li>
	 * </ul>
	 * 
	 * @author jworrell
	 *
	 */
	public enum ImageType {
		GIF("gif"),
		JPEG("jpeg");
		
		private String extension;
		
		ImageType(String extension) {
			this.extension = extension;
		}
		
		public String extension() {return extension;}
	}

	/**
	 * An <code>EnumSet</code> that collects all available diagram types
	 */
	public static final EnumSet<DiagramType> DIAGRAM_TYPES = EnumSet.allOf(DiagramType.class);
	
	/**
	 * Returns the value <code>true</code> if diagram generation is available, that is, if
	 * <em>not</em> running in head-less mode. By testing the value returned a <em>Generator</em>
	 * can be written to generate suitable alternative text.
	 * @return the value <code>true</code> if diagram generation is available
	 */
	public boolean isDiagramGenerationAvailable();

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation.
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams();

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation
	 * that are of the type identified by the "diagramType".
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType"
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(DiagramType diagramType);

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation
	 * that are of the type identified by the supplied "diagramType" and which match the supplied "namePattern".
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @param namePattern the pattern that when matched against the names of the diagrams selects those that will be
	 * included in the results
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType"
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(DiagramType diagramType, String namePattern);
	
	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation
	 * that are of the type identified by the supplied "diagramType", that are in the supplied package,
	 * and which match the supplied "namePattern".
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @param packij the package in which to search for diagrams
	 * @param namePattern the pattern that when matched against the names of the diagrams selects those that will be
	 * included in the results
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType", are in the supplied package ,
	 * and match the supplied name-pattern
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(DiagramType diagramType, IPackageArtifact packij, String namePattern);

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation
	 * that are of the type identified by the "diagramType".
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType"
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(String diagramType);

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation
	 * that are of the type identified by the supplied "diagramType" and which match the supplied "namePattern".
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @param namePattern the pattern that when matched against the names of the diagrams selects those that will be
	 * included in the results
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType"
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(String diagramType, String namePattern);

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation
	 * that are of the type identified by the supplied "diagramType", that are in the supplied package,
	 * and which match the supplied "namePattern".
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @param packij the package in which to search for diagrams
	 * @param namePattern the pattern that when matched against the names of the diagrams selects those that will be
	 * included in the results
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType", are in the supplied package ,
	 * and match the supplied name-pattern
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(String diagramType, IPackageArtifact packij, String namePattern);

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation
	 * if the supplied <code>boolean</code> is <code>false</code>, or for all referenced projects also
	 * if <code>true</code>.
	 * @param includeDependencies if supplied as <code>true</code> include diagrams from all referenced projects
	 * as well as from the project selected
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies);

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation,
	 * and in the referenced projects if "includeDependencies" is supplied as <code>true</code>,
	 * that are of the type identified by the "diagramType".
	 * @param includeDependencies if supplied as <code>true</code> include diagrams from all referenced projects
	 * as well as from the project selected
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType"
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, DiagramType diagramType);

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation,
	 * and in the referenced projects if "includeDependencies" is supplied as <code>true</code>,
	 * that are of the type identified by the supplied "diagramType" and which match the supplied "namePattern".
	 * @param includeDependencies if supplied as <code>true</code> include diagrams from all referenced projects
	 * as well as from the project selected
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @param namePattern the pattern that when matched against the names of the diagrams selects those that will be
	 * included in the results
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType"
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, DiagramType diagramType, String namePattern);
	
	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation,
	 * and in the referenced projects if "includeDependencies" is supplied as <code>true</code>,
	 * that are of the type identified by the supplied "diagramType", that are in the supplied package,
	 * and which match the supplied "namePattern".
	 * @param includeDependencies if supplied as <code>true</code> include diagrams from all referenced projects
	 * as well as from the project selected
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @param packij the package in which to search for diagrams
	 * @param namePattern the pattern that when matched against the names of the diagrams selects those that will be
	 * included in the results
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType", are in the supplied package ,
	 * and match the supplied name-pattern
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, DiagramType diagramType, IPackageArtifact packij, String namePattern);

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation,
	 * and in the referenced projects if "includeDependencies" is supplied as <code>true</code>,
	 * that are of the type identified by the "diagramType".
	 * @param includeDependencies if supplied as <code>true</code> include diagrams from all referenced projects
	 * as well as from the project selected
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType"
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, String diagramType);

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation,
	 * and in the referenced projects if "includeDependencies" is supplied as <code>true</code>,
	 * that are of the type identified by the supplied "diagramType" and which match the supplied "namePattern".
	 * @param includeDependencies if supplied as <code>true</code> include diagrams from all referenced projects
	 * as well as from the project selected
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @param namePattern the pattern that when matched against the names of the diagrams selects those that will be
	 * included in the results
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType"
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, String diagramType, String namePattern);

	/**
	 * Returns a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects (each of
	 * which contains the data necessary to identify a diagram) in the project selected for generation,
	 * and in the referenced projects if "includeDependencies" is supplied as <code>true</code>,
	 * that are of the type identified by the supplied "diagramType", that are in the supplied package,
	 * and which match the supplied "namePattern".
	 * @param includeDependencies if supplied as <code>true</code> include diagrams from all referenced projects
	 * as well as from the project selected
	 * @param diagramType the type of diagram to be included in the results, i.e. "class" or "instance"
	 * @param packij the package in which to search for diagrams
	 * @param namePattern the pattern that when matched against the names of the diagrams selects those that will be
	 * included in the results
	 * @return a <code>Collection</code> of all <code>IDiagramDescriptor</code> objects in the project
	 * selected for generation that are of the type identified by the "diagramType", are in the supplied package ,
	 * and match the supplied name-pattern
	 */
	public Collection<IDiagramDescriptor> getAllDiagrams(boolean includeDependencies, String diagramType, IPackageArtifact packij, String namePattern);
	
	/**
	 * Render the diagram described by the supplied <code>IDiagramDescriptor</code> to the directory at the
	 * supplied "outputPath" relative to the output path for the generator, of the supplied "imageType".
	 * @param diagram the <code>IDiagramdescriptor</code> that describes the diagram to be rendered
	 * @param outputPath the path, relative to the output path for the generator, into which the image file
	 * should be placed
	 * @param imageType the type of the image file required, i.e. JPEG or GIF
	 * @return the value <code>true</code> if the diagram was rendered successfully
	 */
	public boolean generateDiagram(IDiagramDescriptor diagram, String outputPath, ImageType imageType);

	/**
	 * Render the diagram described by the supplied <code>IDiagramDescriptor</code> to the directory at the
	 * supplied "outputPath" relative to the output path for the generator, of the supplied "imageType".
	 * @param diagram the <code>IDiagramdescriptor</code> that describes the diagram to be rendered
	 * @param outputPath the path, relative to the output path for the generator, into which the image file
	 * should be placed
	 * @param imageType the type of the image file required, i.e. JPEG or GIF
	 * @return the value <code>true</code> if the diagram was rendered successfully
	 */
	public boolean generateDiagram(IDiagramDescriptor path, String outputPath, String imageType);
}
