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
package org.eclipse.tigerstripe.workbench.ui.rendererplugin.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramImageGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToImageUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.DiagramIOUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.rendering.IDiagramRenderer;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ResourceUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.AbstractArtifactUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.AttributeUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.ExtendsUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.ImplementsUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.LiteralUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.MethodUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.QueryReturnsUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.SessionEmitsUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.SessionExposesUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.SessionManagesUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.SessionSupportsUpdateCommand;

public class Renderer implements IDiagramRenderer {

	private ImageFileFormat mapFormat(String pictType) {
		if (IDiagramRenderer.JPEG.equalsIgnoreCase(pictType))
			return ImageFileFormat.JPEG;
		else if (IDiagramRenderer.GIF.equalsIgnoreCase(pictType))
			return ImageFileFormat.GIF;
		throw new IllegalArgumentException("Un-supported image type: "
				+ pictType);
	}

	public void renderDiagram(String projectLabel, String diagRelPath,
			final String pictType, String outputProjectLabel, final String imagePath) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProject project = workspace.getRoot().getProject(projectLabel);
		final IProject outputProject = workspace.getRoot().getProject(outputProjectLabel);
		if (project != null) {
			final IPath iPath = new Path(imagePath);
			final IPath diagPath = project.getLocation().append(diagRelPath);
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					try {
						IFolder folder = outputProject.getFolder(iPath
								.removeLastSegments(1));
						ResourceUtils.createFolders(folder, null);
						renderDiagram(diagPath, outputProject.getLocation().append(
								imagePath), new NullProgressMonitor(),
								mapFormat(pictType));
					} catch (CoreException e) {
						EclipsePlugin.log(e);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			});
		}
	}

	public void renderDiagram(String projectLabel, String diagRelPath,
			final String pictType, final String imagePath) {
		renderDiagram(projectLabel, diagRelPath, pictType, projectLabel, imagePath);
	}

	/**
	 * Renders the diagram identified by diagram
	 * 
	 * @param diagramPath -
	 *            Absolute path to diagram to render. Note that the diagram
	 *            needs to be in a proper Tigerstripe Project.
	 * @param imagePath -
	 *            The absolute path to the file to be created
	 * @param monitor
	 * @param format
	 */
	public void renderDiagram(IPath diagramPath, IPath imagePath,
			IProgressMonitor monitor, ImageFileFormat format)
			throws CoreException, TigerstripeException {
		CopyToImageUtil imageUtil = new CopyToImageUtil();
		IFile diagramFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFileForLocation(diagramPath);
		IFile imageFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFileForLocation(imagePath);
		IProject diagramProject = diagramFile.getProject();
		ITigerstripeModelProject tmpTsProject = null;
		if (diagramProject.getAdapter(ITigerstripeModelProject.class) instanceof ITigerstripeModelProject) {
			tmpTsProject = (ITigerstripeModelProject) diagramProject
					.getAdapter(ITigerstripeModelProject.class);
		} else
			throw new TigerstripeException("Diagram not in Tigerstripe project");
		final ITigerstripeModelProject tsProject = tmpTsProject;
		TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain();

		// Make sure we can read the diagram and that we'll be able
		// to create the image
		if (diagramFile.exists() && imageFile.getParent().exists()
				&& tsProject != null) {
			Diagram diagram = DiagramIOUtil.load(domain, diagramFile, true,
					monitor);
			initDiagram(tsProject, diagram);

			// create a transactional command that does an "initial Refresh"
			// (similar to the initial refresh happening in the synchrnonizer)
			final EObject diagramElement = diagram.getElement();
			Command cmd = new AbstractCommand() {
				@Override
				public boolean canExecute() {
					return true;
				}

				public void execute() {
					// if (diagramElement instanceof InstanceMap) {
					// refreshDiagram(tsProject, (InstanceMap) diagramElement);
					// } else if (diagramElement instanceof Map) {
					// refreshDiagram(tsProject, (Map) diagramElement);
					// }
				}

				public void redo() {
					;
				}

				@Override
				public boolean canUndo() {
					return false;
				}
			};
			domain.getCommandStack().execute(cmd);
			// domain.getCommandStack().flush();

			// Render it now
			Shell shell = new Shell();
			OffscreenEditPartFactory factory = OffscreenEditPartFactory.getInstance();
			DiagramEditPart diagramEP = factory.createDiagramEditPart(diagram, shell);
			imageUtil.copyToImage(diagramEP, imagePath, format, monitor);
			shell.dispose();
		}
	}

	/**
	 * Initialize the extra bits in the Diagram so the content can be resolved
	 * properly with regards to the TS domain.
	 * 
	 * NOTE: this initialization is usually do by the Synchronizer when diagram
	 * is opened in Editor.
	 * 
	 * @param diagram
	 */
	private void initDiagram(ITigerstripeModelProject tsProject, Diagram diagram) {
		// Need to initialize the Map with the TS Project so diagram
		// content can be resolved
		EObject eObj = diagram.getElement();
		if (eObj instanceof Map)
			((Map) eObj).setCorrespondingITigerstripeProject(tsProject);
		else if (eObj instanceof InstanceMap)
			((InstanceMap) eObj).setCorrespondingITigerstripeProject(tsProject);
	}

	private List<AbstractArtifactUpdateCommand> getUpdateCommands(
			AbstractArtifact eArtifact, IAbstractArtifact iArtifact) {
		List<AbstractArtifactUpdateCommand> cmdList = new ArrayList<AbstractArtifactUpdateCommand>();
		if (eArtifact instanceof Enumeration) {
			cmdList.add(new ExtendsUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new LiteralUpdateCommand(eArtifact, iArtifact));
		} else if (eArtifact instanceof NamedQueryArtifact) {
			cmdList.add(new QueryReturnsUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new ExtendsUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new AttributeUpdateCommand(eArtifact, iArtifact));
		} else if (eArtifact instanceof SessionFacadeArtifact) {
			cmdList.add(new SessionEmitsUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new SessionExposesUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new SessionManagesUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new SessionSupportsUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new ExtendsUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new MethodUpdateCommand(eArtifact, iArtifact));
		} else {
			cmdList.add(new ExtendsUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new ImplementsUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new AttributeUpdateCommand(eArtifact, iArtifact));
			cmdList.add(new MethodUpdateCommand(eArtifact, iArtifact));
		}
		return cmdList;
	}

}
