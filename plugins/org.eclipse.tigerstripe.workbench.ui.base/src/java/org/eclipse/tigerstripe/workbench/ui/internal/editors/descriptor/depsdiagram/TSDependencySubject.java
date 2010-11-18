/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.depsdiagram;

import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyType;
import org.eclipse.tigerstripe.workbench.ui.dependencies.utils.IdObject;

public class TSDependencySubject extends IdObject implements IDependencySubject {

	private final IDependency dep;

	public TSDependencySubject(IDependency dep) {
		this.dep = dep;
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	public String getName() {
		return dep.getPath();
	}

	public Set<IDependencySubject> getDependencies() {
		return Collections.emptySet();
	}

	public SortedMap<String, String> getProperties() {
		TreeMap<String, String> props = new TreeMap<String, String>();
		props.put("version", dep.getIProjectDetails().getVersion());
		return props;

	}

	public String getDescription() {
		return null;
	}

	public IDependencyType getType() {
		return TigerstripeTypes.DEPENDENCY;
	}

	@Override
	protected String internalId() {
		return dep.getPath();
	}

	public Image getIcon() {
		return null;
	}

}
