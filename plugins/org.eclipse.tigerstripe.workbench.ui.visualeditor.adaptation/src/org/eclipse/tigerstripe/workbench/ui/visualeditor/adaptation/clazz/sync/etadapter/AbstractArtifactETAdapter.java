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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.etadapter;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.ETAdapter;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILabelRemoveRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodRemoveRequest;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public abstract class AbstractArtifactETAdapter extends
		QualifiedNamedElementETAdapter implements ETAdapter {

	private AbstractArtifact artifact;

	public AbstractArtifactETAdapter(AbstractArtifact artifact,
			IModelUpdater modelUpdater, ClassDiagramSynchronizer synchronizer) {
		super(artifact, modelUpdater, synchronizer);
		this.artifact = artifact;

		initialize();
	}

	protected AbstractArtifact getArtifact() {
		return this.artifact;
	}

	protected void initialize() {

		List<Attribute> attributes = artifact.getAttributes();
		for (Attribute attribute : attributes) {
			try {
				AttributeETAdapter adapter = (AttributeETAdapter) ETAdapterFactory
						.makeETAdapterFor(attribute, getModelUpdater(),
								getSynchronizer());
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		List<Method> methods = artifact.getMethods();
		for (Method method : methods) {
			try {
				MethodETAdapter adapter = (MethodETAdapter) ETAdapterFactory
						.makeETAdapterFor(method, getModelUpdater(),
								getSynchronizer());
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		List<Reference> references = artifact.getReferences();
		for (Reference reference : references) {
			try {
				ReferenceETAdapter adapter = (ReferenceETAdapter) ETAdapterFactory
						.makeETAdapterFor(reference, getModelUpdater(),
								getSynchronizer());
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		// register self
		artifact.eAdapters().add(this);
	}

	@Override
	protected void handleRemoveNotification(Notification arg0) {
		if (arg0.getOldValue() instanceof Attribute
				|| arg0.getOldValue() instanceof Reference) {
			// if (arg0.getOldValue() instanceof Attribute) {
			handleRemoveAttribute(arg0);
		} else if (arg0.getOldValue() instanceof Method) {
			handleRemoveMethod(arg0);
		} else if (arg0.getOldValue() instanceof Literal) {
			handleRemoveLiteral(arg0);
		}
	}

	protected void handleRemoveAttribute(Notification arg0) {
		if (arg0.getOldValue() instanceof Attribute) {
			Attribute removedAttribute = (Attribute) arg0.getOldValue();
			removedAttribute.eAdapters().clear();
			try {
				IAttributeRemoveRequest request = (IAttributeRemoveRequest) getModelUpdater()
						.getRequestFactory().makeRequest(
								IModelChangeRequestFactory.ATTRIBUTE_REMOVE);
				request.setAttributeName(removedAttribute.getName());
				request.setArtifactFQN(artifact.getFullyQualifiedName());
				postChangeRequest(request);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		} else if (arg0.getOldValue() instanceof Reference) {
			Reference removedReference = (Reference) arg0.getOldValue();
			removedReference.eAdapters().clear();
			try {
				IAttributeRemoveRequest request = (IAttributeRemoveRequest) getModelUpdater()
						.getRequestFactory().makeRequest(
								IModelChangeRequestFactory.ATTRIBUTE_REMOVE);
				request.setAttributeName(removedReference.getName());
				request.setArtifactFQN(artifact.getFullyQualifiedName());
				postChangeRequest(request);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	protected void handleRemoveMethod(Notification arg0) {
		Method removedMethod = (Method) arg0.getOldValue();
		removedMethod.eAdapters().clear();
		try {
			IMethodRemoveRequest request = (IMethodRemoveRequest) getModelUpdater()
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.METHOD_REMOVE);
			request.setMethodName(removedMethod.getName());
			request.setArtifactFQN(artifact.getFullyQualifiedName());
			postChangeRequest(request);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected void handleRemoveLiteral(Notification arg0) {
		Literal removedLiteral = (Literal) arg0.getOldValue();
		removedLiteral.eAdapters().clear();
		try {
			ILabelRemoveRequest request = (ILabelRemoveRequest) getModelUpdater()
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.LABEL_REMOVE);
			request.setLabelName(removedLiteral.getName());
			request.setArtifactFQN(artifact.getFullyQualifiedName());
			postChangeRequest(request);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

}
