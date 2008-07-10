/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.tools;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;

public class ModelChangeReducer {

	public static Collection<IModelChangeDelta> reduceDeltas(
			List<IModelChangeDelta> deltas) {
		List<IModelChangeDelta> reducedDeltas;

		reducedDeltas = lookForNeighboringRenames(deltas);

		reducedDeltas = lookForNonRename(reducedDeltas);
		return reducedDeltas;
	}

	protected static List<IModelChangeDelta> lookForNeighboringRenames(
			List<IModelChangeDelta> deltas) {

		LinkedList<IModelChangeDelta> result = new LinkedList<IModelChangeDelta>();

		IModelChangeDelta previous = null;
		for (ListIterator<IModelChangeDelta> iter = deltas.listIterator(deltas
				.size()); iter.hasPrevious();) {
			IModelChangeDelta delta = iter.previous();
			if (previous == null) {
				previous = delta;
				continue;
			} else {

				if ((previous.getProject() == null || delta.getProject() == null)
						|| (!previous.getProject().equals(delta.getProject()))) {
					result.addFirst(delta);
					previous = delta;
					continue;
				}

				// If previous is rename
				if (previous.getType() == IModelChangeDelta.SET
						&& "name".equals(previous.getFeature())) {

					// if current is correponsding rename, we can reduce to 1
					// rename only
					if (delta.getType() == IModelChangeDelta.SET
							&& "name".equals(delta.getFeature())
							&& delta.getNewValue().equals(
									previous.getAffectedModelComponentURI())) {

						ModelChangeDelta reducedDelta = new ModelChangeDelta(
								IModelChangeDelta.SET);
						reducedDelta.setAffectedModelComponentURI(delta
								.getAffectedModelComponentURI());
						reducedDelta.setFeature(delta.getFeature());
						reducedDelta.setOldValue(delta.getOldValue());
						reducedDelta.setNewValue(previous.getNewValue());
						reducedDelta.setProject(delta.getProject());
						previous = reducedDelta;
						continue;
					}
					// else nothing to reduce (as we're not looking further than
					// 1 step)
					else {
						result.addFirst(previous);
						previous = delta;
					}
				} else {
					result.addFirst(delta);
					previous = delta;
				}
			}
		}

		if (previous != null)
			result.addFirst(previous);

		return result;
	}

	protected static List<IModelChangeDelta> lookForNonRename(
			List<IModelChangeDelta> deltas) {
		for (Iterator<IModelChangeDelta> iter = deltas.iterator(); iter
				.hasNext();) {
			IModelChangeDelta delta = iter.next();
			if (delta.getType() == IModelChangeDelta.SET
					&& delta.getNewValue().equals(delta.getOldValue())) {
				iter.remove();
			}
		}

		return deltas;
	}
}
