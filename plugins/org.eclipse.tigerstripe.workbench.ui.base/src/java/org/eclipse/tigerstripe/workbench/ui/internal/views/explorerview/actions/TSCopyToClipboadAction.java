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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.MEMBER_CONTAINER_NAME;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.allMemebers;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.copyMemebers;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.refactoring.reorg.CopyToClipboardAction;
import org.eclipse.jdt.internal.ui.refactoring.reorg.ReorgMessages;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ResourceTransfer;

public class TSCopyToClipboadAction extends CopyToClipboardAction {

	private final Clipboard theClipboard;
	private TSPasteAction fPasteAction;// may be null

	public TSCopyToClipboadAction(IWorkbenchSite site, Clipboard clipboard,
			TSPasteAction pasteAction) {
		super(site, clipboard);
		setText(ReorgMessages.CopyToClipboardAction_text);
		setDescription(ReorgMessages.CopyToClipboardAction_description);
		Assert.isNotNull(clipboard);
		theClipboard = clipboard;
		ISharedImages workbenchImages = getWorkbenchSharedImages();
		setDisabledImageDescriptor(workbenchImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		setImageDescriptor(workbenchImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setHoverImageDescriptor(workbenchImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		update(getSelection());

		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(this, IJavaHelpContextIds.COPY_ACTION);
		fPasteAction = pasteAction;
	}

	public void setAutoRepeatOnFailure(boolean autorepeatOnFailure) {
	}

	private static ISharedImages getWorkbenchSharedImages() {
		return JavaPlugin.getDefault().getWorkbench().getSharedImages();
	}

	/**
	 * Currently if selection includes AbstractLogicalExplorerNode the whole
	 * selection is required to be only AbstractLogicalExplorerNode
	 */
	@Override
	public void selectionChanged(IStructuredSelection selection) {
		super.selectionChanged(selection);

		Object[] objects = selection.toArray();
		if (allMemebers(objects)) {
			setEnabled(true);
			return;
		}

		int numberOfAbstractLogicalExplorerNodes = 0;
		for (Object obj : objects) {
			if (obj instanceof AbstractLogicalExplorerNode) {
				numberOfAbstractLogicalExplorerNodes++;
			}
		}
		if (numberOfAbstractLogicalExplorerNodes == 0)
			; // no Abstract logical node, delegate
		else if (numberOfAbstractLogicalExplorerNodes == objects.length) {
			setEnabled(true); // only abstractlogical nodes selected
		} else
			setEnabled(false); // mixture of abstractlogical nodes and other
		// stuff.
	}

	@Override
	public void run(IStructuredSelection selection) {
		Object[] objects = selection.toArray();

		if (copyMemebers(objects, theClipboard)) {
			if (fPasteAction != null) {
				fPasteAction.setMembersToCut(null);
			}
			return;
		}

		int numberOfAbstractLogicalExplorerNodes = 0;
		for (Object obj : objects) {
			if (obj instanceof AbstractLogicalExplorerNode) {
				numberOfAbstractLogicalExplorerNodes++;
			}
		}
		if (numberOfAbstractLogicalExplorerNodes == 0)
			super.run(selection); // no Abstract logical node, delegate
		else if (numberOfAbstractLogicalExplorerNodes != objects.length) {
			// mixture of abstractlogical nodes and other stuff. Do not run.
		} else {
			// do it
			internalRun(selection);
		}
	}

	protected void internalRun(IStructuredSelection selection) {
		AbstractLogicalExplorerNode[] nodes = (AbstractLogicalExplorerNode[]) selection
				.toList().toArray(
						new AbstractLogicalExplorerNode[selection.toList()
								.size()]);
		new TSClipboardCopier(nodes, theClipboard).copyToClipboard();

		// update the enablement of the paste action
		// workaround since the clipboard does not support callbacks
		if (fPasteAction != null && fPasteAction.getSelection() != null)
			fPasteAction.update(fPasteAction.getSelection());
	}

	private static class TSClipboardCopier {

		private final Clipboard theClipboard;

		AbstractLogicalExplorerNode[] nodes = null;

		private TSClipboardCopier(AbstractLogicalExplorerNode[] nodes,
				Clipboard theClipboard) {
			Assert.isNotNull(nodes);
			this.nodes = nodes;
			this.theClipboard = theClipboard;
		}

		public void copyToClipboard() {
			Set<String> fileNames = new HashSet<String>(nodes.length);
			Set<IResource> resources = new HashSet<IResource>(nodes.length);
			StringBuffer namesBuf = new StringBuffer();
			processNodes(fileNames, resources, namesBuf);

			IResource[] resourcesForClipboard = resources
					.toArray(new IResource[resources.size()]);
			String[] fileNameArray = fileNames.toArray(new String[fileNames
					.size()]);
			copyToClipboard(resourcesForClipboard, fileNameArray,
					namesBuf.toString(), 0);
		}

		private void processNodes(Set<String> fileNames,
				Set<IResource> resources, StringBuffer namesBuf) {
			for (int i = 0; i < nodes.length; i++) {
				AbstractLogicalExplorerNode node = nodes[i];
				for (IResource resource : node.getUnderlyingResources()) {
					addFileName(fileNames, resource);
					resources.add(resource);
				}

				if (i > 0)
					namesBuf.append('\n');
				namesBuf.append(node.getText());
			}
		}

		private static void addFileName(Set<String> fileName, IResource resource) {
			if (resource == null)
				return;
			IPath location = resource.getLocation();
			if (location != null) {
				fileName.add(location.toOSString());
			} else {
				// not a file system path. skip file.
			}
		}

		private void copyToClipboard(IResource[] resources, String[] fileNames,
				String names, int repeat) {
			final int repeat_max_count = 10;
			try {
				theClipboard.setContents(
						createDataArray(resources, fileNames, names),
						createDataTypeArray(resources, fileNames));
			} catch (SWTError e) {
				if (e.code != DND.ERROR_CANNOT_SET_CLIPBOARD
						|| repeat >= repeat_max_count)
					throw e;
				// if (fAutoRepeatOnFailure) {
				// try {
				// Thread.sleep(500);
				// } catch (InterruptedException e1) {
				// // do nothing.
				// }
				// }
				// if (fAutoRepeatOnFailure
				// || MessageDialog.openQuestion(fShell,
				// ReorgMessages.CopyToClipboardAction_4,
				// ReorgMessages.CopyToClipboardAction_5))
				// copyToClipboard(resources, fileNames, names, javaElements,
				// typedSources, repeat + 1);
			}
		}

		private static Transfer[] createDataTypeArray(IResource[] resources,
				String[] fileNames) {
			List result = new ArrayList(2);
			if (resources.length != 0)
				result.add(ResourceTransfer.getInstance());
			if (fileNames.length != 0)
				result.add(FileTransfer.getInstance());
			result.add(TextTransfer.getInstance());
			return (Transfer[]) result.toArray(new Transfer[result.size()]);
		}

		private static Object[] createDataArray(IResource[] resources,
				String[] fileNames, String names) {
			List result = new ArrayList(4);
			if (resources.length != 0)
				result.add(resources);
			if (fileNames.length != 0)
				result.add(fileNames);
			result.add(names);
			return result.toArray();
		}

	};
}
