/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.components;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.internal.core.model.ComponentNameProvider;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactMethodsSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ModelComponentSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.StereotypeSectionManager;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.IModifyCallback;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.MethodInfoEditComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.MethodInfoEditComponent.Handler;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class NewMethodPage extends NewModelComponentPage {

	private IMethod newMethod;
	private MethodInfoEditComponent methodInfoEditComponent;
	private StereotypeSectionManager stereotypeMgr;

	protected NewMethodPage(IAbstractArtifact initArtifact) {
		super("NewMethod", initArtifact);
		setTitle("Creating new method for artifact");
		setDescription("Specify method parameters");

		if (getArtifact() != null) {
			newMethod = createMethod(getArtifact());
		}
	}

	private IMethod createMethod(IAbstractArtifact artifact) {
		IMethod newMethod = artifact.makeMethod();

		String newMethodName = ComponentNameProvider.getInstance()
				.getNewMethodName(artifact);

		newMethod.setName(newMethodName);
		newMethod.setVoid(true);
		IType type = newMethod.makeType();
		type.setFullyQualifiedName("void");
		type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		newMethod.setReturnType(type);
		newMethod.setVoid(true);
		newMethod.setVisibility(EVisibility.PUBLIC);
		return newMethod;
	}

	@Override
	protected IModelComponent getModelComponent() {
		return newMethod;
	}

	@Override
	protected void fill(FormToolkit toolkit, Composite parent) {
		Handler handler = new MethodInfoEditComponent.Handler() {

			public void stateModified() {
				revalidate();
			}

			public void refreshMethodLabel() {
			}

			public void navigateToKeyPressed(KeyEvent e) {
			}

			public void commit() {
			}

			public void afterUpdate() {
			}

			public void stereotypeModify() {
			}
		};

		methodInfoEditComponent = new MethodInfoEditComponent(false, true,
				getShell(), toolkit, handler, null);
		methodInfoEditComponent.createContents(parent);
		methodInfoEditComponent.setMethod(newMethod);
		methodInfoEditComponent.update();
		methodInfoEditComponent.getNameText().setFocus();
		methodInfoEditComponent.getNameText().selectAll();
		
		stereotypeMgr = new StereotypeSectionManager(methodInfoEditComponent
				.getAddAnno(), methodInfoEditComponent.getEditAnno(),
				methodInfoEditComponent.getRemoveAnno(),
				methodInfoEditComponent.getAnnTable(),
				(IStereotypeCapable) newMethod, getShell(),
				IModifyCallback.EMPTY);
		stereotypeMgr.delegate();
	}

	@Override
	public void onArtifactChange() {
		if (getArtifact() != null) {
			newMethod = createMethod(getArtifact());
		} else {
			newMethod = null;
		}
		if (methodInfoEditComponent != null) {
			methodInfoEditComponent.setMethod(newMethod);
			stereotypeMgr.setArtifactComponent((IStereotypeCapable) newMethod);
			methodInfoEditComponent.update();
		}
	}

	@Override
	public void beforeSave() {
		getArtifact().addMethod(newMethod);
	}

	@Override
	public Class<? extends ModelComponentSectionPart> getSectionPartClass() {
		return ArtifactMethodsSection.class;
	}
}
