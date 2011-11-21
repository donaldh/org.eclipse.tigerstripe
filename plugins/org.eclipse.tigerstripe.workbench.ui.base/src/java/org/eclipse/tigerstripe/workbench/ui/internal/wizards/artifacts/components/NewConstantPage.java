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
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactConstantsSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ModelComponentSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.StereotypeSectionManager;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ConstantInfoEditComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ConstantInfoEditComponent.Handler;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.IModifyCallback;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class NewConstantPage extends NewModelComponentPage {

	private ILiteral newLiteral;
	private ConstantInfoEditComponent constantInfoEditComponent;
	private StereotypeSectionManager stereotypeMgr;

	protected NewConstantPage(IAbstractArtifact initArtifact) {
		super("NewConstant", initArtifact);

		setTitle("Creating new constant for artifact");
		setDescription("Specify constant parameters");

		if (getArtifact() != null) {
			newLiteral = createLiteral(getArtifact());
		}
	}

	private ILiteral createLiteral(IAbstractArtifact artifact) {
		ILiteral newLiteral = artifact.makeLiteral();

		ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();

		String newLabelName = nameFactory.getNewLiteralName(artifact);
		newLiteral.setName(newLabelName);
		IType defaultType = newLiteral.makeType();
		
		String baseTypeFQN = ArtifactConstantsSection.getForcedBaseType(artifact);
		if (baseTypeFQN == null) {
			baseTypeFQN = "String";
		}
		defaultType.setFullyQualifiedName(baseTypeFQN);

		defaultType.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		newLiteral.setType(defaultType);
		newLiteral.setVisibility(EVisibility.PUBLIC);
		
		newLiteral.setValue(getInitialLiteralValue(newLiteral));
		return newLiteral;
	}
	
	private String getInitialLiteralValue(ILiteral newLiteral) {
		if ("int".equals(newLiteral.getType().getFullyQualifiedName())) {
			return "0";
		} else {
			return String.format("\"%s__VALUE\"", newLiteral.getName());
		}
	}

	@Override
	protected IModelComponent getModelComponent() {
		return newLiteral;
	}

	@Override
	protected void fill(FormToolkit toolkit, Composite parent) {
		Handler handler = new ConstantInfoEditComponent.Handler() {

			public void afterUpdate() {
			}

			public void navigateToKeyPressed(KeyEvent e) {
			}

			public void refresh() {
			}

			public void stateModified() {
				revalidate();
			}
		};

		constantInfoEditComponent = new ConstantInfoEditComponent(false,
				toolkit, handler);
		constantInfoEditComponent.createContents(parent);
		constantInfoEditComponent.setLiteral(newLiteral);
		constantInfoEditComponent.update();
		constantInfoEditComponent.getNameText().setFocus();
		constantInfoEditComponent.getNameText().selectAll();
		
		stereotypeMgr = new StereotypeSectionManager(constantInfoEditComponent
				.getAddAnno(), constantInfoEditComponent.getEditAnno(),
				constantInfoEditComponent.getRemoveAnno(),
				constantInfoEditComponent.getAnnTable(),
				(IStereotypeCapable) newLiteral, getShell(),
				IModifyCallback.EMPTY);
		stereotypeMgr.delegate();
	}

	@Override
	public void onArtifactChange() {
		if (getArtifact() != null) {
			newLiteral = createLiteral(getArtifact());
		} else {
			newLiteral = null;
		}
		if (constantInfoEditComponent != null) {
			constantInfoEditComponent.setLiteral(newLiteral);
			stereotypeMgr.setArtifactComponent((IStereotypeCapable) newLiteral);
			constantInfoEditComponent.update();
		}
	}

	@Override
	public void beforeSave() {
		getArtifact().addLiteral(newLiteral);
	}

	@Override
	public Class<? extends ModelComponentSectionPart> getSectionPartClass() {
		return ArtifactConstantsSection.class;
	}
}
