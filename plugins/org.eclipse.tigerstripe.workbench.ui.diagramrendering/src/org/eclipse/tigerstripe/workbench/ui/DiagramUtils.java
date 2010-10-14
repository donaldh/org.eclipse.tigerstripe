/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.PreferencesHelper;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractGMFDiagramNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.ClassDiagramLogicalNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.InstanceDiagramLogicalNode;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.utils.ArtifactPropertyChangeHandler;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class DiagramUtils {

	public static void convertExitingAll() {

		final Set<AbstractGMFDiagramNode> toConvert = new HashSet<AbstractGMFDiagramNode>();

		try {
			ResourcesPlugin.getWorkspace().getRoot()
					.accept(new IResourceVisitor() {

						public boolean visit(IResource resource)
								throws CoreException {

							String ext = resource.getFileExtension();
							if (ClassDiagramLogicalNode.DIAG_EXT.equals(ext)) {
								ClassDiagramLogicalNode node = new ClassDiagramLogicalNode(
										(IFile) resource);
								node.getUnderlyingResources();
								toConvert.add(node);
							} else if (InstanceDiagramLogicalNode.DIAG_EXT
									.equals(ext)) {
								InstanceDiagramLogicalNode node = new InstanceDiagramLogicalNode(
										(IFile) resource);
								node.getUnderlyingResources();
								toConvert.add(node);
							}

							return true;
						}
					});
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}

		final IEditorReference[] editorReferences = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();

		for (AbstractGMFDiagramNode node : toConvert) {
			final DiagramDocumentEditor editor = getDiagramEditor(node,
					editorReferences);

			final Diagram diagram;
			if (editor != null) {
				diagram = editor.getDiagram();
			} else {
				diagram = loadFromFile(node.getDiagramFile(),
						node.getModelFile());
			}
			Assert.isNotNull(diagram);

			Map<String, String> localData = PreferencesHelper
					.toMap(PreferencesHelper.findStyle(diagram));
			final IPreferenceStore store = DiagramsPreferences
					.chooseStore(localData);

			transactionSafelyRun("Update Diagram", editor, new Runnable() {

				public void run() {

					DiagramUtils.convertExiting(diagram, store);

					if (editor == null) {
						try {
							diagram.getElement().eResource()
									.save(Collections.emptyMap());
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				}
			});
		}
	}

	public static void convertExiting(View view, IPreferenceStore store) {
		convertBase(view, store);
		// To avoid unnecessary synchronization
		boolean lastIgnoreNofigy = BaseETAdapter.ignoreNofigy();
		BaseETAdapter.setIgnoreNotify(true);
		try {
			convertExtendsRelationships(view, store);
		} finally {
			BaseETAdapter.setIgnoreNotify(lastIgnoreNofigy);
		}

	}

	private static void convertBase(View view, IPreferenceStore store) {
		Iterator<?> edgeIt = view.getSourceEdges().iterator();
		while (edgeIt.hasNext()) {
			PreferencesHelper.setRoutingStyle(((Edge) edgeIt.next()), store);
		}
		// We set this style for all views, which contains DrawerStyle.
		PreferencesHelper.setCompartments(view, store);

		Iterator<?> it = view.getChildren().iterator();
		while (it.hasNext()) {
			Object next = it.next();
			if (next instanceof View) {
				convertBase((View) next, store);
			}
		}
	}

	private static void convertExtendsRelationships(View view,
			IPreferenceStore store) {

		EObject element = view.getElement();
		if (element instanceof AbstractArtifact) {

			AbstractArtifact artifact = (AbstractArtifact) element;
			NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
					artifact);

			String oldValue = helper
					.getProperty(NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS);
			String newValue = PreferencesHelper.extendsRelationshipValue(store);

			helper.setProperty(
					NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS,
					newValue);
			ArtifactPropertyChangeHandler handler = new ArtifactPropertyChangeHandler(
					artifact);
			handler.handleArtifactPropertyChange(
					NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS,
					oldValue, newValue);
		}

		Iterator<?> it = view.getChildren().iterator();
		while (it.hasNext()) {
			Object next = it.next();
			if (next instanceof View) {
				convertExtendsRelationships((View) next, store);
			}
		}
	}

	public static Diagram loadFromFile(IFile diagramFile, IFile modelFile) {

		ResourceSet resSet = new ResourceSetImpl();
		Resource modelResource = resSet.createResource(URI.createURI(modelFile
				.getLocationURI().toString()));
		Resource resource = resSet.createResource(URI.createURI(diagramFile
				.getLocationURI().toString()));

		try {
			modelResource.load(Collections.emptyMap());
			for (EObject item : modelResource.getContents()) {
				if (item instanceof org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) {
					((org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) item)
							.setCorrespondingITigerstripeProject((ITigerstripeModelProject) diagramFile
									.getProject().getAdapter(
											ITigerstripeModelProject.class));
					break;
				}
			}
			resource.load(Collections.emptyMap());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (EObject eo : resource.getContents()) {
			if (eo instanceof Diagram) {
				return (Diagram) eo;
			}
		}
		return null;
	}

	public static DiagramDocumentEditor getDiagramEditor(
			AbstractLogicalExplorerNode cd) {
		return getDiagramEditor(cd, PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences());
	}

	public static DiagramDocumentEditor getDiagramEditor(
			AbstractLogicalExplorerNode cd, IEditorReference[] editorReferences) {

		for (IEditorReference ref : editorReferences) {

			IEditorInput editorInput;
			try {
				editorInput = ref.getEditorInput();
			} catch (PartInitException e) {
				throw new RuntimeException(e);
			}

			if (editorInput instanceof IFileEditorInput) {
				if (((IFileEditorInput) editorInput).getFile().getFullPath()
						.equals(cd.getKeyResource().getFullPath())) {
					return (DiagramDocumentEditor) ref.getEditor(true);
				}
			}
		}
		return null;
	}

	public static void transactionSafelyRun(final String opName,
			final DiagramDocumentEditor editor, Runnable runnable) {
		if (editor != null) {
			final Runnable delegat = runnable;
			runnable = new Runnable() {

				public void run() {
					AbstractTransactionalCommand command = new AbstractTransactionalCommand(
							editor.getEditingDomain(), opName, null) {

						@Override
						protected CommandResult doExecuteWithResult(
								IProgressMonitor monitor, IAdaptable info)
								throws ExecutionException {
							delegat.run();
							return CommandResult.newOKCommandResult();
						}
					};

					try {
						OperationHistoryFactory.getOperationHistory().execute(
								command, new NullProgressMonitor(), null);
					} catch (ExecutionException e) {
						throw new RuntimeException(e);
					}
				}
			};
		}
		runnable.run();
	}
}
