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
package org.eclipse.tigerstripe.workbench.ui.gmf.synchronization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.DiagramIOUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

/**
 * Common basic features for a Closed Diagram Synchronizer
 * 
 * @author eric
 * 
 */
public abstract class ClosedDiagramSynchronizerBase implements
		IClosedDiagramSynchronizer {

	private long modelFileTStamp = -1;

	private long diagramFileTStamp = -1;

	private Diagram diagram;

	private DiagramHandle handle;

	private DiagramEditPart diagramEP;

	private TransactionalEditingDomain editingDomain;

	// We need to lock access to components of the diagram until it has been
	// properly
	// initialized
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private ReadLock readLock = lock.readLock();

	private WriteLock writeLock = lock.writeLock();

	public ClosedDiagramSynchronizerBase() {
	}

	public abstract void artifactRenamed(String fromFQN, String toFQN)
			throws TigerstripeException;

	public abstract void artifactChanged(IAbstractArtifact iArtifact)
			throws TigerstripeException;

	public abstract void artifactRemoved(String targetFQN)
			throws TigerstripeException;

	protected TransactionalEditingDomain getEditingDomain() {
		try {
			readLock.lock();
			return this.editingDomain;
		} finally {
			readLock.unlock();
		}
	}

	protected DiagramEditPart getDiagramEP() {
		try {
			readLock.lock();
			return this.diagramEP;
		} finally {
			readLock.unlock();
		}
	}

	protected IDiagramEditDomain getDiagramEditDomain() {
		try {
			readLock.lock();
			return diagramEP.getDiagramEditDomain();
		} finally {
			readLock.unlock();
		}
	}

	public Diagram getDiagram() throws TigerstripeException {
		try {
			readLock.lock();
			if (diagramEP != null)
				return diagramEP.getDiagramView();
			else
				throw new TigerstripeException("Can't read diagram: "
						+ handle.getDiagramResource().getFullPath());
		} finally {
			readLock.unlock();
		}
	}

	protected DiagramHandle getHandle() {
		try {
			readLock.lock();
			return this.handle;
		} finally {
			readLock.unlock();
		}
	}

	protected void saveDiagram() throws TigerstripeException {

		try {
			readLock.lock();

			EList list = diagramEP.getEditingDomain().getResourceSet()
					.getResources();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Resource res = (Resource) iter.next();
				try {
					res.save(new HashMap());
				} catch (IOException e) {
					throw new TigerstripeException(
							"Error while saving synchronized diagram "
									+ handle.getDiagramResource().getFullPath()
									+ ": " + e.getMessage(), e);
				}
			}
			updateTStamps();
		} finally {
			readLock.lock();
		}
	}

	protected abstract void initDiagramElement(Object diagramElement);

	private void readDiagram() throws TigerstripeException {
		try {
			diagram = DiagramIOUtil.load(editingDomain, (IFile) handle
					.getDiagramResource(), true, new NullProgressMonitor());
			initDiagramElement(diagram.getElement());
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					OffscreenEditPartFactory factory = OffscreenEditPartFactory
							.getInstance();
					diagramEP = factory.createDiagramEditPart(diagram);
				}
			});
			assert diagramEP != null;
			updateTStamps();
		} catch (CoreException e) {
			throw new TigerstripeException("Error occured while loading "
					+ handle.getDiagramResource().getFullPath()
					+ " for synchronization: " + e.getMessage(), e);
		}
	}

	private void updateTStamps() {
		if (getHandle().getModelResource() != null)
			modelFileTStamp = getHandle().getModelResource()
					.getLocalTimeStamp();
		diagramFileTStamp = getHandle().getDiagramResource()
				.getLocalTimeStamp();
	}

	public void setDiagramHandle(DiagramHandle handle)
			throws TigerstripeException {
		try {
			writeLock.lock();

			// Check this is the correct type of diagram
			if (!getSupportedDiagramExtension().equals(
					handle.getDiagramResource().getFileExtension()))
				throw new TigerstripeException(
						"Incompatible Diagram Synchronizer, was expecting .wvd and got "
								+ handle.getDiagramResource()
										.getFileExtension());
			this.handle = handle;

			initialize();
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * 
	 */
	private void initialize() throws TigerstripeException {
		Assert.isNotNull(handle);

		editingDomain = TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain();
		readDiagram();
	}

	public abstract String getSupportedDiagramExtension();

	public boolean isOutofDate() {
		IResource diagramFile = getHandle().getDiagramResource();
		IResource modelFile = getHandle().getModelResource();
		return diagramFile.getLocalTimeStamp() != diagramFileTStamp
				|| (modelFile != null && modelFile.getLocalTimeStamp() != modelFileTStamp);
	}

}
