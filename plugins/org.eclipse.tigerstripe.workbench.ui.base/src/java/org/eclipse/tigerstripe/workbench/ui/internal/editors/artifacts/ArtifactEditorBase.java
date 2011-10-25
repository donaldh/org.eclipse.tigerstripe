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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.EditorUndoManager;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.MessageListDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.StatusUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.viewers.TigerstripeDecoratorManager;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

public abstract class ArtifactEditorBase extends TigerstripeFormEditor
		implements IActiveFacetChangeListener, ITigerstripeChangeListener,
		IArtifactChangeListener {

	private final AbstractArtifactLabelProvider prov = new AbstractArtifactLabelProvider();

	private boolean ignoreResourceChange = false;

	private IAbstractArtifact artifact;

	private boolean previousPageWasModel = true;

	private int sourcePageIndex = -1;

	private ArtifactSourcePage sourcePage;

	private final Collection<TigerstripeFormPage> modelPages = new ArrayList<TigerstripeFormPage>();

	private IArtifactManagerSession session;

	private State savedState;

	public ArtifactEditorBase() {
		// Performance improvements - only register for the right sort of
		// changes.
		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(
				this, ITigerstripeChangeListener.ANNOTATION);

	}

	public IAbstractArtifact getIArtifact() {
		return artifact;
	}

	protected void setIArtifact(IAbstractArtifact artifact) {
		this.artifact = artifact;
		savedState = State.createState(artifact);
	}

	// @Override
	// public void resourceChanged(IResourceChangeEvent event) {
	// if (ignoreResourceChange)
	// return;
	//
	// IResourceDelta selfDelta = lookforSelf(event.getDelta());
	//
	// if (selfDelta != null && selfDelta.getKind() != IResourceDelta.REMOVED) {
	//
	// if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
	//
	// // make sure we're in the UI thread
	// Display.getDefault().asyncExec(new Runnable() {
	//
	// public void run() {
	// try {
	// System.out.println("Editor u[dating in respnose to a resource change event");
	// FileEditorInput fileInput = (FileEditorInput)
	// sourcePage.getEditorInput();
	// IAbstractArtifact artifact = TSExplorerUtils
	// .getArtifactFor(fileInput.getFile());
	//
	// setIArtifact(((AbstractArtifact) artifact)
	// .makeWorkingCopy(null));
	// updateTextEditorFromArtifact();
	//
	// } catch (TigerstripeException e) {
	// EclipsePlugin.log(e);
	// }
	// }
	// });
	// }
	// }
	// super.resourceChanged(event);
	// }

	@Override
	protected void closeMyself() {
		removeListeners();
		super.closeMyself();
	}

	private void removeListeners() {
		if (session != null) {
			session.removeArtifactChangeListener(this);
			session.removeActiveFacetListener(this);
			session = null;
		}
	}

	@Override
	protected void setInput(IEditorInput input) {
		if (input instanceof FileEditorInput) {
			FileEditorInput fileInput = (FileEditorInput) input;

			if (!fileInput.getFile().getProject().isOpen()) {
				closeMyself();
				dispose();
				return;
			}

			IAbstractArtifact artifact = TSExplorerUtils
					.getArtifactFor(fileInput.getFile());

			try {
				if (artifact != null) {
					setIArtifact(((IAbstractArtifactInternal) artifact)
							.makeWorkingCopy(new NullProgressMonitor()));

					if (artifact.getTigerstripeProject() != null) {
						session = artifact.getTigerstripeProject()
								.getArtifactManagerSession();
						session.addArtifactChangeListener(this);
						session.addActiveFacetListener(this);
					}
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		} else if (input instanceof ReadOnlyArtifactEditorInput) {
			ReadOnlyArtifactEditorInput roInput = (ReadOnlyArtifactEditorInput) input;
			setIArtifact(roInput.getArtifact());
			// there won't be any change to listen for since this is readonly
		}

		super.setInput(input);
	}

	private void updateTitle() {

		IAbstractArtifact artifact = getIArtifact();
		if (artifact != null) {
			setPartName(artifact.getName());
			AbstractArtifactLabelProvider prov = new AbstractArtifactLabelProvider();
			setTitleImage(prov.getImage(artifact));
		} else {
			FileEditorInput input = (FileEditorInput) getEditorInput();
			setPartName(input.getFile().getName());
		}
	}

	@Override
	protected void addPages() {
		try {
			addSourcePage();
		} catch (PartInitException e) {
			TigerstripeRuntime.logErrorMessage("PartInitException detected", e);
		}
		updateTitle();
	}

	protected void addSourcePage() throws PartInitException {

		if ((getIArtifact() != null)
				&& (!(getIArtifact().isReadonly() || getIArtifact() instanceof IContextProjectAware))) {
			sourcePage = new ArtifactSourcePage(this, "id", "Source");

			sourcePageIndex = addPage(sourcePage, getEditorInput());
			setPageText(sourcePageIndex, "Source");
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		final State state = this.savedState;

		getUndoManager().editorSaved();

		if (getActivePage() != sourcePageIndex) {
			updateTextEditorFromArtifact();
		} else {
			try {
				updateArtifactFromTextEditor();
			} catch (TigerstripeException ee) {
				Status status = new Status(IStatus.WARNING,
						EclipsePlugin.getPluginId(), 111,
						"Unexpected Exception", ee);
				EclipsePlugin.log(status);
			}
		}
		monitor.beginTask("Saving " + getIArtifact().getFullyQualifiedName(),
				100);
		// check for errors, if errors are found they will be displayed
		IStatus errorList = getIArtifact().validate();
		if (!errorList.isOK()) {

			List<IStatus> statuses = StatusUtils.flat(errorList);

			if (StatusUtils.findMaxSeverity(statuses) == IStatus.ERROR) {
				MessageListDialog dialog = new MessageListDialog(getContainer()
						.getShell(), statuses, "Save Failed: Invalid Artifact");
				dialog.create();
				dialog.disableOKButton();
				dialog.open();
				return;
			}

			// display warning/info list and save when user clicks on "OK"
			// button
			MessageListDialog dialog = new MessageListDialog(getContainer()
					.getShell(), statuses,
					"Warning: non-fatal errors with Artifact");
			int returnCode = dialog.open();
			if (returnCode != Window.OK)
				return;
		}

		monitor.worked(20);

		// We let Eclipse do the save to the file.
		try {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {

				public void run(IProgressMonitor monitor) throws CoreException {
					setIgnoreResourceChange(true); // ignore the resource change
													// notif here
					getEditor(sourcePageIndex).doSave(monitor);
					setIgnoreResourceChange(false);
					state.processChanges(artifact);
				}
			}, new SubProgressMonitor(monitor, 80));
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
		monitor.done();
	}

	@Override
	public void pageChange(int newPageIndex) {
		if (newPageIndex == sourcePageIndex) {
			if (isPageModified)
				updateTextEditorFromArtifact();
			previousPageWasModel = false;
		} else {
			if (!previousPageWasModel && isDirty()) {
				try {
					updateArtifactFromTextEditor();
				} catch (TigerstripeException ee) {
					Status status = new Status(IStatus.WARNING,
							EclipsePlugin.getPluginId(), 111,
							"Unexpected Exception", ee);
					EclipsePlugin.log(status);
				}
			}
			previousPageWasModel = true;
		}
		super.pageChange(newPageIndex);
	}

	private void updateTextEditorFromArtifact() {
		FileEditorInput input = (FileEditorInput) sourcePage.getEditorInput();
		IAbstractArtifact artifact = getIArtifact();
		try {
			sourcePage.getDocumentProvider().getDocument(input)
					.set(artifact.asText());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void updateArtifactFromTextEditor() throws TigerstripeException {
		FileEditorInput input = (FileEditorInput) sourcePage.getEditorInput();
		IAbstractArtifact originalArtifact = getIArtifact();

		ITigerstripeModelProject project = originalArtifact
				.getTigerstripeProject();
		IArtifactManagerSession session = project.getArtifactManagerSession();

		if (sourcePage.getDocumentProvider().getDocument(input) != null) { // Bug
			// 810
			String text = sourcePage.getDocumentProvider().getDocument(input)
					.get();
			StringReader reader = new StringReader(text);
			try {
				IAbstractArtifact newArtifact = session.extractArtifact(reader,
						new NullProgressMonitor());
				setIArtifact(newArtifact);
				refreshModelPages();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	private boolean isPageModified;

	public void pageModified() {
		isPageModified = true;
		if (!super.isDirty())
			firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	protected void handlePropertyChange(int propertyId) {
		if (propertyId == IEditorPart.PROP_DIRTY)
			isPageModified = super.isDirty();
		super.handlePropertyChange(propertyId);
	}

	@Override
	public boolean isDirty() {
		return isPageModified || super.isDirty();
	}

	protected void addModelPage(TigerstripeFormPage page) {
		modelPages.add(page);
	}

	private void refreshModelPages() {
		for (Iterator<TigerstripeFormPage> iter = modelPages.iterator(); iter
				.hasNext();) {
			TigerstripeFormPage page = iter.next();
			page.refresh();
		}
	}

	public void artifactAdded(IAbstractArtifact artifact) {
		// Nothing to do here.
	}

	public void artifactChanged(final IAbstractArtifact artifact,
			IAbstractArtifact oldArtifact) {
		IAbstractArtifact myArtifact = getIArtifact();
		if (myArtifact != null
				&& myArtifact.getFullyQualifiedName().equals(
						artifact.getFullyQualifiedName())) {
			try {
				setIArtifact(((IAbstractArtifactInternal) artifact)
						.makeWorkingCopy(null));
				refreshModelPages();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	@Override
	public void dispose() {
		removeListeners();
		TigerstripeWorkspaceNotifier.INSTANCE
				.removeTigerstripeChangeListener(this);
		super.dispose();
	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		// TODO Auto-generated method stub
		// Shouldn't this close the editor?
	}

	public void managerReloaded() {
		// TODO Auto-generated method stub

	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		// TODO Auto-generated method stub

	}

	public void facetChanged(IFacetReference oldFacet, IFacetReference newFacet) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				AbstractArtifactLabelProvider prov = new AbstractArtifactLabelProvider();
				setTitleImage(prov.getImage(getIArtifact()));
			}
		});
	}

	private void setIgnoreResourceChange(boolean ignoreResourceChange) {
		this.ignoreResourceChange = ignoreResourceChange;
	}

	@Override
	protected EditorUndoManager createUndoManager() {
		return new ArtifactEditorUndoManager(this);
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {

		if (getIArtifact() == null)
			return;

		// make sure we're in the UI thread
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				Image img = prov.getImage(getIArtifact());
				setTitleImage(TigerstripeDecoratorManager.getDefault()
						.decorateImage(img, getIArtifact()));
			}
		});
	}

	public void modelChanged(IModelChangeDelta[] delta) {
		// Not registered for these

	}

	public void projectAdded(IAbstractTigerstripeProject project) {
		// Not registered for these

	}

	public void projectDeleted(String projectName) {
		// Not registered for these

	}

	public void descriptorChanged(IResource changedDescriptor) {
		// NOT USED HERE
	}

	public void artifactResourceChanged(IResource changedArtifactResource) {
		// NOT USED HERE

	}

	public void artifactResourceAdded(IResource addedArtifactResource) {
		// TODO Auto-generated method stub

	}

	public void artifactResourceRemoved(IResource removedArtifactResource) {
		// TODO Auto-generated method stub

	}

	public void activeFacetChanged(ITigerstripeModelProject project) {
		// TODO Auto-generated method stub

	}

	protected ArtifactOverviewPage createOverviewPage() {

		IAdapterManager adapterManager = Platform.getAdapterManager();

		if (artifact != null) {

			IOssjArtifactFormContentProvider contentProvider = (IOssjArtifactFormContentProvider) adapterManager
					.getAdapter(artifact,
							IOssjArtifactFormContentProvider.class);

			IArtifactFormLabelProvider labelProvider = (IArtifactFormLabelProvider) adapterManager
					.getAdapter(artifact, IArtifactFormLabelProvider.class);

			if (contentProvider == null || labelProvider == null) {
				throw new IllegalStateException(String.format(
						"Not found adapters for artifact type '%s'", artifact
								.getClass().getName()));
			}

			return new ArtifactOverviewPage(this, labelProvider,
					contentProvider);
		} else {
			// Navid Mehregani: Inspired by bugzilla 321257. This can happen
			// when we have a very ugly compile error
			// In this case, instruct the user to open the file with Java
			// editor.
			return new ArtifactOverviewPage(this);
		}

	}

	/**
	 * Helper class to pass all model components updates to annotation
	 * framework.
	 */
	public static class State {
		private final Map<IField, String> fields = new IdentityHashMap<IField, String>();
		private final Map<ILiteral, String> literals = new IdentityHashMap<ILiteral, String>();
		private final Map<IMethod, String> methods = new IdentityHashMap<IMethod, String>();
		private final Map<IArgument, String> arguments = new IdentityHashMap<IArgument, String>();

		private State() {
		}

		public static State createState(IAbstractArtifact artifact) {
			State newState = new State();
			if (artifact != null) {
				newState.initState(artifact);
			}
			return newState;
		}

		private void initState(IAbstractArtifact artifact) {
			for (IField field : artifact.getFields()) {
				fields.put(field, field.getName());
			}
			for (ILiteral literal : artifact.getLiterals()) {
				literals.put(literal, literal.getName());
			}
			for (IMethod method : artifact.getMethods()) {
				methods.put(method, method.getMethodId());
				for (IArgument argument : method.getArguments()) {
					arguments.put(argument, argument.getName());
				}
			}
		}

		public void processChanges(IAbstractArtifact artifact) {
			process(artifact, artifact.getFields(), fields);
			process(artifact, artifact.getLiterals(), literals);
			process(artifact, artifact.getMethods(), methods);
			for (IMethod method : artifact.getMethods()) {
				process(artifact, method.getArguments(), arguments);
			}
		}

		private <T> List<T> process(IAbstractArtifact artifact,
				Collection<T> objects, Map<T, String> oldObjectsMap) {
			List<T> removed = new ArrayList<T>();
			Map<T, String> objectsMap = new IdentityHashMap<T, String>();
			for (T object : objects) {
				objectsMap.put(object, getKey(object));
			}

			for (Entry<T, String> oldEntry : oldObjectsMap.entrySet()) {
				String id = objectsMap.get(oldEntry.getKey());
				if (id == null) {
					try {
						if (oldEntry.getKey() instanceof IMember) {
							notifyMemberRemoved(artifact, oldEntry.getValue());
						} else if (oldEntry.getKey() instanceof IArgument) {
							IArgument arg = (IArgument) oldEntry.getKey();
							String methodId = methods.get(arg
									.getContainingMethod());
							notifyArgumentRemoved(artifact, methodId,
									oldEntry.getValue());
						}
					} catch (TigerstripeException e) {
						BasePlugin.log(e);
					}
				} else if (!id.equals(oldEntry.getValue())) {
					try {
						if (oldEntry.getKey() instanceof IMember) {
							notifyMemberURIChanged(artifact,
									oldEntry.getValue(), id);
						}
					} catch (TigerstripeException e) {
						BasePlugin.log(e);
					}
				}
			}
			return removed;
		}

		private void notifyMemberURIChanged(IAbstractArtifact artifact,
				String oldId,
				String id) throws TigerstripeException {
			URI oldUri = TigerstripeURIAdapterFactory.memberToURI(artifact,
					oldId);
			URI uri = TigerstripeURIAdapterFactory.memberToURI(artifact, id);
			AnnotationPlugin.getManager().changed(oldUri, uri, true);
		}

		private void notifyMemberRemoved(IAbstractArtifact artifact,
				String memberId)
				throws TigerstripeException {
			URI uri = TigerstripeURIAdapterFactory.memberToURI(artifact,
					memberId);
			AnnotationPlugin.getManager().deleted(uri, true);
		}

		private void notifyArgumentRemoved(IAbstractArtifact artifact,
				String memberId, String argumentId) throws TigerstripeException {
			URI uri = TigerstripeURIAdapterFactory.argumentToURI(artifact,
					memberId, argumentId);
			AnnotationPlugin.getManager().deleted(uri, true);
		}

		private String getKey(Object object) {
			if (object instanceof IMethod) {
				return ((IMethod) object).getMethodId();
			} else if (object instanceof IArgument) {
				return ((IArgument) object).getName();
			} else if (object instanceof IModelComponent) {
				return ((IModelComponent) object).getName();
			}
			throw new IllegalArgumentException();
		}
	}
}