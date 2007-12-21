/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.core.generation.rendering;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.rendering.IDiagramRenderer;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;

/**
 * This class is instantiated in the velocity context for use case processing
 * and will capture the list of diagrams to be renderered before actually
 * triggering the rendering
 * 
 * As of v2.2.0 The actual rendering of diagrams will only occur if the
 * TigerstripeRuntime.getRuntype() == ECLIPSE_RUN
 * 
 * Other cases aren't currently supported.
 * 
 * @author eric
 * 
 */
public class DiagramRenderer {

	/*
	 * we need to keep track of how deep we are in the project so, refs to
	 * diagrams are correct.
	 */
	private int fileDepth;

	private enum PictType {
		JPEG
	};

	private class DiagKey {
		String projectLabel = "";

		String diagRelPath;

		public DiagKey(String projectLabel, String diagRelPath) {
			this.projectLabel = projectLabel;
			this.diagRelPath = diagRelPath;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof DiagKey) {
				DiagKey other = (DiagKey) obj;
				return projectLabel.equals(other.projectLabel)
						&& diagRelPath.equals(other.diagRelPath);
			}
			return false;
		}
	}

	private class DiagEntry {
		String projectLabel = "";

		String diagRelPath;

		PictType pictureType;

		int depth = 0;

		public String getLabel() {
			return getLabel(true);
		}

		/**
		 * Returns the label corresponding to the path to the rendered path
		 * 
		 * @return
		 */
		public String getLabel(boolean includePrefix) {

			String prefix = "";
			if (includePrefix) {
				for (int i = 0; i < depth; i++) {
					prefix += ".." + File.separator;
				}
			}
			String result = prefix + "diagrams" + File.separator;
			result += projectLabel + "_";
			result += diagRelPath.replace(File.separatorChar, '_');
			result += ".";

			if (pictureType == PictType.JPEG) {
				result += "jpg";
			}
			return result;
		}

		public DiagEntry(String projectLabel, String diagRelPath,
				PictType pictureType, int depth) {
			this.projectLabel = projectLabel;
			this.diagRelPath = diagRelPath;
			this.pictureType = pictureType;
			this.depth = depth;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof DiagEntry) {
				DiagEntry other = (DiagEntry) obj;
				return projectLabel.equals(other.projectLabel)
						&& diagRelPath.equals(other.diagRelPath)
						&& pictureType.equals(other.pictureType)
						&& depth == other.depth;
			}
			return false;
		}

		public DiagKey getKey() {
			return new DiagKey(projectLabel, diagRelPath);
		}
	}

	private ITigerstripeProject project;

	private Map<DiagKey, DiagEntry> diagMap = new HashMap<DiagKey, DiagEntry>();

	public DiagramRenderer(ITigerstripeProject project) {
		this.project = project;
	}

	public void setFileDepth(int fileDepth) {
		this.fileDepth = fileDepth;
	}

	private String renderAs(PictType pictureType, String projectLabel,
			String diagRelPath) {
		DiagEntry entry = new DiagEntry(projectLabel, diagRelPath, pictureType,
				fileDepth);
		registerEntry(entry);

		return entry.getLabel();
	}

	public String asJPEG(String diagRelPath) {
		return renderAs(PictType.JPEG, "", diagRelPath);
	}

	public String asJPEG(String projectLabel, String diagRelPath) {
		return renderAs(PictType.JPEG, projectLabel, diagRelPath);
	}

	private void registerEntry(DiagEntry entry) {
		diagMap.put(entry.getKey(), entry);
	}

	public void renderDiagrams() throws TigerstripeException {
		if (TigerstripeRuntime.getRuntype() != TigerstripeRuntime.ECLIPSE_GUI_RUN)
			throw new TigerstripeException(
					"Rendering disabled when running outside of Eclipse.");
		else {

			String output = project.getProjectDetails().getOutputDirectory();

			IDiagramRenderer renderer = API
					.getIDiagramRenderingSession()
					.getRendererByName(
							"org.eclipse.tigerstripe.workbench.rendererplugin.actions.Renderer");
			for (DiagEntry entry : diagMap.values()) {
				TigerstripeRuntime.logInfoMessage("Rendering "
						+ entry.getLabel());
				if (entry.projectLabel.length() == 0) {
					renderer.renderDiagram(project.getProjectLabel(),
							entry.diagRelPath, "JPEG", output + File.separator
									+ entry.getLabel(false));
				} else {
					renderer.renderDiagram(entry.projectLabel,
							entry.diagRelPath, "JPEG", output + File.separator
									+ entry.getLabel(false));
				}
			}
		}
	}
}
