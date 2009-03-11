/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.refactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.refactor.IRefactorCommand;
import org.eclipse.tigerstripe.workbench.refactor.RefactorRequest;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.IDiagramChangeDelta;

public class BaseRefactorCommand implements IRefactorCommand {

	private RefactorRequest[] requests;
	private List<IModelChangeDelta> deltas = new ArrayList<IModelChangeDelta>();
	private List<IDiagramChangeDelta> diagramDeltas = new ArrayList<IDiagramChangeDelta>();

	public BaseRefactorCommand(RefactorRequest[] requests) {
		this.requests = requests;
	}

	/**
	 * 	Executing a refactor command means applying the list of {@link IModelChangeDelta} that has been
	 * assembled in a locked-up workspace state.
	 * 
	 * There are 4 kinds of changes that may happen
	 * 1.- Artifacts being renamed. This requires a "rename of the POJO".
	 * 2.- Content of artifact being changed, i.e. in artifact that had a reference to changed FQN.
	 * 3.- Diagram content being changed, i.e. a artifact on the diagram had a changed FQN
	 * 4.- Diagram being moved, i.e. the diagram was in a package that was renamed.
	 * 
	 * The changes are applied in the following order:
	 * <ul>
	 *  <li>Prior to anything else, we disable audits and diagram synchronization.</li>
	 *  <li> (2) - we update all contents of artifacts first. This will lead to the creation of "Proxy artifacts"
	 *   that will be resolved when the references are traversed later. </li>
	 *  <li> (1) - we move the artifacts to their new location.</li>
	 *  <li>(3) - we update the content of the diagrams with the new FQNs</li>
	 *  <li>(4) - we move the diagrams to their new location</li>
	 *  <li>We delete the old packages/directories/diagrams as needed.</li>
	 *  <li>We re-index changed diagrams.</li>
	 *  <li>We trigger a manual audit to re-index the POJOs and make sure the smart auditing is in sync after
	 *  all changes.</li>
	 *  <li>We re-enable audits and diagram synchronization</li>
	 * </ul>
	 * 
	 * With a bit of luck, all is good at this point.
	 * @param monitor -
	 */
	public void execute(IProgressMonitor monitor) throws TigerstripeException {
		// Gets the list of all resources that will be impacted. 
		Set<IResource> affectedResources = getAffectedResources();
		
		
		System.out.println(affectedResources);
	}

	public RefactorRequest[] getRequests() {
		return requests;
	}

	public void addDeltas(Collection<IModelChangeDelta> deltas) {
		this.deltas.addAll(deltas);
	}

	public void addDiagramDeltas(Collection<IDiagramChangeDelta> deltas) {
		this.diagramDeltas.addAll(deltas);
	}

	/**
	 * Traverses the list of IModelChangeDeltas to return a list of all affected
	 * resources.
	 * 
	 * @return
	 */
	protected Set<IResource> getAffectedResources() {
		Set<IResource> resources = new HashSet<IResource>();
		for (IModelChangeDelta delta : deltas) {
			IResource res = delta.getAffectedResource();
			if (res != null)
				resources.add(res);
		}
		return resources;
	}
}
