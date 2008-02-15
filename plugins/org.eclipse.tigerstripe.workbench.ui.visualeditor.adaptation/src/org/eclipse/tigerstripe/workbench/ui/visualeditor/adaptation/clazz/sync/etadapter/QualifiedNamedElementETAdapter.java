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

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.ETAdapter;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ElementTypeMapper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public abstract class QualifiedNamedElementETAdapter extends BaseETAdapter
		implements ETAdapter {

	private QualifiedNamedElement element;

	public QualifiedNamedElementETAdapter(QualifiedNamedElement element,
			IModelUpdater modelUpdater, ClassDiagramSynchronizer synchronizer) {
		super(modelUpdater, synchronizer);
		this.element = element;
	}

	@Override
	public void notifyChanged(Notification arg0) {

		boolean isInUndoCommand = isUndoCommand();

		if (isInUndoCommand) {
			BaseETAdapter.setIgnoreNotify(true);
		}
		try {
			super.notifyChanged(arg0);
			switch (arg0.getEventType()) {
			case Notification.SET:
				handleSetNotification(arg0);
				break;
			case Notification.ADD:
				handleAddNotification(arg0);
				break;
			case Notification.REMOVE:
				handleRemoveNotification(arg0);
				break;
			}
		} finally {
			if (isInUndoCommand) {
				BaseETAdapter.setIgnoreNotify(false);
			}
		}

	}

	private boolean isUndoCommand() {
		Exception e = new Exception();
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (StackTraceElement stackTraceElement : stackTrace) {
			String className = stackTraceElement.getClassName();
			int lastDotPos = className.lastIndexOf(".");
			if (lastDotPos >= 0
					&& lastDotPos < className.length() - 2
					&& (className.substring(lastDotPos + 1).startsWith(
							"CompositeEMFOperation") || className.substring(
							lastDotPos + 1).startsWith("AbstractEMFOperation"))
					&& stackTraceElement.getMethodName().equals("doUndo"))
				return true;
		}
		return false;
	}

	protected void handleSetNotification(Notification arg0) {

		if (arg0.getFeature() instanceof EAttribute) {
			EAttribute eAttribute = (EAttribute) arg0.getFeature();
			if (eAttribute.getName().equals("name")) {
				handleSetName(arg0.getOldStringValue(), arg0
						.getNewStringValue(), (QualifiedNamedElement) arg0
						.getNotifier());

			} else if (eAttribute.getName().equals("package")) {
				handleSetPackage(arg0.getOldStringValue(), arg0
						.getNewStringValue(), (QualifiedNamedElement) arg0
						.getNotifier());

			} else if (eAttribute.getName().equals("isAbstract")) {
				handleSetAbstract(arg0.getOldBooleanValue(), arg0
						.getNewBooleanValue(), (QualifiedNamedElement) arg0
						.getNotifier());
			}
		} else if (arg0.getFeature() instanceof EReference) {
			EReference eRef = (EReference) arg0.getFeature();
			if (eRef.getName().equals("extends")) {
				handleSetExtends((QualifiedNamedElement) arg0.getOldValue(),
						(QualifiedNamedElement) arg0.getNewValue(),
						(QualifiedNamedElement) arg0.getNotifier());
			}
		}
	}

	protected void handleSetAbstract(boolean oldValue, boolean newValue,
			QualifiedNamedElement element) {
		IArtifactSetFeatureRequest request = null;
		try {
			request = (IArtifactSetFeatureRequest) getModelUpdater()
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
			request.setFeatureId(IArtifactSetFeatureRequest.ISABSTRACT_FEATURE);
			request.setArtifactFQN(element.getFullyQualifiedName());
			request.setFeatureValue(Boolean.toString(newValue));
			postChangeRequest(request);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected void handleSetExtends(QualifiedNamedElement oldValue,
			QualifiedNamedElement newValue, QualifiedNamedElement element) {
		IArtifactSetFeatureRequest request = null;
		try {
			request = (IArtifactSetFeatureRequest) getModelUpdater()
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ARTIFACT_SET_FEATURE);
			request.setFeatureId(IArtifactSetFeatureRequest.EXTENDS_FEATURE);
			request.setArtifactFQN(element.getFullyQualifiedName());

			if (newValue == null) {
				// For now removal on the diagram shouldn't trigger removal
				// in the model.
				// request.setFeatureValue(null);
			} else {
				request.setFeatureValue(newValue.getFullyQualifiedName());
				postChangeRequest(request);
			}
		} catch (TigerstripeException e) {
			// Ignore
		}
	}

	/**
	 * Makes an IArtifactCreateRequest for the appropriate artifact type
	 * 
	 * @return
	 */
	protected IArtifactCreateRequest makeArtifactCreateRequest(
			QualifiedNamedElement element) {
		IArtifactCreateRequest request = null;
		try {
			request = (IArtifactCreateRequest) getModelUpdater()
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ARTIFACT_CREATE);
			request.setArtifactType(ElementTypeMapper.mapToIArtifactType(
					element).getName());
			request.setArtifactName(element.getName());
			request.setArtifactPackage(element.getPackage());
		} catch (TigerstripeException e) {
			// Ignore
		}
		return request;
	}

	protected void handleSetName(String oldValue, String newValue,
			QualifiedNamedElement element) {
		if (oldValue != null && element.getPackage() != null
				&& newValue != null && newValue.length() != 0) {
			// This is a rename of the artifact within its package
			try {
				IArtifactRenameRequest request = (IArtifactRenameRequest) getModelUpdater()
						.getRequestFactory().makeRequest(
								IModelChangeRequestFactory.ARTIFACT_RENAME);

				String packageName = element.getPackage();
				String targetArt = oldValue;
				if (packageName != null && packageName.length() != 0) {
					targetArt = packageName + "." + oldValue;
				}

				request.setArtifactFQN(targetArt);
				request.setNewName(newValue);

				// Before we can rename a qualifiedNamedElement we need to make
				// sure
				// no editor is open on it
				IArtifactManagerSession session = getModelUpdater()
						.getSession();
				IAbstractArtifact artifact = session
						.getArtifactByFullyQualifiedName(targetArt);
				if (artifact != null) {
					IResource res = TSExplorerUtils
							.getIResourceForArtifact(artifact);
					EclipsePlugin.closeEditorForResource(res);
				}

				postChangeRequest(request);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		// Note: if the element.getPackage() == null it means we are
		// in the process of creating a new element on the diagram
		// / and the default name is set first.
	}

	protected void handleSetPackage(String oldValue, String newValue,
			QualifiedNamedElement element) {
		if (oldValue == null) {
			// this is the last part of a new element being created
			IArtifactCreateRequest request = makeArtifactCreateRequest(element);
			try {
				postChangeRequest(request);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		} else {
			// this is a refactor, where the package of the artifact is changed.
		}
	}

	protected void handleAddNotification(Notification arg0) {
		// on an add we don't trigger any request if the content has
		// null attributes, just making sure the added feature gets
		// an adapter right away
		tryCreate(arg0);

		if (arg0.getFeature() instanceof EReference) {
			EReference eRef = (EReference) arg0.getFeature();
			if (eRef.getName().equals("implements")) {
				handleAddImplements((QualifiedNamedElement) arg0.getNewValue(),
						(QualifiedNamedElement) arg0.getNotifier());
			}
		}

		try {
			if (arg0.getNewValue() instanceof EObject) {
				ETAdapter adapter = ETAdapterFactory.makeETAdapterFor(
						(EObject) arg0.getNewValue(), getModelUpdater(),
						getSynchronizer());
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void handleAddImplements(QualifiedNamedElement newValue,
			QualifiedNamedElement notifier) {
		IArtifactAddFeatureRequest request = null;
		try {
			request = (IArtifactAddFeatureRequest) getModelUpdater()
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ARTIFACT_ADD_FEATURE);
			request.setFeatureId(IArtifactAddFeatureRequest.IMPLEMENTS_FEATURE);
			request.setArtifactFQN(element.getFullyQualifiedName());

			if (newValue == null) {
				// For now removal on the diagram shouldn't trigger removal
				// in the model.
				// request.setFeatureValue(null);
			} else {
				request.setFeatureValue(newValue.getFullyQualifiedName());
				postChangeRequest(request);
			}
		} catch (TigerstripeException e) {
			// Ignore
		}
	}

	/**
	 * Try to create the incoming feature if its attributes are correctly
	 * populated
	 * 
	 * @param arg0
	 */
	protected void tryCreate(Notification arg0) {
		if (arg0.getNewValue() instanceof Attribute) {
			Attribute attr = (Attribute) arg0.getNewValue();
			if (attr.getName() != null && attr.getType() != null) {
				AttributeETAdapter.createAttribute(attr.getName(), attr
						.getType(), attr.getTypeMultiplicity().getLiteral(),
						element.getFullyQualifiedName(), getModelUpdater());
			}
		} else if (arg0.getNewValue() instanceof Reference) {
			// When first creating a reference, the zEnd is not set.
			// so this piece of code is not used. However, when undoing a
			// delete of a reference, the ref is re-added with once all its
			// attributes have been set. This piece of code is use then.
			Reference ref = (Reference) arg0.getNewValue();
			if (ref.getName() != null && ref.getZEnd() != null) {
				ReferenceETAdapter.createAttribute(ref.getName(), ref.getZEnd()
						.getFullyQualifiedName(), ref.getTypeMultiplicity()
						.getLiteral(), ((QualifiedNamedElement) arg0
						.getNotifier()).getFullyQualifiedName(),
						getModelUpdater());
			}
		} else if (arg0.getNewValue() instanceof Method) {
			Method meth = (Method) arg0.getNewValue();
			if (meth.getName() != null) {
				MethodETAdapter.createMethod(meth.getName(), meth.getType(),
						meth.getTypeMultiplicity().getLiteral(),
						((QualifiedNamedElement) arg0.getNotifier())
								.getFullyQualifiedName(), getModelUpdater());
			}
		} else if (arg0.getNewValue() instanceof Literal) {
			Literal meth = (Literal) arg0.getNewValue();
			if (meth.getName() != null) {
				LiteralETAdapter.createLiteral(meth.getName(), meth.getValue(),
						meth.getTypeMultiplicity().getLiteral(),
						((QualifiedNamedElement) arg0.getNotifier())
								.getFullyQualifiedName(), getModelUpdater(),
						(AbstractArtifact) arg0.getNotifier());
			}
		}
	}

	protected abstract void handleRemoveNotification(Notification arg0);
}
