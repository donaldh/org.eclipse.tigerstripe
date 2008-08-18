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

/**
 * @author jworrell
 *
 */
public interface IDiagramDescriptor {
	
	/**
	 * This enum classifies the types of diagram that may be encountered. The types are:
	 * <ul>
	 * <li>CLASS - class diagram</li>
	 * <li>INSTANCE - instance diagram</li>
	 * </ul>
	 * 
	 * @author jworrell
	 *
	 */
	public enum DiagramType {
		CLASS("wvd"),
		INSTANCE("wod");
		
		private String extension;
		
		DiagramType(String extension) {
			this.extension = extension;
		}
		
		public String extension() {return extension;}
	}
	
	/**
	 * Returns the name of the diagram. This is the same as the file name with the
	 * file-extension removed.
	 * @return the name of the diagram
	 */
	public String getName();
	
	/**
	 * Returns the label for the project to which the diagram belongs.
	 * @return the label for the project to which the diagram belongs
	 */
	public String getProjectLabel();
	
	/**
	 * Returns the path to the diagram relative to the project route.
	 * @return the path to the diagram relative to the project route
	 */
	public String getRelativePath();
	
	/**
	 * Returns the type of the diagram as a <code>DiagramType</code>,
	 * that is, whether "class" or "instance" diagram.
	 * @return the type of the diagram as a <code>DiagramType</code>
	 */
	public DiagramType getDiagramType();
}
