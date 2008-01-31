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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.ETAdapter;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class MethodETAdapter extends BaseETAdapter implements ETAdapter {

	private Method method;

	public MethodETAdapter(Method method, IModelUpdater modelUpdater,
			ClassDiagramSynchronizer synchronizer) {
		super(modelUpdater, synchronizer);
		this.method = method;
		initialize();
	}

	protected void initialize() {

		// TigerstripeRuntime.logInfoMessage(" Creating MethodETAdapter for " +
		// method.getName() + " " + this);
		// register self
		method.eAdapters().add(this);
	}

	@Override
	public void notifyChanged(Notification arg0) {
		super.notifyChanged(arg0);

		switch (arg0.getEventType()) {
		case Notification.SET:
			handleSetNotification(arg0);
		}
	}

	protected void handleSetNotification(Notification arg0) {
		if (method.eContainer() == null) {
			Exception e = new Exception("null eContainer found for method "
					+ method.getName());
			IStatus status = new Status(IStatus.WARNING, EclipsePlugin
					.getPluginId(), 222, "null eContainer found for method "
					+ method.getName(), e);
			EclipsePlugin.log(status);
			return;
		}
		// We're getting 2 successive "SET"s for the name and the type of this
		// attribute
		// We can only trigger a request to the Tigerstripe domain when we've
		// got both
		if (arg0.getFeature() instanceof EAttribute) {
			EAttribute attr = (EAttribute) arg0.getFeature();
			if (attr.getName().equals("name")) {
				// name has changed
				if (arg0.getOldStringValue() == null) {
					// it's a create, but can only propagate if the type is set
					// as well
					if (method.getType() != null) {
						// Yes! Create the attribute
						createMethod(arg0.getNewStringValue(),
								method.getType(), "FIXME",
								((AbstractArtifact) method.eContainer())
										.getFullyQualifiedName(),
								getModelUpdater());
					}
				} else if (!arg0.getOldStringValue().equals(
						arg0.getNewStringValue())) {
					try {
						IMethodSetRequest request = (IMethodSetRequest) getModelUpdater()
								.getRequestFactory().makeRequest(
										IModelChangeRequestFactory.METHOD_SET);
						request.setArtifactFQN(((AbstractArtifact) method
								.eContainer()).getFullyQualifiedName());
						request.setFeatureId(IMethodSetRequest.NAME_FEATURE);
						String oldMethodLabel = computeOldLabel(method, arg0
								.getOldStringValue(),
								IMethodSetRequest.NAME_FEATURE);
						request.setMethodLabelBeforeChange(oldMethodLabel);
						request.setOldValue(arg0.getOldStringValue());
						request.setNewValue(arg0.getNewStringValue());

						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("typeMultiplicity")) {
				if (((AssocMultiplicity) arg0.getOldValue()) != ((AssocMultiplicity) arg0
						.getNewValue())) {
					try {
						AssocMultiplicity oldVal = (AssocMultiplicity) arg0
								.getOldValue();
						AssocMultiplicity newVal = (AssocMultiplicity) arg0
								.getNewValue();
						IMethodSetRequest request = (IMethodSetRequest) getModelUpdater()
								.getRequestFactory().makeRequest(
										IModelChangeRequestFactory.METHOD_SET);
						request.setArtifactFQN(((AbstractArtifact) method
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IMethodSetRequest.MULTIPLICITY_FEATURE);
						String oldMethodLabel = computeOldLabel(method, oldVal
								.getLiteral(),
								IMethodSetRequest.MULTIPLICITY_FEATURE);
						request.setMethodLabelBeforeChange(oldMethodLabel);
						request.setOldValue(oldVal.getLiteral());
						request.setNewValue(newVal.getLiteral());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("type")) {
				// type has changed
				if (arg0.getOldStringValue() == null) {
					// it's a create, but can only propagate if the name is set
					// as well
					if (method.getName() != null) {
						// Yes! Create the attribute
						createMethod(method.getName(),
								arg0.getNewStringValue(), "FIXME",
								((AbstractArtifact) method.eContainer())
										.getFullyQualifiedName(),
								getModelUpdater());
					}
				} else if (!arg0.getOldStringValue().equals(
						arg0.getNewStringValue())) {
					try {
						IMethodSetRequest request = (IMethodSetRequest) getModelUpdater()
								.getRequestFactory().makeRequest(
										IModelChangeRequestFactory.METHOD_SET);
						request.setArtifactFQN(((AbstractArtifact) method
								.eContainer()).getFullyQualifiedName());
						request.setFeatureId(IMethodSetRequest.TYPE_FEATURE);
						String oldMethodLabel = computeOldLabel(method, arg0
								.getOldStringValue(),
								IMethodSetRequest.TYPE_FEATURE);
						request.setMethodLabelBeforeChange(oldMethodLabel);
						request.setOldValue(arg0.getOldStringValue());
						request.setNewValue(arg0.getNewStringValue());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("visibility")) {
				if (method.getName() != null) {
					try {
						IMethodSetRequest request = (IMethodSetRequest) getModelUpdater()
								.getRequestFactory().makeRequest(
										IModelChangeRequestFactory.METHOD_SET);
						request.setArtifactFQN(((AbstractArtifact) method
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IMethodSetRequest.VISIBILITY_FEATURE);
						String oldMethodLabel = computeOldLabel(method, arg0
								.getOldStringValue(),
								IMethodSetRequest.VISIBILITY_FEATURE);
						request.setMethodLabelBeforeChange(oldMethodLabel);
						request.setOldValue(arg0.getOldStringValue());
						request.setNewValue(arg0.getNewStringValue());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("isAbstract")) {
				if (method.getName() != null) {
					try {
						IMethodSetRequest request = (IMethodSetRequest) getModelUpdater()
								.getRequestFactory().makeRequest(
										IModelChangeRequestFactory.METHOD_SET);
						request.setArtifactFQN(((AbstractArtifact) method
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IMethodSetRequest.ISABSTRACT_FEATURE);
						String oldMethodLabel = computeOldLabel(method, arg0
								.getOldStringValue(),
								IMethodSetRequest.ISABSTRACT_FEATURE);
						request.setMethodLabelBeforeChange(oldMethodLabel);
						request.setOldValue(Boolean.toString(arg0
								.getOldBooleanValue()));
						request.setNewValue(Boolean.toString(arg0
								.getNewBooleanValue()));
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("defaultValue")) {
				if (method.getName() != null) {
					try {
						IMethodSetRequest request = (IMethodSetRequest) getModelUpdater()
								.getRequestFactory().makeRequest(
										IModelChangeRequestFactory.METHOD_SET);
						request.setArtifactFQN(((AbstractArtifact) method
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IMethodSetRequest.DEFAULTRETURNVALUE_FEATURE);
						String oldMethodLabel = computeOldLabel(method, arg0
								.getOldStringValue(),
								IMethodSetRequest.DEFAULTRETURNVALUE_FEATURE);
						request.setMethodLabelBeforeChange(oldMethodLabel);
						request.setOldValue(arg0.getOldStringValue());
						request.setNewValue(arg0.getNewStringValue());
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("isOrdered")) {
				if (method.getName() != null) {
					try {
						IMethodSetRequest request = (IMethodSetRequest) getModelUpdater()
								.getRequestFactory().makeRequest(
										IModelChangeRequestFactory.METHOD_SET);
						request.setArtifactFQN(((AbstractArtifact) method
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IMethodSetRequest.ISORDERED_FEATURE);
						String oldMethodLabel = computeOldLabel(method, arg0
								.getOldStringValue(),
								IMethodSetRequest.ISORDERED_FEATURE);
						request.setMethodLabelBeforeChange(oldMethodLabel);
						request.setOldValue(String.valueOf(arg0
								.getOldBooleanValue()));
						request.setNewValue(String.valueOf(arg0
								.getNewBooleanValue()));
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			} else if (attr.getName().equals("isUnique")) {
				if (method.getName() != null) {
					try {
						boolean bool = arg0.getNewBooleanValue();
						IMethodSetRequest request = (IMethodSetRequest) getModelUpdater()
								.getRequestFactory().makeRequest(
										IModelChangeRequestFactory.METHOD_SET);
						request.setArtifactFQN(((AbstractArtifact) method
								.eContainer()).getFullyQualifiedName());
						request
								.setFeatureId(IMethodSetRequest.ISUNIQUE_FEATURE);
						request.setMethodLabelBeforeChange(method
								.getLabelString());
						request.setOldValue(String.valueOf(arg0
								.getOldBooleanValue()));
						request.setNewValue(String.valueOf(arg0
								.getNewBooleanValue()));
						postChangeRequest(request);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
	}

	/**
	 * The request to change an attribute in the details of a method needs to be
	 * matched on the old Label of that method (containing the
	 * name,profile,returnType) so this method computes that old label, based on
	 * what change is being reported.
	 * 
	 * The key signifies what change is being reported.
	 * 
	 * @since Bug 997
	 * @param method
	 * @param oldValueString
	 * @param key
	 * @return
	 */
	private String computeOldLabel(Method method, String oldValueString,
			String key) {

		String result = method.getLabelString();

		method.eSetDeliver(false);

		// We only care about the changes that would affect the method label
		if (IMethodSetRequest.NAME_FEATURE.equals(key)) {
			String newName = method.getName();
			method.setName(oldValueString);
			result = method.getLabelString();
			method.setName(newName);
		} else if (IMethodSetRequest.DEFAULTRETURNVALUE_FEATURE.equals(key)) {
			String newValue = method.getDefaultValue();
			method.setDefaultValue(oldValueString);
			result = method.getLabelString();
			method.setDefaultValue(newValue);
		} else if (IMethodSetRequest.MULTIPLICITY_FEATURE.equals(key)) {
			AssocMultiplicity newMult = method.getTypeMultiplicity();
			method.setTypeMultiplicity(AssocMultiplicity.get(oldValueString));
			result = method.getLabelString();
			method.setTypeMultiplicity(newMult);
		} else if (IMethodSetRequest.TYPE_FEATURE.equals(key)) {
			String newValue = method.getType();
			method.setType(oldValueString);
			result = method.getLabelString();
			method.setType(newValue);
		}

		method.eSetDeliver(true);
		return result;
	}

	public static void createMethod(String methodName, String methodType,
			String methodMultiplicity, String parentArtifactFQN,
			IModelUpdater modelUpdater) {
		try {
			// Yes! Create the attribute
			IMethodCreateRequest request = (IMethodCreateRequest) modelUpdater
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.METHOD_CREATE);
			request.setArtifactFQN(parentArtifactFQN);
			request.setMethodName(methodName);
			request.setMethodType(methodType);
			request.setMethodMultiplicity(methodMultiplicity);

			modelUpdater.handleChangeRequest(request);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}

	}
}
