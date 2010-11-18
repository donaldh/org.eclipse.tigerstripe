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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyType;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IExternalContext;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.editor.DependencyDiagramEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

public abstract class AbstractTigerstripeDependenciesDiagram extends
		DependencyDiagramEditor {

	private static final String VISUAL_STATE_FILE = ".visualstate";
	private IExternalContext context;
	protected TigerstripeSubjectsFactory factory;

	private static LinkedHashSet<IDependencyType> types;

	static {
		types = new LinkedHashSet<IDependencyType>();
		types.addAll(Arrays.asList(TigerstripeTypes.values()));
	};

	@Override
	protected void setInput(final IEditorInput input) {

		if (factory != null) {
			factory.dispose();
		}
		factory = new TigerstripeSubjectsFactory(getHandler());
		factory.setCurrentProjectProvider(new ITigerstripeProjectProvider() {

			public ITigerstripeModelProject getProject() {
				return getTSProject(input);
			}
		});

		context = new IExternalContext() {

			public IDependencySubject getRootExternalModel() {
				return getRootModel(input);
			}

			public String getName() {
				return "Tigerstripe Dependency Diagram";
			}

			public void save(EObject data) {
				Resource resource = getResource();
				EList<EObject> contents = resource.getContents();
				contents.clear();
				contents.add(data);
				try {
					resource.save(Collections.emptyMap());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

			}

			public <T extends EObject> T load(Class<T> forClass) {
				Resource resource = getResource();
				try {
					resource.load(Collections.emptyMap());
				} catch (IOException e) {
					return null;
				}
				for (EObject eo : resource.getContents()) {
					if (forClass.isInstance(eo)) {
						return forClass.cast(eo);
					}
				}
				return null;
			}

			public Set<IDependencyType> getAllTypes() {
				return types;
			}

		};

		super.setInput(input);
	}

	private URI getFileInputURI() {
		IFileEditorInput fileInput = (IFileEditorInput) getEditorInput();
		return URI.createPlatformResourceURI(fileInput.getFile().getFullPath()
				.removeLastSegments(1).append(VISUAL_STATE_FILE).toString(),
				true);
	}

	private Resource getResource() {
		return new XMIResourceImpl(getFileInputURI());
	}

	@Override
	public IExternalContext getExternalContext() {
		return context;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (factory != null) {
			factory.dispose();
		}
	}

	protected abstract IDependencySubject getRootModel(IEditorInput input);

	protected abstract ITigerstripeModelProject getTSProject(IEditorInput input);
}
